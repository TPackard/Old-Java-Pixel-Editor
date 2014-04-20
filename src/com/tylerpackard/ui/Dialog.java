package com.tylerpackard.ui;

import javax.swing.*;
import java.awt.*;

public class Dialog extends JPanel {
	public Window parent;


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
