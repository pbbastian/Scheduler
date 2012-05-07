package softwarehuset.scheduler.cli;

import java.io.InputStream;
import java.io.PrintStream;

public interface Dialog { // Peter
    void display(InputStream in, PrintStream out);
}
