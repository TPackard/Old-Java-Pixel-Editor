package com.tylerpackard.tools;

import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ColorPicker extends Tool {
	private ColorChooser colorChooser;


	public ColorPicker(ToolChooser parent, int x, int y, ColorChooser colorChooser) {
		super(parent, x, y);
		this.colorChooser = colorChooser;
		icon = loadImage("picker");
	}


	@Override
	public void clicked(int x, int y, Graphics g, BufferedImage image, int zoom) {
		colorChooser.setColor(new Color(image.getRGB(x / zoom, y / zoom)));
	}

	@Override
	public void dragged(MouseEvent e, int x, int y, Graphics g, int zoom) {
		// Do nothing
	}

	@Override
	public Color getColor() {
		return noColor;
	}
}
