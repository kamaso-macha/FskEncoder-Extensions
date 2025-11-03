/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxReaderTest.java
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import extension.model.MemoryMap;
import extension.source.Reader;
import extension.source.ReaderException;
import source.ihx.IhxException;
import source.ihx.IhxLexer;
import source.ihx.IhxParser;
import source.ihx.x8.Ihx8MemoryRegionBuilder;
import source.ihx.x8.Ihx8Reader;

/**
 * Responsibilities:<br>
 * Verification of the correct behavior of the class IhxReader
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

class IhxReaderTest {

	private static Logger LOGGER = null;

	protected MemoryMap memoryMapMock = mock(MemoryMap.class);
	
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
	 * Test method for {@link source.ihx.x8.Ihx8Reader#IhxReader()}.
	 */
	@Test
	final void testIhxReader() {
		LOGGER.info("testIhxReader()");
		
		IllegalArgumentException iargthrown;
		
		iargthrown = assertThrows(IllegalArgumentException.class, () -> new Ihx8Reader(null));
		assertEquals("aMemoryMap can't be null",  iargthrown.getMessage());
		
		try(
			
			MockedConstruction<Ihx8MemoryRegionBuilder> mrbMockConstruction = Mockito.mockConstruction(Ihx8MemoryRegionBuilder.class);
			MockedConstruction<IhxLexer> lexerMockConstruction = Mockito.mockConstruction(IhxLexer.class);
			MockedConstruction<IhxParser> parserMockConstruction = Mockito.mockConstruction(IhxParser.class);
				
		) { // resource
			
			Reader cut = new Ihx8Reader(memoryMapMock);
			
			assertTrue(cut != null);
			assertEquals("Initialized.", cut.getOperationStatus());

			assertEquals(1, mrbMockConstruction.constructed().size());
			assertEquals(1, lexerMockConstruction.constructed().size());
			assertEquals(1, parserMockConstruction.constructed().size());
							
		} // yrt
		catch(IhxException e) {
			
			LOGGER.error("Unexpected exception caught.", e);
			fail("Unexpected exception caught.");
			
		}

	} // testIhxReader()


	/**
	 * Test method for {@link source.ihx.x8.Ihx8Reader#IhxReader()}.
	 */
	@Test
	final void testIhxReader_setupError() {
		LOGGER.info("testIhxReader_setupError()");
		
		IllegalArgumentException iargthrown;
		
		iargthrown = assertThrows(IllegalArgumentException.class, () -> new Ihx8Reader(null));
		assertEquals("aMemoryMap can't be null",  iargthrown.getMessage());
		

		// TODO: test coverage, Throw exception
		/*
		 * 	Figure out how to throw an exception at constructor call
		 */
		
//		try(

//			MockedConstruction<IhxLexer> lexerMockConstruction = Mockito.mockConstruction(IhxLexer.class,
//				withSettings().defaultAnswer(
//					new Answer<Exception>() {
//						@Override
//						public Exception answer(InvocationOnMock invocation) throws Throwable {
//							throw new Exception();
//						}
//					} // Answer()
//				) // defaultAnswer(...)
//			) // mockConstruction

						
//			@SuppressWarnings("unchecked")
//			MockedConstruction<IhxLexer> lexerMockConstruction = Mockito.mockConstruction(IhxLexer.class,
//				(mock, context) -> {
//					
//					LOGGER.info("testIhxReader_setupError(): lexerMock construction");
//					
////					throw new NoSuchMethodException("test");					
////					doThrow(NoSuchMethodException.class).when(mock);
//					
////					doThrow(NoSuchMethodException.class);
//					
//				} // ->
//			) // mockConstruction
//			;

//			MockedConstruction<IhxLexer> lexerMockConstruction = Mockito.mockConstructionWithAnswer(IhxLexer.class, 
//				invocation -> { 
//					LOGGER.info("invocation");
//					throw new Exception();
//				}
//			)
//		) { // resource

		
//		try {
//			LOGGER.info("testIhxReader_setupError() new IhxReader");
//			Reader cut = new IhxReader(memoryMapMock);
//			
//			LOGGER.info("testIhxReader_setupError() getOperationStatus()");
//			assertEquals("Unable to initialize IhxReader, details provided by getOperationStatus()", cut.getOperationStatus());
//							
//		} // yrt
//		catch(IhxException e) {
//			
//			LOGGER.error("Unexpected exception caught.", e);
//			fail("Unexpected exception caught.");
//			
//		}

	} // testIhxReader_setupError()


	/**
	 * Test method for {@link source.ihx.x8.Ihx8Reader#getOperationStatus()}.
	 */
	@Test
	final void testGetOperationStatus() {
		LOGGER.info("testGetOperationStatus()");
		
		/*
		 * 	Implicitly tested by all other tests
		 */
		
		assertTrue(true);
		
	} // testGetOperationStatus()


	/**
	 * Test method for {@link source.ihx.x8.Ihx8Reader#setFilename(java.lang.String)}.
	 */
	@Test
	final void testSetFilename() {
		LOGGER.info("testSetFilename()");
		
		try {
			IllegalArgumentException iargthrown;
			
			Reader cut = new Ihx8Reader(memoryMapMock);
	
			iargthrown = assertThrows(IllegalArgumentException.class, () -> cut.setFilename(null));
			assertEquals("aFileName can't be null",  iargthrown.getMessage());
			
			iargthrown = assertThrows(IllegalArgumentException.class, () -> cut.setFilename("  \t "));
			assertEquals("aFileName can't be blank",  iargthrown.getMessage());
			
		} // yrt
		catch(IhxException e) {
			
			LOGGER.error("Unexpected exception caught.", e);
			fail("Unexpected exception caught.");
		
		}
		
	} // testSetFilename()


	/**
	 * Test method for {@link source.ihx.x8.Ihx8Reader#loadFile()}.
	 */
	@Test
	final void testLoadFile() {
		LOGGER.info("testLoadFile()");

		final String SRC_FILE = "foo.bar";

		IllegalAccessError thrown;
		boolean result = false;
		
		try(
				
			MockedConstruction<IhxLexer> lexerMockConstruction = Mockito.mockConstruction(IhxLexer.class);
			MockedConstruction<IhxParser> parserMockConstruction = Mockito.mockConstruction(IhxParser.class);
				
		) { // resource
			
			Reader cut = new Ihx8Reader(memoryMapMock);

			IhxLexer  mockLexer  = lexerMockConstruction.constructed().get(0);
			IhxParser mockParser = parserMockConstruction.constructed().get(0);
			
			thrown = assertThrows(IllegalAccessError.class, () -> cut.loadFile());
			assertEquals("No source file set",  thrown.getMessage());
			
			cut.setFilename(SRC_FILE);			
			result = cut.loadFile();
			assertTrue(result);
			assertEquals("Successfuly loaded.", cut.getOperationStatus());
			
			try {
				
				verify(mockLexer, times(1)).setFile(SRC_FILE);
				verify(mockParser, times(1)).parse();
				
			} 
			catch (
				InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException
				| InvocationTargetException 
				| ReaderException 
				| IOException 
				e) {
				
				e.printStackTrace();
				
				fail("Unexpected exception caught!");
				
			}
			
		} // yrt
		catch(IhxException e) {
			
			LOGGER.error("Unexpected exception caught.", e);
			fail("Unexpected exception caught.");
		
		}
		
	} // testLoadFile()


	/**
	 * Test method for {@link source.ihx.x8.Ihx8Reader#loadFile()}.
	 */
	@Test
	final void testLoadFile_CantFindFileError() {
		LOGGER.info("testLoadFile_CantFindFileError()");

		final String SRC_FILE = "foo.bar";

		boolean result = false;
		
		try(
			
			MockedConstruction<IhxParser> parserMock = Mockito.mockConstruction(IhxParser.class,
				(mock, context) -> {
	
					doThrow(FileNotFoundException.class).when(mock).parse();
						
				} // ->
			) // mockConstruction
		) { // resource
			
			Reader cut = new Ihx8Reader(memoryMapMock);
			cut.setFilename(SRC_FILE);
			
			result = cut.loadFile();
			assertFalse(result);
			
			assertEquals("Can't find selected file 'foo.bar'.", cut.getOperationStatus());
			
		} // yrt
		catch(IhxException e) {
			
			LOGGER.error("Unexpected exception caught.", e);
			fail("Unexpected exception caught.");
		
		}
		
	} // testLoadFile_CantFindFileError()



	/**
	 * Test method for {@link source.ihx.x8.Ihx8Reader#loadFile()}.
	 */
	@Test
	final void testLoadFile_ReadFileError() {
		LOGGER.info("testLoadFile_ReadFileError()");

		final String SRC_FILE = "foo.bar";

		boolean result = false;
		
		try(
			
			MockedConstruction<IhxLexer> lexerMockConstruction = Mockito.mockConstruction(IhxLexer.class);
			
			MockedConstruction<IhxParser> parserMock = Mockito.mockConstruction(IhxParser.class,
				(mock, context) -> {
	
					doThrow(IOException.class).when(mock).parse();
						
				} // ->
			) // mockConstruction
		) { // resource
			
			Reader cut = new Ihx8Reader(memoryMapMock);
			cut.setFilename(SRC_FILE);
			
			result = cut.loadFile();
			assertFalse(result);
			
			assertEquals("Can't access selected file 'foo.bar'.", cut.getOperationStatus());
			
		} // yrt
		catch(IhxException e) {
			
			LOGGER.error("Unexpected exception caught.", e);
			fail("Unexpected exception caught.");
		
		}
		
	} // testLoadFile_ReadFileError()


	/**
	 * Test method for {@link source.ihx.x8.Ihx8Reader#loadFile()}.
	 */
	@Test
	final void testLoadFile_IhxFileError() {
		LOGGER.info("testLoadFile_IhxFileError()");

		final String SRC_FILE = "foo.bar";

		boolean result = false;
		
		try(
			
			MockedConstruction<IhxLexer> lexerMockConstruction = Mockito.mockConstruction(IhxLexer.class);
			
			MockedConstruction<IhxParser> parserMock = Mockito.mockConstruction(IhxParser.class,
				(mock, context) -> {
	
					doThrow(IhxException.class).when(mock).parse();
						
				} // ->
			) // mockConstruction
		) { // resource
			
			Reader cut = new Ihx8Reader(memoryMapMock);
			cut.setFilename(SRC_FILE);
			
			result = cut.loadFile();
			assertFalse(result);
			
			assertEquals("Invalid IHX file 'foo.bar', details: 'null'.", cut.getOperationStatus());
			
		} // yrt
		catch(IhxException e) {
			
			LOGGER.error("Unexpected exception caught.", e);
			fail("Unexpected exception caught.");
		
		}
		
	} // testLoadFile_IhxFileError()


	/**
	 * Test method for {@link source.ihx.x8.Ihx8Reader#loadFile()}.
	 */
	@Test
	final void testLoadFile_ReflectionError() {
		LOGGER.info("testLoadFile_ReflectionError()");

		final String SRC_FILE = "foo.bar";

		boolean result = false;
		
		try(
			
			MockedConstruction<IhxLexer> lexerMockConstruction = Mockito.mockConstruction(IhxLexer.class);
			
			@SuppressWarnings("unchecked")
			MockedConstruction<IhxParser> parserMock = Mockito.mockConstruction(IhxParser.class,
				(mock, context) -> {
	
					doThrow(
						InstantiationException.class,
						IllegalAccessException.class,
						IllegalArgumentException.class,
						InvocationTargetException.class
					).when(mock).parse();
						
				} // ->
			) // mockConstruction
		) { // resource
			
			Reader cut = new Ihx8Reader(memoryMapMock);
			cut.setFilename(SRC_FILE);
			
			result = cut.loadFile();
			assertFalse(result);
			assertEquals("Internal processing error, 'java.lang.InstantiationException: null'.", cut.getOperationStatus());
			
			result = cut.loadFile();
			assertFalse(result);
			assertEquals("Internal processing error, 'java.lang.IllegalAccessException: null'.", cut.getOperationStatus());
			
			result = cut.loadFile();
			assertFalse(result);
			assertEquals("Internal processing error, 'java.lang.IllegalArgumentException: null'.", cut.getOperationStatus());
			
			result = cut.loadFile();
			assertFalse(result);
			assertEquals("Internal processing error, 'java.lang.reflect.InvocationTargetException: null'.", cut.getOperationStatus());
			
		} // yrt
		catch(IhxException e) {
			
			LOGGER.error("Unexpected exception caught.", e);
			fail("Unexpected exception caught.");
		
		}
		
	} // testLoadFile_ReflectionError()


	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		final String SRC_FILE = "foo.bar";

		try {
			
			Reader cut = new Ihx8Reader(memoryMapMock);
			cut.setFilename(SRC_FILE);
			LOGGER.info("toString(): {}", cut.toString());		
			
			assertTrue(cut.toString().startsWith(
				"IhxReader [lexer=Lexer [t=null, constructor=public source.ihx.IhxRecord(int,java.lang.String) " 
					+ "throws source.ihx.IhxException, reader=null, recordIndex=0], " 
				+ "parser=IhxParser [lexer=Lexer [t=null, constructor=public source.ihx.IhxRecord(int,java.lang.String) " 
					+ "throws source.ihx.IhxException, reader=null, recordIndex=0], " 
				+ "memoryRegionBuilder=IhxMemoryRegionBuilder [eofRecord=false, MemoryRegionBuilder " 
					+ "[startAddress=0x0000, memoryRegion=null, memoryMap=Mock for MemoryMap, hashCode: "));
			
			assertTrue(cut.toString().contains("]]], Reader [memoryMap=Mock for MemoryMap, hashCode: "));
			assertTrue(cut.toString().endsWith(", sourceFileName=foo.bar, operationStatus=Initialized.]]"));
	
		} // yrt
		catch(IhxException e) {
			
			LOGGER.error("Unexpected exception caught.", e);
			fail("Unexpected exception caught.");
		
		}
	
	} // testToString()
	
	
} // ssalc
