package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum D2D1_LINE_JOIN {
	D2D1_LINE_JOIN_MITER(0),
	D2D1_LINE_JOIN_BEVEL(1),
	D2D1_LINE_JOIN_ROUND(2),
	D2D1_LINE_JOIN_MITER_OR_BEVEL(3),
	D2D1_LINE_JOIN_FORCE_DWORD(0xffffffff)
	;
	
	public final int value;
}
