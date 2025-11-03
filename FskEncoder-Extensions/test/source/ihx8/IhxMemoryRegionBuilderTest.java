/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxMemoryRegionBuilderTest.java
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import extension.model.ExtendableMemory;
import extension.model.MemoryRegion;
import source.ihx.IhxException;
import source.ihx.IhxRecord;
import source.ihx.IhxRecordType;
import source.ihx.x8.Ihx8MemoryRegionBuilder;

/**
 * Responsibilities:<br>
 * Verification of the correct behavior of the class xxx
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

class IhxMemoryRegionBuilderTest {

	private static Logger LOGGER = null;

	IhxRecord dataRecordMock = mock(IhxRecord.class);
	IhxRecord  eofRecordMock  = mock(IhxRecord.class);
	
	
	/*
	 * Test implementation of abstract class IhxMemoryRegionBuilder
	 */
	class TestIhxMemoryRegionBuilder extends Ihx8MemoryRegionBuilder {

		public TestIhxMemoryRegionBuilder(ExtendableMemory aMemoryMap) { super(aMemoryMap); }
		
	} // ssalc
	
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
	 * Test method for {@link source.ihx.x8.Ihx8MemoryRegionBuilder#IhxMemoryRegionBuilder(extension.model.ExtendableMemory)}.
	 */
	@Test
	final void testIhxMemoryRegionBuilder() {
		LOGGER.info("testIhxMemoryRegionBuilder()");
		
		IllegalArgumentException thrown;
		
		thrown = assertThrows(IllegalArgumentException.class, () -> new Ihx8MemoryRegionBuilder(null));
		assertTrue(thrown.getMessage().equals("aMemoryMap can't be null"));
		
		ExtendableMemory mmMock = mock(ExtendableMemory.class);
		assertDoesNotThrow(() -> new Ihx8MemoryRegionBuilder(mmMock));
		
	} // testIhxMemoryRegionBuilder()

	
	/**
	 * Test method for {@link source.ihx.x8.Ihx8MemoryRegionBuilder#feed(source.ihx8.IhxDataRecord)}.
	 */
	@Test
	final void testAppendIhxDataRecord() {
		LOGGER.info("testAppendIhxDataRecord()");
		
		final long START_ADDRESS = 0x1000;
		
		IhxException thrown;
		
		ExtendableMemory mmMock = mock(ExtendableMemory.class);
		
		doReturn(START_ADDRESS, START_ADDRESS + 15 + 1)
			.when(dataRecordMock).getOffset();
		
		doReturn(IhxRecordType.DATA).when(dataRecordMock).getRecordType();
		
		doReturn(IhxRecordType.EOF).when(eofRecordMock).getRecordType();
				
		try( 
			MockedConstruction<MemoryRegion> memoryRegionMock = Mockito.mockConstruction(MemoryRegion.class,
				(mock, context) -> {
				
					long startAddress = (long) context.arguments().get(0);
					assertTrue(startAddress == START_ADDRESS);

					doReturn(START_ADDRESS + 15)
						.when(mock).getEndAddress();

				} // ->
			) // mockedConstruction(...)
		) {
			
			Ihx8MemoryRegionBuilder cut = new Ihx8MemoryRegionBuilder(mmMock);
			cut.append(dataRecordMock);
			
			verify(dataRecordMock, times(1)).getOffset();
			verify(dataRecordMock, times(1)).getData();
			
			cut.append(dataRecordMock);

			verify(dataRecordMock, times(2)).getOffset();
			verify(dataRecordMock, times(2)).getData();
			
			assertDoesNotThrow(() -> cut.append(eofRecordMock));
			
			thrown = assertThrows(IhxException.class, () -> cut.append(dataRecordMock));
			assertEquals("Invalid file structure: DATA record after EOF record", thrown.getMessage());
			
		} // yrt
		catch (IhxException e) {
			
			e.printStackTrace();
			
		} // hctac
		
		
	} // testAppendIhxDataRecord()

	
	/**
	 * Test method for {@link source.ihx.x8.Ihx8MemoryRegionBuilder#feed(source.ihx8.IhxEofRecord)}.
	 */
	@Test
	final void testAppendIhxEofRecord() {
		LOGGER.info("testAppendIhxEofRecord()");
		
		/*
		 * 	Implicitly tested by testFeedIhxDataRecord()
		 */
		
		assertTrue(true);
		
	} // testAppendIhxEofRecord()

	
	/**
	 * Test method for {@link source.ihx.x8.Ihx8MemoryRegionBuilder#feed(source.ihx8.IhxEofRecord)}.
	 */
	@Test
	final void testAppendInvalidIhxRecord() {
		LOGGER.info("testAppendInvalidIhxRecord()");
		
		IhxException thrown;
		
		ExtendableMemory mmMock = mock(ExtendableMemory.class);
		IhxRecord  invalidRecordMock  = mock(IhxRecord.class);
		doReturn(IhxRecordType.EXT_SEG).when(invalidRecordMock).getRecordType();
		
		Ihx8MemoryRegionBuilder cut = new Ihx8MemoryRegionBuilder(mmMock);

		thrown = assertThrows(IhxException.class, () -> cut.append(invalidRecordMock));
		assertEquals("unknown recordType " + IhxRecordType.EXT_SEG, thrown.getMessage());
		
	} // testAppendInvalidIhxRecord()

	
	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link extension.source.MemoryRegionBuilder#toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		ExtendableMemory mmMock = mock(ExtendableMemory.class);
		Ihx8MemoryRegionBuilder cut = new Ihx8MemoryRegionBuilder(mmMock);
		LOGGER.info("toString(): {}", cut.toString());

		assertTrue(cut.toString().startsWith("IhxMemoryRegionBuilder [eofRecord=false, " 
			+ "MemoryRegionBuilder [startAddress=0x0000, memoryRegion=null, " 
			+ "memoryMap=Mock for ExtendableMemory, hashCode: "));
	
	} // testToString()
	
	
} // ssalc
