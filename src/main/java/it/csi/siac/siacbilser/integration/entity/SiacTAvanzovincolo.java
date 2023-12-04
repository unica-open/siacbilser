/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//SIAC-7930
import it.csi.siac.siacbilser.integration.entity.SiacTEnteBase;


/**
 * The persistent class for the siac_t_azione database table.
 * 
 */
@Entity
@Table(name="siac_t_avanzovincolo")
public class SiacTAvanzovincolo extends SiacTEnteBase implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="avav_id")
	private Integer avavId;

	//bi-directional many-to-one association to SiacDAvanzovincoloTipo
	@ManyToOne
	@JoinColumn(name="avav_tipo_id")
	private SiacDAvanzovincoloTipo siacDAvanzovincoloTipo;
	
	@Column(name="avav_importo_massimale")
	private BigDecimal avavImportoMassimale;
	
	
	public SiacDAvanzovincoloTipo getSiacDAvanzovincoloTipo() {
		return siacDAvanzovincoloTipo;
	}

	public void setSiacDAvanzovincoloTipo(
			SiacDAvanzovincoloTipo siacDAvanzovincoloTipo) {
		this.siacDAvanzovincoloTipo = siacDAvanzovincoloTipo;
	}

	public BigDecimal getAvavImportoMassimale() {
		return avavImportoMassimale;
	}

	public void setAvavImportoMassimale(BigDecimal avavImportoMassimale) {
		this.avavImportoMassimale = avavImportoMassimale;
	}

	public Integer getAvavId() {
		return avavId;
	}

	public void setAvavId(Integer avavId) {
		this.avavId = avavId;
	}

	@Override
	public Integer getUid() {
		return getAvavId();
	}

	@Override
	public void setUid(Integer uid) {
		setAvavId(uid);
	}


}