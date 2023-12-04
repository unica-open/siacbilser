/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
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
 * The Class SiacTAccFondiDubbiaEsigBil.
 */
@Entity
@Table(name="siac_t_acc_fondi_dubbia_esig_bil")
@NamedQuery(name="SiacTAccFondiDubbiaEsigBil.findAll", query="SELECT s FROM SiacTAccFondiDubbiaEsigBil s")
public class SiacTAccFondiDubbiaEsigBil extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The afde bil id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ACC_FONDI_DUBBIA_ESIG_BIL_AFDEBILID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ACC_FONDI_DUBBIA_ESIG_BIL_AFDE_BIL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ACC_FONDI_DUBBIA_ESIG_BIL_AFDEBILID_GENERATOR")
	@Column(name="afde_bil_id")
	private Integer afdeBilId;
	
	/** The afde bil accantonamento graduale. */
	@Column(name="afde_bil_accantonamento_graduale")
	private BigDecimal afdeBilAccantonamentoGraduale;

	/** The afde bil quinquennio riferimento. */
	@Column(name="afde_bil_quinquennio_riferimento")
	private Integer afdeBilQuinquennioRiferimento;

	/** The afde bil riscossione virtuosa. */
	@Column(name="afde_bil_riscossione_virtuosa")
	private Boolean afdeBilRiscossioneVirtuosa;

	/** The afde bil crediti stralciati. */
	@Column(name="afde_bil_crediti_stralciati")
	private BigDecimal afdeBilCreditiStralciati;

	/** The afde bil crediti stralciati fcde. */
	@Column(name="afde_bil_crediti_stralciati_fcde")
	private BigDecimal afdeBilCreditiStralciatiFcde;

	/** The afde bil accertamenti anni successivi. */
	@Column(name="afde_bil_accertamenti_anni_successivi")
	private BigDecimal afdeBilAccertamentiAnniSuccessivi;

	/** The afde bil accertamenti anni successivi fcde. */
	@Column(name="afde_bil_accertamenti_anni_successivi_fcde")
	private BigDecimal afdeBilAccertamentiAnniSuccessiviFcde;

	/** The afde bil versione. */
	@Column(name="afde_bil_versione")
	private Integer afdeBilVersione;

	/** The siac T bil. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;
	
	/** The siac D acc fondi dubbia esig. */
	@ManyToOne
	@JoinColumn(name="afde_tipo_id")
	private SiacDAccFondiDubbiaEsigTipo siacDAccFondiDubbiaEsigTipo;
	
	/** The siac D acc fondi dubbia stato. */
	@ManyToOne
	@JoinColumn(name="afde_stato_id")
	private SiacDAccFondiDubbiaEsigStato siacDAccFondiDubbiaEsigStato;
	
	/** The siac T acc fondi dubbia esigs. */
	@OneToMany(mappedBy="siacTAccFondiDubbiaEsigBil")
	private List<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs;
	
	/**
	 * Gets the afde bil id.
	 *
	 * @return the afdeBilId
	 */
	public Integer getAfdeBilId() {
		return this.afdeBilId;
	}

	/**
	 * Sets the afde bil id.
	 *
	 * @param afdeBilId the afdeBilId to set
	 */
	public void setAfdeBilId(Integer afdeBilId) {
		this.afdeBilId = afdeBilId;
	}

	/**
	 * Gets the afde bil accantonamento graduale.
	 *
	 * @return the afdeBilAccantonamentoGraduale
	 */
	public BigDecimal getAfdeBilAccantonamentoGraduale() {
		return this.afdeBilAccantonamentoGraduale;
	}

	/**
	 * Sets the afde bil accantonamento graduale.
	 *
	 * @param afdeBilAccantonamentoGraduale the afdeBilAccantonamentoGraduale to set
	 */
	public void setAfdeBilAccantonamentoGraduale(BigDecimal afdeBilAccantonamentoGraduale) {
		this.afdeBilAccantonamentoGraduale = afdeBilAccantonamentoGraduale;
	}

	/**
	 * Gets the afde bil quinquennio riferimento.
	 *
	 * @return the afdeBilQuinquennioRiferimento
	 */
	public Integer getAfdeBilQuinquennioRiferimento() {
		return this.afdeBilQuinquennioRiferimento;
	}

	/**
	 * Sets the afde bil quinquennio riferimento.
	 *
	 * @param afdeBilQuinquennioRiferimento the afdeBilQuinquennioRiferimento to set
	 */
	public void setAfdeBilQuinquennioRiferimento(Integer afdeBilQuinquennioRiferimento) {
		this.afdeBilQuinquennioRiferimento = afdeBilQuinquennioRiferimento;
	}

	/**
	 * Gets the afde bil riscossione virtuosa.
	 *
	 * @return the afdeBilRiscossioneVirtuosa
	 */
	public Boolean getAfdeBilRiscossioneVirtuosa() {
		return this.afdeBilRiscossioneVirtuosa;
	}

	/**
	 * @return the afdeBilCreditiStralciati
	 */
	public BigDecimal getAfdeBilCreditiStralciati() {
		return this.afdeBilCreditiStralciati;
	}

	/**
	 * @param afdeBilCreditiStralciati the afdeBilCreditiStralciati to set
	 */
	public void setAfdeBilCreditiStralciati(BigDecimal afdeBilCreditiStralciati) {
		this.afdeBilCreditiStralciati = afdeBilCreditiStralciati;
	}

	/**
	 * @return the afdeBilCreditiStralciatiFcde
	 */
	public BigDecimal getAfdeBilCreditiStralciatiFcde() {
		return this.afdeBilCreditiStralciatiFcde;
	}

	/**
	 * @param afdeBilCreditiStralciatiFcde the afdeBilCreditiStralciatiFcde to set
	 */
	public void setAfdeBilCreditiStralciatiFcde(BigDecimal afdeBilCreditiStralciatiFcde) {
		this.afdeBilCreditiStralciatiFcde = afdeBilCreditiStralciatiFcde;
	}

	/**
	 * @return the afdeBilAccertamentiAnniSuccessivi
	 */
	public BigDecimal getAfdeBilAccertamentiAnniSuccessivi() {
		return this.afdeBilAccertamentiAnniSuccessivi;
	}

	/**
	 * @param afdeBilAccertamentiAnniSuccessivi the afdeBilAccertamentiAnniSuccessivi to set
	 */
	public void setAfdeBilAccertamentiAnniSuccessivi(BigDecimal afdeBilAccertamentiAnniSuccessivi) {
		this.afdeBilAccertamentiAnniSuccessivi = afdeBilAccertamentiAnniSuccessivi;
	}

	/**
	 * @return the afdeBilAccertamentiAnniSuccessiviFcde
	 */
	public BigDecimal getAfdeBilAccertamentiAnniSuccessiviFcde() {
		return this.afdeBilAccertamentiAnniSuccessiviFcde;
	}

	/**
	 * @param afdeBilAccertamentiAnniSuccessiviFcde the afdeBilAccertamentiAnniSuccessiviFcde to set
	 */
	public void setAfdeBilAccertamentiAnniSuccessiviFcde(BigDecimal afdeBilAccertamentiAnniSuccessiviFcde) {
		this.afdeBilAccertamentiAnniSuccessiviFcde = afdeBilAccertamentiAnniSuccessiviFcde;
	}

	/**
	 * @return the afdeBilVersione
	 */
	public Integer getAfdeBilVersione() {
		return this.afdeBilVersione;
	}

	/**
	 * @param afdeBilVersione the afdeBilVersione to set
	 */
	public void setAfdeBilVersione(Integer afdeBilVersione) {
		this.afdeBilVersione = afdeBilVersione;
	}

	/**
	 * Sets the afde bil riscossione virtuosa.
	 *
	 * @param afdeBilRiscossioneVirtuosa the afdeBilRiscossioneVirtuosa to set
	 */
	public void setAfdeBilRiscossioneVirtuosa(Boolean afdeBilRiscossioneVirtuosa) {
		this.afdeBilRiscossioneVirtuosa = afdeBilRiscossioneVirtuosa;
	}

	/**
	 * Gets the siac T bil.
	 *
	 * @return the siacTBil
	 */
	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	/**
	 * Sets the siac T bil.
	 *
	 * @param siacTBil the siacTBil to set
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	/**
	 * Gets the siac D acc fondi dubbia esig tipo.
	 *
	 * @return the siacDAccFondiDubbiaEsigTipo
	 */
	public SiacDAccFondiDubbiaEsigTipo getSiacDAccFondiDubbiaEsigTipo() {
		return this.siacDAccFondiDubbiaEsigTipo;
	}

	/**
	 * Sets the siac D acc fondi dubbia esig tipo.
	 *
	 * @param siacDAccFondiDubbiaEsigTipo the siacDAccFondiDubbiaEsigTipo to set
	 */
	public void setSiacDAccFondiDubbiaEsigTipo(SiacDAccFondiDubbiaEsigTipo siacDAccFondiDubbiaEsigTipo) {
		this.siacDAccFondiDubbiaEsigTipo = siacDAccFondiDubbiaEsigTipo;
	}

	/**
	 * Gets the siac D acc fondi dubbia esig stato.
	 *
	 * @return the siacDAccFondiDubbiaEsigStato
	 */
	public SiacDAccFondiDubbiaEsigStato getSiacDAccFondiDubbiaEsigStato() {
		return this.siacDAccFondiDubbiaEsigStato;
	}

	/**
	 * Sets the siac D acc fondi dubbia esig stato.
	 *
	 * @param siacDAccFondiDubbiaEsigStato the siacDAccFondiDubbiaEsigStato to set
	 */
	public void setSiacDAccFondiDubbiaEsigStato(SiacDAccFondiDubbiaEsigStato siacDAccFondiDubbiaEsigStato) {
		this.siacDAccFondiDubbiaEsigStato = siacDAccFondiDubbiaEsigStato;
	}

	/**
	 * @return the siacTAccFondiDubbiaEsigs
	 */
	public List<SiacTAccFondiDubbiaEsig> getSiacTAccFondiDubbiaEsigs() {
		return this.siacTAccFondiDubbiaEsigs;
	}

	/**
	 * @param siacTAccFondiDubbiaEsigs the siacTAccFondiDubbiaEsigs to set
	 */
	public void setSiacTAccFondiDubbiaEsigs(List<SiacTAccFondiDubbiaEsig> siacTAccFondiDubbiaEsigs) {
		this.siacTAccFondiDubbiaEsigs = siacTAccFondiDubbiaEsigs;
	}

	@Override
	public Integer getUid() {
		return afdeBilId;
	}

	@Override
	public void setUid(Integer uid) {
		this.afdeBilId = uid;
	}

}