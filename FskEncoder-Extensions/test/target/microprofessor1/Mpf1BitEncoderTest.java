/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Mpf1BitEncoderTest.java
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

package target.microprofessor1;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.encoder.BitEncoder;
import extension.encoder.WaveCycleEncoder;

/**
 * Responsibilities:<br>
 * Test the  class.
 * 
 * 
 * <p>
 * Collaborators:<br>
 * Class under test.
 * 
 * 
 * <p>
 * Description:<br>
 * Performs all tests required to guarantee the correct function of CUT
 * 
 * <p>
 * @author Stefan
 *
 */

class Mpf1BitEncoderTest {

	private static Logger LOGGER = null;
	
	protected WaveCycleEncoder waveCycleEncoder = new WaveCycleEncoder(Mpf1BitEncoder.SAMPLING_RATE);

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
	 * Test method for {@link encoder.BitEncoder#encode(int, int)}.
	 */
	@Test
	final void testEncode0Bit() {
		LOGGER.info("testEncode0Bit()");

		BitEncoder cut = new Mpf1BitEncoder(waveCycleEncoder);
		
		ByteBuffer result = cut.encode(0, 1);
		byte[] ref = new byte[] { 
				0, 127, 0, -127,
				0, 127, 0, -127,
				0, 127, 0, -127,
				0, 127, 0, -127,
				0, 127, 0, -127,
				0, 127, 0, -127,
				0, 127, 0, -127,
				0, 127, 0, -127,
				0, 89, 127, 89, 0, -89, -127, -89, 
				0, 89, 127, 89, 0, -89, -127, -89, 
				};
		
		LOGGER.debug("pos: " + result.limit());
		
		for(int n = 0; n < result.limit(); n++) {
			LOGGER.debug("n: " + n + " - " + result.get(n));
			assertTrue(result.get(n) == ref[n]);
		}
		
	} // testEncode0Bit


	/**
	 * Test method for {@link encoder.BitEncoder#encode(int, int)}.
	 */
	@Test
	final void testEncode1Bit() {
		LOGGER.info("testEncode1Bit()");

		BitEncoder cut = new Mpf1BitEncoder(waveCycleEncoder);
		
		ByteBuffer result = cut.encode(1, 1);
		byte[] ref = new byte[] { 
				0, 127, 0, -127,
				0, 127, 0, -127,
				0, 127, 0, -127,
				0, 127, 0, -127,
				0, 89, 127, 89, 0, -89, -127, -89, 
				0, 89, 127, 89, 0, -89, -127, -89, 
				0, 89, 127, 89, 0, -89, -127, -89, 
				0, 89, 127, 89, 0, -89, -127, -89, 
				};
		
		LOGGER.debug("pos: " + result.limit());
		
		for(int n = 0; n < result.limit(); n++) {
			LOGGER.debug("n: " + n + " - " + result.get(n));
			assertTrue(result.get(n) == ref[n]);
		}
		
	} // testEncode1Bit


} // class
