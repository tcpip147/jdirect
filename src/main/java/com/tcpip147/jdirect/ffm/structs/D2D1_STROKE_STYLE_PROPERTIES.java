package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.Struct;
import com.tcpip147.jdirect.ffm.enums.D2D1_CAP_STYLE;
import com.tcpip147.jdirect.ffm.enums.D2D1_DASH_STYLE;
import com.tcpip147.jdirect.ffm.enums.D2D1_LINE_JOIN;

//@formatter:off
public class D2D1_STROKE_STYLE_PROPERTIES extends Struct {
	
	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		JAVA_INT.withName("startCap"),
		JAVA_INT.withName("endCap"),
		JAVA_INT.withName("dashCap"),
		JAVA_INT.withName("lineJoin"),
		JAVA_FLOAT.withName("miterLimit"),
		JAVA_INT.withName("dashStyle"),
		JAVA_FLOAT.withName("dashOffset")
	);
	
	private static final VarHandle START_CAP = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("startCap"));
	private static final VarHandle END_CAP = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("endCap"));
	private static final VarHandle DASH_CAP = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("dashCap"));
	private static final VarHandle LINE_JOIN = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("lineJoin"));
	private static final VarHandle MITER_LIMIT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("miterLimit"));
	private static final VarHandle DASH_STYLE = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("dashStyle"));
	private static final VarHandle DASH_OFFSET = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("dashOffset"));

	public D2D1_STROKE_STYLE_PROPERTIES(Arena arena) {
		super(arena);
	}
	
	public D2D1_STROKE_STYLE_PROPERTIES(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public D2D1_CAP_STYLE getStartCap() {
		return (D2D1_CAP_STYLE) NativeUtils.from(D2D1_CAP_STYLE.class, (int)START_CAP.get(ref, 0));
	}
	
	public void setStartCap(D2D1_CAP_STYLE e) {
		START_CAP.set(ref, 0, e.value);
	}
	
	public D2D1_CAP_STYLE getEndCap() {
		return (D2D1_CAP_STYLE) NativeUtils.from(D2D1_CAP_STYLE.class, (int)END_CAP.get(ref, 0));
	}
	
	public void setEndCap(D2D1_CAP_STYLE e) {
		END_CAP.set(ref, 0, e.value);
	}
	
	public D2D1_CAP_STYLE getDashCap() {
		return (D2D1_CAP_STYLE) NativeUtils.from(D2D1_CAP_STYLE.class, (int)DASH_CAP.get(ref, 0));
	}
	
	public void setDashCap(D2D1_CAP_STYLE e) {
		DASH_CAP.set(ref, 0, e.value);
	}
	
	public D2D1_LINE_JOIN getLineJoin() {
		return (D2D1_LINE_JOIN) NativeUtils.from(D2D1_LINE_JOIN.class, (int) LINE_JOIN.get(ref, 0));
	}
	
	public void setLineJoin(D2D1_LINE_JOIN e) {
		LINE_JOIN.set(ref, 0, e.value);
	}
	
	public float getMiterLimit() {
		return (float) MITER_LIMIT.get(ref, 0);
	}
	
	public void setMiterLimit(float miterLimit) {
		MITER_LIMIT.set(ref, 0, miterLimit);
	}
	
	public D2D1_DASH_STYLE getDashStyle() {
		return (D2D1_DASH_STYLE) NativeUtils.from(D2D1_DASH_STYLE.class, (int) DASH_STYLE.get(ref, 0));
	}
	
	public void setDashStyle(D2D1_DASH_STYLE e) {
		DASH_STYLE.set(ref, 0, e.value);
	}
	
	public float getDashOffset() {
		return (float) DASH_OFFSET.get(ref, 0);
	}
	
	public void setDashOffset(float dashOffset) {
		DASH_OFFSET.set(ref, 0, dashOffset);
	}
}
