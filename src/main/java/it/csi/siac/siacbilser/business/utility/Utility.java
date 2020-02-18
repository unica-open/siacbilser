/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * @author Domenico
 */
package it.csi.siac.siacbilser.business.utility;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;

import org.apache.commons.lang.WordUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import it.csi.siac.siacbilser.integration.utility.threadlocal.AnteprimaAmmortamentoAnnuoCespiteThreadLocal;
import it.csi.siac.siacbilser.integration.utility.threadlocal.BilancioThreadLocal;
import it.csi.siac.siacbilser.integration.utility.threadlocal.CapitoloThreadLocal;
import it.csi.siac.siacbilser.integration.utility.threadlocal.ModelDetailThreadLocal;
import it.csi.siac.siacbilser.integration.utility.threadlocal.MovimentoDettaglioThreadLocal;
import it.csi.siac.siacbilser.model.ImportoPersistente;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommon.util.threadlocal.ThreadLocalUtil;
import it.csi.siac.siaccorser.model.Entita;

/**
 * The Class Utility.
 */
public class Utility {
	
	/** The LOG. */
	private static final LogUtil LOG = new LogUtil(Utility.class);
	/** Thread local per le date */
	private static final ThreadLocal<DateFormat> dateFormatTL = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
		}
	};
	/** Thread local per i model detail */
	public static final ModelDetailThreadLocal MDTL = (ModelDetailThreadLocal) ThreadLocalUtil.registerThreadLocal(ModelDetailThreadLocal.class);
	/** Thread local per il bilancio */
	public static final BilancioThreadLocal BTL = (BilancioThreadLocal) ThreadLocalUtil.registerThreadLocal(BilancioThreadLocal.class);
	/** Thread local per il capitolo */
	public static final CapitoloThreadLocal CTL = (CapitoloThreadLocal) ThreadLocalUtil.registerThreadLocal(CapitoloThreadLocal.class);
	/** METTI UN COMMENTO SENSATO!!! */
	public static final AnteprimaAmmortamentoAnnuoCespiteThreadLocal AAACTL = (AnteprimaAmmortamentoAnnuoCespiteThreadLocal) ThreadLocalUtil.registerThreadLocal(AnteprimaAmmortamentoAnnuoCespiteThreadLocal.class);
	/**valutare se necessario */
	public static final MovimentoDettaglioThreadLocal MDETTL = (MovimentoDettaglioThreadLocal) ThreadLocalUtil.registerThreadLocal(MovimentoDettaglioThreadLocal.class);
	
	/** Prevent instantiation */
	private Utility() {
		// Prevent instantiation
	}

	/**
	 * Logga un oggetto complesso con annotazione XmlType.
	 *
	 * @author Domenico Lisi
	 * @param obj oggetto con annotazione XmlType da convertire in xml per il logging
	 * @param msg nome del parametro da loggare (per il logging)
	 */
	public static void logXmlTypeObject(Object obj, String msg) {
		String methodName = "logXmlTypeObject";
		if (LOG.isDebugEnabled()) {			
			String result;
			try {
				result = toXml(obj);
			} catch (PropertyException e) {
				result = e.getMessage();
				LOG.warn("Impossibile loggare "+msg,e);
			} catch (JAXBException e) {
				result = e.getMessage();
				LOG.warn("Impossibile loggare "+msg,e);
			}
			
			LOG.debug(methodName, msg +": "+result);
		}
	}

	/**
	 * Trasforma un oggetto con annotazione XmlType in una stringa xml.
	 *
	 * @author Domenico Lisi
	 * @param obj the obj
	 * @return the string
	 * @throws JAXBException the JAXB exception
	 */
	public static String toXml(Object obj) throws JAXBException {
		return JAXBUtility.marshall(obj);

	}
	
	
	/**
	 * Gets the field value.
	 *
	 * @param <T> the generic type
	 * @param obj the obj
	 * @param fieldName the field name
	 * @return the field value
	 */
	public static<T> T getFieldValue(Object obj, String fieldName) {	
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(obj);
		return getFieldValue(bw, fieldName);
	}
	
	/**
	 * Gets the field value.
	 *
	 * @param <T> the generic type
	 * @param bw the bw
	 * @param fieldName the field name
	 * @return the field value
	 */
	@SuppressWarnings("unchecked")
	public static<T> T getFieldValue(BeanWrapper bw, String fieldName) {		
		return (T) bw.getPropertyValue(fieldName);
	}
	
	/**
	 * Convenience method for access ImportoPerisistente annotation.
	 *
	 * @param obj the obj
	 * @return the field name value map by annotation importo persistente
	 */
	public static Map<String,BigDecimal> getFieldNameValueMapByAnnotationImportoPersistente(Object obj) {
		return getFieldNameValueMapByAnnotation(obj, BigDecimal.class, ImportoPersistente.class);
		
	}
	
	/**
	 * Ottiene una mappa chiave valore di tutti i fields della classe annotati con l'annotation passata come parametro.
	 *
	 * @author Domenico Lisi
	 * @param <T> the generic type
	 * @param <A> the generic type
	 * @param obj l'oggetto da cui estrarre la mappa
	 * @param mapValueType il tipo del field annotato.
	 * @param annotation annotazione da cercare
	 * @return the field name value map by annotation
	 */
	public static<T,A extends Annotation> Map<String,T> getFieldNameValueMapByAnnotation(Object obj,  Class<T> mapValueType, final Class<A> annotation) {

		final Map<String,T> result = new HashMap<String,T>();
		final BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(obj);
		
		ReflectionUtils.doWithFields(obj.getClass(), new FieldCallback() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void doWith(Field f) throws IllegalAccessException {
				if(f.getAnnotation(annotation)!=null){
					result.put(f.getName(), (T) bw.getPropertyValue(f.getName()));
				}
				
			}
		});
		
		return result;
		
	}
	
	
	/**
	 * Ricerca i field dell'oggetto che hanno per tipo la classe passata come parametro 
	 * e restituisce una mappa di tutti i field contenente come chiave il nome del parametro e come valore il valore del parametro.
	 *
	 * @author Domenico Lisi
	 * @param <T> the generic type
	 * @param obj the obj
	 * @param typeToSearch the type to search
	 * @return the field name value map by type
	 */
	public static<T> Map<String,T> getFieldNameValueMapByType(Object obj, Class<T> typeToSearch) {		
		return getFieldNameValueMapByType(PropertyAccessorFactory.forBeanPropertyAccess(obj), typeToSearch);
	}

	/**
	 * Ricerca i field dell'oggetto che hanno per tipo la classe passata come parametro 
	 * e restituisce una mappa di tutti i field contenente come chiave il nome del parametro e come valore il valore del parametro.
	 *
	 * @author Domenico Lisi
	 * @param <T> the generic type
	 * @param bwic the bwic
	 * @param typeToSearch the type to search
	 * @return the field name value map by type
	 */
	public static<T> Map<String,T> getFieldNameValueMapByType(BeanWrapper bwic, Class<T> typeToSearch) {
		Map<String,T> result = new HashMap<String,T>();		
		
		PropertyDescriptor[] propertyDescriptors = bwic.getPropertyDescriptors();
		
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Class<?> propertyType = propertyDescriptor.getPropertyType();
			if(propertyType.equals(typeToSearch)){
				String fieldName = propertyDescriptor.getName();
				@SuppressWarnings("unchecked")
				T fieldValue = (T)bwic.getPropertyValue(fieldName);
				result.put(fieldName,fieldValue);
			}
		}
		
		return result;
	}
	
	
	/**
	 * Restituisce il codice jpql per la ricerca case-insensitive e accent-insensitive di un campo.
	 *
	 * @param jpqlParam the jpql param
	 * @return the string
	 */
	public static String toJpqlSearchParam(String jpqlParam) {		
		return " UPPER(TRANSLATE("+jpqlParam+",'ÀÁáàÉÈéèÍíÓóÒòÚú','AAaaEEeeIiOoOoUu')) ";		
	}
	
	/**
	 * Restituisce il codice jpql per il confronto case-insensitive e accent-insensitive tra due campi.
	 *
	 * @param jpqlParamA the jpql param a
	 * @param jpqlParamB the jpql param b
	 * @return the string
	 */
	public static String toJpqlSearchEqual(String jpqlParamA, String jpqlParamB) {
		return toJpqlSearchParam(jpqlParamA) + " = " + toJpqlSearchParam(jpqlParamB);
	}
	
	/**
	 * Restituisce il codice jpql per il confronto case-insensitive e accent-insensitive tra due campi.
	 *
	 * @param jpqlParamA the jpql param a
	 * @param jpqlParamB the jpql param b
	 * @return the string
	 */
	public static String toJpqlSearchLike(String jpqlParamA, String jpqlParamB) {
		return toJpqlSearchParam(jpqlParamA) + " LIKE " + toJpqlSearchParam(jpqlParamB);
	}
	
	/**
	 * Restituisce il codice jpql per il confronto case-insensitive e accent-insensitive tra due campi, in cui il secondo &eacute; circondato da percentuali.
	 *
	 * @param jpqlParamA the jpql param a
	 * @param jpqlParamB the jpql param b
	 * @return the string
	 */
	public static String toJpqlSearchLikePercent(String jpqlParamA, String jpqlParamB) {
		return toJpqlSearchParam(jpqlParamA) + " LIKE CONCAT('%', " + toJpqlSearchParam(jpqlParamB) + ", '%')";
	}
	
	/**
	 * Restituisce il codice jpql per la ricerca per data uguale al parametro.
	 *
	 * @param jpqlParamA the jpql param a
	 * @param jpqlParamB the jpql param b
	 * @return the string
	 */
	public static String toJpqlDateParamEquals(String jpqlParamA, String jpqlParamB) {
		return toJpqlDateParamOperator(jpqlParamA, jpqlParamB, "=");
	}
	
	/**
	 * Restituisce il codice jpql per la ricerca per data maggiore o uguale al parametro.
	 *
	 * @param jpqlParamA the jpql param a
	 * @param jpqlParamB the jpql param b
	 * @return the string
	 */
	public static String toJpqlDateParamGreaterOrEquals(String jpqlParamA, String jpqlParamB) {
		return toJpqlDateParamOperator(jpqlParamA, jpqlParamB, ">=");
	}
	
	/**
	 * Restituisce il codice jpql per la ricerca per data minore o uguale al parametro.
	 *
	 * @param jpqlParamA the jpql param a
	 * @param jpqlParamB the jpql param b
	 * @return the string
	 */
	public static String toJpqlDateParamLesserOrEquals(String jpqlParamA, String jpqlParamB) {
		return toJpqlDateParamOperator(jpqlParamA, jpqlParamB, "<=");
	}
	
	/**
	 * Restituisce il codice jpql per la ricerca per data operatorialmente valida rispetto al parametro.
	 *
	 * @param jpqlParamA the jpql param a
	 * @param jpqlParamB the jpql param b
	 * @param operator   the operator
	 * 
	 * @return the string
	 */
	public static String toJpqlDateParamOperator(String jpqlParamA, String jpqlParamB, String operator) {
		return "DATE_TRUNC('day', CAST(" + jpqlParamA + " AS date)) " + operator + " DATE_TRUNC('day', CAST(" + jpqlParamB + " AS date)) ";
	}
	
	/**
	 * Effettua un cast del parametro al tipo fornito. Importante &eacute; ricordare che il tipo <strong>deve</strong> essere <code>lowercase</code> durante la traduzione della query.
	 * <br/>
	 * Il metodo affronta il problema fornendo la sintassi corretta per il cast e impostando il tipo in <code>lowercase</code>.
	 * 
	 * @param jpqlParam il parametro da castare
	 * @param type      il tipo di cast
	 * 
	 * @return the string
	 */
	public static String castToType(String jpqlParam, String type) {
		return "CAST(" + jpqlParam + " AS " + type.toLowerCase(Locale.ITALIAN) + ")";
	}

	/**
	 * Marshall.
	 *
	 * @param obj the obj
	 * @return the string
	 */
	public static String marshall(Object obj){
		return JAXBUtility.marshall(obj);
	}
	
	/**
	 * Unmarshall.
	 *
	 * @param <T> the generic type
	 * @param xml the xml
	 * @param clazz the clazz
	 * @return the t
	 */
	public static <T> T unmarshall(String xml, Class<T> clazz){
		return JAXBUtility.unmarshall(xml, clazz);
	}
	
	/**
	 * Calcola totale pagine.
	 *
	 * @param numeroTotaleElementi the numero totale elementi
	 * @param elementiPerPagina the elementi per pagina
	 * @return the int
	 */
	public static int calcolaTotalePagine(int numeroTotaleElementi, int elementiPerPagina) {		
		int numeroPagine = (numeroTotaleElementi/elementiPerPagina) + (numeroTotaleElementi % elementiPerPagina);
		return numeroPagine;
	}
	
	/**
	 * List to comma separated lower case string.
	 *
	 * @param l the l
	 * @return the string
	 */
	public static String listToCommaSeparatedLowerCaseString(List<?> l) {
		String s = l.toString();
		s = s.replaceAll("\\[|\\]", "");
		s = s.replaceAll("_", " ");
		return s.toLowerCase(Locale.ITALIAN);
	}
	
	/**
	 * List to comma separated fully capitalized string.
	 *
	 * @param l the l
	 * @return the string
	 */
	public static String listToCommaSeparatedFullyCapitalizedString(List<?> l) {
		return WordUtils.capitalizeFully(listToCommaSeparatedLowerCaseString(l));
	}
	
	
	/**
	 * Ottiene il primo giorno dell anno passato come parametro.
	 *
	 * @param anno the anno
	 * @return the date
	 */
	public static Date primoGiornoDellAnno(int anno) {
		return new GregorianCalendar(anno, 0, 1, 0, 0, 0).getTime();
	}
	
	/**
	 * Primo giorno dell anno.
	 *
	 * @param date the date
	 * @return the date
	 */
	public static Date primoGiornoDellAnno(Date date) {
		int anno = getAnno(date);
		return primoGiornoDellAnno(anno);
		
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
	 * Ottiene l'ultimo giorno dell anno passato come parametro.
	 *
	 * @param anno the anno
	 * @return the date
	 */
	public static Date ultimoGiornoDellAnno(int anno) {
		return new GregorianCalendar(anno, 11, 31, 0, 0, 0).getTime();
	}
	

	/**
	 * Utlimo giorno dell anno.
	 *
	 * @param date the date
	 * @return the date
	 */
	public static Date ultimoGiornoDellAnno(Date date) {
		int anno = getAnno(date);
		return ultimoGiornoDellAnno(anno);
		
	}
	
	/**
	 * Utlimo giorno dell anno precedente.
	 *
	 * @param date the date
	 * @return the date
	 */
	public static Date ultimoGiornoDellAnnoPrecedente(Date date) {
		int anno = getAnno(date)-1;
		return ultimoGiornoDellAnno(anno);
	}
	
	/**
	 * Data piu recente.
	 *
	 * @param dates the dates
	 * @return the date
	 */
	public static Date dataPiuRecente(Date... dates){
		
		Date result = null;
		if(dates == null) {
			return result;
		}
		for(Date date : dates) {
			if(date == null){
				continue;
			}
			
			if(result == null){
				result = date;
				continue;
			}
			
			if(date.compareTo(result)>0){
				result = date;
			}
		}
		
		return result;
	}
	
	/**
	 * Gets the data piu prossima.
	 *
	 * @param dates the dates
	 * @return the data piu prossima
	 */
	public static Date getDataPiuProssima(List<Date> dates) {
		Date now = new Date();
		Date dataScadenzaPiuProssimaAScadenza = null;
		Date dataScadenzaPiuVecchia = null;
		if(dates == null || dates.size() == 0) {
			return null;
		}
		
		for (Date dsf : dates) {
			if(dsf == null) {
				continue;
			}
			
			if(now.before(dsf) && (dataScadenzaPiuProssimaAScadenza == null || dataScadenzaPiuProssimaAScadenza.after(dsf))) {
				// Se la data e' successiva ad ora ed e' prima della data a scadenza piu' prossima gia' registrata...
				dataScadenzaPiuProssimaAScadenza = dsf;
				continue;
			} 
			if(dataScadenzaPiuVecchia == null || dsf.before(dataScadenzaPiuVecchia)) {
				// Se la data di scadenza e' dopo la piu' vecchia gia' registrata
				dataScadenzaPiuVecchia = dsf;
			}
		}
		return dataScadenzaPiuProssimaAScadenza != null ? dataScadenzaPiuProssimaAScadenza : dataScadenzaPiuVecchia;
	}
	
	/**
	 * To camel case.
	 *
	 * @param str the str
	 * @return the string
	 */
	public static String toCamelCase(String str){
		String delim = " ";
		
		str = str.replaceAll("[_ \\,\\-]", delim);
		str = WordUtils.capitalizeFully(str, delim.toCharArray());
		str = str.replaceAll("["+delim+"]", "");
		
		return str;
	}
	
	/**
	 * Ottiene il primo oggetto non nullo.
	 * 
	 * @param objs gli oggetti da controllare
	 * 
	 * @return il primo oggetto non nullo
	 */
	public static <T> T firstNotNull(T... objs) {
		for(T t : objs) {
			if(t != null) {
				return t;
			}
		}
		return null;
	}
	
	
	public static Date truncateToStartOfDay(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	
	
	/**
	 * Ottiene il nome di default di un bean di Spring a partire dalla sua classe.
	 *
	 * @param c the c
	 * @return the string
	 */
	public static String toDefaultBeanName(Class<?> c){
		return firstLetterToLowerCase(c.getSimpleName());
	}
	
	/**
	 * Ottiene il nome di default di un bean di Spring a partire dalla sua classe.
	 *
	 * @param c the c
	 * @return the string
	 */
	public static <T> T getBeanViaDefaultName(ApplicationContext appCtx, Class<T> c){
		return appCtx.getBean(toDefaultBeanName(c), c);
	}
	
	/**
	 * Restituisce la stringa con la prima lettera minuscola.
	 *
	 * @param s la stringa
	 * @return la stringa modificata.
	 */
	public static String firstLetterToLowerCase(String s){
		if(s == null || s.length()==0){
			return s;
		}		
		return s.substring(0,1).toLowerCase() + s.substring(1);
		
		//### Alternativa con le Regex
//		Pattern pattern = Pattern.compile("([a-zA-Z])(.*)");
//		Matcher matcher = pattern.matcher(s);
//		matcher.find();
//		return matcher.group(1).toLowerCase() + matcher.group(2);
	}
	
	public static String formatDate(Date date) {
		if(date == null) {
			return "";
		}
		return dateFormatTL.get().format(date);
	}
	
	public static BigDecimal roundCurrency(BigDecimal currency){
		return currency.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	public static String formatCurrency(BigDecimal currency){
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.ITALY); 
		return numberFormat.format(currency);
	}
	
	public static String formatCurrencyAsString(BigDecimal value) {
		if(value == null) {
			return "";
		}
		
		DecimalFormat df = (DecimalFormat)NumberFormat.getInstance(Locale.ITALY);
		df.setParseBigDecimal(true);
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		return df.format(value);
	}
	
	/**
	 * Instanziazione del tipo generico
	 * @param actualClass la classe che risolve il tipo generico
	 * @param genericClass la classe con il tipo generico
	 * @param genericIndex l'indice del tipo generico nella classe generica
	 * @return un'istanza del tipo generico
	 * @throws IllegalArgumentException nel caso in cui l'instanziazione non sia possibile
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> findGenericType(Class<?> actualClass, Class<?> genericClass, int genericIndex) {
		String methodName = "findGenericType";
		
		try {
			@SuppressWarnings("rawtypes")
			Class[] genericTypeArguments = GenericTypeResolver.resolveTypeArguments(actualClass, genericClass);
			if(genericTypeArguments == null) {
				throw new IllegalStateException("Nessun tipo generico risolto");
			}
			if(genericTypeArguments.length < genericIndex) {
				throw new IllegalArgumentException("Impossibile trovare il tipo generico: l'array di tipi contiene " + genericTypeArguments.length
						+ " elementi, ma e' richiesto l'elemento di indice " + genericIndex);
			}
			return genericTypeArguments[genericIndex];
		} catch(IllegalArgumentException iae) {
			// Filter through
			throw iae;
		} catch(IllegalStateException iae) {
			// Filter through
			throw iae;
		} catch (Exception t) {
			String msg = "Errore instanziamento automatico result. ";
			LOG.error(methodName, msg, t);
			throw new IllegalArgumentException(msg, t);
		}
	}
	
	/**
	 * Instanziazione del tipo generico
	 * @param actualClass la classe che risolve il tipo generico
	 * @param genericClass la classe con il tipo generico
	 * @param genericIndex l'indice del tipo generico nella classe generica
	 * @return un'istanza del tipo generico
	 * @throws IllegalArgumentException nel caso in cui l'instanziazione non sia possibile
	 */
	public static <T> T instantiateGenericType(Class<?> actualClass, Class<?> genericClass, int genericIndex) {
		String methodName = "instantiateGenericType";
		
		try {
			Class<T> genericType = findGenericType(actualClass, genericClass, genericIndex);
			T res = genericType.newInstance();
			if(res == null) {
				throw new IllegalStateException("Instanziamento automatico del result fallito: result non creato correttamente");
			}
			return res;
		} catch (InstantiationException e) {
			String msg = "Errore instanziamento automatico result. "
					+ "Deve esistere un costruttore vuoto. Per esigenze più complesse "
					+ "sovrascrivere il metodo instantiateResult a livello dell'handler.";
			LOG.error(methodName, msg, e);
			throw new IllegalArgumentException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "Errore instanziamento automatico result. Il costruttore vuoto non è accessibile.";
			LOG.error(methodName, msg, e);
			throw new IllegalArgumentException(msg, e);
		} catch(IllegalArgumentException iae) {
			// Filter through
			throw iae;
		} catch(IllegalStateException iae) {
			// Filter through
			throw iae;
		} catch (Exception t) {
			String msg = "Errore instanziamento automatico result. ";
			LOG.error(methodName, msg, t);
			throw new IllegalArgumentException(msg, t);
		}
	}
	
	/**
	 * Projetta le entit&agrave; sugli uid
	 * @param entitas le entit&agrave; da projettare
	 * @return gli uid delle entit&agrave;
	 */
	public static <E extends Entita> List<Integer> projectToUidList(Collection<E> entitas) {
		List<Integer> res = new ArrayList<Integer>();
		if(entitas == null) {
			return res;
		}
		for(E e : entitas) {
			if(e != null && e.getUid() != 0) {
				res.add(e.getUid());
			}
		}
		return res;
	}
	
}
