package com.tylerpackard;

import com.tylerpackard.tools.colorchooser.ColorChooser;
import com.tylerpackard.ui.*;

class Main implements Runnable{
	private Window window;
	private ColorChooser colorChooser;


	public static void main(String[] args) {
		new Thread(new Main()).start();
	}

	@Override
	public void run() {
		// This all happens once when the program starts
		window = new Window(720, 480);
		colorChooser = new ColorChooser(window);
		while (true) {
			loop();
		}
	}

	void loop() {
		colorChooser.update();
	}
}