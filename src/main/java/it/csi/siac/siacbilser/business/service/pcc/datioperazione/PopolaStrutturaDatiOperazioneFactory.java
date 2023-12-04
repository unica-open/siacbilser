/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pcc.datioperazione;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.tesoro.fatture.TipoOperazioneTipo;

/**
 * Factory per creare PopolaStrutturaDatiOperazione in base al tipo di operazione.
 * 
 * @author Domenico Lisi
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class PopolaStrutturaDatiOperazioneFactory {
	
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	@Autowired
	private ApplicationContext appCtx;
	
	/**
	 * Ottiene una istanza di PopolaStrutturaDatiOperazione per il tipo passato come parametro.
	 *
	 * @param tipoOperazione the tipo operazione
	 * @param registroComunicazioniPCC the registro comunicazioni pcc
	 * @return the popola struttura dati operazione
	 */
	public PopolaStrutturaDatiOperazione create(TipoOperazioneTipo tipoOperazione, RegistroComunicazioniPCC registroComunicazioniPCC){
		final String methodName = "create";
		
		String beanName = "popolaStrutturaDatiOperazione"+tipoOperazione.name();
		log.debug(methodName, "Istanzio il bean con nome: "+beanName);
		
		try {
			return (PopolaStrutturaDatiOperazione) appCtx.getBean(beanName, registroComunicazioniPCC);
			
		} catch(NoSuchBeanDefinitionException nsbde) {
			throw new UnsupportedOperationException("Tipo operazione " + tipoOperazione +" non supportato.", nsbde);
		} catch(BeanDefinitionStoreException bdse) {
			throw new IllegalStateException("Il bean "+beanName+ "+ deve avere scope prototype.", bdse);
		} catch(BeansException be){
			throw new RuntimeException("Impossibile ottenere il bean "+beanName, be);
		}
			
	}

}
