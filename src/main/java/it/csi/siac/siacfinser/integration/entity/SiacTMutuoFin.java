/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_mutuo database table.
 * 
 */
@Entity
@Table(name="siac_t_mutuo")
public class SiacTMutuoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MUTUO_MUTUO_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_mutuo_mut_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MUTUO_MUTUO_ID_GENERATOR")
	@Column(name="mut_id")
	private Integer mutId;

	@Column(name="mut_code")
	private String mutCode;

	@Column(name="mut_data_fine")
	private Date mutDataFine;

	@Column(name="mut_data_inizio")
	private Date mutDataInizio;

	@Column(name="mut_desc")
	private String mutDesc;

	@Column(name="mut_durata")
	private Integer mutDurata;

	@Column(name="mut_importo_attuale")
	private BigDecimal mutImportoAttuale;

	@Column(name="mut_importo_iniziale")
	private BigDecimal mutImportoIniziale;

	@Column(name="mut_note")
	private String mutNote;

	@Column(name="mut_num_registrazione")
	private String mutNumRegistrazione;

	//bi-directional many-to-one association to SiacRMutuoStatoFin
	@OneToMany(mappedBy="siacTMutuo")
	// @OneToMany(mappedBy="siacTMutuo", cascade = {CascadeType.ALL})
	private List<SiacRMutuoStatoFin> siacRMutuoStatos;

	//bi-directional many-to-one association to SiacRMutuoAttoAmmFin
	@OneToMany(mappedBy="siacTMutuo")
	// @OneToMany(mappedBy="siacTMutuo", cascade = {CascadeType.ALL})
	private List<SiacRMutuoAttoAmmFin> siacRMutuoAttoAmms;

	//bi-directional many-to-one association to SiacRMutuoSoggettoFin
	@OneToMany(mappedBy="siacTMutuo")
	// @OneToMany(mappedBy="siacTMutuo", cascade = {CascadeType.ALL})
	private List<SiacRMutuoSoggettoFin> siacRMutuoSoggettos;

	//bi-directional many-to-one association to SiacDMutuoTipoFin
	@ManyToOne
	@JoinColumn(name="mut_tipo_id")
	private SiacDMutuoTipoFin siacDMutuoTipo;

	//bi-directional many-to-one association to SiacTMutuoVoceFin
	@OneToMany(mappedBy="siacTMutuo")
	// @OneToMany(mappedBy="siacTMutuo", cascade = {CascadeType.ALL})
	private List<SiacTMutuoVoceFin> siacTMutuoVoces;

	public SiacTMutuoFin() {
	}

	public Integer getMutId() {
		return this.mutId;
	}

	public void setMutId(Integer mutId) {
		this.mutId = mutId;
	}

	public String getMutCode() {
		return this.mutCode;
	}

	public void setMutCode(String mutCode) {
		this.mutCode = mutCode;
	}

	public Date getMutDataFine() {
		return this.mutDataFine;
	}

	public void setMutDataFine(Date mutDataFine) {
		this.mutDataFine = mutDataFine;
	}

	public Date getMutDataInizio() {
		return this.mutDataInizio;
	}

	public void setMutDataInizio(Date mutDataInizio) {
		this.mutDataInizio = mutDataInizio;
	}

	public String getMutDesc() {
		return this.mutDesc;
	}

	public void setMutDesc(String mutDesc) {
		this.mutDesc = mutDesc;
	}

	public Integer getMutDurata() {
		return this.mutDurata;
	}

	public void setMutDurata(Integer mutDurata) {
		this.mutDurata = mutDurata;
	}

	public BigDecimal getMutImportoAttuale() {
		return this.mutImportoAttuale;
	}

	public void setMutImportoAttuale(BigDecimal mutImportoAttuale) {
		this.mutImportoAttuale = mutImportoAttuale;
	}

	public BigDecimal getMutImportoIniziale() {
		return this.mutImportoIniziale;
	}

	public void setMutImportoIniziale(BigDecimal mutImportoIniziale) {
		this.mutImportoIniziale = mutImportoIniziale;
	}

	public String getMutNote() {
		return this.mutNote;
	}

	public void setMutNote(String mutNote) {
		this.mutNote = mutNote;
	}

	public String getMutNumRegistrazione() {
		return this.mutNumRegistrazione;
	}

	public void setMutNumRegistrazione(String mutNumRegistrazione) {
		this.mutNumRegistrazione = mutNumRegistrazione;
	}

	public List<SiacRMutuoStatoFin> getSiacRMutuoStatos() {
		return this.siacRMutuoStatos;
	}

	public void setSiacRMutuoStatos(List<SiacRMutuoStatoFin> siacRMutuoStatos) {
		this.siacRMutuoStatos = siacRMutuoStatos;
	}

	public SiacRMutuoStatoFin addSiacRMutuoStato(SiacRMutuoStatoFin siacRMutuoStato) {
		getSiacRMutuoStatos().add(siacRMutuoStato);
		siacRMutuoStato.setSiacTMutuo(this);

		return siacRMutuoStato;
	}

	public SiacRMutuoStatoFin removeSiacRMutuoStato(SiacRMutuoStatoFin siacRMutuoStato) {
		getSiacRMutuoStatos().remove(siacRMutuoStato);
		siacRMutuoStato.setSiacTMutuo(null);

		return siacRMutuoStato;
	}

	public List<SiacRMutuoAttoAmmFin> getSiacRMutuoAttoAmms() {
		return this.siacRMutuoAttoAmms;
	}

	public void setSiacRMutuoAttoAmms(List<SiacRMutuoAttoAmmFin> siacRMutuoAttoAmms) {
		this.siacRMutuoAttoAmms = siacRMutuoAttoAmms;
	}

	public SiacRMutuoAttoAmmFin addSiacRMutuoAttoAmm(SiacRMutuoAttoAmmFin siacRMutuoAttoAmm) {
		getSiacRMutuoAttoAmms().add(siacRMutuoAttoAmm);
		siacRMutuoAttoAmm.setSiacTMutuo(this);

		return siacRMutuoAttoAmm;
	}

	public SiacRMutuoAttoAmmFin removeSiacRMutuoAttoAmm(SiacRMutuoAttoAmmFin siacRMutuoAttoAmm) {
		getSiacRMutuoAttoAmms().remove(siacRMutuoAttoAmm);
		siacRMutuoAttoAmm.setSiacTMutuo(null);

		return siacRMutuoAttoAmm;
	}

	public List<SiacRMutuoSoggettoFin> getSiacRMutuoSoggettos() {
		return this.siacRMutuoSoggettos;
	}

	public void setSiacRMutuoSoggettos(List<SiacRMutuoSoggettoFin> siacRMutuoSoggettos) {
		this.siacRMutuoSoggettos = siacRMutuoSoggettos;
	}

	public SiacRMutuoSoggettoFin addSiacRMutuoSoggetto(SiacRMutuoSoggettoFin siacRMutuoSoggetto) {
		getSiacRMutuoSoggettos().add(siacRMutuoSoggetto);
		siacRMutuoSoggetto.setSiacTMutuo(this);

		return siacRMutuoSoggetto;
	}

	public SiacRMutuoSoggettoFin removeSiacRMutuoSoggetto(SiacRMutuoSoggettoFin siacRMutuoSoggetto) {
		getSiacRMutuoSoggettos().remove(siacRMutuoSoggetto);
		siacRMutuoSoggetto.setSiacTMutuo(null);

		return siacRMutuoSoggetto;
	}

	public SiacDMutuoTipoFin getSiacDMutuoTipo() {
		return this.siacDMutuoTipo;
	}

	public void setSiacDMutuoTipo(SiacDMutuoTipoFin siacDMutuoTipo) {
		this.siacDMutuoTipo = siacDMutuoTipo;
	}

	public List<SiacTMutuoVoceFin> getSiacTMutuoVoces() {
		return this.siacTMutuoVoces;
	}

	public void setSiacTMutuoVoces(List<SiacTMutuoVoceFin> siacTMutuoVoces) {
		this.siacTMutuoVoces = siacTMutuoVoces;
	}

	public SiacTMutuoVoceFin addSiacTMutuoVoce(SiacTMutuoVoceFin siacTMutuoVoce) {
		getSiacTMutuoVoces().add(siacTMutuoVoce);
		siacTMutuoVoce.setSiacTMutuo(this);

		return siacTMutuoVoce;
	}

	public SiacTMutuoVoceFin removeSiacTMutuoVoce(SiacTMutuoVoceFin siacTMutuoVoce) {
		getSiacTMutuoVoces().remove(siacTMutuoVoce);
		siacTMutuoVoce.setSiacTMutuo(null);

		return siacTMutuoVoce;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return mutId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutId = uid;
	}
}