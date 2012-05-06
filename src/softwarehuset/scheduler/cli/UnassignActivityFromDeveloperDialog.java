package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.ActivityNotAssignedToDeveloperException;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class UnassignActivityFromDeveloperDialog implements Dialog {
    private Session session;
    private Developer developer;
    private Activity activity;
    private Dialog previousDialog;

    public UnassignActivityFromDeveloperDialog(Session session, Activity activity, Developer developer, Dialog previousDialog) {
        this.session = session;
        this.developer = developer;
        this.activity = activity;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        try {
            session.unassignActivityFromDeveloper(activity, developer);
            out.println("You have successfully assigned the activity '" + activity.getDescription() + "' to the developer '" + developer.getName() + "'!");
        } catch (ActivityNotAssignedToDeveloperException e) {
            out.println("The developer is not assigned to that activity.");
        }
        previousDialog.display(in, out);
    }

    public static class FromActivityDialog implements Dialog {
        private Session session;
        private Activity activity;
        private Dialog previousDialog;

        public FromActivityDialog(Session session, Activity activity, Dialog previousDialog) {
            this.session = session;
            this.activity = activity;
            this.previousDialog = previousDialog;
        }

        @Override
        public void display(InputStream in, PrintStream out) {
            int size = activity.getDevelopers().size();
            Choice[] choices = new Choice[size+1];
            for (int i = 0; i < size; i++) {
                Developer developer = activity.getDevelopers().get(i);
                choices[i] = new Choice(developer.getName(), new UnassignActivityFromDeveloperDialog(session, activity, developer, previousDialog));
            }
            choices[size] = new Choice("Go back", previousDialog);
            new ChoiceDialog(choices, "Developers assigned to the activity:", "Select a developer: ").display(in, out);
        }
    }

    public static class FromDeveloperDialog implements Dialog {
        private Session session;
        private Developer developer;
        private Project project;
        private Dialog previousDialog;

        public FromDeveloperDialog(Session session, Developer developer, Project project, Dialog previousDialog) {
            this.session = session;
            this.developer = developer;
            this.project = project;
            this.previousDialog = previousDialog;
        }

        @Override
        public void display(InputStream in, PrintStream out) {
            int size = developer.getCurrentActivities().size();
            Choice[] choices = new Choice[size+1];
            for (int i = 0; i < size; i++) {
                Activity activity = developer.getCurrentActivities().get(i);
                String message = "W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                        + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR) + ": " + activity.getDescription();
                choices[i] = new Choice(message, new UnassignActivityFromDeveloperDialog(session, activity, developer, previousDialog));
            }
            choices[size] = new Choice("Go back", previousDialog);
            new ChoiceDialog(choices, "Activities assigned to this developer:", "Select an activity: ").display(in, out);
        }
    }
}
