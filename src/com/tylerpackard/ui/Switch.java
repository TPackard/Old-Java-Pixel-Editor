package com.tylerpackard.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Switch extends JPanel implements MouseListener{
	private final JLabel trueLabel;
	private final JLabel falseLabel;
	private boolean state = true;
	private boolean flag = false;


	public Switch(String trueText, String falseText, int x, int y, int width, int height, boolean state) {
		super();
		setLayout(null);
		setBounds(x, y, width, height);
		trueLabel = new JLabel(trueText, SwingConstants.CENTER);
		falseLabel = new JLabel(falseText, SwingConstants.CENTER);
		this.state = state;

		if (state) {
			trueLabel.setBackground(new Color(0x0090FF));
			trueLabel.setForeground(new Color(0xFFFFFF));
			falseLabel.setBackground(new Color(0xE0E0E4));
		} else {
			trueLabel.setBackground(new Color(0xE0E0E4));
			falseLabel.setBackground(new Color(0x0090FF));
			falseLabel.setForeground(new Color(0xFFFFFF));
		}

		trueLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
		trueLabel.setBounds(0, 0, width / 2, height);
		trueLabel.setOpaque(true);
		add(trueLabel);

		falseLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
		falseLabel.setBounds(width / 2, 0, width / 2, height);
		falseLabel.setOpaque(true);
		add(falseLabel);
		addMouseListener(this);
	}


	public void update() {
		repaint();
	}

	public boolean getState() {
		return state;
	}

	public boolean flagged() {
		if (flag) {
			flag = false;
			return true;
		}
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getX() > getWidth() / 2) {
			state = false;
			trueLabel.setBackground(new Color(0xE0E0E4));
			trueLabel.setForeground(new Color(0x000000));
			falseLabel.setBackground(new Color(0x0090FF));
			falseLabel.setForeground(new Color(0xFFFFFF));
		} else {
			state = true;
			trueLabel.setBackground(new Color(0x0090FF));
			trueLabel.setForeground(new Color(0xFFFFFF));
			falseLabel.setBackground(new Color(0xE0E0E4));
			falseLabel.setForeground(new Color(0x000000));
		}
		flag = true;
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
