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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_cespiti_categoria database table.
 * 
 */
@Entity
@Table(name="siac_t_cespiti_ammortamento")
@NamedQuery(name="SiacTCespitiAmmortamento.findAll", query="SELECT s FROM SiacTCespitiAmmortamento s")
public class SiacTCespitiAmmortamento extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="siac_t_cespiti_ammortamento_ces_amm_idGENERATOR", allocationSize=1, sequenceName="siac_t_cespiti_ammortamento_ces_amm_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_t_cespiti_ammortamento_ces_amm_idGENERATOR")
	@Column(name="ces_amm_id")
	private Integer cesAmmId;
	
	@Column(name="ces_amm_ultimo_anno_reg")
	private Integer cesAmmUltimoAnnoReg;
	
	@Column(name="ces_amm_importo_tot_reg")
	private BigDecimal cesAmmImportoTotReg;
	
	@OneToMany(mappedBy="siacTCespitiAmmortamento", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTCespitiAmmortamentoDett> siacTCespitiAmmortamentoDetts;
	
	// bi-directional many-to-one association 
	@ManyToOne
	@JoinColumn(name="ces_id")
	private SiacTCespiti siacTCespiti;
	
	@Column(name="ces_amm_completo")
	private Boolean cesAmmCompleto;

	public SiacTCespitiAmmortamento(Integer cesAmmId) {
		this();
		setCesAmmId(cesAmmId);
	}

	public SiacTCespitiAmmortamento() {
		super();
	}

	/**
	 * @return the cesAmmId
	 */
	public Integer getCesAmmId() {
		return cesAmmId;
	}

	/**
	 * @param cesAmmId the cesAmmId to set
	 */
	public void setCesAmmId(Integer cesAmmId) {
		this.cesAmmId = cesAmmId;
	}

	/**
	 * @return the cesAmmUltimoAnno
	 */
	public Integer getCesAmmUltimoAnnoReg() {
		return cesAmmUltimoAnnoReg;
	}

	/**
	 * @param cesAmmUltimoAnno the cesAmmUltimoAnno to set
	 */
	public void setCesAmmUltimoAnnoReg(Integer cesAmmUltimoAnnoReg) {
		this.cesAmmUltimoAnnoReg = cesAmmUltimoAnnoReg;
	}


	/**
	 * @return the cesAmmImportoTot
	 */
	public BigDecimal getCesAmmImportoTotReg() {
		return cesAmmImportoTotReg;
	}

	/**
	 * @param importoTotaleAmmortato the cesAmmImportoTot to set
	 */
	public void setCesAmmImportoTotReg(BigDecimal cesAmmImportoTotReg) {
		this.cesAmmImportoTotReg = cesAmmImportoTotReg;
	}

	/**
	 * @return the siacTCespitiAmmortamentoDetts
	 */
	public List<SiacTCespitiAmmortamentoDett> getSiacTCespitiAmmortamentoDetts() {
		return siacTCespitiAmmortamentoDetts;
	}

	/**
	 * @param siacTCespitiAmmortamentoDetts the siacTCespitiAmmortamentoDetts to set
	 */
	public void setSiacTCespitiAmmortamentoDetts(List<SiacTCespitiAmmortamentoDett> siacTCespitiAmmortamentoDetts) {
		this.siacTCespitiAmmortamentoDetts = siacTCespitiAmmortamentoDetts;
	}
	
	/**
	 * Adds the siac t cespiti variazione.
	 *
	 * @param siacTCespitiAmmortamentoDett the siac T cespiti ammortamento dett
	 * @return the siac t cespiti variazione
	 */
	public SiacTCespitiAmmortamentoDett addSiacTCespitiAmmortamentoDett(SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett) {
		getSiacTCespitiAmmortamentoDetts().add(siacTCespitiAmmortamentoDett);
		siacTCespitiAmmortamentoDett.setSiacTCespitiAmmortamento(this);

		return siacTCespitiAmmortamentoDett;
	}

	/**
	 * Removes the siac t cespiti variazione.
	 *
	 * @param siacTCespitiAmmortamentoDett the siac T cespiti ammortamento dett
	 * @return the siac t cespiti variazione
	 */
	public SiacTCespitiAmmortamentoDett removeSiacTCespitiAmmortamentoDett(SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett) {
		getSiacTCespitiAmmortamentoDetts().remove(siacTCespitiAmmortamentoDett);
		siacTCespitiAmmortamentoDett.setSiacTCespitiAmmortamento(null);

		return siacTCespitiAmmortamentoDett;
	}

	/**
	 * @return the siacTCespiti
	 */
	public SiacTCespiti getSiacTCespiti() {
		return siacTCespiti;
	}

	/**
	 * @param siacTCespiti the siacTCespiti to set
	 */
	public void setSiacTCespiti(SiacTCespiti siacTCespiti) {
		this.siacTCespiti = siacTCespiti;
	}
	
	/**
	 * @return the cesAmmCompleto
	 */
	public Boolean getCesAmmCompleto() {
		return cesAmmCompleto;
	}

	/**
	 * @param cesAmmCompleto the cesAmmCompleto to set
	 */
	public void setCesAmmCompleto(Boolean cesAmmCompleto) {
		this.cesAmmCompleto = cesAmmCompleto;
	}

	@Override
	public Integer getUid() {
		return this.cesAmmId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cesAmmId = uid;
	}

}