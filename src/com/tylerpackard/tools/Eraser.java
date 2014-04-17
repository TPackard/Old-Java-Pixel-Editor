package com.tylerpackard.tools;

import com.tylerpackard.canvas.Canvas;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Eraser extends Tool{
	public Eraser(ToolChooser parent, int x, int y) {
		super(parent, x, y);
		icon = loadImage("eraser");
		mouse = loadImage("eraser mouse");
		parent.getParent().getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("E"), "select-eraser");
		parent.getParent().getActionMap().put("select-eraser", new Shortcut(this));
	}


	@Override
	public void clicked(int x, int y, Graphics graphics, BufferedImage image, int zoom) {
		Graphics2D g = (Graphics2D) graphics;
		g.setComposite(AlphaComposite.Clear);
		super.clicked(x, y, g, image, zoom);
		g.setComposite(AlphaComposite.SrcOver);
	}

	@Override
	public void dragged(MouseEvent e, int x, int y, Graphics graphics, int zoom) {
		Graphics2D g = (Graphics2D) graphics;
		g.setComposite(AlphaComposite.Clear);
		super.dragged(e, x, y, g, zoom);
		g.setComposite(AlphaComposite.SrcOver);
	}
}
