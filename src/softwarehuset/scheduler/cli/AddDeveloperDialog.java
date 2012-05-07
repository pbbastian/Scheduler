package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.NonProjectLeaderException;
import softwarehuset.scheduler.exceptions.NonRegisteredDeveloperException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class AddDeveloperDialog implements Dialog { // Kristian
    private Scheduler scheduler;
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public AddDeveloperDialog(Scheduler scheduler, Session session, Project project, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.project = project;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        boolean validDeveloper = false;
        while (!validDeveloper) {
            out.print("Enter the name of the developer you want to add: ");
            String developerName = new Scanner(in).nextLine();
            Developer developer = null;

            for (Developer d : scheduler.getDevelopers()) {
                if (d.getName().equalsIgnoreCase(developerName)) {
                    developer = d;
                    break;
                }
            }

            if (developer == null) {
                out.println("Developer not found, please try again.");
            } else {
                try {
                    session.addDeveloperToProject(developer, project);
                    validDeveloper = true;
                    out.println("You have successfully added '" + developer.getName()
                            + "' to the project '" + project.getName() + "'!");
                    previousDialog.display(in, out);
                } catch (NonProjectLeaderException e) {
                    e.printStackTrace();  // This shouldn't be possible
                } catch (NonRegisteredDeveloperException e) {
                    e.printStackTrace();  // This shouldn't be possible
                }
            }
        }
    }
}
