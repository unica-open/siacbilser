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

import it.csi.siac.siacfinser.integration.entity.base.SiacLoginMultiplo;







/**
 * The persistent class for the siac_r_soggrel_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_soggrel_modpag")
public class SiacRSoggrelModpagFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SOGGREL_MODPAG_SOGGRELMPAG_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggrel_modpag_soggrelmpag_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGREL_MODPAG_SOGGRELMPAG_ID_GENERATOR")
	@Column(name="soggrelmpag_id")
	private Integer soggrelmpagId;

	//bi-directional many-to-one association to SiacRSoggettoRelazFin
	@ManyToOne
	@JoinColumn(name="soggetto_relaz_id")
	private SiacRSoggettoRelazFin siacRSoggettoRelaz;

	//bi-directional many-to-one association to SiacTModpagFin
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpagFin siacTModpag;
	
	
	//bi-directional many-to-one association to SiacRSoggrelModpagModFin
	@OneToMany(mappedBy="siacRSoggrelModpag")
	private List<SiacRSoggrelModpagModFin> siacRSoggrelModpagMods;
	
	//bi-directional many-to-one association to SiacRModpagOrdineFin
	@OneToMany(mappedBy="siacRSoggrelModpag")
	private List<SiacRModpagOrdineFin> siacRModpagOrdines;

	private String note;
	
	public SiacRSoggrelModpagFin() {
	}

	public Integer getSoggrelmpagId() {
		return this.soggrelmpagId;
	}

	public void setSoggrelmpagId(Integer soggrelmpagId) {
		this.soggrelmpagId = soggrelmpagId;
	}

	public SiacRSoggettoRelazFin getSiacRSoggettoRelaz() {
		return this.siacRSoggettoRelaz;
	}

	public void setSiacRSoggettoRelaz(SiacRSoggettoRelazFin siacRSoggettoRelaz) {
		this.siacRSoggettoRelaz = siacRSoggettoRelaz;
	}

	public SiacTModpagFin getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpagFin siacTModpag) {
		this.siacTModpag = siacTModpag;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public Integer getUid() {
		//  Auto-generated method stub
		return this.soggrelmpagId;
	}

	@Override
	public void setUid(Integer uid) {
		//  Auto-generated method stub
		this.soggrelmpagId = uid;
	}
	
	public List<SiacRSoggrelModpagModFin> getSiacRSoggrelModpagMods() {
		return this.siacRSoggrelModpagMods;
	}

	public void setSiacRSoggrelModpagMods(List<SiacRSoggrelModpagModFin> siacRSoggrelModpagMods) {
		this.siacRSoggrelModpagMods = siacRSoggrelModpagMods;
	}

	public SiacRSoggrelModpagModFin addSiacRSoggrelModpagMod(SiacRSoggrelModpagModFin siacRSoggrelModpagMod) {
		getSiacRSoggrelModpagMods().add(siacRSoggrelModpagMod);
		siacRSoggrelModpagMod.setSiacRSoggrelModpag(this);

		return siacRSoggrelModpagMod;
	}

	public SiacRSoggrelModpagModFin removeSiacRSoggrelModpagMod(SiacRSoggrelModpagModFin siacRSoggrelModpagMod) {
		getSiacRSoggrelModpagMods().remove(siacRSoggrelModpagMod);
		siacRSoggrelModpagMod.setSiacRSoggrelModpag(null);

		return siacRSoggrelModpagMod;
	}
	
	public List<SiacRModpagOrdineFin> getSiacRModpagOrdines() {
		return this.siacRModpagOrdines;
	}

	public void setSiacRModpagOrdines(List<SiacRModpagOrdineFin> siacRModpagOrdines) {
		this.siacRModpagOrdines = siacRModpagOrdines;
	}

	public SiacRModpagOrdineFin addSiacRModpagOrdine(SiacRModpagOrdineFin siacRModpagOrdine) {
		getSiacRModpagOrdines().add(siacRModpagOrdine);
		siacRModpagOrdine.setSiacRSoggrelModpag(this);

		return siacRModpagOrdine;
	}

	public SiacRModpagOrdineFin removeSiacRModpagOrdine(SiacRModpagOrdineFin siacRModpagOrdine) {
		getSiacRModpagOrdines().remove(siacRModpagOrdine);
		siacRModpagOrdine.setSiacRSoggrelModpag(null);

		return siacRModpagOrdine;
	}
}