/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : BinMemoryRegionBuilder.java
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


package source.bin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.model.ExtendableMemory;
import extension.source.DataRecord;
import extension.source.MemoryRegionBuilder;
import source.ihx.IhxException;

/**
 * Responsibilities:<br>
 * 
 * 
 * <p>
 * Collaborators:<br>
 * 
 * 
 * <p>
 * Description:<br>
 * 
 * 
 * <p>
 * @author Stefan
 *
 */

// DOC
// Created at 2024-05-27 14:44:28

public class BinMemoryRegionBuilder extends MemoryRegionBuilder {
	
	private Logger logger = LogManager.getLogger(BinMemoryRegionBuilder.class.getName());

	
	/**
	 * @param aMemoryMap
	 */
	public BinMemoryRegionBuilder(ExtendableMemory aMemoryMap) {
		super(aMemoryMap);
		
		logger.trace("BinMemoryRegionBuilder()");
		
	} // BinMemoryRegionBuilder()


	@Override
	public void append(final DataRecord aDataRecord) throws IhxException {
		logger.trace("append(): aDataRecord = {}", aDataRecord);

		if(memoryRegion == null) {
			newRegion(aDataRecord);
			logger.trace(String.format("newRegion:endAddress = 0x%04X", memoryRegion.getEndAddress()));
			endRegion();
		}
		else {
			
			throw new IllegalAccessError("Can't expand binary memory regions.");
			
		}

	} // append()
	
	
} // ssalc
