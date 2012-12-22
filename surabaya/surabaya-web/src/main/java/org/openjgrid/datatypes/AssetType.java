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

/**
 * @author Akira Sonoda
 *
 * The different types of grid assets
 */
public enum AssetType {

    /** 
     * Unknown asset type
     */
    Unknown((byte) -1),
    /** 
     * Texture asset, stores in JPEG2000 J2C stream format
     */
    Texture((byte) 0),
    /** 
     * Sound asset
     */
    Sound((byte) 1),
    /** 
     * Calling card for another avatar
     */
    CallingCard((byte) 2),
    /** 
     * Link to a location in world
     */
    Landmark((byte) 3),
    // <summary>
    // Legacy script asset, you should never see one of these</summary>
    //[Obsolete]
    //Script = 4,
    /** 
     * Collection of textures and parameters that can be worn by an avatar
     */
    Clothing((byte) 5),
    /** 
     * Primitive that can contain textures, sounds, scripts and more
     */
    Object((byte) 6),
    /** 
     * Notecard asset
     */
    Notecard((byte) 7),
    /** 
     * Holds a collection of inventory items
     */
    Folder((byte) 8),
    /** 
     * Root inventory folder
     */
    RootFolder((byte) 9),
    /** 
     * Linden scripting language script
     */
    LSLText((byte) 10),
    /** 
     * LSO bytecode for a script
     */
    LSLBytecode((byte) 11),
    /** 
     * Uncompressed TGA texture
     */
    TextureTGA((byte) 12),
    /** 
     * Collection of textures and shape parameters that can be worn
     */
    Bodypart((byte) 13),
    /** 
     * Trash folder
     */
    TrashFolder((byte) 14),
    /** 
     * Snapshot folder
     */
    SnapshotFolder((byte) 15),
    /** 
     * Lost and found folder
     */
    LostAndFoundFolder((byte) 16),
    /** 
     * Uncompressed sound
     */
    SoundWAV((byte) 17),
    /** 
     * Uncompressed TGA non-square image, not to be used as a texture
     */
    ImageTGA((byte) 18),
    /** 
     * Compressed JPEG non-square image, not to be used as a texture
     */
    ImageJPEG((byte) 19),
    /** 
     * Animation
     */
    Animation((byte) 20),
    /** 
     * Sequence of animations, sounds, chat, and pauses
     */
    Gesture((byte) 21),
    /** 
     * Simstate file
     */
    Simstate((byte) 22),
    /** 
     * Contains landmarks for favorites
     */
    FavoriteFolder((byte) 23),
    /** 
     * Asset is a link to another inventory item
     */
    Link((byte) 24),
    /** 
     * Asset is a link to another inventory folder
     */
    LinkFolder((byte) 25),
    /** 
     * Beginning of the range reserved for ensembles
     */
    EnsembleStart((byte) 26),
    /** 
     * End of the range reserved for ensembles
     */
    EnsembleEnd((byte) 45),
    /** 
     * Folder containing inventory links to wearables and attachments
     * that are part of the current outfit
     */
    CurrentOutfitFolder((byte) 46),
    /** Folder containing inventory items or links to
     * inventory items of wearables and attachment
     * together make a full outfit
     */
    OutfitFolder((byte) 47),
    /** Root folder for the folders of type OutfitFolder
     */
    MyOutfitsFolder((byte) 48),
    /** 
     * Linden mesh format
     */
    Mesh((byte) 49);
    private byte assetType;

    private AssetType(byte assetType) {
        this.assetType = assetType;
    }

    public byte getAssetType() {
        return this.assetType;
    }

    public static AssetType fromByte(byte value) {
        for (AssetType b : AssetType.values()) {
            if (value == b.getAssetType()) {
                return b;
            }
        }
        return null;
    }
    
}
