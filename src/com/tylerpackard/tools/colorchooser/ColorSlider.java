package com.tylerpackard.tools.colorchooser;

import com.tylerpackard.ui.TextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ColorSlider extends JPanel implements MouseListener, MouseMotionListener {
	private final ColorChooser parent;
	private int value = 0;
	private double scale;
	private int limit;
	private double doubleLimit;
	private KeyAdapter keyAdapter;
	private final TextField textField = new TextField("0", TextField.NUMS_ONLY);
	private final JLabel label;
	private final int barStart = 16;
	private final int barEnd = 112;
	private final int barWidth = 8;


	public ColorSlider(ColorChooser parent, int x, int y, String toolTip, final int limit) {
		this.parent = parent;
		parent.add(this);
		setBounds(x, y, 160, 16);
		setVisible(true);
		setOpaque(false);
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		setLayout(null);
		setLimit(limit);

		textField.setBounds(barEnd + barWidth, 0, 40, 16);
		textField.setFont(new Font("SansSerif", Font.PLAIN, 10));
		textField.addKeyListener(keyAdapter);
		add(textField);

		label = new JLabel(toolTip.substring(0, 1));
		label.setFont(new Font("SansSerif", Font.PLAIN, 10));
		label.setBounds(0, 0, barStart, 16);
		add(label);

		setToolTipText(toolTip);
	}


	public void update() {
		repaint();
	}

	public float getValue() {
		return value / (float)doubleLimit;
	}

	public void setValue(double value) {
		this.value = (int) Math.round(value * limit);
		textField.setText(Integer.toString(this.value));
		parent.updateColor();
	}

	public void setLimit(final int limit) {
		this.limit = limit;
		this.doubleLimit = limit;
		scale = (barEnd - barStart - barWidth) / doubleLimit;
		keyAdapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					double value = Float.parseFloat(textField.getText());
					if (value > limit) {
						value = (double) limit;
					}
					value /= (double) limit;
					setValue(value);
				}
			}
		};
	}

	@Override
	public void setToolTipText(String text) {
		super.setToolTipText(text);
		label.setText(text.substring(0, 1));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(0xCCCCD1));
		g.fillRect(barStart, 4, barEnd - barStart, 8);
		g.setColor(new Color(0x444448));
		g.fillRect((int)Math.round(value * scale) + barStart, 0, barWidth, 16);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		if (x <= barStart) {
			value = 0;
		} else if (x > barEnd - barWidth) {
			value = limit;
		} else {
			value = (int)Math.round((e.getX() - barStart) / scale);
		}
		textField.setText(Integer.toString(value));
		parent.updateColor();
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
		int x = e.getX();
		if (x <= barStart) {
			value = 0;
		} else if (x > barEnd - barWidth) {
			value = limit;
		} else {
			value = (int)Math.round((e.getX() - barStart) / scale);
		}
		textField.setText(Integer.toString(value));
		parent.updateColor();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}