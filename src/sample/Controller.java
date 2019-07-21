package sample;

import javafx.event.ActionEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

    private Stage stage;

    public void setup(Stage stage) {
        this.stage = stage;
    }

    public void browseSelectorFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.showOpenDialog(stage);
    }

    public void browseInputPath(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.showDialog(stage);
    }

    public void browseOutputFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.showSaveDialog(stage);
    }
}
