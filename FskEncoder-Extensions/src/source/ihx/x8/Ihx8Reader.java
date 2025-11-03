/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Ihx8reader.java
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.model.MemoryMap;
import extension.source.ReaderBase;
import extension.source.ReaderException;
import source.ihx.IhxException;
import source.ihx.IhxLexer;
import source.ihx.IhxParser;

/**
 * Responsibilities:<br>
 * Controls the loading of a ihx8 structured file:
 * <ul>
 *   <li> creates and holds the MemoryMap</li>
 *   <li> creates and holds the Parser </li>
 *   <li> creates and holds the Lexer </li>
 *   <li> creates and holds a ihx8 specific GUI extension </li>
 *   <li> triggers and controls the Parser/Lexer process and the GUI extension </li>
 *   <li> returns a list of MemoryRegions which lays out the memory structure </li>
 *   <li> returns a dedicated MemoryRegion containing a block of data </li>
 * </ul>
 * 
 * <p>
 * Collaborators:<br>
 * IhxLexer,<br>
 * IhxParser,<br>
 * MemoryMap,<br>
 * GUI.
 * 
 * <p>
 * Description:<br>
 * The IhxReader is triggered by the GUI-controller to process a source file. 
 * As a result of the processing, the GUI-controller can obtain a memory image from the MemoryMap for further actions. 
 * 
 * <p>
 * @author Stefan
 *
 */

public class Ihx8Reader extends ReaderBase {

	private Logger logger = LogManager.getLogger(Ihx8Reader.class.getName());
	
	protected IhxLexer lexer;
	protected IhxParser parser;
	
	
	/**
	 * Constructor, which takes a MemoryPas as parameter. <br>
	 * This MemoryMap is served by the reader with every detected MemoryRegion.
	 * 
	 * @throws IhxException 
	 * if the Parser is unable to parse a raw record.
	 * 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * If the Lexer is unable to create a raw record.
	 * 
	 */
	public Ihx8Reader(final MemoryMap aMemoryMap) throws IhxException {
		super(aMemoryMap);
		
		logger.trace("IhxReader()");
		
		filter = new FileNameExtensionFilter("Intel Hex", "hex", "ihx", "i8x", "i8h");
		
		// TODO: test coverage

		try {
			
			setUp();
			
			operationStatus = "Initialized.";
			
		} catch (NoSuchMethodException | SecurityException e) {

			operationStatus = "Internal processing error, '" + e.getClass().getName() + ": " + e.getCause() + "'.";
			logger.fatal(operationStatus);
			
			throw new IhxException("Unable to initialize IhxReader, details provided by getOperationStatus()");
			
		}

	} // IhxReader()
	
	
	/**
	 * Starts the processing of a given source file.<br>
	 * It set the source file in the Lexer and triggers the Parser who starts to process the candidate file.
	 * <p>
	 * Exceptions are thrown if the processing leads to an error.
	 * <p>
	 */
	@Override
	public boolean loadFile()  {
		logger.trace("loadFile()");
		
		if(sourceFileName == null) throw new IllegalAccessError("No source file set");
		
		try {
			
			lexer.setFile(sourceFileName);
			parser.parse();
			
			operationStatus = "Successfuly loaded.";
			logger.info(operationStatus);
			
			return true;
			
		} catch (FileNotFoundException e) {
			
			operationStatus = "Can't find selected file '" + sourceFileName + "'.";
			logger.error(operationStatus);
			
			return false;
		
		} catch (InstantiationException 
				| IllegalAccessException
				| IllegalArgumentException
				| InvocationTargetException
				e) {
			
			operationStatus = "Internal processing error, '" + e.getClass().getName() + ": " + e.getCause() + "'.";
			logger.fatal(operationStatus);
			
			return false;
		
		} catch (ReaderException e) { // NOSONAR
			
			operationStatus = "Invalid IHX file '" + sourceFileName + "', details: '" + e.getMessage() + "'.";
			logger.error(operationStatus);
			
			return false;
		
		} catch (IOException e) { // NOSONAR
			
			operationStatus = "Can't access selected file '" + sourceFileName + "'.";
			logger.error(operationStatus);
			
			return false;
		
		} // chtac
		
	} // loadFile()
	
	
	/*
	 * Set up the environment to be ready to work. 
	 */
	protected void setUp() throws NoSuchMethodException, SecurityException {
		logger.trace("setUp()");
		
		Ihx8MemoryRegionBuilder memoryRegionBuilder = new Ihx8MemoryRegionBuilder(memoryMap);
		lexer = new IhxLexer();
		parser = new IhxParser(lexer, memoryRegionBuilder);
		
	} // setUp()


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "IhxReader [lexer=" + lexer + ", parser=" + parser 
				+ ", " + super.toString()
				+ "]";
	}
	
	
} // ssalc
