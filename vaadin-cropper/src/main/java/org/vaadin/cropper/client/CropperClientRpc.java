package org.vaadin.cropper.client;

import com.vaadin.shared.communication.ClientRpc;

public interface CropperClientRpc extends ClientRpc {

    void setCropSelection(CropSelection cropSelection);

    void setCropMaxSelection();
}
