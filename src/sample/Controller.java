package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

    private Stage stage;
    private Scene scene;

    @FXML private TextField txtSelectFile;
    @FXML private TextField txtInputPath;
    @FXML private TextField txtOutputFile;

    private String selectFile = "";
    private String inputPath = "";
    private String saveFile = "";

    public void setup(Stage stage) {
        this.stage = stage;
        this.scene = stage.getScene();
    }

    public void browseSelectorFile() {
        FileChooser fileChooser = new FileChooser();
        selectFile = fileChooser.showOpenDialog(stage).getAbsoluteFile().toString();
        txtSelectFile.setText(selectFile);
    }

    public void browseInputPath() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        inputPath = dirChooser.showDialog(stage).getAbsolutePath() + "\\";
        txtInputPath.setText(inputPath);
    }

    public void browseOutputFile() {
        FileChooser fileChooser = new FileChooser();
        saveFile = fileChooser.showSaveDialog(stage).getAbsoluteFile().toString();
        txtOutputFile.setText(saveFile);
    }
}
