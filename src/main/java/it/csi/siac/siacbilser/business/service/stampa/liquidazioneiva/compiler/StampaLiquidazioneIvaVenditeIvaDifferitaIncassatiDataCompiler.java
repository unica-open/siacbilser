/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.compiler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaDatiERiepilogo;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaLiquidazioneIvaVenditeIvaDifferitaIncassatiDataCompiler extends StampaLiquidazioneIvaBaseVenditeDataCompiler {
	
	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;
	
	@Override
	protected void createAndSetPage() {
		final String methodName = "createAndSetPage";
		pagina = new StampaLiquidazioneIvaDatiERiepilogo();
		result.setVenditeIvaDifferitaIncassata(pagina);
		
		log.debug(methodName, "Pagina creata per la VenditeIvaDifferitaIncassata");
	}
	
	@Override
	protected void ottieniListaSubdocumentoIva() {
//		final String methodName = "ottieniListaSubdocumentoIva";
		
		listaSubdocumentoIva = new ArrayList<SubdocumentoIvaEntrata>();
		for(RegistroIva registroIva : listaRegistroIva) {
			if(!TipoRegistroIva.VENDITE_IVA_DIFFERITA.equals(registroIva.getTipoRegistroIva())) {
				continue;
			}
//			SubdocumentoIvaEntrata sie = new SubdocumentoIvaEntrata();
//			sie.setAnnoEsercizio(handler.getAnnoEsercizio());
//			sie.setEnte(handler.getEnte());
//			sie.setRegistroIva(registroIva);
//			sie.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO);
			
			Date inizioPeriodo = handler.getPeriodo().getInizioPeriodo(handler.getAnnoEsercizio());
			Date finePeriodo = handler.getPeriodo().getFinePeriodo(handler.getAnnoEsercizio());
			
			List<SubdocumentoIvaEntrata> list = subdocumentoIvaEntrataDad.ricercaDettaglioSubdocumentoIvaEntrataPerDifferitaIncassati(registroIva, inizioPeriodo, finePeriodo);
			listaSubdocumentoIva.addAll(list);
			
//			List<SubdocumentoIvaEntrata> temp = subdocumentoIvaEntrataDad.ricercaDettaglioSubdocumentoIvaEntrata(sie, null, null, null, null);
//			boolean passaggioDiStatoIvaVenditeSuQuota = false;
			
//			for(SubdocumentoIvaEntrata sieTemp : temp) {
//				// 1. la registrazione iva è stata fatta sulla singola quota (ovvero 'numOrdinativoDoc' diverso da NULL)
//				if(sieTemp.getNumeroOrdinativoDocumento() != null) {
//					log.debug(methodName, "SubdocumentoIvaEntrata.uid = " + sieTemp.getUid() + " con numero ordinativo " + sieTemp.getNumeroOrdinativoDocumento());
//					Date dpd = sieTemp.getDataProtocolloDefinitivo();
//					// In questo caso se la quota è stata incassata, vuol dire che è stato incassato tutto il Subdocumento Iva e, per quanto riguarda il Periodo,
//					// il criterio di selezione è il seguente:
//					// 		- 'Subdocumento Iva'.'Data Protocollo Definitivo' >= 'inizio_periodo' e
//					// 		- 'Subdocumento Iva'.'Data Protocollo Definitivo' <= 'fine_periodo'
//					if(dpd != null && dpd.compareTo(inizioPeriodo) >= 0 && dpd.compareTo(finePeriodo) <= 0) {
//						log.debug(methodName, "Registro.uid = " + registroIva.getUid() +
//								" SubdocumentoIvaEntrata.uid = " + sieTemp.getUid() + " con numero ordinativo " + sieTemp.getNumeroOrdinativoDocumento() +
//								" selezionato");
//						listaSubdocumentoIva.add(sieTemp);
//					}
//				} else {
//					// 2. la registrazione iva è stata fatta sull’intero documento (ovvero 'numOrdinativoDoc' = NULL)
////					if (passaggioDiStatoIvaVenditeSuQuota) {
//						// Il parametro di configurazione a livello di Ente 'Passaggio di stato Iva Vendite' = SU QUOTA
//						
//						 // I dati del Protocollo Definitivo che occorrono per la stampa del Registro Iva sono relativi sempre alla stessa entità 'Subdocumento Iva'
//						// (V. modello concettuale 3), ma che sono collegate ad un Subdocumento Iva di riferimento attraverso la relazione 'quote iva differita'.
//						for(SubdocumentoIvaEntrata qid : sieTemp.getListaQuoteIvaDifferita()) {
//							log.debug(methodName, "Registro.uid = " + registroIva.getUid() + " QuotaIvaDifferita.uid = " + qid.getUid());
//							Date dpd = qid.getDataProtocolloDefinitivo();
//							if(dpd != null && dpd.compareTo(inizioPeriodo) >= 0 && dpd.compareTo(finePeriodo) <= 0 && qid.getNumeroOrdinativoDocumento() != null) {
//								log.debug(methodName, "QuotaIvaDifferita.uid = " + qid.getUid() + " selezionata");
//								
////								SubdocumentoIvaEntrata sieQID = new SubdocumentoIvaEntrata();
////								DummyMapper.mapNotNullNotEmpty(qid, sieQID);
//								// TODO: popolare dato aggiuntivi?
//								
//								// Converto la nota in un subdoc iva di entrata
//								SubdocumentoIvaEntrata sieQID = subdocumentoIvaEntrataDad.findSubdocumentoIvaEntrataById(qid.getUid());
//								listaSubdocumentoIva.add(sieQID);
//							}
//						}
//					}
//				}
//			}
		}
	}
	
	@Override
	protected BigDecimal obtainImpostaFromProgressiviIvaAndAliquotaIva(ProgressiviIva progressiviIva, AliquotaIva aliquotaIva) {
		return progressiviIva.getTotaleIvaDefinitivo();
	}
	
	@Override
	protected boolean isRigaToSkip(BigDecimal totaleImponibileDefinitivo, BigDecimal totaleIvaDetraibilieDefinitivo) {
		String methodName ="isRigaToSkip";
		log.debug(methodName, "TotaleImponibileDefinitivo: " +totaleImponibileDefinitivo 
				+ " TotaleIvaDetraibileDefinitivo:"+totaleIvaDetraibilieDefinitivo);
		return isNullOrZero(totaleImponibileDefinitivo)
				&& isNullOrZero(totaleIvaDetraibilieDefinitivo);
	}

}
