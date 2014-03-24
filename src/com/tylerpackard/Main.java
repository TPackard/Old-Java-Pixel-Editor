package com.tylerpackard;

import com.tylerpackard.canvas.Canvas;
import com.tylerpackard.tools.colorchooser.ColorChooser;
import com.tylerpackard.ui.*;

import java.util.ArrayList;

class Main implements Runnable{
	private Window window;
	private ColorChooser colorChooser;
	private Canvas canvas;


	public static void main(String[] args) {
		new Thread(new Main()).start();
	}

	@Override
	public void run() {
		// This all happens once when the program starts
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Crisp");

		window = new Window(720, 480);
		colorChooser = new ColorChooser(window);
		canvas = new Canvas(window, colorChooser);

		while (true) {
			loop();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	void loop() {
		window.update();
	}
}