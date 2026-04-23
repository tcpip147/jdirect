package com.tcpip147.jdirect.ffm.coms;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import com.tcpip147.jdirect.ffm.ComObject;
import com.tcpip147.jdirect.ffm.NativeUtils;

public class ID2D1SolidColorBrush extends ComObject {

	public static final MemorySegment IID = NativeUtils.uuidOf("2cd906a9-12e2-11dc-9fed-001143a055f9");

	public ID2D1SolidColorBrush(Arena arena) {
		super(arena);
	}
}
