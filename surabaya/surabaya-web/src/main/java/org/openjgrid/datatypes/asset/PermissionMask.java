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
 * @author Akira Sonoda
 *
 * The different types of Inventory Items
 */
public enum PermissionMask {

    None((long)0),
    Transfer((long) 0x2000),
    Modify  ((long) 0x4000),
    Copy    ((long) 0x8000),
    Move    ((long) 0x80000),
    Damage  ((long) 0x100000),
    All     ((long) 0x7FFFFFFF);
    private long permissionMask;

    private PermissionMask(long permissionMask) {
        this.permissionMask = permissionMask;
    }

    public long getPermissionMask() {
        return this.permissionMask;
    }
    
    public static PermissionMask fromLong(long value) {
        for (PermissionMask b : PermissionMask.values()) {
            if (value == b.permissionMask) {
                return b;
            }
        }
        return null;
    }
}
