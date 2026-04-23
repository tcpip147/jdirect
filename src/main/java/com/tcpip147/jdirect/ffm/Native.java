package com.tcpip147.jdirect.ffm;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

abstract public class Native {

	public MemorySegment ref;

	public Native(Arena arena) {
		setRef(arena);
	}

	public Native(MemorySegment ref) {
		this.ref = ref;
	}

	abstract protected void setRef(Arena arena);
}
