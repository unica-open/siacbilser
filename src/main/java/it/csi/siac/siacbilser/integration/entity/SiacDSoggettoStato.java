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
 * The persistent class for the siac_d_soggetto_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_soggetto_stato")
@NamedQuery(name="SiacDSoggettoStato.findAll", query="SELECT s FROM SiacDSoggettoStato s")
public class SiacDSoggettoStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SOGGETTO_STATO_SOGGETTOSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SOGGETTO_STATO_SOGGETTO_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SOGGETTO_STATO_SOGGETTOSTATOID_GENERATOR")
	@Column(name="soggetto_stato_id")
	private Integer soggettoStatoId;


	/** The soggetto stato code. */
	@Column(name="soggetto_stato_code")
	private String soggettoStatoCode;

	/** The soggetto stato desc. */
	@Column(name="soggetto_stato_desc")
	private String soggettoStatoDesc;

	
	//bi-directional many-to-one association to SiacRSoggettoStato
	/** The siac r soggetto statos. */
	@OneToMany(mappedBy="siacDSoggettoStato")
	private List<SiacRSoggettoStato> siacRSoggettoStatos;

	/**
	 * Instantiates a new siac d soggetto stato.
	 */
	public SiacDSoggettoStato() {
	}

	/**
	 * Gets the soggetto stato id.
	 *
	 * @return the soggetto stato id
	 */
	public Integer getSoggettoStatoId() {
		return this.soggettoStatoId;
	}

	/**
	 * Sets the soggetto stato id.
	 *
	 * @param soggettoStatoId the new soggetto stato id
	 */
	public void setSoggettoStatoId(Integer soggettoStatoId) {
		this.soggettoStatoId = soggettoStatoId;
	}

	/**
	 * Gets the soggetto stato code.
	 *
	 * @return the soggetto stato code
	 */
	public String getSoggettoStatoCode() {
		return this.soggettoStatoCode;
	}

	/**
	 * Sets the soggetto stato code.
	 *
	 * @param soggettoStatoCode the new soggetto stato code
	 */
	public void setSoggettoStatoCode(String soggettoStatoCode) {
		this.soggettoStatoCode = soggettoStatoCode;
	}

	/**
	 * Gets the soggetto stato desc.
	 *
	 * @return the soggetto stato desc
	 */
	public String getSoggettoStatoDesc() {
		return this.soggettoStatoDesc;
	}

	/**
	 * Sets the soggetto stato desc.
	 *
	 * @param soggettoStatoDesc the new soggetto stato desc
	 */
	public void setSoggettoStatoDesc(String soggettoStatoDesc) {
		this.soggettoStatoDesc = soggettoStatoDesc;
	}



	/**
	 * Gets the siac r soggetto statos.
	 *
	 * @return the siac r soggetto statos
	 */
	public List<SiacRSoggettoStato> getSiacRSoggettoStatos() {
		return this.siacRSoggettoStatos;
	}

	/**
	 * Sets the siac r soggetto statos.
	 *
	 * @param siacRSoggettoStatos the new siac r soggetto statos
	 */
	public void setSiacRSoggettoStatos(List<SiacRSoggettoStato> siacRSoggettoStatos) {
		this.siacRSoggettoStatos = siacRSoggettoStatos;
	}

	/**
	 * Adds the siac r soggetto stato.
	 *
	 * @param siacRSoggettoStato the siac r soggetto stato
	 * @return the siac r soggetto stato
	 */
	public SiacRSoggettoStato addSiacRSoggettoStato(SiacRSoggettoStato siacRSoggettoStato) {
		getSiacRSoggettoStatos().add(siacRSoggettoStato);
		siacRSoggettoStato.setSiacDSoggettoStato(this);

		return siacRSoggettoStato;
	}

	/**
	 * Removes the siac r soggetto stato.
	 *
	 * @param siacRSoggettoStato the siac r soggetto stato
	 * @return the siac r soggetto stato
	 */
	public SiacRSoggettoStato removeSiacRSoggettoStato(SiacRSoggettoStato siacRSoggettoStato) {
		getSiacRSoggettoStatos().remove(siacRSoggettoStato);
		siacRSoggettoStato.setSiacDSoggettoStato(null);

		return siacRSoggettoStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoStatoId = uid;
		
	}

}