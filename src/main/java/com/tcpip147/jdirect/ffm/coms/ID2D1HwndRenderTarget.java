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
import com.tcpip147.jdirect.ffm.enums.D2D1_ANTIALIAS_MODE;
import com.tcpip147.jdirect.ffm.enums.D2D1_DRAW_TEXT_OPTIONS;
import com.tcpip147.jdirect.ffm.enums.DWRITE_MEASURING_MODE;
import com.tcpip147.jdirect.ffm.structs.D2D1_COLOR_F;
import com.tcpip147.jdirect.ffm.structs.D2D1_ELLIPSE;
import com.tcpip147.jdirect.ffm.structs.D2D1_MATRIX_3X2_F;
import com.tcpip147.jdirect.ffm.structs.D2D1_POINT_2F;
import com.tcpip147.jdirect.ffm.structs.D2D1_RECT_F;
import com.tcpip147.jdirect.ffm.structs.D2D1_ROUNDED_RECT;
import com.tcpip147.jdirect.ffm.structs.D2D1_SIZE_U;

//@formatter:off
public class ID2D1HwndRenderTarget extends ComObject {

	public static final MemorySegment IID = NativeUtils.uuidOf("2cd90698-12e2-11dc-9fed-001143a055f9");

	public ID2D1HwndRenderTarget(Arena arena) {
		super(arena);
	}

	// https://learn.microsoft.com/en-us/windows/win32/api/d2d1/nf-d2d1-id2d1rendertarget-createsolidcolorbrush(constd2d1_color_f_constd2d1_brush_properties_id2d1solidcolorbrush)
	private static MethodHandle CreateSolidColorBrush;

	public int CreateSolidColorBrush(
			D2D1_COLOR_F color,
			ID2D1SolidColorBrush brush
		) {
		if (CreateSolidColorBrush == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				ADDRESS, // const D2D1_COLOR_F          *color,
				ADDRESS, // const D2D1_BRUSH_PROPERTIES *brushProperties,
				ADDRESS  // ID2D1SolidColorBrush        **solidColorBrush
			);
			CreateSolidColorBrush = NativeUtils.LINKER.downcallHandle(findSymbol(8), descriptor);
		}
		try {
			return (int) CreateSolidColorBrush.invokeExact(
				ref.get(ADDRESS, 0),
				color.ref,
				MemorySegment.NULL,
				brush.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle BeginDraw;

	public void BeginDraw() {
		if (BeginDraw == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS
			);
			BeginDraw = NativeUtils.LINKER.downcallHandle(findSymbol(48), descriptor);
		}
		try {
			BeginDraw.invokeExact(
				ref.get(ADDRESS, 0)
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle Clear;

	public void Clear(D2D1_COLOR_F color) {
		if (Clear == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS
			);
			Clear = NativeUtils.LINKER.downcallHandle(findSymbol(47), descriptor);
		}
		try {
			Clear.invokeExact(
				ref.get(ADDRESS, 0),
				color.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle DrawLine;

	public void DrawLine(
			D2D1_POINT_2F point0,
			D2D1_POINT_2F point1,
			ID2D1SolidColorBrush brush,
			float strokeWidth,
			ID2D1StrokeStyle strokeStyle
		) {
		if (DrawLine == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				D2D1_POINT_2F.LAYOUT,
				D2D1_POINT_2F.LAYOUT,
				ADDRESS,
				JAVA_FLOAT,
				ADDRESS
			);
			DrawLine = NativeUtils.LINKER.downcallHandle(findSymbol(15), descriptor);
		}
		try {
			MemorySegment strokeStyleSegment;
			if (strokeStyle == null) {
				strokeStyleSegment = MemorySegment.NULL;
			} else {
				strokeStyleSegment = strokeStyle.ref.get(ADDRESS, 0);
			}
			DrawLine.invokeExact(
				ref.get(ADDRESS, 0),
				point0.ref,
				point1.ref,
				brush.ref.get(ADDRESS, 0),
				strokeWidth,
				strokeStyleSegment
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle DrawRectangle;

	public void DrawRectangle(
			D2D1_RECT_F rect,
			ID2D1SolidColorBrush brush,
			float strokeWidth,
			ID2D1StrokeStyle strokeStyle
		) {
		if (DrawRectangle == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS,
				ADDRESS,
				JAVA_FLOAT,
				ADDRESS
			);
			DrawRectangle = NativeUtils.LINKER.downcallHandle(findSymbol(16), descriptor);
		}
		try {
			MemorySegment strokeStyleSegment;
			if (strokeStyle == null) {
				strokeStyleSegment = MemorySegment.NULL;
			} else {
				strokeStyleSegment = strokeStyle.ref.get(ADDRESS, 0);
			}
			DrawRectangle.invokeExact(
				ref.get(ADDRESS, 0),
				rect.ref,
				brush.ref.get(ADDRESS, 0),
				strokeWidth,
				strokeStyleSegment
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle FillRectangle;

	public void FillRectangle(
			D2D1_RECT_F rect,
			ID2D1SolidColorBrush brush
		) {
		if (FillRectangle == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS,
				ADDRESS
			);
			FillRectangle = NativeUtils.LINKER.downcallHandle(findSymbol(17), descriptor);
		}
		try {
			FillRectangle.invokeExact(
				ref.get(ADDRESS, 0),
				rect.ref,
				brush.ref.get(ADDRESS, 0)
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle DrawRoundedRectangle;

	public void DrawRoundedRectangle(
			D2D1_ROUNDED_RECT roundedRect,
			ID2D1SolidColorBrush brush,
			float strokeWidth,
			ID2D1StrokeStyle strokeStyle
		) {
		if (DrawRoundedRectangle == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS,
				ADDRESS,
				JAVA_FLOAT,
				ADDRESS
			);
			DrawRoundedRectangle = NativeUtils.LINKER.downcallHandle(findSymbol(18), descriptor);
		}
		try {
			MemorySegment strokeStyleSegment;
			if (strokeStyle == null) {
				strokeStyleSegment = MemorySegment.NULL;
			} else {
				strokeStyleSegment = strokeStyle.ref.get(ADDRESS, 0);
			}
			DrawRoundedRectangle.invokeExact(
				ref.get(ADDRESS, 0),
				roundedRect.ref,
				brush.ref.get(ADDRESS, 0),
				strokeWidth,
				strokeStyleSegment
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle FillRoundedRectangle;

	public void FillRoundedRectangle(
			D2D1_ROUNDED_RECT roundedRect,
			ID2D1SolidColorBrush brush
		) {
		if (FillRoundedRectangle == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS,
				ADDRESS
			);
			FillRoundedRectangle = NativeUtils.LINKER.downcallHandle(findSymbol(19), descriptor);
		}
		try {
			FillRoundedRectangle.invokeExact(
				ref.get(ADDRESS, 0),
				roundedRect.ref,
				brush.ref.get(ADDRESS, 0)
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle DrawEllipse;

	public void DrawEllipse(
			D2D1_ELLIPSE ellipse,
			ID2D1SolidColorBrush brush,
			float strokeWidth
		) {
		if (DrawEllipse == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS,
				ADDRESS,
				JAVA_FLOAT,
				ADDRESS
			);
			DrawEllipse = NativeUtils.LINKER.downcallHandle(findSymbol(20), descriptor);
		}
		try {
			DrawEllipse.invokeExact(
				ref.get(ADDRESS, 0),
				ellipse.ref,
				brush.ref.get(ADDRESS, 0),
				strokeWidth,
				MemorySegment.NULL
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle FillEllipse;

	public void FillEllipse(
			D2D1_ELLIPSE ellipse,
			ID2D1SolidColorBrush brush
		) {
		if (FillEllipse == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS,
				ADDRESS
			);
			FillEllipse = NativeUtils.LINKER.downcallHandle(findSymbol(21), descriptor);
		}
		try {
			FillEllipse.invokeExact(
				ref.get(ADDRESS, 0),
				ellipse.ref,
				brush.ref.get(ADDRESS, 0)
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle EndDraw;

	public int EndDraw() {
		if (EndDraw == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				ADDRESS,
				ADDRESS
			);
			EndDraw = NativeUtils.LINKER.downcallHandle(findSymbol(49), descriptor);
		}
		try {
			return (int) EndDraw.invokeExact(
				ref.get(ADDRESS, 0),
				MemorySegment.NULL,
				MemorySegment.NULL
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle Resize;

	public int Resize(D2D1_SIZE_U size) {
		if (Resize == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.of(
				JAVA_INT,
				ADDRESS,
				ADDRESS
			);
			Resize = NativeUtils.LINKER.downcallHandle(findSymbol(58), descriptor);
		}
		try {
			return (int) Resize.invokeExact(
				ref.get(ADDRESS, 0),
				size.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle DrawGeometry;

	public void DrawGeometry(
	        ID2D1PathGeometry geometry,
	        ID2D1SolidColorBrush brush,
	        float strokeWidth,
	        ID2D1StrokeStyle strokeStyle
		) {
		if (DrawGeometry == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS,
				ADDRESS,
				JAVA_FLOAT,
				ADDRESS
			);
			DrawGeometry = NativeUtils.LINKER.downcallHandle(findSymbol(22), descriptor);
		}
		try {
			MemorySegment strokeStyleSegment;
			if (strokeStyle == null) {
				strokeStyleSegment = MemorySegment.NULL;
			} else {
				strokeStyleSegment = strokeStyle.ref.get(ADDRESS, 0);
			}
			DrawGeometry.invokeExact(
				ref.get(ADDRESS, 0),
				geometry.ref.get(ADDRESS, 0),
				brush.ref.get(ADDRESS, 0),
				strokeWidth,
				strokeStyleSegment
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle FillGeometry;

	public void FillGeometry(
	        ID2D1PathGeometry geometry,
	        ID2D1SolidColorBrush brush
		) {
		if (FillGeometry == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS,
				ADDRESS,
				ADDRESS
			);
			FillGeometry = NativeUtils.LINKER.downcallHandle(findSymbol(23), descriptor);
		}
		try {
			FillGeometry.invokeExact(
				ref.get(ADDRESS, 0),
				geometry.ref.get(ADDRESS, 0),
				brush.ref.get(ADDRESS, 0),
				MemorySegment.NULL
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle DrawText;

	public void DrawText(
			WCHAR string,
			int stringLength,
			IDWriteTextFormat textFormat,
			D2D1_RECT_F layoutRect,
			ID2D1SolidColorBrush defaultFillBrush,
			D2D1_DRAW_TEXT_OPTIONS options,
			DWRITE_MEASURING_MODE measuringMode
		) {
		if (DrawText == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS,
				JAVA_INT,
				ADDRESS,
				ADDRESS,
				ADDRESS,
				JAVA_INT,
				JAVA_INT
			);
			DrawText = NativeUtils.LINKER.downcallHandle(findSymbol(27), descriptor);
		}
		try {
			DrawText.invokeExact(
				ref.get(ADDRESS, 0),
				string.ref,
				stringLength,
				textFormat.ref.get(ADDRESS, 0),
				layoutRect.ref,
				defaultFillBrush.ref.get(ADDRESS, 0),
				options.value,
				measuringMode.value
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle DrawTextLayout;

	public void DrawTextLayout(
			D2D1_POINT_2F origin,
			IDWriteTextLayout textLayout,
			ID2D1SolidColorBrush defaultFillBrush,
			D2D1_DRAW_TEXT_OPTIONS options
		) {
		if (DrawTextLayout == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				D2D1_POINT_2F.LAYOUT,
				ADDRESS,
				ADDRESS,
				JAVA_INT
			);
			DrawTextLayout = NativeUtils.LINKER.downcallHandle(findSymbol(28), descriptor);
		}
		try {
			DrawTextLayout.invokeExact(
				ref.get(ADDRESS, 0),
				origin.ref,
				textLayout.ref.get(ADDRESS, 0),
				defaultFillBrush.ref.get(ADDRESS, 0),
				options.value
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle SetTransform;

	public void SetTransform(
			D2D1_MATRIX_3X2_F transform
		) {
		if (SetTransform == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				ADDRESS
			);
			SetTransform = NativeUtils.LINKER.downcallHandle(findSymbol(30), descriptor);
		}
		try {
			SetTransform.invokeExact(
				ref.get(ADDRESS, 0),
				transform.ref
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static MethodHandle SetAntialiasMode;

	public void SetAntialiasMode(
			D2D1_ANTIALIAS_MODE antialiasMode
		) {
		if (SetAntialiasMode == null) {
			FunctionDescriptor descriptor = FunctionDescriptor.ofVoid(
				ADDRESS,
				JAVA_INT
			);
			SetAntialiasMode = NativeUtils.LINKER.downcallHandle(findSymbol(32), descriptor);
		}
		try {
			SetAntialiasMode.invokeExact(
				ref.get(ADDRESS, 0),
				antialiasMode.value
			);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
