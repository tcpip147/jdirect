package com.tcpip147.jdirect.ffm;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.charset.StandardCharsets;

public class WCHAR {

	public final MemorySegment ref;
	public final int size;

	public WCHAR(Arena arena, String str) {
		byte[] bytes = str.getBytes(StandardCharsets.UTF_16LE);
		size = str.length();
		ref = arena.allocate(bytes.length);
		MemorySegment.copy(bytes, 0, ref, ValueLayout.JAVA_BYTE, 0, bytes.length);
	}

	public WCHAR(MemorySegment mem, int size) {
		ref = mem;
		this.size = size;
	}
}
