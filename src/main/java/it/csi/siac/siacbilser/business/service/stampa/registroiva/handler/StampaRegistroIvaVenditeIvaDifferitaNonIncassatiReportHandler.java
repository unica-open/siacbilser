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
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaRiepilogoIva;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRegistroIvaVenditeIvaDifferitaNonIncassatiReportHandler extends StampaRegistroIvaVenditeReportHandler {
	
	@Override
	protected void  ottieniListaSubdocumentoIvaEntrata() {
		listaSubdocumentoIvaEntrata = ottieniListaSubdocumentoIvaEntrataNelPeriodo(getPeriodo());
	}
	
	@Override
	protected List<SubdocumentoIvaEntrata> ottieniListaSubdocumentoIvaEntrataNelPeriodo(Periodo p) {
		final String methodName = "ottieniListaSubdocumentoIvaEntrataNelPeriodo";
		
		if(p == null){
			log.debug(methodName, "Il periodo di cui si stanno cercando i documenti di entrata risulta essere null. ");
			return new ArrayList<SubdocumentoIvaEntrata>();
		}
		
		SubdocumentoIvaEntrata sie = new SubdocumentoIvaEntrata();
		// SIAC-4609
//		sie.setAnnoEsercizio(annoEsercizio);
		sie.setEnte(getEnte());
		sie.setRegistroIva(registroIva);
		//		CR-3661 (laura moscatelli) si chiama 'non incassate' ma devono essere visulizzate tutte, quindi anche quelle in stato provvisorio_definitivo
//		sie.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
		// inizio periodo
		Date docDataOperazioneDa = p.getInizioPeriodo(getBilancio().getAnno());
		// fine periodo
		Date docDataOperazioneA = p.getFinePeriodo(getBilancio().getAnno());
		
		List<SubdocumentoIvaEntrata> listaSubdocumentoIvaEntrataNelPeriodo = subdocumentoIvaEntrataDad.ricercaDettaglioSubdocumentoIvaEntrataNonQPID(sie,
				null, null, null, null, docDataOperazioneDa, docDataOperazioneA);
		
		return listaSubdocumentoIvaEntrataNelPeriodo;
		 
	}

	@Override
	protected void sortSezione1(StampaRegistroIvaDatiIva sezione1) {
		log.debug("sortSezione1", "Sorting della lista dei dati iva");
		Collections.sort(sezione1.getListaDatiIva(), ComparatorRigaSezione1StampaRegistroIva.PROVVISORIO);
	}
	
	@Override
	protected BigDecimal obtainTotaleImponibile(ProgressiviIva progressivoIva) {
		return progressivoIva.getTotaleImponibileProvvisorio();
	}
	
	@Override
	protected BigDecimal obtainTotaleIva(ProgressiviIva progressivoIva) {
		return progressivoIva.getTotaleIvaProvvisorio();
	}
	
	@Override
	protected void popolaTotaleImponibile(ProgressiviIva progressiviIva, StampaRegistroIvaRiepilogoIva riga) {
		final String methodName = "popolaTotaleImponibile";
		if(progressiviIva.getTotaleImponibileProvvisorio() != null && progressiviIva.getTotaleImponibileProvvisorio().compareTo(BigDecimal.ZERO) != 0){
			log.debug(methodName, "importo già impostato per aliquota" + riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleImponibileProvvisorio " + progressiviIva.getTotaleImponibileProvvisorio());
			return;
		}
//		BigDecimal oldTotaleImponibile = obtainTotaleImponibile(progressiviIva);
//		progressiviIva.setTotaleImponibileProvvisorio(oldTotaleImponibile.add(riga.getImponibile()));
		progressiviIva.setTotaleImponibileProvvisorio(riga.getProgressivoImponibile());
		log.debug(methodName, riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleImponibileProvvisorio " + progressiviIva.getTotaleImponibileProvvisorio());
	}

	@Override
	protected void popolaTotaleIva(ProgressiviIva progressiviIva, StampaRegistroIvaRiepilogoIva riga) {
		final String methodName = "popolaTotaleIva";
		if(progressiviIva.getTotaleIvaProvvisorio() != null && progressiviIva.getTotaleIvaProvvisorio().compareTo(BigDecimal.ZERO) != 0){
			log.debug(methodName, "importo già impostato per aliquota" + riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleIvaProvvisorio " + progressiviIva.getTotaleIvaProvvisorio());
			return;
		}
		progressiviIva.setTotaleIvaProvvisorio(riga.getProgressivoIva());
		log.debug(methodName, riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleIvaProvvisorio " + progressiviIva.getTotaleIvaProvvisorio());
	}
	
	@Override
	protected void impostaDatiStampaProvvisorioDefinitivo(StampaIva stampaIva, GeneraReportResponse reportResponse) {
		stampaIva.setFlagStampaDefinitivo(Boolean.FALSE);
		stampaIva.setFlagStampaProvvisorio(Boolean.TRUE);
		stampaIva.setUltimaPaginaStampaDefinitiva(null);
		stampaIva.setUltimaPaginaStampaProvvisoria(reportResponse.getNumeroPagineGenerate().intValue() + primaPaginaDaStampare.intValue());
		stampaIva.setUltimoNumProtocolloDefinitivo(null);
		stampaIva.setUltimoNumProtocolloProvvisorio(ottieniUltimoNumeroProtocolloProvvisorio());
		stampaIva.setUltimaDataProtocolloDefinitiva(null);
		stampaIva.setUltimaDataProtocolloProvvisoria(ottieniUltimaDataProtocolloProvvisorio());
	}

	/**
	 * Aggiorna i progressivi sulla base dati.
	 */
	protected void inserisciProgressiviIvaCorrenti() {
		final String methodName = "inserisciProgressiviIvaCorrenti";
		for(ProgressiviIva pi : cacheProgressiviAttuali.values()) {
			log.debug(methodName, "Inserimento progressivo per registro " + pi.getRegistroIva().getUid() + " e aliquota " + pi.getAliquotaIva().getUid()
					+ " per periodo " + annoEsercizio + "/" + periodo.getCodice() +" totale imponibile provvisorio: " + pi.getTotaleImponibileProvvisorio());
//			progressiviIvaDad.inserisciProgressiviIva(pi);

			// Se ho già un record del progressivo, allora lo aggiorno. Altrimenti ne inserisco uno nuovo
			ProgressiviIva pidb = progressiviIvaDad.findProgressiviIvaByRegistroAndAliquotaIvaAndPeriodoAndAnnoAndEnte(pi.getRegistroIva(), pi.getAliquotaIva(),
					pi.getPeriodo(), pi.getAnnoEsercizio());
			if(pidb == null) {
				log.debug(methodName, "Inserisci");
				progressiviIvaDad.inserisciProgressiviIva(pi);
			} else {
				pidb.setTotaleImponibileProvvisorio(pi.getTotaleImponibileProvvisorio());
				pidb.setTotaleIvaProvvisorio(pi.getTotaleIvaProvvisorio());
				
				progressiviIvaDad.aggiornaProgressiviIva(pidb);
			}
		
			
			//----------------------
			
			
			
			log.debug(methodName, "Inserito progressivo con uid " + pi.getUid());
		}
	}
	
}