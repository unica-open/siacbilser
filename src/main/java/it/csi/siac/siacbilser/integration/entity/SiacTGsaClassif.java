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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_gsa_classif database table.
 * 
 */
@Entity
@Table(name="siac_t_gsa_classif")
@NamedQuery(name="SiacTGsaClassif.findAll", query="SELECT s FROM SiacTGsaClassif s")
public class SiacTGsaClassif extends SiacTEnteBaseExt {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6602888341482196966L;

	@Id
	@SequenceGenerator(name="SIAC_T_GSA_CLASSIF_GSACLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_GSA_CLASSIF_GSA_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_GSA_CLASSIF_GSACLASSIFID_GENERATOR")
	@Column(name="gsa_classif_id")
	private Integer gsaClassifId;

	private Integer livello;

	@Column(name="gsa_classif_code")
	private String gsaClassifCode;

	@Column(name="gsa_classif_desc")
	private String gsaClassifDesc;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="gsa_classif_id_padre")
	private SiacTGsaClassif siacTGsaClassifPadre;

	//bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;
	
	/** The siac r doc statos. */
	@OneToMany(mappedBy="siacTGsaClassif", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRGsaClassifStato> siacRGsaClassifStatos;
	
	/** The siac r doc statos. */
	@OneToMany(mappedBy="siacTGsaClassifPadre")
	private List<SiacTGsaClassif> siacTGsaClassifFiglios;
	
	/** The siac r gsa classif prima notas. */
	@OneToMany(mappedBy="siacTGsaClassif")
	private List<SiacRGsaClassifPrimaNota> siacRGsaClassifPrimaNotas;
	
	/**
	 * @return the gsaClassifId
	 */
	public Integer getGsaClassifId() {
		return gsaClassifId;
	}

	/**
	 * @param gsaClassifId the gsaClassifId to set
	 */
	public void setGsaClassifId(Integer gsaClassifId) {
		this.gsaClassifId = gsaClassifId;
	}

	/**
	 * @return the livello
	 */
	public Integer getLivello() {
		return livello;
	}

	/**
	 * @param livello the livello to set
	 */
	public void setLivello(Integer livello) {
		this.livello = livello;
	}

	/**
	 * @return the gsaClassifCode
	 */
	public String getGsaClassifCode() {
		return gsaClassifCode;
	}

	/**
	 * @param gsaClassifCode the gsaClassifCode to set
	 */
	public void setGsaClassifCode(String gsaClassifCode) {
		this.gsaClassifCode = gsaClassifCode;
	}

	/**
	 * @return the gsaClassifDesc
	 */
	public String getGsaClassifDesc() {
		return gsaClassifDesc;
	}

	/**
	 * @return the siacTGsaClassif
	 */
	public SiacTGsaClassif getSiacTGsaClassifPadre() {
		return siacTGsaClassifPadre;
	}

	/**
	 * @param siacTGsaClassif the siacTGsaClassif to set
	 */
	public void setSiacTGsaClassifPadre(SiacTGsaClassif siacTGsaClassif) {
		this.siacTGsaClassifPadre = siacTGsaClassif;
	}

	/**
	 * @return the siacDAmbito
	 */
	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	/**
	 * @param siacDAmbito the siacDAmbito to set
	 */
	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}

	/**
	 * @param gsaClassifDesc the gsaClassifDesc to set
	 */
	public void setGsaClassifDesc(String gsaClassifDesc) {
		this.gsaClassifDesc = gsaClassifDesc;
	}

	/**
	 * Gets the siac r doc statos.
	 *
	 * @return the siac r doc statos
	 */
	public List<SiacRGsaClassifStato> getSiacRGsaClassifStatos() {
		return this.siacRGsaClassifStatos;
	}

	/**
	 * Sets the siac r doc statos.
	 *
	 * @param siacRGsaClassifStatos the new siac r doc statos
	 */
	public void setSiacRGsaClassifStatos(List<SiacRGsaClassifStato> siacRGsaClassifStatos) {
		this.siacRGsaClassifStatos = siacRGsaClassifStatos;
	}

	/**
	 * Adds the siac r gsa classif stato.
	 *
	 * @param siacRGsaClassifStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRGsaClassifStato addSiacRGsaClassifStato(SiacRGsaClassifStato siacRGsaClassifStato) {
		getSiacRGsaClassifStatos().add(siacRGsaClassifStato);
		siacRGsaClassifStato.setSiacTGsaClassif(this);

		return siacRGsaClassifStato;
	}

	/**
	 * Removes the iac r gsa classif stato.
	 *
	 * @param siacRGsaClassifStato the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacRGsaClassifStato removeSiacRGsaClassifStato(SiacRGsaClassifStato siacRGsaClassifStato) {
		getSiacRGsaClassifStatos().remove(siacRGsaClassifStato);
		siacRGsaClassifStato.setSiacTGsaClassif(null);

		return siacRGsaClassifStato;
	}
	
	/**
	 * Gets the siac r doc statos.
	 *
	 * @return the siac r doc statos
	 */
	public List<SiacTGsaClassif> getSiacTGsaClassifFiglios() {
		return this.siacTGsaClassifFiglios;
	}

	/**
	 * Sets the siac r doc statos.
	 *
	 * @param siacTGsaClassifFiglios the new siac r doc statos
	 */
	public void setSiacTGsaClassifFiglios(List<SiacTGsaClassif> siacTGsaClassifFiglios) {
		this.siacTGsaClassifFiglios = siacTGsaClassifFiglios;
	}

	/**
	 * Adds the siac r gsa classif stato.
	 *
	 * @param siacTGsaClassifFiglio the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacTGsaClassif addSiacTGsaClassifFiglio(SiacTGsaClassif siacTGsaClassifFiglio) {
		getSiacTGsaClassifFiglios().add(siacTGsaClassifFiglio);
		siacTGsaClassifFiglio.setSiacTGsaClassifPadre(this);

		return siacTGsaClassifFiglio;
	}

	/**
	 * Removes the iac r gsa classif stato.
	 *
	 * @param siacTGsaClassifFiglio the siac r doc stato
	 * @return the siac r doc stato
	 */
	public SiacTGsaClassif removeSiacTGsaClassifFiglio(SiacTGsaClassif siacTGsaClassifFiglio) {
		getSiacTGsaClassifFiglios().remove(siacTGsaClassifFiglio);
		siacTGsaClassifFiglio.setSiacTGsaClassifPadre(null);

		return siacTGsaClassifFiglio;
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
		siacRGsaClassifPrimaNota.setSiacTGsaClassif(this);

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
		siacRGsaClassifPrimaNota.setSiacTGsaClassif(null);

		return siacRGsaClassifPrimaNota;
	}

	@Override
	public Integer getUid() {
		return gsaClassifId;
	}

	@Override
	public void setUid(Integer uid) {
		this.gsaClassifId = uid;
	}

}