package PurpleGooseTimer;

import java.awt.BorderLayout; // Import necessary classes for GUI layout
import java.awt.Color; // Import necessary classes for color
import java.awt.Dimension; // Import necessary classes for dimension
import java.awt.Font; // Import necessary classes for font
import java.awt.GridLayout; // Import necessary classes for grid layout
import java.util.Timer; // Import Timer class for scheduling tasks
import java.util.TimerTask; // Import TimerTask class for defining tasks to be scheduled

import javax.swing.BorderFactory; // Import BorderFactory class for creating borders
import javax.swing.ImageIcon; // Import ImageIcon class for loading images
import javax.swing.JButton; // Import JButton class for buttons
import javax.swing.JFrame; // Import JFrame class for creating windows
import javax.swing.JLabel; // Import JLabel class for displaying text or images
import javax.swing.JPanel; // Import JPanel class for grouping components
import javax.swing.JSpinner; // Import JSpinner class for spinner input
import javax.swing.SpinnerNumberModel; // Import SpinnerNumberModel class for spinner model
import javax.swing.SwingConstants; // Import SwingConstants class for constants used in Swing
import javax.swing.SwingUtilities; // Import SwingUtilities class for accessing Swing utilities
import javax.swing.WindowConstants; // Import WindowConstants class for window constants

public class TimerApp extends JFrame { // Define TimerApp class extending JFrame
	private static final long serialVersionUID = 1L; // Serial version UID for serialization

	private Timer timer; // Timer object for scheduling tasks
	private TimerTask timerTask; // TimerTask object for defining tasks
	private JLabel timerLabel; // JLabel for displaying timer
	private JButton startButton; // JButton for starting/pausing/resuming timer
	private JButton stopButton; // JButton for stopping timer
	private JSpinner hourSpinner; // JSpinner for selecting hours
	private JSpinner minuteSpinner; // JSpinner for selecting minutes
	private int seconds; // Variable to store total seconds
	private boolean isRunning; // Flag to track timer state
	private boolean isPaused; // Flag to track pause state

	public TimerApp() { // Constructor
		setTitle("PurpleGooseTimer"); // Set window title
		setSize(400, 200); // Set window size
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Set default close operation
		setLocationRelativeTo(null); // Center window on screen

		JPanel titleBar = new JPanel(); // Create JPanel for custom title bar
		titleBar.setPreferredSize(new Dimension(getWidth(), 30)); // Set title bar height
		titleBar.setBackground(new Color(90, 0, 128)); // Set title bar background color

		JLabel titleLabel = new JLabel("PurpleGooseTimer"); // Create JLabel for title text
		titleLabel.setForeground(Color.WHITE); // Set text color to white
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align text

		titleBar.add(titleLabel); // Add title label to title bar
		add(titleBar, BorderLayout.NORTH); // Add title bar to frame's north region

		JPanel topPanel = new JPanel(new GridLayout(2, 2)); // Create JPanel for spinner inputs
		topPanel.setBackground(new Color(90, 0, 128)); // Set background color
		JLabel hourLabel = new JLabel("Hours:"); // Create JLabel for hours
		hourLabel.setForeground(Color.WHITE); // Set text color to white

		hourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1)); // Create JSpinner for hours
		Dimension spinnerSize = new Dimension(40, 30); // Set spinner size
		hourSpinner.setPreferredSize(spinnerSize); // Set preferred size for spinner

		topPanel.add(hourLabel); // Add hour label to top panel
		topPanel.add(hourSpinner); // Add hour spinner to top panel

		JLabel minuteLabel = new JLabel("Minutes:"); // Create JLabel for minutes
		minuteLabel.setForeground(Color.WHITE); // Set text color to white

		minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1)); // Create JSpinner for minutes
		minuteSpinner.setPreferredSize(spinnerSize); // Set preferred size for spinner

		topPanel.add(minuteLabel); // Add minute label to top panel
		topPanel.add(minuteSpinner); // Add minute spinner to top panel

		add(topPanel, BorderLayout.NORTH); // Add top panel to frame's north region

		JPanel centerPanel = new JPanel(new BorderLayout()); // Create JPanel for timer label
		centerPanel.setBackground(new Color(90, 0, 128)); // Set background color
		timerLabel = new JLabel("00:00:00", SwingConstants.CENTER); // Create JLabel for timer text
		timerLabel.setFont(new Font("Arial", Font.BOLD, 50)); // Set font size and style
		timerLabel.setForeground(Color.WHITE); // Set text color to white

		centerPanel.add(timerLabel, BorderLayout.CENTER); // Add timer label to center panel
		add(centerPanel, BorderLayout.CENTER); // Add center panel to frame's center region

		JPanel buttonPanel = new JPanel(new BorderLayout()); // Create JPanel for buttons
		buttonPanel.setBackground(new Color(90, 0, 128)); // Set background color

		startButton = new JButton("Start"); // Create JButton for starting/pausing/resuming timer
		startButton.setForeground(Color.WHITE); // Set text color to white
		startButton.setBackground(new Color(90, 0, 128)); // Set background color
		startButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3)); // Set border color and thickness
		startButton.addActionListener(e -> toggleTimer()); // Add action listener for button click
		startButton.setPreferredSize(new Dimension(150, 50)); // Set preferred size for button

		buttonPanel.add(startButton, BorderLayout.WEST); // Add start button to west side of button panel

		stopButton = new JButton("Stop"); // Create JButton for stopping timer
		stopButton.setForeground(Color.WHITE); // Set text color to white
		stopButton.setBackground(new Color(90, 0, 128)); // Set background color
		stopButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3)); // Set border color and thickness
		stopButton.addActionListener(e -> stopTimer()); // Add action listener for button click
		stopButton.setPreferredSize(new Dimension(150, 50)); // Set preferred size for button

		buttonPanel.add(stopButton, BorderLayout.EAST); // Add stop button to east side of button panel

		add(buttonPanel, BorderLayout.SOUTH); // Add button panel to frame's south region

		ImageIcon icon = new ImageIcon(TimerApp.class.getResource("/resource/GooseTimer.png")); // Load icon image
		if (icon != null) { // Check if icon image exists
			setIconImage(icon.getImage()); // Set window icon to loaded image
		} else {
			System.err.println("Icon image not found or could not be loaded."); // Log error if icon image not found
		}

		timer = new Timer(); // Initialize timer
		isRunning = false; // Set initial running state to false
		isPaused = false; // Set initial pause state to false
	}

	private void toggleTimer() { // Method to toggle timer start/pause/resume
		if (!isRunning) { // If timer is not running
			startTimer(); // Start the timer
			isRunning = true; // Update running state
			isPaused = false; // Update pause state
			startButton.setText("Pause"); // Update button text
		} else { // If timer is running
			if (isPaused) { // If timer is paused
				resumeTimer(); // Resume the timer
			} else { // If timer is not paused
				pauseTimer(); // Pause the timer
			}
		}
	}

	private void startTimer() { // Method to start the timer
		if (timerTask != null) { // If timer task already exists
			return; // Do nothing
		}

		int hours = (int) hourSpinner.getValue(); // Get hours from spinner
		int minutes = (int) minuteSpinner.getValue(); // Get minutes from spinner
		seconds = hours * 3600 + minutes * 60; // Calculate total seconds
		updateTimerLabel(); // Update timer label

		timerTask = new TimerTask() { // Define new timer task
			@Override
			public void run() { // Define task to be executed
				seconds--; // Decrement remaining seconds
				if (seconds <= 0) { // If time is up
					stopTimer(); // Stop the timer
				} else { // If time is not up
					updateTimerLabel(); // Update timer label
				}
			}
		};

		timer.scheduleAtFixedRate(timerTask, 0, 1000); // Schedule timer task to run every second
	}

	private void pauseTimer() { // Method to pause the timer
		if (timerTask != null) { // If timer task exists
			timerTask.cancel(); // Cancel the timer task
			isPaused = true; // Update pause state
			startButton.setText("Resume"); // Update button text
		}
	}

	private void resumeTimer() { // Method to resume the timer
		if (timerTask != null && isPaused) { // If timer task exists and timer is paused
			timerTask = new TimerTask() { // Define new timer task
				@Override
				public void run() { // Define task to be executed
					seconds--; // Decrement remaining seconds
					if (seconds <= 0) { // If time is up
						stopTimer(); // Stop the timer
					} else { // If time is not up
						updateTimerLabel(); // Update timer label
					}
				}
			};

			timer.scheduleAtFixedRate(timerTask, 0, 1000); // Schedule timer task to run every second
			isPaused = false; // Update pause state
			startButton.setText("Pause"); // Update button text
		}
	}

	private void stopTimer() { // Method to stop the timer
		if (timerTask != null) { // If timer task exists
			timerTask.cancel(); // Cancel the timer task
			timerTask = null; // Reset timer task
			isRunning = false; // Update running state
			isPaused = false; // Update pause state
			startButton.setText("Start"); // Update button text
			updateTimerLabel(); // Update timer label
		}
	}

	private void updateTimerLabel() { // Method to update the timer label
		int hours = seconds / 3600; // Calculate remaining hours
		int minutes = (seconds % 3600) / 60; // Calculate remaining minutes
		int secs = seconds % 60; // Calculate remaining seconds
		String time = String.format("%02d:%02d:%02d", hours, minutes, secs); // Format time as HH:MM:SS
		timerLabel.setText(time); // Update timer label
	}

	public static void main(String[] args) { // Main method
		SwingUtilities.invokeLater(() -> { // Execute GUI-related code on Event Dispatch Thread
			TimerApp timerApp = new TimerApp(); // Create instance of TimerApp
			timerApp.setAlwaysOnTop(true); // Set window to always be on top
			timerApp.setVisible(true); // Make window visible
		});
	}
}
