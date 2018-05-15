package ru.school38dz52.board;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class JsonObjectStore<T> {
	
	private File file;
	private Gson gson = new GsonBuilder().registerTypeAdapter(JsonElement.class, new JsonElementJsonDeserializer()).create();
	private JsonParser parser = new JsonParser();
	private Map<Integer, T> store;
	
	public JsonObjectStore() {
		file = new File("temp_" + System.currentTimeMillis() + ".txt");
		store = new ConcurrentHashMap<Integer, T>();
	}
	
	public JsonObjectStore(File file) throws Exception {
		this();
		this.file = file;
		if (file.exists()) {
			String json = readFile();
			readJson(json);
		}
	}
	
	public T get(int id) {
		return store.get(id);
	}
	
	public int size() {
		return store.size();
	}
	
	public Collection<T> getStore() {
		return store.values();
	}
	
	public boolean contains(int id) {
		return store.containsKey(id);
	}
	
	public boolean contains(T object) {
		return store.containsValue(object);
	}
	
	public void set(int id, T object) {
		store.put(id, object);
	}
	
	private String readFile() throws Exception {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
	        StringBuilder sb = new StringBuilder();
	        String buf;
	        while ((buf = in.readLine()) != null) {
	            sb.append(buf);
	        }
	        return sb.toString();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readJson(String source) {
		JsonElement element = gson.fromJson(source, JsonElement.class);
		JsonArray array = element.getAsJsonArray();
		for (int i = 0; i < array.size(); i++) {
			JsonObject obj = array.get(i).getAsJsonObject();
			String type = obj.get("type").getAsString();
			try {
				Object object = gson.fromJson(gson.toJson(obj.get("object")), Class.forName(type));
				set(obj.get("id").getAsInt(), (T)object);
			} catch (Exception e) {
				Server.log.severe("Can't cast object to class " + type);
			}
		}
	}
	
	public void saveFile() {
		try {
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			JsonArray array = new JsonArray();
			for (Entry<Integer, T> entry : store.entrySet()) {
				JsonObject object = new JsonObject();
				object.addProperty("id", ((Number) entry.getKey()));
				object.addProperty("type", entry.getValue().getClass().getCanonicalName());
				object.add("object", parser.parse(gson.toJson(entry.getValue())));
				array.add(object);
			}
			gson.toJson(array, writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class JsonElementJsonDeserializer implements JsonDeserializer<JsonElement> {
		@Override
		public JsonElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			return json;
		}
	}

}
