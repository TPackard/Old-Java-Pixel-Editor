package com.tylerpackard;

import com.tylerpackard.canvas.Canvas;
import com.tylerpackard.tools.colorchooser.ColorChooser;
import com.tylerpackard.ui.*;

import java.util.ArrayList;

class Main implements Runnable{
	private Window window;
	private ColorChooser colorChooser;
	private Canvas canvas;
	private ArrayList<Updatable> updatables = new ArrayList<Updatable>();


	public static void main(String[] args) {
		new Thread(new Main()).start();
	}

	@Override
	public void run() {
		// This all happens once when the program starts
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		window = new Window(720, 480);
		colorChooser = new ColorChooser(window);
		canvas = new Canvas(window, colorChooser);

		updatables.add(colorChooser);
		updatables.add(canvas);

		while (true) {
			loop();
		}
	}

	void loop() {
		for (Updatable updatable : updatables) {
			updatable.update();
		}
	}
}