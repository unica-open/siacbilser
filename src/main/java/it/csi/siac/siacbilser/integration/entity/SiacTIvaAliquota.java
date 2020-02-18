/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

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

/**
 * The persistent class for the siac_t_iva_aliquota database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_aliquota")
@NamedQuery(name="SiacTIvaAliquota.findAll", query="SELECT s FROM SiacTIvaAliquota s")
public class SiacTIvaAliquota extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivaaliquota id. */
	@Id
	@SequenceGenerator(name="SIAC_T_IVA_ALIQUOTA_IVAALIQUOTAID_GENERATOR", sequenceName="SIAC_T_IVA_ALIQUOTA_IVAALIQUOTA_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_IVA_ALIQUOTA_IVAALIQUOTAID_GENERATOR")
	@Column(name="ivaaliquota_id")
	private Integer ivaaliquotaId;

	/** The ivaaliquota code. */
	@Column(name="ivaaliquota_code")
	private String ivaaliquotaCode;

	/** The ivaaliquota desc. */
	@Column(name="ivaaliquota_desc")
	private String ivaaliquotaDesc;

	/** The ivaaliquota perc. */
	@Column(name="ivaaliquota_perc")
	private BigDecimal ivaaliquotaPerc;

	/** The ivaaliquota perc indetr. */
	@Column(name="ivaaliquota_perc_indetr")
	private BigDecimal ivaaliquotaPercIndetr;

	/** The ivaaliquota_split indetr. */
	@Column(name="ivaaliquota_split")
	private Boolean ivaaliquotaSplit;
	
	//bi-directional many-to-one association to SiacDIvaOperazioneTipo
	/** The siac d iva operazione tipo. */
	@ManyToOne
	@JoinColumn(name="ivaop_tipo_id")
	private SiacDIvaOperazioneTipo siacDIvaOperazioneTipo;

	//bi-directional many-to-one association to SiacTIvamov
	/** The siac t ivamovs. */
	@OneToMany(mappedBy="siacTIvaAliquota")
	private List<SiacTIvamov> siacTIvamovs;
	
	//bi-directional many-to-one association to SiacTIvaRegistroTotale
	@OneToMany(mappedBy="siacTIvaAliquota")
	private List<SiacTIvaRegistroTotale> siacTIvaRegistroTotales;
	
	
	//bi-directional many-to-one association to SiacDIvaOperazioneTipo
	@ManyToOne
	@JoinColumn(name="ivaaliquota_tipo_id")
	private SiacDIvaAliquotaTipo siacDIvaAliquotaTipo;
	

	/**
	 * Instantiates a new siac t iva aliquota.
	 */
	public SiacTIvaAliquota() {
	}

	/**
	 * Gets the ivaaliquota id.
	 *
	 * @return the ivaaliquota id
	 */
	public Integer getIvaaliquotaId() {
		return this.ivaaliquotaId;
	}

	/**
	 * Sets the ivaaliquota id.
	 *
	 * @param ivaaliquotaId the new ivaaliquota id
	 */
	public void setIvaaliquotaId(Integer ivaaliquotaId) {
		this.ivaaliquotaId = ivaaliquotaId;
	}

	/**
	 * Gets the ivaaliquota code.
	 *
	 * @return the ivaaliquota code
	 */
	public String getIvaaliquotaCode() {
		return this.ivaaliquotaCode;
	}

	/**
	 * Sets the ivaaliquota code.
	 *
	 * @param ivaaliquotaCode the new ivaaliquota code
	 */
	public void setIvaaliquotaCode(String ivaaliquotaCode) {
		this.ivaaliquotaCode = ivaaliquotaCode;
	}

	/**
	 * Gets the ivaaliquota desc.
	 *
	 * @return the ivaaliquota desc
	 */
	public String getIvaaliquotaDesc() {
		return this.ivaaliquotaDesc;
	}

	/**
	 * Sets the ivaaliquota desc.
	 *
	 * @param ivaaliquotaDesc the new ivaaliquota desc
	 */
	public void setIvaaliquotaDesc(String ivaaliquotaDesc) {
		this.ivaaliquotaDesc = ivaaliquotaDesc;
	}

	/**
	 * Gets the ivaaliquota perc.
	 *
	 * @return the ivaaliquota perc
	 */
	public BigDecimal getIvaaliquotaPerc() {
		return this.ivaaliquotaPerc;
	}

	/**
	 * Sets the ivaaliquota perc.
	 *
	 * @param ivaaliquotaPerc the new ivaaliquota perc
	 */
	public void setIvaaliquotaPerc(BigDecimal ivaaliquotaPerc) {
		this.ivaaliquotaPerc = ivaaliquotaPerc;
	}

	/**
	 * Gets the ivaaliquota perc indetr.
	 *
	 * @return the ivaaliquota perc indetr
	 */
	public BigDecimal getIvaaliquotaPercIndetr() {
		return this.ivaaliquotaPercIndetr;
	}

	/**
	 * Sets the ivaaliquota perc indetr.
	 *
	 * @param ivaaliquotaPercIndetr the new ivaaliquota perc indetr
	 */
	public void setIvaaliquotaPercIndetr(BigDecimal ivaaliquotaPercIndetr) {
		this.ivaaliquotaPercIndetr = ivaaliquotaPercIndetr;
	}

	/**
	 * Gets the siac d iva operazione tipo.
	 *
	 * @return the siac d iva operazione tipo
	 */
	public SiacDIvaOperazioneTipo getSiacDIvaOperazioneTipo() {
		return this.siacDIvaOperazioneTipo;
	}

	/**
	 * Sets the siac d iva operazione tipo.
	 *
	 * @param siacDIvaOperazioneTipo the new siac d iva operazione tipo
	 */
	public void setSiacDIvaOperazioneTipo(SiacDIvaOperazioneTipo siacDIvaOperazioneTipo) {
		this.siacDIvaOperazioneTipo = siacDIvaOperazioneTipo;
	}

	/**
	 * Gets the siac t ivamovs.
	 *
	 * @return the siac t ivamovs
	 */
	public List<SiacTIvamov> getSiacTIvamovs() {
		return this.siacTIvamovs;
	}

	/**
	 * Sets the siac t ivamovs.
	 *
	 * @param siacTIvamovs the new siac t ivamovs
	 */
	public void setSiacTIvamovs(List<SiacTIvamov> siacTIvamovs) {
		this.siacTIvamovs = siacTIvamovs;
	}

	/**
	 * Adds the siac t ivamov.
	 *
	 * @param siacTIvamov the siac t ivamov
	 * @return the siac t ivamov
	 */
	public SiacTIvamov addSiacTIvamov(SiacTIvamov siacTIvamov) {
		getSiacTIvamovs().add(siacTIvamov);
		siacTIvamov.setSiacTIvaAliquota(this);

		return siacTIvamov;
	}

	/**
	 * Removes the siac t ivamov.
	 *
	 * @param siacTIvamov the siac t ivamov
	 * @return the siac t ivamov
	 */
	public SiacTIvamov removeSiacTIvamov(SiacTIvamov siacTIvamov) {
		getSiacTIvamovs().remove(siacTIvamov);
		siacTIvamov.setSiacTIvaAliquota(null);

		return siacTIvamov;
	}
	
	public List<SiacTIvaRegistroTotale> getSiacTIvaRegistroTotales() {
		return this.siacTIvaRegistroTotales;
	}

	public void setSiacTIvaRegistroTotales(List<SiacTIvaRegistroTotale> siacTIvaRegistroTotales) {
		this.siacTIvaRegistroTotales = siacTIvaRegistroTotales;
	}

	public SiacTIvaRegistroTotale addSiacTIvaRegistroTotale(SiacTIvaRegistroTotale siacTIvaRegistroTotale) {
		getSiacTIvaRegistroTotales().add(siacTIvaRegistroTotale);
		siacTIvaRegistroTotale.setSiacTIvaAliquota(this);

		return siacTIvaRegistroTotale;
	}

	public SiacTIvaRegistroTotale removeSiacTIvaRegistroTotale(SiacTIvaRegistroTotale siacTIvaRegistroTotale) {
		getSiacTIvaRegistroTotales().remove(siacTIvaRegistroTotale);
		siacTIvaRegistroTotale.setSiacTIvaAliquota(null);

		return siacTIvaRegistroTotale;
	}
	
	/**
	 * @return the siacDIvaAliquotaTipo
	 */
	public SiacDIvaAliquotaTipo getSiacDIvaAliquotaTipo() {
		return siacDIvaAliquotaTipo;
	}

	/**
	 * @param siacDIvaAliquotaTipo the siacDIvaAliquotaTipo to set
	 */
	public void setSiacDIvaAliquotaTipo(SiacDIvaAliquotaTipo siacDIvaAliquotaTipo) {
		this.siacDIvaAliquotaTipo = siacDIvaAliquotaTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivaaliquotaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivaaliquotaId = uid;
	}

	public Boolean getIvaaliquotaSplit() {
		return ivaaliquotaSplit;
	}

	public void setIvaaliquotaSplit(Boolean ivaaliquotaSplit) {
		this.ivaaliquotaSplit = ivaaliquotaSplit;
	}
	
}