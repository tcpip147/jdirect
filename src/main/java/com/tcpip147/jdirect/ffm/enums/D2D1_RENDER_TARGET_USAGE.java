package com.tcpip147.jdirect.ffm.enums;

import lombok.AllArgsConstructor;

//@formatter:off
@AllArgsConstructor
public enum D2D1_RENDER_TARGET_USAGE {
	D2D1_RENDER_TARGET_USAGE_NONE(0x00000000),
	D2D1_RENDER_TARGET_USAGE_FORCE_BITMAP_REMOTING(0x00000001),
	D2D1_RENDER_TARGET_USAGE_GDI_COMPATIBLE(0x00000002),
	D2D1_RENDER_TARGET_USAGE_FORCE_DWORD(0xffffffff)
	;
	
	public final int value;
}
