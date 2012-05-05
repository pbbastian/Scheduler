package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;

import java.io.InputStream;
import java.io.PrintStream;

public class ApplicationDialog implements Dialog {
    private Scheduler scheduler;

    public ApplicationDialog(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println();
        ChoiceDialog choiceDialog1 = new ChoiceDialog(new Choice[]{
                new Choice("Log in", new LogInDialog(scheduler, this)),
                new Choice("Register as a new developer", new RegisterDialog(scheduler, this)),
                new Choice("Exit the application", new ExitDialog(this))
        }, "Available actions:", "Select an action: ");
        choiceDialog1.display(in, out);
    }
}
