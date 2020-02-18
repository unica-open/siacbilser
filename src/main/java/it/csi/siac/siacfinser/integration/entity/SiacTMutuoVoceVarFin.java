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
 * The persistent class for the siac_t_mutuo_voce_var database table.
 * 
 */
@Entity
@Table(name="siac_t_mutuo_voce_var")
public class SiacTMutuoVoceVarFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MUTUO_VOCE_VAR_MUTUO_VOCE_VAR_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_mutuo_voce_var_mut_voce_var_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MUTUO_VOCE_VAR_MUTUO_VOCE_VAR_ID_GENERATOR")
	@Column(name="mut_voce_var_id")
	private Integer mutVoceVarId;

	@Column(name="mut_voce_var_importo")
	private BigDecimal mutVoceVarImporto;

	//bi-directional many-to-one association to SiacDMutuoVarTipoFin
	@ManyToOne
	@JoinColumn(name="mut_var_tipo_id")
	private SiacDMutuoVarTipoFin siacDMutuoVarTipo;

	//bi-directional many-to-one association to SiacTMutuoVoceFin
	@ManyToOne
	@JoinColumn(name="mut_voce_id")
	private SiacTMutuoVoceFin siacTMutuoVoce;

	public SiacTMutuoVoceVarFin() {
	}

	public Integer getMutVoceVarId() {
		return this.mutVoceVarId;
	}

	public void setMutVoceVarId(Integer mutVoceVarId) {
		this.mutVoceVarId = mutVoceVarId;
	}

	public BigDecimal getMutVoceVarImporto() {
		return this.mutVoceVarImporto;
	}

	public void setMutVoceVarImporto(BigDecimal mutVoceVarImporto) {
		this.mutVoceVarImporto = mutVoceVarImporto;
	}

	public SiacDMutuoVarTipoFin getSiacDMutuoVarTipo() {
		return this.siacDMutuoVarTipo;
	}

	public void setSiacDMutuoVarTipo(SiacDMutuoVarTipoFin siacDMutuoVarTipo) {
		this.siacDMutuoVarTipo = siacDMutuoVarTipo;
	}

	public SiacTMutuoVoceFin getSiacTMutuoVoce() {
		return this.siacTMutuoVoce;
	}

	public void setSiacTMutuoVoce(SiacTMutuoVoceFin siacTMutuoVoce) {
		this.siacTMutuoVoce = siacTMutuoVoce;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.mutVoceVarId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutVoceVarId = uid;
	}
}