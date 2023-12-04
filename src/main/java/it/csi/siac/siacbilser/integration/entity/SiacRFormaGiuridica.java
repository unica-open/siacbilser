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
 * The persistent class for the siac_r_forma_giuridica database table.
 * 
 */
@Entity
@Table(name="siac_r_forma_giuridica")
@NamedQuery(name="SiacRFormaGiuridica.findAll", query="SELECT s FROM SiacRFormaGiuridica s")
public class SiacRFormaGiuridica extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto forma giuridica id. */
	@Id
	@SequenceGenerator(name="SIAC_R_FORMA_GIURIDICA_SOGGETTOFORMAGIURIDICAID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_FORMA_GIURIDICA_SOGGETTO_FORMA_GIURIDICA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_FORMA_GIURIDICA_SOGGETTOFORMAGIURIDICAID_GENERATOR")
	@Column(name="soggetto_forma_giuridica_id")
	private Integer soggettoFormaGiuridicaId;


	//bi-directional many-to-one association to SiacTFormaGiuridica
	/** The siac t forma giuridica. */
	@ManyToOne
	@JoinColumn(name="forma_giuridica_id")
	private SiacTFormaGiuridica siacTFormaGiuridica;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac r forma giuridica.
	 */
	public SiacRFormaGiuridica() {
	}

	/**
	 * Gets the soggetto forma giuridica id.
	 *
	 * @return the soggetto forma giuridica id
	 */
	public Integer getSoggettoFormaGiuridicaId() {
		return this.soggettoFormaGiuridicaId;
	}

	/**
	 * Sets the soggetto forma giuridica id.
	 *
	 * @param soggettoFormaGiuridicaId the new soggetto forma giuridica id
	 */
	public void setSoggettoFormaGiuridicaId(Integer soggettoFormaGiuridicaId) {
		this.soggettoFormaGiuridicaId = soggettoFormaGiuridicaId;
	}

	/**
	 * Gets the siac t forma giuridica.
	 *
	 * @return the siac t forma giuridica
	 */
	public SiacTFormaGiuridica getSiacTFormaGiuridica() {
		return this.siacTFormaGiuridica;
	}

	/**
	 * Sets the siac t forma giuridica.
	 *
	 * @param siacTFormaGiuridica the new siac t forma giuridica
	 */
	public void setSiacTFormaGiuridica(SiacTFormaGiuridica siacTFormaGiuridica) {
		this.siacTFormaGiuridica = siacTFormaGiuridica;
	}

	/**
	 * Gets the siac t soggetto.
	 *
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	/**
	 * Sets the siac t soggetto.
	 *
	 * @param siacTSoggetto the new siac t soggetto
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoFormaGiuridicaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoFormaGiuridicaId = uid;
	}

}