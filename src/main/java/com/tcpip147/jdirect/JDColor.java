package com.tcpip147.jdirect;

public class JDColor {

	public final float r;
	public final float g;
	public final float b;
	public final float a;

	protected final int resourceIndex;
	private static int nextResourceIndex = 0;

	public static final int ARENA_GLOBAL = 1;
	public static final int ARENA_GRAPHICS = 2;
	protected int arena;

	public JDColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		resourceIndex = nextResourceIndex;
		nextResourceIndex++;
	}
}
