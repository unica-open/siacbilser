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
 * The persistent class for the siac_r_ordinativo_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_modpag")
public class SiacROrdinativoModpagFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_MODPAG_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_ordinativo_modpag_ord_modpag_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_MODPAG_ID_GENERATOR")
	@Column(name="ord_modpag_id")
	private Integer ordModpagId;

	//bi-directional many-to-one association to SiacTModpagFin
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpagFin siacTModpag;
	
	
	@ManyToOne
	@JoinColumn(name="soggetto_relaz_id")
	private SiacRSoggettoRelazFin cessioneId;
	
	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativoFin siacTOrdinativo;

	public SiacROrdinativoModpagFin() {
	}
	
	

	/**
	 * @return the cessioneId
	 */
	public SiacRSoggettoRelazFin getCessioneId() {
		return cessioneId;
	}



	/**
	 * @param cessioneId the cessioneId to set
	 */
	public void setCessioneId(SiacRSoggettoRelazFin cessioneId) {
		this.cessioneId = cessioneId;
	}



	public Integer getOrdModpagId() {
		return this.ordModpagId;
	}

	public void setOrdModpagId(Integer ordModpagId) {
		this.ordModpagId = ordModpagId;
	}

	public SiacTModpagFin getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpagFin siacTModpag) {
		this.siacTModpag = siacTModpag;
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
		return this.ordModpagId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordModpagId = uid;
	}
}