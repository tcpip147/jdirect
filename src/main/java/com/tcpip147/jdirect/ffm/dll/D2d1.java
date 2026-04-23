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

//@formatter:off
public class D2d1 {
	
	private static SymbolLookup DLL = SymbolLookup.libraryLookup("d2d1", Arena.global());

	// https://learn.microsoft.com/en-us/windows/win32/api/d2d1/nf-d2d1-d2d1createfactory
	private static MethodHandle D2D1CreateFactory;
	static {
		MemorySegment symbol = DLL.find("D2D1CreateFactory").get();
		FunctionDescriptor descriptor = FunctionDescriptor.of(
			JAVA_INT, // 
			JAVA_INT, // [in]           D2D1_FACTORY_TYPE          factoryType,
			ADDRESS,  // [in]           REFIID                     riid,
			ADDRESS,  // [in, optional] const D2D1_FACTORY_OPTIONS *pFactoryOptions,
			ADDRESS   // [out]          void                       **ppIFactory
		);
		D2D1CreateFactory = NativeUtils.LINKER.downcallHandle(symbol, descriptor);
	}

	public static int D2D1CreateFactory(
			int factoryType,
			ID2D1Factory factory
		) {
		try {
			return (int) D2D1CreateFactory.invokeExact(factoryType, ID2D1Factory.IID, MemorySegment.NULL, factory.ref);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
