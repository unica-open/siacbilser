/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_mutuo database table.
 * 
 */
@Entity
@Table(name="siac_t_mutuo")
@NamedQuery(name="SiacTMutuo.findAll", query="SELECT s FROM SiacTMutuo s")
public class SiacTMutuo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut id. */
	@Id
	@SequenceGenerator(name="SIAC_T_MUTUO_MUTID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MUTUO_MUT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MUTUO_MUTID_GENERATOR")
	@Column(name="mut_id")
	private Integer mutId;

	/** The mut code. */
	@Column(name="mut_code")
	private String mutCode;

	/** The mut data fine. */
	@Column(name="mut_data_fine")
	private Date mutDataFine;

	/** The mut data inizio. */
	@Column(name="mut_data_inizio")
	private Date mutDataInizio;

	/** The mut desc. */
	@Column(name="mut_desc")
	private String mutDesc;

	/** The mut durata. */
	@Column(name="mut_durata")
	private Integer mutDurata;

	/** The mut importo attuale. */
	@Column(name="mut_importo_attuale")
	private BigDecimal mutImportoAttuale;

	/** The mut importo iniziale. */
	@Column(name="mut_importo_iniziale")
	private BigDecimal mutImportoIniziale;

	/** The mut note. */
	@Column(name="mut_note")
	private String mutNote;

	/** The mut num registrazione. */
	@Column(name="mut_num_registrazione")
	private String mutNumRegistrazione;

	//bi-directional many-to-one association to SiacRMutuoStato
	/** The siac r muto statos. */
	@OneToMany(mappedBy="siacTMutuo")
	private List<SiacRMutuoStato> siacRMutoStatos;

	//bi-directional many-to-one association to SiacRMutuoAttoAmm
	/** The siac r mutuo atto amms. */
	@OneToMany(mappedBy="siacTMutuo")
	private List<SiacRMutuoAttoAmm> siacRMutuoAttoAmms;

	//bi-directional many-to-one association to SiacRMutuoSoggetto
	/** The siac r mutuo soggettos. */
	@OneToMany(mappedBy="siacTMutuo")
	private List<SiacRMutuoSoggetto> siacRMutuoSoggettos;

	//bi-directional many-to-one association to SiacDMutuoTipo
	/** The siac d mutuo tipo. */
	@ManyToOne
	@JoinColumn(name="mut_tipo_id")
	private SiacDMutuoTipo siacDMutuoTipo;

	//bi-directional many-to-one association to SiacTMutuoVoce
	/** The siac t mutuo voces. */
	@OneToMany(mappedBy="siacTMutuo")
	private List<SiacTMutuoVoce> siacTMutuoVoces;

	/**
	 * Instantiates a new siac t mutuo.
	 */
	public SiacTMutuo() {
	}

	/**
	 * Gets the mut id.
	 *
	 * @return the mut id
	 */
	public Integer getMutId() {
		return this.mutId;
	}

	/**
	 * Sets the mut id.
	 *
	 * @param mutId the new mut id
	 */
	public void setMutId(Integer mutId) {
		this.mutId = mutId;
	}

	/**
	 * Gets the mut code.
	 *
	 * @return the mut code
	 */
	public String getMutCode() {
		return this.mutCode;
	}

	/**
	 * Sets the mut code.
	 *
	 * @param mutCode the new mut code
	 */
	public void setMutCode(String mutCode) {
		this.mutCode = mutCode;
	}

	/**
	 * Gets the mut data fine.
	 *
	 * @return the mut data fine
	 */
	public Date getMutDataFine() {
		return this.mutDataFine;
	}

	/**
	 * Sets the mut data fine.
	 *
	 * @param mutDataFine the new mut data fine
	 */
	public void setMutDataFine(Date mutDataFine) {
		this.mutDataFine = mutDataFine;
	}

	/**
	 * Gets the mut data inizio.
	 *
	 * @return the mut data inizio
	 */
	public Date getMutDataInizio() {
		return this.mutDataInizio;
	}

	/**
	 * Sets the mut data inizio.
	 *
	 * @param mutDataInizio the new mut data inizio
	 */
	public void setMutDataInizio(Date mutDataInizio) {
		this.mutDataInizio = mutDataInizio;
	}

	/**
	 * Gets the mut desc.
	 *
	 * @return the mut desc
	 */
	public String getMutDesc() {
		return this.mutDesc;
	}

	/**
	 * Sets the mut desc.
	 *
	 * @param mutDesc the new mut desc
	 */
	public void setMutDesc(String mutDesc) {
		this.mutDesc = mutDesc;
	}

	/**
	 * Gets the mut durata.
	 *
	 * @return the mut durata
	 */
	public Integer getMutDurata() {
		return this.mutDurata;
	}

	/**
	 * Sets the mut durata.
	 *
	 * @param mutDurata the new mut durata
	 */
	public void setMutDurata(Integer mutDurata) {
		this.mutDurata = mutDurata;
	}

	/**
	 * Gets the mut importo attuale.
	 *
	 * @return the mut importo attuale
	 */
	public BigDecimal getMutImportoAttuale() {
		return this.mutImportoAttuale;
	}

	/**
	 * Sets the mut importo attuale.
	 *
	 * @param mutImportoAttuale the new mut importo attuale
	 */
	public void setMutImportoAttuale(BigDecimal mutImportoAttuale) {
		this.mutImportoAttuale = mutImportoAttuale;
	}

	/**
	 * Gets the mut importo iniziale.
	 *
	 * @return the mut importo iniziale
	 */
	public BigDecimal getMutImportoIniziale() {
		return this.mutImportoIniziale;
	}

	/**
	 * Sets the mut importo iniziale.
	 *
	 * @param mutImportoIniziale the new mut importo iniziale
	 */
	public void setMutImportoIniziale(BigDecimal mutImportoIniziale) {
		this.mutImportoIniziale = mutImportoIniziale;
	}

	/**
	 * Gets the mut note.
	 *
	 * @return the mut note
	 */
	public String getMutNote() {
		return this.mutNote;
	}

	/**
	 * Sets the mut note.
	 *
	 * @param mutNote the new mut note
	 */
	public void setMutNote(String mutNote) {
		this.mutNote = mutNote;
	}

	/**
	 * Gets the mut num registrazione.
	 *
	 * @return the mut num registrazione
	 */
	public String getMutNumRegistrazione() {
		return this.mutNumRegistrazione;
	}

	/**
	 * Sets the mut num registrazione.
	 *
	 * @param mutNumRegistrazione the new mut num registrazione
	 */
	public void setMutNumRegistrazione(String mutNumRegistrazione) {
		this.mutNumRegistrazione = mutNumRegistrazione;
	}

	/**
	 * Gets the siac r muto statos.
	 *
	 * @return the siac r muto statos
	 */
	public List<SiacRMutuoStato> getSiacRMutoStatos() {
		return this.siacRMutoStatos;
	}

	/**
	 * Sets the siac r muto statos.
	 *
	 * @param siacRMutoStatos the new siac r muto statos
	 */
	public void setSiacRMutoStatos(List<SiacRMutuoStato> siacRMutoStatos) {
		this.siacRMutoStatos = siacRMutoStatos;
	}

	/**
	 * Adds the siac r muto stato.
	 *
	 * @param siacRMutoStato the siac r muto stato
	 * @return the siac r mutuo stato
	 */
	public SiacRMutuoStato addSiacRMutoStato(SiacRMutuoStato siacRMutoStato) {
		getSiacRMutoStatos().add(siacRMutoStato);
		siacRMutoStato.setSiacTMutuo(this);

		return siacRMutoStato;
	}

	/**
	 * Removes the siac r muto stato.
	 *
	 * @param siacRMutoStato the siac r muto stato
	 * @return the siac r mutuo stato
	 */
	public SiacRMutuoStato removeSiacRMutoStato(SiacRMutuoStato siacRMutoStato) {
		getSiacRMutoStatos().remove(siacRMutoStato);
		siacRMutoStato.setSiacTMutuo(null);

		return siacRMutoStato;
	}

	/**
	 * Gets the siac r mutuo atto amms.
	 *
	 * @return the siac r mutuo atto amms
	 */
	public List<SiacRMutuoAttoAmm> getSiacRMutuoAttoAmms() {
		return this.siacRMutuoAttoAmms;
	}

	/**
	 * Sets the siac r mutuo atto amms.
	 *
	 * @param siacRMutuoAttoAmms the new siac r mutuo atto amms
	 */
	public void setSiacRMutuoAttoAmms(List<SiacRMutuoAttoAmm> siacRMutuoAttoAmms) {
		this.siacRMutuoAttoAmms = siacRMutuoAttoAmms;
	}

	/**
	 * Adds the siac r mutuo atto amm.
	 *
	 * @param siacRMutuoAttoAmm the siac r mutuo atto amm
	 * @return the siac r mutuo atto amm
	 */
	public SiacRMutuoAttoAmm addSiacRMutuoAttoAmm(SiacRMutuoAttoAmm siacRMutuoAttoAmm) {
		getSiacRMutuoAttoAmms().add(siacRMutuoAttoAmm);
		siacRMutuoAttoAmm.setSiacTMutuo(this);

		return siacRMutuoAttoAmm;
	}

	/**
	 * Removes the siac r mutuo atto amm.
	 *
	 * @param siacRMutuoAttoAmm the siac r mutuo atto amm
	 * @return the siac r mutuo atto amm
	 */
	public SiacRMutuoAttoAmm removeSiacRMutuoAttoAmm(SiacRMutuoAttoAmm siacRMutuoAttoAmm) {
		getSiacRMutuoAttoAmms().remove(siacRMutuoAttoAmm);
		siacRMutuoAttoAmm.setSiacTMutuo(null);

		return siacRMutuoAttoAmm;
	}

	/**
	 * Gets the siac r mutuo soggettos.
	 *
	 * @return the siac r mutuo soggettos
	 */
	public List<SiacRMutuoSoggetto> getSiacRMutuoSoggettos() {
		return this.siacRMutuoSoggettos;
	}

	/**
	 * Sets the siac r mutuo soggettos.
	 *
	 * @param siacRMutuoSoggettos the new siac r mutuo soggettos
	 */
	public void setSiacRMutuoSoggettos(List<SiacRMutuoSoggetto> siacRMutuoSoggettos) {
		this.siacRMutuoSoggettos = siacRMutuoSoggettos;
	}

	/**
	 * Adds the siac r mutuo soggetto.
	 *
	 * @param siacRMutuoSoggetto the siac r mutuo soggetto
	 * @return the siac r mutuo soggetto
	 */
	public SiacRMutuoSoggetto addSiacRMutuoSoggetto(SiacRMutuoSoggetto siacRMutuoSoggetto) {
		getSiacRMutuoSoggettos().add(siacRMutuoSoggetto);
		siacRMutuoSoggetto.setSiacTMutuo(this);

		return siacRMutuoSoggetto;
	}

	/**
	 * Removes the siac r mutuo soggetto.
	 *
	 * @param siacRMutuoSoggetto the siac r mutuo soggetto
	 * @return the siac r mutuo soggetto
	 */
	public SiacRMutuoSoggetto removeSiacRMutuoSoggetto(SiacRMutuoSoggetto siacRMutuoSoggetto) {
		getSiacRMutuoSoggettos().remove(siacRMutuoSoggetto);
		siacRMutuoSoggetto.setSiacTMutuo(null);

		return siacRMutuoSoggetto;
	}

	/**
	 * Gets the siac d mutuo tipo.
	 *
	 * @return the siac d mutuo tipo
	 */
	public SiacDMutuoTipo getSiacDMutuoTipo() {
		return this.siacDMutuoTipo;
	}

	/**
	 * Sets the siac d mutuo tipo.
	 *
	 * @param siacDMutuoTipo the new siac d mutuo tipo
	 */
	public void setSiacDMutuoTipo(SiacDMutuoTipo siacDMutuoTipo) {
		this.siacDMutuoTipo = siacDMutuoTipo;
	}

	/**
	 * Gets the siac t mutuo voces.
	 *
	 * @return the siac t mutuo voces
	 */
	public List<SiacTMutuoVoce> getSiacTMutuoVoces() {
		return this.siacTMutuoVoces;
	}

	/**
	 * Sets the siac t mutuo voces.
	 *
	 * @param siacTMutuoVoces the new siac t mutuo voces
	 */
	public void setSiacTMutuoVoces(List<SiacTMutuoVoce> siacTMutuoVoces) {
		this.siacTMutuoVoces = siacTMutuoVoces;
	}

	/**
	 * Adds the siac t mutuo voce.
	 *
	 * @param siacTMutuoVoce the siac t mutuo voce
	 * @return the siac t mutuo voce
	 */
	public SiacTMutuoVoce addSiacTMutuoVoce(SiacTMutuoVoce siacTMutuoVoce) {
		getSiacTMutuoVoces().add(siacTMutuoVoce);
		siacTMutuoVoce.setSiacTMutuo(this);

		return siacTMutuoVoce;
	}

	/**
	 * Removes the siac t mutuo voce.
	 *
	 * @param siacTMutuoVoce the siac t mutuo voce
	 * @return the siac t mutuo voce
	 */
	public SiacTMutuoVoce removeSiacTMutuoVoce(SiacTMutuoVoce siacTMutuoVoce) {
		getSiacTMutuoVoces().remove(siacTMutuoVoce);
		siacTMutuoVoce.setSiacTMutuo(null);

		return siacTMutuoVoce;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mutId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutId = uid;
	}

}