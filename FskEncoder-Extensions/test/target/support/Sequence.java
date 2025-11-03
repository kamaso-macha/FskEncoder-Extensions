/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Sequence.java
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

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Responsibilities:<br>
 * Store all steps belonging to a sequence of method invocations.
 * 
 * <p>
 * Collaborators:<br>
 * Journal, Step.
 * 
 * <p>
 * Description:<br>
 * The Mockito framework knows an inOrder test which can be used to verify that
 * method calls are in a specific sequence. But this capability has some backdraws 
 * which are explained in detail in the official documentation and discussion boards.
 * <br>
 * To overcome those limitations an own sequence tracer was created. 
 * <br>
 * This class holds a list of method invocation steps and a unique number generator 
 * used as sequence parameter for the steps created.
 * 
 * <p>
 * @author Stefan
 *
 */

public class Sequence {
	
	private Logger logger = LogManager.getLogger(Sequence.class.getName());
	
	protected List<Step> steps; 
	protected int sequence = 0;
	
	/**
	 * Default constructor.
	 */
	public Sequence() {
		logger.info("Sequence():");
		steps = new ArrayList<>();
	}
	
	
	/**
	 * Add a new step to the sequence.<br>
	 * From the given parameters a new Step object is created and appended to the list of steps.
	 * After this, the sequence counter is incremented.
	 * 
	 * @param aMethod
	 * A string describing the method which was invoked at the point of capture.<br>
	 * It can also be set up with any other information because it has no special sementic value.
	 * 
	 * @param aArguments
	 */
	public void add(String aMethod, Object[] aArguments) {
		
		Step step = new Step(sequence, aMethod, aArguments);
		logger.info("Sequence.add(): sequence = {}, step = {}",sequence, step);
		
		steps.add(step);
		sequence++;
	}
	
	
	/**
	 * Returns the current value of the unique sequence number.
	 *  
	 * @return
	 * An integer representing the current value of the sequence counter.
	 */
	public int getSequence() { return sequence; }
	
	
	/**
	 * Return the list of recorded steps.
	 * 
	 * @return
	 * A List<Step> holding the recorded steps.
	 */
	public List<Step> getSteps() { return steps; }

	
	/**
	 * A string representation of the current state.
	 * 
	 * @return
	 * A string object.
	 */
	@Override
	public String toString() {
		return "Sequence [steps=" + steps + ", sequence=" + sequence + "]";
	}

} // Sequence
