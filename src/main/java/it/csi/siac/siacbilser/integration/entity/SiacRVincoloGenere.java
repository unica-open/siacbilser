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
 * The persistent class for the siac_r_vincolo_genere database table.
 * 
 */
@Entity
@Table(name="siac_r_vincolo_genere")
@NamedQuery(name="SiacRVincoloGenere.findAll", query="SELECT s FROM SiacRVincoloGenere s")
public class SiacRVincoloGenere extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_VINCOLO_GENERE_VINCOLOGENRID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_VINCOLO_GENERE_VINCOLO_GEN_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_VINCOLO_GENERE_VINCOLOGENRID_GENERATOR")
	@Column(name="vincolo_gen_r_id")
	private Integer vincoloGenRId;

	//bi-directional many-to-one association to SiacDVincoloGenere
	@ManyToOne
	@JoinColumn(name="vincolo_gen_id")
	private SiacDVincoloGenere siacDVincoloGenere;

	//bi-directional many-to-one association to SiacTVincolo
	@ManyToOne
	@JoinColumn(name="vincolo_id")
	private SiacTVincolo siacTVincolo;

	public SiacRVincoloGenere() {
	}

	public Integer getVincoloGenRId() {
		return this.vincoloGenRId;
	}

	public void setVincoloGenRId(Integer vincoloGenRId) {
		this.vincoloGenRId = vincoloGenRId;
	}

	public SiacDVincoloGenere getSiacDVincoloGenere() {
		return this.siacDVincoloGenere;
	}

	public void setSiacDVincoloGenere(SiacDVincoloGenere siacDVincoloGenere) {
		this.siacDVincoloGenere = siacDVincoloGenere;
	}

	public SiacTVincolo getSiacTVincolo() {
		return this.siacTVincolo;
	}

	public void setSiacTVincolo(SiacTVincolo siacTVincolo) {
		this.siacTVincolo = siacTVincolo;
	}

	@Override
	public Integer getUid() {
		return vincoloGenRId;
	}

	@Override
	public void setUid(Integer uid) {
		vincoloGenRId = uid;
	}

}