package TIG.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	public enum Level {
		DEBUG(0),
		INFO(1),
		WARN(2),
		ERROR(3),
		FATAL(4);
		
		private int level;
		
		private Level(int lvl) {
			this.level = lvl;
		}
		
		public int compare(Level l) {
			if(this.level > l.level)
				return 1;
			else if(this.level == l.level)
				return 0;
			else
				return -1;
		}
	}
	
	private static final String LOG_PATH = "logs\\";
	private static final String LOG_EXT = ".log";
	private static File LOG_FILE = null;
	
	private static Level currentLevel = Level.DEBUG;
	
	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy | HH:mm:ss");
	
	// Create the log file based on the current day
	// Log files are separated by the day the program was run on.  So all logs from
	// one day are in the same file.  Additionally, if the program is run and is left
	// running overnight, the logs will be logged to the file of the day the program
	// was started.
	//
	// It might be good to also limit the log file sizes...
	static {
		DateFormat logFileDateFormat = new SimpleDateFormat("MMM-dd-yyyy");
		String today = logFileDateFormat.format(new Date());
		
		File logFile = null;
		try {
			File logDir = new File(LOG_PATH);
			if(!logDir.exists())
				logDir.mkdirs();
			
			// Find the latest log file
			for(File f : logDir.listFiles()) {
				if(f.getName().startsWith(today)) {
					logFile = f;
					break;
				}
			}
			
			// If we found a log file from today
			if(logFile != null) {
				// Set the log file to this file
				Log.LOG_FILE = logFile;
			}else {
				// If not, create a new log file
				File file = new File(Log.LOG_PATH + today + LOG_EXT);
				file.createNewFile();
				PrintWriter pw = new PrintWriter(file);
				pw.println("Logging: " + today);
				pw.flush();
				pw.close();
				Log.LOG_FILE = file;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setLogLevel(Level level) {
		Log.currentLevel = level;
	}
	
	public static Level currentLogLevel() {
		return Log.currentLevel;
	}
	
	private static boolean shouldLog(Level lvl) {
		if(lvl.compare(Log.currentLevel) >= 0) {
			return true;
		}
		
		return false;
	}
	
	private static void write(Level level, String msg) {
		try {
			// Write to the log
			PrintWriter pw = new PrintWriter(new FileWriter(Log.LOG_FILE, true), true);
			
			pw.print(dateFormat.format(new Date()));
			pw.print(" [");
			pw.print(level);
			pw.print("]: ");
			pw.println(msg);
			
			pw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void log(Level level, String message) {
		if(shouldLog(level))
			write(level, message);
	}
	
	public static void debug(String message) {
		if(shouldLog(Level.DEBUG))
			write(Level.DEBUG, message);
	}
	
	public static void info(String message) {
		if(shouldLog(Level.INFO))
			write(Level.INFO, message);
	}
	
	public static void warn(String message) {
		if(shouldLog(Level.WARN))
			write(Level.WARN, message);
	}
	
	public static void error(String message) {
		if(shouldLog(Level.ERROR))
			write(Level.ERROR, message);
	}
	
	public static void fatal(String message) {
		if(shouldLog(Level.FATAL))
			write(Level.FATAL, message);
		System.exit(1);
	}
	
}
