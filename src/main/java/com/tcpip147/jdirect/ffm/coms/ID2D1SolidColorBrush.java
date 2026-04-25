package com.tcpip147.jdirect.ffm.coms;

import static java.lang.foreign.ValueLayout.ADDRESS;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

import com.tcpip147.jdirect.ffm.ComObject;
import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.structs.D2D1_COLOR_F;

//@formatter:off
public class ID2D1SolidColorBrush extends ComObject {

	public static final MemorySegment IID = NativeUtils.uuidOf("2cd906a9-12e2-11dc-9fed-001143a055f9");

	public ID2D1SolidColorBrush(Arena arena) {
		super(arena);
	}

	private static MethodHandle setColor;

	public void setColor(D2D1_COLOR_F color) {
		if (setColor == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS
			);
			setColor = NativeUtils.LINKER.downcallHandle(findSymbol(8), descriptor);
		}
		try {
			setColor.invokeExact(
				ref.get(ADDRESS, 0),
				color.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
