package com.tylerpackard.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Switch extends JPanel implements MouseListener{
	private final JLabel leftLabel;
	private final JLabel rightLabel;
	private boolean state = true;
	private boolean flag = false;

	private final Color trueFore = new Color(0xFFFFFF);
	private final Color trueBack = new Color(0x0090FF);
	private final Color falseFore = new Color(0xDDDDE1);
	private final Color falseBack = new Color(0x303033);


	public Switch(String trueText, String falseText, int x, int y, int width, int height, boolean state) {
		super();
		setLayout(null);
		setBounds(x, y, width, height);
		leftLabel = new JLabel(trueText, SwingConstants.CENTER);
		rightLabel = new JLabel(falseText, SwingConstants.CENTER);
		this.state = state;

		if (state) {
			leftLabel.setBackground(trueBack);
			leftLabel.setForeground(trueFore);
			rightLabel.setBackground(falseBack);
			rightLabel.setForeground(falseFore);
		} else {
			leftLabel.setBackground(falseBack);
			leftLabel.setForeground(falseFore);
			rightLabel.setBackground(trueBack);
			rightLabel.setForeground(trueFore);
		}

		leftLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
		leftLabel.setBounds(0, 0, width / 2, height);
		leftLabel.setOpaque(true);
		add(leftLabel);

		rightLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
		rightLabel.setBounds(width / 2, 0, width / 2, height);
		rightLabel.setOpaque(true);
		add(rightLabel);
		addMouseListener(this);
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
			leftLabel.setBackground(falseBack);
			leftLabel.setForeground(falseFore);
			rightLabel.setBackground(trueBack);
			rightLabel.setForeground(trueFore);
		} else {
			state = true;
			leftLabel.setBackground(trueBack);
			leftLabel.setForeground(trueFore);
			rightLabel.setBackground(falseBack);
			rightLabel.setForeground(falseFore);
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
