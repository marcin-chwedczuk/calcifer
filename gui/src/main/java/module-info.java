module pl.marcinchwedczuk.calcifer.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires java.desktop;
    requires java.prefs;

    exports pl.marcinchwedczuk.calcifer.gui;

    // Allow @FXML injection to private fields.
    opens pl.marcinchwedczuk.calcifer.gui.mainwindow;
    opens pl.marcinchwedczuk.calcifer.gui.aboutdialog;
}