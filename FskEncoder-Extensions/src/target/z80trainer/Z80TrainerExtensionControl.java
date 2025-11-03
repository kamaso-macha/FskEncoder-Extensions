/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : InputFileLoadController.java
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

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.control.StatusMessenger;
import extension.control.TargetSystemExtensionControl;
import extension.protocol.Protocol;
import extension.sound.FskAudioFormat;
import extension.view.gui.ExtensionGui;


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
// Created at 2024-05-09 12:53:57

public class Z80TrainerExtensionControl  extends TargetSystemExtensionControl implements ChangeListener, FocusListener {

	private Logger logger = LogManager.getLogger(Z80TrainerExtensionControl.class.getName());

	protected final String DEFAULT_PROGRAM_NUMBER = "0x0001";
	
	private Z80TrainerExtensionGui gui;	// NOSONAR
	
	protected Z80TrainerProtocol protocol;
	
	
	/**
	 * @param aWorkFlowEngine
	 */
	public Z80TrainerExtensionControl(StatusMessenger aStatusMessenger) {
		logger.trace("Z80TrainerExtensionControl(): StatusMessenger {}", aStatusMessenger);
		
		if(aStatusMessenger == null) throw new IllegalArgumentException("aStatusMessenger can't be null");
		
		protocol = new Z80TrainerProtocol();
		gui = new Z80TrainerExtensionGui(this);

		/*
		 * Default value
		 */
		gui.setTxtProgrammNumberText(DEFAULT_PROGRAM_NUMBER);
		setProgramNumber();
		
	} // Z80TrainerExtensionControl()
	

	@Override
	public void actionPerformed(ActionEvent e) {
		logger.trace("actionPerformed(): e = {}", e);
		
	} // actionPerformed()


	@Override
	public JPanel createLayout() {
		logger.trace("createLayout()");
		
		JPanel panel = gui.createLayout();
		gui.setTxtProgrammNumberText(DEFAULT_PROGRAM_NUMBER);
		
		return panel;
		
	} // createLayout()

	
	@Override
	public void focusGained(FocusEvent e) {
		logger.trace("focusGained(): e = {}", e);
		
	} // focusGained()
	

	@Override
	public void focusLost(FocusEvent e) {
		logger.trace("focusLost(): e = {}", e);
		
		setProgramNumber();
		
	} // focusLost()


	/**
	 * 
	 */
	protected void setProgramNumber() {
		logger.trace("setProgramNumber()");
		
		String txtProgramNumber = gui.getTxtProgrammNumberText().replaceAll("^0x", ""); 
		logger.trace("txtProgramNumber = {}", txtProgramNumber);
		
		int programNumber = Integer.parseInt(txtProgramNumber, 16);
		logger.trace("txtProgramNumber = {}, programNumber = {}", txtProgramNumber, programNumber);
		
		protocol.setProgramNbr(programNumber);
		
	} // setProgramNumber()
	

	@Override
	public void stateChanged(ChangeEvent e) {
		logger.trace("stateChanged(): e = {}", e);
		
	} // stateChanged()


	@Override
	public FskAudioFormat getAudioFormat() {
		logger.trace("getAudioFormat()");
		
		return protocol.getAudioFormat();
		
	} // getGui()
	

	/**
	 * @return
	 */
	@Override
	public ExtensionGui getGui() {
		logger.trace("getGui()");
		
		return gui;
		
	} // getGui()


	/**
	 * @return
	 */
	@Override
	public Protocol getProtocol() {
		logger.trace("getProtocol()");
		
		return protocol;
		
	} // getProtocol()


} // ssalc
