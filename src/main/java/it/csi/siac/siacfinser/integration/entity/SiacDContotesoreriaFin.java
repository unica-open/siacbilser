/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_contotesoreria database table.
 * 
 */
@Entity
@Table(name="siac_d_contotesoreria")
public class SiacDContotesoreriaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_CONTOTESORERIA_CONTOTES_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_contotesoreria_contotes_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CONTOTESORERIA_CONTOTES_ID_GENERATOR")
	@Column(name="contotes_id")
	private Integer contotesId;

	@Column(name="contotes_code")
	private String contotesCode;

	@Column(name="contotes_desc")
	private String contotesDesc;

	//bi-directional many-to-one association to SiacTLiquidazioneFin
	@OneToMany(mappedBy="siacDContotesoreria")
	private List<SiacTLiquidazioneFin> siacTLiquidaziones;

	public SiacDContotesoreriaFin() {
	}

	public Integer getContotesId() {
		return this.contotesId;
	}

	public void setContotesId(Integer contotesId) {
		this.contotesId = contotesId;
	}

	public String getContotesCode() {
		return this.contotesCode;
	}

	public void setContotesCode(String contotesCode) {
		this.contotesCode = contotesCode;
	}

	public String getContotesDesc() {
		return this.contotesDesc;
	}

	public void setContotesDesc(String contotesDesc) {
		this.contotesDesc = contotesDesc;
	}

	public List<SiacTLiquidazioneFin> getSiacTLiquidaziones() {
		return this.siacTLiquidaziones;
	}

	public void setSiacTLiquidaziones(List<SiacTLiquidazioneFin> siacTLiquidaziones) {
		this.siacTLiquidaziones = siacTLiquidaziones;
	}

	public SiacTLiquidazioneFin addSiacTLiquidazione(SiacTLiquidazioneFin siacTLiquidazione) {
		getSiacTLiquidaziones().add(siacTLiquidazione);
		siacTLiquidazione.setSiacDContotesoreria(this);

		return siacTLiquidazione;
	}

	public SiacTLiquidazioneFin removeSiacTLiquidazione(SiacTLiquidazioneFin siacTLiquidazione) {
		getSiacTLiquidaziones().remove(siacTLiquidazione);
		siacTLiquidazione.setSiacDContotesoreria(null);

		return siacTLiquidazione;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return contotesId;
	}

	@Override
	public void setUid(Integer uid) {
		this.contotesId = uid;
		
	}

}