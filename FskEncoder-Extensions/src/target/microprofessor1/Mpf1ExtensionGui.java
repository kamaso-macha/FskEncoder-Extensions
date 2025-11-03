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


package target.microprofessor1;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.view.gui.ExtensionGui;
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

public class Mpf1ExtensionGui extends ExtensionGui {

	private static final long serialVersionUID = 1L;

	private Logger logger = LogManager.getLogger(Mpf1ExtensionGui.class.getName());
	
	protected Mpf1ExtensionControl targetExtensioController;

	protected JLabel lblFileName;
	protected JTextField txtFileName;
	protected JLabel lblTarget;

	
	public Mpf1ExtensionGui(Mpf1ExtensionControl aTargetExtensioController) {
		
		logger.trace("TargetExtensionGui()");

		targetExtensioController = aTargetExtensioController;
		
		createComponents();
		createLayout();

	} // OutputDeviceGui()


	/**
	 * 
	 */
	protected void createComponents() {

		lblFileName = new JLabel("File Name");
		
		txtFileName = new JTextField();
		txtFileName.setName("txtFileName");
		txtFileName.addFocusListener(targetExtensioController);
		txtFileName.addActionListener(targetExtensioController);
		txtFileName.setHorizontalAlignment(SwingConstants.RIGHT);

		lblTarget = new JLabel();
		lblTarget.setName("lblTarget");
		
	} // createComponents()
	
	
	/**
	 * 
	 */
	protected JPanel createLayout() {
		
		JPanel detail = new JPanel();

		detail.setLayout(new MigLayout("", "[100px,left] 20 [50] 30 [230]", "[] 20 []"));
		
		detail.add(lblFileName);
		detail.add(txtFileName, "growx");
		detail.add(lblTarget, "growx, wrap");
		
		detail.add(new JSeparator(), "cell 0 1, growx, spanx ");

		return detail;

	} // createLayout()
	
	
	public String getTxtFileNameTxt() { return txtFileName.getText(); }

	public void setLblTargetText(final String aText) { lblTarget.setText(aText); }
	public void setTxtFileNameText(final String aText) { txtFileName.setText(aText); }


} // ssalc
