package org.zelvator.questions.answers;

/**
 * Abstract class Answer, used for storing data from the question nodes
 * as correct and wrong answers.
 * 
 * @author zelvator
 * 
 */
public abstract class Answer {

	protected boolean correctAnswer;
	protected String answer;
	protected String answerPicture;

	public Answer() {
	}

	public boolean isCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(boolean correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswerPicture() {
		return answerPicture;
	}

	public void setAnswerPicture(String answerPicture) {
		this.answerPicture = answerPicture;
	}

}
