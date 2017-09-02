package mohz.PCControllers;

import javafx.concurrent.Task;
import mohz.Communication.DatagramReceiver;
import mohz.NirCMD.NircmdHelper;

import java.io.File;
import java.net.SocketException;
import java.util.HashMap;

public class CommandHandler extends Task<Boolean> {
    private static DatagramReceiver commandReceiver;

    @Override
    protected Boolean call() throws Exception {
        try {
            commandReceiver = new DatagramReceiver(1316, 2000);
        } catch (SocketException se) {
            return false;
        }

        Thread cmdHandlerThread = new Thread(() -> {
            try{ cmdHandlingMethod(); } catch(Exception e) {cancel();}
        });
        cmdHandlerThread.start();

        return true;
    }

    private static String movieName;
    private static void cmdHandlingMethod() throws Exception {
        while(true) {
            String cmd = commandReceiver.getDatagramData();
            if(cmd == null) continue;
            if(cmd.contains("play movie")) {
                playProc();
            } else if(cmd.contains("resume")) {
                NircmdHelper.execute("sendkeypress 0xB3");
                NircmdHelper.execute("speak text \"Resuming!\"");
            } else if(cmd.contains("pause") || cmd.contains("pose")) {
                NircmdHelper.execute("sendkeypress 0xB3");
                NircmdHelper.execute("speak text \"Paused!\"");
            } else if(cmd.contains("full screen")) {
                NircmdHelper.execute("sendkeypress f");
                NircmdHelper.execute("speak text \"Done!\"");
            } else if(cmd.contains("stop")) {
                NircmdHelper.execute("sendkeypress s");
                NircmdHelper.execute("speak text \"Stopped\"");
            } else if(cmd.contains("close")) {
                NircmdHelper.execute("sendkeypress alt+F4");
                NircmdHelper.execute("speak text \"Closed\"");
            } else {
                NircmdHelper.execute("speak text \"Command does not exist!\"");
            }
        }
    }

    private static void playProc() throws Exception {
        NircmdHelper.execute("speak text \"Which movie?\"");
        movieName = commandReceiver.getDatagramData();
        File movie = getFile();

        if (movie != null) {
            NircmdHelper.execute("shexec \"open\" \"" + movie.getAbsolutePath() + "\"");
            NircmdHelper.execute("speak text \"Playing requested movie!\"");
            NircmdHelper.execute("win activate ititle \"media player\"");
            NircmdHelper.execute("win max ititle \"media player\"");
        } else {
            NircmdHelper.execute("speak text \"No such movie found!\"");
        }

    }

    private static File getFile() {
        //String videoDir = "N:\\Videos\\";
        String videoDir = System.getProperty("user.home") + "\\Videos\\";
        HashMap<File, Integer> videoPaths = new HashMap<>();

        fillFiles(videoPaths, new File(videoDir));
        setPriorities(videoPaths);

        return getMaxPriorityFile(videoPaths);
    }

    private static void fillFiles(HashMap<File, Integer> videoPaths, File file) {
        File[] flist = file.listFiles();

        for(File f: flist) {
            if(f.isFile() && isVideo(f)) {
                videoPaths.put(f, 0);
            } else if(f.isDirectory()) {
                fillFiles(videoPaths, f);
            }
        }
    }

    private static void setPriorities(HashMap<File, Integer> videoPaths) {
        for(File key: videoPaths.keySet()) {
            String mName = key.getName();
            mName = mName.substring(0, mName.lastIndexOf(".")).toLowerCase();

            if(mName.contains(movieName)) {
                int p = videoPaths.get(key);
                videoPaths.replace(key, p, p + 3);
            }

            String[] mkeywords = mName.split(" ");
            String[] inkeywords = movieName.split(" ");
            for(String inkw: inkeywords) {
                for(String kw: mkeywords) {
                    if(inkw.equals(kw)) {
                        int p = videoPaths.get(key);
                        videoPaths.replace(key, p, p + 1);
                    }
                }
            }
        }
    }

    private static File getMaxPriorityFile(HashMap<File, Integer> videoPaths) {
        int maxPriority = 0;
        File maxPriorityFile = null;

        for(File key: videoPaths.keySet()) {
            int priority = videoPaths.get(key);
            if(priority > maxPriority) {
                maxPriority = priority;
                maxPriorityFile = key;
            }
        }

        return maxPriorityFile;
    }

    private static boolean isVideo(File f) {
        String ext = f.getName();
        ext = ext.substring(ext.lastIndexOf(".") + 1);

        switch(ext) {
            case "3g2":
            case "3gp":
            case "amv":
            case "asf":
            case "avi":
            case "drc":
            case "flv":
            case "m4v":
            case "mkv":
            case "mov":
            case "qt":
            case "mp4":
            case "m4p":
            case "mpg":
            case "mp2":
            case "mpv":
            case "m2v":
            case "ogv":
            case "ogg":
            case "rm":
            case "rmvb":
            case "vob":
            case "webm":
            case "wmv": return true;
            default: return false;
        }
    }
}
