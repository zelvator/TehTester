package org.zelvator.loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Node;
import org.zelvator.file.ModifyXMLFile;
import org.zelvator.questions.Question;
import org.zelvator.questions.answers.Answer;
import org.zelvator.questions.answers.CorrectAnswer;
import org.zelvator.questions.answers.WrongAnswer;

public class Loader {

	static List<Question> questions;
	List<Answer> answers;
	static int id = 1;
	ModifyXMLFile modXML;
	static String path = "C:\\Users\\zelvator\\Desktop\\Tester\\tests\\CISCO CNNA 1 2013\\CISCO CNNA 1 2013.xml";
	Question question;

	public void readFile() {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("C:\\Users\\zelvator\\Desktop\\CISCO tester2013\\otazky.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			String line = br.readLine();
			questions = new ArrayList<>();

			while (line != null) {
				if (line.startsWith("=")) {
					question = new Question(id);
					id++;
					question.setQuestion(line.substring(5).trim());
					line = br.readLine();
					System.out.println(line);
				} else {
					answers = new ArrayList<>();
					while (line != null && !line.startsWith("=")) {
						if (line.startsWith("+")) {
							answers.add(new CorrectAnswer(line.substring(1).trim(), ""));
							line = br.readLine();
							System.out.println(line);
						} else {
							answers.add(new WrongAnswer(line.trim(), ""));
							line = br.readLine();
							System.out.println(line);
						}
					}
					question.setAnswers(answers);
					questions.add(question);

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void createFile() {
		ModifyXMLFile.createNewXML(path);

	}

	public void updateFile() {
		modXML = new ModifyXMLFile(path);
		int id = 1;
		for (Question qest : questions) {
			modXML.createQuestionNode(id);
			Node questionNode = modXML.getQuestionNode(id);
			modXML.updateQuestionNode(questionNode, qest.getQuestion(), "", qest.getAnswers());
			id++;
		}
	}

	public void saveToFile() {
		try {
			ModifyXMLFile.saveToXML(modXML.getDoc(), path);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		Loader loader = new Loader();
//		loader.createFile();
//		loader.readFile();
//		loader.updateFile();
//		loader.saveToFile();
//	}
}
