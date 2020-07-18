package org.mastersimplified.sb.rest;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MasterSimplifiedRest {

	@Scheduled(fixedDelay = 15000)
	public void genewsFromTimesOfInd() {
		try {
			final String uri = "https://timesofindia.indiatimes.com/rssfeedstopstories.cms";

			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.getForObject(uri, String.class);
			JSONObject lOutputJSON = parseXmlToJson(result);
			JSONArray itemsObj = lOutputJSON.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");
			HashMap<String, JSONObject> outMap = buildApi(itemsObj);
			for (Map.Entry<String, JSONObject> entry : outMap.entrySet()) {
				System.out.println("Title: " + entry.getValue().getString("title") + "\n");
				System.out.println("Published Date: " + entry.getValue().getString("pubDate") + "\n");
				System.out.println("Description: " + entry.getValue().getString("description") + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static JSONObject parseXmlToJson(String output) {
		return XML.toJSONObject(output);
	}

	private HashMap<String, JSONObject> buildApi(JSONArray items) {
		JSONArray outputArray = items;
		int length = outputArray.length();
		System.out.println("News Lenght ::" + length);
		HashMap<String, JSONObject> hm = new HashMap<>();
		for (int i = 0; i < length; i++) {
			JSONObject insideArr = (JSONObject) outputArray.get(i);
			hm.put(insideArr.getString("guid"), insideArr);
		}
		return hm;
	}

}
