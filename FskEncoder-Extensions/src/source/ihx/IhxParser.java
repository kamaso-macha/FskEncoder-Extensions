/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxParser.java
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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.source.MemoryRegionBuilder;
import extension.source.ReaderException;


/**
 * Responsibilities:<br>
 * Read all records from IhxLexer and hand them over to the IhxMemoryRegionBuilder
 * 
 * <p>
 * Collaborators:<br>
 * IhxLexer,<br>
 * IhxMemoryRegionBuilder.
 * 
 * <p>
 * Description:<br>
 * Because the IhxRecord has a built-in parser mechanism, the IhxParser has not much to do.
 * Just read from the IhcLexer as long as new records are available ant push them in the 
 *  IhxMemoryRegionBuilder.
 * 
 * <p>
 * @author Stefan
 *
 */

public class IhxParser {

	private Logger logger = LogManager.getLogger(IhxParser.class.getName());
	
	protected IhxLexer lexer;
	protected MemoryRegionBuilder memoryRegionBuilder;
	
	
	/**
	 * Constructor.<br>
	 * 
	 * @param aLexer 
	 * the IhxLexer to read records from.
	 * 
	 * @param aMemoryRegionBuilder
	 * receives the IhxRecords and build a MemoryImage from it.
	 * 
	 * @throws IllegalArgumentException<br>
	 * if aLexer is null<br>
	 * or aMemoryRegionBuilder is null.
	 */
	public IhxParser(final IhxLexer aLexer, final MemoryRegionBuilder aMemoryRegionBuilder) {

		logger.trace("IhxParser(): aLexer = {}, aMemoryRegionBuilder = {}", aLexer, aMemoryRegionBuilder);
		
		if(aLexer == null) throw new IllegalArgumentException("aLexer can't be null");
		if(aMemoryRegionBuilder == null) throw new IllegalArgumentException("aMemoryRegionBuilder can't be null");
		
		lexer = aLexer;
		memoryRegionBuilder = aMemoryRegionBuilder;
	
	} // IhxParser(...)


	/**
	 * The trigger to run the parser process.<br>
	 * The IhxLexer is read as long as IhxRecords are obtainable and the records are handed over to the IhxMemoryRegionBuilder.
	 * 
	 * @throws IhxException
	 * on IHX format violations during creation of the IhxRecords.
	 *  
	 * @throws IOException
	 * on errors reading the source file
	 *  
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * if errors occurs on instantiation of an IhxRecord.
	 * 
	 */
	public void parse() throws ReaderException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		logger.trace("parse()");
		
		
		IhxRecord record = null;
		memoryRegionBuilder.clear();
		
		while((record = lexer.getRecord()) != null) {
			
			memoryRegionBuilder.append(record);
			
		}
		
	} // parse()


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "IhxParser [lexer=" + lexer + ", memoryRegionBuilder=" + memoryRegionBuilder + "]";
	}
	
	
} // ssalc
