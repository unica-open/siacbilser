/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;

/**
 * The Class RicercaQuotaSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioQuotaSpesaService extends CheckedAccountBaseService<RicercaDettaglioQuotaSpesa, RicercaDettaglioQuotaSpesaResponse> {

	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	private SubdocumentoSpesa subdocumentoSpesa;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		subdocumentoSpesa = req.getSubdocumentoSpesa();
		checkNotNull(subdocumentoSpesa, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento spesa"));
		checkCondition(subdocumentoSpesa.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento spesa"));
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioQuotaSpesaResponse executeService(RicercaDettaglioQuotaSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		SubdocumentoSpesa subdoc = subdocumentoSpesaDad.findSubdocumentoSpesaById(subdocumentoSpesa.getUid(),
				// Model details
				SubdocumentoSpesaModelDetail.AttoAmm,
				SubdocumentoSpesaModelDetail.ImpegnoSubimpegnoConDisponibilita,
				SubdocumentoSpesaModelDetail.LiquidazioneMinimalStato,
				SubdocumentoSpesaModelDetail.Predocumento,
				SubdocumentoSpesaModelDetail.CommissioniDocumento,
				SubdocumentoSpesaModelDetail.Classif,
				SubdocumentoSpesaModelDetail.ModPag,
				SubdocumentoSpesaModelDetail.SedeSecondariaSoggetto,
				SubdocumentoSpesaModelDetail.SubdocumentoIva,
				SubdocumentoSpesaModelDetail.ElencoDocumenti,
				SubdocumentoSpesaModelDetail.ProvvisorioDiCassa,
				SubdocumentoSpesaModelDetail.Ordinativo,
				SubdocumentoSpesaModelDetail.RegistroComunicazioniPCC,
				SubdocumentoSpesaModelDetail.TipoIvaSplitReverse,
				SubdocumentoSpesaModelDetail.SiopeTipoDebito,
				SubdocumentoSpesaModelDetail.SiopeAssenzaMotivazione,
				SubdocumentoSpesaModelDetail.SiopeScadenzaMotivo,
				SubdocumentoSpesaModelDetail.SospensioneSubdocumento,
				SubdocumentoSpesaModelDetail.NoteTesoriere,
				SubdocumentoSpesaModelDetail.ContoTesoreria,
				//SIAC-8153
				SubdocumentoSpesaModelDetail.StrutturaCompetenteQuota
				);
		
		if(subdoc==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("subdocumento spesa", "id: "+subdocumentoSpesa.getUid()));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		
		res.setSubdocumentoSpesa(subdoc);
		
	}

}
