/**
 * 
 */
package com.web.common.utils.print;

import com.itextpdf.text.DocListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;

/**
 * <p>
 * Description:
 * </p>
 * 
 *  * @author sunhua
 * @version 1.0
 * @since JDK 1.8
 */
public class PdfDocument extends Document {

	public PdfDocument(Rectangle pageSize, float marginLeft, float marginRight,
			float marginTop, float marginBottom) {
        this.pageSize = pageSize;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }
	
	public PdfDocument(Rectangle pageSize) {
	        this(pageSize, 36, 36, 36, 36);
	    }

	public boolean setMargins(PrintMareginEnum printMarginEnum) {
		this.marginLeft = printMarginEnum.getMarginLeft();
		this.marginRight = printMarginEnum.getMarginLeft();
		this.marginTop = printMarginEnum.getMarginTop();
		this.marginBottom = printMarginEnum.getMarginBottom();
		for (DocListener listener : listeners) {
			listener.setMargins(marginLeft, marginRight, marginTop, marginBottom);
		}
		return true;
	}
}
