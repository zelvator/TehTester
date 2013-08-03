package org.zelvator.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.zelvator.dragImage.DragDropListener;
import org.zelvator.dragImage.ImagePanel;
import org.zelvator.file.FileChooser;
import org.zelvator.file.FileFilter;
import org.zelvator.file.FilesInFolder;
import org.zelvator.questions.answers.Answer;
import org.zelvator.questions.answers.CorrectAnswer;
import org.zelvator.questions.answers.WrongAnswer;
import javax.swing.JScrollPane;

/**
 * Class representing Adding and editing answers. Extends JDialog.
 * 
 * @author zelvator
 * 
 */
public class AddAnswer extends JDialog {

	private static final long serialVersionUID = 5082262550985365821L;
	private final JPanel contentPanel = new JPanel();
	private boolean isCorrect;
	private QuestionEditation questionEditation;
	private JCheckBox chckbxSprvn;
	private ImagePanel answerPanel;
	private JTextArea answerText;
	private boolean pictureFromFolder = false;
	private String answerPicture;
	private DragDropListener myDragDropListener;
	private JPanel panel;
	private Answer answer;
	private boolean newAnswer = true;
	private JLabel eventLabel;

	public AddAnswer() {
		setTitle("Vytvo\u0159it odpov\u011B\u010F");
		setResizable(false);
		init();
	}

	/**
	 * Constructor for creating new Answer
	 * 
	 * @param questionEditation
	 */
	public AddAnswer(QuestionEditation questionEditation) {
		this.setQuestionEditation(questionEditation);
		setAnswerPicture("");
		setCorrect(false);
		init();
	}

	/**
	 * Constructor for editing Answer.
	 * 
	 * @param questionEditation
	 * @param answer
	 */
	public AddAnswer(QuestionEditation questionEditation, Answer answer) {
		this.setAnswer(answer);
		this.setQuestionEditation(questionEditation);
		setNewAnswer(false);
		setCorrect(answer.isCorrectAnswer());
		setAnswerPicture(answer.getAnswerPicture());
		init();
		answerText.setText(answer.getAnswer());
		getChckbxSprvn().setSelected(isCorrect());
	}

	/**
	 * Initialize components.
	 */
	public void init() {
		setModal(true);
		setBounds(100, 100, 360, 400);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);
		{
			JLabel lblVloteOdpov = new JLabel("Vlo\u017Ete odpov\u011B\u010F:");
			lblVloteOdpov.setBounds(10, 11, 80, 14);
			contentPanel.add(lblVloteOdpov);
		}

		setChckbxSprvn(new JCheckBox("Spr\u00E1vn\u00E1"));
		getChckbxSprvn().setToolTipText("Za\u0161krnout spr\u00E1vnou odpov\u011B\u010F");
		getChckbxSprvn().setBounds(10, 80, 97, 23);
		contentPanel.add(getChckbxSprvn());
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 28, 334, 45);
			contentPanel.add(scrollPane);

			answerText = new JTextArea();
			answerText.setLineWrap(true);
			scrollPane.setViewportView(answerText);
			answerText.setFont(new Font("Tahoma", Font.PLAIN, 11));
		}

		setPanel(new JPanel());
		getPanel().setToolTipText("P\u0159et\u00E1hn\u011Bte sem obr\u00E1zky, soubory typu .jpg, .png a .bmp");
		getPanel().setBounds(20, 110, 313, 165);
		myDragDropListener = new DragDropListener(this);
		// Connect the Panel with a drag and drop listener
		new DropTarget(getPanel(), myDragDropListener);
		contentPanel.add(getPanel());
		getPanel().setLayout(null);

		if (getAnswerPicture().equals("")) {
			setAnswerPanel(new ImagePanel());
		} else {
			setAnswerPanel(new ImagePanel(getAnswerPicture(), false));
			setPictureIsFromFolder(true);
		}
		getAnswerPanel().setBounds(0, 0, 284, 156);
		getPanel().add(getAnswerPanel());
		getAnswerPanel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				answerPanelEventMouseEntered();
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				answerPanelEventMouseExited();
			}
		});

		JButton btnVybratZeSloky = new JButton("Vybrat ze slo\u017Eky");
		btnVybratZeSloky.setToolTipText("Vyberte ze slo\u017Eky ji\u017E existuj\u00EDc\u00ED obr\u00E1zek, mus\u00ED b\u00FDt ze slo\u017Eky, kter\u00E1 n\u00E1le\u017E\u00ED ot\u00E1zce");
		btnVybratZeSloky.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectImageFromFolder();
			}
		});
		btnVybratZeSloky.setBounds(10, 286, 111, 23);
		contentPanel.add(btnVybratZeSloky);

		JButton btnOdstranitObrzek = new JButton("Odstranit obr\u00E1zek");
		btnOdstranitObrzek.setToolTipText("Odstran\u00ED obr\u00E1zek z n\u00E1hledu, p\u0159\u00EDpadn\u011B i ze slo\u017Eky");
		btnOdstranitObrzek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteImageFromAnswerPanel();
			}
		});
		btnOdstranitObrzek.setBounds(220, 286, 124, 23);
		contentPanel.add(btnOdstranitObrzek);

		JLabel lblPethnteSemObrzek = new JLabel("P\u0159et\u00E1hn\u011Bte sem obr\u00E1zek");
		lblPethnteSemObrzek.setBounds(117, 84, 124, 14);
		contentPanel.add(lblPethnteSemObrzek);

		eventLabel = new JLabel("");
		eventLabel.setBounds(10, 320, 334, 14);
		contentPanel.add(eventLabel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveAnswer();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);

			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AddAnswer.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * Method checks for path, not empty ("") only if image is loaded either from file
	 * or from Drag and Drop event. If true, it will set answerPicture (path for image)
	 * empty, else gets PictureDirectory, loads every file in it, sorts them, takes last
	 * picture and gets last used index in it, then it use to to create new index for this
	 * image and save it to specified folder. <br>
	 * Example of created path: answerPicture ="tests\test\pics\otazka id x\answerxxx.jpg"
	 */
	public void convertPicAndSaveToFile() {

		if (getAnswerPanel().getPath().equals("")) {
			setAnswerPicture("");
		} else {

			StringBuilder path = getPicsDirectory();
			// get pictures
			FilesInFolder filesInFolder = new FilesInFolder(path.toString());
			filesInFolder.listFilesForFolder(filesInFolder.getFolder(), ".jpg");
			// sort them
			List<File> files = filesInFolder.getFiles();
			int number = getNextIdForAnswerImage(files);
			path.append("\\answer" + number + ".jpg");

			// save
			saveJpgImage(path);
		}
	}

	/**
	 * Method used in convertPicAndSaveToFile(). Takes created path and save it there as JPG.
	 * 
	 * @param path
	 */
	private void saveJpgImage(StringBuilder path) {
		BufferedImage img;
		try {
			if (getAnswerPanel().getPath().equals("none")) {
				img = getAnswerPanel().getImage();
			} else {
				img = ImageIO.read(new File(getAnswerPanel().getPath()));
			}
			File f = new File(path.toString());
			ImageIO.write(img, "JPG", f);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		setAnswerPicture(path.toString());
	}

	/**
	 * Method used in convertPicAndSaveToFile() for searching for last index of answer's
	 * picture. If there is an error, number will be set to one, as there are no pictures
	 * in that folder yet and this will be first index.
	 * 
	 * @param files
	 * @return
	 */
	private int getNextIdForAnswerImage(List<File> files) {
		List<Integer> numbers = new ArrayList<>();
		int number = 1;
		// get last id of picture
		try {
			for (File file : files) {
				String nameOfFile = file.getName();
				String lastNumber = FilesInFolder.stripNonDigits(nameOfFile);
				numbers.add(Integer.parseInt(lastNumber));
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
	 * then adds rest of new path for picture + generated Id of question to create folder
	 * specially for holding answer in concrete question folder.
	 * 
	 * @return StringBuilder path of picture directory
	 */
	public StringBuilder getPicsDirectory() {
		StringBuilder pictureDirectoryPath = new StringBuilder();
		pictureDirectoryPath.append(getQuestionEditation().getQuestionSelection().getPath());
		int startIndex = 0;
		int endIndex = pictureDirectoryPath.lastIndexOf("\\");
		int questionId = getQuestionEditation().getQuestion().getId();
		String replacement = (pictureDirectoryPath.substring(startIndex, endIndex) + "\\pics\\otazka id " + questionId);
		pictureDirectoryPath.replace(0, pictureDirectoryPath.length(), replacement);
		File picsDirectory = new File(pictureDirectoryPath.toString());
		if (!picsDirectory.exists()) {
			picsDirectory.mkdirs();
		}
		return pictureDirectoryPath;
	}

	/**
	 * Almost same functionality as getPicsDirectory() but this method is used for
	 * check if path of chosen directory is equals to directory with the folder of question. <br>
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

	public ImagePanel getAnswerPanel() {
		return answerPanel;
	}

	public JTextArea getAnswerText() {
		return answerText;
	}

	public void setPictureIsFromFolder(boolean tof) {
		this.setPictureFromFolder(tof);
	}

	public boolean getPictureFromFolder() {
		return isPictureFromFolder();
	}

	public String getAnswerPicture() {
		return answerPicture;
	}

	public void setAnswerPicture(String answerPicture) {
		this.answerPicture = answerPicture;
	}

	public boolean noPathSet() {
		return (getAnswerPanel().getPath().equals(""));
	}

	public JDialog getAddAnswerDialog() {
		return this;
	}

	public JPanel getPanel() {
		return panel;
	}

	public JLabel getEventLabel() {
		return eventLabel;
	}

	/**
	 * Method for rendering square and cross when mouse enters panel.
	 * Object is rendered only if no picture is present yet.
	 */
	private void answerPanelEventMouseEntered() {
		if (noPathSet()) {
			Graphics g = getPanel().getGraphics();
			int x1 = 8;
			int y1 = 8;
			int x2 = getPanel().getWidth() - 10;
			int y2 = getPanel().getHeight() - 10;
			g.drawRect(x1, y1, x2, y2);
			g.drawLine(x1, y1, x2, y2);
			g.drawLine(x2, y1, x1, y2);
		}
	}

	/**
	 * Method for removing painted object when mouse exits panel.
	 */
	private void answerPanelEventMouseExited() {
		if (noPathSet()) {
			getPanel().repaint();
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
	 * 
	 */
	private void selectImageFromFolder() {
		String picturesFolder = getPicsDirectory().toString();

		FileChooser chooseFile = new FileChooser(picturesFolder);
		FileFilter filter = new FileFilter();
		File file = new File("");
		String inputPath;
		filter.getDescription();
		chooseFile.getFileChooser().setFileFilter(filter);
		int returnVal = chooseFile.getFileChooser().showOpenDialog(getAddAnswerDialog());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = chooseFile.getFileChooser().getSelectedFile();
			String chosenDirectory = getPicsDirectory(file.getPath()).toString();
			System.out.println(chosenDirectory);
			System.out.println(picturesFolder);
			if (file.getName().toLowerCase().endsWith(".jpg") && chosenDirectory.equals(picturesFolder)) {
				inputPath = chosenDirectory + "\\" + file.getName();
				System.out.println(inputPath);
				getAnswerPanel().setImage(inputPath, false);
				getAnswerPanel().repaint();
				setAnswerPicture(inputPath);
				setPictureIsFromFolder(true);
				getEventLabel().setText("Obrázek úspìšnì naèten");
			} else {
				JOptionPane.showMessageDialog(getAddAnswerDialog(), "Vybrán špatný soubor, ze špatné složky, nebo jen bez pøípony .jpg", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			getEventLabel().setText("Naètení obrázku zrušeno");
			System.out.println("zruseno");
		}
	}

	/**
	 * Method will simply delete selected answer. If the picture is present, it will try to
	 * delete picture too, but it can fail, if the picture is still in use by program, then
	 * you have to try to delete it again later.
	 */
	private void deleteImageFromAnswerPanel() {
		int answer = JOptionPane.showConfirmDialog(null, "Chcete smazat obrázek z disku?");
		if (answer == JOptionPane.YES_OPTION) {
			getAnswerPanel().emptyImage();
			File pic = new File(getAnswerPicture());
			if (pic.delete()) {
				getEventLabel().setText("Obrázek smazán ze složky");
				setAnswerPicture("");
				getAnswerPanel().repaint();
			} else {
				getEventLabel().setText("Chyba pøi mazání obrázku ze složky, zkuste to prosím znova");
			}
		} else if (answer == JOptionPane.NO_OPTION) {
			getAnswerPanel().emptyImage();
			setAnswerPicture("");
			getAnswerPanel().repaint();
			getEventLabel().setText("Obrázek smazán, zachován ve složce");
		} else {
			// zruseno
			getEventLabel().setText("Mazání zrušeno");
			System.out.println("zruseno");
		}
	}

	/**
	 * Method for saving answer to the question. There are also checks if
	 * it is new question, to not creating new item in JList, if picture is
	 * changed and it is in the folder, do not create new image, like pic02.jpg
	 * if 01 exists and it is same, and creating wrong/bad answer objects for
	 * saving it in xml file.
	 */
	private void saveAnswer() {
		if (getAnswerText().getText().equals("")) {
			// prazdny
			getEventLabel().setText("Nelze uložit, nezadali jste text k odpovìdi");
			System.out.println("Prazdno");
		} else {

			boolean exists = false;
			for (Answer ans : getQuestionEditation().getQuestion().getAnswers()) {
				if (ans.getAnswer().equals(getAnswerText().getText().trim())) {
					exists = true;
				}
			}
			if (!exists || !isNewAnswer()) {
				if (!isPictureFromFolder()) {
					convertPicAndSaveToFile();
				}
				setCorrect(getChckbxSprvn().isSelected());
				if (!isNewAnswer()) {
					getAnswer().setAnswer(getAnswerText().getText().trim());
					getAnswer().setAnswerPicture(getAnswerPicture());
					getAnswer().setCorrectAnswer(isCorrect());
				} else if (isCorrect()) {
					getQuestionEditation().getQuestion().getAnswers().add(new CorrectAnswer(getAnswerText().getText().trim(), getAnswerPicture()));
				} else {
					getQuestionEditation().getQuestion().getAnswers().add(new WrongAnswer(getAnswerText().getText().trim(), getAnswerPicture()));
				}
				getQuestionEditation().refreshAnswers();
				AddAnswer.this.dispose();
			} else {
				// already exists
				getEventLabel().setText("Nelze uložit, text odpovìdi je stejný jako jedna z dalších odpovìdí");
				System.out.println("Takový název již existuje");
			}
		}
	}

	/**
	 * @param answerPanel
	 *            the answerPanel to set
	 */
	private void setAnswerPanel(ImagePanel answerPanel) {
		this.answerPanel = answerPanel;
	}

	/**
	 * @return the questionEditation
	 */
	private QuestionEditation getQuestionEditation() {
		return questionEditation;
	}

	/**
	 * @param questionEditation
	 *            the questionEditation to set
	 */
	private void setQuestionEditation(QuestionEditation questionEditation) {
		this.questionEditation = questionEditation;
	}

	/**
	 * @param panel
	 *            the panel to set
	 */
	private void setPanel(JPanel panel) {
		this.panel = panel;
	}

	/**
	 * @return the answer
	 */
	private Answer getAnswer() {
		return answer;
	}

	/**
	 * @param answer
	 *            the answer to set
	 */
	private void setAnswer(Answer answer) {
		this.answer = answer;
	}

	/**
	 * @return the newAnswer
	 */
	private boolean isNewAnswer() {
		return newAnswer;
	}

	/**
	 * @param newAnswer
	 *            the newAnswer to set
	 */
	private void setNewAnswer(boolean newAnswer) {
		this.newAnswer = newAnswer;
	}

	/**
	 * @return the pictureFromFolder
	 */
	private boolean isPictureFromFolder() {
		return pictureFromFolder;
	}

	/**
	 * @param pictureFromFolder
	 *            the pictureFromFolder to set
	 */
	private void setPictureFromFolder(boolean pictureFromFolder) {
		this.pictureFromFolder = pictureFromFolder;
	}

	/**
	 * @return the isCorrect
	 */
	private boolean isCorrect() {
		return isCorrect;
	}

	/**
	 * @param isCorrect
	 *            the isCorrect to set
	 */
	private void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	/**
	 * @return the chckbxSprvn
	 */
	private JCheckBox getChckbxSprvn() {
		return chckbxSprvn;
	}

	/**
	 * @param chckbxSprvn
	 *            the chckbxSprvn to set
	 */
	private void setChckbxSprvn(JCheckBox chckbxSprvn) {
		this.chckbxSprvn = chckbxSprvn;
	}
}
