package com.hydra.common.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvert implements Converter<String, Date> {

	public Date convert(String string) {
		Date date = null;
		if (string != null) {
			if (!string.equals("")) {
				SimpleDateFormat format = null;
				if (string.contains(":")) {
					format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				} else {
					format = new SimpleDateFormat("yyyy-MM-dd");
				}
				try {
					date = format.parse(string);
					return date;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return date;
	}
}