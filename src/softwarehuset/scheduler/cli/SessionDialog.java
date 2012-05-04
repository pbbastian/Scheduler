package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Session;

public class SessionDialog implements Dialog {
    private Session session;
    private Dialog previousDialog;

    public SessionDialog(Session session, Dialog previousDialog) {
        this.session = session;
        this.previousDialog = previousDialog;
    }

    @Override
    public void display() {
        Choice[] choices = new Choice[] {
                new Choice("Create a new project", null),
                new Choice("Select a project", null),
                new Choice("View activities that has been assigned to me", null),
                new Choice("Log out", previousDialog),
        };
        new ChoiceDialog(choices).display();
    }
}
