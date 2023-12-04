/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_vincolo_genere database table.
 * 
 */
@Entity
@Table(name="siac_d_vincolo_risorse_vincolate")
@NamedQuery(name="SiacDVincoloRisorseVincolate.findAll", query="SELECT s FROM SiacDVincoloRisorseVincolate s")
public class SiacDVincoloRisorseVincolate extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_VINCOLO_RISORSE_VINCOLATE_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_VINCOLO_RISORSE_VINCOLA_VINCOLO_RISORSE_VINCOLATE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_VINCOLO_RISORSE_VINCOLATE_GENERATOR")
	@Column(name="vincolo_risorse_vincolate_id")
	private Integer vincoloRisorseVincolateId;

	@Column(name="vincolo_risorse_vincolate_code")
	private String vincoloRisorseVincolateCode;

	@Column(name="vincolo_risorse_vincolate_desc")
	private String vincoloRisorseVincolateDesc;

	//bi-directional many-to-one association to SiacRVincoloGenere
	@OneToMany(mappedBy="siacDVincoloRisorseVincolate")
	private List<SiacRVincoloRisorseVincolate> siacRVincoloRisorseVincolates;
	
	public SiacDVincoloRisorseVincolate() {}

	/**
	 * @return the vincoloRisorseVincolateId
	 */
	public Integer getVincoloRisorseVincolateId() {
		return vincoloRisorseVincolateId;
	}

	/**
	 * @param vincoloRisorseVincolateId the vincoloRisorseVincolateId to set
	 */
	public void setVincoloRisorseVincolateId(Integer vincoloRisorseVincolateId) {
		this.vincoloRisorseVincolateId = vincoloRisorseVincolateId;
	}

	/**
	 * @return the vincoloRisorseVincolateCode
	 */
	public String getVincoloRisorseVincolateCode() {
		return vincoloRisorseVincolateCode;
	}

	/**
	 * @param vincoloRisorseVincolateCode the vincoloRisorseVincolateCode to set
	 */
	public void setVincoloRisorseVincolateCode(String vincoloRisorseVincolateCode) {
		this.vincoloRisorseVincolateCode = vincoloRisorseVincolateCode;
	}

	/**
	 * @return the vincoloRisorseVincolateDesc
	 */
	public String getVincoloRisorseVincolateDesc() {
		return vincoloRisorseVincolateDesc;
	}

	/**
	 * @param vincoloRisorseVincolateDesc the vincoloRisorseVincolateDesc to set
	 */
	public void setVincoloRisorseVincolateDesc(String vincoloRisorseVincolateDesc) {
		this.vincoloRisorseVincolateDesc = vincoloRisorseVincolateDesc;
	}

	/**
	 * @return the siacRVincoloRisorseVincolates
	 */
	public List<SiacRVincoloRisorseVincolate> getSiacRVincoloRisorseVincolates() {
		return siacRVincoloRisorseVincolates;
	}

	/**
	 * @param siacRVincoloRisorseVincolates the siacRVincoloRisorseVincolates to set
	 */
	public void setSiacRVincoloRisorseVincolates(List<SiacRVincoloRisorseVincolate> siacRVincoloRisorseVincolates) {
		this.siacRVincoloRisorseVincolates = siacRVincoloRisorseVincolates;
	}

	@Override
	public Integer getUid() {
		return vincoloRisorseVincolateId;
	}

	@Override
	public void setUid(Integer uid) {
		vincoloRisorseVincolateId = uid;
	}
	

}
