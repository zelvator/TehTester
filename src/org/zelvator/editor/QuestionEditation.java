package org.zelvator.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Node;
import org.zelvator.dragImage.DragDropListener;
import org.zelvator.dragImage.ImagePanel;
import org.zelvator.file.FileChooser;
import org.zelvator.file.FileFilter;
import org.zelvator.file.FilesInFolder;
import org.zelvator.file.ModifyXMLFile;
import org.zelvator.models.ListModelAnswers;
import org.zelvator.questions.Question;
import org.zelvator.questions.answers.Answer;

/**
 * Dialog for question editation.
 * 
 * @author zelvator
 * 
 */
public class QuestionEditation {

	private JDialog frmUpravteOtzku;
	private QuestionSelection questionSelection;
	private Question question;
	private ModifyXMLFile xmlFile;
	private JList<Answer> answerList;
	private JTextArea textQuestion;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JPanel panel_1;
	private DragDropListener myDragDropListener = null;
	private JButton btnRemovePic;
	private BufferedImage image;
	private ImagePanel imagePanel;
	private ImagePanel answerPanel;

	private int index;
	private int lastId;
	private boolean newQuestion = false;
	private String pathToPic = "";
	private JButton btnVybratZeSlozky;
	private boolean pictureIsFromFolder = false;
	private JButton btnUpravit;
	private JLabel eventLabelQE;

	/**
	 * @wbp.parser.constructor
	 */
	public QuestionEditation() {
		initialize();
	}

	/**
	 * Constructor for editing existing question.
	 * 
	 * @param questionSelection
	 * @param xml
	 * @param question
	 */
	public QuestionEditation(QuestionSelection questionSelection, ModifyXMLFile xml, Question question) {
		this.setQuestionSelection(questionSelection);
		this.setXmlFile(xml);
		this.setQuestion(question);
		setPathToPic(question.getPathToPic());
		initialize();
		getTextQuestion().setText(question.getQuestion());
		setIndex(getAnswerList().getSelectedIndex());
	}

	/**
	 * Constructor for creating new question.
	 * 
	 * @param questionSelection
	 * @param xml
	 * @param lastId
	 */
	public QuestionEditation(QuestionSelection questionSelection, ModifyXMLFile xml, int lastId) {
		this.setQuestionSelection(questionSelection);
		this.setXmlFile(xml);
		setNewQuestion(true);
		this.setLastId(lastId);
		setQuestion(new Question(lastId));
		initialize();
		setIndex(getAnswerList().getSelectedIndex());
	}

	/**
	 * Initialize the contents of the dialog.
	 */
	private void initialize() {
		setFrmUpravteOtzku(new JDialog());
		getFrmUpravteOtzku().setModal(true);
		getFrmUpravteOtzku().setResizable(false);
		getFrmUpravteOtzku().setTitle("Upravit ot\u00E1zku");
		getFrmUpravteOtzku().setBounds(100, 100, 700, 480);
		getFrmUpravteOtzku().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getFrmUpravteOtzku().setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		getFrmUpravteOtzku().getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JButton addAnswerButton = new JButton("P\u0159idat");
		addAnswerButton.setToolTipText("Vytvo\u0159\u00ED novou odpov\u011B\u010F");
		addAnswerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AddAnswer addAnswer = new AddAnswer(QuestionEditation.this);
				addAnswer.setVisible(true);
			}
		});
		addAnswerButton.setBounds(10, 387, 89, 23);
		panel.add(addAnswerButton);

		JButton deleteAnswerButton = new JButton("Smazat");
		deleteAnswerButton.setToolTipText("Sma\u017Ee vybranou odpov\u011B\u010F");
		deleteAnswerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelectedAnswer();
			}
		});
		deleteAnswerButton.setBounds(109, 387, 89, 23);
		panel.add(deleteAnswerButton);

		JButton saveQuestionButton = new JButton("ULO\u017DIT");
		saveQuestionButton.setToolTipText("Ulo\u017E\u00ED upravenou ot\u00E1zku do souboru");
		saveQuestionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCurrentQuestion();
			}
		});
		saveQuestionButton.setBounds(595, 417, 89, 23);
		panel.add(saveQuestionButton);

		setPanel_1(new JPanel());
		getPanel_1().setToolTipText("P\u0159et\u00E1hn\u011Bte sem obr\u00E1zky, soubory typu .jpg, .png a .bmp");
		getPanel_1().setBounds(400, 95, 284, 247);
		getPanel_1().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				panelEventMouseEntered();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				panelEventMouseExited();
			}
		});
		myDragDropListener = new DragDropListener(this);
		// Connect the Panel with a drag and drop listener
		new DropTarget(getPanel_1(), myDragDropListener);
		panel.add(getPanel_1());
		getPanel_1().setLayout(null);

		if (getPathToPic().equals("")) {
			setImagePanel(new ImagePanel());
		} else {
			setImagePanel(new ImagePanel(getPathToPic(), false));
			setPictureIsFromFolder(true);
		}
		getImagePanel().setSize(getPanel_1().getWidth(), getPanel_1().getHeight());
		getPanel_1().add(getImagePanel());

		setAnswerPanel(new ImagePanel());

		JLabel lblNewLabel = new JLabel("P\u0159et\u00E1hn\u011Bte sem obr\u00E1zek");
		lblNewLabel.setBounds(484, 82, 129, 14);
		panel.add(lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 25, 674, 46);
		panel.add(scrollPane);

		setTextQuestion(new JTextArea());
		scrollPane.setViewportView(getTextQuestion());
		getTextQuestion().setFont(new Font("Tahoma", Font.PLAIN, 11));

		setScrollPane_1(new JScrollPane());
		getScrollPane_1().setBounds(10, 95, 380, 281);
		panel.add(getScrollPane_1());

		btnRemovePic = new JButton("Odstranit obr\u00E1zek");
		btnRemovePic.setToolTipText("Odstran\u00ED obr\u00E1zek z n\u00E1hledu, p\u0159\u00EDpadn\u011B i ze slo\u017Eky");
		btnRemovePic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteImageFromPanel();
			}
		});
		btnRemovePic.setBounds(400, 353, 119, 23);
		panel.add(btnRemovePic);

		btnVybratZeSlozky = new JButton("Vybrat ze slo\u017Eky");
		btnVybratZeSlozky.setToolTipText("Vyberte ze slo\u017Eky ji\u017E existuj\u00EDc\u00ED obr\u00E1zek, mus\u00ED b\u00FDt ze slo\u017Eky, kter\u00E1 n\u00E1le\u017E\u00ED testu");
		btnVybratZeSlozky.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectQuestionImageFromFile();
			}
		});
		btnVybratZeSlozky.setBounds(573, 353, 111, 23);
		panel.add(btnVybratZeSlozky);

		btnUpravit = new JButton("Upravit");
		btnUpravit.setToolTipText("Upravit vybranou odpov\u011B\u010F");
		btnUpravit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editAnswer();
			}
		});
		btnUpravit.setBounds(208, 387, 89, 23);
		panel.add(btnUpravit);

		eventLabelQE = new JLabel("");
		eventLabelQE.setBounds(10, 426, 575, 14);
		panel.add(eventLabelQE);

		JLabel lblOtzka = new JLabel("Ot\u00E1zka:");
		lblOtzka.setBounds(10, 11, 53, 14);
		panel.add(lblOtzka);

		JLabel lblOdpovdi = new JLabel("Odpov\u011Bdi:");
		lblOdpovdi.setBounds(10, 82, 68, 14);
		panel.add(lblOdpovdi);

		refreshAnswers();
	}

	class AnswersRenderer extends JPanel implements ListCellRenderer<Object> {
		private static final long serialVersionUID = 4635401484999279341L;

		protected final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
		private final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

		public AnswersRenderer() {
			setOpaque(true);
		}

		private Border getNoFocusBorder() {
			if (System.getSecurityManager() != null) {
				return SAFE_NO_FOCUS_BORDER;
			} else {
				return UIManager.getBorder("List.noFocusBorder");
			}
		}

		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			if (value instanceof Answer) {
				removeAll();
				getAnswerPanel().setImage(((Answer) value).getAnswerPicture(), true);
				StyledDocument document = null;
				if (!((Answer) value).getAnswerPicture().equals("")) {
					document = new DefaultStyledDocument();
					Style defaultStyle = document.getStyle(StyleContext.DEFAULT_STYLE);
					StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_CENTER);
					setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
					
				} else {
					document = new DefaultStyledDocument();
					Style defaultStyle = document.getStyle(StyleContext.DEFAULT_STYLE);
					StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_LEFT);
					setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
				}

				JTextPane textPane = new JTextPane(document);
				textPane.setText((index + 1) + ". " + ((Answer) value).getAnswer());
				textPane.setSize(new Dimension(380, 15));
				textPane.setMaximumSize(new Dimension(380, 1000));
				add(textPane);

				JLabel label = new JLabel();
				label.setIcon(new ImageIcon((BufferedImage) getAnswerPanel().getImage()));
				label.setAlignmentX(Component.CENTER_ALIGNMENT);
				add(label);

				if (((Answer) value).isCorrectAnswer()) {
					setBackground(new Color(142, 236, 143)); // green
					textPane.setBackground(new Color(142, 236, 143));
				} else {
					setBackground(new Color(226, 80, 80)); // red
					textPane.setBackground(new Color(226, 80, 80));
				}
				Border border = null;
				if (cellHasFocus) {
					if (isSelected) {
						border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
					}
					if (border == null) {
						border = UIManager.getBorder("List.focusCellHighlightBorder");
					}
				} else {
					border = getNoFocusBorder();
				}
				setBorder(border);
			}
			return this;
		}
	}

	/**
	 * This method reinitializes JList with answers, updates the list according values in question object
	 */
	public void refreshAnswers() {
		setAnswerList(new JList<Answer>(new ListModelAnswers(getQuestion().getAnswers())));
		getAnswerList().setFixedCellWidth(250);
		getAnswerList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getScrollPane_1().setViewportView(getAnswerList());
		getAnswerList().setSelectedIndex(0);
		getAnswerList().setCellRenderer(new AnswersRenderer());
	}

	/**
	 * Method checks for path, not empty ("") only if image is loaded either from file
	 * or from Drag and Drop event. If true, it will set pathToPic (path for image)
	 * empty, else gets PictureDirectory, loads every file in it, sorts them, takes last
	 * picture and gets last used index in it, then it use to to create new index for this
	 * image and save it to specified folder. <br>
	 * Example of created path: pathToPic ="tests\test\pics\picxxx.jpg"
	 */

	public void convertPicAndSaveToFile() {

		if (getImagePanel().getPath().equals("")) {
			setPathToPic("");
		} else {

			StringBuilder path = getPicsDirectory();
			// get pictures
			FilesInFolder filesInFolder = new FilesInFolder(path.toString());
			filesInFolder.listFilesForFolder(filesInFolder.getFolder(), ".jpg");
			// sort them
			List<File> files = filesInFolder.getFiles();
			int number = getIdForNextImage(files);
			path.append("\\pic" + number + ".jpg");

			// save
			saveImageAsJpg(path);
		}
	}

	/**
	 * Method used in convertPicAndSaveToFile(). Takes created path and save it there as JPG.
	 * 
	 * @param path
	 */
	private void saveImageAsJpg(StringBuilder path) {
		BufferedImage img;
		try {
			if (getImagePanel().getPath().equals("none")) {
				img = getImagePanel().getImage();
			} else {
				img = ImageIO.read(new File(getImagePanel().getPath()));
			}
			File f = new File(path.toString());
			ImageIO.write(img, "JPG", f);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		setPathToPic(path.toString());
	}

	/**
	 * Method used in convertPicAndSaveToFile() for searching for last index of answer's
	 * picture. If there is an error, number will be set to one, as there are no pictures
	 * in that folder yet and this will be first index.
	 * 
	 * @param files
	 * @return
	 */
	private int getIdForNextImage(List<File> files) {
		List<Integer> numbers = new ArrayList<>();
		int number = 1;
		// get last id of picture
		try {
			for (File file : files) {
				if (file.getName().startsWith("pic")) {
					String nameOfFile = file.getName();
					String lastNumber = FilesInFolder.stripNonDigits(nameOfFile);
					numbers.add(Integer.parseInt(lastNumber));
				}
			}

		} catch (NullPointerException ex) {
			number = 1;
		} catch (ArrayIndexOutOfBoundsException ae) {
			number = 1;
		} finally {
			Collections.sort(numbers);
			try {
				number = numbers.get(numbers.size() - 1) + 1;
			} catch (ArrayIndexOutOfBoundsException ex) {
				number = 1;
			}
		}
		return number;
	}

	/**
	 * Method gets loaded path of test, separate substring to get parent folder path,
	 * then adds rest of new path for picture to create folder specially for holding
	 * answer in concrete question folder.
	 * 
	 * @return StringBuilder path of picture directory
	 */
	public StringBuilder getPicsDirectory() {
		StringBuilder pictureDirectoryPath = new StringBuilder();
		pictureDirectoryPath.append(getQuestionSelection().getPath());
		int startIndex = 0;
		int endIndex = pictureDirectoryPath.lastIndexOf("\\");
		String replacement = (pictureDirectoryPath.substring(startIndex, endIndex) + "\\pics");
		pictureDirectoryPath.replace(0, pictureDirectoryPath.length(), replacement);
		File picsDirectory = new File(pictureDirectoryPath.toString());
		if (!picsDirectory.exists()) {
			picsDirectory.mkdir();
		}
		return pictureDirectoryPath;
	}

	/**
	 * Almost same functionality as getPicsDirectory() but this method is used for
	 * check if path of chosen directory is equals to directory with the folder of test. <br>
	 * Used in selectImageFromFolder() method.
	 * 
	 * @see #getPicsDirectory()
	 * @param path
	 * @return StringBuilder
	 */

	public StringBuilder getPicsDirectory(String path) {
		StringBuilder pictureDirectoryPath = new StringBuilder();
		pictureDirectoryPath.append(path);
		StringBuilder originalPath = getPicsDirectory();
		int startIndex = pictureDirectoryPath.lastIndexOf(originalPath.toString());
		int endIndex = pictureDirectoryPath.lastIndexOf("\\");
		String replacement = pictureDirectoryPath.substring(startIndex, endIndex);
		pictureDirectoryPath.replace(0, pictureDirectoryPath.length(), replacement);
		File picsDirectory = new File(pictureDirectoryPath.toString());
		if (!picsDirectory.exists()) {
			picsDirectory.mkdir();
		}
		return pictureDirectoryPath;
	}

	public JDialog getFrmUpravteOtzku() {
		return frmUpravteOtzku;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public JTextArea getTextQuestion() {
		return textQuestion;
	}

	public void setTextQuestion(JTextArea textQuestion) {
		this.textQuestion = textQuestion;
		textQuestion.setWrapStyleWord(true);
		textQuestion.setLineWrap(true);
	}

	public JList<Answer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(JList<Answer> answerList) {
		this.answerList = answerList;
	}

	public JScrollPane getScrollPane_2() {
		return scrollPane_2;
	}

	public String getPathToPic() {
		return pathToPic;
	}

	public void setPathToPic(String pathToPic) {
		this.pathToPic = pathToPic;
	}

	public JButton getBtnRemovePic() {
		return btnRemovePic;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public boolean noPathSet() {
		return (getImagePanel().getPath().equals(""));
	}

	public boolean isPictureIsFromFolder() {
		return pictureIsFromFolder;
	}

	public void setPictureIsFromFolder(boolean pictureIsFromFolder) {
		this.pictureIsFromFolder = pictureIsFromFolder;
	}

	public ImagePanel getAnswerPanel() {
		return answerPanel;
	}

	public void setAnswerPanel(ImagePanel answerPanel) {
		this.answerPanel = answerPanel;
	}

	public ImagePanel getImagePanel() {
		return imagePanel;
	}

	public QuestionSelection getQuestionSelection() {
		return questionSelection;
	}

	public void setQuestionSelection(QuestionSelection questionSelection) {
		this.questionSelection = questionSelection;
	}

	public JLabel getEventLabelQE() {
		return eventLabelQE;
	}

	/**
	 * Method will remove selected Answer from JList, if it contains image,
	 * it will try to delete it first, if succeeded, then it will continue with
	 * deleting answer on the index, if not, then picture not exists and it can continue.
	 * If an error occurs, then is something wrong with picture, probably can not be deletet
	 * for some reason and boolean for identify error will be set to true and it will
	 * forbids deleting from JList. Else it will be deleted from list and refreshed.
	 */
	private void deleteSelectedAnswer() {
		try {
			setIndex(getAnswerList().getSelectedIndex());
			boolean errorDeletedPicture = false;
			getEventLabelQE().setText("");
			try {
				File pic = new File(getAnswerList().getSelectedValue().getAnswerPicture());
				if (pic.delete()) {
					getEventLabelQE().setText("Obrázek i ");
				} else {
					System.out.println("No picture");
				}
			} catch (Exception ex) {
				errorDeletedPicture = true;
				getEventLabelQE().setText("Chyba pøi mazání obrázku odpovìdi, zkuste to znovu");
			}
			if (!errorDeletedPicture) {
				getQuestion().getAnswers().remove(getIndex());
				refreshAnswers();
				getEventLabelQE().setText(getEventLabelQE().getText() + "Otázka úspìšnì smazána");
			}
		} catch (NullPointerException ex) {
			System.out.println("Nebylo nic vybrano");
			getEventLabelQE().setText("Chyba pøi mazání, nebylo nic vybráno");
		}
	}

	/**
	 * Method will try to save current question to the xml file. Checks if there is something
	 * in the text field, cannot be blank question. Then it gets node with the Id of question
	 * saves picture, if it is present and not from folder (to not create duplicates of image)
	 * update Node question with data within Question object in loaded Node and then it will
	 * be save to xml file with the test name.
	 */
	private void saveCurrentQuestion() {
		if (getTextQuestion().getText().equals("")) {
			System.out.println("Prazdna otazka");
			getEventLabelQE().setText("Chyba pøi ukládání, otázka nesmí být bez zadání");

		} else {
			boolean exists = false;
			for (Question question : getQuestionSelection().getXmlFile().getQuestions()) {
				if (question.getQuestion().equals(getTextQuestion().getText().trim())) {
					exists = true;
				}
			}
			if (!exists || !isNewQuestion()) {
				int indexOfQuestion;
				if (isNewQuestion()) {
					getXmlFile().createQuestionNode(getLastId());
					indexOfQuestion = getLastId();
				} else {
					indexOfQuestion = getQuestion().getId();
				}
				if (!isPictureIsFromFolder()) {
					convertPicAndSaveToFile();
				}

				Node questionNode = getXmlFile().getQuestionNode(indexOfQuestion);
				getXmlFile().updateQuestionNode(questionNode, getTextQuestion().getText().trim(), getPathToPic(), getQuestion().getAnswers());
				try {
					ModifyXMLFile.saveToXML(getXmlFile().getDoc(), getXmlFile().getPath());
				} catch (TransformerException e1) {
					e1.printStackTrace();
				}
				getXmlFile().fillQuestionsList();
				getQuestionSelection().refreshListOfQuestions();
				getFrmUpravteOtzku().dispose();
			} else {
				// already exists
				getEventLabelQE().setText("Nelze uložit, text otázky má stejný obsah jako již nìjaká jiná existující otázka");
				System.out.println("Takový název již existuje");
			}
		}
	}

	/**
	 * Method for rendering square and cross when mouse enters panel.
	 * Object is rendered only if no picture is present yet.
	 */
	private void panelEventMouseEntered() {
		if (noPathSet()) {
			Graphics g = getPanel_1().getGraphics();
			int x1 = 8;
			int y1 = 8;
			int x2 = getPanel_1().getWidth() - 10;
			int y2 = getPanel_1().getHeight() - 10;
			g.drawRect(x1, y1, x2, y2);
			g.drawLine(x1, y1, x2, y2);
			g.drawLine(x2, y1, x1, y2);
		}
	}

	/**
	 * Method for removing painted object when mouse exits panel.
	 */
	private void panelEventMouseExited() {
		if (noPathSet()) {
			getPanel_1().repaint();
		}
	}

	/**
	 * This method will remove image from panel. First it will ask, if you wish delete image
	 * form folder too, if yes, it will try to remove picture, it may fail it the image
	 * file is still in use, in no option is selected, it will remove only image from panel
	 * leaving image in the folder, and cancel option will leave it as it is.
	 */
	private void deleteImageFromPanel() {
		int answer = JOptionPane.showConfirmDialog(null, "Chcete smazat obrázek z disku?");
		if (answer == JOptionPane.YES_OPTION) {
			getImagePanel().emptyImage();
			File pic = new File(getPathToPic());
			if (pic.delete()) {
				setPathToPic("");
				getImagePanel().repaint();
				getEventLabelQE().setText("Obrázek úspìšnì smazán ze složky");
			} else {
				getEventLabelQE().setText("Chyba pøi mazání obrázku, zkuste to znovu");
			}

		} else if (answer == JOptionPane.NO_OPTION) {
			getImagePanel().emptyImage();
			setPathToPic("");
			getImagePanel().repaint();
			getEventLabelQE().setText("Obrázek úspìšnì smazán z náhledu");
		} else {
			// zruseno
			System.out.println("zruseno");
			getEventLabelQE().setText("Mazání zrušeno");
		}
	}

	/**
	 * Method for button action performed, when user chooses to get image
	 * from folder. Usually is used Drag and Drop feature, but in the case of
	 * someone removes picture from answer but not from folder, or assign it
	 * to wrong answer, then this function can be called.<br>
	 * <br>
	 * Here are created FileChooser and Filter for being able to choose only
	 * picture files with .jpg prefix. According the option, if the Approve
	 * option is selected, it will compare picture folder with selected folder
	 * (in case of someone tries to choose file from Documents or other non test
	 * folder, program might not find picture after moving tester to somewhere else)
	 * and if it is jpg file, loads it into container.
	 */
	private void selectQuestionImageFromFile() {
		String picturesFolder = getPicsDirectory().toString();

		FileChooser chooseFile = new FileChooser(picturesFolder);
		FileFilter filter = new FileFilter();
		File file = new File("");
		String inputPath;
		filter.getDescription();
		chooseFile.getFileChooser().setFileFilter(filter);
		int returnVal = chooseFile.getFileChooser().showOpenDialog(getFrmUpravteOtzku());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = chooseFile.getFileChooser().getSelectedFile();
			String chosenDirectory = getPicsDirectory(file.getPath()).toString();
			System.out.println(chosenDirectory);
			System.out.println(picturesFolder);
			if (file.getName().toLowerCase().endsWith(".jpg") && chosenDirectory.equals(picturesFolder)) {
				inputPath = chosenDirectory + "\\" + file.getName();
				System.out.println(inputPath);
				getImagePanel().setImage(inputPath, false);
				getImagePanel().repaint();
				setPathToPic(inputPath);
				setPictureIsFromFolder(true);

			} else {
				JOptionPane.showMessageDialog(getFrmUpravteOtzku(), "Vybrán špatný soubor, ze špatné složky, nebo jen bez pøípony .jpg", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			System.out.println("zruseno");
			getEventLabelQE().setText("Výbìr ze složky zrušen");
		}
	}

	/**
	 * This method opens new window with the selected answer.
	 */
	private void editAnswer() {
		try {
			AddAnswer addAnswer = new AddAnswer(QuestionEditation.this, getAnswerList().getSelectedValue());
			addAnswer.setVisible(true);
		} catch (ArrayIndexOutOfBoundsException ex) {
			getEventLabelQE().setText("Chyba pøi výbìru otázky, nebylo nic vybráno");
			System.out.println("nevybrano");
		} catch (NullPointerException ex) {
			System.out.println("nevybrano");
			getEventLabelQE().setText("Chyba pøi výbìru otázky, nebylo nic vybráno");
		}
	}

	/**
	 * @return the scrollPane_1
	 */
	private JScrollPane getScrollPane_1() {
		return scrollPane_1;
	}

	/**
	 * @param scrollPane_1
	 *            the scrollPane_1 to set
	 */
	private void setScrollPane_1(JScrollPane scrollPane_1) {
		this.scrollPane_1 = scrollPane_1;
	}

	/**
	 * @param imagePanel
	 *            the imagePanel to set
	 */
	private void setImagePanel(ImagePanel imagePanel) {
		this.imagePanel = imagePanel;
	}

	/**
	 * @return the index
	 */
	private int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	private void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the newQuestion
	 */
	private boolean isNewQuestion() {
		return newQuestion;
	}

	/**
	 * @param newQuestion
	 *            the newQuestion to set
	 */
	private void setNewQuestion(boolean newQuestion) {
		this.newQuestion = newQuestion;
	}

	/**
	 * @return the xmlFile
	 */
	private ModifyXMLFile getXmlFile() {
		return xmlFile;
	}

	/**
	 * @param xmlFile
	 *            the xmlFile to set
	 */
	private void setXmlFile(ModifyXMLFile xmlFile) {
		this.xmlFile = xmlFile;
	}

	/**
	 * @return the lastId
	 */
	private int getLastId() {
		return lastId;
	}

	/**
	 * @param lastId
	 *            the lastId to set
	 */
	private void setLastId(int lastId) {
		this.lastId = lastId;
	}

	/**
	 * @param frmUpravteOtzku
	 *            the frmUpravteOtzku to set
	 */
	private void setFrmUpravteOtzku(JDialog frmUpravteOtzku) {
		this.frmUpravteOtzku = frmUpravteOtzku;
	}

	/**
	 * @return the panel_1
	 */
	private JPanel getPanel_1() {
		return panel_1;
	}

	/**
	 * @param panel_1
	 *            the panel_1 to set
	 */
	private void setPanel_1(JPanel panel_1) {
		this.panel_1 = panel_1;
	}

}
