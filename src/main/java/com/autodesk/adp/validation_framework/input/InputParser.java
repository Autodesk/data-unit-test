package com.autodesk.adp.validation_framework.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class InputParser {
	private static final Logger LOG = LoggerFactory.getLogger(InputParser.class);
	
	public void readInputYAML(String yamlLocation) throws FileNotFoundException, IOException {
		try (FileInputStream fin = new FileInputStream(new File(yamlLocation));) {
			Yaml yaml = new Yaml();
			List<Map<String, String>> ymlData = (List<Map<String, String>>) yaml.load(fin);
			for(Map<String, String> data : ymlData)
				System.out.println(data);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		InputParser parser = new InputParser();
		parser.readInputYAML("src/main/resources/tests/tests.yml");
	}

}
