package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.Struct;

//@formatter:off
public class D2D1_ROUNDED_RECT extends Struct {

	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		D2D1_RECT_F.LAYOUT.withName("rect"),
		JAVA_FLOAT.withName("radiusX"),
		JAVA_FLOAT.withName("radiusY")
	);

	private static final long RECT_OFFSET = LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("rect"));
	private static final VarHandle RADIUS_X = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("radiusX"));
	private static final VarHandle RADIUS_Y = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("radiusY"));

	public D2D1_ROUNDED_RECT(Arena arena) {
		super(arena);
	}
	
	public D2D1_ROUNDED_RECT(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}

	public D2D1_RECT_F getRect() {
		return new D2D1_RECT_F(ref.asSlice(RECT_OFFSET, D2D1_RECT_F.LAYOUT.byteSize()));
	}
	
	public void setRect(D2D1_RECT_F value) {
		ref.asSlice(RECT_OFFSET, D2D1_RECT_F.LAYOUT.byteSize()).copyFrom(value.ref);
	}
	
	public float getRadiusX() {
		return (float) RADIUS_X.get(ref, 0);
	}
	
	public void setRadiusX(float value) {
		RADIUS_X.set(ref, 0, value);
	}
	
	public float getRadiusY() {
		return (float) RADIUS_Y.get(ref, 0);
	}
	
	public void setRadiusY(float value) {
		RADIUS_Y.set(ref, 0, value);
	}	
}
