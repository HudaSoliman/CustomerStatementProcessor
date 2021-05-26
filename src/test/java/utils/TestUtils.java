package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import lombok.Cleanup;
import lombok.var;

public class TestUtils {

	public JSONArray loadJsonFile(String fileName) {
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

	public String getResourcePath(String fileName) {
		Path resourcePath = Paths.get("src", "main", "test", "resources", fileName);
		return resourcePath.toFile().getAbsolutePath();
	}

	public String getData(String absolutOutputePath) throws IOException {
		var stringBuilder = new StringBuilder();
		var is = new FileInputStream(absolutOutputePath);
		var isr = new InputStreamReader(is, StandardCharsets.UTF_8);
		@Cleanup
		var buffReader = new BufferedReader(isr);
		while (buffReader.ready()) {
			stringBuilder.append(buffReader.readLine() + "\n");
		}

		return stringBuilder.toString();
	}
}
