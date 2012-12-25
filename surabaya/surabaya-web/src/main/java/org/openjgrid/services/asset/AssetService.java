package org.openjgrid.services.asset;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import org.openjgrid.datatypes.AssetBase;
import org.openjgrid.datatypes.llsd.InventoryCollection;
import org.openjgrid.services.configuration.ConfigurationService;
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
			
		} catch (Exception ex) {
			log.error("Exception in AssetService", ex);
		}
		
		return (assetBase);
	}

}
