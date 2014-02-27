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
package org.openjgrid.services.infrastructure;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.openjgrid.datatypes.asset.AssetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton implementation class SLTypeMappingService
 * 
 * Author: Akira Sonoda
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SLTypeMappingService {
	
	private static final Logger log = LoggerFactory.getLogger(SLTypeMappingService.class);
	
    private ConcurrentMap<Byte, String > asset2content = new ConcurrentHashMap<Byte, String>();
    
    /**
     * 
     */
    public SLTypeMappingService() {
        asset2content.put(AssetType.Unknown.getAssetType(), "application/octet-stream");
        asset2content.put(AssetType.Texture.getAssetType(), "image/x-j2c");
        asset2content.put(AssetType.TextureTGA.getAssetType(), "image/tga");
        asset2content.put(AssetType.ImageTGA.getAssetType(), "image/tga");
        asset2content.put(AssetType.ImageJPEG.getAssetType(), "image/jpeg");
        asset2content.put(AssetType.Sound.getAssetType(), "audio/ogg");
        asset2content.put(AssetType.SoundWAV.getAssetType(), "audio/x-wav");
        asset2content.put(AssetType.CallingCard.getAssetType(), "application/vnd.ll.callingcard");
        asset2content.put(AssetType.Landmark.getAssetType(), "application/vnd.ll.landmark");
        asset2content.put(AssetType.Clothing.getAssetType(), "application/vnd.ll.clothing");
        asset2content.put(AssetType.Object.getAssetType(), "application/vnd.ll.primitive");
        asset2content.put(AssetType.Notecard.getAssetType(), "application/vnd.ll.notecard");
        asset2content.put(AssetType.Folder.getAssetType(), "application/vnd.ll.folder");
        asset2content.put(AssetType.RootFolder.getAssetType(), "application/vnd.ll.rootfolder");
        asset2content.put(AssetType.LSLText.getAssetType(), "application/vnd.ll.lsltext");
        asset2content.put(AssetType.LSLBytecode.getAssetType(), "application/vnd.ll.lslbyte");
        asset2content.put(AssetType.Bodypart.getAssetType(), "application/vnd.ll.bodypart");
        asset2content.put(AssetType.TrashFolder.getAssetType(), "application/vnd.ll.trashfolder");
        asset2content.put(AssetType.SnapshotFolder.getAssetType(), "application/vnd.ll.snapshotfolder");
        asset2content.put(AssetType.LostAndFoundFolder.getAssetType(), "application/vnd.ll.lostandfoundfolder");
        asset2content.put(AssetType.Animation.getAssetType(), "application/vnd.ll.animation");
        asset2content.put(AssetType.Gesture.getAssetType(), "application/vnd.ll.gesture");
        asset2content.put(AssetType.Simstate.getAssetType(), "application/x-metaverse-simstate");
        asset2content.put(AssetType.FavoriteFolder.getAssetType(), "application/vnd.ll.favoritefolder");
        asset2content.put(AssetType.Link.getAssetType(), "application/vnd.ll.link");
        asset2content.put(AssetType.LinkFolder.getAssetType(), "application/vnd.ll.linkfolder");
        asset2content.put(AssetType.CurrentOutfitFolder.getAssetType(), "application/vnd.ll.currentoutfitfolder");
        asset2content.put(AssetType.OutfitFolder.getAssetType(), "application/vnd.ll.outfitfolder");
        asset2content.put(AssetType.MyOutfitsFolder.getAssetType(), "application/vnd.ll.myoutfitsfolder");
        asset2content.put(AssetType.Mesh.getAssetType(), "application/vnd.ll.mesh");
    }
 
    public String slAssetTypeToContentType(byte assetType) {
    	String contentType;
        if (!asset2content.containsKey(assetType)) {
        	log.error("Unknown AssetType encountered: {}", assetType); 
            return (asset2content.get(AssetType.Unknown.getAssetType()));
        } else {
        	contentType = asset2content.get(assetType);
        	log.debug("ContentType: {}", contentType);
        	return (contentType);
        }
    }

}
