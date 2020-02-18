/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.AccertamentoBilDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class InserisceElencoService extends InserisceElencoBaseService {
	
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	@Autowired
	private AccertamentoBilDad accertamentoBilDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.elencoDocumentiAllegato = req.getElencoDocumentiAllegato();
		
		checkEntita(req.getBilancio(), "bilancio", false);
		
		checkNotNull(elencoDocumentiAllegato, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti"));

		checkEntita(elencoDocumentiAllegato.getEnte(), "ente elenco documenti", false);
		checkNotNull(elencoDocumentiAllegato.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno elenco documenti"), false);
		
		//l'allegato atto e' facoltativo!
//		checkNotNull(elencoDocumentiAllegato.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
//		checkCondition(elencoDocumentiAllegato.getAllegatoAtto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"));
		
//		checkNotNull(elencoDocumentiAllegato.getStatoOperativoElencoDocumenti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo elenco documenti"));
		
		if (elencoDocumentiAllegato.getStatoOperativoElencoDocumenti() == null) {
			//Se non specificato lo stato operativo elenco di default è BOZZA
			elencoDocumentiAllegato.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
		}
		
		checkNotNull(elencoDocumentiAllegato.getSubdocumenti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumenti"));
		
		// SIAC-5001: permetto l'inserimento di un elenco senza subdoc
		if(!req.isAccettaElencoSenzaSubdocumenti()) {
			checkCondition(!elencoDocumentiAllegato.getSubdocumenti().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumenti"));
			
			for(Subdocumento<?, ?> subdoc : elencoDocumentiAllegato.getSubdocumenti()){
				checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento"));
				checkCondition(subdoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento"));
			}
		}
	}
	
	@Override
	@Transactional
	public InserisceElencoResponse executeService(InserisceElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		bilancio = caricaBilancio(req.getBilancio().getUid());
		
		checkMovimentiResidui();
		
		staccaNumeroElenco();
		
		if(elencoDocumentiAllegato.getAllegatoAtto() != null){
			AllegatoAtto allegato = caricaAllegatoAtto(elencoDocumentiAllegato.getAllegatoAtto().getUid());
			if (allegato != null){
				for(Subdocumento<?, ?> subdoc : elencoDocumentiAllegato.getSubdocumenti()) {
					allegatoAttoDad.collegaAllegatoSubdocumento(allegato, subdoc);
				}
				if(StatoOperativoAllegatoAtto.COMPLETATO.equals(allegato.getStatoOperativoAllegatoAtto())
						|| StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(allegato.getStatoOperativoAllegatoAtto())) {
					valutaInserimentoLiquidazioni(elencoDocumentiAllegato.getSubdocumentiSpesa(), allegato);
					elencoDocumentiAllegato.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.COMPLETATO);
				}
			}
		}
		
		elencoDocumentiAllegatoDad.inserisciElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		// SIAC-5300
		aggiornaStatoDocumenti();

		res.setElencoDocumentiAllegato(elencoDocumentiAllegato);
	}

	@Override
	protected Impegno caricaDatiImpegno(SubdocumentoSpesa ss) {
		Impegno impegnoOSubImpegno = impegnoBilDad.findSubImpegnoQuota(ss.getUid());
		if(impegnoOSubImpegno==null){
			impegnoOSubImpegno = impegnoBilDad.findImpegnoQuota(ss.getUid());
		}
		if(impegnoOSubImpegno==null){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO
					.getErrore("Impossibile trovare l'Impegno/SubImpegno associato alla quota: " + ss.getNumero()
							+ " [uid: " + ss.getUid() + "]"));
		}
		
		return impegnoOSubImpegno;
		
//		SubdocumentoSpesa subdoc = subdocumentoSpesaDad.findSubdocumentoSpesaById(ss.getUid(), SubdocumentoSpesaModelDetail.ImpegnoSubimpegno);
//		Impegno impegnoOSubImpegno = subdoc.getImpegnoOSubImpegno();
//		return impegnoOSubImpegno;
	}

	@Override
	protected Accertamento caricaDatiAccertamento(SubdocumentoEntrata se) {
		Accertamento accertamentoOSubAccertamento = accertamentoBilDad.findSubAccertamentoQuota(se.getUid());
		if(accertamentoOSubAccertamento==null){
			accertamentoOSubAccertamento = accertamentoBilDad.findAccertamentoQuota(se.getUid());
		}
		if(accertamentoOSubAccertamento==null){
			throw new BusinessException(ErroreBil.ERRORE_GENERICO
					.getErrore("Impossibile trovare l'Accertamento/SubAccertamento associato alla quota: " + se.getNumero()
							+ " [uid: " + se.getUid() + "]"));
		}
		
		return accertamentoOSubAccertamento;
		
//		SubdocumentoEntrata subdoc = subdocumentoEntrataDad.findSubdocumentoEntrataById(se.getUid(), SubdocumentoEntrataModelDetail.AccertamentoSubaccertamento);
//		Accertamento accertamentoOSubAccertamento = subdoc.getAccertamentoOSubAccertamento();
//		return accertamentoOSubAccertamento;
	}


	
	

}
