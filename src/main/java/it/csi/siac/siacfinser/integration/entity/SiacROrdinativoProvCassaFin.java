/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_ordinativo_prov_cassa database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_prov_cassa")
public class SiacROrdinativoProvCassaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_PROV_CASSA_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_ordinativo_prov_cassa_ord_provc_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_PROV_CASSA_ID_GENERATOR")
	@Column(name="ord_provc_id")
	private Integer ordProvcId;

	@Column(name="ord_provc_importo")
	private BigDecimal ordProvcImporto;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativoFin siacTOrdinativo;

	//bi-directional many-to-one association to SiacTProvCassaFin
	@ManyToOne
	@JoinColumn(name="provc_id")
	private SiacTProvCassaFin siacTProvCassa;

	public SiacROrdinativoProvCassaFin() {
	}

	public Integer getOrdProvcId() {
		return this.ordProvcId;
	}

	public void setOrdProvcId(Integer ordProvcId) {
		this.ordProvcId = ordProvcId;
	}

	public BigDecimal getOrdProvcImporto() {
		return this.ordProvcImporto;
	}

	public void setOrdProvcImporto(BigDecimal ordProvcImporto) {
		this.ordProvcImporto = ordProvcImporto;
	}

	public SiacTOrdinativoFin getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	public void setSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	public SiacTProvCassaFin getSiacTProvCassa() {
		return this.siacTProvCassa;
	}

	public void setSiacTProvCassa(SiacTProvCassaFin siacTProvCassa) {
		this.siacTProvCassa = siacTProvCassa;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordProvcId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordProvcId = uid;
	}

}