package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;

public class Application { // Peter
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();

        new ApplicationDialog(scheduler).display(System.in, System.out);
    }
}
