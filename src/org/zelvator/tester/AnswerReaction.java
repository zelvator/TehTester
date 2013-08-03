package org.zelvator.tester;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.zelvator.models.ListModelAnswers;
import org.zelvator.questions.answers.Answer;

/**
 * Class for representing answer reaction in the Tester. It shows
 * wrong and correct answer, your choice and calculate score for your answer.
 * 
 * @author zelvator
 * 
 */
public class AnswerReaction extends JDialog {
	private static final long serialVersionUID = -7468069092915735477L;
	private final JPanel contentPanel = new JPanel();
	private Tester tester;
	private List<Answer> selectedAnwers;
	private List<Answer> allAnswers;
	private JList<Answer> list;
	private double totalAnwers = 0;
	private double correctAnswers = 0;
	private double wrongAnswers = 0;
	private double percentage = 0;

	public Tester getTester() {
		return tester;
	}

	/**
	 * Create the dialog.
	 */
	public AnswerReaction(Tester tester, List<Answer> selectedAnwers, List<Answer> allAnswers) {
		setModal(true);

		this.tester = tester;
		this.setSelectedAnwers(selectedAnwers);
		this.setAllAnswers(allAnswers);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closingWindow();
			}
		});
		init();
		setTitle(setTitle());
	}

	/**
	 * Initialize components.
	 */
	public void init() {
		setBounds(100, 100, 460, 300);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(42, 48, 362, 155);
		contentPanel.add(scrollPane);

		list = new JList<Answer>(new ListModelAnswers(getAllAnswers()));
		scrollPane.setViewportView(list);
		list.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 5096162554693729971L;

			/**
			 * Method will color background of the row depending on answer.
			 */
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof Answer) {
					((JLabel) renderer).setText(((Answer) value).getAnswer());
					if (((Answer) value).isCorrectAnswer()) {
						setBackground(new Color(224, 201, 82)); // yellow
					}
					for (Answer answer : getSelectedAnwers()) {
						if (((Answer) value).getAnswer().equals(answer.getAnswer())) {
							if (answer.isCorrectAnswer()) {
								setBackground(new Color(142, 236, 143)); // green
							} else {
								setBackground(new Color(226, 80, 80)); // red
							}
						}
					}
				}
				return renderer;
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		{
			JLabel lblSprvnOdpovdi = new JLabel("Spr\u00E1vn\u00E9 odpov\u011Bdi:");
			lblSprvnOdpovdi.setBounds(42, 23, 96, 14);
			contentPanel.add(lblSprvnOdpovdi);
		}
		{
			JLabel label = new JLabel("Zelen\u00E1 je spr\u00E1vn\u00E1 odpov\u011B\u010F, \u010Derven\u00E1 \u0161patn\u00E1 a \u017Elut\u00E1, kter\u00E1 m\u011Bla b\u00FDt spr\u00E1vn\u011B");
			label.setBounds(0, 241, 368, 14);
			contentPanel.add(label);
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			label.setHorizontalTextPosition(SwingConstants.LEFT);
			label.setHorizontalAlignment(SwingConstants.LEFT);
		}
		{
			JButton okButton = new JButton("OK");
			okButton.setBounds(387, 232, 47, 23);
			contentPanel.add(okButton);
			okButton.setToolTipText("Pokra\u010Dovat v testu");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispatchEvent(new WindowEvent(AnswerReaction.this, WindowEvent.WINDOW_CLOSING));
				}
			});
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
	}

	/**
	 * Method calculates percentage success of this question.
	 * If you select wrong answer, points are subtracted.
	 * You could eventually get minus points, but I set it as you
	 * would have 0 % success of this question.
	 */
	public void calculatePercentage() {
		try {
			setPercentage(((getCorrectAnswers() - getWrongAnswers()) / getTotalAnwers()) * 100);

			if (getPercentage() < 0) {
				setPercentage(0);
			}
		} catch (ArithmeticException ex) {
			setPercentage(0);
		}
	}

	/**
	 * This method compares selected answers with those which should be correct,
	 * or incorrect and add points for it.
	 */
	public void setAnwerPoints() {
		for (Answer allAnswer : getAllAnswers()) {
			if (allAnswer.isCorrectAnswer()) {
				setTotalAnwers(getTotalAnwers() + 1);
			}
			for (Answer answer : getSelectedAnwers()) {
				if (allAnswer.getAnswer().equals(answer.getAnswer())) {
					if (answer.isCorrectAnswer()) {
						setCorrectAnswers(getCorrectAnswers() + 1);
					} else {
						setWrongAnswers(getWrongAnswers() + 1);
					}
				}
			}
		}
	}

	/**
	 * This method finds if you have selected all answers correct, wrong or
	 * just some of them and set it to the title.
	 */
	public String setTitle() {
		boolean allCorrect = true;
		List<Answer> correctAnswers = new ArrayList<>();
		for (Answer allAnswer : getAllAnswers()) {
			if (allAnswer.isCorrectAnswer()) {
				correctAnswers.add(allAnswer);
			}
		}

		if (getSelectedAnwers().containsAll(correctAnswers)) {
			return "Správnì!";
		}
		int count = 0;
		for (Answer allAnswer : getAllAnswers()) {
			for (Answer answer : getSelectedAnwers()) {
				if (allAnswer.getAnswer().equals(answer.getAnswer())) {
					if (!answer.isCorrectAnswer()) {
						allCorrect = false;
					}
					if (answer.isCorrectAnswer()) {
						count++;
					}
				}
			}
		}
		if (allCorrect && count > 0) {
			return "Èásteèná odpovìï...";
		} else {
			return "Špatnì!";
		}
	}

	/**
	 * Closing this dialog will either loads next question or ends the test.
	 */
	private void closingWindow() {
		
		if (getTester().getQuestions().size() > 1) {
			getTester().getQuestions().remove(getTester().getIndex());
			getTester().loadQuestion();
			getTester().getBtnPokrauj().doClick();
		} else {
			// change something to see its over
			getTester().getBtnNext().setVisible(false);
			getTester().getBtnZptNaTesty().setVisible(true);
			getTester().getListOfAnswers().setVisible(false);
			getTester().getQuestionText().setText("Konec testu");

			getTester().getPanel_1().setVisible(false);
			getTester().getImagePanel().emptyImage();
			getTester().setSize(455, 400);
			getTester().getBtnPauza().doClick();
			getTester().getBtnPokrauj().setVisible(false);
		}
		setAnwerPoints();
		calculatePercentage();
		tester.setScore(getCorrectAnswers(), getWrongAnswers(), getTotalAnwers(), getPercentage());
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
	 * @return the correctAnswers
	 */
	private double getCorrectAnswers() {
		return correctAnswers;
	}

	/**
	 * @param correctAnswers
	 *            the correctAnswers to set
	 */
	private void setCorrectAnswers(double correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	/**
	 * @return the wrongAnswers
	 */
	private double getWrongAnswers() {
		return wrongAnswers;
	}

	/**
	 * @param wrongAnswers
	 *            the wrongAnswers to set
	 */
	private void setWrongAnswers(double wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}

	/**
	 * @return the totalAnwers
	 */
	private double getTotalAnwers() {
		return totalAnwers;
	}

	/**
	 * @param totalAnwers
	 *            the totalAnwers to set
	 */
	private void setTotalAnwers(double totalAnwers) {
		this.totalAnwers = totalAnwers;
	}

	/**
	 * @return the allAnswers
	 */
	private List<Answer> getAllAnswers() {
		return allAnswers;
	}

	/**
	 * @param allAnswers
	 *            the allAnswers to set
	 */
	private void setAllAnswers(List<Answer> allAnswers) {
		this.allAnswers = allAnswers;
	}

	/**
	 * @return the selectedAnwers
	 */
	private List<Answer> getSelectedAnwers() {
		return selectedAnwers;
	}

	/**
	 * @param selectedAnwers
	 *            the selectedAnwers to set
	 */
	private void setSelectedAnwers(List<Answer> selectedAnwers) {
		this.selectedAnwers = selectedAnwers;
	}
}
