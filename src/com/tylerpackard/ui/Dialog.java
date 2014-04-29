package com.tylerpackard.ui;

import javax.swing.*;
import java.awt.*;

/**
 * The Dialog is an unfinished class that will pop up a dialog that lets the user do something. This will replace Java
 * defined Dialogs that pop up in a new window.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public class Dialog extends JPanel {

	/**
	 * The Window to display the Dialog over
	 */
	public Window parent;


	/**
	 * Creates a new Dialog and centers it over the parent. It also enlargens the parent Window to fit its size.
	 *
	 * @param parent The Window that contains the Dialog
	 * @param width The width of the Dialog
	 * @param height The height of the Dialog
	 */
	public Dialog(Window parent, int width, int height) {
		super(null, true);
		setBackground(new Color(0xEEEEF2));
		setBounds((parent.width() - width) / 2, 0, width, height);
		this.parent = parent;
		parent.setResizable(false);
		if (parent.width() < width) {
			parent.setFrameSize(width, parent.frameHeight());
		}
		if (parent.height() < height) {
			parent.setFrameSize(parent.width(), height);
		}
	}
}
