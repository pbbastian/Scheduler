package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;

import java.io.InputStream;
import java.io.PrintStream;

public class ProjectDevelopersDialog implements Dialog {
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public ProjectDevelopersDialog(Session session, Project project, Dialog previousDialog) {
        this.session = session;
        this.project = project;
        this.previousDialog = previousDialog;
    }
    
    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();
        int size = project.getDevelopers().size();
        Choice[] choices = new Choice[size+1];
        for (int i = 0; i < size; i++) {
            Developer developer = project.getDevelopers().get(i);
            choices[i] = new Choice(developer.getName(), new ProjectDeveloperDialog(session, project, developer, this));
        }
        choices[size] = new Choice("Go back", previousDialog);
        new ChoiceDialog(choices, "Developers in this project:", "Select a developer: ").display(in, out);
    }
}
