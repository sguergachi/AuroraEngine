package aurora.engine.V1.Logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class ATimeKeeper extends Timer {

	private int seconds;
	private int days;
	private int hours;
	private int min;
	private int sec;
	private long start;
	private String timeString;

	public ATimeKeeper(long startTime) {

		super(1000, null);
		this.start = startTime;

		buildTimer();
	}

	private void buildTimer() {

		this.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				seconds = (int) (System.currentTimeMillis() - start) / 1000;
				days = seconds / 86400;
				hours = (seconds / 3600) - (days * 24);
				min = (seconds / 60) - (days * 1440) - (hours * 60);
				sec = seconds % 60;
				timeString = new String("" + hours + " hours " + min + " min "
						+ sec + " sec");
			}

		});
	}

	public String getTimeString() {
		return timeString;

	}

	public int getDays() {
		return days;
	}

	public int getHours() {
		return hours;
	}

	public int getMin() {
		return min;
	}

	public int getSec() {
		return sec;
	}

	public int getSeconds() {
		return seconds;
	}

}
