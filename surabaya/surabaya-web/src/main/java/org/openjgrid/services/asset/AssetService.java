package org.openjgrid.services.asset;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openjgrid.datatypes.asset.AssetBase;
import org.openjgrid.services.infrastructure.ConfigurationService;
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

	@Resource(lookup = "java:jboss/surabaya_cache")
	private org.infinispan.manager.CacheContainer container;
	private org.infinispan.Cache<String, String> cache;

	@EJB(mappedName = "java:module/ConfigurationService")
	private ConfigurationService configuration;

	private HttpClient httpclient = null;

	@PostConstruct
	public void init() {
		this.cache = this.container.getCache();
		httpclient = new DefaultHttpClient();
	}

	public AssetBase getAsset(String assetID) {
		log.debug("getAsset(assetID:{})", assetID);
		String content = null;
		AssetBase assetBase = new AssetBase();
		try {
			if (cache.containsKey(assetID)) {
				log.debug("Cache Hit: {}", assetID);
			    content = cache.get(assetID);
			} else {
				log.debug("Cache Miss: {}", assetID);

				HttpGet httpget = new HttpGet(configuration.getProperty("Grid", "asset_service") + "/assets/" + assetID);
				HttpResponse httpResponse = httpclient.execute(httpget);
				HttpEntity entity = httpResponse.getEntity();

				long contentLength = entity.getContentLength();
				Header contentType = entity.getContentType();
				content = IOUtils.toString(entity.getContent());
				log.debug("ContentLength: {}", contentLength);
				log.debug("ContentType: {}", contentType);
				if(!Util.isNullOrEmpty(content)) {
					cache.put(assetID, content);					
					log.debug("80 bytes respstring: " + content.substring(0,80));
				}
			}
			
			if (Util.isNullOrEmpty(content)) {
				return (null);
			}
			assetBase.fromXml(content);

			return (assetBase);

		} catch (Exception ex) {
			log.error("Exception in AssetService", ex);
		}

		return (assetBase);
	}

}
