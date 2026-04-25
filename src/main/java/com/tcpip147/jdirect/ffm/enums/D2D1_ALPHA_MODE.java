package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum D2D1_ALPHA_MODE {
	D2D1_ALPHA_MODE_UNKNOWN(0),
	D2D1_ALPHA_MODE_PREMULTIPLIED(1),
	D2D1_ALPHA_MODE_STRAIGHT(2),
	D2D1_ALPHA_MODE_IGNORE(3),
	D2D1_ALPHA_MODE_FORCE_DWORD(0xffffffff)
	;
	
	public final int value;
}
