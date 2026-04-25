package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum DWRITE_MEASURING_MODE {
    DWRITE_MEASURING_MODE_NATURAL(0),
    DWRITE_MEASURING_MODE_GDI_CLASSIC(1),
    DWRITE_MEASURING_MODE_GDI_NATURAL(2)
	;
	
	public final int value;
}
