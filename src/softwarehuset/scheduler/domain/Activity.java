package softwarehuset.scheduler.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Activity {
	private String id;
	private String description;
	private Calendar creationDate;
	private Calendar dueDate;
	private Project project;
	private List<Developer> developers;
	private Developer author;
	
	public Activity(String description, Calendar creationDate, Calendar dueDate) {
		this.description = description;
		this.creationDate = creationDate;
		this.dueDate = dueDate;
		this.developers = new ArrayList<Developer>();
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

	public Calendar getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Calendar createdDate) {
		this.creationDate = createdDate;
	}

	public Calendar getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Calendar dueDate) {
		this.dueDate = dueDate;
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
}
