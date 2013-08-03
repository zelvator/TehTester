package org.zelvator.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

/**
 * Model for JList for representing loaded Tests.
 * 
 * @author zelvator
 * 
 */
public class ListModelTests extends DefaultListModel<File> {
	private static final long serialVersionUID = -7878847180676800089L;
	private List<File> tests = new ArrayList<File>();

	public ListModelTests(List<File> list) {
		super();
		this.tests = list;
	}

	public void addElement(File test) {
		tests.add(test);
		fireIntervalAdded(this, tests.size() - 1, tests.size() - 1);
	}

	@Override
	public File getElementAt(int index) {
		return tests.get(index);
	}

	@Override
	public int getSize() {
		return tests.size();
	}
}
