package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum D2D1_FEATURE_LEVEL {
	D2D1_FEATURE_LEVEL_DEFAULT(0),
	D2D1_FEATURE_LEVEL_9(0x9100),
	D2D1_FEATURE_LEVEL_10(0xa000),
	D2D1_FEATURE_LEVEL_FORCE_DWORD(0xffffffff)
	;
	
	public final int value;
}
