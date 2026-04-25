package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.Struct;

//@formatter:off
public class D2D1_RECT_F extends Struct {

	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		JAVA_FLOAT.withName("left"),
		JAVA_FLOAT.withName("top"),
		JAVA_FLOAT.withName("right"),
		JAVA_FLOAT.withName("bottom")
	);
	
	private static final VarHandle LEFT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("left"));
	private static final VarHandle TOP = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("top"));
	private static final VarHandle RIGHT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("right"));
	private static final VarHandle BOTTOM = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("bottom"));

	public D2D1_RECT_F(Arena arena) {
		super(arena);
	}
	
	public D2D1_RECT_F(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public float getLeft() {
		return (float) LEFT.get(ref, 0);
	}
	
	public void setLeft(float value) {
		LEFT.set(ref, 0, value);
	}
	
	public float getTop() {
		return (float) TOP.get(ref, 0);
	}
	
	public void setTop(float value) {
		TOP.set(ref, 0, value);
	}
	
	public float getRight() {
		return (float) RIGHT.get(ref, 0);
	}
	
	public void setRight(float value) {
		RIGHT.set(ref, 0, value);
	}
	
	public float getBottom() {
		return (float) BOTTOM.get(ref, 0);
	}
	
	public void setBottom(float value) {
		BOTTOM.set(ref, 0, value);
	}
}
