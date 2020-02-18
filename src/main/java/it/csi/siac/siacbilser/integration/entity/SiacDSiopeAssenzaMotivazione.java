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
 * The persistent class for the siac_d_siope_assenza_motivazione database table.
 * 
 */
@Entity
@Table(name="siac_d_siope_assenza_motivazione")
@NamedQuery(name="SiacDSiopeAssenzaMotivazione.findAll", query="SELECT s FROM SiacDSiopeAssenzaMotivazione s")
public class SiacDSiopeAssenzaMotivazione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The siope assenza motivazione id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SIOPE_ASSENZA_MOTIVAZIONE_SIOPEASSENZAMOTIVAZIONEID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SIOPE_ASSENZA_MOTIVAZIONE_SIOPE_ASSENZA_MOTIVAZIONE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SIOPE_ASSENZA_MOTIVAZIONE_SIOPEASSENZAMOTIVAZIONEID_GENERATOR")
	@Column(name="siope_assenza_motivazione_id")
	private Integer siopeAssenzaMotivazioneId;

	/** The siope assenza motivazione code. */
	@Column(name="siope_assenza_motivazione_code")
	private String siopeAssenzaMotivazioneCode;

	/** The siope assenza motivazione desc. */
	@Column(name="siope_assenza_motivazione_desc")
	private String siopeAssenzaMotivazioneDesc;

	/** The siope assenza motivazione desc bnkit. */
	@Column(name="siope_assenza_motivazione_desc_bnkit")
	private String siopeAssenzaMotivazioneDescBnkit;
	
	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest ts. */
	@OneToMany(mappedBy="siacDSiopeAssenzaMotivazione")
	private List<SiacTMovgestT> siacTMovgestTs;
	
	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidaziones. */
	@OneToMany(mappedBy="siacDSiopeAssenzaMotivazione")
	private List<SiacTLiquidazione> siacTLiquidaziones;
	
	//bi-directional many-to-one association to SiacTOrdinativoT
	/** The siac t atto amms. */
	@OneToMany(mappedBy="siacDSiopeAssenzaMotivazione")
	private List<SiacTOrdinativo> siacTOrdinativos;
	
	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t atto amms. */
	@OneToMany(mappedBy="siacDSiopeAssenzaMotivazione")
	private List<SiacTSubdoc> siacTSubdocs;

	/**
	 * Instantiates a new siac d siope assenza motivazione.
	 */
	public SiacDSiopeAssenzaMotivazione() {
	}

	/**
	 * Gets the siope assenza motivazione id.
	 * @return the siopeAssenzaMotivazioneId
	 */
	public Integer getSiopeAssenzaMotivazioneId() {
		return siopeAssenzaMotivazioneId;
	}

	/**
	 * Sets the siope assenza motivazione id.
	 * @param siopeAssenzaMotivazioneId the siopeAssenzaMotivazioneId to set
	 */
	public void setSiopeAssenzaMotivazioneId(Integer siopeAssenzaMotivazioneId) {
		this.siopeAssenzaMotivazioneId = siopeAssenzaMotivazioneId;
	}

	/**
	 * Gets the siope assenza motivazione code.
	 * @return the siopeAssenzaMotivazioneCode
	 */
	public String getSiopeAssenzaMotivazioneCode() {
		return siopeAssenzaMotivazioneCode;
	}

	/**
	 * Sets the siope assenza motivazione code.
	 * @param siopeAssenzaMotivazioneCode the siopeAssenzaMotivazioneCode to set
	 */
	public void setSiopeAssenzaMotivazioneCode(String siopeAssenzaMotivazioneCode) {
		this.siopeAssenzaMotivazioneCode = siopeAssenzaMotivazioneCode;
	}

	/**
	 * Gets the siope assenza motivazione desc.
	 * @return the siopeAssenzaMotivazioneDesc
	 */
	public String getSiopeAssenzaMotivazioneDesc() {
		return siopeAssenzaMotivazioneDesc;
	}

	/**
	 * Sets the siope assenza motivazione desc.
	 * @param siopeAssenzaMotivazioneDesc the siopeAssenzaMotivazioneDesc to set
	 */
	public void setSiopeAssenzaMotivazioneDesc(String siopeAssenzaMotivazioneDesc) {
		this.siopeAssenzaMotivazioneDesc = siopeAssenzaMotivazioneDesc;
	}

	/**
	 * Gets the siope assenza motivazione desc bnkit.
	 * @return the siopeAssenzaMotivazioneDescBnkit
	 */
	public String getSiopeAssenzaMotivazioneDescBnkit() {
		return siopeAssenzaMotivazioneDescBnkit;
	}

	/**
	 * Sets the siope assenza motivazione desc bnkit.
	 * @param siopeAssenzaMotivazioneDescBnkit the siopeAssenzaMotivazioneDescBnkit to set
	 */
	public void setSiopeAssenzaMotivazioneDescBnkit(String siopeAssenzaMotivazioneDescBnkit) {
		this.siopeAssenzaMotivazioneDescBnkit = siopeAssenzaMotivazioneDescBnkit;
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
		siacTMovgestT.setSiacDSiopeAssenzaMotivazione(this);

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
		siacTMovgestT.setSiacDSiopeAssenzaMotivazione(null);

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
		siacTLiquidazione.setSiacDSiopeAssenzaMotivazione(this);

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
		siacTLiquidazione.setSiacDSiopeAssenzaMotivazione(null);

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
		siacTOrdinativo.setSiacDSiopeAssenzaMotivazione(this);

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
		siacTOrdinativo.setSiacDSiopeAssenzaMotivazione(null);

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
		siacTSubdoc.setSiacDSiopeAssenzaMotivazione(this);

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
		siacTSubdoc.setSiacDSiopeAssenzaMotivazione(null);

		return siacTSubdoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return siopeAssenzaMotivazioneId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		siopeAssenzaMotivazioneId = uid;
	}

}