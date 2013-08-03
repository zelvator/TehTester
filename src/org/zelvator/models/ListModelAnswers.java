package org.zelvator.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import org.zelvator.questions.answers.Answer;

/**
 * Model for JList for representing Answer objects.
 * 
 * @author zelvator
 * 
 */
public class ListModelAnswers extends DefaultListModel<Answer> {
	private static final long serialVersionUID = -7878847180676800089L;
	private List<Answer> answers = new ArrayList<Answer>();

	public ListModelAnswers(List<Answer> list) {
		super();
		this.answers = list;
	}

	public void addElement(Answer answ) {
		answers.add(answ);
		fireIntervalAdded(this, answers.size() - 1, answers.size() - 1);
	}

	@Override
	public Answer getElementAt(int index) {
		return answers.get(index);
	}

	@Override
	public int getSize() {
		return answers.size();
	}

}
