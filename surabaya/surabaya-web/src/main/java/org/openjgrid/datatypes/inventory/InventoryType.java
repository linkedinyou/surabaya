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

/**
 * @author Akira Sonoda
 *
 * The different types of Inventory Items
 */
public enum InventoryType {

    /** 
     * Unknown
     */
    Unknown((byte)-1),
    /** 
     * Texture
     */
    Texture((byte)0),
    /** 
     * Sound
     */
    Sound((byte)1),
    /** 
     * Calling Card
     */
    CallingCard((byte)2),
    /** 
     * Landmark
     */
    Landmark((byte)3),
    //        
    //  Script
    //  Obsolete Script = 4,
    //  Clothing
    //  Obsolete Clothing = 5,
    
    /**  
     * Object, both single and coalesced</summary>
     */         
    Object((byte)6),
    /** 
     * Notecard
     */
    Notecard((byte)7),

    Category((byte)8),
    /** 
     * Folder
     */
    Folder((byte)8),
    RootCategory((byte)9),
    /** 
     * an LSL Script
     */
    LSL((byte)10),
    //        
    // Obsolete LSLBytecode = 11,
    // Obsolete TextureTGA = 12,
    // Obsolete Bodypart = 13,
    // Obsolete Trash = 14,
    //         
    Snapshot((byte)15),
    //        
    // Obsolete LostAndFound = 16,
    //         
    Attachment((byte)17),
    Wearable((byte)18),
    Animation((byte)19),
    Gesture((byte)20),
    Mesh((byte)22);
    private byte inventoryType;

    private InventoryType(byte inventoryType) {
        this.inventoryType = inventoryType;
    }

    public byte getInventoryType() {
        return this.inventoryType;
    }
    
    public static InventoryType fromByte(byte value) {
        for (InventoryType b : InventoryType.values()) {
            if (value == b.inventoryType) {
                return b;
            }
        }
        return null;
    }

}
