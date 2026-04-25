package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum DWRITE_FONT_STYLE {
    DWRITE_FONT_STYLE_NORMAL(0),
    DWRITE_FONT_STYLE_OBLIQUE(1),
    DWRITE_FONT_STYLE_ITALIC(2)
	;

	public final int value;
}
