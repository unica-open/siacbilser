/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_ivamov database table.
 * 
 */
@Entity
@Table(name="siac_t_ivamov")
@NamedQuery(name="SiacTIvamov.findAll", query="SELECT s FROM SiacTIvamov s")
public class SiacTIvamov extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivamov id. */
	@Id
	@SequenceGenerator(name="SIAC_T_IVAMOV_IVAMOVID_GENERATOR", sequenceName="SIAC_T_IVAMOV_IVAMOV_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_IVAMOV_IVAMOVID_GENERATOR")
	@Column(name="ivamov_id")
	private Integer ivamovId;

	/** The ivamov imponibile. */
	@Column(name="ivamov_imponibile")
	private BigDecimal ivamovImponibile;

	/** The ivamov imposta. */
	@Column(name="ivamov_imposta")
	private BigDecimal ivamovImposta;

	/** The ivamov totale. */
	@Column(name="ivamov_totale")
	private BigDecimal ivamovTotale;

	//bi-directional many-to-one association to SiacRIvamov
	/** The siac r ivamovs. */
	@OneToMany(mappedBy="siacTIvamov", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRIvamov> siacRIvamovs;

	//bi-directional many-to-one association to SiacTIvaAliquota
	/** The siac t iva aliquota. */
	@ManyToOne
	@JoinColumn(name="ivaaliquota_id")
	private SiacTIvaAliquota siacTIvaAliquota;

	/**
	 * Instantiates a new siac t ivamov.
	 */
	public SiacTIvamov() {
	}

	/**
	 * Gets the ivamov id.
	 *
	 * @return the ivamov id
	 */
	public Integer getIvamovId() {
		return this.ivamovId;
	}

	/**
	 * Sets the ivamov id.
	 *
	 * @param ivamovId the new ivamov id
	 */
	public void setIvamovId(Integer ivamovId) {
		this.ivamovId = ivamovId;
	}

	/**
	 * Gets the ivamov imponibile.
	 *
	 * @return the ivamov imponibile
	 */
	public BigDecimal getIvamovImponibile() {
		return this.ivamovImponibile;
	}

	/**
	 * Sets the ivamov imponibile.
	 *
	 * @param ivamovImponibile the new ivamov imponibile
	 */
	public void setIvamovImponibile(BigDecimal ivamovImponibile) {
		this.ivamovImponibile = ivamovImponibile;
	}

	/**
	 * Gets the ivamov imposta.
	 *
	 * @return the ivamov imposta
	 */
	public BigDecimal getIvamovImposta() {
		return this.ivamovImposta;
	}

	/**
	 * Sets the ivamov imposta.
	 *
	 * @param ivamovImposta the new ivamov imposta
	 */
	public void setIvamovImposta(BigDecimal ivamovImposta) {
		this.ivamovImposta = ivamovImposta;
	}

	/**
	 * Gets the ivamov totale.
	 *
	 * @return the ivamov totale
	 */
	public BigDecimal getIvamovTotale() {
		return this.ivamovTotale;
	}

	/**
	 * Sets the ivamov totale.
	 *
	 * @param ivamovTotale the new ivamov totale
	 */
	public void setIvamovTotale(BigDecimal ivamovTotale) {
		this.ivamovTotale = ivamovTotale;
	}

	/**
	 * Gets the siac r ivamovs.
	 *
	 * @return the siac r ivamovs
	 */
	public List<SiacRIvamov> getSiacRIvamovs() {
		return this.siacRIvamovs;
	}

	/**
	 * Sets the siac r ivamovs.
	 *
	 * @param siacRIvamovs the new siac r ivamovs
	 */
	public void setSiacRIvamovs(List<SiacRIvamov> siacRIvamovs) {
		this.siacRIvamovs = siacRIvamovs;
	}

	/**
	 * Adds the siac r ivamov.
	 *
	 * @param siacRIvamov the siac r ivamov
	 * @return the siac r ivamov
	 */
	public SiacRIvamov addSiacRIvamov(SiacRIvamov siacRIvamov) {
		getSiacRIvamovs().add(siacRIvamov);
		siacRIvamov.setSiacTIvamov(this);

		return siacRIvamov;
	}

	/**
	 * Removes the siac r ivamov.
	 *
	 * @param siacRIvamov the siac r ivamov
	 * @return the siac r ivamov
	 */
	public SiacRIvamov removeSiacRIvamov(SiacRIvamov siacRIvamov) {
		getSiacRIvamovs().remove(siacRIvamov);
		siacRIvamov.setSiacTIvamov(null);

		return siacRIvamov;
	}

	/**
	 * Gets the siac t iva aliquota.
	 *
	 * @return the siac t iva aliquota
	 */
	public SiacTIvaAliquota getSiacTIvaAliquota() {
		return this.siacTIvaAliquota;
	}

	/**
	 * Sets the siac t iva aliquota.
	 *
	 * @param siacTIvaAliquota the new siac t iva aliquota
	 */
	public void setSiacTIvaAliquota(SiacTIvaAliquota siacTIvaAliquota) {
		this.siacTIvaAliquota = siacTIvaAliquota;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivamovId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivamovId = uid;
	}

}