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

/**
 * The persistent class for the siac_t_subdoc_iva_prot_def_num database table.
 * 
 */
@Entity
@Table(name="siac_t_subdoc_iva_prot_def_num")
public class SiacTSubdocIvaProtDefNum extends SiacTEnteBase {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="SIAC_T_SUBDOC_IVA_PROT_PROV_GENERATOR", allocationSize=1, sequenceName="siac_t_subdoc_iva_prot_def_num_subdociva_prot_def_num_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_SUBDOC_IVA_PROT_PROV_GENERATOR")
	@Column(name="subdociva_prot_def_num_id")
	private Integer subdocivaProtDefNumId;
	
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
	
	@Column(name="subdociva_data_prot_def")
	private Date subdocivaDataProtDef;

	@Version
	@Column(name="subdociva_prot_def")
	private Integer subdocivaProtDef;

	/**
	 * @return the subdocivaProtDefNumId
	 */
	public Integer getSubdocivaProtDefNumId() {
		return subdocivaProtDefNumId;
	}

	/**
	 * @param subdocivaProtDefNumId the subdocivaProtDefNumId to set
	 */
	public void setSubdocivaProtDefNumId(Integer subdocivaProtDefNumId) {
		this.subdocivaProtDefNumId = subdocivaProtDefNumId;
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
	 * @return the subdocivaDataProtDef
	 */
	public Date getSubdocivaDataProtDef() {
		return subdocivaDataProtDef;
	}

	/**
	 * @param subdocivaDataProtDef the subdocivaDataProtDef to set
	 */
	public void setSubdocivaDataProtDef(Date subdocivaDataProtDef) {
		this.subdocivaDataProtDef = subdocivaDataProtDef;
	}

	/**
	 * @return the subdocivaProtDef
	 */
	public Integer getSubdocivaProtDef() {
		return subdocivaProtDef;
	}

	/**
	 * @param subdocivaProtDef the subdocivaProtDef to set
	 */
	public void setSubdocivaProtDef(Integer subdocivaProtDef) {
		this.subdocivaProtDef = subdocivaProtDef;
	}

	@Override
	public Integer getUid() {
		return subdocivaProtDefNumId;
	}

	@Override
	public void setUid(Integer uid) {
		this.subdocivaProtDefNumId = uid;
	}

}
