package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.Struct;

//@formatter:off
public class D2D1_ELLIPSE extends Struct {

	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		D2D1_POINT_2F.LAYOUT.withName("point"),
		JAVA_FLOAT.withName("radiusX"),
		JAVA_FLOAT.withName("radiusY")
	);

	private static final long POINT_OFFSET = LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("point"));
	private static final VarHandle RADIUS_X = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("radiusX"));
	private static final VarHandle RADIUS_Y = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("radiusY"));

	public D2D1_ELLIPSE(Arena arena) {
		super(arena);
	}
	
	public D2D1_ELLIPSE(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}

	public D2D1_POINT_2F getPoint() {
		return new D2D1_POINT_2F(ref.asSlice(POINT_OFFSET, D2D1_POINT_2F.LAYOUT.byteSize()));
	}
	
	public void setPoint(D2D1_POINT_2F value) {
		ref.asSlice(POINT_OFFSET, D2D1_POINT_2F.LAYOUT.byteSize()).copyFrom(value.ref);
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
