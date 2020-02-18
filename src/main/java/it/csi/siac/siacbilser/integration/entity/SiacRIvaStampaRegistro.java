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
 * The persistent class for the siac_r_iva_stampa_registro database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_stampa_registro")
@NamedQuery(name="SiacRIvaStampaRegistro.findAll", query="SELECT s FROM SiacRIvaStampaRegistro s")
public class SiacRIvaStampaRegistro extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_IVA_STAMPA_REGISTRO_IVASTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_IVA_STAMPA_REGISTRO_IVASTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_IVA_STAMPA_REGISTRO_IVASTRID_GENERATOR")
	@Column(name="ivastr_id")
	private Integer ivastrId;

	//bi-directional many-to-one association to SiacTIvaRegistro
	@ManyToOne
	@JoinColumn(name="ivareg_id")
	private SiacTIvaRegistro siacTIvaRegistro;

	//bi-directional many-to-one association to SiacTIvaStampa
	@ManyToOne
	@JoinColumn(name="ivast_id")
	private SiacTIvaStampa siacTIvaStampa;

	public SiacRIvaStampaRegistro() {
	}

	public Integer getIvastrId() {
		return this.ivastrId;
	}

	public void setIvastrId(Integer ivastrId) {
		this.ivastrId = ivastrId;
	}

	public SiacTIvaRegistro getSiacTIvaRegistro() {
		return this.siacTIvaRegistro;
	}

	public void setSiacTIvaRegistro(SiacTIvaRegistro siacTIvaRegistro) {
		this.siacTIvaRegistro = siacTIvaRegistro;
	}

	public SiacTIvaStampa getSiacTIvaStampa() {
		return this.siacTIvaStampa;
	}

	public void setSiacTIvaStampa(SiacTIvaStampa siacTIvaStampa) {
		this.siacTIvaStampa = siacTIvaStampa;
	}

	@Override
	public Integer getUid() {
		return ivastrId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ivastrId = uid;
	}

}