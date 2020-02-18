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
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaLiquidazioneIvaVenditeIvaImmediataDataCompiler extends StampaLiquidazioneIvaBaseVenditeDataCompiler {
	
	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;
	
	@Override
	protected void createAndSetPage() {
		final String methodName = "createAndSetPage";
		pagina = new StampaLiquidazioneIvaDatiERiepilogo();
		result.setVenditeIvaImmediata(pagina);
		
		log.debug(methodName, "Pagina creata per la VenditeIvaImmediata");
	}
	
	@Override
	protected void ottieniListaSubdocumentoIva() {
		final String methodName = "ottieniListaSubdocumentoIva";
		
		listaSubdocumentoIva = new ArrayList<SubdocumentoIvaEntrata>();
		for(RegistroIva registroIva : listaRegistroIva) {
			if(!getTipoRegistroIvaPerFiltro().equals(registroIva.getTipoRegistroIva())) {
				continue;
			}
			SubdocumentoIvaEntrata sie = new SubdocumentoIvaEntrata();
			// SIAC-4609
//			sie.setAnnoEsercizio(handler.getAnnoEsercizio());
			sie.setEnte(handler.getEnte());
			sie.setRegistroIva(registroIva);
			sie.setStatoSubdocumentoIva(StatoSubdocumentoIva.DEFINITIVO);
			// SIAC-6120: corretto l'anno di esercizio, letto dall'handler e non dal subdocumento iva
			//inizio periodo
			Date protocolloDefinitivoDa = handler.getPeriodo().getInizioPeriodo(handler.getAnnoEsercizio()); 
			//fine periodo
			Date protocolloDefinitivoA = handler.getPeriodo().getFinePeriodo(handler.getAnnoEsercizio());
			
			List<SubdocumentoIvaEntrata> list = subdocumentoIvaEntrataDad.ricercaDettaglioSubdocumentoIvaEntrataNonQPID(sie, null, null, protocolloDefinitivoDa, protocolloDefinitivoA);
			log.debug(methodName, "RegistroIva.uid = " + registroIva.getUid() + ", subdocumentiIva.size= " + list.size());
			listaSubdocumentoIva.addAll(list);
		}
	}
	
	@Override
	protected BigDecimal obtainImpostaFromProgressiviIvaAndAliquotaIva(ProgressiviIva progressiviIva, AliquotaIva aliquotaIva) {
		return progressiviIva.getTotaleIvaDefinitivo();
	}
	
	/**
	 * Ottiene il tipo di registro Iva per filtrare i registri.
	 * 
	 * @return il tipo per il filtro
	 */
	protected TipoRegistroIva getTipoRegistroIvaPerFiltro() {
		return TipoRegistroIva.VENDITE_IVA_IMMEDIATA;
	}
	
}
