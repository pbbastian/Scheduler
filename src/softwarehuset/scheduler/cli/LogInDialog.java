package softwarehuset.scheduler.cli;

import softwarehuset.scheduler.application.Scheduler;
import softwarehuset.scheduler.application.Session;
import softwarehuset.scheduler.exceptions.IncorrectCredentialsException;

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
    public void display() {
        boolean validLogin = false;
        while(!validLogin) {
            System.out.print("Enter your name: ");
            String name = new Scanner(System.in).nextLine();
            System.out.print("Enter your PIN: ");
            String pin = new Scanner(System.in).nextLine();
            try {
                session = scheduler.logIn(name, pin);
                validLogin = true;
                System.out.println("You are now logged in as " + name + "!");
                new SessionDialog(session, previousDialog).display();
            } catch (IncorrectCredentialsException e) {
                System.out.println("Invalid name and PIN combination");
            }
        }
    }

    public Session getSession() {
        return session;
    }
}
