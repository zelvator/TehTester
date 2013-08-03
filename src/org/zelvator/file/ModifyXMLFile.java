package org.zelvator.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.zelvator.questions.Question;
import org.zelvator.questions.answers.Answer;
import org.zelvator.questions.answers.CorrectAnswer;
import org.zelvator.questions.answers.WrongAnswer;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * This class is for editing xml nodes, creating new xml file and saving to the xml file.
 * 
 * @author zelvator
 * 
 */
public class ModifyXMLFile implements TesterTagsAndAttributes {
	/*
	 * http://www.mkyong.com/tutorials/java-xml-tutorials/
	 */
	private List<Question> questions = new ArrayList<>();
	private Document doc;
	private NodeList nList;
	private String path;

	/**
	 * Constructor will load xml file from given path into memory,
	 * which can be modified and then eventually saved to the file again.
	 * 
	 * @param path
	 */
	public ModifyXMLFile(String path) {
		try {
			this.path = path;
			doc = getDocument(path);
			setnList(getNodeList(doc));
		} catch (SAXException e) {
			JOptionPane.showMessageDialog(null, "Chyba: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Chyba: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "Chyba: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public NodeList getnList() {
		return nList;
	}

	public void setnList(NodeList nList) {
		this.nList = nList;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Method loads xml file into memory and create Document instance, which can be modified.
	 * 
	 * @param path
	 * @return Document
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public Document getDocument(String path) throws SAXException, IOException, ParserConfigurationException {
		File fXmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		return doc;
	}

	/**
	 * Method will normalize given Document and then loads all question nodes into
	 * NodeList.
	 * 
	 * @param doc
	 * @return NodeList, list which contains elements and their childs
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public NodeList getNodeList(Document doc) throws SAXException, IOException, ParserConfigurationException {
		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName(QUESTION);
		System.out.println("----------------------------");
		return nList;
	}

	/**
	 * This method will search for question with given index (id).
	 * Id of question is in their attribute.
	 * 
	 * @param index
	 * @return selected Node
	 */
	public Node getQuestionNode(int index) {

		// Get the root element
		@SuppressWarnings("unused")
		Node questions = getDoc().getFirstChild();

		// Get the staff element , it may not working if tag has spaces, or
		// whatever weird characters in front...it's better to use
		// getElementsByTagName() to get it directly.
		// Node staff = company.getFirstChild();

		// Get the staff element by tag name directly
		Node question = null;
		for (int i = 0; i < getnList().getLength(); i++) {
			question = getDoc().getElementsByTagName(QUESTION).item(i);
			NamedNodeMap attr = question.getAttributes();
			Node nodeAttr = attr.getNamedItem(ID);
			if (Integer.parseInt(nodeAttr.getTextContent()) == index) {
				return question;
			}
		}
		return null;
	}

	/**
	 * Method will search for last used id and add one for purpose to creating
	 * new question id. <br>
	 * In some cases there could be problem, because it will get last element
	 * no matter what. By using editor, it should be all fine, if you swap elements
	 * manually in the xml file, you could create duplicates by this method, because
	 * it will not know about that and there could be errors with loading test.
	 * 
	 * @return new usable ID for question node
	 */
	public int findLastId() {
		@SuppressWarnings("unused")
		Node questions = getDoc().getFirstChild();
		int index = getDoc().getElementsByTagName(QUESTION).getLength() - 1;
		Node question = getDoc().getElementsByTagName(QUESTION).item(index);

		NamedNodeMap attr = question.getAttributes();
		Node nodeAttr = attr.getNamedItem(ID);
		int id = Integer.parseInt(nodeAttr.getTextContent()) + 1;
		return id;
	}

	/**
	 * This method will create new question node with given ID.
	 * 
	 * @param id
	 */
	public void createQuestionNode(int id) {
		Node questions = getDoc().getFirstChild();

		Element qst = doc.createElement(QUESTION);
		questions.appendChild(qst);

		Attr attrib = doc.createAttribute(ID);
		attrib.setValue(id + "");
		qst.setAttributeNode(attrib);
	}

	/**
	 * Method used for changing attribute in the node.
	 * Might be used for editing ID attribute in the question Node.
	 * 
	 * @param question
	 * @param context
	 * @param item
	 */
	public void changeAttr(Node question, String context, String item) {
		NamedNodeMap attr = question.getAttributes();
		Node nodeAttr = attr.getNamedItem(item);
		nodeAttr.setTextContent(context);
	}

	/**
	 * This method will append new Node to Question node, for instance
	 * question will have correct answer in the tag "spravna" (correct),
	 * all nodes have must have some value and attribute named "cesta" (path to image),
	 * attribute can be empty, tester will handle this exception by empty image.
	 * 
	 * @param question
	 * @param newElement
	 * @param elementValue
	 * @param pathToPic
	 */
	public void appendNodetoQuestion(Node question, String newElement, String elementValue, String pathToPic) {
		Element childNode = doc.createElement(newElement);
		childNode.appendChild(doc.createTextNode(elementValue));
		childNode.setAttribute(CESTA, pathToPic);

		question.appendChild(childNode);
	}

	/**
	 * Method will remove all existing elements in the question node, (in case of multiple
	 * answers, there is problem with recognizing which is which... there could be IDs for
	 * those elements, as they are in the question node, and you could find them and manage them,
	 * but this seemed easier in the time of writing code, so it is like this) and then append all
	 * nodes again from Question object, here are sent attributes from object right away.
	 * 
	 * @param question
	 * @param otazka
	 * @param pathToPic
	 * @param answers
	 */
	public void updateQuestionNode(Node question, String otazka, String pathToPic, List<Answer> answers) {
		removeNodeInQuestionNode(question, OTAZKA);
		removeNodeInQuestionNode(question, SPRAVNA);
		removeNodeInQuestionNode(question, SPATNA);

		appendNodetoQuestion(question, OTAZKA, otazka, pathToPic);
		for (Answer answer : answers) {
			if (answer.isCorrectAnswer()) {
				appendNodetoQuestion(question, SPRAVNA, answer.getAnswer(), answer.getAnswerPicture());
			} else {
				appendNodetoQuestion(question, SPATNA, answer.getAnswer(), answer.getAnswerPicture());
			}
		}
	}

	/**
	 * This method will remove all occurrences of child node in the question node.
	 * That's why it is in the loop.
	 * 
	 * @param question
	 * @param nameOfNode
	 */
	public void removeNodeInQuestionNode(Node question, String nameOfNode) {
		// loop the question child node
		NodeList list = question.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);

			if (nameOfNode.equals(node.getNodeName())) {
				question.removeChild(node);
			}
		}
	}

	/**
	 * Method will go through the NodeList, where are loaded question nodes and for
	 * each one by one will be set attributes for the Question object from element in
	 * the node list. After loading all values and attributes, question will be
	 * added to the Question ArrayList.
	 */
	public void fillQuestionsList() {
		getQuestions().clear();
		for (int temp = 0; temp < getnList().getLength(); temp++) {
			Node nNode = getnList().item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				int id = Integer.parseInt(eElement.getAttribute(ID));
				Question question = new Question(id);
				try {
					question.setQuestion(eElement.getElementsByTagName(OTAZKA).item(0).getTextContent());
				} catch (NullPointerException ex) {
					question.setQuestion("");
				}
				try {
					question.setPathToPic(eElement.getElementsByTagName(OTAZKA).item(0).getAttributes().getNamedItem(CESTA).getTextContent());
				} catch (NullPointerException ex) {
					question.setPathToPic("");
				}

				for (int i = 0; i < eElement.getElementsByTagName(SPRAVNA).getLength(); i++) {
					String textAnswer = eElement.getElementsByTagName(SPRAVNA).item(i).getTextContent();
					String picPathAnswer = eElement.getElementsByTagName(SPRAVNA).item(i).getAttributes().getNamedItem(CESTA).getTextContent();
					question.getAnswers().add(new CorrectAnswer(textAnswer, picPathAnswer));
				}
				for (int i = 0; i < eElement.getElementsByTagName(SPATNA).getLength(); i++) {
					String textAnswer = eElement.getElementsByTagName(SPATNA).item(i).getTextContent();
					String picPathAnswer = eElement.getElementsByTagName(SPATNA).item(i).getAttributes().getNamedItem(CESTA).getTextContent();
					question.getAnswers().add(new WrongAnswer(textAnswer, picPathAnswer));
				}
				getQuestions().add(question);
			}
		}
	}

	/**
	 * This method is supposed to save all changes in the nodes to the XML file.
	 * Method takes Document and file path, format it and serializes (saves) as XML file.
	 * 
	 * @param doc
	 * @param filepath
	 * @throws TransformerException
	 */
	public static void saveToXML(Document doc, String filepath) throws TransformerException {

		OutputFormat format = new OutputFormat(doc);
		format.setIndenting(true);
		format.setEncoding("Windows-1250");

		try {
			File file = new File(filepath);
			file.setWritable(true);
			XMLSerializer serializer = new XMLSerializer(new FileOutputStream(file), format);
			serializer.serialize(doc);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Chyba: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		System.out.println("Done");
	}

	/**
	 * Method will try to create new XML file in the specified path,
	 * which should be created before by taking name of the test, which
	 * user sets in the dialog for test editation. Before saving it will
	 * create new Document, create root element and append it as a node and
	 * after that it will save it to the folder.
	 * 
	 * @param path
	 */

	public static void createNewXML(String path) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(QUESTIONS);
			doc.appendChild(rootElement);
			saveToXML(doc, path);
		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "Chyba: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (TransformerException e) {
			JOptionPane.showMessageDialog(null, "Chyba: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
