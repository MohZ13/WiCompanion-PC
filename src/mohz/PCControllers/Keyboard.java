package mohz.PCControllers;

import javafx.concurrent.Task;
import mohz.Communication.DatagramReceiver;
import mohz.NirCMD.NircmdHelper;

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

    private void handleKeyStrokes() throws Exception {
        while (true) {
            String keys = keyReceiver.getDatagramData();

            switch(keys) {
                case "up": keys = "0x26"; break;
                case "down": keys = "0x28"; break;
                case "left": keys = "0x25"; break;
                case "right": keys = "0x27"; break;
                case "win": keys = "0x5B"; break;
                case "enter": keys = "0x0D"; break;
                case "tab": keys = "0x09"; break;
                case "alttab": keys = "0x12+0x09"; break;
                case "altf4": keys = "0x12+0x73"; break;
                default: keys = "";
            }

            if (!keys.equals("")) {
                NircmdHelper.execute("sendkeypress " + keys);
            }
        }
    }
}
