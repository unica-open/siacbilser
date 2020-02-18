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
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaReportModel;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistroIvaReportModelFactory;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRegistroIvaAcquistiIvaDifferitaPagatiReportHandler extends StampaRegistroIvaAcquistiReportHandler {
	
	@Override
	protected void ottieniListaSubdocumentoIvaSpesa() {
		listaSubdocumentoIvaSpesa = ottieniListaSubdocumentoIvaSpesaNelPeriodo(periodo);
	}
	
	@Override
	protected List<SubdocumentoIvaSpesa> ottieniListaSubdocumentoIvaSpesaNelPeriodo(Periodo p) {
		final String methodName = "ottieniListaSubdocumentoIvaSpesaNelPeriodo";
		List<SubdocumentoIvaSpesa> listaSubdocumenti = new ArrayList<SubdocumentoIvaSpesa>();
		if( p ==null){
			log.debug(methodName, "Il periodo di cui si stanno cercando i documenti di entrata risulta essere null. ");
			return listaSubdocumenti;
		}
		
		 //inizio periodo
		Date inizioPeriodo = p.getInizioPeriodo(getBilancio().getAnno());
		//fine periodo
		Date finePeriodo = p.getFinePeriodo(getBilancio().getAnno()); 
		
		listaSubdocumenti = subdocumentoIvaSpesaDad.ricercaDettaglioSubdocumentoIvaSpesaPerDifferitaPagati(registroIva, inizioPeriodo, finePeriodo);
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
	@Override
	protected void preGeneraReport() {
		final String methodName = "preGeneraReport";
		StampaRegistroIvaReportModel elaboratedResult = StampaRegistroIvaReportModelFactory.obtainDatiMinimal(result);
		log.info(methodName, "il file per la creazione dell'xml e' stato alleggerito");
		this.result = elaboratedResult;
	}
	



	
}