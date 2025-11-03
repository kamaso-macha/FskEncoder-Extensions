/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Mpf1BitEncoder.java
 *
 * PURPOSE       : Specific bit encoder for Microprofessor I (MFP-I) target.
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

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.encoder.FskBitEncoder;
import extension.encoder.FullCycleBitEncoder;
import extension.encoder.WaveCycleEncoder;

/**
 * Responsibilities:<br>
 * 	Encode a single 0 or 1 bit into their corresponding sound samples.<br>
 * 	Store the created sound samples in the byte buffers for low and high bit.<br>
 * 
 * <p>
 * Collaborators:<br>
 * 	Superclasses FullCycleBitEncoder and BitEncoder
 * 
 * <p>
 * Description:<br>
 * 	Provides the sound samples needed to encode a single bit according the MFP-I specification:<br>
 * 
 * <blockquote><tt>
 * 
 *    '0'     8 cycles 2000Hz   ( 8 * 0,5 ms = 4 ms )
 *          + 2 cycles 1000Hz   ( 2 * 1,0 ms = 2 ms )
 *          
 *          |                               ^           |
 *         _|¯|_|¯|_|¯|_|¯|_|¯|_|¯|_|¯|_|¯|_|¯¯|__|¯¯|__|¯
 *          |0   1   2   3   4   5   6   7   0     1    | 
 *    
 *            
 *    '1'     4 cycles 2000Hz   ( 4 * 0,5 ms = 2 ms )
 *          + 4 cycles 1000Hz   ( 4 * 1,0 ms = 4 ms )
 *          
 *          |               ^                       |
 *         _|¯|_|¯|_|¯|_|¯|_|¯¯|__|¯¯|__|¯¯|__|¯¯|__|¯
 *          |0   1   2   3   0     1     2     3    | 
 *    
 *    
 *      1 bit equals 6ms
 * 
 * </tt></blockquote>
 * 
 * <br>
 * 	The created sound samples are stored in two byte buffers, one for each bit value.
 * 
 * <p>
 * @author Stefan
 *
 */

public class Mpf1BitEncoder extends FullCycleBitEncoder {
	
	/*
	 * These parameters are defined in the protocol and should be defined only there and once!
	 */
	protected static final int F_LOW 	= Mpf1Protocol.F_LOW;
	protected static final int F_HIGH	= Mpf1Protocol.F_HIGH;
	
	protected static final int SAMPLING_RATE = Mpf1Protocol.SAMPLING_RATE;
	
	private Logger logger = LogManager.getLogger(FskBitEncoder.class.getName());
	
	
	/**
	 * 
	 * MFP-I uses a specific bit encoding which is considered as incompatible to other targets.
	 * Thats the reason why all parameters are hard coded.
	 *   
	 */
	public Mpf1BitEncoder(final WaveCycleEncoder aWaveCycleEncoder) {
		super(aWaveCycleEncoder);
		
		logger.trace("Mpf1BitEncoder(): {}", aWaveCycleEncoder);
		
		prepareSampleBuffers();

	} // Mpf1BitEncoder(...)


	/**
	 * Creates the sound samples used for encoding.<br>
	 * See 'Description' for the encoding rules.
	 */
	@Override
	protected void prepareSampleBuffers() {
		logger.trace("prepareSampleBuffers()");
		
		int lowBitSampleCount  = SAMPLING_RATE / F_LOW;
		int highBitSampleCount = SAMPLING_RATE / F_HIGH;
		
		int lowBitBufferSize = (8 * highBitSampleCount) + (2 * lowBitSampleCount);
		
		super.lowBitSamples = ByteBuffer.allocate(lowBitBufferSize);
		super.lowBitSamples.put(super.waveCycleEncoder.encode(F_HIGH, 8));
		super.lowBitSamples.put(super.waveCycleEncoder.encode(F_LOW, 2));
		super.lowBitSamples.flip();
		logger.trace("lowBitSamples.capacity = {}, lowBitSamples.position = {}", lowBitSamples.capacity(), lowBitSamples.position());
		
		int highBitBufferSize = (4 * highBitSampleCount) + (4 * lowBitSampleCount);
				
		super.highBitSamples = ByteBuffer.allocate(highBitBufferSize);
		super.highBitSamples.put(super.waveCycleEncoder.encode(F_HIGH, 4));
		super.highBitSamples.put(super.waveCycleEncoder.encode(F_LOW, 4));
		super.highBitSamples.flip();
		logger.trace("highBitSamples.capacity = {}, highBitSamples.position = {}", highBitSamples.capacity(), highBitSamples.position());
		
	} // prepareSampleBuffers
	

} // class
