package org.vaadin.cropper.client;

public class CropSelection {

    public static final int MAX_SELECTION = 99999;

    private int x;

    private int y;

    private int width;

    private int height;

    public CropSelection(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public CropSelection() {
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CropSelection)) {
            return false;
        }
        final CropSelection other = (CropSelection) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        if (this.getX() != other.getX()) {
            return false;
        }
        if (this.getY() != other.getY()) {
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
        result = result * PRIME + this.getX();
        result = result * PRIME + this.getY();
        result = result * PRIME + this.getWidth();
        result = result * PRIME + this.getHeight();
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CropSelection;
    }

    public String toString() {
        return "x: " + this.getX() + ", y: " + this.getY() + ", width: " + this.getWidth() + ", height: " + this.getHeight();
    }
}
