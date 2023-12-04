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

import it.csi.siac.siacfinser.integration.entity.base.SiacLoginMultiplo;




@Entity
@Table(name="siac_r_modpag_ordine")
public class SiacRModpagOrdineFin extends SiacLoginMultiplo {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SIAC_R_MODPAG_ORDINE_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_modpag_ordine_modpagord_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MODPAG_ORDINE_ID_GENERATOR")
	@Column(name="modpagord_id")
	private Integer modpagordId;
	
	
	private Integer ordine;
	
	
	
	@ManyToOne
	@JoinColumn(name="soggrelmpag_id")
	private SiacRSoggrelModpagFin siacRSoggrelModpag;

	//bi-directional many-to-one association to SiacTModpagFin
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpagFin siacTModpag;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;
	
	public SiacRModpagOrdineFin(){
		
	}
	
	
	
	@Override
	public Integer getUid() {
		// Auto-generated method stub
		return this.modpagordId;
	}

	@Override
	public void setUid(Integer uid) {
		//  Auto-generated method stub
		this.modpagordId = uid;
	}
	
	
	public SiacTModpagFin getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpagFin siacTModpag) {
		this.siacTModpag = siacTModpag;
	}
	
	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}



	public Integer getOrdine() {
		return ordine;
	}



	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}



	public Integer getModpagordId() {
		return modpagordId;
	}



	public void setModpagordId(Integer modpagordId) {
		this.modpagordId = modpagordId;
	}
	
	public SiacRSoggrelModpagFin getSiacRSoggrelModpag() {
		return this.siacRSoggrelModpag;
	}

	public void setSiacRSoggrelModpag(SiacRSoggrelModpagFin siacRSoggrelModpag) {
		this.siacRSoggrelModpag = siacRSoggrelModpag;
	}


}
