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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_predoc database table.
 * 
 */
@Entity
@Table(name="siac_t_predoc_num")
@NamedQuery(name="SiacTPredocNum.findAll", query="SELECT s FROM SiacTPredocNum s")
public class SiacTPredocNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The predoc num id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PREDOC_PREDOCNUMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PREDOC_NUM_PREDOC_NUM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PREDOC_PREDOCNUMID_GENERATOR")
	@Column(name="predoc_num_id")
	private Integer predocNumId;
	
	/** The predoc numero. */
	@Version
	@Column(name="predoc_numero")
	private Integer predocNumero;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac t predoc. */
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredoc siacTPredoc;
	
	/**
	 * Instantiates a new siac t predoc num.
	 */
	public SiacTPredocNum() {
	}

	/**
	 * Gets the pre doc num id.
	 *
	 * @return the pre doc num id
	 */
	public Integer getPreDocNumId() {
		return this.predocNumId;
	}

	/**
	 * Sets the pre doc num id.
	 *
	 * @param predocId the new pre doc num id
	 */
	public void setPreDocNumId(Integer predocId) {
		this.predocNumId = predocId;
	}

	/**
	 * Gets the predoc numero.
	 *
	 * @return the predoc numero
	 */
	public Integer getPredocNumero() {
		return this.predocNumero;
	}

	/**
	 * Sets the predoc numero.
	 *
	 * @param predocNumero the new predoc numero
	 */
	public void setPredocNumero(Integer predocNumero) {
		this.predocNumero = predocNumero;
	}

	/**
	 * Gets the siac t predoc.
	 *
	 * @return the siac t predoc
	 */
	public SiacTPredoc getSiacTPredoc() {
		return this.siacTPredoc;
	}

	/**
	 * Sets the siac t predoc.
	 *
	 * @param siacTPredoc the new siac t predoc
	 */
	public void setSiacTPredoc(SiacTPredoc siacTPredoc) {
		this.siacTPredoc = siacTPredoc;
	}	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return predocNumId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.predocNumId = uid;		
	}


}