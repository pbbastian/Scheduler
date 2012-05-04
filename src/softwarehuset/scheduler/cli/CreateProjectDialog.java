package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.AlreadyRegisteredProjectException;
import softwarehuset.scheduler.exceptions.ArgumentException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class CreateProjectDialog implements Dialog {
    private Session session;
    private Dialog previousDialog;

    public CreateProjectDialog(Session session, Dialog previousDialog) {
        this.session = session;
        this.previousDialog = previousDialog;
    }
    
    @Override
    public void display(InputStream in, PrintStream out) {
        boolean validProject = false;
        while(!validProject) {
            out.print("Enter a project name: ");
            String name = new Scanner(in).nextLine();
            try {
                session.registerProject(new Project(name));
                validProject = true;
                out.println("You have successfully created a project named " + name + "!");
                previousDialog.display(in, out);
            } catch (AlreadyRegisteredProjectException e) {
                e.printStackTrace();  // This shouldn't be possible
            } catch (ArgumentException e) {
                out.println("Invalid name, please try again.");
            }
        }
    }
}
