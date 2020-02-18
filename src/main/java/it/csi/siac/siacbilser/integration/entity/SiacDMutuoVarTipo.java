/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_mutuo_var_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_var_tipo")
@NamedQuery(name="SiacDMutuoVarTipo.findAll", query="SELECT s FROM SiacDMutuoVarTipo s")
public class SiacDMutuoVarTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut var tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_VAR_TIPO_MUTVARTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MUTUO_VAR_TIPO_MUT_VAR_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_VAR_TIPO_MUTVARTIPOID_GENERATOR")
	@Column(name="mut_var_tipo_id")
	private Integer mutVarTipoId;	

	/** The mut var tipo code. */
	@Column(name="mut_var_tipo_code")
	private String mutVarTipoCode;

	/** The mut var tipo desc. */
	@Column(name="mut_var_tipo_desc")
	private String mutVarTipoDesc;



	//bi-directional many-to-one association to SiacTMutuoVoceVar
	/** The siac t mutuo voce vars. */
	@OneToMany(mappedBy="siacDMutuoVarTipo")
	private List<SiacTMutuoVoceVar> siacTMutuoVoceVars;

	/**
	 * Instantiates a new siac d mutuo var tipo.
	 */
	public SiacDMutuoVarTipo() {
	}

	/**
	 * Gets the mut var tipo id.
	 *
	 * @return the mut var tipo id
	 */
	public Integer getMutVarTipoId() {
		return this.mutVarTipoId;
	}

	/**
	 * Sets the mut var tipo id.
	 *
	 * @param mutVarTipoId the new mut var tipo id
	 */
	public void setMutVarTipoId(Integer mutVarTipoId) {
		this.mutVarTipoId = mutVarTipoId;
	}

	


	/**
	 * Gets the mut var tipo code.
	 *
	 * @return the mut var tipo code
	 */
	public String getMutVarTipoCode() {
		return this.mutVarTipoCode;
	}

	/**
	 * Sets the mut var tipo code.
	 *
	 * @param mutVarTipoCode the new mut var tipo code
	 */
	public void setMutVarTipoCode(String mutVarTipoCode) {
		this.mutVarTipoCode = mutVarTipoCode;
	}

	/**
	 * Gets the mut var tipo desc.
	 *
	 * @return the mut var tipo desc
	 */
	public String getMutVarTipoDesc() {
		return this.mutVarTipoDesc;
	}

	/**
	 * Sets the mut var tipo desc.
	 *
	 * @param mutVarTipoDesc the new mut var tipo desc
	 */
	public void setMutVarTipoDesc(String mutVarTipoDesc) {
		this.mutVarTipoDesc = mutVarTipoDesc;
	}

	/**
	 * Gets the siac t mutuo voce vars.
	 *
	 * @return the siac t mutuo voce vars
	 */
	public List<SiacTMutuoVoceVar> getSiacTMutuoVoceVars() {
		return this.siacTMutuoVoceVars;
	}

	/**
	 * Sets the siac t mutuo voce vars.
	 *
	 * @param siacTMutuoVoceVars the new siac t mutuo voce vars
	 */
	public void setSiacTMutuoVoceVars(List<SiacTMutuoVoceVar> siacTMutuoVoceVars) {
		this.siacTMutuoVoceVars = siacTMutuoVoceVars;
	}

	/**
	 * Adds the siac t mutuo voce var.
	 *
	 * @param siacTMutuoVoceVar the siac t mutuo voce var
	 * @return the siac t mutuo voce var
	 */
	public SiacTMutuoVoceVar addSiacTMutuoVoceVar(SiacTMutuoVoceVar siacTMutuoVoceVar) {
		getSiacTMutuoVoceVars().add(siacTMutuoVoceVar);
		siacTMutuoVoceVar.setSiacDMutuoVarTipo(this);

		return siacTMutuoVoceVar;
	}

	/**
	 * Removes the siac t mutuo voce var.
	 *
	 * @param siacTMutuoVoceVar the siac t mutuo voce var
	 * @return the siac t mutuo voce var
	 */
	public SiacTMutuoVoceVar removeSiacTMutuoVoceVar(SiacTMutuoVoceVar siacTMutuoVoceVar) {
		getSiacTMutuoVoceVars().remove(siacTMutuoVoceVar);
		siacTMutuoVoceVar.setSiacDMutuoVarTipo(null);

		return siacTMutuoVoceVar;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mutVarTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutVarTipoId = uid;
	}

}