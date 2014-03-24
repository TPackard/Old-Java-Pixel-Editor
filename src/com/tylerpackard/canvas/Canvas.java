package com.tylerpackard.canvas;

import com.tylerpackard.tools.colorchooser.ColorChooser;
import com.tylerpackard.ui.Updatable;
import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener, Updatable{
	private BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
	private Graphics imageG = image.getGraphics();
	private final Window parent;
	private ColorChooser colorChooser;
	private JFileChooser fileChooser = new JFileChooser();
	private int imageX;
	private int imageY;
	private int mouseX;
	private int mouseY;


	public Canvas(Window parent, ColorChooser colorChooser) {
		this.parent = parent;
		this.colorChooser = colorChooser;
		parent.add(this);
		setLayout(null);
		setBounds(192, 0, parent.getWidth() - 192, parent.height());
		setOpaque(false);
		addMouseListener(this);
		addMouseMotionListener(this);
	}


	@Override
	public void paint(Graphics g) {
		super.paint(g);

		g.setColor(new Color(0xCCCCD2));
		g.fillRect(imageX, imageY, image.getWidth(), image.getHeight());

		/* DRAW BACKGROUND */
		int squareSize = 8;
		g.setColor(Color.WHITE);
		for (int x = 0; x < image.getWidth(); x += squareSize * 2) {
			for (int y = 0; y < image.getWidth(); y += squareSize * 2) {
				drawBackground(g, x, y, squareSize);
			}
		}
		for (int x = squareSize; x < image.getWidth(); x += squareSize * 2) {
			for (int y = squareSize; y < image.getWidth(); y += squareSize * 2) {
				drawBackground(g, x, y, squareSize);
			}
		}

		g.drawImage(image, imageX, imageY, null);
	}

	public void drawBackground(Graphics g, int x, int y, int squareSize) {
		int width = squareSize;
		int height = squareSize;
		if (x + width > image.getWidth()) {
			width = image.getWidth() - x;
		}
		if (y + height > image.getHeight()) {
			height = image.getHeight() - y;
		}
		g.fillRect(imageX + x, imageY + y, width, height);
	}

	@Override
	public void update() {
		setSize(parent.getWidth() - 192, parent.height());
		imageX = getWidth() / 2 - image.getWidth() / 2;
		imageY = getHeight() / 2 - image.getHeight() / 2;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		imageG.setColor(colorChooser.getColor());
		imageG.fillRect(mouseX - imageX, mouseY - imageY, 1, 1);
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
		imageG.drawLine(mouseX - imageX, mouseY - imageY, e.getX() - imageX, e.getY() - imageY);
		mouseX = e.getX();
		mouseY = e.getY();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}
