package com.tcpip147.jdirect.ffm.dll;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_BOOLEAN;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;

import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.structs.LPRECT;

//@formatter:off
public class User32 {

	private static final SymbolLookup DLL = SymbolLookup.libraryLookup("user32", Arena.global());

	// https://learn.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getclientrect
	private static MethodHandle GetClientRect;
	static {
		FunctionDescriptor descriptor = FunctionDescriptor.of(
			JAVA_BOOLEAN, //
			ADDRESS,      // [in]  HWND   hWnd,
			ADDRESS       // [out] LPRECT lpRect
		);
		GetClientRect = NativeUtils.LINKER.downcallHandle(DLL.find("GetClientRect").get(), descriptor);
	}

	public static boolean GetClientRect(
			MemorySegment hwnd,
			LPRECT rect
		) {
		try {
			return (boolean) GetClientRect.invokeExact(
				hwnd,
				rect.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
