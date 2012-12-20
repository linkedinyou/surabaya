package org.openjgrid.services.inventory;

import java.util.ArrayList;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openjgrid.datatypes.llsd.InventoryCollection;
import org.openjgrid.datatypes.llsd.InventoryFolderBase;
import org.openjgrid.datatypes.llsd.InventoryItemBase;
import org.openjgrid.services.configuration.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session Bean implementation class InventoryService
 */
@Stateless
@LocalBean
public class InventoryService {
	private static final Logger log = LoggerFactory
			.getLogger(InventoryService.class);

	@EJB(mappedName = "java:module/ConfigurationService")
	private ConfigurationService configuration;

	public InventoryCollection getFolderContent(UUID userID, UUID folderID) {
		log.debug("getFolderContent(userID:{}, folderID:{} )", userID, folderID);
		InventoryCollection invCollection = new InventoryCollection();
		try {
			invCollection.folderList = new ArrayList<InventoryFolderBase>();
			invCollection.itemList = new ArrayList<InventoryItemBase>();
			invCollection.userId = userID;

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(configuration.getProperty("grid",
					"inventory_service") + "/xinventory");
			
			StringBuilder sb = new StringBuilder("PRINCIPAL=").append(userID.toString());
			sb.append("&FOLDER=").append(folderID.toString());
			sb.append("&METHOD=GETFOLDERCONTENT");
			log.debug("SynchronousRestForms "+ httppost.getMethod() +" "+ httppost.getURI() +" "+ sb.toString());
    		StringEntity stringEntity = new StringEntity(sb.toString());
    		stringEntity.setContentType("application/x-www-form-urlencoded");
    		httppost.setEntity(stringEntity);

    		HttpResponse httpResponse = httpclient.execute(httppost);
        	HttpEntity entity = httpResponse.getEntity();

        	long contentLength = entity.getContentLength();
        	Header contentType = entity.getContentType();
        	String content = IOUtils.toString(entity.getContent(), "UTF-8");
        	log.debug("ContentLength: {}", contentLength);
        	log.debug("ContentType: {}", contentType);
        	log.debug("respstring: {}", content);
    		
		} catch (Exception ex) {
			log.debug("Exception occurred in getFolderContent()", ex);
		}
		return (invCollection);

	}

}
