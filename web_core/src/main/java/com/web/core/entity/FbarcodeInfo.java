package com.web.core.entity;

/**
 *  条形码信息
 */
public class FbarcodeInfo {
	
	private String fbarcode;//主码
	private String fbarcodeSec;//次码
	private String flot;//批号
	private String prodDate;//生产日期
	private String usefulDate;//有效期至
	private String message;//解析信息

	public String getFbarcode() {
		return fbarcode;
	}

	public void setFbarcode(String fbarcode) {
		this.fbarcode = fbarcode;
	}

	public String getFbarcodeSec() {
		return fbarcodeSec;
	}

	public void setFbarcodeSec(String fbarcodeSec) {
		this.fbarcodeSec = fbarcodeSec;
	}

	public String getFlot() {
		return flot;
	}

	public void setFlot(String flot) {
		this.flot = flot;
	}

	public String getProdDate() {
		return prodDate;
	}

	public void setProdDate(String prodDate) {
		this.prodDate = prodDate;
	}

	public String getUsefulDate() {
		return usefulDate;
	}

	public void setUsefulDate(String usefulDate) {
		this.usefulDate = usefulDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
