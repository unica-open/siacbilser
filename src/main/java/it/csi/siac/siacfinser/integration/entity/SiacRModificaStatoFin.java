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
 * The persistent class for the siac_r_modifica_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_modifica_stato")
public class SiacRModificaStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MODIFICA_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_modifica_stato_mod_stato_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MODIFICA_STATO_ID_GENERATOR")
	@Column(name="mod_stato_r_id")
	private Integer modStatoRId;

	//bi-directional many-to-one association to SiacDModificaStatoFin
	@ManyToOne
	@JoinColumn(name="mod_stato_id")
	private SiacDModificaStatoFin siacDModificaStato;

	//bi-directional many-to-one association to SiacTModificaFin
	@ManyToOne
	@JoinColumn(name="mod_id")
	private SiacTModificaFin siacTModifica;

	//bi-directional many-to-one association to SiacRMovgestTsSogModFin
	@OneToMany(mappedBy="siacRModificaStato")
	private List<SiacRMovgestTsSogModFin> siacRMovgestTsSogMods;

	//bi-directional many-to-one association to SiacRMovgestTsSogclasseModFin
	@OneToMany(mappedBy="siacRModificaStato")
	private List<SiacRMovgestTsSogclasseModFin> siacRMovgestTsSogclasseMods;

	//bi-directional many-to-one association to SiacTMovgestTsDetModFin
	@OneToMany(mappedBy="siacRModificaStato")
	private List<SiacTMovgestTsDetModFin> siacTMovgestTsDetMods;

	public SiacRModificaStatoFin() {
	}

	public Integer getModStatoRId() {
		return this.modStatoRId;
	}

	public void setModStatoRId(Integer modStatoRId) {
		this.modStatoRId = modStatoRId;
	}

	public SiacDModificaStatoFin getSiacDModificaStato() {
		return this.siacDModificaStato;
	}

	public void setSiacDModificaStato(SiacDModificaStatoFin siacDModificaStato) {
		this.siacDModificaStato = siacDModificaStato;
	}

	public SiacTModificaFin getSiacTModifica() {
		return this.siacTModifica;
	}

	public void setSiacTModifica(SiacTModificaFin siacTModifica) {
		this.siacTModifica = siacTModifica;
	}

	public List<SiacRMovgestTsSogModFin> getSiacRMovgestTsSogMods() {
		return this.siacRMovgestTsSogMods;
	}

	public void setSiacRMovgestTsSogMods(List<SiacRMovgestTsSogModFin> siacRMovgestTsSogMods) {
		this.siacRMovgestTsSogMods = siacRMovgestTsSogMods;
	}

	public SiacRMovgestTsSogModFin addSiacRMovgestTsSogMod(SiacRMovgestTsSogModFin siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().add(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacRModificaStato(this);

		return siacRMovgestTsSogMod;
	}

	public SiacRMovgestTsSogModFin removeSiacRMovgestTsSogMod(SiacRMovgestTsSogModFin siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().remove(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacRModificaStato(null);

		return siacRMovgestTsSogMod;
	}

	public List<SiacRMovgestTsSogclasseModFin> getSiacRMovgestTsSogclasseMods() {
		return this.siacRMovgestTsSogclasseMods;
	}

	public void setSiacRMovgestTsSogclasseMods(List<SiacRMovgestTsSogclasseModFin> siacRMovgestTsSogclasseMods) {
		this.siacRMovgestTsSogclasseMods = siacRMovgestTsSogclasseMods;
	}

	public SiacRMovgestTsSogclasseModFin addSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseModFin siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().add(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacRModificaStato(this);

		return siacRMovgestTsSogclasseMod;
	}

	public SiacRMovgestTsSogclasseModFin removeSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseModFin siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().remove(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacRModificaStato(null);

		return siacRMovgestTsSogclasseMod;
	}

	public List<SiacTMovgestTsDetModFin> getSiacTMovgestTsDetMods() {
		return this.siacTMovgestTsDetMods;
	}

	public void setSiacTMovgestTsDetMods(List<SiacTMovgestTsDetModFin> siacTMovgestTsDetMods) {
		this.siacTMovgestTsDetMods = siacTMovgestTsDetMods;
	}

	public SiacTMovgestTsDetModFin addSiacTMovgestTsDetMod(SiacTMovgestTsDetModFin siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().add(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacRModificaStato(this);

		return siacTMovgestTsDetMod;
	}

	public SiacTMovgestTsDetModFin removeSiacTMovgestTsDetMod(SiacTMovgestTsDetModFin siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().remove(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacRModificaStato(null);

		return siacTMovgestTsDetMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.modStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.modStatoRId = uid;
	}
}