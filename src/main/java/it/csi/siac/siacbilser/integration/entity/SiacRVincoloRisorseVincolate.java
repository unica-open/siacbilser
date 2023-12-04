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
 * The persistent class for the siac_d_vincolo_genere database table.
 * 
 */
@Entity
@Table(name="siac_r_vincolo_risorse_vincolate")
@NamedQuery(name="SiacRVincoloRisorseVincolate.findAll", query="SELECT s FROM SiacRVincoloRisorseVincolate s")
public class SiacRVincoloRisorseVincolate extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_VINCOLO_RISORSE_VINCOLATE_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_VINCOLO_RISORSE_VINCOL_VINCOLO_RISORSE_VINCOLATE_R_I_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_VINCOLO_RISORSE_VINCOLATE_GENERATOR")
	@Column(name="vincolo_risorse_vincolate_r_id")
	private Integer vincoloRisorseVincolateRId;

	//bi-directional many-to-one association to SiacDVincoloGenere
	@ManyToOne
	@JoinColumn(name="vincolo_risorse_vincolate_id")
	private SiacDVincoloRisorseVincolate siacDVincoloRisorseVincolate;

	//bi-directional many-to-one association to SiacTVincolo
	@ManyToOne
	@JoinColumn(name="vincolo_id")
	private SiacTVincolo siacTVincolo;
	
	public SiacRVincoloRisorseVincolate() {}

	/**
	 * @return the vincoloRisorseVincolateRId
	 */
	public Integer getVincoloRisorseVincolateRId() {
		return vincoloRisorseVincolateRId;
	}

	/**
	 * @param vincoloRisorseVincolateRId the vincoloRisorseVincolateRId to set
	 */
	public void setVincoloRisorseVincolateRId(Integer vincoloRisorseVincolateRId) {
		this.vincoloRisorseVincolateRId = vincoloRisorseVincolateRId;
	}

	/**
	 * @return the siacDVincoloRisorseVincolate
	 */
	public SiacDVincoloRisorseVincolate getSiacDVincoloRisorseVincolate() {
		return siacDVincoloRisorseVincolate;
	}

	/**
	 * @param siacDVincoloRisorseVincolate the siacDVincoloRisorseVincolate to set
	 */
	public void setSiacDVincoloRisorseVincolate(SiacDVincoloRisorseVincolate siacDVincoloRisorseVincolate) {
		this.siacDVincoloRisorseVincolate = siacDVincoloRisorseVincolate;
	}

	/**
	 * @return the siacTVincolo
	 */
	public SiacTVincolo getSiacTVincolo() {
		return siacTVincolo;
	}

	/**
	 * @param siacTVincolo the siacTVincolo to set
	 */
	public void setSiacTVincolo(SiacTVincolo siacTVincolo) {
		this.siacTVincolo = siacTVincolo;
	}

	@Override
	public Integer getUid() {
		return vincoloRisorseVincolateRId;
	}

	@Override
	public void setUid(Integer uid) {
		vincoloRisorseVincolateRId = (uid);
	}
	
	
	
}
