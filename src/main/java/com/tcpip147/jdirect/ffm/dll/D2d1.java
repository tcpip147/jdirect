package com.tcpip147.jdirect.ffm.dll;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;

import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.coms.ID2D1Factory;
import com.tcpip147.jdirect.ffm.enums.D2D1_ALPHA_MODE;
import com.tcpip147.jdirect.ffm.enums.D2D1_FACTORY_TYPE;
import com.tcpip147.jdirect.ffm.enums.D2D1_FEATURE_LEVEL;
import com.tcpip147.jdirect.ffm.enums.D2D1_PRESENT_OPTIONS;
import com.tcpip147.jdirect.ffm.enums.D2D1_RENDER_TARGET_TYPE;
import com.tcpip147.jdirect.ffm.enums.D2D1_RENDER_TARGET_USAGE;
import com.tcpip147.jdirect.ffm.enums.DXGI_FORMAT;
import com.tcpip147.jdirect.ffm.structs.D2D1_HWND_RENDER_TARGET_PROPERTIES;
import com.tcpip147.jdirect.ffm.structs.D2D1_PIXEL_FORMAT;
import com.tcpip147.jdirect.ffm.structs.D2D1_RENDER_TARGET_PROPERTIES;
import com.tcpip147.jdirect.ffm.structs.D2D1_SIZE_U;

//@formatter:off
public class D2d1 {
	
	private static final SymbolLookup DLL = SymbolLookup.libraryLookup("d2d1", Arena.global());

	// https://learn.microsoft.com/en-us/windows/win32/api/d2d1/nf-d2d1-d2d1createfactory
	private static MethodHandle D2D1CreateFactory;
	static {
		FunctionDescriptor descriptor = FunctionDescriptor.of(
			JAVA_INT, // 
			JAVA_INT, // [in]           D2D1_FACTORY_TYPE          factoryType,
			ADDRESS,  // [in]           REFIID                     riid,
			ADDRESS,  // [in, optional] const D2D1_FACTORY_OPTIONS *pFactoryOptions,
			ADDRESS   // [out]          void                       **ppIFactory
		);
		D2D1CreateFactory = NativeUtils.LINKER.downcallHandle(DLL.find("D2D1CreateFactory").get(), descriptor);
	}

	public static int D2D1CreateFactory(
			D2D1_FACTORY_TYPE factoryType,
			ID2D1Factory factory
		) {
		try {
			return (int) D2D1CreateFactory.invokeExact(
				factoryType.value,
				ID2D1Factory.IID,
				MemorySegment.NULL,
				factory.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	public static D2D1_RENDER_TARGET_PROPERTIES RenderTargetProperties(Arena arena) {
		D2D1_RENDER_TARGET_PROPERTIES properties = new D2D1_RENDER_TARGET_PROPERTIES(arena);
		properties.setType(D2D1_RENDER_TARGET_TYPE.D2D1_RENDER_TARGET_TYPE_DEFAULT);
		properties.setPixelFormat(PixelFormat(arena));
		properties.setDpiX(0);
		properties.setDpiY(0);
		properties.setUsage(D2D1_RENDER_TARGET_USAGE.D2D1_RENDER_TARGET_USAGE_NONE);
		properties.setMinLevel(D2D1_FEATURE_LEVEL.D2D1_FEATURE_LEVEL_DEFAULT);
		return properties;
	}
	
	public static D2D1_PIXEL_FORMAT PixelFormat(Arena arena) {
		D2D1_PIXEL_FORMAT format = new D2D1_PIXEL_FORMAT(arena);
		format.setFormat(DXGI_FORMAT.DXGI_FORMAT_UNKNOWN);
		format.setAlphaMode(D2D1_ALPHA_MODE.D2D1_ALPHA_MODE_UNKNOWN);
		return format;
	}
	
	public static D2D1_HWND_RENDER_TARGET_PROPERTIES HwndRenderTargetProperties(Arena arena, MemorySegment hwnd, D2D1_SIZE_U size) {
		D2D1_HWND_RENDER_TARGET_PROPERTIES properties = new D2D1_HWND_RENDER_TARGET_PROPERTIES(arena);
		properties.setHwnd(hwnd);
		properties.setPixelSize(size);
		properties.setPresentOptions(D2D1_PRESENT_OPTIONS.D2D1_PRESENT_OPTIONS_NONE);
		return properties;
	}
}
