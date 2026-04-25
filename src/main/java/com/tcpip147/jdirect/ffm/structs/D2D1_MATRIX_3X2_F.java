package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.Struct;

//@formatter:off
public class D2D1_MATRIX_3X2_F extends Struct {

	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        JAVA_FLOAT.withName("_11"),
        JAVA_FLOAT.withName("_12"),
        JAVA_FLOAT.withName("_21"),
        JAVA_FLOAT.withName("_22"),
        JAVA_FLOAT.withName("_31"),
        JAVA_FLOAT.withName("_32")
    );

	private static final VarHandle _11 = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("_11"));
	private static final VarHandle _12 = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("_12"));
	private static final VarHandle _21 = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("_21"));
	private static final VarHandle _22 = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("_22"));
	private static final VarHandle _31 = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("_31"));
	private static final VarHandle _32 = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("_32"));
	
	public D2D1_MATRIX_3X2_F(Arena arena) {
		super(arena);
	}
	
	public D2D1_MATRIX_3X2_F(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public float get_11() {
		return (float) _11.get(ref, 0);
	}
	
	public void set_11(float value) {
		_11.set(ref, 0, value);
	}
	
	public float get_12() {
		return (float) _12.get(ref, 0);
	}
	
	public void set_12(float value) {
		_12.set(ref, 0, value);
	}
	
	public float get_21() {
		return (float) _21.get(ref, 0);
	}
	
	public void set_21(float value) {
		_21.set(ref, 0, value);
	}
	
	public float get_22() {
		return (float) _22.get(ref, 0);
	}
	
	public void set_22(float value) {
		_22.set(ref, 0, value);
	}
	
	public float get_31() {
		return (float) _31.get(ref, 0);
	}
	
	public void set_31(float value) {
		_31.set(ref, 0, value);
	}
	
	public float get_32() {
		return (float) _32.get(ref, 0);
	}
	
	public void set_32(float value) {
		_32.set(ref, 0, value);
	}
}
