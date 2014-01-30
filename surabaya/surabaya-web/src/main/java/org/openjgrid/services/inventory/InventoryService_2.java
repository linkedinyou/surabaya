package org.openjgrid.services.inventory;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openjgrid.datatypes.inventory.InventoryCollection;
import org.openjgrid.datatypes.inventory.InventoryException;
import org.openjgrid.datatypes.inventory.InventoryFolderBase;
import org.openjgrid.datatypes.inventory.InventoryItemBase;
import org.openjgrid.services.infrastructure.ConfigurationService;
import org.openjgrid.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session Bean implementation class InventoryService
 */
@Stateless
@LocalBean
public class InventoryService_2 {
	private static final Logger log = LoggerFactory
			.getLogger(InventoryService_2.class);

	@EJB(mappedName = "java:module/ConfigurationService")
	private ConfigurationService configuration;
	
	private HttpClient httpclient = null;

	@PostConstruct
	public void init() {
		httpclient = new DefaultHttpClient();
	}
	public InventoryCollection getFolderContent(UUID userID, UUID folderID, String inventoryServerURL) {
		log.debug("getFolderContent(userID:{}, folderID:{} )", userID, folderID);
		InventoryCollection invCollection = new InventoryCollection();
		try {
			invCollection.userId = userID;
			HttpPost httppost = new HttpPost( inventoryServerURL + "/xinventory");
			
			StringBuilder sb = new StringBuilder("PRINCIPAL=").append(userID.toString());
			sb.append("&FOLDER=").append(folderID.toString());
			sb.append("&METHOD=GETFOLDERCONTENT");
			log.debug("SynchronousRestForms "+ httppost.getMethod() +" "+ httppost.getURI() +" "+ sb.toString());
    		StringEntity stringEntity = new StringEntity(sb.toString());
    		stringEntity.setContentType("application/x-www-form-urlencoded");
    		httppost.setEntity(stringEntity);

    		long startTime = System.currentTimeMillis();
    		HttpResponse httpResponse = httpclient.execute(httppost);
    		long endTime = System.currentTimeMillis();
    		log.info("Call to {} took {} ms", inventoryServerURL, endTime - startTime);

    		int statuscode = httpResponse.getStatusLine().getStatusCode();
    		if (statuscode != HttpStatus.SC_OK) {
    			log.warn("getFolderContent(userID: "+userID+", folderID: "+folderID+") http Status: "+statuscode);
    		}
        	HttpEntity entity = httpResponse.getEntity();

        	long contentLength = entity.getContentLength();
        	Header contentType = entity.getContentType();
        	String content = IOUtils.toString(entity.getContent(), "UTF-8");
        	log.debug("ContentLength: {}", contentLength);
        	log.debug("ContentType: {}", contentType);
//        	log.debug("respstring: {}", content);
    		
        	if(Util.isNullOrEmpty(content)) {
        		return(null);
        	}
        	invCollection.fromXml(content);
        	
    		return (invCollection);
        	
        	
		} catch (Exception ex) {
			log.debug("Exception occurred in getFolderContent()", ex);
			return (null);
		}

	}

	/**
	 * @param containingFolder
	 * @return
	 */
	public InventoryFolderBase getFolder(InventoryFolderBase containingFolder, String inventoryServerURL) {
		log.debug("getFolder()");
		try {
			HttpPost httppost = new HttpPost(inventoryServerURL + "/xinventory");

			StringBuilder sb = new StringBuilder();
			sb.append("&ID=").append(containingFolder.getId().toString());
			sb.append("&METHOD=GETFOLDER");
			log.debug("SynchronousRestForms "+ httppost.getMethod() +" "+ httppost.getURI() +" "+ sb.toString());
    		StringEntity stringEntity = new StringEntity(sb.toString());
    		stringEntity.setContentType("application/x-www-form-urlencoded");
    		httppost.setEntity(stringEntity);

    		long startTime = System.currentTimeMillis();
    		HttpResponse httpResponse = httpclient.execute(httppost);
    		long endTime = System.currentTimeMillis();
    		log.info("Call to {} took {} ms", inventoryServerURL, endTime - startTime);
    		
    		int statuscode = httpResponse.getStatusLine().getStatusCode();
    		if (statuscode != HttpStatus.SC_OK) {
    			log.warn("getFolder("+containingFolder.getName()+") http Status: "+statuscode);
    		}
        	HttpEntity entity = httpResponse.getEntity();

        	long contentLength = entity.getContentLength();
        	Header contentType = entity.getContentType();
        	String content = IOUtils.toString(entity.getContent(), "UTF-8");
        	log.debug("ContentLength: {}", contentLength);
        	log.debug("ContentType: {}", contentType);
//        	log.debug("respstring: {}", content);
			
        	containingFolder.fromXml(content);
        	
		} catch (Exception ex) {
			log.debug("Exception occurred in getFolderContent()", ex);
			return (null);			
		}
		
		return(containingFolder);
	}
	/**
	 * @param inventoryItemBase
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws XMLStreamException 
	 * @throws InventoryException 
	 */
	public InventoryItemBase getItem(InventoryItemBase inventoryItemBase, String inventoryServerURL ) 
			throws ClientProtocolException, IOException, XMLStreamException, InventoryException {
		
		log.debug("getItem({}) inventoryServerURL: {}", inventoryItemBase.getId().toString(), inventoryServerURL);
		HttpPost httppost = new HttpPost(inventoryServerURL + "/xinventory");

		StringBuilder sb = new StringBuilder();
		sb.append("&ID=").append(inventoryItemBase.getId().toString());
		sb.append("&METHOD=GETITEM");
		log.debug("SynchronousRestForms "+ httppost.getMethod() +" "+ httppost.getURI() +" "+ sb.toString());
		StringEntity stringEntity = new StringEntity(sb.toString());
		stringEntity.setContentType("application/x-www-form-urlencoded");
		httppost.setEntity(stringEntity);

		long startTime = System.currentTimeMillis();
		HttpResponse httpResponse = httpclient.execute(httppost);
		long endTime = System.currentTimeMillis();
		log.info("Call to {} took {} ms", inventoryServerURL, endTime - startTime);
		
		int statuscode = httpResponse.getStatusLine().getStatusCode();
		if (statuscode != HttpStatus.SC_OK) {
			log.warn("getItem("+inventoryItemBase.getName()+") http Status: "+statuscode);
		}
    	HttpEntity entity = httpResponse.getEntity();

    	long contentLength = entity.getContentLength();
    	Header contentType = entity.getContentType();
    	String content = IOUtils.toString(entity.getContent(), "UTF-8");
    	log.debug("ContentLength: {}", contentLength);
    	log.debug("ContentType: {}", contentType);
//    	log.debug("respstring: {}", content);

    	inventoryItemBase.fromXml(content);
    	
    	return (inventoryItemBase);
	}

}
