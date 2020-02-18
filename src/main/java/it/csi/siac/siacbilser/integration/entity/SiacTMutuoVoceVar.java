/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_mutuo_voce_var database table.
 * 
 */
@Entity
@Table(name="siac_t_mutuo_voce_var")
@NamedQuery(name="SiacTMutuoVoceVar.findAll", query="SELECT s FROM SiacTMutuoVoceVar s")
public class SiacTMutuoVoceVar extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut voce var id. */
	@Id
	@SequenceGenerator(name="SIAC_T_MUTUO_VOCE_VAR_MUTVOCEVARID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MUTUO_VOCE_VAR_MUT_VOCE_VAR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MUTUO_VOCE_VAR_MUTVOCEVARID_GENERATOR")
	@Column(name="mut_voce_var_id")
	private Integer mutVoceVarId;

	/** The mut voce var importo. */
	@Column(name="mut_voce_var_importo")
	private BigDecimal mutVoceVarImporto;

	//bi-directional many-to-one association to SiacDMutuoVarTipo
	/** The siac d mutuo var tipo. */
	@ManyToOne
	@JoinColumn(name="mut_var_tipo_id")
	private SiacDMutuoVarTipo siacDMutuoVarTipo;

	//bi-directional many-to-one association to SiacTMutuoVoce
	/** The siac t mutuo voce. */
	@ManyToOne
	@JoinColumn(name="mut_voce_id")
	private SiacTMutuoVoce siacTMutuoVoce;

	/**
	 * Instantiates a new siac t mutuo voce var.
	 */
	public SiacTMutuoVoceVar() {
	}

	/**
	 * Gets the mut voce var id.
	 *
	 * @return the mut voce var id
	 */
	public Integer getMutVoceVarId() {
		return this.mutVoceVarId;
	}

	/**
	 * Sets the mut voce var id.
	 *
	 * @param mutVoceVarId the new mut voce var id
	 */
	public void setMutVoceVarId(Integer mutVoceVarId) {
		this.mutVoceVarId = mutVoceVarId;
	}

	/**
	 * Gets the mut voce var importo.
	 *
	 * @return the mut voce var importo
	 */
	public BigDecimal getMutVoceVarImporto() {
		return this.mutVoceVarImporto;
	}

	/**
	 * Sets the mut voce var importo.
	 *
	 * @param mutVoceVarImporto the new mut voce var importo
	 */
	public void setMutVoceVarImporto(BigDecimal mutVoceVarImporto) {
		this.mutVoceVarImporto = mutVoceVarImporto;
	}

	/**
	 * Gets the siac d mutuo var tipo.
	 *
	 * @return the siac d mutuo var tipo
	 */
	public SiacDMutuoVarTipo getSiacDMutuoVarTipo() {
		return this.siacDMutuoVarTipo;
	}

	/**
	 * Sets the siac d mutuo var tipo.
	 *
	 * @param siacDMutuoVarTipo the new siac d mutuo var tipo
	 */
	public void setSiacDMutuoVarTipo(SiacDMutuoVarTipo siacDMutuoVarTipo) {
		this.siacDMutuoVarTipo = siacDMutuoVarTipo;
	}



	/**
	 * Gets the siac t mutuo voce.
	 *
	 * @return the siac t mutuo voce
	 */
	public SiacTMutuoVoce getSiacTMutuoVoce() {
		return this.siacTMutuoVoce;
	}

	/**
	 * Sets the siac t mutuo voce.
	 *
	 * @param siacTMutuoVoce the new siac t mutuo voce
	 */
	public void setSiacTMutuoVoce(SiacTMutuoVoce siacTMutuoVoce) {
		this.siacTMutuoVoce = siacTMutuoVoce;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mutVoceVarId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutVoceVarId = uid;
	}

}