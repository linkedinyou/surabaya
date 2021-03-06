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

import org.openjgrid.datatypes.inventory.InventoryCollection_2;
import org.openjgrid.datatypes.inventory.InventoryFolderBase;
import org.openjgrid.datatypes.inventory.InventoryItemBase;

/**
 * Workaround class in order to keep the original OpenSim logic working
 * because Java supports no out arguments.
 * 
 * @author Akira Sonoda
 */
public class Fetch_2 {

    public InventoryCollection_2 inventoryCollection_2 = null;
    public int version = 0;
    public int descendents = 0;

    public Fetch_2() {
    	inventoryCollection_2 = new InventoryCollection_2();
        inventoryCollection_2.folderList = new ArrayList<InventoryFolderBase>();
        inventoryCollection_2.itemList = new ArrayList<InventoryItemBase>();
    	version = 0;
    	descendents = 0;
    }
	
}
