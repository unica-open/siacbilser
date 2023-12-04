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
import javax.persistence.Version;


/**
 * The persistent class for the siac_t_eldoc database table.
 * 
 */
@Entity
@Table(name="siac_t_elenco_doc_num")
@NamedQuery(name="SiacTElencoDocNum.findAll", query="SELECT s FROM SiacTElencoDocNum s")
public class SiacTElencoDocNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The eldoc num id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ELENCO_DOC_ELENCO_DOCNUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ELENCO_DOC_NUM_ELDOC_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ELENCO_DOC_ELENCO_DOCNUMID_GENERATOR")
	@Column(name="eldoc_num_id")
	private Integer eldocNumId;
	
	/** The eldoc numero. */
	@Version
	@Column(name="eldoc_numero")
	private Integer eldocNumero;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t doc. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	/**
	 * Instantiates a new siac t eldoc num.
	 */
	public SiacTElencoDocNum() {
	}

	/**
	 * Gets the eldoc num id.
	 *
	 * @return the eldoc num id
	 */
	public Integer getElencoDocNumId() {
		return this.eldocNumId;
	}

	/**
	 * Sets the eldoc num id.
	 *
	 * @param eldocId the new eldoc num id
	 */
	public void setElencoDocNumId(Integer eldocId) {
		this.eldocNumId = eldocId;
	}

	/**
	 * @return the eldocNumId
	 */
	public Integer getEldocNumId() {
		return eldocNumId;
	}

	/**
	 * @param eldocNumId the eldocNumId to set
	 */
	public void setEldocNumId(Integer eldocNumId) {
		this.eldocNumId = eldocNumId;
	}

	/**
	 * @return the eldocNumero
	 */
	public Integer getEldocNumero() {
		return eldocNumero;
	}

	/**
	 * @param eldocNumero the eldocNumero to set
	 */
	public void setEldocNumero(Integer eldocNumero) {
		this.eldocNumero = eldocNumero;
	}

	/**
	 * @return the siacTBil
	 */
	public SiacTBil getSiacTBil() {
		return siacTBil;
	}

	/**
	 * @param siacTBil the siacTBil to set
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return eldocNumId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.eldocNumId = uid;		
	}


}