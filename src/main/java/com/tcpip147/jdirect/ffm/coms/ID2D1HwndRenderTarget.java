package com.tcpip147.jdirect.ffm.coms;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

import com.tcpip147.jdirect.ffm.ComObject;
import com.tcpip147.jdirect.ffm.NativeUtils;

public class ID2D1HwndRenderTarget extends ComObject {

	public static final MemorySegment IID = NativeUtils.uuidOf("2cd90698-12e2-11dc-9fed-001143a055f9");

	public ID2D1HwndRenderTarget(Arena arena) {
		super(arena);
	}

	private static MethodHandle BeginDraw;

	public void BeginDraw() {
		// TODO
	}

	private static MethodHandle SetTransform;

	public void SetTransform() {
		// TODO
	}

	private static MethodHandle Clear;

	public void Clear() {
		// TODO
	}

	private static MethodHandle GetSize;

	public void GetSize() {
		// TODO
	}

	private static MethodHandle FillRectangle;

	public void FillRectangle() {
		// TODO
	}

	private static MethodHandle EndDraw;

	public void EndDraw() {
		// TOOD
	}
}
