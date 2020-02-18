/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.CodificheFactory;
import it.csi.siac.siacbilser.model.TipoCodifica;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Ente;

/**
 * The Class CodificaDad.
 * 
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional(propagation=Propagation.SUPPORTS)
public class CodificaDad extends BaseDadImpl {

	
	@Autowired
	private CodificheFactory cf; 

	private Ente ente;
	
	/**
	 * Sets the ente.
	 *
	 * @param ente the new ente
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
		
	}


	/**
	 * Ricerca tutte le codifiche di un tipo associate all'ente.
	 *
	 * @param <C> the generic type
	 * @param codificaClass the codifica class
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public <C extends Codifica> List<C> ricercaCodifiche(TipoCodifica tc) {
		return (List<C>) cf.ricercaCodifiche(tc.getTipoCodifica(), tc.getNomeCodifica(), ente.getUid());
	}
	
	/**
	 * Ricerca tutte le codifiche di un tipo associate all'ente.
	 *
	 * @param <C> the generic type
	 * @param codificaClass the codifica class
	 * @return the list
	 */
	public <C extends Codifica> List<C> ricercaCodifiche(Class<C> codificaClass) {
		return cf.ricercaCodifiche(codificaClass, ente.getUid());
	}
	
	/**
	 * Ricerca tutte le codifiche di un tipo associate all'ente.
	 *
	 * @param <C> the generic type
	 * @param codificaClass the codifica class
	 * @return the list
	 */
	public <C extends Codifica> List<C> ricercaCodifiche(String codificaName) {
		return cf.ricercaCodifiche(codificaName, ente.getUid());
	}
	
	/**
	 * Ricerca tutte le codifiche di un tipo associate all'ente.
	 *
	 * @param <C> the generic type
	 * @param codificaClass the codifica class
	 * @return the list
	 */
	public <C extends Codifica> List<C> ricercaCodifiche(Class<C> codificaClass, String codificaName) {
		return cf.ricercaCodifiche(codificaClass, codificaName, ente.getUid());
	}
	
	
	/**
	 * Ricerca la codifica ded tipo e codice passato come parametro associata all'ente. 
	 *
	 * @param <C> the generic type
	 * @param codificaClass the codifica class
	 * @param codice the codice
	 * @return the c
	 */
	public <C extends Codifica> C ricercaCodifica(Class<C> codificaClass, String codice) {
		return cf.ricercaCodifica(codificaClass, codice, ente.getUid());
	}
	
	/**
	 * Ricerca la codifica del tipo e codice passato come parametro associata all'ente. 
	 *
	 * @param <C> the generic type
	 * @param codificaClass the codifica class
	 * @param codificaName the codifica name
	 * @param codice the codice
	 * @return the c
	 */
	public <C extends Codifica> C ricercaCodifica(Class<C> codificaClass, String codificaName, String codice) {
		return cf.ricercaCodifica(codificaClass, codificaName, codice, ente.getUid());
	}
	
	
	/**
	 * Ricerca la codifica del tipo e codice passato come parametro associata all'ente. 
	 *
	 * @param <C> the generic type
	 * @param tipoCodifica the tipo codifica
	 * @param codice the codice
	 * @return the c
	 */
	@SuppressWarnings("unchecked")
	public <C extends Codifica> C ricercaCodifica(TipoCodifica tipoCodifica, String codice) {
		return (C) cf.ricercaCodifica(tipoCodifica.getTipoCodifica(), tipoCodifica.getNomeCodifica(), codice, ente.getUid());
	}
	
	/**
	 * Ricerca la codifica del tipo e codice passato come parametro associata all'ente. 
	 *
	 * @param <C> the generic type
	 * @param tipoCodifica the tipo codifica
	 * @param codice the codice
	 * @return the c
	 */
	public <C extends Codifica> C ricercaCodifica(Class<C> codificaClass, Integer id) {
		return (C) cf.ricercaCodifica(codificaClass, id);
	}

	
	/**
	 * Popola l'uid e la descrizione della codifica passata come parametro.
	 *
	 * @param <C> the generic type
	 * @param codifica Codifica con valorizzato il getCodice().
	 * @return la codifica valorizzata.
	 */
	@SuppressWarnings("unchecked")
	public <C extends Codifica> C ricercaCodifica(C codifica) {
		if(codifica==null) {
			throw new IllegalArgumentException("Codifica non valorizzata.");
		}
		return (C) cf.ricercaCodifica(codifica.getClass(), codifica.getCodice(), ente.getUid());
	}
	
	

}
