/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import it.csi.siac.siacbilser.business.service.capitoloentratagestione.AnnullaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.AnnullaCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.AnnullaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.AnnullaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.BaseService;
import it.csi.siac.siaccorser.model.ServiceRequest;

/**
 * The Enum CapitoloServiceEnum.
 */
public enum CapitoloServiceEnum {
	
	/** The annulla capitolo entrata previsione. */
	ANNULLA_CAPITOLO_ENTRATA_PREVISIONE(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE, AnnullaCapitoloEntrataPrevisioneService.class, new String[] {"Capitolo","CapitoloEntrataPrev"}
																																, new String[] {"Test","TestA"}	
																																),
	
	/** The annulla capitolo uscita previsione. */
	ANNULLA_CAPITOLO_USCITA_PREVISIONE_(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, AnnullaCapitoloUscitaPrevisioneService.class, new String[] {"Capitolo","CapitoloUscitaPrev"}
																																, new String[] {"Test","TestA"}	
																																),
	
	/** The annulla capitolo entrata gestione. */
	ANNULLA_CAPITOLO_ENTRATA_GESTIONE(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE, AnnullaCapitoloEntrataGestioneService.class, new String[] {"Capitolo","CapitoloEntrataGest"}
																																, new String[] {"Test","TestA"}	
																																),
	
	/** The annulla capitolo uscita gestione. */
	ANNULLA_CAPITOLO_USCITA_GESTIONE(TipoCapitolo.CAPITOLO_USCITA_GESTIONE, AnnullaCapitoloUscitaGestioneService.class, new String[] {"Capitolo","CapitoloUscitaGest"}
																																, new String[] {"Test","TestA"}	
																																),
	;
	
	
	/** The tipo capitolo. */
	private TipoCapitolo tipoCapitolo;
	
	/** The service class. */
	private Class<?> serviceClass;
	
	/** The property map. */
	private Map<String,String> propertyMap;
	
	/** The log. */
	private transient LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/**
	 * Instantiates a new capitolo service enum.
	 *
	 * @param tipoCapitolo the tipo capitolo
	 * @param serviceClass the service class
	 * @param v the v
	 */
	private CapitoloServiceEnum( TipoCapitolo tipoCapitolo, Class<?> serviceClass, String[]... v) {
		this.tipoCapitolo = tipoCapitolo;
		this.serviceClass = serviceClass;
		
		propertyMap = new HashMap<String,String>();
		for(int i = 0; i<v.length; i++){
			propertyMap.put( (String)v[i][0], v[i][1]);
		}	
		
	}
	
	
	
	/**
	 * Gets the tipo capitolo.
	 *
	 * @return the tipo capitolo
	 */
	public TipoCapitolo getTipoCapitolo() {
		return tipoCapitolo;
	}
	
	/**
	 * Gets the property name.
	 *
	 * @param propertyName the property name
	 * @return the property name
	 */
	public String getPropertyName(String propertyName) {
		String result = propertyMap.get(propertyName);
		if(result==null){
			result = propertyName;
		}
		return result;
	}
	
	/**
	 * Gets the service class.
	 *
	 * @param <T> the generic type
	 * @return the service class
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseService<?, ?>> Class<T> getServiceClass() {
		return (Class<T>) serviceClass;
	}


	/**
	 * By operazione e tipo capitolo.
	 *
	 * @param operazione the operazione
	 * @param tipoCapitolo the tipo capitolo
	 * @return the capitolo service enum
	 */
	public static CapitoloServiceEnum byOperazioneETipoCapitolo(String operazione, TipoCapitolo tipoCapitolo){
		for(CapitoloServiceEnum e : CapitoloServiceEnum.values()){
			if(e.name().startsWith(operazione.toUpperCase(Locale.ITALIAN)) && tipoCapitolo.equals(e.getTipoCapitolo())){
				return e;
			}
		}
		
		throw new IllegalArgumentException("L'operazione "+operazione +" sul tipo capitolo "+ tipoCapitolo + " non ha un mapping corrispondente in CapitoloServiceEnum");
	}
	
	/**
	 * Gets the.
	 *
	 * @param obj the obj
	 * @param propertyName the property name
	 * @return the object
	 */
	public Object get(Object obj, String propertyName){		
		String pn = getPropertyName(propertyName);		
		return invokeMethod(obj, "get"+pn);
	}
	
	/**
	 * Sets the.
	 *
	 * @param obj the obj
	 * @param propertyName the property name
	 * @param args the args
	 */
	public void set(Object obj, String propertyName, Object... args){		
		String pn = getPropertyName(propertyName);		
		invokeMethod(obj, "get"+pn, args);
	}
	
	/**
	 * Invoke method.
	 *
	 * @param obj the obj
	 * @param methodName the method name
	 * @param args the args
	 * @return the object
	 */
	private Object invokeMethod(Object obj, String methodName, Object... args) {
		Method method = ReflectionUtils.findMethod(obj.getClass(), methodName);
		return ReflectionUtils.invokeMethod(method, obj, args);
	}
	
	
	
	/**
	 * Gets the service request class.
	 *
	 * @return the service request class
	 */
	public Class<?> getServiceRequestClass(){
		return Utility.findGenericType(getServiceClass(), BaseService.class, 0);
	}
	
	/**
	 * Gets the service response class.
	 *
	 * @return the service response class
	 */
	public Class<?> getServiceResponseClass(){
		return Utility.findGenericType(getServiceClass(), BaseService.class, 1);
	}
	
	/**
	 * Gets the service request new instance.
	 *
	 * @param <T> the generic type
	 * @return the service request new instance
	 */
	@SuppressWarnings("unchecked")
	public <T extends ServiceRequest> T getServiceRequestNewInstance() {
		final String methodName = "getServiceRequestNewInstance";
		
		try {						
			return (T) getServiceRequestClass().newInstance();
		} catch (InstantiationException e) {
			log.error(methodName, "",e);
			throw new IllegalArgumentException("Errore instanziamento automatico serviceRequest. "
					+ "Deve esistere un costruttore vuoto.", e);
		} catch (IllegalAccessException e) {
			log.error(methodName, "",e);
			throw new IllegalArgumentException("Errore instanziamento automatico serviceRequest. Il costruttore vuoto non è accessibile.", e);
		} catch (Exception e) {
			log.error(methodName, "",e);
			throw new IllegalArgumentException("Errore instanziamento automatico serviceRequest. ", e);
		} 
	}

}
