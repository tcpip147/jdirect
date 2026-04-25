package com.tcpip147.jdirect;

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
	
	public final String name;
	public final int weight;
	public final int style;
	public final int stretch;
	public final int size;

	protected final int resourceIndex;
	private static int nextResourceIndex = 0;

	public static final int ARENA_GLOBAL = 1;
	public static final int ARENA_GRAPHICS = 2;
	protected int arena;

	public JDFont(String name, int weight, int style, int stretch, int size) {
		this.name = name;
		this.weight = weight;
		this.style = style;
		this.stretch = stretch;
		this.size = size;
		resourceIndex = nextResourceIndex;
		nextResourceIndex++;
	}
}
