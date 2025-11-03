/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Step.java
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

import java.util.Arrays;

/**
 * Responsibilities:<br>
 * Keep all information of a step in a sequence. 
 * 
 * <p>
 * Collaborators:<br>
 * None.
 * 
 * <p>
 * Description:<br>
 * The Mockito framework knows an inOrder test which can be used to verify that
 * method calls are in a specific sequence. But this capability has some backdraws 
 * which are explained in detail in the official documentation and discussion boards.
 * <br>
 * To overcome those limitations an own sequence tracer was created. 
 * <br>
 * The arguments for this class are normally provided by the Journal class which implement the
 * interface to Mockito framework. But it can also set up manually as reference.
 * <p>
 * @author Stefan
 *
 */

public class Step {
	
	public final int sequenceNbr;
	public final String method;
	public final Object[] arguments;
	
	
	/**
	 * Parameterized constructor for initializing the object.
	 */
	public Step(final int aSequenceNbr, final String aMethod, final Object[] aArguments) {
		sequenceNbr = aSequenceNbr;
		method      = aMethod;
		arguments   = aArguments;
	}
	
	@Override
	public String toString() {
		return "Step [" + "seqNbr=" + sequenceNbr +  ", method=" + method + ", arguments=" + Arrays.toString(arguments) + "]";
	}
	
} // Step
