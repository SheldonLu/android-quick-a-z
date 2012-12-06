package com.mzone.android.tools.util;

import android.annotation.SuppressLint;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@SuppressLint("DefaultLocale")
public class PingYinUtil {
	 /**
	    *
	    * @param inputString
	    * @return
	    */
	    public static String getPingYin(String inputString) {
	    	String pinyinName = "";
			char[] nameChar = inputString.toCharArray();
			HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
			defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			for (int i = 0; i < nameChar.length; i++) {
				if (nameChar[i] > 128) {
					try {
						pinyinName += PinyinHelper.toHanyuPinyinStringArray(
								nameChar[i], defaultFormat)[0].charAt(0);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					}
				} else {
					pinyinName += nameChar[i];
				}
			}
			return pinyinName.toUpperCase();
	    }
}