/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxMemoryRegionBuilder.java
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

import extension.model.ExtendableMemory;
import extension.source.DataRecord;
import extension.source.MemoryRegionBuilder;
import source.ihx.IhxException;
import source.ihx.IhxRecord;
import source.ihx.IhxRecordType;

/**
 * Responsibilities:<br>
 * IHX specific implementation of MemoryRegionBuilder.
 * Receive IhxRecords and handle them according to their IhxRecordType.
 * 
 * <p>
 * Collaborators:<br>
 * IhxParser,<br>
 * MemoryRegionBuilder
 * 
 * <p>
 * Description:<br>
 * The IhxMemoryRegionBuilder receives IhxRecords and determines on their IhxRecordType whether to treat it as 
 * DATA or as EOF record.<br>
 * The further processing of the incoming record is based on that decision.  
 * 
 * <p>
 * @author Stefan
 *
 */

public class Ihx8MemoryRegionBuilder extends MemoryRegionBuilder {

	private Logger logger = LogManager.getLogger(Ihx8MemoryRegionBuilder.class.getName());
	
	/**
	 * Default constructor.
	 * 
	 * @param aMemoryMap 
	 * the MemoryMap which is build during a parsing process.
	 * 
	 */
	public Ihx8MemoryRegionBuilder(final ExtendableMemory aMemoryMap) {
		super(aMemoryMap);
		
		logger.trace("IhxMemoryRegionBuilder()");
		
	} // IhxMemoryRegionBuilder()
	
	
	/**
	 * Takes an IhxRecord and decides on it's type (DATA or EOF) the further processing.
	 * <p>
	 * On receive of the first DATA record at all a new MemoryRegion object is created and feed with the DATA record. 
	 * This MemoryRegion is feed with all subsequent DATA records.
	 * <p>
	 * If the source file contains more than one memory region, the creation of different MemoryRegions is handled 
	 * in the base class. 
	 * 
	 * @param aDataRecord 
	 * an IhxRecord to process.
	 * 
	 * @throws IhxException 
	 * if a DATA record appears after an EOF record.
	 */
	@Override
	public void append(final DataRecord aDataRecord) throws IhxException {
		logger.trace("append(IhxRecord): aDataRecord = {}", aDataRecord);
		
		if(aDataRecord == null) throw new IllegalArgumentException("aDataRecord can't be null.");
		if(eofFlag == true) throw new IhxException("Invalid file structure: DATA record after EOF record");
		
		IhxRecord record = (IhxRecord) aDataRecord;
		
		if(record.getRecordType()== IhxRecordType.DATA) {
			
			dataRecord(record);
			
		}
		else if(record.getRecordType()== IhxRecordType.EOF) {
			
			eofRecord();
			
		}
		else {
			throw new IhxException("unknown recordType " + record.getRecordType());
		}
		
	} // IhxDataRecord()


	/*
	 * Processing of the DATA record.
	 * 
	 * If no memory region is defined, a new one is created and served with the current record.
	 * 
	 * All subsequent records are appended to that memory region until method extendRegion() of the superclass detects
	 * a disruption of the current memory block. Than it closes the current memory region and requests this method 
	 * to create a new memory region.
	 * 
	 */
	protected void dataRecord(final IhxRecord aDataRecord) {
		logger.trace("dataRecord(IhxRecord): aDataRecord = {}", aDataRecord);

		if(memoryRegion == null) {
			newRegion((DataRecord) aDataRecord);
			logger.trace(String.format("newRegion: memoryRegion.endAddress = 0x%04X", memoryRegion.getEndAddress()));
		}
		else {
			logger.trace(String.format("extendRegion: memoryRegion.endAddress = 0x%04X", memoryRegion.getEndAddress()));
			extendRegion((DataRecord) aDataRecord);
		}
		
	} // dataRecord()
	
	
	/*
	 * Handles an EOF record.
	 * 
	 * The eofRecord is set to true to indicate that no more records can be accepted.
	 * Next, it requests the base class to close the current memory region. 
	 * 
	 */
	protected void eofRecord() {
		logger.trace("eofRecord()");

		eofFlag = true;			
		endRegion();
		
	} // eofRecord()


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "IhxMemoryRegionBuilder [eofRecord=" + eofFlag
			+ ", " + super.toString()
			+ "]";
	}


} // ssalc
