/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * The Class AggiornaQuotaDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaQuotaDocumentoEntrataService extends CrudDocumentoDiEntrataBaseService<AggiornaQuotaDocumentoEntrata, AggiornaQuotaDocumentoEntrataResponse> {
	
	private static final String DESCRIZIONE_OPERAZIONE = "Aggoiornamento quota documento di entrata";
	
//	/** The subdocumento entrata dad. */
//	@Autowired
//	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		capitolo = req.getCapitolo();
		subdoc = req.getSubdocumentoEntrata();
		bilancio = req.getBilancio();
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento Entrata"));
		checkCondition(subdoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento Entrata"));
		
		checkNotNull(subdoc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(subdoc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(subdoc.getDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento subdocumento Entrata"));
		checkCondition(subdoc.getDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento subdocumento Entrata"));
		
		checkNotNull(subdoc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero subdocumento"));
		checkNotNull(subdoc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo subdocumento"));
		
		checkCondition( subdoc.getProvvisorioCassa() == null ||
				(subdoc.getProvvisorioCassa().getAnno() != null && subdoc.getProvvisorioCassa().getNumero() != null) ||
				(subdoc.getProvvisorioCassa().getAnno() == null && subdoc.getProvvisorioCassa().getNumero() == null),
				 ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero provvisorio di cassa") );
		
		checkCondition(subdoc.getAccertamento()==null || subdoc.getAccertamento().getUid()== 0 ||
				(subdoc.getAccertamento().getAnnoMovimento()!=0 && subdoc.getAccertamento().getNumero()!=null), 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno o numero accertamento")); 
		
		checkCondition(subdoc.getAttoAmministrativo()==null || subdoc.getAttoAmministrativo().getUid() != 0,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid atto amministrativo")); 
		
		this.gestisciModificaImportoAccertamento = req.isGestisciModificaImporto();
		this.msgOperazione = DESCRIZIONE_OPERAZIONE;
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
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
	public AggiornaQuotaDocumentoEntrataResponse executeService(AggiornaQuotaDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {	
		String methodName = "execute";
		
		checkQuotaAggiornabile();
		
		caricaAttoAmministrativo();
		checkAttoAmministrativo();
		
		caricaBilancio();
		
		caricaDettaglioDocumentoAssociato();
		
		caricaAccertamentoESubAccertamento();
		checkAccertamentoSubAccertamento();
		
		caricaProvvisorioDiCassa();
		checkProvvisorioDiCassa();

		//SIAC-6888
		String gestioneLivello = getGestioneLivello(TipologiaGestioneLivelli.ABILITAZIONE_INSERIMENTO_ACC_AUTOMATICO);
		if("ABILITAZIONE_INSERIMENTO_ACC_AUTOMATICO".equals(gestioneLivello)) {
			log.debug(methodName, "ENTE ABILITATO - accertamento automatico.");
			gestisciInserimentoAccertamentoAutomatico();
		}
		
		impostaFlagOrdinativo();
		impostaFlagConvalidaManuale(req.isImpostaFlagConvalidaManuale());
		gestisciNumeroRegistrazioneIva();
		subdocumentoEntrataDad.aggiornaAnagraficaSubdocumentoEntrata(subdoc);
		log.debug(methodName, "anagrafica del subdocumento aggioranta.");
		
		
		if(req.isAggiornaStatoDocumento()) {
			DocumentoEntrata statoOperativoDocumento = aggiornaStatoOperativoDocumento(subdoc.getDocumento().getUid());
			subdoc.getDocumento().setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());
			if(statoOperativoDocumento.getDataInizioValiditaStato() != null){
				subdoc.getDocumento().setDataInizioValiditaStato(statoOperativoDocumento.getDataInizioValiditaStato());
			}
		}
		
		res.setSubdocumentoEntrata(subdoc);
	}

	private void gestisciInserimentoAccertamentoAutomatico() {
		final String methodName = "gestisciInserimentoAccertamentoAutomatico";
		
		if(subdoc.getAttoAmministrativo() == null || subdoc.getAttoAmministrativo().getUid() == 0){
			log.debug(methodName, "atto amministrativo del subdoc non valorizzato. Non devo inserire accertamento automatico. Esco.");
			return;
		}
		
		log.debug(methodName, "ho un atto amministrativo valido. Carico i dati del capitolo.");
		caricaCapitolo();
		inserisciAccertamentoAutomatico();
		
		//SIAC-3977, SIAC-3988
		// SIAC-4775: il flag deve essere SEMPRE impostato
		//SE AttivaGenPcc TRUE o il documento non e' di tipo da registrare in GEN 
//		boolean isflagConvalidaManualeDaInizializzare = Boolean.TRUE.equals(subdoc.getDocumento().getContabilizzaGenPcc()) 
//				||  !Boolean.TRUE.equals(subdoc.getDocumento().getTipoDocumento().getFlagAttivaGEN());
//		log.debug(methodName, "isflagConvalidaManualeDaInizializzare? "+isflagConvalidaManualeDaInizializzare);
//		subdoc.setFlagConvalidaManuale(isflagConvalidaManualeDaInizializzare?getDefaultFlagConvalidaManuale():null);
	}
	
	private void checkQuotaAggiornabile() {
			
			//TODO
	//		1. DA NON SVILUPPARE IN V1 - La quota non deve essere legata ad un 
	//		elenco in elaborazione (entità StatoOperativoElaborazioniAsincrone in stato IE), <FIN_ERR_0246, 
	//		Quota non aggiornabile perché in elaborazione asincrona.>.
			
			if(subdoc.getOrdinativo() != null && subdoc.getOrdinativo().getUid() != 0){
				throw new BusinessException(ErroreFin.QUOTA_NON_AGGIORNABILE_PERCHE_CON_ORDINATIVO.getErrore());
			}
	}

}
