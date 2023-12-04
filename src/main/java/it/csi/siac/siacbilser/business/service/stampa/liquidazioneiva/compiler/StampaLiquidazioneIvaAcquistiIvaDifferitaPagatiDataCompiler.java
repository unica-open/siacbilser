/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.compiler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaDatiERiepilogo;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaLiquidazioneIvaAcquistiIvaDifferitaPagatiDataCompiler extends StampaLiquidazioneIvaBaseAcquistiDataCompiler {
	
	@Override
	protected void createAndSetPage() {
		final String methodName = "createAndSetPage";
		pagina = new StampaLiquidazioneIvaDatiERiepilogo();
		result.setAcquistiIvaDifferitaPagata(pagina);
		
		log.debug(methodName, "Pagina creata per gli AcquistiIvaImmediata");
	}
	
	@Override
	protected void ottieniListaSubdocumentoIva() {
//		final String methodName = "ottieniListaSubdocumentoIva";
		
		listaSubdocumentoIva = new ArrayList<SubdocumentoIvaSpesa>();
		for(RegistroIva registroIva : listaRegistroIva) {
			if(!TipoRegistroIva.ACQUISTI_IVA_DIFFERITA.equals(registroIva.getTipoRegistroIva())) {
				continue;
			}
//			SubdocumentoIvaSpesa subdocIva = new SubdocumentoIvaSpesa();
//			subdocIva.setAnnoEsercizio(handler.getAnnoEsercizio());
//			subdocIva.setEnte(handler.getEnte());
//			subdocIva.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO);
//			subdocIva.setRegistroIva(registroIva);
			
			Date inizioPeriodo = handler.getPeriodo().getInizioPeriodo(handler.getAnnoEsercizio()); //inizio periodo
			Date finePeriodo = handler.getPeriodo().getFinePeriodo(handler.getAnnoEsercizio()); //fine periodo
			
			
			List<SubdocumentoIvaSpesa> list = subdocumentoIvaSpesaDad.ricercaDettaglioSubdocumentoIvaSpesaPerDifferitaPagati(registroIva, inizioPeriodo, finePeriodo);
			listaSubdocumentoIva.addAll(list);
			
//			boolean passaggioDiStatoIvaVenditeSuQuota = false;
			
//			for(SubdocumentoIvaSpesa sisTemp : temp) {
//				// 1. la registrazione iva è stata fatta sulla singola quota (ovvero 'numOrdinativoDoc' diverso da NULL)
//				if(sisTemp.getNumeroOrdinativoDocumento() != null) {
//					log.debug(methodName, "SubdocumentoIvaSpesa.uid = " + sisTemp.getUid() + " con numero ordinativo " + sisTemp.getNumeroOrdinativoDocumento());
//					Date dpd = sisTemp.getDataProtocolloDefinitivo();
//					// In questo caso se la quota è stata incassata, vuol dire che è stato incassato tutto il Subdocumento Iva e, per quanto riguarda il Periodo,
//					// il criterio di selezione è il seguente:
//					// 		- 'Subdocumento Iva'.'Data Protocollo Definitivo' >= 'inizio_periodo' e
//					// 		- 'Subdocumento Iva'.'Data Protocollo Definitivo' <= 'fine_periodo'
//					if(dpd != null && dpd.compareTo(inizioPeriodo) >= 0 && dpd.compareTo(finePeriodo) <= 0) {
//						log.debug(methodName, "Registro.uid = " + registroIva.getUid() +
//								" SubdocumentoIvaSpesa.uid = " + sisTemp.getUid() + " con numero ordinativo " + sisTemp.getNumeroOrdinativoDocumento() +
//								" selezionato");
//						listaSubdocumentoIva.add(sisTemp);
//					}
//				} else {
//					// 2. la registrazione iva è stata fatta sull’intero documento (ovvero 'numOrdinativoDoc' = NULL)
////					if (passaggioDiStatoIvaVenditeSuQuota) {
//						// Il parametro di configurazione a livello di Ente 'Passaggio di stato Iva Vendite' = SU QUOTA
//						
//						 // I dati del Protocollo Definitivo che occorrono per la stampa del Registro Iva sono relativi sempre alla stessa entità 'Subdocumento Iva'
//						// (V. modello concettuale 3), ma che sono collegate ad un Subdocumento Iva di riferimento attraverso la relazione 'quote iva differita'.
//						for(SubdocumentoIvaSpesa qid : sisTemp.getListaQuoteIvaDifferita()) {
//							log.debug(methodName, "Registro.uid = " + registroIva.getUid() + " QuotaIvaDifferita.uid = " + qid.getUid());
//							Date dpd = qid.getDataProtocolloDefinitivo();
//							if(dpd != null && dpd.compareTo(inizioPeriodo) >= 0 && dpd.compareTo(finePeriodo) <= 0 && qid.getNumeroOrdinativoDocumento() != null) {
//								log.debug(methodName, "QuotaIvaDifferita.uid = " + qid.getUid() + " selezionata");
//								// Converto la nota in un subdoc iva di entrata
//								SubdocumentoIvaSpesa sisQID = subdocumentoIvaSpesaDad.findSubdocumentoIvaSpesaById(qid.getUid());
//								
////								SubdocumentoIvaSpesa sisQID = new SubdocumentoIvaSpesa();
////								DummyMapper.mapNotNullNotEmpty(qid, sisQID);
//								// TODO: popolare dato aggiuntivi?
//								listaSubdocumentoIva.add(sisQID);
//							}
//						}
////					}
//				}
//			}
		}
	}
	
	@Override
	protected BigDecimal obtainImpostaFromProgressiviIvaAndAliquotaIva(ProgressiviIva progressiviIva, AliquotaIva aliquotaIva) {
		final BigDecimal totaleIndetraibilita = progressiviIva.getTotaleIvaDefinitivo()
				.multiply(aliquotaIva.getPercentualeIndetraibilita())
				.divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED);
		return progressiviIva.getTotaleIvaDefinitivo().subtract(totaleIndetraibilita);
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
