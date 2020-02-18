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


/**
 * The persistent class for the siac_r_pdce_conto database table.
 * 
 */
@Entity
@Table(name="siac_r_pdce_conto")
@NamedQuery(name="SiacRPdceConto.findAll", query="SELECT s FROM SiacRPdceConto s")
public class SiacRPdceConto extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_PDCE_CONTO_PDCERCONTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PDCE_CONTO_PDCE_R_CONTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PDCE_CONTO_PDCERCONTOID_GENERATOR")
	@Column(name="pdce_r_conto_id")
	private Integer pdceRContoId;

	//bi-directional many-to-one association to SiacDPdceRelTipo
	@ManyToOne
	@JoinColumn(name="pdcerel_id")
	private SiacDPdceRelTipo siacDPdceRelTipo;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="pdce_conto_a_id")
	private SiacTPdceConto siacTPdceConto1;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="pdce_conto_b_id")
	private SiacTPdceConto siacTPdceConto2;

	public SiacRPdceConto() {
	}

	public Integer getPdceRContoId() {
		return this.pdceRContoId;
	}

	public void setPdceRContoId(Integer pdceRContoId) {
		this.pdceRContoId = pdceRContoId;
	}

	public SiacDPdceRelTipo getSiacDPdceRelTipo() {
		return this.siacDPdceRelTipo;
	}

	public void setSiacDPdceRelTipo(SiacDPdceRelTipo siacDPdceRelTipo) {
		this.siacDPdceRelTipo = siacDPdceRelTipo;
	}

	public SiacTPdceConto getSiacTPdceConto1() {
		return this.siacTPdceConto1;
	}

	public void setSiacTPdceConto1(SiacTPdceConto siacTPdceConto1) {
		this.siacTPdceConto1 = siacTPdceConto1;
	}

	public SiacTPdceConto getSiacTPdceConto2() {
		return this.siacTPdceConto2;
	}

	public void setSiacTPdceConto2(SiacTPdceConto siacTPdceConto2) {
		this.siacTPdceConto2 = siacTPdceConto2;
	}

	@Override
	public Integer getUid() {
		return pdceRContoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pdceRContoId = uid;
	}

}