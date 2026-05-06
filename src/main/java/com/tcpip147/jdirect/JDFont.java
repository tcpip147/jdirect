package com.tcpip147.jdirect;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JDFont {
	public static final int WEIGHT_THIN = 100;
	public static final int WEIGHT_EXTRA_LIGHT = 200;
	public static final int WEIGHT_LIGHT = 300;
	public static final int WEIGHT_SEMI_LIGHT = 350;
	public static final int WEIGHT_NORMAL = 400;
	public static final int WEIGHT_MEDIUM = 500;
	public static final int WEIGHT_SEMI_BOLD = 600;
	public static final int WEIGHT_BOLD = 700;
	public static final int WEIGHT_EXTRA_BOLD = 800;
	public static final int WEIGHT_BLACK = 900;
	public static final int WEIGHT_EXTRA_BLACK = 950;

	public static final int STYLE_NORMAL = 0;
	public static final int STYLE_OBLIQUE = 1;
	public static final int STYLE_ITALIC = 2;

	public static final int STRETCH_UNDEFINED = 0;
	public static final int STRETCH_ULTRA_CONDENSED = 1;
	public static final int STRETCH_EXTRA_CONDENSED = 2;
	public static final int STRETCH_CONDENSED = 3;
	public static final int STRETCH_SEMI_CONDENSED = 4;
	public static final int STRETCH_NORMAL = 5;
	public static final int STRETCH_SEMI_EXPANDED = 6;
	public static final int STRETCH_EXPANDED = 7;
	public static final int STRETCH_EXTRA_EXPANDED = 8;
	public static final int STRETCH_ULTRA_EXPANDED = 9;

	private String name;
	private int weight;
	private int style;
	private int stretch;
	private int size;

	public JDFont() {
		name = "맑은 고딕";
		weight = WEIGHT_NORMAL;
		style = STYLE_NORMAL;
		stretch = STRETCH_NORMAL;
		size = 12;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, size, stretch, style, weight);
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
		JDFont other = (JDFont) obj;
		return Objects.equals(name, other.name) && size == other.size && stretch == other.stretch
				&& style == other.style && weight == other.weight;
	}

}
