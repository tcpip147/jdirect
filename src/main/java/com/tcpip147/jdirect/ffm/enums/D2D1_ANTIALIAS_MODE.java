package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum D2D1_ANTIALIAS_MODE {
    D2D1_ANTIALIAS_MODE_PER_PRIMITIVE(0),
    D2D1_ANTIALIAS_MODE_ALIASED(1),
    D2D1_ANTIALIAS_MODE_FORCE_DWORD(0xffffffff)
	;
	
	public final int value;
}
