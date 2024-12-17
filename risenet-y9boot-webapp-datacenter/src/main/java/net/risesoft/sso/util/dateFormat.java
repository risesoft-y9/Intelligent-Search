package net.risesoft.sso.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.util.StdDateFormat;

public class dateFormat extends DateFormat {
	private static final long serialVersionUID = -6176158877433688690L;

	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	StdDateFormat defaultDF = StdDateFormat.instance;

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		return df1.format(date, toAppendTo, fieldPosition);
	}

	@Override
	public Date parse(String source, ParsePosition pos) {
		Date date = detectFormat(source).parse(source, pos);
		return date;
	}

	@Override
	public Object clone() {
		return this;
	}

	private DateFormat detectFormat(String source) {
		if (source == null) {
			return df1;
		}

		String str = source.trim();
		if (str.length() == "yyyy-MM-dd HH:mm:ss".length() && str.contains("-") && str.contains(" ") && str.contains(":")) {
			return df1;
		} else if (str.length() == "yyyy-MM-dd HH:mm".length() && str.contains("-") && str.contains(" ") && str.contains(":")) {
			return df2;
		}
		return defaultDF;
	}
}
