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
 * The persistent class for the siac_d_giustificativo_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_giustificativo_tipo")
@NamedQuery(name="SiacDGiustificativoTipo.findAll", query="SELECT s FROM SiacDGiustificativoTipo s")
public class SiacDGiustificativoTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_GIUSTIFICATIVO_TIPO_GIUSTTIPOID_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_GIUSTIFICATIVO_TIPO_GIUST_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_GIUSTIFICATIVO_TIPO_GIUSTTIPOID_GENERATOR")
	@Column(name="giust_tipo_id")
	private Integer giustTipoId;

	@Column(name="giust_tipo_code")
	private String giustTipoCode;

	@Column(name="giust_tipo_desc")
	private String giustTipoDesc;

	//bi-directional many-to-one association to SiacDGiustificativo
	@OneToMany(mappedBy="siacDGiustificativoTipo")
	private List<SiacDGiustificativo> siacDGiustificativos;

	public SiacDGiustificativoTipo() {
	}

	public Integer getGiustTipoId() {
		return this.giustTipoId;
	}

	public void setGiustTipoId(Integer giustTipoId) {
		this.giustTipoId = giustTipoId;
	}

	public String getGiustTipoCode() {
		return this.giustTipoCode;
	}

	public void setGiustTipoCode(String giustTipoCode) {
		this.giustTipoCode = giustTipoCode;
	}

	public String getGiustTipoDesc() {
		return this.giustTipoDesc;
	}

	public void setGiustTipoDesc(String giustTipoDesc) {
		this.giustTipoDesc = giustTipoDesc;
	}

	public List<SiacDGiustificativo> getSiacDGiustificativos() {
		return this.siacDGiustificativos;
	}

	public void setSiacDGiustificativos(List<SiacDGiustificativo> siacDGiustificativos) {
		this.siacDGiustificativos = siacDGiustificativos;
	}

	public SiacDGiustificativo addSiacDGiustificativo(SiacDGiustificativo siacDGiustificativo) {
		getSiacDGiustificativos().add(siacDGiustificativo);
		siacDGiustificativo.setSiacDGiustificativoTipo(this);

		return siacDGiustificativo;
	}

	public SiacDGiustificativo removeSiacDGiustificativo(SiacDGiustificativo siacDGiustificativo) {
		getSiacDGiustificativos().remove(siacDGiustificativo);
		siacDGiustificativo.setSiacDGiustificativoTipo(null);

		return siacDGiustificativo;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return giustTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.giustTipoId = uid;
		
	}

}