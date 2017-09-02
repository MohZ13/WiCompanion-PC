package mohz.NirCMD;

import java.io.IOException;

public class NircmdHelper {
    private final static String NIRCMD_EXE;
    private final static Runtime RUNTIME;

    static {
        NIRCMD_EXE = "nircmd.exe";
        RUNTIME = Runtime.getRuntime();
    }

    public static void execute(String command) throws IOException {
        RUNTIME.exec(NIRCMD_EXE + " " + command);
    }
}
