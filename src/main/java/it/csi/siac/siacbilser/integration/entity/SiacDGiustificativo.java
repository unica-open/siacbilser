/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacbilser.business.utility.BilUtilities;


/**
 * The persistent class for the siac_d_giustificativo database table.
 * 
 */
@Entity
@Table(name="siac_d_giustificativo")
@NamedQuery(name="SiacDGiustificativo.findAll", query="SELECT s FROM SiacDGiustificativo s")
public class SiacDGiustificativo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_GIUSTIFICATIVO_GIUSTID_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_GIUSTIFICATIVO_GIUST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_GIUSTIFICATIVO_GIUSTID_GENERATOR")
	@Column(name="giust_id")
	private Integer giustId;

	@Column(name="giust_code")
	private String giustCode;

	@Column(name="giust_desc")
	private String giustDesc;
	
	@Column(name="giust_ant_missione_perc")
	private BigDecimal giustAntMissionePerc;
	
	@Column(name="giust_ant_trasferta_perc")
	private BigDecimal giustAntTrasfertaPerc;
	
	// Lotto P
	@Column(name="giust_importo")
	private BigDecimal giustImporto;
	
	//bi-directional many-to-one association to SiacDGiustificativoTipo
	@ManyToOne
	@JoinColumn(name="giust_tipo_id")
	private SiacDGiustificativoTipo siacDGiustificativoTipo;
	
	//bi-directional many-to-one association to SiacRGiustificativoStato
	@OneToMany(mappedBy="siacTGiustificativo")
	private List<SiacTGiustificativoDet> siacTGiustificativoDets;
	
	//bi-directional many-to-one association to SiacRGiustificativoStato
	@OneToMany(mappedBy="siacDGiustificativo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCassaEconGiustificativo> siacRCassaEconGiustificativos;
	
	public SiacDGiustificativo() {
	}
	
	/**
	 * Sets the default values on the first persistence of the entity.
	 */
	@PrePersist
	public void onCreate() {
		// Default values
		if(giustAntMissionePerc == null) {
			giustAntMissionePerc = BilUtilities.BIG_DECIMAL_ONE_HUNDRED;
		}
		if(giustAntTrasfertaPerc == null) {
			giustAntTrasfertaPerc = BilUtilities.BIG_DECIMAL_ONE_HUNDRED;
		}
	}
	
	/**
	 * Sets the default values on the updates of the entity.
	 */
	@PreUpdate
	public void onUpdate() {
		// Default values
		if(giustAntMissionePerc == null) {
			giustAntMissionePerc = BilUtilities.BIG_DECIMAL_ONE_HUNDRED;
		}
		if(giustAntTrasfertaPerc == null) {
			giustAntTrasfertaPerc = BilUtilities.BIG_DECIMAL_ONE_HUNDRED;
		}
	}

	public Integer getGiustId() {
		return this.giustId;
	}

	public void setGiustId(Integer giustId) {
		this.giustId = giustId;
	}

	public String getGiustCode() {
		return this.giustCode;
	}

	public void setGiustCode(String giustCode) {
		this.giustCode = giustCode;
	}

	public String getGiustDesc() {
		return this.giustDesc;
	}

	public void setGiustDesc(String giustDesc) {
		this.giustDesc = giustDesc;
	}

	public BigDecimal getGiustAntMissionePerc() {
		return giustAntMissionePerc;
	}

	public void setGiustAntMissionePerc(BigDecimal giustAntMissionePerc) {
		this.giustAntMissionePerc = giustAntMissionePerc;
	}

	public BigDecimal getGiustAntTrasfertaPerc() {
		return giustAntTrasfertaPerc;
	}

	public void setGiustAntTrasfertaPerc(BigDecimal giustAntTrasfertaPerc) {
		this.giustAntTrasfertaPerc = giustAntTrasfertaPerc;
	}

	public BigDecimal getGiustImporto() {
		return giustImporto;
	}

	public void setGiustImporto(BigDecimal giustImporto) {
		this.giustImporto = giustImporto;
	}

	public SiacDGiustificativoTipo getSiacDGiustificativoTipo() {
		return this.siacDGiustificativoTipo;
	}

	public void setSiacDGiustificativoTipo(SiacDGiustificativoTipo siacDGiustificativoTipo) {
		this.siacDGiustificativoTipo = siacDGiustificativoTipo;
	}
	
	@Override
	public Integer getUid() {
		return giustId;
	}

	@Override
	public void setUid(Integer uid) {
		this.giustId = uid;
		
	}
	
	public List<SiacTGiustificativoDet> getSiacTGiustificativoDets() {
		return this.siacTGiustificativoDets;
	}

	public void setSiacTGiustificativoDets(List<SiacTGiustificativoDet> siacTGiustificativoDets) {
		this.siacTGiustificativoDets = siacTGiustificativoDets;
	}

	public SiacTGiustificativoDet addSiacTGiustificativoDet(SiacTGiustificativoDet siacTGiustificativoDet) {
		getSiacTGiustificativoDets().add(siacTGiustificativoDet);
		siacTGiustificativoDet.setSiacDGiustificativo(this);

		return siacTGiustificativoDet;
	}

	public SiacTGiustificativoDet removeSiacTGiustificativoDet(SiacTGiustificativoDet siacTGiustificativoDet) {
		getSiacTGiustificativoDets().remove(siacTGiustificativoDet);
		siacTGiustificativoDet.setSiacTGiustificativo(null);

		return siacTGiustificativoDet;
	}

	public List<SiacRCassaEconGiustificativo> getSiacRCassaEconGiustificativos() {
		return this.siacRCassaEconGiustificativos;
	}

	public void setSiacRCassaEconGiustificativos(List<SiacRCassaEconGiustificativo> siacRCassaEconGiustificativos) {
		this.siacRCassaEconGiustificativos = siacRCassaEconGiustificativos;
	}

	public SiacRCassaEconGiustificativo addSiacRCassaEconGiustificativo(SiacRCassaEconGiustificativo siacRCassaEconGiustificativo) {
		getSiacRCassaEconGiustificativos().add(siacRCassaEconGiustificativo);
		siacRCassaEconGiustificativo.setSiacDGiustificativo(this);

		return siacRCassaEconGiustificativo;
	}

	public SiacRCassaEconGiustificativo removeSiacRCassaEconGiustificativo(SiacRCassaEconGiustificativo siacRCassaEconGiustificativo) {
		getSiacRCassaEconGiustificativos().remove(siacRCassaEconGiustificativo);
		siacRCassaEconGiustificativo.setSiacDGiustificativo(null);

		return siacRCassaEconGiustificativo;
	}

}