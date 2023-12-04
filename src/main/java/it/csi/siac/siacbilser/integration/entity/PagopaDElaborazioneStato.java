/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "pagopa_d_elaborazione_stato")
public class PagopaDElaborazioneStato extends SiacTEnteBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PAGOPA_D_ELABORAZIONESTATOPAGOPA_ELAB_STATO_ID_GENERATOR", allocationSize = 1, sequenceName = "PAGOPA_D_ELABORAZIONE_STATO_PAGOPA_ELAB_STATO_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGOPA_D_ELABORAZIONESTATOPAGOPA_ELAB_STATO_ID_GENERATOR")
	@Column(name = "pagopa_elab_stato_id")
	private Integer pagopaElabStatoId;
	
	
	
	@Column(name = "pagopa_elab_stato_code")
	private String pagopaElabStatoCode;
	
	@Column(name = "pagopa_elab_stato_desc")
	private String pagopaElabStatoDesc;
	 
	public PagopaDElaborazioneStato() {
	}

	@Override
	public Integer getUid() {
		return pagopaElabStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pagopaElabStatoId = uid;
	}

	/**
	 * @return the pagopaElabStatoId
	 */
	public Integer getPagopaElabStatoId()
	{
		return pagopaElabStatoId;
	}

	/**
	 * @param pagopaElabStatoId the pagopaElabStatoId to set
	 */
	public void setPagopaElabStatoId(Integer pagopaElabStatoId)
	{
		this.pagopaElabStatoId = pagopaElabStatoId;
	}

	/**
	 * @return the pagopaElabStatoCode
	 */
	public String getPagopaElabStatoCode()
	{
		return pagopaElabStatoCode;
	}

	/**
	 * @param pagopaElabStatoCode the pagopaElabStatoCode to set
	 */
	public void setPagopaElabStatoCode(String pagopaElabStatoCode)
	{
		this.pagopaElabStatoCode = pagopaElabStatoCode;
	}

	/**
	 * @return the pagopaElabStatoDesc
	 */
	public String getPagopaElabStatoDesc()
	{
		return pagopaElabStatoDesc;
	}

	/**
	 * @param pagopaElabStatoDesc the pagopaElabStatoDesc to set
	 */
	public void setPagopaElabStatoDesc(String pagopaElabStatoDesc)
	{
		this.pagopaElabStatoDesc = pagopaElabStatoDesc;
	}
	
	 
}