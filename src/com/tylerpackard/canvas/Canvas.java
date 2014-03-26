package com.tylerpackard.canvas;

import com.tylerpackard.tools.colorchooser.ColorChooser;
import com.tylerpackard.ui.Updatable;
import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener, Updatable, MouseWheelListener {
	private BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
	private Graphics imageG = image.getGraphics();
	private final Window parent;
	private ColorChooser colorChooser;
	private int imageX;
	private int imageY;
	private int mouseX;
	private int mouseY;
	private int zoomFactor = 1;
	private int scrollX = 0;
	private int scrollY = 0;


	public Canvas(Window parent, ColorChooser colorChooser) {
		this.parent = parent;
		this.colorChooser = colorChooser;
		setLayout(null);
		setBounds(192, 0, parent.width() - 192, parent.height());
		setOpaque(false);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		setFocusable(true);
		setImagePos();
	}

	public int imageWidth() {
		return image.getWidth() * zoomFactor;
	}

	public int imageHeight() {
		return image.getHeight() * zoomFactor;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		g.setColor(new Color(0xCCCCD2));
		g.fillRect(imageX, imageY, imageWidth(), imageHeight());

		/* DRAW BACKGROUND */
		int squareSize = 8;
		g.setColor(Color.WHITE);
		for (int x = 0; x < imageWidth(); x += squareSize * 2) {
			for (int y = 0; y < imageHeight(); y += squareSize * 2) {
				drawBackground(g, x, y, squareSize);
			}
		}
		for (int x = squareSize; x < imageWidth(); x += squareSize * 2) {
			for (int y = squareSize; y < imageHeight(); y += squareSize * 2) {
				drawBackground(g, x, y, squareSize);
			}
		}

		g.drawImage(image, imageX, imageY, imageWidth(), imageHeight(), null);
	}

	public void drawBackground(Graphics g, int x, int y, int squareSize) {
		int width = squareSize;
		int height = squareSize;
		if (x + width > imageWidth()) {
			width = imageWidth() - x;
		}
		if (y + height > imageHeight()) {
			height = imageHeight() - y;
		}
		g.fillRect(imageX + x, imageY + y, width, height);
	}

	@Override
	public void update() {

	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		imageG.dispose();
		this.image = image;
		imageG = this.image.getGraphics();
		setImagePos();
	}

	public void setZoom(int zoomFactor) {
		this.zoomFactor = zoomFactor;

		if (imageWidth() <= getWidth()) {
			scrollX = 0;
		}
		if (imageHeight() <= getHeight()) {
			scrollY = 0;
		}

		setImagePos();
	}

	@Override
	public void reposition() {
		setSize(parent.width() - 192, parent.height());
		setZoom(zoomFactor);
	}

	public void setImagePos() {
		imageX = getWidth() / 2 - imageWidth() / 2 + scrollX;
		imageY = getHeight() / 2 - imageHeight() / 2 + scrollY;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		imageG.setColor(colorChooser.getColor());
		imageG.fillRect((mouseX - imageX) / zoomFactor, (mouseY - imageY) / zoomFactor, 1, 1);
		colorChooser.defocus();
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		imageG.setColor(colorChooser.getColor());
		int x1 = (mouseX - imageX) / zoomFactor;
		int y1 = (mouseY - imageY) / zoomFactor;
		int x2 = (e.getX() - imageX) / zoomFactor;
		int y2 = (e.getY() - imageY) / zoomFactor;
		imageG.drawLine(x1, y1, x2, y2);
		mouseX = e.getX();
		mouseY = e.getY();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int amount = (int)Math.round(e.getPreciseWheelRotation() * 2);
		if (e.isShiftDown()) { // Horizontal scroll
			if (imageWidth() > getWidth()) {
				if (imageX - amount > 0 && amount < 0) {
					scrollX = imageWidth() / 2 - getWidth() / 2;
				} else if (imageX - amount <= getWidth() - imageWidth() && amount > 0) {
					scrollX = getWidth() / 2 - imageWidth() /2;
				} else {
					scrollX -= amount;
				}
				setImagePos();
			}
		} else { // Vertical scroll
			if (imageHeight() > getHeight()) {
				if (imageY - amount > 0) {
					scrollY = imageHeight() / 2 - getHeight() / 2;
				} else if (imageY - amount <= getHeight() - imageHeight()) {
					scrollY = getHeight() / 2 - imageHeight() / 2;
				} else {
					scrollY -= amount;
				}
				setImagePos();
			}
		}
	}
}
