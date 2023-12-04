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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the siac_d_siope_tipo_debito database table.
 * 
 */
@Entity
@Table(name="siac_d_siope_tipo_debito")
@NamedQuery(name="SiacDSiopeTipoDebito.findAll", query="SELECT s FROM SiacDSiopeTipoDebito s")
public class SiacDSiopeTipoDebito extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The siope tipo debito id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SIOPE_TIPO_DEBITO_SIOPETIPODEBITOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SIOPE_TIPO_DEBITO_SIOPE_TIPO_DEBITO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SIOPE_TIPO_DEBITO_SIOPETIPODEBITOID_GENERATOR")
	@Column(name="siope_tipo_debito_id")
	private Integer siopeTipoDebitoId;

	/** The siope tipo debito code. */
	@Column(name="siope_tipo_debito_code")
	private String siopeTipoDebitoCode;

	/** The siope tipo debito desc. */
	@Column(name="siope_tipo_debito_desc")
	private String siopeTipoDebitoDesc;
	
	/** The siope tipo debito desc bnkit. */
	@Column(name="siope_tipo_debito_desc_bnkit")
	private String siopeTipoDebitoDescBnkit;
	
	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest ts. */
	@OneToMany(mappedBy="siacDSiopeTipoDebito")
	private List<SiacTMovgestT> siacTMovgestTs;
	
	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidaziones. */
	@OneToMany(mappedBy="siacDSiopeTipoDebito")
	private List<SiacTLiquidazione> siacTLiquidaziones;
	
	//bi-directional many-to-one association to SiacTOrdinativo
	/** The siac t atto amms. */
	@OneToMany(mappedBy="siacDSiopeTipoDebito")
	private List<SiacTOrdinativo> siacTOrdinativos;
	
	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t atto amms. */
	@OneToMany(mappedBy="siacDSiopeTipoDebito")
	private List<SiacTSubdoc> siacTSubdocs;

	/**
	 * Instantiates a new siac d siope tipo debito.
	 */
	public SiacDSiopeTipoDebito() {
	}

	/**
	 * Gets the siope tipo debito id.
	 * @return the siopeTipoDebitoId
	 */
	public Integer getSiopeTipoDebitoId() {
		return siopeTipoDebitoId;
	}

	/**
	 * Sets the siope tipo debito id.
	 * @param siopeTipoDebitoId the siopeTipoDebitoId to set
	 */
	public void setSiopeTipoDebitoId(Integer siopeTipoDebitoId) {
		this.siopeTipoDebitoId = siopeTipoDebitoId;
	}

	/**
	 * Gets the siope tipo debito code.
	 * @return the siopeTipoDebitoCode
	 */
	public String getSiopeTipoDebitoCode() {
		return siopeTipoDebitoCode;
	}

	/**
	 * Sets the siope tipo debito code.
	 * @param siopeTipoDebitoCode the siopeTipoDebitoCode to set
	 */
	public void setSiopeTipoDebitoCode(String siopeTipoDebitoCode) {
		this.siopeTipoDebitoCode = siopeTipoDebitoCode;
	}

	/**
	 * Gets the siope tipo debito desc.
	 * @return the siopeTipoDebitoDesc
	 */
	public String getSiopeTipoDebitoDesc() {
		return siopeTipoDebitoDesc;
	}

	/**
	 * Sets the siope tipo debito desc.
	 * @param siopeTipoDebitoDesc the siopeTipoDebitoDesc to set
	 */
	public void setSiopeTipoDebitoDesc(String siopeTipoDebitoDesc) {
		this.siopeTipoDebitoDesc = siopeTipoDebitoDesc;
	}
	
	/**
	 * Gets the siope tipo debito desc bnkit.
	 * @return the siopeTipoDebitoDescBnkit
	 */
	public String getSiopeTipoDebitoDescBnkit() {
		return siopeTipoDebitoDescBnkit;
	}

	/**
	 * Sets the siope tipo debito desc bnkit.
	 * @param siopeTipoDebitoDescBnkit the siopeTipoDebitoDescBnkit to set
	 */
	public void setSiopeTipoDebitoDescBnkit(String siopeTipoDebitoDescBnkit) {
		this.siopeTipoDebitoDescBnkit = siopeTipoDebitoDescBnkit;
	}

	/**
	 * Gets the siac t movgest ts.
	 * @return the siacTMovgestTs
	 */
	public List<SiacTMovgestT> getSiacTMovgestTs() {
		return siacTMovgestTs;
	}

	/**
	 * Sets the siac t movgest ts.
	 * @param siacTMovgestTs the siacTMovgestTs to set
	 */
	public void setSiacTMovgestTs(List<SiacTMovgestT> siacTMovgestTs) {
		this.siacTMovgestTs = siacTMovgestTs;
	}
	
	/**
	 * Adds the siac t movgest t.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the siac t movgest t
	 */
	public SiacTMovgestT addSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		getSiacTMovgestTs().add(siacTMovgestT);
		siacTMovgestT.setSiacDSiopeTipoDebito(this);

		return siacTMovgestT;
	}

	/**
	 * Removes the siac t movgest t.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the siac t movgest t
	 */
	public SiacTMovgestT removeSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		getSiacTMovgestTs().remove(siacTMovgestT);
		siacTMovgestT.setSiacDSiopeTipoDebito(null);

		return siacTMovgestT;
	}

	/**
	 * Gets the siac t liquidaziones.
	 * @return the siacTLiquidaziones
	 */
	public List<SiacTLiquidazione> getSiacTLiquidaziones() {
		return siacTLiquidaziones;
	}

	/**
	 * Sets the siac t liquidaziones.
	 * @param siacTLiquidaziones the siacTLiquidaziones to set
	 */
	public void setSiacTLiquidaziones(List<SiacTLiquidazione> siacTLiquidaziones) {
		this.siacTLiquidaziones = siacTLiquidaziones;
	}
	
	/**
	 * Adds the siac t liquidazione.
	 *
	 * @param siacTMovgestT the siac t liquidazione
	 * @return the siac t liquidazione
	 */
	public SiacTLiquidazione addSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		getSiacTLiquidaziones().add(siacTLiquidazione);
		siacTLiquidazione.setSiacDSiopeTipoDebito(this);

		return siacTLiquidazione;
	}

	/**
	 * Removes the siac t liquidazione
	 *
	 * @param siacTMovgestT the siac t liquidazione
	 * @return the siac t liquidazione
	 */
	public SiacTLiquidazione removeSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		getSiacTLiquidaziones().remove(siacTLiquidazione);
		siacTLiquidazione.setSiacDSiopeTipoDebito(null);

		return siacTLiquidazione;
	}

	/**
	 * Gets the siac t ordinativos.
	 * @return the siacTOrdinativos
	 */
	public List<SiacTOrdinativo> getSiacTOrdinativos() {
		return siacTOrdinativos;
	}

	/**
	 * Sets the siac t ordinativos.
	 * @param siacTOrdinativos the siacTOrdinativos to set
	 */
	public void setSiacTOrdinativos(List<SiacTOrdinativo> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}
	
	/**
	 * Adds the siac t ordinativo.
	 *
	 * @param siacTOrdinativo the siac t ordinativo
	 * @return the siac t ordinativo
	 */
	public SiacTOrdinativo addSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacDSiopeTipoDebito(this);

		return siacTOrdinativo;
	}

	/**
	 * Removes the siac t ordinativo.
	 *
	 * @param siacTOrdinativo the siac t ordinativo
	 * @return the siac t ordinativo
	 */
	public SiacTOrdinativo removeSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacDSiopeTipoDebito(null);

		return siacTOrdinativo;
	}

	/**
	 * Gets the siac t subdocs.
	 * @return the siacTSubdocs
	 */
	public List<SiacTSubdoc> getSiacTSubdocs() {
		return siacTSubdocs;
	}

	/**
	 * Sets the siac t subdocs.
	 * @param siacTSubdocs the siacTSubdocs to set
	 */
	public void setSiacTSubdocs(List<SiacTSubdoc> siacTSubdocs) {
		this.siacTSubdocs = siacTSubdocs;
	}
	
	/**
	 * Adds the siac t subdoc.
	 *
	 * @param siacTSubdoc the siac t subdoc
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc addSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		getSiacTSubdocs().add(siacTSubdoc);
		siacTSubdoc.setSiacDSiopeTipoDebito(this);

		return siacTSubdoc;
	}

	/**
	 * Removes the siac t subdoc.
	 *
	 * @param siacTSubdoc the siac t subdoc
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc removeSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		getSiacTSubdocs().remove(siacTSubdoc);
		siacTSubdoc.setSiacDSiopeTipoDebito(null);

		return siacTSubdoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return siopeTipoDebitoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		siopeTipoDebitoId = uid;
	}

}