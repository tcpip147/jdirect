package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.Struct;

//@formatter:off
public class D2D1_POINT_2F extends Struct {

	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		JAVA_FLOAT.withName("x"),
		JAVA_FLOAT.withName("y")
	);
	
	private static final VarHandle X = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("x"));
	private static final VarHandle Y = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("y"));
	
	public D2D1_POINT_2F(Arena arena) {
		super(arena);
	}
	
	public D2D1_POINT_2F(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public float getX() {
		return (float) X.get(ref, 0);
	}
	
	public void setX(float value) {
		X.set(ref, 0, value);
	}
	
	public float getY() {
		return (float) Y.get(ref, 0);
	}
	
	public void setY(float value) {
		Y.set(ref, 0, value);
	}
}
