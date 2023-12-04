/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_liquidazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_avanzovincolo_tipo")
public class SiacDAvanzovincoloTipo extends SiacTEnteBase implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_AVANZOVINCOLO_TIPO_AVAV_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_avanzovincolo_tipo_avav_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_AVANZOVINCOLO_TIPO_AVAV_TIPO_ID_GENERATOR")
	@Column(name="avav_tipo_id")
	private Integer avavTipoId;

	
	@Column(name="avav_tipo_code")
	private String avavTipoCode;

	@Column(name="avav_tipo_desc")
	private String avavTipoDesc;


	public SiacDAvanzovincoloTipo() {
	}
	
	


	public Integer getAvavTipoId() {
		return avavTipoId;
	}




	public void setAvavTipoId(Integer avavTipoId) {
		this.avavTipoId = avavTipoId;
	}




	public String getAvavTipoCode() {
		return avavTipoCode;
	}




	public void setAvavTipoCode(String avavTipoCode) {
		this.avavTipoCode = avavTipoCode;
	}




	public String getAvavTipoDesc() {
		return avavTipoDesc;
	}




	public void setAvavTipoDesc(String avavTipoDesc) {
		this.avavTipoDesc = avavTipoDesc;
	}




	@Override
	public Integer getUid() {
		return avavTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.avavTipoId=uid;
	}

}