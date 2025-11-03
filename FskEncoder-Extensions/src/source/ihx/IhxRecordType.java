/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxRecordType.java
 *
 * PURPOSE       : what is it for?
 *
 * This file is part of the FSK-Encoder project. More information about
 * this project can be found here:  http://...
 * **********************************************************************
 *
 * Copyright (C) [2024] by Stefan Dickel, id4mqtt at gmx.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 *
 */


package source.ihx;

/**
 * Responsibilities:<br>
 * Enumerates all valid record types of the <b>Intel Hex Format</b> based on the document
 * <pre>
 * 	Intel Hexadecimal Object File Format Specification
 * 	Revision A, 1/6/88
 * </pre>
 * Provides translation from integer record id to enum type. <br>
 * Provides translation from enum type to integer id 
 * 
 * <p>
 * Collaborators:<br>
 * IhxRecord and the derived types
 * 
 * <p>
 * Description:<br>
 * This enum is used for type safety in IHX data handling.
 * 
 * <p>
 * @author Stefan
 *
 */

public enum IhxRecordType {

    DATA		(0x00),
    EOF			(0x01),
    EXT_SEG		(0x02),
    START_SEG	(0x03),
    EXT_LIN		(0x04),
    START_LIN	(0x05),
    
    UNKNOWN		(0xFF),
    ;
    
	int id;

    IhxRecordType(int id) {
        this.id = id;
    }

    /**
     * Convert enum type to id
     *
     * @return record type integer value
     */
    public int toId() {
        return id;
    }

    /**
     * Convert integer id to enum type
     *
     * @param id 
     * integer value as in the recordType field of an ihx record
     * 
     * @return enum value associated to the given id.
     */
    public static IhxRecordType toType(int id) {
    	
        for (IhxRecordType d : IhxRecordType.values()) {
            if (d.id == id) {
                return d;
            }
        }
        
        return IhxRecordType.UNKNOWN;
        
    } // fromInt(...)
	
	
} // ssalc
