package com.tigo.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

	public static double round(double value, int decimalDigits) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(decimalDigits, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	public static BigDecimal round(BigDecimal value, int decimalDigits) {
		value = value.setScale(decimalDigits, BigDecimal.ROUND_HALF_UP);
		return value;
	}

	public static String dateToString(Date date, String formato) {
		if (formato == null || formato.isEmpty()) {
			formato = "dd/MM/yyyy";
		}
		return date == null ? "" : new SimpleDateFormat(formato).format(date);
	}

	public static Date stringToDate(String date, String formato) throws ParseException {
		if (formato == null || formato.isEmpty()) {
			formato = "dd/MM/yyyy";
		}
		return date == null ? null : new SimpleDateFormat(formato).parse(date);
	}

	public static double stringToDouble(String valorString) throws Exception {
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
		char sep = symbols.getDecimalSeparator();
		valorString = valorString.replaceAll(",", "");
		if (sep == ',') {
			valorString = valorString.replaceAll("\\.", sep + "");
		}
		return format.parse(valorString).doubleValue();
	}

	public static int diferenciaDias(Date fechaIni, Date fechaFin) {
		Calendar ci = Calendar.getInstance();
		ci.setTime(fechaIni);

		Calendar cf = Calendar.getInstance();
		cf.setTime(fechaFin);

		long diferencia = cf.getTimeInMillis() - ci.getTimeInMillis();

		return (int) Math.ceil((double) diferencia / (1000 * 60 * 60 * 24));
	}

}
