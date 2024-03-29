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
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
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
		SubdocumentoIvaEntrata sie = new SubdocumentoIvaEntrata();
		// SIAC-4609
//		sie.setAnnoEsercizio(annoEsercizio);
		sie.setEnte(getEnte());
		sie.setRegistroIva(registroIva);
		sie.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO);
		Date docDataOperazioneDa = p.getInizioPeriodo(getAnnoEsercizio());
		Date docDataOperazioneA = p.getFinePeriodo(getAnnoEsercizio());
		
		//SIAC-7516
		List<SubdocumentoIvaEntrata> listaSubdocumenti = subdocumentoIvaEntrataDad.ricercaDettaglioSubdocumentoIvaEntrataNonQPID(sie,
				null, null, null, null, docDataOperazioneDa, docDataOperazioneA);
//		List<SubdocumentoIvaEntrata> listaSubdocumenti = subdocumentoIvaEntrataDad.ricercaDettaglioSubdocumentoIvaEntrataPerDifferitaIncassati(registroIva, inizioPeriodo, finePeriodo);
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