package scheduler.core;

public class Project {
	private String name;
	private Developer author;
	
	public Project(String name, Developer author) {
		this.name = name;
		this.author = author;
	}

	public Developer getAuthor() {
		return author;
	}
}
