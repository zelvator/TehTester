package org.zelvator.main;

import java.awt.EventQueue;

import org.zelvator.editor.QuestionSelection;
import org.zelvator.editor.TestEditation;

public class StartApp {
	/**
	 * Launch the application.
	 */
		public static void main(String[] args) {
			try {
				for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
					if ("Windows".equals(info.getName())) {
						javax.swing.UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (ClassNotFoundException ex) {
				java.util.logging.Logger.getLogger(QuestionSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			} catch (InstantiationException ex) {
				java.util.logging.Logger.getLogger(QuestionSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			} catch (IllegalAccessException ex) {
				java.util.logging.Logger.getLogger(QuestionSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			} catch (javax.swing.UnsupportedLookAndFeelException ex) {
				java.util.logging.Logger.getLogger(QuestionSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			}

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						TestEditation window = new TestEditation();
						window.getFrmUpravitTest().setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
}
