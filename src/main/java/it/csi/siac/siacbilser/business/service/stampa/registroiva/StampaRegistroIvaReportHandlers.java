/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.business.service.stampa.registroiva.handler.StampaRegistroIvaAcquistiIvaDifferitaNonPagatiReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.handler.StampaRegistroIvaAcquistiIvaDifferitaPagatiReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.handler.StampaRegistroIvaAcquistiIvaImmediataReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.handler.StampaRegistroIvaCorrispettiviReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.handler.StampaRegistroIvaVenditeIvaDifferitaIncassatiReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.handler.StampaRegistroIvaVenditeIvaDifferitaNonIncassatiReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.handler.StampaRegistroIvaVenditeIvaImmediataReportHandler;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

public enum StampaRegistroIvaReportHandlers {
	
	ACQUISTI_IVA_IMMEDIATA("RegistroIvaAcquistiIvaImmediata", StampaRegistroIvaAcquistiIvaImmediataReportHandler.class, TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA, null, null),
	ACQUISTI_IVA_DIFFERITA_PAGATI("RegistroIvaAcquistiIvaDifferitaPagati", StampaRegistroIvaAcquistiIvaDifferitaPagatiReportHandler.class, TipoRegistroIva.ACQUISTI_IVA_DIFFERITA, Boolean.TRUE, null),
	ACQUISTI_IVA_DIFFERITA_NON_PAGATI("RegistroIvaAcquistiIvaDifferitaNonPagati", StampaRegistroIvaAcquistiIvaDifferitaNonPagatiReportHandler.class, TipoRegistroIva.ACQUISTI_IVA_DIFFERITA, Boolean.FALSE, null),
	VENDITE_IVA_IMMEDIATA("RegistroIvaVenditeIvaImmediata", StampaRegistroIvaVenditeIvaImmediataReportHandler.class, TipoRegistroIva.VENDITE_IVA_IMMEDIATA, null, null),
	VENDITE_IVA_DIFFERITA_INCASSATI("RegistroIvaVenditeIvaDifferitaIncassati",  StampaRegistroIvaVenditeIvaDifferitaIncassatiReportHandler.class, TipoRegistroIva.VENDITE_IVA_DIFFERITA, null, Boolean.TRUE),
	VENDITE_IVA_DIFFERITA_NON_INCASSATI("RegistroIvaVenditeIvaDifferitaNonIncassati", StampaRegistroIvaVenditeIvaDifferitaNonIncassatiReportHandler.class, TipoRegistroIva.VENDITE_IVA_DIFFERITA, null, Boolean.FALSE),
	CORRISPETTIVI("RegistroIvaCorrispettivi", StampaRegistroIvaCorrispettiviReportHandler.class, TipoRegistroIva.CORRISPETTIVI, null, null),
	;
	
	private String codiceTemplate;
	private TipoRegistroIva tipoRegistroIva;
	private Boolean pagato;
	private Boolean incassato;
	private Class<? extends StampaRegistroIvaReportHandler> reportHandlerClass;
	
	/** Costruttore */
	private StampaRegistroIvaReportHandlers(String codiceTemplate, Class<? extends StampaRegistroIvaReportHandler> reportHandlerClass, 
			TipoRegistroIva tipoRegistroIva, Boolean pagato, Boolean incassato) {
		this.codiceTemplate = codiceTemplate;
		this.reportHandlerClass = reportHandlerClass;
		this.tipoRegistroIva = tipoRegistroIva;
		this.pagato = pagato;
		this.incassato = incassato;
	}

	/**
	 * @return the codiceTemplate
	 */
	public String getCodiceTemplate() {
		return codiceTemplate;
	}



	/**
	 * @return the tipoRegistroIva
	 */
	public TipoRegistroIva getTipoRegistroIva() {
		return tipoRegistroIva;
	}

	/**
	 * @return the pagato
	 */
	public Boolean getPagato() {
		return pagato;
	}

	/**
	 * @return the incassato
	 */
	public Boolean getIncassato() {
		return incassato;
	}

	/**
	 * @return the modelCompiler
	 */
	@SuppressWarnings("unchecked")
	public Class<StampaRegistroIvaReportHandler> getReportHandlerClass() {
		return (Class<StampaRegistroIvaReportHandler>) reportHandlerClass;
	}	
	
	
	/**
	 * Ottiene il valore dell'enum corrispondente al tipoRegistroIva e al flag pagato.
	 * 
	 * @param tipoRegistroIva il tipo rispetto cui trovare il mapping
	 * @param pagato          se il mapping si riferisca al pagato
	 * @param pagato          se il mapping si riferisca all'incassato
	 * 
	 * @return il valore dell'enum corrispondente ai dati in input
	 * 
	 * @throws IllegalArgumentException nel caso non esista un mapping corrispondente agli argomenti forniti in input
	 */
	public static StampaRegistroIvaReportHandlers fromTipoRegistroIvaAndPagatoAndIncassato(TipoRegistroIva tipoRegistroIva, Boolean pagato, Boolean incassato) {
		List<StampaRegistroIvaReportHandlers> tmcList = new ArrayList<StampaRegistroIvaReportHandlers>();
		for(StampaRegistroIvaReportHandlers tmc : values()) {
			if(tmc.getTipoRegistroIva() == tipoRegistroIva) {
				tmcList.add(tmc);
			}
		}
		
		if(tmcList.isEmpty()) {
			throw new IllegalArgumentException("Nessun mapping corrispondente a " + tipoRegistroIva.name() + " in StampaRegistroIvaReportHandlers");
		}
		
		for(StampaRegistroIvaReportHandlers tmc : tmcList) {
			if(tmc.getIncassato() == null && tmc.getPagato() == null) {
				return tmc;
			}
			if(pagato != null && tmc.getPagato() != null && pagato.equals(tmc.getPagato())) {
				return tmc;
			}
			if(incassato != null && tmc.getIncassato() != null && incassato.equals(tmc.getIncassato())) {
				return tmc;
			}
		}
		
		throw new IllegalArgumentException("Nessun mapping corrispondente a " + tipoRegistroIva.name() + " con flag pagato pari a " +
				(pagato == null ? "null" : pagato.toString()) + " in StampaRegistroIvaReportHandlers");
	}
	
	public static StampaRegistroIvaReportHandlers byReportHandlerClass(Class<? extends StampaRegistroIvaReportHandler> clazz) {
		for(StampaRegistroIvaReportHandlers srirh : values()) {
			if(srirh.getReportHandlerClass().equals(clazz)){
				return srirh;				
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping corrispondete per la classe: "+clazz.getName());
	}
	
}
