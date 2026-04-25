package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.Struct;

//@formatter:off
public class D2D1_SIZE_F extends Struct {
	
	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		JAVA_FLOAT.withName("width"),
		JAVA_FLOAT.withName("height")
	);

	private static final VarHandle WIDTH = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("width"));
	private static final VarHandle HEIGHT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("height"));
	
	public D2D1_SIZE_F(Arena arena) {
		super(arena);
	}
	
	public D2D1_SIZE_F(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public float getWidth() {
		return (float) WIDTH.get(ref, 0);
	}
	
	public void setWidth(float value) {
		WIDTH.set(ref, 0, value);
	}
	
	public float getHeight() {
		return (float) HEIGHT.get(ref, 0);
	}
	
	public void setHeight(float value) {
		HEIGHT.set(ref, 0, value);
	}
}
