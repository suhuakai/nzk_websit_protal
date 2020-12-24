/**
 * 
 */
package com.web.common.utils.print;

/**
 * <p>
 * Description:
 * </p>
 * 
 *  * @author
 * @version 1.0
 * @since JDK 1.8
 */
public class PrintMareginEnum {

	private float marginLeft;
	private float marginRight;
	private float marginTop;
	private float marginBottom;

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
	
	public PrintMareginEnum(){
		
	}

	/**
	 * @param marginLeft
	 * @param marginRight
	 * @param marginTop
	 * @param marginBottom
	 */
	public PrintMareginEnum(float marginLeft, float marginRight, float marginTop, float marginBottom) {
		super();
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.marginTop = marginTop;
		this.marginBottom = marginBottom;
	}

}
