package com.tcpip147.jdirect.ffm.coms;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_FLOAT;
import static java.lang.foreign.ValueLayout.JAVA_INT;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;

import com.tcpip147.jdirect.ffm.ComObject;
import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.WCHAR;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FONT_STRETCH;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FONT_STYLE;
import com.tcpip147.jdirect.ffm.enums.DWRITE_FONT_WEIGHT;

//@formatter:off
public class IDWriteFactory extends ComObject {

	public static final MemorySegment IID = NativeUtils.uuidOf("b859ee5a-d838-4b5b-a2e8-1adc7d93db48");

	public IDWriteFactory(Arena arena) {
		super(arena);
	}

	private static MethodHandle CreateTextFormat;

	public int CreateTextFormat(
			WCHAR fontFamilyName,
			DWRITE_FONT_WEIGHT fontWeight,
			DWRITE_FONT_STYLE fontStyle,
			DWRITE_FONT_STRETCH fontStretch,
			float fontSize,
			WCHAR localName,
			IDWriteTextFormat textFormat
		) {
		if (CreateTextFormat == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,    // 
				ADDRESS,    // [in]  WCHAR const           *fontFamilyName,  
				ADDRESS,    //       IDWriteFontCollection *fontCollection,
				JAVA_INT,   //       DWRITE_FONT_WEIGHT    fontWeight,     
				JAVA_INT,   //       DWRITE_FONT_STYLE     fontStyle,      
				JAVA_INT,   //       DWRITE_FONT_STRETCH   fontStretch,    
				JAVA_FLOAT, //       FLOAT                 fontSize,       
				ADDRESS,    // [in]  WCHAR const           *localeName,    
				ADDRESS     // [out] IDWriteTextFormat     **textFormat    
			);
			CreateTextFormat = NativeUtils.LINKER.downcallHandle(findSymbol(15), descriptor);
		}
		try {
			return (int) CreateTextFormat.invokeExact(
				ref.get(ADDRESS, 0),
				fontFamilyName.ref,
				MemorySegment.NULL,
				fontWeight.value,
				fontStyle.value,
				fontStretch.value,
				fontSize,
				localName.ref,
				textFormat.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	private static MethodHandle CreateTextLayout;

	public int CreateTextLayout(
			WCHAR string,
			int stringLength,
			IDWriteTextFormat textFormat,
			float maxWidth,
			float maxHeight,
			IDWriteTextLayout textLayout
		) {
		if (CreateTextLayout == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				ADDRESS,
				JAVA_INT,
				ADDRESS,
				JAVA_FLOAT,
				JAVA_FLOAT,
				ADDRESS
			);
			CreateTextLayout = NativeUtils.LINKER.downcallHandle(findSymbol(18), descriptor);
		}
		try {
			return (int) CreateTextLayout.invokeExact(
				ref.get(ADDRESS, 0),
				string.ref,
				stringLength,
				textFormat.ref.get(ADDRESS, 0),
				maxWidth,
				maxHeight,
				textLayout.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
