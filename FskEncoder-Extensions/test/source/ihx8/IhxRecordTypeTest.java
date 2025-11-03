/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxRecordTypeTest.java
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


package source.ihx8;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import source.ihx.IhxRecordType;

/**
 * Responsibilities:<br>
 * Verification of the correct behavior of the class IhxRecordType
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

class IhxRecordTypeTest {

	private static Logger LOGGER = null;

	
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

	
	@Test
	final void testOrdinalValues() {
		LOGGER.info("testOrdinalValues()");
		
		assertTrue(IhxRecordType.values().length == 7);
		
		assertTrue(IhxRecordType.DATA.ordinal()			== 0);
		assertTrue(IhxRecordType.EOF.ordinal()			== 1);
		assertTrue(IhxRecordType.EXT_SEG.ordinal()		== 2);
		assertTrue(IhxRecordType.START_SEG.ordinal()	== 3);
		assertTrue(IhxRecordType.EXT_LIN.ordinal()		== 4);
		assertTrue(IhxRecordType.START_LIN.ordinal()	== 5);
		assertTrue(IhxRecordType.UNKNOWN.ordinal()		== 6);
		
	} // testOrdinalValues()
	
	
	/**
	 * Test method for {@link source.ihx.IhxRecordType#toId()}.
	 */
	@Test
	final void testToId() {
		LOGGER.info("testToId()");
		
		assertTrue(IhxRecordType.DATA.toId()		==   0);
		assertTrue(IhxRecordType.EOF.toId()			==   1);
		assertTrue(IhxRecordType.EXT_SEG.toId()		==   2);
		assertTrue(IhxRecordType.START_SEG.toId()	==   3);
		assertTrue(IhxRecordType.EXT_LIN.toId()		==   4);
		assertTrue(IhxRecordType.START_LIN.toId()	==   5);
		assertTrue(IhxRecordType.UNKNOWN.toId()		== 255);
		
	} // testToId()
	

	/**
	 * Test method for {@link source.ihx.IhxRecordType#toType(int)}.
	 */
	@Test
	final void testToType() {
		LOGGER.info("testToType()");
		
		assertTrue(IhxRecordType.toType(  0) == IhxRecordType.DATA);
		assertTrue(IhxRecordType.toType(  1) == IhxRecordType.EOF);
		assertTrue(IhxRecordType.toType(  2) == IhxRecordType.EXT_SEG);
		assertTrue(IhxRecordType.toType(  3) == IhxRecordType.START_SEG);
		assertTrue(IhxRecordType.toType(  4) == IhxRecordType.EXT_LIN);
		assertTrue(IhxRecordType.toType(  5) == IhxRecordType.START_LIN);
		assertTrue(IhxRecordType.toType(255) == IhxRecordType.UNKNOWN);
		
	} // testToType()
	

} // ssalc
