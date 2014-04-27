package com.tylerpackard.tools;

import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * The ColorPicker is a tool that detects the color of the pixel clicked on. It can be selected by pressing 'Q' or
 * temporarily selected by holding down the Command key.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public class ColorPicker extends Tool {

	/**
	 * The ColorChooser to set the color of.
	 */
	private ColorChooser colorChooser;


	/**
	 * Creates a new ColorPicker, loads the images, and sets the keyboard shortcuts.
	 *
	 * @param parent The containing ToolChooser
	 * @param x The X position to put the button
	 * @param y The Y position to put the button
	 * @param colorChooser The ColorChooser to set the color of
	 */
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


	/**
	 * Sets the color of the ColorChooser to the color of the pixel clicked on.
	 *
	 * @param x The X position of the click
	 * @param y The Y position of the click
	 * @param image The image to edit
	 * @param zoom How much the image is zoomed in
	 * @param newEdit Whether or not to make a new edit
	 */
	@Override
	public void clicked(int x, int y, BufferedImage image, int zoom, boolean newEdit) {
		colorChooser.setColor(new Color(image.getRGB(x / zoom, y / zoom)));
	}


	/**
	 * An action that tells the parent ToolChooser to temporarily select the color picker.
	 */
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
