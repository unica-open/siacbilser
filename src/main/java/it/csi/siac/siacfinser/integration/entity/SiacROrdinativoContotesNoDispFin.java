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
 * The persistent class for the siac_r_ordinativo database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_contotes_nodisp")
public class SiacROrdinativoContotesNoDispFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_CONTOTES_NO_DISP_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_ordinativo_contotes_nodisp_ord_contotes_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_CONTOTES_NO_DISP_ID_GENERATOR")
	@Column(name="ord_contotes_id")
	private Integer ordContotesId;

	//bi-directional many-to-one association to SiacDRelazTipoFin
	@ManyToOne
	@JoinColumn(name="contotes_id")
	private SiacDContotesoreriaFin siacDContoTesoreria;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativoFin siacTOrdinativo;

	public SiacROrdinativoContotesNoDispFin() {
	}

	
	public Integer getOrdContotesId() {
		return ordContotesId;
	}




	public void setOrdContotesId(Integer ordContotesId) {
		this.ordContotesId = ordContotesId;
	}




	public SiacDContotesoreriaFin getSiacDContoTesoreria() {
		return siacDContoTesoreria;
	}




	public void setSiacDContoTesoreria(SiacDContotesoreriaFin siacDContoTesoreria) {
		this.siacDContoTesoreria = siacDContoTesoreria;
	}




	public SiacTOrdinativoFin getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	public void setSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo1) {
		this.siacTOrdinativo = siacTOrdinativo1;
	}

	@Override
	public Integer getUid() {
		return this.ordContotesId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ordContotesId = uid;
	}
}