package com.rim.samples.device.helloworlddemo;



public class DaemonScheduler implements Runnable{
	static HTTPInterface _httpInterface = new HTTPInterface();
	private HelloWorldScreen mainScreen;
	private static final long checkRate = 3600000; //3600000
	private boolean isRunning = false;
	public static Thread daemonThread;

	public DaemonScheduler(HelloWorldScreen mainInstance)
	{
		daemonThread = new Thread(this, "My background job"); 
		mainScreen = mainInstance;
	}
	

	public void run()
	{
		while(isRunning == true) { 
            { 
            	String bmpUrl = _httpInterface.getImageUrl();
            	_httpInterface.getBytesForDaemon(bmpUrl);
        		mainScreen.update();
            } 
            try { 
                Thread.sleep(checkRate); 
            } catch (InterruptedException e){ 
                // handle ex 
            } 
        } 
	}
	
	public void setRun()
	{
		try {
			daemonThread.start(); 
		}
		catch (IllegalThreadStateException e) {
			System.out.println("Thread is already running!");
		}
		
		isRunning = true;
	}
	 
	public void setStop()
	{
		Thread.yield(); 
		isRunning = false;
	}

}
