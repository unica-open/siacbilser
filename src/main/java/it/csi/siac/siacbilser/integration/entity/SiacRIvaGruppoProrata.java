/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
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
 * The persistent class for the siac_r_iva_gruppo_prorata database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_gruppo_prorata")
@NamedQuery(name="SiacRIvaGruppoProrata.findAll", query="SELECT s FROM SiacRIvaGruppoProrata s")
public class SiacRIvaGruppoProrata extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivagrupro id. */
	@Id
	@SequenceGenerator(name="SIAC_R_IVA_GRUPPO_PRORATA_IVAGRUPROID_GENERATOR", sequenceName="SIAC_R_IVA_GRUPPO_PRORATA_IVAGRUPRO_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_IVA_GRUPPO_PRORATA_IVAGRUPROID_GENERATOR")
	@Column(name="ivagrupro_id")
	private Integer ivagruproId;

	/** The ivagrupro anno. */
	@Column(name="ivagrupro_anno")
	private Integer ivagruproAnno;
	
	/** The ivagrupro ivaprecedente. */
	@Column(name="ivagru_ivaprecedente")
	private BigDecimal ivagruIvaprecedente;

	//bi-directional many-to-one association to SiacTIvaGruppo
	/** The siac t iva gruppo. */
	@ManyToOne
	@JoinColumn(name="ivagru_id")
	private SiacTIvaGruppo siacTIvaGruppo;

	//bi-directional many-to-one association to SiacTIvaProrata
	/** The siac t iva prorata. */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	@JoinColumn(name="ivapro_id")
	private SiacTIvaProrata siacTIvaProrata;

	/**
	 * Instantiates a new siac r iva gruppo prorata.
	 */
	public SiacRIvaGruppoProrata() {
	}

	/**
	 * Gets the ivagrupro id.
	 *
	 * @return the ivagrupro id
	 */
	public Integer getIvagruproId() {
		return this.ivagruproId;
	}

	/**
	 * Sets the ivagrupro id.
	 *
	 * @param ivagruproId the new ivagrupro id
	 */
	public void setIvagruproId(Integer ivagruproId) {
		this.ivagruproId = ivagruproId;
	}

	/**
	 * Gets the ivagrupro anno.
	 *
	 * @return the ivagrupro anno
	 */
	public Integer getIvagruproAnno() {
		return this.ivagruproAnno;
	}

	/**
	 * Sets the ivagrupro anno.
	 *
	 * @param ivagruproAnno the new ivagrupro anno
	 */
	public void setIvagruproAnno(Integer ivagruproAnno) {
		this.ivagruproAnno = ivagruproAnno;
	}
	
	/**
	 * @return the ivagruIvaprecedente
	 */
	public BigDecimal getIvagruIvaprecedente() {
		return ivagruIvaprecedente;
	}

	/**
	 * @param ivagruIvaprecedente the ivagruIvaprecedente to set
	 */
	public void setIvagruIvaprecedente(BigDecimal ivagruIvaprecedente) {
		this.ivagruIvaprecedente = ivagruIvaprecedente;
	}

	/**
	 * Gets the siac t iva gruppo.
	 *
	 * @return the siac t iva gruppo
	 */
	public SiacTIvaGruppo getSiacTIvaGruppo() {
		return this.siacTIvaGruppo;
	}

	/**
	 * Sets the siac t iva gruppo.
	 *
	 * @param siacTIvaGruppo the new siac t iva gruppo
	 */
	public void setSiacTIvaGruppo(SiacTIvaGruppo siacTIvaGruppo) {
		this.siacTIvaGruppo = siacTIvaGruppo;
	}

	/**
	 * Gets the siac t iva prorata.
	 *
	 * @return the siac t iva prorata
	 */
	public SiacTIvaProrata getSiacTIvaProrata() {
		return this.siacTIvaProrata;
	}

	/**
	 * Sets the siac t iva prorata.
	 *
	 * @param siacTIvaProrata the new siac t iva prorata
	 */
	public void setSiacTIvaProrata(SiacTIvaProrata siacTIvaProrata) {
		this.siacTIvaProrata = siacTIvaProrata;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivagruproId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivagruproId = uid;
	}
}