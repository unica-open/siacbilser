/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_bil_elem_var database table.
 * 
 */
@Entity
@Table(name="siac_t_bil_elem_var")
public class SiacTBilElemVarFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_BIL_ELEM_VAR_ELEM_VAR_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_bil_elem_var_elem_var_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_ELEM_VAR_ELEM_VAR_ID_GENERATOR")
	@Column(name="elem_var_id")
	private Integer elemVarId;

	@Column(name="bil_id")
	private Integer bilId;

	@Column(name="elem_code")
	private String elemCode;

	@Column(name="elem_code2")
	private String elemCode2;

	@Column(name="elem_code3")
	private String elemCode3;

	@Column(name="elem_desc")
	private String elemDesc;

	@Column(name="elem_desc2")
	private String elemDesc2;

	@Column(name="elem_id_padre")
	private Integer elemIdPadre;

	@Column(name="elem_tipo_id")
	private Integer elemTipoId;

	private Integer livello;

	private String ordine;

	@Column(name="periodo_id")
	private Integer periodoId;

	//bi-directional many-to-one association to SiacRVariazioneStatoFin
	@ManyToOne
	@JoinColumn(name="variazione_stato_id")
	private SiacRVariazioneStatoFin siacRVariazioneStato;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	public SiacTBilElemVarFin() {
	}

	public Integer getElemVarId() {
		return this.elemVarId;
	}

	public void setElemVarId(Integer elemVarId) {
		this.elemVarId = elemVarId;
	}

	public Integer getBilId() {
		return this.bilId;
	}

	public void setBilId(Integer bilId) {
		this.bilId = bilId;
	}

	public String getElemCode() {
		return this.elemCode;
	}

	public void setElemCode(String elemCode) {
		this.elemCode = elemCode;
	}

	public String getElemCode2() {
		return this.elemCode2;
	}

	public void setElemCode2(String elemCode2) {
		this.elemCode2 = elemCode2;
	}

	public String getElemCode3() {
		return this.elemCode3;
	}

	public void setElemCode3(String elemCode3) {
		this.elemCode3 = elemCode3;
	}

	public String getElemDesc() {
		return this.elemDesc;
	}

	public void setElemDesc(String elemDesc) {
		this.elemDesc = elemDesc;
	}

	public String getElemDesc2() {
		return this.elemDesc2;
	}

	public void setElemDesc2(String elemDesc2) {
		this.elemDesc2 = elemDesc2;
	}

	public Integer getElemIdPadre() {
		return this.elemIdPadre;
	}

	public void setElemIdPadre(Integer elemIdPadre) {
		this.elemIdPadre = elemIdPadre;
	}

	public Integer getElemTipoId() {
		return this.elemTipoId;
	}

	public void setElemTipoId(Integer elemTipoId) {
		this.elemTipoId = elemTipoId;
	}

	public Integer getLivello() {
		return this.livello;
	}

	public void setLivello(Integer livello) {
		this.livello = livello;
	}

	public String getOrdine() {
		return this.ordine;
	}

	public void setOrdine(String ordine) {
		this.ordine = ordine;
	}

	public Integer getPeriodoId() {
		return this.periodoId;
	}

	public void setPeriodoId(Integer periodoId) {
		this.periodoId = periodoId;
	}

	public SiacRVariazioneStatoFin getSiacRVariazioneStato() {
		return this.siacRVariazioneStato;
	}

	public void setSiacRVariazioneStato(SiacRVariazioneStatoFin siacRVariazioneStato) {
		this.siacRVariazioneStato = siacRVariazioneStato;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.elemVarId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.elemVarId = uid;
	}
}