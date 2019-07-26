package sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

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
    
    public void merge() throws InvalidFileFormatException, IOException, DocumentException {
    	PdfReader pdfReader = null;
    	
    	// Preparation
    	Wini selectIni = new Wini(new File(selectFile));
    	String description = selectIni.get("General", "Description");
    	int inputFilesCount = selectIni.get("General", "InputFilesCount", int.class);
    	String inputFileName = ""; String selectPages = ""; String[] selectPagesArray;
    	Document pdfIn = null; Document pdfOut = new Document(); PdfCopy copy = null;
    	
    	// Log here
    	
    	// For each file
    	for (int i = 1; i <= inputFilesCount; i++) {
    		// Preparation
    		// Log here
    		inputFileName = selectIni.get("Input" + i, "InputFile");
    		pdfReader = new PdfReader(inputPath + inputFileName + ".pdf");
    		copy = new PdfCopy(pdfOut, new FileOutputStream(saveFile));
    		
    		selectPages = selectIni.get("Input" + i, "SelectPages");
    		selectPagesArray = selectPages.split(",");
    		
    		for (String p : selectPagesArray) {
    			// Log here
    			
    			copy.addPage(copy.getImportedPage(pdfReader, Integer.valueOf(p)));
    		}
    	}
    }
}
