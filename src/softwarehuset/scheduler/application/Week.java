package softwarehuset.scheduler.application;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Week {

	public static GregorianCalendar get(int week) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(GregorianCalendar.WEEK_OF_YEAR, week);
		return calendar;
	}

}
