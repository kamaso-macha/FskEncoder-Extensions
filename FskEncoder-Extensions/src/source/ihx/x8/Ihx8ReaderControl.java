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


package source.ihx.x8;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

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

public class Ihx8ReaderControl extends ReaderExtensionControl {
	
	private Logger logger = LogManager.getLogger(Ihx8ReaderControl.class.getName());
	
	@SuppressWarnings("unused")
	private StatusMessenger statusMessenger;
	protected Ihx8ReaderGui ihxReaderGui;
	
	private Reader inputReader;
	protected MemoryMap memoryMap;


	/**
	 * @param aStatusMessenger 
	 * @param memoryMapGui
	 */
	public Ihx8ReaderControl(StatusMessenger aStatusMessenger) {
		logger.trace("Ihx8ReaderControl(): aStatusMessenger = {}", aStatusMessenger);

		if(aStatusMessenger == null) throw new IllegalArgumentException("aStatusMessenger can't be null");
		
		statusMessenger = aStatusMessenger;
		
		ihxReaderGui = new Ihx8ReaderGui(this);
		
	} // Ihx8ReaderControl()


	@Override
	public void actionPerformed(ActionEvent e) {
		logger.trace("actionPerformed(): e = {}", e);
		
		logger.trace("name: ", ((JCheckBox)e.getSource()).getName());

	} // actionPerformed()


	@Override
	public JPanel createLayout() {
		logger.trace("createLayout()");
		
		List<MemoryBlockDescription> memoryLayout = memoryMap.getMemoryLayout();
		logger.trace("createLayout(): memoryLayout.size = {}", memoryLayout.size());
		
		return ihxReaderGui.createLayout(memoryLayout);
		
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
		
		List<MemoryBlockDescription> selectedRegions = ihxReaderGui.getSelectedEntries();
		Collections.sort(selectedRegions);
		
		List<MemoryRegion> candidates = new ArrayList<>();
		
		MemoryRegion candidate = null;
		
		for(MemoryBlockDescription mbd : selectedRegions) {
			
			candidate = memoryMap.getMemoryRegion(mbd.START_ADDRESS);
			logger.trace("candidate = {}", candidate);
			candidates.add(candidate);
			
		}
		
		logger.trace("getSelectedMemoryRegions(): candidates.size = {}", candidates.size());
		
		return candidates; 
		
	} // getSelectedEntries()


	/**
	 * @return
	 */
	// FIXME: Refactor Ihx8ReaderExtension to remove this method.
	public ExtensionGui getGui() {
		logger.trace("getGui()");
		
		return ihxReaderGui;
		
	} // getSelectedEntries()


} // ssalc
