package TIG.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TIG.utils.IO;
import javafx.concurrent.Worker.State;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class CharacterSheetPane {
	
	private static final Logger LOG = LoggerFactory.getLogger(CharacterSheetPane.class);
	
	private WebView view;
	private WebEngine engine;
	
	private Page currentPage = null;
	
	private CharacterSheetController sheetController;
	
	public CharacterSheetPane(Stage mainStage) {
		LOG.debug("Building character sheet display");
		
		view = new WebView();
		
		engine = view.getEngine();
		view.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.F5) {
				reload();
			}
		});
		
		engine.setOnAlert(e -> {
			System.out.println("ALERT: " + e.getData());
		});
		
		// Setup javascript callbacks when finished loading a page
		sheetController = new CharacterSheetController(mainStage, this);
		engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if(newState == State.SUCCEEDED) {
				LOG.debug("Finished loading page");
				JSObject window = (JSObject) engine.executeScript("window");
				window.setMember("rpgsys", sheetController);
			}
		});
		
		// TEMP: Load a character sheet
		sheetController.setCharacterSheet(IO.loadCharacter("characters\\Illra.rpgc"));
		
		// Load page
		currentPage = new Page(getClass().getResource("/ui/html/index.html"));
		loadPage(currentPage);
	}
	
	public WebView getView() {
		return view;
	}
	
	private void loadPage(Page page) {
		LOG.debug("Loading page: " + page.getUrl());
		
		load();
	}
	
	public void next(Page page) {
		LOG.debug("Loading next page: " + page.getUrl());
		
		currentPage = currentPage.forward(page);
		
		load();
	}
	
	public void back() {
		LOG.debug("Loading previous page");
		
		currentPage = currentPage.back();
		
		load();
	}
	
	private void load() {
		engine.load(currentPage.getUrl());
		reload();
	}
	
	public void reload() {
		LOG.debug("Reloading page...");
		
		// WebEngine reloads content
		// Hopefully causes a refresh of the stylesheets
		engine.reload();
	}
	
	public void hardReload() {
		LOG.debug("Hard reloading page...");
		
		loadPage(currentPage);
	}

}
