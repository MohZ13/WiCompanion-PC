package mohz.PCControllers;

import javafx.concurrent.Task;
import mohz.Communication.DatagramReceiver;
import mohz.NirCMD.NircmdHelper;

import java.net.SocketException;

public class Mouse extends Task<Boolean> {
    private static DatagramReceiver posReceiver;
    private static DatagramReceiver clkReceiver;

    @Override
    protected Boolean call() {
        try {
            posReceiver = new DatagramReceiver(1313, 20);
            clkReceiver = new DatagramReceiver(1314, 20);
        } catch (SocketException se) {
            return false;
        }

        Thread coords = new Thread(() -> {
            try{ receiveCoords(); } catch(Exception e) {cancel();}
        });
        coords.start();

        Thread clicks = new Thread(() -> {
            try{ receiveClicks(); } catch(Exception e) {cancel();}
        });
        clicks.start();

        return true;
    }

    private static void receiveCoords() throws Exception {
        while(true) {
            String cmd = posReceiver.getDatagramData();

            String[] c = cmd.split(",");
            float x = Float.parseFloat(c[0]) * 1.5f;
            float y = Float.parseFloat(c[1]) * 1.5f;

            NircmdHelper.execute("movecursor " + x + " " + y);
        }
    }

    private static void  receiveClicks() throws Exception {
        while(true) {
            String cmd = clkReceiver.getDatagramData();

            NircmdHelper.execute("sendmouse " + cmd + " click");
        }
    }
}
