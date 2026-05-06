package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum D2D1_CAP_STYLE {
	D2D1_CAP_STYLE_FLAT(0),
	D2D1_CAP_STYLE_SQUARE(1),
	D2D1_CAP_STYLE_ROUND(2),
	D2D1_CAP_STYLE_TRIANGLE(3),
	D2D1_CAP_STYLE_FORCE_DWORD(0xffffffff)
	;
	
	public final int value;
}
