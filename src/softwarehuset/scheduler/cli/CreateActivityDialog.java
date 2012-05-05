package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.ArgumentException;
import softwarehuset.scheduler.exceptions.NonProjectLeaderException;
import softwarehuset.scheduler.exceptions.NonRegisteredProjectException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class CreateActivityDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public CreateActivityDialog(Scheduler scheduler, Session session, Project project, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.project = project;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();
        boolean validActivity = false;
        while(!validActivity) {
            out.print("Enter a description for the activity: ");
            String description = new Scanner(in).nextLine();
            out.print("Enter a start week and year (separated by a space) for the activity: ");
            Scanner scanner = new Scanner(in);
            GregorianCalendar start = Scheduler.getWeek(scanner.nextInt(), scanner.nextInt());
            out.print("Enter an end week and year (separated by a space) for the activity: ");
            GregorianCalendar end = Scheduler.getWeek(scanner.nextInt(), scanner.nextInt());
            try {
                session.addActivityToProject(new Activity(description, start, end), project);
                validActivity = true;
                out.println("You have successfully created a new activity with the description '" + description + "'!");
                previousDialog.display(in, out);
            } catch (NonRegisteredProjectException e) {
                e.printStackTrace();  // This should not be possible
            } catch (NonProjectLeaderException e) {
                e.printStackTrace();  // This should not be possible
            } catch (ArgumentException e) {
                if (e.getArgument().equals(description)) {
                    out.print("Invalid description. ");
                }
                if (e.getArgument().equals(start)) {
                    out.print("Invalid start week. ");
                }
                if (e.getArgument().equals(end)) {
                    out.print("Invalid end week. ");
                }
                System.out.println("Please try again.");
            }
        }
    }
}
