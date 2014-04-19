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

public abstract class Tool extends JPanel implements MouseListener{
	ToolChooser parent;
	BufferedImage icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	BufferedImage mouse;
	final BufferedImage defaultBG;
	final BufferedImage selectedBG;
	private boolean selected;
	private boolean hover;


	public Tool(ToolChooser parent, int x, int y) {
		this.parent = parent;
		defaultBG = loadImage("defaultBG");
		selectedBG = loadImage("selectedBG");
		addMouseListener(this);
		setBounds(x, y, 32, 32);
	}


	BufferedImage loadImage(String filePath) {
		if (parent.getParent().hasRetina && getClass().getResourceAsStream("images/" + filePath + "@2x.png") != null) {
			filePath += "@2x";
		}
		try {
			return ImageIO.read(getClass().getResourceAsStream("images/" + filePath + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}
	}

	public BufferedImage getBG() {
		if (hover || selected) {
			return selectedBG;
		}
		return defaultBG;
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public void select() {
		selected = true;
	}

	public void deselect() {
		selected = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		parent.requestSelection(this);
		selected = true;
		parent.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		parent.repaint();
		hover = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		parent.repaint();
		hover = false;
	}

	public void clicked(int x, int y, BufferedImage image, int rgb, boolean newEdit) {}
	public void dragged(MouseEvent e, int x2, int y2, BufferedImage image, int zoom) {}

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
			edit.addChange(x, y, image.getRGB(x, y), rgb);
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

	public void drawMouse(Graphics g, int x, int y) {
		g.drawImage(mouse, x, y - 31, 32, 32, null);
	}
}
