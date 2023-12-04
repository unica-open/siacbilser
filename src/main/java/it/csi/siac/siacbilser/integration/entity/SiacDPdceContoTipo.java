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
 * The persistent class for the siac_d_pdce_conto_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_pdce_conto_tipo")
@NamedQuery(name="SiacDPdceContoTipo.findAll", query="SELECT s FROM SiacDPdceContoTipo s")
public class SiacDPdceContoTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_PDCE_CONTO_TIPO_PDCECTTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PDCE_CONTO_TIPO_PDCE_CT_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PDCE_CONTO_TIPO_PDCECTTIPOID_GENERATOR")
	@Column(name="pdce_ct_tipo_id")
	private Integer pdceCtTipoId;
	
	@Column(name="pdce_ct_tipo_code")
	private String pdceCtTipoCode;

	@Column(name="pdce_ct_tipo_desc")
	private String pdceCtTipoDesc;
	

	//bi-directional many-to-one association to SiacTPdceConto
	@OneToMany(mappedBy="siacDPdceContoTipo")
	private List<SiacTPdceConto> siacTPdceContos;

	public SiacDPdceContoTipo() {
	}

	public Integer getPdceCtTipoId() {
		return this.pdceCtTipoId;
	}

	public void setPdceCtTipoId(Integer pdceCtTipoId) {
		this.pdceCtTipoId = pdceCtTipoId;
	}

	public String getPdceCtTipoCode() {
		return this.pdceCtTipoCode;
	}

	public void setPdceCtTipoCode(String pdceCtTipoCode) {
		this.pdceCtTipoCode = pdceCtTipoCode;
	}

	public String getPdceCtTipoDesc() {
		return this.pdceCtTipoDesc;
	}

	public void setPdceCtTipoDesc(String pdceCtTipoDesc) {
		this.pdceCtTipoDesc = pdceCtTipoDesc;
	}

	public List<SiacTPdceConto> getSiacTPdceContos() {
		return this.siacTPdceContos;
	}

	public void setSiacTPdceContos(List<SiacTPdceConto> siacTPdceContos) {
		this.siacTPdceContos = siacTPdceContos;
	}

	public SiacTPdceConto addSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		getSiacTPdceContos().add(siacTPdceConto);
		siacTPdceConto.setSiacDPdceContoTipo(this);

		return siacTPdceConto;
	}

	public SiacTPdceConto removeSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		getSiacTPdceContos().remove(siacTPdceConto);
		siacTPdceConto.setSiacDPdceContoTipo(null);

		return siacTPdceConto;
	}

	@Override
	public Integer getUid() {
		return this.pdceCtTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pdceCtTipoId = uid;
	}

}