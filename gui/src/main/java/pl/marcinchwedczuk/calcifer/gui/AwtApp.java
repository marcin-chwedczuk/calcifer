package pl.marcinchwedczuk.calcifer.gui;

import java.awt.*;
import java.io.IOException;

/**
 - This class is intended to start application as AWT application before initializing
 - JavaFX application. JavaFX does not support dock-icon-less application so we are
 - creating JavaFX application from AWT application so that we can achieve the desired
 - functionality.
 - */

public class AwtApp {
    static {
        //System.setProperty("prism.lcdtext", "false");

        // This is awt property which enables dock-icon-less
        // applications
        System.setProperty("apple.awt.UIElement", "true");
    }

    public static void main(String[] args) throws IOException, AWTException {

        java.awt.Toolkit.getDefaultToolkit();

        // This is a call to JavaFX application main method.
        // From now on we are transferring control to FX application.
        App.main(args);
    }
}
