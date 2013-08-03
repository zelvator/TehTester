package org.zelvator.dragImage;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import org.zelvator.editor.AddAnswer;
import org.zelvator.editor.QuestionEditation;

/**
 * This class represents function of Drag and Drop feature. You
 * can add this to some kind of container, like JPanel, and it will
 * recognize a file, or whatever you program instead, if you drag and
 * drop to this container, and it will response. In this case it take
 * path of loaded file and draw it to the container.
 * 
 * @author zelvator
 */
public class DragDropListener implements DropTargetListener {
	/*
	 * http://blog.christoffer.me/post/2011-01-09-drag-and-dropping-files-to-java
	 * -desktop-application/#.UeVkdW0ixIN
	 */
	public String path;
	private QuestionEditation questEditation;
	private AddAnswer addAnswer;

	/**
	 * Constructor for DragDropListener, takes QuestionEditation class in case you want draw loaded picture in QuestionEditation class.
	 * 
	 * @param questEditation
	 * 
	 */
	public DragDropListener(QuestionEditation questEditation) {
		this.setQuestEditation(questEditation);
	}

	/**
	 * Constructor for DragDropListener, takes AddAnswer class in case you want draw loaded picture in AddAnswer class.
	 * 
	 * @param addAnswer
	 * 
	 */
	public DragDropListener(AddAnswer addAnswer) {
		this.setAddAnswer(addAnswer);
	}

	/**
	 * Event when drop is performed.
	 * Code will list for flavors, which can be transfered. Check if it is a file and search through an array
	 * of files for picture file. If multiple files are selected, it will set the first suitable image file's path
	 * and draw it in the panel.
	 * At the end informs if drop is complete.
	 */
	@Override
	public void drop(DropTargetDropEvent event) {

		// Accept copy drops
		event.acceptDrop(DnDConstants.ACTION_COPY);

		// Get the transfer which can provide the dropped item data
		Transferable transferable = event.getTransferable();

		// Get the data formats of the dropped item
		DataFlavor[] flavors = transferable.getTransferDataFlavors();

		// Loop through the flavors
		for (DataFlavor flavor : flavors) {
			try {
				// If the drop items are files
				if (flavor.isFlavorJavaFileListType()) {

					// Get all of the dropped files
					@SuppressWarnings("unchecked")
					List<File> files = (List<File>) transferable.getTransferData(flavor);

					// Loop them through
					for (File file : files) {
						String path = file.getPath();
						if (path.toLowerCase().endsWith(".png") || path.toLowerCase().endsWith(".jpg") || path.toLowerCase().endsWith(".jpeg") || path.toLowerCase().endsWith(".bmp")) {
							// Print out the file path
							path = file.getPath();
							System.out.println("File path is '" + path + "'.");
							if (getQuestEditation() != null) {
								getQuestEditation().getImagePanel().setImage(path, false);
								getQuestEditation().getImagePanel().repaint();
								getQuestEditation().setPictureIsFromFolder(false);
							} else {
								getAddAnswer().getAnswerPanel().setImage(path, false);
								getAddAnswer().getAnswerPanel().repaint();
								getAddAnswer().setPictureIsFromFolder(false);
							}
						}
						break;
					}
				} else if (flavor.match(DataFlavor.imageFlavor)) {
					try {
						Image image = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
						if (getQuestEditation() != null) {
							getQuestEditation().getImagePanel().setImage((BufferedImage) image);
							getQuestEditation().getImagePanel().setPath("none");
							getQuestEditation().getImagePanel().repaint();
							getQuestEditation().setPictureIsFromFolder(false);
						} else {
							getAddAnswer().getAnswerPanel().setImage((BufferedImage) image);
							getAddAnswer().getAnswerPanel().setPath("none");
							getAddAnswer().getAnswerPanel().repaint();
							getAddAnswer().setPictureIsFromFolder(false);
						}
					} catch (InvalidDnDOperationException ex) {
						
						System.out.println("word 2010 error");
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Inform that the drop is complete
		event.dropComplete(true);
	}

	@Override
	public void dragEnter(DropTargetDragEvent event) {
	}

	@Override
	public void dragExit(DropTargetEvent event) {
	}

	@Override
	public void dragOver(DropTargetDragEvent event) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent event) {
	}

	/**
	 * @return the questEditation
	 */
	private QuestionEditation getQuestEditation() {
		return questEditation;
	}

	/**
	 * @param questEditation
	 *            the questEditation to set
	 */
	private void setQuestEditation(QuestionEditation questEditation) {
		this.questEditation = questEditation;
	}

	/**
	 * @return the addAnswer
	 */
	private AddAnswer getAddAnswer() {
		return addAnswer;
	}

	/**
	 * @param addAnswer
	 *            the addAnswer to set
	 */
	private void setAddAnswer(AddAnswer addAnswer) {
		this.addAnswer = addAnswer;
	}

}