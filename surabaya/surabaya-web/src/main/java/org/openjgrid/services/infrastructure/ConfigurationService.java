/**
 *  Surabaya - a replacement http server for the OpenSimulator
 *  Copyright (C) 2012 Akira Sonoda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openjgrid.services.infrastructure;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton implementation class ConfigurationService
 * 
 * Author: Akira Sonoda
 * TODO Add Database/Properties/xml storage of the values
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class ConfigurationService {
	
	private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class);
	
    private ConcurrentMap<String, ConcurrentMap<String, String> > props = new ConcurrentHashMap<String, ConcurrentMap<String, String> >();
    
    /**
     * 
     */
    public ConfigurationService() {
    	
    	log.debug("CongirurationService() starting");
    	ConcurrentMap<String, String> sim_props = new ConcurrentHashMap<String, String>();
    	sim_props.put("sim_http_port", "9000");
    	props.put("OpenSim", sim_props);
    	ConcurrentMap<String, String> surabaya_props = new ConcurrentHashMap<String, String>();
    	surabaya_props.put("hostname", "suai.dyndns.org");
    	surabaya_props.put("http_port", "8080");
    	props.put("Surabaya", surabaya_props);
    	ConcurrentMap<String, String> grid_props = new ConcurrentHashMap<String, String>();
    	grid_props.put("inventory_service", "http://inventory.osgrid.org");
    	grid_props.put("asset_service", "http://assets.osgrid.org");
    	props.put("grid", grid_props);
    	
    
    }
 
    /**
     * @param aPropertyGroup
     * @return
     */
    public ConcurrentMap<String, String> getPropertyGroup(String aPropertyGroup) {
        if (props.containsKey(aPropertyGroup)) {
            return (props.get(aPropertyGroup));
        } else {
            return (null);
        }
    }

    /**
     * @param aPropertyGroup
     * @param property
     * @return
     */
    public String getProperty(String aPropertyGroup, String property) {
        if(props.containsKey(aPropertyGroup)) {
            if(props.get(aPropertyGroup).containsKey(property)) {
                return ((String) props.get(aPropertyGroup).get(property));
            } else {
                return (null);
            }             
        } else {
            return(null);
        }
    }

    /**
     * @param aPropertyGroup
     * @param property
     * @param value
     */
    public void setProperty(String aPropertyGroup, String property, String value ) {
        if(props.containsKey(aPropertyGroup)) {
           props.get(aPropertyGroup).put(property, value);
        }
    }

}
