package com.tylerpackard.tools;

import com.tylerpackard.edits.DrawEdit;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * The Eraser is a tool that removes the pixels where clicked or dragged. The keyboard shortcut is the 'E' key.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public class Eraser extends Tool{

	/**
	 * The hexadecimal value of a color that's all alpha.
	 */
	private final int fullAlpha = 0x1010101;


	/**
	 * Creates a new Eraser, loads the images, and sets the shortcut.
	 *
	 * @param parent The containing ToolChooser
	 * @param x The X position of the button
	 * @param y The Y position of the button
	 */
	public Eraser(ToolChooser parent, int x, int y) {
		super(parent, x, y);
		icon = loadImage("eraser");
		mouse = loadImage("eraser mouse");
		parent.getParent().getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("E"), "select-eraser");
		parent.getParent().getActionMap().put("select-eraser", new Shortcut(this));
	}


	/**
	 * Erases the pixel where clicked and adds it to an edit.
	 *
	 * @param x The X position of the click
	 * @param y The Y position of the click
	 * @param image The image to edit
	 * @param zoom How far the image is zoomed in
	 * @param newEdit Whether or not to make a new edit
	 */
	@Override
	public void clicked(int x, int y, BufferedImage image, int zoom, boolean newEdit) {
		DrawEdit edit = new DrawEdit(this, image);
		edit.addChange(x / zoom, y / zoom, image.getRGB(x / zoom, y / zoom), fullAlpha);
		parent.getEditManager().push(edit);
		newEdit = false;
	}

	/**
	 * Erases the pixels where dragged and adds it to an edit.
	 *
	 * @param e The mouse event
	 * @param x The X position of the drag
	 * @param y The Y position of the drag
	 * @param image The image to edit
	 * @param zoom How far the image is zoomed in (Needed to scale mouse event)
	 */
	@Override
	public void dragged(MouseEvent e, int x, int y, BufferedImage image, int zoom) {
		drawLine(e, x, y, image, fullAlpha, zoom);
	}
}
