package org.zelvator.timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * This class allows to add Timer on the Tester. When loaded, timer will
 * start adding seconds, converting to minutes or hours as needed and also
 * it allows to pause and continue as you click on buttons on the Tester.
 * 
 * @author zelvator
 * 
 */
public class TestTimer {
	private JLabel label;
	Timer countdownTimer;
	private int hour;
	private int minute;
	private int second = 0;
	private boolean pause = false;

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
		if (hour < 0) {
			this.hour = 0;
		}
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;

		if (minute >= 60) {
			int hourplus = getHour() + minute / 60;
			setHour(hourplus);
			this.minute = minute % 60;
		}
		if (minute < 0) {
			this.minute = 0;
		}
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
		if (second >= 60) {
			int minuteplus = getMinute() + second / 60;
			setMinute(minuteplus);
			this.second = second % 60;
		}
		if (second < 0) {
			this.second = 0;
		}
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
		if (pause) {
			countdownTimer.stop();
		} else {
			countdownTimer.start();
		}
	}

	public TestTimer(JLabel passedLabel) {
		countdownTimer = new Timer(1000, new CountdownTimerListener());
		this.label = passedLabel;
		countdownTimer.start();
	}

	@Override
	public String toString() {
		String h = "", m = "", s = "";
		setSecond(second);
		if (getHour() >= 1) {
			h = getHour() + " h ";
		}

		if (getMinute() >= 1) {
			m = getMinute() + " m ";
		}

		if (getSecond() >= 1) {
			s = getSecond() + " s ";
		}
		return (h + m + s);
	}

	class CountdownTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!pause) {
				++second;
				label.setText(TestTimer.this.toString());
			} else {
				label.setText("Pozastaveno");
			}
		}
	}
}