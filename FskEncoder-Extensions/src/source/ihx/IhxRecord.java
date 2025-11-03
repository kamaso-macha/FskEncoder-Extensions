/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxRecord.java
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

import extension.source.Record;

/**
 * Responsibilities:<br>
 * represent a Intel Hex Format (IHX) specific data record.<br>
 * Parse a given string according to the IHX format specification.<br>
 * Validates the input string against the format specification.<br>
 * 
 * <p>
 * Collaborators:<br>
 * Base class Record,<br>
 * IhxMemoryRegionBuilder,<br>
 * IhxRecordType.
 * 
 * <p>
 * Description:<br>
 * A string, given in the constructor call, is parsed and validated against the IHX format specification.<br>
 * If errors are detected, an IhxException is thrown, set up with a message to indicate the cause of the error.
 * 
 * <p>
 * @author Stefan
 *
 */

public class IhxRecord extends Record {
	
	/*
	 
	 	Intel Hexadecimal Object File Format Specification, Revision A, 1/6/88
	 
	 	Intel Hex General Record Format as described in §2 of the specification. 
	 	
	 	0          1          2          4          5           5+n  
	 	+----------+----------+----------+----------+-----------+----------+
	 	|  Record  |   Load   |   Load   |  RecTyp  |  Info or  |  ChkSum  |
	 	|   Mark   |  RecLen  |  Offset  |          |   Data    |          |
	 	+----------+----------+----------+----------+-----------+----------+
          1 byte     1 byte     2 bytes     1 byte    n bytes     1 byte

		0         1         2         3         4         5         6         7
		012345678901234567890123456789012345678901234567890123456789012345678901234 
		01 2   4 5                                                               x
		:2020A00000CDCF2106000E08CDAB212012327222473A7122CD5621CD752106FFCB40C93EE4
	 	|| |   | |                                                               |
	 	|| |   | |                                                               +- ChkSum
	 	|| |   | |    
	 	|| |   | +- Info or Data
	 	|| |   +- RecTyp
	 	|| +- Load Offset
	 	|+- Load RecLen
	 	+- Record Mark
	 */
	
	protected static final int HEX = 16;
	
    protected static final String RECORD_MARK = ":";

	
    private Logger logger = LogManager.getLogger(IhxRecord.class.getName());
	
	protected byte recordLength;
	protected IhxRecordType recordType;
	protected int chkeckSum;
	
	
	/**
	 * Constructor.<br>
	 * The constructor does a call to the parse() method which is responsible for parsing and validating the input.
	 * 
	 * @param aContent 
	 * String to parse
	 * 
	 * @throws IhxException 
	 * if the parser detects any format violation.
	 */
	public IhxRecord(final int aRecordNumber, final String aContent) throws IhxException {
		super(aRecordNumber);

		logger.trace("IhxRecord(): aRecordNumber = {}, aContent = {}", aRecordNumber, aContent);

		parseContent(aContent);

	} // IhxRecord(...)
	
	
	/**
	 * Returns the length of the record as defined in the record data.
	 * 
	 * @return
	 * the length of the record in bytes.
	 */
	public byte getRecordLength() { return recordLength; }
	
	
	/**
	 * Returns the type of the record.
	 * 
	 * @return
	 * the enum type related to the numeric record type tag in the record data. 
	 */
	public IhxRecordType getRecordType() { return recordType; }

	
	/*
	 * Internal helper methods
	 */
	
	/*
	 * Translation from ASCII representation in the given input string to the binary equivalent.
	 * While the conversion steps on byte by byte a checksum is calculated in parallel.
	 * 
	 * The binary data are returned in a byte[].
	 */
	protected byte[] ascii2bin(final String aContent) {
		logger.trace("ascii2bin(): aContent = {}", aContent);
		
        byte[] hexcontent = new byte[aContent.length() / 2];
        chkeckSum = 0;
        
        int startIdx;
        int endIdx;
        
        for (int i = 0; i < hexcontent.length; i++) {
        	
        	startIdx = i * 2 + 1;
        	endIdx   = startIdx + 2;
        	
            String num = aContent.substring(startIdx, endIdx);
            hexcontent[i] = (byte) Integer.parseInt(num, HEX);
            chkeckSum += hexcontent[i] & 0xff;
            
        }
        
        return hexcontent;
        
	} // ascii2bin(...)
	
	
	/*
	 * Verifies the checksum and throws an IhxException on an invalid result.
	 * The checksum must always be 0!
	 * Refer to the IHX specification §2 for detailed information.
	 */
	protected void checksum() throws IhxException {
		logger.trace("checksum()");
        
        chkeckSum &= 0xff;
        logger.debug("Checksum: checkSum: {}", String.format("0x%02X", chkeckSum));
        
		if (chkeckSum != 0) throw new IhxException("Invalid checksum in line " + super.recordNumber);
		
	} // checksum()


	/*
	 * Copies the binary payload (after ascii2bin() was called) into a data buffer. 
	 */
	protected void infoOrData(final byte[] aBinRecord) throws IhxException {
		logger.trace("infoOrData(): aContent = {}", aBinRecord);

		dataBuffer = new byte[recordLength];
		System.arraycopy(aBinRecord, 4, dataBuffer, 0, recordLength);
		
	} // infoOrData(...)


	/*
	 * Fetches the binary value of the offset field and calculates th integer equivalent.
	 */
	protected void offset(final byte[] aBinRecord) throws IhxException {
		logger.trace("offset(): aContent = {}", aBinRecord);

		offset = ((aBinRecord[1] & 0xFF) << 8) + (aBinRecord[2] & 0xFF);
		
	} // offset(...)


	/*
	 * Parses the given input string.
	 * For each part of the record structure a specific method is responsible.
	 */
	@Override
	protected void parseContent(final Object aContent) throws IhxException {
		String content = (String) aContent;

		logger.trace("parseContent(): aContent = {}", aContent);

		recordMark(content);

		byte[] binRecord = ascii2bin(content);
		
		recordLength(binRecord);
		offset(binRecord);
		rectype(binRecord);
		infoOrData(binRecord);
		checksum();
		
	} // parseContent(...)
	
	
	/*
	 * Extracts and validates the record length field.
	 * A IhxException is thrown if the resulting payload in not equal to the length field of the record structure.
	 */
	protected void recordLength(final byte[] aBinRecord) throws IhxException {
		logger.trace("recordLength(): aContent = {}", aBinRecord);
		
		recordLength = aBinRecord[0];
		
		if(recordLength != aBinRecord.length - 5)
			throw new IhxException("Invalid record length in line " + super.recordNumber);
		
	} // recordLength(...)
	
	
	/*
	 * Verifies the the given input is a marked as IHX record.
	 * If it's not recognized as IHX record, an IhxException is thrown.
	 */
	protected void recordMark(final String aContent) throws IhxException {
		logger.trace("recordMark(): aContent = {}", aContent);

		if(! aContent.startsWith(RECORD_MARK)) 
			throw new IhxException("Invalid IHX record in line " + super.recordNumber);
		
	} // recordMark(...)


	/*
	 * Fetches the record type id from input record structure and translates it into the related enum type.
	 * 
	 * If the record type can't be recognized an IhxException is thrown.
	 */
	protected void rectype(final byte[] aBinRecord) throws IhxException {
		logger.trace("rectype(): aContent = {}", aBinRecord);
		
		recordType = IhxRecordType.toType(aBinRecord[3] & 0xFF);
		if(recordType == IhxRecordType.UNKNOWN) 
			throw new IhxException("Invalid record type in line " + super.recordNumber);
		
	} // rectype(...)


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "IhxRecord [recordLength=" + recordLength + ", recordType=" + recordType + ", chkeckSum=" + chkeckSum
				+ ", " + super.toString()
				+ "]";
	}
	

} // ssalc
