/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Mpf1ProtocolTest.java
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import extension.control.BackgroundExecutor;
import extension.encoder.BitValue;
import extension.encoder.Encoder;
import extension.protocol.DefaultChecksumCalculator;
import target.support.Journal;
import target.support.Step;

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

class Mpf1ProtocolTest {

	private static Logger LOGGER = null;
	
	protected int samplingRate = Mpf1Protocol.SAMPLING_RATE;
	protected int sampleSize   = samplingRate / Mpf1Protocol.F_LOW;

	protected static List<Step> reference;

	
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
	 * Test method for {@link target.microprofessor1.Mpf1Protocol#Mpf1Protocol(encoder.Encoder)}.
	 */
	@Test
	final void testMpf1Protocol() {
		LOGGER.info("testMpf1Protocol()");

		/*
		 * No constructor test needed.
		 */
		assertTrue(true);
		
	} // testMpf1Protocol()

	/**
	 * Test method for {@link target.microprofessor1.Mpf1Protocol#setFileName(int)}.
	 */
	@Test
	final void testSetFileName() {
		LOGGER.info("testSetFileName()");

		IllegalAccessError thrown;
		
		ByteBuffer buffer = ByteBuffer.allocate(0);
		
		Journal journal = new Journal(128);
		
		/*
		 * Mock the Encoder class and store every step in a sequence.
		 */
		try(
			MockedConstruction<Encoder> encoderMock = Mockito.mockConstruction(Encoder.class,
				(mock, context) -> {
				
					System.out.println("mock: " + mock + ", context: " + context);
					
					doReturn(mock).when(mock).withSilenceEncoder(any());
					doReturn(mock).when(mock).withWaveCycleEncoder(any());
					doReturn(mock).when(mock).withStartBits(anyInt(), any());
					doReturn(mock).when(mock).withStopBits(anyInt(),  any());

					doAnswer(journal).when(mock).clearSampleBuffer();
					doAnswer(journal).when(mock).encodeBit(Mockito.anyByte(), anyInt());
					doAnswer(journal).when(mock).encodeByte(Mockito.anyByte(), Mockito.anyBoolean());
					doAnswer(journal).when(mock).encodeByteBuffer(Mockito.any(ByteBuffer.class), Mockito.anyBoolean());
					doAnswer(journal).when(mock).encodeSilence(anyInt());
					doAnswer(journal).when(mock).encodeWaveCycle(anyInt(), anyInt());
					doAnswer(journal).when(mock).encodeWord(anyInt(), Mockito.anyBoolean());
					doAnswer(journal).when(mock).getSampleBuffer();
	
					when(mock.getSampleSize()).thenReturn(sampleSize);
									
				} // ->
			); // mockConstruction(...)
			
			/*
			 * Mock the ChecksumCalculator
			 */
			MockedConstruction<DefaultChecksumCalculator> chkSumMock = Mockito.mockConstruction(DefaultChecksumCalculator.class,
				(mock, context) -> {
				
					System.out.println("mock: " + mock + ", context: " + context);

					doReturn(42).when(mock).getCheckSum();

				} // ->
			  ) // mockConstruction(...)
			) 
			{

				Mpf1Protocol cut = new Mpf1Protocol();
				cut.setStartAddress(0x7654);
				cut.setEndAddress(0x9876);
				
				// error handling
				thrown = assertThrows(IllegalAccessError.class, () -> cut.compile(buffer) );
				assertEquals("aFileName not set!", thrown.getMessage());
	
				cut.setFileName(0x1234);
				assertDoesNotThrow(() -> cut.compile(buffer));
				
			} // yrt
			
	} // testSetFileName()


	/**
	 * Test method for {@link target.microprofessor1.Mpf1Protocol#setStartAdr(int)}.
	 */
	@Test
	final void testSetStartAdr() {
		LOGGER.info("testSetStartAdr()");

		IllegalAccessError thrown;
		
		ByteBuffer buffer = ByteBuffer.allocate(0);
		
		Journal journal = new Journal(128);
		
		/*
		 * Mock the Encoder class and store every step in a sequence.
		 */
		try(
			MockedConstruction<Encoder> encoderMock = Mockito.mockConstruction(Encoder.class,
				(mock, context) -> {
				
					System.out.println("mock: " + mock + ", context: " + context);
					
					doReturn(mock).when(mock).withSilenceEncoder(any());
					doReturn(mock).when(mock).withWaveCycleEncoder(any());
					doReturn(mock).when(mock).withStartBits(anyInt(), any());
					doReturn(mock).when(mock).withStopBits(anyInt(),  any());

					doAnswer(journal).when(mock).clearSampleBuffer();
					doAnswer(journal).when(mock).encodeBit(Mockito.anyByte(), anyInt());
					doAnswer(journal).when(mock).encodeByte(Mockito.anyByte(), Mockito.anyBoolean());
					doAnswer(journal).when(mock).encodeByteBuffer(Mockito.any(ByteBuffer.class), Mockito.anyBoolean());
					doAnswer(journal).when(mock).encodeSilence(anyInt());
					doAnswer(journal).when(mock).encodeWaveCycle(anyInt(), anyInt());
					doAnswer(journal).when(mock).encodeWord(anyInt(), Mockito.anyBoolean());
					doAnswer(journal).when(mock).getSampleBuffer();
	
					when(mock.getSampleSize()).thenReturn(sampleSize);
									
				} // ->
			); // mockConstruction(...)
			
			/*
			 * Mock the ChecksumCalculator
			 */
			MockedConstruction<DefaultChecksumCalculator> chkSumMock = Mockito.mockConstruction(DefaultChecksumCalculator.class,
				(mock, context) -> {
				
					System.out.println("mock: " + mock + ", context: " + context);

					doReturn(42).when(mock).getCheckSum();

				} // ->
			  ) // mockConstruction(...)
			) 
			{

				Mpf1Protocol cut = new Mpf1Protocol();
				cut.setFileName(0x9876);
				cut.setEndAddress(0x9876);
				
				// error handling
				thrown = assertThrows(IllegalAccessError.class, () -> cut.compile(buffer) );
				assertEquals("aStartAdr not set!", thrown.getMessage());
	
				cut.setStartAddress(0x1234);
				assertDoesNotThrow(() -> cut.compile(buffer));
				
			} // yrt
			
	} // testSetStartAdr()
	
	
	/**
	 * Test method for {@link target.microprofessor1.Mpf1Protocol#setEndAdr(int)}.
	 */
	@Test
	final void testSetEndtAdr() {
		LOGGER.info("testSetEndtAdr()");

		IllegalAccessError thrown;
		
		ByteBuffer buffer = ByteBuffer.allocate(0);
		
		Journal journal = new Journal(128);
		
		/*
		 * Mock the Encoder class and store every step in a sequence.
		 */
		try(
			MockedConstruction<Encoder> encoderMock = Mockito.mockConstruction(Encoder.class,
				(mock, context) -> {
				
					System.out.println("mock: " + mock + ", context: " + context);
					
					doReturn(mock).when(mock).withSilenceEncoder(any());
					doReturn(mock).when(mock).withWaveCycleEncoder(any());
					doReturn(mock).when(mock).withStartBits(anyInt(), any());
					doReturn(mock).when(mock).withStopBits(anyInt(),  any());

					doAnswer(journal).when(mock).clearSampleBuffer();
					doAnswer(journal).when(mock).encodeBit(Mockito.anyByte(), anyInt());
					doAnswer(journal).when(mock).encodeByte(Mockito.anyByte(), Mockito.anyBoolean());
					doAnswer(journal).when(mock).encodeByteBuffer(Mockito.any(ByteBuffer.class), Mockito.anyBoolean());
					doAnswer(journal).when(mock).encodeSilence(anyInt());
					doAnswer(journal).when(mock).encodeWaveCycle(anyInt(), anyInt());
					doAnswer(journal).when(mock).encodeWord(anyInt(), Mockito.anyBoolean());
					doAnswer(journal).when(mock).getSampleBuffer();
	
					when(mock.getSampleSize()).thenReturn(sampleSize);
									
				} // ->
			); // mockConstruction(...)

			/*
			 * Mock the ChecksumCalculator
			 */
			MockedConstruction<DefaultChecksumCalculator> chkSumMock = Mockito.mockConstruction(DefaultChecksumCalculator.class,
				(mock, context) -> {
				
					System.out.println("mock: " + mock + ", context: " + context);

					doReturn(42).when(mock).getCheckSum();

				} // ->
			  ) // mockConstruction(...)
			) 
			{

				Mpf1Protocol cut = new Mpf1Protocol();
				cut.setFileName(0x9876);
				cut.setStartAddress(0x1234);
				
				// error handling
				thrown = assertThrows(IllegalAccessError.class, () -> cut.compile(buffer) );
				assertEquals("aEndAdr not set!", thrown.getMessage());
	
				cut.setEndAddress(0x2345);
				assertDoesNotThrow(() -> cut.compile(buffer));
				
			} // yrt
			
	} // testSetEndtAdr()
	
	
	/**
	 * Test method for {@link target.microprofessor1.Mpf1Protocol#setEndAdr(int)}.
	 */
	@Test
	final void testStartAdrEndtAdr() {
		LOGGER.info("testStartAdrEndtAdr()");

		IllegalAccessError thrown;
		
		ByteBuffer buffer = ByteBuffer.allocate(0);
		
		Journal journal = new Journal(128);
		
		/*
		 * Mock the Encoder class and store every step in a sequence.
		 */
		try(
			MockedConstruction<Encoder> encoderMock = Mockito.mockConstruction(Encoder.class,
				(mock, context) -> {
				
					System.out.println("mock: " + mock + ", context: " + context);
					
					doReturn(mock).when(mock).withSilenceEncoder(any());
					doReturn(mock).when(mock).withWaveCycleEncoder(any());
					doReturn(mock).when(mock).withStartBits(anyInt(), any());
					doReturn(mock).when(mock).withStopBits(anyInt(),  any());

					doAnswer(journal).when(mock).clearSampleBuffer();
					doAnswer(journal).when(mock).encodeBit(Mockito.anyByte(), anyInt());
					doAnswer(journal).when(mock).encodeByte(Mockito.anyByte(), Mockito.anyBoolean());
					doAnswer(journal).when(mock).encodeByteBuffer(Mockito.any(ByteBuffer.class), Mockito.anyBoolean());
					doAnswer(journal).when(mock).encodeSilence(anyInt());
					doAnswer(journal).when(mock).encodeWaveCycle(anyInt(), anyInt());
					doAnswer(journal).when(mock).encodeWord(anyInt(), Mockito.anyBoolean());
					doAnswer(journal).when(mock).getSampleBuffer();
	
					when(mock.getSampleSize()).thenReturn(sampleSize);
									
				} // ->
			  ); // mockConstruction(...)
			
			/*
			 * Mock the ChecksumCalculator
			 */
			MockedConstruction<DefaultChecksumCalculator> chkSumMock = Mockito.mockConstruction(DefaultChecksumCalculator.class,
				(mock, context) -> {
				
					System.out.println("mock: " + mock + ", context: " + context);

					doReturn(42).when(mock).getCheckSum();

				} // ->
			  ) // mockConstruction(...)
			) // yrt
			{

				Mpf1Protocol cut = new Mpf1Protocol();
				cut.setFileName(0x9876);
				cut.setStartAddress(0x1234);
				
				// error handling
				cut.setEndAddress(0x1233);
				thrown = assertThrows(IllegalAccessError.class, () -> cut.compile(buffer) );
				assertEquals("aStartAdr must be less than endAdr", thrown.getMessage());
	
				cut.setEndAddress(0x1234);
				thrown = assertThrows(IllegalAccessError.class, () -> cut.compile(buffer) );
				assertEquals("aStartAdr must be less than endAdr", thrown.getMessage());

				cut.setEndAddress(0x1235);
				assertDoesNotThrow(() -> cut.compile(buffer));
				
			} // yrt
			
	} // testStartAdrEndtAdr()
	
	
	/**
	 * Test method for {@link target.microprofessor1.Mpf1Protocol#compile(java.nio.ByteBuffer)}.
	 */
	@Test
	final void testCompile() {
		LOGGER.info("testCompile()");
		
		Journal journal = new Journal(128);
				
		ByteBuffer dataBuffer = ByteBuffer.allocate(8)
			.put((byte) 0x00)	//   0
			.put((byte) 0x05)	//   5
			.put((byte) 0x50)	//  80
			.put((byte) 0x0A)	//  10
			.put((byte) 0xA0)	// -96
			.put((byte) 0x5A)	//  90
			.put((byte) 0xA5)	// -91
			.put((byte) 0xFF)	//  -1
			.rewind()			// chkSum 0xFD / -3
			;
		
		/*
		 * Mock the Encoder class and store every step in a sequence.
		 */
		try(
			MockedConstruction<Encoder> encoderMock = Mockito.mockConstruction(Encoder.class,
				(mock, context) -> {
				
					System.out.println("mock: " + mock + ", context: " + context);
					
					doAnswer(journal).when(mock).withSilenceEncoder(any());
					doAnswer(journal).when(mock).withWaveCycleEncoder(any());
					doAnswer(journal).when(mock).withStartBits(anyInt(), any());
					doAnswer(journal).when(mock).withStopBits(anyInt(),  any());

					doAnswer(journal).when(mock).clearSampleBuffer();
					
					doAnswer(journal).when(mock).encodeBit(Mockito.anyByte(), anyInt());
					doAnswer(journal).when(mock).encodeByte(Mockito.anyByte(), Mockito.anyBoolean());
					doAnswer(journal).when(mock).encodeByteBuffer(Mockito.any(ByteBuffer.class), Mockito.anyBoolean());
					doAnswer(journal).when(mock).encodeSilence(anyInt());
					doAnswer(journal).when(mock).encodeWaveCycle(anyInt(), anyInt());
					doAnswer(journal).when(mock).encodeWord(anyInt(), Mockito.anyBoolean());
					
					doAnswer(journal).when(mock).getSampleBuffer();
	
					when(mock.getSampleSize()).thenReturn(sampleSize);
								
			} // ->
		  ) // mockConstruction(...)
		) {

			Mpf1Protocol cut = new Mpf1Protocol();
			cut.setFileName(0x9876);
			cut.setStartAddress(0x1234);			
			cut.setEndAddress(0x5678);
			
			cut.compile(dataBuffer);
			
		} // yrt
		
		// define expectations
		reference = List.of(
				new Step( 0, "withSilenceEncoder",		new Object[] {"SilenceEncoder [samplingRate=8000]"}),
				new Step( 1, "withWaveCycleEncoder",	new Object[] {"WaveCycle [samplingRate=8000]"}),
				
				new Step( 2, "withStartBits",			new Object[] {1, BitValue.LOW.toString()}),
				new Step( 3, "withStopBits",			new Object[] {1, BitValue.HIGH.toString()}),

				new Step( 4, "encodeSilence",		new Object[] {500}),
				new Step( 5, "encodeWaveCycle",		new Object[] {1000, 4000}),
				new Step( 6, "encodeWord",			new Object[] {39030, true}),
				new Step( 7, "encodeWord",			new Object[] {4660, true}),
				new Step( 8, "encodeWord",			new Object[] {22136, true}),
				new Step( 9, "encodeByte",			new Object[] {-3, true}),
				new Step(10, "encodeWaveCycle",		new Object[] {2000, 4000}),
				new Step(11, "encodeByteBuffer",	new Object[] {new byte[] {0, 5, 80, 10, -96, 90, -91, -1}, true}),
				new Step(12, "encodeWaveCycle",		new Object[] {2000, 4000}),
				new Step(13, "encodeSilence",		new Object[] {500}),
				new Step(14, "getSampleBuffer",		new Object[] {})
			);

		List<Step> steps = journal.getSteps();
		
		Step step;
		Object[] stepArgs;
		
		Step ref ;
		Object[] refArgs;
		
		assertTrue(steps.size() == reference.size());
		
		// compare recorded sequence against the expectations
		for(int n = 0; n < steps.size(); n++) {
			step = (Step) steps.get(n);
			ref  = (Step) reference.get(n);
			
			assertTrue(step.sequenceNbr == ref.sequenceNbr);
			assertTrue(step.method.equals(ref.method));
			
			stepArgs = step.arguments;
			refArgs  = ref.arguments;
			
			assertTrue(stepArgs.length == refArgs.length);
			
			if(step.method.equals("encodeByteBuffer")) {
				ByteBuffer stepBuffer = (ByteBuffer) step.arguments[0];
				
				byte[] stepBytes = new byte[stepBuffer.limit()];
				byte[] refBytes  = (byte[]) ref.arguments[0];
				
				assertTrue(stepBytes.length == refBytes.length);
				
				for(int m = 0; m < refBytes.length; m++) {
					stepBytes[m] = stepBuffer.get(m);
					
					assertTrue(stepBytes[m] == refBytes[m]);
				}
				
				LOGGER.info("step: {}, content: {}", step, Arrays.toString(stepBytes));
				LOGGER.info("ref:  {}, content: {}", ref, Arrays.toString(refBytes));
				
			} // fi
			else {
				LOGGER.info("step: {}", step);
				LOGGER.info("ref : {}", ref);
				
				for(int i = 0; i < refArgs.length; i++) {
					LOGGER.info("i: {} stepArgs[i]: {}, refArgs[i]: {}", i, stepArgs[i].toString(), refArgs[i].toString());
					assertTrue(stepArgs[i].toString().equals(refArgs[i].toString()));
				}

			} // esle
			
		} // rof
		
	} // testCompile()
	

	/**
	 * 
	 * @throws Exception
	 */
	
	class Mpf1Test extends Mpf1Protocol {
		public void progress(final int aStep ) { super.progress(aStep); }
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	void testProgress() throws Exception {
		LOGGER.info("testProgress()");
		
		try(
		
			MockedConstruction<BackgroundExecutor> bgeMockConstructed = mockConstruction(BackgroundExecutor.class);
				
		) // yrt() 
		{
			
			
			Mpf1Protocol cut = new Mpf1Protocol();
			cut.setDataBuffer(ByteBuffer.allocate(1));
			cut.setFileName(0x01234);
			cut.setStartAddress(0x01234);
			cut.setEndAddress(0x2345);
			
			// execute created the BackgroundExecutor
			cut.execute();

			assertEquals(1,  bgeMockConstructed.constructed().size());
			
			BackgroundExecutor bgeMock = bgeMockConstructed.constructed().get(0);
			LOGGER.info("bgeMock = {}", bgeMock);
			
			InOrder progressSteps = inOrder(bgeMock);

			progressSteps.verify(bgeMock).execute();

			// We MUST trigger cut.runBackgroundTask() to start execution!
			cut.runBackgroundTask();
			
			progressSteps.verify(bgeMock, times(10)).stepOn(anyInt());

		} // yrt {}
		
	} // testProgress()
	

	/**
	 * Verifies the correct behavior of toString()
	 * 
	 * Test method for {@link #toString()}.
	 */
	@Test
	void testToString() {
		LOGGER.info("testToString()");
		
		Mpf1Protocol cut = new Mpf1Protocol();
		cut.setFileName(0x9876);
		cut.setStartAddress(0x1234);			
		cut.setEndAddress(0x5678);

		LOGGER.info("toString(): {}", cut.toString());
		assertEquals(
			 "Mpf1Protocol ["
			 +  "numericFileName=39030, "
			 +  "haveFileName=true, "
			 +  "startAdr=4660, "
			 +  "haveStartAdr=true, "
			 +  "endAdr=22136, "
			 +  "haveEndAdr=true, "
			 +  "BackgroundTaskProtokol ["
			 +    "checksumCalculator=ChecksumCalculator [mask=255, chkSum=0, isInitialized=false], "
			 +    "encoder=Encoder ["
			 +      "silenceEncoder=SilenceEncoder [samplingRate=8000], "
			 +      "waveCycleEncoder=WaveCycle [samplingRate=8000], "
			 +      "bitEncoder=FullCycleBitEncoder ["
			 +        "lowBitSamples=java.nio.HeapByteBuffer[pos=48 lim=48 cap=48], "
			 +        "highBitSamples=java.nio.HeapByteBuffer[pos=48 lim=48 cap=48], "
			 +        "BitEncoder ["
			 +          "waveCycleEncoder=WaveCycle [samplingRate=8000]"
			 +        "]"
			 +      "], "
			 +      "startBitSamples=java.nio.HeapByteBuffer[pos=48 lim=48 cap=48], "
			 +      "stopBitSamples=java.nio.HeapByteBuffer[pos=48 lim=48 cap=48], "
			 +      "sampleBuffer=null, bitOrder=LSB_MSB, byteOrder=LITTLE_ENDIAN"
			 +    "], "
			 +    "dataBuffer=null, "
			 +    "soundsampleBuffer=null, "
			 +    "soundSampleBufferSize=0, "
			 +    "isRunning=false, "
			 +    "currentProgress=0, "
			 +    "BackgroundTask [backgroundExecutor=null, isRunning=false]"
			 +  "]"
			 +"]"
			 ,
	        cut.toString())
			;

	} // testToString()
	
	
} // class
