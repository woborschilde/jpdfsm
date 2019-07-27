package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
    private String configFile = "";

    public void setup(Stage stage) {
        this.stage = stage;
        this.scene = stage.getScene();
        loadConfig();
    }

    private void loadConfig() {
    	Wini config;
		try {
						 // JAR directory
			configFile = new File(Controller.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath() + "/config.ini";
			txtLog.appendText("Using " + configFile);
			config = new Wini(new File(configFile));
			
			selectFile = config.get("General", "SelectFile");
	    	inputPath = config.get("General", "InputPath");
	    	saveFile = config.get("General", "SaveFile");
	    	
	    	if (selectFile == null) { selectFile = ""; }
	    	if (inputPath == null) { inputPath = ""; }
	    	if (saveFile == null) { saveFile = ""; }
	    	
	    	txtSelectFile.setText(selectFile);
	    	txtInputPath.setText(inputPath);
	    	txtOutputFile.setText(saveFile);
		} catch (URISyntaxException e) {
			showError(e);
		} catch (NullPointerException e) {
			// Nothing here, as this is okay (missing entries).
		} catch (InvalidFileFormatException e) {
			showError(e);
		} catch (FileNotFoundException e) {
			txtLog.appendText(" (not existing yet)");
		} catch (IOException e) {
			showError(e);
		}
    	
		txtLog.appendText("\n\n");
    	mergeButtonAvailable();
    }
    
    public void browseSelectorFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        selectFile = file != null ? file.getAbsoluteFile().toString() : selectFile;
        txtSelectFile.setText(selectFile);
        
        write("General", "SelectFile", selectFile);
        mergeButtonAvailable();
    }

    public void browseInputPath() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File dir = dirChooser.showDialog(stage);
        inputPath = dir != null ? dir.getAbsolutePath() + "/" : inputPath;
        txtInputPath.setText(inputPath);
        
        write("General", "InputPath", inputPath);        
        mergeButtonAvailable();
    }

    public void browseOutputFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(stage);
        saveFile = file != null ? file.getAbsoluteFile().toString() : saveFile;
        txtOutputFile.setText(saveFile);
        
        write("General", "SaveFile", saveFile);
        mergeButtonAvailable();
    }
    
    private void write(String section, String key, String value) {
    	txtLog.clear();
    	File c = new File(configFile);
        try {
			if (!c.exists()) { c.createNewFile(); }
			Wini config = new Wini(c);
			config.put(section, key, value);
			config.store();
			txtLog.appendText(key + " written to " + configFile);
		} catch (InvalidFileFormatException e) {
			showError(e);
		} catch (IOException e) {
			txtLog.appendText("Tip: If you move this program to a writable directory, it could remember your configured paths. ;)\n\n");
		}
    }
    
    private void mergeButtonAvailable() {
    	if (!txtSelectFile.getText().equals("") && !txtInputPath.getText().equals("") && !txtOutputFile.getText().equals("")) {
    		btnMerge.setDisable(false);
    	} else {
    		btnMerge.setDisable(true);
    	}
    }
    
    public void merge() {
    	btnMerge.setDisable(true); txtLog.clear();
    	PdfReader pdfReader = null;
    	
    	try {
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
		} catch (Exception e) {
			showError(e);
		}
    	
    	btnMerge.setDisable(false);
    	txtLog.appendText("Finished.");
    }
    
    // Source: https://o7planning.org/en/11529/javafx-alert-dialogs-tutorial
    private void showError(Exception e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error alert");
        alert.setHeaderText(e.getMessage());
 
        VBox dialogPaneContent = new VBox();
 
        Label label = new Label("Stack Trace:");
 
        String stackTrace = this.getStackTrace(e);
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);
 
        dialogPaneContent.getChildren().addAll(label, textArea);
 
        // Set content for Dialog Pane
        alert.getDialogPane().setContent(dialogPaneContent);
 
        alert.showAndWait();
    }
    
    // Source: https://o7planning.org/en/11529/javafx-alert-dialogs-tutorial
    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String s = sw.toString();
        return s;
    }
}
