package com.tcpip147.jdirect.ffm.structs;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

import com.tcpip147.jdirect.ffm.Struct;

//@formatter:off
public class DWRITE_TEXT_METRICS extends Struct {

	public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
		JAVA_FLOAT.withName("left"),
		JAVA_FLOAT.withName("top"),
		JAVA_FLOAT.withName("width"),
		JAVA_FLOAT.withName("widthIncludingTrailingWhitespace"),
		JAVA_FLOAT.withName("height"),
		JAVA_FLOAT.withName("layoutWidth"),
		JAVA_FLOAT.withName("layoutHeight"),
		JAVA_INT.withName("maxBidiReorderingDepth"),
		JAVA_INT.withName("lineCount")
	);
	
	private static final VarHandle LEFT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("left"));
	private static final VarHandle TOP = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("top"));
	private static final VarHandle WIDTH = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("width"));
	private static final VarHandle WIDTH_INCLUDING_TRAILING_WHITESPACE = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("widthIncludingTrailingWhitespace"));
	private static final VarHandle HEIGHT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("height"));
	private static final VarHandle LAYOUT_WIDTH = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("layoutWidth"));
	private static final VarHandle LAYOUT_HEIGHT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("layoutHeight"));
	private static final VarHandle MAX_BIDI_REORDERING_DEPTH = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("maxBidiReorderingDepth"));
	private static final VarHandle LINE_COUNT = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("lineCount"));
	
	public DWRITE_TEXT_METRICS(Arena arena) {
		super(arena);
	}
	
	public DWRITE_TEXT_METRICS(MemorySegment ref) {
		super(ref);
	}

	@Override
	protected MemoryLayout getLayout() {
		return LAYOUT;
	}
	
	public float getLeft() {
		return (float) LEFT.get(ref, 0);
	}
	
	public void setLeft(float value) {
		LEFT.set(ref, 0, value);
	}
	
	public float getTop() {
		return (float) TOP.get(ref, 0);
	}
	
	public void setTop(float value) {
		TOP.set(ref, 0, value);
	}
	
	public float getWidth() {
		return (float) WIDTH.get(ref, 0);
	}
	
	public void setWidth(float value) {
		WIDTH.set(ref, 0, value);
	}
	
	public float getWithIncludingTrailingWhitespace() {
		return (float) WIDTH_INCLUDING_TRAILING_WHITESPACE.get(ref, 0);
	}
	
	public void setWithIncludingTrailingWhitespace(float value) {
		WIDTH_INCLUDING_TRAILING_WHITESPACE.set(ref, 0, value);
	}
	
	public float getHeight() {
		return (float) HEIGHT.get(ref, 0);
	}
	
	public void setHeight(float value) {
		HEIGHT.set(ref, 0, value);
	}
	
	public float getLayoutWidth() {
		return (float) LAYOUT_WIDTH.get(ref, 0);
	}
	
	public void setLayoutWidth(float value) {
		LAYOUT_WIDTH.set(ref, 0, value);
	}
	
	public float getLayoutHeight() {
		return (float) LAYOUT_HEIGHT.get(ref, 0);
	}
	
	public void setLayoutHeight(float value) {
		LAYOUT_HEIGHT.set(ref, 0, value);
	}
	
	public int getMaxBidiReorderingDepth() {
		return (int) MAX_BIDI_REORDERING_DEPTH.get(ref, 0);
	}
	
	public void setMaxBidiReorderingDepth(int value) {
		MAX_BIDI_REORDERING_DEPTH.set(ref, 0, value);
	}
	
	public int getLineCount() {
		return (int) LINE_COUNT.get(ref, 0);
	}
	
	public void setLineCount(int value) {
		LINE_COUNT.set(ref, 0, value);
	}
}
