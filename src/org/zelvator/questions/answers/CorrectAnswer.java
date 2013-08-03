package org.zelvator.questions.answers;

/**
 * Class used for correct answers, there could be path to the image.
 * 
 * @author zelvator
 * 
 */
public class CorrectAnswer extends Answer {

	public CorrectAnswer(String answer, String answerPicture) {
		this.correctAnswer = true;
		this.answer = answer;
		this.answerPicture = answerPicture;
	}
}
