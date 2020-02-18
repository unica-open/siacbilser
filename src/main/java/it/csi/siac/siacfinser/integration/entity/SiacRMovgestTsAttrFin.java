/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_movgest_ts_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_attr")
public class SiacRMovgestTsAttrFin extends SiacRAttrBaseFin {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_ATTR_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_movgest_ts_attr_bil_elem_attr_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_ATTR_ID_GENERATOR")
	@Column(name="bil_elem_attr_id")
	private Integer bilElemAttrId;

	
	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	public SiacRMovgestTsAttrFin() {
	}

	public Integer getBilElemAttrId() {
		return this.bilElemAttrId;
	}

	public void setBilElemAttrId(Integer bilElemAttrId) {
		this.bilElemAttrId = bilElemAttrId;
	}

	public SiacTMovgestTsFin getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.bilElemAttrId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.bilElemAttrId = uid;
	}
}