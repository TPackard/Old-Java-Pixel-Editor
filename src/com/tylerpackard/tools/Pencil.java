package com.tylerpackard.tools;

import com.tylerpackard.edits.DrawEdit;
import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * The Pencil Tool allows the user to draw lines on the image, and uses the selected color from the ColorChooser. Its
 * keyboard shortcut is the 'W' key.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public class Pencil extends Tool{

	/**
	 * The ColorChooser from which to get the color to draw with.
	 */
	private ColorChooser colorChooser;

	/**
	 * The X position of the previous click.
	 */
	private int prevX;

	/**
	 * The Y position of the previous click.
	 */
	private int prevY;


	/**
	 * Creates a new Pencil, sets the ColorChooser and images, and sets the shortcut.
	 *
	 * @param parent The containing ToolChooser
	 * @param x The X position of the button
	 * @param y The Y position of the button
	 * @param colorChooser The ColorChooser from which to get the color to draw with.
	 */
	public Pencil(ToolChooser parent, int x, int y, ColorChooser colorChooser) {
		super(parent, x, y);
		this.colorChooser = colorChooser;
		icon = loadImage("pencil");
		mouse = loadImage("pencil mouse");
		parent.getParent().getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "select-pencil");
		parent.getParent().getActionMap().put("select-pencil", new Shortcut(this));
	}


	/**
	 * Draws on the pixel where clicked with the selected color of the ColorChooser and adds it to a new edit.
	 *
	 * @param e The mouse clicking event
	 * @param image The image to edit
	 * @param zoom How far the image is zoomed in
	 */
	@Override
	public void clicked(MouseEvent e, BufferedImage image, int zoom) {
		DrawEdit edit = new DrawEdit(this, image);
		parent.getEditManager().push(edit);

		if (e.isShiftDown()) {
			drawLine(e, prevX, prevY, image, colorChooser.getColor().getRGB(), zoom);
		} else {
			int x = e.getX() / zoom;
			int y = e.getY() / zoom;
			edit.addChange(x, y, image.getRGB(x, y), colorChooser.getColor().getRGB());
		}

		prevX = e.getX();
		prevY = e.getY();
	}

	/**
	 * Draws on the pixels where dragged with the selected color of the ColorChooser and adds it to an edit.
	 *
	 * @param e The mouse event
	 * @param x The X position of the drag
	 * @param y The Y position of the drag
	 * @param image The image to edit
	 * @param zoom How far the image is zoomed in (Needed to scale mouse event)
	 */
	@Override
	public void dragged(MouseEvent e, int x, int y, BufferedImage image, int zoom) {
		prevX = e.getX();
		prevY = e.getY();
		drawLine(e, x, y, image, colorChooser.getColor().getRGB(), zoom);
	}

	/**
	 * Draws the mouse icon at the location of the user's mouse and sets the color of the tip of the icon to the color
	 * selected in the ColorChooser.
	 *
	 * @param g The Graphics to paint on
	 * @param x The X position of the mouse
	 * @param y The Y position of the mouse
	 */
	@Override
	public void drawMouse(Graphics g, int x, int y) {
		g.setColor(colorChooser.getColor());
		g.fillRect(x + 1, y - 3, 3, 3);
		g.drawImage(mouse, x, y - 31, 32, 32, null);
	}
}
