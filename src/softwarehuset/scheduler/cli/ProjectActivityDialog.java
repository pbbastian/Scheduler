package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Activity;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProjectActivityDialog implements Dialog {
    private Session session;
    private Activity activity;
    private Dialog previousDialog;

    public ProjectActivityDialog(Session session, Activity activity, Dialog previousDialog) {
        this.session = session;
        this.activity = activity;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println("Description: " + activity.getDescription());
        out.println("Period: " + "W" + activity.getStart().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR)
                + " -> W" + activity.getEnd().get(Calendar.WEEK_OF_YEAR) + " Y" + activity.getStart().get(Calendar.YEAR));
        out.print("Developers: ");
        if (activity.getDevelopers().isEmpty()) {
            out.print("None.");
        } else {
            out.print(activity.getDevelopers().get(0).getName());
            for (int i = 1; i < activity.getDevelopers().size(); i++) {
                out.print(", " + activity.getDevelopers().get(i).getName());
            }
        }
        out.println();
        out.println();
        List<Choice> choiceList = new ArrayList<Choice>();
        if (activity.getProject().getProjectLeader().equals(session.getDeveloper()) || activity.getDevelopers().contains(session.getDeveloper())) {
            choiceList.add(new Choice("Set status", new SetActivityStatusDialog(session, activity, this)));
        }
        if (activity.getProject().getProjectLeader().equals(session.getDeveloper())) {
            choiceList.add(new Choice("Assign a developer to this activity", new AssignActivityToDeveloperDialog.FromActivityDialog(session, activity, this)));
            if (!activity.getDevelopers().isEmpty()) {
                choiceList.add(new Choice("Unassign a developer from this activity", new UnassignActivityFromDeveloperDialog.FromActivityDialog(session, activity, this)));
            }
            choiceList.add(new Choice("Remove this activity", new RemoveActivityDialog(session, activity, this)));
        }
        choiceList.add(new Choice("Go back", previousDialog));
        Choice[] choices = choiceList.toArray(new Choice[choiceList.size()]);
        new ChoiceDialog(choices, "Available actions:", "Select an action: ").display(in, out);
    }
}
