package mohz.PCControllers;

import javafx.concurrent.Task;
import mohz.Communication.DatagramReceiver;
import mohz.NirCMD.NircmdHelper;

import java.io.IOException;
import java.net.SocketException;

public class Keyboard extends Task<Boolean> {
    private static DatagramReceiver keyReceiver;

    @Override
    protected Boolean call() throws Exception {
        try {
            keyReceiver = new DatagramReceiver(1315, 20);
        } catch (SocketException se) {
            return false;
        }

        Thread keyThread = new Thread(() -> {
            try{ handleKeyStrokes(); } catch(Exception e) {cancel();}
        });
        keyThread.start();

        return true;
    }

    private void handleKeyStrokes() throws IOException {
        while (true) {
            String keys = keyReceiver.getDatagramData();

            switch(keys) {
                case "up": keys = Key.UP; break;
                case "down": keys = Key.DOWN; break;
                case "left": keys = Key.LEFT; break;
                case "right": keys = Key.RIGHT; break;

                case "win": keys = Key.WINDOWS_KEY; break;
                case "enter": keys = Key.ENTER; break;
                case "tab": keys = Key.TAB; break;

                case "home": keys = Key.HOME; break;
                case "end": keys = Key.END; break;
                case "pgup": keys = Key.PGUP; break;
                case "pgdn": keys = Key.PGDN; break;

                case "alttab": keys = combineKeys(Key.ALT, Key.TAB); break;
                case "altf4": keys = combineKeys(Key.ALT, Key.F4); break;

                case "explorer": keys = combineKeys(Key.WINDOWS_KEY, Key.E); break;
                case "desktop": keys = combineKeys(Key.WINDOWS_KEY, Key.D); break;
                case "search": keys = combineKeys(Key.WINDOWS_KEY, Key.Q); break;
                case "minimize": keys = combineKeys(Key.WINDOWS_KEY, Key.M); break;
                case "task_view": keys = combineKeys(Key.WINDOWS_KEY, Key.TAB); break;
                case "cortana": keys = combineKeys(Key.WINDOWS_KEY, Key.C); break;
                case "run": keys = combineKeys(Key.WINDOWS_KEY, Key.R); break;
                case "screenshot": keys = combineKeys(Key.WINDOWS_KEY, Key.PRTSC); break;
                case "settings": keys = combineKeys(Key.WINDOWS_KEY, Key.I); break;
                case "winTools": keys = combineKeys(Key.WINDOWS_KEY, Key.X); break;

                case "winUp": keys = combineKeys(Key.WINDOWS_KEY, Key.UP); break;
                case "winDown": keys = combineKeys(Key.WINDOWS_KEY, Key.DOWN); break;
                case "winLeft": keys = combineKeys(Key.WINDOWS_KEY, Key.LEFT); break;
                case "winRight": keys = combineKeys(Key.WINDOWS_KEY, Key.RIGHT); break;

                case "brightnessUp":
                case "brightnessDown": handleBrightness(keys); continue;
                case "volumeUp":
                case "volumeDown": handleVolume(keys); continue;
                case "offMonitor":
                case "onMonitor":
                case "lockComputer":
                case "sleepComputer":
                case "shutDownComputer":
                case "restartComputer": handleOperation(keys); continue;
            }

            NircmdHelper.execute("sendkeypress " + keys);
        }
    }

    private void handleVolume(String key) throws IOException {
        switch (key) {
            case "volumeUp": NircmdHelper.execute("changesysvolume 655.35"); break;
            case "volumeDown": NircmdHelper.execute("changesysvolume -655.35"); break;
        }
    }

    private void handleBrightness(String key) throws IOException {
        switch (key) {
            case "brightnessUp": NircmdHelper.execute("changebrightness 10"); break;
            case "brightnessDown": NircmdHelper.execute("changebrightness -10"); break;
        }
    }

    private void handleOperation(String key) throws IOException {
        switch (key) {
            case "offMonitor": NircmdHelper.execute("monitor off"); break;
            case "onMonitor": NircmdHelper.execute("monitor on"); break;
            case "lockComputer": NircmdHelper.execute("lockws"); break;
            case "sleepComputer": NircmdHelper.execute("standby"); break;
            case "shutDownComputer": NircmdHelper.execute("exitwin shutdown"); break;
            case "restartComputer": NircmdHelper.execute("exitwin reboot"); break;
        }
    }

    private class Key {
        final static String WINDOWS_KEY = "0x5B";
        final static String ALT = "0x12";
        final static String TAB = "0x09";
        final static String F4 = "0x73";
        final static String ENTER = "0x0D";
        final static String PRTSC = "0x2C";
        final static String PGUP = "0x21";
        final static String PGDN = "0x22";
        final static String END = "0x23";
        final static String HOME = "0x24";

        final static String LEFT = "0x25";
        final static String UP = "0x26";
        final static String RIGHT = "0x27";
        final static String DOWN = "0x28";

        final static String C = "0x43";
        final static String D = "0x44";
        final static String E = "0x45";
        final static String I = "0x49";
        final static String M = "0x4D";
        final static String Q = "0x51";
        final static String R = "0x52";
        final static String X = "0x58";
    }

    private String combineKeys(String keyOne, String keyTwo) {
        return keyOne + "+" + keyTwo;
    }
}
