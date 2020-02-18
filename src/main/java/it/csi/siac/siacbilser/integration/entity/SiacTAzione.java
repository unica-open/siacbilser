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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_azione database table.
 * 
 */
@Entity
@Table(name="siac_t_azione")
public class SiacTAzione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The azione id. */
	@Id
	@SequenceGenerator(name="SIAC_T_AZIONE_AZIONEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_AZIONE_AZIONE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_AZIONE_AZIONEID_GENERATOR")
	@Column(name="azione_id")
	private Integer azioneId;

	/** The azione code. */
	@Column(name="azione_code")
	private String azioneCode;

	/** The azione desc. */
	@Column(name="azione_desc")
	private String azioneDesc;

	/** The nomeprocesso. */
	private String nomeprocesso;

	/** The nometask. */
	private String nometask;

	/** The urlapplicazione. */
	private String urlapplicazione;

	/** The verificauo. */
	private Boolean verificauo;

	//bi-directional many-to-one association to SiacDFileTipo
	@OneToMany(mappedBy="siacTAzione")
	private List<SiacDFileTipo> siacDFileTipos;

	//bi-directional many-to-one association to SiacRRuoloOpAzione
	/** The siac r ruolo op aziones. */
	@OneToMany(mappedBy="siacTAzione")
	private List<SiacRRuoloOpAzione> siacRRuoloOpAziones;

	//bi-directional many-to-one association to SiacDAzioneTipo
	/** The siac d azione tipo. */
	@ManyToOne
	@JoinColumn(name="azione_tipo_id")
	private SiacDAzioneTipo siacDAzioneTipo;

	//bi-directional many-to-one association to SiacDGruppoAzioni
	/** The siac d gruppo azioni. */
	@ManyToOne
	@JoinColumn(name="gruppo_azioni_id")
	private SiacDGruppoAzioni siacDGruppoAzioni;

	//bi-directional many-to-one association to SiacTAzioneRichiesta
	/** The siac t azione richiestas. */
	@OneToMany(mappedBy="siacTAzione")
	private List<SiacTAzioneRichiesta> siacTAzioneRichiestas;

	//bi-directional many-to-one association to SiacTOperazioneAsincrona
	@OneToMany(mappedBy="siacTAzione")
	private List<SiacTOperazioneAsincrona> siacTOperazioneAsincronas;

	/**
	 * Instantiates a new siac t azione.
	 */
	public SiacTAzione() {
	}

	/**
	 * Gets the azione id.
	 *
	 * @return the azione id
	 */
	public Integer getAzioneId() {
		return this.azioneId;
	}

	/**
	 * Sets the azione id.
	 *
	 * @param azioneId the new azione id
	 */
	public void setAzioneId(Integer azioneId) {
		this.azioneId = azioneId;
	}

	/**
	 * Gets the azione code.
	 *
	 * @return the azione code
	 */
	public String getAzioneCode() {
		return this.azioneCode;
	}

	/**
	 * Sets the azione code.
	 *
	 * @param azioneCode the new azione code
	 */
	public void setAzioneCode(String azioneCode) {
		this.azioneCode = azioneCode;
	}

	/**
	 * Gets the azione desc.
	 *
	 * @return the azione desc
	 */
	public String getAzioneDesc() {
		return this.azioneDesc;
	}

	/**
	 * Sets the azione desc.
	 *
	 * @param azioneDesc the new azione desc
	 */
	public void setAzioneDesc(String azioneDesc) {
		this.azioneDesc = azioneDesc;
	}

	/**
	 * Gets the nomeprocesso.
	 *
	 * @return the nomeprocesso
	 */
	public String getNomeprocesso() {
		return this.nomeprocesso;
	}

	/**
	 * Sets the nomeprocesso.
	 *
	 * @param nomeprocesso the new nomeprocesso
	 */
	public void setNomeprocesso(String nomeprocesso) {
		this.nomeprocesso = nomeprocesso;
	}

	/**
	 * Gets the nometask.
	 *
	 * @return the nometask
	 */
	public String getNometask() {
		return this.nometask;
	}

	/**
	 * Sets the nometask.
	 *
	 * @param nometask the new nometask
	 */
	public void setNometask(String nometask) {
		this.nometask = nometask;
	}

	/**
	 * Gets the urlapplicazione.
	 *
	 * @return the urlapplicazione
	 */
	public String getUrlapplicazione() {
		return this.urlapplicazione;
	}

	/**
	 * Sets the urlapplicazione.
	 *
	 * @param urlapplicazione the new urlapplicazione
	 */
	public void setUrlapplicazione(String urlapplicazione) {
		this.urlapplicazione = urlapplicazione;
	}

	/**
	 * Gets the verificauo.
	 *
	 * @return the verificauo
	 */
	public Boolean getVerificauo() {
		return this.verificauo;
	}

	/**
	 * Sets the verificauo.
	 *
	 * @param verificauo the new verificauo
	 */
	public void setVerificauo(Boolean verificauo) {
		this.verificauo = verificauo;
	}

	public List<SiacDFileTipo> getSiacDFileTipos() {
		return this.siacDFileTipos;
	}

	public void setSiacDFileTipos(List<SiacDFileTipo> siacDFileTipos) {
		this.siacDFileTipos = siacDFileTipos;
	}

	public SiacDFileTipo addSiacDFileTipo(SiacDFileTipo siacDFileTipo) {
		getSiacDFileTipos().add(siacDFileTipo);
		siacDFileTipo.setSiacTAzione(this);

		return siacDFileTipo;
	}

	public SiacDFileTipo removeSiacDFileTipo(SiacDFileTipo siacDFileTipo) {
		getSiacDFileTipos().remove(siacDFileTipo);
		siacDFileTipo.setSiacTAzione(null);

		return siacDFileTipo;
	}

	/**
	 * Gets the siac r ruolo op aziones.
	 *
	 * @return the siac r ruolo op aziones
	 */
	public List<SiacRRuoloOpAzione> getSiacRRuoloOpAziones() {
		return this.siacRRuoloOpAziones;
	}

	/**
	 * Sets the siac r ruolo op aziones.
	 *
	 * @param siacRRuoloOpAziones the new siac r ruolo op aziones
	 */
	public void setSiacRRuoloOpAziones(List<SiacRRuoloOpAzione> siacRRuoloOpAziones) {
		this.siacRRuoloOpAziones = siacRRuoloOpAziones;
	}

	/**
	 * Adds the siac r ruolo op azione.
	 *
	 * @param siacRRuoloOpAzione the siac r ruolo op azione
	 * @return the siac r ruolo op azione
	 */
	public SiacRRuoloOpAzione addSiacRRuoloOpAzione(SiacRRuoloOpAzione siacRRuoloOpAzione) {
		getSiacRRuoloOpAziones().add(siacRRuoloOpAzione);
		siacRRuoloOpAzione.setSiacTAzione(this);

		return siacRRuoloOpAzione;
	}

	/**
	 * Removes the siac r ruolo op azione.
	 *
	 * @param siacRRuoloOpAzione the siac r ruolo op azione
	 * @return the siac r ruolo op azione
	 */
	public SiacRRuoloOpAzione removeSiacRRuoloOpAzione(SiacRRuoloOpAzione siacRRuoloOpAzione) {
		getSiacRRuoloOpAziones().remove(siacRRuoloOpAzione);
		siacRRuoloOpAzione.setSiacTAzione(null);

		return siacRRuoloOpAzione;
	}

	/**
	 * Gets the siac d azione tipo.
	 *
	 * @return the siac d azione tipo
	 */
	public SiacDAzioneTipo getSiacDAzioneTipo() {
		return this.siacDAzioneTipo;
	}

	/**
	 * Sets the siac d azione tipo.
	 *
	 * @param siacDAzioneTipo the new siac d azione tipo
	 */
	public void setSiacDAzioneTipo(SiacDAzioneTipo siacDAzioneTipo) {
		this.siacDAzioneTipo = siacDAzioneTipo;
	}

	/**
	 * Gets the siac d gruppo azioni.
	 *
	 * @return the siac d gruppo azioni
	 */
	public SiacDGruppoAzioni getSiacDGruppoAzioni() {
		return this.siacDGruppoAzioni;
	}

	/**
	 * Sets the siac d gruppo azioni.
	 *
	 * @param siacDGruppoAzioni the new siac d gruppo azioni
	 */
	public void setSiacDGruppoAzioni(SiacDGruppoAzioni siacDGruppoAzioni) {
		this.siacDGruppoAzioni = siacDGruppoAzioni;
	}

	/**
	 * Gets the siac t azione richiestas.
	 *
	 * @return the siac t azione richiestas
	 */
	public List<SiacTAzioneRichiesta> getSiacTAzioneRichiestas() {
		return this.siacTAzioneRichiestas;
	}

	/**
	 * Sets the siac t azione richiestas.
	 *
	 * @param siacTAzioneRichiestas the new siac t azione richiestas
	 */
	public void setSiacTAzioneRichiestas(List<SiacTAzioneRichiesta> siacTAzioneRichiestas) {
		this.siacTAzioneRichiestas = siacTAzioneRichiestas;
	}

	/**
	 * Adds the siac t azione richiesta.
	 *
	 * @param siacTAzioneRichiesta the siac t azione richiesta
	 * @return the siac t azione richiesta
	 */
	public SiacTAzioneRichiesta addSiacTAzioneRichiesta(SiacTAzioneRichiesta siacTAzioneRichiesta) {
		getSiacTAzioneRichiestas().add(siacTAzioneRichiesta);
		siacTAzioneRichiesta.setSiacTAzione(this);

		return siacTAzioneRichiesta;
	}

	/**
	 * Removes the siac t azione richiesta.
	 *
	 * @param siacTAzioneRichiesta the siac t azione richiesta
	 * @return the siac t azione richiesta
	 */
	public SiacTAzioneRichiesta removeSiacTAzioneRichiesta(SiacTAzioneRichiesta siacTAzioneRichiesta) {
		getSiacTAzioneRichiestas().remove(siacTAzioneRichiesta);
		siacTAzioneRichiesta.setSiacTAzione(null);

		return siacTAzioneRichiesta;
	}

	public List<SiacTOperazioneAsincrona> getSiacTOperazioneAsincronas() {
		return this.siacTOperazioneAsincronas;
	}

	public void setSiacTOperazioneAsincronas(List<SiacTOperazioneAsincrona> siacTOperazioneAsincronas) {
		this.siacTOperazioneAsincronas = siacTOperazioneAsincronas;
	}

	public SiacTOperazioneAsincrona addSiacTOperazioneAsincrona(SiacTOperazioneAsincrona siacTOperazioneAsincrona) {
		getSiacTOperazioneAsincronas().add(siacTOperazioneAsincrona);
		siacTOperazioneAsincrona.setSiacTAzione(this);

		return siacTOperazioneAsincrona;
	}

	public SiacTOperazioneAsincrona removeSiacTOperazioneAsincrona(SiacTOperazioneAsincrona siacTOperazioneAsincrona) {
		getSiacTOperazioneAsincronas().remove(siacTOperazioneAsincrona);
		siacTOperazioneAsincrona.setSiacTAzione(null);

		return siacTOperazioneAsincrona;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return azioneId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.azioneId = uid;
	}

}