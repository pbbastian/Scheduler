package softwarehuset.scheduler.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ChoiceDialog implements Dialog {
    private Choice[] choices;
    private String titleText;
    private String selectText;

    public ChoiceDialog(Choice[] choices, String titleText, String selectText) {
        this.choices = choices;
        this.titleText = titleText;
        this.selectText = selectText;
    }

    @Override
    public void display(InputStream in, PrintStream out) {
        out.println(titleText);
        for (int i = 0; i < choices.length; i++) {
            out.printf("  [%d] %s\n", i+1, choices[i].getDescription());
        }

        int choice = -1;
        while (choice == -1) {
            out.print(selectText);
            choice = new Scanner(in).nextInt() - 1;
            if (choice < 0 || choice >= choices.length) {
                choice = -1;
            }
        }

        if (!(choices[choice].getDialog() instanceof ContinueDialog)) {
            choices[choice].getDialog().display(in, out);
        }
    }
}
