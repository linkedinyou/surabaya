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

import java.util.UUID;

/**
 * @author Akira Sonoda
 *
 */
@LLSDMapping(mapTo="struct", mappedName="")
public class LLSDInventoryFolder {
	
	@LLSDMapping(mapTo="uuid", mappedName = "folder_id")
    public UUID folder_id;
	@LLSDMapping(mapTo="uuid", mappedName = "parent_id")
    public UUID parent_id;
	@LLSDMapping(mapTo="string", mappedName = "name")
    public String name;
	@LLSDMapping(mapTo="integer", mappedName = "type")
    public int type;
	@LLSDMapping(mapTo="uuid", mappedName = "preferre_type")
    public int preferred_type;

    public LLSDInventoryFolder (InventoryFolderBase invFolder) {
        this.folder_id = invFolder.getId();
        this.parent_id = invFolder.getParentFolderId();
        this.name = invFolder.getName();
        this.type = invFolder.getType();
        this.preferred_type = -1;
    }

}
