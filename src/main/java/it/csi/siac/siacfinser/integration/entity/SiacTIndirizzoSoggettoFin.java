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
 * The persistent class for the siac_t_indirizzo_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_t_indirizzo_soggetto")
public class SiacTIndirizzoSoggettoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_INDIRIZZO_SOGGETTO_INDIRIZZO_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_indirizzo_soggetto_indirizzo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_INDIRIZZO_SOGGETTO_INDIRIZZO_ID_GENERATOR")
	@Column(name="indirizzo_id")
	private Integer indirizzoId;

	private String avviso;

	private String frazione;

	private String interno;

	@Column(name="numero_civico")
	private String numeroCivico;

	private String principale;

	private String toponimo;

	@Column(name="zip_code")
	private String zipCode;

	//bi-directional many-to-one association to SiacRIndirizzoSoggettoTipoFin
	@OneToMany(mappedBy="siacTIndirizzoSoggetto")
	private List<SiacRIndirizzoSoggettoTipoFin> siacRIndirizzoSoggettoTipos;

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

	public SiacTIndirizzoSoggettoFin() {
	}

	public Integer getIndirizzoId() {
		return this.indirizzoId;
	}

	public void setIndirizzoId(Integer indirizzoId) {
		this.indirizzoId = indirizzoId;
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

	public List<SiacRIndirizzoSoggettoTipoFin> getSiacRIndirizzoSoggettoTipos() {
		return this.siacRIndirizzoSoggettoTipos;
	}

	public void setSiacRIndirizzoSoggettoTipos(List<SiacRIndirizzoSoggettoTipoFin> siacRIndirizzoSoggettoTipos) {
		this.siacRIndirizzoSoggettoTipos = siacRIndirizzoSoggettoTipos;
	}

	public SiacRIndirizzoSoggettoTipoFin addSiacRIndirizzoSoggettoTipo(SiacRIndirizzoSoggettoTipoFin siacRIndirizzoSoggettoTipo) {
		getSiacRIndirizzoSoggettoTipos().add(siacRIndirizzoSoggettoTipo);
		siacRIndirizzoSoggettoTipo.setSiacTIndirizzoSoggetto(this);

		return siacRIndirizzoSoggettoTipo;
	}

	public SiacRIndirizzoSoggettoTipoFin removeSiacRIndirizzoSoggettoTipo(SiacRIndirizzoSoggettoTipoFin siacRIndirizzoSoggettoTipo) {
		getSiacRIndirizzoSoggettoTipos().remove(siacRIndirizzoSoggettoTipo);
		siacRIndirizzoSoggettoTipo.setSiacTIndirizzoSoggetto(null);

		return siacRIndirizzoSoggettoTipo;
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

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.indirizzoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.indirizzoId = uid;
	}
}