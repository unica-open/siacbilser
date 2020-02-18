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
 * The persistent class for the siac_r_liquidazione_class database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_class")
public class SiacRLiquidazioneClassFin extends SiacRClassBaseFin {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_LIQUIDAZIONE_CLASS_LIQ_CLASSIF_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_liquidazione_class_liq_classif_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_LIQUIDAZIONE_CLASS_LIQ_CLASSIF_ID_GENERATOR")
	@Column(name="liq_classif_id")
	private Integer liqClassifId;

	//@Column(name="classif_id")
	//private Integer classifId;
	
	
	//bi-directional many-to-one association to SiacTLiquidazioneFin
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazioneFin siacTLiquidazione;

	public SiacRLiquidazioneClassFin() {
	}

	public Integer getLiqClassifId() {
		return this.liqClassifId;
	}

	public void setLiqClassifId(Integer liqClassifId) {
		this.liqClassifId = liqClassifId;
	}

//	public Integer getClassifId() {
//		return this.classifId;
//	}
//
//	public void setClassifId(Integer classifId) {
//		this.classifId = classifId;
//	}

	
	public SiacTLiquidazioneFin getSiacTLiquidazione() {
		return this.siacTLiquidazione;
	}

	public void setSiacTLiquidazione(SiacTLiquidazioneFin siacTLiquidazione) {
		this.siacTLiquidazione = siacTLiquidazione;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return liqClassifId;
	}

	@Override
	public void setUid(Integer uid) {
		this.liqClassifId = uid;
		
	}

}