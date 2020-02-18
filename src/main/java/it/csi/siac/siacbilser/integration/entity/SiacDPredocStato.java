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
 * The persistent class for the siac_d_predoc_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_predoc_stato")
@NamedQuery(name="SiacDPredocStato.findAll", query="SELECT s FROM SiacDPredocStato s")
public class SiacDPredocStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The predoc stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_PREDOC_STATO_PREDOCSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PREDOC_STATO_PREDOC_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PREDOC_STATO_PREDOCSTATOID_GENERATOR")
	@Column(name="predoc_stato_id")
	private Integer predocStatoId;	

	/** The predoc stato code. */
	@Column(name="predoc_stato_code")
	private String predocStatoCode;

	/** The predoc stato desc. */
	@Column(name="predoc_stato_desc")
	private String predocStatoDesc;

	

	//bi-directional many-to-one association to SiacRPredocStato
	/** The siac r predoc statos. */
	@OneToMany(mappedBy="siacDPredocStato")
	private List<SiacRPredocStato> siacRPredocStatos;

	/**
	 * Instantiates a new siac d predoc stato.
	 */
	public SiacDPredocStato() {
	}

	/**
	 * Gets the predoc stato id.
	 *
	 * @return the predoc stato id
	 */
	public Integer getPredocStatoId() {
		return this.predocStatoId;
	}

	/**
	 * Sets the predoc stato id.
	 *
	 * @param predocStatoId the new predoc stato id
	 */
	public void setPredocStatoId(Integer predocStatoId) {
		this.predocStatoId = predocStatoId;
	}	

	/**
	 * Gets the predoc stato code.
	 *
	 * @return the predoc stato code
	 */
	public String getPredocStatoCode() {
		return this.predocStatoCode;
	}

	/**
	 * Sets the predoc stato code.
	 *
	 * @param predocStatoCode the new predoc stato code
	 */
	public void setPredocStatoCode(String predocStatoCode) {
		this.predocStatoCode = predocStatoCode;
	}

	/**
	 * Gets the predoc stato desc.
	 *
	 * @return the predoc stato desc
	 */
	public String getPredocStatoDesc() {
		return this.predocStatoDesc;
	}

	/**
	 * Sets the predoc stato desc.
	 *
	 * @param predocStatoDesc the new predoc stato desc
	 */
	public void setPredocStatoDesc(String predocStatoDesc) {
		this.predocStatoDesc = predocStatoDesc;
	}

	/**
	 * Gets the siac r predoc statos.
	 *
	 * @return the siac r predoc statos
	 */
	public List<SiacRPredocStato> getSiacRPredocStatos() {
		return this.siacRPredocStatos;
	}

	/**
	 * Sets the siac r predoc statos.
	 *
	 * @param siacRPredocStatos the new siac r predoc statos
	 */
	public void setSiacRPredocStatos(List<SiacRPredocStato> siacRPredocStatos) {
		this.siacRPredocStatos = siacRPredocStatos;
	}

	/**
	 * Adds the siac r predoc stato.
	 *
	 * @param siacRPredocStato the siac r predoc stato
	 * @return the siac r predoc stato
	 */
	public SiacRPredocStato addSiacRPredocStato(SiacRPredocStato siacRPredocStato) {
		getSiacRPredocStatos().add(siacRPredocStato);
		siacRPredocStato.setSiacDPredocStato(this);

		return siacRPredocStato;
	}

	/**
	 * Removes the siac r predoc stato.
	 *
	 * @param siacRPredocStato the siac r predoc stato
	 * @return the siac r predoc stato
	 */
	public SiacRPredocStato removeSiacRPredocStato(SiacRPredocStato siacRPredocStato) {
		getSiacRPredocStatos().remove(siacRPredocStato);
		siacRPredocStato.setSiacDPredocStato(null);

		return siacRPredocStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return predocStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.predocStatoId = uid;
	}

}