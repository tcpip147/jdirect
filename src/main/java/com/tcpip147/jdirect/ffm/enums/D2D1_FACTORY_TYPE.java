package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum D2D1_FACTORY_TYPE {
	D2D1_FACTORY_TYPE_SINGLE_THREADED(0),
	D2D1_FACTORY_TYPE_MULTI_THREADED(1),
	D2D1_FACTORY_TYPE_FORCE_DWORD(0xffffffff)
	;
	
	public final int value;
}
