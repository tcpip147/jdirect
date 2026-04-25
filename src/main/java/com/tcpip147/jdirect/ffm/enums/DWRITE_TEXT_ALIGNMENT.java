package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum DWRITE_TEXT_ALIGNMENT {
    DWRITE_TEXT_ALIGNMENT_LEADING(0),
    DWRITE_TEXT_ALIGNMENT_TRAILING(1),
    DWRITE_TEXT_ALIGNMENT_CENTER(2),
    DWRITE_TEXT_ALIGNMENT_JUSTIFIED(3)
	;
	
	public final int value;
}
