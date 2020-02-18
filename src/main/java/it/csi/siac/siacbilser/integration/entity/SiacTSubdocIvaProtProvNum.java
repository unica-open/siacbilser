/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="siac_t_subdoc_iva_prot_prov_num")
public class SiacTSubdocIvaProtProvNum extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SIAC_T_SUBDOC_IVA_PROT_PROV_GENERATOR", allocationSize=1, sequenceName="siac_t_subdoc_iva_prot_prov_num_subdociva_prot_prov_num_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_SUBDOC_IVA_PROT_PROV_GENERATOR")
	@Column(name="subdociva_prot_prov_num_id")
	private Integer subdocivaProtProvNumId;

	//bi-directional many-to-one association to SiacTIvaRegistro
	/** The siac t iva registro. */
	@ManyToOne
	@JoinColumn(name="ivareg_id")
	private SiacTIvaRegistro siacTIvaRegistro;

	//bi-directional many-to-one association to SiacTPeriodo
	/** The siac t periodo. */
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodo siacTPeriodo;

	@Column(name="subdociva_data_prot_prov")
	private Date subdocivaDataProtProv;

	@Version
	@Column(name="subdociva_prot_prov")
	private Integer subdocivaProtProv;

	@Override
	public Integer getUid() {
		return subdocivaProtProvNumId;
	}

	/**
	 * @return the subdocivaProtProvNumId
	 */
	public Integer getSubdocivaProtProvNumId() {
		return subdocivaProtProvNumId;
	}

	/**
	 * @param subdocivaProtProvNumId the subdocivaProtProvNumId to set
	 */
	public void setSubdocivaProtProvNumId(Integer subdocivaProtProvNumId) {
		this.subdocivaProtProvNumId = subdocivaProtProvNumId;
	}

	/**
	 * @return the siacTIvaRegistro
	 */
	public SiacTIvaRegistro getSiacTIvaRegistro() {
		return siacTIvaRegistro;
	}

	/**
	 * @param siacTIvaRegistro the siacTIvaRegistro to set
	 */
	public void setSiacTIvaRegistro(SiacTIvaRegistro siacTIvaRegistro) {
		this.siacTIvaRegistro = siacTIvaRegistro;
	}

	/**
	 * @return the siacTPeriodo
	 */
	public SiacTPeriodo getSiacTPeriodo() {
		return siacTPeriodo;
	}

	/**
	 * @param siacTPeriodo the siacTPeriodo to set
	 */
	public void setSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	/**
	 * @return the subdocivaDataProtProv
	 */
	public Date getSubdocivaDataProtProv() {
		return subdocivaDataProtProv;
	}

	/**
	 * @param subdocivaDataProtProv the subdocivaDataProtProv to set
	 */
	public void setSubdocivaDataProtProv(Date subdocivaDataProtProv) {
		this.subdocivaDataProtProv = subdocivaDataProtProv;
	}

	/**
	 * @return the subdocivaProtProv
	 */
	public Integer getSubdocivaProtProv() {
		return subdocivaProtProv;
	}

	/**
	 * @param subdocivaProtProv the subdocivaProtProv to set
	 */
	public void setSubdocivaProtProv(Integer subdocivaProtProv) {
		this.subdocivaProtProv = subdocivaProtProv;
	}

	@Override
	public void setUid(Integer uid) {
		this.subdocivaProtProvNumId = uid;
	}

}
