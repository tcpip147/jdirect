package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum DWRITE_PARAGRAPH_ALIGNMENT {
    DWRITE_PARAGRAPH_ALIGNMENT_NEAR(0),
    DWRITE_PARAGRAPH_ALIGNMENT_FAR(1),
    DWRITE_PARAGRAPH_ALIGNMENT_CENTER(2)
	;
	
	public final int value;
}
