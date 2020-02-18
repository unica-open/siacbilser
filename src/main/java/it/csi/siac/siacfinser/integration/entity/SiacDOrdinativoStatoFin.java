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
 * The persistent class for the siac_d_ordinativo_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_ordinativo_stato")
public class SiacDOrdinativoStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_ORDINATIVO_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_ordinativo_stato_ord_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ORDINATIVO_STATO_ID_GENERATOR")
	@Column(name="ord_stato_id")
	private Integer ordStatoId;

	@Column(name="ord_stato_code")
	private String ordStatoCode;

	@Column(name="ord_stato_desc")
	private String ordStatoDesc;

	//bi-directional many-to-one association to SiacROrdinativoStatoFin
	@OneToMany(mappedBy="siacDOrdinativoStato")
	private List<SiacROrdinativoStatoFin> siacROrdinativoStatos;

	public SiacDOrdinativoStatoFin() {
	}

	public Integer getOrdStatoId() {
		return this.ordStatoId;
	}

	public void setOrdStatoId(Integer ordStatoId) {
		this.ordStatoId = ordStatoId;
	}

	public String getOrdStatoCode() {
		return this.ordStatoCode;
	}

	public void setOrdStatoCode(String ordStatoCode) {
		this.ordStatoCode = ordStatoCode;
	}

	public String getOrdStatoDesc() {
		return this.ordStatoDesc;
	}

	public void setOrdStatoDesc(String ordStatoDesc) {
		this.ordStatoDesc = ordStatoDesc;
	}

	public List<SiacROrdinativoStatoFin> getSiacROrdinativoStatos() {
		return this.siacROrdinativoStatos;
	}

	public void setSiacROrdinativoStatos(List<SiacROrdinativoStatoFin> siacROrdinativoStatos) {
		this.siacROrdinativoStatos = siacROrdinativoStatos;
	}

	public SiacROrdinativoStatoFin addSiacROrdinativoStato(SiacROrdinativoStatoFin siacROrdinativoStato) {
		getSiacROrdinativoStatos().add(siacROrdinativoStato);
		siacROrdinativoStato.setSiacDOrdinativoStato(this);

		return siacROrdinativoStato;
	}

	public SiacROrdinativoStatoFin removeSiacROrdinativoStato(SiacROrdinativoStatoFin siacROrdinativoStato) {
		getSiacROrdinativoStatos().remove(siacROrdinativoStato);
		siacROrdinativoStato.setSiacDOrdinativoStato(null);

		return siacROrdinativoStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordStatoId = uid;
	}
}