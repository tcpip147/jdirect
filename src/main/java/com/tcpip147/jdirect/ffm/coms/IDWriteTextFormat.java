package com.tcpip147.jdirect.ffm.coms;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import com.tcpip147.jdirect.ffm.ComObject;
import com.tcpip147.jdirect.ffm.NativeUtils;

public class IDWriteTextFormat extends ComObject {

	public static final MemorySegment IID = NativeUtils.uuidOf("9c906818-31d7-4fd3-a151-7c5e225db55a");

	public IDWriteTextFormat(Arena arena) {
		super(arena);
	}
}
