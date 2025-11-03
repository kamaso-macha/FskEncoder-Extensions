/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxRecordTest.java
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

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import source.ihx.IhxException;
import source.ihx.IhxRecord;
import source.ihx.IhxRecordType;

/**
 * Responsibilities:<br>
 * Verification of the correct behavior of the class IhxRecord
 * 
 * <p>
 * Collaborators:<br>
 * Class under test.
 * 
 * <p>
 * Description:<br>
 * Unit test for the class under test.
 * 
 * <p>
 * @author Stefan
 *
 */

class IhxRecordTest {

	private static Logger LOGGER = null;

	private static final int RECORD_NBR = 42;
	
	private static final byte			RECORD_LENGHT	= (byte)0x020;
	private static final int			LOAD_OFFSET		= 0x6000;
	private static final IhxRecordType	RECORD_TYPE		= IhxRecordType.DATA;
	private static final byte[] 		RECORD_DATA		= { (byte) 0x0CD, (byte) 0x067, (byte) 0x060, (byte) 0x0CD, 
															(byte) 0x073, (byte) 0x060, (byte) 0x0CD, (byte) 0x009, 
															(byte) 0x063, (byte) 0x0CD, (byte) 0x000, (byte) 0x063, 
															(byte) 0x0CD, (byte) 0x01B, (byte) 0x063, (byte) 0x0CD, 
															(byte) 0x08F, (byte) 0x061, (byte) 0x018, (byte) 0x03C, 
															(byte) 0x000, (byte) 0x000, (byte) 0x000, (byte) 0x000, 
															(byte) 0x000, (byte) 0x000, (byte) 0x000, (byte) 0x000, 
															(byte) 0x000, (byte) 0x000, (byte) 0x000, (byte) 0x000
														  };
	
	private static final String IHX_RECORD			= ":20600000CD6760CD7360CD0963CD0063CD1B63CD8F61183C00000000000000000000000087"; 
	private static final String IHX_ERROR_1_MARK	= " 20600000CD6760CD7360CD0963CD0063CD1B63CD8F61183C00000000000000000000000087"; 
	private static final String IHX_ERROR_2_LENG	= ":bb600000CD6760CD7360CD0963CD0063CD1B63CD8F61183C00000000000000000000000087"; 
	private static final String IHX_ERROR_4_TYPE	= ":206000eeCD6760CD7360CD0963CD0063CD1B63CD8F61183C00000000000000000000000087"; 
	private static final String IHX_ERROR_5_CKSM	= ":20600000CD6760CD7360CD0963CD0063CD1B63CD8F61183C000000000000000000000000aa"; 
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.setProperty("log4j.configurationFile","./test-cfg/log4j2.xml");
		LOGGER = LogManager.getLogger();
	}


	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

		
	/**
	 * Test method for {@link source.ihx.IhxRecord#IhxRecord()}.
	 */
	@Test
	final void testIhxRecordIntString() {
		LOGGER.info("testIhxRecordIntString()");
		
		IhxException thrown;
		
		thrown = assertThrows(IhxException.class, () -> new IhxRecord(RECORD_NBR, IHX_ERROR_1_MARK));
		assertTrue(thrown.getMessage().startsWith("Invalid IHX record in line"));
		
		thrown = assertThrows(IhxException.class, () -> new IhxRecord(RECORD_NBR, IHX_ERROR_2_LENG));
		assertTrue(thrown.getMessage().startsWith("Invalid record length in line"));
		
		thrown = assertThrows(IhxException.class, () -> new IhxRecord(RECORD_NBR, IHX_ERROR_4_TYPE));
		assertTrue(thrown.getMessage().startsWith("Invalid record type in line"));
		
		thrown = assertThrows(IhxException.class, () -> new IhxRecord(RECORD_NBR, IHX_ERROR_5_CKSM));
		assertTrue(thrown.getMessage().startsWith("Invalid checksum in line"));
		
		assertDoesNotThrow(() -> new IhxRecord(RECORD_NBR, IHX_RECORD));
		
	} // testIhxRecordIntString()


	/**
	 * Test method for {@link source.ihx.IhxRecord#getInfoOrData()}.
	 */
	@Test
	final void testGetInfoOrData() {
		LOGGER.info("testGetInfoOrData()");
		
		try {
			
			IhxRecord cut = new IhxRecord(RECORD_NBR, IHX_RECORD);
			byte[] result = null;
			
			result = cut.getData();
			assertTrue(result != null);
			
			for(int n = 0; n < RECORD_DATA.length; n++) {
				assertTrue(result[n] == RECORD_DATA[n]);
			}
			
		} 
		catch (IhxException e) { LOGGER.error("unexpected exception: ", e); }
		
	} // testGetInfoOrData()


	/**
	 * Test method for {@link source.ihx.IhxRecord#getOffset()}.
	 */
	@Test
	final void testGetOffset() {
		LOGGER.info("testGetOffset()");
		
		try {
			
			IhxRecord cut = new IhxRecord(RECORD_NBR, IHX_RECORD);
			
			assertTrue(cut.getOffset() == LOAD_OFFSET);
			
		} 
		catch (IhxException e) { LOGGER.error("unexpected exception: ", e); }
		
	} // testGetOffset()


	/**
	 * Test method for {@link source.ihx.IhxRecord#getRecordLength()}.
	 */
	@Test
	final void testGetRecordLength() {
		LOGGER.info("testGetRecordLength()");
		
		try {
			
			IhxRecord cut = new IhxRecord(RECORD_NBR, IHX_RECORD);
			
			assertTrue(cut.getRecordLength() == RECORD_LENGHT);
			
		} 
		catch (IhxException e) { LOGGER.error("unexpected exception: ", e); }
		
	} // testGetRecordLength()


	/**
	 * Test method for {@link source.ihx.IhxRecord#getRecordType()}.
	 */
	@Test
	final void testGetRecordType() {
		LOGGER.info("testGetRecordType()");
		
		try {
			
			IhxRecord cut = new IhxRecord(RECORD_NBR, IHX_RECORD);
			
			assertTrue(cut.getRecordType() == RECORD_TYPE);
			
		} 
		catch (IhxException e) { LOGGER.error("unexpected exception: ", e); }
		
	} // testGetRecordType()


	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		try {
			
			IhxRecord cut = new IhxRecord(RECORD_NBR, ":02001800303185");
			LOGGER.info("toString(): {}", cut.toString());
			assertEquals(
				"IhxRecord [recordLength=2, recordType=DATA, chkeckSum=0, Record [recordNumber=42, offset=24, dataBuffer=[48, 49]]]",
				cut.toString())
				;
	
		} catch (IhxException e) {
			e.printStackTrace();
			fail("Unexpected exception caught");
		}
	
	} // testToString()
	
	
} // ssalc
