package org.zelvator.file;

import java.io.File;

/**
 * Class for setting file filter in FileChooser. Accepts only jpg files.
 * Used in "choose image from folder" which allows only files saved in folder
 * and those are jpgs.
 * 
 * @author zelvator
 */
public class FileFilter extends javax.swing.filechooser.FileFilter {

	@Override
	public boolean accept(File f) {
		return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg");
	}

	@Override
	public String getDescription() {
		return ".jpg files";
	}
}
