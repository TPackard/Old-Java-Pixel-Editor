package com.tylerpackard.tools;

import com.tylerpackard.canvas.Canvas;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Eraser extends Tool{
	private final Color color = new Color(0, true);


	public Eraser(ToolChooser parent, int x, int y) {
		super(parent, x, y);
		icon = loadImage("eraser.png");
	}


	@Override
	public void clicked(int x, int y, Graphics graphics, int zoom) {
		Graphics2D g = (Graphics2D) graphics;
		g.setComposite(AlphaComposite.Clear);
		super.clicked(x, y, g, zoom);
		g.setComposite(AlphaComposite.SrcOver);
	}

	@Override
	public void dragged(MouseEvent e, int x, int y, Graphics graphics, int zoom) {
		Graphics2D g = (Graphics2D) graphics;
		g.setComposite(AlphaComposite.Clear);
		super.dragged(e, x, y, g, zoom);
		g.setComposite(AlphaComposite.SrcOver);
	}

	@Override
	public Color getColor() {
		return color;
	}
}
