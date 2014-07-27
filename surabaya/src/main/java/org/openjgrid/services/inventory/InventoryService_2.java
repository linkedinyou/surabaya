package org.openjgrid.services.inventory;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.stream.XMLStreamException;

import org.apache.http.client.ClientProtocolException;
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
    private static final Logger log = LoggerFactory.getLogger(InventoryService_2.class);

    Client client = null;

    @EJB ( mappedName = "java:module/ConfigurationService")
    private ConfigurationService configuration;

    @PostConstruct
    private void init() {
        client = ClientBuilder.newClient();
    }

    @PreDestroy
    private void close() {
        log.info("close()");
        client.close();     
    }
    
    public InventoryCollection getFolderContent( UUID userID, UUID folderID, String inventoryServerURL ) {
        log.debug("getFolderContent(userID:{}, folderID:{} )", userID, folderID);
        InventoryCollection invCollection = new InventoryCollection();
        Response response = null;
        
        try {
            invCollection.userId = userID;

            WebTarget webTarget = client.target(inventoryServerURL + "/xinventory");
            Builder builder = webTarget.request();

            StringBuilder sb = new StringBuilder("PRINCIPAL=").append(userID.toString());
            sb.append("&FOLDER=").append(folderID.toString());
            sb.append("&METHOD=GETFOLDERCONTENT");

            log.info("geFolderContent({}) inventoryServerURL: {}", folderID.toString(), inventoryServerURL);            
            
            long startTime = System.currentTimeMillis();
            response = builder.post(Entity.entity(sb.toString(), MediaType.APPLICATION_FORM_URLENCODED));
            long endTime = System.currentTimeMillis();
            log.info("Call to {} took {} ms", inventoryServerURL, endTime - startTime);

            if( response.getStatus() != Response.Status.OK.getStatusCode() ) {
                log.warn("getFolderContent(userID: " + userID + ", folderID: " + folderID + ") http Status: " + response.getStatus());
            }

            String content = response.readEntity(String.class);

            log.debug("respstring: {}", content);

            if( Util.isNullOrEmpty(content) ) {
                return (null);
            }
            invCollection.fromXml(content);

            return (invCollection);

        } catch( Exception ex ) {
            log.debug("Exception occurred in getFolderContent()", ex);
            return (null);
        } finally {
            response.close();            
        }

    }

    /**
     * @param containingFolder
     * @return
     */
    public InventoryFolderBase getFolder( InventoryFolderBase containingFolder, String inventoryServerURL ) {
        log.debug("getFolder()");
        Response response = null;

        try {

            WebTarget webTarget = client.target(inventoryServerURL + "/xinventory");
            Builder builder = webTarget.request();

            StringBuilder sb = new StringBuilder();
            sb.append("&ID=").append(containingFolder.getId().toString());
            sb.append("&METHOD=GETFOLDER");

            log.info("geFolder({}) inventoryServerURL: {}", containingFolder.getId().toString(), inventoryServerURL);
            
            
            long startTime = System.currentTimeMillis();
            response = builder.post(Entity.entity(sb.toString(), MediaType.APPLICATION_FORM_URLENCODED));
            long endTime = System.currentTimeMillis();
            log.info("Call to {} took {} ms", inventoryServerURL, endTime - startTime);

            if( response.getStatus() != Response.Status.OK.getStatusCode() ) {
                log.warn("getFolder(http Status: " + response.getStatus());
            }

            String content = response.readEntity(String.class);

            log.debug("respstring: {}", content);

            containingFolder.fromXml(content);

        } catch( Exception ex ) {
            log.debug("Exception occurred in getFolderContent()", ex);
            return (null);
        } finally {
            response.close();
        }

        return (containingFolder);
    }

    /**
     * @param inventoryItemBase
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     * @throws XMLStreamException
     * @throws InventoryException
     */
    public InventoryItemBase getItem( InventoryItemBase inventoryItemBase, String inventoryServerURL ) throws IOException,
            XMLStreamException, InventoryException {
        Response response = null;
        try {
            WebTarget webTarget = client.target(inventoryServerURL + "/xinventory");
            Builder builder = webTarget.request();

            log.info("getItem({}) inventoryServerURL: {}", inventoryItemBase.getId().toString(), inventoryServerURL);

            StringBuilder sb = new StringBuilder();
            sb.append("&ID=").append(inventoryItemBase.getId().toString());
            sb.append("&METHOD=GETITEM");

            long startTime = System.currentTimeMillis();
            response = builder.post(Entity.entity(sb.toString(), MediaType.APPLICATION_FORM_URLENCODED));
            long endTime = System.currentTimeMillis();
            log.info("Call to {} took {} ms", inventoryServerURL, endTime - startTime);

            if( response.getStatus() != Response.Status.OK.getStatusCode() ) {
                log.warn("getItem(http Status: " + response.getStatus());
            }

            String content = response.readEntity(String.class);

            log.debug("respstring: {}", content);

            inventoryItemBase.fromXml(content);

            return (inventoryItemBase);
        } finally {
            response.close();
        }
    }

}
