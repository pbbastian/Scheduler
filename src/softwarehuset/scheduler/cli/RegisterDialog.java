package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.domain.Developer;
import softwarehuset.scheduler.exceptions.ArgumentException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class RegisterDialog implements Dialog {
    private Scheduler scheduler;
    private Dialog returnDialog;

    public RegisterDialog(Scheduler scheduler, Dialog returnDialog) {
        this.scheduler = scheduler;
        this.returnDialog = returnDialog;
    }
    
    @Override
    public void display(InputStream in, PrintStream out) {
        boolean validRegistration = false;
        while(!validRegistration) {
            System.out.print("Enter your name: ");
            String name = new Scanner(System.in).nextLine();
            System.out.print("Enter your PIN: ");
            String pin = new Scanner(System.in).nextLine();
            try {
                scheduler.register(new Developer(name, pin));
                System.out.println("You have successfully registered as " + name + "!");
                validRegistration = true;
                returnDialog.display(System.in, System.out);
            } catch (ArgumentException e) {
                if (e.getArgument().equals(name)) {
                    System.out.println("Invalid name, please try again.");
                } else if (e.getArgument().equals(pin)) {
                    System.out.println("Invalid pin, please try again");
                }
            }
        }
    }
}
