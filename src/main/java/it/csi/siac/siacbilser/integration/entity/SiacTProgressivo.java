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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_t_progressivo database table.
 * 
 */
@Entity
@Table(name="siac_t_progressivo")
@NamedQuery(name="SiacTProgressivo.findAll", query="SELECT s FROM SiacTProgressivo s")
public class SiacTProgressivo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PROGRESSIVO_PROGID_GENERATOR", allocationSize = 1, sequenceName="SIAC_T_PROGRESSIVO_PROG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PROGRESSIVO_PROGID_GENERATOR")
	@Column(name="prog_id")
	private Integer progId;

	@Column(name="prog_key")
	private String progKey;

	@Column(name="prog_value")
	private Integer progValue;

	//bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;

	public SiacTProgressivo() {
	}

	public Integer getProgId() {
		return this.progId;
	}

	public void setProgId(Integer progId) {
		this.progId = progId;
	}

	public String getProgKey() {
		return this.progKey;
	}

	public void setProgKey(String progKey) {
		this.progKey = progKey;
	}

	public Integer getProgValue() {
		return this.progValue;
	}

	public void setProgValue(Integer progValue) {
		this.progValue = progValue;
	}

	public SiacDAmbito getSiacDAmbito() {
		return this.siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}

	@Override
	public Integer getUid() {
		return progId;
	}

	@Override
	public void setUid(Integer uid) {
		progId = uid;
	}

}