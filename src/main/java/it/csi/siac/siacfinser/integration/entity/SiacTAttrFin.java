/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;



/**
 * The persistent class for the siac_t_attr database table.
 * 
 */
@Entity
@Table(name="siac_t_attr")
public class SiacTAttrFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attr_id")
	private Integer attrId;

	@Column(name="attr_code")
	private String attrCode;

	@Column(name="attr_desc")
	private String attrDesc;

	@Column(name="tabella_nome")
	private String tabellaNome;

	//bi-directional many-to-one association to SiacRAttrClassTipoFin
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRAttrClassTipoFin> siacRAttrClassTipos;

	//bi-directional many-to-one association to SiacRAttrEntitaFin
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRAttrEntitaFin> siacRAttrEntitas;

	//bi-directional many-to-one association to SiacRClassAttrFin
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRClassAttrFin> siacRClassAttrs;

	//bi-directional many-to-one association to SiacRMovgestTsAttrFin
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRMovgestTsAttrFin> siacRMovgestTsAttrs;

	//bi-directional many-to-one association to SiacROnereAttrFin
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacROnereAttrFin> siacROnereAttrs;

	//bi-directional many-to-one association to SiacRSoggettoAttrFin
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRSoggettoAttrFin> siacRSoggettoAttrs;

	//bi-directional many-to-one association to SiacRSoggettoAttrModFin
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRSoggettoAttrModFin> siacRSoggettoAttrMods;

	//bi-directional many-to-one association to SiacRVariazioneAttrFin
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRVariazioneAttrFin> siacRVariazioneAttrs;

	//bi-directional many-to-one association to SiacDAttrTipoFin
	@ManyToOne
	@JoinColumn(name="attr_tipo_id")
	private SiacDAttrTipoFin siacDAttrTipo;

	
	//bi-directional many-to-one association to SiacRProgrammaAttrFin
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRProgrammaAttrFin> siacRProgrammaAttrs;
	
	public SiacTAttrFin() {
	}

	public Integer getAttrId() {
		return this.attrId;
	}

	public void setAttrId(Integer attrId) {
		this.attrId = attrId;
	}

	public String getAttrCode() {
		return this.attrCode;
	}

	public void setAttrCode(String attrCode) {
		this.attrCode = attrCode;
	}

	public String getAttrDesc() {
		return this.attrDesc;
	}

	public void setAttrDesc(String attrDesc) {
		this.attrDesc = attrDesc;
	}

	public String getTabellaNome() {
		return this.tabellaNome;
	}

	public void setTabellaNome(String tabellaNome) {
		this.tabellaNome = tabellaNome;
	}

	public List<SiacRAttrClassTipoFin> getSiacRAttrClassTipos() {
		return this.siacRAttrClassTipos;
	}

	public void setSiacRAttrClassTipos(List<SiacRAttrClassTipoFin> siacRAttrClassTipos) {
		this.siacRAttrClassTipos = siacRAttrClassTipos;
	}

	public SiacRAttrClassTipoFin addSiacRAttrClassTipo(SiacRAttrClassTipoFin siacRAttrClassTipo) {
		getSiacRAttrClassTipos().add(siacRAttrClassTipo);
		siacRAttrClassTipo.setSiacTAttr(this);

		return siacRAttrClassTipo;
	}

	public SiacRAttrClassTipoFin removeSiacRAttrClassTipo(SiacRAttrClassTipoFin siacRAttrClassTipo) {
		getSiacRAttrClassTipos().remove(siacRAttrClassTipo);
		siacRAttrClassTipo.setSiacTAttr(null);

		return siacRAttrClassTipo;
	}

	public List<SiacRAttrEntitaFin> getSiacRAttrEntitas() {
		return this.siacRAttrEntitas;
	}

	public void setSiacRAttrEntitas(List<SiacRAttrEntitaFin> siacRAttrEntitas) {
		this.siacRAttrEntitas = siacRAttrEntitas;
	}

	public SiacRAttrEntitaFin addSiacRAttrEntita(SiacRAttrEntitaFin siacRAttrEntita) {
		getSiacRAttrEntitas().add(siacRAttrEntita);
		siacRAttrEntita.setSiacTAttr(this);

		return siacRAttrEntita;
	}

	public SiacRAttrEntitaFin removeSiacRAttrEntita(SiacRAttrEntitaFin siacRAttrEntita) {
		getSiacRAttrEntitas().remove(siacRAttrEntita);
		siacRAttrEntita.setSiacTAttr(null);

		return siacRAttrEntita;
	}

	public List<SiacRClassAttrFin> getSiacRClassAttrs() {
		return this.siacRClassAttrs;
	}

	public void setSiacRClassAttrs(List<SiacRClassAttrFin> siacRClassAttrs) {
		this.siacRClassAttrs = siacRClassAttrs;
	}

	public SiacRClassAttrFin addSiacRClassAttr(SiacRClassAttrFin siacRClassAttr) {
		getSiacRClassAttrs().add(siacRClassAttr);
		siacRClassAttr.setSiacTAttr(this);

		return siacRClassAttr;
	}

	public SiacRClassAttrFin removeSiacRClassAttr(SiacRClassAttrFin siacRClassAttr) {
		getSiacRClassAttrs().remove(siacRClassAttr);
		siacRClassAttr.setSiacTAttr(null);

		return siacRClassAttr;
	}

	public List<SiacRMovgestTsAttrFin> getSiacRMovgestTsAttrs() {
		return this.siacRMovgestTsAttrs;
	}

	public void setSiacRMovgestTsAttrs(List<SiacRMovgestTsAttrFin> siacRMovgestTsAttrs) {
		this.siacRMovgestTsAttrs = siacRMovgestTsAttrs;
	}

	public SiacRMovgestTsAttrFin addSiacRMovgestTsAttr(SiacRMovgestTsAttrFin siacRMovgestTsAttr) {
		getSiacRMovgestTsAttrs().add(siacRMovgestTsAttr);
		siacRMovgestTsAttr.setSiacTAttr(this);

		return siacRMovgestTsAttr;
	}

	public SiacRMovgestTsAttrFin removeSiacRMovgestTsAttr(SiacRMovgestTsAttrFin siacRMovgestTsAttr) {
		getSiacRMovgestTsAttrs().remove(siacRMovgestTsAttr);
		siacRMovgestTsAttr.setSiacTAttr(null);

		return siacRMovgestTsAttr;
	}

	public List<SiacROnereAttrFin> getSiacROnereAttrs() {
		return this.siacROnereAttrs;
	}

	public void setSiacROnereAttrs(List<SiacROnereAttrFin> siacROnereAttrs) {
		this.siacROnereAttrs = siacROnereAttrs;
	}

	public SiacROnereAttrFin addSiacROnereAttr(SiacROnereAttrFin siacROnereAttr) {
		getSiacROnereAttrs().add(siacROnereAttr);
		siacROnereAttr.setSiacTAttr(this);

		return siacROnereAttr;
	}

	public SiacROnereAttrFin removeSiacROnereAttr(SiacROnereAttrFin siacROnereAttr) {
		getSiacROnereAttrs().remove(siacROnereAttr);
		siacROnereAttr.setSiacTAttr(null);

		return siacROnereAttr;
	}

	public List<SiacRSoggettoAttrFin> getSiacRSoggettoAttrs() {
		return this.siacRSoggettoAttrs;
	}

	public void setSiacRSoggettoAttrs(List<SiacRSoggettoAttrFin> siacRSoggettoAttrs) {
		this.siacRSoggettoAttrs = siacRSoggettoAttrs;
	}

	public SiacRSoggettoAttrFin addSiacRSoggettoAttr(SiacRSoggettoAttrFin siacRSoggettoAttr) {
		getSiacRSoggettoAttrs().add(siacRSoggettoAttr);
		siacRSoggettoAttr.setSiacTAttr(this);

		return siacRSoggettoAttr;
	}

	public SiacRSoggettoAttrFin removeSiacRSoggettoAttr(SiacRSoggettoAttrFin siacRSoggettoAttr) {
		getSiacRSoggettoAttrs().remove(siacRSoggettoAttr);
		siacRSoggettoAttr.setSiacTAttr(null);

		return siacRSoggettoAttr;
	}

	public List<SiacRSoggettoAttrModFin> getSiacRSoggettoAttrMods() {
		return this.siacRSoggettoAttrMods;
	}

	public void setSiacRSoggettoAttrMods(List<SiacRSoggettoAttrModFin> siacRSoggettoAttrMods) {
		this.siacRSoggettoAttrMods = siacRSoggettoAttrMods;
	}

	public SiacRSoggettoAttrModFin addSiacRSoggettoAttrMod(SiacRSoggettoAttrModFin siacRSoggettoAttrMod) {
		getSiacRSoggettoAttrMods().add(siacRSoggettoAttrMod);
		siacRSoggettoAttrMod.setSiacTAttr(this);

		return siacRSoggettoAttrMod;
	}

	public SiacRSoggettoAttrModFin removeSiacRSoggettoAttrMod(SiacRSoggettoAttrModFin siacRSoggettoAttrMod) {
		getSiacRSoggettoAttrMods().remove(siacRSoggettoAttrMod);
		siacRSoggettoAttrMod.setSiacTAttr(null);

		return siacRSoggettoAttrMod;
	}

	public List<SiacRVariazioneAttrFin> getSiacRVariazioneAttrs() {
		return this.siacRVariazioneAttrs;
	}

	public void setSiacRVariazioneAttrs(List<SiacRVariazioneAttrFin> siacRVariazioneAttrs) {
		this.siacRVariazioneAttrs = siacRVariazioneAttrs;
	}

	public SiacRVariazioneAttrFin addSiacRVariazioneAttr(SiacRVariazioneAttrFin siacRVariazioneAttr) {
		getSiacRVariazioneAttrs().add(siacRVariazioneAttr);
		siacRVariazioneAttr.setSiacTAttr(this);

		return siacRVariazioneAttr;
	}

	public SiacRVariazioneAttrFin removeSiacRVariazioneAttr(SiacRVariazioneAttrFin siacRVariazioneAttr) {
		getSiacRVariazioneAttrs().remove(siacRVariazioneAttr);
		siacRVariazioneAttr.setSiacTAttr(null);

		return siacRVariazioneAttr;
	}

	public SiacDAttrTipoFin getSiacDAttrTipo() {
		return this.siacDAttrTipo;
	}

	public void setSiacDAttrTipo(SiacDAttrTipoFin siacDAttrTipo) {
		this.siacDAttrTipo = siacDAttrTipo;
	}
	
	
	public List<SiacRProgrammaAttrFin> getSiacRProgrammaAttrs() {
		return this.siacRProgrammaAttrs;
	}

	public void setSiacRProgrammaAttrs(List<SiacRProgrammaAttrFin> siacRProgrammaAttrs) {
		this.siacRProgrammaAttrs = siacRProgrammaAttrs;
	}

	public SiacRProgrammaAttrFin addSiacRProgrammaAttr(SiacRProgrammaAttrFin siacRProgrammaAttr) {
		getSiacRProgrammaAttrs().add(siacRProgrammaAttr);
		siacRProgrammaAttr.setSiacTAttr(this);

		return siacRProgrammaAttr;
	}

	public SiacRProgrammaAttrFin removeSiacRProgrammaAttr(SiacRProgrammaAttrFin siacRProgrammaAttr) {
		getSiacRProgrammaAttrs().remove(siacRProgrammaAttr);
		siacRProgrammaAttr.setSiacTAttr(null);

		return siacRProgrammaAttr;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attrId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attrId = uid;
	}
}