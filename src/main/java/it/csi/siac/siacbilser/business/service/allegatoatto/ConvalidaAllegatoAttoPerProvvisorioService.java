/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerProvvisorio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerProvvisorioResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * 
 * Gestisce la convalida di uno o più documenti legati a un provvisorio di cassa.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConvalidaAllegatoAttoPerProvvisorioService extends ConvalidaAllegatoAttoBaseService<ConvalidaAllegatoAttoPerProvvisorio, ConvalidaAllegatoAttoPerProvvisorioResponse> {


	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getProvvisorioDiCassa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("provvisorio di cassa"));
		checkCondition(req.getProvvisorioDiCassa().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid provvisorio di cassa"), false);
		
		checkNotNull(req.getFlagConvalidaManuale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag convalida manuale"));
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"), false);
		this.ente = req.getEnte();
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public ConvalidaAllegatoAttoPerProvvisorioResponse executeServiceTxRequiresNew(ConvalidaAllegatoAttoPerProvvisorio serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional
	public ConvalidaAllegatoAttoPerProvvisorioResponse executeService(ConvalidaAllegatoAttoPerProvvisorio serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		String methodName = "execute";
		
		List<AllegatoAtto> allegatiAtto = trovaAllegatiAttoLegatiAProvvisorioDiCassa(req.getProvvisorioDiCassa());
		
		//Per ogni allegatoAtto legato al Provvisorio
		for(AllegatoAtto allegatoAtto : allegatiAtto){
			//allegatoAtto = caricaAllegatoAtto(req.getAllegatoAtto().getUid()); // si presume già caricato da trovaAllegatiAttoLegatiAProvvisorioDiCassa
			try{
				checkStatoOperativoAllegatoAttoCompletato(allegatoAtto);
			} catch (BusinessException be){
				log.info(methodName, "allegatoAtto scartato! ui:"+ allegatoAtto.getUid() 
						+ ". Errore: "+ (be.getErrore()!=null? be.getErrore().getTesto():"null"));
				res.addErrore(be.getErrore());
				res.getAllegatiAttoScartati().add(allegatoAtto);
				continue; //L'elenco viene scartato. Si continua con il prossimo.
			}
				
			//Per ogni elenco passato...
			for(ElencoDocumentiAllegato elencoDocumentiAllegatoReq : allegatoAtto.getElenchiDocumentiAllegato()){
				ElencoDocumentiAllegato elencoDocumentiAllegato = caricaElencoDocumentiAllegato(elencoDocumentiAllegatoReq.getUid());
				
				try{
					checkStatoElenco(elencoDocumentiAllegato);
					checkPresenzaSubordinati(elencoDocumentiAllegato);
				} catch (BusinessException be){
					log.info(methodName, "elencoDocumentiAllegato scartato! uid elenco:"+ elencoDocumentiAllegato.getUid() 
							+ ". Errore: "+ (be.getErrore()!=null? be.getErrore().getTesto():"null"));
					res.addErrore(be.getErrore());
					res.getElenchiScartati().add(elencoDocumentiAllegato);
					continue; //L'elenco viene scartato. Si continua con il prossimo.
				}
				
				List<Subdocumento<?, ?>> subdocumenti = elencoDocumentiAllegato.getSubdocumenti();
				impostaFlagConvalidaManuale(subdocumenti);
				
				//Per tutti i subdocumenti in elenco...
				for(Subdocumento<?, ?> subdocReq : subdocumenti){
					Subdocumento<?, ?> subdoc = subdocReq; //caricaSubdocumentoConDatiDiBase(subdocReq); //Mi bastano i dati di base tirati su da caricaElencoDocumentiAllegato
					
					try{
						checkTipoConvalida(subdoc);
						checkSoggettoSospeso(allegatoAtto, subdoc);
					} catch (BusinessException be){
						log.info(methodName, "subdocumento scartato! uid elenco:"+ elencoDocumentiAllegato.getUid() 
								+ ". Errore: "+ (be.getErrore()!=null? be.getErrore().getTesto():"null"));
						res.addErrore(be.getErrore());
						res.getSubdocumentiScartati().add(subdoc);
						continue; //Il subdocumento viene scartato. Si continua con il prossimo.
					}
					
					aggiornaTipoConvalida(subdocReq);
					if(subdocReq instanceof SubdocumentoSpesa){
						((SubdocumentoSpesa)subdocReq).setDocumento(((SubdocumentoSpesa)subdoc).getDocumento());
						aggiornaLiquidazioneModulare((SubdocumentoSpesa)subdocReq);
					}
					
					res.incCountQuoteConvalidate();
					
					//SIAC-4752 - Aggiunto messaggio di dettaglio
					res.addMessaggio("CONV_INFO_QUOTA", "Convalidata quota numero "
							+ subdoc.getNumero() + (subdoc.getDocumento()!=null?" del documento "+ subdoc.getDocumento().getDescAnnoNumeroTipoDoc():"")
							+ " appartenente all'elenco "+ elencoDocumentiAllegato.getAnno()+"/"+ elencoDocumentiAllegato.getNumero() + " [uid: "+elencoDocumentiAllegato.getUid()+"]"
						);
				}
			}
			
			aggiornaStatoOperativoAllegatoAtto(allegatoAtto);
		
		}
		
		//SIAC-4752 - Aggiunto messaggio di fine elaborazione (TODO migliorabile)
		res.addMessaggio("CONV_INFO_RIEP", "Totale allegati atto elaborati: " + allegatiAtto.size()
				+ " di cui scartati: " + res.getAllegatiAttoScartati().size()
				+ ", totale quote in elenco convalidate: " + res.getCountQuoteConvalidate()
			);
		

	}

	
	/**
	 * Trova tutti gli allegati atto legati a provvisorio di cassa.
	 *
	 * @param provvisorioDiCassa the provvisorio di cassa
	 * @return the list
	 */
	private List<AllegatoAtto> trovaAllegatiAttoLegatiAProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDiCassa) {
		return allegatoAttoDad.findAllegatiAttoDettaglioByProvvisorioDiCassa(provvisorioDiCassa.getUid());
	}



}
