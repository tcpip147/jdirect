package com.tcpip147.jdirect;

public class JDPath {

	public final float[] path;

	protected final int resourceIndex;
	private static int nextResourceIndex = 0;

	public static final int ARENA_GLOBAL = 1;
	public static final int ARENA_GRAPHICS = 2;
	protected int arena;

	public JDPath(float[] path) {
		this.path = path;
		resourceIndex = nextResourceIndex;
		nextResourceIndex++;
	}
}
