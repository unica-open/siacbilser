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
 * The persistent class for the siac_d_onere_attivita database table.
 * 
 */
@Entity
@Table(name="siac_d_onere_attivita")
@NamedQuery(name="SiacDOnereAttivita.findAll", query="SELECT s FROM SiacDOnereAttivita s")
public class SiacDOnereAttivita extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The onere att id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ONERE_ATTIVITA_ONEREATTID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ONERE_ATTIVITA_ONERE_ATT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ONERE_ATTIVITA_ONEREATTID_GENERATOR")
	@Column(name="onere_att_id")
	private Integer onereAttId;

	/** The onere att code. */
	@Column(name="onere_att_code")
	private String onereAttCode;

	/** The onere att desc. */
	@Column(name="onere_att_desc")
	private String onereAttDesc;
	

	//bi-directional many-to-one association to SiacRDocOnere
	/** The siac r doc oneres. */
	@OneToMany(mappedBy="siacDOnereAttivita")
	private List<SiacRDocOnere> siacRDocOneres;

	//bi-directional many-to-one association to SiacROnereAttivita
	/** The siac r onere attivitas. */
	@OneToMany(mappedBy="siacDOnereAttivita")
	private List<SiacROnereAttivita> siacROnereAttivitas;

	/**
	 * Instantiates a new siac d onere attivita.
	 */
	public SiacDOnereAttivita() {
	}

	/**
	 * Gets the onere att id.
	 *
	 * @return the onere att id
	 */
	public Integer getOnereAttId() {
		return this.onereAttId;
	}

	/**
	 * Sets the onere att id.
	 *
	 * @param onereAttId the new onere att id
	 */
	public void setOnereAttId(Integer onereAttId) {
		this.onereAttId = onereAttId;
	}

	

	/**
	 * Gets the onere att code.
	 *
	 * @return the onere att code
	 */
	public String getOnereAttCode() {
		return this.onereAttCode;
	}

	/**
	 * Sets the onere att code.
	 *
	 * @param onereAttCode the new onere att code
	 */
	public void setOnereAttCode(String onereAttCode) {
		this.onereAttCode = onereAttCode;
	}

	/**
	 * Gets the onere att desc.
	 *
	 * @return the onere att desc
	 */
	public String getOnereAttDesc() {
		return this.onereAttDesc;
	}

	/**
	 * Sets the onere att desc.
	 *
	 * @param onereAttDesc the new onere att desc
	 */
	public void setOnereAttDesc(String onereAttDesc) {
		this.onereAttDesc = onereAttDesc;
	}

	

	

	/**
	 * Gets the siac r doc oneres.
	 *
	 * @return the siac r doc oneres
	 */
	public List<SiacRDocOnere> getSiacRDocOneres() {
		return this.siacRDocOneres;
	}

	/**
	 * Sets the siac r doc oneres.
	 *
	 * @param siacRDocOneres the new siac r doc oneres
	 */
	public void setSiacRDocOneres(List<SiacRDocOnere> siacRDocOneres) {
		this.siacRDocOneres = siacRDocOneres;
	}

	/**
	 * Adds the siac r doc onere.
	 *
	 * @param siacRDocOnere the siac r doc onere
	 * @return the siac r doc onere
	 */
	public SiacRDocOnere addSiacRDocOnere(SiacRDocOnere siacRDocOnere) {
		getSiacRDocOneres().add(siacRDocOnere);
		siacRDocOnere.setSiacDOnereAttivita(this);

		return siacRDocOnere;
	}

	/**
	 * Removes the siac r doc onere.
	 *
	 * @param siacRDocOnere the siac r doc onere
	 * @return the siac r doc onere
	 */
	public SiacRDocOnere removeSiacRDocOnere(SiacRDocOnere siacRDocOnere) {
		getSiacRDocOneres().remove(siacRDocOnere);
		siacRDocOnere.setSiacDOnereAttivita(null);

		return siacRDocOnere;
	}

	/**
	 * Gets the siac r onere attivitas.
	 *
	 * @return the siac r onere attivitas
	 */
	public List<SiacROnereAttivita> getSiacROnereAttivitas() {
		return this.siacROnereAttivitas;
	}

	/**
	 * Sets the siac r onere attivitas.
	 *
	 * @param siacROnereAttivitas the new siac r onere attivitas
	 */
	public void setSiacROnereAttivitas(List<SiacROnereAttivita> siacROnereAttivitas) {
		this.siacROnereAttivitas = siacROnereAttivitas;
	}

	/**
	 * Adds the siac r onere attivita.
	 *
	 * @param siacROnereAttivita the siac r onere attivita
	 * @return the siac r onere attivita
	 */
	public SiacROnereAttivita addSiacROnereAttivita(SiacROnereAttivita siacROnereAttivita) {
		getSiacROnereAttivitas().add(siacROnereAttivita);
		siacROnereAttivita.setSiacDOnereAttivita(this);

		return siacROnereAttivita;
	}

	/**
	 * Removes the siac r onere attivita.
	 *
	 * @param siacROnereAttivita the siac r onere attivita
	 * @return the siac r onere attivita
	 */
	public SiacROnereAttivita removeSiacROnereAttivita(SiacROnereAttivita siacROnereAttivita) {
		getSiacROnereAttivitas().remove(siacROnereAttivita);
		siacROnereAttivita.setSiacDOnereAttivita(null);

		return siacROnereAttivita;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return onereAttId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.onereAttId = uid;
		
	}

}