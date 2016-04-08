package br.com.botmoter.util;

import org.joda.time.LocalTime;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public final class Constants {

	public static final LocalTime TIME_5_00 = new LocalTime(5, 0);
	public static final LocalTime TIME_11_00 = new LocalTime(11, 0);
	public static final LocalTime TIME_15_00 = new LocalTime(15, 0);
	public static final LocalTime TIME_18_00 = new LocalTime(18, 0);
	public static final LocalTime TIME_21_00 = new LocalTime(21, 0);
	public static final LocalTime TIME_23_00 = new LocalTime(23, 0);
	public static final LocalTime TIME_23_59 = LocalTime.MIDNIGHT.minusSeconds(1);
	public static final LocalTime TIME_00_00 = LocalTime.MIDNIGHT;
	private Constants() {
	}

}
