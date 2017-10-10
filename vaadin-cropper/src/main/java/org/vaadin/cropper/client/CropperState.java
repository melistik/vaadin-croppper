package org.vaadin.cropper.client;


import com.vaadin.shared.AbstractComponentState;

/**
 * Transfer states to GWT connector
 *
 * @author Marten Prieß (http://www.rocketbase.io)
 * @version 1.0
 */
public class CropperState extends AbstractComponentState {

    public String sourceChanged = "";

    public double aspectRatio = 0;

    public Dimension imageDimension = null;

    public Dimension canvasDimension = null;

    public CropSelection cropSelection = null;

    public int cropChangeEventTimeout = 200;

    public int minimalWidth = 30;

    public int minimalHeight = 30;
}
