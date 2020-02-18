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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the siac_d_variazione_applicazione database table.
 * 
 */
@Entity
@Table(name="siac_d_variazione_applicazione")
public class SiacDVariazioneApplicazione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bil tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_VAR_APPLI_APPLICAZIONEID_GENERATOR", allocationSize=1, sequenceName="siac_d_variazione_applicazione_applicazione_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_VAR_APPLI_APPLICAZIONEID_GENERATOR")
	@Column(name="applicazione_id")
	private Integer applicazioneId;

	/** The bil tipo code. */
	@Column(name="applicazione_code")
	private String applicazioneCode;

	/** The bil tipo desc. */
	@Column(name="applicazione_desc")
	private String applicazioneDesc;

	//bi-directional many-to-one association to SiacTVariazione
	@OneToMany(mappedBy="siacDVariazioneApplicazione")
	private List<SiacTVariazione> siacTVariaziones;

	/**
	 * Instantiates a new siac d bil tipo.
	 */
	public SiacDVariazioneApplicazione() {
	}

	/**
	 * Gets the bil tipo id.
	 *
	 * @return the bil tipo id
	 */
	public Integer getApplicazioneId() {
		return this.applicazioneId;
	}

	/**
	 * Sets the bil tipo id.
	 *
	 * @param bilTipoId the new bil tipo id
	 */
	public void setApplicazioneId(Integer bilTipoId) {
		this.applicazioneId = bilTipoId;
	}

	/**
	 * Gets the bil tipo code.
	 *
	 * @return the bil tipo code
	 */
	public String getApplicazioneCode() {
		return this.applicazioneCode;
	}

	/**
	 * Sets the bil tipo code.
	 *
	 * @param bilTipoCode the new bil tipo code
	 */
	public void setApplicazioneCode(String bilTipoCode) {
		this.applicazioneCode = bilTipoCode;
	}

	/**
	 * Gets the bil tipo desc.
	 *
	 * @return the bil tipo desc
	 */
	public String getApplicazioneDesc() {
		return this.applicazioneDesc;
	}

	/**
	 * Sets the bil tipo desc.
	 *
	 * @param bilTipoDesc the new bil tipo desc
	 */
	public void setApplicazioneDesc(String bilTipoDesc) {
		this.applicazioneDesc = bilTipoDesc;
	}

	/**
	 * @return the siacTVariaziones
	 */
	public List<SiacTVariazione> getSiacTVariaziones() {
		return siacTVariaziones;
	}

	/**
	 * @param siacTVariaziones the siacTVariaziones to set
	 */
	public void setSiacTVariaziones(List<SiacTVariazione> siacTVariaziones) {
		this.siacTVariaziones = siacTVariaziones;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return applicazioneId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.applicazioneId = uid;		
	}

}