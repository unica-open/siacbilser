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
 * The persistent class for the siac_d_gsa_classif_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_gsa_classif_stato")
@NamedQuery(name="SiacDGsaClassifStato.findAll", query="SELECT s FROM SiacDGsaClassifStato s")
public class SiacDGsaClassifStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_DOC_STATO_DOCSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_DOC_STATO_DOC_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_DOC_STATO_DOCSTATOID_GENERATOR")
	@Column(name="gsa_classif_stato_id")
	private Integer gsaClassifStatoId;
	
	/** The doc stato code. */
	@Column(name="gsa_classif_stato_code")
	private String gsaClassifStatoCode;

	/** The doc stato desc. */
	@Column(name="gsa_classif_stato_desc")
	private String gsaClassifStatoDesc;
	

	//bi-directional many-to-one association to SiacRGsaClassifStato
	/** The siac r doc statos. */
	@OneToMany(mappedBy="siacDGsaClassifStato")
	private List<SiacRGsaClassifStato> siacRGsaClassifStatos;

	/**
	 * Instantiates a new siac d doc stato.
	 */
	public SiacDGsaClassifStato() {
	}

	/**
	 * Gets the doc stato id.
	 *
	 * @return the doc stato id
	 */
	public Integer getGsaClassifStatoId() {
		return this.gsaClassifStatoId;
	}

	/**
	 * Sets the doc stato id.
	 *
	 * @param gsaClassifStatoId the new doc stato id
	 */
	public void setGsaClassifStatoId(Integer gsaClassifStatoId) {
		this.gsaClassifStatoId = gsaClassifStatoId;
	}


	/**
	 * Gets the doc stato code.
	 *
	 * @return the doc stato code
	 */
	public String getGsaClassifStatoCode() {
		return this.gsaClassifStatoCode;
	}

	/**
	 * Sets the doc stato code.
	 *
	 * @param gsaClassifStatoCode the new doc stato code
	 */
	public void setGsaClassifStatoCode(String gsaClassifStatoCode) {
		this.gsaClassifStatoCode = gsaClassifStatoCode;
	}

	/**
	 * Gets the doc stato desc.
	 *
	 * @return the doc stato desc
	 */
	public String getGsaClassifStatoDesc() {
		return this.gsaClassifStatoDesc;
	}

	/**
	 * Sets the doc stato desc.
	 *
	 * @param gsaClassifStatoDesc the new doc stato desc
	 */
	public void setGsaClassifStatoDesc(String gsaClassifStatoDesc) {
		this.gsaClassifStatoDesc = gsaClassifStatoDesc;
	}

	

	


	/**
	 * Gets the siac r doc statos.
	 *
	 * @return the siac r doc statos
	 */
	public List<SiacRGsaClassifStato> getSiacRGsaClassifStatos() {
		return this.siacRGsaClassifStatos;
	}

	/**
	 * Sets the siac r doc statos.
	 *
	 * @param siacRGsaClassifStatos the new siac r doc statos
	 */
	public void setSiacRGsaClassifStatos(List<SiacRGsaClassifStato> siacRGsaClassifStatos) {
		this.siacRGsaClassifStatos = siacRGsaClassifStatos;
	}

	/**
	 * Adds the siac r doc stato.
	 *
	 * @param siacRGsaClassifStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRGsaClassifStato addSiacRGsaClassifStato(SiacRGsaClassifStato siacRGsaClassifStato) {
		getSiacRGsaClassifStatos().add(siacRGsaClassifStato);
		siacRGsaClassifStato.setSiacDGsaClassifStato(this);

		return siacRGsaClassifStato;
	}

	/**
	 * Removes the siac r doc stato.
	 *
	 * @param siacRGsaClassifStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRGsaClassifStato removeSiacRGsaClassifStato(SiacRGsaClassifStato siacRGsaClassifStato) {
		getSiacRGsaClassifStatos().remove(siacRGsaClassifStato);
		siacRGsaClassifStato.setSiacDGsaClassifStato(null);

		return siacRGsaClassifStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return gsaClassifStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.gsaClassifStatoId = uid;
		
	}

}