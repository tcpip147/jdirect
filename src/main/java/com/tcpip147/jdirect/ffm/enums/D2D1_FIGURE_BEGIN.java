package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum D2D1_FIGURE_BEGIN {
    D2D1_FIGURE_BEGIN_FILLED(0),
    D2D1_FIGURE_BEGIN_HOLLOW(1),
    D2D1_FIGURE_BEGIN_FORCE_DWORD(0xffffffff)
    ;
	
	public final int value;
}
