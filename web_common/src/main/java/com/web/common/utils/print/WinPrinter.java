package com.web.common.utils.print;

import java.awt.Desktop;
/**  

* <p>Title: WinPrinter</p>  

* <p>Description: </p>  

* @author

*

*/
import java.io.File;

public class WinPrinter {

	// 打印指定的文件
	public static void print(File file) {
		Desktop desktop = null;
		if (Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
			try {
				desktop.print(file);
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File file = new File("d://222.pdf");
		print(file);
		
		
	}
}