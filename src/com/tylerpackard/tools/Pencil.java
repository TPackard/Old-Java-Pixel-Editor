package com.tylerpackard.tools;

import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Pencil extends Tool{
	private ColorChooser colorChooser;


	public Pencil(ToolChooser parent, int x, int y, ColorChooser colorChooser) {
		super(parent, x, y);
		this.colorChooser = colorChooser;
		icon = loadImage("pencil");
		mouse = loadImage("pencil mouse");
		parent.getParent().getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "select-pencil");
		parent.getParent().getActionMap().put("select-pencil", new Shortcut(this));
	}


	@Override
	public void clicked(int x, int y, Graphics g, BufferedImage image, int zoom) {
		g.setColor(colorChooser.getColor());
		super.clicked(x, y, g, image, zoom);
	}

	@Override
	public void dragged(MouseEvent e, int x, int y, Graphics g, int zoom) {
		g.setColor(colorChooser.getColor());
		super.dragged(e, x, y, g, zoom);
	}

	@Override
	public void drawMouse(Graphics g, int x, int y) {
		g.setColor(colorChooser.getColor());
		g.fillRect(x + 1, y - 3, 3, 3);
		g.drawImage(mouse, x, y - 31, 32, 32, null);
	}
}
