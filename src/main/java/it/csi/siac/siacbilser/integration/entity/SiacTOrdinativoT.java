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
 * The persistent class for the siac_t_ordinativo_ts database table.
 * 
 */
@Entity
@Table(name="siac_t_ordinativo_ts")
@NamedQuery(name="SiacTOrdinativoT.findAll", query="SELECT s FROM SiacTOrdinativoT s")
public class SiacTOrdinativoT extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord ts id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ORDINATIVO_TS_ORDTSID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ORDINATIVO_TS_ORD_TS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ORDINATIVO_TS_ORDTSID_GENERATOR")
	@Column(name="ord_ts_id")
	private Integer ordTsId;

	/** The ord ts code. */
	@Column(name="ord_ts_code")
	private String ordTsCode;

	/** The ord ts data scadenza. */
	@Column(name="ord_ts_data_scadenza")
	private Date ordTsDataScadenza;

	/** The ord ts desc. */
	@Column(name="ord_ts_desc")
	private String ordTsDesc;


	//bi-directional many-to-one association to SiacRLiquidazioneOrd
	/** The siac r liquidazione ords. */
	@OneToMany(mappedBy="siacTOrdinativoT")
	private List<SiacRLiquidazioneOrd> siacRLiquidazioneOrds;

	//bi-directional many-to-one association to SiacROrdinativoTsMovgestT
	/** The siac r ordinativo ts movgest ts. */
	@OneToMany(mappedBy="siacTOrdinativoT")
	private List<SiacROrdinativoTsMovgestT> siacROrdinativoTsMovgestTs;
	
	//bi-directional many-to-one association to SiacRSubdocOrdinativoT
	@OneToMany(mappedBy="siacTOrdinativoT", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocOrdinativoT> siacRSubdocOrdinativoTs;

	//bi-directional many-to-one association to SiacRDocOnere
	/** The siac r doc onere. */
	@ManyToOne
	@JoinColumn(name="doc_onere_id")
	private SiacRDocOnere siacRDocOnere;

	//bi-directional many-to-one association to SiacTOrdinativo
	/** The siac t ordinativo. */
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativo siacTOrdinativo;

	//bi-directional many-to-one association to SiacTOrdinativoTsDet
	/** The siac t ordinativo ts dets. */
	@OneToMany(mappedBy="siacTOrdinativoT")
	private List<SiacTOrdinativoTsDet> siacTOrdinativoTsDets;

	/**
	 * Instantiates a new siac t ordinativo t.
	 */
	public SiacTOrdinativoT() {
	}

	/**
	 * Gets the ord ts id.
	 *
	 * @return the ord ts id
	 */
	public Integer getOrdTsId() {
		return this.ordTsId;
	}

	/**
	 * Sets the ord ts id.
	 *
	 * @param ordTsId the new ord ts id
	 */
	public void setOrdTsId(Integer ordTsId) {
		this.ordTsId = ordTsId;
	}

	/**
	 * Gets the ord ts code.
	 *
	 * @return the ord ts code
	 */
	public String getOrdTsCode() {
		return this.ordTsCode;
	}

	/**
	 * Sets the ord ts code.
	 *
	 * @param ordTsCode the new ord ts code
	 */
	public void setOrdTsCode(String ordTsCode) {
		this.ordTsCode = ordTsCode;
	}

	/**
	 * Gets the ord ts data scadenza.
	 *
	 * @return the ord ts data scadenza
	 */
	public Date getOrdTsDataScadenza() {
		return this.ordTsDataScadenza;
	}

	/**
	 * Sets the ord ts data scadenza.
	 *
	 * @param ordTsDataScadenza the new ord ts data scadenza
	 */
	public void setOrdTsDataScadenza(Date ordTsDataScadenza) {
		this.ordTsDataScadenza = ordTsDataScadenza;
	}

	/**
	 * Gets the ord ts desc.
	 *
	 * @return the ord ts desc
	 */
	public String getOrdTsDesc() {
		return this.ordTsDesc;
	}

	/**
	 * Sets the ord ts desc.
	 *
	 * @param ordTsDesc the new ord ts desc
	 */
	public void setOrdTsDesc(String ordTsDesc) {
		this.ordTsDesc = ordTsDesc;
	}

	/**
	 * Gets the siac r liquidazione ords.
	 *
	 * @return the siac r liquidazione ords
	 */
	public List<SiacRLiquidazioneOrd> getSiacRLiquidazioneOrds() {
		return this.siacRLiquidazioneOrds;
	}

	/**
	 * Sets the siac r liquidazione ords.
	 *
	 * @param siacRLiquidazioneOrds the new siac r liquidazione ords
	 */
	public void setSiacRLiquidazioneOrds(List<SiacRLiquidazioneOrd> siacRLiquidazioneOrds) {
		this.siacRLiquidazioneOrds = siacRLiquidazioneOrds;
	}

	/**
	 * Adds the siac r liquidazione ord.
	 *
	 * @param siacRLiquidazioneOrd the siac r liquidazione ord
	 * @return the siac r liquidazione ord
	 */
	public SiacRLiquidazioneOrd addSiacRLiquidazioneOrd(SiacRLiquidazioneOrd siacRLiquidazioneOrd) {
		getSiacRLiquidazioneOrds().add(siacRLiquidazioneOrd);
		siacRLiquidazioneOrd.setSiacTOrdinativoT(this);

		return siacRLiquidazioneOrd;
	}

	/**
	 * Removes the siac r liquidazione ord.
	 *
	 * @param siacRLiquidazioneOrd the siac r liquidazione ord
	 * @return the siac r liquidazione ord
	 */
	public SiacRLiquidazioneOrd removeSiacRLiquidazioneOrd(SiacRLiquidazioneOrd siacRLiquidazioneOrd) {
		getSiacRLiquidazioneOrds().remove(siacRLiquidazioneOrd);
		siacRLiquidazioneOrd.setSiacTOrdinativoT(null);

		return siacRLiquidazioneOrd;
	}

	/**
	 * Gets the siac r ordinativo ts movgest ts.
	 *
	 * @return the siac r ordinativo ts movgest ts
	 */
	public List<SiacROrdinativoTsMovgestT> getSiacROrdinativoTsMovgestTs() {
		return this.siacROrdinativoTsMovgestTs;
	}

	/**
	 * Sets the siac r ordinativo ts movgest ts.
	 *
	 * @param siacROrdinativoTsMovgestTs the new siac r ordinativo ts movgest ts
	 */
	public void setSiacROrdinativoTsMovgestTs(List<SiacROrdinativoTsMovgestT> siacROrdinativoTsMovgestTs) {
		this.siacROrdinativoTsMovgestTs = siacROrdinativoTsMovgestTs;
	}

	/**
	 * Adds the siac r ordinativo ts movgest t.
	 *
	 * @param siacROrdinativoTsMovgestT the siac r ordinativo ts movgest t
	 * @return the siac r ordinativo ts movgest t
	 */
	public SiacROrdinativoTsMovgestT addSiacROrdinativoTsMovgestT(SiacROrdinativoTsMovgestT siacROrdinativoTsMovgestT) {
		getSiacROrdinativoTsMovgestTs().add(siacROrdinativoTsMovgestT);
		siacROrdinativoTsMovgestT.setSiacTOrdinativoT(this);

		return siacROrdinativoTsMovgestT;
	}

	/**
	 * Removes the siac r ordinativo ts movgest t.
	 *
	 * @param siacROrdinativoTsMovgestT the siac r ordinativo ts movgest t
	 * @return the siac r ordinativo ts movgest t
	 */
	public SiacROrdinativoTsMovgestT removeSiacROrdinativoTsMovgestT(SiacROrdinativoTsMovgestT siacROrdinativoTsMovgestT) {
		getSiacROrdinativoTsMovgestTs().remove(siacROrdinativoTsMovgestT);
		siacROrdinativoTsMovgestT.setSiacTOrdinativoT(null);

		return siacROrdinativoTsMovgestT;
	}

	/**
	 * @return the siacRSubdocOrdinativoTs
	 */
	public List<SiacRSubdocOrdinativoT> getSiacRSubdocOrdinativoTs() {
		return siacRSubdocOrdinativoTs;
	}

	/**
	 * @param siacRSubdocOrdinativoTs the siacRSubdocOrdinativoTs to set
	 */
	public void setSiacRSubdocOrdinativoTs(List<SiacRSubdocOrdinativoT> siacRSubdocOrdinativoTs) {
		this.siacRSubdocOrdinativoTs = siacRSubdocOrdinativoTs;
	}

	/**
	 * Gets the siac r doc onere.
	 *
	 * @return the siac r doc onere
	 */
	public SiacRDocOnere getSiacRDocOnere() {
		return this.siacRDocOnere;
	}

	/**
	 * Sets the siac r doc onere.
	 *
	 * @param siacRDocOnere the new siac r doc onere
	 */
	public void setSiacRDocOnere(SiacRDocOnere siacRDocOnere) {
		this.siacRDocOnere = siacRDocOnere;
	}

	/**
	 * Gets the siac t ordinativo.
	 *
	 * @return the siac t ordinativo
	 */
	public SiacTOrdinativo getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	/**
	 * Sets the siac t ordinativo.
	 *
	 * @param siacTOrdinativo the new siac t ordinativo
	 */
	public void setSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	/**
	 * Gets the siac t ordinativo ts dets.
	 *
	 * @return the siac t ordinativo ts dets
	 */
	public List<SiacTOrdinativoTsDet> getSiacTOrdinativoTsDets() {
		return this.siacTOrdinativoTsDets;
	}

	/**
	 * Sets the siac t ordinativo ts dets.
	 *
	 * @param siacTOrdinativoTsDets the new siac t ordinativo ts dets
	 */
	public void setSiacTOrdinativoTsDets(List<SiacTOrdinativoTsDet> siacTOrdinativoTsDets) {
		this.siacTOrdinativoTsDets = siacTOrdinativoTsDets;
	}

	/**
	 * Adds the siac t ordinativo ts det.
	 *
	 * @param siacTOrdinativoTsDet the siac t ordinativo ts det
	 * @return the siac t ordinativo ts det
	 */
	public SiacTOrdinativoTsDet addSiacTOrdinativoTsDet(SiacTOrdinativoTsDet siacTOrdinativoTsDet) {
		getSiacTOrdinativoTsDets().add(siacTOrdinativoTsDet);
		siacTOrdinativoTsDet.setSiacTOrdinativoT(this);

		return siacTOrdinativoTsDet;
	}

	/**
	 * Removes the siac t ordinativo ts det.
	 *
	 * @param siacTOrdinativoTsDet the siac t ordinativo ts det
	 * @return the siac t ordinativo ts det
	 */
	public SiacTOrdinativoTsDet removeSiacTOrdinativoTsDet(SiacTOrdinativoTsDet siacTOrdinativoTsDet) {
		getSiacTOrdinativoTsDets().remove(siacTOrdinativoTsDet);
		siacTOrdinativoTsDet.setSiacTOrdinativoT(null);

		return siacTOrdinativoTsDet;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordTsId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ordTsId = uid;
	}

}