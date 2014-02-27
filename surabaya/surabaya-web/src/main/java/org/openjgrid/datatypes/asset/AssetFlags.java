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
package org.openjgrid.datatypes.asset;

/**
 *
 * @author Akira Sonoda
 */
public enum AssetFlags {

    Normal((int) 0), // Immutable asset
    Maptile((int) 1), // Deprecated, use Deletable instead: What it says
    Rewritable((int) 2), // Content can be rewritten
    Collectable((int) 4), // Can be GC'ed after some time
    Deletable((int) 8), // The asset can be deleted
    Local((int) 16), // Region-only asset, never stored in the database
    Temperary((int) 32), // Is this asset going to exist permanently in the database, or can it be purged after a set amount of time?
    RemotelyAccessable((int) 64); // Regions outside of this grid can access this asset
    private int type;

    private AssetFlags(int aInt) {
        type = aInt;
    }

    public int getType() {
        return type;
    }
}
