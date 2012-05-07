package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.PrivateActivity;
import softwarehuset.scheduler.domain.Project;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProjectDeveloperDialog implements Dialog { // Peter
    private Session session;
    private Project project;
    private Developer developer;
    private Dialog previousDialog;

    public ProjectDeveloperDialog(Session session, Project project, Developer developer, Dialog previousDialog) {
        this.session = session;
        this.project = project;
        this.developer = developer;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println("Name: " + developer.getName());
        out.print("Activities: ");
        if (developer.getCurrentActivities().isEmpty()) {
            out.print("None.");
        } else {
            Activity activity = developer.getCurrentActivities().get(0);
            out.print("W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                    + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR));
            for (int i = 1; i < developer.getCurrentActivities().size(); i++) {
                activity = developer.getCurrentActivities().get(i);
                out.print(", " + "W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                        + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR));
            }
        }
        out.println();
        out.print("Private activities: ");
        if (developer.getPrivateActivities().isEmpty()) {
            out.print("None.");
        } else {
            PrivateActivity activity = developer.getPrivateActivities().get(0);
            out.print("W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                    + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR));
            for (int i = 1; i < developer.getCurrentActivities().size(); i++) {
                activity = developer.getPrivateActivities().get(i);
                out.print(", " + "W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                        + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR));
            }
        }
        out.println();
        out.println();
        
        List<Choice> choiceList = new ArrayList<Choice>();
        if (project.getProjectLeader().equals(session.getDeveloper())) {
            choiceList.add(new Choice("Assign to activity", new AssignActivityToDeveloperDialog.FromDeveloperDialog(session, developer, project, this)));
            choiceList.add(new Choice("Unassign from activity", new UnassignActivityFromDeveloperDialog.FromDeveloperDialog(session, developer, project, this)));
            choiceList.add(new Choice("Remove from project", new RemoveDeveloperFromProjectDialog(session, project, developer, previousDialog)));
        }
        choiceList.add(new Choice("Go back", previousDialog));
        Choice[] choices = choiceList.toArray(new Choice[choiceList.size()]);
        new ChoiceDialog(choices, "Available actions:", "Select an action: ").display(in, out);
    }
}
