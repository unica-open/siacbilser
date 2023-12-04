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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_doc_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_collegamento_tipo")
@NamedQuery(name="SiacDCollegamentoTipo.findAll", query="SELECT s FROM SiacDCollegamentoTipo s")
public class SiacDCollegamentoTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The collegamento tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_COLLEGAMENTO_TIPO_DOCTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_COLLEGAMENTO_TIPO_COLLEGAMENTO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_COLLEGAMENTO_TIPO_DOCTIPOID_GENERATOR")
	@Column(name="collegamento_tipo_id")
	private Integer collegamentoTipoId;	

	/** The collegamento tipo code. */
	@Column(name="collegamento_tipo_code")
	private String collegamentoTipoCode;

	/** The collegamento tipo desc. */
	@Column(name="collegamento_tipo_desc")
	private String collegamentoTipoDesc;

	//bi-directional many-to-one association to SiacTDoc
	/** The siac d eventos. */
	@OneToMany(mappedBy="siacDCollegamentoTipo")
	private List<SiacDEvento> siacDEventos;


	
	public SiacDCollegamentoTipo() {
	}
	/**
	 * @return the collegamentoTipoId
	 */
	public Integer getCollegamentoTipoId() {
		return collegamentoTipoId;
	}
	/**
	 * @param collegamentoTipoId the collegamentoTipoId to set
	 */
	public void setCollegamentoTipoId(Integer collegamentoTipoId) {
		this.collegamentoTipoId = collegamentoTipoId;
	}
	/**
	 * @return the collegamentoTipoCode
	 */
	public String getCollegamentoTipoCode() {
		return collegamentoTipoCode;
	}
	/**
	 * @param collegamentoTipoCode the collegamentoTipoCode to set
	 */
	public void setCollegamentoTipoCode(String collegamentoTipoCode) {
		this.collegamentoTipoCode = collegamentoTipoCode;
	}
	/**
	 * @return the collegamentoTipoDesc
	 */
	public String getCollegamentoTipoDesc() {
		return collegamentoTipoDesc;
	}
	/**
	 * @param collegamentoTipoDesc the collegamentoTipoDesc to set
	 */
	public void setCollegamentoTipoDesc(String collegamentoTipoDesc) {
		this.collegamentoTipoDesc = collegamentoTipoDesc;
	}
	/**
	 * @return the siacDEventos
	 */
	public List<SiacDEvento> getSiacDEventos() {
		return siacDEventos;
	}
	/**
	 * @param siacDEventos the siacDEventos to set
	 */
	public void setSiacDEventos(List<SiacDEvento> siacDEventos) {
		this.siacDEventos = siacDEventos;
	}
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return collegamentoTipoId;
	}
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.collegamentoTipoId = uid;		
	}

}