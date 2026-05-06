package com.tcpip147.jdirect.ffm.coms;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

import com.tcpip147.jdirect.ffm.ComObject;
import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.structs.D2D1_HWND_RENDER_TARGET_PROPERTIES;
import com.tcpip147.jdirect.ffm.structs.D2D1_RENDER_TARGET_PROPERTIES;
import com.tcpip147.jdirect.ffm.structs.D2D1_STROKE_STYLE_PROPERTIES;

//@formatter:off
public class ID2D1Factory extends ComObject {

	public static final MemorySegment IID = NativeUtils.uuidOf("06152247-6f50-465a-9245-118bfd3b6007");

	public ID2D1Factory(Arena arena) {
		super(arena);
	}
	
	private static MethodHandle CreatePathGeometry;

	public int CreatePathGeometry(
			ID2D1PathGeometry pathGeometry
		) {
		if (CreatePathGeometry == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				ADDRESS
			);
			CreatePathGeometry = NativeUtils.LINKER.downcallHandle(findSymbol(10), descriptor);
		}
		try {
			return (int) CreatePathGeometry.invokeExact(
				ref.get(ADDRESS, 0),
				pathGeometry.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle CreateStrokeStyle;

	public int CreateStrokeStyle(
			D2D1_STROKE_STYLE_PROPERTIES strokeStyleProperties,
			ID2D1StrokeStyle strokeStyle
		) {
		if (CreateStrokeStyle == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				ADDRESS,
				ADDRESS,
				JAVA_INT,
				ADDRESS
			);
			CreateStrokeStyle = NativeUtils.LINKER.downcallHandle(findSymbol(11), descriptor);
		}
		try {
			return (int) CreateStrokeStyle.invokeExact(
				ref.get(ADDRESS, 0),
				strokeStyleProperties.ref,
				MemorySegment.NULL,
				0,
				strokeStyle.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
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
				ADDRESS,
				ADDRESS,  // const D2D1_RENDER_TARGET_PROPERTIES &      renderTargetProperties,
				ADDRESS,  // const D2D1_HWND_RENDER_TARGET_PROPERTIES & hwndRenderTargetProperties,
				ADDRESS   // ID2D1HwndRenderTarget                      **hwndRenderTarget
			);
			CreateHwndRenderTarget = NativeUtils.LINKER.downcallHandle(findSymbol(14), descriptor);
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
}
