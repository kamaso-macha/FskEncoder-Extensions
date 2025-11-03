/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxLexer.java
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.source.Lexer;

/**
 * Responsibilities:<br>
 * The IHX specific implementation of the Lexer class.
 * 
 * <p>
 * Collaborators:<br>
 * Lexer,<br>
 * IhxReader,<br>
 * IhxParser.
 * 
 * <p>
 * Description:<br>
 * The one and only function of this class is to set up the base class with the required parameters.
 * <p>
 * All the work is done in the base class!.
 * 
 * <p>
 * @author Stefan
 *
 */

public class IhxLexer extends Lexer<IhxRecord> {

	private Logger logger = LogManager.getLogger(IhxLexer.class.getName());
	
	/**
	 * Default constructor.
	 * 
	 * It supplies the constructor of the base class with an Constructor object of the required record type.
	 * 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * if the given Constructor object is invalid. 
	 * 
	 */
	public IhxLexer() throws NoSuchMethodException, SecurityException {
		super(IhxRecord.class.getConstructor(int.class, String.class));
		
		logger.trace("IhxLexer()");

	} // IhxLexer()
	

} // ssalc
