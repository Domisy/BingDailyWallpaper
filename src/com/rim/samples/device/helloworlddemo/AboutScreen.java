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
		
		/*Font fonts[] = new Font[3];
		int[] offset = new int[4];
		byte[] attribute = new byte[3];
		fonts[0] = Font.getDefault();
	    fonts[1] = Font.getDefault().derive(Font.BOLD);
	    fonts[2] = Font.getDefault().derive(Font.BOLD | Font.ITALIC);
	    offset[0] = 0;
	    attribute[0] = 2;
	    //offset[3] = 
*/		
		VerticalFieldManager vfm = new VerticalFieldManager(USE_ALL_WIDTH | FIELD_HCENTER){
			protected void sublayout(int maxWidth, int maxHeight) { 
				 
                super.sublayout(Display.getWidth(),Display.getHeight()); 
                setExtent(Display.getWidth(),Display.getHeight());

            } 

	    };
	    vfm.add(new RichTextField("All images provided by Bing.com.", Field.NON_FOCUSABLE | RichTextField.TEXT_JUSTIFY_HCENTER));
		vfm.add(new RichTextField("This application is in no way affiliated with Bing or Microsoft corporations.", Field.NON_FOCUSABLE | RichTextField.TEXT_JUSTIFY_HCENTER));
		vfm.add(new SpacerField(0, 25));
		vfm.add(new RichTextField("Bing Daily Wallpaper was brought to you by - Domisy Dev", Field.NON_FOCUSABLE | RichTextField.TEXT_JUSTIFY_HCENTER));
		vfm.add(new SpacerField(0, 25));
		vfm.add(new RichTextField("Lead Developer - Theodore Mavrakis", Field.NON_FOCUSABLE | RichTextField.TEXT_JUSTIFY_HCENTER));
		vfm.add(new SpacerField(0, 25));
		vfm.add(new RichTextField("For Support - domisydev@gmail.com", Field.NON_FOCUSABLE | RichTextField.TEXT_JUSTIFY_HCENTER));
		vfm.add(new RichTextField("Follow us on Twitter - @flaminSaganaki and @DomisyDev", Field.NON_FOCUSABLE | RichTextField.TEXT_JUSTIFY_HCENTER));
		vfm.add(new SpacerField(0, 25));
		vfm.add(new RichTextField("Version - 1.0.0", Field.NON_FOCUSABLE | RichTextField.TEXT_JUSTIFY_HCENTER));
		add(vfm);
		
	}
	
	
}
