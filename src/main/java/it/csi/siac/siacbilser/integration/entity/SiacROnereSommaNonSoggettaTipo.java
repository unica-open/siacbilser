/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
 * The persistent class for the siac_r_doc_onere database table.
 * 
 */
@Entity
@Table(name="siac_r_onere_somma_non_soggetta_tipo")
@NamedQuery(name="SiacROnereSommaNonSoggettaTipo.findAll", query="SELECT s FROM SiacRDocOnere s")
public class SiacROnereSommaNonSoggettaTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc onere id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ONERE_SOMMA_NON_SOGGETTA_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ONERE_SOMMA_NON_SOGGET_ONERE_SOMMA_NON_SOGGETTA_TIPO_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ONERE_SOMMA_NON_SOGGETTA_TIPO_ID_GENERATOR")
	@Column(name="onere_somma_non_soggetta_tipo_id")
	private Integer onereSommaNonSoggattaTipoId;

	//bi-directional many-to-one association to SiacDCausale
	@ManyToOne
	@JoinColumn(name="somma_non_soggetta_tipo_id")
	private SiacDSommaNonSoggettaTipo siacDSommaNonSoggettaTipo;
	
	@ManyToOne
	@JoinColumn(name="onere_id")
	private SiacDOnere siacDOnere;
	
	/**
	 * @return the onereSommaNonSoggattaTipoId
	 */
	public Integer getOnereSommaNonSoggattaTipoId() {
		return onereSommaNonSoggattaTipoId;
	}

	/**
	 * @param onereSommaNonSoggattaTipoId the onereSommaNonSoggattaTipoId to set
	 */
	public void setOnereSommaNonSoggattaTipoId(Integer onereSommaNonSoggattaTipoId) {
		this.onereSommaNonSoggattaTipoId = onereSommaNonSoggattaTipoId;
	}

	/**
	 * @return the siacDSommaNonSoggettaTipo
	 */
	public SiacDSommaNonSoggettaTipo getSiacDSommaNonSoggettaTipo() {
		return siacDSommaNonSoggettaTipo;
	}

	/**
	 * @param siacDSommaNonSoggettaTipo the siacDSommaNonSoggettaTipo to set
	 */
	public void setSiacDSommaNonSoggettaTipo(
			SiacDSommaNonSoggettaTipo siacDSommaNonSoggettaTipo) {
		this.siacDSommaNonSoggettaTipo = siacDSommaNonSoggettaTipo;
	}

	/**
	 * @return the siacDOnere
	 */
	public SiacDOnere getSiacDOnere() {
		return siacDOnere;
	}

	/**
	 * @param siacDOnere the siacDOnere to set
	 */
	public void setSiacDOnere(SiacDOnere siacDOnere) {
		this.siacDOnere = siacDOnere;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return onereSommaNonSoggattaTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.onereSommaNonSoggattaTipoId = uid;		
	}

}