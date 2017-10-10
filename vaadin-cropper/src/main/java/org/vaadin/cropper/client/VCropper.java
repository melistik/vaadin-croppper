package org.vaadin.cropper.client;

import com.google.code.gwt.crop.client.GWTCropper;
import com.google.code.gwt.crop.client.IGWTCropperPreview;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * the main gwt implementation of the {@link GWTCropper}
 *
 * @author Marten Prieß (http://www.rocketbase.io)
 * @version 1.0
 */
public class VCropper extends FlowPanel {

    public static final String CLASSNAME = "v-cropper-wrapper";

    private String url;

    private double aspectRatio = 0;

    private GWTCropper cropper;

    private CropListener listener;

    private CropSelection cropSelection;

    private Dimension imgDimension;

    private Dimension canvasDimension;

    private boolean cropChangeEventScheduled = false;

    private int cropChangeEventTimeout = 200;

    private Timer cropEventTimer;

    private int minimalWidth = 30, minimalHeight = 30;

    private IGWTCropperPreview cropperPreview = new IGWTCropperPreview() {
        @Override
        public void init(String imageUrl, int canvasWidth, int canvasHeight, double aspectRatio) {
            if (listener != null) {
                listener.canvasResize(new Dimension(canvasWidth, canvasHeight));
            }
        }

        @Override
        public void updatePreview(int cropShapeWidth, int cropShapeHeight, int cropLeft, int cropTop) {
            if (listener != null) {
                cropSelection = new CropSelection(cropper.getSelectionXCoordinate(),
                        cropper.getSelectionYCoordinate(),
                        cropper.getSelectionWidth(),
                        cropper.getSelectionHeight());
                if (!cropChangeEventScheduled) {
                    cropEventTimer.cancel();
                    cropEventTimer.schedule(cropChangeEventTimeout);
                    cropChangeEventScheduled = true;
                }
            }
        }
    };

    public VCropper() {
        setStylePrimaryName(CLASSNAME);

        cropEventTimer = new Timer() {
            public void run() {
                if (VCropper.this.isAttached()) {
                    listener.cropChanged(cropSelection);
                    cropChangeEventScheduled = false;
                }
            }
        };
    }

    public void setUrl(String url) {
        this.url = url;
        this.cropper = null;
        this.cropSelection = null;
        this.canvasDimension = new Dimension();

        Image image = new Image(url);
        image.setStyleName("img-loading");
        image.addLoadHandler(new LoadHandler() {
            @Override
            public void onLoad(LoadEvent event) {
                Image img = (Image) event.getSource();
                imgDimension = new Dimension(img.getWidth(), img.getHeight());
                listener.imageLoaded(imgDimension);

                int elemWidth = VCropper.this.getElement()
                        .getOffsetWidth();
                int elemHeight = VCropper.this.getElement()
                        .getOffsetHeight();


                if (imgDimension.getWidth() > elemWidth || imgDimension.getHeight() > elemHeight) {
                    double ratioX = elemWidth / (double) imgDimension.getWidth();
                    double ratioY = elemHeight / (double) imgDimension.getHeight();
                    if (ratioX < ratioY) {
                        canvasDimension.setWidth((int) (imgDimension.getWidth() * ratioX));
                        canvasDimension.setHeight((int) (imgDimension.getHeight() * ratioX));
                    } else {
                        canvasDimension.setWidth((int) (imgDimension.getWidth() * ratioY));
                        canvasDimension.setHeight((int) (imgDimension.getHeight() * ratioY));
                    }
                } else {
                    canvasDimension = imgDimension;
                }
                layout();
            }
        });
        add(image);
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
        if (cropper != null) {
            cropper.setAspectRatio(aspectRatio);
        }
    }

    public void clearSelection() {
        cropSelection = null;
        if (cropper != null) {
            layout();
        }
    }

    public void setSelection(CropSelection selection) {
        this.cropSelection = selection;
        if (cropper != null) {
            layout();
        }
    }

    public void setMaxSelection() {
        this.cropSelection = new CropSelection(0, 0, CropSelection.MAX_SELECTION, CropSelection.MAX_SELECTION);
        if (cropper != null) {
            layout();
        }
    }

    public void setCropChangeEventTimeout(int cropChangeEventTimeout) {
        this.cropChangeEventTimeout = cropChangeEventTimeout;
    }

    public void setMinimalCropSize(int minimalWidth, int minimalHeight) {
        this.minimalWidth = minimalWidth;
        this.minimalHeight = minimalHeight;
    }

    private void layout() {
        clear();

        cropper = new GWTCropper(url);
        cropper.addStyleName("v-cropper");
        cropper.registerPreviewWidget(cropperPreview);
        cropper.setAspectRatio(aspectRatio);
        cropper.setWidth(canvasDimension.getWidth());
        cropper.setMinimalWidth(minimalWidth);
        cropper.setMinimalHeight(minimalWidth);

        if (cropSelection != null) {
            if (cropSelection.getWidth() == CropSelection.MAX_SELECTION && cropSelection.getHeight() == CropSelection.MAX_SELECTION) {
                Dimension cropCanvasDimension = null;
                if (aspectRatio <= 0 || canvasDimension.getAspectRatio() == aspectRatio) {
                    cropCanvasDimension = canvasDimension;
                } else {
                    if (aspectRatio < canvasDimension.getAspectRatio()) {
                        cropCanvasDimension = new Dimension((int) (canvasDimension.getHeight() * aspectRatio), canvasDimension.getHeight());
                    } else {
                        cropCanvasDimension = new Dimension(canvasDimension.getWidth(), (int) (canvasDimension.getWidth() / aspectRatio));
                    }
                }

                int top = (int) ((canvasDimension.getHeight() - cropCanvasDimension.getHeight()) / 2.0);
                int left = (int) ((canvasDimension.getWidth() - cropCanvasDimension.getWidth()) / 2.0);

                cropper.setInitialSelection(left, top,
                        cropCanvasDimension.getWidth(),
                        cropCanvasDimension.getHeight());
            } else {
                double ratio = canvasDimension.getWidth() / ((double) imgDimension.getWidth());

                cropper.setInitialSelection((int) (cropSelection.getX() * ratio),
                        (int) (cropSelection.getY() * ratio),
                        (int) (cropSelection.getWidth() * ratio),
                        (int) (cropSelection.getHeight() * ratio));
            }
        }

        add(cropper);
    }

    public void setListener(CropListener listener) {
        this.listener = listener;
    }


    /**
     * used to log in javascript console
     *
     * @param message info to get logged
     */
    native void consoleLog(final String message) /*-{
      console.debug(message);
    }-*/;


    public interface CropListener {
        void cropChanged(CropSelection cropSelection);

        void imageLoaded(Dimension dimension);

        void canvasResize(Dimension dimension);
    }
}
