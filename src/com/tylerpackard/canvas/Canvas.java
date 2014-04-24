package com.tylerpackard.canvas;

import com.tylerpackard.toolbox.toolchooser.ToolChooser;
import com.tylerpackard.ui.Updatable;
import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * The Canvas holds the image that's being edited, although the image is stored in a child ImageHolder. The Canvas
 * allows the ImageHolder to be scrolled in any direction if it is larger than the Canvas.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 * @see ImageHolder
 */
public class Canvas extends JPanel implements Updatable, MouseWheelListener {

	/**
	 * The ImageHolder contained by the Canvas. The Canvas can get and set the image contained by the ImageHolder and
	 * can scroll it.
	 */
	private final ImageHolder imageHolder;

	/**
	 * The Window which contains the Canvas.
	 */
	private final Window parent;

	/**
	 * The ToolChooser which contains the tools to use on the Canvas.
	 */
	private ToolChooser toolChooser;

	/**
	 * How much the child ImageHolder has been scrolled horizontally.
	 *
	 * @see ImageHolder
	 */
	private int scrollX = 0;

	/**
	 * How much the child ImageHolder has been scrolled vertically.
	 *
	 * @see ImageHolder
	 */
	private int scrollY = 0;

	/**
	 * The width of the image scaled by the scaled zoom factor
	 *
	 * @see ImageHolder#scaledZoom
	 */
	private int imageWidth;

	/**
	 * The height of the image scaled by the scaled zoom factor
	 *
	 * @see ImageHolder#scaledZoom
	 */
	private int imageHeight;

	/**
	 * Stores whether or not the mouse is inside the ImageHolder
	 */
	private boolean mouseInBounds;


	/**
	 * Sets the parent Window and ToolChooser to get tools from, and sets itself up.
	 *
	 * @param parent The Window containing it
	 * @param toolChooser The ToolChooser to get tools from
	 */
	public Canvas(Window parent, ToolChooser toolChooser) {
		this.parent = parent;
		this.toolChooser = toolChooser;
		setLayout(null);
		setBounds(parent.getLeftWidth(), 0, 1, 1);
		setBackground(new Color(0x55555A));
		addMouseWheelListener(this);
		setFocusable(true);
		imageHolder = new ImageHolder(this);
		add(imageHolder);
		setImagePos();
	}

	/**
	 * Unused method
	 */
	@Override
	public void update() {}

	/**
	 * Unused method
	 */
	@Override
	public void defocus() {

	}

	/**
	 * Returns the image being edited
	 *
	 * @return The image being edited
	 */
	public BufferedImage getImage() {
		return imageHolder.getImage();
	}

	/**
	 * Sets the image being edited
	 *
	 * @param image The new image to be edited
	 */
	public void setImage(BufferedImage image) {
		imageHolder.setImage(image);
	}

	/**
	 * Sets the zoom level of the image holder
	 *
	 * @param zoomFactor How much to be zoomed in
	 * @see ImageHolder#setZoom(int)
	 */
	public void setZoom(int zoomFactor) {
		imageHolder.setZoom(zoomFactor);
	}

	/**
	 * Repositions the Canvas inside the parent Window when the parent Window is resized. The Canvas spans between the
	 * left and right toolbars, and occupies the entire height of the Window.
	 *
	 * @see Window#reposition()
	 */
	@Override
	public void reposition() {
		setSize(parent.width() - parent.getLeftWidth() - parent.getRightWidth(), parent.height());
		imageHolder.setZoom();
	}

	/**
	 * Sets the position of the image holder based on how much the user has scrolled
	 *
	 * @see #mouseWheelMoved(MouseWheelEvent)
	 */
	public void setImagePos() {
		imageHolder.setPos();
	}

	/**
	 * Occurs when the user scrolls inside the Canvas. It repositions the image holder is it hasn't been scrolled out
	 * of bounds and is larger than the canvas in the axis being scrolled.
	 *
	 * @param e The scrolling event
	 * @see MouseWheelEvent
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int amount = (int)Math.round(e.getPreciseWheelRotation() * 2);
		if (e.isShiftDown()) { // Horizontal scroll
			if (imageWidth > getWidth()) {
				if (imageHolder.getXPos() - amount > 0 && amount < 0) {
					scrollX = imageWidth / 2 - getWidth() / 2;
				} else if (imageHolder.getXPos() - amount <= getWidth() - imageWidth && amount > 0) {
					scrollX = getWidth() / 2 - imageWidth /2;
				} else {
					scrollX -= amount;
				}
				setImagePos();
			}
		} else { // Vertical scroll
			if (imageHeight > getHeight()) {
				if (imageHolder.getYPos() - amount > 0) {
					scrollY = imageHeight / 2 - getHeight() / 2;
				} else if (imageHolder.getYPos() - amount <= getHeight() - imageHeight) {
					scrollY = getHeight() / 2 - imageHeight / 2;
				} else {
					scrollY -= amount;
				}
				setImagePos();
			}
		}
	}


	  //////////////////
	 // Image Holder //
	//////////////////

	/**
	 * The section that holds the image and allows it to be edited. It can be scrolled around and zoomed in by the
	 * Canvas, and communicates with it to change the image being edited.
	 *
	 * @author Tyler Packard
	 * @version 1
	 * @since 0.0.1
	 * @see Canvas
	 */
	private class ImageHolder extends JComponent implements MouseListener, MouseMotionListener, Updatable {

		/**
		 * The Canvas that contains this instance of an ImageHolder
		 */
		private final Canvas parent;

		/**
		 * The image that is being held and edited
		 */
		private BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

		/**
		 * The Graphics object of the image being held
		 */
		private Graphics imageG = image.getGraphics();

		/**
		 * The amount to be zoomed in, unscaled
		 */
		private int zoomFactor = 1;

		/**
		 * The amount to zoom in, scaled exponentially
		 */
		private int scaledZoom = 1;

		/**
		 * The previous X position of the mouse while pressed
		 */
		private int mouseX;

		/**
		 * The previous Y position of the mouse while pressed
		 */
		private int mouseY;

		/**
		 * The previous X position of the mouse
		 */
		private int hoverX;

		/**
		 * The previous Y position of the mouse
		 */
		private int hoverY;

		/**
		 * The X position of the ImageHolder in the Canvas
		 */
		private int xPos;

		/**
		 * The Y position of the ImageHolder in the Canvas
		 */
		private int yPos;

		/**
		 * Whether or not to make a new edit or append the last one
		 */
		private boolean newEdit = true;


		/**
		 * Adds mouse listeners and makes the cursor invisible when it's inside itself.
		 *
		 * @param parent The Canvas containing the ImageHolder
		 */
		public ImageHolder(Canvas parent) {
			this.parent = parent;
			addMouseListener(this);
			addMouseMotionListener(this);
			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor"));
		}


		/**
		 * Sets the image being held to the given one and disposes the old Graphics and replaces them with the new
		 * image's Graphics. It also resets the zoom level to one.
		 *
		 * @param image The new image to be held
		 */
		public void setImage(BufferedImage image) {
			imageG.dispose();
			this.image = image;
			imageG = this.image.getGraphics();
			setZoom(1);
		}

		/**
		 * Returns the image being held
		 *
		 * @return The image being held
		 */
		public BufferedImage getImage() {
			return image;
		}

		public int getXPos() {
			return xPos;
		}

		public int getYPos() {
			return yPos;
		}

		/**
		 * Sets the zoom level to the current one, forcing the ImageHolder to make sure the image is in a legal
		 * position and to reposition itself.
		 */
		public void setZoom() {
			setZoom(zoomFactor);
		}

		/**
		 * Zooms in or out as specified and repositions the image and itself.
		 *
		 * @param zoomFactor How much to be zoomed in
		 */
		public void setZoom(int zoomFactor) {
			this.zoomFactor = zoomFactor;
			scaledZoom = (int)Math.pow(2, zoomFactor - 1);

			imageWidth = image.getWidth() * (int)Math.pow(2, zoomFactor - 1);
			imageHeight = image.getHeight() * (int)Math.pow(2, zoomFactor - 1);

			// Keep the scroll in-bounds
			if (imageWidth <= getWidth()) {
				scrollX = 0;
			} else if (xPos <= getWidth() - imageWidth) {
				scrollX = getWidth() / 2 - imageWidth /2;
			}
			if (imageHeight <= getHeight()) {
				scrollY = 0;
			} else if (yPos <= getHeight() - imageHeight) {
				scrollY = getHeight() / 2 - imageHeight /2;
			}

			setPos();
		}

		/**
		 * Repositions itself and repaints.
		 */
		public void setPos() {
			xPos = parent.getWidth() / 2 - imageWidth / 2 + scrollX;
			yPos = parent.getHeight() / 2 - imageHeight / 2 + scrollY;
			reposition();
			repaint();
		}

		/**
		 * Repaints the parent Canvas in addition to repainting itself.
		 */
		@Override
		public void repaint() {
			parent.repaint();
		}

		/**
		 * Draws the image, the background, and the mouse icon if in bounds.
		 *
		 * @param g The Graphics object to paint with
		 */
		@Override
		public void paint(Graphics g) {
			super.paint(g);

			/* DRAW BACKGROUND */
			g.setColor(new Color(0xCCCCD2));
			g.fillRect(0, 0, getWidth(), getHeight());

			int squareSize = 8;
			g.setColor(Color.WHITE);
			for (int x = 0; x < imageWidth; x += squareSize * 2) {
				for (int y = 0; y < imageHeight; y += squareSize * 2) {
					g.fillRect(x, y, squareSize, squareSize);
					g.fillRect(x + squareSize, y + squareSize, squareSize, squareSize);
				}
			}

			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

			if (mouseInBounds) {
				if (zoomFactor > 1) {
					drawHover(g);
				}
				toolChooser.getSelectedTool().drawMouse(g, hoverX, hoverY);
			}
		}

		/**
		 * Draws the bounds around the pixel being hovered over
		 *
		 * @param g The Graphics object to paint with
		 */
		void drawHover(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.WHITE);
			g2d.drawRect(hoverX - (hoverX % scaledZoom), hoverY - (hoverY % scaledZoom), scaledZoom, scaledZoom);
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1, new float[]{3, 5}, 0));
			g2d.drawRect(hoverX - (hoverX % scaledZoom), hoverY - (hoverY % scaledZoom), scaledZoom, scaledZoom);
		}

		/**
		 * Occurs when the mouse is pressed and released.
		 *
		 * @param e The clicking event
		 */
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		/**
		 * Draws on the image when clicked by sending the event to the ToolChooser's current tool.
		 *
		 * @param e The pressing event
		 * @see com.tylerpackard.tools.Tool#clicked(int, int, java.awt.image.BufferedImage, int, boolean)
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			toolChooser.getSelectedTool().clicked(mouseX, mouseY, image, scaledZoom, newEdit);
			parent.parent.requestFocus(parent);
			repaint();
		}

		/**
		 * Notifies the ImageHolder that a new edit should be made when the mouse is released.
		 *
		 * @param e The releasing event
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			newEdit = true;
		}

		/**
		 * Tells the ImageHolder to draw the mouse icon when the mouse is inside itself
		 *
		 * @param e The entering event
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			mouseInBounds = true;
			repaint();
		}

		/**
		 * Tells the ImageHolder not to draw the mouse icon when the mouse is outside itself
		 *
		 * @param e The exiting event
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			mouseInBounds = false;
			repaint();
		}

		/**
		 * Draws on the image when dragged by sending the event to the ToolChooser's current tool.
		 *
		 * @param e The dragging event
		 * @see com.tylerpackard.tools.Tool#dragged(java.awt.event.MouseEvent, int, int, java.awt.image.BufferedImage, int)
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			toolChooser.getSelectedTool().dragged(e, mouseX, mouseY, image, scaledZoom);
			mouseX = e.getX();
			mouseY = e.getY();
			hoverX = e.getX();
			hoverY = e.getY();
			repaint();
		}

		/**
		 * Changes the position of the mouse icon and repaints the ImageHolder.
		 *
		 * @param e The moving event
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			hoverX = e.getX();
			hoverY = e.getY();
			repaint();
		}

		/**
		 * Unused, called by the parent Canvas' parent Window
		 */
		@Override
		public void update() {

		}

		/**
		 * Repositions the ImageHolder inside the parent Canvas
		 */
		@Override
		public void reposition() {
			setBounds(xPos, yPos, imageWidth, imageHeight);
		}

		/**
		 * Unused, would remove focus from focusable objects inside
		 */
		@Override
		public void defocus() {

		}
	}
}
