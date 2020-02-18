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
 * The persistent class for the siac_d_doc_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_doc_stato")
@NamedQuery(name="SiacDDocStato.findAll", query="SELECT s FROM SiacDDocStato s")
public class SiacDDocStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The doc stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_DOC_STATO_DOCSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_DOC_STATO_DOC_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_DOC_STATO_DOCSTATOID_GENERATOR")
	@Column(name="doc_stato_id")
	private Integer docStatoId;
	
	/** The doc stato code. */
	@Column(name="doc_stato_code")
	private String docStatoCode;

	/** The doc stato desc. */
	@Column(name="doc_stato_desc")
	private String docStatoDesc;
	

	//bi-directional many-to-one association to SiacRDocStato
	/** The siac r doc statos. */
	@OneToMany(mappedBy="siacDDocStato")
	private List<SiacRDocStato> siacRDocStatos;

	/**
	 * Instantiates a new siac d doc stato.
	 */
	public SiacDDocStato() {
	}

	/**
	 * Gets the doc stato id.
	 *
	 * @return the doc stato id
	 */
	public Integer getDocStatoId() {
		return this.docStatoId;
	}

	/**
	 * Sets the doc stato id.
	 *
	 * @param docStatoId the new doc stato id
	 */
	public void setDocStatoId(Integer docStatoId) {
		this.docStatoId = docStatoId;
	}


	/**
	 * Gets the doc stato code.
	 *
	 * @return the doc stato code
	 */
	public String getDocStatoCode() {
		return this.docStatoCode;
	}

	/**
	 * Sets the doc stato code.
	 *
	 * @param docStatoCode the new doc stato code
	 */
	public void setDocStatoCode(String docStatoCode) {
		this.docStatoCode = docStatoCode;
	}

	/**
	 * Gets the doc stato desc.
	 *
	 * @return the doc stato desc
	 */
	public String getDocStatoDesc() {
		return this.docStatoDesc;
	}

	/**
	 * Sets the doc stato desc.
	 *
	 * @param docStatoDesc the new doc stato desc
	 */
	public void setDocStatoDesc(String docStatoDesc) {
		this.docStatoDesc = docStatoDesc;
	}

	

	


	/**
	 * Gets the siac r doc statos.
	 *
	 * @return the siac r doc statos
	 */
	public List<SiacRDocStato> getSiacRDocStatos() {
		return this.siacRDocStatos;
	}

	/**
	 * Sets the siac r doc statos.
	 *
	 * @param siacRDocStatos the new siac r doc statos
	 */
	public void setSiacRDocStatos(List<SiacRDocStato> siacRDocStatos) {
		this.siacRDocStatos = siacRDocStatos;
	}

	/**
	 * Adds the siac r doc stato.
	 *
	 * @param siacRDocStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRDocStato addSiacRDocStato(SiacRDocStato siacRDocStato) {
		getSiacRDocStatos().add(siacRDocStato);
		siacRDocStato.setSiacDDocStato(this);

		return siacRDocStato;
	}

	/**
	 * Removes the siac r doc stato.
	 *
	 * @param siacRDocStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRDocStato removeSiacRDocStato(SiacRDocStato siacRDocStato) {
		getSiacRDocStatos().remove(siacRDocStato);
		siacRDocStato.setSiacDDocStato(null);

		return siacRDocStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docStatoId = uid;
		
	}

}