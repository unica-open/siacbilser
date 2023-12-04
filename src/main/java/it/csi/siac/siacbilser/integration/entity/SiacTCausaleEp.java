/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_causale_ep database table.
 * 
 */
@Entity
@Table(name="siac_t_causale_ep")
@NamedQuery(name="SiacTCausaleEp.findAll", query="SELECT s FROM SiacTCausaleEp s")
public class SiacTCausaleEp extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CAUSALE_EP_CAUSALEEPID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CAUSALE_EP_CAUSALE_EP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CAUSALE_EP_CAUSALEEPID_GENERATOR")
	@Column(name="causale_ep_id")
	private Integer causaleEpId;

	@Column(name="causale_ep_code")
	private String causaleEpCode;

	@Column(name="causale_ep_desc")
	private String causaleEpDesc;

	@Column(name="causale_ep_default")
	private Boolean causaleEpDefault;

	//bi-directional many-to-one association to SiacRCausaleEpClass
	@OneToMany(mappedBy="siacTCausaleEp", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleEpClass> siacRCausaleEpClasses;

	//bi-directional many-to-one association to SiacRCausaleEpPdceConto
	@OneToMany(mappedBy="siacTCausaleEp", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleEpPdceConto> siacRCausaleEpPdceContos;

	//bi-directional many-to-one association to SiacRCausaleEpSoggetto
	@OneToMany(mappedBy="siacTCausaleEp", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleEpSoggetto> siacRCausaleEpSoggettos;

	//bi-directional many-to-one association to SiacRCausaleEpStato
	@OneToMany(mappedBy="siacTCausaleEp", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleEpStato> siacRCausaleEpStatos;

	//bi-directional many-to-one association to SiacREventoCausale
	@OneToMany(mappedBy="siacTCausaleEp", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacREventoCausale> siacREventoCausales;

	//bi-directional many-to-one association to SiacDCausaleEpTipo
	@ManyToOne
	@JoinColumn(name="causale_ep_tipo_id")
	private SiacDCausaleEpTipo siacDCausaleEpTipo;

	//bi-directional many-to-one association to SiacTMovEp
	@OneToMany(mappedBy="siacTCausaleEp")
	private List<SiacTMovEp> siacTMovEps;
	
	//bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;

//	//bi-directional many-to-one association to SiacRConciliazioneClasseCausaleEp
//	@OneToMany(mappedBy="siacTCausaleEp", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
//	private List<SiacRConciliazioneClasseCausaleEp> siacRConciliazioneClasseCausaleEps; //deprecated SIAC-4956

	public SiacTCausaleEp() {
	}

	public Integer getCausaleEpId() {
		return this.causaleEpId;
	}

	public void setCausaleEpId(Integer causaleEpId) {
		this.causaleEpId = causaleEpId;
	}

	public String getCausaleEpCode() {
		return this.causaleEpCode;
	}

	public void setCausaleEpCode(String causaleEpCode) {
		this.causaleEpCode = causaleEpCode;
	}

	public String getCausaleEpDesc() {
		return this.causaleEpDesc;
	}

	public void setCausaleEpDesc(String causaleEpDesc) {
		this.causaleEpDesc = causaleEpDesc;
	}

	public Boolean getCausaleEpDefault() {
		return causaleEpDefault;
	}

	public void setCausaleEpDefault(Boolean causaleEpDefault) {
		this.causaleEpDefault = causaleEpDefault;
	}

	public List<SiacRCausaleEpClass> getSiacRCausaleEpClasses() {
		return this.siacRCausaleEpClasses;
	}

	public void setSiacRCausaleEpClasses(List<SiacRCausaleEpClass> siacRCausaleEpClasses) {
		this.siacRCausaleEpClasses = siacRCausaleEpClasses;
	}

	public SiacRCausaleEpClass addSiacRCausaleEpClass(SiacRCausaleEpClass siacRCausaleEpClass) {
		getSiacRCausaleEpClasses().add(siacRCausaleEpClass);
		siacRCausaleEpClass.setSiacTCausaleEp(this);

		return siacRCausaleEpClass;
	}

	public SiacRCausaleEpClass removeSiacRCausaleEpClass(SiacRCausaleEpClass siacRCausaleEpClass) {
		getSiacRCausaleEpClasses().remove(siacRCausaleEpClass);
		siacRCausaleEpClass.setSiacTCausaleEp(null);

		return siacRCausaleEpClass;
	}

	public List<SiacRCausaleEpPdceConto> getSiacRCausaleEpPdceContos() {
		return this.siacRCausaleEpPdceContos;
	}

	public void setSiacRCausaleEpPdceContos(List<SiacRCausaleEpPdceConto> siacRCausaleEpPdceContos) {
		this.siacRCausaleEpPdceContos = siacRCausaleEpPdceContos;
	}

	public SiacRCausaleEpPdceConto addSiacRCausaleEpPdceConto(SiacRCausaleEpPdceConto siacRCausaleEpPdceConto) {
		getSiacRCausaleEpPdceContos().add(siacRCausaleEpPdceConto);
		siacRCausaleEpPdceConto.setSiacTCausaleEp(this);

		return siacRCausaleEpPdceConto;
	}

	public SiacRCausaleEpPdceConto removeSiacRCausaleEpPdceConto(SiacRCausaleEpPdceConto siacRCausaleEpPdceConto) {
		getSiacRCausaleEpPdceContos().remove(siacRCausaleEpPdceConto);
		siacRCausaleEpPdceConto.setSiacTCausaleEp(null);

		return siacRCausaleEpPdceConto;
	}

	public List<SiacRCausaleEpSoggetto> getSiacRCausaleEpSoggettos() {
		return this.siacRCausaleEpSoggettos;
	}

	public void setSiacRCausaleEpSoggettos(List<SiacRCausaleEpSoggetto> siacRCausaleEpSoggettos) {
		this.siacRCausaleEpSoggettos = siacRCausaleEpSoggettos;
	}

	public SiacRCausaleEpSoggetto addSiacRCausaleEpSoggetto(SiacRCausaleEpSoggetto siacRCausaleEpSoggetto) {
		getSiacRCausaleEpSoggettos().add(siacRCausaleEpSoggetto);
		siacRCausaleEpSoggetto.setSiacTCausaleEp(this);

		return siacRCausaleEpSoggetto;
	}

	public SiacRCausaleEpSoggetto removeSiacRCausaleEpSoggetto(SiacRCausaleEpSoggetto siacRCausaleEpSoggetto) {
		getSiacRCausaleEpSoggettos().remove(siacRCausaleEpSoggetto);
		siacRCausaleEpSoggetto.setSiacTCausaleEp(null);

		return siacRCausaleEpSoggetto;
	}

	public List<SiacRCausaleEpStato> getSiacRCausaleEpStatos() {
		return this.siacRCausaleEpStatos;
	}

	public void setSiacRCausaleEpStatos(List<SiacRCausaleEpStato> siacRCausaleEpStatos) {
		this.siacRCausaleEpStatos = siacRCausaleEpStatos;
	}

	public SiacRCausaleEpStato addSiacRCausaleEpStato(SiacRCausaleEpStato siacRCausaleEpStato) {
		getSiacRCausaleEpStatos().add(siacRCausaleEpStato);
		siacRCausaleEpStato.setSiacTCausaleEp(this);

		return siacRCausaleEpStato;
	}

	public SiacRCausaleEpStato removeSiacRCausaleEpStato(SiacRCausaleEpStato siacRCausaleEpStato) {
		getSiacRCausaleEpStatos().remove(siacRCausaleEpStato);
		siacRCausaleEpStato.setSiacTCausaleEp(null);

		return siacRCausaleEpStato;
	}

	public List<SiacREventoCausale> getSiacREventoCausales() {
		return this.siacREventoCausales;
	}

	public void setSiacREventoCausales(List<SiacREventoCausale> siacREventoCausales) {
		this.siacREventoCausales = siacREventoCausales;
	}

	public SiacREventoCausale addSiacREventoCausale(SiacREventoCausale siacREventoCausale) {
		getSiacREventoCausales().add(siacREventoCausale);
		siacREventoCausale.setSiacTCausaleEp(this);

		return siacREventoCausale;
	}

	public SiacREventoCausale removeSiacREventoCausale(SiacREventoCausale siacREventoCausale) {
		getSiacREventoCausales().remove(siacREventoCausale);
		siacREventoCausale.setSiacTCausaleEp(null);

		return siacREventoCausale;
	}

	public SiacDCausaleEpTipo getSiacDCausaleEpTipo() {
		return this.siacDCausaleEpTipo;
	}

	public void setSiacDCausaleEpTipo(SiacDCausaleEpTipo siacDCausaleEpTipo) {
		this.siacDCausaleEpTipo = siacDCausaleEpTipo;
	}

	public List<SiacTMovEp> getSiacTMovEps() {
		return this.siacTMovEps;
	}

	public void setSiacTMovEps(List<SiacTMovEp> siacTMovEps) {
		this.siacTMovEps = siacTMovEps;
	}

	public SiacTMovEp addSiacTMovEp(SiacTMovEp siacTMovEp) {
		getSiacTMovEps().add(siacTMovEp);
		siacTMovEp.setSiacTCausaleEp(this);

		return siacTMovEp;
	}

	public SiacTMovEp removeSiacTMovEp(SiacTMovEp siacTMovEp) {
		getSiacTMovEps().remove(siacTMovEp);
		siacTMovEp.setSiacTCausaleEp(null);

		return siacTMovEp;
	}
	
	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}

//	public List<SiacRConciliazioneClasseCausaleEp> getSiacRConciliazioneClasseCausaleEps() {
//		return this.siacRConciliazioneClasseCausaleEps;
//	}
//
//	public void setSiacRConciliazioneClasseCausaleEps(List<SiacRConciliazioneClasseCausaleEp> siacRConciliazioneClasseCausaleEps) {
//		this.siacRConciliazioneClasseCausaleEps = siacRConciliazioneClasseCausaleEps;
//	}
//
//	public SiacRConciliazioneClasseCausaleEp addSiacRConciliazioneClasseCausaleEp(SiacRConciliazioneClasseCausaleEp siacRConciliazioneClasseCausaleEp) {
//		getSiacRConciliazioneClasseCausaleEps().add(siacRConciliazioneClasseCausaleEp);
//		siacRConciliazioneClasseCausaleEp.setSiacTCausaleEp(this);
//
//		return siacRConciliazioneClasseCausaleEp;
//	}
//
//	public SiacRConciliazioneClasseCausaleEp removeSiacRConciliazioneClasseCausaleEp(SiacRConciliazioneClasseCausaleEp siacRConciliazioneClasseCausaleEp) {
//		getSiacRConciliazioneClasseCausaleEps().remove(siacRConciliazioneClasseCausaleEp);
//		siacRConciliazioneClasseCausaleEp.setSiacTCausaleEp(null);
//
//		return siacRConciliazioneClasseCausaleEp;
//	}

	@Override
	public Integer getUid() {
		return this.causaleEpId;
	}

	@Override
	public void setUid(Integer uid) {
		this.causaleEpId = uid;
	}

}