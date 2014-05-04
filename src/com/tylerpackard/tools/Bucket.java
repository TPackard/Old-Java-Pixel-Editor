package com.tylerpackard.tools;

import com.tylerpackard.edits.DrawEdit;
import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * The Bucket Tool allows an area of a uniform color to be completely replaced by another color. The Bucket tool only
 * acts on a click, but not on a drag. It fills an area with a color by using the flood fill algorithm. The Bucket's
 * keyboard shortcut is 'A'.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public class Bucket extends Tool {
	/**
	 * The color chooser to get the fill color from
	 */
	private ColorChooser colorChooser;


	/**
	 * Creates a new bucket tool selector at the specified location, loads its icons, and sets the keyboard shortcuts.
	 *
	 * @param parent The ToolChooser that contains this Bucket tool
	 * @param x The selection button's X position
	 * @param y The selection button's Y position
	 * @param colorChooser The color chooser to get the fill color from
	 */
	public Bucket(ToolChooser parent, int x, int y, ColorChooser colorChooser) {
		super(parent, x, y);
		this.colorChooser = colorChooser;
		icon = loadImage("bucket");
		mouse = loadImage("bucket mouse");
		parent.getParent().getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "select-bucket");
		parent.getParent().getActionMap().put("select-bucket", new Shortcut(this));
	}


	/**
	 * Fills the clicked area with the color chooser's current color. It uses the flood fill algorithm to do this, and
	 * stores points in a linked-list instead of using recursion to prevent stack overflow errors when filling large
	 * sections.
	 *
	 * @param x X position of the click
	 * @param y Y position of the click
	 * @param image The image being edited
	 * @param zoom How far the image has been zoomed in
	 * @param newEdit Whether or not to make a new edit - Unused, this will always make a new edit
	 */
	@Override
	public void clicked(int x, int y, BufferedImage image, int zoom, boolean newEdit) {
		if (colorChooser.getColor().getRGB() == image.getRGB(x / zoom, y / zoom)) {
			return;
		}

		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int targetRGB = image.getRGB(x / zoom, y / zoom);
		int newRGB = colorChooser.getColor().getRGB();
		DrawEdit edit = new DrawEdit(this, image);

		LinkedList<Point> points = new LinkedList<>();
		points.add(new Point(x / zoom, y / zoom));

		while (!points.isEmpty()) {
			Point p = points.remove();
			if (p.x >= 0 && p.y >= 0 && p.x < imageWidth && p.y < imageHeight) {
				if (targetRGB == image.getRGB(p.x, p.y)) {
					edit.addChange(p.x, p.y, targetRGB, newRGB);
					points.add(new Point(p.x + 1, p.y));
					points.add(new Point(p.x - 1, p.y));
					points.add(new Point(p.x, p.y + 1));
					points.add(new Point(p.x, p.y - 1));
				}
			}
		}

		parent.getEditManager().push(edit);
	}

	/**
	 * Draws the mouse when the cursor is inside the image holder
	 *
	 * @param g Graphics to paint on
	 * @param x X position of the mouse
	 * @param y Y position of the mouse
	 */
	@Override
	public void drawMouse(Graphics g, int x, int y) {
		g.setColor(colorChooser.getColor());
		g.fillRect(x, y - 7, 2, 4);
		g.fillRect(x + 2, y - 8, 14, 4);
		g.fillRect(x + 4, y - 5, 5, 3);
		g.fillRect(x + 5, y - 10, 10, 2);
		g.fillRect(x + 8, y - 12, 6, 2);
		g.fillRect(x + 4, y - 9, 1, 1);
		g.drawImage(mouse, x - 1, y - 27, 32, 32, null);
	}
}
