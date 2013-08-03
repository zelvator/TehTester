package org.zelvator.tester;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.zelvator.dragImage.ImagePanel;
import org.zelvator.editor.TestEditation;
import org.zelvator.models.ListModelAnswers;
import org.zelvator.questions.Question;
import org.zelvator.questions.answers.Answer;
import org.zelvator.timer.TestTimer;

/**
 * This Frame represents The Tester, which loads questions from file into array and
 * randomly set questions to the end of the list.
 * 
 * @author zelvator
 * 
 */
public class Tester extends JFrame {

	private static final long serialVersionUID = 2490924705284481582L;
	private TestEditation testEditation;
	private JPanel contentPane;
	private JPanel panel_1;
	private Question question;
	private List<Question> questions = new ArrayList<>();
	private JList<Answer> listOfAnswers;
	private JScrollPane scrollPane_1;
	private ImagePanel imagePanel;
	private int index;
	private JTextArea questionText;
	private JLabel selectedAnswers;
	private MaximizedImage maximizedImage;
	private ImagePanel answerPanel;
	private TestTimer testTimer;

	private int totalQuestions;
	private int completedQuestions = 0;
	private double totalPoints = 0;
	private double correctPoints = 0;
	private double wrongPoints = 0;
	private double totalPercent = 0;
	private double percentage = 0;
	private String nameOfTest;
	private JLabel lblPocetOtazek;
	private JLabel lblBody;
	private JLabel lblProcent;
	private JLabel lblVybrano;
	private JLabel lblQuestions;
	private JLabel lblPercentage;
	private JLabel lblPoints;
	private JButton btnNext;
	private JButton btnZptNaTesty;
	private JLabel lblS;
	private JButton btnPauza;
	private JButton btnPokrauj;
	private JLabel lblType;

	/**
	 * Constructor loads questions to the list, set number of total questions and set score to 0 for all.
	 * 
	 * @param questions
	 * @param testEditation
	 * @param name
	 */
	public Tester(List<Question> questions, TestEditation testEditation, String name) {
		setMaximumSize(new Dimension(750, 450));
		setMinimumSize(new Dimension(505, 450));
		this.testEditation = testEditation;
		this.setQuestions(questions);
		this.nameOfTest = name;
		init();
		loadQuestion();
		setTotalQuestions(questions.size());
		setScore(0, 0, 0, 0);
	}

	/**
	 * Initialize components.
	 */
	public void init() {
		setSize(new Dimension(700, 450));
		setResizable(false);
		setTitle("Test: " + nameOfTest);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, 744, 421);
		contentPane.add(panel);

		setPanel_1(new JPanel());
		getPanel_1().setLayout(null);
		getPanel_1().setBounds(504, 93, 230, 219);
		panel.add(getPanel_1());

		setMaximizedImage(new MaximizedImage(""));

		setImagePanel(new ImagePanel());
		getImagePanel().setToolTipText("Dvojklikem zv\u011Bt\u0161\u00EDte obr\u00E1zek");
		getImagePanel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					if (!getMaximizedImage().isVisible()) {
						setMaximizedImage(new MaximizedImage(getQuestion().getPathToPic()));
						getMaximizedImage().setVisible(true);
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				getImagePanel().setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				getImagePanel().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		getImagePanel().setSize(getPanel_1().getWidth(), getPanel_1().getHeight());
		getPanel_1().add(getImagePanel());

		setAnswerPanel(new ImagePanel());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 25, 484, 57);
		panel.add(scrollPane);

		setQuestionText(new JTextArea());
		getQuestionText().setEditable(false);
		getQuestionText().setFont(new Font("Tahoma", Font.PLAIN, 11));
		scrollPane.setViewportView(getQuestionText());

		setScrollPane_1(new JScrollPane());
		getScrollPane_1().setBounds(10, 93, 484, 257);
		panel.add(getScrollPane_1());

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 361, 484, 49);
		panel.add(panel_2);
		panel_2.setLayout(null);

		lblPocetOtazek = new JLabel("Po\u010Det ot\u00E1zek:");
		lblPocetOtazek.setBounds(0, 0, 81, 14);
		panel_2.add(lblPocetOtazek);

		lblQuestions = new JLabel("0/0");
		lblQuestions.setBounds(86, 0, 84, 14);
		panel_2.add(lblQuestions);

		lblBody = new JLabel("Body:");
		lblBody.setBounds(0, 13, 43, 14);
		panel_2.add(lblBody);
		lblBody.setAlignmentX(0.5f);

		lblPoints = new JLabel("0/0/0");
		lblPoints.setToolTipText("spr\u00E1vn\u011B/\u0161patn\u011B/celkem");
		lblPoints.setBounds(86, 13, 84, 14);
		panel_2.add(lblPoints);
		lblPoints.setAlignmentX(0.5f);

		lblProcent = new JLabel("Procent:");
		lblProcent.setBounds(180, 0, 43, 14);
		panel_2.add(lblProcent);

		lblPercentage = new JLabel("0 %");
		lblPercentage.setBounds(233, 0, 43, 14);
		panel_2.add(lblPercentage);

		btnNext = new JButton("Dal\u0161\u00ED");
		btnNext.setToolTipText("Vyhodnotit a dal\u0161\u00ED");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				getBtnPauza().doClick();
				result();
			}
		});
		btnNext.setBounds(394, 0, 90, 23);
		panel_2.add(btnNext);

		lblVybrano = new JLabel("Vybr\u00E1no:");
		lblVybrano.setAlignmentX(0.5f);
		lblVybrano.setBounds(180, 13, 63, 14);
		panel_2.add(lblVybrano);

		setSelectedAnswers(new JLabel(""));
		getSelectedAnswers().setAlignmentX(0.5f);
		getSelectedAnswers().setBounds(233, 13, 81, 14);
		panel_2.add(getSelectedAnswers());

		btnZptNaTesty = new JButton("Zp\u011Bt na testy");
		btnZptNaTesty.setToolTipText("Vr\u00E1tit zp\u011Bt na testy");
		btnZptNaTesty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testEditation.getFrmUpravitTest().setVisible(true);
				dispose();
			}
		});
		btnZptNaTesty.setVisible(false);
		btnZptNaTesty.setBounds(387, 0, 97, 23);
		panel_2.add(btnZptNaTesty);

		btnPauza = new JButton("");
		btnPauza.setToolTipText("Pauza");
		ImageIcon pauseIcon = setIconPauseButton();
		btnPauza.setIcon(pauseIcon);
		btnPauza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testTimer.setPause(true);
				btnPauza.setVisible(false);
				btnPokrauj.setVisible(true);
			}
		});
		btnPauza.setBounds(180, 25, 34, 24);
		panel_2.add(btnPauza);

		btnPokrauj = new JButton("");
		btnPokrauj.setToolTipText("Pokra\u010Dovat");
		ImageIcon continueIcon = setIconPlayButton();
		btnPokrauj.setIcon(continueIcon);
		btnPokrauj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testTimer.setPause(false);
				btnPauza.setVisible(true);
				btnPokrauj.setVisible(false);
			}
		});
		btnPokrauj.setBounds(180, 25, 34, 24);
		panel_2.add(btnPokrauj);

		lblS = new JLabel("0 s");
		lblS.setBounds(86, 27, 84, 14);
		panel_2.add(lblS);
		testTimer = new TestTimer(lblS);

		JLabel lblas = new JLabel("\u010Cas:");
		lblas.setBounds(0, 25, 43, 16);
		panel_2.add(lblas);

		setListOfAnswers(new JList<Answer>(new ListModelAnswers(new ArrayList<Answer>())));
		getListOfAnswers().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getScrollPane_1().setViewportView(getListOfAnswers());
		getListOfAnswers().setCellRenderer(new AnswersRenderer());

		lblType = new JLabel("Typ:");
		lblType.setBounds(10, 11, 426, 14);
		panel.add(lblType);
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
				textPane.setText(((Answer) value).getAnswer());
				textPane.setSize(new Dimension(getScrollPane_1().getWidth() - 20, 10));
				textPane.setMaximumSize(new Dimension(getScrollPane_1().getWidth() - 20, 1000));
				add(textPane);

				JLabel label = new JLabel();
				label.setIcon(new ImageIcon((BufferedImage) getAnswerPanel().getImage()));
				add(label);

				Border border = null;
				if (cellHasFocus) {
					if (isSelected) {
						setBackground(new Color(51, 153, 255)); //blue
						textPane.setBackground(new Color(51, 153, 255));
						border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
						textPane.setForeground(Color.white);
					}
					if (border == null) {
						setBackground(new Color(51, 153, 255)); 
						textPane.setBackground(new Color(51, 153, 255));
						textPane.setForeground(Color.white);
						border = UIManager.getBorder("List.focusCellHighlightBorder");
					}
				} else {
					border = getNoFocusBorder();
					setBackground(Color.white); 
					textPane.setBackground(Color.white);
					textPane.setForeground(Color.black);
				}
				setBorder(border);
			}
			return this;
		}
	}
	/**
	 * Method for setting Icon for PlayButton.
	 * 
	 * @return ImageIcon
	 */
	private ImageIcon setIconPlayButton() {
		Image continueImage;
		ImageIcon continueIcon = null;
		try {
			continueImage = ImageIO.read(getClass().getResource("/Play.png"));
			continueIcon = new ImageIcon(continueImage);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return continueIcon;
	}

	/**
	 * Method for setting Icon for PauseButton.
	 * 
	 * @return ImageIcon
	 */
	private ImageIcon setIconPauseButton() {
		Image pauseImage;
		ImageIcon pauseIcon = null;
		try {
			pauseImage = ImageIO.read(getClass().getResource("/Pause.png"));
			pauseIcon = new ImageIcon(pauseImage);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return pauseIcon;
	}

	/**
	 * Method will change window depending on if there is some image for this question or not.
	 * The code will resize window and set image panel to not visible, if someone could manage
	 * to get over locked resizing somehow.
	 */
	public void changeWindow() {
		if (getQuestion().getPathToPic().equals("")) {
			getPanel_1().setVisible(false);
			getImagePanel().emptyImage();
			setSize(getMinimumSize());
		} else {
			getPanel_1().setVisible(true);
			getImagePanel().setImage(getQuestion().getPathToPic(), false);
			getImagePanel().repaint();
			setSize(getMaximumSize());
		}
	}

	/**
	 * Method for deciding if the question has more answers or only one.
	 * 
	 * @return true or false
	 */
	public boolean moreThanOneAnswer() {
		int count = 0;
		for (Answer answer : getQuestion().getAnswers()) {
			if (answer.isCorrectAnswer()) {
				count++;
			}
		}
		if (count > 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method will generate new question, if there are some in the list.
	 */
	public void setNewQuestion() {
		if (getQuestions().size() > 0) {
			getNextQuestionId();
			setQuestion(getQuestions().get(getIndex()));
			long seed = System.nanoTime();
			Collections.shuffle(getQuestion().getAnswers(), new Random(seed));
		}
	}

	/**
	 * Method for generating random number in the range of 1 and last index of the array list.
	 */
	public void getNextQuestionId() {
		Random rand = new Random();
		int min = 1;
		int max = getQuestions().size() - 1;
		int randomNum;

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		if (getQuestions().size() == 1) {
			randomNum = 0;
		} else {
			randomNum = rand.nextInt(max - min + 1) + min;
		}
		setIndex(randomNum);
	}

	/**
	 * This method will load question to the labels, Jlist and resize window if needed.
	 */
	public void loadQuestion() {
		setNewQuestion();
		changeWindow();
		getQuestionText().setText(getQuestion().getQuestion());
		System.out.println(getQuestion().getPathToPic());
		if (moreThanOneAnswer()) {
			refreshMultipleAnswers();
			getLblType().setText("Vyberte více odpovìdí:");
		} else {
			refreshAnswers();
			getLblType().setText("Vyberte pouze jednu odpovìï:");
		}
	}

	/**
	 * This method will redirect you to the AnswerReaction to see result.
	 */
	public void result() {
		AnswerReaction answerReaction = new AnswerReaction(this, getListOfAnswers().getSelectedValuesList(), getQuestion().getAnswers());
		answerReaction.setVisible(true);
		if (getMaximizedImage() != null) {
			getMaximizedImage().dispose();
		}
	}

	/**
	 * This method is for representing selected answers in the JList like 1./2./...
	 */
	public void changeSelectedAnswers() {
		StringBuilder indexes = new StringBuilder();
		if (getListOfAnswers().getSelectedIndex() == -1) {
			indexes.append("");
			getSelectedAnswers().setText(indexes.toString());
		} else {
			for (int index : getListOfAnswers().getSelectedIndices()) {
				indexes.append((index + 1) + "./");
			}
			indexes.delete(indexes.lastIndexOf("/"), indexes.length());
			getSelectedAnswers().setText(indexes.toString());
		}
	}

	/**
	 * Method will take some parameters from AnswerReaction where percentage score is calculated and
	 * here it is added and calculated average percentage of all completed questions.
	 * 
	 * @param correctAnswers
	 * @param totalAnwers
	 * @param percent
	 */
	public void setScore(double correctAnswers, double wrongAnswers, double totalAnwers, double percent) {
		setCompletedQuestions(getTotalQuestions() - getQuestions().size() + 1);
		setCorrectPoints(getCorrectPoints() + correctAnswers);
		setWrongPoints(getWrongPoints() + wrongAnswers);
		setTotalPoints(getTotalPoints() + totalAnwers);
		setTotalPercent(getTotalPercent() + percent);

		setPercentage((getTotalPercent()) / (getCompletedQuestions()));
		setPercentage(Math.round(getPercentage() * 100.0) / 100.0);
		getLblPoints().setText("<html><font color=green>" + (int) getCorrectPoints() 
				+ "</font> / <font color=red>" + (int) getWrongPoints() + "</font> / "
				+ "<font color=black>" + (int) getTotalPoints() + "</html>");
		getLblQuestions().setText(getCompletedQuestions() + "/" + getTotalQuestions());
		getLblPercentage().setText(getPercentage() + " %");
	}

	/**
	 * This method will update and refresh JList with answers, for this particular
	 * method is needed to make possible to select multiple answers. I thought it
	 * would be better to select it with your mouse without holding CTRL key. So
	 * this will make it possible. Additionally it can render images in here, as
	 * the other refreshSomethingInJList methods.
	 */
	public void refreshMultipleAnswers() {
		/*
		 * http://java-swing-tips.blogspot.cz/2012/04/select-multiple-items-in-jlist
		 * -by.html
		 */
		setListOfAnswers(new JList<Answer>(new ListModelAnswers(getQuestion().getAnswers())) {
			private static final long serialVersionUID = -8104047021819918891L;

			@Override
			public void updateUI() {
				setForeground(null);
				setBackground(null);
				setSelectionForeground(null);
				setSelectionBackground(null);
				super.updateUI();
			}

			@Override
			protected void processMouseMotionEvent(MouseEvent e) {
				super.processMouseMotionEvent(convertMouseEvent(e));
			}

			@Override
			protected void processMouseEvent(MouseEvent e) {
				if (e.getID() == MouseEvent.MOUSE_ENTERED || e.getID() == MouseEvent.MOUSE_EXITED) {
					super.processMouseEvent(e);
				} else {
					if (getCellBounds(0, getModel().getSize() - 1).contains(e.getPoint())) {
						super.processMouseEvent(convertMouseEvent(e));
					} else {
						e.consume();
						requestFocusInWindow();
					}
				}
			}

			private MouseEvent convertMouseEvent(MouseEvent e) {
				// Thread: JList where mouse click acts like ctrl-mouse click
				// http://forums.oracle.com/forums/thread.jspa?messageID=5692411
				return new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(),
				// e.getModifiers() | InputEvent.CTRL_MASK,
				// select multiple objects in OS X: Command+click
				// pointed out by nsby
						e.getModifiers() | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
			}
		});
		getListOfAnswers().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				changeSelectedAnswers();
			}
		});
		getScrollPane_1().setViewportView(getListOfAnswers());
		getListOfAnswers().setSelectedIndex(-1);
		changeSelectedAnswers();
		getListOfAnswers().setCellRenderer(new AnswersRenderer());
	}

	/**
	 * This method is used for updating JList, where only one answer is correct.
	 */
	public void refreshAnswers() {
		setListOfAnswers(new JList<Answer>(new ListModelAnswers(getQuestion().getAnswers())));
		getListOfAnswers().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				changeSelectedAnswers();
			}
		});
		getListOfAnswers().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getScrollPane_1().setViewportView(getListOfAnswers());
		getListOfAnswers().setSelectedIndex(-1);
		changeSelectedAnswers();
		getListOfAnswers().setCellRenderer(new AnswersRenderer());
	}

	public JLabel getLblQuestions() {
		return lblQuestions;
	}

	public JLabel getLblPercentage() {
		return lblPercentage;
	}

	public JLabel getLblPoints() {
		return lblPoints;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public int getIndex() {
		return index;
	}

	public JList<Answer> getListOfAnswers() {
		return listOfAnswers;
	}

	public JTextArea getQuestionText() {
		return questionText;
	}

	public JButton getBtnNext() {
		return btnNext;
	}

	public JButton getBtnZptNaTesty() {
		return btnZptNaTesty;
	}

	public ImagePanel getImagePanel() {
		return imagePanel;
	}

	public Tester getTester() {
		return this;
	}

	public JPanel getPanel_1() {
		return panel_1;
	}

	public TestTimer getTestTimer() {
		return testTimer;
	}

	public JButton getBtnPauza() {
		return btnPauza;
	}

	public JButton getBtnPokrauj() {
		return btnPokrauj;
	}

	public JLabel getLblType() {
		return lblType;
	}

	/**
	 * @return the question
	 */
	private Question getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	private void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @param panel_1
	 *            the panel_1 to set
	 */
	private void setPanel_1(JPanel panel_1) {
		this.panel_1 = panel_1;
	}

	/**
	 * @param imagePanel
	 *            the imagePanel to set
	 */
	private void setImagePanel(ImagePanel imagePanel) {
		this.imagePanel = imagePanel;
		imagePanel.setLocation(0, 0);
	}

	/**
	 * @param questions
	 *            the questions to set
	 */
	private void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	private void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @param questionText
	 *            the questionText to set
	 */
	private void setQuestionText(JTextArea questionText) {
		this.questionText = questionText;
		questionText.setLineWrap(true);
	}

	/**
	 * @param listOfAnswers
	 *            the listOfAnswers to set
	 */
	private void setListOfAnswers(JList<Answer> listOfAnswers) {
		this.listOfAnswers = listOfAnswers;
		listOfAnswers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					String path = getListOfAnswers().getSelectedValue().getAnswerPicture();
					if (!getMaximizedImage().isVisible()) {
						if (!path.equals("")) {
							setMaximizedImage(new MaximizedImage(path));
							getMaximizedImage().setVisible(true);
						}
					}
				}
			}
		});
	}

	/**
	 * @return the maximizedImage
	 */
	private MaximizedImage getMaximizedImage() {
		return maximizedImage;
	}

	/**
	 * @param maximizedImage
	 *            the maximizedImage to set
	 */
	private void setMaximizedImage(MaximizedImage maximizedImage) {
		this.maximizedImage = maximizedImage;
	}

	/**
	 * @return the selectedAnswers
	 */
	private JLabel getSelectedAnswers() {
		return selectedAnswers;
	}

	/**
	 * @param selectedAnswers
	 *            the selectedAnswers to set
	 */
	private void setSelectedAnswers(JLabel selectedAnswers) {
		this.selectedAnswers = selectedAnswers;
	}

	/**
	 * @return the completedQuestions
	 */
	private int getCompletedQuestions() {
		return completedQuestions;
	}

	/**
	 * @param completedQuestions
	 *            the completedQuestions to set
	 */
	private void setCompletedQuestions(int completedQuestions) {
		this.completedQuestions = completedQuestions;
	}

	/**
	 * @return the correctPoints
	 */
	private double getCorrectPoints() {
		return correctPoints;
	}

	/**
	 * @param correctPoints
	 *            the correctPoints to set
	 */
	private void setCorrectPoints(double correctPoints) {
		this.correctPoints = correctPoints;
	}

	/**
	 * @return the totalPoints
	 */
	private double getTotalPoints() {
		return totalPoints;
	}

	/**
	 * @param totalPoints
	 *            the totalPoints to set
	 */
	private void setTotalPoints(double totalPoints) {
		this.totalPoints = totalPoints;
	}

	/**
	 * @return the totalPercent
	 */
	private double getTotalPercent() {
		return totalPercent;
	}

	/**
	 * @param totalPercent
	 *            the totalPercent to set
	 */
	private void setTotalPercent(double totalPercent) {
		this.totalPercent = totalPercent;
	}

	/**
	 * @return the percentage
	 */
	private double getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage
	 *            the percentage to set
	 */
	private void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the totalQuestions
	 */
	private int getTotalQuestions() {
		return totalQuestions;
	}

	/**
	 * @param totalQuestions
	 *            the totalQuestions to set
	 */
	private void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
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
	 * @return the answerPanel
	 */
	private ImagePanel getAnswerPanel() {
		return answerPanel;
	}

	/**
	 * @param answerPanel
	 *            the answerPanel to set
	 */
	private void setAnswerPanel(ImagePanel answerPanel) {
		this.answerPanel = answerPanel;
	}

	public double getWrongPoints() {
		return wrongPoints;
	}

	public void setWrongPoints(double wrongPoints) {
		this.wrongPoints = wrongPoints;
	}
}
