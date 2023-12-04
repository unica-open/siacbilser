/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa.factory;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiPagamentoType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DettaglioPagamentoType;
import it.csi.siac.sirfelser.model.DettaglioPagamentoFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.ModalitaPagamentoFEL;
import it.csi.siac.sirfelser.model.PagamentoFEL;


public final class InvioFatturaPAPagamentoFELFactory {

	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private InvioFatturaPAPagamentoFELFactory() {
		// Previene l'instanziazione
	}
	
	public static PagamentoFEL init(FatturaFEL fatturaFEL, Integer progressivo, DatiPagamentoType datiPagamentoType, Ente ente) {
		PagamentoFEL pagamentoFEL = new PagamentoFEL();
		pagamentoFEL.setFattura(fatturaFEL);
		pagamentoFEL.setEnte(ente);
		
		pagamentoFEL.setProgressivo(progressivo);
		if(datiPagamentoType == null) {
			return pagamentoFEL;
		}
		
		setCondizioniPagamento(pagamentoFEL, datiPagamentoType);
		
		return pagamentoFEL;
	}

	private static void setCondizioniPagamento(PagamentoFEL pagamentoFEL, DatiPagamentoType datiPagamentoType) {
		if(datiPagamentoType.getCondizioniPagamento() != null) {
			pagamentoFEL.setCondizioniPagamento(datiPagamentoType.getCondizioniPagamento().value());
		}
	}

	public static DettaglioPagamentoFEL initDettaglio(PagamentoFEL pagamentoFEL, Integer progressivoDettaglio, DettaglioPagamentoType datiDettaglioPagamento, Ente ente) {
		DettaglioPagamentoFEL dettaglioPagamentoFEL = new DettaglioPagamentoFEL();
		dettaglioPagamentoFEL.setProgressivoDettaglio(progressivoDettaglio);
		dettaglioPagamentoFEL.setPagamento(pagamentoFEL);
		dettaglioPagamentoFEL.setEnte(ente);
		
		if(datiDettaglioPagamento == null) {
			return dettaglioPagamentoFEL;
		}
		
		dettaglioPagamentoFEL.setBeneficiario(datiDettaglioPagamento.getBeneficiario());
		dettaglioPagamentoFEL.setGiorniTerminiPagamento(datiDettaglioPagamento.getGiorniTerminiPagamento());
		dettaglioPagamentoFEL.setImportoPagamento(datiDettaglioPagamento.getImportoPagamento());
		dettaglioPagamentoFEL.setCodiceUfficioPostale(datiDettaglioPagamento.getCodUfficioPostale());
		dettaglioPagamentoFEL.setCognomeQuietanzante(datiDettaglioPagamento.getCognomeQuietanzante());
		dettaglioPagamentoFEL.setNomeQuietanzante(datiDettaglioPagamento.getNomeQuietanzante());
		dettaglioPagamentoFEL.setCfQuietanzante(datiDettaglioPagamento.getCFQuietanzante());
		dettaglioPagamentoFEL.setTitoloQuietanzante(datiDettaglioPagamento.getTitoloQuietanzante());
		dettaglioPagamentoFEL.setIstitutoFinanziario(datiDettaglioPagamento.getIstitutoFinanziario());
		dettaglioPagamentoFEL.setIban(datiDettaglioPagamento.getIBAN());
		dettaglioPagamentoFEL.setAbi(datiDettaglioPagamento.getABI());
		dettaglioPagamentoFEL.setCab(datiDettaglioPagamento.getCAB());
		dettaglioPagamentoFEL.setBic(datiDettaglioPagamento.getBIC());
		dettaglioPagamentoFEL.setScontoPagamentoAnticipato(datiDettaglioPagamento.getScontoPagamentoAnticipato());
		dettaglioPagamentoFEL.setPenalitaPagamentiRitardati(datiDettaglioPagamento.getPenalitaPagamentiRitardati());
		dettaglioPagamentoFEL.setCodicePagamento(datiDettaglioPagamento.getCodicePagamento());
		
		setModalitaPagamentoFEL(dettaglioPagamentoFEL, datiDettaglioPagamento);
		setDataRifTerminiPagamento(dettaglioPagamentoFEL, datiDettaglioPagamento);
		setDataScadenzaPagamento(dettaglioPagamentoFEL, datiDettaglioPagamento);
		setDataLimitePagamentoAnt(dettaglioPagamentoFEL, datiDettaglioPagamento);
		setDataDecorrenzaPenale(dettaglioPagamentoFEL, datiDettaglioPagamento);
		
		return dettaglioPagamentoFEL;
	
	}

	private static void setModalitaPagamentoFEL(DettaglioPagamentoFEL dettaglioPagamentoFEL, DettaglioPagamentoType dettaglioPagamentoType) {
		if(dettaglioPagamentoType.getModalitaPagamento() != null) {
			dettaglioPagamentoFEL.setModalitaPagamentoFEL(ModalitaPagamentoFEL.byCodice(dettaglioPagamentoType.getModalitaPagamento().value()));
		}
	}
	
	private static void setDataRifTerminiPagamento(DettaglioPagamentoFEL dettaglioPagamentoFEL, DettaglioPagamentoType datiDettaglioPagamento) {
		if(datiDettaglioPagamento.getDataRiferimentoTerminiPagamento() != null) {
			dettaglioPagamentoFEL.setDataRifTerminiPagamento(datiDettaglioPagamento.getDataRiferimentoTerminiPagamento().toGregorianCalendar().getTime());
		}
	}
	
	private static void setDataScadenzaPagamento(DettaglioPagamentoFEL dettaglioPagamentoFEL, DettaglioPagamentoType dettaglioPagamentoType) {
		if(dettaglioPagamentoType.getDataScadenzaPagamento() != null) {
			dettaglioPagamentoFEL.setDataScadenzaPagamento(dettaglioPagamentoType.getDataScadenzaPagamento().toGregorianCalendar().getTime());
		}
	}
	
	private static void setDataLimitePagamentoAnt(DettaglioPagamentoFEL dettaglioPagamentoFEL, DettaglioPagamentoType dettaglioPagamentoType) {
		if(dettaglioPagamentoType.getDataLimitePagamentoAnticipato() != null) {
			dettaglioPagamentoFEL.setDataLimitePagamentoAnt(dettaglioPagamentoType.getDataLimitePagamentoAnticipato().toGregorianCalendar().getTime());
		}
	}
	
	private static void setDataDecorrenzaPenale(DettaglioPagamentoFEL dettaglioPagamentoFEL, DettaglioPagamentoType dettaglioPagamentoType) {
		if(dettaglioPagamentoType.getDataDecorrenzaPenale() != null) {
			dettaglioPagamentoFEL.setDataDecorrenzaPenale(dettaglioPagamentoType.getDataDecorrenzaPenale().toGregorianCalendar().getTime());
		}
	}
	
}
