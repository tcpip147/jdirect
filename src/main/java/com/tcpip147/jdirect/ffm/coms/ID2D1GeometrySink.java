package com.tcpip147.jdirect.ffm.coms;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

import com.tcpip147.jdirect.ffm.ComObject;
import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.enums.D2D1_FIGURE_BEGIN;
import com.tcpip147.jdirect.ffm.enums.D2D1_FIGURE_END;
import com.tcpip147.jdirect.ffm.structs.D2D1_POINT_2F;

//@formatter:off
public class ID2D1GeometrySink extends ComObject {

	public static final MemorySegment IID = NativeUtils.uuidOf("2cd9069f-12e2-11dc-9fed-001143a055f9");

	public ID2D1GeometrySink(Arena arena) {
		super(arena);
	}

	private static MethodHandle BeginFigure;

	public void BeginFigure(
			D2D1_POINT_2F startPoint,
			D2D1_FIGURE_BEGIN figureBegin 
		) {
		if (BeginFigure == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				D2D1_POINT_2F.LAYOUT,
				JAVA_INT
			);
			BeginFigure = NativeUtils.LINKER.downcallHandle(findSymbol(5), descriptor);
		}
		try {
			BeginFigure.invokeExact(
				ref.get(ADDRESS, 0),
				startPoint.ref,
				figureBegin.value
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle EndFigure;

	public void EndFigure(
			D2D1_FIGURE_END figureEnd 
		) {
		if (EndFigure == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				JAVA_INT
			);
			EndFigure = NativeUtils.LINKER.downcallHandle(findSymbol(8), descriptor);
		}
		try {
			EndFigure.invokeExact(
				ref.get(ADDRESS, 0),
				figureEnd.value
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle Close;

	public void Close(
		) {
		if (Close == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS
			);
			Close = NativeUtils.LINKER.downcallHandle(findSymbol(9), descriptor);
		}
		try {
			Close.invokeExact(
				ref.get(ADDRESS, 0)
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle AddLine;

	public void AddLine(
			D2D1_POINT_2F point
		) {
		if (AddLine == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				D2D1_POINT_2F.LAYOUT
			);
			AddLine = NativeUtils.LINKER.downcallHandle(findSymbol(10), descriptor);
		}
		try {
			AddLine.invokeExact(
				ref.get(ADDRESS, 0),
				point.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
