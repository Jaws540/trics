package TIG.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TIG.utils.Def;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GUI extends Application {
	
	private static final Logger LOG = LoggerFactory.getLogger(GUI.class);
	
	private CharacterSheetPane csView;

	@Override
	public void start(Stage stage) throws Exception {
		LOG.debug("Building FX Window");
		
		// Create CharacterSheetPane
		csView = new CharacterSheetPane(stage);
		
		// Setup menu
		MenuItem refreshPage = new MenuItem("Hard Page Reload");
		refreshPage.setOnAction(e -> {
			csView.hardReload();
		});
		refreshPage.setMnemonicParsing(true);
		
		Menu debug = new Menu("Debug");
		debug.getItems().add(refreshPage);
		
		MenuBar menu = new MenuBar(debug);
		
		// Layout UI elements and display UI
		BorderPane root = new BorderPane();
		root.setTop(menu);
		root.setCenter(csView.getView());
		
		Scene scene = new Scene(root, 640, 480);
		stage.setTitle(Def.TITLE);
		stage.setScene(scene);
		stage.show();
		
		LOG.debug("FX Window shown");
	}
	
	public static void main(String[] args) {
		launch();
	}

}
