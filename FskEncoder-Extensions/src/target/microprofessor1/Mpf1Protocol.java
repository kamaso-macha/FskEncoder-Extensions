/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Mpf1Protocol.java
 *
 * More information about this project can be found on Github
 * http://github.com/kamaso-macha/FskEncoder-Extensions
 *
 * **********************************************************************
 *
 * Copyright (C)2025 by Kama So Macha (http://github.com/kamaso-macha)
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

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.encoder.BitEncoder;
import extension.encoder.BitOrder;
import extension.encoder.BitValue;
import extension.encoder.ByteOrder;
import extension.encoder.Encoder;
import extension.encoder.SilenceEncoder;
import extension.encoder.WaveCycleEncoder;
import extension.protocol.BackgroundTaskProtokol;
import extension.protocol.DefaultChecksumCalculator;
import extension.sound.FskAudioFormat;

/**
 * Responsibilities:<br>
 * Encode a data buffer into MPF-1 specific sound samples.
 * 
 * 
 * <p>
 * Collaborators:<br>
 * <ul>
 * 	<li>DefaultChecksumCalculator</li>
 * 	<li>SilenceEncoder</li>
 * 	<li>WaveCycleEncoder</li>
 * 	<li>Mpf1BitEncoder</li>
 * 	<li>Encoder</li>
 * </ul>
 * 
 * <p>
 * Description:<br>
 * Implements the MPF-1 specific protocol required to convert a data buffer
 * into the correct sound samples that can be loaded into the target system. 
 * 
 * <blockquote><tt><pre>

		Multitech Microprofessor MPF-I tape format


		Bit format
		--------------------------------------------------------------------------------
		
		'0'     8 cycles 2000Hz   ( 8 * 0,5 ms = 4 ms )
		      + 2 cycles 1000Hz   ( 2 * 1,0 ms = 2 ms )
		      
		      |                               ^           |
		     _|"|_|"|_|"|_|"|_|"|_|"|_|"|_|"|_|""|__|""|__|"
		      |0   1   2   3   4   5   6   7   0     1    | 
		
		        
		'1'     4 cycles 2000Hz   ( 4 * 0,5 ms = 2 ms )
		      + 4 cycles 1000Hz   ( 4 * 1,0 ms = 4 ms )
		      
		      |               ^                       |
		     _|"|_|"|_|"|_|"|_|""|__|""|__|""|__|""|__|"
		      |0   1   2   3   0     1     2     3    | 
		
		
		  1 bit equals 6ms
		
		
		
		Envelope
		--------------------------------------------------------------------------------
		
		  1 start bit '0'
		  8 data bits, lsb first (b0 to b7)
		  1 stop bit '1'
		
		
		
		Byte and Word format
		--------------------------------------------------------------------------------
		  
		Word:		Lo-byte,  Hi-byte
		Byte:		Lo-nible, Hi-nible
		
		E.g. 
		
		The	WORD 		0x1234		0001.0010 : 0011.0100	(b7 ... b0 : b7 ... b0) 
		is send as		  4321		0010.1100 : 0100.1000	(b0 ... b7 : b0 ... b7)

		
		
		File format
		--------------------------------------------------------------------------------
		
		1.    4000 cycles 1000Hz Lead sync
		
		2.    2 envlp   filename
		3.    2 envlp   starting address
		4.    2 envlp   ending address
		
		5.    1 envlp   checksum of datablock start adr to end adr
		
		6.    4000 cycles 2000Hz Mid sync
		
		7.    n envlp   datablock
		
		8.    4000 cycles 2000Hz Tail sync
		

 * </pre></tt></blockquote>
 *
 * <p>
 * @author Stefan
 *
 */

public class Mpf1Protocol extends BackgroundTaskProtokol {

	private Logger logger = LogManager.getLogger(Mpf1Protocol.class.getName());
	
	/*
	 * FSK and envelope parameter definition
	 */
	protected static final int F_LOW 					=   1000;
	protected static final int F_HIGH					=	2000;
	
	protected static final int NBR_START_BITS			=	1;
	protected static final BitValue START_BIT_VALUE	=	BitValue.LOW;
	
	protected static final int NBR_STOP_BITS			=	1;
	protected static final BitValue STOP_BIT_VALUE		=	BitValue.HIGH;
	
	protected static final int SAMPLING_RATE			=	F_HIGH * 4;

	/*
	 * Structure of a complete file for upload
	 */
	protected static final int SILENCE_BLOCK		= 500;
	
	protected static final int LEAD_IN				= 4000;		//cycle
	protected static final int F_LEAD_IN			= 1000;		// Hz
	
	protected static final int FILE_NAME				= 1;		// word
	protected static final int START_ADR			= 1;		// word
	protected static final int END_ADR				= 1;		// word

	protected static final int CK_SUM				= 1;		// byte
	
	protected static final int MID_SYNC				= 4000;		//cycle
	protected static final int F_MID_SYNC			= 2000;		// Hz
	
	// 						byte[] data				= n 		// bytes TX-frames

	protected static final int TAIL_SYNC			= 4000;		//cycle
	protected static final int F_TAIL_SYNC			= 2000;		// Hz
	

	protected int numericFileName;
	protected boolean haveFileName = false;

	protected int startAdr;
	protected boolean haveStartAdr = false;
	protected int endAdr;
	protected boolean haveEndAdr = false;


	/**
	 * Constructor.
	 */
	public Mpf1Protocol() {
		
		logger.trace("Mpf1Protocol()");
		
		checksumCalculator	= new DefaultChecksumCalculator(0x00FF);
		
		SilenceEncoder silenceEncoder = new SilenceEncoder(SAMPLING_RATE);
		WaveCycleEncoder waveCycleEncoder = new WaveCycleEncoder(SAMPLING_RATE);
		
		BitEncoder bitEncoder = new Mpf1BitEncoder(waveCycleEncoder);
		
		encoder = new Encoder(bitEncoder, ByteOrder.LITTLE_ENDIAN, BitOrder.LSB_MSB)
			.withSilenceEncoder(silenceEncoder)
			.withWaveCycleEncoder(waveCycleEncoder)
			.withStartBits(NBR_START_BITS, START_BIT_VALUE)
			.withStopBits(NBR_STOP_BITS, STOP_BIT_VALUE)
			;
		
		logger.trace("Mpf1Protocol(): aEncoder: {}, checksumCalculator: {}", encoder, checksumCalculator);
		
	} // Mpf1Protocol()
	
	
	/**
	 * Set the end address of the data block.
	 * 
	 * @param aEndAdr 
	 * The endAdr to set
	 */
	public void setEndAddress(long aEndAdr) { 
		logger.trace("setEndAddress(): aEndAdr = {}", String.format("0x%04X", aEndAdr));

		endAdr = (int) (aEndAdr & 0x0FFFF);
		haveEndAdr = true;
		
	} // setEndAdr()


	/**
	 * Set the file name that originally identified a unique block of data on tape.<br>
	 * 
	 * This application needs a file name for protocol purposes only.
	 * It can easily be set to a default value, e.g. 1. 
	 * 
	 * @param aFileName 
	 * The file name be to set. It's an integer value in the range between 0x001 and 0xFFFF
	 * 
	 */
	public void setFileName(int aFileName) {
		logger.trace("setFileName(): aFileName = {}", String.format("0x%04X", aFileName));
		
		numericFileName = aFileName;
		haveFileName = true;
		
	} // setFileName()

	
	/**
	 * Returns the audio format used for this protocol.
	 * 
	 * @return FskAudioFormat 
	 * The used audio format
	 */
	public FskAudioFormat getAudioFormat() { return new FskAudioFormat(SAMPLING_RATE); }
	
	
	/**
	 * Set the start address of the data block.
	 * 
	 * @param aStartAdr 
	 * The startAdr to be set
	 */
	@Override
	public void setStartAddress(long aStartAdr) {
		logger.trace("setStartAddress(): aStartAdr = {}", String.format("0x%04X", aStartAdr));
		
		startAdr = (int)aStartAdr & 0x0FFFF;
		haveStartAdr = true;
		
	} // setStartAddress()


	/**
	 * Starts the translation of the given data buffer and returns the generated sound samples.
	 * 
	 * Translation is performed according to the defined protocol.
	 * A sound sample buffer is filled with all protocol elements and data and then returned.
	 * 
	 */
	@Override
	public ByteBuffer compile(final ByteBuffer aDataBuffer) {
		logger.trace("compile(): aDataBuffer = {}", aDataBuffer);
		
		if(!haveFileName) {
			logger.trace("ERROR: aFileName not set!");
			throw new IllegalAccessError("aFileName not set!");
		}
		if(!haveStartAdr) {
			logger.trace("ERROR: aStartAdr not set!");
			throw new IllegalAccessError("aStartAdr not set!");
		}
		if(!haveEndAdr) {
			logger.trace("ERROR: aEndAdr not set!");
			throw new IllegalAccessError("aEndAdr not set!");
		}
		if(startAdr >= endAdr)	{
			logger.trace("ERROR: aStartAdr must be less than endAdr");
			throw new IllegalAccessError("aStartAdr must be less than endAdr");
		}
		
		logger.info("compile(): err chk done & OK");
		logger.info(String.format("FileName: 0x%04X, Start: 0x%04X, End: 0x%04X, Size: 0x%04X", 
				numericFileName, startAdr, endAdr, (endAdr - startAdr + 1))
		);

		isRunning = true;
		currentProgress = 0;
		setFullProgress(aDataBuffer.limit());
		
		calculateBufferSize(aDataBuffer);
		encoder.setBufferSize(soundSampleBufferSize);
		
		logger.trace("compile(): buffer alloc");

		// TODO: Test coverage
		
		try {
			silence();
			progress(SILENCE_BLOCK);
			if(!isRunning) return null;
						
			syncPatern(F_LEAD_IN, LEAD_IN);			
			progress(LEAD_IN);
			if(!isRunning) return null;
			
			fileName();
			progress(FILE_NAME);
			if(!isRunning) return null;
			
			address(startAdr);
			progress(START_ADR);
			if(!isRunning) return null;
			
			address(endAdr);
			progress(END_ADR);
			if(!isRunning) return null;
			
			
			checkSum(aDataBuffer);
			progress(CK_SUM);
			if(!isRunning) return null;
			
			
			syncPatern(F_MID_SYNC, MID_SYNC);
			progress(MID_SYNC);
			if(!isRunning) return null;
			
			
			dataBlock(aDataBuffer);
			progress(aDataBuffer.limit());
			if(!isRunning) return null;
			
			
			syncPatern(F_TAIL_SYNC, TAIL_SYNC);
			progress(TAIL_SYNC);
			if(!isRunning) return null;
			
			
			silence();
			progress(SILENCE_BLOCK);
			if(!isRunning) return null;
			
		}
		catch(IllegalAccessException e) {
			logger.error("Unexpected exception caught:", e);
		}
		
		return encoder.getSampleBuffer();
		
	} // compile()
	
	
	/*
	 * Calculates the amount of sound samples needed.
	 * 
	 * The encoder knows the size of the sound samples and provides a method to obtain that value.
	 * 
	 * Each protocol element is now multiplied by the number of items used for their encoding.
	 * Lastly a sum is calculated over all protocol elements representing the maximum number of 
	 * sound samples.
	 * 
	 */
	protected void calculateBufferSize(final ByteBuffer aDataBuffer) {
		logger.trace("calculateBufferSize(): aDataBuffer = {}", aDataBuffer);
		
		int sampleSize = encoder.getSampleSize();
		int envelopeSize = 
				(NBR_START_BITS * sampleSize) 
			  + (8 * sampleSize) 
			  + (NBR_STOP_BITS * sampleSize)
			  ;
		/*
		 * For a better readability a tabular form is used.
		 */
		soundSampleBufferSize
			// item					mult	samples
			= SILENCE_BLOCK				* 1		* (int)(SAMPLING_RATE * SILENCE_BLOCK / 1000) + 1	// samples
			+ LEAD_IN				* 1		* sampleSize	// wave cycles
			+ FILE_NAME				* 2		* envelopeSize	// word
			+ START_ADR				* 2		* envelopeSize	// word
			+ END_ADR				* 2		* envelopeSize	// word
			+ CK_SUM				* 1		* envelopeSize	// bytes
			+ MID_SYNC				* 1		* sampleSize	// wave cycles
			+ aDataBuffer.limit()	* 1		* envelopeSize	// bytes
			+ CK_SUM				* 1		* envelopeSize	// bytes
			+ TAIL_SYNC				* 1		* sampleSize	// wave cycles
			+ SILENCE_BLOCK				* 1		* (int)(SAMPLING_RATE * SILENCE_BLOCK / 1000) + 1	// samples
			;
		
		logger.trace("soundSampleBufferSize = {}", soundSampleBufferSize);
		
	} //calculateBufferSize()	
	

	/*
	 * 
	 * The following helper methods are all self-explantory and are not intended to be commented on in detail.
	 * 
	 * 
	 */
	
	
	protected void address(final int aAddress) throws IllegalAccessException {
		logger.trace("address()");
		
		encoder.encodeWord(aAddress, true);
		
	} //address()


	protected void checkSum(final ByteBuffer aDataBuffer) throws IllegalAccessException {
		logger.trace("chkSum()");
		
		aDataBuffer.rewind();
		checksumCalculator.clear();
		
		logger.trace("dataBlock(): aDataBuffer = {}", aDataBuffer);
		
		for(int n = 0; n < aDataBuffer.limit(); n++) {
//			logger.trace("dataBlock(): n = {}, byte = {}", n, String.format("%02X", aDataBuffer.get(n)));
			checksumCalculator.sumUp(aDataBuffer.get(n));
		}
		
		int chkSum = checksumCalculator.getCheckSum();	
		encoder.encodeByte((byte)chkSum, true);
		
	} //checkSum()


	protected void dataBlock(final ByteBuffer aDataBuffer) throws IllegalAccessException {
		logger.trace("dataBlock(): aDataBuffer = {}", aDataBuffer);
		
		aDataBuffer.rewind();
		encoder.encodeByteBuffer(aDataBuffer, true);
		
	} //dataBlock()


	protected void fileName() {
		logger.trace("fileName(): {}", String.format("0x%04X", numericFileName));
		
		encoder.encodeWord(numericFileName, true);
		
	} //fileName()


	protected void silence() {
		logger.trace("silence()");
		
		encoder.encodeSilence(SILENCE_BLOCK);
		
	} //silence()


	protected void syncPatern(final int aFrequency, final int aCount) {
		logger.trace("syncPatern()");
		
		encoder.encodeWaveCycle(aFrequency, aCount);
		
	} //syncPatern()


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "Mpf1Protocol [numericFileName=" + numericFileName + ", haveFileName=" + haveFileName + ", startAdr="
				+ startAdr + ", haveStartAdr=" + haveStartAdr + ", endAdr=" + endAdr + ", haveEndAdr=" + haveEndAdr
				+ ", " + super.toString()
				+ "]";
	}
	
	
	@Override
	protected void setFullProgress(final int aBufferSize) {
		logger.trace("setFullProgress()");
		
		fullProgress = 
		// item					mult
		  SILENCE_BLOCK			* 1
		+ LEAD_IN				* 1	
		+ LEAD_IN				* 1		// wave cycles
		+ FILE_NAME				* 2		// word
		+ START_ADR				* 2		// word
		+ END_ADR				* 2		// word
		+ CK_SUM				* 1		// bytes
		+ MID_SYNC				* 1		// wave cycles
		+ aBufferSize			* 1		// bytes
		+ CK_SUM				* 1		// bytes
		+ TAIL_SYNC				* 1		// wave cycles
		+ SILENCE_BLOCK			* 1
		;
		
	} // setFullProgress()


} // class
