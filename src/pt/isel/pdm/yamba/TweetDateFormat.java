package pt.isel.pdm.yamba;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TweetDateFormat extends DateFormat {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6641804145275713299L;
	
	private static SimpleDateFormat _formatSec;
	private static SimpleDateFormat	_formatMin;
	private static SimpleDateFormat _formatHour;
	private static SimpleDateFormat _formatDay;
	private static SimpleDateFormat _formatYear;
	
	private static final String _formatSecPattern = "s's'";
	private static final String _formatMinPattern = "m'm'";
	private static final String _formatHourPattern = "H'h'";
	private static final String _formatDayPattern = "D'd'";
	private static final String _formatYearPattern = "'Over 'y' years'";
	
	private SimpleDateFormat getFormatSec() {
		if(_formatSec == null) {
			_formatSec = new SimpleDateFormat(_formatSecPattern);
		}
		return _formatSec;
	}
	
	private SimpleDateFormat getFormatMin() {
		if(_formatMin == null) {
			_formatMin = new SimpleDateFormat(_formatMinPattern);
		}
		return _formatMin;
	}
	
	private SimpleDateFormat getFormatHour() {
		if(_formatHour == null) {
			_formatHour = new SimpleDateFormat(_formatHourPattern);
		}
		return _formatHour;
	}
	
	private SimpleDateFormat getFormatDay() {
		if(_formatDay == null) {
			_formatDay = new SimpleDateFormat(_formatDayPattern);
		}
		return _formatDay;
	}
	
	private SimpleDateFormat getFormatYear() {
		if(_formatYear == null) {
			_formatYear = new SimpleDateFormat(_formatYearPattern);
		}
		return _formatYear;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public StringBuffer format(Date date, StringBuffer buffer, FieldPosition fieldPos) {
					
		if(date.getYear() == 70) {
			if(date.getMonth() == 0 && date.getDate() == 1) {
				if(date.getHours() == 0) {
					if(date.getMinutes() == 0) {
						return getFormatSec().format(date, buffer, fieldPos);
					}
					return getFormatMin().format(date, buffer, fieldPos);
				}
				return getFormatHour().format(date, buffer, fieldPos);
			}
			return getFormatDay().format(date, buffer, fieldPos);
		}
		else {
			return getFormatYear().format(date, buffer, fieldPos);
		}
	}

	@Override
	public Date parse(String arg0, ParsePosition arg1) {
		throw new UnsupportedOperationException("Parse isn't implemented!");
	}

	
	
}
