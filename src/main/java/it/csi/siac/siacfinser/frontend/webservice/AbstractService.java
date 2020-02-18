/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.frontend.webservice;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

public class AbstractService {

	protected transient LogUtil log = new LogUtil(this.getClass());

	@Autowired
	protected ApplicationContext appCtx;
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	 /**
	  * Helper class per il metodo execute
	  * @author luca.romanello
	  * 
	  * @see AbstractService.execute
	  *
	  * @param <RESP>
	  * @param <REQ>
	  */
	 protected abstract class ServiceExecutor<RESP extends ServiceResponse, REQ extends ServiceRequest> {
	  private String serviceName;
	  private boolean toUpper;
	  
	  public ServiceExecutor(String serviceName) {
	   this(serviceName, false);
	  }
	  
	  public ServiceExecutor(String serviceName, boolean toUpper) {
	   this.serviceName = serviceName;
	   this.toUpper = toUpper;
	  }
	  
	  /**
	   * Metodo per gestire la logica comune a tutti i servizi, prima e dopo l'esecuzione degli stessi, per esempio:
	   * log di inizio e fine, tempistiche di risposta, ecc. 
	   * @param request
	   * @return response
	   */
	  public RESP execute(REQ request) {
	   long t1 = System.currentTimeMillis();
	   boolean esito = true;
	   RESP resp = null;
	   try {
	    debug(serviceName, "START SERVICE");
	    
	    // elevo a maiuscolo tutte le stringhe
	    toUpper(request);
	    
	    // richiamo il service
	    resp = executeService(request);
	   } catch (RuntimeException rte) {
		    esito = false;
		    error(serviceName, rte);
		    if(resp!=null){
		    	//se resp diverso da null
		    	resp.setEsito(Esito.FALLIMENTO);
		    }
		    throw rte;
	   } finally {
		   debug(serviceName, "END SERVICE -", System.currentTimeMillis() - t1, "ms. Esito:", esito);
	   }
	   return resp;
	  }
	  
	  /**
	   * metodo che via introspector cicla in tutti gli oggetti fino alle foglie e porta
	   * a maiuscolo tutte le property di tipo stringa 
	   * @param obj
	   */
	  private void toUpper(Object obj){
	   if (toUpper && obj != null) {
		try{   
		    BeanInfo info = Introspector.getBeanInfo(obj.getClass());
		    PropertyDescriptor[] pds = info.getPropertyDescriptors();
		    for (PropertyDescriptor pd : pds) {
		     Class<?> propertyType = pd.getPropertyType();
		     if (propertyType.equals(String.class)) {
		    	 
		      if (pd.getReadMethod().getParameterTypes().length == 0) {
		     
		         try {
		        String val = (String)pd.getReadMethod().invoke(obj);
		       
		       if (val != null) {
		        String upperVal = val.toUpperCase();
		        if (!val.equals(upperVal)) {
		        	
					pd.getWriteMethod().invoke(obj, upperVal);
		         
		        }
		       }
				} catch (Exception e) {
					debug("warningtoUpper", "probabile errore di conversione della stringa senza alcun effetto");
//					warn("toUpper", e.getMessage(),);
				}
		      }
		     } else if (Collection.class.isAssignableFrom(propertyType)) {
		    	 
		      	 
		      try {
				@SuppressWarnings("rawtypes")
				  Collection coll = (Collection)pd.getReadMethod().invoke(obj);
				  if(coll!=null){
					  for (Object o : coll) {
					   toUpper( o );
					  }
				  }
				} catch (Exception e) {
//					warn("toUpper", e);
					debug("warningtoUpper", "probabile errore di conversione della Collection senza alcun effetto");
				}
		      
		     } else if (propertyType.isArray()) {
		    	
		    	 
		      try {
				Object[] objs = (Object[])pd.getReadMethod().invoke(obj);
				if(objs!=null){
				  for (Object o : objs) {
				   toUpper( o );
				  }
				}  
			  } catch (Exception e) {
				  	debug("warningtoUpper", "probabile errore di conversione di Object[] senza alcun effetto");
//					warn("toUpper", e);
			  }
		      
		     } else if (Entita.class.isAssignableFrom(propertyType)) {
		      try {
		    	  toUpper(pd.getReadMethod().invoke(obj));
				} catch (Exception e) {
//					warn("toUpper", e);
					debug("warningtoUpper", "probabile errore di conversione di Entita senza alcun effetto");
				} 
		      
		     } 
		     
		    }
		    
		}catch(IntrospectionException ie){
			warn("toUpper", ie);
		}
	    
	    
	   }
	  }
	  
	  abstract RESP executeService(REQ request);
	 }
	
	protected void error(String methodName, Throwable exc, Object... parameters) {
		log.error(methodName, getLogMessage(parameters), exc);
	}
	
	protected void debug(String methodName, Object... parameters) {
		if (log.isDebugEnabled())
			log.debug(methodName, getLogMessage(parameters));
	}
	
	protected void info(String methodName, Object... parameters) {
		if (log.isInfoEnabled())
			log.info(methodName, getLogMessage(parameters));
	}
	
	protected void warn(String methodName, Throwable exc, Object... parameters) {
		log.warn(methodName, getLogMessage(parameters), exc);
	}
	
	private String getLogMessage(Object... parameters) {
		if (parameters == null || parameters.length == 0) return "";
		StringBuilder logMessage = new StringBuilder();
		for (int i=0; i<parameters.length; i++) {
			logMessage.append(parameters[i]).append(i == parameters.length - 1 ? "" : " ");
		}
		return logMessage.toString();
	}

}
