package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Session;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class SelectProjectDialog implements Dialog {
    private Session session;
    private Dialog previousDialog;

    public SelectProjectDialog(Session session, Dialog previousDialog) {
        this.session = session;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        ArrayList<Choice> projectChoices = new ArrayList<Choice>(session.getDeveloper().getProjects().size());


        Choice[] projectChoicesArray = new Choice[projectChoices.size()];
        new ChoiceDialog(projectChoices.toArray(projectChoicesArray), "Available projects:", "Select a project: ").display(in, out);
    }
}
