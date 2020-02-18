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
 * The persistent class for the siac_d_causale_ep_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_causale_ep_tipo")
@NamedQuery(name="SiacDCausaleEpTipo.findAll", query="SELECT s FROM SiacDCausaleEpTipo s")
public class SiacDCausaleEpTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CAUSALE_EP_TIPO_CAUSALEEPTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CAUSALE_EP_TIPO_CAUSALE_EP_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CAUSALE_EP_TIPO_CAUSALEEPTIPOID_GENERATOR")
	@Column(name="causale_ep_tipo_id")
	private Integer causaleEpTipoId;

	@Column(name="causale_ep_tipo_code")
	private String causaleEpTipoCode;

	@Column(name="causale_ep_tipo_desc")
	private String causaleEpTipoDesc;
	
	//bi-directional many-to-one association to SiacTCausaleEp
	@OneToMany(mappedBy="siacDCausaleEpTipo")
	private List<SiacTCausaleEp> siacTCausaleEps;

	//bi-directional many-to-one association to SiacTPrimaNota
	@OneToMany(mappedBy="siacDCausaleEpTipo")
	private List<SiacTPrimaNota> siacTPrimaNotas;
	
	//bi-directional many-to-one association to SiacRCausaleEpTipoEventoTipo
	@OneToMany(mappedBy="siacDCausaleEpTipo")
	private List<SiacRCausaleEpTipoEventoTipo> siacRCausaleEpTipoEventoTipos;

	public SiacDCausaleEpTipo() {
	}

	public Integer getCausaleEpTipoId() {
		return this.causaleEpTipoId;
	}

	public void setCausaleEpTipoId(Integer causaleEpTipoId) {
		this.causaleEpTipoId = causaleEpTipoId;
	}

	public String getCausaleEpTipoCode() {
		return this.causaleEpTipoCode;
	}

	public void setCausaleEpTipoCode(String causaleEpTipoCode) {
		this.causaleEpTipoCode = causaleEpTipoCode;
	}

	public String getCausaleEpTipoDesc() {
		return this.causaleEpTipoDesc;
	}

	public void setCausaleEpTipoDesc(String causaleEpTipoDesc) {
		this.causaleEpTipoDesc = causaleEpTipoDesc;
	}

	public List<SiacTCausaleEp> getSiacTCausaleEps() {
		return this.siacTCausaleEps;
	}

	public void setSiacTCausaleEps(List<SiacTCausaleEp> siacTCausaleEps) {
		this.siacTCausaleEps = siacTCausaleEps;
	}

	public SiacTCausaleEp addSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
		getSiacTCausaleEps().add(siacTCausaleEp);
		siacTCausaleEp.setSiacDCausaleEpTipo(this);

		return siacTCausaleEp;
	}

	public SiacTCausaleEp removeSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
		getSiacTCausaleEps().remove(siacTCausaleEp);
		siacTCausaleEp.setSiacDCausaleEpTipo(null);

		return siacTCausaleEp;
	}

	public List<SiacTPrimaNota> getSiacTPrimaNotas() {
		return this.siacTPrimaNotas;
	}

	public void setSiacTPrimaNotas(List<SiacTPrimaNota> siacTPrimaNotas) {
		this.siacTPrimaNotas = siacTPrimaNotas;
	}

	public SiacTPrimaNota addSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		getSiacTPrimaNotas().add(siacTPrimaNota);
		siacTPrimaNota.setSiacDCausaleEpTipo(this);

		return siacTPrimaNota;
	}

	public SiacTPrimaNota removeSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		getSiacTPrimaNotas().remove(siacTPrimaNota);
		siacTPrimaNota.setSiacDCausaleEpTipo(null);

		return siacTPrimaNota;
	}
	
	public List<SiacRCausaleEpTipoEventoTipo> getSiacRCausaleEpTipoEventoTipos() {
		return this.siacRCausaleEpTipoEventoTipos;
	}

	public void setSiacRCausaleEpTipoEventoTipos(List<SiacRCausaleEpTipoEventoTipo> siacRCausaleEpTipoEventoTipos) {
		this.siacRCausaleEpTipoEventoTipos = siacRCausaleEpTipoEventoTipos;
	}

	public SiacRCausaleEpTipoEventoTipo addSiacRCausaleEpTipoEventoTipo(SiacRCausaleEpTipoEventoTipo siacRCausaleEpTipoEventoTipo) {
		getSiacRCausaleEpTipoEventoTipos().add(siacRCausaleEpTipoEventoTipo);
		siacRCausaleEpTipoEventoTipo.setSiacDCausaleEpTipo(this);

		return siacRCausaleEpTipoEventoTipo;
	}

	public SiacRCausaleEpTipoEventoTipo removeSiacRCausaleEpTipoEventoTipo(SiacRCausaleEpTipoEventoTipo siacRCausaleEpTipoEventoTipo) {
		getSiacRCausaleEpTipoEventoTipos().remove(siacRCausaleEpTipoEventoTipo);
		siacRCausaleEpTipoEventoTipo.setSiacDCausaleEpTipo(null);

		return siacRCausaleEpTipoEventoTipo;
	}

	@Override
	public Integer getUid() {
		return causaleEpTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.causaleEpTipoId = uid;
	}

}