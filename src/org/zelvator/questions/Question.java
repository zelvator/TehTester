package org.zelvator.questions;

import java.util.ArrayList;
import java.util.List;

import org.zelvator.questions.answers.Answer;

/**
 * Class used for storing attributes from the child nodes within question node in the xml file.
 * Answers could be N, and they could be correct and wrong, so that's why it is in the List of Answer objects.
 * 
 * @author zelvator
 * 
 */
public class Question {

	private List<Answer> answers = new ArrayList<>();
	private String pathToPic = "";
	private String question;
	private int id;

	public Question(int id) {
		this.id = id;
		question = "none";
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public String getPathToPic() {
		return pathToPic;
	}

	public void setPathToPic(String pathToPic) {
		this.pathToPic = pathToPic;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
