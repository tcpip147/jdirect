package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum D2D1_RENDER_TARGET_TYPE {
	D2D1_RENDER_TARGET_TYPE_DEFAULT(0),
	D2D1_RENDER_TARGET_TYPE_SOFTWARE(1),
	D2D1_RENDER_TARGET_TYPE_HARDWARE(2),
	D2D1_RENDER_TARGET_TYPE_FORCE_DWORD(0xffffffff)
	;
	
	public final int value;
}
