package org.vaadin.cropper.client;

import com.vaadin.shared.communication.ServerRpc;

public interface CropperServerRpc extends ServerRpc {

    void cropChanged(CropSelection cropSelection);

    void imageLoaded(Dimension dimension);

    void canvasResize(Dimension dimension);
}
