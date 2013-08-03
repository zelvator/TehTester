package org.zelvator.questions.answers;

/**
 * Class used for wrong answers, there could be path to the image.
 * 
 * @author zelvator
 * 
 */
public class WrongAnswer extends Answer {

	public WrongAnswer(String answer, String answerPicture) {
		this.correctAnswer = false;
		this.answer = answer;
		this.answerPicture = answerPicture;
	}
}
