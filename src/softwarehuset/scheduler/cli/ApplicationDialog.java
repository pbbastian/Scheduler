package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;

public class ApplicationDialog implements Dialog {
    private Scheduler scheduler;

    public ApplicationDialog(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void display() {
        ChoiceDialog choiceDialog1 = new ChoiceDialog(new Choice[]{
                new Choice("Log in", new LogInDialog(scheduler, this)),
                new Choice("Register", new RegisterDialog(scheduler, this)),
                new Choice("Exit application", new ExitDialog(this))
        });
        choiceDialog1.display();
    }
}
