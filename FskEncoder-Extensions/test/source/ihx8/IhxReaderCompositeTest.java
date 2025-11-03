/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : IhxCompositeTest.java
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

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.model.MemoryBlockDescription;
import extension.model.MemoryMap;
import extension.model.MemoryRegion;
import extension.source.Reader;
import source.ihx.IhxException;
import source.ihx.x8.Ihx8Reader;

/**
 * Responsibilities:<br>
 * Verify the correct interworking of collaborators.
 * Assures an end-to-end functionality.
 * 
 * <p>
 * Collaborators:<br>
 * <pre>
 * 	IhxReader, 
 * 	IhxParser,
 * 	IhxLexer,
 * 	IhxMemoryRegionBuilder,
 * 	MemoryMap and
 * 	MemoryRegion
 * </pre>
 * <p>
 * Description:<br>
 * There are three source files available in folder ./testresources.<br>
 * Each of them is processed by a call to IhxReader.loadFile().<br>
 * After processing the result is obtained from MemoryMap and verifies against predefined reference data.
 * 
 * <p>
 * @author Stefan
 *
 */

class IhxReaderCompositeTest {

	private static Logger LOGGER = null;

	protected static final String PATH = "./testresources/";

	/*
	 * Reference data.
	 * Each BLOCK_x has a corrospinding source file in the folder ./testresources/
	 */
	protected static final byte[] BLOCK_1 = new byte[] {
		(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07,
		(byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17,
		(byte) 0x20, (byte) 0x21, (byte) 0x22, (byte) 0x23, (byte) 0x24, (byte) 0x25, (byte) 0x26, (byte) 0x27,
		(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37,
	}; // block_1
	
	protected static final byte[] BLOCK_2 = new byte[] {
		(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, 
		(byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F,
		                                                                                                     
		(byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, 
		(byte) 0x18, (byte) 0x19, (byte) 0x1A, (byte) 0x1B, (byte) 0x1C, (byte) 0x1D, (byte) 0x1E, (byte) 0x1F,
		                                                                                                     
		(byte) 0x20, (byte) 0x21, (byte) 0x22, (byte) 0x23, (byte) 0x24, (byte) 0x25, (byte) 0x26, (byte) 0x27, 
		(byte) 0x28, (byte) 0x29, (byte) 0x2A, (byte) 0x2B, (byte) 0x2C, (byte) 0x2D, (byte) 0x2E, (byte) 0x2F,
		                                                                                                    
		(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, 
		(byte) 0x38, (byte) 0x39, (byte) 0x3A, (byte) 0x3B, (byte) 0x3C, (byte) 0x3D, (byte) 0x3E, (byte) 0x3F,
	}; // block_2
	
	protected static final byte[] BLOCK_3 = new byte[] {
		(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, 
		(byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F, 
		(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, 
		(byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F,
		                                                                                                     
		(byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, 
		(byte) 0x18, (byte) 0x19, (byte) 0x1A, (byte) 0x1B, (byte) 0x1C, (byte) 0x1D, (byte) 0x1E, (byte) 0x1F, 
		(byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, 
		(byte) 0x18, (byte) 0x19, (byte) 0x1A, (byte) 0x1B, (byte) 0x1C, (byte) 0x1D, (byte) 0x1E, (byte) 0x1F,
		                                                                                                     
		(byte) 0x20, (byte) 0x21, (byte) 0x22, (byte) 0x23, (byte) 0x24, (byte) 0x25, (byte) 0x26, (byte) 0x27, 
		(byte) 0x28, (byte) 0x29, (byte) 0x2A, (byte) 0x2B, (byte) 0x2C, (byte) 0x2D, (byte) 0x2E, (byte) 0x2F, 
		(byte) 0x20, (byte) 0x21, (byte) 0x22, (byte) 0x23, (byte) 0x24, (byte) 0x25, (byte) 0x26, (byte) 0x27, 
		(byte) 0x28, (byte) 0x29, (byte) 0x2A, (byte) 0x2B, (byte) 0x2C, (byte) 0x2D, (byte) 0x2E, (byte) 0x2F,
		                                                                                                     
		(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, 
		(byte) 0x38, (byte) 0x39, (byte) 0x3A, (byte) 0x3B, (byte) 0x3C, (byte) 0x3D, (byte) 0x3E, (byte) 0x3F, 
		(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, 
		(byte) 0x38, (byte) 0x39, (byte) 0x3A, (byte) 0x3B, (byte) 0x3C, (byte) 0x3D, (byte) 0x3E, (byte) 0x3F,
	}; // block_3
	
	protected byte[] reference;

	protected MemoryMap memoryMap;
	protected List<MemoryBlockDescription> memoryLayout;
	protected MemoryBlockDescription memoryBlockDescription;
	protected MemoryRegion memoryRegion;
	
	protected Reader ihxReader;
	protected ByteBuffer result;
	protected String srcFileName;
	
	
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
	 * 
	 */
	@BeforeEach
	void setUp() {

		try {
			
			memoryMap = new MemoryMap();
			ihxReader = new Ihx8Reader(memoryMap);
			
			assertEquals("Initialized.", ihxReader.getOperationStatus());
			
		} catch (IhxException e) { fail("Unexpected exception caught:" + e); }
		
	} // setUp()()

	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	
	@Test
	final void test_1block() {
		LOGGER.info("test_1block()");
		
		srcFileName = PATH + "1block.hex";
		ihxReader.setFilename(srcFileName);
		ihxReader.loadFile();

		assertEquals("Successfuly loaded.", ihxReader.getOperationStatus());
		assertEquals(1, memoryMap.getRegionCount());
		
		memoryLayout = memoryMap.getMemoryLayout();
		
		assertEquals(1, memoryLayout.size());
		
		memoryBlockDescription = memoryLayout.get(0);
		
		assertEquals(0x00000, memoryBlockDescription.START_ADDRESS);
		assertEquals(0x0001F, memoryBlockDescription.END_ADDRESS);
		assertEquals(0x00020, memoryBlockDescription.SIZE);

		memoryRegion = memoryMap.getMemoryRegion(memoryBlockDescription.START_ADDRESS);
		reference    = BLOCK_1;
		
		verifyMemoryRegion();
			
		
	} // test_1block()


	@Test
	final void test_2blocks() {
		LOGGER.info("()");
		
		srcFileName = PATH + "2block.hex";
		ihxReader.setFilename(srcFileName);
		ihxReader.loadFile();

		assertEquals("Successfuly loaded.", ihxReader.getOperationStatus());
		assertEquals(2, memoryMap.getRegionCount());
		
		memoryLayout = memoryMap.getMemoryLayout();
		
		assertEquals(2, memoryLayout.size());
		
		memoryBlockDescription = memoryLayout.get(0);
		
		assertEquals(0x00000, memoryBlockDescription.START_ADDRESS);
		assertEquals(0x0001F, memoryBlockDescription.END_ADDRESS);
		assertEquals(0x00020, memoryBlockDescription.SIZE);

		memoryRegion = memoryMap.getMemoryRegion(memoryBlockDescription.START_ADDRESS);
		reference    = BLOCK_1;
		
		verifyMemoryRegion();
		
		memoryBlockDescription = memoryLayout.get(1);
		
		assertEquals(0x01000, memoryBlockDescription.START_ADDRESS);
		assertEquals(0x0103F, memoryBlockDescription.END_ADDRESS);
		assertEquals(0x00040, memoryBlockDescription.SIZE);

		memoryRegion = memoryMap.getMemoryRegion(memoryBlockDescription.START_ADDRESS);
		reference    = BLOCK_2;
		
		verifyMemoryRegion();
		
	} // ()


	@Test
	final void test_3blocks() {
		LOGGER.info("()");
		
		srcFileName = PATH + "3block.hex";
		ihxReader.setFilename(srcFileName);
		ihxReader.loadFile();

		assertEquals("Successfuly loaded.", ihxReader.getOperationStatus());
		assertEquals(3, memoryMap.getRegionCount());
		
		memoryLayout = memoryMap.getMemoryLayout();
		
		assertEquals(3, memoryLayout.size());
		
		memoryBlockDescription = memoryLayout.get(0);
		
		assertEquals(0x00000, memoryBlockDescription.START_ADDRESS);
		assertEquals(0x0001F, memoryBlockDescription.END_ADDRESS);
		assertEquals(0x00020, memoryBlockDescription.SIZE);

		memoryRegion = memoryMap.getMemoryRegion(memoryBlockDescription.START_ADDRESS);
		reference    = BLOCK_1;
		
		verifyMemoryRegion();
		
		memoryBlockDescription = memoryLayout.get(1);
		
		assertEquals(0x01000, memoryBlockDescription.START_ADDRESS);
		assertEquals(0x0103F, memoryBlockDescription.END_ADDRESS);
		assertEquals(0x00040, memoryBlockDescription.SIZE);

		memoryRegion = memoryMap.getMemoryRegion(memoryBlockDescription.START_ADDRESS);
		reference    = BLOCK_2;
		
		verifyMemoryRegion();
		
		memoryBlockDescription = memoryLayout.get(2);
		
		assertEquals(0x02000, memoryBlockDescription.START_ADDRESS);
		assertEquals(0x0207F, memoryBlockDescription.END_ADDRESS);
		assertEquals(0x00080, memoryBlockDescription.SIZE);

		memoryRegion = memoryMap.getMemoryRegion(memoryBlockDescription.START_ADDRESS);
		reference    = BLOCK_3;
		
		verifyMemoryRegion();
		
	} // ()


	/**
	 * 
	 */
	protected void verifyMemoryRegion() {
		LOGGER.info("verifyMemoryRegion()");

		result = memoryRegion.getContent();
		assertTrue(result != null);
		assertTrue(memoryRegion != null);

		LOGGER.info("memoryBlockDescription {}", memoryBlockDescription);
		LOGGER.info("memoryRegion           {}", memoryRegion);
		LOGGER.info("result                 {}", result);
		LOGGER.info("reference.length       {}", reference.length);

		assertEquals(memoryBlockDescription.START_ADDRESS, memoryRegion.getStartAddress());
		assertEquals(memoryBlockDescription.END_ADDRESS, memoryRegion.getEndAddress());
		assertEquals(memoryBlockDescription.SIZE, memoryRegion.getSize());
		
		assertEquals(memoryBlockDescription.SIZE, result.capacity());
		assertEquals(reference.length, result.capacity());
		
		for(int n = 0; n < reference.length; n++) {
			
			assertEquals(reference[n], result.get(n));
			
		} // rof
		
	} // verifyMemoryRegion()


} // ssalc
