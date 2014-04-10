package com.tylerpackard;

import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Main implements Runnable{
	private Window window;


	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Pixel Editor");
		new Thread(new Main()).start();
	}

	@Override
	public void run() {
		// This all happens once when the program starts
		window = new Window(720, 480);

		while (true) {
			loop();
			sleep(10);
		}
	}

	void loop() {
		window.update();
	}

	public static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}