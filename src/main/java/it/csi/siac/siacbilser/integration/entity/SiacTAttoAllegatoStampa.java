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
 * The persistent class for the siac_t_atto_allegato_stampa database table.
 * 
 */
@Entity
@Table(name="siac_t_atto_allegato_stampa")
@NamedQuery(name="SiacTAttoAllegatoStampa.findAll", query="SELECT s FROM SiacTAttoAllegatoStampa s")
public class SiacTAttoAllegatoStampa extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_ATTO_ALLEGATO_STAMPA_ATTOALSTID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ATTO_ALLEGATO_STAMPA_ATTOALST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ATTO_ALLEGATO_STAMPA_ATTOALSTID_GENERATOR")
	@Column(name="attoalst_id")
	private Integer attoalstId;

	@Column(name="attoal_versione_invio_firma")
	private Integer attoalVersioneInvioFirma;

	@Column(name="attoalst_anno")
	private Integer attoalstAnno;

	@Column(name="attoalst_code")
	private String attoalstCode;

	@Column(name="attoalst_desc")
	private String attoalstDesc;

	//bi-directional many-to-one association to SiacRAllegatoAttoStampaStato
	@OneToMany(mappedBy="siacTAttoAllegatoStampa", cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRAllegatoAttoStampaStato> siacRAllegatoAttoStampaStatos;

	//bi-directional many-to-one association to SiacRAttoAllegatoStampaFile
	@OneToMany(mappedBy="siacTAttoAllegatoStampa", cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRAttoAllegatoStampaFile> siacRAttoAllegatoStampaFiles;

	//bi-directional many-to-one association to SiacDAttoAllegatoStampaTipo
	@ManyToOne
	@JoinColumn(name="attoalst_tipo_id")
	private SiacDAttoAllegatoStampaTipo siacDAttoAllegatoStampaTipo;

	//bi-directional many-to-one association to SiacTAttoAllegato
	@ManyToOne
	@JoinColumn(name="attoal_id")
	private SiacTAttoAllegato siacTAttoAllegato;

	//bi-directional many-to-one association to SiacTBil
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	public SiacTAttoAllegatoStampa() {
	}

	public Integer getAttoalstId() {
		return this.attoalstId;
	}

	public void setAttoalstId(Integer attoalstId) {
		this.attoalstId = attoalstId;
	}

	public Integer getAttoalVersioneInvioFirma() {
		return this.attoalVersioneInvioFirma;
	}

	public void setAttoalVersioneInvioFirma(Integer attoalVersioneInvioFirma) {
		this.attoalVersioneInvioFirma = attoalVersioneInvioFirma;
	}

	public Integer getAttoalstAnno() {
		return this.attoalstAnno;
	}

	public void setAttoalstAnno(Integer attoalstAnno) {
		this.attoalstAnno = attoalstAnno;
	}

	public String getAttoalstCode() {
		return this.attoalstCode;
	}

	public void setAttoalstCode(String attoalstCode) {
		this.attoalstCode = attoalstCode;
	}

	public String getAttoalstDesc() {
		return this.attoalstDesc;
	}

	public void setAttoalstDesc(String attoalstDesc) {
		this.attoalstDesc = attoalstDesc;
	}

	public List<SiacRAllegatoAttoStampaStato> getSiacRAllegatoAttoStampaStatos() {
		return this.siacRAllegatoAttoStampaStatos;
	}

	public void setSiacRAllegatoAttoStampaStatos(List<SiacRAllegatoAttoStampaStato> siacRAllegatoAttoStampaStatos) {
		this.siacRAllegatoAttoStampaStatos = siacRAllegatoAttoStampaStatos;
	}

	public SiacRAllegatoAttoStampaStato addSiacRAllegatoAttoStampaStato(SiacRAllegatoAttoStampaStato siacRAllegatoAttoStampaStato) {
		getSiacRAllegatoAttoStampaStatos().add(siacRAllegatoAttoStampaStato);
		siacRAllegatoAttoStampaStato.setSiacTAttoAllegatoStampa(this);

		return siacRAllegatoAttoStampaStato;
	}

	public SiacRAllegatoAttoStampaStato removeSiacRAllegatoAttoStampaStato(SiacRAllegatoAttoStampaStato siacRAllegatoAttoStampaStato) {
		getSiacRAllegatoAttoStampaStatos().remove(siacRAllegatoAttoStampaStato);
		siacRAllegatoAttoStampaStato.setSiacTAttoAllegatoStampa(null);

		return siacRAllegatoAttoStampaStato;
	}

	public List<SiacRAttoAllegatoStampaFile> getSiacRAttoAllegatoStampaFiles() {
		return this.siacRAttoAllegatoStampaFiles;
	}

	public void setSiacRAttoAllegatoStampaFiles(List<SiacRAttoAllegatoStampaFile> siacRAttoAllegatoStampaFiles) {
		this.siacRAttoAllegatoStampaFiles = siacRAttoAllegatoStampaFiles;
	}

	public SiacRAttoAllegatoStampaFile addSiacRAttoAllegatoStampaFile(SiacRAttoAllegatoStampaFile siacRAttoAllegatoStampaFile) {
		getSiacRAttoAllegatoStampaFiles().add(siacRAttoAllegatoStampaFile);
		siacRAttoAllegatoStampaFile.setSiacTAttoAllegatoStampa(this);

		return siacRAttoAllegatoStampaFile;
	}

	public SiacRAttoAllegatoStampaFile removeSiacRAttoAllegatoStampaFile(SiacRAttoAllegatoStampaFile siacRAttoAllegatoStampaFile) {
		getSiacRAttoAllegatoStampaFiles().remove(siacRAttoAllegatoStampaFile);
		siacRAttoAllegatoStampaFile.setSiacTAttoAllegatoStampa(null);

		return siacRAttoAllegatoStampaFile;
	}

	public SiacDAttoAllegatoStampaTipo getSiacDAttoAllegatoStampaTipo() {
		return this.siacDAttoAllegatoStampaTipo;
	}

	public void setSiacDAttoAllegatoStampaTipo(SiacDAttoAllegatoStampaTipo siacDAttoAllegatoStampaTipo) {
		this.siacDAttoAllegatoStampaTipo = siacDAttoAllegatoStampaTipo;
	}

	public SiacTAttoAllegato getSiacTAttoAllegato() {
		return this.siacTAttoAllegato;
	}

	public void setSiacTAttoAllegato(SiacTAttoAllegato siacTAttoAllegato) {
		this.siacTAttoAllegato = siacTAttoAllegato;
	}

	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	@Override
	public Integer getUid() {
		return this.attoalstId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoalstId = uid;
	}

}