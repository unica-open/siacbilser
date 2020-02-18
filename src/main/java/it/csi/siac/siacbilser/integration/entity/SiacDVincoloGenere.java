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
@Table(name="siac_d_vincolo_genere")
@NamedQuery(name="SiacDVincoloGenere.findAll", query="SELECT s FROM SiacDVincoloGenere s")
public class SiacDVincoloGenere extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_VINCOLO_GENERE_VINCOLOGENID_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_VINCOLO_GENERE_VINCOLO_GEN_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_VINCOLO_GENERE_VINCOLOGENID_GENERATOR")
	@Column(name="vincolo_gen_id")
	private Integer vincoloGenId;

	@Column(name="vincolo_gen_code")
	private String vincoloGenCode;

	@Column(name="vincolo_gen_desc")
	private String vincoloGenDesc;

	//bi-directional many-to-one association to SiacRVincoloGenere
	@OneToMany(mappedBy="siacDVincoloGenere")
	private List<SiacRVincoloGenere> siacRVincoloGeneres;

	public SiacDVincoloGenere() {
	}

	public Integer getVincoloGenId() {
		return this.vincoloGenId;
	}

	public void setVincoloGenId(Integer vincoloGenId) {
		this.vincoloGenId = vincoloGenId;
	}

	public String getVincoloGenCode() {
		return this.vincoloGenCode;
	}

	public void setVincoloGenCode(String vincoloGenCode) {
		this.vincoloGenCode = vincoloGenCode;
	}

	public String getVincoloGenDesc() {
		return this.vincoloGenDesc;
	}

	public void setVincoloGenDesc(String vincoloGenDesc) {
		this.vincoloGenDesc = vincoloGenDesc;
	}

	public List<SiacRVincoloGenere> getSiacRVincoloGeneres() {
		return this.siacRVincoloGeneres;
	}

	public void setSiacRVincoloGeneres(List<SiacRVincoloGenere> siacRVincoloGeneres) {
		this.siacRVincoloGeneres = siacRVincoloGeneres;
	}

	public SiacRVincoloGenere addSiacRVincoloGenere(SiacRVincoloGenere siacRVincoloGenere) {
		getSiacRVincoloGeneres().add(siacRVincoloGenere);
		siacRVincoloGenere.setSiacDVincoloGenere(this);

		return siacRVincoloGenere;
	}

	public SiacRVincoloGenere removeSiacRVincoloGenere(SiacRVincoloGenere siacRVincoloGenere) {
		getSiacRVincoloGeneres().remove(siacRVincoloGenere);
		siacRVincoloGenere.setSiacDVincoloGenere(null);

		return siacRVincoloGenere;
	}

	@Override
	public Integer getUid() {
		return vincoloGenId;
	}

	@Override
	public void setUid(Integer uid) {
		vincoloGenId = uid;
	}

}