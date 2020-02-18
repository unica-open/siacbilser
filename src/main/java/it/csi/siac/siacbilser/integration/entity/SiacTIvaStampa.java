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
 * The persistent class for the siac_t_iva_stampa database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_stampa")
@NamedQuery(name="SiacTIvaStampa.findAll", query="SELECT s FROM SiacTIvaStampa s")
public class SiacTIvaStampa extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_IVA_STAMPA_IVASTID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_IVA_STAMPA_IVAST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_IVA_STAMPA_IVASTID_GENERATOR")
	@Column(name="ivast_id")
	private Integer ivastId;


	@Column(name="ivast_anno")
	private Integer ivastAnno;

	@Column(name="ivast_code")
	private String ivastCode;

	@Column(name="ivast_desc")
	private String ivastDesc;


	//bi-directional many-to-one association to SiacRIvaStampaFile
	@OneToMany(mappedBy="siacTIvaStampa",  cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<SiacRIvaStampaFile> siacRIvaStampaFiles;

	//bi-directional many-to-one association to SiacRIvaStampaRegistro
	@OneToMany(mappedBy="siacTIvaStampa", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<SiacRIvaStampaRegistro> siacRIvaStampaRegistros;

	//bi-directional many-to-one association to SiacRIvaStampaStato
	@OneToMany(mappedBy="siacTIvaStampa", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<SiacRIvaStampaStato> siacRIvaStampaStatos;

	//bi-directional many-to-one association to SiacDIvaStampaTipo
	@ManyToOne
	@JoinColumn(name="ivast_tipo_id")
	private SiacDIvaStampaTipo siacDIvaStampaTipo;

	//bi-directional many-to-one association to SiacTPeriodo
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodo siacTPeriodo;

	//bi-directional many-to-one association to SiacTIvaStampaValore
	@OneToMany(mappedBy="siacTIvaStampa", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private List<SiacTIvaStampaValore> siacTIvaStampaValores;

	public SiacTIvaStampa() {
	}

	public Integer getIvastId() {
		return this.ivastId;
	}

	public void setIvastId(Integer ivastId) {
		this.ivastId = ivastId;
	}

	public Integer getIvastAnno() {
		return this.ivastAnno;
	}

	public void setIvastAnno(Integer ivastAnno) {
		this.ivastAnno = ivastAnno;
	}

	public String getIvastCode() {
		return this.ivastCode;
	}

	public void setIvastCode(String ivastCode) {
		this.ivastCode = ivastCode;
	}

	public String getIvastDesc() {
		return this.ivastDesc;
	}

	public void setIvastDesc(String ivastDesc) {
		this.ivastDesc = ivastDesc;
	}



	public List<SiacRIvaStampaFile> getSiacRIvaStampaFiles() {
		return this.siacRIvaStampaFiles;
	}

	public void setSiacRIvaStampaFiles(List<SiacRIvaStampaFile> siacRIvaStampaFiles) {
		this.siacRIvaStampaFiles = siacRIvaStampaFiles;
	}

	public SiacRIvaStampaFile addSiacRIvaStampaFile(SiacRIvaStampaFile siacRIvaStampaFile) {
		getSiacRIvaStampaFiles().add(siacRIvaStampaFile);
		siacRIvaStampaFile.setSiacTIvaStampa(this);

		return siacRIvaStampaFile;
	}

	public SiacRIvaStampaFile removeSiacRIvaStampaFile(SiacRIvaStampaFile siacRIvaStampaFile) {
		getSiacRIvaStampaFiles().remove(siacRIvaStampaFile);
		siacRIvaStampaFile.setSiacTIvaStampa(null);

		return siacRIvaStampaFile;
	}

	public List<SiacRIvaStampaRegistro> getSiacRIvaStampaRegistros() {
		return this.siacRIvaStampaRegistros;
	}

	public void setSiacRIvaStampaRegistros(List<SiacRIvaStampaRegistro> siacRIvaStampaRegistros) {
		this.siacRIvaStampaRegistros = siacRIvaStampaRegistros;
	}

	public SiacRIvaStampaRegistro addSiacRIvaStampaRegistro(SiacRIvaStampaRegistro siacRIvaStampaRegistro) {
		getSiacRIvaStampaRegistros().add(siacRIvaStampaRegistro);
		siacRIvaStampaRegistro.setSiacTIvaStampa(this);

		return siacRIvaStampaRegistro;
	}

	public SiacRIvaStampaRegistro removeSiacRIvaStampaRegistro(SiacRIvaStampaRegistro siacRIvaStampaRegistro) {
		getSiacRIvaStampaRegistros().remove(siacRIvaStampaRegistro);
		siacRIvaStampaRegistro.setSiacTIvaStampa(null);

		return siacRIvaStampaRegistro;
	}

	public List<SiacRIvaStampaStato> getSiacRIvaStampaStatos() {
		return this.siacRIvaStampaStatos;
	}

	public void setSiacRIvaStampaStatos(List<SiacRIvaStampaStato> siacRIvaStampaStatos) {
		this.siacRIvaStampaStatos = siacRIvaStampaStatos;
	}

	public SiacRIvaStampaStato addSiacRIvaStampaStato(SiacRIvaStampaStato siacRIvaStampaStato) {
		getSiacRIvaStampaStatos().add(siacRIvaStampaStato);
		siacRIvaStampaStato.setSiacTIvaStampa(this);

		return siacRIvaStampaStato;
	}

	public SiacRIvaStampaStato removeSiacRIvaStampaStato(SiacRIvaStampaStato siacRIvaStampaStato) {
		getSiacRIvaStampaStatos().remove(siacRIvaStampaStato);
		siacRIvaStampaStato.setSiacTIvaStampa(null);

		return siacRIvaStampaStato;
	}

	public SiacDIvaStampaTipo getSiacDIvaStampaTipo() {
		return this.siacDIvaStampaTipo;
	}

	public void setSiacDIvaStampaTipo(SiacDIvaStampaTipo siacDIvaStampaTipo) {
		this.siacDIvaStampaTipo = siacDIvaStampaTipo;
	}

	public SiacTPeriodo getSiacTPeriodo() {
		return this.siacTPeriodo;
	}

	public void setSiacTPeriodo(SiacTPeriodo siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	public List<SiacTIvaStampaValore> getSiacTIvaStampaValores() {
		return this.siacTIvaStampaValores;
	}

	public void setSiacTIvaStampaValores(List<SiacTIvaStampaValore> siacTIvaStampaValores) {
		this.siacTIvaStampaValores = siacTIvaStampaValores;
	}

	public SiacTIvaStampaValore addSiacTIvaStampaValore(SiacTIvaStampaValore siacTIvaStampaValore) {
		getSiacTIvaStampaValores().add(siacTIvaStampaValore);
		siacTIvaStampaValore.setSiacTIvaStampa(this);

		return siacTIvaStampaValore;
	}

	public SiacTIvaStampaValore removeSiacTIvaStampaValore(SiacTIvaStampaValore siacTIvaStampaValore) {
		getSiacTIvaStampaValores().remove(siacTIvaStampaValore);
		siacTIvaStampaValore.setSiacTIvaStampa(null);

		return siacTIvaStampaValore;
	}

	@Override
	public Integer getUid() {
		return ivastId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ivastId = uid;
	}

}