package com.tylerpackard.tools;

import com.tylerpackard.edits.DrawEdit;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * The Tool is an abstract object that serves as a groundwork for tools that the user can use to edit the image, such
 * as a pencil or eraser. The tool itself is a button that selects itself when clicked, and receives click and drag
 * events from the Canvas if it is currently selected. Each tool has an button icon and mouse icon. The Tool comes
 * with predefined methods to load images, draw the mouse, and draw lines.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 * @see ToolChooser
 */
public abstract class Tool extends JPanel implements MouseListener{

	/**
	 * The ToolChooser that contains this tool
	 */
	ToolChooser parent;

	/**
	 * The icon to draw on the button
	 */
	BufferedImage icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

	/**
	 * The icon to use for the mouse
	 */
	BufferedImage mouse;

	/**
	 * The default button background
	 */
	final BufferedImage defaultBG;

	/**
	 * The button background when selected or hovered over
	 */
	final BufferedImage selectedBG;

	/**
	 * Whether or not this tool is selected
	 */
	private boolean selected;

	/**
	 * Whether or not the button is being hovered over
	 */
	private boolean hover;


	/**
	 * Assigns the parent ToolChooser and loads the button backgrounds.
	 *
	 * @param parent The ToolChooser that contains this tool
	 * @param x The X position of the button
	 * @param y The Y position of the button
	 */
	public Tool(ToolChooser parent, int x, int y) {
		this.parent = parent;
		defaultBG = loadImage("defaultBG");
		selectedBG = loadImage("selectedBG");
		addMouseListener(this);
		setBounds(x, y, 32, 32);
	}


	/**
	 * Loads an image from the images directory in the Tools package. If the display supports retina resolution images
	 * and a retina resolution version of the image is found, it is loaded instead.
	 *
	 * @param imageName The name of the image to be loaded
	 * @return The image with the given name
	 */
	BufferedImage loadImage(String imageName) {
		if (parent.getParent().hasRetina && getClass().getResourceAsStream("images/" + imageName + "@2x.png") != null) {
			imageName += "@2x";
		}
		try {
			return ImageIO.read(getClass().getResourceAsStream("images/" + imageName + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}
	}

	/**
	 * Returns the button's background image. If the the button is selected or being hovered over, it returns the hover
	 * background, else it returns the default background.
	 *
	 * @return The button's background image
	 */
	public BufferedImage getBG() {
		if (hover || selected) {
			return selectedBG;
		}
		return defaultBG;
	}

	/**
	 * Returns the button icon.
	 *
	 * @return The button icon
	 */
	public BufferedImage getIcon() {
		return icon;
	}

	/**
	 * Selects the tool's button
	 */
	public void select() {
		selected = true;
	}

	/**
	 * Deselects the tool's button
	 */
	public void deselect() {
		selected = false;
	}

	/**
	 * Tells the parent ToolChooser to make this tool the selected tool when clicked.
	 *
	 * @param e The clicking event
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		parent.requestSelection(this);
		selected = true;
		parent.repaint();
	}

	/**
	 * Occurs when the mouse is pressed.
	 *
	 * @param e The pressing event
	 */
	@Override
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Occurs when the mouse is released.
	 *
	 * @param e The releasing event
	 */
	@Override
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * Notifies the button that the mouse is hovering over it.
	 *
	 * @param e The entering event
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		parent.repaint();
		hover = true;
	}

	/**
	 * Notifies the button when the mouse stops hovering over it.
	 *
	 * @param e The exiting event
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		parent.repaint();
		hover = false;
	}

	/**
	 * Occurs when the Canvas is clicked. Should be overridden unless the tool shouldn't do anything on a click.
	 *
	 * @param x The X position of the click
	 * @param y The Y position of the click
	 * @param image The image to edit
	 * @param rgb The color to use
	 * @param newEdit Whether or not to make a new edit
	 */
	public void clicked(int x, int y, BufferedImage image, int rgb, boolean newEdit) {}

	/**
	 * Occurs when the mouse is dragged over the Canvas. Should be overridden unless the tool shouldn't do anything
	 * when dragged.
	 *
	 * @param e The mouse event
	 * @param x2 The last X position of the mouse
	 * @param y2 The last Y position of the mouse
	 * @param image The image to edit
	 * @param zoom How far the image is zoomed in (Needed to scale mouse event)
	 */
	public void dragged(MouseEvent e, int x2, int y2, BufferedImage image, int zoom) {}

	/**
	 * This method uses the Bresenham algorithm to draw a line from the mouse's last position, to its current position.
	 * To make points, it appends to a new edit.
	 *
	 * @param e The mouse event
	 * @param x2 The last X position of the mouse
	 * @param y2 The last Y position of the mouse
	 * @param image The image to edit
	 * @param rgb The color to use
	 * @param zoom How far the image is zoomed in (Needed to scale mouse event)
	 */
	public void drawLine(MouseEvent e, int x2, int y2, BufferedImage image, int rgb, int zoom) {
		DrawEdit edit = (DrawEdit) parent.getEditManager().peek();
		int x = e.getX() / zoom;
		int y= e.getY() / zoom;
		x2 /= zoom;
		y2 /= zoom;
		int width = x2 - x;
		int height = y2 - y;
		int longest = Math.abs(width);
		int shortest = Math.abs(height);
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		boolean wasIn = false;

		if (width < 0) {
			dx1 = -1;
			dx2 = -1;
		} else if (width > 0) {
			dx1 = 1;
			dx2 = 1;
		}

		if (height < 0) {
			dy1 = -1;
		} else if (height > 0) {
			dy1 = 1;
		}

		if (longest <= shortest) {
			longest = Math.abs(height);
			shortest = Math.abs(width);
			if (height < 0) {
				dy2 = -1;
			} else if (height > 0) {
				dy2 = 1;
			}
			dx2 = 0;
		}

		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			wasIn = drawPoint(x, y, wasIn, image, rgb, edit);
			numerator += shortest;
			if (numerator >= longest) {
				numerator -= longest;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
	}

	/**
	 * Draws a point of a line if it's legal. Only called by the drawLine method.
	 *
	 * @param x The X position of the drawing
	 * @param y The Y position of the drawing
	 * @param wasIn Whether or not the last point drawn was inside the bounds
	 * @param image The image to edit
	 * @param rgb The color to use
	 */
	private boolean drawPoint(int x, int y, boolean wasIn, BufferedImage image, int rgb, DrawEdit edit) {
		if (x < 0) {
			if (wasIn) {
				edit.addChange(0, y, image.getRGB(0, y), rgb);
				wasIn = false;
			}
		} else if (x >= image.getWidth()) {
			if (wasIn) {
				edit.addChange(image.getWidth() - 1, y, image.getRGB(image.getWidth() - 1, y), rgb);
				wasIn = false;
			}
		} else if (y < 0) {
			if (wasIn) {
				edit.addChange(x, 0, image.getRGB(x, 0), rgb);
				wasIn = false;
			}
		} else if (y >= image.getHeight()) {
			if (wasIn) {
				edit.addChange(x, image.getHeight() - 1, image.getRGB(x, image.getHeight() - 1), rgb);
				wasIn = false;
			}
		} else {
			edit.addChange(x, y, image.getRGB(x, y), rgb);
			wasIn = true;
		}
		return wasIn;
	}

	/**
	 * An action that tells the parent ToolChooser to select this tool
	 * @see ToolChooser
	 */
	class Shortcut extends AbstractAction {
		private final Tool parent;

		public Shortcut(Tool parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			parent.parent.requestSelection(parent);
		}
	}

	/**
	 * Draws the mouse over the ImageHolder.
	 *
	 * @param g The Graphics to paint on
	 * @param x The X position of the mouse
	 * @param y The Y position of the mouse
	 */
	public void drawMouse(Graphics g, int x, int y) {
		g.drawImage(mouse, x, y - 31, 32, 32, null);
	}
}
