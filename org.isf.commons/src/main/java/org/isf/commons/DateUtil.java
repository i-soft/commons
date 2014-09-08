package org.isf.commons;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	
	private static final int DAYS_IN_JANUARY = 31;
	private static final int DAYS_IN_FEBRUARY = 28; // + mod
	private static final int DAYS_IN_MARCH = 31;
	private static final int DAYS_IN_APRIL = 30;
	private static final int DAYS_IN_MAY = 31;
	private static final int DAYS_IN_JUNE = 30;
	private static final int DAYS_IN_JULY = 31;
	private static final int DAYS_IN_AUGUST = 31;
	private static final int DAYS_IN_SEPTEMBER = 30;
	private static final int DAYS_IN_OCTOBER = 31;
	private static final int DAYS_IN_NOVEMBER = 30;
	private static final int DAYS_IN_DECEMBER = 31;

	public static final String STD_STRING_TO_DATE = "yyyy-MM-dd-HH:mm:ss.S";
	public static final String STD_STRING_TO_SQLDATE = "yyyy-MM-dd";
	public static final String STD_STRING_TO_SQLTIME = "HH:mm:ss.S";
	public static final String STD_STRING_TO_SQLTIMESTAMP = STD_STRING_TO_DATE;
	
	public static Date toDate(String s) {
		return toDate(STD_STRING_TO_DATE, s);
	}
	
	public static Date toDate(String format, String s) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(s);
		} catch(ParseException pe) {
			return null;
		}
	}
	
	public static Date toDate(java.sql.Date sqlDate) {
		return sqlDate == null ? null :new Date(sqlDate.getTime());
	}
	
	public static Date toDate(Time sqlTime) {
		return sqlTime == null ? null : new Date(sqlTime.getTime());
	}
	
	public static Date toDate(Timestamp sqlTimestamp) {
		return sqlTimestamp == null ? null : new Date(sqlTimestamp.getTime());
	}
	
	public static java.sql.Date toSqlDate(String s) {
		return toSqlDate(toDate(STD_STRING_TO_SQLDATE, s));
	}
	
	public static java.sql.Date toSqlDate(Date date) {
		return date == null ? null : new java.sql.Date(date.getTime());
	}
	
	public static Time toSqlTime(String s) {
		return toSqlTime(toDate(STD_STRING_TO_SQLTIME, s));
	}
	
	public static Time toSqlTime(Date date) {
		return new Time(date.getTime());
	}
	
	public static Timestamp toSqlTimestamp(String s) {
		return toSqlTimestamp(toDate(STD_STRING_TO_SQLTIMESTAMP, s));
	}
	
	public static Timestamp toSqlTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
	
	public static String toString(Date date) {
		return toString(STD_STRING_TO_DATE, date);
	}
	
	public static String toString(String format, Date date) {
		if (date == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static String toString(java.sql.Date sqlDate) {
		return toString(STD_STRING_TO_SQLDATE, toDate(sqlDate));
	}
	
	public static String toString(Time sqlTime) {
		return toString(STD_STRING_TO_SQLTIME, toDate(sqlTime));
	}
	
	public static String toString(Timestamp sqlTimestamp) {
		return toString(STD_STRING_TO_SQLTIMESTAMP, toDate(sqlTimestamp));
	}
	
	public static Calendar toCalendar(int year, int month, int day) {
		return toCalendar(year, month, day, 0);
	}
	
	public static Calendar toCalendar(int year, int month, int day, int hour) {
		return toCalendar(year, month, day, hour, 0);
	}
	
	public static Calendar toCalendar(int year, int month, int day, int hour, int minute) {
		return toCalendar(year, month, day, hour, minute, 0);
	}
	
	public static Calendar toCalendar(int year, int month, int day, int hour, int minute, int seconds) {
		return toCalendar(year, month, day, hour, minute, seconds, 0);
	}
	
	public static Calendar toCalendar(int year, int month, int day, int hour, int minute, int seconds, int millis) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, seconds);
		c.set(Calendar.MILLISECOND, millis);
		return c;
	}
	
	
	public static int getLeapYearModifier(int year) {
		if (year % 4 == 0) 
			if (year < 1582) return 1;
			else if (!(year % 100 == 0)) return 1;
			else if (year % 400 == 0) return 1;
		return 0;
	}
	
	public static boolean isLeapYear(int year) {
		return getLeapYearModifier(year) == 0;
	}
	
	public static int[] getMonthDays(int year) {
		return new int[] {DAYS_IN_JANUARY, DAYS_IN_FEBRUARY+getLeapYearModifier(year), DAYS_IN_MARCH, DAYS_IN_APRIL, 
				DAYS_IN_MAY, DAYS_IN_JUNE, DAYS_IN_JULY, DAYS_IN_AUGUST, DAYS_IN_SEPTEMBER, DAYS_IN_OCTOBER, 
				DAYS_IN_NOVEMBER, DAYS_IN_DECEMBER};
	}
	
	public static int dayOfTheYear(int day, int month, int year) {
		int days = 0;
		int[] monthDays = getMonthDays(year);
		if (day <= monthDays[month-1]) {
			for (int i=0;i<month-1;i++)
				days += monthDays[i];
			days+=day;
		}
		return days;
	}
	
	public static int dayOfTheYear360(int day, int month, int year) {
		int days = 0;
		int[] monthDays = getMonthDays(year);
		if (day>=monthDays[month-1]) day=30;
			
//		if ((month == 2 && (day == 28 || day == 29)) || (day == 31)) day = 30; 
			
		for (int i=0;i<month-1;i++) days+=30;
		days+=day;
		
		return days;
	}
	
	public static int daysInYear(int year) { return 365+getLeapYearModifier(year); }
	
	public static int daysBetween(int aday, int amonth, int ayear, int bday, int bmonth, int byear) {
		int ret = 0;
		int adays = dayOfTheYear(aday, amonth, ayear);
		int bdays = dayOfTheYear(bday, bmonth, byear);
		if ((ayear > byear) || ((ayear == byear) && (adays > bdays))) {
			int cday = aday; int cmonth = amonth; int cyear = ayear;
			aday=bday;amonth=bmonth;ayear=byear;
			bday=cday;bmonth=cmonth;byear=cyear;
			adays = dayOfTheYear(aday, amonth, ayear);
			bdays = dayOfTheYear(bday, bmonth, byear);
		}
		
		if (ayear == byear) ret = bdays-adays;
		else {
			ret = daysInYear(ayear)-adays;
			int year = ayear+1;
			while(year < byear)
				ret += daysInYear(year++);
			ret+=bdays;
		}
		
		return ret;
	}
	
	public static int daysBetween(Date a, Date b) {
		if (a == null || b == null) return 0;
		Calendar cala = Calendar.getInstance();
		cala.setTimeInMillis(a.getTime());
		Calendar calb = Calendar.getInstance();
		calb.setTimeInMillis(b.getTime());
		return daysBetween(cala.get(Calendar.DATE), cala.get(Calendar.MONTH)+1,cala.get(Calendar.YEAR),
				calb.get(Calendar.DATE), calb.get(Calendar.MONTH)+1,calb.get(Calendar.YEAR));
	}
	
	public static int daysBetween360(int aday, int amonth, int ayear, int bday, int bmonth, int byear) {
		int ret = 0;
		int adays = dayOfTheYear360(aday, amonth, ayear);
		int bdays = dayOfTheYear360(bday, bmonth, byear);
		if ((ayear > byear) || ((ayear == byear) && (adays > bdays))) {
			int cday = aday; int cmonth = amonth; int cyear = ayear;
			aday=bday;amonth=bmonth;ayear=byear;
			bday=cday;bmonth=cmonth;byear=cyear;
			adays = dayOfTheYear360(aday, amonth, ayear);
			bdays = dayOfTheYear360(bday, bmonth, byear);
		}
		
		
		if (ayear == byear) ret = bdays-adays;
		else {
			ret = 360-adays;
			int year = ayear+1;
			while(year < byear)
				ret += 360;
			ret+=bdays;
		}
		
		return ret;
	}
	
	public static int daysBetween360(Date a, Date b) {
		Calendar cala = Calendar.getInstance();
		cala.setTimeInMillis(a.getTime());
		Calendar calb = Calendar.getInstance();
		calb.setTimeInMillis(b.getTime());
		return daysBetween360(cala.get(Calendar.DATE), cala.get(Calendar.MONTH)+1,cala.get(Calendar.YEAR),
				calb.get(Calendar.DATE), calb.get(Calendar.MONTH)+1,calb.get(Calendar.YEAR));
	}
	
	public static Date lastDayOfMonth(int month, int year) {
		Calendar cal = new GregorianCalendar(year, month-1, getMonthDays(year)[month-1]);
		return cal.getTime();
	}
	
	public static Date lastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, getMonthDays(cal.get(Calendar.YEAR))[cal.get(Calendar.MONTH)]);
		return cal.getTime();
	}
	
	public static Date lastDayOfMonth360(int month, int year) {
		return lastDayOfMonth360(new GregorianCalendar(year, month-1, 1).getTime());
	}
	
	public static Date lastDayOfMonth360(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastDayOfMonth(date));
		if (cal.get(Calendar.DAY_OF_MONTH) == 31) cal.set(Calendar.DAY_OF_MONTH, 30);
		return cal.getTime();
	}
	
	public static int getMonthsBetween(Date d1, Date d2) {
		if (d1.getTime() > d2.getTime()) {
			Date tmp = d1;
			d1 = d2;
			d2 = tmp;
		}
		Calendar cd1 = Calendar.getInstance();
		cd1.setTime(d1);
		Calendar cd2 = Calendar.getInstance();
		cd1.setTime(d2);
		int yd = cd2.get(Calendar.YEAR)-cd1.get(Calendar.YEAR);
		int mbw = cd2.get(Calendar.MONTH)-cd1.get(Calendar.MONTH)+12*yd;
		if (cd2.get(Calendar.DAY_OF_MONTH) >= cd1.get(Calendar.DAY_OF_MONTH))
			mbw++;
		return mbw;
	}
	
	public static boolean isLessOrEqual(Date d1, Date d2) {
		if ((d1 == null && d2 == null) || (d1 == null && d2 != null)) return true;
		else if (d1 != null && d2 == null) return false;
		Calendar cala = Calendar.getInstance();
		cala.setTimeInMillis(d1.getTime());
		Calendar calb = Calendar.getInstance();
		calb.setTimeInMillis(d2.getTime());
		return isLessOrEqual(cala.get(Calendar.DATE), cala.get(Calendar.MONTH)+1,cala.get(Calendar.YEAR),
				calb.get(Calendar.DATE), calb.get(Calendar.MONTH)+1,calb.get(Calendar.YEAR));
	}
	
	public static boolean isLessOrEqual(int aday, int amonth, int ayear, int bday, int bmonth, int byear) {
		int adays = dayOfTheYear(aday, amonth, ayear);
		int bdays = dayOfTheYear(bday, bmonth, byear);
		return ((ayear < byear) || ((ayear == byear) && (adays <= bdays)));
	}
	
	public static boolean isLess(Date d1, Date d2) {
		if (d1 == null && d2 != null) return true;
		else if ((d1 == null && d2 == null) || (d1 != null && d2 == null)) return false;
		Calendar cala = Calendar.getInstance();
		cala.setTimeInMillis(d1.getTime());
		Calendar calb = Calendar.getInstance();
		calb.setTimeInMillis(d2.getTime());
		return isLess(cala.get(Calendar.DATE), cala.get(Calendar.MONTH)+1,cala.get(Calendar.YEAR),
				calb.get(Calendar.DATE), calb.get(Calendar.MONTH)+1,calb.get(Calendar.YEAR));
	}
	
	public static boolean isLess(int aday, int amonth, int ayear, int bday, int bmonth, int byear) {
		int adays = dayOfTheYear(aday, amonth, ayear);
		int bdays = dayOfTheYear(bday, bmonth, byear);
		return ((ayear < byear) || ((ayear == byear) && (adays < bdays)));
	}
	
	public static boolean isGreaterOrEqual(Date d1, Date d2) {
		if ((d1 == null && d2 == null) || (d1 != null && d2 == null)) return true;
		else if (d1 == null && d2 != null) return false;
		Calendar cala = Calendar.getInstance();
		cala.setTimeInMillis(d1.getTime());
		Calendar calb = Calendar.getInstance();
		calb.setTimeInMillis(d2.getTime());
		return isGreaterOrEqual(cala.get(Calendar.DATE), cala.get(Calendar.MONTH)+1,cala.get(Calendar.YEAR),
				calb.get(Calendar.DATE), calb.get(Calendar.MONTH)+1,calb.get(Calendar.YEAR));
	}
	
	public static boolean isGreaterOrEqual(int aday, int amonth, int ayear, int bday, int bmonth, int byear) {
		int adays = dayOfTheYear(aday, amonth, ayear);
		int bdays = dayOfTheYear(bday, bmonth, byear);
		return ((ayear > byear) || ((ayear == byear) && (adays >= bdays)));
	}
	
	public static boolean isGreater(Date d1, Date d2) {
		if (d1 != null && d2 == null) return true;
		else if ((d1 == null && d2 == null) || (d1 == null && d2 != null)) return false;
		Calendar cala = Calendar.getInstance();
		cala.setTimeInMillis(d1.getTime());
		Calendar calb = Calendar.getInstance();
		calb.setTimeInMillis(d2.getTime());
		return isGreater(cala.get(Calendar.DATE), cala.get(Calendar.MONTH)+1,cala.get(Calendar.YEAR),
				calb.get(Calendar.DATE), calb.get(Calendar.MONTH)+1,calb.get(Calendar.YEAR));
	}
	
	public static boolean isGreater(int aday, int amonth, int ayear, int bday, int bmonth, int byear) {
		int adays = dayOfTheYear(aday, amonth, ayear);
		int bdays = dayOfTheYear(bday, bmonth, byear);
		return ((ayear > byear) || ((ayear == byear) && (adays > bdays)));
	}
	
	public static boolean isEqual(Date d1, Date d2) {
		if (d1 == null && d2 == null) return true;
		else if (d1 == null ^ d2 == null) return false;
		Calendar cala = Calendar.getInstance();
		cala.setTimeInMillis(d1.getTime());
		Calendar calb = Calendar.getInstance();
		calb.setTimeInMillis(d2.getTime());
		return isEqual(cala.get(Calendar.DAY_OF_MONTH), cala.get(Calendar.MONTH)+1,cala.get(Calendar.YEAR),
				calb.get(Calendar.DAY_OF_MONTH), calb.get(Calendar.MONTH)+1,calb.get(Calendar.YEAR));
	}
	
	public static boolean isEqual(int aday, int amonth, int ayear, int bday, int bmonth, int byear) {
		int adays = dayOfTheYear(aday, amonth, ayear);
		int bdays = dayOfTheYear(bday, bmonth, byear);
		return ayear == byear && adays == bdays;
	}
	
	public static boolean between(Date date, Date from, Date to) {
		if (from == null && to == null) return false;
		else if (from == null) return isLessOrEqual(date, to);  // EURO-Leasing Logic
		else if (to == null) return isGreater(date, from);  // EURO-Leasing Logic
		else return isGreaterOrEqual(date, from) && isLessOrEqual(date, to);
	}
	
	public static int weekdaysBetween(Date from, Date to, int ... weekdays) {
		int cnt = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(from);
		while (!isEqual(c.getTime(), to)) {
			c.add(Calendar.DAY_OF_MONTH, 1);
			for (int wd : weekdays) {
				if (wd == c.get(Calendar.DAY_OF_WEEK)) {
					cnt++;
				}
			}
		}
		return cnt;
	}
	
	public static int getCurrentWeek() { return getISOCalendar().get(Calendar.WEEK_OF_YEAR); }
	
	public static Calendar getISOCalendar() {
		Calendar c = Calendar.getInstance();
		c.setMinimalDaysInFirstWeek(4);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		return c;
	}
	
	public static Date[] getLastWeeks(int count) {
		Date[] ret = new Date[(count+1)*7];
		Calendar c = getISOCalendar();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.add(Calendar.WEEK_OF_YEAR, -1*count);
		for (int i=0;i<ret.length;i++) {
			ret[i] = c.getTime();
			c.add(Calendar.DAY_OF_MONTH, 1);
		}			
		return ret;
	}
	
	public static void main(String[] args) {
//		Date pstart = (new GregorianCalendar(2013, 0, 17)).getTime();
//		Date pend = (new GregorianCalendar(2013, 0, 31)).getTime();
//		System.out.println("days between(360) "+toString("yyyy-MM-dd", pstart)+" and "+toString("yyyy-MM-dd", pend)+" "+(daysBetween360(pstart, pend)+1));
//		pstart = (new GregorianCalendar(2013, 1, 17)).getTime();
//		pend = (new GregorianCalendar(2013, 1, 28)).getTime();		
//		System.out.println("days between(360) "+toString("yyyy-MM-dd", pstart)+" and "+toString("yyyy-MM-dd", pend)+" "+(daysBetween360(pstart, pend)+1));
//		pstart = (new GregorianCalendar(2013, 1, 1)).getTime();
//		pend = (new GregorianCalendar(2013, 1, 28)).getTime();		
//		System.out.println("days between(360) "+toString("yyyy-MM-dd", pstart)+" and "+toString("yyyy-MM-dd", pend)+" "+(daysBetween360(pstart, pend)+1));
//		pstart = (new GregorianCalendar(2013, 2, 1)).getTime();
//		pend = (new GregorianCalendar(2013, 2, 31)).getTime();		
//		System.out.println("days between(360) "+toString("yyyy-MM-dd", pstart)+" and "+toString("yyyy-MM-dd", pend)+" "+(daysBetween360(pstart, pend)+1));
//		pstart = (new GregorianCalendar(2013, 3, 1)).getTime();
//		pend = (new GregorianCalendar(2013, 3, 18)).getTime();		
//		System.out.println("days between(360) "+toString("yyyy-MM-dd", pstart)+" and "+toString("yyyy-MM-dd", pend)+" "+(daysBetween360(pstart, pend)+1));
//		
//		pstart = (new GregorianCalendar(2013, 8, 5)).getTime();
//		pend = (new GregorianCalendar(2013, 8, 21)).getTime();
//		System.out.println("weekdaysBetween "+toString("yyyy-MM-dd", pstart)+" and "+toString("yyyy-MM-dd", pend)+" "+weekdaysBetween(pstart, pend, Calendar.SATURDAY, Calendar.SUNDAY));
		
	}
	
}
