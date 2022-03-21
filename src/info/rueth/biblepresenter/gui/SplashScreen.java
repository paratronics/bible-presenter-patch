/*
 * Created on Apr 25, 2004
 * $Id: SplashScreen.java,v 1.5 2004/04/30 22:55:09 Ulrich Rueth Exp $
 */
package info.rueth.biblepresenter.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * Simple splash screen. It is created in a static initializer inside the
 * console class and stays up for the given amount of seconds passed in on
 * construction.
 */
class SplashScreen extends Window
{
	//**********************************************************
	// Constructors
	//**********************************************************
	
	/**
	 * Only constructor for splash screen.
	 * 
	 * @param frame
	 *            the frame for this window, if available
	 * @param image
	 *            the image to display
	 */
	SplashScreen(Frame frame, ImageIcon image, String initialMessage)
	{
		super((null == frame) ? new Frame() : frame);
		try
		{
			setLayout(new BorderLayout());
			imageLabel = new JLabel(image); 
			add(imageLabel, BorderLayout.CENTER);
			this.statusPanel = new JPanel();
			statusPanel.setBackground(Color.RED);
			statusPanel.add(new JLabel(initialMessage));
			add(statusPanel, BorderLayout.SOUTH);
			displaySplash();
		}
		catch (Exception e)
		{
			System.out.println("Exception in SplashScreen constructor - " + e);
			// don't try to do anything else if there was a problem.
			return;
		}
		catch (Error e)
		{
			System.out.println("Error in SplashScreen constructor - " + e);
			// don't try to do anything else if there was a problem.
			return;
		}

		// interrupt the thread if the mouse is clicked.
		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				kill();
			}
		});

		addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
				{
					kill();
				}
			}
		});

		// change the cursor to the wait cursor
		//startWaitCursor();
	}
	
	//**********************************************************
	// Public methods
	//**********************************************************

	/**
	 * @see java.awt.Component#setVisible(boolean)
	 */
	public void setVisible(final boolean visible)
	{
		Runnable runner = new Runnable()
		{
			public void run()
			{
				superSetVisible(visible);
			}
		};

		Utilities.runOnEventThreadLater(runner);
	}
	
	/**
	 * Set the splash screen background, icons panel and redisplay window.
	 */
	synchronized public void changeSplashImage(final ImageIcon icon)
	{
		Runnable runner = new Runnable()
		{
			public void run()
			{
				m_icon = icon;
				m_splashHeight = m_icon.getIconHeight();
				m_splashWidth = m_icon.getIconWidth();

				// set the window size based on the size of splash image
				setSize(m_splashWidth, m_splashHeight + m_iconsHeight + 12);
				//D.out("Splash screen w,h:
				// "+m_icon.getIconWidth()+","+m_icon.getIconHeight());

				// define splashlabel layout
				imageLabel = new JLabel(m_icon)
				{
					public void paintComponent(Graphics g)
					{
						super.paintComponent(g);
						int tleft = 76;
						int ttop = 133;
						Font f = g.getFont();
						Font newFont = new Font(f.getName(), f.getStyle(), f
								.getSize() - 2);
						g.setFont(newFont);
						//                        g.drawString(Version.getDisplayVersion(),
						// this.getInsets().left+tleft,
						// this.getInsets().top+ttop);
					}
				};

				displaySplash();
			}
		};

		Utilities.runOnEventThreadLater(runner);
	}

	/**
	 * Add text to the bottom of the splash screen.
	 */
	synchronized public void addText(final String msg)
	{
		Runnable runner = new Runnable()
		{
			public void run()
			{
				statusPanel.removeAll();

				// clear and setup the icon panel
				JLabel txtLabel = new JLabel(msg);

				Dimension d = txtLabel.getPreferredSize();
				d.height = m_iconsHeight;
				txtLabel.setPreferredSize(d);
				statusPanel.add(txtLabel);

				// redisplay icon panel on splash window
				statusPanel.doLayout();
				statusPanel.repaint();
			}
		};

		Utilities.runOnEventThreadLater(runner);
	}

	/**
	 * Add an icon to the bottom of the splash screen.
	 */
	synchronized public void addIcon(final ImageIcon icon)
	{
		Runnable runner = new Runnable()
		{
			public void run()
			{
				int numIcons = m_splashWidth / m_iconsWidth;

				if (m_iconsIdx < numIcons)
				{
					//D.out("Adding icon " + icon);
					m_icons[m_iconsIdx] = icon;
					m_iconsIdx++;
				}
				else
				{
					// move everything down one and then insert it
					for (int i = 0; i < numIcons - 1; i++)
					{
						m_icons[i] = m_icons[i + 1];
					}
					m_icons[numIcons - 1] = icon;
					m_bLayout = true;
				}


			}
		};

		Utilities.runOnEventThreadLater(runner);
	}

	/**
	 * Kill the splash screen.
	 */
	synchronized public void kill()
	{
		Runnable runner = new Runnable()
		{
			public void run()
			{
				if (!m_bKilled)
				{ // If the splashScreen has not been killed before
					dispose();
					m_bKilled = true;

					// set the cursor back to the default cursor
					//endWaitCursor();
				}
			}
		};

		Utilities.runOnEventThreadLater(runner);
	}
	
	//**********************************************************
	// Private methods
	//**********************************************************

	private void superSetVisible(boolean visible)
	{
		super.setVisible(visible);
	}

	// Sets the dialog cursor to a wait cursor.
	private void startWaitCursor()
	{
		if (!m_bWaiting)
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			m_bWaiting = true;
		}
	}

	// Sets the cursor back to normal from a wait cursor.
	private void endWaitCursor()
	{
		if (m_bWaiting)
		{
			m_bWaiting = false;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}


	/**
	 * Setup the window layout and display it.
	 */
	synchronized private void displaySplash()
	{
		removeAll(); // remove all containers from window

		// add mainLabel to window
		add(imageLabel, BorderLayout.CENTER);
		add(statusPanel, BorderLayout.SOUTH);
		pack();


		// Center the dialog over the window
		Utilities.centerWindow(this);
	}

	//**********************************************************
	// Private variables
	//**********************************************************
	
	private static final int m_iconsHeight = 30;
	private static final int m_iconsWidth = 30;

	private int m_splashWidth;
	private int m_splashHeight;
	private ImageIcon m_icon; // splash image
	private ImageIcon[] m_icons = new ImageIcon[100]; // product icons
	private JLabel imageLabel;
	private JPanel statusPanel;
	private boolean m_bKilled = false;
	private int m_iconsIdx = 0; // number of product icons
	private boolean m_bLayout = false;
	private boolean m_bWaiting = false;

}