package timer.didadida;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	private static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final int TIME = 0;
	public static final int DATE = 1;
	public static final int DATE_TIME = 2;
	
	public TimeUtil() {
	}
	
	public static String getCurrentTime() {
		return getCurrentTime(TIME);
	}
	
	public static String getCurrentTime(int type) {
		String result = null;
		
		Date now = new Date(System.currentTimeMillis());
		switch (type) {
		case TIME:
			result = sdfTime.format(now);
			break;
		case DATE:
			result = sdfDate.format(now);
			break;
		case DATE_TIME:
			result = sdfDateTime.format(now);
			break;
		default:
			break;
		}
		return result;
	}
}
