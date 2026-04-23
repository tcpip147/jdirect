package com.tcpip147.jdirect.ffm.coms;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

import com.tcpip147.jdirect.ffm.ComObject;
import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.structs.D2D1_COLOR_F;
import com.tcpip147.jdirect.ffm.structs.D2D1_HWND_RENDER_TARGET_PROPERTIES;
import com.tcpip147.jdirect.ffm.structs.D2D1_RENDER_TARGET_PROPERTIES;

//@formatter:off
public class ID2D1Factory extends ComObject {

	public static final MemorySegment IID = NativeUtils.uuidOf("06152247-6f50-465a-9245-118bfd3b6007");

	public ID2D1Factory(Arena arena) {
		super(arena);
	}

	private static MethodHandle CreateHwndRenderTarget;

	public int CreateHwndRenderTarget(
			D2D1_RENDER_TARGET_PROPERTIES renderTargetProperties,
			D2D1_HWND_RENDER_TARGET_PROPERTIES hwndRenderTargetProperties,
			ID2D1HwndRenderTarget hwndRenderTarget
		) {
		if (CreateHwndRenderTarget == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,  // const D2D1_RENDER_TARGET_PROPERTIES &      renderTargetProperties,
				ADDRESS,  // const D2D1_HWND_RENDER_TARGET_PROPERTIES & hwndRenderTargetProperties,
				ADDRESS   // ID2D1HwndRenderTarget                      **hwndRenderTarget
			);
			CreateHwndRenderTarget = NativeUtils.LINKER.downcallHandle(findSymbol(2), descriptor);
		}
		try {
			return (int) CreateHwndRenderTarget.invokeExact(
				ref.get(ADDRESS, 0),
				renderTargetProperties.ref,
				hwndRenderTargetProperties.ref,
				hwndRenderTarget.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle CreateSolidColorBrush;

	public int CreateSolidColorBrush(
			D2D1_COLOR_F color,
			ID2D1SolidColorBrush brush
		) {
		if (CreateSolidColorBrush == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,  // const D2D1_RENDER_TARGET_PROPERTIES &      renderTargetProperties,
				ADDRESS,  // const D2D1_HWND_RENDER_TARGET_PROPERTIES & hwndRenderTargetProperties,
				ADDRESS   // ID2D1HwndRenderTarget                      **hwndRenderTarget
			);
			CreateSolidColorBrush = NativeUtils.LINKER.downcallHandle(findSymbol(2), descriptor);
		}
		try {
			return (int) CreateSolidColorBrush.invokeExact(
				ref.get(ADDRESS, 0),
				color.ref,
				brush.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
