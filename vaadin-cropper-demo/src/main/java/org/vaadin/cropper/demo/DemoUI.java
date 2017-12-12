package org.vaadin.cropper.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.HasValue;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.v7.data.Property;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.cropper.Cropper;
import org.vaadin.cropper.client.CropSelection;
import org.vaadin.easyuploads.UploadField;

import java.io.File;

@SpringUI()
@Theme("valo")
@Widgetset("org.vaadin.cropper.demo.WidgetSet")
public class DemoUI extends UI {

    private Cropper cropper;

    @Value("${build.version}")
    private String buildVersion;

    private Label cropEvent;

    @Override
    protected void init(final VaadinRequest vaadinRequest) {

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        // event area
        cropEvent = new Label();
        cropEvent.setWidth("100%");
        cropEvent.setHeight("40px");

        // cropper
        cropper = new Cropper(new ExternalResource(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/66/SMPTE_Color_Bars.svg/2000px-SMPTE_Color_Bars.svg.png"));
        cropper.setSizeFull();
        cropper.addCropSelectionChangedListener(new Cropper.CropperSelectionChangedListener() {
            @Override
            public void selectionChanged(Cropper.CropperSelectionChangedEvent event) {
                cropEvent.setValue(event.getSelection() != null ? event.getSelection()
                        .toString() : "null");
            }
        });
        layout.addComponent(cropper);
        layout.setExpandRatio(cropper, 1);
        layout.setSizeFull();

        // control and events
        layout.addComponent(cropEvent);

        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setHeight("50px");
        buttonBar.addComponent(new Button("ImageSize", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Notification.show(cropper.getImageDimension()
                        .toString());
            }
        }));
        buttonBar.addComponent(new Button("CanvasSize", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Notification.show(cropper.getCanvasDimension()
                        .toString());
            }
        }));
        buttonBar.addComponent(new Button("Get CropSelection", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Notification.show(cropper.getCropSelection()
                        .toString());
            }
        }));
        buttonBar.addComponent(new Button("Set CropSelection", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                cropper.setCropSelection(new CropSelection(0, 0, 400, 600));
            }
        }));
        buttonBar.addComponent(new Button("Set CropMaxSelection", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                cropper.setCropMaxSelection();
            }
        }));
        TextField aspectRatio = new TextField();
        aspectRatio.setDescription("aspectRatio");
        aspectRatio.setValue("0.0");
        aspectRatio.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                if (aspectRatio.getValue() != null) {
                    cropper.setAspectRatio(Double.parseDouble(aspectRatio.getValue()));
                }
            }
        });
        buttonBar.addComponent(aspectRatio);
        layout.addComponent(buttonBar);

        UploadField uploadField = new UploadField(UploadField.StorageMode.FILE);
        uploadField.setFieldType(UploadField.FieldType.FILE);
        uploadField.setAcceptFilter("image/*");
        uploadField.setWriteThrough(true);
        uploadField.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if (uploadField.getValue() != null) {
                    cropper.setSource(new FileResource((File) uploadField.getValue()));
                }
            }
        });
        buttonBar.addComponent(uploadField);

        setContent(layout);
    }

}
