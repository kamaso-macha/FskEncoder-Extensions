/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxParserTest.java
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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import extension.source.ReaderException;
import source.ihx.IhxLexer;
import source.ihx.IhxParser;
import source.ihx.IhxRecord;
import source.ihx.x8.Ihx8MemoryRegionBuilder;

/**
 * Responsibilities:<br>
 * Verification of the correct behavior of the class IhxParser
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

class IhxParserTest {

	private static Logger LOGGER = null;

	protected IhxLexer lexerMock = mock(IhxLexer.class);
	protected Ihx8MemoryRegionBuilder memoryRegionBuilderMock 
				= mock(Ihx8MemoryRegionBuilder.class);
	
	protected IhxRecord recordMock = mock(IhxRecord.class);
	
	
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
	 * Test method for {@link source.ihx.IhxParser#IhxParser(source.ihx.IhxLexer, source.ihx8.Ihx8MemoryRegionBuilder)}.
	 */
	@Test
	final void testIhxParser() {
		LOGGER.info("testIhxParser()");
		
		IllegalArgumentException thrown;
		
		thrown = assertThrows(IllegalArgumentException.class, () -> new IhxParser(null, null));
		assertEquals("aLexer can't be null", thrown.getMessage());
		
		thrown = assertThrows(IllegalArgumentException.class, () -> new IhxParser(lexerMock, null));
		assertEquals("aMemoryRegionBuilder can't be null", thrown.getMessage());
		
		assertDoesNotThrow(() -> new IhxParser(lexerMock, memoryRegionBuilderMock));
		
	} // testIhxParser()


	/**
	 * Test method for {@link source.ihx.IhxParser#parse()}.
	 */
	@Test
	final void testParse() {
		LOGGER.info("testParse()");
		
		try {
			
			InOrder inOrder = inOrder(memoryRegionBuilderMock);
			
			IhxRecord nullRecord = null;
			
			when(lexerMock.getRecord()).thenReturn(recordMock, nullRecord);
			
			IhxParser cut = new IhxParser(lexerMock, memoryRegionBuilderMock);

			cut.parse();
			
			inOrder.verify(memoryRegionBuilderMock, times(1)).append(recordMock);
			inOrder.verify(memoryRegionBuilderMock, never()).append(null);
		
		} // yrt
		
		catch (
				ReaderException
				| InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException
				| InvocationTargetException
				| IOException 
				e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught");
			
		} // hctac
		

	} // testParse()


	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		IhxParser cut = new IhxParser(lexerMock, memoryRegionBuilderMock);
		LOGGER.info("toString(): {}", cut.toString());

		assertTrue(cut.toString().startsWith("IhxParser [lexer=Mock for IhxLexer, hashCode: "));
		assertTrue(cut.toString().contains(", memoryRegionBuilder=Mock for Ihx8MemoryRegionBuilder, hashCode: "));
		assertTrue(cut.toString().endsWith("]"));
	
	} // testToString()
	
	
} // ssalc
