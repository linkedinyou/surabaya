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
package org.openjgrid.datatypes;

import java.util.UUID;

import org.joda.time.DateTime;

/**
 * @author Akira Sonoda
 *
 */
public class AssetMetadata {
	private UUID fullid;
	private String id;
	private String name = "";
	private String description = "";
	private DateTime creation_date;
	private byte asset_type = (byte) AssetType.Unknown.getAssetType();
	private String content_type;
	private byte[] sha1;
	private boolean islocal;
	private boolean istemporary;
	private String creatorid;
	private int flags;
	
	public UUID getFullID() {
		return(fullid);
	}

	public void setFullID(UUID value) {
		fullid = value;
		id = fullid.toString();
	}

	public String getID() {
        if (id==null || id.isEmpty()) {
            id = fullid.toString();
        }
        return( id );
	}
	
	public void setID(String value) {
//  TODO      UUID uuid = UUID.fromString("");
//        if (UUID.TryParse(value, out uuid))
//        {
//            m_fullid = uuid;
//            m_id = m_fullid.ToString();
//        }
//        else
//            m_id = value;
		
	}
	
	public String getName() {
		return(name);
	}

	public void setName(String value) {
		name = value;
	}

	public String getDescription() {
		return(description);
	}

	public void setDescription(String value) {
		description = value;
	}

	public DateTime getCreationDate() {
		return(creation_date);
	}

	public void setCreationDate(DateTime value) {
		creation_date = value;
	}

	public byte getType() {
		return(asset_type);
	}

	public void setType(byte value) {
		asset_type = value;
	}

	public String getContentType() {
// TODO		if ( !(/* TODO [ String.IsNullOrEmpty(m_content_type) ] */) ) {
//			return(m_content_type);
//		}
		return(content_type);
	}

	public byte[] getSHA1() {
		return(sha1);
	}

	public void setSHA1(byte[] value) {
		sha1 = value;
	}

	public boolean isLocal() {
		return(islocal);
	}

	public void isLocal(boolean value) {
		islocal = value;
	}

	public boolean isTemporary() {
		return(istemporary);
	}

	public void isTemporary(boolean value) {
		istemporary = value;
	}

	public String getCreatorID() {
		return ( creatorid ) ;
	}

	public void setCreatorID(String value) {
		creatorid = value;
	}

	public int getFlags() {
		return( flags );
	}

	public void setFlags(int value) {
		flags = value;
	}

}
