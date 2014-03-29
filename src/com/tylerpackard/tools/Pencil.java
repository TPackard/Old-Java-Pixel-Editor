package com.tylerpackard.tools;

import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Pencil extends Tool{
	private ColorChooser colorChooser;


	public Pencil(ToolChooser parent, int x, int y, ColorChooser colorChooser) {
		super(parent, x, y);
		this.colorChooser = colorChooser;
		icon = loadImage("pencil.png");
	}


	@Override
	public void clicked(int x, int y, Graphics g, int zoom) {
		g.setColor(colorChooser.getColor());
		super.clicked(x, y, g, zoom);
	}

	@Override
	public void dragged(MouseEvent e, int x, int y, Graphics g, int zoom) {
		g.setColor(colorChooser.getColor());
		super.dragged(e, x, y, g, zoom);
	}

	@Override
	public Color getColor() {
		return colorChooser.getColor();
	}
}
