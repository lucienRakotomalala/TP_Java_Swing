/**
 * Here, we establish a specialised frame that will operate as the main
 *	application screen. A window listener handles the correct closing
 *	procedure.
 *
 * The frame is fully populated with a panel which ultimately carries three
 *	subcomponents.
 *
 * The frame also sports a menubar with File and Help menus. The corresponding
 *	menu items are associated with specific listener objects.
 *
 *  This class implements the singleton design pattern.
 *
 *  The class now implements the PropertyChangeListener interface. It awaits
 *	a change event advising that the user has changed the model and that
 *	the save/save as buttons be enabled.
 *
 * @author	K Barclay
 */

package window;



import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.border.*;

import collie.Collie;



public class CollieFrame	extends JFrame implements PropertyChangeListener {

    public static CollieFrame		getCollieFrame()	{ return collieFrame; }
    
    
    
//
//  Source:	CollieModelPanel
//  Event:	MODEL_DIRTIED_CHANGED_PROPERTY
//
//  If the model has been dirtied, then we enable the save and save as menu items
//	and enable the save toolbar button.
//
    public void				propertyChange(PropertyChangeEvent event) {
    	if(event.getPropertyName().equals(CollieModelPanel.MODEL_DIRTIED_CHANGED_PROPERTY)) {
    	    fileSaveAction.setEnabled(true);
    	    fileSaveAsAction.setEnabled(true);
    	}
    }
    
    
    
    protected				CollieFrame(String captionTitle) {
        super(captionTitle);

	Container contentPane = this.getContentPane();
	contentPane.setLayout(new BorderLayout());
	ColliePanel colliePanel = ColliePanel.getColliePanel(); //new ColliePanel();
	contentPane.add(colliePanel, BorderLayout.CENTER);

	assembleMenuAndToolBar();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width * 3 / 4;
        int height = screen.height * 3 / 4;
        this.setBounds(screen.width / 8, screen.height / 8, width, height);
        this.setVisible(true);
        
        colliePanel.setDivider();

        
    }



// ---------- statics -------------------------------------

    private static final Boolean FALSE		= new Boolean(false);
    private static final Boolean TRUE		= new Boolean(true);
    
    
    
// ---------- properties ----------------------------------

    private static CollieFrame	collieFrame	= new CollieFrame("COLLIE: Collaboration diagrammer");
    
    private JMenuBar		menuBar	= new JMenuBar();
    private JToolBar		toolBar	= new JToolBar();



// ---------- private -------------------------------------
    
    //
    // Menu items listener objects
    //
    private FileNewAction		fileNewAction 		= new FileNewAction("New",	new ImageIcon(Collie.class.getResource("/images/newButton_large.gif")));
    private FileOpenAction		fileOpenAction 		= new FileOpenAction("Open...",	new ImageIcon(Collie.class.getResource("/images/openButton_large.gif")));
    private FileSaveAction		fileSaveAction 		= new FileSaveAction("Save",	new ImageIcon(Collie.class.getResource("/images/saveButton_large.gif")));
    private FileSaveAsAction		fileSaveAsAction	= new FileSaveAsAction("Save As...");
    private FileExitAction		fileExitAction 		= new FileExitAction("Exit");

    private HelpAboutAction		helpAboutAction 	= new HelpAboutAction("About...");

    //
    //  Menu tables:
    //
    //	menu name		menu mnemonic					(first row)
    //	menu item action	menu item mnemonic	has 	enabled? 	(subsequent rows)
    //							toolbar
    //							button?
    //
    private Object[][] fileMenus = {
    	{ "File",		new Character('F') 			},
    	{ fileNewAction,	new Character('N'),	TRUE,	TRUE	},
    	{ fileOpenAction,	new Character('O'),	TRUE,	TRUE	},
    	{ fileSaveAction,	new Character('S'),	TRUE,	FALSE	},
    	{ fileSaveAsAction,	new Character('a'),	FALSE,	FALSE	},
    	{ null								},
    	{ fileExitAction,	new Character('x'),	FALSE,	TRUE	}
    };
    
    private Object[][] helpMenus = {
    	{ "Help",		new Character('H')			},
    	{ helpAboutAction,	new Character('a'),	FALSE,	TRUE	}
    };
    
    private Object[] menus = {
    	fileMenus,
    	helpMenus
    };




    //
    //  Assemble the various menus and for some of the menu items prepare
    //	  matching toolbar buttons. Attach the menu and the toolbar to the
    //    frame.
    //
    private void			assembleMenuAndToolBar() {
    	menuBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    	
    	for(int k = 0; k < menus.length; k++)
    	    assembleMenu((Object[][])menus[k]);
    	    
    	this.setJMenuBar(menuBar);
	
	toolBar.setFloatable(false);
	this.getContentPane().add(toolBar, BorderLayout.NORTH);
    }
    
    
    
    //
    //  Extract the menu name and mnemonic from the first row. Create a
    //	  menu, add it to the menubar, and add a toolbar separator. For
    //	  each subsequent row, prepare a menu item and, if necessary, a
    //	  toolbar button. Add each to their respective container.
    //
    private void	assembleMenu(Object[][] menuData) {
    	String menuName = (String)menuData[0][0];
    	Character menuMnemonic = (Character)menuData[0][1];
    	JMenu menu = new JMenu(menuName);
    	menu.setMnemonic(menuMnemonic.charValue());
    	
    	for(int k = 1; k < menuData.length; k++) {
    	    if(menuData[k][0] == null)
    	    	menu.addSeparator();
    	    else {
    	    	Action menuItemAction = (Action)menuData[k][0];
    	    	Character menuItemMnemonic = (Character)menuData[k][1];
    	    	Boolean menuItemHasButton = (Boolean)menuData[k][2];
    	    	Boolean menuItemEnabled = (Boolean)menuData[k][3];
    	    	menuItemAction.setEnabled(menuItemEnabled.booleanValue());
    	    	JMenuItem menuItem = menu.add(menuItemAction);
    	    	menuItem.setMnemonic(menuItemMnemonic.charValue());
    	    	if(menuItemHasButton.booleanValue() == true) {
    	    	    JButton button = toolBar.add(menuItemAction);
    	    	    button.setActionCommand((String)menuItemAction.getValue(Action.NAME));
    	    	    button.setToolTipText((String)menuItemAction.getValue(Action.NAME));
    	    	}
    	    }
    	}
    	
    	menuBar.add(menu);
    	toolBar.addSeparator();
    }


// ---------- inner class ---------------------------------

    class HelpAboutAction extends AbstractAction {

    	public				HelpAboutAction(String label) {
	    super(label);
    	}



    	public void				actionPerformed(ActionEvent event) {
	    String message = "COLLIE\n";
	    message += "Author  : K Barclay\n";
	    message += "Version : 1\n";
	    message += "Date    : October 2001";
	    JOptionPane.showMessageDialog(CollieFrame.this, message, "COLLIE", JOptionPane.INFORMATION_MESSAGE);
    	}
    	
    }	// class HelpAboutAction



// ---------- action classes ------------------------------
//METTRE VOS CLASSES ACTION ICI
   

}	// class CollieFrame

