/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : Gui.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.model.MemoryBlockDescription;
import extension.view.gui.MemoryMapGui;
import net.miginfocom.swing.MigLayout;

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
// Created at 2024-05-08 17:26:55

public class Ihx8ReaderGui extends MemoryMapGui {

	private static final long serialVersionUID = 1L;

	Logger logger = LogManager.getLogger(Ihx8ReaderGui.class.getName());
	
	protected Ihx8ReaderControl memoryMapController;
	
	protected Map<JCheckBox, MemoryBlockDescription> chkBoxMap;
	

	public Ihx8ReaderGui(final Ihx8ReaderControl aMemoryMapController) {
		logger.trace("Ihx8ReaderGui(): aMemoryMapController = {}", aMemoryMapController);

		memoryMapController = aMemoryMapController;
		
		chkBoxMap = new HashMap<>();
		
	} // Ihx8ReaderGui()

	
	/**
	 * 
	 */
	protected JPanel createComponents(final int aRegionIndex) {

		MemoryBlockDescription memoryBlock = memoryMap.get(aRegionIndex);
		
		JLabel lblMemoryRegion = new JLabel("Region " + (aRegionIndex + 1));
		
		JCheckBox cbxSelected = new JCheckBox();
		cbxSelected.setName("cbxSelected_" + aRegionIndex);
		if(aRegionIndex == 0
		&& memoryMap.size() == 1) {
			cbxSelected.setEnabled(false);
			cbxSelected.setSelected(true);
		}

		chkBoxMap.put(cbxSelected, memoryBlock);
		
		JTextField txtStartAddress = new JTextField();
		txtStartAddress.setName("txtStartAddress_" + aRegionIndex);
		txtStartAddress.setColumns(10);
		txtStartAddress.setEditable(false);
		txtStartAddress.setFocusable(false);
		txtStartAddress.setFocusTraversalKeysEnabled(false);
		txtStartAddress.setText(String.format("0x%04X", memoryBlock.START_ADDRESS));
		
		JTextField txtEndAddress = new JTextField();
		txtEndAddress.setName("txtEndAddress_" + aRegionIndex);
		txtEndAddress.setColumns(10);
		txtEndAddress.setEditable(false);
		txtEndAddress.setFocusable(false);
		txtEndAddress.setFocusTraversalKeysEnabled(false);
		txtEndAddress.setText(String.format("0x%04X", memoryBlock.END_ADDRESS));
		
		JTextField txtSize = new JTextField();
		txtSize.setName("txtSize_" + aRegionIndex);
		txtSize.setColumns(10);
		txtSize.setEditable(false);
		txtSize.setFocusable(false);
		txtSize.setFocusTraversalKeysEnabled(false);
		txtSize.setText(String.format("0x%02X", memoryBlock.SIZE));
		
		JPanel detail = new JPanel();
		detail.setLayout(new MigLayout("insets 0 0 0 0", "[100px,left] 20 [60] 20 [60] 25 [60] 25 [60]", ""));
		
		detail.add(lblMemoryRegion, "growx"); // NOSONAR
		detail.add(cbxSelected, "gapleft 10");
		detail.add(txtStartAddress, "growx");
		detail.add(txtEndAddress, "growx");
		detail.add(txtSize, "growx");
		
		return detail;

	} // createComponents()
		
	
	protected JPanel createHeading() {
		logger.trace("createHeading()");
		
		JLabel lblMemoryRegion = new JLabel("Region");
		JLabel lblSelect = new JLabel("Select");
		JLabel lblStartAddress = new JLabel("Start Adr");
		JLabel lblEndAddress = new JLabel("End Adr");
		JLabel lblSize = new JLabel("Size");

		JPanel heading = new JPanel();
		heading.setLayout(new MigLayout("insets 0 0 0 0", "[100px,left] 20 [60] 20 [60] 25 [60] 25 [60]", ""));
		
		heading.add(lblMemoryRegion, "growx");
		heading.add(lblSelect, "growx");
		heading.add(lblStartAddress, "growx");
		heading.add(lblEndAddress, "growx");
		heading.add(lblSize, "growx");
		
		return heading;
		
	} // createHeading()
	
	
	protected JPanel createDetails() {
		logger.trace("createDetails():");
		
		JPanel details = new JPanel();
		details.setLayout(new MigLayout("wrap 1"));
		
		JPanel candidate = new JPanel();
		
		for(int n = 0; n < memoryMap.size(); n++) {
			
			candidate = createComponents(n);
			details.add(candidate);
			
		} // rof
		
		return details;
		
	} // createDetails()
	
	
	public JPanel createLayout(List<MemoryBlockDescription> aMemoryLayout) {
		logger.trace("createLayout(): aMemoryLayout = {}", aMemoryLayout);
		
		memoryMap = aMemoryLayout;

		logger.trace("createLayout(): memoryMap.size = {}, chkBoxMap.size = {}", 
				memoryMap.size(), chkBoxMap.size());

		JPanel memoryMapPanel = new JPanel();		
		memoryMapPanel.setLayout(new MigLayout("wrap 1"));
		
		memoryMapPanel.add(createHeading(), "growx");
		
		chkBoxMap.clear();
		memoryMapPanel.add(createDetails(), "growx");
		memoryMapPanel.add(new JSeparator(), "growx,gaptop 20");
	
		return memoryMapPanel;
		
	} // createLayout()
	

	protected List<MemoryBlockDescription> getSelectedEntries() {
		logger.trace("getSelectedEntries()");
		
		List<MemoryBlockDescription> selectedRegions = new ArrayList<>();
		
		for(Entry<JCheckBox, MemoryBlockDescription> entry : chkBoxMap.entrySet()) {
			
			if(entry.getKey().isSelected()) {
				logger.trace("key = {}, entry = {}", entry.getKey(), entry.getValue());
				selectedRegions.add(entry.getValue());
			}
			
		} // rof
		
		return selectedRegions;
		
	} // getSelectedEntries()


} // ssalc
