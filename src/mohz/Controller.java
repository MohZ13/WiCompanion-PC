package mohz;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import mohz.PCControllers.CommandHandler;
import mohz.PCControllers.Keyboard;
import mohz.PCControllers.Mouse;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class Controller {
    public Label cmdInterfaceLabel;
    public Label keyboardInterfaceLabel;
    public Label mouseInterfaceLabel;
    public TextArea logText;
    public Label ipAddress;

    public void initialize() {
        setIpAddress();
        startMouseTask();
        startCommandHandlerTask();
        startKeyboardTask();
    }

    private void setIpAddress() {
        try {
            ipAddress.setText(InetAddress.getLocalHost().toString());
        } catch (UnknownHostException e) {
            ipAddress.setText("Not found!");
        }
    }

    private void startMouseTask() { Mouse mouseTask = new Mouse();
        mouseTask.setOnSucceeded(event -> {
            mouseInterfaceLabel.setText("Running!");
            mouseInterfaceLabel.setTextFill(Paint.valueOf("#009900"));
            logText.appendText(new Date(System.currentTimeMillis()) + " Mouse interface running...\n");
        });
        mouseTask.setOnFailed(event -> {
            mouseInterfaceLabel.setText("Problem!");
            logText.appendText(new Date(System.currentTimeMillis()) + " There is some problem with mouse interface...\n");
        });
        mouseTask.setOnCancelled(event -> {
            mouseInterfaceLabel.setText("Problem!");
            logText.appendText(new Date(System.currentTimeMillis()) + " There is some problem with mouse interface...\n");
        });

        Thread mouseThread = new Thread(mouseTask);
        mouseThread.setPriority(Thread.MAX_PRIORITY);
        mouseThread.setDaemon(true);
        mouseThread.start();
        logText.appendText(new Date(System.currentTimeMillis()) + " Starting mouse interface...\n");
    }

    private void startCommandHandlerTask() {
        CommandHandler commandHandlerTask = new CommandHandler();
        commandHandlerTask.setOnSucceeded(event -> {
            cmdInterfaceLabel.setText("Running!");
            cmdInterfaceLabel.setTextFill(Paint.valueOf("#009900"));
            logText.appendText(new Date(System.currentTimeMillis()) + " Command interface running...\n");
        });
        commandHandlerTask.setOnFailed(event -> {
            cmdInterfaceLabel.setText("Problem!");
            logText.appendText(new Date(System.currentTimeMillis()) + " There is some problem with command interface...\n");
        });
        commandHandlerTask.setOnCancelled(event -> {
            cmdInterfaceLabel.setText("Problem!");
            logText.appendText(new Date(System.currentTimeMillis()) + " There is some problem with command interface...\n");
        });

        Thread commandThread = new Thread(commandHandlerTask);
        commandThread.setDaemon(true);
        commandThread.start();
        logText.appendText(new Date(System.currentTimeMillis()) + " Starting command interface...\n");
    }

    private void startKeyboardTask() {
        Keyboard keyboardTask = new Keyboard();
        keyboardTask.setOnSucceeded(event -> {
            keyboardInterfaceLabel.setText("Running!");
            keyboardInterfaceLabel.setTextFill(Paint.valueOf("#009900"));
            logText.appendText(new Date(System.currentTimeMillis()) + " Keyboard interface running...\n");
        });
        keyboardTask.setOnFailed(event -> {
            keyboardInterfaceLabel.setText("Problem!");
            logText.appendText(new Date(System.currentTimeMillis()) + " There is some problem with keyboard interface...\n");
        });
        keyboardTask.setOnCancelled(event -> {
            keyboardInterfaceLabel.setText("Problem!");
            logText.appendText(new Date(System.currentTimeMillis()) + " There is some problem with keyboard interface...\n");
        });

        Thread keyboardThread = new Thread(keyboardTask);
        keyboardThread.setDaemon(true);
        keyboardThread.start();
        logText.appendText(new Date(System.currentTimeMillis()) + " Starting keyboard interface...\n");
    }
}