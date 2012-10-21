package com.rim.samples.device.helloworlddemo;


import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;


public class AboutScreen extends MainScreen {

	/**
	 * 
	 */
	public AboutScreen() {
		super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
		
		VerticalFieldManager vfm = new VerticalFieldManager(USE_ALL_WIDTH | FIELD_HCENTER){
			protected void sublayout(int maxWidth, int maxHeight) { 
				 
                super.sublayout(Display.getWidth(),Display.getHeight()); 
                setExtent(Display.getWidth(),Display.getHeight());

            } 

	    };
	    //For 7.0+ use TEXT_ALIGN_HCENTER, and 6.0 use TEXT_JUSTIFY_HCENTER
	    vfm.add(new RichTextField("All images provided by Bing.com.", Field.NON_FOCUSABLE | RichTextField.TEXT_ALIGN_HCENTER));
		vfm.add(new RichTextField("This application is in no way affiliated with Bing or Microsoft corporations.", Field.NON_FOCUSABLE | RichTextField.TEXT_ALIGN_HCENTER));
		vfm.add(new SpacerField(0, 25));
		vfm.add(new RichTextField("Daily Wallpaper - with Bing was brought to you by - Domisy Dev", Field.NON_FOCUSABLE | RichTextField.TEXT_ALIGN_HCENTER));
		vfm.add(new SpacerField(0, 25));
		vfm.add(new RichTextField("Lead Developer - Theodore Mavrakis", Field.NON_FOCUSABLE | RichTextField.TEXT_ALIGN_HCENTER));
		vfm.add(new SpacerField(0, 25));
		vfm.add(new RichTextField("For Support - domisydev@gmail.com", Field.NON_FOCUSABLE | RichTextField.TEXT_ALIGN_HCENTER));
		vfm.add(new RichTextField("Follow us on Twitter - @flaminSaganaki and @DomisyDev", Field.NON_FOCUSABLE | RichTextField.TEXT_ALIGN_HCENTER));
		vfm.add(new SpacerField(0, 25));
		vfm.add(new RichTextField("Version - 1.0.0", Field.NON_FOCUSABLE | RichTextField.TEXT_ALIGN_HCENTER));
		add(vfm);
		
	}
	
	
}
