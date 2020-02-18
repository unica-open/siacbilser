/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacLoginMultiplo;


/**
 * The persistent class for the siac_t_doc_iva database table.
 * 
 */
@Entity
@Table(name="siac_t_doc_iva")
public class SiacTDocIvaFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="dociva_id")
	private Integer docivaId;

	@Column(name="dociva_anno")
	private Integer docivaAnno;

	@Column(name="dociva_data_emissione")
	private Timestamp docivaDataEmissione;

	@Column(name="dociva_data_scadenza")
	private Timestamp docivaDataScadenza;

	@Column(name="dociva_desc")
	private String docivaDesc;

	@Column(name="dociva_importo")
	private BigDecimal docivaImporto;

	@Column(name="dociva_numero")
	private Integer docivaNumero;

	//bi-directional many-to-one association to SiacRDocIvaFin
	@OneToMany(mappedBy="siacTDocIva")
	private List<SiacRDocIvaFin> siacRDocIvas;

	//bi-directional many-to-one association to SiacRDocIvaSogFin
	@OneToMany(mappedBy="siacTDocIva")
	private List<SiacRDocIvaSogFin> siacRDocIvaSogs;

	//bi-directional many-to-one association to SiacDDocTipoFin
	@ManyToOne
	@JoinColumn(name="doc_tipo_id")
	private SiacDDocTipoFin siacDDocTipo;

	public SiacTDocIvaFin() {
	}

	public Integer getDocivaId() {
		return this.docivaId;
	}

	public void setDocivaId(Integer docivaId) {
		this.docivaId = docivaId;
	}

	public Integer getDocivaAnno() {
		return this.docivaAnno;
	}

	public void setDocivaAnno(Integer docivaAnno) {
		this.docivaAnno = docivaAnno;
	}

	public Timestamp getDocivaDataEmissione() {
		return this.docivaDataEmissione;
	}

	public void setDocivaDataEmissione(Timestamp docivaDataEmissione) {
		this.docivaDataEmissione = docivaDataEmissione;
	}

	public Timestamp getDocivaDataScadenza() {
		return this.docivaDataScadenza;
	}

	public void setDocivaDataScadenza(Timestamp docivaDataScadenza) {
		this.docivaDataScadenza = docivaDataScadenza;
	}

	public String getDocivaDesc() {
		return this.docivaDesc;
	}

	public void setDocivaDesc(String docivaDesc) {
		this.docivaDesc = docivaDesc;
	}

	public BigDecimal getDocivaImporto() {
		return this.docivaImporto;
	}

	public void setDocivaImporto(BigDecimal docivaImporto) {
		this.docivaImporto = docivaImporto;
	}

	public Integer getDocivaNumero() {
		return this.docivaNumero;
	}

	public void setDocivaNumero(Integer docivaNumero) {
		this.docivaNumero = docivaNumero;
	}

	public List<SiacRDocIvaFin> getSiacRDocIvas() {
		return this.siacRDocIvas;
	}

	public void setSiacRDocIvas(List<SiacRDocIvaFin> siacRDocIvas) {
		this.siacRDocIvas = siacRDocIvas;
	}

	public SiacRDocIvaFin addSiacRDocIva(SiacRDocIvaFin siacRDocIva) {
		getSiacRDocIvas().add(siacRDocIva);
		siacRDocIva.setSiacTDocIva(this);

		return siacRDocIva;
	}

	public SiacRDocIvaFin removeSiacRDocIva(SiacRDocIvaFin siacRDocIva) {
		getSiacRDocIvas().remove(siacRDocIva);
		siacRDocIva.setSiacTDocIva(null);

		return siacRDocIva;
	}

	public List<SiacRDocIvaSogFin> getSiacRDocIvaSogs() {
		return this.siacRDocIvaSogs;
	}

	public void setSiacRDocIvaSogs(List<SiacRDocIvaSogFin> siacRDocIvaSogs) {
		this.siacRDocIvaSogs = siacRDocIvaSogs;
	}

	public SiacRDocIvaSogFin addSiacRDocIvaSog(SiacRDocIvaSogFin siacRDocIvaSog) {
		getSiacRDocIvaSogs().add(siacRDocIvaSog);
		siacRDocIvaSog.setSiacTDocIva(this);

		return siacRDocIvaSog;
	}

	public SiacRDocIvaSogFin removeSiacRDocIvaSog(SiacRDocIvaSogFin siacRDocIvaSog) {
		getSiacRDocIvaSogs().remove(siacRDocIvaSog);
		siacRDocIvaSog.setSiacTDocIva(null);

		return siacRDocIvaSog;
	}

	public SiacDDocTipoFin getSiacDDocTipo() {
		return this.siacDDocTipo;
	}

	public void setSiacDDocTipo(SiacDDocTipoFin siacDDocTipo) {
		this.siacDDocTipo = siacDDocTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.docivaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.docivaId = uid;
	}


}