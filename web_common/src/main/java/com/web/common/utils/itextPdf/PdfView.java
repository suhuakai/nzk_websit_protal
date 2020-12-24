package com.web.common.utils.itextPdf;

/**
 * @Auther: eva
 * @Description:
 */
public class PdfView {
    /** the lower left x-coordinate. */
    protected float llx;

    /** the lower left y-coordinate. */
    protected float lly;

    /** the upper right x-coordinate. */
    protected float urx;

    /** the upper right y-coordinate. */
    protected float ury;

    /** The rotation of the Rectangle */
    protected int rotation = 0;
    /** margin in x direction starting from the left */
    protected float marginLeft = 0;

    /** margin in x direction starting from the right */
    protected float marginRight = 0;

    /** margin in y direction starting from the top */
    protected float marginTop = 0;

    /** margin in y direction starting from the bottom */
    protected float marginBottom = 0;

    public PdfView(float urx, final float ury, float marginLeft, float marginRight,
                    float marginTop, float marginBottom) {
        this.urx = urx;
        this.ury = ury;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }

    public float getLlx() {
        return llx;
    }

    public void setLlx(float llx) {
        this.llx = llx;
    }

    public float getLly() {
        return lly;
    }

    public void setLly(float lly) {
        this.lly = lly;
    }

    public float getUrx() {
        return urx;
    }

    public void setUrx(float urx) {
        this.urx = urx;
    }

    public float getUry() {
        return ury;
    }

    public void setUry(float ury) {
        this.ury = ury;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(float marginLeft) {
        this.marginLeft = marginLeft;
    }

    public float getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(float marginRight) {
        this.marginRight = marginRight;
    }

    public float getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(float marginTop) {
        this.marginTop = marginTop;
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(float marginBottom) {
        this.marginBottom = marginBottom;
    }
}
