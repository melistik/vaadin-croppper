package org.vaadin.cropper.client;

import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import org.vaadin.cropper.Cropper;

/**
 * connects Cropper with GWT VCropper
 *
 * @author Marten Prieß (http://www.rocketbase.io)
 * @version 1.0
 */
@Connect(Cropper.class)
public class CropperConnector extends AbstractComponentConnector {

    private CropperServerRpc rpc = RpcProxy.create(CropperServerRpc.class, this);

    public CropperConnector() {
        super();

        registerRpc(CropperClientRpc.class, new CropperClientRpc() {

            @Override
            public void setCropSelection(CropSelection cropSelection) {
                if (cropSelection == null) {
                    getWidget().clearSelection();
                } else {
                    getWidget().setSelection(cropSelection);
                }
            }

            @Override
            public void setCropMaxSelection() {
                getWidget().setMaxSelection();
            }
        });

        getWidget().setListener(new VCropper.CropListener() {
            @Override
            public void cropChanged(CropSelection cropSelection) {
                rpc.cropChanged(cropSelection);
            }

            @Override
            public void imageLoaded(Dimension dimension) {
                rpc.imageLoaded(dimension);
            }

            @Override
            public void canvasResize(Dimension dimension) {
                rpc.canvasResize(dimension);
            }
        });

    }

    @Override
    public CropperState getState() {
        return (CropperState) super.getState();
    }

    @Override
    public VCropper getWidget() {
        return (VCropper) super.getWidget();
    }

    @Override
    public void onStateChanged(final StateChangeEvent event) {
        super.onStateChanged(event);

        if (event.hasPropertyChanged("aspectRatio")) {
            getWidget().setAspectRatio(getState().aspectRatio);
        }
        if (event.hasPropertyChanged("cropChangeEventTimeout")) {
            getWidget().setCropChangeEventTimeout(getState().cropChangeEventTimeout);
        }
        if (event.hasPropertyChanged("minimalWidth") || event.hasPropertyChanged("minimalHeight")) {
            getWidget().setMinimalCropSize(getState().minimalWidth, getState().minimalHeight);
        }

        if (event.hasPropertyChanged("sourceChanged")) {
            String url = this.getResourceUrl("source");
            getWidget().setUrl(url != null ? url : "");
        }
    }

    @Override
    public boolean delegateCaptionHandling() {
        return false;
    }
}
