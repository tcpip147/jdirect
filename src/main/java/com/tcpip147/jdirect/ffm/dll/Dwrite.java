package com.tcpip147.jdirect.ffm.dll;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;

import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.coms.IDWriteFactory;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FACTORY_TYPE;

//@formatter:off
public class Dwrite {

	private static final SymbolLookup DLL = SymbolLookup.libraryLookup("dwrite", Arena.global());

	// https://learn.microsoft.com/en-us/windows/win32/api/dwrite/nf-dwrite-dwritecreatefactory
	private static MethodHandle DWriteCreateFactory;
	static {
		FunctionDescriptor descriptor = FunctionDescriptor.of(
			JAVA_INT, // 
			JAVA_INT, // [in]  DWRITE_FACTORY_TYPE factoryType,
			ADDRESS,  // [in]  REFIID              iid,
			ADDRESS   // [out] IUnknown            **factory
		);
		DWriteCreateFactory = NativeUtils.LINKER.downcallHandle(DLL.find("DWriteCreateFactory").get(), descriptor);
	}

	public static int DWriteCreateFactory(
			DWRITE_FACTORY_TYPE factoryType,
			IDWriteFactory factory
		) {
		try {
			return (int) DWriteCreateFactory.invokeExact(
				factoryType.value,
				IDWriteFactory.IID,
				factory.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
