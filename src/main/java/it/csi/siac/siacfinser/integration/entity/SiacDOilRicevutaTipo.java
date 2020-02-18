/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_oil_ricevuta_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_oil_ricevuta_tipo")
public class SiacDOilRicevutaTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_OIL_RICEVUTA_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_oil_ricevuta_tipo_oil_ricevuta_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_OIL_RICEVUTA_TIPO_ID_GENERATOR")
	@Column(name="oil_ricevuta_tipo_id")
	private Integer oilRicevutaTipoId;


	@Column(name="oil_ricevuta_tipo_code")
	private String oilRicevutaTipoCode;

	@Column(name="oil_ricevuta_tipo_code_fl")
	private String oilRicevutaTipoCodeFl;

	@Column(name="oil_ricevuta_tipo_desc")
	private String oilRicevutaTipoDesc;


	//bi-directional many-to-one association to SiacTOilRicevuta
	@OneToMany(mappedBy="siacDOilRicevutaTipo")
	private List<SiacTOilRicevuta> siacTOilRicevutas;

	public SiacDOilRicevutaTipo() {
	}

	public Integer getOilRicevutaTipoId() {
		return this.oilRicevutaTipoId;
	}

	public void setOilRicevutaTipoId(Integer oilRicevutaTipoId) {
		this.oilRicevutaTipoId = oilRicevutaTipoId;
	}


	public String getOilRicevutaTipoCode() {
		return this.oilRicevutaTipoCode;
	}

	public void setOilRicevutaTipoCode(String oilRicevutaTipoCode) {
		this.oilRicevutaTipoCode = oilRicevutaTipoCode;
	}

	public String getOilRicevutaTipoCodeFl() {
		return this.oilRicevutaTipoCodeFl;
	}

	public void setOilRicevutaTipoCodeFl(String oilRicevutaTipoCodeFl) {
		this.oilRicevutaTipoCodeFl = oilRicevutaTipoCodeFl;
	}

	public String getOilRicevutaTipoDesc() {
		return this.oilRicevutaTipoDesc;
	}

	public void setOilRicevutaTipoDesc(String oilRicevutaTipoDesc) {
		this.oilRicevutaTipoDesc = oilRicevutaTipoDesc;
	}

	public List<SiacTOilRicevuta> getSiacTOilRicevutas() {
		return this.siacTOilRicevutas;
	}

	public void setSiacTOilRicevutas(List<SiacTOilRicevuta> siacTOilRicevutas) {
		this.siacTOilRicevutas = siacTOilRicevutas;
	}

	@Override
	public Integer getUid() {
		return this.oilRicevutaTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.oilRicevutaTipoId = uid;
	}

}