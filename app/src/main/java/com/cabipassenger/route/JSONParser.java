package com.cabipassenger.route;

/**
 * @author zeeshan0026
 * parse the json that for route api
 */

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

public class JSONParser {
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {
	}

	public String getJSONFromUrl(String Url) {
		// Making HTTP request
		try {
			// defaultHttpClient

			java.net.URL url = new java.net.URL(Url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
		//	System.out.println("Response Code: " + conn.getResponseCode());
			is = new BufferedInputStream(conn.getInputStream());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			json = sb.toString();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}