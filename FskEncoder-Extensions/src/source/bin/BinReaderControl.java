/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : MemoryMapController.java
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


package source.bin;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.control.ReaderExtensionControl;
import extension.control.StatusMessenger;
import extension.model.InputReaderExtensionDao;
import extension.model.MemoryBlockDescription;
import extension.model.MemoryMap;
import extension.model.MemoryRegion;
import extension.source.Reader;
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
// Created at 2024-05-09 13:23:12

public class BinReaderControl extends ReaderExtensionControl implements ChangeListener, FocusListener {
	
	private Logger logger = LogManager.getLogger(BinReaderControl.class.getName());
	
	protected StatusMessenger statusMessenger;
	protected BinReaderGui binReaderGui;
	
	protected Reader inputReader;
	protected MemoryMap memoryMap;


	/**
	 * @param aStatusMessenger 
	 * @param memoryMapGui
	 */
	public BinReaderControl(StatusMessenger aStatusMessenger) {
		logger.trace("Ihx8ReaderControl(): aStatusMessenger = {}", aStatusMessenger);

		if(aStatusMessenger == null) throw new IllegalArgumentException("aStatusMessenger can't be null");
		
		statusMessenger = aStatusMessenger;
		
		binReaderGui = new BinReaderGui(this);
		
	} // Ihx8ReaderControl()


	@Override
	public void actionPerformed(ActionEvent e) {
		logger.trace("actionPerformed(): e = {}", e);
		
		setEndAddress();
		
	} // actionPerformed()


	@Override
	public JPanel createLayout() {
		logger.trace("createLayout()");
		
		List<MemoryBlockDescription> memoryLayout = memoryMap.getMemoryLayout();
		
		return binReaderGui.createLayout(memoryLayout);
		
	}


	@Override
	public void initialize(InputReaderExtensionDao aInputReaderExtensionDao, StatusMessenger aWorkflowEngine) {
		logger.trace("initialize(): aInputReaderExtensionDao = {}, aWorkflowEngine = {}", aInputReaderExtensionDao, aWorkflowEngine);
		
		inputReader = aInputReaderExtensionDao.READER;
		memoryMap   = aInputReaderExtensionDao.MEMORY_MAP;
				
	} // initialize()

	
	@Override
	public void load() {
		logger.trace("load()");
		
		inputReader.loadFile();
		
	} // setFileName()


	@Override
	public void setFileName(String aFilePath) {
		logger.trace("setFileName(): aFilePath = {}", aFilePath);
		
		inputReader.setFilename(aFilePath);
		
	} // setFileName()


	@Override
	public List<MemoryRegion> getSelectedMemoryRegions() {
		logger.trace("getSelectedMemoryRegions()");
		
		if(getStartAddress() == -1) {
			
			statusMessenger.setStatusMessage("Please enter a valid start address!");
			return null;
		}
		
		List<MemoryBlockDescription> selectedRegions = binReaderGui.getSelectedEntries();
		
		Collections.sort(selectedRegions);
		
		List<MemoryRegion> candidates = new ArrayList<>();
		
		MemoryRegion candidate = memoryMap.getMemoryRegion(selectedRegions.get(0).START_ADDRESS);
		candidate.changeStartAddress(getStartAddress());
		candidates.add(candidate);
		
		return candidates; 
		
	} // getSelectedEntries()


	/**
	 * @return
	 */
	// FIXME: Refactor Ihx8ReaderExtension to remove this method.
	public ExtensionGui getGui() {
		logger.trace("getGui()");
		
		return binReaderGui;
		
	} // getSelectedEntries()


	@Override
	public void focusGained(FocusEvent e) {
		logger.trace("focusGained(): e = {}", e);
		
	} // focusGained()
	

	@Override
	public void focusLost(FocusEvent e) {
		logger.trace("focusLost(): e = {}", e);
		
		setEndAddress();
				
	} // focusLost()



	/**
	 * 
	 */
	private void setEndAddress() {
		logger.trace("setEndAddress()");
		
		long size;
		long startAddress = getStartAddress();
		
		size = binReaderGui.getTxtSize();
		
		binReaderGui.setTxtStartAddress(startAddress);
		binReaderGui.setTxtEndAddress(startAddress + size);
				
	} // setEndAddress()


	/**
	 * @return
	 */
	protected long getStartAddress() {
		logger.trace("getStartAddress()");
		
		String startAddressString =  binReaderGui.getTxtStartAddress();
		
		if(startAddressString.isBlank() == true) {
			
			return -1;
			
		}
		
		long startAddress = 0;
		
		if(startAddressString.matches("^0[xX].*")) {
			
			startAddress = Long.parseLong(startAddressString.replaceAll("^0[xX]", ""), 16);
					
		}
		else {
			
			startAddress = Long.parseLong(startAddressString);

		}
		
		return startAddress;
		
	} // getStartAddress()


	@Override
	public void stateChanged(ChangeEvent e) {
		logger.trace("stateChanged(): e = {}", e);
		
	} // stateChanged()


} // ssalc
