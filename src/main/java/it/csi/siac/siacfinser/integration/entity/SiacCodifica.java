/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

/**
 * Base di tutte le codifiche
 * 
 * @author rmontuori
 * @version @version $Id: $
 */

@Entity
@Table(name = "siac_t_class")
@SecondaryTables({
		@SecondaryTable(name = "siac_r_class_fam_tree", pkJoinColumns = { @PrimaryKeyJoinColumn(name = "classif_id") }),
		@SecondaryTable(name = "siac_r_class_attr", pkJoinColumns = { @PrimaryKeyJoinColumn(name = "classif_id") }) })
public class SiacCodifica extends SiacTEnteBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "classif_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CLASS")
	@SequenceGenerator(name = "SEQ_T_CLASS", sequenceName = "siac_t_class_classif_id_seq")
	private int uid;

	@Basic
	@Column(name = "classif_code")
	private String codice;

	@Basic
	@Column(name = "classif_desc")
	private String descrizione;

	@ManyToOne
	@JoinColumn(name = "classif_tipo_id")
	private SiacTipoClassificatore tipoClassificatore;

	@OneToMany(mappedBy = "codificaFiglia")
	private Set<SiacRClassCodificaFin> codificheFiglie; // mappa la relazione con
														// r_class

	@ManyToOne
	@JoinColumn(name = "attr_id", referencedColumnName = "attr_id", table = "siac_r_class_attr", insertable = false, updatable = false)
	private SiacCodificaAttributo codificaAttributo;

	@ManyToOne
	@JoinColumn(name = "classif_fam_tree_id", referencedColumnName = "classif_fam_tree_id", table = "siac_r_class_fam_tree", insertable = false, updatable = false)
	private SiacCodificaFamiglia codificaFamiglia;

	@ManyToOne
	@JoinColumn(name = "classif_id_padre", referencedColumnName = "classif_id", table = "siac_r_class_fam_tree", insertable = false, updatable = false)
	private SiacCodifica padre;

	@OneToMany(mappedBy = "padre", fetch=FetchType.LAZY)
	@Fetch ( value =  FetchMode . SELECT ) 
	@BatchSize ( size =  20 ) 
	@OrderColumn
	private Set<SiacCodifica> figli;

	public SiacCodificaAttributo getCodificaAttributo() {
		return codificaAttributo;
	}

	public void setCodificaAttributo(SiacCodificaAttributo codificaAttributo) {
		this.codificaAttributo = codificaAttributo;
	}

	public SiacCodificaFamiglia getCodificaFamiglia() {
		return codificaFamiglia;
	}

	public void setCodificaFamiglia(SiacCodificaFamiglia codificaFamiglia) {
		this.codificaFamiglia = codificaFamiglia;
	}

	public SiacCodifica() {
	}

	public SiacCodifica(String codice, String descrizione) {
		this.codice = codice;
		this.descrizione = descrizione;
	}

	@Override
	public Integer getUid() {
		return uid;
	}

	@Override
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public SiacTipoClassificatore getTipoClassificatore() {
		return tipoClassificatore;
	}

	public void setTipoClassificatore(SiacTipoClassificatore tipoClassificatore) {
		this.tipoClassificatore = tipoClassificatore;
	}

	public SiacCodifica getPadre() {
		return padre;
	}

	public void setPadre(SiacCodifica padre) {
		this.padre = padre;
	}

	public Set<SiacCodifica> getFigli() {
		return figli;
	}

	public void setFigli(Set<SiacCodifica> figli) {
		this.figli = figli;
	}

	public void setCodificheFiglie(Set<SiacRClassCodificaFin> codificheFiglie) {
		this.codificheFiglie = codificheFiglie;
	}

	public Set<SiacRClassCodificaFin> getCodificheFiglie() {
		return codificheFiglie;
	}

	
}
