package com.tcpip147.jdirect.ffm;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;
import static java.lang.foreign.ValueLayout.JAVA_INT;
import static java.lang.foreign.ValueLayout.JAVA_SHORT;

import java.lang.foreign.Arena;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NativeUtils {

	public static final Linker LINKER = Linker.nativeLinker();

	//@formatter:off
	public static MemorySegment uuidOf(String hexIID) {
		MemorySegment iid = Arena.global().allocate(16);
		int offset = 0;
		iid.set(JAVA_INT, offset, Integer.parseUnsignedInt(hexIID.substring(0, 8), 16));
		iid.set(JAVA_SHORT, offset += JAVA_INT.byteSize(), (short) Integer.parseUnsignedInt(hexIID.substring(9, 13), 16));
		iid.set(JAVA_SHORT, offset += JAVA_SHORT.byteSize(), (short) Integer.parseUnsignedInt(hexIID.substring(14, 18), 16));
		iid.set(JAVA_BYTE, offset += JAVA_SHORT.byteSize(), (byte) Integer.parseUnsignedInt(hexIID.substring(19, 21), 16));		
		iid.set(JAVA_BYTE, offset += JAVA_BYTE.byteSize(), (byte) Integer.parseUnsignedInt(hexIID.substring(21, 23), 16));
		iid.set(JAVA_BYTE, offset += JAVA_BYTE.byteSize(), (byte) Integer.parseUnsignedInt(hexIID.substring(24, 26), 16));
		iid.set(JAVA_BYTE, offset += JAVA_BYTE.byteSize(), (byte) Integer.parseUnsignedInt(hexIID.substring(26, 28), 16));
		iid.set(JAVA_BYTE, offset += JAVA_BYTE.byteSize(), (byte) Integer.parseUnsignedInt(hexIID.substring(28, 30), 16));
		iid.set(JAVA_BYTE, offset += JAVA_BYTE.byteSize(), (byte) Integer.parseUnsignedInt(hexIID.substring(30, 32), 16));
		iid.set(JAVA_BYTE, offset += JAVA_BYTE.byteSize(), (byte) Integer.parseUnsignedInt(hexIID.substring(32, 34), 16));
		iid.set(JAVA_BYTE, offset += JAVA_BYTE.byteSize(), (byte) Integer.parseUnsignedInt(hexIID.substring(34, 36), 16));
		return iid;
	}
	
	public static MemorySegment hwndOf(long handle) {
		return MemorySegment.ofAddress(handle);
	}


	private static final Map<Class<?>, Map<Integer, Object>> CACHE = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> E from(Class<E> clazz, int value) {
		Map<Integer, Object> map = CACHE.computeIfAbsent(clazz, _ -> {
			Map<Integer, Object> internalMap = new HashMap<>();
			try {
				var field = clazz.getDeclaredField("value");
				for (E constant : clazz.getEnumConstants()) {
					internalMap.put(field.getInt(constant), constant);
				}
			} catch (Exception e) {
				throw new RuntimeException("NativeEnum mapping failed for " + clazz.getName(), e);
			}
			return internalMap;
		});
		return (E) map.get(value);
	}
}
