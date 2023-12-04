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
 * The persistent class for the siac_r_pcc_ufficio_codice database table.
 * 
 */
@Entity
@Table(name="siac_r_pcc_ufficio_codice")
@NamedQuery(name="SiacRPccUfficioCodice.findAll", query="SELECT s FROM SiacRPccUfficioCodice s")
public class SiacRPccUfficioCodice extends SiacTEnteBase {

	private static final long serialVersionUID = -7897654021373581623L;

	@Id
	@SequenceGenerator(name="SIAC_R_PCC_UFFICIO_CODICE_PCCUFFCODID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PCC_UFFICIO_CODICE_PCCUFFCOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PCC_UFFICIO_CODICE_PCCUFFCODID_GENERATOR")
	@Column(name="pccuffcod_id")
	private Integer pccuffcodId;

	// bi-directional many-to-one association to SiacDPccCodice
	@ManyToOne
	@JoinColumn(name="pcccod_id")
	private SiacDPccCodice siacDPccCodice;

	//bi-directional many-to-one association to SiacDPccUfficio
	@ManyToOne
	@JoinColumn(name="pccuff_id")
	private SiacDPccUfficio siacDPccUfficio;

	/**
	 * @return the pccuffcodId
	 */
	public Integer getPccuffcodId() {
		return pccuffcodId;
	}

	/**
	 * @param pccuffcodId the pccuffcodId to set
	 */
	public void setPccuffcodId(Integer pccuffcodId) {
		this.pccuffcodId = pccuffcodId;
	}

	/**
	 * @return the siacDPccCodice
	 */
	public SiacDPccCodice getSiacDPccCodice() {
		return siacDPccCodice;
	}

	/**
	 * @param siacDPccCodice the siacDPccCodice to set
	 */
	public void setSiacDPccCodice(SiacDPccCodice siacDPccCodice) {
		this.siacDPccCodice = siacDPccCodice;
	}

	/**
	 * @return the siacDPccUfficio
	 */
	public SiacDPccUfficio getSiacDPccUfficio() {
		return siacDPccUfficio;
	}

	/**
	 * @param siacDPccUfficio the siacDPccUfficio to set
	 */
	public void setSiacDPccUfficio(SiacDPccUfficio siacDPccUfficio) {
		this.siacDPccUfficio = siacDPccUfficio;
	}

	@Override
	public Integer getUid() {
		return getPccuffcodId();
	}

	@Override
	public void setUid(Integer uid) {
		setPccuffcodId(uid);
	}

}