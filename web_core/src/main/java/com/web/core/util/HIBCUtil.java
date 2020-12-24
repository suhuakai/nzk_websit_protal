package com.web.core.util;
import com.web.core.entity.FbarcodeInfo;

/**
 * HIBC条码解析
 */
public class HIBCUtil {

	/**
	 * 解析整个条形码
	 * @param inStr
	 * @return
	 */
	public static FbarcodeInfo HIBCExtract(String inStr) {
		FbarcodeInfo fbarcodeInfo = new FbarcodeInfo();
		try {
	    	//分割出主码
			//HIBC主码和次码单独使用时都以'+'开头
			//次码单独使用的时候以'+$$开头'
			//主码次码一起的时候用'/'分割
			String splitFirst[] = inStr.split("\\/");
			if(splitFirst.length == 2){
				String mainFbarcode = splitFirst[0];
				fbarcodeInfo = HIBCExtractSec(mainFbarcode,splitFirst[1]);
			}else{
				fbarcodeInfo.setMessage("error code");
			}
	    }catch (Exception e){
			e.printStackTrace();
		}
	    return fbarcodeInfo;
	}

	/**
	 * 解析次码,code: 主码；sec：次码
	 */
	public static FbarcodeInfo HIBCExtractSec(String code,String sec) {
		FbarcodeInfo fbarcodeInfo = new FbarcodeInfo();
		try {
			//$$
			fbarcodeInfo.setFbarcode(code);
			fbarcodeInfo.setFbarcodeSec(sec);
		}catch (Exception e){
			e.printStackTrace();
		}
		return fbarcodeInfo;
	}
	 // 测试主函数  
    public static void main(String args[]) {  
        String s = new String("+H124002347031061/$$1012311234567L");

        FbarcodeInfo fbarcodeInfo = HIBCExtract(s);
		System.out.println("主码：" + fbarcodeInfo.getFbarcode());
		System.out.println("次码：" + fbarcodeInfo.getFbarcodeSec());
		System.out.println("xinxi：" + fbarcodeInfo.getMessage());

	}
	
}
