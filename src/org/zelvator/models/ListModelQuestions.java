package org.zelvator.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import org.zelvator.questions.Question;

/**
 * Model for JList for representing Question objects.
 * 
 * @author zelvator
 * 
 */
public class ListModelQuestions extends DefaultListModel<Question> {

	private static final long serialVersionUID = 6714557855186008651L;
	private List<Question> questions = new ArrayList<Question>();

	public ListModelQuestions(List<Question> list) {
		super();
		this.questions = list;
	}

	public void addElement(Question qst) {
		questions.add(qst);
		fireIntervalAdded(this, questions.size() - 1, questions.size() - 1);
	}

	@Override
	public Question getElementAt(int index) {
		return questions.get(index);
	}

	@Override
	public int getSize() {
		return questions.size();
	}

	@Override
	public void clear() {
		super.clear();
	}

}
