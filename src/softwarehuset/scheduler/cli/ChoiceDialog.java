package softwarehuset.scheduler.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ChoiceDialog implements Dialog {

    private Choice[] choices;
    private String choice;

    public ChoiceDialog(Choice[] choices) {
        this.choices = choices;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println("Available actions:");
        for (int i = 0; i < choices.length; i++) {
            out.printf("  [%d] %s\n", i+1, choices[i].getDescription());
        }
        
        int choice = -1;
        while (choice == -1) {
            out.print("Select an action: ");
            choice = new Scanner(in).nextInt() - 1;
            if (choice < 0 || choice >= choices.length) {
                choice = -1;
            }
        }
        
        choices[choice].getDialog().display(in, out);
    }

    public String getChoice() {
        return choice;
    }
}
