package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.domain.Project;
import softwarehuset.scheduler.exceptions.InsufficientRightsException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class RemoveDeveloperFromProjectDialog implements Dialog {
    private Scheduler scheduler;
    private Session session;
    private Project project;
    private Dialog previousDialog;

    public RemoveDeveloperFromProjectDialog(Scheduler scheduler, Session session, Project project, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.session = session;
        this.project = project;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        List<Choice> choiceList = new ArrayList<Choice>();
        for (Developer developer : project.getDevelopers()) {
            choiceList.add(new Choice(developer.getName(), new InnerDialog(scheduler, session, project, developer, previousDialog)));
        }
        choiceList.add(new Choice("Go back", previousDialog));
        Choice[] choices = choiceList.toArray(new Choice[choiceList.size()]);
        new ChoiceDialog(choices, "Developers in project:", "Select a developer you want to remove from the project").display(in, out);
    }

    public class InnerDialog implements Dialog {
        private Scheduler scheduler;
        private Session session;
        private Project project;
        private Developer developer;
        private Dialog previousDialog;

        public InnerDialog(Scheduler scheduler, Session session, Project project, Developer developer, Dialog previousDialog) {
            this.scheduler = scheduler;
            this.session = session;
            this.project = project;
            this.developer = developer;
            this.previousDialog = previousDialog;
        }

        @Override
        public void display(InputStream in, PrintStream out) {
            try {
                session.removeDeveloperFromProject(developer, project);
                out.println("You have successfully removed '" + developer.getName() + "' from the project!");
            } catch (InsufficientRightsException e) {
                e.printStackTrace();  // This shouldn't be possible
            }
        }
    }
}
