package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.Struct;

//@formatter:off
public class D2D1_RENDER_TARGET_PROPERTIES extends Struct {

	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		JAVA_INT.withName("type"),                        // D2D1_RENDER_TARGET_TYPE
		D2D1_PIXEL_FORMAT.LAYOUT.withName("pixelFormat"),
		JAVA_FLOAT.withName("dpiX"),
		JAVA_FLOAT.withName("dpiY"),
		JAVA_INT.withName("usage"),                       // D2D1_RENDER_TARGET_USAGE
		JAVA_INT.withName("minLevel")                     // D2D1_FEATURE_LEVEL
	);
	
	private static final VarHandle TYPE = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("type"));
	private static final long PIXEL_FORMAT_OFFSET = LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("pixelFormat"));
	private static final VarHandle DPI_X = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("dpiX"));
	private static final VarHandle DPI_Y = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("dpiY"));
	private static final VarHandle USAGE = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("usage"));
	private static final VarHandle MIN_LEVEL = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("minLevel"));
	
	public D2D1_RENDER_TARGET_PROPERTIES(Arena arena) {
		super(arena);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public int getType() {
		return (int) TYPE.get(ref, 0);
	}
	
	public void setType(int type) {
		TYPE.set(ref, 0, type);
	}
	
	public D2D1_PIXEL_FORMAT getPixelFormat() {
		return new D2D1_PIXEL_FORMAT(ref.asSlice(PIXEL_FORMAT_OFFSET, D2D1_PIXEL_FORMAT.LAYOUT.byteSize()));
	}
	
	public void setPixelFormat(D2D1_PIXEL_FORMAT pixelFormat) {
		MemorySegment target = this.ref.asSlice(PIXEL_FORMAT_OFFSET, D2D1_PIXEL_FORMAT.LAYOUT.byteSize());
		target.copyFrom(pixelFormat.ref);
	}
	
	public float getDpiX() {
		return (float) DPI_X.get(ref, 0);
	}
	
	public void setDpiX(float dpiX) {
		DPI_X.set(ref, 0, dpiX);
	}
	
	public float getDpiY() {
		return (float) DPI_Y.get(ref, 0);
	}
	
	public void setDpiY(float dpiY) {
		DPI_X.set(ref, 0, dpiY);
	}
	
	public int getUsage() {
		return (int) USAGE.get(ref, 0);
	}
	
	public void setUsage(int usage) {
		USAGE.set(ref, 0, usage);
	}
	
	public int getMinLevel() {
		return (int) MIN_LEVEL.get(ref, 0);
	}
	
	public void setMinLevel(int minLevel) {
		MIN_LEVEL.set(ref, 0, minLevel);
	}
}
