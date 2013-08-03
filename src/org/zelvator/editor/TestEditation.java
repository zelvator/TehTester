package org.zelvator.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.zelvator.file.FilesInFolder;
import org.zelvator.file.ModifyXMLFile;
import org.zelvator.main.About;
import org.zelvator.models.ListModelTests;
import org.zelvator.tester.Tester;

/**
 * Frame for selection, modification and starting test.
 * 
 * @author zelvator
 * 
 */
public class TestEditation {

	private JFrame frmUpravitTest;

	private JPanel panel;
	private JList<File> list;
	private FilesInFolder filesInFolder;
	private JScrollPane scrollPane;
	private JLabel eventLabelTE;

	/**
	 * Constructor will read all files ending with .xml prefix in the folder "tests"
	 * into the FilesInFolder.
	 */
	public TestEditation() {
		setFilesInFolder(new FilesInFolder("tests"));
		getFilesInFolder().listFilesForFolder(getFilesInFolder().getFolder(), ".xml");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrmUpravitTest(new JFrame());
		getFrmUpravitTest().setResizable(false);
		getFrmUpravitTest().setTitle("Teh Tester");
		getFrmUpravitTest().setBounds(100, 100, 500, 350);
		getFrmUpravitTest().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrmUpravitTest().setLocationRelativeTo(null);

		panel = new JPanel();
		getFrmUpravitTest().getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JButton btnUpravit = new JButton("Upravit test");
		btnUpravit.setToolTipText("Upravit vybran\u00FD test");
		btnUpravit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				editTest();
			}
		});
		btnUpravit.setBounds(375, 79, 109, 23);
		panel.add(btnUpravit);

		JButton btnNovytest = new JButton("Nov\u00FD test");
		btnNovytest.setToolTipText("Vytvo\u0159it nov\u00FD test");
		btnNovytest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewTest();

			}
		});
		btnNovytest.setBounds(375, 113, 109, 23);
		panel.add(btnNovytest);

		JButton btnSmazat = new JButton("Smazat test");
		btnSmazat.setToolTipText("Sma\u017Ee vybran\u00FD test");
		btnSmazat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteTest();
			}
		});
		btnSmazat.setBounds(375, 147, 109, 23);
		panel.add(btnSmazat);

		JButton btnStart = new JButton("Start");
		btnStart.setToolTipText("Spust\u00ED vybran\u00FD test");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startTest();
			}
		});
		btnStart.setBounds(145, 247, 89, 23);
		panel.add(btnStart);

		setScrollPane(new JScrollPane());
		getScrollPane().setBounds(10, 11, 355, 225);
		panel.add(getScrollPane());
		setList(new JList<File>(new ListModelTests(getFilesInFolder().getFiles())));
		getList().setSelectedIndex(0);
		getScrollPane().setViewportView(getList());
		getList().setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 5096162554693729971L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof File) {
					((JLabel) renderer).setText(((File) value).getName().substring(0, ((File) value).getName().length() - 4));
				}
				return renderer;
			}
		});
		getList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		eventLabelTE = new JLabel("");
		eventLabelTE.setBounds(10, 281, 474, 14);
		panel.add(eventLabelTE);

		JMenuBar menuBar = new JMenuBar();
		getFrmUpravitTest().setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Soubor");
		menuBar.add(mnNewMenu);

		JMenuItem mntmOAplikaci = new JMenuItem("O Aplikaci");
		mntmOAplikaci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				About about = new About();
				about.setVisible(true);
			}
		});
		mnNewMenu.add(mntmOAplikaci);
	}

	/**
	 * This method reinitializes JList with tests, updates the list according files found in the folder
	 * and displays only name without prefix.
	 */
	public void refreshListofTests() {
		setFilesInFolder(new FilesInFolder("tests"));
		getFilesInFolder().listFilesForFolder(getFilesInFolder().getFolder(), ".xml");
		setList(new JList<File>(new ListModelTests(getFilesInFolder().getFiles())));
		getList().setSelectedIndex(0);
		getScrollPane().setViewportView(getList());
		// nacist testy z adresare a zobrazit je
		getList().setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 5096162554693729971L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof File) {
					((JLabel) renderer).setText(((File) value).getName().substring(0, ((File) value).getName().length() - 4));
				}
				return renderer;
			}
		});
		getList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public JFrame getFrmUpravitTest() {
		return frmUpravitTest;
	}

	public JLabel getEventLabelTE() {
		return eventLabelTE;
	}

	/**
	 * Method will delete selected test from folder, delete operation will delete every question and
	 * picture in the folder. If some error occurs, it may be required to delete it again, because
	 * of possibility that Java might be using some of files which can not be deleted right away.
	 * You need to do it again.
	 */
	private void deleteTest() {
		if (getList().getSelectedValue() != null) {
			int answer = JOptionPane.showConfirmDialog(null, "Chcete smazat kompletnì celý test vèetnì otázek i obrázkù?");
			if (answer == JOptionPane.YES_OPTION) {
				try {
					File file = new File(getList().getSelectedValue().getParentFile().getAbsolutePath());
					FilesInFolder.delete(file);

					refreshListofTests();
				} catch (NullPointerException ex) {
					System.out.println("Nebylo nic vybrano");
					getEventLabelTE().setText("Chyba pøi vybírání testu, nebyl žádný vybrán");
				} catch (IOException ioe) {
					System.out.println("Mazani testu selhalo");
					getEventLabelTE().setText("Chyba pøi mazání testu, nìkteré soubory zùstaly, zkuste to znova");
					ioe.printStackTrace();
				}
			}
		} else {
			System.out.println("Nebylo nic vybrano");
			getEventLabelTE().setText("Chyba pøi vybírání testu, nebyl žádný vybrán");
		}
	}

	/**
	 * Opens QuestionSelection dialog with selected test and close this one.
	 */
	private void editTest() {
		try {
			File file = getList().getSelectedValue();
			String path = "";
			path = file.getPath();
			System.out.println(path);
			QuestionSelection questSelection = new QuestionSelection(path, TestEditation.this);
			questSelection.getFrmVyberteOtzku().setVisible(true);
			getFrmUpravitTest().setVisible(false);
		} catch (NullPointerException ex) {
			getEventLabelTE().setText("Chyba pøi vybírání testu, nebyl žádný vybrán");
			System.out.println("Nebylo nic vybrano");
		}
	}

	/**
	 * Method will ask for new Test name, no empty names and no duplicates allowed.
	 * Then it will create new folder and xml file with the name given and refresh JList.
	 */
	private void createNewTest() {
		String newTestName = JOptionPane.showInputDialog(null, "Vložte název nového testu: ", "Název testu", JOptionPane.INFORMATION_MESSAGE);
		if (!newTestName.equals("")) {
			boolean alreadyExists = false;
			for (File file : getFilesInFolder().getFiles()) {
				if (file.getName().substring(0, (file.getName().length() - 4)).equals(newTestName)) {
					getEventLabelTE().setText("Chyba pøi vytváøení testu, test s takovým názvem již existuje");
					System.out.println("Uz takovy test existuje");
					alreadyExists = true;
					break;
				}
			}
			if (!alreadyExists) {
				String path = "tests\\" + newTestName;
				System.out.println(path);
				File file = new File(path);
				if (file.mkdir()) {
					System.out.println("Slozka vytvorena");
				}
				path = path + "\\" + newTestName + ".xml";
				ModifyXMLFile.createNewXML(path);
				refreshListofTests();
			}
		} else {
			System.out.println("Zruseno");
			getEventLabelTE().setText("Vyváøení nového testu zrušeno");
		}
	}

	/**
	 * Method will start selected test, it will open Tester dialog and close this one.
	 */
	private void startTest() {
		try {
			File file = getList().getSelectedValue();
			String path = "";
			try {
				path = file.getCanonicalPath();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			System.out.println(path);
			ModifyXMLFile xmlFile = new ModifyXMLFile(path);
			xmlFile.fillQuestionsList();
			String nameOfTest = FilesInFolder.getNameOfTest(path);
			Tester tester = new Tester(xmlFile.getQuestions(), TestEditation.this, nameOfTest);
			tester.setVisible(true);
			getFrmUpravitTest().setVisible(false);
		} catch (NullPointerException ex) {
			System.out.println("Nebylo nic vybrano");
			getEventLabelTE().setText("Chyba pøi spouštìní testu, nebyl žádný vybrán");
		}
	}

	/**
	 * @return the filesInFolder
	 */
	private FilesInFolder getFilesInFolder() {
		return filesInFolder;
	}

	/**
	 * @param filesInFolder the filesInFolder to set
	 */
	private void setFilesInFolder(FilesInFolder filesInFolder) {
		this.filesInFolder = filesInFolder;
	}

	/**
	 * @return the list
	 */
	private JList<File> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	private void setList(JList<File> list) {
		this.list = list;
	}

	/**
	 * @return the scrollPane
	 */
	private JScrollPane getScrollPane() {
		return scrollPane;
	}

	/**
	 * @param scrollPane the scrollPane to set
	 */
	private void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	/**
	 * @param frmUpravitTest the frmUpravitTest to set
	 */
	private void setFrmUpravitTest(JFrame frmUpravitTest) {
		this.frmUpravitTest = frmUpravitTest;
	}
}
