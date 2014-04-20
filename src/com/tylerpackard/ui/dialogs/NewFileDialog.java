package com.tylerpackard.ui.dialogs;

import com.tylerpackard.canvas.Canvas;
import com.tylerpackard.ui.TextField;
import com.tylerpackard.ui.Window;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * The dialog for creating new images. It allows the user to specify a height and width for a new image.
 * After creation, the image on the canvas is replaced by a blank one with the specified dimensions.
 *
 * @author Tyler Packard
 * @see JDialog
 */

public class NewFileDialog extends JDialog {

	/**
	 * The textfields to let the user specify the image's width and height.
	 * @see javax.swing.JTextField
	 */
	private final TextField widthField = new TextField("100", TextField.NUMS_ONLY);
	private final TextField heightField = new TextField("100", TextField.NUMS_ONLY);

	/**
	 * The parent of the dialog on which the dialog should be centered
	 */
	private Window parent;

	/**
	 * The canvas on which to set the image of
	 */
	private Canvas canvas;


	/**
	 * Creates the dialog, but does not show it. Adds all of the labels, textfields, and buttons to itself
	 * and specifies their locations. Also adds an <code>ActionListener</code> to the textfields, buttons,
	 * and itself. The <code>createAction</code> action listener hides the dialog and sets the image of
	 * the canvas to a new blank image with the specified dimensions.
	 *
	 * @param parent The parent of the dialog on which it should be centered
	 */
	public NewFileDialog(Window parent) {
		super(parent.getFrame(), "New File", true);
		this.parent = parent;
		setSize(270, 110);
		setResizable(false);
		setVisible(false);
		requestFocus();
		setLayout(null);

		JLabel widthLabel = new JLabel("Width");
		add(widthLabel);
		add(widthField);
		JLabel heightLabel = new JLabel("Height");
		add(heightLabel);
		add(heightField);
		JButton createButton = new JButton("Create");
		add(createButton);
		JButton cancelButton = new JButton("Cancel");
		add(cancelButton);

		widthLabel.setBounds(15, 15, 36, 20);
		widthField.setBounds(60, 15, 50, 20);
		heightLabel.setBounds(159, 15, 42, 20);
		heightField.setBounds(205, 15, 50, 20);
		createButton.setBounds(155, 50, 70, 25);
		cancelButton.setBounds(45, 50, 70, 25);

		ActionListener createAction = e -> {
			setVisible(false);
			canvas.setImage(new BufferedImage(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()), BufferedImage.TYPE_INT_ARGB));
		};
		widthField.addActionListener(createAction);
		heightField.addActionListener(createAction);
		createButton.addActionListener(createAction);
		cancelButton.addActionListener(e -> setVisible(false));
	}


	/**
	 * Shows the dialog and centers it over the parent <code>Window</code>. It also sets the canvas that will
	 * receive the new image.
	 *
	 * @param canvas The canvas which will receive the new image
	 */
	public void showDialog(Canvas canvas) {
		this.canvas = canvas;
		setLocationRelativeTo(parent);
		setVisible(true);
	}
}
