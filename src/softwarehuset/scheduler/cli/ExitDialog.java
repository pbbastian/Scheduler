package softwarehuset.scheduler.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ExitDialog implements Dialog {
    private Dialog cancelDialog;

    public ExitDialog(Dialog cancelDialog) {
        this.cancelDialog = cancelDialog;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.print("Are you sure you want to exit the application? ");
        if (new Scanner(in).next().equalsIgnoreCase("yes")) {
            System.exit(0);
        } else {
            cancelDialog.display(in, out);
        }
    }
}
