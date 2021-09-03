package pl.marcinchwedczuk.calcifer.gui.mainwindow;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.marcinchwedczuk.calcifer.gui.aboutdialog.AboutDialog;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {

    public static MainWindow show(Stage window) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("MainWindow.fxml"));

            MainWindow controller = new MainWindow();
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            // window.initStyle(StageStyle.UNDECORATED);
            window.setTitle("calcifer");
            window.setScene(scene);
            window.setResizable(true);

            window.show();

            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private MenuBar mainMenu;

    @FXML
    private BorderPane mainWindow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
        Node popupContent = datePickerSkin.getPopupContent();

        mainWindow.setCenter(popupContent);
    }

    @FXML
    private void guiShowAbout() {
        AboutDialog.show(thisWindow());
    }

    @FXML
    private void guiExit() {
        Platform.exit();
    }

    private Stage thisWindow() {
        return (Stage)this.mainMenu.getScene().getWindow();
    }
}
