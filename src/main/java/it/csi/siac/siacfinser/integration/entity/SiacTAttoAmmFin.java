/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_t_atto_amm")
public class SiacTAttoAmmFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attoamm_id")
	private Integer attoammId;

	@Column(name="attoamm_anno")
	private String attoammAnno;

	@Column(name="attoamm_note")
	private String attoammNote;

	@Column(name="attoamm_numero")
	private Integer attoammNumero;

	@Column(name="attoamm_oggetto")
	private String attoammOggetto;
	
	@Column(name="attoamm_blocco")
	private Boolean attoammBlocco;

	@Column(name="attoamm_provenienza")
	private String attoammProvenienza;
	
	/** The attoamm temporaneo. */
	@Column(name="parere_regolarita_contabile")
	private Boolean parereRegolaritaContabile;
	
	//bi-directional many-to-one association to SiacTAttoAllegato
	/** The siac t atto allegatos. */
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacTAllegatoAttoFin> siacTAttoAllegatos;

	//bi-directional many-to-one association to SiacRAttoAmmClassFin
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRAttoAmmClassFin> siacRAttoAmmClasses;

	//bi-directional many-to-one association to SiacRAttoAmmClassFin
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRCronopAttoAmmFin> siacRCronopAttoAmms;

	//bi-directional many-to-one association to SiacRAttoAmmStatoFin
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRAttoAmmStatoFin> siacRAttoAmmStatos;

	//bi-directional many-to-one association to SiacRMovgestTsAttoAmmFin
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRMovgestTsAttoAmmFin> siacRMovgestTsAttoAmms;

	//bi-directional many-to-one association to SiacRVariazioneStatoFin
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRVariazioneStatoFin> siacRVariazioneStatos;

	//bi-directional many-to-one association to SiacDAttoAmmTipoFin
	@ManyToOne
	@JoinColumn(name="attoamm_tipo_id")
	private SiacDAttoAmmTipoFin siacDAttoAmmTipo;

	//bi-directional many-to-one association to SiacRMutuoAttoAmmFin
	@OneToMany(mappedBy="siacTAttoAmm")
	private List<SiacRMutuoAttoAmmFin> siacRMutuoAttoAmms;

	public SiacTAttoAmmFin() {
	}

	public Integer getAttoammId() {
		return this.attoammId;
	}

	public void setAttoammId(Integer attoammId) {
		this.attoammId = attoammId;
	}

	public String getAttoammAnno() {
		return this.attoammAnno;
	}

	public void setAttoammAnno(String attoammAnno) {
		this.attoammAnno = attoammAnno;
	}

	public String getAttoammNote() {
		return this.attoammNote;
	}

	public void setAttoammNote(String attoammNote) {
		this.attoammNote = attoammNote;
	}

	public Integer getAttoammNumero() {
		return this.attoammNumero;
	}

	public void setAttoammNumero(Integer attoammNumero) {
		this.attoammNumero = attoammNumero;
	}

	public String getAttoammOggetto() {
		return this.attoammOggetto;
	}

	public void setAttoammOggetto(String attoammOggetto) {
		this.attoammOggetto = attoammOggetto;
	}

	public List<SiacRAttoAmmClassFin> getSiacRAttoAmmClasses() {
		return this.siacRAttoAmmClasses;
	}

	public void setSiacRAttoAmmClasses(List<SiacRAttoAmmClassFin> siacRAttoAmmClasses) {
		this.siacRAttoAmmClasses = siacRAttoAmmClasses;
	}

	public SiacRAttoAmmClassFin addSiacRAttoAmmClass(SiacRAttoAmmClassFin siacRAttoAmmClass) {
		getSiacRAttoAmmClasses().add(siacRAttoAmmClass);
		siacRAttoAmmClass.setSiacTAttoAmm(this);

		return siacRAttoAmmClass;
	}

	public SiacRAttoAmmClassFin removeSiacRAttoAmmClass(SiacRAttoAmmClassFin siacRAttoAmmClass) {
		getSiacRAttoAmmClasses().remove(siacRAttoAmmClass);
		siacRAttoAmmClass.setSiacTAttoAmm(null);

		return siacRAttoAmmClass;
	}

	public List<SiacRAttoAmmStatoFin> getSiacRAttoAmmStatos() {
		return this.siacRAttoAmmStatos;
	}

	public void setSiacRAttoAmmStatos(List<SiacRAttoAmmStatoFin> siacRAttoAmmStatos) {
		this.siacRAttoAmmStatos = siacRAttoAmmStatos;
	}

	public SiacRAttoAmmStatoFin addSiacRAttoAmmStato(SiacRAttoAmmStatoFin siacRAttoAmmStato) {
		getSiacRAttoAmmStatos().add(siacRAttoAmmStato);
		siacRAttoAmmStato.setSiacTAttoAmm(this);

		return siacRAttoAmmStato;
	}

	public SiacRAttoAmmStatoFin removeSiacRAttoAmmStato(SiacRAttoAmmStatoFin siacRAttoAmmStato) {
		getSiacRAttoAmmStatos().remove(siacRAttoAmmStato);
		siacRAttoAmmStato.setSiacTAttoAmm(null);

		return siacRAttoAmmStato;
	}

	public List<SiacRMovgestTsAttoAmmFin> getSiacRMovgestTsAttoAmms() {
		return this.siacRMovgestTsAttoAmms;
	}

	public void setSiacRMovgestTsAttoAmms(List<SiacRMovgestTsAttoAmmFin> siacRMovgestTsAttoAmms) {
		this.siacRMovgestTsAttoAmms = siacRMovgestTsAttoAmms;
	}

	public SiacRMovgestTsAttoAmmFin addSiacRMovgestTsAttoAmm(SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm) {
		getSiacRMovgestTsAttoAmms().add(siacRMovgestTsAttoAmm);
		siacRMovgestTsAttoAmm.setSiacTAttoAmm(this);

		return siacRMovgestTsAttoAmm;
	}

	public SiacRMovgestTsAttoAmmFin removeSiacRMovgestTsAttoAmm(SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm) {
		getSiacRMovgestTsAttoAmms().remove(siacRMovgestTsAttoAmm);
		siacRMovgestTsAttoAmm.setSiacTAttoAmm(null);

		return siacRMovgestTsAttoAmm;
	}

	public List<SiacRVariazioneStatoFin> getSiacRVariazioneStatos() {
		return this.siacRVariazioneStatos;
	}

	public void setSiacRVariazioneStatos(List<SiacRVariazioneStatoFin> siacRVariazioneStatos) {
		this.siacRVariazioneStatos = siacRVariazioneStatos;
	}

	public SiacRVariazioneStatoFin addSiacRVariazioneStato(SiacRVariazioneStatoFin siacRVariazioneStato) {
		getSiacRVariazioneStatos().add(siacRVariazioneStato);
		siacRVariazioneStato.setSiacTAttoAmm(this);

		return siacRVariazioneStato;
	}

	public SiacRVariazioneStatoFin removeSiacRVariazioneStato(SiacRVariazioneStatoFin siacRVariazioneStato) {
		getSiacRVariazioneStatos().remove(siacRVariazioneStato);
		siacRVariazioneStato.setSiacTAttoAmm(null);

		return siacRVariazioneStato;
	}

	public SiacDAttoAmmTipoFin getSiacDAttoAmmTipo() {
		return this.siacDAttoAmmTipo;
	}

	public void setSiacDAttoAmmTipo(SiacDAttoAmmTipoFin siacDAttoAmmTipo) {
		this.siacDAttoAmmTipo = siacDAttoAmmTipo;
	}

	public List<SiacRMutuoAttoAmmFin> getSiacRMutuoAttoAmms() {
		return this.siacRMutuoAttoAmms;
	}

	public void setSiacRMutuoAttoAmms(List<SiacRMutuoAttoAmmFin> siacRMutuoAttoAmms) {
		this.siacRMutuoAttoAmms = siacRMutuoAttoAmms;
	}

	public SiacRMutuoAttoAmmFin addSiacRMutuoAttoAmm(SiacRMutuoAttoAmmFin siacRMutuoAttoAmm) {
		getSiacRMutuoAttoAmms().add(siacRMutuoAttoAmm);
		siacRMutuoAttoAmm.setSiacTAttoAmm(this);

		return siacRMutuoAttoAmm;
	}

	public SiacRMutuoAttoAmmFin removeSiacRMutuoAttoAmm(SiacRMutuoAttoAmmFin siacRMutuoAttoAmm) {
		getSiacRMutuoAttoAmms().remove(siacRMutuoAttoAmm);
		siacRMutuoAttoAmm.setSiacTAttoAmm(null);

		return siacRMutuoAttoAmm;
	}
	

	public List<SiacTAllegatoAttoFin> getSiacTAttoAllegatos() {
		return siacTAttoAllegatos;
	}

	public void setSiacTAttoAllegatos(List<SiacTAllegatoAttoFin> siacTAttoAllegatos) {
		this.siacTAttoAllegatos = siacTAttoAllegatos;
	}

	public Boolean getParereRegolaritaContabile() {
		return parereRegolaritaContabile;
	}

	public void setParereRegolaritaContabile(Boolean parereRegolaritaContabile) {
		this.parereRegolaritaContabile = parereRegolaritaContabile;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attoammId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attoammId = uid;
	}

	public List<SiacRCronopAttoAmmFin> getSiacRCronopAttoAmms() {
		return siacRCronopAttoAmms;
	}

	public void setSiacRCronopAttoAmms(List<SiacRCronopAttoAmmFin> siacRCronopAttoAmms) {
		this.siacRCronopAttoAmms = siacRCronopAttoAmms;
	}

	public Boolean getAttoammBlocco() {
		return attoammBlocco;
	}

	public void setAttoammBlocco(Boolean attoammBlocco) {
		this.attoammBlocco = attoammBlocco;
	}

	public String getAttoammProvenienza() {
		return attoammProvenienza;
	}

	public void setAttoammProvenienza(String attoammProvenienza) {
		this.attoammProvenienza = attoammProvenienza;
	}

}