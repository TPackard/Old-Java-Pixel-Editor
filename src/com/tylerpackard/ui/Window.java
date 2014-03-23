package com.tylerpackard.ui;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {


	public Window(int width, int height) {
		setSize(width, height);
		setLayout(null);
		setVisible(true);
		setTitle("Untitled");
		getContentPane().setBackground(new Color(0xEEEEF2));

		com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this, true); // Allow fullscreen
		// com.apple.eawt.Application.getApplication().requestToggleFullScreen(this); // Make fullscreen
	}

	@Override
	public int getHeight() {
		return super.getHeight() - 22;
	}
}