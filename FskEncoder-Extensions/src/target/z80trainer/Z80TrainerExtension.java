/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Z80Extension.java
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


package target.z80trainer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.control.StatusMessenger;
import extension.factory.TargetSystemExtensionFactory;
import extension.model.TargetSystemExtensionDao;


/**
 * Responsibilities:<br>
 * 
 * 
 * <p>
 * Collaborators:<br>
 * 
 * 
 * <p>
 * Description:<br>
 * 
 * 
 * <p>
 * @author Stefan
 *
 */

// DOC
// Created at 2024-05-13 09:56:40

public class Z80TrainerExtension implements TargetSystemExtensionFactory {
	
	private Logger logger = LogManager.getLogger(Z80TrainerExtension.class.getName());

	
	@Override
	public TargetSystemExtensionDao getTargetSystemExtension(StatusMessenger aStatusMessenger) {
		logger.trace("getTargetSystemExtension()");
		
		if(aStatusMessenger == null) throw new IllegalArgumentException("aStatusMessenger can't be null");
		
		Z80TrainerExtensionControl control = new Z80TrainerExtensionControl(aStatusMessenger);
		
		return new TargetSystemExtensionDao(control.getProtocol(), control.getGui(), control);
		
	} // getTargetSystemExtension()
	
	
} // ssalc
