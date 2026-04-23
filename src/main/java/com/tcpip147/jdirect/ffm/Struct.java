package com.tcpip147.jdirect.ffm;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

abstract public class Struct extends Native {

	public Struct(Arena arena) {
		super(arena);
	}

	public Struct(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected void setRef(Arena arena) {
		MemoryLayout layout = getLayout();
		ref = arena.allocate(layout).reinterpret(layout.byteSize());
	}

	abstract protected MemoryLayout getLayout();
}
