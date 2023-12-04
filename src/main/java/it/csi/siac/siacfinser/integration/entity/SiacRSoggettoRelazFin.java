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
 * The persistent class for the siac_r_soggetto_relaz database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_relaz")
public class SiacRSoggettoRelazFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_RELAZ_SOGGETTO_RELAZ_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggetto_relaz_soggetto_relaz_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_RELAZ_SOGGETTO_RELAZ_ID_GENERATOR")
	@Column(name="soggetto_relaz_id")
	private Integer soggettoRelazId;

	//bi-directional many-to-one association to SiacDRelazTipoFin
	@ManyToOne
	@JoinColumn(name="relaz_tipo_id")
	private SiacDRelazTipoFin siacDRelazTipo;

	//bi-directional many-to-one association to SiacDRuoloFin
	@ManyToOne
	@JoinColumn(name="ruolo_id_da")
	private SiacDRuoloFin siacDRuolo1;

	//bi-directional many-to-one association to SiacDRuoloFin
	@ManyToOne
	@JoinColumn(name="ruolo_id_a")
	private SiacDRuoloFin siacDRuolo2;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id_da")
	private SiacTSoggettoFin siacTSoggetto1;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id_a")
	private SiacTSoggettoFin siacTSoggetto2;

	//bi-directional many-to-one association to SiacRSoggettoRelazStatoFin
	@OneToMany(mappedBy="siacRSoggettoRelaz")
	private List<SiacRSoggettoRelazStatoFin> siacRSoggettoRelazStatos;

	//bi-directional many-to-one association to SiacRSoggrelModpagFin
	@OneToMany(mappedBy="siacRSoggettoRelaz")
	private List<SiacRSoggrelModpagFin> siacRSoggrelModpags;

//	//bi-directional many-to-one association to SiacRSoggrelModpagModFin
//	@OneToMany(mappedBy="siacRSoggettoRelaz")
//	private List<SiacRSoggrelModpagModFin> siacRSoggrelModpagMods;

	public SiacRSoggettoRelazFin() {
	}

	public Integer getSoggettoRelazId() {
		return this.soggettoRelazId;
	}

	public void setSoggettoRelazId(Integer soggettoRelazId) {
		this.soggettoRelazId = soggettoRelazId;
	}

	public SiacDRelazTipoFin getSiacDRelazTipo() {
		return this.siacDRelazTipo;
	}

	public void setSiacDRelazTipo(SiacDRelazTipoFin siacDRelazTipo) {
		this.siacDRelazTipo = siacDRelazTipo;
	}

	public SiacDRuoloFin getSiacDRuolo1() {
		return this.siacDRuolo1;
	}

	public void setSiacDRuolo1(SiacDRuoloFin siacDRuolo1) {
		this.siacDRuolo1 = siacDRuolo1;
	}

	public SiacDRuoloFin getSiacDRuolo2() {
		return this.siacDRuolo2;
	}

	public void setSiacDRuolo2(SiacDRuoloFin siacDRuolo2) {
		this.siacDRuolo2 = siacDRuolo2;
	}

	public SiacTSoggettoFin getSiacTSoggetto1() {
		return this.siacTSoggetto1;
	}

	public void setSiacTSoggetto1(SiacTSoggettoFin siacTSoggetto1) {
		this.siacTSoggetto1 = siacTSoggetto1;
	}

	public SiacTSoggettoFin getSiacTSoggetto2() {
		return this.siacTSoggetto2;
	}

	public void setSiacTSoggetto2(SiacTSoggettoFin siacTSoggetto2) {
		this.siacTSoggetto2 = siacTSoggetto2;
	}

	public List<SiacRSoggettoRelazStatoFin> getSiacRSoggettoRelazStatos() {
		return this.siacRSoggettoRelazStatos;
	}

	public void setSiacRSoggettoRelazStatos(List<SiacRSoggettoRelazStatoFin> siacRSoggettoRelazStatos) {
		this.siacRSoggettoRelazStatos = siacRSoggettoRelazStatos;
	}

	public SiacRSoggettoRelazStatoFin addSiacRSoggettoRelazStato(SiacRSoggettoRelazStatoFin siacRSoggettoRelazStato) {
		getSiacRSoggettoRelazStatos().add(siacRSoggettoRelazStato);
		siacRSoggettoRelazStato.setSiacRSoggettoRelaz(this);

		return siacRSoggettoRelazStato;
	}

	public SiacRSoggettoRelazStatoFin removeSiacRSoggettoRelazStato(SiacRSoggettoRelazStatoFin siacRSoggettoRelazStato) {
		getSiacRSoggettoRelazStatos().remove(siacRSoggettoRelazStato);
		siacRSoggettoRelazStato.setSiacRSoggettoRelaz(null);

		return siacRSoggettoRelazStato;
	}

	public List<SiacRSoggrelModpagFin> getSiacRSoggrelModpags() {
		return this.siacRSoggrelModpags;
	}

	public void setSiacRSoggrelModpags(List<SiacRSoggrelModpagFin> siacRSoggrelModpags) {
		this.siacRSoggrelModpags = siacRSoggrelModpags;
	}

	public SiacRSoggrelModpagFin addSiacRSoggrelModpag(SiacRSoggrelModpagFin siacRSoggrelModpag) {
		getSiacRSoggrelModpags().add(siacRSoggrelModpag);
		siacRSoggrelModpag.setSiacRSoggettoRelaz(this);

		return siacRSoggrelModpag;
	}

	public SiacRSoggrelModpagFin removeSiacRSoggrelModpag(SiacRSoggrelModpagFin siacRSoggrelModpag) {
		getSiacRSoggrelModpags().remove(siacRSoggrelModpag);
		siacRSoggrelModpag.setSiacRSoggettoRelaz(null);

		return siacRSoggrelModpag;
	}

//	public List<SiacRSoggrelModpagModFin> getSiacRSoggrelModpagMods() {
//		return this.siacRSoggrelModpagMods;
//	}
//
//	public void setSiacRSoggrelModpagMods(List<SiacRSoggrelModpagModFin> siacRSoggrelModpagMods) {
//		this.siacRSoggrelModpagMods = siacRSoggrelModpagMods;
//	}

//	public SiacRSoggrelModpagModFin addSiacRSoggrelModpagMod(SiacRSoggrelModpagModFin siacRSoggrelModpagMod) {
//		getSiacRSoggrelModpagMods().add(siacRSoggrelModpagMod);
//		siacRSoggrelModpagMod.setSiacRSoggettoRelaz(this);
//
//		return siacRSoggrelModpagMod;
//	}
//
//	public SiacRSoggrelModpagModFin removeSiacRSoggrelModpagMod(SiacRSoggrelModpagModFin siacRSoggrelModpagMod) {
//		getSiacRSoggrelModpagMods().remove(siacRSoggrelModpagMod);
//		siacRSoggrelModpagMod.setSiacRSoggettoRelaz(null);
//
//		return siacRSoggrelModpagMod;
//	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoRelazId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoRelazId = uid;
	}
}