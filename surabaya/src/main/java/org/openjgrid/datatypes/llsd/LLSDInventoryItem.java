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

import org.openjgrid.datatypes.inventory.InventoryItemBase;

/**
 * @author Akira Sonoda
 *
 */
@LLSDMapping(mapTo="struct", mappedName = "")
public class LLSDInventoryItem {

	@LLSDMapping(mapTo="uuid", mappedName = "parent_id")
	public UUID parent_id;

	@LLSDMapping(mapTo="uuid", mappedName = "asset_id")
    public UUID asset_id;
	@LLSDMapping(mapTo="uuid", mappedName = "item_id")
    public UUID item_id;
	@LLSDMapping(mapTo="struct", mappedName = "permissions")
    public LLSDPermissions permissions;
	@LLSDMapping(mapTo="integer", mappedName = "type")
    public long type;
	@LLSDMapping(mapTo="integer", mappedName = "inv_type")
    public long inv_type;
	@LLSDMapping(mapTo="integer", mappedName = "flags")
    public long flags;

	@LLSDMapping(mapTo="struct", mappedName = "sale_info")
    public LLSDSaleInfo sale_info;
	@LLSDMapping(mapTo="string", mappedName = "name")
    public String name;
	@LLSDMapping(mapTo="string", mappedName = "desc")
    public String desc;
	@LLSDMapping(mapTo="integer", mappedName = "created_at")
    public long created_at;

    public LLSDInventoryItem (InventoryItemBase invItem) {
        this.asset_id = invItem.getAssetId();
        this.created_at = invItem.getCreationDate();
        this.desc = invItem.getDescription();
        this.flags = invItem.getFlags();
        this.item_id = invItem.getId();
        this.name = invItem.getName();
        this.parent_id = invItem.getParentFolderId();
        this.type = invItem.getAssetType();
        this.inv_type = invItem.getInvType();

        this.permissions = new LLSDPermissions();
        this.permissions.creator_id = invItem.getCreatorIdAsUUID();
        this.permissions.base_mask = invItem.getCurrentPermissions();
        this.permissions.everyone_mask = invItem.getEveryOnePermissions();
        this.permissions.group_id = invItem.getGroupId();
        this.permissions.group_mask = invItem.getGroupPermissions();
        this.permissions.is_owner_group = invItem.isGroupOwned();
        this.permissions.next_owner_mask = invItem.getNextPermissions();
        this.permissions.owner_id = invItem.getOwnerId();
        this.permissions.owner_mask = invItem.getCurrentPermissions();
        this.sale_info = new LLSDSaleInfo();
        this.sale_info.sale_price = invItem.getSalePrice();
        this.sale_info.sale_type = invItem.getSaleType();

    }

}
