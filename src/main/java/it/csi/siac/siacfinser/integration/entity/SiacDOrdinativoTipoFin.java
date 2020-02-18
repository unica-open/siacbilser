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
 * The persistent class for the siac_d_ordinativo_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_ordinativo_tipo")
public class SiacDOrdinativoTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_ORDINATIVO_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_ordinativo_tipo_ord_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ORDINATIVO_TIPO_ID_GENERATOR")
	@Column(name="ord_tipo_id")
	private Integer ordTipoId;

	@Column(name="ord_tipo_code")
	private String ordTipoCode;

	@Column(name="ord_tipo_desc")
	private String ordTipoDesc;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@OneToMany(mappedBy="siacDOrdinativoTipo")
	private List<SiacTOrdinativoFin> siacTOrdinativos;

	//bi-directional many-to-one association to SiacROrdinativoTipoClassTipFin
	@OneToMany(mappedBy="siacDOrdinativoTipo")
	private List<SiacROrdinativoTipoClassTipFin> siacROrdinativoTipoClassTips;
	
	public SiacDOrdinativoTipoFin() {
	}

	public Integer getOrdTipoId() {
		return this.ordTipoId;
	}

	public void setOrdTipoId(Integer ordTipoId) {
		this.ordTipoId = ordTipoId;
	}

	public String getOrdTipoCode() {
		return this.ordTipoCode;
	}

	public void setOrdTipoCode(String ordTipoCode) {
		this.ordTipoCode = ordTipoCode;
	}

	public String getOrdTipoDesc() {
		return this.ordTipoDesc;
	}

	public void setOrdTipoDesc(String ordTipoDesc) {
		this.ordTipoDesc = ordTipoDesc;
	}

	public List<SiacTOrdinativoFin> getSiacTOrdinativos() {
		return this.siacTOrdinativos;
	}

	public void setSiacTOrdinativos(List<SiacTOrdinativoFin> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}

	public SiacTOrdinativoFin addSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacDOrdinativoTipo(this);

		return siacTOrdinativo;
	}

	public SiacTOrdinativoFin removeSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacDOrdinativoTipo(null);

		return siacTOrdinativo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordTipoId = uid;
	}
	
	public List<SiacROrdinativoTipoClassTipFin> getSiacROrdinativoTipoClassTips() {
		return this.siacROrdinativoTipoClassTips;
	}

	public void setSiacROrdinativoTipoClassTips(List<SiacROrdinativoTipoClassTipFin> siacROrdinativoTipoClassTips) {
		this.siacROrdinativoTipoClassTips = siacROrdinativoTipoClassTips;
	}
}