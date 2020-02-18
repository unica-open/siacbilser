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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_iva_gruppo database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_gruppo")
@NamedQuery(name="SiacTIvaGruppo.findAll", query="SELECT s FROM SiacTIvaGruppo s")
public class SiacTIvaGruppo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivagru id. */
	@Id
	@SequenceGenerator(name="SIAC_T_IVA_GRUPPO_IVAGRUID_GENERATOR", sequenceName="SIAC_T_IVA_GRUPPO_IVAGRU_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_IVA_GRUPPO_IVAGRUID_GENERATOR")
	@Column(name="ivagru_id")
	private Integer ivagruId;

	/** The ivagru code. */
	@Column(name="ivagru_code")
	private String ivagruCode;

	/** The ivagru desc. */
	@Column(name="ivagru_desc")
	private String ivagruDesc;
	
	/** The siac d iva gruppo tipo. */
	@ManyToOne
	@JoinColumn(name="ivagru_tipo_id")
	private SiacDIvaGruppoTipo siacDIvaGruppoTipo;

	/** The ivagru ivaprecedente. */
	@Column(name="ivagru_ivaprecedente")
	private BigDecimal ivagruIvaprecedente;

	//bi-directional many-to-one association to SiacRIvaGruppoAttivita
	/** The siac r iva gruppo attivitas. */
	@OneToMany(mappedBy="siacTIvaGruppo",  cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<SiacRIvaGruppoAttivita> siacRIvaGruppoAttivitas;

	//bi-directional many-to-one association to SiacRIvaGruppoChiusura
	/** The siac r iva gruppo chiusuras. */
	@OneToMany(mappedBy="siacTIvaGruppo", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRIvaGruppoChiusura> siacRIvaGruppoChiusuras;

	//bi-directional many-to-one association to SiacRIvaGruppoProrata
	/** The siac r iva gruppo proratas. */
	@OneToMany(mappedBy="siacTIvaGruppo", cascade = { CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRIvaGruppoProrata> siacRIvaGruppoProratas;

	//bi-directional many-to-one association to SiacRIvaRegistroGruppo
	/** The siac r iva registro gruppos. */
	@OneToMany(mappedBy="siacTIvaGruppo"/*, cascade = {CascadeType.PERSIST, CascadeType.MERGE}*/)
	private List<SiacRIvaRegistroGruppo> siacRIvaRegistroGruppos;

	/**
	 * Instantiates a new siac t iva gruppo.
	 */
	public SiacTIvaGruppo() {
	}

	/**
	 * Gets the ivagru id.
	 *
	 * @return the ivagru id
	 */
	public Integer getIvagruId() {
		return this.ivagruId;
	}

	/**
	 * Sets the ivagru id.
	 *
	 * @param ivagruId the new ivagru id
	 */
	public void setIvagruId(Integer ivagruId) {
		this.ivagruId = ivagruId;
	}

	/**
	 * Gets the ivagru code.
	 *
	 * @return the ivagru code
	 */
	public String getIvagruCode() {
		return this.ivagruCode;
	}

	/**
	 * Sets the ivagru code.
	 *
	 * @param ivagruCode the new ivagru code
	 */
	public void setIvagruCode(String ivagruCode) {
		this.ivagruCode = ivagruCode;
	}

	/**
	 * Gets the ivagru desc.
	 *
	 * @return the ivagru desc
	 */
	public String getIvagruDesc() {
		return this.ivagruDesc;
	}

	/**
	 * Sets the ivagru desc.
	 *
	 * @param ivagruDesc the new ivagru desc
	 */
	public void setIvagruDesc(String ivagruDesc) {
		this.ivagruDesc = ivagruDesc;
	}

	/**
	 * Gets the ivagru ivaprecedente.
	 *
	 * @return the ivagru ivaprecedente
	 */
	public BigDecimal getIvagruIvaprecedente() {
		return this.ivagruIvaprecedente;
	}

	/**
	 * Sets the ivagru ivaprecedente.
	 *
	 * @param ivagruIvaprecedente the new ivagru ivaprecedente
	 */
	public void setIvagruIvaprecedente(BigDecimal ivagruIvaprecedente) {
		this.ivagruIvaprecedente = ivagruIvaprecedente;
	}

	/**
	 * Gets the siac r iva gruppo attivitas.
	 *
	 * @return the siac r iva gruppo attivitas
	 */
	public List<SiacRIvaGruppoAttivita> getSiacRIvaGruppoAttivitas() {
		return this.siacRIvaGruppoAttivitas;
	}

	/**
	 * Sets the siac r iva gruppo attivitas.
	 *
	 * @param siacRIvaGruppoAttivitas the new siac r iva gruppo attivitas
	 */
	public void setSiacRIvaGruppoAttivitas(List<SiacRIvaGruppoAttivita> siacRIvaGruppoAttivitas) {
		this.siacRIvaGruppoAttivitas = siacRIvaGruppoAttivitas;
	}

	/**
	 * Adds the siac r iva gruppo attivita.
	 *
	 * @param siacRIvaGruppoAttivita the siac r iva gruppo attivita
	 * @return the siac r iva gruppo attivita
	 */
	public SiacRIvaGruppoAttivita addSiacRIvaGruppoAttivita(SiacRIvaGruppoAttivita siacRIvaGruppoAttivita) {
		getSiacRIvaGruppoAttivitas().add(siacRIvaGruppoAttivita);
		siacRIvaGruppoAttivita.setSiacTIvaGruppo(this);

		return siacRIvaGruppoAttivita;
	}

	/**
	 * Removes the siac r iva gruppo attivita.
	 *
	 * @param siacRIvaGruppoAttivita the siac r iva gruppo attivita
	 * @return the siac r iva gruppo attivita
	 */
	public SiacRIvaGruppoAttivita removeSiacRIvaGruppoAttivita(SiacRIvaGruppoAttivita siacRIvaGruppoAttivita) {
		getSiacRIvaGruppoAttivitas().remove(siacRIvaGruppoAttivita);
		siacRIvaGruppoAttivita.setSiacTIvaGruppo(null);

		return siacRIvaGruppoAttivita;
	}

	/**
	 * Gets the siac r iva gruppo chiusuras.
	 *
	 * @return the siac r iva gruppo chiusuras
	 */
	public List<SiacRIvaGruppoChiusura> getSiacRIvaGruppoChiusuras() {
		return this.siacRIvaGruppoChiusuras;
	}

	/**
	 * Sets the siac r iva gruppo chiusuras.
	 *
	 * @param siacRIvaGruppoChiusuras the new siac r iva gruppo chiusuras
	 */
	public void setSiacRIvaGruppoChiusuras(List<SiacRIvaGruppoChiusura> siacRIvaGruppoChiusuras) {
		this.siacRIvaGruppoChiusuras = siacRIvaGruppoChiusuras;
	}

	/**
	 * Adds the siac r iva gruppo chiusura.
	 *
	 * @param siacRIvaGruppoChiusura the siac r iva gruppo chiusura
	 * @return the siac r iva gruppo chiusura
	 */
	public SiacRIvaGruppoChiusura addSiacRIvaGruppoChiusura(SiacRIvaGruppoChiusura siacRIvaGruppoChiusura) {
		getSiacRIvaGruppoChiusuras().add(siacRIvaGruppoChiusura);
		siacRIvaGruppoChiusura.setSiacTIvaGruppo(this);

		return siacRIvaGruppoChiusura;
	}

	/**
	 * Removes the siac r iva gruppo chiusura.
	 *
	 * @param siacRIvaGruppoChiusura the siac r iva gruppo chiusura
	 * @return the siac r iva gruppo chiusura
	 */
	public SiacRIvaGruppoChiusura removeSiacRIvaGruppoChiusura(SiacRIvaGruppoChiusura siacRIvaGruppoChiusura) {
		getSiacRIvaGruppoChiusuras().remove(siacRIvaGruppoChiusura);
		siacRIvaGruppoChiusura.setSiacTIvaGruppo(null);

		return siacRIvaGruppoChiusura;
	}

	/**
	 * Gets the siac r iva gruppo proratas.
	 *
	 * @return the siac r iva gruppo proratas
	 */
	public List<SiacRIvaGruppoProrata> getSiacRIvaGruppoProratas() {
		return this.siacRIvaGruppoProratas;
	}

	/**
	 * Sets the siac r iva gruppo proratas.
	 *
	 * @param siacRIvaGruppoProratas the new siac r iva gruppo proratas
	 */
	public void setSiacRIvaGruppoProratas(List<SiacRIvaGruppoProrata> siacRIvaGruppoProratas) {
		this.siacRIvaGruppoProratas = siacRIvaGruppoProratas;
	}

	/**
	 * Adds the siac r iva gruppo prorata.
	 *
	 * @param siacRIvaGruppoProrata the siac r iva gruppo prorata
	 * @return the siac r iva gruppo prorata
	 */
	public SiacRIvaGruppoProrata addSiacRIvaGruppoProrata(SiacRIvaGruppoProrata siacRIvaGruppoProrata) {
		getSiacRIvaGruppoProratas().add(siacRIvaGruppoProrata);
		siacRIvaGruppoProrata.setSiacTIvaGruppo(this);

		return siacRIvaGruppoProrata;
	}

	/**
	 * Removes the siac r iva gruppo prorata.
	 *
	 * @param siacRIvaGruppoProrata the siac r iva gruppo prorata
	 * @return the siac r iva gruppo prorata
	 */
	public SiacRIvaGruppoProrata removeSiacRIvaGruppoProrata(SiacRIvaGruppoProrata siacRIvaGruppoProrata) {
		getSiacRIvaGruppoProratas().remove(siacRIvaGruppoProrata);
		siacRIvaGruppoProrata.setSiacTIvaGruppo(null);

		return siacRIvaGruppoProrata;
	}

	/**
	 * Gets the siac r iva registro gruppos.
	 *
	 * @return the siac r iva registro gruppos
	 */
	public List<SiacRIvaRegistroGruppo> getSiacRIvaRegistroGruppos() {
		return this.siacRIvaRegistroGruppos;
	}

	/**
	 * Sets the siac r iva registro gruppos.
	 *
	 * @param siacRIvaRegistroGruppos the new siac r iva registro gruppos
	 */
	public void setSiacRIvaRegistroGruppos(List<SiacRIvaRegistroGruppo> siacRIvaRegistroGruppos) {
		this.siacRIvaRegistroGruppos = siacRIvaRegistroGruppos;
	}

	/**
	 * Adds the siac r iva registro gruppo.
	 *
	 * @param siacRIvaRegistroGruppo the siac r iva registro gruppo
	 * @return the siac r iva registro gruppo
	 */
	public SiacRIvaRegistroGruppo addSiacRIvaRegistroGruppo(SiacRIvaRegistroGruppo siacRIvaRegistroGruppo) {
		getSiacRIvaRegistroGruppos().add(siacRIvaRegistroGruppo);
		siacRIvaRegistroGruppo.setSiacTIvaGruppo(this);

		return siacRIvaRegistroGruppo;
	}

	/**
	 * Removes the siac r iva registro gruppo.
	 *
	 * @param siacRIvaRegistroGruppo the siac r iva registro gruppo
	 * @return the siac r iva registro gruppo
	 */
	public SiacRIvaRegistroGruppo removeSiacRIvaRegistroGruppo(SiacRIvaRegistroGruppo siacRIvaRegistroGruppo) {
		getSiacRIvaRegistroGruppos().remove(siacRIvaRegistroGruppo);
		siacRIvaRegistroGruppo.setSiacTIvaGruppo(null);

		return siacRIvaRegistroGruppo;
	}	

	/**
	 * Gets the siac d iva gruppo tipo.
	 *
	 * @return the siacDIvaGruppoTipo
	 */
	public SiacDIvaGruppoTipo getSiacDIvaGruppoTipo() {
		return siacDIvaGruppoTipo;
	}

	/**
	 * Sets the siac d iva gruppo tipo.
	 *
	 * @param siacDIvaGruppoTipo the siacDIvaGruppoTipo to set
	 */
	public void setSiacDIvaGruppoTipo(SiacDIvaGruppoTipo siacDIvaGruppoTipo) {
		this.siacDIvaGruppoTipo = siacDIvaGruppoTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivagruId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivagruId = uid;
	}
}