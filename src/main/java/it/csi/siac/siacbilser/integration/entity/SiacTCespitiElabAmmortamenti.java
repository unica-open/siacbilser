/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
 * The persistent class for the siac_d_cespiti_categoria database table.
 * 
 */
@Entity
@Table(name="siac_t_cespiti_elab_ammortamenti")
@NamedQuery(name="SiacTCespitiElabAmmortamenti.findAll", query="SELECT s FROM SiacTCespitiElabAmmortamenti s")
public class SiacTCespitiElabAmmortamenti extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id //
	@SequenceGenerator(name="siac_t_cespiti_elab_ammortamenti_ces_amm_idGENERATOR", allocationSize=1, sequenceName="siac_t_cespiti_elab_ammortamenti_elab_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_t_cespiti_elab_ammortamenti_ces_amm_idGENERATOR")
	@Column(name="elab_id")
	private Integer elabId;
	
	@Column(name="anno")
	private Integer anno;
	
	@Column(name="stato_elaborazione")
	private String statoElaborazione;
	
	@OneToMany(mappedBy="siacTCespitiElabAmmortamenti", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTCespitiElabAmmortamentiDett> siacTCespitiElabAmmortamentiDetts;
	
	@Column(name="data_elaborazione")
	private Date dataElaborazione;

	/**
	 * @return the elabId
	 */
	public Integer getElabId() {
		return elabId;
	}

	/**
	 * @param elabId the elabId to set
	 */
	public void setElabId(Integer elabId) {
		this.elabId = elabId;
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @return the statoElaborazione
	 */
	public String getStatoElaborazione() {
		return statoElaborazione;
	}

	/**
	 * @param statoElaborazione the statoElaborazione to set
	 */
	public void setStatoElaborazione(String statoElaborazione) {
		this.statoElaborazione = statoElaborazione;
	}

	/**
	 * @return the siacTCespitiElabAmmortamentiDett
	 */
	public List<SiacTCespitiElabAmmortamentiDett> getSiacTCespitiElabAmmortamentiDetts() {
		return siacTCespitiElabAmmortamentiDetts;
	}

	/**
	 * @param siacTCespitiElabAmmortamentiDett the siacTCespitiElabAmmortamentiDett to set
	 */
	public void setSiacTCespitiElabAmmortamentiDetts(List<SiacTCespitiElabAmmortamentiDett> siacTCespitiElabAmmortamentiDetts) {
		this.siacTCespitiElabAmmortamentiDetts = siacTCespitiElabAmmortamentiDetts;
	}

	/**
	 * @return the dataElaborazione
	 */
	public Date getDataElaborazione() {
		return dataElaborazione;
	}

	/**
	 * @param dataElaborazione the dataElaborazione to set
	 */
	public void setDataElaborazione(Date dataElaborazione) {
		this.dataElaborazione = dataElaborazione;
	}

	/**
	 * Adds the siac t cespiti variazione.
	 *
	 * @param siacTCespitiAmmortamentoDett the siac T cespiti ammortamento dett
	 * @return the siac t cespiti variazione
	 */
	public SiacTCespitiElabAmmortamentiDett addSiacTCespitiAmmortamentoDett(SiacTCespitiElabAmmortamentiDett siacTCespitiAmmortamentoDett) {
		getSiacTCespitiElabAmmortamentiDetts().add(siacTCespitiAmmortamentoDett);
		siacTCespitiAmmortamentoDett.setSiacTCespitiElabAmmortamenti(this);

		return siacTCespitiAmmortamentoDett;
	}

	/**
	 * Removes the siac t cespiti variazione.
	 *
	 * @param siacTCespitiAmmortamentoDett the siac T cespiti ammortamento dett
	 * @return the siac t cespiti variazione
	 */
	public SiacTCespitiElabAmmortamentiDett removeSiacTCespitiAmmortamentoDett(SiacTCespitiElabAmmortamentiDett siacTCespitiAmmortamentoDett) {
		getSiacTCespitiElabAmmortamentiDetts().remove(siacTCespitiAmmortamentoDett);
		siacTCespitiAmmortamentoDett.setSiacTCespitiElabAmmortamenti(null);

		return siacTCespitiAmmortamentoDett;
	}


	@Override
	public Integer getUid() {
		return this.elabId;
	}

	@Override
	public void setUid(Integer uid) {
		this.elabId = uid;
	}

}