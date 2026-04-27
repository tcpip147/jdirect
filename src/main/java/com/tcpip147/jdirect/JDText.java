package com.tcpip147.jdirect;

public class JDText {

	public static final int HORIZONTAL_ALIGN_LEFT = 0;
	public static final int HORIZONTAL_ALIGN_CENTER = 2;
	public static final int HORIZONTAL_ALIGN_RIGHT = 1;
	public static final int HORIZONTAL_ALIGN_JUSTIFIED = 3;

	public static final int VERTICAL_ALIGN_TOP = 0;
	public static final int VERTICAL_ALIGN_CENTER = 2;
	public static final int VERTICAL_ALIGN_BOTTOM = 1;

	public final String text;
	public final JDFont font;
	public float width;
	public float height;
	public int horizontalAlign;
	public int verticalAlign;

	protected final int resourceIndex;
	private static int nextResourceIndex = 0;

	public static final int ARENA_GLOBAL = 1;
	public static final int ARENA_GRAPHICS = 2;
	protected int arena;

	public JDText(JDFont font, String text) {
		this(font, text, Float.MAX_VALUE, Float.MAX_VALUE);
	}

	public JDText(JDFont font, String text, float width, float height) {
		this.text = text;
		this.font = font;
		this.width = width;
		this.height = height;
		resourceIndex = nextResourceIndex;
		nextResourceIndex++;
	}

	public static void close() {
		nextResourceIndex = 0;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setHorizontalAlign(int horizontalAlign) {
		this.horizontalAlign = horizontalAlign;
	}

	public void setVerticalAlign(int verticalAlign) {
		this.verticalAlign = verticalAlign;
	}
}
