package softwarehuset.scheduler.domain;

import java.util.GregorianCalendar;

public class PrivateActivity { // Peter
    private String description;
    private GregorianCalendar start;
    private GregorianCalendar end;
    private Developer developer;

    public PrivateActivity(String description, GregorianCalendar start, GregorianCalendar end) {
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GregorianCalendar getStart() {
        return start;
    }

    public void setStart(GregorianCalendar start) {
        this.start = start;
    }

    public GregorianCalendar getEnd() {
        return end;
    }

    public void setEnd(GregorianCalendar end) {
        this.end = end;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }
}
