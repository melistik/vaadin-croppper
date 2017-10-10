package org.vaadin.cropper.client;

public class Dimension {

    private int width;

    private int height;

    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Dimension() {
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getAspectRatio() {
        return width / (double) height;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Dimension)) {
            return false;
        }
        final Dimension other = (Dimension) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        if (this.getWidth() != other.getWidth()) {
            return false;
        }
        if (this.getHeight() != other.getHeight()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getWidth();
        result = result * PRIME + this.getHeight();
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Dimension;
    }

    public String toString() {
        return "width: " + this.getWidth() + ", height: " + this.getHeight();
    }
}
