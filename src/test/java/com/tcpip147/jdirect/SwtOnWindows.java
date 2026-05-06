package com.tcpip147.jdirect;

import org.eclipse.swt.SWT;
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

		shell.open();

		//

		JDGraphics g = JDGraphics.create(canvas.handle, 3 * 1024);

		canvas.addPaintListener(_ -> {
			g.beginDraw();

			g.clear("#F5F5F5");

			g.translate(0.5f, 0.5f);

			g.setColor("#FF0000");
			g.drawRect(10, 10, 100, 100);

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

		try {
			g.close();
		} catch (Exception e1) {
		}

		display.dispose();
	}
}
