/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

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
 * The persistent class for the siac_r_ordinativo_prov_cassa database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_prov_cassa")
@NamedQuery(name="SiacROrdinativoProvCassa.findAll", query="SELECT s FROM SiacROrdinativoProvCassa s")
public class SiacROrdinativoProvCassa extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord provc id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_PROV_CASSA_ORDPROVCID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ORDINATIVO_PROV_CASSA_ORD_PROVC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_PROV_CASSA_ORDPROVCID_GENERATOR")
	@Column(name="ord_provc_id")
	private Integer ordProvcId;

	/** The ord provc importo. */
	@Column(name="ord_provc_importo")
	private BigDecimal ordProvcImporto;

	//bi-directional many-to-one association to SiacTOrdinativo
	/** The siac t ordinativo. */
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativo siacTOrdinativo;

	//bi-directional many-to-one association to SiacTProvCassa
	/** The siac t prov cassa. */
	@ManyToOne
	@JoinColumn(name="provc_id")
	private SiacTProvCassa siacTProvCassa;

	/**
	 * Instantiates a new siac r ordinativo prov cassa.
	 */
	public SiacROrdinativoProvCassa() {
	}

	/**
	 * Gets the ord provc id.
	 *
	 * @return the ord provc id
	 */
	public Integer getOrdProvcId() {
		return this.ordProvcId;
	}

	/**
	 * Sets the ord provc id.
	 *
	 * @param ordProvcId the new ord provc id
	 */
	public void setOrdProvcId(Integer ordProvcId) {
		this.ordProvcId = ordProvcId;
	}

	


	/**
	 * Gets the ord provc importo.
	 *
	 * @return the ord provc importo
	 */
	public BigDecimal getOrdProvcImporto() {
		return this.ordProvcImporto;
	}

	/**
	 * Sets the ord provc importo.
	 *
	 * @param ordProvcImporto the new ord provc importo
	 */
	public void setOrdProvcImporto(BigDecimal ordProvcImporto) {
		this.ordProvcImporto = ordProvcImporto;
	}

	/**
	 * Gets the siac t ordinativo.
	 *
	 * @return the siac t ordinativo
	 */
	public SiacTOrdinativo getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	/**
	 * Sets the siac t ordinativo.
	 *
	 * @param siacTOrdinativo the new siac t ordinativo
	 */
	public void setSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	/**
	 * Gets the siac t prov cassa.
	 *
	 * @return the siac t prov cassa
	 */
	public SiacTProvCassa getSiacTProvCassa() {
		return this.siacTProvCassa;
	}

	/**
	 * Sets the siac t prov cassa.
	 *
	 * @param siacTProvCassa the new siac t prov cassa
	 */
	public void setSiacTProvCassa(SiacTProvCassa siacTProvCassa) {
		this.siacTProvCassa = siacTProvCassa;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordProvcId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ordProvcId = uid;
	}

}