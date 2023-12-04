/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epay.epaywso.rendicontazione.FlussoRiconciliazioneType;
import it.csi.siac.siacbilser.integration.dad.PagoPADad;
import it.csi.siac.siacbilser.integration.entity.SiacTFilePagopa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDFilePagopaStatoEnum;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.util.misc.TimeoutValue;
import it.csi.siac.siaccorser.model.Ente;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PagoPAFlussoHelper {

	private static LogSrvUtil log = new LogSrvUtil(PagoPAFlussoHelper.class);
	
	@Autowired
	private PagoPADad pagoPADad;
	
	public void init(Ente ente, String loginOperazione) {
		pagoPADad.setEnte(ente);
		pagoPADad.setLoginOperazione(loginOperazione);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, timeout = TimeoutValue.INTERVAL_8_HOUR)
	public void elaboraFlusso(SiacTFilePagopa siacTFilePagopa) throws Exception {
		try {
			log.info("elaboraFlussiAcquisiti", String.format("flusso siacTFilePagopa.uid=%d", siacTFilePagopa.getUid()));

			pagoPADad.aggiornaStato(siacTFilePagopa, SiacDFilePagopaStatoEnum.IN_ACQUISIZIONE);

			FlussoRiconciliazioneType flussoRiconciliazione = 
					JAXBUtility.unmarshall(new String(siacTFilePagopa.getFilePagopa()), FlussoRiconciliazioneType.class);
			
			check(); // TODO

			pagoPADad.salvaFlusso(flussoRiconciliazione, siacTFilePagopa); 
			
		} catch (BusinessException be) {
			pagoPADad.aggiornaStato(siacTFilePagopa, SiacDFilePagopaStatoEnum.RIFIUTATO);
			
			throw be;
		} catch (Exception e) {
			log.error("", String.format("errore flusso siacTFilePagopa.uid=%d", siacTFilePagopa.getUid()), e);
			
			throw e;
		}
	}

	private void check() {
		 /** 
		 * Verifica che il numero di flussi di rendicontazione presente nei dettagli sia esattamente quello citato in testata al flusso;

		 * Verifica che l’importo totale del file citato in testata sia esattamente la somma dei flussi di rendicontazione presenti sui dettagli;
		 * 
		 * Verifica che l’importo riconciliato per ciascun flusso sia la somma dei singoli importi riconciliati presenti sui dettagli;
		 * 
		 * Identificativo File XML già presente in archivio (già trasmesso)
			 new BusinessException(ErroreCore.ENTITA_PRESENTE)
		 *
		 */
		
		
	}

}
