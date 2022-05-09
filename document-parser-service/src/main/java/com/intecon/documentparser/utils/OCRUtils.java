package com.intecon.documentparser.utils;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Value;

public class OCRUtils {
	private static int OSTYPE = 2;// 1=WIN,2=LINUX
	@Value("${global.context.path}")
	private static String globalContextPath;
	private static String INVOICECONFPATH = globalContextPath + "V2/files/dimdata/diminvoice.conf";


	public static String cleanForSeriNo(String seriNo) {
		return seriNo.trim().replace("SER�", "").replace("SERD", "").replace("SERI", "").replace("SERO", "").replace("SEB�", "").replace("SER�", "").replace("Seri", "")
				.replace("SERl", "").replace("SEH�", "").replace(":", "").replace("�", "A").replace("8", "B").replace(".", "").replace(",", "").replace("/", "")
				.replace("\\", "").replace("(", "").replace(")", "").trim();
	}

	public static String cleanForItem(String item) {
		return item.trim().replace(".", "").replace(",", "").trim();
	}

	public static String cleanForUnitCode(String unitCode) {
		return unitCode.trim().replace('7', 'A').replace("I)", "D").replace("0", "D").replace(".", "").replace(",", "").trim();
	}

}
