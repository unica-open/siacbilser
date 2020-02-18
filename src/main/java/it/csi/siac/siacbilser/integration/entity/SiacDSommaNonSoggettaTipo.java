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
 * The persistent class for the siac_d_causale_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_somma_non_soggetta_tipo")
@NamedQuery(name="SiacDSommaNonSoggettaTipo.findAll", query="SELECT s FROM SiacDSommaNonSoggettaTipo s")
public class SiacDSommaNonSoggettaTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SOMMA_NON_SOGGETTA_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SOMMA_NON_SOGGETTA_TIPO_SOMMA_NON_SOGGETTA_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SOMMA_NON_SOGGETTA_TIPO_ID_GENERATOR")
	@Column(name="somma_non_soggetta_tipo_id")
	private Integer sommaNonSoggettaTipoId;

	@Column(name="somma_non_soggetta_tipo_code")
	private String sommaNonSoggettaTipoCode;

	@Column(name="somma_non_soggetta_tipo_desc")
	private String sommaNonSoggettaTipoDesc;

	//bi-directional many-to-one association to SiacRCausaleTipo
	@OneToMany(mappedBy="siacDSommaNonSoggettaTipo")
	private List<SiacRDocOnere> siacRDocOneres;
	
	@OneToMany(mappedBy="siacDSommaNonSoggettaTipo")
	private List<SiacROnereSommaNonSoggettaTipo> siacROnereSommaNonSoggettaTipos;

	/**
	 * Instantiates a new siac d causale tipo.
	 */
	public SiacDSommaNonSoggettaTipo() {
	}

	/**
	 * @return the sommaNonSoggettaTipoId
	 */
	public Integer getSommaNonSoggettaTipoId() {
		return sommaNonSoggettaTipoId;
	}

	/**
	 * @param sommaNonSoggettaTipoId the sommaNonSoggettaTipoId to set
	 */
	public void setSommaNonSoggettaTipoId(Integer sommaNonSoggettaTipoId) {
		this.sommaNonSoggettaTipoId = sommaNonSoggettaTipoId;
	}

	/**
	 * @return the sommaNonSoggettaTipoCode
	 */
	public String getSommaNonSoggettaTipoCode() {
		return sommaNonSoggettaTipoCode;
	}

	/**
	 * @param sommaNonSoggettaTipoCode the sommaNonSoggettaTipoCode to set
	 */
	public void setSommaNonSoggettaTipoCode(String sommaNonSoggettaTipoCode) {
		this.sommaNonSoggettaTipoCode = sommaNonSoggettaTipoCode;
	}

	/**
	 * @return the sommaNonSoggettaTipoDesc
	 */
	public String getSommaNonSoggettaTipoDesc() {
		return sommaNonSoggettaTipoDesc;
	}

	/**
	 * @param sommaNonSoggettaTipoDesc the sommaNonSoggettaTipoDesc to set
	 */
	public void setSommaNonSoggettaTipoDesc(String sommaNonSoggettaTipoDesc) {
		this.sommaNonSoggettaTipoDesc = sommaNonSoggettaTipoDesc;
	}

	/**
	 * @return the siacRDocOneres
	 */
	public List<SiacRDocOnere> getSiacRDocOneres() {
		return siacRDocOneres;
	}

	/**
	 * @param siacRDocOneres the siacRDocOneres to set
	 */
	public void setSiacRDocOneres(List<SiacRDocOnere> siacRDocOneres) {
		this.siacRDocOneres = siacRDocOneres;
	}

	/**
	 * @return the siacROnereSommaNonSoggettaTipos
	 */
	public List<SiacROnereSommaNonSoggettaTipo> getSiacROnereSommaNonSoggettaTipos() {
		return siacROnereSommaNonSoggettaTipos;
	}

	/**
	 * @param siacROnereSommaNonSoggettaTipos the siacROnereSommaNonSoggettaTipos to set
	 */
	public void setSiacROnereSommaNonSoggettaTipos(
			List<SiacROnereSommaNonSoggettaTipo> siacROnereSommaNonSoggettaTipos) {
		this.siacROnereSommaNonSoggettaTipos = siacROnereSommaNonSoggettaTipos;
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return sommaNonSoggettaTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.sommaNonSoggettaTipoId = uid;
	}

}