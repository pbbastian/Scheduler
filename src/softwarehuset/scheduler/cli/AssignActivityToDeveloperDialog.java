package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.DeveloperNotInProjectException;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class AssignActivityToDeveloperDialog implements Dialog {
    private Session session;
    private Developer developer;
    private Activity activity;
    private Dialog previousDialog;

    public AssignActivityToDeveloperDialog(Session session, Activity activity, Developer developer, Dialog previousDialog) {
        this.session = session;
        this.developer = developer;
        this.activity = activity;
        this.previousDialog = previousDialog;
    }
    
    @Override
    public void display(InputStream in, PrintStream out) {
        try {
            session.assignActivityToDeveloper(activity, developer);
            out.println("You have successfully assigned the activity '" + activity.getDescription() + "' to the developer '" + developer.getName() + "'!");
        } catch (InsufficientRightsException e) {
            out.println("You are not allowed to do that."); // Shouldn't be possible
        } catch (DeveloperNotInProjectException e) {
            out.println("That developer is not in the project."); // Shouldn't be possible
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
            int size = activity.getProject().getDevelopers().size();
            Choice[] choices = new Choice[size+1];
            for (int i = 0; i < size; i++) {
                Developer developer = activity.getProject().getDevelopers().get(i);
                choices[i] = new Choice(developer.getName(), new AssignActivityToDeveloperDialog(session, activity, developer, previousDialog));
            }
            choices[size] = new Choice("Go back", previousDialog);
            new ChoiceDialog(choices, "Developers in this project:", "Select a developer: ").display(in, out);
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
            int size = project.getActivities().size();
            Choice[] choices = new Choice[size+1];
            for (int i = 0; i < size; i++) {
                Activity activity = project.getActivities().get(i);
                String message = "W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                        + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR) + ": " + activity.getDescription();
                choices[i] = new Choice(message, new AssignActivityToDeveloperDialog(session, activity, developer, previousDialog));
            }
            choices[size] = new Choice("Go back", previousDialog);
            new ChoiceDialog(choices, "Activities in this project:", "Select an activity: ").display(in, out);
        }
    }
}
