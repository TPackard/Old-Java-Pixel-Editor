package com.tylerpackard.tools;

import com.tylerpackard.edits.DrawEdit;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Eraser extends Tool{
	private final int fullAlpha = 0x1010101;


	public Eraser(ToolChooser parent, int x, int y) {
		super(parent, x, y);
		icon = loadImage("eraser");
		mouse = loadImage("eraser mouse");
		parent.getParent().getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("E"), "select-eraser");
		parent.getParent().getActionMap().put("select-eraser", new Shortcut(this));
	}


	@Override
	public void clicked(int x, int y, BufferedImage image, int zoom, boolean newEdit) {
		DrawEdit edit = new DrawEdit(this, image);
		edit.addChange(x / zoom, y / zoom, image.getRGB(x / zoom, y / zoom), fullAlpha);
		parent.getEditManager().push(edit);
	}

	@Override
	public void dragged(MouseEvent e, int x, int y, BufferedImage image, int zoom) {
		drawLine(e, x, y, image, fullAlpha, zoom);
	}
}
