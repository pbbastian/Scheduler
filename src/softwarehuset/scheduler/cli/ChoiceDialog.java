package softwarehuset.scheduler.cli;

import java.util.Scanner;

public class ChoiceDialog implements Dialog {

    private Choice[] choices;
    private String choice;

    public ChoiceDialog(Choice[] choices) {
        this.choices = choices;
    }

    @Override
    public void display() {
        System.out.println("Available actions:");
        for (int i = 0; i < choices.length; i++) {
            System.out.printf("  [%d] %s\n", i+1, choices[i].getDescription());
        }
        
        int choice = -1;
        while (choice == -1) {
            System.out.print("Select an action: ");
            choice = new Scanner(System.in).nextInt() - 1;
            if (choice < 0 || choice >= choices.length) {
                choice = -1;
            }
        }
        
        choices[choice].getDialog().display();
    }

    public String getChoice() {
        return choice;
    }
}
