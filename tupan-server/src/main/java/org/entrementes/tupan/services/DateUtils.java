package org.entrementes.tupan.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

	private static DateFormat webDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

	  public static Date asDate(LocalDate localDate) {
		  return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	  }

	  public static Date asDate(LocalDateTime localDateTime) {
		  return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	  }

	  public static LocalDate asLocalDate(Date date) {
	    return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	  }

	  public static LocalDateTime asLocalDateTime(Date date) {
	    return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	  }

	public static LocalDateTime parseWebDate(String dateString){
		try {
			return asLocalDateTime(webDateFormat.parse(dateString));
		}catch(ParseException ex){
			return null;
		}
	}
}
