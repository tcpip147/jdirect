package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.Struct;

//@formatter:off
public class LPRECT extends Struct {
	
	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		JAVA_INT.withName("left"),
		JAVA_INT.withName("top"),
		JAVA_INT.withName("right"),
		JAVA_INT.withName("bottom")
	);
	
	private static final VarHandle LEFT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("left"));
	private static final VarHandle TOP = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("top"));
	private static final VarHandle RIGHT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("right"));
	private static final VarHandle BOTTOM = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("bottom"));

	public LPRECT(Arena arena) {
		super(arena);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public int getLeft() {
		return (int) LEFT.get(ref, 0);
	}
	
	public void setLeft(int left) {
		LEFT.set(ref, 0, left);
	}
	
	public int getTop() {
		return (int) TOP.get(ref, 0);
	}
	
	public void setTop(int top) {
		LEFT.set(ref, 0, top);
	}
	
	public int getRight() {
		return (int) RIGHT.get(ref, 0);
	}
	
	public void setRight(int top) {
		TOP.set(ref, 0, top);
	}
	
	public int getBottom() {
		return (int) BOTTOM.get(ref, 0);
	}
	
	public void setBottom(int bottom) {
		BOTTOM.set(ref, 0, bottom);;
	}
}
