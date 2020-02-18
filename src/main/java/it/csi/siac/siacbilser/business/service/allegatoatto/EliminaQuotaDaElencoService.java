/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDaElencoResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaQuotaDaElencoService extends DisassociaQuotaBaseService<EliminaQuotaDaElenco,EliminaQuotaDaElencoResponse> {

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		elencoDocumentiAllegato = req.getElencoDocumentiAllegato();
		checkNotNull(elencoDocumentiAllegato, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco"));
		checkCondition(elencoDocumentiAllegato.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid elenco"), false);
		
		subdocumento = req.getSubdocumento();
		checkNotNull(subdocumento, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento"));
		checkCondition(subdocumento.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento"), false);
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"), false);
	}
	
	@Override
	@Transactional
	public EliminaQuotaDaElencoResponse executeService(EliminaQuotaDaElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/**
	 * L'operazione di eliminazione di una o pi&uacute; quote documento gi&agrave; associate all'atto si differenzia per le quote
	 * documento di tipo diverso da ALG - ALLEGATO ATTO:
	 * <ul>
	 *     <li>Annullamento della liquidazione abbinata se presente (utilizzare l'operazione annullaLiquidazione 
	 *     del servizio 'SPES005 Servizio Gestione Liquidazione') e in caso di esito positivo eliminazione del legame
	 *     con l'atto della quota eliminata. In caso di errore interrompere l'elaborazione con invio dei messaggi al chiamante.</li>
	 * </ul>
	 * Rispetto alle quote documento riferite invece ad un documento di tipo ALG - ALLEGATO ATTO:
	 * <ul>
	 *     <li>Annullamento della liquidazione abbinata se presente (come al punto precedente);</li>
	 *     <li>Eliminazione della quota documento se possibile (ad es. se non &eacute; l'unica quota del documento);</li>
	 *     <li>Aggiornamento importo documento relativo alla quota eliminata: Importo documento = &Sigma;importo quote per quel documento;</li>
	 *     <li>Annullamento del documento associato alla quota solo se la quota che si sta eliminando &eacute; l'unica quota associata al documento.</li>
	 * </ul>
	 */
	@Override
	protected void execute() {
		bilancio = caricaDettaglioBilancio(req.getBilancio().getUid());
		allegatoAtto = caricaAllegatoAtto();
		
		checkQuotaNonConvalidata();
		eliminaLegameElencoQuota();
	}

}
