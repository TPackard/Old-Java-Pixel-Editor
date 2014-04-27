package com.tylerpackard.toolbox.colorchooser;

import com.tylerpackard.ui.TextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * The ColorSlider lets the user change one value of the color either by sliding the slider, entering a value into the
 * text field, or clicking up and down the arrows.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
class ColorSlider extends JPanel implements MouseListener, MouseMotionListener {

	/**
	 * The containing ColorChooser to edit the color of.
	 */
	private final ColorChooser parent;

	/**
	 * The value of the slider.
	 */
	private int value = 0;

	/**
	 * How much to scale the position of the slider to get the value.
	 */
	private double scale;

	/**
	 * The limit that the value cannot exceed.
	 */
	private int limit;

	/**
	 * The KeyAdapter to use on the textbox.
	 */
	private KeyAdapter keyAdapter;

	/**
	 * The focus adapter to add to the textbox.
	 */
	private FocusAdapter focusAdapter;

	/**
	 * The textfield that displays the value and can be used to edit it. It only takes numbers.
	 */
	private final TextField textField = new TextField("0", TextField.NUMS_ONLY);

	/**
	 * The label that shows the user what value is being changed.
	 */
	private final JLabel label;

	/**
	 * The X position where the bar starts.
	 */
	private final int barStart = 16;

	/**
	 * The X position where the bar ends.
	 */
	private final int barEnd = 112;

	/**
	 * The width of the bar.
	 */
	private final int barWidth = 8;

	/**
	 * Whether or not the mouse is currently down.
	 */
	private boolean mouseDown;

	/**
	 * The last X position of the mouse.
	 */
	private int mouseX;

	/**
	 * The last Y position of the mouse.
	 */
	private int mouseY;

	/**
	 * The time since the mouse was last updated.
	 */
	private long lastMouseUpdate = 0;

	/**
	 * The X positions of the arrows.
	 */
	private final int[] arrowX;

	/**
	 * The Y positions of the top arrow.
	 */
	private final int[] arrowY1 = new int[]{6, 0, 6};

	/**
	 * The Y positions of the bottom arrow.
	 */
	private final int[] arrowY2 = new int[]{10, 16, 10};

	/**
	 * The Y positions of the slider bar.
	 */
	private final int[] sliderYPoints = new int[] {4, 0, 0, 4, 12, 15, 15, 12};

	/**
	 * The image of the slider bar.
	 */
	private BufferedImage bar;

	/**
	 * The image of the bar background.
	 */
	private BufferedImage barBackground;

	/**
	 * The bar Graphics object.
	 */
	private Graphics barG;

	/**
	 * The bar and arrow color.
	 */
	private final Color lightColor = new Color(0xCCCCD0);


	/**
	 * Creates a new ColorSlider at the specified position in the given ColorChooser. It also sets the max value and
	 * text.
	 *
	 * @param parent The containing ColorChooser
	 * @param x The X position of the bar
	 * @param y The Y position of the bar
	 * @param toolTip The tooltip text
	 * @param limit The max value the slider can have.
	 */
	public ColorSlider(ColorChooser parent, int x, int y, String toolTip, final int limit) {
		super();
		this.parent = parent;
		parent.add(this);
		setBounds(x, y, 172, 18);
		bar = new BufferedImage(barEnd - barStart, 8, BufferedImage.TYPE_INT_ARGB);
		barG = bar.getGraphics();
		makeBarBackground();
		setVisible(true);
		setOpaque(false);
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		setLayout(null);
		setLimit(limit);
		setCursor(new Cursor(Cursor.HAND_CURSOR));

		textField.setBounds(barEnd + 8, 0, 34, 16);
		textField.setFont(new Font("SansSerif", Font.PLAIN, 10));
		textField.addKeyListener(keyAdapter);
		textField.addFocusListener(focusAdapter);
		add(textField);

		label = new JLabel(toolTip.substring(0, 1));
		label.setFont(new Font("SansSerif", Font.PLAIN, 10));
		label.setForeground(lightColor);
		label.setBounds(0, 0, barStart, 16);
		add(label);

		setToolTipText(toolTip);

		arrowX = new int[] {getWidth() - 12, getWidth() - 6, getWidth()};
	}


	/**
	 * Detects if the arrows are clicked and increments or decrements the value.
	 */
	public void update() {
		if (mouseDown && System.nanoTime() - lastMouseUpdate >= 200_000_000) {
			if (mouseX >= arrowX[0]) {
				if (mouseY <= 8) {
					if (value < limit) {
						value++;
						textField.setText(Integer.toString(value));
						parent.updateColor();
					} else {
						Toolkit.getDefaultToolkit().beep();
					}
				} else if (mouseY >= 12) {
					if (value > 0) {
						value--;
						textField.setText(Integer.toString(value));
						parent.updateColor();
					} else {
						Toolkit.getDefaultToolkit().beep();
					}
				}
			}
			lastMouseUpdate = System.nanoTime();
		}
	}

	/**
	 * @return The value of the slider
	 */
	public float getValue() {
		return value / (float)limit;
	}

	/**
	 * Sets the value of the slider. It changes the value in the textfield and recalculates the colors of the bar.
	 *
	 * @param value The value to use
	 */
	public void setValue(double value) {
		this.value = (int) Math.round(value * limit);
		textField.setText(Integer.toString(this.value));
		parent.updateColor();
	}

	/**
	 * Sets the limit that the value cannot exceed.
	 *
	 * @param limit The limit of value
	 */
	public void setLimit(int limit) {
		this.limit = limit;
		scale = (barEnd - barStart) / (double)limit;
		createAdapters(limit);
	}

	/**
	 * Creates the key and focus adapters for the textfield. The adapters make sure that the value isn't above the
	 * limit when enter is pressed or the textfield is deselected.
	 *
	 * @param limit The limit of value
	 */
	private void createAdapters(final int limit) {
		keyAdapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					double value = Float.parseFloat(textField.getText());
					if (value > limit) {
						value = (double) limit;
					}
					value /= (double) limit;
					setValue(value);
				}
			}
		};

		focusAdapter = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				double value = Float.parseFloat(textField.getText());
				if (value > limit) {
					value = (double) limit;
				}
				value /= (double) limit;
				setValue(value);
			}
		};
	}

	/**
	 * Removes focus from the textfield.
	 */
	public void defocus() {
		textField.setFocusable(false);
		textField.setFocusable(true);
	}

	/**
	 * Sets the tooltip text and the letter displayed on the label.
	 *
	 * @param text The text to use
	 */
	@Override
	public void setToolTipText(String text) {
		super.setToolTipText(text);
		label.setText(text.substring(0, 1));
	}

	/**
	 * Draws the bar, slider, and incrementer arrows.
	 *
	 * @param g The Graphics to paint with
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		g.drawImage(bar, barStart, 4, null);

		/* SLIDER */
		g.setColor(lightColor);
		int point = (int)Math.round(value * scale) + barStart;
		int offset = barWidth / 2;
		int[] sliderXPoints = new int[]{point, point - offset, point + offset, point, point, point - offset + 1, point + offset - 1, point};
		g.fillPolygon(sliderXPoints, sliderYPoints, 8);

		/* ARROWS */
		g.fillPolygon(arrowX, arrowY1, 3);
		g.fillPolygon(arrowX, arrowY2, 3);
	}

	/**
	 * Paints the bar with the values given if the slider goes to that location.
	 *
	 * @see ColorChooser#colorAt(float, ColorSlider)
	 */
	public void paintBar() {
		final int sliverWidth = 3;
		barG.drawImage(barBackground, 0, 0, null);
		for (int i = 0; i < barEnd - barStart; i += sliverWidth) {
			barG.setColor(parent.colorAt((i / (float) scale) / (float) limit, this));
			barG.fillRect(i, 0, sliverWidth, 8);
		}
	}

	/**
	 * Makes the background for translucent colors and puts it on an image.
	 */
	public void makeBarBackground() {
		barBackground = new BufferedImage(barEnd - barStart, 8, BufferedImage.TYPE_INT_ARGB);
		int width = barBackground.getWidth();
		int height = barBackground.getHeight();

		Graphics g = barBackground.getGraphics();
		g.setColor(new Color(0xCCCCD2));
		g.fillRect(0, 0, width, 8);

		int squareSize = 4;
		g.setColor(Color.WHITE);
		for (int x = 0; x < width; x += squareSize * 2) {
			for (int y = 0; y < height; y += squareSize * 2) {
				g.fillRect(x, y, squareSize, squareSize);
				g.fillRect(x + squareSize, y + squareSize, squareSize, squareSize);
			}
		}
	}

	/**
	 * Occurs when the mouse is clicked.
	 *
	 * @param e The clicking event
	 */
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * Jumps the slider to location clicked if valid and updates the bar.
	 *
	 * @param e The pressing event
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown = true;
		mouseX = e.getX();
		mouseY = e.getY();
		if (mouseX > barStart && mouseX <= barEnd) {
			value = (int)Math.round((e.getX() - barStart) / scale);
			textField.setText(Integer.toString(value));
			parent.updateColor();
		}
	}

	/**
	 * Lets the slider know that the mouse isn't pressed.
	 *
	 * @param e The releasing event
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
	}

	/**
	 * Occurs when the mouse enters the slider region.
	 *
	 * @param e The entering event
	 */
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	/**
	 * Occurs when the mouse exits the slider region.
	 *
	 * @param e The exiting event
	 */
	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * Moves the slider to the location dragged to if valid and updates the bar.
	 *
	 * @param e The dragging event
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if (mouseX >= barStart - barWidth / 2 && mouseX <= barEnd + barWidth / 2) {
			int x = e.getX();
			if (x <= barStart) {
				value = 0;
			} else if (x > barEnd) {
				value = limit;
			} else {
				value = (int)Math.round((e.getX() - barStart) / scale);
			}
			textField.setText(Integer.toString(value));
			parent.updateColor();
		}
	}

	/**
	 * Occurs when the mouse moves.
	 *
	 * @param e The moving event
	 */
	@Override
	public void mouseMoved(MouseEvent e) {

	}
}