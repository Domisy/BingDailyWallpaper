/**
 * HelloWorld.java 
 * Project - Bing Daily Wallpaper
 * Copyright © 2012 Domisy Dev
 * Author - Theodore Mavrakis
 */

package com.rim.samples.device.helloworlddemo;


//import net.rim.blackberry.api.homescreen.HomeScreen;
import java.io.IOException;


import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;

import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.SystemListener;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.util.StringProvider;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class HelloWorldDemo extends UiApplication implements SystemListener
{
	public static HTTPInterface _httpInterface = new HTTPInterface(); 
	private static HelloWorldScreen _mainScreen;
    private static HelloWorldDemo theApp;
    	
	private HelloWorldDemo() {                
        pushScreen(new AppScreen(this));  
     
    }
	
	 
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
    	String deviceVersionString = DeviceInfo.getSoftwareVersion();
    	String deviceVersionSubString = deviceVersionString.substring(0, 3);
    	BingModel.deviceVersion = Double.parseDouble(deviceVersionSubString);
    	theApp = new HelloWorldDemo(); 
    	if (args != null && args.length > 0 && "startupEntry".equals(args[0]))
        {
    		
    		boolean isChecked = BingModel.retrieveState();
    		if(isChecked == true) {
    			 // If system startup is still in progress when this        
    			// application is run.        
    			if (ApplicationManager.getApplicationManager().inStartup()) {            
    				theApp.addSystemListener(theApp);        
    				} 
    			else {            
    				theApp.doStartupWorkLater();        
    				}        
    			theApp.enterEventDispatcher();
    		}
    		else
    			System.exit(0);
        }
    	else
    	{
    		//theApp = new HelloWorldDemo();
    		BingModel.isRunStartup = false;      
    		theApp.enterEventDispatcher();
    	}
    	
    }
    

    private void doStartupWorkLater() {        
		 invokeLater(new Runnable() {            
			 public void run() {                
				 doStartupWork();            
				 }        
			 });
	 }
	 
	private void doStartupWork() { 
		BingModel.isRunStartup = true;
		UiApplication.getUiApplication().requestBackground();
	}
	
	public void powerUp() {        
		removeSystemListener(this);        
		doStartupWork();    
	}
	 
	 
	 

    private static class AppScreen extends MainScreen 
    {
        UiApplication _app;
        final int scaleWidth = net.rim.device.api.system.Display.getWidth();
        final int scaleHeight = net.rim.device.api.system.Display.getHeight();

        
        /**
         * Default constructor.
         */
        public AppScreen(UiApplication app) {
	        //Layout for loading UI.
            _app = app;
            /*LabelField title = new LabelField("Bing Image Desktop",
                    DrawStyle.ELLIPSIS | Field.USE_ALL_WIDTH);
            setTitle(title);*/
    
            Bitmap spashScreen = Bitmap.getBitmapResource("img/Splashscreen.png");
            Bitmap newImage = new Bitmap(scaleWidth, scaleHeight); 
            spashScreen.scaleInto(newImage, Bitmap.FILTER_BILINEAR, Bitmap.SCALE_TO_FIT); 
        	BitmapField bmpField = new BitmapField(newImage);
            add(bmpField);

            // Queue up the loading process.
            startLoading();
            
        }               

        
        /**
         * Create the loading thread. Make sure to invoke later as you will
         * need to push a screen or show a dialog after the loading is complete, eventhough
         * you are creating the thread before the app is in the event dispatcher.
         */
        public void startLoading() { 
        	
            Thread loadThread = new Thread() {
                public void run() {
                	
                    // Make sure to invokeLater to avoid problems with the event thread.
                    try{ 
                    	//_mainScreen = new HelloWorldScreen();
                      
                        Thread.sleep(50);
                    } catch(java.lang.InterruptedException e){}

                    _app.invokeLater( new Runnable() {                
                        public void run() {                    
                            // This represents the next step after loading. This just shows 
                            // a dialog, but you could push the applications main menu screen.
                        	_mainScreen = new HelloWorldScreen();
                        	getUiEngine().pushScreen(_mainScreen);
 
                        }
                    });
                }
            };
            loadThread.start();
        }
        
      
       
    }



	public void batteryGood() {
		// TODO Auto-generated method stub
		
	}

	public void batteryLow() {
		// TODO Auto-generated method stub
		
	}

	public void batteryStatusChange(int status) {
		// TODO Auto-generated method stub
		
	}

	public void powerOff() {
		// TODO Auto-generated method stub
		
	}
    
   
}


/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
final class HelloWorldScreen extends MainScreen
{
	private static MenuItem aboutMenuItem;
	private static MenuItem picInfoMenuItem;
	static Bitmap bitmapImage = null;	
	static BitmapField bitmapField = null;
	static String imgUrl = "";
	static CheckboxField checkField = new CheckboxField();
	static ButtonField buttonField;
	BitmapButtonField refreshBitmapButton;
	SpacerField spacerField;
	HorizontalFieldManager hfm;
	VerticalFieldManager vfm;
	static int resizeX = net.rim.device.api.system.Display.getWidth();
    static int resizeY = (net.rim.device.api.system.Display.getHeight() - 146);
    
    //public HelloWorldDemo _mainApplication;
	public static HTTPInterface _httpInterface = new HTTPInterface(); 
	public DaemonScheduler _daemonScheduler = new DaemonScheduler(this);
	private AboutScreen _aboutScreen = new AboutScreen();
	
	/*static PersistentObject store;
	static {
	store = PersistentStore.getPersistentObject( 0x481f92f8b85af84aL );
	}*/
	
    /**
     * Creates a new HelloWorldScreen object
     */
    HelloWorldScreen()
    {        
    	super(NO_VERTICAL_SCROLL|NO_VERTICAL_SCROLLBAR);
        // Set the displayed title of the screen       
        setTitle("Today's Bing Image of the Day");

        if(BingModel.isRunStartup == true)
        {
        	runDaemonOnStartup();
        }
        
        imgUrl = _httpInterface.getImageUrl();
        bitmapImage = _httpInterface.setBitmapImage(imgUrl);
        Bitmap bitmapImageForResize = _httpInterface.resizeImage(bitmapImage, resizeX, resizeY);
        bitmapField =  _httpInterface.bitmapToField(bitmapImageForResize);
        
        vfm = new VerticalFieldManager(USE_ALL_HEIGHT) {
            protected void sublayout(int width, int height) {
            	 super.sublayout(width, height);
            	 UiApplication.getUiApplication().invokeLater(new Runnable() 
                 {
                    public void run() 
                    {
                    	 resizeX = net.rim.device.api.system.Display.getWidth();
		            	 resizeY = (net.rim.device.api.system.Display.getHeight() - 146);
		       			 int spacerWidth = net.rim.device.api.system.Display.getWidth() - buttonField.getPreferredWidth() - 119;
		               	 spacerField.setWidth(spacerWidth);
		               	 Bitmap bitmapImageForResize = _httpInterface.resizeImage(bitmapImage, resizeX, resizeY);
		                 bitmapField.setBitmap(bitmapImageForResize);
		               	 //update();
                    }
                 });
            }
        };
        hfm = new HorizontalFieldManager(USE_ALL_WIDTH);
        
        buttonField = new ButtonField("Set As Wallpaper", ButtonField.CONSUME_CLICK | DrawStyle.VCENTER) {
    		
        	public int getPreferredWidth() {
    			return 240; //changed from 250
    		}
        	public int getPreferredHeight() {
    			return 37;
    		}
    		
    		protected void fieldChangeNotify(int context)
    		{
    			ChangeBackground getRequest = new ChangeBackground();
    			PleaseWaitPopupScreen.showScreenAndWait(getRequest, "Working...");
    		}
    		
    		
    	};
    	
    	
    	
    	Bitmap reloadUp = Bitmap.getBitmapResource("img/reload37.png");
    	Bitmap reloadDown = Bitmap.getBitmapResource("img/reloadDown37.png");
    	refreshBitmapButton = new BitmapButtonField(reloadUp, reloadDown, ButtonField.CONSUME_CLICK) {
    		protected void fieldChangeNotify(int context)
    		{
    			update();
    			//updateDisplay();
    		}
    	};
    	

    	
    	
    	hfm.add(buttonField);
    	int spacerWidth = net.rim.device.api.system.Display.getWidth() - buttonField.getPreferredWidth() - 119;
    	System.out.println("Screen width: " + net.rim.device.api.system.Display.getWidth());
    	hfm.add(spacerField = new SpacerField(spacerWidth, 0)); //290
    	hfm.add(refreshBitmapButton);
    	vfm.add(hfm);
    	
    	//add(hfm);
   		if (bitmapField != null)
       		vfm.add(bitmapField);
   			//add(bitmapField);
   		
   		checkField.setChecked(BingModel.isChecked);
   		checkField.setLabel("Automatically Update Wallpaper");
   		vfm.add(checkField);
   		add(vfm);
    }
    
    
    static class UpdateManual implements Runnable {

		public UpdateManual() {
        }
		public void run() {
		//Thread thread = new Thread(new Runnable() { 
		UiApplication.getUiApplication().invokeLater(new Runnable() {  
			public void run() 
	          {
	        	 //buttonField.setEnabled(false);
	        	 //checkField.setEnabled(false);
	        	 resizeX = net.rim.device.api.system.Display.getWidth();
	        	 resizeY = (net.rim.device.api.system.Display.getHeight() - 146);
	             imgUrl = _httpInterface.getImageUrl();
	             bitmapImage = _httpInterface.setBitmapImage(imgUrl);
	             Bitmap bitmapImageForResize = _httpInterface.resizeImage(bitmapImage, resizeX, resizeY);
	             bitmapField.setBitmap(bitmapImageForResize);
	             //buttonField.setEnabled(true);
	             //checkField.setEnabled(true);
	          }

	       });
	    	//thread.start();
		}
	}
    
    static class ChangeBackground implements Runnable {

		public ChangeBackground() {
        }
		public void run() {
			 UiApplication.getUiApplication().invokeLater(new Runnable() {
                public void run() {
                	if (_httpInterface.connectionExists == true) {
    					try {
    						_httpInterface.setAsBackground();
    					} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					Dialog.alert("Wallpaper succesfully updated!");
    				}
    				else
    					Dialog.alert("Please check your network settings!");
                    }
	         });
	    }

	}
    
    protected void getPictureInfo()
    {
    	String infoString = _httpInterface.getPictureData();
    	Dialog.inform(infoString);
    }
    
    protected void makeMenu(Menu menu, int instance)
    {
    	 aboutMenuItem = new MenuItem(new StringProvider("About"), 0x100050, 2); 
         aboutMenuItem.setCommand(new Command(new CommandHandler() 
         {
             /**
              * @see net.rim.device.api.command.CommandHandler#execute(ReadOnlyCommandMetadata, Object)
              */
             public void execute(ReadOnlyCommandMetadata metadata, Object context) 
             {
            	 UiApplication.getUiApplication().pushScreen(_aboutScreen);
                //make a new screen and push it here.
             }
         }));
        menu.add(aboutMenuItem);
        
        picInfoMenuItem = new MenuItem(new StringProvider("Image Info"), 0x100050, 2); 
        picInfoMenuItem.setCommand(new Command(new CommandHandler() 
        {
            
            public void execute(ReadOnlyCommandMetadata metadata, Object context) 
            {
            	getPictureInfo();
               //make a new screen and push it here.
            }
        }));
        menu.add(picInfoMenuItem);
        super.makeMenu(menu, instance);
    }
    

    public void update()
    {   
    	UpdateManual getRequest = new UpdateManual();
		PleaseWaitPopupScreen.showScreenAndWait(getRequest, "Checking for latest image...");
    }


	 
    
    public boolean onSavePrompt() //Removes save confirmation when exiting the application
    {
        return true;
    }

    
    
    public void close()
    {
    	BingModel.saveState(checkField.getChecked());
    
        // Display a farewell message before closing the application
    	if (BingModel.isChecked ==  true)
    	{
    		
    		Dialog.alert("The application will continue to run in the background.");
    		_daemonScheduler.setRun();
    		UiApplication.getUiApplication().requestBackground();
    	}
    	else 
    	{
    		Dialog.alert("Your homescreen image will NOT be automatically updated every day."); 
    		_daemonScheduler.setStop();	
    		System.exit(0);
    	}
    }   
    
    public void runDaemonOnStartup() {
    	_daemonScheduler.setRun();
    }
}
