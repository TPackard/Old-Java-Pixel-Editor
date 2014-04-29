package com.tylerpackard.ui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * The Textfield is a JTextfield with predefined DocumentFilters.
 *
 * @author Tyler Packard
 * @version 1
 * @since 0.0.1
 */
public class TextField extends JTextField {

	/**
	 * The constant for having no filter
	 */
	public static final int NO_FILTER = 0;

	/**
	 * The constant for a filter that only accepts numbers
	 */
	public static final int NUMS_ONLY = 1;


	/**
	 * Creates a new TextField with the specified text and no filter.
	 *
	 * @param text The text to display
	 */
	public TextField(String text) {
		this(text, NO_FILTER);
	}

	/**
	 * Creates a new TextField with the specified filter and no text.
	 *
	 * @param filter The filter to use
	 */
	public TextField(int filter) {
		this("", filter);
	}

	/**
	 * Creates a new TextField with the specified text and filter.
	 *
	 * @param text The text to display
	 * @param filter The filter to use
	 */
	public TextField(String text, int filter) {
		super(text);
		if (filter == NUMS_ONLY) {
			((AbstractDocument)getDocument()).setDocumentFilter(new NumsOnly());
		}
	}


	/**
	 * The filter that only accepts numbers, all other characters get removed and a ping is played.
	 */
	private class NumsOnly extends DocumentFilter {
		@Override
		public void insertString(DocumentFilter.FilterBypass bypass, int offset, String string, AttributeSet aSet) throws BadLocationException {
			if (string.matches(".*\\D++.*")) {
				Toolkit.getDefaultToolkit().beep();
			}
			bypass.insertString(offset, string.replaceAll("\\D++", ""), aSet);
		}

		@Override
		public void replace(DocumentFilter.FilterBypass bypass, int offset, int length, String string, AttributeSet aSet) throws BadLocationException {
			if (string.matches(".*\\D++.*")) {
				Toolkit.getDefaultToolkit().beep();
			}
			bypass.replace(offset, length, string.replaceAll("\\D++", ""), aSet);
		}
	}
}