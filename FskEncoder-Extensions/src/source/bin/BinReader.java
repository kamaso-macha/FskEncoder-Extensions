/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : BinReader.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.model.MemoryMap;
import extension.source.DataRecord;
import extension.source.ReaderBase;

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
// Created at 2024-05-27 14:41:10

public class BinReader extends ReaderBase {
	
	private Logger logger = LogManager.getLogger(BinReader.class.getName());
	private BinMemoryRegionBuilder memoryRegionBuilder;

	/**
	 * @param aMemoryMap
	 * @throws IllegalArgumentException
	 */
	public BinReader(MemoryMap aMemoryMap) throws IllegalArgumentException {
		super(aMemoryMap);
		
		logger.trace("BinReader()");
		
		filter = new FileNameExtensionFilter("Binary", "bin");
		
		// TODO: test coverage

		try {
			
			setUp();
			
			operationStatus = "Initialized.";
			
		} catch (SecurityException e) {

			operationStatus = "Internal processing error, '" + e.getClass().getName() + ": " + e.getCause() + "'.";
			logger.fatal(operationStatus);
			
		}

	} // BinReader()
	

	@Override
	public boolean loadFile() throws IllegalAccessError {
		logger.trace("loadFile()");
		
		FileInputStream fis = null;
		
		try{

			memoryRegionBuilder.clear();
		
			File file = new File(sourceFileName);
			
			byte[] bytes = new byte[(int) file.length()];
			
			fis = new FileInputStream(file);
			fis.read(bytes);
			fis.close();
		  
			DataRecord record = new BinRecord(1, bytes);
			memoryRegionBuilder.append(record);
			
			operationStatus = "Successfuly loaded.";
			logger.info(operationStatus);

			return true;
		  
		} 
		catch (FileNotFoundException e) {
			operationStatus = "Internal processing error, '" + e.getClass().getName() + ": " + e.getCause() + "'.";
			logger.fatal(operationStatus);
			
			return false;
		
		} 
		catch (IOException e) {
			operationStatus = "Internal processing error, '" + e.getClass().getName() + ": " + e.getCause() + "'.";
			logger.fatal(operationStatus);
			
			return false;
		
		} 
		catch (Exception e) {
			operationStatus = "Internal processing error, '" + e.getClass().getName() + ": " + e.getCause() + "'.";
			logger.fatal(operationStatus);
			
			return false;
		
		}
		
	} // loadFile()
	

	/*
	 * Set up the environment to be ready to work. 
	 */
	protected void setUp() {
		logger.trace("setUp()");
		
		memoryRegionBuilder = new BinMemoryRegionBuilder(memoryMap);
		
	} // setUp()


} // ssalc
