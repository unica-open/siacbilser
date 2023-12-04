/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class TimingUtils {

	
	enum FormatoData{
		DB("yyyy-MM-dd HH:mm:ss"),
		aaaaMMdd_HHmmssSSS("yyyy-MM-dd HH:mm:ss.SSS"),
		ggMMaaaahhmmss("dd/MM/yyyy HH:mm:ss"),
		ggMMyyyy_hhmmss("dd-MM-yyyy hh:mm:ss"),
		ggMMaaaa("dd/MM/yyyy"),
		yyyyMMdd("yyyy-MM-dd"),
		MMMddHHmmssyyyyz("MMM dd HH:mm:ss yyyy zzz", Locale.UK),
		MMMddHHmmssyyy("MMM dd HH:mm:ss yyyy");
		
		private String pattern;
		private Locale locale;

		FormatoData(String pattern){
			this.pattern = pattern;
			this.locale = Locale.getDefault();
		}

		FormatoData(String pattern, Locale locale){
			this.pattern = pattern;
			this.locale = locale;
		}

		public SimpleDateFormat getFormatter(){
			return new SimpleDateFormat(pattern, locale);
		}
		
	}
	
	public final static String convertiDataIn_GgMmYyyy(Date _time){
		if (_time==null) return null;
		try{
			return FormatoData.ggMMaaaa.getFormatter().format(_time);
		} catch (Exception e){
			return null;
		}
	}
	
	
	public final static String convertiDataIn_GgMmYyyy(Timestamp _time){
		if (_time==null) return null;
		try{
			return FormatoData.ggMMaaaa.getFormatter().format(_time);
		} catch (Exception e){
			return null;
		}
	}
	
	public final static String convertiDataIn_GgMmYyyy_hhmmss(Timestamp _time){
		if (_time==null) return null;
		try{
			return FormatoData.ggMMaaaahhmmss.getFormatter().format(_time);
		} catch (Exception e){
			return null;
		}
	}
	
	public final static Timestamp convertiDataInTimeStamp(Date data){
		Timestamp ts= null;
		try{
			ts= new  Timestamp(data.getTime());
		return  ts;
		}catch(Exception e){
		return ts;
		}
	}
	
	public final static Timestamp getNowTs(){
		return new Timestamp(System.currentTimeMillis());
	}
	
	public final static Date getNowDate(){
		Timestamp stamp = getNowTs();
		Date date = new Date(stamp.getTime());
		return date;
	}
	
	public final static Date resetMillisecond(Date data){
	    Calendar cTo = Calendar.getInstance();
	      cTo.setTime(data);

	      cTo.set(Calendar.MILLISECOND, 0);
	      return cTo.getTime();
	}
	
	/**
	 * Data una certa data restituisce il timestamp alla mezzanotte meno un millisecondo
	 * @param data
	 * @return
	 */
	public static Timestamp setToMidNigth(Date data){
		Calendar cTo = Calendar.getInstance();
	    cTo.setTime(data);
	    cTo.set(Calendar.HOUR_OF_DAY, 23);
	    cTo.set(Calendar.MINUTE, 59);
	    cTo.set(Calendar.SECOND, 59);
	    cTo.set(Calendar.MILLISECOND, 999);
	    Timestamp dataOutput = new Timestamp(cTo.getTime().getTime());
	    return dataOutput;
	}
	
	public static Timestamp buildTimestamp(String _data){
		Timestamp result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
	    try{
	      result = new Timestamp(sdf.parse(_data).getTime());
	    }catch(Exception exc){
	    }
	    return result;
	}
	
	
	//SEMPLICI METODI DI UTILITIES PER PRENDERE MISURAZIONI SULLA DURATA DI PORZIONI DI CODICE:
	public static long start(){
		return System.currentTimeMillis();
	}
	
	public static long stop(long start){
		long stop = System.currentTimeMillis();
		long delta = stop - start;
		return delta;
	}
	
	public static long addToCount(long start, long tot){
		long time = stop(start);
		tot = tot + time;
		return tot;
	}
	//////////////////////////////////////////////////////////////////////////////////////
	
	public static Timestamp getUltimoGiornoAnnoBilancio(Integer annoBilancio){
		Timestamp tstampFineAnno = null;
		if(annoBilancio!=null){
		 //salvo l'attributo:
		 Calendar cal = GregorianCalendar.getInstance();
		 cal.set(Calendar.DAY_OF_MONTH, 31);
		 cal.set(Calendar.MONTH, 11);// -1 as month is zero-based
		 cal.set(Calendar.YEAR, annoBilancio);
		 cal.set(Calendar.HOUR_OF_DAY, 0);
		 cal.set(Calendar.MINUTE, 0);
		 cal.set(Calendar.SECOND, 0);
		 cal.set(Calendar.MILLISECOND, 0);
		 tstampFineAnno = new Timestamp(cal.getTimeInMillis());
		}
		return tstampFineAnno;
	}
	
	
	public static  BigDecimal timeInSecondi(long start, long stop) {
		long diff = stop - start;
		BigDecimal secondi = timeInSecondi(diff);
		return secondi;
	}
	
	public static  BigDecimal timeInSecondi(long elapsedMillisec) {
		BigDecimal secondi = new BigDecimal(elapsedMillisec / 1000);
		return secondi;
	}
	
	public static int getAnnoCorrente() {
		return getAnno(getNowDate());
	}
	
	/**
	 * Gets the anno.
	 *
	 * @param date the date
	 * @return the anno
	 */
	public static int getAnno(Date date) {
		GregorianCalendar cg =   new GregorianCalendar();
		cg.setTime(date);
		int anno = cg.get(Calendar.YEAR);
		return anno;
	}
	
	/**
	 * Primo istante dell'anno indicato
	 * @param anno
	 * @return
	 */
	public static Date getStartYear(int anno) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, anno);
		cal.set(Calendar.DAY_OF_YEAR, 1); 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * ultimo istante dell'anno indicato
	 * @param anno
	 * @return
	 */
	public static Date getEndYear(int anno) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, anno);
		cal.set(Calendar.MONTH, 11); // 11 = december
		cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	public static Timestamp getStartYearTs(int anno) {
		Date date = getStartYear(anno); 
		return convertiDataInTimeStamp(date);
	}
	
	public static Timestamp getEndYearTs(int anno) {
		Date date = getEndYear(anno); 
		return convertiDataInTimeStamp(date);
	}
	 
}
