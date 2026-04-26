package com.tcpip147.jdirect.ffm.coms;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

import com.tcpip147.jdirect.ffm.ComObject;
import com.tcpip147.jdirect.ffm.NativeUtils;

//@formatter:off
public class ID2D1PathGeometry extends ComObject {

	public static final MemorySegment IID = NativeUtils.uuidOf("2cd906a5-12e2-11dc-9fed-001143a055f9");

	public ID2D1PathGeometry(Arena arena) {
		super(arena);
	}

	private static MethodHandle Open;

	public int Open(
			ID2D1GeometrySink geometrySink
		) {
		if (Open == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				ADDRESS
			);
			Open = NativeUtils.LINKER.downcallHandle(findSymbol(17), descriptor);
		}
		try {
			return (int) Open.invokeExact(
				ref.get(ADDRESS, 0),
				geometrySink.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
