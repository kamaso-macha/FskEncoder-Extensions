/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxLexerTest.java
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import source.ihx.IhxLexer;
import source.ihx.IhxRecord;
import source.ihx.IhxRecordType;

/**
 * Responsibilities:<br>
 * Verification of the correct behavior of the class IhxLexer
 * 
 * <p>
 * Collaborators:<br>
 * Calss under test.
 * 
 * <p>
 * Description:<br>
 * Unit test for the class under test.
 * 
 * <p>
 * @author Stefan
 *
 */

class IhxLexerTest {

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

	/**
	 * Test method for {@link source.ihx.IhxLexer#IhxLexer()}.
	 */
	@Test
	final void testIhxLexer() {
		LOGGER.info("testIhxLexer()");
		
		/*
		 * 	Implicitly tested
		 */
		
		assertTrue(true);

	} // testIhxLexer()

	/**
	 * Test method for {@link extension.source.Lexer#setFile(java.lang.String)}.
	 */
	@Test
	final void testSetFile() {
		LOGGER.info("testSetFile()");
		
		IllegalArgumentException illegalArgument;
		FileNotFoundException fileNotFound;
		
		assertDoesNotThrow(() -> new IhxLexer());
		
		try {
			
			final IhxLexer cut = new IhxLexer();
			
			illegalArgument = assertThrows(IllegalArgumentException.class, () -> cut.setFile(null));
			assertTrue(illegalArgument.getMessage().equalsIgnoreCase("aFile cant be null!"));
			
			illegalArgument = assertThrows(IllegalArgumentException.class, () -> cut.setFile(""));
			assertTrue(illegalArgument.getMessage().equalsIgnoreCase("aFile cant be blank nor empty!"));

			illegalArgument = assertThrows(IllegalArgumentException.class, () -> cut.setFile(" \t "));
			assertTrue(illegalArgument.getMessage().equalsIgnoreCase("aFile cant be blank nor empty!"));
			
			fileNotFound = assertThrows(FileNotFoundException.class, () -> cut.setFile("foo/bar.baz"));
			assertTrue(fileNotFound.getMessage().startsWith("foo"));
			
			assertDoesNotThrow(() -> cut.setFile("./testresources/Sample.ihx.hex"));
			
		} 
		catch (SecurityException | NoSuchMethodException e) {
			fail("unexpected exception: " + e);
		}
		
	} // testSetFile()

	
	/**
	 * Test method for {@link extension.source.Lexer#getRecord()}.
	 */
	@Test
	final void testGetRecord() {
		LOGGER.info("testGetRecord()");
		
		List<IhxRecord> result = new ArrayList<>();
		IhxRecord record = null;
		
		try {
		
			IhxLexer cut = new IhxLexer();
			cut.setFile("./testresources/Sample.ihx.hex");
			
			while((record = cut.getRecord()) != null) {
				result.add(record);
			}
			
			int n = 0;
			
			for(; n < result.size() - 1; n++) {
				assertTrue(result.get(n).getRecordType() == IhxRecordType.DATA);
			}
			
			record = result.get(n);
			assertTrue(record.getRecordType() == IhxRecordType.EOF);
			assertTrue(record.getRecordNumber() == 28);

		
		} catch (SecurityException | 
				InstantiationException | IllegalAccessException | IllegalArgumentException | 
				InvocationTargetException | IOException | NoSuchMethodException e) {

			fail("unexpected exception: " + e);
			
		} // hctac
		
		
	} // testGetRecord()
	

} // ssalc
