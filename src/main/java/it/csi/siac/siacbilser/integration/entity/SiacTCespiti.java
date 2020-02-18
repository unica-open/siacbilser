/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_cespiti_categoria database table.
 * 
 */
@Entity
@Table(name="siac_t_cespiti")
@NamedQuery(name="SiacTCespiti.findAll", query="SELECT s FROM SiacTCespiti s")
public class SiacTCespiti extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="siac_t_cespiti_ces_idGENERATOR", allocationSize=1, sequenceName="siac_t_cespiti_ces_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_t_cespiti_ces_idGENERATOR")
	@Column(name="ces_id")
	private Integer cesId;

	@Column(name="ces_code")
	private String cesCode;

	@Column(name="ces_desc")
	private String cesDesc;

	@Column(name="soggetto_beni_culturali")
	private Boolean soggettoBeniCulturali;
	
	@Column(name="num_inventario")
	private String numInventario;
	
	@Column(name="data_ingresso_inventario")
	private Date dataIngressoInventario;
	
	@Column(name="data_cessazione")
	private Date dataCessazione;
	
	@Column(name="valore_iniziale")
	private BigDecimal valoreIniziale;
	
	@Column(name="valore_attuale")
	private BigDecimal valoreAttuale;
	
	@Column(name="descrizione_stato")
	private String descrizioneStato;
	
	@Column(name="ubicazione")
	private String ubicazione;
	
	@Column(name="note")
	private String note;
	
	@Column(name="flg_donazione_rinvenimento")
	private Boolean flgDonazioneRinvenimento;
	
	@Column(name="flg_stato_bene")
	private Boolean flgStatoBene;
	
	// SIAC-6374
	@Column(name="num_inventario_prefisso")
	private String numInventarioPrefisso;
	
	@Column(name="num_inventario_numero")
	private Integer numInventarioNumero;
	
	// bi-directional many-to-one association
	@ManyToOne
	@JoinColumn(name="ces_bene_tipo_id")
	private SiacDCespitiBeneTipo siacDCespitiBeneTipo;
	
	// bi-directional many-to-one association
	@ManyToOne
	@JoinColumn(name="ces_dismissioni_id")
	private SiacTCespitiDismissioni siacTCespitiDismissioni;

	// bi-directional many-to-one association
	@ManyToOne
	@JoinColumn(name="ces_class_giu_id")
	private SiacDCespitiClassificazioneGiuridica siacDCespitiClassificazioneGiuridica;
	
	//bi-directional many-to-one association to SiacRCespitiPrimaNota
	@OneToMany(mappedBy="siacTCespiti", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCespitiPrimaNota> siacRCespitiPrimaNotas;
	
	//bi-directional many-to-one association to SiacRCespitiPrimaNota
	// No cascade: the relation is handled by the other side of the relation
	@OneToMany(mappedBy="siacTCespiti")
	private List<SiacTCespitiVariazione> siacTCespitiVariaziones;
	
	@OneToMany(mappedBy="siacTCespiti")
	private List<SiacTCespitiAmmortamento> siacTCespitiAmmortamentos;
	
	//bi-directional many-to-one association to SiacRCespitiPrimaNota
	@OneToMany(mappedBy="siacTCespiti")
	private List<SiacRCespitiCespitiElabAmmortamentiDett> siacRCespitiCespitiElabAmmortamentiDetts;
	
	@OneToMany(mappedBy="siacTCespiti")
	private List<SiacRCespitiMovEpDet> siacRCespitiMovEpDets;
	
	public SiacTCespiti(Integer cesId) {
		this();
		setCesId(cesId);
	}

	public SiacTCespiti() {
		super();
	}

	/**
	 * @return the cesId
	 */
	public Integer getCesId() {
		return cesId;
	}

	/**
	 * @param cesId the cesId to set
	 */
	public void setCesId(Integer cesId) {
		this.cesId = cesId;
	}

	/**
	 * @return the cesCode
	 */
	public String getCesCode() {
		return cesCode;
	}

	/**
	 * @param cesCode the cesCode to set
	 */
	public void setCesCode(String cesCode) {
		this.cesCode = cesCode;
	}

	/**
	 * @return the cesDesc
	 */
	public String getCesDesc() {
		return cesDesc;
	}

	/**
	 * @param cesDesc the cesDesc to set
	 */
	public void setCesDesc(String cesDesc) {
		this.cesDesc = cesDesc;
	}

	/**
	 * @return the soggettoBeniCulturali
	 */
	public Boolean getSoggettoBeniCulturali() {
		return soggettoBeniCulturali;
	}

	/**
	 * @param soggettoBeniCulturali the soggettoBeniCulturali to set
	 */
	public void setSoggettoBeniCulturali(Boolean soggettoBeniCulturali) {
		this.soggettoBeniCulturali = soggettoBeniCulturali;
	}

	/**
	 * @return the numInventario
	 */
	public String getNumInventario() {
		return numInventario;
	}

	/**
	 * @param numInventario the numInventario to set
	 */
	public void setNumInventario(String numInventario) {
		this.numInventario = numInventario;
	}

	/**
	 * @return the dataIngressoInventario
	 */
	public Date getDataIngressoInventario() {
		return dataIngressoInventario;
	}

	/**
	 * @param dataIngressoInventario the dataIngressoInventario to set
	 */
	public void setDataIngressoInventario(Date dataIngressoInventario) {
		this.dataIngressoInventario = dataIngressoInventario;
	}
	
	/**
	 * @return the dataCessazione
	 */
	public Date getDataCessazione() {
		return dataCessazione;
	}

	/**
	 * @param dataCessazione the dataCessazione to set
	 */
	public void setDataCessazione(Date dataCessazione) {
		this.dataCessazione = dataCessazione;
	}

	/**
	 * @return the valoreIniziale
	 */
	public BigDecimal getValoreIniziale() {
		return valoreIniziale;
	}

	/**
	 * @param valoreIniziale the valoreIniziale to set
	 */
	public void setValoreIniziale(BigDecimal valoreIniziale) {
		this.valoreIniziale = valoreIniziale;
	}

	/**
	 * @return the valoreAttuale
	 */
	public BigDecimal getValoreAttuale() {
		return valoreAttuale;
	}

	/**
	 * @param valoreAttuale the valoreAttuale to set
	 */
	public void setValoreAttuale(BigDecimal valoreAttuale) {
		this.valoreAttuale = valoreAttuale;
	}

	/**
	 * @return the descrizioneStato
	 */
	public String getDescrizioneStato() {
		return descrizioneStato;
	}

	/**
	 * @param descrizioneStato the descrizioneStato to set
	 */
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}

	/**
	 * @return the ubicazione
	 */
	public String getUbicazione() {
		return ubicazione;
	}

	/**
	 * @param ubicazione the ubicazione to set
	 */
	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the flgDonazioneRinvenimento
	 */
	public Boolean getFlgDonazioneRinvenimento() {
		return flgDonazioneRinvenimento;
	}

	/**
	 * @param flgDonazioneRinvenimento the flgDonazioneRinvenimento to set
	 */
	public void setFlgDonazioneRinvenimento(Boolean flgDonazioneRinvenimento) {
		this.flgDonazioneRinvenimento = flgDonazioneRinvenimento;
	}

	/**
	 * @return the flgStatoBene
	 */
	public Boolean getFlgStatoBene() {
		return flgStatoBene;
	}

	/**
	 * @param flgStatoBene the flgStatoBene to set
	 */
	public void setFlgStatoBene(Boolean flgStatoBene) {
		this.flgStatoBene = flgStatoBene;
	}

	/**
	 * @return the numInventarioPrefisso
	 */
	public String getNumInventarioPrefisso() {
		return this.numInventarioPrefisso;
	}

	/**
	 * @param numInventarioPrefisso the numInventarioPrefisso to set
	 */
	public void setNumInventarioPrefisso(String numInventarioPrefisso) {
		this.numInventarioPrefisso = numInventarioPrefisso;
	}

	/**
	 * @return the numInventarioNumero
	 */
	public Integer getNumInventarioNumero() {
		return this.numInventarioNumero;
	}

	/**
	 * @param numInventarioNumero the numInventarioNumero to set
	 */
	public void setNumInventarioNumero(Integer numInventarioNumero) {
		this.numInventarioNumero = numInventarioNumero;
	}

	/**
	 * @return the siacDCespitiBeneTipo
	 */
	public SiacDCespitiBeneTipo getSiacDCespitiBeneTipo() {
		return siacDCespitiBeneTipo;
	}

	/**
	 * @param siacDCespitiBeneTipo the siacDCespitiBeneTipo to set
	 */
	public void setSiacDCespitiBeneTipo(SiacDCespitiBeneTipo siacDCespitiBeneTipo) {
		this.siacDCespitiBeneTipo = siacDCespitiBeneTipo;
	}

	/**
	 * @return the siacTCespitiDismissioni
	 */
	public SiacTCespitiDismissioni getSiacTCespitiDismissioni() {
		return siacTCespitiDismissioni;
	}

	/**
	 * @param siacTCespitiDismissioni the siacTCespitiDismissioni to set
	 */
	public void setSiacTCespitiDismissioni(SiacTCespitiDismissioni siacTCespitiDismissioni) {
		this.siacTCespitiDismissioni = siacTCespitiDismissioni;
	}

	/**
	 * @return the siacDCespitiClassificazioneGiuridica
	 */
	public SiacDCespitiClassificazioneGiuridica getSiacDCespitiClassificazioneGiuridica() {
		return siacDCespitiClassificazioneGiuridica;
	}

	/**
	 * @param siacDCespitiClassificazioneGiuridica the siacDCespitiClassificazioneGiuridica to set
	 */
	public void setSiacDCespitiClassificazioneGiuridica(
			SiacDCespitiClassificazioneGiuridica siacDCespitiClassificazioneGiuridica) {
		this.siacDCespitiClassificazioneGiuridica = siacDCespitiClassificazioneGiuridica;
	}
	

	
	/**
	 * @return the siacRCespitiPrimaNotas
	 */
	public List<SiacRCespitiPrimaNota> getSiacRCespitiPrimaNotas() {
		return siacRCespitiPrimaNotas;
	}

	/**
	 * @param siacRCespitiPrimaNotas the siacRCespitiPrimaNotas to set
	 */
	public void setSiacRCespitiPrimaNotas(List<SiacRCespitiPrimaNota> siacRCespitiPrimaNotas) {
		this.siacRCespitiPrimaNotas = siacRCespitiPrimaNotas;
	}
	
	/**
	 * Gets the siac t cespiti variaziones
	 *
	 * @return the siac t cespiti variaziones
	 */
	public List<SiacTCespitiVariazione> getSiacTCespitiVariaziones() {
		return this.siacTCespitiVariaziones;
	}

	/**
	 * Sets the siac t cespiti variaziones.
	 *
	 * @param siacTCespitiVariaziones the new siac t cespiti variaziones
	 */
	public void setSiacTCespitiVariaziones(List<SiacTCespitiVariazione> siacTCespitiVariaziones) {
		this.siacTCespitiVariaziones = siacTCespitiVariaziones;
	}

	/**
	 * Adds the siac t cespiti variazione.
	 *
	 * @param siacTCespitiVariazione the siac t cespiti variazione
	 * @return the siac t cespiti variazione
	 */
	public SiacTCespitiVariazione addSiacTCespitiVariazione(SiacTCespitiVariazione siacTCespitiVariazione) {
		getSiacTCespitiVariaziones().add(siacTCespitiVariazione);
		siacTCespitiVariazione.setSiacTCespiti(this);

		return siacTCespitiVariazione;
	}

	/**
	 * Removes the siac t cespiti variazione.
	 *
	 * @param siacTCespitiVariazione the siac t cespiti variazione
	 * @return the siac t cespiti variazione
	 */
	public SiacTCespitiVariazione removeSiacTCespitiVariazione(SiacTCespitiVariazione siacTCespitiVariazione) {
		getSiacTCespitiVariaziones().remove(siacTCespitiVariazione);
		siacTCespitiVariazione.setSiacTCespiti(null);

		return siacTCespitiVariazione;
	}
	
	/**
	 * Gets the siac t cespiti ammortamentos
	 *
	 * @return the siac t cespiti ammortamentos
	 */
	public List<SiacTCespitiAmmortamento> getSiacTCespitiAmmortamentos() {
		return this.siacTCespitiAmmortamentos;
	}

	/**
	 * Sets the siac t cespiti ammortamentos.
	 *
	 * @param siacTCespitiAmmortamentos the new siac t cespiti ammortamentos
	 */
	public void setSiacTCespitiAmmortamentos(List<SiacTCespitiAmmortamento> siacTCespitiAmmortamentos) {
		this.siacTCespitiAmmortamentos = siacTCespitiAmmortamentos;
	}

	/**
	 * Adds the siac t cespiti variazione.
	 *
	 * @param siacTCespitiAmmortamento the siac t cespiti variazione
	 * @return the siac t cespiti variazione
	 */
	public SiacTCespitiAmmortamento addSiacTCespitiAmmortamento(SiacTCespitiAmmortamento siacTCespitiAmmortamento) {
		getSiacTCespitiAmmortamentos().add(siacTCespitiAmmortamento);
		siacTCespitiAmmortamento.setSiacTCespiti(this);

		return siacTCespitiAmmortamento;
	}

	/**
	 * Removes the siac t cespiti variazione.
	 *
	 * @param siacTCespitiAmmortamento the siac t cespiti variazione
	 * @return the siac t cespiti variazione
	 */
	public SiacTCespitiAmmortamento removeSiacTCespitiAmmortamento(SiacTCespitiAmmortamento siacTCespitiAmmortamento) {
		getSiacTCespitiAmmortamentos().remove(siacTCespitiAmmortamento);
		siacTCespitiAmmortamento.setSiacTCespiti(null);

		return siacTCespitiAmmortamento;
	}
	
	
	/**
	 * @return the siacRCespitiCespitiElabAmmortamentiDetts
	 */
	public List<SiacRCespitiCespitiElabAmmortamentiDett> getSiacRCespitiCespitiElabAmmortamentiDetts() {
		return siacRCespitiCespitiElabAmmortamentiDetts;
	}

	/**
	 * @param siacRCespitiCespitiElabAmmortamentiDetts the siacRCespitiCespitiElabAmmortamentiDetts to set
	 */
	public void setSiacRCespitiCespitiElabAmmortamentiDetts(
			List<SiacRCespitiCespitiElabAmmortamentiDett> siacRCespitiCespitiElabAmmortamentiDetts) {
		this.siacRCespitiCespitiElabAmmortamentiDetts = siacRCespitiCespitiElabAmmortamentiDetts;
	}

	
	/**
	 * @return the siacRCespitiMovEpDets
	 */
	public List<SiacRCespitiMovEpDet> getSiacRCespitiMovEpDets() {
		return siacRCespitiMovEpDets;
	}

	/**
	 * @param siacRCespitiMovEpDets the siacRCespitiMovEpDets to set
	 */
	public void setSiacRCespitiMovEpDets(List<SiacRCespitiMovEpDet> siacRCespitiMovEpDets) {
		this.siacRCespitiMovEpDets = siacRCespitiMovEpDets;
	}

	/**
	 * Adds the siac t cespiti variazione.
	 *
	 * @param siacRCespitiCespitiElabAmmortamentiDett the siac R cespiti cespiti elab ammortamenti dett
	 * @return the siac t cespiti variazione
	 */
	public SiacRCespitiCespitiElabAmmortamentiDett addSiacTCespitiAmmortamento(SiacRCespitiCespitiElabAmmortamentiDett siacRCespitiCespitiElabAmmortamentiDett) {
		getSiacRCespitiCespitiElabAmmortamentiDetts().add(siacRCespitiCespitiElabAmmortamentiDett);
		siacRCespitiCespitiElabAmmortamentiDett.setSiacTCespiti(this);

		return siacRCespitiCespitiElabAmmortamentiDett;
	}

	/**
	 * Removes the siac t cespiti variazione.
	 *
	 * @param siacRCespitiCespitiElabAmmortamentiDett the siac R cespiti cespiti elab ammortamenti dett
	 * @return the siac t cespiti variazione
	 */
	public SiacRCespitiCespitiElabAmmortamentiDett removeSiacTCespitiAmmortamento(SiacRCespitiCespitiElabAmmortamentiDett siacRCespitiCespitiElabAmmortamentiDett) {
		getSiacRCespitiCespitiElabAmmortamentiDetts().remove(siacRCespitiCespitiElabAmmortamentiDett);
		siacRCespitiCespitiElabAmmortamentiDett.setSiacTCespiti(null);

		return siacRCespitiCespitiElabAmmortamentiDett;
	}
	
	
	@Override
	public Integer getUid() {
		return this.cesId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cesId = uid;
	}

}