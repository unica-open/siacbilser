/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacbilser.integration.entity.SiacRGestioneEnte;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;




/**
 * The persistent class for the siac_t_ente_proprietario database table.
 * 
 */
@Entity
@Table(name="siac_t_ente_proprietario")
public class SiacTEnteProprietarioFin extends SiacTBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ente_proprietario_id")
	private Integer enteProprietarioId;

	@Column(name="codice_fiscale")
	private String codiceFiscale;

	@Column(name="ente_denominazione")
	private String enteDenominazione;
	
	//bi-directional many-to-one association to SiacRGestioneEnte
	/** The siac r gestione entes. */
	@OneToMany(mappedBy="siacTEnteProprietario")
	private List<SiacRGestioneEnte> siacRGestioneEntes;

	public SiacTEnteProprietarioFin() {
	}

	public SiacTEnteProprietarioFin(int uid) {
		this();
		setUid(uid);
	}

	public Integer getEnteProprietarioId() {
		return this.enteProprietarioId;
	}

	public void setEnteProprietarioId(Integer enteProprietarioId) {
		this.enteProprietarioId = enteProprietarioId;
	}

	public String getCodiceFiscale() {
		return this.codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getEnteDenominazione() {
		return this.enteDenominazione;
	}

	public void setEnteDenominazione(String enteDenominazione) {
		this.enteDenominazione = enteDenominazione;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.enteProprietarioId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.enteProprietarioId = uid;
	}

	public List<SiacRGestioneEnte> getSiacRGestioneEntes() {
		return siacRGestioneEntes;
	}

	public void setSiacRGestioneEntes(List<SiacRGestioneEnte> siacRGestioneEntes) {
		this.siacRGestioneEntes = siacRGestioneEntes;
	}
}