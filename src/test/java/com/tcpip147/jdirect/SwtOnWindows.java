package com.tcpip147.jdirect;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.tcpip147.jdirect.ffm.NativeUtils;
import com.tcpip147.jdirect.ffm.coms.ID2D1Factory;
import com.tcpip147.jdirect.ffm.dll.D2d1;
import com.tcpip147.jdirect.ffm.dll.User32;
import com.tcpip147.jdirect.ffm.enums.D2D1_FACTORY_TYPE;
import com.tcpip147.jdirect.ffm.structs.D2D1_PIXEL_FORMAT;
import com.tcpip147.jdirect.ffm.structs.D2D1_RENDER_TARGET_PROPERTIES;
import com.tcpip147.jdirect.ffm.structs.D2D1_SIZE_U;
import com.tcpip147.jdirect.ffm.structs.LPRECT;

public class SwtOnWindows {

	public static void main(String[] args) {

		// 1. OS와의 연결을 담당하는 Display 생성
		Display display = new Display();

		// 2. 창(Window) 역할을 하는 Shell 생성
		Shell shell = new Shell(display);
		shell.setText("SWT 2026 Example");
		shell.setSize(800, 600);
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		// 3. 위젯 추가
		Label label = new Label(shell, SWT.CENTER);
		label.setText("Hello, SWT World!");

		Canvas canvas = new Canvas(shell, SWT.CENTER);
		canvas.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		// 4. 창 열기
		shell.open();

		MemorySegment hwnd = NativeUtils.hwndOf(canvas.handle);

		try (Arena arena = Arena.ofConfined(); ID2D1Factory factory = new ID2D1Factory(arena)) {
			int hr = D2d1.D2D1CreateFactory(D2D1_FACTORY_TYPE.D2D1_FACTORY_TYPE_SINGLE_THREADED, factory);
			if (hr == 0) {
				LPRECT rect = new LPRECT(arena);
				boolean ok = User32.GetClientRect(hwnd, rect);
				if (ok) {
					D2D1_SIZE_U size = new D2D1_SIZE_U(arena);
					size.setWidth(rect.getRight() - rect.getLeft());
					size.setHeight(rect.getBottom() - rect.getTop());

					D2D1_RENDER_TARGET_PROPERTIES props = new D2D1_RENDER_TARGET_PROPERTIES(arena);
					D2D1_PIXEL_FORMAT format = new D2D1_PIXEL_FORMAT(arena);
					props.setPixelFormat(format);


				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 5. 이벤트 루프: 창이 닫힐 때까지 대기
		// 이 루프가 없으면 프로그램이 즉시 종료됩니다.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		// 6. 리소스 해제
		display.dispose();
	}
}
