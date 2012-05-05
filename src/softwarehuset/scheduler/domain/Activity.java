package softwarehuset.scheduler.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Activity {
	private String id;
	private String description;
	private Calendar start;
	private Calendar end;
	private Project project;
	private List<Developer> developers;
	private Developer author;
    private Status status;
    private double timeSpent;

    public Activity(String description, Calendar start, Calendar end) {
		this.description = description;
		this.start = start;
		this.end = end;
		this.developers = new ArrayList<Developer>();
        this.status = Status.ONGOING;
	}

	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getStart() {
		return this.start;
	}

	public void setStart(Calendar createdDate) {
		this.start = createdDate;
	}

	public Calendar getEnd() {
		return this.end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Developer> getDevelopers() {
		return developers;
	}

	public Developer getAuthor() {
		return author;
	}

	public void setAuthor(Developer author) {
		this.author = author;
	}

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

	public double getTimeSpent() {
		return this.timeSpent;
	}
	
	public void spendTime(double time) {
		this.timeSpent = this.timeSpent + time;
	}
	
	public void setTimeSpent(double time) {
		this.timeSpent = time;
	}
}
