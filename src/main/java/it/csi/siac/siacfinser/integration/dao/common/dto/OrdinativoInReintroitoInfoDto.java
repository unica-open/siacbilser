/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacfinser.ReintroitoUtils;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

/**
 * Contiene i dati appena caricati a partire da quelli richiesti in input al servizio.
 * 
 * Se i dati non sono accettabili riporta gli errori.
 * 
 * @author claudio.picco
 *
 */
public class OrdinativoInReintroitoInfoDto extends EsitoControlliDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean utilizzaProvvedimentoDaMovimenti;
	private AttoAmministrativo attoAmministrativo;
	
	private OrdinativoPagamento ordinativoPagamento;
	private ImpegnoPerReintroitoInfoDto impegnoDestinazione;
	
	private List<RitenutaSpiltPerReintroitoInfoDto> listaRitenuteSplit;
	
	private List<AccertamentoModAutomaticaPerReintroitoInfoDto> modificheAutomaticheNecessarie;
	
	private ScrittureGenPerReintroitoOrdinativoInfoDto scrittureGen;
	
	public List<RitenuteReintroitoConStessoMovimentoDto> raggruppatePerAccertamenti(){
		return ReintroitoUtils.raggruppaConStessoAccertamento(this.listaRitenuteSplit);
	}
	
	public List<RitenuteReintroitoConStessoMovimentoDto> raggruppatePerImpegni(){
		return ReintroitoUtils.raggruppaConStessoImpegno(this.listaRitenuteSplit);
	}
	
	public boolean presenzaRitenute(){
		return !StringUtilsFin.isEmpty(this.listaRitenuteSplit);
	}

	public List<AccertamentoModAutomaticaPerReintroitoInfoDto> getModificheAutomaticheNecessarie() {
		return modificheAutomaticheNecessarie;
	}

	public void setModificheAutomaticheNecessarie(
			List<AccertamentoModAutomaticaPerReintroitoInfoDto> modificheAutomaticheNecessarie) {
		this.modificheAutomaticheNecessarie = modificheAutomaticheNecessarie;
	}

	public OrdinativoPagamento getOrdinativoPagamento() {
		return ordinativoPagamento;
	}

	public void setOrdinativoPagamento(OrdinativoPagamento ordinativoPagamento) {
		this.ordinativoPagamento = ordinativoPagamento;
	}

	public ImpegnoPerReintroitoInfoDto getImpegnoDestinazione() {
		return impegnoDestinazione;
	}

	public void setImpegnoDestinazione(ImpegnoPerReintroitoInfoDto impegnoDestinazione) {
		this.impegnoDestinazione = impegnoDestinazione;
	}

	public List<RitenutaSpiltPerReintroitoInfoDto> getListaRitenuteSplit() {
		return listaRitenuteSplit;
	}

	public void setListaRitenuteSplit(List<RitenutaSpiltPerReintroitoInfoDto> listaRitenuteSplit) {
		this.listaRitenuteSplit = listaRitenuteSplit;
	}

	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}

	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}

	public boolean isUtilizzaProvvedimentoDaMovimenti() {
		return utilizzaProvvedimentoDaMovimenti;
	}

	public void setUtilizzaProvvedimentoDaMovimenti(boolean utilizzaProvvedimentoDaMovimenti) {
		this.utilizzaProvvedimentoDaMovimenti = utilizzaProvvedimentoDaMovimenti;
	}

	public ScrittureGenPerReintroitoOrdinativoInfoDto getScrittureGen() {
		return scrittureGen;
	}

	public void setScrittureGen(ScrittureGenPerReintroitoOrdinativoInfoDto scrittureGen) {
		this.scrittureGen = scrittureGen;
	}
	
}
