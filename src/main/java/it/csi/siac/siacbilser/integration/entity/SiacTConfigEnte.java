/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_ente_proprietario database table.
 * 
 */
@Entity
@Table(name="siac_t_config_ente")
public class SiacTConfigEnte extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4440859436193619159L;

	/** The ente proprietario id. */
	@Id
	@SequenceGenerator(name="SIAC_T_CONFIG_ENTE_CONFIGENTEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CONFIG_ENTE_CONFIG_ENTE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CONFIG_ENTE_CONFIGENTEID_GENERATOR")
	@Column(name="config_ente_id")
	private Integer configEnteId;

	/** The codice fiscale. */
	@Column(name="config_ente_valore")
	private String configEnteValore;

	
	
	//bi-directional many-to-one association to SiacDAmbito
	/** The siac d ambitos. */
	@ManyToOne
	@JoinColumn(name="config_tipo_code")
	private SiacDConfigTipo siacDConfigTipo;

	public Integer getConfigEnteId() {
		return configEnteId;
	}

	public void setConfigEnteId(Integer configEnteId) {
		this.configEnteId = configEnteId;
	}

	public String getConfigEnteValore() {
		return configEnteValore;
	}

	public void setConfigEnteValore(String configEnteValore) {
		this.configEnteValore = configEnteValore;
	}
	public SiacDConfigTipo getSiacDConfigTipo() {
		return siacDConfigTipo;
	}

	public void setSiacDConfigTipo(SiacDConfigTipo siacDConfigTipo) {
		this.siacDConfigTipo = siacDConfigTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return configEnteId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.configEnteId = uid;
		
	}

}