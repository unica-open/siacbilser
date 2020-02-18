/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.stampa.registroiva.handler.comparator.ComparatorRigaSezione1StampaRegistroIva;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaDatiIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRegistroIvaVenditeIvaDifferitaIncassatiReportHandler extends StampaRegistroIvaVenditeReportHandler {
	
	@Override
	protected void ottieniListaSubdocumentoIvaEntrata() {
		listaSubdocumentoIvaEntrata = ottieniListaSubdocumentoIvaEntrataNelPeriodo(periodo);		
	}	

	@Override
	protected List<SubdocumentoIvaEntrata> ottieniListaSubdocumentoIvaEntrataNelPeriodo(Periodo p) {
		final String methodName = "ottieniListaSubdocumentoIvaEntrataNelPeriodo";
		
		if(p == null){
			log.debug(methodName, "Il periodo di cui si stanno cercando i documenti di entrata risulta essere null. ");
			return new ArrayList<SubdocumentoIvaEntrata>();
		}
//		List<SubdocumentoIvaEntrata> listaSubdocumenti = new ArrayList<SubdocumentoIvaEntrata>();
//		SubdocumentoIvaEntrata sie = new SubdocumentoIvaEntrata();
//		sie.setAnnoEsercizio(annoEsercizio);
//		sie.setEnte(ente);
//		sie.setRegistroIva(registroIva);
//		sie.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO);
		
		Date inizioPeriodo = p.getInizioPeriodo(getAnnoEsercizio());
		Date finePeriodo = p.getFinePeriodo(getAnnoEsercizio());
		
//		List<SubdocumentoIvaEntrata> temp = subdocumentoIvaEntrataDad.ricercaDettaglioSubdocumentoIvaEntrata(sie, null, null, null, null);
//		
//		// TODO: controllare il pasaggio di stato dall'ente
////		boolean passaggioDiStatoIvaVenditeSuQuota = false;
//		
//		for(SubdocumentoIvaEntrata sieTemp : temp) {
//			// 1. la registrazione iva è stata fatta sulla singola quota (ovvero “numOrdinativoDoc” diverso da NULL)
//			if(sieTemp.getNumeroOrdinativoDocumento() != null) {
//				log.debug(methodName, "SubdocumentoIvaEntrata.uid = " + sieTemp.getUid() + " con numero ordinativo " + sieTemp.getNumeroOrdinativoDocumento());
//				Date dpd = sieTemp.getDataProtocolloDefinitivo();
//				// In questo caso se la quota è stata incassata, vuol dire che è stato incassato tutto il Subdocumento Iva e, per quanto riguarda il Periodo,
//				// il criterio di selezione è il seguente:
//				// 		- “Subdocumento Iva“.”Data Protocollo Definitivo” >= “inizio_periodo” e
//				// 		- “Subdocumento Iva“.”Data Protocollo Definitivo” <= “fine_periodo”
//				if(dpd != null && dpd.compareTo(inizioPeriodo) >= 0 && dpd.compareTo(finePeriodo) <= 0) {
//					log.debug(methodName, "SubdocumentoIvaEntrata.uid = " + sieTemp.getUid() + " con numero ordinativo " + sieTemp.getNumeroOrdinativoDocumento() +
//							" selezionato");
//					listaSubdocumentoIvaEntrataNelPeriodo.add(sieTemp);
//				}
//			} else {
//				// 2. la registrazione iva è stata fatta sull’intero documento (ovvero “numOrdinativoDoc” = NULL)
////				if (passaggioDiStatoIvaVenditeSuQuota) {
//					// Il parametro di configurazione a livello di Ente “Passaggio di stato Iva Vendite” = SU QUOTA
//					
//					// I dati del Protocollo Definitivo che occorrono per la stampa del Registro Iva sono relativi sempre alla stessa entità “Subdocumento Iva”
//					// (V. modello concettuale 3), ma che sono collegate ad un Subdocumento Iva di riferimento attraverso la relazione “quote iva differita”.
//					for(SubdocumentoIvaEntrata qid : sieTemp.getListaQuoteIvaDifferita()) {
//						log.debug(methodName, "QuotaIvaDifferita.uid = " + qid.getUid());
//						Date dpd = qid.getDataProtocolloDefinitivo();
//						if(dpd != null && dpd.compareTo(inizioPeriodo) >= 0 && dpd.compareTo(finePeriodo) <= 0 && qid.getNumeroOrdinativoDocumento() != null) {
//							log.debug(methodName, "QuotaIvaDifferita.uid = " + qid.getUid() + " selezionata");
//							// Converto la nota in un subdoc iva di entrata
//							SubdocumentoIvaEntrata sieQID = subdocumentoIvaEntrataDad.findSubdocumentoIvaEntrataById(qid.getUid());
//							
////							DummyMapper.mapNotNullNotEmpty(qid, sieQID);
//							// TODO: popolare dato aggiuntivi?
//							
//							//le quote iva differita non hanno data di protocollo provvisorio, ma sulla stampa si vuole visualizzare quella della quota padre
//							sieQID.setDataProtocolloProvvisorioSI(sieTemp.getDataProtocolloProvvisorioSI());
//							listaSubdocumentoIvaEntrataNelPeriodo.add(sieQID);
//						}
//					}
////				}
//			}
//		}
		
		List<SubdocumentoIvaEntrata> listaSubdocumenti = subdocumentoIvaEntrataDad.ricercaDettaglioSubdocumentoIvaEntrataPerDifferitaIncassati(registroIva, inizioPeriodo, finePeriodo);
		return listaSubdocumenti;

	}

	@Override
	protected void sortSezione1(StampaRegistroIvaDatiIva sezione1) {
		Collections.sort(sezione1.getListaDatiIva(), ComparatorRigaSezione1StampaRegistroIva.DEFINITIVO);
	}
	
	@Override
	protected BigDecimal obtainTotaleImponibile(ProgressiviIva progressivoIva) {
		return progressivoIva.getTotaleImponibileDefinitivo();
	}
	
	@Override
	protected BigDecimal obtainTotaleIva(ProgressiviIva progressivoIva) {
		return progressivoIva.getTotaleIvaDefinitivo();
	}
	
//	@Override
//	protected void popolaTotaleImponibile(ProgressiviIva progressiviIva, StampaRegistroIvaRiepilogoIva riga) {
//		final String methodName = "popolaTotaleImponibile";
////		BigDecimal oldTotaleImponibile = obtainTotaleImponibile(progressiviIva);
////		progressiviIva.setTotaleImponibileDefinitivo(oldTotaleImponibile.add(riga.getImponibile()));
//		progressiviIva.setTotaleImponibileDefinitivo(riga.getProgressivoImponibile());
//		log.debug(methodName, riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleImponibileDefinitivo " + progressiviIva.getTotaleImponibileDefinitivo());
//	}
//
//	@Override
//	protected void popolaTotaleIva(ProgressiviIva progressiviIva, StampaRegistroIvaRiepilogoIva riga) {
//		final String methodName = "popolaTotaleIva";
////		BigDecimal oldTotaleIva = obtainTotaleIva(progressiviIva);
////		progressiviIva.setTotaleIvaDefinitivo(oldTotaleIva.add(riga.getIva()));
//		progressiviIva.setTotaleIvaDefinitivo(riga.getProgressivoIva());
//		log.debug(methodName, riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleIvaDefinitivo " + progressiviIva.getTotaleIvaDefinitivo());
//	}
	
//	@Override
//	protected void inserisciProgressiviIvaCorrenti() {
//		for(ProgressiviIva pi : cacheProgressiviAttuali.values()) {
//			// Se ho già un record del progressivo, allora lo aggiorno. Altrimenti ne inserisco uno nuovo
//			ProgressiviIva pidb = progressiviIvaDad.findProgressiviIvaByRegistroAndAliquotaIvaAndPeriodoAndAnnoAndEnte(pi.getRegistroIva(), pi.getAliquotaIva(),
//					pi.getPeriodo(), pi.getAnnoEsercizio());
//			if(pidb == null) {
//				progressiviIvaDad.inserisciProgressiviIva(pi);
//			} else {
//				pidb.setTotaleImponibileDefinitivo(pi.getTotaleImponibileDefinitivo());
//				pidb.setTotaleIvaDefinitivo(pi.getTotaleIvaDefinitivo());
//				
//				progressiviIvaDad.aggiornaProgressiviIva(pidb);
//			}
//		}
//	}

}