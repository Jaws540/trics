package characterModel;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import characterModel.utils.Def;
import characterModel.utils.io.BundleIO;
import characterModel.utils.io.CharacterIO;

public class CharacterSystem {
	
	private static final Logger LOG = LoggerFactory.getLogger(CharacterSystem.class);
	
	public static void init() {
		LOG.debug("Initializing Character System...");
		
		// Character save directory initiliazation
		File characterSaveDir = new File(Def.characterDirPath);
		if(!characterSaveDir.exists()) {
			LOG.info("Creating character save directory");
			characterSaveDir.mkdirs();
			LOG.info("Character save directory: '" + characterSaveDir.getAbsolutePath() + "'");
		}
		CharacterIO.initGson();
		
		// Bundle directory initialization
		File bundleDir = new File(Def.bundleDirPath);
		if(!bundleDir.exists()) {
			LOG.info("Creating bundle directory");
			bundleDir.mkdirs();
			LOG.info("Bundle directory: '" + bundleDir.getAbsolutePath() + "'");
		}
		
    	BundleIO.loadBundles();
		
		LOG.debug("Finished initializing Character System");
	}

}
