package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.PrivateActivity;
import softwarehuset.scheduler.exceptions.ArgumentException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class CreatePrivateActivity implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Dialog previousDialog;

    public CreatePrivateActivity(Scheduler scheduler, Session session, Dialog previousDialog) { // Kristian
        this.scheduler = scheduler;
        this.session = session;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();
        boolean validActivity = false;
        while(!validActivity) {
            out.print("Enter a description for the private activity: ");
            String description = new Scanner(in).nextLine();
            out.print("Enter a start week and year (separated by a space) for the private activity: ");
            Scanner scanner = new Scanner(in);
            GregorianCalendar start = Scheduler.getWeek(scanner.nextInt(), scanner.nextInt());
            out.print("Enter an end week and year (separated by a space) for the private activity: ");
            GregorianCalendar end = Scheduler.getWeek(scanner.nextInt(), scanner.nextInt());
            try {
                session.addPrivateActivity(new PrivateActivity(description, start, end));
                validActivity = true;
                out.println("You have successfully created a new private activity with the description '" + description + "'!");
                previousDialog.display(in, out);
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
