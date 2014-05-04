package com.tylerpackard.toolbox.colorchooser;

import com.tylerpackard.ui.Switch;
import com.tylerpackard.ui.Updatable;
import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.*;

/**
 * The ColorChooser lets the user select a color using sliders, textboxes or arrows. They can either use the RGB system,
 * or HSB system. The ColorChooser holds two colors, the foreground and background colors. The user can switch between
 * them by choosing one or the other in the color preview section. The ColorChooser works with a ToolChooser to send
 * tools the color to use.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public class ColorChooser extends JPanel implements Updatable{

	/**
	 * The Window that contains the ColorChooser.
	 */
	private Window parent;

	/**
	 * The switch to switch between HSB and RGB color systems.
	 */
	private final Switch typeSwitch = new Switch("HSB", "RGB", 60, 16, 72, 16, true);

	/**
	 * The slider for Hue or Red.
	 */
	private final ColorSlider slider1 = new ColorSlider(this, 10, 48, "Hue", 360);

	/**
	 * The slider for Saturation or Green.
	 */
	private final ColorSlider slider2 = new ColorSlider(this, 10, 72, "Saturation", 100);

	/**
	 * The slider for Brightness or Blue.
	 */
	private final ColorSlider slider3 = new ColorSlider(this, 10, 96, "Brightness", 100);

	/**
	 * The alpha (opacity) slider.
	 */
	private final ColorSlider sliderA = new ColorSlider(this, 10, 120, "Alpha", 100);

	/**
	 * The foreground color.
	 */
	private Color foreground = new Color(0x000000);

	/**
	 * THe background color.
	 */
	private Color background = new Color(0xFFFFFF);

	/**
	 * Whether or not the foreground color is the one currently selected.
	 */
	boolean foreSelected = true;

	/**
	 * Whether or not the user is currently typing numbers.
	 */
	boolean typingNumbers = false;


	/**
	 * Creates a new ColorChooser in the specified Window and adds its GUI components to itself.
	 *
	 * @param parent The Window that contains it.
	 */
	public ColorChooser(Window parent) {
		super();
		this.parent = parent;
		setSize(parent.getLeftWidth(), 224);
		setLocation(0, parent.height() - getHeight());
		setVisible(true);
		setOpaque(true);
		setBackground(new Color(0x444448));
		setFocusable(false);
		add(typeSwitch);
		add(new ColorPreview(this, 152));
		setForeSelected(true);
		setLayout(null);
	}


	/**
	 * Updates the sliders and switches to a different color system if told to do so.
	 */
	@Override
	public void update() {
		if (typeSwitch.flagged()) {
			switchType();
		}
		slider1.update();
		slider2.update();
		slider3.update();
		sliderA.update();
	}

	/**
	 * Repositions itself inside the parent Window.
	 */
	@Override
	public void reposition() {
		setLocation(0, parent.height() - getHeight());
	}

	/**
	 * Updates the colors of the slider bars when the color is changed.
	 */
	public void updateColor() {
		Color color;

		if (typeSwitch.getState()) {
			float[] rgb = Color.getHSBColor(slider1.getValue(), slider2.getValue(), slider3.getValue()).getRGBColorComponents(null);
			color = new Color(rgb[0], rgb[1], rgb[2], sliderA.getValue());
		} else {
			color = new Color(slider1.getValue(), slider2.getValue(), slider3.getValue(), sliderA.getValue());
		}

		if (foreSelected) {
			foreground = color;
		} else {
			background = color;
		}

		slider1.paintBar();
		slider2.paintBar();
		slider3.paintBar();
		sliderA.paintBar();

		repaint();
	}

	/**
	 * @return The foreground color
	 */
	public Color getForegroundColor() {
		return foreground;
	}

	/**
	 * @return The background color
	 */
	public Color getBackgroundColor() {
		return background;
	}

	/**
	 * @return Whether or not the user is currently typing numbers.
	 */
	public boolean isTypingNumbers() {
		return typingNumbers;
	}

	/**
	 * @return The currently selected color
	 */
	public Color getColor() {
		if (foreSelected) {
			return foreground;
		} else {
			return background;
		}
	}

	/**
	 * The same as SetSliders, but doesn't change th alpha value.
	 *
	 * @param color The color to set the selected color to
	 */
	public void setColor(Color color) {
		setSliders(color, false);
	}

	/**
	 * Sets which color is selected and repaints the sliders.
	 *
	 * @param foreSelected Whether or not the foreground color is selected
	 */
	public void setForeSelected(boolean foreSelected) {
		this.foreSelected = foreSelected;
		if (foreSelected) {
			setSliders(foreground);
		} else {
			setSliders(background);
		}
	}

	/**
	 * Sets the sliders to the color and retains the alpha.
	 *
	 * @param color The color to set the sliders to
	 */
	void setSliders(Color color) {
		setSliders(color, true);
	}

	/**
	 * Sets the value of the sliders to each component of the given color. If alpha is true, then the alpha slider is
	 * set too.
	 *
	 * @param color The color to set the sliders to
	 * @param setAlpha Whether or not to set the alpha slider
	 */
	void setSliders(Color color, boolean setAlpha) {
		if (typeSwitch.getState()) {
			float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
			slider1.setValue(hsb[0]);
			slider2.setValue(hsb[1]);
			slider3.setValue(hsb[2]);
		} else {
			slider1.setValue(color.getRed() / 255.0);
			slider2.setValue(color.getGreen() / 255.0);
			slider3.setValue(color.getBlue() / 255.0);
		}
		if (setAlpha) {
			sliderA.setValue(color.getAlpha() / 255.0);
		}
	}

	/**
	 * Switches between the RGB and HSB color systems.
	 */
	void switchType() {
		if (typeSwitch.getState()) {
			slider1.setLimit(360);
			slider1.setToolTipText("Hue");
			slider2.setLimit(100);
			slider2.setToolTipText("Saturation");
			slider3.setLimit(100);
			slider3.setToolTipText("Brightness");
		} else {
			slider1.setLimit(255);
			slider1.setToolTipText("Red");
			slider2.setLimit(255);
			slider2.setToolTipText("Green");
			slider3.setLimit(255);
			slider3.setToolTipText("Blue");
		}
		setForeSelected(foreSelected);
	}

	/**
	 * Finds and returns what the color would be if a specified slider was set to the specified value.
	 *
	 * @param value The value to use instead of the given slider value
	 * @param slider The Slider whose value is to replaced by the given one
	 * @return The resulting color
	 */
	Color colorAt(float value, ColorSlider slider) {
		float value1 = slider1.getValue();
		float value2 = slider2.getValue();
		float value3 = slider3.getValue();
		float valueA = sliderA.getValue();
		if (slider.equals(slider1)) {
			value1 = value;
		} else if (slider.equals(slider2)) {
			value2 = value;
		} else if (slider.equals(slider3)) {
			value3 = value;
		} else if (slider.equals(sliderA)) {
			valueA = value;
		}

		if (typeSwitch.getState()) {
			float[] rgb = Color.getHSBColor(value1, value2, value3).getRGBColorComponents(null);
			return new Color(rgb[0], rgb[1], rgb[2], valueA);
		} else {
			return new Color(value1, value2, value3, valueA);
		}
	}

	/**
	 * Removes focus from all of the sliders.
	 */
	@Override
	public void defocus() {
		slider1.defocus();
		slider2.defocus();
		slider3.defocus();
		sliderA.defocus();
		typingNumbers = false;
	}
}
