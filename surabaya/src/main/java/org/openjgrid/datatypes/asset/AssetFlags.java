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
