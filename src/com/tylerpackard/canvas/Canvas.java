package com.tylerpackard.canvas;

import com.tylerpackard.toolbox.toolchooser.ToolChooser;
import com.tylerpackard.ui.Updatable;
import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel implements Updatable, MouseWheelListener {
	private final ImageHolder imageHolder;
	private final Window parent;
	private ToolChooser toolChooser;
	private int scrollX = 0;
	private int scrollY = 0;
	private int imageWidth;
	private int imageHeight;
	private boolean mouseInBounds;


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


	@Override
	public void update() {

	}

	@Override
	public void defocus() {

	}

	public BufferedImage getImage() {
		return imageHolder.getImage();
	}

	public void setImage(BufferedImage image) {
		imageHolder.setImage(image);
	}

	public void setZoom(int zoomFactor) {
		imageHolder.setZoom(zoomFactor);
	}

	@Override
	public void reposition() {
		setSize(parent.width() - parent.getLeftWidth() - parent.getRightWidth(), parent.height());
		imageHolder.setZoom();
	}

	public void setImagePos() {
		imageHolder.setPos();
	}

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
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	//   IMAGEHOLDER   ////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	private class ImageHolder extends JComponent implements MouseListener, MouseMotionListener, Updatable {
		private final Canvas parent;
		private BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		private Graphics imageG = image.getGraphics();
		private int zoomFactor = 1; // The zoom factor
		private int scaledZoom = 1; // The zoom factor scaled exponentially
		private int mouseX;
		private int mouseY;
		private int hoverX;
		private int hoverY;
		private final Color hoverBorder = new Color(0x88888888, true);
		private int xPos;
		private int yPos;
		private boolean newEdit = true;


		public ImageHolder(Canvas parent) {
			this.parent = parent;
			addMouseListener(this);
			addMouseMotionListener(this);
			setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor"));
		}


		public void setImage(BufferedImage image) {
			imageG.dispose();
			this.image = image;
			imageG = this.image.getGraphics();
			setZoom(1);
		}

		public BufferedImage getImage() {
			return image;
		}

		public void setZoom() {
			setZoom(zoomFactor);
		}

		public int getXPos() {
			return xPos;
		}

		public int getYPos() {
			return yPos;
		}

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

		public void setPos() {
			xPos = parent.getWidth() / 2 - imageWidth / 2 + scrollX;
			yPos = parent.getHeight() / 2 - imageHeight / 2 + scrollY;
			reposition();
			repaint();
		}

		@Override
		public void repaint() {
			parent.repaint();
		}

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

		void drawHover(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.WHITE);
			g2d.drawRect(hoverX - (hoverX % scaledZoom), hoverY - (hoverY % scaledZoom), scaledZoom, scaledZoom);
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1, new float[]{3, 5}, 0));
			g2d.drawRect(hoverX - (hoverX % scaledZoom), hoverY - (hoverY % scaledZoom), scaledZoom, scaledZoom);
		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			toolChooser.getSelectedTool().clicked(mouseX, mouseY, image, scaledZoom, newEdit);
			parent.parent.requestFocus(parent);
			repaint();
			newEdit = false;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			newEdit = true;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			mouseInBounds = true;
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			mouseInBounds = false;
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			toolChooser.getSelectedTool().dragged(e, mouseX, mouseY, image, scaledZoom);
			mouseX = e.getX();
			mouseY = e.getY();
			hoverX = e.getX();
			hoverY = e.getY();
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			hoverX = e.getX();
			hoverY = e.getY();
			repaint();
		}

		@Override
		public void update() {

		}

		@Override
		public void reposition() {
			setBounds(xPos, yPos, imageWidth, imageHeight);
		}

		@Override
		public void defocus() {

		}
	}
}
