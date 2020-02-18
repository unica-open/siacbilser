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
 * The persistent class for the siac_t_cassa_econ_stampa database table.
 * 
 */
@Entity
@Table(name="siac_t_cassa_econ_stampa")
@NamedQuery(name="SiacTCassaEconStampa.findAll", query="SELECT s FROM SiacTCassaEconStampa s")
public class SiacTCassaEconStampa extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CASSA_ECON_STAMPA_CESTID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CASSA_ECON_STAMPA_CEST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CASSA_ECON_STAMPA_CESTID_GENERATOR")
	@Column(name="cest_id")
	private Integer cestId;

	@Column(name="cest_anno")
	private Integer cestAnno;

	@Column(name="cest_code")
	private String cestCode;

	@Column(name="cest_desc")
	private String cestDesc;

	//bi-directional many-to-one association to SiacRCassaEconOperazStampa
	@OneToMany(mappedBy="siacTCassaEconStampa", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<SiacRCassaEconOperazStampa> siacRCassaEconOperazStampas;

	//bi-directional many-to-one association to SiacRCassaEconStampaFile
	@OneToMany(mappedBy="siacTCassaEconStampa", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<SiacRCassaEconStampaFile> siacRCassaEconStampaFiles;

	//bi-directional many-to-one association to SiacRCassaEconStampaStato
	@OneToMany(mappedBy="siacTCassaEconStampa", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<SiacRCassaEconStampaStato> siacRCassaEconStampaStatos;

	//bi-directional many-to-one association to SiacRMovimentoStampa
	@OneToMany(mappedBy="siacTCassaEconStampa", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<SiacRMovimentoStampa> siacRMovimentoStampas;

	//bi-directional many-to-one association to SiacDCassaEconStampaTipo
	@ManyToOne
	@JoinColumn(name="cest_tipo_id")
	private SiacDCassaEconStampaTipo siacDCassaEconStampaTipo;

	//bi-directional many-to-one association to SiacTBil
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;

	//bi-directional many-to-one association to SiacTCassaEconStampaValore
	@OneToMany(mappedBy="siacTCassaEconStampa", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<SiacTCassaEconStampaValore> siacTCassaEconStampaValores;
	
	// SIAC-4799
	// bi-directional many-to-one association to SiacTAttoAllegato
	@ManyToOne
	@JoinColumn(name="attoal_id")
	private SiacTAttoAllegato siacTAttoAllegato;

	public SiacTCassaEconStampa() {
	}

	public Integer getCestId() {
		return this.cestId;
	}

	public void setCestId(Integer cestId) {
		this.cestId = cestId;
	}

	public Integer getCestAnno() {
		return this.cestAnno;
	}

	public void setCestAnno(Integer cestAnno) {
		this.cestAnno = cestAnno;
	}

	public String getCestCode() {
		return this.cestCode;
	}

	public void setCestCode(String cestCode) {
		this.cestCode = cestCode;
	}

	public String getCestDesc() {
		return this.cestDesc;
	}

	public void setCestDesc(String cestDesc) {
		this.cestDesc = cestDesc;
	}

	public List<SiacRCassaEconOperazStampa> getSiacRCassaEconOperazStampas() {
		return this.siacRCassaEconOperazStampas;
	}

	public void setSiacRCassaEconOperazStampas(List<SiacRCassaEconOperazStampa> siacRCassaEconOperazStampas) {
		this.siacRCassaEconOperazStampas = siacRCassaEconOperazStampas;
	}

	public SiacRCassaEconOperazStampa addSiacRCassaEconOperazStampa(SiacRCassaEconOperazStampa siacRCassaEconOperazStampa) {
		getSiacRCassaEconOperazStampas().add(siacRCassaEconOperazStampa);
		siacRCassaEconOperazStampa.setSiacTCassaEconStampa(this);

		return siacRCassaEconOperazStampa;
	}

	public SiacRCassaEconOperazStampa removeSiacRCassaEconOperazStampa(SiacRCassaEconOperazStampa siacRCassaEconOperazStampa) {
		getSiacRCassaEconOperazStampas().remove(siacRCassaEconOperazStampa);
		siacRCassaEconOperazStampa.setSiacTCassaEconStampa(null);

		return siacRCassaEconOperazStampa;
	}

	public List<SiacRCassaEconStampaFile> getSiacRCassaEconStampaFiles() {
		return this.siacRCassaEconStampaFiles;
	}

	public void setSiacRCassaEconStampaFiles(List<SiacRCassaEconStampaFile> siacRCassaEconStampaFiles) {
		this.siacRCassaEconStampaFiles = siacRCassaEconStampaFiles;
	}

	public SiacRCassaEconStampaFile addSiacRCassaEconStampaFile(SiacRCassaEconStampaFile siacRCassaEconStampaFile) {
		getSiacRCassaEconStampaFiles().add(siacRCassaEconStampaFile);
		siacRCassaEconStampaFile.setSiacTCassaEconStampa(this);

		return siacRCassaEconStampaFile;
	}

	public SiacRCassaEconStampaFile removeSiacRCassaEconStampaFile(SiacRCassaEconStampaFile siacRCassaEconStampaFile) {
		getSiacRCassaEconStampaFiles().remove(siacRCassaEconStampaFile);
		siacRCassaEconStampaFile.setSiacTCassaEconStampa(null);

		return siacRCassaEconStampaFile;
	}

	public List<SiacRCassaEconStampaStato> getSiacRCassaEconStampaStatos() {
		return this.siacRCassaEconStampaStatos;
	}

	public void setSiacRCassaEconStampaStatos(List<SiacRCassaEconStampaStato> siacRCassaEconStampaStatos) {
		this.siacRCassaEconStampaStatos = siacRCassaEconStampaStatos;
	}

	public SiacRCassaEconStampaStato addSiacRCassaEconStampaStato(SiacRCassaEconStampaStato siacRCassaEconStampaStato) {
		getSiacRCassaEconStampaStatos().add(siacRCassaEconStampaStato);
		siacRCassaEconStampaStato.setSiacTCassaEconStampa(this);

		return siacRCassaEconStampaStato;
	}

	public SiacRCassaEconStampaStato removeSiacRCassaEconStampaStato(SiacRCassaEconStampaStato siacRCassaEconStampaStato) {
		getSiacRCassaEconStampaStatos().remove(siacRCassaEconStampaStato);
		siacRCassaEconStampaStato.setSiacTCassaEconStampa(null);

		return siacRCassaEconStampaStato;
	}

	public List<SiacRMovimentoStampa> getSiacRMovimentoStampas() {
		return this.siacRMovimentoStampas;
	}

	public void setSiacRMovimentoStampas(List<SiacRMovimentoStampa> siacRMovimentoStampas) {
		this.siacRMovimentoStampas = siacRMovimentoStampas;
	}

	public SiacRMovimentoStampa addSiacRMovimentoStampa(SiacRMovimentoStampa siacRMovimentoStampa) {
		getSiacRMovimentoStampas().add(siacRMovimentoStampa);
		siacRMovimentoStampa.setSiacTCassaEconStampa(this);

		return siacRMovimentoStampa;
	}

	public SiacRMovimentoStampa removeSiacRMovimentoStampa(SiacRMovimentoStampa siacRMovimentoStampa) {
		getSiacRMovimentoStampas().remove(siacRMovimentoStampa);
		siacRMovimentoStampa.setSiacTCassaEconStampa(null);

		return siacRMovimentoStampa;
	}

	public SiacDCassaEconStampaTipo getSiacDCassaEconStampaTipo() {
		return this.siacDCassaEconStampaTipo;
	}

	public void setSiacDCassaEconStampaTipo(SiacDCassaEconStampaTipo siacDCassaEconStampaTipo) {
		this.siacDCassaEconStampaTipo = siacDCassaEconStampaTipo;
	}

	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	public SiacTCassaEcon getSiacTCassaEcon() {
		return this.siacTCassaEcon;
	}

	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}

	public List<SiacTCassaEconStampaValore> getSiacTCassaEconStampaValores() {
		return this.siacTCassaEconStampaValores;
	}

	public void setSiacTCassaEconStampaValores(List<SiacTCassaEconStampaValore> siacTCassaEconStampaValores) {
		this.siacTCassaEconStampaValores = siacTCassaEconStampaValores;
	}

	public SiacTCassaEconStampaValore addSiacTCassaEconStampaValore(SiacTCassaEconStampaValore siacTCassaEconStampaValore) {
		getSiacTCassaEconStampaValores().add(siacTCassaEconStampaValore);
		siacTCassaEconStampaValore.setSiacTCassaEconStampa(this);

		return siacTCassaEconStampaValore;
	}

	public SiacTCassaEconStampaValore removeSiacTCassaEconStampaValore(SiacTCassaEconStampaValore siacTCassaEconStampaValore) {
		getSiacTCassaEconStampaValores().remove(siacTCassaEconStampaValore);
		siacTCassaEconStampaValore.setSiacTCassaEconStampa(null);

		return siacTCassaEconStampaValore;
	}

	public SiacTAttoAllegato getSiacTAttoAllegato() {
		return siacTAttoAllegato;
	}

	public void setSiacTAttoAllegato(SiacTAttoAllegato siacTAttoAllegato) {
		this.siacTAttoAllegato = siacTAttoAllegato;
	}

	@Override
	public Integer getUid() {
		return this.cestId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cestId = uid;
	}

}