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
 * The persistent class for the siac_t_prov_cassa database table.
 * 
 */
@Entity
@Table(name="siac_t_prov_cassa")
@NamedQuery(name="SiacTProvCassa.findAll", query="SELECT s FROM SiacTProvCassa s")
public class SiacTProvCassa extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The provc id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PROV_CASSA_PROVCID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PROV_CASSA_PROVC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PROV_CASSA_PROVCID_GENERATOR")
	@Column(name="provc_id")
	private Integer provcId;

	/** The provc anno. */
	@Column(name="provc_anno")
	private Integer provcAnno;

	/** The provc causale. */
	@Column(name="provc_causale")
	private String provcCausale;

	/** The provc data annullamento. */
	@Column(name="provc_data_annullamento")
	private Date provcDataAnnullamento;

	/** The provc data convalida. */
	@Column(name="provc_data_convalida")
	private Date provcDataConvalida;

	/** The provc data emissione. */
	@Column(name="provc_data_emissione")
	private Date provcDataEmissione;
	
	/** The provc data regolarizzazione. */
	@Column(name="provc_data_regolarizzazione")
	private Date provcDataRegolarizzazione;

	/** The provc denom soggetto. */
	@Column(name="provc_denom_soggetto")
	private String provcDenomSoggetto;

	/** The provc importo. */
	@Column(name="provc_importo")
	private BigDecimal provcImporto;

	/** The provc numero. */
	@Column(name="provc_numero")
	private BigDecimal provcNumero;

	/** The provc subcausale. */
	@Column(name="provc_subcausale")
	private String provcSubcausale;

	//bi-directional many-to-one association to SiacROrdinativoProvCassa
	/** The siac r ordinativo prov cassas. */
	@OneToMany(mappedBy="siacTProvCassa")
	private List<SiacROrdinativoProvCassa> siacROrdinativoProvCassas;

	//bi-directional many-to-one association to SiacRPredocProvCassa
	/** The siac r predoc prov cassas. */
	@OneToMany(mappedBy="siacTProvCassa")
	private List<SiacRPredocProvCassa> siacRPredocProvCassas;

	//bi-directional many-to-one association to SiacRSubdocProvCassa
	/** The siac r subdoc prov cassas. */
	@OneToMany(mappedBy="siacTProvCassa")
	private List<SiacRSubdocProvCassa> siacRSubdocProvCassas;

	//bi-directional many-to-one association to SiacDProvCassaTipo
	/** The siac d prov cassa tipo. */
	@ManyToOne
	@JoinColumn(name="provc_tipo_id")
	private SiacDProvCassaTipo siacDProvCassaTipo;

	/**
	 * Instantiates a new siac t prov cassa.
	 */
	public SiacTProvCassa() {
	}

	/**
	 * Gets the provc id.
	 *
	 * @return the provc id
	 */
	public Integer getProvcId() {
		return this.provcId;
	}

	/**
	 * Sets the provc id.
	 *
	 * @param provcId the new provc id
	 */
	public void setProvcId(Integer provcId) {
		this.provcId = provcId;
	}

	/**
	 * Gets the provc anno.
	 *
	 * @return the provc anno
	 */
	public Integer getProvcAnno() {
		return this.provcAnno;
	}

	/**
	 * Sets the provc anno.
	 *
	 * @param provcAnno the new provc anno
	 */
	public void setProvcAnno(Integer provcAnno) {
		this.provcAnno = provcAnno;
	}

	/**
	 * Gets the provc causale.
	 *
	 * @return the provc causale
	 */
	public String getProvcCausale() {
		return this.provcCausale;
	}

	/**
	 * Sets the provc causale.
	 *
	 * @param provcCausale the new provc causale
	 */
	public void setProvcCausale(String provcCausale) {
		this.provcCausale = provcCausale;
	}

	/**
	 * Gets the provc data annullamento.
	 *
	 * @return the provc data annullamento
	 */
	public Date getProvcDataAnnullamento() {
		return this.provcDataAnnullamento;
	}

	/**
	 * Sets the provc data annullamento.
	 *
	 * @param provcDataAnnullamento the new provc data annullamento
	 */
	public void setProvcDataAnnullamento(Date provcDataAnnullamento) {
		this.provcDataAnnullamento = provcDataAnnullamento;
	}

	/**
	 * Gets the provc data convalida.
	 *
	 * @return the provc data convalida
	 */
	public Date getProvcDataConvalida() {
		return this.provcDataConvalida;
	}

	/**
	 * Sets the provc data convalida.
	 *
	 * @param provcDataConvalida the new provc data convalida
	 */
	public void setProvcDataConvalida(Date provcDataConvalida) {
		this.provcDataConvalida = provcDataConvalida;
	}

	/**
	 * Gets the provc data emissione.
	 *
	 * @return the provc data emissione
	 */
	public Date getProvcDataEmissione() {
		return this.provcDataEmissione;
	}

	/**
	 * Sets the provc data emissione.
	 *
	 * @param provcDataEmissione the new provc data emissione
	 */
	public void setProvcDataEmissione(Date provcDataEmissione) {
		this.provcDataEmissione = provcDataEmissione;
	}

	/**
	 * @return the provcDataRegolarizzazione
	 */
	public Date getProvcDataRegolarizzazione() {
		return provcDataRegolarizzazione;
	}

	/**
	 * @param provcDataRegolarizzazione the provcDataRegolarizzazione to set
	 */
	public void setProvcDataRegolarizzazione(Date provcDataRegolarizzazione) {
		this.provcDataRegolarizzazione = provcDataRegolarizzazione;
	}

	/**
	 * Gets the provc denom soggetto.
	 *
	 * @return the provc denom soggetto
	 */
	public String getProvcDenomSoggetto() {
		return this.provcDenomSoggetto;
	}

	/**
	 * Sets the provc denom soggetto.
	 *
	 * @param provcDenomSoggetto the new provc denom soggetto
	 */
	public void setProvcDenomSoggetto(String provcDenomSoggetto) {
		this.provcDenomSoggetto = provcDenomSoggetto;
	}

	/**
	 * Gets the provc importo.
	 *
	 * @return the provc importo
	 */
	public BigDecimal getProvcImporto() {
		return this.provcImporto;
	}

	/**
	 * Sets the provc importo.
	 *
	 * @param provcImporto the new provc importo
	 */
	public void setProvcImporto(BigDecimal provcImporto) {
		this.provcImporto = provcImporto;
	}

	/**
	 * Gets the provc numero.
	 *
	 * @return the provc numero
	 */
	public BigDecimal getProvcNumero() {
		return this.provcNumero;
	}

	/**
	 * Sets the provc numero.
	 *
	 * @param provcNumero the new provc numero
	 */
	public void setProvcNumero(BigDecimal provcNumero) {
		this.provcNumero = provcNumero;
	}

	/**
	 * Gets the provc subcausale.
	 *
	 * @return the provc subcausale
	 */
	public String getProvcSubcausale() {
		return this.provcSubcausale;
	}

	/**
	 * Sets the provc subcausale.
	 *
	 * @param provcSubcausale the new provc subcausale
	 */
	public void setProvcSubcausale(String provcSubcausale) {
		this.provcSubcausale = provcSubcausale;
	}

	/**
	 * Gets the siac r ordinativo prov cassas.
	 *
	 * @return the siac r ordinativo prov cassas
	 */
	public List<SiacROrdinativoProvCassa> getSiacROrdinativoProvCassas() {
		return this.siacROrdinativoProvCassas;
	}

	/**
	 * Sets the siac r ordinativo prov cassas.
	 *
	 * @param siacROrdinativoProvCassas the new siac r ordinativo prov cassas
	 */
	public void setSiacROrdinativoProvCassas(List<SiacROrdinativoProvCassa> siacROrdinativoProvCassas) {
		this.siacROrdinativoProvCassas = siacROrdinativoProvCassas;
	}

	/**
	 * Adds the siac r ordinativo prov cassa.
	 *
	 * @param siacROrdinativoProvCassa the siac r ordinativo prov cassa
	 * @return the siac r ordinativo prov cassa
	 */
	public SiacROrdinativoProvCassa addSiacROrdinativoProvCassa(SiacROrdinativoProvCassa siacROrdinativoProvCassa) {
		getSiacROrdinativoProvCassas().add(siacROrdinativoProvCassa);
		siacROrdinativoProvCassa.setSiacTProvCassa(this);

		return siacROrdinativoProvCassa;
	}

	/**
	 * Removes the siac r ordinativo prov cassa.
	 *
	 * @param siacROrdinativoProvCassa the siac r ordinativo prov cassa
	 * @return the siac r ordinativo prov cassa
	 */
	public SiacROrdinativoProvCassa removeSiacROrdinativoProvCassa(SiacROrdinativoProvCassa siacROrdinativoProvCassa) {
		getSiacROrdinativoProvCassas().remove(siacROrdinativoProvCassa);
		siacROrdinativoProvCassa.setSiacTProvCassa(null);

		return siacROrdinativoProvCassa;
	}

	/**
	 * Gets the siac r predoc prov cassas.
	 *
	 * @return the siac r predoc prov cassas
	 */
	public List<SiacRPredocProvCassa> getSiacRPredocProvCassas() {
		return this.siacRPredocProvCassas;
	}

	/**
	 * Sets the siac r predoc prov cassas.
	 *
	 * @param siacRPredocProvCassas the new siac r predoc prov cassas
	 */
	public void setSiacRPredocProvCassas(List<SiacRPredocProvCassa> siacRPredocProvCassas) {
		this.siacRPredocProvCassas = siacRPredocProvCassas;
	}

	/**
	 * Adds the siac r predoc prov cassa.
	 *
	 * @param siacRPredocProvCassa the siac r predoc prov cassa
	 * @return the siac r predoc prov cassa
	 */
	public SiacRPredocProvCassa addSiacRPredocProvCassa(SiacRPredocProvCassa siacRPredocProvCassa) {
		getSiacRPredocProvCassas().add(siacRPredocProvCassa);
		siacRPredocProvCassa.setSiacTProvCassa(this);

		return siacRPredocProvCassa;
	}

	/**
	 * Removes the siac r predoc prov cassa.
	 *
	 * @param siacRPredocProvCassa the siac r predoc prov cassa
	 * @return the siac r predoc prov cassa
	 */
	public SiacRPredocProvCassa removeSiacRPredocProvCassa(SiacRPredocProvCassa siacRPredocProvCassa) {
		getSiacRPredocProvCassas().remove(siacRPredocProvCassa);
		siacRPredocProvCassa.setSiacTProvCassa(null);

		return siacRPredocProvCassa;
	}

	/**
	 * Gets the siac r subdoc prov cassas.
	 *
	 * @return the siac r subdoc prov cassas
	 */
	public List<SiacRSubdocProvCassa> getSiacRSubdocProvCassas() {
		return this.siacRSubdocProvCassas;
	}

	/**
	 * Sets the siac r subdoc prov cassas.
	 *
	 * @param siacRSubdocProvCassas the new siac r subdoc prov cassas
	 */
	public void setSiacRSubdocProvCassas(List<SiacRSubdocProvCassa> siacRSubdocProvCassas) {
		this.siacRSubdocProvCassas = siacRSubdocProvCassas;
	}

	/**
	 * Adds the siac r subdoc prov cassa.
	 *
	 * @param siacRSubdocProvCassa the siac r subdoc prov cassa
	 * @return the siac r subdoc prov cassa
	 */
	public SiacRSubdocProvCassa addSiacRSubdocProvCassa(SiacRSubdocProvCassa siacRSubdocProvCassa) {
		getSiacRSubdocProvCassas().add(siacRSubdocProvCassa);
		siacRSubdocProvCassa.setSiacTProvCassa(this);

		return siacRSubdocProvCassa;
	}

	/**
	 * Removes the siac r subdoc prov cassa.
	 *
	 * @param siacRSubdocProvCassa the siac r subdoc prov cassa
	 * @return the siac r subdoc prov cassa
	 */
	public SiacRSubdocProvCassa removeSiacRSubdocProvCassa(SiacRSubdocProvCassa siacRSubdocProvCassa) {
		getSiacRSubdocProvCassas().remove(siacRSubdocProvCassa);
		siacRSubdocProvCassa.setSiacTProvCassa(null);

		return siacRSubdocProvCassa;
	}

	/**
	 * Gets the siac d prov cassa tipo.
	 *
	 * @return the siac d prov cassa tipo
	 */
	public SiacDProvCassaTipo getSiacDProvCassaTipo() {
		return this.siacDProvCassaTipo;
	}

	/**
	 * Sets the siac d prov cassa tipo.
	 *
	 * @param siacDProvCassaTipo the new siac d prov cassa tipo
	 */
	public void setSiacDProvCassaTipo(SiacDProvCassaTipo siacDProvCassaTipo) {
		this.siacDProvCassaTipo = siacDProvCassaTipo;
	}



	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return provcId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.provcId = uid;
	}

}