package sample;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    @FXML private Button btnMerge;

    private String selectFile = "";
    private String inputPath = "";
    private String saveFile = "";

    public void setup(Stage stage) {
        this.stage = stage;
        this.scene = stage.getScene();
    }

    public void browseSelectorFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        selectFile = file != null ? file.getAbsoluteFile().toString() : "";
        txtSelectFile.setText(selectFile);
        mergeButtonAvailable();
    }

    public void browseInputPath() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File dir = dirChooser.showDialog(stage);
        inputPath = dir != null ? dir.getAbsolutePath() + "\\" : "";
        txtInputPath.setText(inputPath);
        mergeButtonAvailable();
    }

    public void browseOutputFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(stage);
        saveFile = file != null ? file.getAbsoluteFile().toString() : "";
        txtOutputFile.setText(saveFile);
        mergeButtonAvailable();
    }
    
    private void mergeButtonAvailable() {
    	if (!txtSelectFile.getText().equals("") && !txtInputPath.getText().equals("") && !txtOutputFile.getText().equals("")) {
    		btnMerge.setDisable(false);
    	} else {
    		btnMerge.setDisable(true);
    	}
    }
}
