package org.zelvator.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * Class for managing files and folders, additionally there are methods for
 * getting numbers and name of test from path.
 * 
 * @author zelvator
 * 
 */
public class FilesInFolder {

	private String path;
	final File folder;
	private List<File> files;

	/**
	 * Constructor takes path, which is path to the selected test.
	 * 
	 * @param path
	 */
	public FilesInFolder(String path) {
		this.path = path;
		folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}
		files = new ArrayList<File>();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public File getFolder() {
		return folder;
	}

	/**
	 * Method will fill ArrayList with files in the folder and will search for specified suffix.
	 * Example: in the tests/test search for all .xml files.
	 * 
	 * @param folder
	 * @param suffix
	 */
	public void listFilesForFolder(final File folder, String suffix) {

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry, suffix);
			} else {
				if (fileEntry.getName().endsWith(suffix)) {
					files.add(fileEntry);
					// System.out.println(fileEntry.getName());
				}
			}
		}
	}

	/**
	 * This method will try to delete file. If the file is directory, it will
	 * recursively tries to delete all files in it and then delete directory itself.
	 * Sometimes it can not be deleted because java might be using a file, then
	 * it will fail and offers to delete it again.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static void delete(File file) throws IOException {

		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
				System.out.println("Directory is deleted : " + file.getAbsolutePath());
			} else {
				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					delete(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.setWritable(true);
					file.delete();
					System.out.println("Directory is deleted : " + file.getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(null, "Chyba: nìkteré soubory jsou zrovna používany, zkuste smazat znovu", "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} else {
			// if file, then delete it
			// sometimes cannot be deleted files, its a bug, garbage collector
			// must be run
			System.gc();
			file.setWritable(true);
			file.delete();

			System.out.println("File is deleted : " + file.getAbsolutePath());
		}
	}

	/**
	 * Method will get numbers from String, used as getting last index from
	 * file.<br>
	 * Example: from tests/test/pic10.jpg, only pic10.jpg will be send to this method
	 * and here it will get only index (number), that means, it will return "10".
	 * Then it can be used in creating new file with index pic11.jpg.
	 * 
	 * @param input
	 *            CharSequence in which will be searched for numbers.
	 * @return stripped text with only numbers.
	 */
	public static String stripNonDigits(final CharSequence input) {
		final StringBuilder sb = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			if (c > 47 && c < 58) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * From the path it will get last index of \\ and last index of . ,
	 * this represents in the \tests\test\test.xml String "\test",
	 * for only "test" it is required add + 1.
	 * 
	 * @param path
	 * @return name of the selected test
	 */
	public static String getNameOfTest(String path) {
		int firstIndex = path.lastIndexOf("\\");
		int lastIndex = path.lastIndexOf(".");
		String name = path.substring(firstIndex + 1, lastIndex);
		return name;
	}
}
