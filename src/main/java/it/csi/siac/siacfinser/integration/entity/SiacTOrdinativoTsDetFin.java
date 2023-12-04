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
 * The persistent class for the siac_t_ordinativo_ts_det database table.
 * 
 */
@Entity
@Table(name="siac_t_ordinativo_ts_det")
public class SiacTOrdinativoTsDetFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_ORDINATIVO_TS_DET_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_ordinativo_ts_det_ord_ts_det_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ORDINATIVO_TS_DET_ID_GENERATOR")
	@Column(name="ord_ts_det_id")
	private Integer ordTsDetId;

	@Column(name="ord_ts_det_importo")
	private BigDecimal ordTsDetImporto;

	//bi-directional many-to-one association to SiacDOrdinativoTsDetTipoFin
	@ManyToOne
	@JoinColumn(name="ord_ts_det_tipo_id")
	private SiacDOrdinativoTsDetTipoFin siacDOrdinativoTsDetTipo;

	//bi-directional many-to-one association to SiacTOrdinativoTFin
	@ManyToOne
	@JoinColumn(name="ord_ts_id")
	private SiacTOrdinativoTFin siacTOrdinativoT;

	public SiacTOrdinativoTsDetFin() {
	}

	public Integer getOrdTsDetId() {
		return this.ordTsDetId;
	}

	public void setOrdTsDetId(Integer ordTsDetId) {
		this.ordTsDetId = ordTsDetId;
	}

	public BigDecimal getOrdTsDetImporto() {
		return this.ordTsDetImporto;
	}

	public void setOrdTsDetImporto(BigDecimal ordTsDetImporto) {
		this.ordTsDetImporto = ordTsDetImporto;
	}

	public SiacDOrdinativoTsDetTipoFin getSiacDOrdinativoTsDetTipo() {
		return this.siacDOrdinativoTsDetTipo;
	}

	public void setSiacDOrdinativoTsDetTipo(SiacDOrdinativoTsDetTipoFin siacDOrdinativoTsDetTipo) {
		this.siacDOrdinativoTsDetTipo = siacDOrdinativoTsDetTipo;
	}

	public SiacTOrdinativoTFin getSiacTOrdinativoT() {
		return this.siacTOrdinativoT;
	}

	public void setSiacTOrdinativoT(SiacTOrdinativoTFin siacTOrdinativoT) {
		this.siacTOrdinativoT = siacTOrdinativoT;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordTsDetId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordTsDetId = uid;
	}
}