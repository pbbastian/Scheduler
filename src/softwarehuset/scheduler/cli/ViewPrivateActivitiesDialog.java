package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.PrivateActivity;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class ViewPrivateActivitiesDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Developer developer;
    private Dialog previousDialog;

    public ViewPrivateActivitiesDialog(Scheduler scheduler, Session session, Developer developer, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.developer = developer;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        if (developer.getPrivateActivities().isEmpty()) {
            out.println("\nYou don't have any private activities.");
            previousDialog.display(in, out);
        } else {
            int size = developer.getPrivateActivities().size();
            Choice[] choices = new Choice[size+1];
            for (int i = 0; i < size; i++) {
                PrivateActivity activity = developer.getPrivateActivities().get(i);
                String message = "W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                        + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR) + ": " + activity.getDescription();
                choices[i] = new Choice(message, new RemovePrivateActivityDialog(scheduler, session, activity, this));
            }
            choices[size] = new Choice("Go back", previousDialog);
            new ChoiceDialog(choices, "Private activities:", "Select a private activity to remove it (or just go back): ").display(in, out);
        }
    }
}
