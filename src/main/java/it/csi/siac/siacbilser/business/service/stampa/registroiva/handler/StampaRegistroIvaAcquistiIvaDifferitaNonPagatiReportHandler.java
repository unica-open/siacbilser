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
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRegistroIvaAcquistiIvaDifferitaNonPagatiReportHandler extends StampaRegistroIvaAcquistiReportHandler {
	
	@Override
	protected void ottieniListaSubdocumentoIvaSpesa() {
		
		listaSubdocumentoIvaSpesa = ottieniListaSubdocumentoIvaSpesaNelPeriodo(getPeriodo());
		
	}
	
	@Override
	protected List<SubdocumentoIvaSpesa> ottieniListaSubdocumentoIvaSpesaNelPeriodo(Periodo p) {
		final String methodName = "ottieniListaSubdocumentoIvaSpesaNelPeriodo";
		if(p == null){
			log.debug(methodName, "Il periodo di cui si stanno cercando i documenti di entrata risulta essere null. ");
			return new ArrayList<SubdocumentoIvaSpesa>();
		}
		log.debug(methodName, "sto elaborando il periodo: " + p.getDescrizione());
		SubdocumentoIvaSpesa subdocIva = new SubdocumentoIvaSpesa();
		// SIAC-4609
//		subdocIva.setAnnoEsercizio(getAnnoEsercizio());
		subdocIva.setEnte(getEnte());
		subdocIva.setRegistroIva(getRegistroIva());
//		CR-3661 (laura moscatelli) si chiama 'non pagate' ma devono essere visulizzate tutte, quindi anche quelle in stato provvisorio_definitivo
//		subdocIva.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
		//inizio periodo
		Date protocolloProvvisorioDa = p.getInizioPeriodo(getBilancio().getAnno()); 
		//fine periodo
		Date protocolloProvvisorioA = p.getFinePeriodo(getBilancio().getAnno()); 
		
		log.debug(methodName, "Ricerca di dettaglio per i subdociva: " + subdocIva.getAnnoEsercizio() + "/" + subdocIva.getStatoSubdocumentoIva() +
				", " + protocolloProvvisorioDa + " --- " + protocolloProvvisorioA);
		return subdocumentoIvaSpesaDad.ricercaDettaglioSubdocumentoIvaSpesaNonQPID(subdocIva,protocolloProvvisorioDa, protocolloProvvisorioA, null, null);
	}

	@Override
	protected void sortSezione1(StampaRegistroIvaDatiIva sezione1) {
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
//		BigDecimal oldTotaleIva = obtainTotaleIva(progressiviIva);
//		progressiviIva.setTotaleIvaProvvisorio(oldTotaleIva.add(riga.getIva()));
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
	@Override
	protected void inserisciProgressiviIvaCorrenti() {
		final String methodName = "inserisciProgressiviIvaCorrenti";
		for(ProgressiviIva pi : cacheProgressiviAttuali.values()) {
			log.debug(methodName, "Inserimento progressivo per registro " + pi.getRegistroIva().getUid() + " e aliquota " + pi.getAliquotaIva().getUid()
					+ " per periodo " + annoEsercizio + "/" + periodo.getCodice());
			log.debug(methodName, "totale imponibile provvisorio: " + pi.getTotaleImponibileProvvisorio());
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