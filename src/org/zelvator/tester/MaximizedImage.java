package org.zelvator.tester;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.zelvator.dragImage.ImagePanel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class is to make possible to see loaded picture in better detail,
 * you can double click on selected image on the panel and this dialog will load
 * image in resizable container, where you can "zoom" it as you need.
 * 
 * @author zelvator
 * 
 */
public class MaximizedImage extends JDialog {

	private static final long serialVersionUID = -4547861356515567171L;
	private final JPanel contentPanel = new JPanel();
	private String path;
	private ImagePanel imagePanel;
	private JScrollPane scrollPane;

	/**
	 * Create the dialog.
	 */
	public MaximizedImage(String path) {
		this.path = path;
		init();
	}

	public void init() {
		setBounds(100, 100, 500, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			scrollPane = new JScrollPane();
			contentPanel.add(scrollPane);
		}
		{
			imagePanel = new ImagePanel(path, false);
			imagePanel.setSize(scrollPane.getWidth(), scrollPane.getHeight());
			scrollPane.setViewportView(imagePanel);
			imagePanel.setLayout(new BorderLayout(0, 0));
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
