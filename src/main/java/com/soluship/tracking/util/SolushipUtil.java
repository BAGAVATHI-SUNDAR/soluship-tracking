package com.soluship.tracking.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolushipUtil {
	
	public static boolean isBeginWith(String source, String subItem){
		subItem=addEscapeCharToSpecialChar(subItem);
		String pattern = "^"+subItem;
		Pattern p=Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher m=p.matcher(source);
		return m.find();
	}

	/**
	 * This method is for replacing all special characters to normal character by adding escape character 
	 * @param subItem
	 * @return string without special character with escape character
	 */
	public static String addEscapeCharToSpecialChar(String subItem){
		subItem = subItem.replaceAll("[\\<\\(\\[\\{\\\\\\^\\-\\=\\$\\!\\|\\]\\}\\)\\?\\*\\+\\.\\>]", "\\\\$0");
		return subItem;
	}
}
