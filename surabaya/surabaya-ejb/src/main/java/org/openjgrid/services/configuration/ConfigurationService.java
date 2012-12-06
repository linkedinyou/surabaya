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
package org.openjgrid.services.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

/**
 * Singleton implementation class ConfigurationService
 * 
 * TODO Add Database/Properties/xml storage of the values
 */
@Singleton
@LocalBean
public class ConfigurationService {

    private Map<String, Map<String, String> > props = new HashMap<String, Map<String, String> >();
    
    public ConfigurationService() {
    
    	Map<String, String> sim_props = new HashMap<String, String>();
    	sim_props.put("sim_http_port", "9000");
    	props.put("OpenSim", sim_props);
    
    }
 
    public Map<String, String> getPropertyGroup(String aPropertyGroup) {
        if (props.containsKey(aPropertyGroup)) {
            return (props.get(aPropertyGroup));
        } else {
            return (null);
        }
    }

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

    public void setProperty(String aPropertyGroup, String property, String value ) {
        if(props.containsKey(aPropertyGroup)) {
           props.get(aPropertyGroup).put(property, value);
        }
    }

}
