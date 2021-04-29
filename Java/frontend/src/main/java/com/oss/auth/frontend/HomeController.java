package com.oss.auth.frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@RestController
public class HomeController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getMessage() throws IOException {
		
		String identityEndpoint = System.getenv("IDENTITY_ENDPOINT");
		String identityHeader = System.getenv("IDENTITY_HEADER");
		String resource =  System.getenv("RESOURCE"); 
		String backend_url =  System.getenv("BACKEND_URL"); 
		String api_version = "2019-08-01";
		String endpoint = identityEndpoint + "?resource=" + resource + "&api-version=" + api_version; 
		
		URL url;
		URL url2;
		JsonNode jsonResponse;
		JsonNode jsonResponse2;
		String access_token = null;
		String message = null;
		List<String> values = null;
		
		try {
			url = new URL(endpoint);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("X-IDENTITY-HEADER", identityHeader);
			int responseCode = connection.getResponseCode();
			
			if (responseCode == 200) {
                
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                
                while ((inputLine = in.readLine()) != null) {
                	response.append(inputLine);
                }
                
                in.close();
             
                ObjectMapper mapper = new ObjectMapper();
                jsonResponse = mapper.readTree(response.toString()); 
                access_token = jsonResponse.get("access_token").textValue();
                
                //Once we have a token, do the next call to api.
                
                url2 = new URL(backend_url);
    			HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
    			connection2.setRequestMethod("GET");
    			connection2.setRequestProperty("Authorization","Bearer "+ access_token);
    			connection2.setRequestProperty("Content-Type","application/json");
    			int responseCode2 = connection2.getResponseCode();
    			
    			if (responseCode2 == 200) {
                    
                    BufferedReader in2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
                    String inputLine2;
                    StringBuffer response2 = new StringBuffer();
                    
                    while ((inputLine2 = in2.readLine()) != null) {
                    	response2.append(inputLine2);
                    }
                    
                    in2.close();
                    
                    ObjectMapper mapper2 = new ObjectMapper();
                    jsonResponse2 = mapper2.readTree(response2.toString()); 
                    message = jsonResponse2.get("message").textValue();
                }	
            }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return message;
	}
}