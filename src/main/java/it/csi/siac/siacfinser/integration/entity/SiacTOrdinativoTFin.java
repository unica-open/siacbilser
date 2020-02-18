/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.sql.Timestamp;
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

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_ordinativo_ts database table.
 * 
 */
@Entity
@Table(name="siac_t_ordinativo_ts")
public class SiacTOrdinativoTFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_ORDINATIVO_T_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_ordinativo_ts_ord_ts_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ORDINATIVO_T_ID_GENERATOR")
	@Column(name="ord_ts_id")
	private Integer ordTsId;

	@Column(name="ord_ts_code")
	private String ordTsCode;

	@Column(name="ord_ts_data_scadenza")
	private Timestamp ordTsDataScadenza;

	@Column(name="ord_ts_desc")
	private String ordTsDesc;

	//bi-directional many-to-one association to SiacRLiquidazioneOrdFin
	@OneToMany(mappedBy="siacTOrdinativoT")
	private List<SiacRLiquidazioneOrdFin> siacRLiquidazioneOrds;

	//bi-directional many-to-one association to SiacROrdinativoTsMovgestTFin
	@OneToMany(mappedBy="siacTOrdinativoT")
	private List<SiacROrdinativoTsMovgestTFin> siacROrdinativoTsMovgestTs;

	//bi-directional many-to-one association to SiacRDocOnereFin
	@ManyToOne
	@JoinColumn(name="doc_onere_id")
	private SiacRDocOnereFin siacRDocOnere;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativoFin siacTOrdinativo;

	//bi-directional many-to-one association to SiacTOrdinativoTsDetFin
	@OneToMany(mappedBy="siacTOrdinativoT")
	private List<SiacTOrdinativoTsDetFin> siacTOrdinativoTsDets;
	
	//bi-directional many-to-one association to SiacRDocOnereOrdinativoTsFin
	@OneToMany(mappedBy="siacTOrdinativoT")
	private List<SiacRDocOnereOrdinativoTsFin> siacRDocOnereOrdinativoTsFin;
	
	
	//bi-directional many-to-one association to SiacRSubdocOrdinativoT
	@OneToMany(mappedBy="siacTOrdinativoT", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRSubdocOrdinativoTFin> siacRSubdocOrdinativoTs;


	/**
	 * @return the siacRSubdocOrdinativoTs
	 */
	public List<SiacRSubdocOrdinativoTFin> getSiacRSubdocOrdinativoTs() {
		return siacRSubdocOrdinativoTs;
	}

	/**
	 * @param siacRSubdocOrdinativoTs the siacRSubdocOrdinativoTs to set
	 */
	public void setSiacRSubdocOrdinativoTs(
			List<SiacRSubdocOrdinativoTFin> siacRSubdocOrdinativoTs) {
		this.siacRSubdocOrdinativoTs = siacRSubdocOrdinativoTs;
	}

	public SiacTOrdinativoTFin() {
	}

	public Integer getOrdTsId() {
		return this.ordTsId;
	}

	public void setOrdTsId(Integer ordTsId) {
		this.ordTsId = ordTsId;
	}

	public String getOrdTsCode() {
		return this.ordTsCode;
	}

	public void setOrdTsCode(String ordTsCode) {
		this.ordTsCode = ordTsCode;
	}

	public Timestamp getOrdTsDataScadenza() {
		return this.ordTsDataScadenza;
	}

	public void setOrdTsDataScadenza(Timestamp ordTsDataScadenza) {
		this.ordTsDataScadenza = ordTsDataScadenza;
	}

	public String getOrdTsDesc() {
		return this.ordTsDesc;
	}

	public void setOrdTsDesc(String ordTsDesc) {
		this.ordTsDesc = ordTsDesc;
	}

	public List<SiacRLiquidazioneOrdFin> getSiacRLiquidazioneOrds() {
		return this.siacRLiquidazioneOrds;
	}

	public void setSiacRLiquidazioneOrds(List<SiacRLiquidazioneOrdFin> siacRLiquidazioneOrds) {
		this.siacRLiquidazioneOrds = siacRLiquidazioneOrds;
	}

	public SiacRLiquidazioneOrdFin addSiacRLiquidazioneOrd(SiacRLiquidazioneOrdFin siacRLiquidazioneOrd) {
		getSiacRLiquidazioneOrds().add(siacRLiquidazioneOrd);
		siacRLiquidazioneOrd.setSiacTOrdinativoT(this);

		return siacRLiquidazioneOrd;
	}

	public SiacRLiquidazioneOrdFin removeSiacRLiquidazioneOrd(SiacRLiquidazioneOrdFin siacRLiquidazioneOrd) {
		getSiacRLiquidazioneOrds().remove(siacRLiquidazioneOrd);
		siacRLiquidazioneOrd.setSiacTOrdinativoT(null);

		return siacRLiquidazioneOrd;
	}

	public List<SiacROrdinativoTsMovgestTFin> getSiacROrdinativoTsMovgestTs() {
		return this.siacROrdinativoTsMovgestTs;
	}

	public void setSiacROrdinativoTsMovgestTs(List<SiacROrdinativoTsMovgestTFin> siacROrdinativoTsMovgestTs) {
		this.siacROrdinativoTsMovgestTs = siacROrdinativoTsMovgestTs;
	}

	public SiacROrdinativoTsMovgestTFin addSiacROrdinativoTsMovgestT(SiacROrdinativoTsMovgestTFin siacROrdinativoTsMovgestT) {
		getSiacROrdinativoTsMovgestTs().add(siacROrdinativoTsMovgestT);
		siacROrdinativoTsMovgestT.setSiacTOrdinativoT(this);

		return siacROrdinativoTsMovgestT;
	}

	public SiacROrdinativoTsMovgestTFin removeSiacROrdinativoTsMovgestT(SiacROrdinativoTsMovgestTFin siacROrdinativoTsMovgestT) {
		getSiacROrdinativoTsMovgestTs().remove(siacROrdinativoTsMovgestT);
		siacROrdinativoTsMovgestT.setSiacTOrdinativoT(null);

		return siacROrdinativoTsMovgestT;
	}

	public SiacRDocOnereFin getSiacRDocOnere() {
		return this.siacRDocOnere;
	}

	public void setSiacRDocOnere(SiacRDocOnereFin siacRDocOnere) {
		this.siacRDocOnere = siacRDocOnere;
	}

	public SiacTOrdinativoFin getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	public void setSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	public List<SiacTOrdinativoTsDetFin> getSiacTOrdinativoTsDets() {
		return this.siacTOrdinativoTsDets;
	}

	public void setSiacTOrdinativoTsDets(List<SiacTOrdinativoTsDetFin> siacTOrdinativoTsDets) {
		this.siacTOrdinativoTsDets = siacTOrdinativoTsDets;
	}

	public SiacTOrdinativoTsDetFin addSiacTOrdinativoTsDet(SiacTOrdinativoTsDetFin siacTOrdinativoTsDet) {
		getSiacTOrdinativoTsDets().add(siacTOrdinativoTsDet);
		siacTOrdinativoTsDet.setSiacTOrdinativoT(this);

		return siacTOrdinativoTsDet;
	}

	public SiacTOrdinativoTsDetFin removeSiacTOrdinativoTsDet(SiacTOrdinativoTsDetFin siacTOrdinativoTsDet) {
		getSiacTOrdinativoTsDets().remove(siacTOrdinativoTsDet);
		siacTOrdinativoTsDet.setSiacTOrdinativoT(null);

		return siacTOrdinativoTsDet;
	}
	
	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordTsId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordTsId = uid;
	}

	public List<SiacRDocOnereOrdinativoTsFin> getSiacRDocOnereOrdinativoTsFin() {
		return siacRDocOnereOrdinativoTsFin;
	}

	public void setSiacRDocOnereOrdinativoTsFin(List<SiacRDocOnereOrdinativoTsFin> siacRDocOnereOrdinativoTsFin) {
		this.siacRDocOnereOrdinativoTsFin = siacRDocOnereOrdinativoTsFin;
	}
	
}