/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package TIG;

import TIG.utils.TestData;
import TIG.utils.Utils;

public class App {

    public static void main(String[] args) {
    	// Save Path
    	String path = "D:\\Users\\Jacob\\Coding\\Java\\RPGIS\\RPG-Integrated-System\\res\\";
    	
    	// Custom JSON Serialization
    	System.out.println(Utils.saveJSON(TestData.testCharacter, path + "testSave1.json"));
    }
}
