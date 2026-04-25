package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.Struct;
import com.tcpip147.jdirect.ffm.enums.D2D1_PRESENT_OPTIONS;

//@formatter:off
public class D2D1_HWND_RENDER_TARGET_PROPERTIES extends Struct {

	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		ADDRESS.withName("hwnd"),
		D2D1_SIZE_U.LAYOUT.withName("pixelSize"),
		JAVA_INT.withName("presentOptions")
	);
	
	private static final VarHandle HWND = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("hwnd"));
	private static final long PIXEL_SIZE_OFFSET = LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("pixelSize"));
	private static final VarHandle PRESENT_OPTIONS = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("presentOptions"));

	public D2D1_HWND_RENDER_TARGET_PROPERTIES(Arena arena) {
		super(arena);
	}
	
	public D2D1_HWND_RENDER_TARGET_PROPERTIES(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public MemorySegment getHwnd() {
		return (MemorySegment) HWND.get(ref, 0);
	}
	
	public void setHwnd(MemorySegment value) {
		HWND.set(ref, 0, value);
	}
	
	public D2D1_SIZE_U getPixelSize() {
		return new D2D1_SIZE_U(ref.asSlice(PIXEL_SIZE_OFFSET, D2D1_SIZE_U.LAYOUT.byteSize()));
	}
	
	public void setPixelSize(D2D1_SIZE_U value) {
		ref.asSlice(PIXEL_SIZE_OFFSET, D2D1_SIZE_U.LAYOUT.byteSize()).copyFrom(value.ref);
	}
	
	public D2D1_PRESENT_OPTIONS getPresentOptions() {
		return NativeUtils.from(D2D1_PRESENT_OPTIONS.class, (int) PRESENT_OPTIONS.get(ref, 0));
	}
	
	public void setPresentOptions(D2D1_PRESENT_OPTIONS e) {
		PRESENT_OPTIONS.set(ref, 0, e.value);
	}
}
