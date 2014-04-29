package com.tylerpackard.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The Switch provides two options which the user can toggle between by clicking the switch. It displays the two options
 * on two sections of a button, and highlights the selected one.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public class Switch extends JPanel implements MouseListener{

	/**
	 * The label containing the first option.
	 */
	private final JLabel leftLabel;

	/**
	 * The label containing the second option.
	 */
	private final JLabel rightLabel;

	/**
	 * The state of the switch. True if the left option is selected, false if it isn't.
	 */
	private boolean state = true;

	/**
	 * Turns true if the switch gets updated.
	 */
	private boolean flag = false;

	/**
	 * The colors for the foregrounds and backgrounds of each state.
	 */
	private final Color trueFore = new Color(0xFFFFFF);
	private final Color trueBack = new Color(0x0090FF);
	private final Color falseFore = new Color(0xDDDDE1);
	private final Color falseBack = new Color(0x303033);


	/**
	 * Creates a new Switch with the specified options, dimensions and state.
	 *
	 * @param leftText The text to display on the left
	 * @param rightText The text to display on the right
	 * @param x The X position of the Switch
	 * @param y The Y position of the Switch
	 * @param width The width of the Switch
	 * @param height The height of the Switch
	 * @param state Whether or not the left option is selected
	 */
	public Switch(String leftText, String rightText, int x, int y, int width, int height, boolean state) {
		super();
		setLayout(null);
		setBounds(x, y, width, height);
		leftLabel = new JLabel(leftText, SwingConstants.CENTER);
		rightLabel = new JLabel(rightText, SwingConstants.CENTER);
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


	/**
	 * @return Whether or not the left option is selected
	 */
	public boolean getState() {
		return state;
	}

	/**
	 * Returns whether or not a change has been made and resets the flag to false if so.
	 *
	 * @return Whether or not a change has happened
	 */
	public boolean flagged() {
		if (flag) {
			flag = false;
			return true;
		}
		return false;
	}

	/**
	 * Switches the selection to the side clicked.
	 *
	 * @param e The clicking event
	 */
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

	/**
	 * Occurs when the mouse is pressed.
	 *
	 * @param e The pressing event
	 */
	@Override
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Occurs when the mouse is released.
	 *
	 * @param e The releasing event
	 */
	@Override
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * Occurs when the mouse enters the bounds of the Switch.
	 *
	 * @param e The entering event
	 */
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	/**
	 * Occurs when the mouse exits the bounds of the Switch.
	 *
	 * @param e The exiting event
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}
}
