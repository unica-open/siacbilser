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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_prima_nota database table.
 * 
 */
@Entity
@Table(name="siac_t_prima_nota")
@NamedQuery(name="SiacTPrimaNota.findAll", query="SELECT s FROM SiacTPrimaNota s")
public class SiacTPrimaNota extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PRIMA_NOTA_PNOTAID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PRIMA_NOTA_PNOTA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PRIMA_NOTA_PNOTAID_GENERATOR")
	@Column(name="pnota_id")
	private Integer pnotaId;

	@Column(name="pnota_data")
	private Date pnotaData;

	@Column(name="pnota_dataregistrazionegiornale")
	private Date pnotaDataregistrazionegiornale;

	@Column(name="pnota_desc")
	private String pnotaDesc;

	@Column(name="pnota_numero")
	private Integer pnotaNumero;

	@Column(name="pnota_progressivogiornale")
	private Integer pnotaProgressivogiornale;

	//bi-directional many-to-one association to SiacRPrimaNotaStato
	@OneToMany(mappedBy="siacTPrimaNota", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPrimaNotaStato> siacRPrimaNotaStatos;
	
	//bi-directional many-to-one association to SiacRPrimaNotaFiglio
	@OneToMany(mappedBy="siacTPrimaNotaFiglio", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPrimaNota> siacRPrimaNotaFiglio;
	
	//bi-directional many-to-one association to SiacRPrimaNotaPadre
	@OneToMany(mappedBy="siacTPrimaNotaPadre", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPrimaNota> siacRPrimaNotaPadre;

	//bi-directional many-to-one association to SiacTMovEp
	@OneToMany(mappedBy="siacTPrimaNota", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTMovEp> siacTMovEps;

	
	//bi-directional many-to-one association to SiacRPnDefAccettazioneStato
	@OneToMany(mappedBy="siacTPrimaNota", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPnDefAccettazioneStato> siacRPnDefAccettazioneStatos;

	
	//bi-directional many-to-one association to SiacRPnDefAccettazioneStato
	@OneToMany(mappedBy="siacTPrimaNota", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPnProvAccettazioneStato> siacRPnProvAccettazioneStatos;
	

	//bi-directional many-to-one association to SiacRCespitiPrimaNota
	@OneToMany(mappedBy="siacTPrimaNota", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCespitiPrimaNota> siacRCespitiPrimaNota;
	
	//bi-directional many-to-one association to SiacRCespitiDismissioniPrimaNota
	@OneToMany(mappedBy="siacTPrimaNota", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCespitiDismissioniPrimaNota> siacRCespitiDismissioniPrimaNota;	
	
	
	//bi-directional many-to-one association to SiacDCausaleEpTipo
	@ManyToOne
	@JoinColumn(name="causale_ep_tipo_id")
	private SiacDCausaleEpTipo siacDCausaleEpTipo;

	//bi-directional many-to-one association to SiacTBil
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;
	
	//bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;
	
	//bi-directional many-to-one association to SiacRPrimaNotaStato
	@OneToMany(mappedBy="siacTPrimaNota")
	private List<SiacTPrimaNotaRateiRisconti> siacTPrimaNotaRateiRiscontis;
	
	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id", updatable=false)
	private SiacTSoggetto siacTSoggetto;
	
	/** The siac r gsa classif prima notas. */
	@OneToMany(mappedBy="siacTPrimaNota", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRGsaClassifPrimaNota> siacRGsaClassifPrimaNotas;

	public SiacTPrimaNota() {
	}

	public Integer getPnotaId() {
		return this.pnotaId;
	}

	public void setPnotaId(Integer pnotaId) {
		this.pnotaId = pnotaId;
	}


	public Date getPnotaData() {
		return this.pnotaData;
	}

	public void setPnotaData( Date pnotaData) {
		this.pnotaData = pnotaData;
	}

	public Date getPnotaDataregistrazionegiornale() {
		return this.pnotaDataregistrazionegiornale;
	}

	public void setPnotaDataregistrazionegiornale( Date pnotaDataregistrazionegiornale) {
		this.pnotaDataregistrazionegiornale = pnotaDataregistrazionegiornale;
	}

	public String getPnotaDesc() {
		return this.pnotaDesc;
	}

	public void setPnotaDesc(String pnotaDesc) {
		this.pnotaDesc = pnotaDesc;
	}

	public Integer getPnotaNumero() {
		return this.pnotaNumero;
	}

	public void setPnotaNumero(Integer pnotaNumero) {
		this.pnotaNumero = pnotaNumero;
	}

	public Integer getPnotaProgressivogiornale() {
		return this.pnotaProgressivogiornale;
	}

	public void setPnotaProgressivogiornale(Integer pnotaProgressivogiornale) {
		this.pnotaProgressivogiornale = pnotaProgressivogiornale;
	}

	public List<SiacRPrimaNotaStato> getSiacRPrimaNotaStatos() {
		return this.siacRPrimaNotaStatos;
	}

	public void setSiacRPrimaNotaStatos(List<SiacRPrimaNotaStato> siacRPrimaNotaStatos) {
		this.siacRPrimaNotaStatos = siacRPrimaNotaStatos;
	}

	public SiacRPrimaNotaStato addSiacRPrimaNotaStato(SiacRPrimaNotaStato siacRPrimaNotaStato) {
		getSiacRPrimaNotaStatos().add(siacRPrimaNotaStato);
		siacRPrimaNotaStato.setSiacTPrimaNota(this);

		return siacRPrimaNotaStato;
	}

	public SiacRPrimaNotaStato removeSiacRPrimaNotaStato(SiacRPrimaNotaStato siacRPrimaNotaStato) {
		getSiacRPrimaNotaStatos().remove(siacRPrimaNotaStato);
		siacRPrimaNotaStato.setSiacTPrimaNota(null);

		return siacRPrimaNotaStato;
	}

	/**
	 * @return the siacTPrimaNotaRateiRiscontis
	 */
	public List<SiacTPrimaNotaRateiRisconti> getSiacTPrimaNotaRateiRiscontis() {
		return siacTPrimaNotaRateiRiscontis;
	}

	/**
	 * @param siacTPrimaNotaRateiRiscontis the siacTPrimaNotaRateiRiscontis to set
	 */
	public void setSiacTPrimaNotaRateiRiscontis(List<SiacTPrimaNotaRateiRisconti> siacTPrimaNotaRateiRiscontis) {
		this.siacTPrimaNotaRateiRiscontis = siacTPrimaNotaRateiRiscontis;
	}
	
	public SiacTPrimaNotaRateiRisconti addSiacTPrimaNotaRateiRisconti(SiacTPrimaNotaRateiRisconti siacTPrimaNotaRateiRisconti) {
		getSiacTPrimaNotaRateiRiscontis().add(siacTPrimaNotaRateiRisconti);
		siacTPrimaNotaRateiRisconti.setSiacTPrimaNota(this);

		return siacTPrimaNotaRateiRisconti;
	}

	public SiacTPrimaNotaRateiRisconti removeSiacTPrimaNotaRateiRisconti(SiacTPrimaNotaRateiRisconti siacTPrimaNotaRateiRisconti) {
		getSiacTPrimaNotaRateiRiscontis().remove(siacTPrimaNotaRateiRisconti);
		siacTPrimaNotaRateiRisconti.setSiacTPrimaNota(null);

		return siacTPrimaNotaRateiRisconti;
	}

	/**
	 * @return the siacRPrimaNotaFiglio
	 */
	public List<SiacRPrimaNota> getSiacRPrimaNotaFiglio() {
		return siacRPrimaNotaFiglio;
	}

	/**
	 * @param siacRPrimaNotaFiglio the siacRPrimaNotaFiglio to set
	 */
	public void setSiacRPrimaNotaFiglio(List<SiacRPrimaNota> siacRPrimaNotaFiglio) {
		this.siacRPrimaNotaFiglio = siacRPrimaNotaFiglio;
	}

	/**
	 * @return the siacRPrimaNotaPadre
	 */
	public List<SiacRPrimaNota> getSiacRPrimaNotaPadre() {
		return siacRPrimaNotaPadre;
	}

	/**
	 * @param siacRPrimaNotaPadre the siacRPrimaNotaPadre to set
	 */
	public void setSiacRPrimaNotaPadre(List<SiacRPrimaNota> siacRPrimaNotaPadre) {
		this.siacRPrimaNotaPadre = siacRPrimaNotaPadre;
	}

	public List<SiacTMovEp> getSiacTMovEps() {
		return this.siacTMovEps;
	}

	public void setSiacTMovEps(List<SiacTMovEp> siacTMovEps) {
		this.siacTMovEps = siacTMovEps;
	}

	public SiacTMovEp addSiacTMovEp(SiacTMovEp siacTMovEp) {
		getSiacTMovEps().add(siacTMovEp);
		siacTMovEp.setSiacTPrimaNota(this);

		return siacTMovEp;
	}

	public SiacTMovEp removeSiacTMovEp(SiacTMovEp siacTMovEp) {
		getSiacTMovEps().remove(siacTMovEp);
		siacTMovEp.setSiacTPrimaNota(null);

		return siacTMovEp;
	}

	public SiacDCausaleEpTipo getSiacDCausaleEpTipo() {
		return this.siacDCausaleEpTipo;
	}

	public void setSiacDCausaleEpTipo(SiacDCausaleEpTipo siacDCausaleEpTipo) {
		this.siacDCausaleEpTipo = siacDCausaleEpTipo;
	}

	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}
	
	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}
	
	/**
	 * Gets the siac t soggetto.
	 *
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	/**
	 * Sets the siac t soggetto.
	 *
	 * @param siacTSoggetto the new siac t soggetto
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}
	
	/**
	 * Gets the siac r gsa classif prima notas.
	 *
	 * @return the siac r gsa classif prima notas
	 */
	public List<SiacRGsaClassifPrimaNota> getSiacRGsaClassifPrimaNotas() {
		return this.siacRGsaClassifPrimaNotas;
	}

	/**
	 * Sets the siac r gsa classif prima notas.
	 *
	 * @param siacRGsaClassifPrimaNotas the new siac r gsa classif prima notas
	 */
	public void setSiacRGsaClassifPrimaNotas(List<SiacRGsaClassifPrimaNota> siacRGsaClassifPrimaNotas) {
		this.siacRGsaClassifPrimaNotas = siacRGsaClassifPrimaNotas;
	}

	/**
	 * Adds the siac r gsa classif prima nota.
	 *
	 * @param siacRGsaClassifPrimaNota the siac r gsa classif prima nota
	 * @return the siac r gsa classif prima nota
	 */
	public SiacRGsaClassifPrimaNota addSiacRGsaClassifPrimaNota(SiacRGsaClassifPrimaNota siacRGsaClassifPrimaNota) {
		getSiacRGsaClassifPrimaNotas().add(siacRGsaClassifPrimaNota);
		siacRGsaClassifPrimaNota.setSiacTPrimaNota(this);

		return siacRGsaClassifPrimaNota;
	}

	/**
	 * Removes the siac r gsa classif prima nota.
	 *
	 * @param siacRGsaClassifPrimaNota the siac r gsa classif prima nota
	 * @return the siac r gsa classif prima nota
	 */
	public SiacRGsaClassifPrimaNota removeSiacRGsaClassifPrimaNota(SiacRGsaClassifPrimaNota  siacRGsaClassifPrimaNota) {
		getSiacRGsaClassifPrimaNotas().remove(siacRGsaClassifPrimaNota);
		siacRGsaClassifPrimaNota.setSiacTPrimaNota(null);

		return siacRGsaClassifPrimaNota;
	}
	
	
	/**
	 * @return the siacRPnDefAccettazioneStato
	 */
	public List<SiacRPnDefAccettazioneStato> getSiacRPnDefAccettazioneStatos() {
		return siacRPnDefAccettazioneStatos;
	}

	/**
	 * Sets the siac R pn def accettazione statos.
	 *
	 * @param siacRPnDefAccettazioneStatos the new siac R pn def accettazione statos
	 */
	public void setSiacRPnDefAccettazioneStatos(List<SiacRPnDefAccettazioneStato> siacRPnDefAccettazioneStatos) {
		this.siacRPnDefAccettazioneStatos = siacRPnDefAccettazioneStatos;
	}

	/**
	 * Adds the siac r gsa classif prima nota.
	 *
	 * @param siacRPnDefAccettazioneStato the siac r gsa classif prima nota
	 * @return the siac r gsa classif prima nota
	 */
	public SiacRPnDefAccettazioneStato addSiacRPnDefAccettazioneStatos(SiacRPnDefAccettazioneStato siacRPnDefAccettazioneStato) {
		getSiacRPnDefAccettazioneStatos().add(siacRPnDefAccettazioneStato);
		siacRPnDefAccettazioneStato.setSiacTPrimaNota(this);

		return siacRPnDefAccettazioneStato;
	}

	/**
	 * Removes the siac r gsa classif prima nota.
	 *
	 * @param siacRPnDefAccettazioneStato the siac r gsa classif prima nota
	 * @return the siac r gsa classif prima nota
	 */
	public SiacRPnDefAccettazioneStato removeSiacRPnDefAccettazioneStatos(SiacRPnDefAccettazioneStato  siacRPnDefAccettazioneStato) {
		getSiacRPnDefAccettazioneStatos().remove(siacRPnDefAccettazioneStato);
		siacRPnDefAccettazioneStato.setSiacTPrimaNota(null);

		return siacRPnDefAccettazioneStato;
	}
	
	/**
	 * @return the siacRPnProvAccettazioneStato
	 */
	public List<SiacRPnProvAccettazioneStato> getSiacRPnProvAccettazioneStatos() {
		return siacRPnProvAccettazioneStatos;
	}

	/**
	 * @param siacRPnProvAccettazioneStato the siacRPnProvAccettazioneStato to set
	 */
	public void setSiacRPnProvAccettazioneStatos(List<SiacRPnProvAccettazioneStato> siacRPnProvAccettazioneStatos) {
		this.siacRPnProvAccettazioneStatos = siacRPnProvAccettazioneStatos;
	}

	
	/**
	 * Adds the siac r gsa classif prima nota.
	 *
	 * @param siacRPnProvAccettazioneStato the siac r gsa classif prima nota
	 * @return the siac r gsa classif prima nota
	 */
	public SiacRPnProvAccettazioneStato addSiacRPnProvAccettazioneStato(SiacRPnProvAccettazioneStato siacRPnProvAccettazioneStato) {
		getSiacRPnProvAccettazioneStatos().add(siacRPnProvAccettazioneStato);
		siacRPnProvAccettazioneStato.setSiacTPrimaNota(this);

		return siacRPnProvAccettazioneStato;
	}

	/**
	 * Removes the siac r gsa classif prima nota.
	 *
	 * @param siacRPnProvAccettazioneStato the siac r gsa classif prima nota
	 * @return the siac r gsa classif prima nota
	 */
	public SiacRPnProvAccettazioneStato removeSiacRPnProvAccettazioneStato(SiacRPnProvAccettazioneStato  siacRPnProvAccettazioneStato) {
		getSiacRPnProvAccettazioneStatos().remove(siacRPnProvAccettazioneStato);
		siacRPnProvAccettazioneStato.setSiacTPrimaNota(null);

		return siacRPnProvAccettazioneStato;
	}
	
	/**
	 * @return the siacRCespitiPrimaNota
	 */
	public List<SiacRCespitiPrimaNota> getSiacRCespitiPrimaNota() {
		return siacRCespitiPrimaNota;
	}

	/**
	 * @param siacRCespitiPrimaNota the siacRCespitiPrimaNota to set
	 */
	public void setSiacRCespitiPrimaNota(List<SiacRCespitiPrimaNota> siacRCespitiPrimaNota) {
		this.siacRCespitiPrimaNota = siacRCespitiPrimaNota;
	}

	/**
	 * @return the siacRCespitiDismissioniPrimaNota
	 */
	public List<SiacRCespitiDismissioniPrimaNota> getSiacRCespitiDismissioniPrimaNota() {
		return siacRCespitiDismissioniPrimaNota;
	}

	/**
	 * @param siacRCespitiDismissioniPrimaNota the siacRCespitiDismissioniPrimaNota to set
	 */
	public void setSiacRCespitiDismissioniPrimaNota(
			List<SiacRCespitiDismissioniPrimaNota> siacRCespitiDismissioniPrimaNota) {
		this.siacRCespitiDismissioniPrimaNota = siacRCespitiDismissioniPrimaNota;
	}

	@Override
	public Integer getUid() {
		return pnotaId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pnotaId = uid;
	}

}