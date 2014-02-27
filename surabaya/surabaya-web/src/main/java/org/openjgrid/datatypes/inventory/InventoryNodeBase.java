/*******************************************************************************
 * Copyright (c) 2014 Akira Sonoda.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Akira Sonoda - initial API and implementation
 ******************************************************************************/
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
package org.openjgrid.datatypes.inventory;

import java.util.UUID;

/**
 * Common base class for inventory nodes of different types (files, folders, etc.)
 * 
 * @author Akira Sonoda
 */
public class InventoryNodeBase {

	/**
	 * The name of the node (64 characters or less)
	 */
	private String name;
	
	/**
	 * A UUID containing the ID for the inventory node itself
	 */
	private UUID id;
	
	/**
	 * The agent who's inventory this is contained by 
	 */
	private UUID ownerId;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 * @throws InventoryException 
	 */
	public void setName(String name) throws InventoryException {
		if(name.length() > 64) {
			throw new InventoryException("InventoryNodeBase: name exceeds 64 chars"); 
		}
		this.name = name;
	}
	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}
	/**
	 * @return the owner
	 */
	public UUID getOwnerId() {
		return ownerId;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwnerId(UUID owner) {
		this.ownerId = owner;
	}
	
}
