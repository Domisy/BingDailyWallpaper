package com.rim.samples.device.helloworlddemo;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public class BingModel {
	public static boolean isChecked = false;
	public static boolean isRunStartup = false;
	public static double deviceVersion = 0.0;
	public static PersistentObject store;
	static {
		store = PersistentStore.getPersistentObject( 0x481f92f8b85af84aL );
		}
	
	public static void saveState(boolean checkState)
	{
		isChecked = checkState;
		boolean[] userinfo = {isChecked};
		synchronized(store) {
			store.setContents(userinfo); 
			store.commit();
		}
	}
	
	public static boolean retrieveState()
	{
		synchronized(store) {
    		boolean[] isCheckedArray = (boolean[])store.getContents(); 
    		if(isCheckedArray == null) {
    			isChecked = false;
    			return isChecked;
    		} 
    		else {
    			isChecked = isCheckedArray[0];
    			return isChecked;
    		}
    	}
	}
}
