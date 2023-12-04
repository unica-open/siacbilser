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
 * The persistent class for the siac_r_mod_tipo_appl database table.
 * 
 */
@Entity
@Table(name="siac_r_modifica_tipo_applicazione")
public class SiacRModificaTipoApplicazioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MODIFICA_TIPO_TIPO_APPLICAZIONE_MOD_TIPO_R_TIPO_APPL_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_mod_tipo_appl_mod_tipo_r_tipo_appl_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MODIFICA_TIPO_TIPO_APPLICAZIONE_MOD_TIPO_R_TIPO_APPL_ID_GENERATOR")
	@Column(name="mod_tipo_r_tipo_appl_id")
	private Integer modTipoRTipoApplId;

	//bi-directional many-to-one association to SiacDLiquidazioneStatoFin
	@ManyToOne
	@JoinColumn(name="mod_tipo_id")
	private SiacDModificaTipoFin siacDModificaTipo;

	//bi-directional many-to-one association to SiacTLiquidazioneFin
	@ManyToOne
	@JoinColumn(name="mod_tipo_appl_id")
	private SiacDModificaTipoApplicazioneFin siacDModificaTipoApplicazione;

	public SiacRModificaTipoApplicazioneFin() {
	}


	public Integer getModTipoRTipoApplId() {
		return modTipoRTipoApplId;
	}





	public void setModTipoRTipoApplId(Integer modTipoRTipoApplId) {
		this.modTipoRTipoApplId = modTipoRTipoApplId;
	}





	public SiacDModificaTipoFin getSiacDModificaTipo() {
		return siacDModificaTipo;
	}



	public void setSiacDModificaTipo(SiacDModificaTipoFin siacDModificaTipo) {
		this.siacDModificaTipo = siacDModificaTipo;
	}



	public SiacDModificaTipoApplicazioneFin getSiacDModificaTipoApplicazione() {
		return siacDModificaTipoApplicazione;
	}



	public void setSiacDModificaTipoApplicazione(SiacDModificaTipoApplicazioneFin siacDModificaTipoApplicazione) {
		this.siacDModificaTipoApplicazione = siacDModificaTipoApplicazione;
	}



	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return modTipoRTipoApplId;
	}

	@Override
	public void setUid(Integer uid) {
		this.modTipoRTipoApplId = uid;
		
	}

}