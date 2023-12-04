/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pcc.datioperazione;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.tesoro.fatture.StrutturaDatiOperazioneTipo;

/**
 * Interfaccia per ottenere dati di una operazione PCC.
 * 
 * @author Domenico Lisi
 */
public abstract class PopolaStrutturaDatiOperazione {
	
	protected LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	protected RegistroComunicazioniPCC registrazione;
	
	protected static final ThreadLocal<DatatypeFactory> DATATYPE_FACTORY_HOLDER = new ThreadLocal<DatatypeFactory>() {
		@Override
		protected DatatypeFactory initialValue() {
			try {
				return DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
				throw new IllegalStateException("Impossibile ottenere un'istanza di DatatypeFactory.", e);
			}
		}
	};
	
	
	/**
	 * Instantiates a new popola struttura dati operazione.
	 *
	 * @param registrazione the registrazione
	 */
	public PopolaStrutturaDatiOperazione(RegistroComunicazioniPCC registrazione) {
		this.registrazione = registrazione;
	}
	
	/**
	 * Popola la struttura dati operazione per il tipo corrente.
	 *
	 * @return the struttura dati operazione tipo
	 */
	public abstract StrutturaDatiOperazioneTipo popolaStrutturaDatiOperazione();

}
