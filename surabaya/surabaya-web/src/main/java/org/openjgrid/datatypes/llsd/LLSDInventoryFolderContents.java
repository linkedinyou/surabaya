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
package org.openjgrid.datatypes.llsd;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Akira Sonoda
 *
 */
@LLSDMapping(mapTo="struct", mappedName = "")
public class LLSDInventoryFolderContents {
	
	@LLSDMapping(mapTo="uuid", mappedName = "agent_id")
    public UUID agent_id; 
	@LLSDMapping(mapTo="integer", mappedName = "descendents")
    public int descendents;
	@LLSDMapping(mapTo="uuid", mappedName = "folder_id")
    public UUID folder_id; 
	
	@LLSDMapping(mapTo="array", mappedName = "categories")
    public ArrayList<Object> categories = new ArrayList<Object>();
	@LLSDMapping(mapTo="array", mappedName = "items")
    public ArrayList<Object> items = new ArrayList<Object>();

	@LLSDMapping(mapTo="uuid", mappedName = "owner_id")
	public UUID owner_id; 
	@LLSDMapping(mapTo="integer", mappedName = "version")
    public int version;


}
