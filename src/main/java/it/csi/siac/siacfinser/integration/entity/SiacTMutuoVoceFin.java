/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
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
 * The persistent class for the siac_t_mutuo_voce database table.
 * 
 */
@Entity
@Table(name="siac_t_mutuo_voce")
public class SiacTMutuoVoceFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MUTUO_VOCE_MUTUO_VOCE_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_mutuo_voce_mut_voce_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MUTUO_VOCE_MUTUO_VOCE_ID_GENERATOR")
	@Column(name="mut_voce_id")
	private Integer mutVoceId;

	@Column(name="mut_voce_code")
	private String mutVoceCode;

	@Column(name="mut_voce_desc")
	private String mutVoceDesc;

	@Column(name="mut_voce_importo_attuale")
	private BigDecimal mutVoceImportoAttuale;

	@Column(name="mut_voce_importo_iniziale")
	private BigDecimal mutVoceImportoIniziale;

	//bi-directional many-to-one association to SiacRMutuoVoceLiquidazioneFin
	@OneToMany(mappedBy="siacTMutuoVoce")
	private List<SiacRMutuoVoceLiquidazioneFin> siacRMutuoVoceLiquidaziones;

	//bi-directional many-to-one association to SiacRMutuoVoceMovgestFin
	@OneToMany(mappedBy="siacTMutuoVoce")
	private List<SiacRMutuoVoceMovgestFin> siacRMutuoVoceMovgests;

	//bi-directional many-to-one association to SiacDMutuoVoceTipoFin
	@ManyToOne
	@JoinColumn(name="mut_voce_tipo_id")
	private SiacDMutuoVoceTipoFin siacDMutuoVoceTipo;
	

	//bi-directional many-to-one association to SiacTMutuoFin
	@ManyToOne
	@JoinColumn(name="mut_id")
	private SiacTMutuoFin siacTMutuo;

	//bi-directional many-to-one association to SiacTMutuoVoceVarFin
	@OneToMany(mappedBy="siacTMutuoVoce")
	private List<SiacTMutuoVoceVarFin> siacTMutuoVoceVars;

	public SiacTMutuoVoceFin() {
	}

	public Integer getMutVoceId() {
		return this.mutVoceId;
	}

	public void setMutVoceId(Integer mutVoceId) {
		this.mutVoceId = mutVoceId;
	}

	public String getMutVoceCode() {
		return this.mutVoceCode;
	}

	public void setMutVoceCode(String mutVoceCode) {
		this.mutVoceCode = mutVoceCode;
	}

	public String getMutVoceDesc() {
		return this.mutVoceDesc;
	}

	public void setMutVoceDesc(String mutVoceDesc) {
		this.mutVoceDesc = mutVoceDesc;
	}

	public BigDecimal getMutVoceImportoAttuale() {
		return this.mutVoceImportoAttuale;
	}

	public void setMutVoceImportoAttuale(BigDecimal mutVoceImportoAttuale) {
		this.mutVoceImportoAttuale = mutVoceImportoAttuale;
	}

	public BigDecimal getMutVoceImportoIniziale() {
		return this.mutVoceImportoIniziale;
	}

	public void setMutVoceImportoIniziale(BigDecimal mutVoceImportoIniziale) {
		this.mutVoceImportoIniziale = mutVoceImportoIniziale;
	}

	public List<SiacRMutuoVoceLiquidazioneFin> getSiacRMutuoVoceLiquidaziones() {
		return this.siacRMutuoVoceLiquidaziones;
	}

	public void setSiacRMutuoVoceLiquidaziones(List<SiacRMutuoVoceLiquidazioneFin> siacRMutuoVoceLiquidaziones) {
		this.siacRMutuoVoceLiquidaziones = siacRMutuoVoceLiquidaziones;
	}

	public SiacRMutuoVoceLiquidazioneFin addSiacRMutuoVoceLiquidazione(SiacRMutuoVoceLiquidazioneFin siacRMutuoVoceLiquidazione) {
		getSiacRMutuoVoceLiquidaziones().add(siacRMutuoVoceLiquidazione);
		siacRMutuoVoceLiquidazione.setSiacTMutuoVoce(this);

		return siacRMutuoVoceLiquidazione;
	}

	public SiacRMutuoVoceLiquidazioneFin removeSiacRMutuoVoceLiquidazione(SiacRMutuoVoceLiquidazioneFin siacRMutuoVoceLiquidazione) {
		getSiacRMutuoVoceLiquidaziones().remove(siacRMutuoVoceLiquidazione);
		siacRMutuoVoceLiquidazione.setSiacTMutuoVoce(null);

		return siacRMutuoVoceLiquidazione;
	}

	public List<SiacRMutuoVoceMovgestFin> getSiacRMutuoVoceMovgests() {
		return this.siacRMutuoVoceMovgests;
	}

	public void setSiacRMutuoVoceMovgests(List<SiacRMutuoVoceMovgestFin> siacRMutuoVoceMovgests) {
		this.siacRMutuoVoceMovgests = siacRMutuoVoceMovgests;
	}

	public SiacRMutuoVoceMovgestFin addSiacRMutuoVoceMovgest(SiacRMutuoVoceMovgestFin siacRMutuoVoceMovgest) {
		getSiacRMutuoVoceMovgests().add(siacRMutuoVoceMovgest);
		siacRMutuoVoceMovgest.setSiacTMutuoVoce(this);

		return siacRMutuoVoceMovgest;
	}

	public SiacRMutuoVoceMovgestFin removeSiacRMutuoVoceMovgest(SiacRMutuoVoceMovgestFin siacRMutuoVoceMovgest) {
		getSiacRMutuoVoceMovgests().remove(siacRMutuoVoceMovgest);
		siacRMutuoVoceMovgest.setSiacTMutuoVoce(null);

		return siacRMutuoVoceMovgest;
	}

	public SiacDMutuoVoceTipoFin getSiacDMutuoVoceTipo() {
		return this.siacDMutuoVoceTipo;
	}

	public void setSiacDMutuoVoceTipo(SiacDMutuoVoceTipoFin siacDMutuoVoceTipo) {
		this.siacDMutuoVoceTipo = siacDMutuoVoceTipo;
	}

	public SiacTMutuoFin getSiacTMutuo() {
		return this.siacTMutuo;
	}

	public void setSiacTMutuo(SiacTMutuoFin siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
	}

	public List<SiacTMutuoVoceVarFin> getSiacTMutuoVoceVars() {
		return this.siacTMutuoVoceVars;
	}

	public void setSiacTMutuoVoceVars(List<SiacTMutuoVoceVarFin> siacTMutuoVoceVars) {
		this.siacTMutuoVoceVars = siacTMutuoVoceVars;
	}

	public SiacTMutuoVoceVarFin addSiacTMutuoVoceVar(SiacTMutuoVoceVarFin siacTMutuoVoceVar) {
		getSiacTMutuoVoceVars().add(siacTMutuoVoceVar);
		siacTMutuoVoceVar.setSiacTMutuoVoce(this);

		return siacTMutuoVoceVar;
	}

	public SiacTMutuoVoceVarFin removeSiacTMutuoVoceVar(SiacTMutuoVoceVarFin siacTMutuoVoceVar) {
		getSiacTMutuoVoceVars().remove(siacTMutuoVoceVar);
		siacTMutuoVoceVar.setSiacTMutuoVoce(null);

		return siacTMutuoVoceVar;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.mutVoceId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutVoceId = uid;
	}

	
}