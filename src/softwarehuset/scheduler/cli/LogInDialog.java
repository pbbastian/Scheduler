package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.exceptions.IncorrectCredentialsException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class LogInDialog implements Dialog {
    private Scheduler scheduler;
    private Dialog previousDialog;
    private Session session;

    public LogInDialog(Scheduler scheduler, Dialog previousDialog) {
        this.scheduler = scheduler;
        this.previousDialog = previousDialog;
    }
    
    @Override
    public void display(InputStream in, PrintStream out) {
        boolean validLogin = false;
        while(!validLogin) {
            out.print("Enter your name: ");
            String name = new Scanner(in).nextLine();
            out.print("Enter your PIN: ");
            String pin = new Scanner(in).nextLine();
            try {
                session = scheduler.logIn(name, pin);
                validLogin = true;
                out.println("You are now logged in as " + name + "!");
                new SessionDialog(session, previousDialog).display(in, out);
            } catch (IncorrectCredentialsException e) {
                out.println("Invalid name and PIN combination, please try again.");
            }
        }
    }

    public Session getSession() {
        return session;
    }
}
