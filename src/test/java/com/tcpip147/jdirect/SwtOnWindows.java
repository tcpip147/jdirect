package com.tcpip147.jdirect;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SwtOnWindows {

	public static void main(String[] args) {
		Display display = new Display();

		Shell shell = new Shell(display);
		shell.setText("SwtOnWindows");
		shell.setSize(800, 600);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		shell.setLayout(layout);

		Composite header = new Composite(shell, SWT.NONE);
		header.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		header.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		GridData gridData = (GridData) header.getLayoutData();
		gridData.heightHint = 30;

		Canvas canvas = new Canvas(shell, SWT.BACKGROUND);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Color color = canvas.getBackground();
		float[] background = new float[] { color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f,
				color.getAlpha() / 255f };

		shell.open();

		//

		JDirect.init();
		JDFont font1 = new JDFont("맑은 고딕", JDFont.WEIGHT_NORMAL, JDFont.STYLE_NORMAL, JDFont.STRETCH_NORMAL, 14);
		JDirect.registryFont(font1);

		JDText text1 = new JDText(font1, "안녕하세요");
		JDirect.registryText(text1);

		JDGraphics g = JDirect.createGraphics(canvas.handle, 3 * 1024);

		JDText text2 = new JDText(font1, "안녕하세요22", 350, 200);
		g.registryText(text2);
		g.setAntialiasMode(true);

		canvas.addPaintListener(e -> {
			g.beginDraw();

			g.clear(background[0], 1, background[2], background[3]);

			g.setColor(0, 0, 1, 1);
			g.drawRectangle(10.5f, 10.5f, 100, 100);

			g.drawRoundedRectangle(120.5f, 10.5f, 100, 100, 10, 10);

			g.drawCircle(280.5f, 60.5f, 50, 50);

			g.setColor(1, 0, 0.9f, background[3]);
			g.drawText(10, 10, text2);

			g.endDraw();
		});

		canvas.addListener(SWT.Resize, e -> {
			g.resize();
		});

		canvas.redraw();

		//

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		g.close();

		JDirect.close();

		display.dispose();
	}
}
