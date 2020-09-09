package TIG.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CharacterSheetPane extends Application {
	
	private static final Logger logger = LoggerFactory.getLogger(CharacterSheetPane.class);

	@Override
	public void start(Stage stage) throws Exception {
		String javaVersion = System.getProperty("java.version");
		String javafxVersion = System.getProperty("javafx.version");
		Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion);
    	logger.debug("This is a test message!");
		Scene scene = new Scene(new StackPane(l), 640, 480);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String args[]) {
		launch();
	}

}
