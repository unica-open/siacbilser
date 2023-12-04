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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_distinta database table.
 * 
 */
@Entity
@Table(name="siac_d_distinta")
@NamedQuery(name="SiacDDistinta.findAll", query="SELECT s FROM SiacDDistinta s")
public class SiacDDistinta extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The dist id. */
	@Id
	@SequenceGenerator(name="SIAC_D_DISTINTA_DISTID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_DISTINTA_DIST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_DISTINTA_DISTID_GENERATOR")
	@Column(name="dist_id")
	private Integer distId;

	/** The dist code. */
	@Column(name="dist_code")
	private String distCode;

	/** The dist desc. */
	@Column(name="dist_desc")
	private String distDesc;	
	//bi-directional many-to-one association to SiacDDistintaTipo
	@ManyToOne
	@JoinColumn(name="dist_tipo_id")
	private SiacDDistintaTipo siacDDistintaTipo;

	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidaziones. */
	@OneToMany(mappedBy="siacDDistinta")
	private List<SiacTLiquidazione> siacTLiquidaziones;

	//bi-directional many-to-one association to SiacTOrdinativo
	@OneToMany(mappedBy="siacDDistinta")
	private List<SiacTOrdinativo> siacTOrdinativos;
	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdocs. */
	@OneToMany(mappedBy="siacDDistinta")
	private List<SiacTSubdoc> siacTSubdocs;
	// bi-directional many-to-one association to SiacDCausale
	/** The siac d causales. */
	@OneToMany(mappedBy="siacDDistinta")
	private List<SiacDCausale> siacDCausales;

	/**
	 * Instantiates a new siac d distinta.
	 */
	public SiacDDistinta() {
	}

	/**
	 * Gets the dist id.
	 *
	 * @return the dist id
	 */
	public Integer getDistId() {
		return this.distId;
	}

	/**
	 * Sets the dist id.
	 *
	 * @param distId the new dist id
	 */
	public void setDistId(Integer distId) {
		this.distId = distId;
	}
	
	/**
	 * Gets the dist code.
	 *
	 * @return the dist code
	 */
	public String getDistCode() {
		return this.distCode;
	}

	/**
	 * Sets the dist code.
	 *
	 * @param distCode the new dist code
	 */
	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	/**
	 * Gets the dist desc.
	 *
	 * @return the dist desc
	 */
	public String getDistDesc() {
		return this.distDesc;
	}

	/**
	 * Sets the dist desc.
	 *
	 * @param distDesc the new dist desc
	 */
	public void setDistDesc(String distDesc) {
		this.distDesc = distDesc;
	}

	/**
	 * Gets the siac t liquidaziones.
	 *
	 * @return the siac t liquidaziones
	 */
	public SiacDDistintaTipo getSiacDDistintaTipo() {
		return this.siacDDistintaTipo;
	}

	public void setSiacDDistintaTipo(SiacDDistintaTipo siacDDistintaTipo) {
		this.siacDDistintaTipo = siacDDistintaTipo;
	}

	public List<SiacTLiquidazione> getSiacTLiquidaziones() {
		return this.siacTLiquidaziones;
	}

	/**
	 * Sets the siac t liquidaziones.
	 *
	 * @param siacTLiquidaziones the new siac t liquidaziones
	 */
	public void setSiacTLiquidaziones(List<SiacTLiquidazione> siacTLiquidaziones) {
		this.siacTLiquidaziones = siacTLiquidaziones;
	}

	/**
	 * Adds the siac t liquidazione.
	 *
	 * @param siacTLiquidazione the siac t liquidazione
	 * @return the siac t liquidazione
	 */
	public SiacTLiquidazione addSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		getSiacTLiquidaziones().add(siacTLiquidazione);
		siacTLiquidazione.setSiacDDistinta(this);

		return siacTLiquidazione;
	}

	/**
	 * Removes the siac t liquidazione.
	 *
	 * @param siacTLiquidazione the siac t liquidazione
	 * @return the siac t liquidazione
	 */
	public SiacTLiquidazione removeSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		getSiacTLiquidaziones().remove(siacTLiquidazione);
		siacTLiquidazione.setSiacDDistinta(null);

		return siacTLiquidazione;
	}

	public List<SiacTOrdinativo> getSiacTOrdinativos() {
		return this.siacTOrdinativos;
	}

	public void setSiacTOrdinativos(List<SiacTOrdinativo> siacTOrdinativos) {
		this.siacTOrdinativos = siacTOrdinativos;
	}

	public SiacTOrdinativo addSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().add(siacTOrdinativo);
		siacTOrdinativo.setSiacDDistinta(this);

		return siacTOrdinativo;
	}

	public SiacTOrdinativo removeSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		getSiacTOrdinativos().remove(siacTOrdinativo);
		siacTOrdinativo.setSiacDDistinta(null);

		return siacTOrdinativo;
	}

	/**
	 * Gets the siac t subdocs.
	 *
	 * @return the siac t subdocs
	 */
	public List<SiacTSubdoc> getSiacTSubdocs() {
		return this.siacTSubdocs;
	}

	/**
	 * Sets the siac t subdocs.
	 *
	 * @param siacTSubdocs the new siac t subdocs
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
		siacTSubdoc.setSiacDDistinta(this);

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
		siacTSubdoc.setSiacDDistinta(null);

		return siacTSubdoc;
	}
	
	/**
	 * Gets the siac d causales.
	 *
	 * @return the siac d causales
	 */
	public List<SiacDCausale> getSiacDCausales() {
		return this.siacDCausales;
	}

	/**
	 * Sets the siac d causales.
	 *
	 * @param siacDCausales the new siac d causales
	 */
	public void setSiacDCausales(List<SiacDCausale> siacDCausales) {
		this.siacDCausales = siacDCausales;
	}

	/**
	 * Adds the siac d causale.
	 *
	 * @param siacDCausale the siac d causale
	 * @return the siac d causale
	 */
	public SiacDCausale addSiacDCausale(SiacDCausale siacDCausale) {
		getSiacDCausales().add(siacDCausale);
		siacDCausale.setSiacDDistinta(this);

		return siacDCausale;
	}

	/**
	 * Removes the siac d causale.
	 *
	 * @param siacDCausale the siac d causale
	 * @return the siac d causale
	 */
	public SiacDCausale removeSiacDCausale(SiacDCausale siacDCausale) {
		getSiacDCausales().remove(siacDCausale);
		siacDCausale.setSiacDDistinta(null);

		return siacDCausale;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return distId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.distId = uid;
		
	}

}