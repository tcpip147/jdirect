package com.tcpip147.jdirect.ffm.coms;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_FLOAT;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

import com.tcpip147.jdirect.ffm.ComObject;
import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.enums.DWRITE_PARAGRAPH_ALIGNMENT;
import com.tcpip147.jdirect.ffm.enums.DWRITE_TEXT_ALIGNMENT;
import com.tcpip147.jdirect.ffm.structs.DWRITE_TEXT_METRICS;

//@formatter:off
public class IDWriteTextLayout extends ComObject {

	public static final MemorySegment IID = NativeUtils.uuidOf("53737037-6d14-410b-9bfe-0b182bb70961");

	public IDWriteTextLayout(Arena arena) {
		super(arena);
	}

	private static MethodHandle SetTextAlignment;

	public int SetTextAlignment(
			DWRITE_TEXT_ALIGNMENT textAlignment
		) {
		if (SetTextAlignment == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				JAVA_INT
			);
			SetTextAlignment = NativeUtils.LINKER.downcallHandle(findSymbol(3), descriptor);
		}
		try {
			return (int) SetTextAlignment.invokeExact(
				ref.get(ADDRESS, 0),
				textAlignment.value
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle SetParagraphAlignment;

	public int SetParagraphAlignment(
			DWRITE_PARAGRAPH_ALIGNMENT paragraphAlignment
		) {
		if (SetParagraphAlignment == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				JAVA_INT
			);
			SetParagraphAlignment = NativeUtils.LINKER.downcallHandle(findSymbol(4), descriptor);
		}
		try {
			return (int) SetParagraphAlignment.invokeExact(
				ref.get(ADDRESS, 0),
				paragraphAlignment.value
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle SetMaxWidth;

	public int SetMaxWidth(
			float maxWidth
		) {
		if (SetMaxWidth == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				JAVA_FLOAT
			);
			SetMaxWidth = NativeUtils.LINKER.downcallHandle(findSymbol(28), descriptor);
		}
		try {
			return (int) SetMaxWidth.invokeExact(
				ref.get(ADDRESS, 0),
				maxWidth
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle SetMaxHeight;

	public int SetMaxHeight(
			float maxHeight
		) {
		if (SetMaxHeight == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				JAVA_FLOAT
			);
			SetMaxHeight = NativeUtils.LINKER.downcallHandle(findSymbol(29), descriptor);
		}
		try {
			return (int) SetMaxHeight.invokeExact(
				ref.get(ADDRESS, 0),
				maxHeight
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle GetMaxWidth;

	public float GetMaxWidth(
		) {
		if (GetMaxWidth == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_FLOAT,
				ADDRESS
			);
			GetMaxWidth = NativeUtils.LINKER.downcallHandle(findSymbol(42), descriptor);
		}
		try {
			return (float) GetMaxWidth.invokeExact(
				ref.get(ADDRESS, 0)
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle GetMaxHeight;

	public float GetMaxHeight(
		) {
		if (GetMaxHeight == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_FLOAT,
				ADDRESS
			);
			GetMaxHeight = NativeUtils.LINKER.downcallHandle(findSymbol(43), descriptor);
		}
		try {
			return (float) GetMaxHeight.invokeExact(
				ref.get(ADDRESS, 0)
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle GetMetrics;

	public int GetMetrics(
			DWRITE_TEXT_METRICS textMetrics
		) {
		if (GetMetrics == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				ADDRESS
			);
			GetMetrics = NativeUtils.LINKER.downcallHandle(findSymbol(60), descriptor);
		}
		try {
			return (int) GetMetrics.invokeExact(
				ref.get(ADDRESS, 0),
				textMetrics.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
