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
import javafx.scene.control.TextArea;
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
    @FXML private TextArea txtLog;

    private String selectFile = "";
    private String inputPath = "";
    private String saveFile = "";
    private String configFile = "config.ini";

    public void setup(Stage stage) throws InvalidFileFormatException, IOException {
        this.stage = stage;
        this.scene = stage.getScene();
        loadConfig();
    }

    private void loadConfig() throws InvalidFileFormatException, IOException {
    	Wini config = new Wini(new File(configFile));
    	
    	String confSelectFile = config.get("General", "SelectFile");
    	String confInputPath = config.get("General", "InputPath");
    	String confOutputFile = config.get("General", "SaveFile");
    	
    	txtSelectFile.setText(confSelectFile);
    	txtInputPath.setText(confInputPath);
    	txtOutputFile.setText(confOutputFile);
    	
    	mergeButtonAvailable();
    }
    
    public void browseSelectorFile() throws InvalidFileFormatException, IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        selectFile = file != null ? file.getAbsoluteFile().toString() : "";
        txtSelectFile.setText(selectFile);
        
        Wini config = new Wini(new File(configFile));
        config.put("General", "SelectFile", selectFile);
        config.store();
        
        mergeButtonAvailable();
    }

    public void browseInputPath() throws InvalidFileFormatException, IOException {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File dir = dirChooser.showDialog(stage);
        inputPath = dir != null ? dir.getAbsolutePath() + "\\" : "";
        txtInputPath.setText(inputPath);
        
        Wini config = new Wini(new File(configFile));
        config.put("General", "InputPath", inputPath);
        config.store();
        
        mergeButtonAvailable();
    }

    public void browseOutputFile() throws InvalidFileFormatException, IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(stage);
        saveFile = file != null ? file.getAbsoluteFile().toString() : "";
        txtOutputFile.setText(saveFile);
        
        Wini config = new Wini(new File(configFile));
        config.put("General", "SaveFile", saveFile);
        config.store();
        
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
    	btnMerge.setDisable(true); txtLog.clear();
    	PdfReader pdfReader = null;
    	
    	// Preparation
    	Wini selectIni = new Wini(new File(selectFile));
    	String description = selectIni.get("General", "Description");
    	int inputFilesCount = selectIni.get("General", "InputFilesCount", int.class);
    	String inputFileName = ""; String selectPages = ""; String[] selectPagesArray;
    	Document pdfIn = null; Document pdfOut = new Document(); PdfCopy copy = null;
    	
    	txtLog.appendText(description + "\n\n");
    	
    	copy = new PdfCopy(pdfOut, new FileOutputStream(saveFile));
		pdfOut.open();
    	
    	// For each file
    	for (int i = 1; i <= inputFilesCount; i++) {
    		// Preparation
    		txtLog.appendText("Using file " + i + " of " + inputFilesCount + "...\n");
    		inputFileName = selectIni.get("Input" + i, "InputFile");
    		pdfReader = new PdfReader(inputPath + inputFileName + ".pdf");
    		
    		selectPages = selectIni.get("Input" + i, "SelectPages");
    		selectPagesArray = selectPages.split(",");
    		
    		for (String p : selectPagesArray) {
    			txtLog.appendText("Copying page " + p + "...\n");
    			copy.addPage(copy.getImportedPage(pdfReader, Integer.valueOf(p)));
    		}
    		
    		txtLog.appendText("\n");
    	}
    	
    	pdfOut.close();
    	
    	btnMerge.setDisable(false);
    	txtLog.appendText("Finished.");
    }
}
