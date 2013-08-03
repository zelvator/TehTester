package org.zelvator.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;

/**
 * Dialog used for About button.
 * 
 * @author zelvator
 * 
 */
public class About extends JDialog {

	private static final long serialVersionUID = 4547390533297261224L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public About() {
		setResizable(false);
		setTitle("TEH TESTER");
		setModal(true);
		setBounds(100, 100, 399, 323);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				JTextPane textPane = new JTextPane();
				textPane.setText("\t\t TEH TESTER\r\n\r\nAplikace slou\u017E\u00EDc\u00ED jak pro tvorbu a \u00FApravu test\u016F, tak i pro samotn\u00E9 \"testov\u00E1n\u00ED\".\r\n\r\nTesty se ukl\u00E1daj\u00ED do .xml soubor\u016F do p\u0159\u00EDslu\u0161n\u00FDch slo\u017Eek, nem\u011B\u0148te nic ru\u010Dn\u011B a vyu\u017Eijte editoru.\r\nTo sam\u00E9 plat\u00ED pro obr\u00E1zky, ukl\u00E1daj\u00ED se k p\u0159\u00EDslu\u0161n\u00FDm test\u016Fm a ot\u00E1zk\u00E1m, odstran\u011Bn\u00EDm nebo p\u0159em\u00EDst\u011Bn\u00EDm znemo\u017En\u00EDte na\u010Dten\u00ED obr\u00E1zk\u016F.\r\n\r\nPokud budete m\u00EDt n\u011Bjak\u00FD n\u00E1pad, p\u0159ipom\u00EDnku, nebo naleznete bug, napi\u0161te mi pros\u00EDm na email.\r\n\r\nZn\u00E1m\u00E9 bugy: \r\nP\u0159i m\u00E1z\u00E1n\u00ED soubor\u016F m\u016F\u017Ee b\u00FDt zrovna n\u011Bjak\u00FD pou\u017E\u00EDvan\u00FD a t\u00EDm p\u00E1dem se nesma\u017Ee v\u0161echno, sta\u010D\u00ED klepnou na maz\u00E1n\u00ED je\u0161t\u011B jednou.\r\nP\u0159i p\u0159etahov\u00E1n\u00ED obr\u00E1zk\u016F z Wordu 2010 a 13 doch\u00E1z\u00ED k chyb\u011B, n\u011Bco \u017Ee to nem\u016F\u017Ee p\u0159e\u010D\u00EDst hlavi\u010Dku obr\u00E1zku, probl\u00E9m se \u0159e\u0161\u00ED, ale moment\u00E1ln\u011B to nejde spravit. Pou\u017E\u00EDvejte kdy\u017Etak Wordpad nebo jin\u00E9 editory. P\u0159\u00EDpadn\u011B pokud m\u00E1te obr\u00E1zek jako soubor, m\u016F\u017Eete ho taky pou\u017E\u00EDt.\r\n\r\nAutor: zelvator\r\nEmail: zelvator@gmail.com");
				textPane.setFont(new Font("Tahoma", Font.PLAIN, 11));
				textPane.setEditable(false);
				scrollPane.setViewportView(textPane);
			}
		}
		setLocationRelativeTo(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
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
