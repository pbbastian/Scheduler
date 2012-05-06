package softwarehuset.scheduler.domain;

import softwarehuset.scheduler.domain.Activity;

import java.util.Calendar;

public class ActivityTimePeriod {
    private Activity activity;
    private int fromHour;
    private int toHour;
    private Calendar date;

    public ActivityTimePeriod(Activity activity, int fromHour, int toHour) {
        this.activity = activity;
        this.fromHour = fromHour;
        this.toHour = toHour;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Activity getActivity() {
        return activity;
    }

    public int getFromHour() {
        return fromHour;
    }

    public int getToHour() {
        return toHour;
    }
}
