package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum D2D1_PRESENT_OPTIONS {
	D2D1_PRESENT_OPTIONS_NONE(0x00000000),
	D2D1_PRESENT_OPTIONS_RETAIN_CONTENTS(0x00000001),
	D2D1_PRESENT_OPTIONS_IMMEDIATELY(0x00000002),
	D2D1_PRESENT_OPTIONS_FORCE_DWORD(0xfffffff)
	;
	
	public final int value;
}
