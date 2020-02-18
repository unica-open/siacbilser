/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;

/**
 * Inserimento di una quota di un Documento di Entrata.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceQuotaDocumentoEntrataService extends CrudDocumentoDiEntrataBaseService<InserisceQuotaDocumentoEntrata, InserisceQuotaDocumentoEntrataResponse> {
	
	private static final String DESCRIZIONE_OPERAZIONE = "Inserimento quota documento di entrata";
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdoc = req.getSubdocumentoEntrata();
		bilancio = req.getBilancio();
		capitolo = req.getCapitolo();
		
		checkNotNull(bilancio, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(bilancio.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento Entrata"));
		
		checkNotNull(subdoc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(subdoc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(subdoc.getDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento subdocumento Entrata"));
		checkCondition(subdoc.getDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento subdocumento Entrata"));
		
		//checkNotNull(subdoc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero subdocumento Entrata"));//staccato in automatico
		checkNotNull(subdoc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo subdocumento Entrata"));
		
		checkCondition( subdoc.getProvvisorioCassa() == null || subdoc.getProvvisorioCassa().getUid() != 0 || 
				(subdoc.getProvvisorioCassa().getAnno() != null && subdoc.getProvvisorioCassa().getNumero() != null) ||
				(subdoc.getProvvisorioCassa().getAnno() == null && subdoc.getProvvisorioCassa().getNumero() == null),
				 ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero provvisorio di cassa") );
		
		checkCondition(subdoc.getAccertamento()==null || subdoc.getAccertamento().getUid() == 0 ||
				(subdoc.getAccertamento().getAnnoMovimento()!=0 && subdoc.getAccertamento().getNumero()!=null), 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero accertamento")); 
		
		checkCondition(subdoc.getAttoAmministrativo()==null || subdoc.getAttoAmministrativo().getUid() != 0,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid atto amministrativo")); 
		
		
		
		if(subdoc.getNoteTesoriere()!=null) {
			checkEntita(subdoc.getNoteTesoriere(), "note tesoriere");
		}
		this.gestisciModificaImportoAccertamento = req.isGestisciModificaImporto();
		this.msgOperazione = DESCRIZIONE_OPERAZIONE;
	}
	
	@Override
	protected void init() {
		super.init();
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
		subdocumentoEntrataDad.setEnte(subdoc.getEnte());
		documentoEntrataDad.setEnte(subdoc.getEnte());
		documentoEntrataDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	@Transactional
	public InserisceQuotaDocumentoEntrataResponse executeService(InserisceQuotaDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
		

	@Override
	protected void execute() {
		String methodName = "execute";
		
		if(!req.isQuotaContestuale()){
			caricaAttoAmministrativo();
			checkAttoAmministrativo();
			
			caricaBilancio();
			
			caricaDettaglioDocumentoAssociato();
			
			caricaAccertamentoESubAccertamento();	
			checkAccertamentoSubAccertamento();
			
			caricaProvvisorioDiCassa();
			checkProvvisorioDiCassa();
			
			if(subdoc.getAttoAmministrativo() != null && subdoc.getAttoAmministrativo().getUid() != 0){
				caricaCapitolo();
				//SIAC-6888
				String gestioneLivello = getGestioneLivello(TipologiaGestioneLivelli.ABILITAZIONE_INSERIMENTO_ACC_AUTOMATICO);
				if("ABILITAZIONE_INSERIMENTO_ACC_AUTOMATICO".equals(gestioneLivello)) {
					log.debug(methodName, "ENTE ABILITATO - accertamento automatico.");
					inserisciAccertamentoAutomatico();
				}
			}
		
			impostaFlagOrdinativo();
		}
		// SIAC-4956
		impostaFlagConvalidaManuale(req.isImpostaFlagConvalidaManuale());
		
		Integer numeroSubdocumento = subdocumentoEntrataDad.staccaNumeroSubdocumento(subdoc.getDocumento().getUid());
		subdoc.setNumero(numeroSubdocumento);
		gestisciNumeroRegistrazioneIva();
		
		subdocumentoEntrataDad.inserisciAnagraficaSubdocumentoEntrata(subdoc);	
			
//		gestisciAllegatoAtto();
		
		if(req.isAggiornaStatoDocumento()) {
			DocumentoEntrata statoOperativoDocumento = aggiornaStatoOperativoDocumento(subdoc.getDocumento().getUid());
			subdoc.getDocumento().setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());
			subdoc.getDocumento().setDataInizioValiditaStato(statoOperativoDocumento.getDataInizioValiditaStato());
		}
		
		res.setSubdocumentoEntrata(subdoc);
	}
	
	/*la funzione fnc_siac_disponibilitaincassaremovgesta considera anche i predocumenti di entrata in stato I o C. Per tutti gli enti e operatori che possono sfondare i predocumenti 
	 * (cioè se non hanno l'azione OP-ENT-PreDocNoModAcc ) e il tipo di documento è disposizione di incasso non viene fatto nessun controllo sull'accertamento. Per CMTO invece che
	 * ha richiesto di non sfondare è necessario bypassare il controllo nel caso in cui si stia facendo il DEFINISCI di una predisposizione di incasso con accertamento perché il controllo è già 
	 * stato fatto nell'associazione tra predoc e accertamento. 
	 */
	@Override
	protected boolean isDSIAbilitatoASfondareAccertamento() {
		final String methodName = "isPossibileSfondareAccertamento";
		       //non sono CMTO: non controllo nulla, nel caso verra' poi inserita una modifica automatica di accertamento nell'emissione ordinativi
		return !isAzioneConsentita(AzioniConsentite.PREDOCUMENTO_ENTRATA_MODIFICA_ACC_NON_AMMESSA.getNomeAzione()) 
				//sono CMTO ma sto arrivando da un definisci predocumento: salto il controllo sulla disponibilita
				|| req.isSaltaControlloDisponibilitaAccertamento();
	}
	
}
	
	
