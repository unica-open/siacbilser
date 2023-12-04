/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_vincolo database table.
 * 
 */
@Entity
@Table(name="siac_t_vincolo")
@NamedQuery(name="SiacTVincolo.findAll", query="SELECT s FROM SiacTVincolo s")
public class SiacTVincolo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The vincolo id. */
	@Id
	@SequenceGenerator(name="SIAC_T_VINCOLO_VINCOLOID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_VINCOLO_VINCOLO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_VINCOLO_VINCOLOID_GENERATOR")
	@Column(name="vincolo_id")
	private Integer vincoloId;

	/** The vincolo code. */
	@Column(name="vincolo_code")
	private String vincoloCode;

	/** The vincolo desc. */
	@Column(name="vincolo_desc")
	private String vincoloDesc;

	//bi-directional many-to-one association to SiacRVincoloAttr
	/** The siac r vincolo attrs. */
	@OneToMany(mappedBy="siacTVincolo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRVincoloAttr> siacRVincoloAttrs;

	//bi-directional many-to-one association to SiacRVincoloBilElem
	/** The siac r vincolo bil elems. */
	@OneToMany(mappedBy="siacTVincolo")
	private List<SiacRVincoloBilElem> siacRVincoloBilElems;

	//bi-directional many-to-one association to SiacRVincoloGenere
	@OneToMany(mappedBy="siacTVincolo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRVincoloGenere> siacRVincoloGeneres;

	//bi-directional many-to-one association to SiacRVincoloStato
	/** The siac r vincolo statos. */
	@OneToMany(mappedBy="siacTVincolo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRVincoloStato> siacRVincoloStatos;

	//bi-directional many-to-one association to SiacDVincoloTipo
	/** The siac d vincolo tipo. */
	@ManyToOne
	@JoinColumn(name="vincolo_tipo_id")
	private SiacDVincoloTipo siacDVincoloTipo;

	//bi-directional many-to-one association to SiacTPeriodo
	/** The siac t periodo. */
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodo siacTPeriodo;
	
	//SIAC-7129
	//bi-directional many-to-one association to SiacRVincoloRisorseVincolate
	@OneToMany(mappedBy="siacTVincolo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRVincoloRisorseVincolate> siacRVincoloRisorseVincolates;

	/**
	 * Instantiates a new siac t vincolo.
	 */
	public SiacTVincolo() {
	}

	/**
	 * Gets the vincolo id.
	 *
	 * @return the vincolo id
	 */
	public Integer getVincoloId() {
		return this.vincoloId;
	}

	/**
	 * Sets the vincolo id.
	 *
	 * @param vincoloId the new vincolo id
	 */
	public void setVincoloId(Integer vincoloId) {
		this.vincoloId = vincoloId;
	}

	/**
	 * Gets the vincolo code.
	 *
	 * @return the vincolo code
	 */
	public String getVincoloCode() {
		return this.vincoloCode;
	}

	/**
	 * Sets the vincolo code.
	 *
	 * @param vincoloCode the new vincolo code
	 */
	public void setVincoloCode(String vincoloCode) {
		this.vincoloCode = vincoloCode;
	}

	/**
	 * Gets the vincolo desc.
	 *
	 * @return the vincolo desc
	 */
	public String getVincoloDesc() {
		return this.vincoloDesc;
	}

	/**
	 * Sets the vincolo desc.
	 *
	 * @param vincoloDesc the new vincolo desc
	 */
	public void setVincoloDesc(String vincoloDesc) {
		this.vincoloDesc = vincoloDesc;
	}

	/**
	 * Gets the siac r vincolo attrs.
	 *
	 * @return the siac r vincolo attrs
	 */
	public List<SiacRVincoloAttr> getSiacRVincoloAttrs() {
		return this.siacRVincoloAttrs;
	}

	/**
	 * Sets the siac r vincolo attrs.
	 *
	 * @param siacRVincoloAttrs the new siac r vincolo attrs
	 */
	public void setSiacRVincoloAttrs(List<SiacRVincoloAttr> siacRVincoloAttrs) {
		this.siacRVincoloAttrs = siacRVincoloAttrs;
	}

	/**
	 * Adds the siac r vincolo attr.
	 *
	 * @param siacRVincoloAttr the siac r vincolo attr
	 * @return the siac r vincolo attr
	 */
	public SiacRVincoloAttr addSiacRVincoloAttr(SiacRVincoloAttr siacRVincoloAttr) {
		getSiacRVincoloAttrs().add(siacRVincoloAttr);
		siacRVincoloAttr.setSiacTVincolo(this);

		return siacRVincoloAttr;
	}

	/**
	 * Removes the siac r vincolo attr.
	 *
	 * @param siacRVincoloAttr the siac r vincolo attr
	 * @return the siac r vincolo attr
	 */
	public SiacRVincoloAttr removeSiacRVincoloAttr(SiacRVincoloAttr siacRVincoloAttr) {
		getSiacRVincoloAttrs().remove(siacRVincoloAttr);
		siacRVincoloAttr.setSiacTVincolo(null);

		return siacRVincoloAttr;
	}

	/**
	 * Gets the siac r vincolo bil elems.
	 *
	 * @return the siac r vincolo bil elems
	 */
	public List<SiacRVincoloBilElem> getSiacRVincoloBilElems() {
		return this.siacRVincoloBilElems;
	}

	/**
	 * Sets the siac r vincolo bil elems.
	 *
	 * @param siacRVincoloBilElems the new siac r vincolo bil elems
	 */
	public void setSiacRVincoloBilElems(List<SiacRVincoloBilElem> siacRVincoloBilElems) {
		this.siacRVincoloBilElems = siacRVincoloBilElems;
	}

	/**
	 * Adds the siac r vincolo bil elem.
	 *
	 * @param siacRVincoloBilElem the siac r vincolo bil elem
	 * @return the siac r vincolo bil elem
	 */
	public SiacRVincoloBilElem addSiacRVincoloBilElem(SiacRVincoloBilElem siacRVincoloBilElem) {
		getSiacRVincoloBilElems().add(siacRVincoloBilElem);
		siacRVincoloBilElem.setSiacTVincolo(this);

		return siacRVincoloBilElem;
	}

	/**
	 * Removes the siac r vincolo bil elem.
	 *
	 * @param siacRVincoloBilElem the siac r vincolo bil elem
	 * @return the siac r vincolo bil elem
	 */
	public SiacRVincoloBilElem removeSiacRVincoloBilElem(SiacRVincoloBilElem siacRVincoloBilElem) {
		getSiacRVincoloBilElems().remove(siacRVincoloBilElem);
		siacRVincoloBilElem.setSiacTVincolo(null);

		return siacRVincoloBilElem;
	}

	public List<SiacRVincoloGenere> getSiacRVincoloGeneres() {
		return this.siacRVincoloGeneres;
	}

	public void setSiacRVincoloGeneres(List<SiacRVincoloGenere> siacRVincoloGeneres) {
		this.siacRVincoloGeneres = siacRVincoloGeneres;
	}

	public SiacRVincoloGenere addSiacRVincoloGenere(SiacRVincoloGenere siacRVincoloGenere) {
		getSiacRVincoloGeneres().add(siacRVincoloGenere);
		siacRVincoloGenere.setSiacTVincolo(this);

		return siacRVincoloGenere;
	}

	public SiacRVincoloGenere removeSiacRVincoloGenere(SiacRVincoloGenere siacRVincoloGenere) {
		getSiacRVincoloGeneres().remove(siacRVincoloGenere);
		siacRVincoloGenere.setSiacTVincolo(null);

		return siacRVincoloGenere;
	}

	/**
	 * Gets the siac r vincolo statos.
	 *
	 * @return the siac r vincolo statos
	 */
	public List<SiacRVincoloStato> getSiacRVincoloStatos() {
		return this.siacRVincoloStatos;
	}

	/**
	 * Sets the siac r vincolo statos.
	 *
	 * @param siacRVincoloStatos the new siac r vincolo statos
	 */
	public void setSiacRVincoloStatos(List<SiacRVincoloStato> siacRVincoloStatos) {
		this.siacRVincoloStatos = siacRVincoloStatos;
	}

	/**
	 * Adds the siac r vincolo stato.
	 *
	 * @param siacRVincoloStato the siac r vincolo stato
	 * @return the siac r vincolo stato
	 */
	public SiacRVincoloStato addSiacRVincoloStato(SiacRVincoloStato siacRVincoloStato) {
		getSiacRVincoloStatos().add(siacRVincoloStato);
		siacRVincoloStato.setSiacTVincolo(this);

		return siacRVincoloStato;
	}

	/**
	 * Removes the siac r vincolo stato.
	 *
	 * @param siacRVincoloStato the siac r vincolo stato
	 * @return the siac r vincolo stato
	 */
	public SiacRVincoloStato removeSiacRVincoloStato(SiacRVincoloStato siacRVincoloStato) {
		getSiacRVincoloStatos().remove(siacRVincoloStato);
		siacRVincoloStato.setSiacTVincolo(null);

		return siacRVincoloStato;
	}

	/**
	 * Gets the siac d vincolo tipo.
	 *
	 * @return the siac d vincolo tipo
	 */
	public SiacDVincoloTipo getSiacDVincoloTipo() {
		return this.siacDVincoloTipo;
	}

	/**
	 * Sets the siac d vincolo tipo.
	 *
	 * @param siacDVincoloTipo the new siac d vincolo tipo
	 */
	public void setSiacDVincoloTipo(SiacDVincoloTipo siacDVincoloTipo) {
		this.siacDVincoloTipo = siacDVincoloTipo;
	}

	/**
	 * Gets the siac t periodo.
	 *
	 * @return the siac t periodo
	 */
	public SiacTPeriodo getSiacTPeriodo() {
		return this.siacTPeriodo;
	}

	/**
	 * Sets the siac t periodo.
	 *
	 * @param siacTPeriodo the new siac t periodo
	 */
	public void setSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return vincoloId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.vincoloId = uid;
	}

	/**
	 * @return the siacDVincoloRisorseVincolate
	 */
	public List<SiacRVincoloRisorseVincolate> getSiacRVincoloRisorseVincolates() {
		return siacRVincoloRisorseVincolates;
	}

	/**
	 * @param siacDVincoloRisorseVincolate the siacDVincoloRisorseVincolate to set
	 */
	public void setSiacRVincoloRisorseVincolates(List<SiacRVincoloRisorseVincolate> siacRVincoloRisorseVincolates) {
		this.siacRVincoloRisorseVincolates = siacRVincoloRisorseVincolates;
	}

}