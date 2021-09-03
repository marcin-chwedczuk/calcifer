package pl.marcinchwedczuk.calcifer.gui;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.marcinchwedczuk.calcifer.gui.mainwindow.MainWindow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * JavaFX App
 *
 * TrayIcon impl based on: https://gist.github.com/jewelsea/e231e89e8d36ef4e5d8a
 */
public class App extends Application {
    public static boolean testMode = false;

    private static HostServices hostServices = null;
    public static HostServices hostServices() {
        if (hostServices == null) {
            throw new IllegalStateException();
        }
        return hostServices;
    }

    private Stage stage;
    private java.awt.SystemTray tray;
    private java.awt.TrayIcon trayIcon;

    // sets up the javafx application.
    // a tray icon is setup for the icon, but the main stage remains invisible until the user
    // interacts with the tray icon.
    @Override
    public void start(final Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            showExceptionDialog(e);
        });

        App.hostServices = this.getHostServices();

        this.stage = stage;

        // instructs the javafx system not to exit implicitly when the last application window is shut.
        Platform.setImplicitExit(false);

        // sets up the tray icon (using awt code run on the swing thread).
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

        MainWindow.show(stage);
    }

    /**
     * Sets up a system tray icon for the application.
     */
    private void addAppToTray() {
        try {
            // ensure awt toolkit is initialized.
            // java.awt.Toolkit.getDefaultToolkit();

            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            // set up a system tray icon.
            this.tray = java.awt.SystemTray.getSystemTray();
            URL imageLoc = App.class.getResource("icons/calendar.png");
            java.awt.Image image = ImageIO.read(imageLoc);
            BufferedImage icon2 = TrayIconPainter.textToImage("01 January 2000");
            this.trayIcon = new java.awt.TrayIcon(icon2);



            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        Platform.runLater(() -> showStage());
                    } else if (e.getClickCount() == 2) {
                        Platform.exit();
                        // Must be called on Swing Thread
                        tray.remove(trayIcon);
                    }
                }
            });

            /*
            javax.swing.SwingUtilities.invokeLater(() ->
                                    trayIcon.displayMessage(
                                            "hello",
                                            "The time is now " + timeFormat.format(new Date()),
                                            java.awt.TrayIcon.MessageType.INFO
                                    )
                            );
             */

            tray.add(trayIcon);
        } catch (AWTException | IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
            Platform.exit();
        }
    }

    /**
     * Shows the application stage and ensures that it is brought ot the front of all stages.
     */
    private void showStage() {
        if (stage != null) {
            if (stage.isShowing()) {
                stage.hide();
            } else {
                stage.show();
                stage.toFront();
            }
        }
    }

    public static void main(String[] args) throws IOException, java.awt.AWTException {


        // Just launches the JavaFX application.
        // Due to way the application is coded, the application will remain running
        // until the user selects the Exit menu option from the tray icon.
        launch(args);
    }

    private void showExceptionDialog(Throwable e) {
        StringBuilder msg = new StringBuilder();
        msg.append("Unhandled exception:\n");

        Throwable curr = e;
        while (curr != null) {
            if (curr.getMessage() != null && !curr.getMessage().isBlank()) {
                msg.append(curr.getClass().getSimpleName()).append(": ")
                        .append(curr.getMessage())
                        .append("\n");
            }
            curr = curr.getCause();
        }

        UiService.errorDialog("Unhandled exception", msg.toString());
    }
}