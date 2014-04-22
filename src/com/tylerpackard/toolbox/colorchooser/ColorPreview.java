package com.tylerpackard.toolbox.colorchooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class ColorPreview extends JPanel implements MouseListener{
	private final ColorChooser parent;


	public ColorPreview(ColorChooser parent, int y) {
		super();
		this.parent = parent;
		setBounds(46, y, 100, 64);
		addMouseListener(this);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}


	@Override
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setStroke(new BasicStroke(2));
		if (parent.foreSelected) {
			g.setColor(parent.getBackgroundColor());
			g.fillRect(26, 22, 72, 40);
			g.setColor(parent.getForegroundColor());
			g.fillRect(2, 2, 72, 40);
			g.setColor(new Color(0x0090FF));
			g.drawRect(2, 2, 72, 40);
		} else {
			g.setColor(parent.getForegroundColor());
			g.fillRect(2, 2, 72, 40);
			g.setColor(parent.getBackgroundColor());
			g.fillRect(26, 22, 72, 40);
			g.setColor(new Color(0x0090FF));
			g.drawRect(26, 22, 72, 40);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (parent.foreSelected) {
			if (e.getX() > 72) {
				parent.setForeSelected(false);
			} else if (e.getY() > 40) {
				parent.setForeSelected(false);
			}
		} else {
			if (e.getX() < 24) {
				parent.setForeSelected(true);
			} else if (e.getY() < 22) {
				parent.setForeSelected(true);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

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
}