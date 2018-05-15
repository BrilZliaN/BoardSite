package ru.school38dz52.board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	
	public static String buildSelect(String... array) {
		StringBuilder sb = new StringBuilder(256);
		for (int i = 0; i < array.length - (array.length % 2); i++) {
			if (i == 0) {
				sb.append(array[i]);
			} else if ((i % 2) == 1) {
				sb.append(" = ");
				sb.append(array[i]);
			} else {
				sb.append(" AND ");
				sb.append(array[i]);
			}
		}
		return sb.toString();
	}
	
	public static String arrayToString(String delimeter, String... array) {
		StringBuilder sb = new StringBuilder(256);
		
		for (String string : array) {
			sb.append(string);
			sb.append(delimeter);
		}
		sb.delete(sb.length() - delimeter.length(), sb.length());
		return sb.toString();
	}
	
	public static String arrayToString(String delimeter, Integer[] array) {
		StringBuilder sb = new StringBuilder(256);
		
		for (Integer integer : array) {
			sb.append(integer);
			sb.append(delimeter);
		}
		sb.delete(sb.length() - delimeter.length(), sb.length());
		return sb.toString();
	}
	
	public static String getDate(long ms, String format) {
		Date date = new Date(ms + 3600000);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
	public static String httpGet(String url) throws IOException {
        URL oauth = new URL(url);
        URLConnection yc = oauth.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            sb.append(new String(inputLine.getBytes("utf-8")));
        in.close();
        
        return sb.toString();
	}

}
