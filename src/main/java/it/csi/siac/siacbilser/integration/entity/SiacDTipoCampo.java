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
 * The persistent class for the siac_d_tipo_campo database table.
 * 
 */
@Entity
@Table(name="siac_d_tipo_campo")
@NamedQuery(name="SiacDTipoCampo.findAll", query="SELECT s FROM SiacDTipoCampo s")
public class SiacDTipoCampo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The tc id. */
	@Id
	@SequenceGenerator(name="SIAC_D_TIPO_CAMPO_TCID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_TIPO_CAMPO_TC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_TIPO_CAMPO_TCID_GENERATOR")
	@Column(name="tc_id")
	private Integer tcId;
	
	/** The tc code. */
	@Column(name="tc_code")
	private String tcCode;
	
	/** The tc descr. */
	@Column(name="tc_desc")
	private String tcDesc;
	
	/** The siac t visibilitas. */
	@OneToMany(mappedBy="siacDTipoCampo")
	private List<SiacTVisibilita> siacTVisibilitas;
	
	/**
	 * @return the tcId
	 */
	public Integer getTcId() {
		return this.tcId;
	}

	/**
	 * @param tcId the tcId to set
	 */
	public void setTcId(Integer tcId) {
		this.tcId = tcId;
	}

	/**
	 * @return the tcCode
	 */
	public String getTcCode() {
		return this.tcCode;
	}

	/**
	 * @param tcCode the tcCode to set
	 */
	public void setTcCode(String tcCode) {
		this.tcCode = tcCode;
	}

	/**
	 * @return the tcDesc
	 */
	public String getTcDesc() {
		return this.tcDesc;
	}

	/**
	 * @param tcDesc the tcDesc to set
	 */
	public void setTcDesc(String tcDesc) {
		this.tcDesc = tcDesc;
	}

	/**
	 * @return the siacTVisibilitas
	 */
	public List<SiacTVisibilita> getSiacTVisibilitas() {
		return this.siacTVisibilitas;
	}

	/**
	 * @param siacTVisibilitas the siacTVisibilitas to set
	 */
	public void setSiacTVisibilitas(List<SiacTVisibilita> siacTVisibilitas) {
		this.siacTVisibilitas = siacTVisibilitas;
	}

	@Override
	public Integer getUid() {
		return tcId;
	}

	@Override
	public void setUid(Integer uid) {
		this.tcId = uid;
	}

}