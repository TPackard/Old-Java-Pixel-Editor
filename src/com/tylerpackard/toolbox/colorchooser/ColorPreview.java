package com.tylerpackard.toolbox.colorchooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The ColorPreview displays the foreground and background colors and lets the user switch between the two.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
class ColorPreview extends JPanel implements MouseListener{

	/**
	 * The ColorChooser that contains this preview and has the foreground and background colors.
	 */
	private final ColorChooser parent;


	/**
	 * Creates a new ColorPreview, sets the parent and sets its bounds.
	 *
	 * @param parent The containing ColorChooser
	 * @param y The Y position to place it at
	 */
	public ColorPreview(ColorChooser parent, int y) {
		super();
		this.parent = parent;
		setBounds(46, y, 100, 64);
		addMouseListener(this);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}


	/**
	 * Paints rectangles of the foreground and background colors and highlights the selected one.
	 *
	 * @param graphics The graphics to paint with
	 */
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

	/**
	 * Switches between the colors if the unselected color is clicked on.
	 *
	 * @param e The clicking event
	 */
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

	/**
	 * Occurs when the user presses the mouse.
	 *
	 * @param e The pressing event
	 */
	@Override
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Occurs when the user releases the mouse.
	 *
	 * @param e The releasing event
	 */
	@Override
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * Occurs when the mouse enters the ColorPreview.
	 *
	 * @param e The entering event
	 */
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	/**
	 * Occurs when the mouse exits the ColorPreview.
	 *
	 * @param e The exiting event
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}
}