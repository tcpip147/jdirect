package com.tcpip147.jdirect;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.HashMap;
import java.util.Map;

import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.WCHAR;
import com.tcpip147.jdirect.ffm.coms.ID2D1HwndRenderTarget;
import com.tcpip147.jdirect.ffm.coms.ID2D1SolidColorBrush;
import com.tcpip147.jdirect.ffm.coms.IDWriteTextFormat;
import com.tcpip147.jdirect.ffm.coms.IDWriteTextLayout;
import com.tcpip147.jdirect.ffm.dll.D2d1;
import com.tcpip147.jdirect.ffm.dll.User32;
import com.tcpip147.jdirect.ffm.enums.D2D1_ANTIALIAS_MODE;
import com.tcpip147.jdirect.ffm.enums.D2D1_DRAW_TEXT_OPTIONS;
import com.tcpip147.jdirect.ffm.enums.DWRITE_PARAGRAPH_ALIGNMENT;
import com.tcpip147.jdirect.ffm.enums.DWRITE_TEXT_ALIGNMENT;
import com.tcpip147.jdirect.ffm.structs.D2D1_COLOR_F;
import com.tcpip147.jdirect.ffm.structs.D2D1_ELLIPSE;
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
	private final Map<Integer, IDWriteTextFormat> fontCache = new HashMap<>();
	private final Map<Integer, D2D1_COLOR_F> colorCache = new HashMap<>();
	private final Map<Integer, IDWriteTextLayout> textCache = new HashMap<>();

	protected JDGraphics(long handle, JDirect direct, long memorySize) {
		this.memorySize = memorySize;
		graphicsArena = Arena.ofConfined();
		hwnd = MemorySegment.ofAddress(handle);
		this.direct = direct;
		init();
		drawingArena = Arena.ofConfined();
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

			renderTarget.CreateSolidColorBrush(color, colorBrush);

			if (hr != 0) {
				throw new RuntimeException("Faild to create ColorBrush. HRESULT: 0x%08x".formatted(hr));
			}
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

	public void setAntialiasMode(boolean on) {
		renderTarget.SetAntialiasMode(on ? D2D1_ANTIALIAS_MODE.D2D1_ANTIALIAS_MODE_PER_PRIMITIVE
				: D2D1_ANTIALIAS_MODE.D2D1_ANTIALIAS_MODE_ALIASED);
	}

	@Override
	public void close() {
		drawingArena.close();
		try {
			renderTarget.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try {
			colorBrush.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (IDWriteTextLayout layout : textCache.values()) {
			try {
				layout.close();
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
		try (Arena localArena = Arena.ofConfined()) {
			WCHAR string = new WCHAR(localArena, text.text);
			IDWriteTextLayout textLayout = new IDWriteTextLayout(graphicsArena);
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
		}
	}
}
