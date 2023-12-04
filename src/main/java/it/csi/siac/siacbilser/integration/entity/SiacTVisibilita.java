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
 * The persistent class for the siac_t_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_t_visibilita")
@NamedQuery(name="SiacTVisibilita.findAll", query="SELECT s FROM SiacTVisibilita s")
public class SiacTVisibilita extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The vis id. */
	@Id
	@SequenceGenerator(name="SIAC_T_VISIBILITA_VISID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_VISIBILITA_VIS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_VISIBILITA_VISID_GENERATOR")
	@Column(name="vis_id")
	private Integer visId;
	
	/** The vis campo. */
	@Column(name="vis_campo")
	private String visCampo;

	/** The vis visibile. */
	@Column(name="vis_visibile")
	private Boolean visVisibile;

	/** The vis funzionalita. */
	@Column(name="vis_funzionalita")
	private String visFunzionalita;

	/** The vis default. */
	@Column(name="vis_default")
	private String visDefault;

	/** The siac d tipo campo. */
	@ManyToOne
	@JoinColumn(name="tc_id")
	private SiacDTipoCampo siacDTipoCampo;
	/** The siac t azione. */
	@ManyToOne
	@JoinColumn(name="azione_id")
	private SiacTAzione siacTAzione;
	/**
	 * @return the visId
	 */
	public Integer getVisId() {
		return this.visId;
	}
	/**
	 * @param visId the visId to set
	 */
	public void setVisId(Integer visId) {
		this.visId = visId;
	}
	/**
	 * @return the visCampo
	 */
	public String getVisCampo() {
		return this.visCampo;
	}
	/**
	 * @param visCampo the visCampo to set
	 */
	public void setVisCampo(String visCampo) {
		this.visCampo = visCampo;
	}
	/**
	 * @return the visVisibile
	 */
	public Boolean getVisVisibile() {
		return this.visVisibile;
	}
	/**
	 * @param visVisibile the visVisibile to set
	 */
	public void setVisVisibile(Boolean visVisibile) {
		this.visVisibile = visVisibile;
	}
	/**
	 * @return the visFunzionalita
	 */
	public String getVisFunzionalita() {
		return this.visFunzionalita;
	}
	/**
	 * @param visFunzionalita the visFunzionalita to set
	 */
	public void setVisFunzionalita(String visFunzionalita) {
		this.visFunzionalita = visFunzionalita;
	}
	/**
	 * @return the visDefault
	 */
	public String getVisDefault() {
		return this.visDefault;
	}
	/**
	 * @param visDefault the visDefault to set
	 */
	public void setVisDefault(String visDefault) {
		this.visDefault = visDefault;
	}
	/**
	 * @return the siacDTipoCampo
	 */
	public SiacDTipoCampo getSiacDTipoCampo() {
		return this.siacDTipoCampo;
	}
	/**
	 * @param siacDTipoCampo the siacDTipoCampo to set
	 */
	public void setSiacDTipoCampo(SiacDTipoCampo siacDTipoCampo) {
		this.siacDTipoCampo = siacDTipoCampo;
	}
	/**
	 * @return the siacTAzione
	 */
	public SiacTAzione getSiacTAzione() {
		return this.siacTAzione;
	}
	/**
	 * @param siacTAzione the siacTAzione to set
	 */
	public void setSiacTAzione(SiacTAzione siacTAzione) {
		this.siacTAzione = siacTAzione;
	}

	@Override
	public Integer getUid() {
		return visId;
	}
	
	@Override
	public void setUid(Integer uid) {
		this.visId = uid;
	}
}