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

// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_cronop database table.
 * 
 */
@Entity
@Table(name="siac_t_cronop")
@NamedQuery(name="SiacTCronop.findAll", query="SELECT s FROM SiacTCronop s")
public class SiacTCronop extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cronop id. */
	@Id
	@SequenceGenerator(name="SIAC_T_CRONOP_CRONOPID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CRONOP_CRONOP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CRONOP_CRONOPID_GENERATOR")
	@Column(name="cronop_id")
	private Integer cronopId;

	/** The cronop code. */
	@Column(name="cronop_code")
	private String cronopCode;

	/** The cronop desc. */
	@Column(name="cronop_desc")
	private String cronopDesc;

	//bi-directional many-to-one association to SiacRCronopAttr
	/** The siac r cronop attrs. */
	@OneToMany(mappedBy="siacTCronop", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCronopAttr> siacRCronopAttrs;

	//bi-directional many-to-one association to SiacRCronopStato
	/** The siac r cronop statos. */
	@OneToMany(mappedBy="siacTCronop", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCronopStato> siacRCronopStatos;

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bil. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTProgramma
	/** The siac t programma. */
	@ManyToOne
	@JoinColumn(name="programma_id")
	private SiacTProgramma siacTProgramma;

	//bi-directional many-to-one association to SiacTCronopElem
	/** The siac t cronop elems. */
	@OneToMany(mappedBy="siacTCronop") //, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE} L'associazione ai capitoli è nell'inserimento dei SiacTCronopElem.
	private List<SiacTCronopElem> siacTCronopElems;
	
	
	@Column(name="usato_per_fpv")
	private Boolean usatoPerFpv;
	
	@Column(name="cronop_data_approvazione_fattibilita")
	private Date cronopDataApprovazioneFattibilita;	
	@Column(name="cronop_data_approvazione_programma_def")
	private Date cronopDataApprovazioneProgrammaDef;
	@Column(name="cronop_data_approvazione_programma_esec")
	private Date cronopDataApprovazioneProgrammaEsec;
	@Column(name="cronop_data_avvio_procedura")
	private Date cronopDataAvvioProcedura;
	@Column(name="cronop_data_aggiudicazione_lavori")
	private Date cronopDataAggiudicazioneLavori;
	@Column(name="cronop_data_inizio_lavori")
	private Date cronopDataInizioLavori;
	@Column(name="cronop_data_fine_lavori")
	private Date cronopDataGaraIndizione;
	@Column(name="cronop_data_collaudo")
	private Date cronopDataCollaudo;
	@Column(name="gestione_quadro_economico")
	private Boolean gestioneQuadroEconomico;
	@Column(name="cronop_giorni_durata")
	private Integer cronopGiorniDurata;
	@Column(name="usato_per_fpv_prov")
	private Boolean usatoPerFpvProv;
	
	
	@OneToMany(mappedBy="siacTCronop", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCronopAttoAmm> siacRCronopAttoAmms;
	
	/**
	 * Instantiates a new siac t cronop.
	 */
	public SiacTCronop() {
	}

	/**
	 * Gets the cronop id.
	 *
	 * @return the cronop id
	 */
	public Integer getCronopId() {
		return this.cronopId;
	}

	/**
	 * Sets the cronop id.
	 *
	 * @param cronopId the new cronop id
	 */
	public void setCronopId(Integer cronopId) {
		this.cronopId = cronopId;
	}

	/**
	 * Gets the cronop code.
	 *
	 * @return the cronop code
	 */
	public String getCronopCode() {
		return this.cronopCode;
	}

	/**
	 * Sets the cronop code.
	 *
	 * @param cronopCode the new cronop code
	 */
	public void setCronopCode(String cronopCode) {
		this.cronopCode = cronopCode;
	}

	/**
	 * Gets the cronop desc.
	 *
	 * @return the cronop desc
	 */
	public String getCronopDesc() {
		return this.cronopDesc;
	}

	/**
	 * Sets the cronop desc.
	 *
	 * @param cronopDesc the new cronop desc
	 */
	public void setCronopDesc(String cronopDesc) {
		this.cronopDesc = cronopDesc;
	}

	/**
	 * Gets the siac r cronop attrs.
	 *
	 * @return the siac r cronop attrs
	 */
	public List<SiacRCronopAttr> getSiacRCronopAttrs() {
		return this.siacRCronopAttrs;
	}

	/**
	 * Sets the siac r cronop attrs.
	 *
	 * @param siacRCronopAttrs the new siac r cronop attrs
	 */
	public void setSiacRCronopAttrs(List<SiacRCronopAttr> siacRCronopAttrs) {
		this.siacRCronopAttrs = siacRCronopAttrs;
	}

	/**
	 * Adds the siac r cronop attr.
	 *
	 * @param siacRCronopAttr the siac r cronop attr
	 * @return the siac r cronop attr
	 */
	public SiacRCronopAttr addSiacRCronopAttr(SiacRCronopAttr siacRCronopAttr) {
		getSiacRCronopAttrs().add(siacRCronopAttr);
		siacRCronopAttr.setSiacTCronop(this);

		return siacRCronopAttr;
	}

	/**
	 * Removes the siac r cronop attr.
	 *
	 * @param siacRCronopAttr the siac r cronop attr
	 * @return the siac r cronop attr
	 */
	public SiacRCronopAttr removeSiacRCronopAttr(SiacRCronopAttr siacRCronopAttr) {
		getSiacRCronopAttrs().remove(siacRCronopAttr);
		siacRCronopAttr.setSiacTCronop(null);

		return siacRCronopAttr;
	}

	/**
	 * Gets the siac r cronop statos.
	 *
	 * @return the siac r cronop statos
	 */
	public List<SiacRCronopStato> getSiacRCronopStatos() {
		return this.siacRCronopStatos;
	}

	/**
	 * Sets the siac r cronop statos.
	 *
	 * @param siacRCronopStatos the new siac r cronop statos
	 */
	public void setSiacRCronopStatos(List<SiacRCronopStato> siacRCronopStatos) {
		this.siacRCronopStatos = siacRCronopStatos;
	}

	/**
	 * Adds the siac r cronop stato.
	 *
	 * @param siacRCronopStato the siac r cronop stato
	 * @return the siac r cronop stato
	 */
	public SiacRCronopStato addSiacRCronopStato(SiacRCronopStato siacRCronopStato) {
		getSiacRCronopStatos().add(siacRCronopStato);
		siacRCronopStato.setSiacTCronop(this);

		return siacRCronopStato;
	}

	/**
	 * Removes the siac r cronop stato.
	 *
	 * @param siacRCronopStato the siac r cronop stato
	 * @return the siac r cronop stato
	 */
	public SiacRCronopStato removeSiacRCronopStato(SiacRCronopStato siacRCronopStato) {
		getSiacRCronopStatos().remove(siacRCronopStato);
		siacRCronopStato.setSiacTCronop(null);

		return siacRCronopStato;
	}

	/**
	 * Gets the siac r programma atto amms.
	 *
	 * @return the siac r programma atto amms
	 */
	public List<SiacRCronopAttoAmm> getSiacRCronopAttoAmms() {
		return this.siacRCronopAttoAmms;
	}

	/**
	 * Sets the siac r programma atto amms.
	 *
	 * @param siacRCronopAttoAmms the new siac r programma atto amms
	 */
	public void setSiacRCronopAttoAmms(List<SiacRCronopAttoAmm> siacRCronopAttoAmms) {
		this.siacRCronopAttoAmms = siacRCronopAttoAmms;
	}

	/**
	 * Adds the siac r programma atto amm.
	 *
	 * @param siacRCronopAttoAmm the siac r programma atto amm
	 * @return the siac r programma atto amm
	 */
	public SiacRCronopAttoAmm addSiacRCronopAttoAmm(SiacRCronopAttoAmm siacRCronopAttoAmm) {
		getSiacRCronopAttoAmms().add(siacRCronopAttoAmm);
		siacRCronopAttoAmm.setSiacTCronop(this);

		return siacRCronopAttoAmm;
	}

	/**
	 * Removes the siac r programma atto amm.
	 *
	 * @param siacRCronopAttoAmm the siac r programma atto amm
	 * @return the siac r programma atto amm
	 */
	public SiacRCronopAttoAmm removeSiacRCronopAttoAmm(SiacRCronopAttoAmm siacRCronopAttoAmm) {
		getSiacRCronopAttoAmms().remove(siacRCronopAttoAmm);
		siacRCronopAttoAmm.setSiacTCronop(null);

		return siacRCronopAttoAmm;
	}

	
	
	/**
	 * Gets the siac t bil.
	 *
	 * @return the siac t bil
	 */
	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	/**
	 * Sets the siac t bil.
	 *
	 * @param siacTBil the new siac t bil
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	/**
	 * Gets the siac t programma.
	 *
	 * @return the siac t programma
	 */
	public SiacTProgramma getSiacTProgramma() {
		return this.siacTProgramma;
	}

	/**
	 * Sets the siac t programma.
	 *
	 * @param siacTProgramma the new siac t programma
	 */
	public void setSiacTProgramma(SiacTProgramma siacTProgramma) {
		this.siacTProgramma = siacTProgramma;
	}

	/**
	 * Gets the siac t cronop elems.
	 *
	 * @return the siac t cronop elems
	 */
	public List<SiacTCronopElem> getSiacTCronopElems() {
		return this.siacTCronopElems;
	}

	/**
	 * Sets the siac t cronop elems.
	 *
	 * @param siacTCronopElems the new siac t cronop elems
	 */
	public void setSiacTCronopElems(List<SiacTCronopElem> siacTCronopElems) {
		this.siacTCronopElems = siacTCronopElems;
	}

	/**
	 * Adds the siac t cronop elem.
	 *
	 * @param siacTCronopElem the siac t cronop elem
	 * @return the siac t cronop elem
	 */
	public SiacTCronopElem addSiacTCronopElem(SiacTCronopElem siacTCronopElem) {
		getSiacTCronopElems().add(siacTCronopElem);
		siacTCronopElem.setSiacTCronop(this);

		return siacTCronopElem;
	}

	/**
	 * Removes the siac t cronop elem.
	 *
	 * @param siacTCronopElem the siac t cronop elem
	 * @return the siac t cronop elem
	 */
	public SiacTCronopElem removeSiacTCronopElem(SiacTCronopElem siacTCronopElem) {
		getSiacTCronopElems().remove(siacTCronopElem);
		siacTCronopElem.setSiacTCronop(null);

		return siacTCronopElem;
	}
	
	public Boolean getUsatoPerFpv() {
		return usatoPerFpv;
	}

	public void setUsatoPerFpv(Boolean usatoPerFpv) {
		this.usatoPerFpv = usatoPerFpv;
	}
	
	/**
	 * @return the usatoPerFpvProv
	 */
	public Boolean getUsatoPerFpvProv() {
		return usatoPerFpvProv;
	}

	/**
	 * @param usatoPerFpvProv the usatoPerFpvProv to set
	 */
	public void setUsatoPerFpvProv(Boolean usatoPerFpvProv) {
		this.usatoPerFpvProv = usatoPerFpvProv;
	}

	/**
	 * @return the cronopDataApprovazioneFattibilita
	 */
	public Date getCronopDataApprovazioneFattibilita() {
		return cronopDataApprovazioneFattibilita;
	}

	/**
	 * @param cronopDataApprovazioneFattibilita the cronopDataApprovazioneFattibilita to set
	 */
	public void setCronopDataApprovazioneFattibilita(Date cronopDataApprovazioneFattibilita) {
		this.cronopDataApprovazioneFattibilita = cronopDataApprovazioneFattibilita;
	}

	/**
	 * @return the cronopDataApprovazioneProgrammaDef
	 */
	public Date getCronopDataApprovazioneProgrammaDef() {
		return cronopDataApprovazioneProgrammaDef;
	}

	/**
	 * @param cronopDataApprovazioneProgrammaDef the cronopDataApprovazioneProgrammaDef to set
	 */
	public void setCronopDataApprovazioneProgrammaDef(Date cronopDataApprovazioneProgrammaDef) {
		this.cronopDataApprovazioneProgrammaDef = cronopDataApprovazioneProgrammaDef;
	}

	/**
	 * @return the cronopDataApprovazioneProgrammaEsec
	 */
	public Date getCronopDataApprovazioneProgrammaEsec() {
		return cronopDataApprovazioneProgrammaEsec;
	}

	/**
	 * @param cronopDataApprovazioneProgrammaEsec the cronopDataApprovazioneProgrammaEsec to set
	 */
	public void setCronopDataApprovazioneProgrammaEsec(Date cronopDataApprovazioneProgrammaEsec) {
		this.cronopDataApprovazioneProgrammaEsec = cronopDataApprovazioneProgrammaEsec;
	}

	/**
	 * @return the cronopDataAvvioProcedura
	 */
	public Date getCronopDataAvvioProcedura() {
		return cronopDataAvvioProcedura;
	}

	/**
	 * @param cronopDataAvvioProcedura the cronopDataAvvioProcedura to set
	 */
	public void setCronopDataAvvioProcedura(Date cronopDataAvvioProcedura) {
		this.cronopDataAvvioProcedura = cronopDataAvvioProcedura;
	}

	/**
	 * @return the cronopDataAggiudicazioneLavori
	 */
	public Date getCronopDataAggiudicazioneLavori() {
		return cronopDataAggiudicazioneLavori;
	}

	/**
	 * @param cronopDataAggiudicazioneLavori the cronopDataAggiudicazioneLavori to set
	 */
	public void setCronopDataAggiudicazioneLavori(Date cronopDataAggiudicazioneLavori) {
		this.cronopDataAggiudicazioneLavori = cronopDataAggiudicazioneLavori;
	}

	/**
	 * @return the cronopDataInizioLavori
	 */
	public Date getCronopDataInizioLavori() {
		return cronopDataInizioLavori;
	}

	/**
	 * @param cronopDataInizioLavori the cronopDataInizioLavori to set
	 */
	public void setCronopDataInizioLavori(Date cronopDataInizioLavori) {
		this.cronopDataInizioLavori = cronopDataInizioLavori;
	}

	/**
	 * @return the cronopDataGaraIndizione
	 */
	public Date getCronopDataGaraIndizione() {
		return cronopDataGaraIndizione;
	}

	/**
	 * @param cronopDataGaraIndizione the cronopDataGaraIndizione to set
	 */
	public void setCronopDataGaraIndizione(Date cronopDataGaraIndizione) {
		this.cronopDataGaraIndizione = cronopDataGaraIndizione;
	}

	/**
	 * @return the cronopDataCollaudo
	 */
	public Date getCronopDataCollaudo() {
		return cronopDataCollaudo;
	}

	/**
	 * @param cronopDataCollaudo the cronopDataCollaudo to set
	 */
	public void setCronopDataCollaudo(Date cronopDataCollaudo) {
		this.cronopDataCollaudo = cronopDataCollaudo;
	}

	/**
	 * @return the gestioneQuoadroEconomico
	 */
	public Boolean getGestioneQuadroEconomico() {
		return gestioneQuadroEconomico;
	}

	/**
	 * @param gestioneQuoadroEconomico the gestioneQuoadroEconomico to set
	 */
	public void setGestioneQuadroEconomico(Boolean gestioneQuoadroEconomico) {
		this.gestioneQuadroEconomico = gestioneQuoadroEconomico;
	}
	
	/**
	 * @return the cronopGiorniDurata
	 */
	public Integer getCronopGiorniDurata() {
		return cronopGiorniDurata;
	}

	/**
	 * @param cronopGiorniDurata the cronopGiorniDurata to set
	 */
	public void setCronopGiorniDurata(Integer cronopGiorniDurata) {
		this.cronopGiorniDurata = cronopGiorniDurata;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cronopId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cronopId = uid;
	}

	
	

}