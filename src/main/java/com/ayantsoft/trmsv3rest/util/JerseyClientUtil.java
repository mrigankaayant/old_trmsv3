package com.ayantsoft.trmsv3rest.util;

import java.io.Serializable;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class JerseyClientUtil implements Serializable {


	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8332748755477680368L;


	public static Client getClientConfig(){
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		return client;
	}
	
	
	public static WebResource.Builder getWebResourceBuilder(Client client, String uri){
		return client.resource(uri).accept(MediaType.APPLICATION_JSON);
	}

}
