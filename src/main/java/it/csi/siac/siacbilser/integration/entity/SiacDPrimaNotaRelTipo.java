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
 * The persistent class for the siac_d_relaz_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_prima_nota_rel_tipo")
@NamedQuery(name="SiacDPrimaNotaRelTipo.findAll", query="SELECT s FROM SiacDPrimaNotaRelTipo s")
public class SiacDPrimaNotaRelTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The relaz tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_PRIMA_NOTA_REL_TIPO_PRIMANOTARELTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PRIMA_NOTA_REL_TIPO_PNOTA_REL_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PRIMA_NOTA_REL_TIPO_PRIMANOTARELTIPOID_GENERATOR")
	@Column(name="pnota_rel_tipo_id")
	private Integer pnotaRelTipoId;

	/** The relaz tipo code. */
	@Column(name="pnota_rel_tipo_code")
	private String pnotaRelTipoCode;

	/** The relaz tipo desc. */
	@Column(name="pnota_rel_tipo_desc")
	private String pnotaRelTipoDesc;
	
	/** The relaz tipo desc. */
	@Column(name="pnota_rel_utilizzabile")
	private Boolean pnotaRelUtilizzabile;
	
	//bi-directional many-to-one association to SiacRDoc
	/** The siac r prima notas. */
	@OneToMany(mappedBy="siacDPrimaNotaRelTipo")
	private List<SiacRPrimaNota> siacRPrimaNotas;

	/**
	 * Instantiates a new siac d relaz tipo.
	 */
	public SiacDPrimaNotaRelTipo() {
	}

	/**
	 * @return the pnotaRelTipoId
	 */
	public Integer getPnotaRelTipoId() {
		return pnotaRelTipoId;
	}

	/**
	 * @param pnotaRelTipoId the pnotaRelTipoId to set
	 */
	public void setPnotaRelTipoId(Integer pnotaRelTipoId) {
		this.pnotaRelTipoId = pnotaRelTipoId;
	}

	/**
	 * @return the pnotaRelTipoCode
	 */
	public String getPnotaRelTipoCode() {
		return pnotaRelTipoCode;
	}

	/**
	 * @param pnotaRelTipoCode the pnotaRelTipoCode to set
	 */
	public void setPnotaRelTipoCode(String pnotaRelTipoCode) {
		this.pnotaRelTipoCode = pnotaRelTipoCode;
	}

	/**
	 * @return the pnotaRelTipoDesc
	 */
	public String getPnotaRelTipoDesc() {
		return pnotaRelTipoDesc;
	}

	/**
	 * @param pnotaRelTipoDesc the pnotaRelTipoDesc to set
	 */
	public void setPnotaRelTipoDesc(String pnotaRelTipoDesc) {
		this.pnotaRelTipoDesc = pnotaRelTipoDesc;
	}

	/**
	 * @return the pnotaRelUtilizzabile
	 */
	public Boolean getPnotaRelUtilizzabile() {
		return pnotaRelUtilizzabile;
	}

	/**
	 * @param pnotaRelUtilizzabile the pnotaRelUtilizzabile to set
	 */
	public void setPnotaRelUtilizzabile(Boolean pnotaRelUtilizzabile) {
		this.pnotaRelUtilizzabile = pnotaRelUtilizzabile;
	}

	/**
	 * @return the siacRPrimaNotas
	 */
	public List<SiacRPrimaNota> getSiacRPrimaNotas() {
		return siacRPrimaNotas;
	}

	/**
	 * @param siacRPrimaNotas the siacRPrimaNotas to set
	 */
	public void setSiacRPrimaNotas(List<SiacRPrimaNota> siacRPrimaNotas) {
		this.siacRPrimaNotas = siacRPrimaNotas;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return pnotaRelTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.pnotaRelTipoId = uid;
	}

}