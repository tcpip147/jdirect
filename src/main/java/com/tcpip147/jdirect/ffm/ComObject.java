package com.tcpip147.jdirect.ffm;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@formatter:off
public class ComObject extends Native implements AutoCloseable {

	private static final Map<Class<?>, MethodHandle> RELEASE_HANDLES = new ConcurrentHashMap<>();
	
	public ComObject(Arena arena) {
		super(arena);
	}

	@Override
	public void setRef(Arena arena) {
		ref = arena.allocate(ADDRESS).reinterpret(ADDRESS.byteSize());
	}

	@Override
	public void close() throws Exception {
		if (RELEASE_HANDLES.get(this.getClass()) == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS
			);
			RELEASE_HANDLES.put(this.getClass(), NativeUtils.LINKER.downcallHandle(findSymbol(2), descriptor));
		}
		try {
			int _ = (int) RELEASE_HANDLES.get(this.getClass()).invokeExact(
				ref.get(ADDRESS, 0)
			);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	protected MemorySegment findSymbol(int offset) {
		MemorySegment comPtr = ref.get(ADDRESS, 0).reinterpret(ADDRESS.byteSize());
		MemorySegment vtable = comPtr.get(ADDRESS, 0).reinterpret(ADDRESS.byteSize() * (offset + 1));
		return vtable.get(ADDRESS, ADDRESS.byteSize() * offset);
	}
}
