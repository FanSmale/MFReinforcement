package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.JComboBox;

import vlearning.*;
import vlearning.environment.VTicTacToe;
import vlearning.umpire.VUmpire;
import vlearning.umpire.VUmpireDynamicProgramming;
import common.*;
import gui.guicommon.*;
import gui.guidialog.common.HelpDialog;
import gui.others.*;

/**
 * The GUI for TicTacToe.<br>
 * Project: Reinforcement learning.<br>
 * 
 * @author Fan Min<br>
 *         www.fansmale.com, github.com/fansmale/MFReinforcement.<br>
 *         Email: minfan@swpu.edu.cn, minfanphd@163.com.<br>
 *         Date Created: August 30, 2020.<br>
 *         Last modified: August 30, 2020.
 * @version 1.0
 */

public class ReinforcementGUI implements ActionListener, ItemListener {

	/**
	 * The number of rows.
	 */
	public static final int NUM_ROWS = 3;

	/**
	 * The properties for setting.
	 */
	private Properties settings = new Properties();

	/**
	 * Training scheme.
	 */
	private JComboBox<String> trainingSchemeComboBox;

	/**
	 * Number of base classifiers.
	 */
	private IntegerField trainingEpisodesField;

	/**
	 * Stop when the error reaches 0.
	 */
	private Checkbox[] selectionCheckboxArray;

	/**
	 * Checkbox for variable tracking.
	 */
	private Checkbox processTrackingCheckbox;

	/**
	 * Checkbox for variable tracking.
	 */
	private Checkbox variableTrackingCheckbox;

	/**
	 * The message area.
	 */
	private TextArea messageTextArea;

	/**
	 * The umpire.
	 */
	VUmpire vUmpire;

	/**
	 *************************** 
	 * The only constructor.
	 *************************** 
	 */
	public ReinforcementGUI() {
		vUmpire = null;

		// A simple frame to contain dialogs.
		Frame mainFrame = new Frame();
		mainFrame.setTitle(
				"TicTacToe through reinforcement learning. minfan@swpu.edu.cn, minfanphd@163.com");

		// Step 1. The top part.
		String[] tempTrainingSchemes = { "V-learning", "Dynamic programming" };
		trainingSchemeComboBox = new JComboBox<String>(tempTrainingSchemes);
		Panel tempTrainingSchemesPanel = new Panel();
		tempTrainingSchemesPanel.add(new Label("Training scheme: "));
		tempTrainingSchemesPanel.add(trainingSchemeComboBox);

		trainingEpisodesField = new IntegerField("10000");
		Panel tempTrainingEpisodesPanel = new Panel();
		tempTrainingEpisodesPanel.add(new Label("Training rounds: "));
		tempTrainingEpisodesPanel.add(trainingEpisodesField);

		Panel tempSettingPanel = new Panel();
		tempSettingPanel.setLayout(new GridLayout(2, 1));
		tempSettingPanel.add(tempTrainingSchemesPanel);
		tempSettingPanel.add(tempTrainingEpisodesPanel);

		// Step 2. The play part.
		selectionCheckboxArray = new Checkbox[NUM_ROWS * NUM_ROWS];
		for (int i = 0; i < selectionCheckboxArray.length; i++) {
			selectionCheckboxArray[i] = new Checkbox("?");
			//selectionCheckboxArray[i].setSize(5, 5);
			selectionCheckboxArray[i].addItemListener(this);
			selectionCheckboxArray[i].setEnabled(false);
		} // Of for i
		Panel tempPlayPanel = new Panel();
		tempPlayPanel.setLayout(new GridLayout(NUM_ROWS, NUM_ROWS));
		for (int i = 0; i < NUM_ROWS * NUM_ROWS; i++) {
			tempPlayPanel.add(selectionCheckboxArray[i]);
		} // Of for i
		//tempPlayPanel.setSize(30, 30);

		// Step 3. The tracking part.
		processTrackingCheckbox = new Checkbox(" Process tracking ", false);
		variableTrackingCheckbox = new Checkbox(" Variable tracking ", false);
		Panel trackingPanel = new Panel();
		trackingPanel.add(processTrackingCheckbox);
		trackingPanel.add(variableTrackingCheckbox);

		Panel topPanel = new Panel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add("North", tempSettingPanel);
		topPanel.add("Center", trackingPanel);
		topPanel.add("South", tempPlayPanel);

		Panel centralPanel = new Panel();
		messageTextArea = new TextArea(30, 30);
		centralPanel.add(messageTextArea);

		Button trainButton = new Button(" Train ");
		trainButton.addActionListener(this);

		Button resetButton = new Button(" Reset ");
		// resetButton.addActionListener(new
		// GameResetter(selectionCheckboxArray));
		resetButton.addActionListener(new GameResetter());

		// DialogCloser dialogCloser = new DialogCloser(this);
		Button exitButton = new Button(" Exit ");
		// cancelButton.addActionListener(dialogCloser);
		exitButton.addActionListener(ApplicationShutdown.applicationShutdown);
		Button helpButton = new Button(" Help ");
		helpButton.setSize(20, 10);
		helpButton.addActionListener(new HelpDialog("AdaBoosting", "src/gui/ReinforcementHelp.txt"));
		Panel okPanel = new Panel();
		okPanel.add(trainButton);
		okPanel.add(resetButton);
		okPanel.add(exitButton);
		okPanel.add(helpButton);

		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(BorderLayout.NORTH, topPanel);
		mainFrame.add(BorderLayout.CENTER, centralPanel);
		mainFrame.add(BorderLayout.SOUTH, okPanel);

		mainFrame.setSize(300, 400);
		mainFrame.setLocation(200, 200);
		mainFrame.addWindowListener(ApplicationShutdown.applicationShutdown);
		mainFrame.setBackground(GUICommon.MY_COLOR);
		mainFrame.setVisible(true);
	}// Of the constructor

	/**
	 *************************** 
	 * Read the arff file.
	 *************************** 
	 */
	public void actionPerformed(ActionEvent ae) {
		Common.startTime = new Date().getTime();
		Common.runSteps = 0;
		messageTextArea.setText("Processing ... Please wait.\r\n");

		int tempScheme = trainingSchemeComboBox.getSelectedIndex();
		int tempEpisodes = trainingEpisodesField.getValue();

		SimpleTools.processTracking = processTrackingCheckbox.getState();
		SimpleTools.variableTracking = variableTrackingCheckbox.getState();

		String tempParametersInformation = "Training information: training scheme: " + tempScheme
				+ "\r\n  " + "Training episodes = " + tempEpisodes;
		messageTextArea.append(tempParametersInformation);

		// Now train
		switch (tempScheme) {
		case 0:
			vUmpire = new VUmpire();
			break;
		case 1:
			vUmpire = new VUmpireDynamicProgramming(0);
			break;
		default:
			System.out.println("Unsupported learner.");
			System.exit(0);
		}// Of switch

		vUmpire.train(tempEpisodes, 0.05, 0.1);

		for (int i = 0; i < selectionCheckboxArray.length; i++) {
			selectionCheckboxArray[i].setEnabled(true);
		} // Of for i
		messageTextArea.append("\r\nReady to play!");
		messageTextArea.append("\r\nPlese select a position at the 3 * 3 checkerboard.\r\n");
		
		resetForGame();
		//vUmpire.resetForGame();
	} // Of actionPerformed

	/**
	 *************************** 
	 * Reset for a new game.
	 *************************** 
	 */
	public void resetForGame() {
		for (int i = 0; i < selectionCheckboxArray.length; i++) {
			selectionCheckboxArray[i].setEnabled(true);
			selectionCheckboxArray[i].setLabel("?");
			selectionCheckboxArray[i].setState(false);
		} // Of for i
		vUmpire.resetForGame();
	}// Of resetForGame

	/**
	 *************************** 
	 * Lock the checkerboard so that the player cannot play any longer when the game is end.
	 *************************** 
	 */
	public void lockCheckerboard() {
		for (int i = 0; i < selectionCheckboxArray.length; i++) {
			selectionCheckboxArray[i].setEnabled(false);
		} // Of for i
	}// Of lockCheckerboard


	/**
	 *************************** 
	 * When the checkbox is selected or deselected.
	 *************************** 
	 */
	public void itemStateChanged(ItemEvent paraEvent) {
		Object tempObject = paraEvent.getSource();
		int tempSelection = -1;
		for (int i = 0; i < selectionCheckboxArray.length; i++) {
			if (tempObject == selectionCheckboxArray[i]) {
				tempSelection = i;
				break;
			} // Of if
		} // Of for i
		selectionCheckboxArray[tempSelection].setLabel("o");
		selectionCheckboxArray[tempSelection].setEnabled(false);
		
		//messageTextArea.append("You selected position " + tempSelection + "\r\n");
		int tempSituation = vUmpire.humanPlay(tempSelection);

		//messageTextArea.append("Now the situation is " + tempSituation + "\r\n");

		if (tempSituation == VTicTacToe.WHITE) {
			messageTextArea.append("You win!\r\n-------------\r\n");
			lockCheckerboard();
			return;
		} else if (tempSituation == VTicTacToe.TIE) {
			messageTextArea.append("It is a Tie.\r\n-------------\r\n");
			lockCheckerboard();
			return;
		} // Of if

		tempSituation = vUmpire.agentPlay();
		tempSelection = vUmpire.getRecentAction();
		//messageTextArea.append("The agent selected position " + tempSelection + "\r\n");
		// System.out.println("After agent play, tempSituation = " +
		// tempSituation
		// + ", tempSelection = " + tempSelection);
		selectionCheckboxArray[tempSelection].setLabel("x");
		selectionCheckboxArray[tempSelection].setEnabled(false);

		if (tempSituation == VTicTacToe.BLACK) {
			messageTextArea.append("You lose :(\r\n-------------\r\n");
			lockCheckerboard();
			return;
		} else if (tempSituation == VTicTacToe.TIE) {
			messageTextArea.append("It is a Tie.\r\n-------------\r\n");
			lockCheckerboard();
			return;
		} // Of if
	} // Of itemStateChanged

	
	/**
	 *************************** 
	 * The entrance method.
	 * 
	 * @param args
	 *            The parameters.
	 *************************** 
	 */
	public static void main(String args[]) {
		new ReinforcementGUI();
	} // Of main

	/**
	 *************************** 
	 * The game resetter.
	 *************************** 
	 */
	private class GameResetter implements ActionListener {
		/**
		 *************************** 
		 * Reset the game.
		 *************************** 
		 */
		public void actionPerformed(ActionEvent ae) {
			resetForGame();
		}// Of actionPerformed
	}// Of class Resetter

} // Of class ReinforcementGUI
