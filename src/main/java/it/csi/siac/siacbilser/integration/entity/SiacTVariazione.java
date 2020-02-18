/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

/**
 * The persistent class for the siac_t_variazione database table.
 * 
 */
@Entity
@Table(name="siac_t_variazione")
public class SiacTVariazione extends SiacTEnteBase {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The variazione id. */
	@Id
	@SequenceGenerator(name="SIAC_T_VARIAZIONE_VARIAZIONEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_VARIAZIONE_VARIAZIONE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_VARIAZIONE_VARIAZIONEID_GENERATOR")
	@Column(name="variazione_id")
	private Integer variazioneId;

	/** The variazione data. */
	@Column(name="variazione_data")
	private Date variazioneData;

	/** The variazione desc. */
	@Column(name="variazione_desc")
	private String variazioneDesc;
	
	/** The variazione num. */
	@Column(name="variazione_num")
	private Integer variazioneNum;
	

	//bi-directional many-to-one association to SiacRVariazioneAttr
	/** The siac r variazione attrs. */
	@OneToMany(mappedBy="siacTVariazione", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRVariazioneAttr> siacRVariazioneAttrs;

	//bi-directional many-to-one association to SiacRVariazioneStato
	/** The siac r variazione statos. */
	@OneToMany(mappedBy="siacTVariazione", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRVariazioneStato> siacRVariazioneStatos;

	//bi-directional many-to-one association to SiacDVariazioneTipo
	/** The siac d variazione tipo. */
	@ManyToOne
	@JoinColumn(name="variazione_tipo_id")
	private SiacDVariazioneTipo siacDVariazioneTipo;
	
	//bi-directional many-to-one association to SiacDVariazioneApplicazione
	/** The siac d variazione applicazione. */
	@ManyToOne
	@JoinColumn(name="applicazione_id")
	private SiacDVariazioneApplicazione siacDVariazioneApplicazione;

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bil. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;
	
	//SIAC-6884
	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	
	/** The data apertura proposta. */
	@Column(name="data_apertura_proposta")
	private Date dataAperturaProposta;
	
	/** The variazione data chiusura proposta. */
	@Column(name="data_chiusura_proposta")
	private Date dataChiusuraProposta;
	
	/** The variazione flag consiglio. */
	@Column(name="flag_consiglio")
	private Boolean flagConsiglio;
	
	/** The variazione flag giunta. */
	@Column(name="flag_giunta")
	private Boolean flagGiunta;
	
	/** The variazione data_definitività. */
//	@Column(name="data_definitiva")
	//private Date dataDefinitiva;
	//END SIAC-6884
	
	/**
	 * Instantiates a new siac t variazione.
	 */
	public SiacTVariazione() {
	}
	
	/**
	 * Gets the variazione id.
	 *
	 * @return the variazione id
	 */
	public Integer getVariazioneId() {
		return this.variazioneId;
	}

	/**
	 * Sets the variazione id.
	 *
	 * @param variazioneId the new variazione id
	 */
	public void setVariazioneId(Integer variazioneId) {
		this.variazioneId = variazioneId;
	}

	/**
	 * Gets the variazione data.
	 *
	 * @return the variazione data
	 */
	public Date getVariazioneData() {
		return this.variazioneData;
	}

	/**
	 * Sets the variazione data.
	 *
	 * @param variazioneData the new variazione data
	 */
	public void setVariazioneData(Date variazioneData) {
		this.variazioneData = variazioneData;
	}
	
	//SIAC-6884
	public Boolean getFlagConsiglio() {
		return flagConsiglio;
	}

	public void setFlagConsiglio(Boolean flagConsiglio) {
		this.flagConsiglio = flagConsiglio;
	}

	public Boolean getFlagGiunta() {
		return flagGiunta;
	}

	public void setFlagGiunta(Boolean flagGiunta) {
		this.flagGiunta = flagGiunta;
	}
	
	/**
	 * Gets the apertura proposta
	 *
	 * @return the apertura proposta
	 */
	public Date getDataAperturaProposta() {
		return this.dataAperturaProposta;
	}

	/**
	 * Sets the data apertura proposta.
	 *
	 * @param variazioneData the new data apertura proposta
	 */
	public void setDataAperturaProposta(Date dataAperturaProposta) {
		this.dataAperturaProposta = dataAperturaProposta;
	}
	
	/**
	 * Sets the data definitiva.
	 *
	 * @param variazioneData the new data definitiva
	 */
	//public void setDataDefinitiva(Date dataDefinitiva) {
		//this.dataDefinitiva = dataDefinitiva;
	//}
	
	//SIAC-6884
	/**
	 * Gets the apertura proposta
	 *
	 * @return the apertura proposta
	 */
	public Date getDataChiusuraProposta() {
		return this.dataChiusuraProposta;
	}
	
	
	/**
	 * Gets the dataDefiniiva
	 *
	 * @return the dataDefinitva
	 */
	//public Date getDataDefinitiva() {
	//	return this.dataDefinitiva;
	//}

	/**
	 * Sets the data chiusura proposta.
	 *
	 * @param variazioneData the new data chiusura proposta
	 */
	public void setDataChiusuraProposta(Date dataChiusuraProposta) {
		this.dataChiusuraProposta = dataChiusuraProposta;
	}

	/**
	 * Gets the variazione desc.
	 *
	 * @return the variazione desc
	 */
	public String getVariazioneDesc() {
		return this.variazioneDesc;
	}

	/**
	 * Sets the variazione desc.
	 *
	 * @param variazioneDesc the new variazione desc
	 */
	public void setVariazioneDesc(String variazioneDesc) {
		this.variazioneDesc = variazioneDesc;
	}

	/**
	 * Gets the variazione num.
	 *
	 * @return the variazione num
	 */
	public Integer getVariazioneNum() {
		return this.variazioneNum;
	}

	/**
	 * Sets the variazione num.
	 *
	 * @param variazioneNum the new variazione num
	 */
	public void setVariazioneNum(Integer variazioneNum) {
		this.variazioneNum = variazioneNum;
	}

	/**
	 * Gets the siac r variazione attrs.
	 *
	 * @return the siac r variazione attrs
	 */
	public List<SiacRVariazioneAttr> getSiacRVariazioneAttrs() {
		return this.siacRVariazioneAttrs;
	}

	/**
	 * Sets the siac r variazione attrs.
	 *
	 * @param siacRVariazioneAttrs the new siac r variazione attrs
	 */
	public void setSiacRVariazioneAttrs(List<SiacRVariazioneAttr> siacRVariazioneAttrs) {
		this.siacRVariazioneAttrs = siacRVariazioneAttrs;
	}

	/**
	 * Adds the siac r variazione attr.
	 *
	 * @param siacRVariazioneAttr the siac r variazione attr
	 * @return the siac r variazione attr
	 */
	public SiacRVariazioneAttr addSiacRVariazioneAttr(SiacRVariazioneAttr siacRVariazioneAttr) {
		getSiacRVariazioneAttrs().add(siacRVariazioneAttr);
		siacRVariazioneAttr.setSiacTVariazione(this);

		return siacRVariazioneAttr;
	}

	/**
	 * Removes the siac r variazione attr.
	 *
	 * @param siacRVariazioneAttr the siac r variazione attr
	 * @return the siac r variazione attr
	 */
	public SiacRVariazioneAttr removeSiacRVariazioneAttr(SiacRVariazioneAttr siacRVariazioneAttr) {
		getSiacRVariazioneAttrs().remove(siacRVariazioneAttr);
		siacRVariazioneAttr.setSiacTVariazione(null);

		return siacRVariazioneAttr;
	}

	/**
	 * Gets the siac r variazione statos.
	 *
	 * @return the siac r variazione statos
	 */
	public List<SiacRVariazioneStato> getSiacRVariazioneStatos() {
		return this.siacRVariazioneStatos;
	}

	/**
	 * Sets the siac r variazione statos.
	 *
	 * @param siacRVariazioneStatos the new siac r variazione statos
	 */
	public void setSiacRVariazioneStatos(List<SiacRVariazioneStato> siacRVariazioneStatos) {
		this.siacRVariazioneStatos = siacRVariazioneStatos;
	}

	/**
	 * Adds the siac r variazione stato.
	 *
	 * @param siacRVariazioneStato the siac r variazione stato
	 * @return the siac r variazione stato
	 */
	public SiacRVariazioneStato addSiacRVariazioneStato(SiacRVariazioneStato siacRVariazioneStato) {
		getSiacRVariazioneStatos().add(siacRVariazioneStato);
		siacRVariazioneStato.setSiacTVariazione(this);

		return siacRVariazioneStato;
	}

	/**
	 * Removes the siac r variazione stato.
	 *
	 * @param siacRVariazioneStato the siac r variazione stato
	 * @return the siac r variazione stato
	 */
	public SiacRVariazioneStato removeSiacRVariazioneStato(SiacRVariazioneStato siacRVariazioneStato) {
		getSiacRVariazioneStatos().remove(siacRVariazioneStato);
		siacRVariazioneStato.setSiacTVariazione(null);

		return siacRVariazioneStato;
	}

	/**
	 * Gets the siac d variazione tipo.
	 *
	 * @return the siac d variazione tipo
	 */
	public SiacDVariazioneTipo getSiacDVariazioneTipo() {
		return this.siacDVariazioneTipo;
	}

	/**
	 * Sets the siac d variazione tipo.
	 *
	 * @param siacDVariazioneTipo the new siac d variazione tipo
	 */
	public void setSiacDVariazioneTipo(SiacDVariazioneTipo siacDVariazioneTipo) {
		this.siacDVariazioneTipo = siacDVariazioneTipo;
	}
	
	/**
	 * @return the siacDVariazioneApplicazione
	 */
	public SiacDVariazioneApplicazione getSiacDVariazioneApplicazione() {
		return siacDVariazioneApplicazione;
	}

	/**
	 * @param siacDVariazioneApplicazione the siacDVariazioneApplicazione to set
	 */
	public void setSiacDVariazioneApplicazione(SiacDVariazioneApplicazione siacDVariazioneApplicazione) {
		this.siacDVariazioneApplicazione = siacDVariazioneApplicazione;
	}
	
	//SIAC-6884
	/**
	 * @return the siacTClass
	 */
	public SiacTClass getSiacTClass() {
		return siacTClass;
	}

	/**
	 * @param siacTClass the siacTClass to set
	 */
	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	/**
	 * Gets the siac t bil.
	 *
	 * @return the siac t bil
	 */
	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	/**
	 * Sets the siac t bil.
	 *
	 * @param siacTBil the new siac t bil
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}
	
	@Override
	public Integer getUid() {
		return variazioneId;
	}

	@Override
	public void setUid(Integer uid) {
		this.variazioneId = uid;
		
	}

}