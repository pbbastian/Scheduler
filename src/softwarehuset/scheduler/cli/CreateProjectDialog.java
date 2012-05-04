package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.*;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class CreateProjectDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Dialog previousDialog;

    public CreateProjectDialog(Scheduler scheduler, Session session, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.previousDialog = previousDialog;
    }
    
    @Override
    public void display(InputStream in, PrintStream out) {
        boolean validProject = false;
        Project project = null;
        while(!validProject) {
            out.print("Enter a project name: ");
            String name = new Scanner(in).nextLine();
            try {
                project = new Project(name);
                session.registerProject(project);
                validProject = true;
                out.println("You have successfully created a project named " + name + "!");
            } catch (AlreadyRegisteredProjectException e) {
                e.printStackTrace();  // This shouldn't be possible
            } catch (ArgumentException e) {
                out.println("Invalid name, please try again.");
            }
        }
        boolean validProjectLeader = false;
        while (!validProjectLeader) {
            out.print("Enter the name of who you want to be project leader: ");
            String projectLeaderName = new Scanner(in).nextLine();
            Developer projectLeader = null;
            
            for (Developer developer : scheduler.getDevelopers()) {
                if (developer.getName().equalsIgnoreCase(projectLeaderName)) {
                    projectLeader = developer;
                }
            }
            
            if (projectLeader == null) {
                out.println("Developer not found, please try again.");
            } else {
                try {
                    session.chooseProjectLeader(project, projectLeader);
                    validProject = true;
                    out.println("You have successfully chosen " + projectLeader.getName() + " as the project leader!");
                    previousDialog.display(in, out);
                } catch (Exception e) {
                    e.printStackTrace();  // For this current scenario this shouldn't be possible
                }
            }
        }
    }
}
