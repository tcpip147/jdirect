package com.tcpip147.jdirect;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.WCHAR;
import com.tcpip147.jdirect.ffm.coms.ID2D1Factory;
import com.tcpip147.jdirect.ffm.coms.ID2D1GeometrySink;
import com.tcpip147.jdirect.ffm.coms.ID2D1HwndRenderTarget;
import com.tcpip147.jdirect.ffm.coms.ID2D1PathGeometry;
import com.tcpip147.jdirect.ffm.coms.ID2D1SolidColorBrush;
import com.tcpip147.jdirect.ffm.coms.ID2D1StrokeStyle;
import com.tcpip147.jdirect.ffm.coms.IDWriteFactory;
import com.tcpip147.jdirect.ffm.coms.IDWriteTextFormat;
import com.tcpip147.jdirect.ffm.coms.IDWriteTextLayout;
import com.tcpip147.jdirect.ffm.dll.D2d1;
import com.tcpip147.jdirect.ffm.dll.Dwrite;
import com.tcpip147.jdirect.ffm.dll.User32;
import com.tcpip147.jdirect.ffm.enums.D2D1_ANTIALIAS_MODE;
import com.tcpip147.jdirect.ffm.enums.D2D1_CAP_STYLE;
import com.tcpip147.jdirect.ffm.enums.D2D1_DASH_STYLE;
import com.tcpip147.jdirect.ffm.enums.D2D1_DRAW_TEXT_OPTIONS;
import com.tcpip147.jdirect.ffm.enums.D2D1_FACTORY_TYPE;
import com.tcpip147.jdirect.ffm.enums.D2D1_FIGURE_BEGIN;
import com.tcpip147.jdirect.ffm.enums.D2D1_FIGURE_END;
import com.tcpip147.jdirect.ffm.enums.D2D1_LINE_JOIN;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FACTORY_TYPE;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FONT_STRETCH;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FONT_STYLE;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FONT_WEIGHT;
import com.tcpip147.jdirect.ffm.structs.D2D1_COLOR_F;
import com.tcpip147.jdirect.ffm.structs.D2D1_ELLIPSE;
import com.tcpip147.jdirect.ffm.structs.D2D1_MATRIX_3X2_F;
import com.tcpip147.jdirect.ffm.structs.D2D1_POINT_2F;
import com.tcpip147.jdirect.ffm.structs.D2D1_RECT_F;
import com.tcpip147.jdirect.ffm.structs.D2D1_ROUNDED_RECT;
import com.tcpip147.jdirect.ffm.structs.D2D1_SIZE_U;
import com.tcpip147.jdirect.ffm.structs.D2D1_STROKE_STYLE_PROPERTIES;
import com.tcpip147.jdirect.ffm.structs.DWRITE_TEXT_METRICS;
import com.tcpip147.jdirect.ffm.structs.LPRECT;

public class JDGraphics implements AutoCloseable {

	private static final ReentrantLock mutex = new ReentrantLock();
	private static final AtomicInteger refCount = new AtomicInteger(0);
	private static volatile boolean initialized = false;
	private static volatile Arena globalArena;
	private static ID2D1Factory direct2dFactory;
	private static IDWriteFactory directWriteFactory;

	public static JDGraphics create(long handle, long heapSize) {
		mutex.lock();
		try {
			if (!initialized) {
				globalArena = Arena.ofShared();
				direct2dFactory = new ID2D1Factory(globalArena);
				int hr = D2d1.D2D1CreateFactory(D2D1_FACTORY_TYPE.D2D1_FACTORY_TYPE_SINGLE_THREADED, direct2dFactory);
				if (hr != 0) {
					direct2dFactory = null;
					throw new RuntimeException("Failed to create Direct2D factory. HRESULT: 0x%08x".formatted(hr));
				}
				directWriteFactory = new IDWriteFactory(globalArena);
				hr = Dwrite.DWriteCreateFactory(DWRITE_FACTORY_TYPE.DWRITE_FACTORY_TYPE_SHARED, directWriteFactory);
				if (hr != 0) {
					directWriteFactory = null;
					throw new RuntimeException("Failed to create DirectWrite factory. HRESULT: 0x%08x".formatted(hr));
				}
				initialized = true;
			}
			JDGraphics graphics = new JDGraphics(handle, heapSize);
			refCount.incrementAndGet();
			return graphics;
		} catch (Exception e) {
			releaseGraphics(true);
			throw e;
		} finally {
			mutex.unlock();
		}
	}

	private static void releaseGraphics(boolean closed) {
		mutex.lock();
		try {
			int c = closed ? refCount.decrementAndGet() : refCount.get();
			if (c == 0) {
				if (direct2dFactory != null) {
					try {
						direct2dFactory.close();
					} catch (Exception e) {
					}
					direct2dFactory = null;
				}
				if (directWriteFactory != null) {
					try {
						directWriteFactory.close();
					} catch (Exception e) {
					}
					directWriteFactory = null;
				}
				if (globalArena != null) {
					globalArena.close();
					globalArena = null;
				}
				initialized = false;
			}
		} finally {
			mutex.unlock();
		}
	}

	private Arena graphicsArena;
	private MemorySegment hwnd;
	private Arena drawingArena;
	private MemorySegment drawingMemory;
	private ID2D1HwndRenderTarget renderTarget;
	private ID2D1SolidColorBrush colorBrush;

	private final Map<String, D2D1_COLOR_F> colorCache = new HashMap<>();
	private final Map<JDStrokeStyle, ID2D1StrokeStyle> strokeStyleCache = new HashMap<>();
	private final Map<JDFont, IDWriteTextFormat> fontCache = new HashMap<>();
	private final Map<JDText, IDWriteTextLayout> textLayoutCache = new HashMap<>();
	private Map<JDText, Integer> textTTL = new HashMap<>();

	private int drawingMemoryOffset = 0;
	private final float[] translateTransform = new float[] { 0, 0 };
	private final float[] rotateTransform = new float[] { 0, 0, 0 };

	private JDStrokeStyle strokeStyle;
	private JDFont font;
	private int ttlCounter = 0;

	public JDGraphics(long handle, long heapSize) {
		try {
			graphicsArena = Arena.ofShared();
			drawingArena = Arena.ofShared();
			hwnd = MemorySegment.ofAddress(handle);
			drawingMemory = drawingArena.allocate(heapSize);
			try (Arena localArena = Arena.ofConfined()) {
				LPRECT rect = new LPRECT(localArena);
				User32.GetClientRect(hwnd, rect);
				D2D1_SIZE_U size = new D2D1_SIZE_U(localArena);
				size.setWidth(rect.getRight() - rect.getLeft());
				size.setHeight(rect.getBottom() - rect.getTop());
				renderTarget = new ID2D1HwndRenderTarget(graphicsArena);
				int hr = direct2dFactory.CreateHwndRenderTarget(D2d1.RenderTargetProperties(localArena),
						D2d1.HwndRenderTargetProperties(localArena, hwnd, size), renderTarget);
				if (hr != 0) {
					renderTarget = null;
					throw new RuntimeException("Faild to create RenderTarget. HRESULT: 0x%08x".formatted(hr));
				}

				D2D1_COLOR_F color = new D2D1_COLOR_F(localArena);
				color.setR(0);
				color.setG(0);
				color.setB(0);
				color.setA(1);

				colorBrush = new ID2D1SolidColorBrush(graphicsArena);
				hr = renderTarget.CreateSolidColorBrush(color, colorBrush);
				if (hr != 0) {
					colorBrush = null;
					throw new RuntimeException("Faild to create ColorBrush. HRESULT: 0x%08x".formatted(hr));
				}

				font = new JDFont();
			}
		} catch (Exception e) {
			release(false);
			throw e;
		}
	}

	private void release(boolean closed) {
		for (IDWriteTextLayout textLayout : textLayoutCache.values()) {
			try {
				textLayout.close();
			} catch (Exception e) {
			}
		}
		for (ID2D1StrokeStyle strokeStyle : strokeStyleCache.values()) {
			try {
				strokeStyle.close();
			} catch (Exception e) {
			}
		}
		if (colorBrush != null) {
			try {
				colorBrush.close();
			} catch (Exception e) {
			}
			colorBrush = null;
		}
		if (renderTarget != null) {
			try {
				renderTarget.close();
			} catch (Exception e) {
			}
			renderTarget = null;
		}
		if (drawingArena != null) {
			drawingArena.close();
			drawingArena = null;
		}
		if (graphicsArena != null) {
			graphicsArena.close();
			graphicsArena = null;
		}
		textTTL.clear();
		textLayoutCache.clear();
		fontCache.clear();
		strokeStyleCache.clear();
		colorCache.clear();

		if (closed) {
			releaseGraphics(true);
		}
	}

	@Override
	public void close() throws Exception {
		release(true);
	}

	private MemorySegment allocate(long size) {
		MemorySegment memory = drawingMemory.asSlice(drawingMemoryOffset, size);
		drawingMemoryOffset += size;
		return memory;
	}

	private ID2D1StrokeStyle convert(JDStrokeStyle strokeStyle) {
		if (strokeStyle == null) {
			return null;
		}
		ID2D1StrokeStyle cachedStrokeStyle = strokeStyleCache.get(strokeStyle);
		if (cachedStrokeStyle == null) {
			try (Arena localArena = Arena.ofConfined()) {
				D2D1_STROKE_STYLE_PROPERTIES strokeProperties = new D2D1_STROKE_STYLE_PROPERTIES(localArena);
				strokeProperties.setStartCap(NativeUtils.from(D2D1_CAP_STYLE.class, strokeStyle.getStartCap()));
				strokeProperties.setEndCap(NativeUtils.from(D2D1_CAP_STYLE.class, strokeStyle.getEndCap()));
				strokeProperties.setDashCap(NativeUtils.from(D2D1_CAP_STYLE.class, strokeStyle.getDashCap()));
				strokeProperties.setLineJoin(NativeUtils.from(D2D1_LINE_JOIN.class, strokeStyle.getLineJoin()));
				strokeProperties.setMiterLimit(strokeStyle.getMiterLimit());
				strokeProperties.setDashStyle(NativeUtils.from(D2D1_DASH_STYLE.class, strokeStyle.getDashStyle()));
				strokeProperties.setDashOffset(strokeStyle.getDashOffset());

				cachedStrokeStyle = new ID2D1StrokeStyle(graphicsArena);
				int hr = direct2dFactory.CreateStrokeStyle(strokeProperties, cachedStrokeStyle);
				if (hr != 0) {
					throw new RuntimeException("Faild to create DashStrokeStyle. HRESULT: 0x%08x".formatted(hr));
				}
				strokeStyleCache.put(strokeStyle, cachedStrokeStyle);
			}
		}
		return cachedStrokeStyle;
	}

	private IDWriteTextFormat convert(JDFont font) {
		if (font == null) {
			return null;
		}
		IDWriteTextFormat cachedFont = fontCache.get(font);
		if (cachedFont == null) {
			try (Arena localArena = Arena.ofConfined()) {
				WCHAR fontFamilyName = new WCHAR(localArena, font.getName());
				DWRITE_FONT_WEIGHT fontWeight = NativeUtils.from(DWRITE_FONT_WEIGHT.class, font.getWeight());
				DWRITE_FONT_STYLE fontStyle = NativeUtils.from(DWRITE_FONT_STYLE.class, font.getStyle());
				DWRITE_FONT_STRETCH fontStretch = NativeUtils.from(DWRITE_FONT_STRETCH.class, font.getStretch());
				WCHAR localName = new WCHAR(localArena, "ko-KR");

				IDWriteTextFormat textFormat = new IDWriteTextFormat(graphicsArena);
				int hr = directWriteFactory.CreateTextFormat(fontFamilyName, fontWeight, fontStyle, fontStretch,
						font.getSize(), localName, textFormat);
				if (hr != 0) {
					throw new RuntimeException("Faild to create TextFormat. HRESULT: 0x%08x".formatted(hr));
				}
				fontCache.put(font, textFormat);
			}
		}
		return cachedFont;
	}

	public void resize() {
		try (Arena localArena = Arena.ofConfined()) {
			LPRECT rect = new LPRECT(localArena);
			User32.GetClientRect(hwnd, rect);
			D2D1_SIZE_U size = new D2D1_SIZE_U(localArena);
			size.setWidth(rect.getRight() - rect.getLeft());
			size.setHeight(rect.getBottom() - rect.getTop());
			renderTarget.Resize(size);
		}
	}

	public void beginDraw() {
		if (ttlCounter % 10 == 9) {
			textLayoutCache.keySet().forEach(text -> {
				textTTL.put(text, 0);
			});
		}
		drawingMemoryOffset = 0;
		strokeStyle = null;
		translateTransform[0] = 0;
		translateTransform[1] = 0;
		rotateTransform[0] = 0;
		rotateTransform[1] = 0;
		rotateTransform[2] = 0;
		renderTarget.BeginDraw();
	}

	public void endDraw() {
		int hr = renderTarget.EndDraw();
		if (hr != 0) {
			// TODO
			System.out.println(hr);
		}
		if (ttlCounter % 10 == 9) {
			ttlCounter = 0;
			textLayoutCache.entrySet().removeIf(entry -> {
				if (textTTL.get(entry.getKey()) == 0) {
					try {
						entry.getValue().close();
					} catch (Exception e) {
					}
					return true;
				}
				return false;
			});
		}
		ttlCounter++;
	}

	public void clear(String rgba) {
		if (colorCache.get(rgba) == null) {
			D2D1_COLOR_F c = new D2D1_COLOR_F(graphicsArena);
			c.setR(Integer.parseInt(rgba.substring(1, 3), 16) / 255f);
			c.setG(Integer.parseInt(rgba.substring(3, 5), 16) / 255f);
			c.setB(Integer.parseInt(rgba.substring(5, 7), 16) / 255f);
			if (rgba.length() >= 9) {
				c.setA(Integer.parseInt(rgba.substring(7, 9), 16) / 255f);
			} else {
				c.setA(1);
			}
			colorCache.put(rgba, c);
		}
		renderTarget.Clear(colorCache.get(rgba));
	}

	public void setAntialiasMode(boolean on) {
		renderTarget.SetAntialiasMode(on ? D2D1_ANTIALIAS_MODE.D2D1_ANTIALIAS_MODE_PER_PRIMITIVE
				: D2D1_ANTIALIAS_MODE.D2D1_ANTIALIAS_MODE_ALIASED);
	}

	public void setColor(String rgba) {
		if (colorCache.get(rgba) == null) {
			D2D1_COLOR_F c = new D2D1_COLOR_F(graphicsArena);
			c.setR(Integer.parseInt(rgba.substring(1, 3), 16) / 255f);
			c.setG(Integer.parseInt(rgba.substring(3, 5), 16) / 255f);
			c.setB(Integer.parseInt(rgba.substring(5, 7), 16) / 255f);
			if (rgba.length() >= 9) {
				c.setA(Integer.parseInt(rgba.substring(7, 9), 16) / 255f);
			} else {
				c.setA(1);
			}
			colorCache.put(rgba, c);
		}
		colorBrush.SetColor(colorCache.get(rgba));
	}

	private void setTransform() {
		double rad = Math.toRadians(rotateTransform[0]);
		float s = (float) Math.sin(rad);
		float c = (float) Math.cos(rad);

		float m11 = c;
		float m12 = s;
		float m21 = -s;
		float m22 = c;

		float m31 = translateTransform[0] + rotateTransform[1] - (rotateTransform[1] * c - rotateTransform[2] * s);
		float m32 = translateTransform[1] + rotateTransform[2] - (rotateTransform[1] * s + rotateTransform[2] * c);

		D2D1_MATRIX_3X2_F transform = new D2D1_MATRIX_3X2_F(allocate(D2D1_MATRIX_3X2_F.LAYOUT.byteSize()));
		transform.set_11(m11);
		transform.set_12(m12);
		transform.set_21(m21);
		transform.set_22(m22);
		transform.set_31(m31);
		transform.set_32(m32);

		renderTarget.SetTransform(transform);
	}

	public void translate(float x, float y) {
		translateTransform[0] += x;
		translateTransform[1] += y;
		setTransform();
	}

	public void rotate(float degrees, float px, float py) {
		rotateTransform[0] += degrees;
		rotateTransform[1] += px;
		rotateTransform[2] += py;
		setTransform();
	}

	public void resetTransform() {
		translateTransform[0] = 0;
		translateTransform[1] = 0;
		rotateTransform[0] = 0;
		rotateTransform[1] = 0;
		rotateTransform[2] = 0;
		setTransform();
	}

	public void resetTranslate() {
		translateTransform[0] = 0;
		translateTransform[1] = 0;
		setTransform();
	}

	public void resetRotate() {
		rotateTransform[0] = 0;
		rotateTransform[1] = 0;
		rotateTransform[2] = 0;
		setTransform();
	}

	public void setStrokeStyle(JDStrokeStyle strokeStyle) {
		this.strokeStyle = strokeStyle;
	}

	public void setFont(JDFont font) {
		this.font = font;
	}

	public void drawLine(int x0, int y0, int x1, int y1) {
		D2D1_POINT_2F point0 = new D2D1_POINT_2F(allocate(D2D1_POINT_2F.LAYOUT.byteSize()));
		point0.setX(x0);
		point0.setY(y0);
		D2D1_POINT_2F point1 = new D2D1_POINT_2F(allocate(D2D1_POINT_2F.LAYOUT.byteSize()));
		point1.setX(x1);
		point1.setY(y1);
		renderTarget.DrawLine(point0, point1, colorBrush, strokeStyle == null ? 1f : strokeStyle.getWidth(),
				convert(strokeStyle));
	}

	public void drawRect(int x, int y, int width, int height) {
		D2D1_RECT_F rect = new D2D1_RECT_F(allocate(D2D1_RECT_F.LAYOUT.byteSize()));
		rect.setLeft(x);
		rect.setTop(y);
		rect.setRight(x + width);
		rect.setBottom(y + height);
		renderTarget.DrawRectangle(rect, colorBrush, strokeStyle == null ? 1f : strokeStyle.getWidth(),
				convert(strokeStyle));
	}

	public void fillRect(int x, int y, int width, int height) {
		D2D1_RECT_F rect = new D2D1_RECT_F(allocate(D2D1_RECT_F.LAYOUT.byteSize()));
		rect.setLeft(x);
		rect.setTop(y);
		rect.setRight(x + width);
		rect.setBottom(y + height);
		renderTarget.FillRectangle(rect, colorBrush);
	}

	public void drawRoundedRect(int x, int y, int width, int height, int rx, int ry) {
		D2D1_RECT_F rect = new D2D1_RECT_F(allocate(D2D1_RECT_F.LAYOUT.byteSize()));
		rect.setLeft(x);
		rect.setTop(y);
		rect.setRight(x + width);
		rect.setBottom(y + height);
		D2D1_ROUNDED_RECT roundedRect = new D2D1_ROUNDED_RECT(allocate(D2D1_ROUNDED_RECT.LAYOUT.byteSize()));
		roundedRect.setRect(rect);
		roundedRect.setRadiusX(rx);
		roundedRect.setRadiusY(ry);
		renderTarget.DrawRoundedRectangle(roundedRect, colorBrush, strokeStyle == null ? 1f : strokeStyle.getWidth(),
				convert(strokeStyle));
	}

	public void fillRoundedRect(int x, int y, int width, int height, int rx, int ry) {
		D2D1_RECT_F rect = new D2D1_RECT_F(allocate(D2D1_RECT_F.LAYOUT.byteSize()));
		rect.setLeft(x);
		rect.setTop(y);
		rect.setRight(x + width);
		rect.setBottom(y + height);
		D2D1_ROUNDED_RECT roundedRect = new D2D1_ROUNDED_RECT(allocate(D2D1_ROUNDED_RECT.LAYOUT.byteSize()));
		roundedRect.setRect(rect);
		roundedRect.setRadiusX(rx);
		roundedRect.setRadiusY(ry);
		renderTarget.FillRoundedRectangle(roundedRect, colorBrush);
	}

	public void drawCircle(int x, int y, int radius) {
		D2D1_POINT_2F point = new D2D1_POINT_2F(allocate(D2D1_POINT_2F.LAYOUT.byteSize()));
		point.setX(x);
		point.setY(y);
		D2D1_ELLIPSE ellipse = new D2D1_ELLIPSE(allocate(D2D1_ELLIPSE.LAYOUT.byteSize()));
		ellipse.setPoint(point);
		ellipse.setRadiusX(radius);
		ellipse.setRadiusY(radius);
		renderTarget.FillEllipse(ellipse, colorBrush);
	}

	public void drawPath(int... points) {
		ID2D1PathGeometry pathGeometry = null;
		ID2D1GeometrySink sink = null;
		try (Arena localArena = Arena.ofConfined()) {
			pathGeometry = new ID2D1PathGeometry(localArena);
			int hr = direct2dFactory.CreatePathGeometry(pathGeometry);
			if (hr != 0) {
				pathGeometry = null;
				throw new RuntimeException("Faild to create PathGeometry. HRESULT: 0x%08x".formatted(hr));
			}

			sink = new ID2D1GeometrySink(localArena);
			hr = pathGeometry.Open(sink);
			if (hr != 0) {
				throw new RuntimeException("Faild to create Sink. HRESULT: 0x%08x".formatted(hr));
			}

			for (int i = 0; i < points.length; i += 2) {
				D2D1_POINT_2F point = new D2D1_POINT_2F(allocate(D2D1_POINT_2F.LAYOUT.byteSize()));
				point.setX(points[i]);
				point.setY(points[i + 1]);
				if (i == 0) {
					sink.BeginFigure(point, D2D1_FIGURE_BEGIN.D2D1_FIGURE_BEGIN_HOLLOW);
				} else {
					sink.AddLine(point);
				}
			}

			sink.EndFigure(D2D1_FIGURE_END.D2D1_FIGURE_END_CLOSED);
			sink.Close();

			renderTarget.DrawGeometry(pathGeometry, colorBrush, strokeStyle == null ? 1f : strokeStyle.getWidth(),
					convert(strokeStyle));
		} finally {
			if (sink != null) {
				try {
					sink.close();
				} catch (Exception e) {
				}
			}
			if (pathGeometry != null) {
				try {
					pathGeometry.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void fillPath(int... points) {
		ID2D1PathGeometry pathGeometry = null;
		ID2D1GeometrySink sink = null;
		try (Arena localArena = Arena.ofConfined()) {
			pathGeometry = new ID2D1PathGeometry(localArena);
			int hr = direct2dFactory.CreatePathGeometry(pathGeometry);
			if (hr != 0) {
				pathGeometry = null;
				throw new RuntimeException("Faild to create PathGeometry. HRESULT: 0x%08x".formatted(hr));
			}

			sink = new ID2D1GeometrySink(localArena);
			hr = pathGeometry.Open(sink);
			if (hr != 0) {
				throw new RuntimeException("Faild to create Sink. HRESULT: 0x%08x".formatted(hr));
			}

			for (int i = 0; i < points.length; i += 2) {
				D2D1_POINT_2F point = new D2D1_POINT_2F(allocate(D2D1_POINT_2F.LAYOUT.byteSize()));
				point.setX(points[i]);
				point.setY(points[i + 1]);
				if (i == 0) {
					sink.BeginFigure(point, D2D1_FIGURE_BEGIN.D2D1_FIGURE_BEGIN_FILLED);
				} else {
					sink.AddLine(point);
				}
			}

			sink.EndFigure(D2D1_FIGURE_END.D2D1_FIGURE_END_CLOSED);
			sink.Close();

			renderTarget.FillGeometry(pathGeometry, colorBrush);
		} finally {
			if (sink != null) {
				try {
					sink.close();
				} catch (Exception e) {
				}
			}
			if (pathGeometry != null) {
				try {
					pathGeometry.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void drawText(int x, int y, String text) {
		JDText value = new JDText(text, font);
		IDWriteTextLayout cachedTextLayout = textLayoutCache.get(value);
		if (cachedTextLayout == null) {
			IDWriteTextLayout textLayout = null;
			try (Arena localArena = Arena.ofConfined()) {
				WCHAR string = new WCHAR(localArena, text);
				textLayout = new IDWriteTextLayout(graphicsArena);
				int hr = directWriteFactory.CreateTextLayout(string, string.size, convert(font), Float.MAX_VALUE,
						Float.MAX_VALUE, textLayout);
				if (hr != 0) {
					throw new RuntimeException("Faild to create TextLayout. HRESULT: 0x%08x".formatted(hr));
				}

				DWRITE_TEXT_METRICS metrics = new DWRITE_TEXT_METRICS(localArena);
				textLayout.GetMetrics(metrics);
				textLayout.SetMaxWidth(metrics.getWidth());
				textLayout.SetMaxHeight(metrics.getHeight());

				textLayoutCache.put(value, textLayout);
				textTTL.put(value, 0);
			}
		}

		D2D1_POINT_2F origin = new D2D1_POINT_2F(allocate(D2D1_POINT_2F.LAYOUT.byteSize()));
		origin.setX(x);
		origin.setY(y);

		textTTL.put(value, 1);
		renderTarget.DrawTextLayout(origin, textLayoutCache.get(value), colorBrush,
				D2D1_DRAW_TEXT_OPTIONS.D2D1_DRAW_TEXT_OPTIONS_NONE);
	}
}
