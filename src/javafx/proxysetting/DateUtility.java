package javafx.proxysetting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtility {

	public static String dateToString(Date date, String s) {
		SimpleDateFormat format = new SimpleDateFormat(s, Locale.ROOT);
		if (date != null)
			try {
				return format.format(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return "";
	}
}
