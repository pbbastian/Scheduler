package softwarehuset.scheduler.cli;

public class Choice { // Peter
    private String description;
    private Dialog dialog;

    public Choice(String description, Dialog dialog) {
        this.description = description;
        this.dialog = dialog;
    }

    public String getDescription() {
        return description;
    }

    public Dialog getDialog() {
        return dialog;
    }
}
