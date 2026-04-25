package com.tcpip147.jdirect;

import java.lang.foreign.Arena;
import java.util.HashMap;
import java.util.Map;

import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.WCHAR;
import com.tcpip147.jdirect.ffm.coms.ID2D1Factory;
import com.tcpip147.jdirect.ffm.coms.IDWriteFactory;
import com.tcpip147.jdirect.ffm.coms.IDWriteTextFormat;
import com.tcpip147.jdirect.ffm.coms.IDWriteTextLayout;
import com.tcpip147.jdirect.ffm.dll.D2d1;
import com.tcpip147.jdirect.ffm.dll.Dwrite;
import com.tcpip147.jdirect.ffm.enums.D2D1_FACTORY_TYPE;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FACTORY_TYPE;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FONT_STRETCH;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FONT_STYLE;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FONT_WEIGHT;
import com.tcpip147.jdirect.ffm.structs.D2D1_COLOR_F;

public class JDirect {

	protected static JDirect INSTANCE;

	public static JDGraphics createGraphics(long handle, long memorySize) {
		return new JDGraphics(handle, INSTANCE, memorySize);
	}

	public static void init() {
		INSTANCE = new JDirect();
	}

	public static void close() {
		INSTANCE._close();
	}

	public static void registryFont(JDFont font) {
		font.arena = JDFont.ARENA_GLOBAL;
		IDWriteTextFormat f = INSTANCE._registryFont(font, INSTANCE.globalArena);
		INSTANCE.fontCache.put(font.resourceIndex, f);
	}

	public static void registryColor(JDColor color) {
		color.arena = JDFont.ARENA_GLOBAL;
		D2D1_COLOR_F c = INSTANCE._registryColor(color, INSTANCE.globalArena);
		INSTANCE.colorCache.put(color.resourceIndex, c);
	}

	public static void registryText(JDText text) {
		text.arena = JDFont.ARENA_GLOBAL;
		IDWriteTextLayout t = INSTANCE._registryText(text, INSTANCE.globalArena);
		INSTANCE.textCache.put(text.resourceIndex, t);
	}

	public final Arena globalArena;
	public final ID2D1Factory direct2dFactory;
	public final IDWriteFactory directWriteFactory;
	public final Map<Integer, IDWriteTextFormat> fontCache = new HashMap<>();
	public final Map<Integer, D2D1_COLOR_F> colorCache = new HashMap<>();
	public final Map<Integer, IDWriteTextLayout> textCache = new HashMap<>();

	public JDirect() {
		globalArena = Arena.ofShared();
		direct2dFactory = new ID2D1Factory(globalArena);
		int hr = D2d1.D2D1CreateFactory(D2D1_FACTORY_TYPE.D2D1_FACTORY_TYPE_SINGLE_THREADED, direct2dFactory);
		if (hr != 0) {
			throw new RuntimeException("Faild to create Direct2D factory. HRESULT: 0x%08x".formatted(hr));
		}

		directWriteFactory = new IDWriteFactory(globalArena);
		hr = Dwrite.DWriteCreateFactory(DWRITE_FACTORY_TYPE.DWRITE_FACTORY_TYPE_SHARED, directWriteFactory);
		if (hr != 0) {
			throw new RuntimeException("Faild to create DirectWrite factory. HRESULT: 0x%08x".formatted(hr));
		}
	}

	private void _close() {
		for (IDWriteTextLayout layout : textCache.values()) {
			try {
				layout.close();
			} catch (Exception e) {
			}
		}
		globalArena.close();
	}

	protected IDWriteTextFormat _registryFont(JDFont font, Arena arena) {
		try (Arena localArena = Arena.ofConfined()) {
			WCHAR fontFamilyName = new WCHAR(localArena, font.name);
			DWRITE_FONT_WEIGHT fontWeight = NativeUtils.from(DWRITE_FONT_WEIGHT.class, font.weight);
			DWRITE_FONT_STYLE fontStyle = NativeUtils.from(DWRITE_FONT_STYLE.class, font.style);
			DWRITE_FONT_STRETCH fontStretch = NativeUtils.from(DWRITE_FONT_STRETCH.class, font.stretch);
			IDWriteTextFormat textFormat = new IDWriteTextFormat(arena);
			WCHAR localName = new WCHAR(localArena, "ko-KR");
			int hr = directWriteFactory.CreateTextFormat(fontFamilyName, fontWeight, fontStyle, fontStretch, font.size,
					localName, textFormat);
			if (hr != 0) {
				throw new RuntimeException("Faild to create TextFormat. HRESULT: 0x%08x".formatted(hr));
			}
			return textFormat;
		}
	}

	protected D2D1_COLOR_F _registryColor(JDColor color, Arena arena) {
		D2D1_COLOR_F c = new D2D1_COLOR_F(arena);
		c.setR(color.r);
		c.setG(color.g);
		c.setB(color.b);
		c.setA(color.a);
		return c;
	}

	protected IDWriteTextLayout _registryText(JDText text, Arena arena) {
		if (text.font.arena == JDFont.ARENA_GLOBAL) {
			try (Arena localArena = Arena.ofConfined()) {
				WCHAR string = new WCHAR(localArena, text.text);
				IDWriteTextLayout textLayout = new IDWriteTextLayout(arena);
				int hr = directWriteFactory.CreateTextLayout(string, string.size,
						fontCache.get(text.font.resourceIndex), text.width, text.height, textLayout);
				if (hr != 0) {
					throw new RuntimeException("Faild to create TextLayout. HRESULT: 0x%08x".formatted(hr));
				}
				return textLayout;
			}
		} else {
			throw new RuntimeException("Invalid Font");
		}
	}
}
