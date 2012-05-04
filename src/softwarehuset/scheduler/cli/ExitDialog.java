package softwarehuset.scheduler.cli;

import java.util.Scanner;

public class ExitDialog implements Dialog {
    private Dialog cancelDialog;

    public ExitDialog(Dialog cancelDialog) {
        this.cancelDialog = cancelDialog;
    }

    @Override
    public void display() {
        System.out.print("Are you sure you want to exit the application? ");
        if (new Scanner(System.in).next().equalsIgnoreCase("yes")) {
            System.exit(0);
        } else {
            cancelDialog.display();
        }
    }
}
