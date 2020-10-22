package characterModel.utils.io;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import characterModel.bundler.Bundle;
import characterModel.bundler.Bundler;
import characterModel.features.Feature;
import characterModel.scripts.Script;
import characterModel.utils.Def;

public class BundleIO {

	private static final Logger LOG = LoggerFactory.getLogger(BundleIO.class);
	
	/**
	 * Loads a bundle from the path
	 * @param name - Name of the bundle in the bundle directory
	 * @return A Bundle object
	 */
	public static void loadBundles() {
		LOG.debug("Loading bundles...");
		
		File[] dirContents = new File(Def.bundleDirPath).listFiles();
		
		// Treat every directory as if it is a bundle
		for(File f : dirContents) {
			if(f.isDirectory()) {
				LOG.info("Loading bundle: " + f.getName());
				loadBundle(f);
			}
		}
		
		LOG.debug("Finished loading bundles");
	}
	
	/**
	 * Loads a bundle and registers it with the Bundler
	 * @param f
	 */
	private static void loadBundle(File f) {
		// Get paths
		Path bundleDir = Paths.get(f.getAbsolutePath());
		Path featsDir = Paths.get(f.getAbsolutePath(), Def.bundleFeatsDirName);
		Path scriptsDir = Paths.get(f.getAbsolutePath(), Def.bundleScriptsDirName);
		Path uiDir = Paths.get(f.getAbsolutePath(), Def.bundleHTMLDirName);
		
		// Load all bundle parts
		List<Feature> feats = loadFeatures(featsDir.toFile());
		List<Script> scripts = loadScripts(scriptsDir.toFile());
		List<String> pages = loadPages(uiDir.toFile());
		
		// Register bundle
		Bundler.getInstance().registerBundle(new Bundle(bundleDir.getFileName().toString(), feats, scripts, pages));
	}
	
	private static List<Feature> loadFeatures(File dir) {
		List<Feature> feats = new ArrayList<Feature>();
		
		File[] files = dir.listFiles();
		
		for(File f : files) {
			if(f.isDirectory())
				// Recursively load all sub-directories
				feats.addAll(loadFeatures(f));
			else if(f.isFile() && IO.getExtenstion(f.getAbsolutePath()).equals(Def.featFileExtension)) {
				
			}
		}
		
		return null;
	}
	
	private static List<Script> loadScripts(File dir) {
		return null;
	}
	
	private static List<String> loadPages(File dir) {
		return null;
	}

}
