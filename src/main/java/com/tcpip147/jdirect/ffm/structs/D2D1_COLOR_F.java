package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.Struct;

//@formatter:off
public class D2D1_COLOR_F extends Struct {

	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		JAVA_FLOAT.withName("r"),
		JAVA_FLOAT.withName("g"),
		JAVA_FLOAT.withName("b"),
		JAVA_FLOAT.withName("a")
	);
	
	private static final VarHandle R = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("r"));
	private static final VarHandle G = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("g"));
	private static final VarHandle B = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("b"));
	private static final VarHandle A = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("a"));
	
	public D2D1_COLOR_F(Arena arena) {
		super(arena);
	}
	
	public D2D1_COLOR_F(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public float getR() {
		return (float) R.get(ref, 0);
	}
	
	public void setR(float value) {
		R.set(ref, 0, value);
	}
	
	public float getG() {
		return (float) G.get(ref, 0);
	}
	
	public void setG(float value) {
		G.set(ref, 0, value);
	}
	
	public float getB() {
		return (float) B.get(ref, 0);
	}
	
	public void setB(float value) {
		B.set(ref, 0, value);
	}
	
	public float getA() {
		return (float) A.get(ref, 0);
	}
	
	public void setA(float value) {
		A.set(ref, 0, value);
	}
}
