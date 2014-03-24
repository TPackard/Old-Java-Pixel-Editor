package com.tylerpackard.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import com.apple.eawt.AppEvent.FullScreenEvent;
import com.apple.eawt.FullScreenListener;

import static com.apple.eawt.FullScreenUtilities.*;

public class Window extends JFrame implements FullScreenListener, KeyListener, ComponentListener{
	private boolean fullScreen = false;
	private ArrayList<Updatable> updatables = new ArrayList<Updatable>();
	private boolean resized = true;


	public Window(int width, int height) {
		super();
		setSize(width, height);
		setLayout(null);
		setVisible(true);
		setTitle("Untitled");
		getContentPane().setBackground(new Color(0xEEEEF2));

		addKeyListener(this);
		setFocusable(true);

		addComponentListener(this);
		addFullScreenListenerTo(this, this);
		setWindowCanFullScreen(this, true); // Allow fullscreen
	}


	public void toggleFullscreen() {
		toggleFullscreen(!fullScreen);
	}

	public void toggleFullscreen(boolean state) {
		fullScreen = state;
		com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
	}

	public int height() {
		int offset = 0;
		if (!fullScreen) {
			offset = 22;
		}
		return getHeight() - offset;
	}

	@Override
	public Component add(Component component) {
		super.add(component);
		if (component instanceof  Updatable) {
			updatables.add((Updatable)component);
		}
		return component;
	}

	public void update() {
		for (Updatable updatable : updatables) {
			updatable.update();
		}
		if (resized) {
			reposition();
		}
	}

	public void reposition() {
		for (Updatable updatable : updatables) {
			updatable.reposition();
		}
		resized = false;
	}

	/* FULLSCREEN LISTENER*/
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

	/* KEY LISTENER */
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	/* COMPONENT LISTENER */
	@Override
	public void componentResized(ComponentEvent e) {
		resized = true;
	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}
}