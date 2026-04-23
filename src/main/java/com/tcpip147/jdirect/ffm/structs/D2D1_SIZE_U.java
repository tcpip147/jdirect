package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.Struct;

//@formatter:off
public class D2D1_SIZE_U extends Struct {
	
	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		JAVA_INT.withName("width"),
		JAVA_INT.withName("height")
	);
	
	private static final VarHandle WIDTH = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("width"));
	private static final VarHandle HEIGHT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("height"));

	public D2D1_SIZE_U(Arena arena) {
		super(arena);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public int getWidth() {
		return (int) WIDTH.get(ref, 0);
	}
	
	public void setWidth(int width) {
		WIDTH.set(ref, 0, width);
	}
	
	public int getHeight() {
		return (int) HEIGHT.get(ref, 0);
	}
	
	public void setHeight(int height) {
		HEIGHT.set(ref, 0, height);
	}
}
