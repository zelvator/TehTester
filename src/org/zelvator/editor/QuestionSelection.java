package org.zelvator.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Node;
import org.zelvator.file.ModifyXMLFile;
import org.zelvator.models.FilteredListModel;
import org.zelvator.models.ListModelQuestions;
import org.zelvator.questions.Question;

/**
 * Frame responsible for selection and modification of questions.
 * 
 * @author zelvator
 * 
 */
public class QuestionSelection {

	private JFrame frmVyberteOtzku;
	private ModifyXMLFile xmlFile;

	JPanel panel;
	private JList<Question> list;
	private JTextField filterField;
	private JScrollPane scrollPane;
	private FilteredListModel filteredListModel;
	private TestEditation testEditation;
	private String path;
	private JButton btnZptNaTesty;
	private JLabel lblFiltr;
	private JLabel eventLabelQS;

	/**
	 * Constructor for QuestionSelection, opens test found in path and
	 * reads a file into ArrayList.
	 * 
	 * @param path
	 * @param testEditation
	 * @wbp.parser.constructor
	 */
	public QuestionSelection(String path, TestEditation testEditation) {
		this.testEditation = testEditation;
		this.path = path;
		setXmlFile(new ModifyXMLFile(path));
		getXmlFile().fillQuestionsList();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVyberteOtzku = new JFrame();
		frmVyberteOtzku.setResizable(false);
		frmVyberteOtzku.setTitle("Vybrat ot\u00E1zku");
		frmVyberteOtzku.setBounds(100, 100, 650, 400);
		frmVyberteOtzku.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVyberteOtzku.setLocationRelativeTo(null);

		panel = new JPanel();
		frmVyberteOtzku.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JButton btnUpravit = new JButton("Upravit");
		btnUpravit.setToolTipText("Uprav\u00ED vybranou ot\u00E1zku");
		btnUpravit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editQuestion();
			}
		});
		btnUpravit.setBounds(525, 66, 109, 23);
		panel.add(btnUpravit);

		JButton btnNovOtzka = new JButton("Nov\u00E1 ot\u00E1zka");
		btnNovOtzka.setToolTipText("Vytvo\u0159\u00ED novou ot\u00E1zku");
		btnNovOtzka.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewQuestion();
			}
		});
		btnNovOtzka.setBounds(525, 100, 109, 23);
		panel.add(btnNovOtzka);

		JButton btnSmazat = new JButton("Smazat");
		btnSmazat.setToolTipText("Sma\u017Ee vybranou ot\u00E1zku");
		btnSmazat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelectedQuestion();
			}
		});
		btnSmazat.setBounds(525, 134, 109, 23);
		panel.add(btnSmazat);

		setFilterField(new JTextField());
		getFilterField().setToolTipText("Zadejte hledanou ot\u00E1zku...");
		getFilterField().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				getFilteredListModel().setFilter(new FilteredListModel.Filter() {
					public boolean accept(Object element) {
						Question tempQst = (Question) element;
						if (tempQst.getQuestion().toLowerCase().contains(getFilterField().getText().toLowerCase()) || getFilterField().getText().equals("")) {
							return true;
						} else {
							return false;
						}
					}
				});
			}
		});

		getFilterField().setBounds(10, 323, 505, 20);
		panel.add(getFilterField());
		getFilterField().setColumns(10);

		setScrollPane(new JScrollPane());
		getScrollPane().setBounds(10, 11, 505, 295);
		panel.add(getScrollPane());

		btnZptNaTesty = new JButton("Zp\u011Bt na testy");
		btnZptNaTesty.setToolTipText("Vr\u00E1t\u00ED zp\u011Bt na v\u00FDb\u011Br test\u016F");
		btnZptNaTesty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testEditation.getFrmUpravitTest().setVisible(true);
				getFrmVyberteOtzku().dispose();
			}
		});
		btnZptNaTesty.setBounds(525, 283, 109, 23);
		panel.add(btnZptNaTesty);

		lblFiltr = new JLabel("Filtr:");
		lblFiltr.setBounds(10, 310, 46, 14);
		panel.add(lblFiltr);

		eventLabelQS = new JLabel("");
		eventLabelQS.setBounds(10, 346, 624, 14);
		panel.add(eventLabelQS);

		refreshListOfQuestions();
	}

	/**
	 * This method reinitializes JList with questions, updates the list according values in xml file
	 * and set up Filter for filtering existing questions.
	 */
	public void refreshListOfQuestions() {
		ListModel<Question> source = new ListModelQuestions(getXmlFile().getQuestions());
		setFilteredListModel(new FilteredListModel(source));
		setList(new JList<Question>(getFilteredListModel()));
		getScrollPane().setViewportView(getList());
		getFilteredListModel().setFilter(new FilteredListModel.Filter() {
			public boolean accept(Object element) {
				Question tempQst = (Question) element;
				if (tempQst.getQuestion().toLowerCase().contains(getFilterField().getText().toLowerCase()) || getFilterField().getText().equals("")) {
					return true;
				} else {
					return false;
				}
			}
		});
		getList().setSelectedIndex(0);
		getList().setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 4198411313308223377L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof Question) {
					((JLabel) renderer).setText((index + 1) + ". " + ((Question) value).getQuestion());
				}
				return renderer;
			}
		});
		getList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public JFrame getFrmVyberteOtzku() {
		return frmVyberteOtzku;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public JLabel getEventLabelQS() {
		return eventLabelQS;
	}

	/**
	 * Method will call dialog for question editation from selected value of JList.
	 */
	private void editQuestion() {
		try {
			Question question = getList().getSelectedValue();
			QuestionEditation qe = new QuestionEditation(QuestionSelection.this, getXmlFile(), question);
			qe.getFrmUpravteOtzku().setVisible(true);
		} catch (NullPointerException ex) {
			getEventLabelQS().setText("Chyba pøi výbìru otázky, nebyla žádná vybrána");
			System.out.println("Nebylo nic vybrano");
		}
	}

	/**
	 * Method will find search for suitable last index and creates new question.
	 * After creation it will open QuestionEditation dialog.
	 */
	private void createNewQuestion() {
		int lastId = 1;
		try {
			lastId = getXmlFile().findLastId();
		} catch (NullPointerException ex) {
			lastId = 1;
		} catch (ArrayIndexOutOfBoundsException aibEx) {
			lastId = 1;
		}

		QuestionEditation qe = new QuestionEditation(QuestionSelection.this, getXmlFile(), lastId);
		qe.getFrmUpravteOtzku().setVisible(true);
	}

	/**
	 * Deletes selected question from Node list in ModifyXMLFile and then save it to the xml file.
	 */
	private void deleteSelectedQuestion() {
		try {
			Question tempQuestion = getList().getSelectedValue();
			int index = tempQuestion.getId();
			Node questions = getXmlFile().getDoc().getFirstChild();
			Node node = getXmlFile().getQuestionNode(index);
			questions.removeChild(node);
			try {
				ModifyXMLFile.saveToXML(getXmlFile().getDoc(), getXmlFile().getPath());
			} catch (TransformerException e1) {
				e1.printStackTrace();
			}
			getXmlFile().fillQuestionsList();
			refreshListOfQuestions();
		} catch (NullPointerException ex) {
			System.out.println("Nebylo nic vybrano");
			getEventLabelQS().setText("Chyba pøi výbìru otázky, nebyla žádná vybrána");
		}
	}

	/**
	 * @return the xmlFile
	 */
	public ModifyXMLFile getXmlFile() {
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
	 * @return the filteredListModel
	 */
	private FilteredListModel getFilteredListModel() {
		return filteredListModel;
	}

	/**
	 * @param filteredListModel
	 *            the filteredListModel to set
	 */
	private void setFilteredListModel(FilteredListModel filteredListModel) {
		this.filteredListModel = filteredListModel;
	}

	/**
	 * @return the list
	 */
	JList<Question> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	void setList(JList<Question> list) {
		this.list = list;
	}

	/**
	 * @return the scrollPane
	 */
	private JScrollPane getScrollPane() {
		return scrollPane;
	}

	/**
	 * @param scrollPane
	 *            the scrollPane to set
	 */
	private void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	/**
	 * @return the filterField
	 */
	private JTextField getFilterField() {
		return filterField;
	}

	/**
	 * @param filterField
	 *            the filterField to set
	 */
	private void setFilterField(JTextField filterField) {
		this.filterField = filterField;
	}
}
