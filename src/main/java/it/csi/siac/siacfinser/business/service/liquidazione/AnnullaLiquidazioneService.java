/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.liquidazione;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazioneResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.LiquidazioneDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.subdoc.SiacTSubdocFinRepository;
import it.csi.siac.siacfinser.integration.entity.SiacTSubdocFin;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacgenser.model.TipoCollegamento;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaLiquidazioneService extends AbstractBaseService<AnnullaLiquidazione, AnnullaLiquidazioneResponse> {

	@Autowired
	ProvvedimentoService provvedimentoService;

	@Autowired
	SiacTSubdocFinRepository siacTSubdocFinRepository;

	@Autowired
	LiquidazioneDad liquidazioneDad;
	
	@Autowired
	BilancioDad bilancioDad;
	
	@Autowired
	CommonDad commonDad;
	
	@Autowired
	AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaService;
	
	private DatiOperazioneDto datiOperazioneAnnulla = new DatiOperazioneDto();
	
	@Override
	protected void init() {
		final String methodName = "AnnullaLiquidazioneService : init()";
		log.debug(methodName, "- Begin");
		
		bilancioDad.setEnteEntity(ente);
	}
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	
	@Override
	@Transactional
	public AnnullaLiquidazioneResponse executeService(AnnullaLiquidazione serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	
	
	@Override
	public void execute() {
		final String methodName = "AnnullaLiquidazioneService : execute()";
		//Controllo che non ci siano ordinativi validi agganciati
		boolean hasOrdinativi=liquidazioneDad.checkOrdinativiAgganciati(req.getLiquidazioneDaAnnullare().getAnnoLiquidazione(), req.getLiquidazioneDaAnnullare().getNumeroLiquidazione(), req.getAnnoEsercizio(),datiOperazioneAnnulla, req.getRichiedente());
		
		boolean annullaStatoDocumento = req.isAggiornaStatoDocumento();
		
		if (hasOrdinativi) {
			List<Errore> listaErrori = new ArrayList<Errore>();
			listaErrori.add(ErroreFin.ANNULLAMENTO_LIQUIDAZIONE_IMPOSSIBILE.getErrore(""));
			res.setErrori(listaErrori);
			res.setEsito(Esito.FALLIMENTO);
		}else {
			//Controllo che non ci siano ordinativi validi agganciati al residuo
			String annoEsercizioSucc=String.valueOf((Integer.parseInt(req.getAnnoEsercizio()))+1);
			boolean hasOrdinativiResiduo=liquidazioneDad.checkOrdinativiAgganciati(req.getLiquidazioneDaAnnullare().getAnnoLiquidazione(), req.getLiquidazioneDaAnnullare().getNumeroLiquidazione(), annoEsercizioSucc,datiOperazioneAnnulla, req.getRichiedente());
			if (hasOrdinativiResiduo) {
				
				List<Errore> listaErrori = new ArrayList<Errore>();
				listaErrori.add(ErroreFin.ANNULLAMENTO_LIQUIDAZIONE_IMPOSSIBILE.getErrore("Annullamento in doppia gestione"));
				res.setErrori(listaErrori);
				res.setEsito(Esito.FALLIMENTO);
				
			} else {
				setBilancio(bilancioDad.getBilancioByAnno(Integer.valueOf(req.getAnnoEsercizio())));
				// Se non ci sono ordinativi collegati all'ordinativo o al residuo procedo con l'annullamento
				Liquidazione liquidazioneAnnullata = liquidazioneDad.annullaLiquidazione(req.getLiquidazioneDaAnnullare().getAnnoLiquidazione(), req.getLiquidazioneDaAnnullare().getNumeroLiquidazione(), req.getAnnoEsercizio(), datiOperazioneAnnulla, req.getRichiedente());
				
				if(annullaStatoDocumento){
					AggiornaStatoDocumentoDiSpesa reqDoc = new AggiornaStatoDocumentoDiSpesa();
					
					if(liquidazioneAnnullata.getSubdocumentoSpesa()!=null){
						
						//SIAC-6882 annullo il campo convalida manuale sul subdocumento
						SiacTSubdocFin subdoc = siacTSubdocFinRepository.findOne(liquidazioneAnnullata.getSubdocumentoSpesa().getUid());
						subdoc.setSubdocConvalidaManuale(null);
						siacTSubdocFinRepository.saveAndFlush(subdoc);
						
						
						reqDoc.setDocumentoSpesa(liquidazioneAnnullata.getSubdocumentoSpesa().getDocumento());
						reqDoc.setRichiedente(req.getRichiedente());
						reqDoc.setBilancio(bilancio);
						
						log.debug(methodName, "Richiamo il ser Aggiorna stato documento di spesa per richiedente.account.uid: "+req.getRichiedente().getAccount().getUid());
						AggiornaStatoDocumentoDiSpesaResponse resDoc = executeExternalServiceSuccess(aggiornaStatoDocumentoDiSpesaService,reqDoc);
						if(resDoc.isFallimento() && resDoc.getErrori()!=null){
							log.error(methodName, "Il servizio è andato in errore: "+resDoc.getErrori().get(0).getTesto());
						}
						
					}
				}
				
				
				// innesto fin -gen 
				annullaRegistrazioneEPrimaNotaLiquidazione(TipoCollegamento.LIQUIDAZIONE, liquidazioneAnnullata);
			}
			
			
		}
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "AnnullaLiquidazioneService : checkServiceParam()";
		log.debug(methodName, "- Begin");
		
		Integer annoBilancio = null;
		if(req.getAnnoEsercizio()!=null){
			annoBilancio = Integer.valueOf(req.getAnnoEsercizio());
		}
		
		if(req.getEnte()!=null && req.getRichiedente()!=null){
			datiOperazioneAnnulla = commonDad.inizializzaDatiOperazione(req.getEnte(), req.getRichiedente(), Operazione.ANNULLA, annoBilancio);
		}else{
			res.setErrori(Arrays.asList(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente o richiedente")));
			res.setEsito(Esito.FALLIMENTO);	
		}
				
		
		if(req.getAnnoEsercizio()==null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno Esercizio"));
		}
		if(req.getLiquidazioneDaAnnullare()==null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Liquidazione da annullare"));
		}
		if(req.getLiquidazioneDaAnnullare()!=null && 
				(req.getLiquidazioneDaAnnullare().getAnnoLiquidazione()==null || 
				req.getLiquidazioneDaAnnullare().getAnnoLiquidazione().intValue()==0)){

			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno liquidazione"));
		}
		
		if(req.getLiquidazioneDaAnnullare()!=null && 
				(req.getLiquidazioneDaAnnullare().getNumeroLiquidazione()==null || 
				req.getLiquidazioneDaAnnullare().getNumeroLiquidazione().intValue()==0)){

			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero liquidazione"));
		}
		
		// controllo la presenza di voci di liquidazione
		if(!res.isFallimento()){
		    if(!liquidazioneDad.presenzaLiquidazione(req.getLiquidazioneDaAnnullare().getAnnoLiquidazione(), req.getLiquidazioneDaAnnullare().getNumeroLiquidazione(), req.getAnnoEsercizio(), datiOperazioneAnnulla)){
		    	res.setErrori(Arrays.asList(ErroreFin.ANNULLAMENTO_LIQUIDAZIONE_IMPOSSIBILE.getErrore("")));
		    }
		}
		

	}	
}
