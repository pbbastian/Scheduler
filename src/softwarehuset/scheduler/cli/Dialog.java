package softwarehuset.scheduler.cli;

import java.io.InputStream;
import java.io.PrintStream;

public interface Dialog {
    void display(InputStream in, PrintStream out);
}
