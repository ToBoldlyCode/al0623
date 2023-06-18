package com.example.rentalagreement;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class CalendarValidator {
	
	public static boolean isHoliday(LocalDate date) {
		Month month = date.getMonth();
		int dayOfMonth = date.getDayOfMonth();
		DayOfWeek dayOfWeek = date.getDayOfWeek();

		if (month == Month.JULY) {
			if ((dayOfMonth == 3 && dayOfWeek == DayOfWeek.FRIDAY) ||
				(dayOfMonth == 5 && dayOfWeek == DayOfWeek.MONDAY)) {
				return true;
			}
			if ((dayOfMonth == 4) && (dayOfWeek != DayOfWeek.SATURDAY) && (dayOfWeek != DayOfWeek.SUNDAY)) {
				return true;
			}
		} else if ((month == Month.SEPTEMBER) && (dayOfWeek == DayOfWeek.MONDAY) && (dayOfMonth < 8)) {
			return true;
		}

		return false;
	}
	
	public static boolean isWeekend(LocalDate date) {
		DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
	}
	
}