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
 * The persistent class for the siac_d_atto_amm_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_atto_amm_tipo")
@NamedQuery(name="SiacDAttoAmmTipo.findAll", query="SELECT s FROM SiacDAttoAmmTipo s")
public class SiacDAttoAmmTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The attoamm tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ATTO_AMM_TIPO_ATTOAMMTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ATTO_AMM_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ATTO_AMM_TIPO_ATTOAMMTIPOID_GENERATOR")
	@Column(name="attoamm_tipo_id")
	private Integer attoammTipoId;

	/** The attoamm tipo code. */
	@Column(name="attoamm_tipo_code")
	private String attoammTipoCode;

	/** The attoamm tipo desc. */
	@Column(name="attoamm_tipo_desc")
	private String attoammTipoDesc;
	
	@Column(name="attoamm_progressivo_auto")
	private Boolean attoammProgressivoAuto;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amms. */
	@OneToMany(mappedBy="siacDAttoAmmTipo")
	private List<SiacTAttoAmm> siacTAttoAmms;
	
	//bi-directional many-to-one association to SiacTNumeroAtto
	/** The siac t atto amms. */
	@OneToMany(mappedBy="siacDAttoAmmTipo")
	private List<SiacTNumeroAtto> siacTNumeroAttos;

	/**
	 * Instantiates a new siac d atto amm tipo.
	 */
	public SiacDAttoAmmTipo() {
	}

	/**
	 * Gets the attoamm tipo id.
	 *
	 * @return the attoamm tipo id
	 */
	public Integer getAttoammTipoId() {
		return this.attoammTipoId;
	}

	/**
	 * Sets the attoamm tipo id.
	 *
	 * @param attoammTipoId the new attoamm tipo id
	 */
	public void setAttoammTipoId(Integer attoammTipoId) {
		this.attoammTipoId = attoammTipoId;
	}

	/**
	 * Gets the attoamm tipo code.
	 *
	 * @return the attoamm tipo code
	 */
	public String getAttoammTipoCode() {
		return this.attoammTipoCode;
	}

	/**
	 * Sets the attoamm tipo code.
	 *
	 * @param attoammTipoCode the new attoamm tipo code
	 */
	public void setAttoammTipoCode(String attoammTipoCode) {
		this.attoammTipoCode = attoammTipoCode;
	}

	/**
	 * Gets the attoamm tipo desc.
	 *
	 * @return the attoamm tipo desc
	 */
	public String getAttoammTipoDesc() {
		return this.attoammTipoDesc;
	}

	/**
	 * Sets the attoamm tipo desc.
	 *
	 * @param attoammTipoDesc the new attoamm tipo desc
	 */
	public void setAttoammTipoDesc(String attoammTipoDesc) {
		this.attoammTipoDesc = attoammTipoDesc;
	}

	/**
	 * @return the attoammProgressivoAuto
	 */
	public Boolean getAttoammProgressivoAuto() {
		return attoammProgressivoAuto;
	}

	/**
	 * @param attoammProgressivoAuto the attoammProgressivoAuto to set
	 */
	public void setAttoammProgressivoAuto(Boolean attoammProgressivoAuto) {
		this.attoammProgressivoAuto = attoammProgressivoAuto;
	}

	/**
	 * Gets the siac t atto amms.
	 *
	 * @return the siac t atto amms
	 */
	public List<SiacTAttoAmm> getSiacTAttoAmms() {
		return this.siacTAttoAmms;
	}

	/**
	 * Sets the siac t atto amms.
	 *
	 * @param siacTAttoAmms the new siac t atto amms
	 */
	public void setSiacTAttoAmms(List<SiacTAttoAmm> siacTAttoAmms) {
		this.siacTAttoAmms = siacTAttoAmms;
	}

	/**
	 * Adds the siac t atto amm.
	 *
	 * @param siacTAttoAmm the siac t atto amm
	 * @return the siac t atto amm
	 */
	public SiacTAttoAmm addSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		getSiacTAttoAmms().add(siacTAttoAmm);
		siacTAttoAmm.setSiacDAttoAmmTipo(this);

		return siacTAttoAmm;
	}

	/**
	 * Removes the siac t atto amm.
	 *
	 * @param siacTAttoAmm the siac t atto amm
	 * @return the siac t atto amm
	 */
	public SiacTAttoAmm removeSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		getSiacTAttoAmms().remove(siacTAttoAmm);
		siacTAttoAmm.setSiacDAttoAmmTipo(null);

		return siacTAttoAmm;
	}
	
	/**
	 * Gets the siac t numero attos.
	 *
	 * @return the siac t numero attos
	 */
	public List<SiacTNumeroAtto> getSiacTNumeroAttos() {
		return this.siacTNumeroAttos;
	}

	/**
	 * Sets the siac t numero attos.
	 *
	 * @param siacTNumeroAttos the new siac t numero attos
	 */
	public void setSiacTNumeroAttos(List<SiacTNumeroAtto> siacTNumeroAttos) {
		this.siacTNumeroAttos = siacTNumeroAttos;
	}

	/**
	 * Adds the siac t numero atto.
	 *
	 * @param siacTNumeroAtto the siac t numero atto
	 * @return the siac t numero atto
	 */
	public SiacTNumeroAtto addSiacTNumeroAtto(SiacTNumeroAtto siacTNumeroAtto) {
		getSiacTNumeroAttos().add(siacTNumeroAtto);
		siacTNumeroAtto.setSiacDAttoAmmTipo(this);

		return siacTNumeroAtto;
	}

	/**
	 * Removes the siac t numero atto.
	 *
	 * @param siacTAttoAmm the siac t numero atto
	 * @return the siac t numero atto
	 */
	public SiacTNumeroAtto removeSiacTNumeroAtto(SiacTNumeroAtto siacTNumeroAtto) {
		getSiacTNumeroAttos().remove(siacTNumeroAtto);
		siacTNumeroAtto.setSiacDAttoAmmTipo(null);

		return siacTNumeroAtto;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return attoammTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		attoammTipoId = uid;
	}

}