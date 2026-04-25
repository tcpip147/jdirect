package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.Struct;
import com.tcpip147.jdirect.ffm.enums.D2D1_ALPHA_MODE;
import com.tcpip147.jdirect.ffm.enums.DXGI_FORMAT;

//@formatter:off
public class D2D1_PIXEL_FORMAT extends Struct {
	
	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		JAVA_INT.withName("format"),   // DXGI_FORMAT
		JAVA_INT.withName("alphaMode") // D2D1_ALPHA_MODE
	);
	
	private static final VarHandle FORMAT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("format"));
	private static final VarHandle ALPHA_MODE = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("alphaMode"));

	public D2D1_PIXEL_FORMAT(Arena arena) {
		super(arena);
	}
	
	public D2D1_PIXEL_FORMAT(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public DXGI_FORMAT getFormat() {
		return (DXGI_FORMAT) NativeUtils.from(DXGI_FORMAT.class, (int) FORMAT.get(ref, 0));
	}
	
	public void setFormat(DXGI_FORMAT e) {
		FORMAT.set(ref, 0, e.value);
	}
	
	public D2D1_ALPHA_MODE getAlphaMode() {
		return (D2D1_ALPHA_MODE) NativeUtils.from(D2D1_ALPHA_MODE.class, (int) ALPHA_MODE.get(ref, 0));
	}
	
	public void setAlphaMode(D2D1_ALPHA_MODE e) {
		ALPHA_MODE.set(ref, 0, e.value);
	}
}
