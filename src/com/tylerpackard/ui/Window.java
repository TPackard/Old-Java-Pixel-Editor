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
import com.tylerpackard.edits.EditManager;
import com.tylerpackard.toolbox.colorchooser.ColorChooser;
import com.tylerpackard.toolbox.toolchooser.ToolChooser;
import com.tylerpackard.ui.dialogs.NewFileDialog;

import static com.apple.eawt.FullScreenUtilities.*;

/**
 * A Window contains everything needed to edit an image, containing the canvas, toolbox, color chooser, and various
 * necessary systems. It contains some Mac-specific methods relating to going fullscreen, and cannot be run on Windows
 * computers. It contains all of the keybindings for keyboard shortcuts because every component is a child of the
 * Window.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 * @see JComponent
 * @see JFrame
 */
public class Window extends JComponent implements FullScreenListener, ComponentListener {

	/**
	 * A boolean containing whether or not the window is currently fullscreen
	 */
	private boolean fullscreen = false;

	/**
	 * A list of all updatables contained in the window
	 * @see Updatable
	 */
	private ArrayList<Updatable> updatables = new ArrayList<>();

	/**
	 * Whether or not the window has been resized and not yet repositioned
	 * @see #reposition()
	 */
	private boolean resized = true;

	/**
	 * The JFrame that contains the JComponent part of the Window
	 */
	private final JFrame frame;

	/**
	 * The JFilechooser for opening and saving files
	 */
	private final JFileChooser fileChooser = new JFileChooser();

	/**
	 * The components contained in the Window
	 */
	private final Canvas canvas;
	private final ColorChooser colorChooser;
	private final ToolChooser toolChooser;
	private final EditManager editManager;
	private final NewFileDialog newFileDialog;

	/**
	 * The width of the left toolbar of the window
	 */
	private int leftWidth = 192;

	/**
	 * The width of the right toolbar of the Window
	 */
	private int rightWidth = 48;

	/**
	 * Whether or not the display is a Retina display, or a display with twice the pixel density
	 */
	public Boolean hasRetina = null;

	/**
	* Creates a new Window object with the specified height and width and puts it in its own JFrame. It creates and
	* adds a new ColorChooser, ToolChooser, and Canvas to itself and also creates its own EditManager and NewFileDialog.
	* It adds a component and fullscreen listener and enables the capability to go fullscreen on Macs. It also binds
	* many keys to various shortcuts for file I/O, editing, and zooming.
	*
	* @param width	Specifies the starting width of the window
	* @param height	Specifies the starting height of the window
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
		newFileDialog = new NewFileDialog(this);
		canvas = new Canvas(this, toolChooser);
		add(colorChooser);
		add(toolChooser);
		add(canvas);

		frame.addComponentListener(this);
		addFullScreenListenerTo(frame, this);
		setWindowCanFullScreen(frame, true);


		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("meta pressed S"), "save");
		getActionMap().put("save", new Save(this));
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("meta pressed O"), "open");
		getActionMap().put("open", new Open(this));
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("meta pressed N"), "newImage");
		getActionMap().put("newImage", new NewImage(newFileDialog, canvas));

		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("meta pressed Z"), "undo");
		getActionMap().put("undo", new Undo(editManager));
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("meta pressed Y"), "redo");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("meta shift pressed Z"), "redo");
		getActionMap().put("redo", new Redo(editManager));

		for (int i = 1; i <= 5; i++) {
			String num = Integer.toString(i);
			getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(num), "zoom" + num);
			getActionMap().put("zoom" + num, new Zoom(canvas, i));
		}
	}


	/**
	 * Returns the window's EditManager
	 *
	 * @see EditManager
	 * @return The window's EditManager
	 */
	public EditManager getEditManager() {
		return editManager;
	}

	/**
	 * Returns the JFrame containing the Window
	 *
	 * @return The JFrame containing the Window
	 * @see JFrame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Fullscreens the app if not currently fullscreened, or escapes fullscreen if it currently is fullscreened
	 *
	 * @see #toggleFullscreen(boolean)
	 */
	public void toggleFullscreen() {
		toggleFullscreen(!fullscreen);
	}

	/**
	 * Sets whether or not the app is fullscreen based on the given state.
	 *
	 * @param state True if it should be fullscreen, false if it shouldn't
	 * @see com.apple.eawt.Application#requestToggleFullScreen(java.awt.Window)
	 */
	public void toggleFullscreen(boolean state) {
		fullscreen = state;
		com.apple.eawt.Application.getApplication().requestToggleFullScreen(frame);
	}

	/**
	 * Sets whether or not the frame is resizable
	 *
	 * @param resizable Whether or not the frame is resizable
	 * @see JFrame#setResizable(boolean)
	 */
	public void setResizable(boolean resizable) {
		frame.setResizable(resizable);
	}

	/**
	 * Sets the size of the frame only
	 *
	 * @param width The width of the frame
	 * @param height The height of the frame
	 * @see JFrame#setSize(int, int)
	 */
	public void setFrameSize(int width, int height) {
		frame.setSize(width, height);
	}

	/**
	 * Returns the height of the JFrame
	 *
	 * @return The height of the JFrame
	 * @see JFrame#getHeight()
	 */
	public int frameHeight() {
		return frame.getHeight();
	}

	/**
	 * Returns the height of the Window, and subtracts the height of the containing JFrame's menubar
	 *
	 * @return The height of the Window
	 * @see JFrame#getHeight()
	 */
	public int height() {
		int offset = 0;
		if (!fullscreen) {
			offset = 22;
		}
		return frame.getHeight() - offset;
	}

	/**
	 * Returns the width of the Window
	 *
	 * @return The width of the window
	 * @see JFrame#getWidth()
	 */
	public int width() {
		return frame.getWidth();
	}

	/**
	 * Returns the width of the left toolbar section for use by child toolbars
	 *
	 * @return Left toolbar width
	 */
	public int getLeftWidth() {
		return leftWidth;
	}

	/**
	 * Returns the width of the right toolbar section for use by child toolbars
	 *
	 * @return Right toolbar width
	 */
	public int getRightWidth() {
		return rightWidth;
	}

	/**
	 * Adds the given component to the Window's JComponent. If the given component is and Updatable, then it's added
	 * to the ArrayList of Updatables
	 *
	 * @param component The component to be added to the Window
	 * @return The Component added to the Window
	 * @see Container#add(Component)
	 */
	@Override
	public Component add(Component component) {
		super.add(component);
		if (component instanceof  Updatable) {
			updatables.add((Updatable)component);
		}
		return component;
	}

	/**
	 * Updates all child updatables and if the Window has been resized, then it's repositioned()
	 *
	 * @see Updatable
	 * @see #reposition()
	 */
	public void update() {
		for (Updatable updatable : updatables) {
			updatable.update();
		}
		if (resized) {
			reposition();
		}
	}

	/**
	 * Resizes the JComponent and all child Updatables
	 */
	public void reposition() {
		setSize(width(), height());
		for (Updatable updatable : updatables) {
			updatable.reposition();
		}
		resized = false;
	}

	/**
	 * @return Whether or not numbers are being typed.
	 */
	public boolean isTypingNumbers() {
		return colorChooser.isTypingNumbers();
	}

	/**
	 * It gives the given Updatable focus. It does this by removing focus from all Updatables except for the given one.
	 *
	 * @param updatable The Updatable to give focus to
	 * @see Updatable#defocus()
	 */
	public void requestFocus(Updatable updatable) {
		updatables.stream().filter(member -> !member.equals(updatable)).forEach(Updatable::defocus);
	}

	/**
	 * Paints the Window and detects if the computer's display has retina capability if it hasn't already been detected
	 *
	 * @param g The Graphics that are used to paint
	 * @see JComponent#paint(Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (hasRetina == null) {
			hasRetina = ((Graphics2D) g).getFontRenderContext().getTransform().equals(AffineTransform.getScaleInstance(2.0, 2.0));
		}
	}


	/** Fullscreen Listener **/

	/**
	 * Notifies the Window when the program begins to enter fullscreen
	 *
	 * @param e The fullscreen event
	 * @see FullScreenListener
	 */
	@Override
	public void windowEnteringFullScreen(FullScreenEvent e) {
		fullscreen = true;
	}

	/**
	 * Occurs when the Window has finished entering fullscreen
	 *
	 * @param e The fullscreen event
	 */
	@Override
	public void windowEnteredFullScreen(FullScreenEvent e) {
		// Exclusive Fullscreen
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(frame);
	}

	/**
	 * Notifies the Window when it begins to exit fullscreen
	 *
	 * @param e The fullscreen event
	 */
	@Override
	public void windowExitingFullScreen(FullScreenEvent e) {
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(null);
		fullscreen = false;
	}

	/**
	 * Occurs when the Window has finished exiting fullscreen
	 *
	 * @param e The fullscreen event
	 */
	@Override
	public void windowExitedFullScreen(FullScreenEvent e) {

	}


	/** Component Listener **/

	/**
	 * Notifies the program when the JFrame is resized
	 *
	 * @param e The component event
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		resized = true;
	}

	/**
	 * Occurs when the JFrame is moved
	 *
	 * @param e The component event
	 */
	@Override
	public void componentMoved(ComponentEvent e) {

	}

	/**
	 * Occurs when the JFrame is shown
	 *
	 * @param e The component event
	 */
	@Override
	public void componentShown(ComponentEvent e) {

	}

	/**
	 * Occurs when the JFrame is hidden
	 *
	 * @param e The component event
	 */
	@Override
	public void componentHidden(ComponentEvent e) {

	}


	/** Keybindings **/

	/**
	 * An action that calls the parent Window's save method when the user uses the save shortcut.
	 *
	 * @see #save()
	 */
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

	/**
	 * Called by the save action. It displays a JFileChooser that lets the user
	 * specify a location to save the image. Then it creates a new File containing the child Canvas' image and saves it to
	 * the location specified by the user, if it's valid.
	 *
	 * @see JFileChooser
	 * @see ImageIO#write(java.awt.image.RenderedImage, String, File)
	 */
	void save() {
		toolChooser.setTempPicker(false);
		File file;
		int option = fileChooser.showSaveDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();

			String extension;
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

	/**
	 * An action that calls the parent Window's open method when the user uses the open shortcut.
	 *
	 * @see #open()
	 */
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

	/**
	 * Called by the open action. It displays a JFileChooser that lets the user
	 * specify an image to open. Then it opens the image if it's valid and sends it to the Window's child Canvas.
	 *
	 * @see JFileChooser
	 * @see ImageIO#read(File)
	 */
	void open() {
		toolChooser.setTempPicker(false);
		File file;
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

	/**
	 * An action that calls the parent Window's EditManager's undo method when the user uses the undo shortcut.
	 *
	 * @see EditManager#undo()
	 */
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

	/**
	 * An action that calls the parent Window's EditManager's redo method when the user uses the redo shortcut.
	 *
	 * @see EditManager#redo()
	 */
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

	/**
	 * An action that zooms in the Canvas by the specified amount when the user uses a zoom shortcut.
	 *
	 * @see Canvas#setZoom(int)
	 */
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

	/**
	 * An action that shows a NewDialog when the user uses the new shortcut.
	 *
	 * @see NewFileDialog#showDialog(Canvas)
	 */
	private static class NewImage extends AbstractAction {
		private NewFileDialog parent;
		private Canvas canvas;

		public NewImage(NewFileDialog parent, Canvas canvas) {
			this.parent = parent;
			this.canvas = canvas;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			parent.showDialog(canvas);
		}
	}
}