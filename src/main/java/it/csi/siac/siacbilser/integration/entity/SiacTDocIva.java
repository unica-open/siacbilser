/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
 * The persistent class for the siac_t_doc_iva database table.
 * 
 */
@Entity
@Table(name="siac_t_doc_iva")
@NamedQuery(name="SiacTDocIva.findAll", query="SELECT s FROM SiacTDocIva s")
public class SiacTDocIva extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The dociva id. */
	@Id
	@SequenceGenerator(name="SIAC_T_DOC_IVA_DOCIVAID_GENERATOR", sequenceName="SIAC_T_DOC_IVA_DOCIVA_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_DOC_IVA_DOCIVAID_GENERATOR")
	@Column(name="dociva_id")
	private Integer docivaId;

	/** The dociva anno. */
	@Column(name="dociva_anno")
	private Integer docivaAnno;

	/** The dociva data emissione. */
	@Column(name="dociva_data_emissione")
	private Date docivaDataEmissione;

	/** The dociva data scadenza. */
	@Column(name="dociva_data_scadenza")
	private Date docivaDataScadenza;

	/** The dociva desc. */
	@Column(name="dociva_desc")
	private String docivaDesc;

	/** The dociva importo. */
	@Column(name="dociva_importo")
	private BigDecimal docivaImporto;

	/** The dociva numero. */
	@Column(name="dociva_numero")
	private Integer docivaNumero;

	/** The login cancellazione. */
	@Column(name="login_cancellazione")
	private String loginCancellazione;

	/** The login creazione. */
	@Column(name="login_creazione")
	private String loginCreazione;

	/** The login modifica. */
	@Column(name="login_modifica")
	private String loginModifica;

	//bi-directional many-to-one association to SiacRDocIva
	/** The siac r doc ivas. */
	@OneToMany(mappedBy="siacTDocIva")
	private List<SiacRDocIva> siacRDocIvas;

	//bi-directional many-to-one association to SiacRDocIvaSog
	/** The siac r doc iva sogs. */
	@OneToMany(mappedBy="siacTDocIva")
	private List<SiacRDocIvaSog> siacRDocIvaSogs;

	//bi-directional many-to-one association to SiacDDocTipo
	/** The siac d doc tipo. */
	@ManyToOne
	@JoinColumn(name="doc_tipo_id")
	private SiacDDocTipo siacDDocTipo;

	/**
	 * Instantiates a new siac t doc iva.
	 */
	public SiacTDocIva() {
	}

	/**
	 * Gets the dociva id.
	 *
	 * @return the dociva id
	 */
	public Integer getDocivaId() {
		return this.docivaId;
	}

	/**
	 * Sets the dociva id.
	 *
	 * @param docivaId the new dociva id
	 */
	public void setDocivaId(Integer docivaId) {
		this.docivaId = docivaId;
	}

	/**
	 * Gets the dociva anno.
	 *
	 * @return the dociva anno
	 */
	public Integer getDocivaAnno() {
		return this.docivaAnno;
	}

	/**
	 * Sets the dociva anno.
	 *
	 * @param docivaAnno the new dociva anno
	 */
	public void setDocivaAnno(Integer docivaAnno) {
		this.docivaAnno = docivaAnno;
	}

	/**
	 * Gets the dociva data emissione.
	 *
	 * @return the dociva data emissione
	 */
	public Date getDocivaDataEmissione() {
		return this.docivaDataEmissione;
	}

	/**
	 * Sets the dociva data emissione.
	 *
	 * @param docivaDataEmissione the new dociva data emissione
	 */
	public void setDocivaDataEmissione(Date docivaDataEmissione) {
		this.docivaDataEmissione = docivaDataEmissione;
	}

	/**
	 * Gets the dociva data scadenza.
	 *
	 * @return the dociva data scadenza
	 */
	public Date getDocivaDataScadenza() {
		return this.docivaDataScadenza;
	}

	/**
	 * Sets the dociva data scadenza.
	 *
	 * @param docivaDataScadenza the new dociva data scadenza
	 */
	public void setDocivaDataScadenza(Date docivaDataScadenza) {
		this.docivaDataScadenza = docivaDataScadenza;
	}

	/**
	 * Gets the dociva desc.
	 *
	 * @return the dociva desc
	 */
	public String getDocivaDesc() {
		return this.docivaDesc;
	}

	/**
	 * Sets the dociva desc.
	 *
	 * @param docivaDesc the new dociva desc
	 */
	public void setDocivaDesc(String docivaDesc) {
		this.docivaDesc = docivaDesc;
	}

	/**
	 * Gets the dociva importo.
	 *
	 * @return the dociva importo
	 */
	public BigDecimal getDocivaImporto() {
		return this.docivaImporto;
	}

	/**
	 * Sets the dociva importo.
	 *
	 * @param docivaImporto the new dociva importo
	 */
	public void setDocivaImporto(BigDecimal docivaImporto) {
		this.docivaImporto = docivaImporto;
	}

	/**
	 * Gets the dociva numero.
	 *
	 * @return the dociva numero
	 */
	public Integer getDocivaNumero() {
		return this.docivaNumero;
	}

	/**
	 * Sets the dociva numero.
	 *
	 * @param docivaNumero the new dociva numero
	 */
	public void setDocivaNumero(Integer docivaNumero) {
		this.docivaNumero = docivaNumero;
	}

	/**
	 * Gets the login cancellazione.
	 *
	 * @return the login cancellazione
	 */
	public String getLoginCancellazione() {
		return this.loginCancellazione;
	}

	/**
	 * Sets the login cancellazione.
	 *
	 * @param loginCancellazione the new login cancellazione
	 */
	public void setLoginCancellazione(String loginCancellazione) {
		this.loginCancellazione = loginCancellazione;
	}

	/**
	 * Gets the login creazione.
	 *
	 * @return the login creazione
	 */
	public String getLoginCreazione() {
		return this.loginCreazione;
	}

	/**
	 * Sets the login creazione.
	 *
	 * @param loginCreazione the new login creazione
	 */
	public void setLoginCreazione(String loginCreazione) {
		this.loginCreazione = loginCreazione;
	}

	/**
	 * Gets the login modifica.
	 *
	 * @return the login modifica
	 */
	public String getLoginModifica() {
		return this.loginModifica;
	}

	/**
	 * Sets the login modifica.
	 *
	 * @param loginModifica the new login modifica
	 */
	public void setLoginModifica(String loginModifica) {
		this.loginModifica = loginModifica;
	}

	/**
	 * Gets the siac r doc ivas.
	 *
	 * @return the siac r doc ivas
	 */
	public List<SiacRDocIva> getSiacRDocIvas() {
		return this.siacRDocIvas;
	}

	/**
	 * Sets the siac r doc ivas.
	 *
	 * @param siacRDocIvas the new siac r doc ivas
	 */
	public void setSiacRDocIvas(List<SiacRDocIva> siacRDocIvas) {
		this.siacRDocIvas = siacRDocIvas;
	}

	/**
	 * Adds the siac r doc iva.
	 *
	 * @param siacRDocIva the siac r doc iva
	 * @return the siac r doc iva
	 */
	public SiacRDocIva addSiacRDocIva(SiacRDocIva siacRDocIva) {
		getSiacRDocIvas().add(siacRDocIva);
		siacRDocIva.setSiacTDocIva(this);

		return siacRDocIva;
	}

	/**
	 * Removes the siac r doc iva.
	 *
	 * @param siacRDocIva the siac r doc iva
	 * @return the siac r doc iva
	 */
	public SiacRDocIva removeSiacRDocIva(SiacRDocIva siacRDocIva) {
		getSiacRDocIvas().remove(siacRDocIva);
		siacRDocIva.setSiacTDocIva(null);

		return siacRDocIva;
	}

	/**
	 * Gets the siac r doc iva sogs.
	 *
	 * @return the siac r doc iva sogs
	 */
	public List<SiacRDocIvaSog> getSiacRDocIvaSogs() {
		return this.siacRDocIvaSogs;
	}

	/**
	 * Sets the siac r doc iva sogs.
	 *
	 * @param siacRDocIvaSogs the new siac r doc iva sogs
	 */
	public void setSiacRDocIvaSogs(List<SiacRDocIvaSog> siacRDocIvaSogs) {
		this.siacRDocIvaSogs = siacRDocIvaSogs;
	}

	/**
	 * Adds the siac r doc iva sog.
	 *
	 * @param siacRDocIvaSog the siac r doc iva sog
	 * @return the siac r doc iva sog
	 */
	public SiacRDocIvaSog addSiacRDocIvaSog(SiacRDocIvaSog siacRDocIvaSog) {
		getSiacRDocIvaSogs().add(siacRDocIvaSog);
		siacRDocIvaSog.setSiacTDocIva(this);

		return siacRDocIvaSog;
	}

	/**
	 * Removes the siac r doc iva sog.
	 *
	 * @param siacRDocIvaSog the siac r doc iva sog
	 * @return the siac r doc iva sog
	 */
	public SiacRDocIvaSog removeSiacRDocIvaSog(SiacRDocIvaSog siacRDocIvaSog) {
		getSiacRDocIvaSogs().remove(siacRDocIvaSog);
		siacRDocIvaSog.setSiacTDocIva(null);

		return siacRDocIvaSog;
	}

	/**
	 * Gets the siac d doc tipo.
	 *
	 * @return the siac d doc tipo
	 */
	public SiacDDocTipo getSiacDDocTipo() {
		return this.siacDDocTipo;
	}

	/**
	 * Sets the siac d doc tipo.
	 *
	 * @param siacDDocTipo the new siac d doc tipo
	 */
	public void setSiacDDocTipo(SiacDDocTipo siacDDocTipo) {
		this.siacDDocTipo = siacDDocTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return docivaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.docivaId = uid;
	}
}