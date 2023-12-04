/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;

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
 * The persistent class for the siac_t_bil_elem_det_var database table.
 * 
 */
@Entity
@Table(name="siac_t_bil_elem_det_var")
public class SiacTBilElemDetVarFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_BIL_ELEM_DET_VAR_ELEM_DET_VAR_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_bil_elem_det_var_elem_det_var_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_ELEM_DET_VAR_ELEM_DET_VAR_ID_GENERATOR")
	@Column(name="elem_det_var_id")
	private Integer elemDetVarId;

	@Column(name="elem_det_flag")
	private String elemDetFlag;

	@Column(name="elem_det_importo")
	private BigDecimal elemDetImporto;

	@Column(name="elem_det_tipo_id")
	private Integer elemDetTipoId;

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

	//bi-directional many-to-one association to SiacTBilElemDetFin
	@ManyToOne
	@JoinColumn(name="elem_det_id")
	private SiacTBilElemDetFin siacTBilElemDet;

	public SiacTBilElemDetVarFin() {
	}

	public Integer getElemDetVarId() {
		return this.elemDetVarId;
	}

	public void setElemDetVarId(Integer elemDetVarId) {
		this.elemDetVarId = elemDetVarId;
	}

	public String getElemDetFlag() {
		return this.elemDetFlag;
	}

	public void setElemDetFlag(String elemDetFlag) {
		this.elemDetFlag = elemDetFlag;
	}

	public BigDecimal getElemDetImporto() {
		return this.elemDetImporto;
	}

	public void setElemDetImporto(BigDecimal elemDetImporto) {
		this.elemDetImporto = elemDetImporto;
	}

	public Integer getElemDetTipoId() {
		return this.elemDetTipoId;
	}

	public void setElemDetTipoId(Integer elemDetTipoId) {
		this.elemDetTipoId = elemDetTipoId;
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

	public SiacTBilElemDetFin getSiacTBilElemDet() {
		return this.siacTBilElemDet;
	}

	public void setSiacTBilElemDet(SiacTBilElemDetFin siacTBilElemDet) {
		this.siacTBilElemDet = siacTBilElemDet;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.elemDetVarId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.elemDetVarId = uid;
	}
}