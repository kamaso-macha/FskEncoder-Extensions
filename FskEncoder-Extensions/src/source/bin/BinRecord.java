/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : BinRecord.java
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

import extension.source.Record;

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
// Created at 2024-05-27 15:12:24

public class BinRecord extends Record {
	
	private Logger logger = LogManager.getLogger(BinRecord.class.getName());

	/**
	 * @param aRecordNumber
	 * @throws Exception 
	 */
	public BinRecord(int aRecordNumber, final Object aContent) throws Exception {
		super(aRecordNumber);

		logger.trace("BinRecord(): aRecordNumber = {}, aContent = {}", aRecordNumber, aContent);

		parseContent(aContent);

	} // BinRecord(...)
	

	@Override
	protected void parseContent(Object aContent) throws Exception {
		logger.trace("parseContent(): aContent = {}", aContent);
		
		offset = 0x0000;
		dataBuffer = (byte[]) aContent;

	} // parseContent()

	
} // ssalc
