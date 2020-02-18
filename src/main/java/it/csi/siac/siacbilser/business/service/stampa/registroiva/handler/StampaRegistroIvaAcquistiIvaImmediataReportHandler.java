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
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRegistroIvaAcquistiIvaImmediataReportHandler extends StampaRegistroIvaAcquistiReportHandler {
	
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
		
		SubdocumentoIvaSpesa subdocIva = new SubdocumentoIvaSpesa();
		// SIAC-4609
//		subdocIva.setAnnoEsercizio(getAnnoEsercizio());
		subdocIva.setEnte(getEnte());
		subdocIva.setRegistroIva(getRegistroIva());
		subdocIva.setStatoSubdocumentoIva(StatoSubdocumentoIva.DEFINITIVO);
		//inizio periodo
		Date protocolloDefinitivoDa = p.getInizioPeriodo(getBilancio().getAnno()); 
		//fine periodo
		Date protocolloDefinitivoA = p.getFinePeriodo(getBilancio().getAnno()); 
		
		return subdocumentoIvaSpesaDad.ricercaDettaglioSubdocumentoIvaSpesaNonQPID(subdocIva, null, null, protocolloDefinitivoDa, protocolloDefinitivoA);
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
//		if(progressiviIva.getTotaleImponibileDefinitivo() != null && progressiviIva.getTotaleImponibileDefinitivo().compareTo(BigDecimal.ZERO) != 0){
//			log.debug(methodName, "importo già impostato per aliquota" + riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleImponibileDefinitivo " + progressiviIva.getTotaleImponibileDefinitivo());
//			return;
//		}
//		progressiviIva.setTotaleImponibileDefinitivo(riga.getProgressivoImponibile());
//		log.debug(methodName, riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleImponibileDefinitivo " + progressiviIva.getTotaleImponibileDefinitivo());
//	}
//
//	@Override
//	protected void popolaTotaleIva(ProgressiviIva progressiviIva, StampaRegistroIvaRiepilogoIva riga) {
//		final String methodName = "popolaTotaleIva";
//		if(progressiviIva.getTotaleIvaDefinitivo() != null && progressiviIva.getTotaleIvaDefinitivo().compareTo(BigDecimal.ZERO) != 0){
//			log.debug(methodName, "importo già impostato per aliquota" + riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleIvaDefinitivo " + progressiviIva.getTotaleIvaDefinitivo());
//			return;
//		}
//		progressiviIva.setTotaleIvaDefinitivo(riga.getProgressivoIva());
//		log.debug(methodName, riga.getAliquotaSubdocumentoIva().getAliquotaIva().getCodice() + " - TotaleIvaDefinitivo " + progressiviIva.getTotaleIvaDefinitivo());
//	}



}