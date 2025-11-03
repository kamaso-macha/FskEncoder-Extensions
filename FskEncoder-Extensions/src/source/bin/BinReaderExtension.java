/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Ihx8ReaderExtension.java
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

import extension.control.StatusMessenger;
import extension.factory.InputReaderExtensionFactory;
import extension.model.InputReaderExtensionDao;
import extension.model.MemoryMap;

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
// Created at 2024-05-13 14:32:41

public class BinReaderExtension implements InputReaderExtensionFactory {

	private Logger logger = LogManager.getLogger(BinReaderExtension.class.getName());
	
	@Override
	public InputReaderExtensionDao getInputReaderExtensions(StatusMessenger aStatusMessenger) {
		logger.trace("getInputReaderExtensions(): aStatusMessenger = {}", aStatusMessenger);
		
		if(aStatusMessenger == null) throw new IllegalArgumentException("aStatusMessenger can't be null");
		
		MemoryMap memoryMap = new MemoryMap();
		BinReader reader = new BinReader(memoryMap);
		
		BinReaderControl control = new BinReaderControl(aStatusMessenger);
		
		return new InputReaderExtensionDao(reader, memoryMap, control.getGui(), control);
		
	} // getInputReaderExtensions()

	
	
}
