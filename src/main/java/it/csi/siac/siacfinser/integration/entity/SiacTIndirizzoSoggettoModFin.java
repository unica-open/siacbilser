/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;




/**
 * The persistent class for the siac_t_indirizzo_soggetto_mod database table.
 * 
 */
@Entity
@Table(name="siac_t_indirizzo_soggetto_mod")
public class SiacTIndirizzoSoggettoModFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_INDIRIZZO_SOGGETTO_MOD_INDIRIZZO_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_indirizzo_soggetto_mod_indirizzo_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_INDIRIZZO_SOGGETTO_MOD_INDIRIZZO_MOD_ID_GENERATOR")
	@Column(name="indirizzo_mod_id")
	private Integer indirizzoModId;

	private String avviso;

	private String frazione;

	private String interno;

	@Column(name="numero_civico")
	private String numeroCivico;

	private String principale;

	private String toponimo;

	@Column(name="zip_code")
	private String zipCode;

	//bi-directional many-to-one association to SiacDViaTipoFin
	@ManyToOne
	@JoinColumn(name="via_tipo_id")
	private SiacDViaTipoFin siacDViaTipo;

	//bi-directional many-to-one association to SiacTComuneFin
	@ManyToOne
	@JoinColumn(name="comune_id")
	private SiacTComuneFin siacTComune;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacTSoggettoModFin
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoModFin siacTSoggettoMod;

	//bi-directional many-to-one association to SiacRIndirizzoSoggettoTipoModFin
	@OneToMany(mappedBy="siacTIndirizzoSoggettoMod")
	private List<SiacRIndirizzoSoggettoTipoModFin> siacRIndirizzoSoggettoTipoMods;

	public SiacTIndirizzoSoggettoModFin() {
	}

	public Integer getIndirizzoModId() {
		return this.indirizzoModId;
	}

	public void setIndirizzoModId(Integer indirizzoModId) {
		this.indirizzoModId = indirizzoModId;
	}

	public String getAvviso() {
		return this.avviso;
	}

	public void setAvviso(String avviso) {
		this.avviso = avviso;
	}

	public String getFrazione() {
		return this.frazione;
	}

	public void setFrazione(String frazione) {
		this.frazione = frazione;
	}

	public String getInterno() {
		return this.interno;
	}

	public void setInterno(String interno) {
		this.interno = interno;
	}

	public String getNumeroCivico() {
		return this.numeroCivico;
	}

	public void setNumeroCivico(String numeroCivico) {
		this.numeroCivico = numeroCivico;
	}

	public String getPrincipale() {
		return this.principale;
	}

	public void setPrincipale(String principale) {
		this.principale = principale;
	}

	public String getToponimo() {
		return this.toponimo;
	}

	public void setToponimo(String toponimo) {
		this.toponimo = toponimo;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public List<SiacRIndirizzoSoggettoTipoModFin> getSiacRIndirizzoSoggettoTipoMods() {
		return this.siacRIndirizzoSoggettoTipoMods;
	}

	public void setSiacRIndirizzoSoggettoTipoMods(List<SiacRIndirizzoSoggettoTipoModFin> siacRIndirizzoSoggettoTipoMods) {
		this.siacRIndirizzoSoggettoTipoMods = siacRIndirizzoSoggettoTipoMods;
	}

	public SiacRIndirizzoSoggettoTipoModFin addSiacRIndirizzoSoggettoTipoMod(SiacRIndirizzoSoggettoTipoModFin siacRIndirizzoSoggettoTipoMod) {
		getSiacRIndirizzoSoggettoTipoMods().add(siacRIndirizzoSoggettoTipoMod);
		siacRIndirizzoSoggettoTipoMod.setSiacTIndirizzoSoggettoMod(this);

		return siacRIndirizzoSoggettoTipoMod;
	}

	public SiacRIndirizzoSoggettoTipoModFin removeSiacRIndirizzoSoggettoTipoMod(SiacRIndirizzoSoggettoTipoModFin siacRIndirizzoSoggettoTipoMod) {
		getSiacRIndirizzoSoggettoTipoMods().remove(siacRIndirizzoSoggettoTipoMod);
		siacRIndirizzoSoggettoTipoMod.setSiacTIndirizzoSoggettoMod(null);

		return siacRIndirizzoSoggettoTipoMod;
	}

	public SiacDViaTipoFin getSiacDViaTipo() {
		return this.siacDViaTipo;
	}

	public void setSiacDViaTipo(SiacDViaTipoFin siacDViaTipo) {
		this.siacDViaTipo = siacDViaTipo;
	}

	public SiacTComuneFin getSiacTComune() {
		return this.siacTComune;
	}

	public void setSiacTComune(SiacTComuneFin siacTComune) {
		this.siacTComune = siacTComune;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public SiacTSoggettoModFin getSiacTSoggettoMod() {
		return this.siacTSoggettoMod;
	}

	public void setSiacTSoggettoMod(SiacTSoggettoModFin siacTSoggettoMod) {
		this.siacTSoggettoMod = siacTSoggettoMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.indirizzoModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.indirizzoModId = uid;
	}
}