//drogers@glenbrook225.org

package com.rim.samples.device.helloworlddemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;

import net.rim.blackberry.api.homescreen.HomeScreen;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.PNGEncodedImage;

import net.rim.device.api.ui.component.BitmapField;

import utils.FindConnection;
import utils.URLDecoder;

public class HTTPInterface {
	
	private static final String HEADER_CONTENTTYPE = "content-type";    
    private static final String CONTENTTYPE_TEXTHTML = "text/html";
    private String CONNECTION_TYPE = ";deviceside=true";
    private String content = "";
    private String imageUrl = "";
    private byte imageByte[];
    private static final String bingUrl = "http://www.bing.com";
    public boolean connectionExists = false;

    
    public HTTPInterface()
    {
    	super();
    	
    	
    }
	        
	
	public void getBytesForDaemon(String imgUrl)
	{
		HttpConnection conn = null;
    	InputStream is = null;
    	if (imgUrl.equals("HttpError"))
    	{
    		connectionExists = false;
    	} 
    	try {
    		connectionExists = true;
    		CONNECTION_TYPE = FindConnection.getConnectionString();
            
    		conn = (HttpConnection)Connector.open(imgUrl + CONNECTION_TYPE);
	    	int statusBytes = conn.getResponseCode();
	        
	    	if(statusBytes == HttpConnection.HTTP_OK)
	        {
	    		is = conn.openInputStream();  
	       
		    		try {
		    			
			            ByteArrayOutputStream baos = new ByteArrayOutputStream();
			            int ch;
			            while ((ch = is.read()) != -1) {
			                baos.write(ch);
			            }
			            imageByte = baos.toByteArray();
			    	} 
		    		finally {
			            if (is != null) {
			                is.close();
			            }
			            try {
			            	if (connectionExists == true)
			            		setAsBackground();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	}
	        }
   	 } catch (ConnectionNotFoundException e) { 
	            e.printStackTrace(); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } catch (IllegalArgumentException e) { 
	            e.printStackTrace(); 
	        }

 	}
	
    

    public void setAsBackground() throws IOException  //sets the imported image as device homescreen background
    {
    	Bitmap bmp;
    	bmp = Bitmap.createBitmapFromBytes(imageByte, 0, imageByte.length, 1);
    	if (BingModel.deviceVersion < 7.0) {
    		bmp = resizeImage(bmp, Display.getWidth(), Display.getHeight());
    	}
    	FileConnection fconn = (FileConnection)Connector.open("file:///store/home/user/pictures/bingImageBackground.bmp",Connector.READ_WRITE);
    	if(!fconn.exists())               
    		fconn.create();	
    		//fconn.delete();
     

        OutputStream out = fconn.openOutputStream();
        PNGEncodedImage encodedImage =  PNGEncodedImage.encode(bmp);            
        byte[] imageBytes = encodedImage.getData();                   
        out.write(imageBytes);
        out.close();
        fconn.close();

        
    	HomeScreen.setBackgroundImage("file:///store/home/user/pictures/bingImageBackground.bmp");
  
    }
    
    
    public String getImageUrl()  //retrieves html content from bing.com, extracts url of background image from the html.
    {
    	HttpConnection conn = null; 
    	for(;;) 
    	{
	        try 
	        { 
	        	CONNECTION_TYPE = FindConnection.getConnectionString();
	            conn = (HttpConnection)Connector.open(bingUrl + CONNECTION_TYPE); 
	            
	            int status = conn.getResponseCode();
	            if(status == HttpConnection.HTTP_OK)
	            { 
	            	String contentType = conn.getHeaderField(HEADER_CONTENTTYPE);
	            	boolean isHtmlContent = (contentType != null && contentType.startsWith(CONTENTTYPE_TEXTHTML));
	            	        
	         		InputStream responseData = conn.openInputStream();  
	         		byte[] data = new byte[256];
	                int len = 0;
	                //int size = 0;
	                StringBuffer raw = new StringBuffer();
	         		
	            	if (isHtmlContent)
	             	{
	            		
	            		while ( -1 != (len = responseData.read(data)) )
	                    {
	                        raw.append(new String(data, 0, len, "UTF-8"));
	                        //size += len;    
	                    }   
	            		
	            		content = raw.toString();
	            		conn.close();
	            		responseData.close();
	            		
	            		System.out.println(content);
	            		//Find background image url using String modifiers
	                	int indexBegin = content.indexOf("g_img={url:'") + 12;
	                	int indexEnd = content.indexOf(".jpg", indexBegin) + 4;
	                	imageUrl = "http://www.bing.com" + content.substring(indexBegin, indexEnd);
	                	return imageUrl;
	             	}
	             } 
	            else {
	       
	            	return "HttpError";
	            }
	 
	        } catch (ConnectionNotFoundException e) { 
	        	
            	e.printStackTrace();
     
            	return "HttpError";
	        } catch (IOException e) { 
	        	
	            e.printStackTrace(); 
	        } catch (IllegalArgumentException e) { 
	        	
	        	e.printStackTrace(); 
	        } 
	
			return "HttpError"; 
    	}
    }
    
    
    public Bitmap setBitmapImage(String imgUrl) //writes the image url into a bitmap
    {
    	HttpConnection conn = null;
    	InputStream is = null;
    	System.out.println("is error= " + imgUrl);
    	if (imgUrl.equals("HttpError"))
    	{
    		connectionExists = false;
    		Bitmap noConnectionPng = Bitmap.getBitmapResource("img/NoConnection.png");
        	return noConnectionPng;
    	} 
    	
    	try {
    		connectionExists = true;
    		CONNECTION_TYPE = FindConnection.getConnectionString();
    		imgUrl = imgUrl + CONNECTION_TYPE;
    		conn = (HttpConnection)Connector.open(imgUrl);
	    	int status = conn.getResponseCode();
	        
	    	if(status == HttpConnection.HTTP_OK)
	        {
	    		is = conn.openInputStream();  
	       
		    		try {
		    			
			            ByteArrayOutputStream baos = new ByteArrayOutputStream();
			            int ch;
			            while ((ch = is.read()) != -1) {
			                baos.write(ch);
			            }
			            imageByte = baos.toByteArray(); //essential to setting the homescreen background
			    	} 
		    		finally {
			            if (is != null) {
			                is.close();
			            }
			    	}
		    
		    	Bitmap bmp = Bitmap.createBitmapFromBytes(imageByte, 0, imageByte.length, 1);
		 
		    	//Bitmap bitmapImageForResize = resizeImage(bmp, (net.rim.device.api.system.Display.getWidth()), (net.rim.device.api.system.Display.getHeight() - 146));
		    	
		    	return bmp;
		   		//return bitmapImageForResize;
	        }
    	 } catch (ConnectionNotFoundException e) { 
	            e.printStackTrace(); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } catch (IllegalArgumentException e) { 
	            e.printStackTrace(); 
	        }
    	return null;
    }
    	
   

    public BitmapField bitmapToField(Bitmap bmp)
    {
    	BitmapField bitmapImageField = new BitmapField(bmp);
    	return bitmapImageField;
    	
    }
    public Bitmap resizeImage(Bitmap originalImage, int newWidth, int newHeight) {  //resizes bitmap to fit screen.
        Bitmap newImage = new Bitmap(newWidth, newHeight); 
        if (connectionExists == true)
        	originalImage.scaleInto(newImage, Bitmap.FILTER_BILINEAR, Bitmap.SCALE_TO_FILL); //used to be SCALE_TO_FILL but image rolls over edges of screen
        else 
        	originalImage.scaleInto(newImage, Bitmap.FILTER_BILINEAR, Bitmap.SCALE_TO_FIT);
        return newImage; 	
    } 
    
    public String getPictureData()
    {
    	String decodedInfoString = "";
    	if (connectionExists == false)
    	{
    		return "Could not retrieve info!";
    	}
    	else {
    		int indexBegin = content.indexOf("class=\"hpcCopyInfo\"") + 41;
    		int indexEnd = content.indexOf("</span>", indexBegin) - 4;
    		String infoString = content.substring(indexBegin, indexEnd);
    		try {
				decodedInfoString = URLDecoder.decode(infoString, "utf-8");
				System.out.println(decodedInfoString);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "Could not retrieve info!";
			}
    		return decodedInfoString;
    	}
    }
}
