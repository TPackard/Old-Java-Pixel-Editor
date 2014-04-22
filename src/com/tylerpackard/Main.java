package com.tylerpackard;

import com.tylerpackard.ui.Window;

/**
 * The class that starts and runs the program. It contains the Window, which controls its own subsystems.
 */
public class Main implements Runnable{
	/**
	 * The Window that contains the editor and subsystems
	 */
	private Window window;


	/**
	 * The method that starts the Main runnable and sets the Mac Menubar name.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Pixel Editor");
		new Thread(new Main()).start();
	}

	/**
	 * The run method of the Main runnable. It creates a new window starts the looped section of the code, pausing for
	 * 10 milliseconds between each call of loop().
	 *
	 * @see #loop()
	 */
	@Override
	public void run() {
		window = new Window(720, 480);

		while (true) {
			loop();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Updates the window every 10 milliseconds
	 */
	void loop() {
		window.update();
	}
}