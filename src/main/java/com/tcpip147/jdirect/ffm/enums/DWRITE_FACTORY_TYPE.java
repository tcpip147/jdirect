package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum DWRITE_FACTORY_TYPE {
    DWRITE_FACTORY_TYPE_SHARED(0),
    DWRITE_FACTORY_TYPE_ISOLATED(1)
    ;

	public final int value;
}
