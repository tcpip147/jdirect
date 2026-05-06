package com.tcpip147.jdirect;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JDStrokeStyle {

	public static final int CAP_FLAT = 0;
	public static final int CAP_SQUARE = 1;
	public static final int CAP_ROUND = 2;
	public static final int CAP_TRIANGLE = 3;

	public static final int JOIN_MITER = 0;
	public static final int JOIN_BEVEL = 1;
	public static final int JOIN_ROUND = 2;
	public static final int JOIN_MITER_OR_BEVEL = 3;

	public static final int DASH_STYLE_SOLID = 0;
	public static final int DASH_STYLE_DASH = 1;
	public static final int DASH_STYLE_DOT = 2;
	public static final int DASH_STYLE_DASH_DOT = 3;
	public static final int DASH_STYLE_DASH_DOT_DOT = 4;

	private int width;
	private int startCap;
	private int endCap;
	private int dashCap;
	private int lineJoin;
	private int miterLimit;
	private int dashStyle;
	private int dashOffset;

	public JDStrokeStyle() {
		this.width = 1;
		this.startCap = CAP_FLAT;
		this.endCap = CAP_FLAT;
		this.dashCap = CAP_FLAT;
		this.lineJoin = JOIN_MITER;
		this.miterLimit = 10;
		this.dashStyle = DASH_STYLE_SOLID;
		this.dashOffset = 0;
	}

	@Override
	public int hashCode() {
		return startCap | (endCap << 2) | (dashCap << 4) | (lineJoin << 6) | (dashStyle << 8) | (dashOffset << 11)
				| (miterLimit << 16) ^ Integer.hashCode(width);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		JDStrokeStyle other = (JDStrokeStyle) obj;
		return dashCap == other.dashCap && dashOffset == other.dashOffset && dashStyle == other.dashStyle
				&& endCap == other.endCap && lineJoin == other.lineJoin && miterLimit == other.miterLimit
				&& startCap == other.startCap && width == other.width;
	}

}
