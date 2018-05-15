package ru.school38dz52.board;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonKVStore {
	
	private Map<String, String> kv;
	
	public JsonKVStore() {
		kv = new HashMap<String, String>();
	}
	
	public JsonKVStore(File file) throws Exception {
		this();
		if (file.exists()) {
			String json = readFile(file);
			readJson(json);
		}
	}
	
	public String get(String key) {
		return kv.get("config." + key);
	}
	
	public boolean contains(String key) {
		return kv.containsKey("config." + key);
	}
	
	public void set(String key, String value) {
		kv.put("config." + key, value);
	}
	
	private String readFile(File file) throws Exception {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
	        StringBuilder sb = new StringBuilder();
	        String buf;
	        while ((buf = in.readLine()) != null) {
	            sb.append(buf);
	        }
	        return sb.toString();
		}
	}
	
	private void readJson(String source) {
		JsonParser parser = new JsonParser();
		readJsonObjectRecursively("config", parser.parse(source).getAsJsonObject());
	}
	
	private void readJsonObjectRecursively(String str, JsonObject object) {
		for (Entry<String, JsonElement> e : object.entrySet()) {
			JsonElement element = e.getValue();
			String key = str + "." + e.getKey();
			if (element.isJsonObject()) {
				readJsonObjectRecursively(key, element.getAsJsonObject());
			} else {
				kv.put(key, element.getAsString());
			}
		}
	}

}
