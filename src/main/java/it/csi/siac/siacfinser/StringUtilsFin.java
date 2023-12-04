/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class StringUtilsFin {
	
	
	
	public static boolean boolValueConNullConsideratoFalse(Boolean b){
		if(b == null || !b){
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean sonoUgualiConNullConsideratoFalse(Boolean s1,Boolean s2){
		boolean valore1 = boolValueConNullConsideratoFalse(s1);
		boolean valore2 = boolValueConNullConsideratoFalse(s2);
		if(valore1==valore2){
			return true;
		} else {
			return false;
		}
		
	}
	
	public static boolean sonoUguali(Timestamp s1,Timestamp s2){
		if(s1==null && s2!=null){
			return false;
		}
		if(s1!=null && s2==null){
			return false;
		}
		if(s1==null && s2==null){
			return true;
		}
		return s1.equals(s2);
	}
	
	public static boolean sonoUgualiTrimmed(String s1,String s2){
		if(isEmpty(s1) && !isEmpty(s2)){
			return false;
		}
		if(!isEmpty(s1) && isEmpty(s2)){
			return false;
		}
		if(isEmpty(s1) && isEmpty(s2)){
			return true;
		}
		s1 = s1.trim();
		s2 = s2.trim();
		return s1.equalsIgnoreCase(s2);
	}
	
	public static boolean isNumeroIntero(String numero) {
		boolean isNumero = false;
		if(!isEmpty(numero)){
			try  {
				Integer intNumber = Integer.parseInt(numero); 
				isNumero = true;
			}catch (Throwable t){
				isNumero = false;
			}
		}
		return isNumero;
	}
	
	
	public static String convertiBigDecimalToImporto(BigDecimal importoDB) {

		String importoFormattato = null;

		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		df = (DecimalFormat)NumberFormat.getNumberInstance(Locale.ITALY);
		df.setParseBigDecimal(true);
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		importoFormattato = df.format(importoDB);

		return importoFormattato;
	}
	
	
	
	public static BigDecimal convertiImportoToBigDecimal(String importoFormattato) {

		BigDecimal importoDB = null;

		if(null!=importoFormattato && importoFormattato.contains(".")){
			importoFormattato = importoFormattato.replace(".", "");
			if(StringUtilsFin.isEmpty(importoFormattato)) importoFormattato = "0";
		}
		
		
		if(null!=importoFormattato && importoFormattato.contains(",")){

			importoFormattato = importoFormattato.replace(",", ".");
			if(StringUtilsFin.isEmpty(importoFormattato)) importoFormattato = "0";
		}	
		
		
		importoDB = new BigDecimal(importoFormattato).setScale(2,BigDecimal.ROUND_HALF_UP);

		return importoDB;
	}
	
	
	public static boolean sonoUguali(String s1,String s2){
		if(isEmpty(s1) && !isEmpty(s2)){
			return false;
		}
		if(!isEmpty(s1) && isEmpty(s2)){
			return false;
		}
		if(isEmpty(s1) && isEmpty(s2)){
			return true;
		}
		return s1.equalsIgnoreCase(s2);
	}
	
	public static boolean sonoUguali(String s1,Integer s2){
		String s2Stringed = null;
		if(s2!=null){
			s2Stringed = s2.toString();
		}
		return sonoUguali(s1, s2Stringed);
	}
	
	public static boolean sonoUguali(BigDecimal s1,BigDecimal s2){
		if(s1==null && s2!=null){
			return false;
		}
		if(s1!=null && s2==null){
			return false;
		}
		if(s1==null && s2==null){
			return true;
		}
		return s1.equals(s2);
	}
	
	public static boolean sonoUguali(Integer s1,Integer s2){
		if(s1==null && s2!=null){
			return false;
		}
		if(s1!=null && s2==null){
			return false;
		}
		if(s1==null && s2==null){
			return true;
		}
		return s1.equals(s2);
	}
	
	/**
	 * Ritorna true solo se sono uguali come valore numerico
	 * 
	 * Non considera l'uguaglianza tra tutti e due null!
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean sonoUgualiAndNotNull(Integer s1,String s2){
		if(s1==null || s2==null){
			return false;
		}
		if(!isNumeroIntero(s2)){
			return false;
		}
		Integer intNumber = Integer.parseInt(s2); 
		if(intNumber.intValue()==s1.intValue()){
			return true;
		} else {
			return false;
		}

	}
	
	public static boolean sonoUgualiBoolDb(String s1,String s2){
		String s1Std = checkStringBooleanForDb(s1);
		String s2Std = checkStringBooleanForDb(s2);
		return sonoUguali(s1Std, s2Std);
	}
	
	
	public final static boolean isEmpty(String s) {
		if (s == null)
			return true;
		else{
			return "".equals(s.trim());
		}
	}
	
	public final static List<String> getListByToken(String stringa, String token){
		String[] array = getArrayByToken(stringa, token);
		return arrayToArrayList(array);
	}
	
	/**
	 * Esegue il tokenizer sulla stringa rispetto al token indicato e restituisce
	 * i vari token in un array di stringhe
	 * @param stringa
	 * @param token
	 * @return
	 */
	public final static String[] getArrayByToken(String stringa, String token){
		String tokens[] = null;
		if(stringa == null || token == null)
			return null;
		if(token.length() >= stringa.length()){
			if(token.equals(stringa)){
				return null;
			}
			tokens = new String[1];
			tokens[0] = stringa;
			return tokens;
		} 
		StringTokenizer st = new StringTokenizer(stringa, token);
		if(st!=null && st.countTokens()>0){
			tokens = new String[st.countTokens()];
			int i=0;
			while(st.hasMoreTokens()){
				tokens[i] = st.nextToken();
				i++;
			}
		}
		return tokens;
	}
	
	public static ArrayList<String> arrayToArrayList(String[] parole){
		 ArrayList<String> list = new ArrayList<String>();
		 if(parole!=null && parole.length>0){
			 for(int i=0;i<parole.length;i++){
				 list.add(parole[i]);
			 }
		 }
		 return list;
	}
	
	public final static boolean contieneNumeri(String s) {
		boolean contieneNumeri = false;
		if(!isEmpty(s) && s.length()>0){
			for (int i = 0; i < s.length(); i++){
				if (Character.isDigit(s.charAt(i))){
					return true;
		        }
			}
		}
	    return contieneNumeri;
	}
	
	public final static boolean contieneSoloNumeri(String s) {
		boolean contieneSoloNumeri = false;
		if(!isEmpty(s) && s.length()>0){
			contieneSoloNumeri = true;
			String support = s.trim();
			for (int i = 0; i < support.length(); i++){
				if (!Character.isDigit(support.charAt(i))){
					return false;
		        }
			}
		}
	    return contieneSoloNumeri;
	}
	
	public final static String booleanToStringForDb(boolean b){
		if(b){
			return CostantiFin.TRUE;
		} else {
			return CostantiFin.FALSE;
		}
	}
	
	public final static boolean stringToBooleanForDb(String s) {
		if (CostantiFin.TRUE.equals(s)) {
			return true;
		}
		
		return false;
	}
	
	
	public final static boolean isTrue(String s){
		return stringToBooleanForDb(checkStringBooleanForDb(s));
	}
	
	public final static String checkStringBooleanForDb(String s){
		if(StringUtilsFin.isEmpty(s)){
			return CostantiFin.FALSE;
		} else if("SI".equalsIgnoreCase(s)){
			return CostantiFin.TRUE;
		} else if("S".equalsIgnoreCase(s)){
			return CostantiFin.TRUE;
		} else if("true".equalsIgnoreCase(s)){
			return CostantiFin.TRUE;
		} else {
			return CostantiFin.FALSE;
		}
	}
	
	public static String toLowerSafe(String s){
		String lower = "";
		if(s!=null){
			lower = s.toLowerCase();
		}
		return lower;
	}
	
	public static String toUpperSafe(String s){
		String upper = "";
		if(s!=null){
			upper = s.toUpperCase();
		}
		return upper;
	}
	
	public static boolean contenutoIn(String s, String ... list){
		for(String it: list){
			if(sonoUguali(s, it)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean contenutoIn(String s, List<String> list){
		if(!isEmpty(list)){
			for(String it: list){
				if(sonoUguali(s, it)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean contenutoIn(Integer valore, List<Integer> list){
		if(!isEmpty(list) && valore!=null){
			for(Integer it: list){
				if(it!=null && it.intValue()==valore.intValue()){
					return true;
				}
			}
		}
		return false;
	}
	
	public static <T extends Object> boolean isEmpty(List<T> list){
		if(numeroElementiNonNulli(list)>0){
			return false;
		} else {
			return true;
		}
	}
	
	
	public static <T extends Object> int numeroElementi(List<T> list){
		int numero = 0;
		if(list!=null){
			numero = list.size();
		}
		return numero;
	}
	
	public static <T extends Object> int numeroElementiNonNulli(List<T> list){
		int numero = 0;
		if(list!=null && list.size()>0){
			int nonNulli = 0;
			for(T it : list){
				if(it!=null){
					nonNulli++;
				}
			}
			numero = nonNulli;
		}
		return numero;
	}
	
	public static <T extends Object> List<T> getElementiNonNulli(List<T> list){
		List<T> nonNulli = null;
		int numeroNonNulli = numeroElementiNonNulli(list);
		if(numeroNonNulli>0){
			nonNulli = new ArrayList<T>(numeroNonNulli);
			for(T it : list){
				if(it!=null){
					nonNulli.add(it);
				}
			}
		}
		return nonNulli;
	}
	
	public final static <T extends Object> List<List<T>> esplodiInListe(List<T> listone, int dimensione){
		List<List<T>> listaDiListe = new ArrayList<List<T>>();
		
		//per evitare errori:
		List<T> listoneSenzaNulli = getElementiNonNulli(listone);
		//
		
		if(listoneSenzaNulli!=null && listoneSenzaNulli.size()>0){
			
			int i=0;
			List<T> listaAppoggio = null;
			
			for(T it : listoneSenzaNulli){
				if(i==0){
					listaAppoggio = new ArrayList<T>();
				}
				listaAppoggio.add(it);
				i++;
				
				if(i==dimensione){
					listaAppoggio = getElementiNonNulli(listaAppoggio);
					listaDiListe.add(listaAppoggio);
					listaAppoggio = null;
					i = 0;
				}
			}
			
			//gestione del "resto" ultima lista:
			listaAppoggio = getElementiNonNulli(listaAppoggio);
			if(listaAppoggio!=null && listaAppoggio.size()>0){
				listaDiListe.add(listaAppoggio);
			}
			
		}
		
		listaDiListe = getElementiNonNulli(listaDiListe);
		
		return listaDiListe;
	}
	
	private final static String formattaConCaratteriInTesta(String stringa, int lunghezza, char carattere){
		if(isEmpty(stringa)){
			String vuota = "";
			for(int i=0;i<lunghezza;i++){
				vuota = vuota + carattere;
			}
			return vuota;
		}
		String trimmata = stringa.trim();
		if(trimmata.length()==lunghezza) return trimmata;
		if(trimmata.length()>lunghezza){
			trimmata = trimmata.substring(0, lunghezza);
			return trimmata;
		} else {
			String conCaratteri = "";
			int differenza = lunghezza - trimmata.length();
			for(int i=0;i<differenza;i++){
				conCaratteri = conCaratteri + carattere;
			}
			conCaratteri = conCaratteri + trimmata;
			return conCaratteri;
		}
	}
	
	public final static String formattaConZeriInTesta(String stringa, int lunghezza){
		return formattaConCaratteriInTesta(stringa, lunghezza, '0');
	}
	
	public final static String formatCodIstaForQuery(String code){
		return formattaConZeriInTesta(code, 6);
	}
	
	public final static <V extends Object, K extends Object> ArrayList<V> hashMapToArrayList(HashMap<K, V> map){
		ArrayList<V> list = new ArrayList<V>();
		if(map!=null){
			Iterator<Entry<K, V>> it = map.entrySet().iterator();
			while (it.hasNext()) {
		        Entry<K, V> pairs = it.next();
		        list.add(pairs.getValue());
		    }
		}
	    return list;
	}
	
	public final static boolean valorizzatiAndDiversi(String s1, String s2){
		boolean valorizzatiAndDiversi = false;
		if(!isEmpty(s1) && !isEmpty(s2) && !sonoUguali(s1, s2)){
			valorizzatiAndDiversi = true;
		}
		return valorizzatiAndDiversi;
	}
	
	public final static int calcolaNumeroDiPagine(int totElementi, int dimensionePagine){
		if(totElementi <=0 || dimensionePagine<=0){
			//per evitare eventuali exception di divisione per zero, o per evitare calcoli privi di significato
			return 0;
		}
		int totPagine = totElementi / dimensionePagine;
		int resto =totElementi % dimensionePagine;
		if(resto>0){
			totPagine = totPagine + 1;
		}
		return totPagine;
	}
	
	public static List<String> listaIntegerToString(List<Integer> lista){
		List<String> listaString = new ArrayList<String>();
		if(lista!=null && lista.size()>0){
			for(Integer it: lista){
				if(it!=null){
					listaString.add(""+it.intValue());
				}
			}
		}
		return listaString;
	}
	
	public static List<Integer> listaStringToInteger(List<String> lista){
		List<Integer> listaInteger = new ArrayList<Integer>();
		if(lista!=null && lista.size()>0){
			for(String it: lista){
				if(it!=null && isNumeroIntero(it)){
					listaInteger.add(new Integer(it));
				}
			}
		}
		return listaInteger;
	}
	
	/**
	 * puo essere utile per controllare due campi 
	 * che devono essere valorizzati in mutua esclusione oppure entrambi
	 * @param s1
	 * @param s2
	 * @return
	 */
	public final static boolean entrambiValorizzati(String s1, String s2) {
		if(!isEmpty(s1) && !isEmpty(s2)){
			return true;
		}
		return false;
	}
	
	/**
	 * puo essere utile per controllare due campi che devono o non devono essere entrambi vuoti
	 * @param s1
	 * @param s2
	 * @return
	 */
	public final static boolean entrambiVuoti(String s1, String s2) {
		if(isEmpty(s1) && isEmpty(s2)){
			return true;
		}
		return false;
	}
	
	public final static List<String> getElementiDiBNonIndicatiInA(List<String> a, List<String> b){
		List<String> nonPresenti = new ArrayList<String>();
		if(isEmpty(a)){
			//se a e' vuoto tutti gli elementi di b non sono contenuti in a
			return b;
		}
		if(!StringUtilsFin.isEmpty(b)){
			for(String it: b){
				if(!StringUtilsFin.isEmpty(it) && !a.contains(it)){
					nonPresenti.add(it);
				}
			}
		}
		return nonPresenti;
	}
	
}
