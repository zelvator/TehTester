package org.zelvator.file;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * Class for creating FileChooser dialog.
 * 
 * @author zelvator
 * 
 */
public class FileChooser extends JDialog {
	private static final long serialVersionUID = 4362744611270636030L;
	private JFileChooser fileChooser;

	public FileChooser(String picturesFolder) {
		setMinimumSize(new Dimension(550, 390));

		fileChooser = new JFileChooser(picturesFolder);
		fileChooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
		getContentPane().add(fileChooser, BorderLayout.CENTER);
	}

	public JFileChooser getFileChooser() {
		return fileChooser;
	}
}
