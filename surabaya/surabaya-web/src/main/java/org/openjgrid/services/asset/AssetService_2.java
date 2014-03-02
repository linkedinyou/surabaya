package org.openjgrid.services.asset;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.openjgrid.datatypes.asset.AssetBase;
import org.openjgrid.services.infrastructure.ConfigurationService;
import org.openjgrid.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session Bean implementation class AssetService
 */
@Stateless
@LocalBean
public class AssetService_2 {
	private static final Logger log = LoggerFactory.getLogger(AssetService_2.class);

	@Resource(lookup = "java:jboss/surabaya_cache")
	private org.infinispan.manager.CacheContainer container;
	private org.infinispan.Cache<String, String> cache;

	@EJB(mappedName = "java:module/ConfigurationService")
	private ConfigurationService configuration;

	Client client = null;
	
	@PostConstruct
	public void init() {
		log.info("init()");
		this.cache = this.container.getCache();
        client = ClientBuilder.newClient();		
	}
	
	public AssetBase getAsset(String assetID) {
		log.info("getAsset(assetID:{})", assetID);
		String content = null;
		AssetBase assetBase = new AssetBase();
		Response response = null;
		
		try {
			if (cache.containsKey(assetID)) {
				log.debug("Cache Hit: {}", assetID);
				content = cache.get(assetID);
				log.debug("80 bytes content from cache: " + content.substring(0, 80));
			} else {
				log.debug("Cache Miss: {}", assetID);

				WebTarget webTarget = client.target(configuration.getProperty("Grid", "asset_service") + "/assets" + assetID);
				Builder builder = webTarget.request();
				
				long startTime = System.currentTimeMillis();
				response = builder.get();
				long endTime = System.currentTimeMillis();
				log.info("Call to Grid Asset Server took {} ms", endTime - startTime);

	    		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
	    			log.warn("getAsset(AssetID: "+assetID+") http Status: "+response.getStatus());
	    		}
				
	        	content = response.readEntity(String.class);

				log.debug("Content: {}", content);
				if (!Util.isNullOrEmpty(content)) {
					cache.put(assetID, content);
				} else {
					log.error("Asset with ID: {} requestet, rsult was null", assetID);
				}
			}

			if (Util.isNullOrEmpty(content)) {
				return (null);
			}
			assetBase.fromXml(content);

			return (assetBase);

		} catch (Exception ex) {
			log.error("Exception in AssetService", ex);
		} finally {
		    if( response != null ) {
		        response.close();
		    }
		}

		return (assetBase);
	}

	public void cacheAsset(String assetID, String serializedAsset) {
		log.debug("cacheAsset(assetID: {}, String serializedAsset)", assetID);
		try {
			long startTime = System.currentTimeMillis();
			cache.put(assetID, serializedAsset);
			long endTime = System.currentTimeMillis();
			log.info("Caching Asset {} took {} ms", assetID, endTime - startTime);
			
		} catch (Exception ex) {
			log.error("Exception while caching AssetID {}: {}", assetID, ex.getMessage());
		}
	}

	public void cacheAsset(String assetID, String serializedAsset, long lifespan, TimeUnit timeunit) {
		log.debug("cacheAsset(assetID: {}, String serializedAsset, long , timeunit)", assetID);
		try {
			long startTime = System.currentTimeMillis();
			cache.put(assetID, serializedAsset, lifespan, timeunit);
			long endTime = System.currentTimeMillis();
			log.info("Caching Temporary Asset {} took {} ms", assetID, endTime - startTime);
			
		} catch (Exception ex) {
			log.error("Exception while caching AssetID {}: {}", assetID, ex.getMessage());
		}
	}
}
