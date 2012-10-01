package com.rim.samples.device.helloworlddemo;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

class SpacerField extends Field
{
    private int spacerWidth;
    private int spacerHeight;
    SpacerField(int width, int height) {
        spacerWidth = width;
        spacerHeight = height;
    }

    protected void setWidth(int width) {
    	spacerWidth = width;
    	setExtent(spacerWidth, spacerHeight);
    }
    
    protected void layout(int width, int height) {
        setExtent(getPreferredWidth(), getPreferredHeight());
    }

    protected void paint(Graphics g) {
        // nothing to paint; this is a blank field
    }

    public int getPreferredHeight() {
        return spacerHeight;
    }

    public int getPreferredWidth() {
        return spacerWidth;
    }
}
