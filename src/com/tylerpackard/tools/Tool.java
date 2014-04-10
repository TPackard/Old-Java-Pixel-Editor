package com.tylerpackard.tools;

import com.tylerpackard.toolbox.toolchooser.ToolChooser;
import com.tylerpackard.ui.Window;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Tool extends JPanel implements MouseListener{
	private ToolChooser parent;
	BufferedImage icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	final BufferedImage defaultBG;
	final BufferedImage selectedBG;
	private boolean selected;
	private boolean hover;
	final Color noColor = new Color(0, true);


	public Tool(ToolChooser parent, int x, int y) {
		this.parent = parent;
		defaultBG = loadImage("defaultBG");
		selectedBG = loadImage("selectedBG");
		addMouseListener(this);
		setBounds(x, y, 32, 32);
	}


	BufferedImage loadImage(String filePath) {
		if (Window.hasRetina && getClass().getResourceAsStream("images/" + filePath + "@2x.png") != null) {
			filePath += "@2x";
		}
		try {
			return ImageIO.read(getClass().getResourceAsStream("images/" + filePath + ".png"));
		} catch (IOException e) {
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

	public void clicked(int x, int y, Graphics g, BufferedImage image, int zoom) {
		g.fillRect(x / zoom, y / zoom, 1, 1);
	}
	public void dragged(MouseEvent e, int x, int y, Graphics g, int zoom) {
		g.drawLine(e.getX() / zoom,e.getY() / zoom, x / zoom, y / zoom);
	}
	public abstract Color getColor();
}
