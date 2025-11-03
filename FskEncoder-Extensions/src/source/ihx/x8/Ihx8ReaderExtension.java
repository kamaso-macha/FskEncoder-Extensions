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


package source.ihx.x8;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.control.StatusMessenger;
import extension.factory.InputReaderExtensionFactory;
import extension.model.InputReaderExtensionDao;
import extension.model.MemoryMap;
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
// Created at 2024-05-13 14:32:41

public class Ihx8ReaderExtension implements InputReaderExtensionFactory {

	private Logger logger = LogManager.getLogger(Ihx8ReaderExtension.class.getName());
	
	@Override
	public InputReaderExtensionDao getInputReaderExtensions(StatusMessenger aStatusMessenger) {
		logger.trace("getInputReaderExtensions(): aStatusMessenger = {}", aStatusMessenger);
		
		if(aStatusMessenger == null) throw new IllegalArgumentException("aStatusMessenger can't be null");
		
		try {
			
			MemoryMap memoryMap = new MemoryMap();
			Ihx8Reader reader = new Ihx8Reader(memoryMap);
			
			Ihx8ReaderControl control = new Ihx8ReaderControl(aStatusMessenger);
			
			return new InputReaderExtensionDao(reader, memoryMap, control.getGui(), control);
			
		} catch (IhxException e) {
			logger.error("Unable to initialize IhxReader, reason: {}", e);
		}
		
		return null;
		
	} // getInputReaderExtensions()

	
	
}
