package utils;

import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public abstract class TestUtils {

	public static JSONArray loadJsonFile(String fileName) {
		JSONParser parser = new JSONParser();
		try {
			Path resourcePath = Paths.get("src", "test", "resources", fileName);
			String absolutePath = resourcePath.toFile().getAbsolutePath();
			return  (JSONArray) parser.parse(new FileReader(absolutePath));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
