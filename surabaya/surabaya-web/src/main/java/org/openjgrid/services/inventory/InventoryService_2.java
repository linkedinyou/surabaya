package org.openjgrid.services.inventory;

import java.io.IOException;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.stream.XMLStreamException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	
	public InventoryCollection getFolderContent(UUID userID, UUID folderID, String inventoryServerURL) {
		log.debug("getFolderContent(userID:{}, folderID:{} )", userID, folderID);
		InventoryCollection invCollection = new InventoryCollection();
		try {
			invCollection.userId = userID;
			
			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client.target(inventoryServerURL + "/xinventory");
			Builder builder = webTarget.request();
			
			StringBuilder sb = new StringBuilder("PRINCIPAL=").append(userID.toString());
			sb.append("&FOLDER=").append(folderID.toString());
			sb.append("&METHOD=GETFOLDERCONTENT");
    		long startTime = System.currentTimeMillis();
			Response response = builder.post(Entity.entity(sb.toString(),MediaType.APPLICATION_FORM_URLENCODED));
    		long endTime = System.currentTimeMillis();
    		log.info("Call to {} took {} ms", inventoryServerURL, endTime - startTime);

    		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
    			log.warn("getFolderContent(userID: "+userID+", folderID: "+folderID+") http Status: "+response.getStatus());
    		}
	
        	String content = response.readEntity(String.class);
        	
        	log.debug("respstring: {}", content);
    		
        	response.close();
        	client.close();
        	
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

			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client.target(inventoryServerURL + "/xinventory");
			Builder builder = webTarget.request();
			
			StringBuilder sb = new StringBuilder();
			sb.append("&ID=").append(containingFolder.getId().toString());
			sb.append("&METHOD=GETFOLDER");

    		long startTime = System.currentTimeMillis();
			Response response = builder.post(Entity.entity(sb.toString(),MediaType.APPLICATION_FORM_URLENCODED));
    		long endTime = System.currentTimeMillis();
    		log.info("Call to {} took {} ms", inventoryServerURL, endTime - startTime);
    
    		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
    			log.warn("getFolder(http Status: "+response.getStatus());
    		}
	
        	String content = response.readEntity(String.class);
        	
        	log.debug("respstring: {}", content);
    		
        	response.close();
        	client.close();
			
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
			throws IOException, XMLStreamException, InventoryException {
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(inventoryServerURL + "/xinventory");
		Builder builder = webTarget.request();

		log.debug("getItem({}) inventoryServerURL: {}", inventoryItemBase.getId().toString(), inventoryServerURL);

		StringBuilder sb = new StringBuilder();
		sb.append("&ID=").append(inventoryItemBase.getId().toString());
		sb.append("&METHOD=GETITEM");

		long startTime = System.currentTimeMillis();
		Response response = builder.post(Entity.entity(sb.toString(),MediaType.APPLICATION_FORM_URLENCODED));
		long endTime = System.currentTimeMillis();
		log.info("Call to {} took {} ms", inventoryServerURL, endTime - startTime);

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			log.warn("getItem(http Status: "+response.getStatus());
		}

    	String content = response.readEntity(String.class);
		
    	log.debug("respstring: {}", content);

    	response.close();
    	client.close();
    	
    	inventoryItemBase.fromXml(content);
    	
    	return (inventoryItemBase);
	}

}
