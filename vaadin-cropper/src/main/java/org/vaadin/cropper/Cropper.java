package org.vaadin.cropper;

import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;
import org.vaadin.cropper.client.*;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Vaadin Cropper
 *
 * @author Marten Prieß (http://www.rocketbase.io)
 * @version 1.0
 */
public class Cropper extends AbstractComponent {

    private CropperServerRpc rpc = new CropperServerRpc() {
        @Override
        public void cropChanged(CropSelection cropSelection) {
            getState().cropSelection = cropSelection;
            Cropper.this.fireEvent(new CropperSelectionChangedEvent(Cropper.this, cropSelection));
        }

        @Override
        public void imageLoaded(Dimension dimension) {
            getState().imageDimension = dimension;
        }

        @Override
        public void canvasResize(Dimension dimension) {
            getState().canvasDimension = dimension;
        }
    };

    public Cropper(Resource resource) {
        registerRpc(rpc);
        setSource(resource);
    }

    @Override
    protected CropperState getState() {
        return (CropperState) super.getState();
    }

    public Resource getSource() {
        return this.getResource("source");
    }

    public void setSource(Resource source) {
        getState().cropSelection = null;
        getState().canvasDimension = null;
        getState().imageDimension = null;

        this.setResource("source", source);
        getState().sourceChanged = String.valueOf(System.currentTimeMillis());
    }

    public double getAspectRatio() {
        return getState().aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        getState().aspectRatio = aspectRatio;
    }

    public int getCropChangeEventTimeout() {
        return getState().cropChangeEventTimeout;
    }

    public void setCropChangeEventTimeout(int cropChangeEventTimeout) {
        getState().cropChangeEventTimeout = cropChangeEventTimeout;
    }

    public int getMinimalWidth() {
        return getState().minimalWidth;
    }

    public void setMinimalWidth(int minimalWidth) {
        getState().minimalWidth = minimalWidth;
    }

    public int getMinimalHeight() {
        return getState().minimalHeight;
    }

    public void setMinimalHeight(int minimalHeight) {
        getState().minimalHeight = minimalHeight;
    }

    public Dimension getImageDimension() {
        return getState().imageDimension;
    }

    public Dimension getCanvasDimension() {
        return getState().canvasDimension;
    }

    public CropSelection getCropSelection() {
        return getState().cropSelection;
    }

    public void setCropSelection(CropSelection cropSelection) {
        getRpcProxy(CropperClientRpc.class).setCropSelection(cropSelection);
    }

    public void setCropMaxSelection() {
        if (isAttached()) {
            getRpcProxy(CropperClientRpc.class).setCropMaxSelection();
        } else {

        }
    }

    public void addCropSelectionChangedListener(CropperSelectionChangedListener listener) {
        this.addListener(CropperSelectionChangedEvent.class, listener, CropperSelectionChangedListener.SELECTION_CHANGED);
    }


    public interface CropperSelectionChangedListener extends Serializable {

        Method SELECTION_CHANGED = ReflectTools
                .findMethod(Cropper.CropperSelectionChangedListener.class, "selectionChanged",
                        Cropper.CropperSelectionChangedEvent.class);

        void selectionChanged(CropperSelectionChangedEvent event);
    }

    public static class CropperSelectionChangedEvent extends Component.Event {

        private CropSelection cropSelection;

        public CropperSelectionChangedEvent(Cropper source, CropSelection cropSelection) {
            super(source);
            this.cropSelection = cropSelection;
        }

        @Override
        public Cropper getSource() {
            return (Cropper) super.getSource();
        }

        public CropSelection getSelection() {
            return cropSelection;
        }
    }
}
