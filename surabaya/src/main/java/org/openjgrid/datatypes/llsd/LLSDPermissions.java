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
@LLSDMapping(mapTo="struct", mappedName = "")
public class LLSDPermissions {

	@LLSDMapping(mapTo="uuid", mappedName = "creator_id")
	public UUID creator_id;
	@LLSDMapping(mapTo="uuid", mappedName = "owner_id")
    public UUID owner_id;
	@LLSDMapping(mapTo="uuid", mappedName = "group_id")
    public UUID group_id;
	@LLSDMapping(mapTo="integer", mappedName = "base_mask")
    public long base_mask;
	@LLSDMapping(mapTo="integer", mappedName = "owner_mask")
    public long owner_mask;
	@LLSDMapping(mapTo="integer", mappedName = "group_mask")
    public long group_mask;
	@LLSDMapping(mapTo="integer", mappedName = "everyone_mask")
    public long everyone_mask;
	@LLSDMapping(mapTo="integer", mappedName = "next_owner_mask")
    public long next_owner_mask;
	@LLSDMapping(mapTo="integer", mappedName = "is_owner_group")
	public boolean is_owner_group;

}
