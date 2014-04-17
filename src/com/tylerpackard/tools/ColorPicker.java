package com.tylerpackard.tools;

import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ColorPicker extends Tool {
	private ColorChooser colorChooser;


	public ColorPicker(ToolChooser parent, int x, int y, ColorChooser colorChooser) {
		super(parent, x, y);
		this.colorChooser = colorChooser;
		icon = loadImage("picker");
		mouse = loadImage("picker mouse");
		parent.getParent().getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("Q"), "select-picker");
		parent.getParent().getActionMap().put("select-picker", new Shortcut(this));
		parent.getParent().getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_META, InputEvent.META_DOWN_MASK, false), "temp-picker");
		parent.getParent().getActionMap().put("temp-picker", new SetTempPicker(this, true));
		parent.getParent().getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_META, 0, true), "restore-tool");
		parent.getParent().getActionMap().put("restore-tool", new SetTempPicker(this, false));
	}


	@Override
	public void clicked(int x, int y, Graphics g, BufferedImage image, int zoom) {
		colorChooser.setColor(new Color(image.getRGB(x / zoom, y / zoom)));
	}

	@Override
	public void dragged(MouseEvent e, int x, int y, Graphics g, int zoom) {
		// Do nothing
	}

	private class SetTempPicker extends AbstractAction {
		private ColorPicker parent;
		private boolean state;

		public SetTempPicker(ColorPicker parent, boolean state) {
			this.parent = parent;
			this.state = state;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			parent.parent.setTempPicker(state);
		}
	}
}
