package com.tylerpackard.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.apple.eawt.AppEvent.FullScreenEvent;
import com.apple.eawt.FullScreenListener;
import com.tylerpackard.canvas.Canvas;
import com.tylerpackard.edits.Edit;
import com.tylerpackard.edits.EditManager;
import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;

import static com.apple.eawt.FullScreenUtilities.*;

public class Window extends JComponent implements FullScreenListener, ComponentListener {
	private boolean fullScreen = false;
	private ArrayList<Updatable> updatables = new ArrayList<>();
	private boolean resized = true;
	private final JFrame frame;
	private final JFileChooser fileChooser = new JFileChooser();
	private final Canvas canvas;
	private final ColorChooser colorChooser;
	private final ToolChooser toolChooser;
	private final EditManager editManager;
	private int leftWidth = 192;
	private int rightWidth = 48;
	public Boolean hasRetina = null;

	/**
	* Creates a new Window object with the specified height and width,
	* and creates and adds a new ColorChooser, ToolChooser, and Canvas to itself
	*
	* @param width	An integer that specifies the starting width of the window
	* @param height	An integer that specifies the starting height of the window
	*/
	public Window(int width, int height) {
		super();
		setSize(width, height);
		frame = new JFrame("Untitled");
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		setLayout(null);
		frame.setLayout(null);
		setVisible(true);
		frame.setVisible(true);
		frame.getContentPane().setBackground(new Color(0x444448));
		frame.add(this);
		frame.setFocusable(true);

		colorChooser = new ColorChooser(this);
		toolChooser = new ToolChooser(this, colorChooser);
		editManager = new EditManager(this);
		canvas = new Canvas(this, toolChooser);
		add(colorChooser);
		add(toolChooser);
		add(canvas);

		frame.addComponentListener(this);
		addFullScreenListenerTo(frame, this);
		setWindowCanFullScreen(frame, true); // Allow fullscreen


		/*KEYBINDINGS*/
		// Open and Save
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("meta pressed S"), "save");
		getActionMap().put("save", new Save(this));
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("meta pressed O"), "open");
		getActionMap().put("open", new Open(this));
		// Undo / Redo
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("meta pressed Z"), "undo");
		getActionMap().put("undo", new Undo(editManager));
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("meta pressed Y"), "redo");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("meta shift pressed Z"), "redo");
		getActionMap().put("redo", new Redo(editManager));
		// Zoom
		for (int i = 1; i <= 5; i++) {
			String num = Integer.toString(i);
			getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(num), "zoom" + num);
			getActionMap().put("zoom" + num, new Zoom(canvas, i));
		}
	}


	public void toggleFullscreen() {
		toggleFullscreen(!fullScreen);
	}

	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public EditManager getEditManager() {
		return editManager;
	}

	public void toggleFullscreen(boolean state) {
		fullScreen = state;
		com.apple.eawt.Application.getApplication().requestToggleFullScreen(frame);
	}

	public int height() {
		int offset = 0;
		if (!fullScreen) {
			offset = 22;
		}
		return frame.getHeight() - offset;
	}

	public int width() {
		return frame.getWidth();
	}

	public int getLeftWidth() {
		return leftWidth;
	}

	public int getRightWidth() {
		return rightWidth;
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

	public void repaint() {
		super.repaint();
	}

	public void reposition() {
		setSize(width(), height());
		for (Updatable updatable : updatables) {
			updatable.reposition();
		}
		resized = false;
	}

	public void requestFocus(Updatable updatable) {
		for (Updatable member : updatables) {
			if (!member.equals(updatable)) {
				member.defocus();
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (hasRetina == null) {
			hasRetina = ((Graphics2D) g).getFontRenderContext().getTransform().equals(AffineTransform.getScaleInstance(2.0, 2.0));
		}
	}

	/* FULLSCREEN LISTENER */
	@Override
	public void windowEnteringFullScreen(FullScreenEvent e) {
		fullScreen = true;
	}

	@Override
	public void windowEnteredFullScreen(FullScreenEvent e) {
		// Exclusive Fullscreen
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(frame);
	}

	@Override
	public void windowExitingFullScreen(FullScreenEvent e) {
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(null);
		fullScreen = false;
	}

	@Override
	public void windowExitedFullScreen(FullScreenEvent e) {

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


	/* KEYBINDINGS */
	private static class Save extends AbstractAction {
		private Window parent;

		public Save(Window parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			parent.save();
		}
	}

	void save() {
		File file = null;
		int option = fileChooser.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();

			String extension = "";
			int i = file.getName().lastIndexOf('.');
			if (i > 0) {
				extension = file.getName().substring(i + 1);

				try {
					ImageIO.write(canvas.getImage(), extension, file);
					frame.setTitle(file.getName());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class Open extends AbstractAction {
		private Window parent;

		public Open(Window parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			parent.open();
		}
	}

	void open() {
		File file = null;
		int option = fileChooser.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			try {
				canvas.setImage(ImageIO.read(file));
				frame.setTitle(file.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static class Undo extends AbstractAction {
		private EditManager editManager;

		public Undo(EditManager editManager) {
			this.editManager = editManager;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			editManager.undo();
		}
	}

	private static class Redo extends AbstractAction {
		private EditManager editManager;

		public Redo(EditManager editManager) {
			this.editManager = editManager;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			editManager.redo();
		}
	}

	private static class Zoom extends AbstractAction {
		private Canvas canvas;
		private int zoomFactor;

		public Zoom(Canvas canvas, int zoomFactor) {
			this.canvas = canvas;
			this.zoomFactor = zoomFactor;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.setZoom(zoomFactor);
		}
	}

	private static class New extends AbstractAction {
		public New() {

		}

		@Override
		public void actionPerformed(ActionEvent e) {

		}
	}
}