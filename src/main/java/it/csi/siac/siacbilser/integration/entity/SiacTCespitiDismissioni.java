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
@Table(name="siac_t_cespiti_dismissioni")
@NamedQuery(name="SiacTCespitiDismissioni.findAll", query="SELECT s FROM SiacTCespitiDismissioni s")
public class SiacTCespitiDismissioni  extends SiacTEnteBaseExt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5434428809401992668L;

	@Id
	@SequenceGenerator(name="SIAC_T_CESPITI_DISMISSIONIID_GENERATOR", allocationSize=1, sequenceName="siac_t_cespiti_dismissioni_ces_dismissioni_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CESPITI_DISMISSIONIID_GENERATOR")
	@Column(name="ces_dismissioni_id")
	private Integer cesDismissioniId;
	
	@Column(name="ces_dismissioni_desc")
	private String cesDismissioniDesc;
	
	@Column(name="elenco_dismissioni_anno")
	private Integer elencoDismissioniAnno;
	
	@Column(name="elenco_dismissioni_numero")
	private Integer elencoDismissioniNumero;
					
	@Column(name="data_cessazione")
	private Date dataCessazione;
				
	@Column(name="dismissioni_desc_stato")
	private String dismissioniDescStato;
	
	
	// bi-directional many-to-one association 
	@ManyToOne
	@JoinColumn(name="evento_id")
	private SiacDEvento siacDEvento;
	
	// bi-directional many-to-one association 
	@ManyToOne
	@JoinColumn(name="causale_ep_id")
	private SiacTCausaleEp siacTCausaleEp;

	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;   

	
	//bi-directional many-to-one association to SiacRCespitiDismissioniPrimaNota
	@OneToMany(mappedBy="siacTCespitiDismissioni", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCespitiDismissioniPrimaNota> siacRCespitiDismissioniPrimaNota;
	
	@OneToMany(mappedBy="siacTCespitiDismissioni")
	private List<SiacTCespiti> siacTCespitis;
	
	@ManyToOne
	@JoinColumn(name="ces_dismissioni_stato_id")
	private SiacDCespitiDismissioniStato siacDCespitiDismissioniStato;
	/**
	 * @return the cesDismissioniId
	 */
	public Integer getCesDismissioniId() {
		return cesDismissioniId;
	}

	/**
	 * @param cesDismissioniId the cesDismissioniId to set
	 */
	public void setCesDismissioniId(Integer cesDismissioniId) {
		this.cesDismissioniId = cesDismissioniId;
	}

	/**
	 * @return the cesDismissioniDesc
	 */
	public String getCesDismissioniDesc() {
		return cesDismissioniDesc;
	}

	/**
	 * @param cesDismissioniDesc the cesDismissioniDesc to set
	 */
	public void setCesDismissioniDesc(String cesDismissioniDesc) {
		this.cesDismissioniDesc = cesDismissioniDesc;
	}

	/**
	 * Gets the elenco dismissioni numero.
	 *
	 * @return the dismissioniElencoNum
	 */
	public Integer getElencoDismissioniNumero() {
		return elencoDismissioniNumero;
	}

	/**
	 * Sets the elenco dismissioni anno.
	 *
	 * @param elencoDismissioniNumero the new elenco dismissioni anno
	 */
	public void setElencoDismissioniNumero(Integer elencoDismissioniNumero) {
		this.elencoDismissioniNumero = elencoDismissioniNumero;
	}

	/**
	 * Gets the elenco dismissioni anno.
	 *
	 * @return the dismissioniElencoAnno
	 */
	public Integer getElencoDismissioniAnno() {
		return elencoDismissioniAnno;
	}

	/**
	 * Sets the dismissioni elenco anno.
	 *
	 * @param elencoDismissioniAnno the new dismissioni elenco anno
	 */
	public void setElencoDismissioniAnno(Integer elencoDismissioniAnno) {
		this.elencoDismissioniAnno = elencoDismissioniAnno;
	}

	/**
	 * @return the dataCessazione
	 */
	public Date getDataCessazione() {
		return dataCessazione;
	}

	/**
	 * @param dataCessazione the dataCessazione to set
	 */
	public void setDataCessazione(Date dataCessazione) {
		this.dataCessazione = dataCessazione;
	}

	/**
	 * @return the dismissioniDescStato
	 */
	public String getDismissioniDescStato() {
		return dismissioniDescStato;
	}

	/**
	 * @param dismissioniDescStato the dismissioniDescStato to set
	 */
	public void setDismissioniDescStato(String dismissioniDescStato) {
		this.dismissioniDescStato = dismissioniDescStato;
	}

	/**
	 * @return the siacDEvento
	 */
	public SiacDEvento getSiacDEvento() {
		return siacDEvento;
	}

	/**
	 * @param siacDEvento the siacDEvento to set
	 */
	public void setSiacDEvento(SiacDEvento siacDEvento) {
		this.siacDEvento = siacDEvento;
	}

	/**
	 * @return the siacTCausaleEp
	 */
	public SiacTCausaleEp getSiacTCausaleEp() {
		return siacTCausaleEp;
	}

	/**
	 * @param siacTCausaleEp the siacTCausaleEp to set
	 */
	public void setSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
		this.siacTCausaleEp = siacTCausaleEp;
	}

	/**
	 * @return the siacTAttoAmm
	 */
	public SiacTAttoAmm getSiacTAttoAmm() {
		return siacTAttoAmm;
	}

	/**
	 * @param siacTAttoAmm the siacTAttoAmm to set
	 */
	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	
	/**
	 * @return the siacRCespitiDismissioniPrimaNota
	 */
	public List<SiacRCespitiDismissioniPrimaNota> getSiacRCespitiDismissioniPrimaNota() {
		return siacRCespitiDismissioniPrimaNota;
	}

	/**
	 * @param siacRCespitiDismissioniPrimaNota the siacRCespitiDismissioniPrimaNota to set
	 */
	public void setSiacRCespitiDismissioniPrimaNota(
			List<SiacRCespitiDismissioniPrimaNota> siacRCespitiDismissioniPrimaNota) {
		this.siacRCespitiDismissioniPrimaNota = siacRCespitiDismissioniPrimaNota;
	}
	/**
	 * @return the siacDCespitiDismissioniStato
	 */
	public SiacDCespitiDismissioniStato getSiacDCespitiDismissioniStato() {
		return siacDCespitiDismissioniStato;
	}

	/**
	 * @param siacDCespitiDismissioniStato the siacDCespitiDismissioniStato to set
	 */
	public void setSiacDCespitiDismissioniStato(SiacDCespitiDismissioniStato siacDCespitiDismissioniStato) {
		this.siacDCespitiDismissioniStato = siacDCespitiDismissioniStato;
	}

	/**
	 * Adds the siac R cespiti dismissioni prima nota.
	 *
	 * @param siacRCespitiDismissioniPrimaNota the siac R cespiti dismissioni prima nota
	 * @return the siac R cespiti dismissioni prima nota
	 */
	public SiacRCespitiDismissioniPrimaNota addSiacRCespitiDismissioniPrimaNota(SiacRCespitiDismissioniPrimaNota siacRCespitiDismissioniPrimaNota) {
		getSiacRCespitiDismissioniPrimaNota().add(siacRCespitiDismissioniPrimaNota);
		siacRCespitiDismissioniPrimaNota.setSiacTCespitiDismissioni(this);

		return siacRCespitiDismissioniPrimaNota;
	}
	
	@Override
	public Integer getUid() {
		return this.cesDismissioniId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cesDismissioniId = uid;
	}

}