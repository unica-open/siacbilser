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
 * The persistent class for the siac_d_prov_cassa_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_prov_cassa_tipo")
@NamedQuery(name="SiacDProvCassaTipo.findAll", query="SELECT s FROM SiacDProvCassaTipo s")
public class SiacDProvCassaTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The provc tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_PROV_CASSA_TIPO_PROVCTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PROV_CASSA_TIPO_PROVC_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PROV_CASSA_TIPO_PROVCTIPOID_GENERATOR")
	@Column(name="provc_tipo_id")
	private Integer provcTipoId;

	

	/** The provc tipo code. */
	@Column(name="provc_tipo_code")
	private String provcTipoCode;

	/** The provc tipo desc. */
	@Column(name="provc_tipo_desc")
	private String provcTipoDesc;

	



	//bi-directional many-to-one association to SiacTProvCassa
	/** The siac t prov cassas. */
	@OneToMany(mappedBy="siacDProvCassaTipo")
	private List<SiacTProvCassa> siacTProvCassas;

	/**
	 * Instantiates a new siac d prov cassa tipo.
	 */
	public SiacDProvCassaTipo() {
	}

	/**
	 * Gets the provc tipo id.
	 *
	 * @return the provc tipo id
	 */
	public Integer getProvcTipoId() {
		return this.provcTipoId;
	}

	/**
	 * Sets the provc tipo id.
	 *
	 * @param provcTipoId the new provc tipo id
	 */
	public void setProvcTipoId(Integer provcTipoId) {
		this.provcTipoId = provcTipoId;
	}

	


	
	/**
	 * Gets the provc tipo code.
	 *
	 * @return the provc tipo code
	 */
	public String getProvcTipoCode() {
		return this.provcTipoCode;
	}

	/**
	 * Sets the provc tipo code.
	 *
	 * @param provcTipoCode the new provc tipo code
	 */
	public void setProvcTipoCode(String provcTipoCode) {
		this.provcTipoCode = provcTipoCode;
	}

	/**
	 * Gets the provc tipo desc.
	 *
	 * @return the provc tipo desc
	 */
	public String getProvcTipoDesc() {
		return this.provcTipoDesc;
	}

	/**
	 * Sets the provc tipo desc.
	 *
	 * @param provcTipoDesc the new provc tipo desc
	 */
	public void setProvcTipoDesc(String provcTipoDesc) {
		this.provcTipoDesc = provcTipoDesc;
	}

	

	

	/**
	 * Gets the siac t prov cassas.
	 *
	 * @return the siac t prov cassas
	 */
	public List<SiacTProvCassa> getSiacTProvCassas() {
		return this.siacTProvCassas;
	}

	/**
	 * Sets the siac t prov cassas.
	 *
	 * @param siacTProvCassas the new siac t prov cassas
	 */
	public void setSiacTProvCassas(List<SiacTProvCassa> siacTProvCassas) {
		this.siacTProvCassas = siacTProvCassas;
	}

	/**
	 * Adds the siac t prov cassa.
	 *
	 * @param siacTProvCassa the siac t prov cassa
	 * @return the siac t prov cassa
	 */
	public SiacTProvCassa addSiacTProvCassa(SiacTProvCassa siacTProvCassa) {
		getSiacTProvCassas().add(siacTProvCassa);
		siacTProvCassa.setSiacDProvCassaTipo(this);

		return siacTProvCassa;
	}

	/**
	 * Removes the siac t prov cassa.
	 *
	 * @param siacTProvCassa the siac t prov cassa
	 * @return the siac t prov cassa
	 */
	public SiacTProvCassa removeSiacTProvCassa(SiacTProvCassa siacTProvCassa) {
		getSiacTProvCassas().remove(siacTProvCassa);
		siacTProvCassa.setSiacDProvCassaTipo(null);

		return siacTProvCassa;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return provcTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.provcTipoId = uid;
	}

}