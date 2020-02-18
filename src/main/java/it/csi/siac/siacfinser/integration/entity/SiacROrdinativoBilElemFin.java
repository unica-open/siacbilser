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

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_ordinativo_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_bil_elem")
public class SiacROrdinativoBilElemFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_BIL_ELEM_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_ordinativo_bil_elem_ord_bile_elem_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_BIL_ELEM_ID_GENERATOR")
	@Column(name="ord_bile_elem_id")
	private Integer ordBileElemId;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativoFin siacTOrdinativo;

	public SiacROrdinativoBilElemFin() {
	}

	public Integer getOrdBileElemId() {
		return this.ordBileElemId;
	}

	public void setOrdBileElemId(Integer ordBileElemId) {
		this.ordBileElemId = ordBileElemId;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	public SiacTOrdinativoFin getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	public void setSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordBileElemId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordBileElemId = uid;
	}
}