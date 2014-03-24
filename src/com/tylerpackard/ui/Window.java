package com.tylerpackard.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.apple.eawt.AppEvent.FullScreenEvent;
import com.apple.eawt.FullScreenListener;

import static com.apple.eawt.FullScreenUtilities.*;

public class Window extends JFrame implements FullScreenListener, KeyListener{
	private boolean fullScreen = false;


	public Window(int width, int height) {
		super();
		setSize(width, height);
		setLayout(null);
		setVisible(true);
		setTitle("Untitled");
		getContentPane().setBackground(new Color(0xEEEEF2));

		addKeyListener(this);
		setFocusable(true);

		addFullScreenListenerTo(this, this);
		setWindowCanFullScreen(this, true); // Allow fullscreen
	}

	public void toggleFullscreen() {
		toggleFullscreen(!fullScreen);
	}

	public void toggleFullscreen(boolean state) {
		fullScreen = state;
		com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
//		if (fullScreen) {
//			GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(this);
//		} else {
//			GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(null);
//		}
	}

	public int height() {
		int offset = 0;
		if (!fullScreen) {
			offset = 22;
		}
		return getHeight() - offset;
	}

	@Override
	public void windowEnteringFullScreen(FullScreenEvent e) {
		fullScreen = true;
	}

	@Override
	public void windowEnteredFullScreen(FullScreenEvent e) {
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(this);
	}

	@Override
	public void windowExitingFullScreen(FullScreenEvent e) {
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(null);
		fullScreen = false;
	}

	@Override
	public void windowExitedFullScreen(FullScreenEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}