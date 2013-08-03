package org.zelvator.models;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.zelvator.questions.Question;
/**
 * Class used for filtering feature in the question selection.
 * <br> http://stackoverflow.com/questions/14758313/filtering-jlist-based-on-jtextfield
 * @author zelvator
 *
 */
public class FilteredListModel extends AbstractListModel<Question> {
	/*
	 * http://stackoverflow.com/questions/14758313/filtering-jlist-based-on-jtextfield
	 */
	private static final long serialVersionUID = -8709254624969954011L;

	public static interface Filter {
		boolean accept(Object element);
	}

	private final ListModel<Question> source;
	private Filter filter;
	private final ArrayList<Integer> indices = new ArrayList<Integer>();

	public FilteredListModel(ListModel<Question> source) {
		if (source == null)
			throw new IllegalArgumentException("Source is null");
		this.source = source;
		this.source.addListDataListener(new ListDataListener() {
			public void intervalRemoved(ListDataEvent e) {
				doFilter();
			}

			public void intervalAdded(ListDataEvent e) {
				doFilter();
			}

			public void contentsChanged(ListDataEvent e) {
				doFilter();
			}
		});
	}

	public void setFilter(Filter f) {
		filter = f;
		doFilter();
	}

	private void doFilter() {
		indices.clear();

		Filter f = filter;
		if (f != null) {
			int count = source.getSize();
			for (int i = 0; i < count; i++) {
				Object element = source.getElementAt(i);
				if (f.accept(element)) {
					indices.add(i);
				}
			}
			fireContentsChanged(this, 0, getSize() - 1);
		}
	}

	public int getSize() {
		return (filter != null) ? indices.size() : source.getSize();
	}

	public Question getElementAt(int index) {
		return (filter != null) ? source.getElementAt(indices.get(index)) : source.getElementAt(index);
	}
}