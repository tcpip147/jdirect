package com.tcpip147.jdirect;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.HashMap;
import java.util.Map;

import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.WCHAR;
import com.tcpip147.jdirect.ffm.coms.ID2D1GeometrySink;
import com.tcpip147.jdirect.ffm.coms.ID2D1HwndRenderTarget;
import com.tcpip147.jdirect.ffm.coms.ID2D1PathGeometry;
import com.tcpip147.jdirect.ffm.coms.ID2D1SolidColorBrush;
import com.tcpip147.jdirect.ffm.coms.IDWriteTextFormat;
import com.tcpip147.jdirect.ffm.coms.IDWriteTextLayout;
import com.tcpip147.jdirect.ffm.dll.D2d1;
import com.tcpip147.jdirect.ffm.dll.User32;
import com.tcpip147.jdirect.ffm.enums.D2D1_ANTIALIAS_MODE;
import com.tcpip147.jdirect.ffm.enums.D2D1_DRAW_TEXT_OPTIONS;
import com.tcpip147.jdirect.ffm.enums.D2D1_FIGURE_BEGIN;
import com.tcpip147.jdirect.ffm.enums.D2D1_FIGURE_END;
import com.tcpip147.jdirect.ffm.enums.DWRITE_PARAGRAPH_ALIGNMENT;
import com.tcpip147.jdirect.ffm.enums.DWRITE_TEXT_ALIGNMENT;
import com.tcpip147.jdirect.ffm.structs.D2D1_COLOR_F;
import com.tcpip147.jdirect.ffm.structs.D2D1_ELLIPSE;
import com.tcpip147.jdirect.ffm.structs.D2D1_MATRIX_3X2_F;
import com.tcpip147.jdirect.ffm.structs.D2D1_POINT_2F;
import com.tcpip147.jdirect.ffm.structs.D2D1_RECT_F;
import com.tcpip147.jdirect.ffm.structs.D2D1_ROUNDED_RECT;
import com.tcpip147.jdirect.ffm.structs.D2D1_SIZE_U;
import com.tcpip147.jdirect.ffm.structs.LPRECT;

public class JDGraphics implements AutoCloseable {

	private final Arena graphicsArena;
	private final MemorySegment hwnd;
	private final JDirect direct;
	private ID2D1HwndRenderTarget renderTarget;
	private ID2D1SolidColorBrush colorBrush;

	private final long memorySize;
	private Arena drawingArena;
	private MemorySegment drawingMemory;
	private int drawingMemoryOffset;
	private final Map<Integer, D2D1_COLOR_F> colorCache = new HashMap<>();
	private final Map<Integer, IDWriteTextFormat> fontCache = new HashMap<>();
	private final Map<Integer, IDWriteTextLayout> textCache = new HashMap<>();
	private final Map<Integer, ID2D1PathGeometry> pathCache = new HashMap<>();

	private final float[] translateTransform = new float[] { 0, 0 };
	private final float[] rotateTransform = new float[] { 0, 0, 0 };

	protected JDGraphics(long handle, JDirect direct, long memorySize) {
		this.memorySize = memorySize;
		graphicsArena = Arena.ofConfined();
		hwnd = MemorySegment.ofAddress(handle);
		drawingArena = Arena.ofConfined();
		this.direct = direct;
		init();
	}

	private void init() {
		try (Arena localArena = Arena.ofConfined()) {
			LPRECT rect = new LPRECT(localArena);
			User32.GetClientRect(hwnd, rect);
			D2D1_SIZE_U size = new D2D1_SIZE_U(localArena);
			size.setWidth(rect.getRight() - rect.getLeft());
			size.setHeight(rect.getBottom() - rect.getTop());
			renderTarget = new ID2D1HwndRenderTarget(graphicsArena);
			int hr = direct.direct2dFactory.CreateHwndRenderTarget(D2d1.RenderTargetProperties(localArena),
					D2d1.HwndRenderTargetProperties(localArena, hwnd, size), renderTarget);
			if (hr != 0) {
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
				throw new RuntimeException("Faild to create ColorBrush. HRESULT: 0x%08x".formatted(hr));
			}
		} catch (Exception e) {
			close();
			throw new RuntimeException("Failed to Initialize");
		}
	}

	private MemorySegment allocate(long size) {
		MemorySegment mem = drawingMemory.asSlice(drawingMemoryOffset, size);
		drawingMemoryOffset += size;
		return mem;
	}

	public void beginDraw() {
		drawingMemory = drawingArena.allocate(memorySize);
		drawingMemoryOffset = 0;
		renderTarget.BeginDraw();
	}

	public void clear(JDColor color) {
		if (color.arena == JDColor.ARENA_GLOBAL) {
			renderTarget.Clear(direct.colorCache.get(color.resourceIndex));
		} else if (color.arena == JDColor.ARENA_GRAPHICS) {
			renderTarget.Clear(colorCache.get(color.resourceIndex));
		} else {
			clear(color.r, color.g, color.b, color.a);
		}
	}

	public void clear(float r, float g, float b, float a) {
		D2D1_COLOR_F c = new D2D1_COLOR_F(allocate(D2D1_COLOR_F.LAYOUT.byteSize()));
		c.setR(r);
		c.setG(g);
		c.setB(b);
		c.setA(a);
		renderTarget.Clear(c);
	}

	public void setColor(JDColor color) {
		if (color.arena == JDColor.ARENA_GLOBAL) {
			colorBrush.setColor(direct.colorCache.get(color.resourceIndex));
		} else if (color.arena == JDColor.ARENA_GRAPHICS) {
			colorBrush.setColor(colorCache.get(color.resourceIndex));
		} else {
			setColor(color.r, color.g, color.b, color.a);
		}
	}

	public void setColor(float r, float g, float b, float a) {
		D2D1_COLOR_F c = new D2D1_COLOR_F(allocate(D2D1_COLOR_F.LAYOUT.byteSize()));
		c.setR(r);
		c.setG(g);
		c.setB(b);
		c.setA(a);
		colorBrush.setColor(c);
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

	public void endDraw() {
		int hr = renderTarget.EndDraw();
		if (hr != 0) {
			System.out.println(hr);
		}
	}

	public void drawLine(float x0, float y0, float x1, float y1) {
		D2D1_POINT_2F point0 = new D2D1_POINT_2F(allocate(D2D1_POINT_2F.LAYOUT.byteSize()));
		point0.setX(x0);
		point0.setY(y0);
		D2D1_POINT_2F point1 = new D2D1_POINT_2F(allocate(D2D1_POINT_2F.LAYOUT.byteSize()));
		point1.setX(x1);
		point1.setY(y1);
		renderTarget.DrawLine(point0, point1, colorBrush, 1f);
	}

	public void drawRectangle(float x, float y, float width, float height) {
		D2D1_RECT_F rect = new D2D1_RECT_F(allocate(D2D1_RECT_F.LAYOUT.byteSize()));
		rect.setLeft(x);
		rect.setTop(y);
		rect.setRight(x + width);
		rect.setBottom(y + height);
		renderTarget.DrawRectangle(rect, colorBrush, 1f);
	}

	public void fillRectangle(float x, float y, float width, float height) {
		D2D1_RECT_F rect = new D2D1_RECT_F(allocate(D2D1_RECT_F.LAYOUT.byteSize()));
		rect.setLeft(x);
		rect.setTop(y);
		rect.setRight(x + width);
		rect.setBottom(y + height);
		renderTarget.FillRectangle(rect, colorBrush);
	}

	public void drawRoundedRectangle(float x, float y, float width, float height, float radiusX, float radiusY) {
		D2D1_RECT_F rect = new D2D1_RECT_F(allocate(D2D1_RECT_F.LAYOUT.byteSize()));
		rect.setLeft(x);
		rect.setTop(y);
		rect.setRight(x + width);
		rect.setBottom(y + height);
		D2D1_ROUNDED_RECT roundedRect = new D2D1_ROUNDED_RECT(allocate(D2D1_ROUNDED_RECT.LAYOUT.byteSize()));
		roundedRect.setRect(rect);
		roundedRect.setRadiusX(radiusX);
		roundedRect.setRadiusY(radiusY);
		renderTarget.DrawRoundedRectangle(roundedRect, colorBrush, 1f);
	}

	public void fillRoundedRectangle(float x, float y, float width, float height, float radiusX, float radiusY) {
		D2D1_RECT_F rect = new D2D1_RECT_F(allocate(D2D1_RECT_F.LAYOUT.byteSize()));
		rect.setLeft(x);
		rect.setTop(y);
		rect.setRight(x + width);
		rect.setBottom(y + height);
		D2D1_ROUNDED_RECT roundedRect = new D2D1_ROUNDED_RECT(allocate(D2D1_ROUNDED_RECT.LAYOUT.byteSize()));
		roundedRect.setRect(rect);
		roundedRect.setRadiusX(radiusX);
		roundedRect.setRadiusY(radiusY);
		renderTarget.FillRoundedRectangle(roundedRect, colorBrush);
	}

	public void drawCircle(float x, float y, float radiusX, float radiusY) {
		D2D1_POINT_2F point = new D2D1_POINT_2F(allocate(D2D1_POINT_2F.LAYOUT.byteSize()));
		point.setX(x);
		point.setY(y);
		D2D1_ELLIPSE ellipse = new D2D1_ELLIPSE(allocate(D2D1_ELLIPSE.LAYOUT.byteSize()));
		ellipse.setPoint(point);
		ellipse.setRadiusX(radiusX);
		ellipse.setRadiusY(radiusY);
		renderTarget.DrawEllipse(ellipse, colorBrush, 1f);
	}

	public void fillCircle(float x, float y, float radiusX, float radiusY) {
		D2D1_POINT_2F point = new D2D1_POINT_2F(allocate(D2D1_POINT_2F.LAYOUT.byteSize()));
		point.setX(x);
		point.setY(y);
		D2D1_ELLIPSE ellipse = new D2D1_ELLIPSE(allocate(D2D1_ELLIPSE.LAYOUT.byteSize()));
		ellipse.setPoint(point);
		ellipse.setRadiusX(radiusX);
		ellipse.setRadiusY(radiusY);
		renderTarget.FillEllipse(ellipse, colorBrush);
	}

	public void drawText(float x, float y, JDText text) {
		IDWriteTextLayout textLayout;
		if (text.arena == JDText.ARENA_GLOBAL) {
			textLayout = direct.textCache.get(text.resourceIndex);
		} else if (text.arena == JDText.ARENA_GRAPHICS) {
			textLayout = textCache.get(text.resourceIndex);
		} else {
			throw new RuntimeException("Invalid Text");
		}
		D2D1_POINT_2F origin = new D2D1_POINT_2F(allocate(D2D1_POINT_2F.LAYOUT.byteSize()));
		origin.setX(x);
		origin.setY(y);

		renderTarget.DrawTextLayout(origin, textLayout, colorBrush, D2D1_DRAW_TEXT_OPTIONS.D2D1_DRAW_TEXT_OPTIONS_NONE);
	}

	public void fillPath(JDPath path) {
		ID2D1PathGeometry pathGeometry = pathCache.get(path.resourceIndex);
		renderTarget.FillGeometry(pathGeometry, colorBrush);
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

	public void setAntialiasMode(boolean on) {
		renderTarget.SetAntialiasMode(on ? D2D1_ANTIALIAS_MODE.D2D1_ANTIALIAS_MODE_PER_PRIMITIVE
				: D2D1_ANTIALIAS_MODE.D2D1_ANTIALIAS_MODE_ALIASED);
	}

	@Override
	public void close() {
		drawingArena.close();
		if (renderTarget != null) {
			try {
				renderTarget.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		if (colorBrush != null) {
			try {
				colorBrush.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		for (IDWriteTextFormat font : fontCache.values()) {
			try {
				font.close();
			} catch (Exception e) {
			}
		}
		for (IDWriteTextLayout layout : textCache.values()) {
			try {
				layout.close();
			} catch (Exception e) {
			}
		}
		for (ID2D1PathGeometry path : pathCache.values()) {
			try {
				path.close();
			} catch (Exception e) {
			}
		}
		graphicsArena.close();
	}

	public void registryFont(JDFont font) {
		font.arena = JDFont.ARENA_GRAPHICS;
		IDWriteTextFormat f = JDirect.INSTANCE._registryFont(font, graphicsArena);
		fontCache.put(font.resourceIndex, f);
	}

	public void registryColor(JDColor color) {
		color.arena = JDFont.ARENA_GRAPHICS;
		D2D1_COLOR_F c = JDirect.INSTANCE._registryColor(color, graphicsArena);
		colorCache.put(color.resourceIndex, c);
	}

	public void registryText(JDText text) {
		text.arena = JDFont.ARENA_GRAPHICS;
		IDWriteTextLayout textLayout = null;
		try (Arena localArena = Arena.ofConfined()) {
			WCHAR string = new WCHAR(localArena, text.text);
			textLayout = new IDWriteTextLayout(graphicsArena);
			IDWriteTextFormat textFormat;
			if (text.font.arena == JDFont.ARENA_GLOBAL) {
				textFormat = direct.fontCache.get(text.font.resourceIndex);
			} else if (text.font.arena == JDFont.ARENA_GRAPHICS) {
				textFormat = fontCache.get(text.font.resourceIndex);
			} else {
				throw new RuntimeException("Invalid Font");
			}
			int hr = direct.directWriteFactory.CreateTextLayout(string, string.size, textFormat, text.width,
					text.height, textLayout);
			if (hr != 0) {
				throw new RuntimeException("Faild to create TextLayout. HRESULT: 0x%08x".formatted(hr));
			}
			textLayout.SetTextAlignment(NativeUtils.from(DWRITE_TEXT_ALIGNMENT.class, text.horizontalAlign));
			textLayout.SetParagraphAlignment(NativeUtils.from(DWRITE_PARAGRAPH_ALIGNMENT.class, text.verticalAlign));
			textCache.put(text.resourceIndex, textLayout);
		} catch (Exception e) {
			if (textLayout != null) {
				try {
					textLayout.close();
				} catch (Exception e1) {
				}
			}
			throw new RuntimeException("Faild to create TextLayout.");
		}
	}

	public void registryPath(JDPath path) {
		path.arena = JDFont.ARENA_GRAPHICS;
		ID2D1PathGeometry pathGeometry = null;
		ID2D1GeometrySink sink = null;
		try (Arena localArena = Arena.ofConfined()) {
			pathGeometry = new ID2D1PathGeometry(graphicsArena);
			int hr = direct.direct2dFactory.CreatePathGeometry(pathGeometry);
			if (hr != 0) {
				throw new RuntimeException("Faild to create PathGeometry. HRESULT: 0x%08x".formatted(hr));
			}

			sink = new ID2D1GeometrySink(localArena);
			pathGeometry.Open(sink);

			D2D1_POINT_2F startPoint = new D2D1_POINT_2F(localArena);
			startPoint.setX(path.path[0]);
			startPoint.setY(path.path[1]);
			sink.BeginFigure(startPoint, D2D1_FIGURE_BEGIN.D2D1_FIGURE_BEGIN_FILLED);

			for (int i = 2; i < path.path.length; i += 2) {
				D2D1_POINT_2F point = new D2D1_POINT_2F(localArena);
				point.setX(path.path[i]);
				point.setY(path.path[i + 1]);
				sink.AddLine(point);
			}

			sink.EndFigure(D2D1_FIGURE_END.D2D1_FIGURE_END_CLOSED);
			sink.Close();
			sink.close();

			pathCache.put(path.resourceIndex, pathGeometry);
		} catch (Exception e) {
			if (pathGeometry != null) {
				try {
					pathGeometry.close();
				} catch (Exception e1) {
				}
			}
			if (sink != null) {
				try {
					sink.close();
				} catch (Exception e1) {
				}
			}
			throw new RuntimeException(e);
		}
	}
}
