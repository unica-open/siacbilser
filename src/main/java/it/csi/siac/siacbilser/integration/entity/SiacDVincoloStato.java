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
 * The persistent class for the siac_d_vincolo_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_vincolo_stato")
@NamedQuery(name="SiacDVincoloStato.findAll", query="SELECT s FROM SiacDVincoloStato s")
public class SiacDVincoloStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The vincolo stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_VINCOLO_STATO_VINCOLOSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_VINCOLO_STATO_VINCOLO_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_VINCOLO_STATO_VINCOLOSTATOID_GENERATOR")
	@Column(name="vincolo_stato_id")
	private Integer vincoloStatoId;

	/** The vincolo stato code. */
	@Column(name="vincolo_stato_code")
	private String vincoloStatoCode;

	/** The vincolo stato desc. */
	@Column(name="vincolo_stato_desc")
	private String vincoloStatoDesc;

	//bi-directional many-to-one association to SiacRVincoloStato
	/** The siac r vincolo statos. */
	@OneToMany(mappedBy="siacDVincoloStato")
	private List<SiacRVincoloStato> siacRVincoloStatos;

	/**
	 * Instantiates a new siac d vincolo stato.
	 */
	public SiacDVincoloStato() {
	}

	/**
	 * Gets the vincolo stato id.
	 *
	 * @return the vincolo stato id
	 */
	public Integer getVincoloStatoId() {
		return this.vincoloStatoId;
	}

	/**
	 * Sets the vincolo stato id.
	 *
	 * @param vincoloStatoId the new vincolo stato id
	 */
	public void setVincoloStatoId(Integer vincoloStatoId) {
		this.vincoloStatoId = vincoloStatoId;
	}

	/**
	 * Gets the vincolo stato code.
	 *
	 * @return the vincolo stato code
	 */
	public String getVincoloStatoCode() {
		return this.vincoloStatoCode;
	}

	/**
	 * Sets the vincolo stato code.
	 *
	 * @param vincoloStatoCode the new vincolo stato code
	 */
	public void setVincoloStatoCode(String vincoloStatoCode) {
		this.vincoloStatoCode = vincoloStatoCode;
	}

	/**
	 * Gets the vincolo stato desc.
	 *
	 * @return the vincolo stato desc
	 */
	public String getVincoloStatoDesc() {
		return this.vincoloStatoDesc;
	}

	/**
	 * Sets the vincolo stato desc.
	 *
	 * @param vincoloStatoDesc the new vincolo stato desc
	 */
	public void setVincoloStatoDesc(String vincoloStatoDesc) {
		this.vincoloStatoDesc = vincoloStatoDesc;
	}

	/**
	 * Gets the siac r vincolo statos.
	 *
	 * @return the siac r vincolo statos
	 */
	public List<SiacRVincoloStato> getSiacRVincoloStatos() {
		return this.siacRVincoloStatos;
	}

	/**
	 * Sets the siac r vincolo statos.
	 *
	 * @param siacRVincoloStatos the new siac r vincolo statos
	 */
	public void setSiacRVincoloStatos(List<SiacRVincoloStato> siacRVincoloStatos) {
		this.siacRVincoloStatos = siacRVincoloStatos;
	}

	/**
	 * Adds the siac r vincolo stato.
	 *
	 * @param siacRVincoloStato the siac r vincolo stato
	 * @return the siac r vincolo stato
	 */
	public SiacRVincoloStato addSiacRVincoloStato(SiacRVincoloStato siacRVincoloStato) {
		getSiacRVincoloStatos().add(siacRVincoloStato);
		siacRVincoloStato.setSiacDVincoloStato(this);

		return siacRVincoloStato;
	}

	/**
	 * Removes the siac r vincolo stato.
	 *
	 * @param siacRVincoloStato the siac r vincolo stato
	 * @return the siac r vincolo stato
	 */
	public SiacRVincoloStato removeSiacRVincoloStato(SiacRVincoloStato siacRVincoloStato) {
		getSiacRVincoloStatos().remove(siacRVincoloStato);
		siacRVincoloStato.setSiacDVincoloStato(null);

		return siacRVincoloStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return vincoloStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.vincoloStatoId = uid;		
	}

}