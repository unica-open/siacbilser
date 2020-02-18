/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DisassociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DisassociaElencoResponse;
import it.csi.siac.siacfin2ser.model.Subdocumento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DisassociaElencoService extends DisassociaQuotaBaseService<DisassociaElenco, DisassociaElencoResponse> {

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getElencoDocumentiAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco"));
		checkCondition(req.getElencoDocumentiAllegato().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid elenco"), false);
		elencoDocumentiAllegato = req.getElencoDocumentiAllegato();
		checkNotNull(req.getElencoDocumentiAllegato().getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto elenco"));
		checkCondition(req.getElencoDocumentiAllegato().getAllegatoAtto().getUid() != 0,
			ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto elenco"), false);
		checkEntita(req.getBilancio(), "bilancio");
	}
	
	@Override
	@Transactional
	public DisassociaElencoResponse executeService(DisassociaElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		elencoDocumentiAllegato = elencoDocumentiAllegatoDad.findElencoDocumentiAllegatoLightById(elencoDocumentiAllegato.getUid());
		log.logXmlTypeObject(elencoDocumentiAllegato, "elenco");
		Date dataTrasmissione = elencoDocumentiAllegato.getDataTrasmissione();
		log.debug(methodName, "data trasmissione: " +  dataTrasmissione);
		// Controllo se siano gia' stati collegati
		Boolean giaAssociati = elencoDocumentiAllegatoDad.esisteAssociazioneElencoDocumentiAllegatoAllegatoAtto(req.getElencoDocumentiAllegato(),
			req.getElencoDocumentiAllegato().getAllegatoAtto());
		if(!Boolean.TRUE.equals(giaAssociati)) {
			log.debug(methodName, "Relazione non presente su database tra allegato " + req.getElencoDocumentiAllegato().getAllegatoAtto().getUid()
					+ " ed elenco " + req.getElencoDocumentiAllegato().getUid());
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("relazione elenco allegato atto", ""));
		}
		
		List<Subdocumento<?,?>> listaSubdoc = elencoDocumentiAllegatoDad.findSubdocByElencoDoc(req.getElencoDocumentiAllegato().getUid());
		for(Subdocumento<?,?> s: listaSubdoc){
			log.debug(methodName, "ciclo for - subdocumento con uid: " + s.getUid());
			subdocumento = s;
			bilancio = caricaDettaglioBilancio(req.getBilancio().getUid());
			allegatoAtto = caricaAllegatoAtto();
			
			checkQuotaNonConvalidata();
			eliminaLegameElencoQuota();
		}
		elencoDocumentiAllegatoDad.disassociaElencoDocumentiAllegatoAdAllegatoAtto(req.getElencoDocumentiAllegato(), req.getElencoDocumentiAllegato().getAllegatoAtto());
		if(dataTrasmissione == null){
			log.debug(methodName, "elimino l'elenco!!!");
			elencoDocumentiAllegatoDad.eliminaElencoDocumenti(req.getElencoDocumentiAllegato().getUid());
		}
		res.setElencoDocumentiAllegato(req.getElencoDocumentiAllegato());
	}
	

}
