/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.compiler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaDatiERiepilogo;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaLiquidazioneIvaAcquistiIvaImmediataDataCompiler extends StampaLiquidazioneIvaBaseAcquistiDataCompiler {
	
	@Override
	protected void createAndSetPage() {
		final String methodName = "createAndSetPage";
		pagina = new StampaLiquidazioneIvaDatiERiepilogo();
		result.setAcquistiIvaImmediata(pagina);
		
		log.debug(methodName, "Pagina creata per gli AcquistiIvaImmediata");
	}
	
	@Override
	protected void ottieniListaSubdocumentoIva() {
		final String methodName = "ottieniListaSubdocumentoIva";
		
		listaSubdocumentoIva = new ArrayList<SubdocumentoIvaSpesa>();
		for(RegistroIva registroIva : listaRegistroIva) {
			if(!TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA.equals(registroIva.getTipoRegistroIva())) {
				continue;
			}
			SubdocumentoIvaSpesa sis = new SubdocumentoIvaSpesa();
			// SIAC-4609
//			sis.setAnnoEsercizio(handler.getAnnoEsercizio());
			sis.setEnte(handler.getEnte());
			sis.setRegistroIva(registroIva);
			sis.setStatoSubdocumentoIva(StatoSubdocumentoIva.DEFINITIVO);
			
			List<SubdocumentoIvaSpesa> list = subdocumentoIvaSpesaDad.ricercaDettaglioSubdocumentoIvaSpesaNonQPID(sis, null, null,
					handler.getPeriodo().getInizioPeriodo(handler.getAnnoEsercizio()), handler.getPeriodo().getFinePeriodo(handler.getAnnoEsercizio()));
					
			log.debug(methodName, "RegistroIva.uid = " + registroIva.getUid() + ", subdocumentiIva.size= " + list.size());
			listaSubdocumentoIva.addAll(list);
		}
	}
	
	@Override
	protected BigDecimal obtainImpostaFromProgressiviIvaAndAliquotaIva(ProgressiviIva progressiviIva, AliquotaIva aliquotaIva) {
		final BigDecimal totaleIndetraibilita = progressiviIva.getTotaleIvaDefinitivo().multiply(aliquotaIva.getPercentualeIndetraibilita()).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED);
		return progressiviIva.getTotaleIvaDefinitivo().subtract(totaleIndetraibilita);
	}
	
}
