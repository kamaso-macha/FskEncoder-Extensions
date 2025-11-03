/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : EncodeAnswer.java
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

package target.support;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Responsibilities:<br>
 * extract information from an invocation of the doAnswer method of Mockito framework. 
 * 
 * <p>
 * Collaborators:<br>
 * Mockito framework, interface Answer<Object>.
 * Sequence
 * Step
 * 
 * <p>
 * Description:<br>
 * The Mockito framework knows an inOrder test which can be used to verify that
 * method calls are in a specific sequence. But this capability has some backdraws 
 * which are explained in detail in the official documentation and discussion boards.
 * <br>
 * To overcome those limitations an own sequence tracer was created. 
 * <br>
 * This class extracts the information which is required to emulate an Mockito.inOrder verification 
 * and stores it as a step in the sequence object.
 * <p>
 * @author Stefan
 *
 */

public class Journal implements Answer<Object> {
	
	private Logger logger = LogManager.getLogger(Sequence.class.getName());
	
	final int bufferSize;
	final Sequence sequence;
	
	
	/**
	 * Constructor.
	 *  
	 * @param aBufferSize
	 * The size of a ByteBUffer to be returned when ten method argument 
	 * NOT starts with the string 'with'.
	 * If the invoked method starts with the string 'with' than a reference to the called mock is returned.
	 *  
	 */
	public Journal(final int aBufferSize) {
		
		logger.info("EncodeAnswer(): aBufferSize = {}", aBufferSize);
		bufferSize = aBufferSize;
		sequence   = new Sequence();
		
	}

	
	/**
	 * Interface method of Mockito framework.
	 * It obtains the required information and put them in the sequence.
	 * 
	 * @return
	 * Either a reference to the invoked mock if the method string starts with 'with'
	 * or a ByteBuffer with aBufferSize capacity.
	 */
	@Override
	public Object answer(InvocationOnMock invocation) throws Throwable {
		
		String method = invocation.getMethod().getName();
		Object[] arguments = invocation.getArguments();
		sequence.add(method, arguments);
		
		logger.debug("EncodeAnswer.answer(): sequence = {}, invocation: {}, method: {}, arguments: {}", 
			sequence.getSequence(), 
			invocation, 
			method,
			arguments
			);
		
		for(int n = 0; n < arguments.length; n++) {
			logger.debug("EncodeAnswer.answer(): n = {}, argument: {}", n, arguments[n]); 
		}
		
		if(method.startsWith("with")) {
			
			return invocation.getMock();
			
		}
		else {
			
			return ByteBuffer.allocate(bufferSize);
			
		} // esle
		
	} // answer(...)
	
	
	/**
	 * Returns the current value of the unique sequence number.
	 *  
	 * @return
	 * An integer representing the current value of the sequence counter.
	 */
	public int getSequence() { return sequence.getSequence(); }
	
	
	/**
	 * Return the list of recorded steps.
	 * 
	 * @return
	 * A List<Step> holding the recorded steps.
	 */
	public List<Step> getSteps() { return sequence.getSteps(); }
	
} // EncodeAnswer
