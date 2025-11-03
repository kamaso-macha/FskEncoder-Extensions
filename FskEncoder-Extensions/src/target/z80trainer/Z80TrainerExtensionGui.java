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


package target.z80trainer;

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

public class Z80TrainerExtensionGui extends ExtensionGui {

	private static final long serialVersionUID = 1L;

	private Logger logger = LogManager.getLogger(Z80TrainerExtensionGui.class.getName());
	
	protected Z80TrainerExtensionControl targetExtensioController;

	protected JLabel lblProgramNumber;
	protected JTextField txtProgramNumber;
	protected JLabel lblTarget;

	
	public Z80TrainerExtensionGui(Z80TrainerExtensionControl aTargetExtensioController) {
		
		logger.trace("Z80TrainerExtensionGui()");

		targetExtensioController = aTargetExtensioController;
		
		createComponents();
		createLayout();
		
	} // Z80TrainerExtensionGui()


	/**
	 * 
	 */
	protected void createComponents() {

		lblProgramNumber = new JLabel("Program number");
		
		txtProgramNumber = new JTextField();
		txtProgramNumber.setName("txtProgramNumber");
		txtProgramNumber.addFocusListener(targetExtensioController);
		txtProgramNumber.addActionListener(targetExtensioController);
		txtProgramNumber.setHorizontalAlignment(SwingConstants.RIGHT);

		lblTarget = new JLabel();
		lblTarget.setName("lblTarget");
		
	} // createComponents()

	/**
	 * 
	 */
	public JPanel createLayout() {

		createComponents();
		
		JPanel detail = new JPanel();
		detail.setLayout(new MigLayout("", "[100px,left] 20 [50] 30 [230]", "[] 20 []"));
		
		detail.add(lblProgramNumber);
		detail.add(txtProgramNumber, "growx");
		detail.add(lblTarget, "growx, wrap");
		
		detail.add(new JSeparator(), "cell 0 1, growx, spanx ");
	
		return detail;

	} // createLayout()
	
	
	
	public String getTxtProgrammNumberText() { return txtProgramNumber.getText(); }

	public void setLblTargetText(final String aText) { lblTarget.setText(aText); }
	
	public void setTxtProgrammNumberText(final String aText) { 
		logger.trace("setTxtProgrammNumberText(): aText = {}", aText);
		
		txtProgramNumber.setText(aText);
		
	} // setTxtProgrammNumberText()


} // ssalc
