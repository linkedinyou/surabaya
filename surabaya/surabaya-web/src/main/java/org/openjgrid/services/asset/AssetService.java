package org.openjgrid.services.asset;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.openjgrid.datatypes.AssetBase;
import org.openjgrid.datatypes.llsd.InventoryCollection;
import org.openjgrid.services.infrastructure.ConfigurationService;
import org.openjgrid.util.HexDump;
import org.openjgrid.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session Bean implementation class AssetService
 */
@Stateless(mappedName = "assetservice")
@LocalBean
public class AssetService {
	private static final Logger log = LoggerFactory.getLogger(AssetService.class);

	@EJB(mappedName = "java:module/ConfigurationService")
	private ConfigurationService configuration;
	
	private HttpClient httpclient = null;

	@PostConstruct
	public void init() {
		httpclient = new DefaultHttpClient();
	}

	public AssetBase getAsset(String assetID) {
		log.debug("getAsset(assetID:{})", assetID);
		AssetBase assetBase = new AssetBase();
		try {
			HttpGet httpget = new HttpGet(configuration.getProperty("grid",
					"asset_service") + "/assets/" + assetID);
    		HttpResponse httpResponse = httpclient.execute(httpget);
        	HttpEntity entity = httpResponse.getEntity();

        	long contentLength = entity.getContentLength();
        	Header contentType = entity.getContentType();
        	String content = IOUtils.toString(entity.getContent());
        	log.debug("ContentLength: {}", contentLength);
        	log.debug("ContentType: {}", contentType);
//        	log.debug("respstring: " + content);
        	
        	if(Util.isNullOrEmpty(content)) {
        		return (null);
        	}
        	assetBase.fromXml(content);
        	
        	return(assetBase);
			
		} catch (Exception ex) {
			log.error("Exception in AssetService", ex);
		}
		
		return (assetBase);
	}

}
