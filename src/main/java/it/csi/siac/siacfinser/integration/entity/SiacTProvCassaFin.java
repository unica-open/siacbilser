/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_prov_cassa database table.
 * 
 */
@Entity
@Table(name="siac_t_prov_cassa")
public class SiacTProvCassaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PROV_CASSA_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_prov_cassa_provc_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PROV_CASSA_ID_GENERATOR")
	@Column(name="provc_id")
	private Integer provcId;

	@Column(name="provc_anno")
	private Integer provcAnno;

	@Column(name="provc_causale")
	private String provcCausale;

	@Column(name="provc_data_annullamento")
	private Timestamp provcDataAnnullamento;

	@Column(name="provc_data_convalida")
	private Timestamp provcDataConvalida;

	@Column(name="provc_data_emissione")
	private Timestamp provcDataEmissione;

	@Column(name="provc_data_trasmissione")
	private Timestamp provcDataTrasmissione;

	@Column(name="provc_data_regolarizzazione")
	private Timestamp provcDataRegolarizzazione;

	@Column(name="provc_denom_soggetto")
	private String provcDenomSoggetto;

	@Column(name="provc_importo")
	private BigDecimal provcImporto;

	@Column(name="provc_numero")
	private BigDecimal provcNumero;
	
	@Column(name="provc_conto_evidenza")
	private String provcCodiceContoEvidenza;
	
	@Column(name="provc_conto_evidenza_desc")
	private String provcDescrizioneContoEvidenza;

	@Column(name="provc_subcausale")
	private String provcSubcausale;
	
	
	@Column(name="provc_accettato")
	private Boolean accettato;
		
	@Column(name="provc_note")
	private String note;
	
	
	@Column(name="provc_data_invio_servizio")
	private Date provcDataInvioServizio;

	@Column(name="provc_data_presa_in_carico_servizio")
	private Date provcDataPresaInCaricoServizio;

	@Column(name="provc_data_rifiuto_errata_attribuzione")
	private Date provcDataRifiutoErrataAttribuzione;

	
	
	//bi-directional many-to-one association to SiacRAttoAmmClassFin
	@OneToMany(mappedBy="siacTProvCassaFin")
	private List<SiacRProvCassaClassFin> siacRProvCassaClasses;

	//bi-directional many-to-one association to SiacROrdinativoProvCassaFin
	@OneToMany(mappedBy="siacTProvCassa")
	private List<SiacROrdinativoProvCassaFin> siacROrdinativoProvCassas;

	//bi-directional many-to-one association to SiacRPredocProvCassaFin
	@OneToMany(mappedBy="siacTProvCassa")
	private List<SiacRPredocProvCassaFin> siacRPredocProvCassas;

	//bi-directional many-to-one association to SiacRSubdocProvCassaFin
	@OneToMany(mappedBy="siacTProvCassa")
	private List<SiacRSubdocProvCassaFin> siacRSubdocProvCassas;

	//bi-directional many-to-one association to SiacDProvCassaTipoFin
	@ManyToOne
	@JoinColumn(name="provc_tipo_id")
	private SiacDProvCassaTipoFin siacDProvCassaTipo;

	public SiacTProvCassaFin() {
	}

	public Integer getProvcId() {
		return this.provcId;
	}

	public void setProvcId(Integer provcId) {
		this.provcId = provcId;
	}

	public Integer getProvcAnno() {
		return this.provcAnno;
	}

	public void setProvcAnno(Integer provcAnno) {
		this.provcAnno = provcAnno;
	}

	public String getProvcCausale() {
		return this.provcCausale;
	}

	public void setProvcCausale(String provcCausale) {
		this.provcCausale = provcCausale;
	}

	public Timestamp getProvcDataAnnullamento() {
		return this.provcDataAnnullamento;
	}

	public void setProvcDataAnnullamento(Timestamp provcDataAnnullamento) {
		this.provcDataAnnullamento = provcDataAnnullamento;
	}

	public Timestamp getProvcDataConvalida() {
		return this.provcDataConvalida;
	}

	public void setProvcDataConvalida(Timestamp provcDataConvalida) {
		this.provcDataConvalida = provcDataConvalida;
	}

	public Timestamp getProvcDataEmissione() {
		return this.provcDataEmissione;
	}

	public void setProvcDataEmissione(Timestamp provcDataEmissione) {
		this.provcDataEmissione = provcDataEmissione;
	}

	public String getProvcDenomSoggetto() {
		return this.provcDenomSoggetto;
	}

	public void setProvcDenomSoggetto(String provcDenomSoggetto) {
		this.provcDenomSoggetto = provcDenomSoggetto;
	}

	public BigDecimal getProvcImporto() {
		return this.provcImporto;
	}

	public void setProvcImporto(BigDecimal provcImporto) {
		this.provcImporto = provcImporto;
	}

	public BigDecimal getProvcNumero() {
		return this.provcNumero;
	}

	public void setProvcNumero(BigDecimal provcNumero) {
		this.provcNumero = provcNumero;
	}

	public String getProvcSubcausale() {
		return this.provcSubcausale;
	}

	public void setProvcSubcausale(String provcSubcausale) {
		this.provcSubcausale = provcSubcausale;
	}

	public List<SiacROrdinativoProvCassaFin> getSiacROrdinativoProvCassas() {
		return this.siacROrdinativoProvCassas;
	}

	public void setSiacROrdinativoProvCassas(List<SiacROrdinativoProvCassaFin> siacROrdinativoProvCassas) {
		this.siacROrdinativoProvCassas = siacROrdinativoProvCassas;
	}

	public SiacROrdinativoProvCassaFin addSiacROrdinativoProvCassa(SiacROrdinativoProvCassaFin siacROrdinativoProvCassa) {
		getSiacROrdinativoProvCassas().add(siacROrdinativoProvCassa);
		siacROrdinativoProvCassa.setSiacTProvCassa(this);

		return siacROrdinativoProvCassa;
	}

	public SiacROrdinativoProvCassaFin removeSiacROrdinativoProvCassa(SiacROrdinativoProvCassaFin siacROrdinativoProvCassa) {
		getSiacROrdinativoProvCassas().remove(siacROrdinativoProvCassa);
		siacROrdinativoProvCassa.setSiacTProvCassa(null);

		return siacROrdinativoProvCassa;
	}

	public List<SiacRPredocProvCassaFin> getSiacRPredocProvCassas() {
		return this.siacRPredocProvCassas;
	}

	public void setSiacRPredocProvCassas(List<SiacRPredocProvCassaFin> siacRPredocProvCassas) {
		this.siacRPredocProvCassas = siacRPredocProvCassas;
	}

	public SiacRPredocProvCassaFin addSiacRPredocProvCassa(SiacRPredocProvCassaFin siacRPredocProvCassa) {
		getSiacRPredocProvCassas().add(siacRPredocProvCassa);
		siacRPredocProvCassa.setSiacTProvCassa(this);

		return siacRPredocProvCassa;
	}

	public SiacRPredocProvCassaFin removeSiacRPredocProvCassa(SiacRPredocProvCassaFin siacRPredocProvCassa) {
		getSiacRPredocProvCassas().remove(siacRPredocProvCassa);
		siacRPredocProvCassa.setSiacTProvCassa(null);

		return siacRPredocProvCassa;
	}

	public List<SiacRSubdocProvCassaFin> getSiacRSubdocProvCassas() {
		return this.siacRSubdocProvCassas;
	}

	public void setSiacRSubdocProvCassas(List<SiacRSubdocProvCassaFin> siacRSubdocProvCassas) {
		this.siacRSubdocProvCassas = siacRSubdocProvCassas;
	}

	public SiacRSubdocProvCassaFin addSiacRSubdocProvCassa(SiacRSubdocProvCassaFin siacRSubdocProvCassa) {
		getSiacRSubdocProvCassas().add(siacRSubdocProvCassa);
		siacRSubdocProvCassa.setSiacTProvCassa(this);

		return siacRSubdocProvCassa;
	}

	public SiacRSubdocProvCassaFin removeSiacRSubdocProvCassa(SiacRSubdocProvCassaFin siacRSubdocProvCassa) {
		getSiacRSubdocProvCassas().remove(siacRSubdocProvCassa);
		siacRSubdocProvCassa.setSiacTProvCassa(null);

		return siacRSubdocProvCassa;
	}

	public SiacDProvCassaTipoFin getSiacDProvCassaTipo() {
		return this.siacDProvCassaTipo;
	}

	public void setSiacDProvCassaTipo(SiacDProvCassaTipoFin siacDProvCassaTipo) {
		this.siacDProvCassaTipo = siacDProvCassaTipo;
	}

	public Timestamp getProvcDataRegolarizzazione() {
		return provcDataRegolarizzazione;
	}

	public void setProvcDataRegolarizzazione(Timestamp provcDataRegolarizzazione) {
		this.provcDataRegolarizzazione = provcDataRegolarizzazione;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.provcId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.provcId = uid;
	}

	public List<SiacRProvCassaClassFin> getSiacRProvCassaClasses() {
		return siacRProvCassaClasses;
	}

	public void setSiacRProvCassaClasses(
			List<SiacRProvCassaClassFin> siacRProvCassaClasses) {
		this.siacRProvCassaClasses = siacRProvCassaClasses;
	}

	public Timestamp getProvcDataTrasmissione()
	{
		return provcDataTrasmissione;
	}

	public void setProvcDataTrasmissione(Timestamp provcDataTrasmissione)
	{
		this.provcDataTrasmissione = provcDataTrasmissione;
	}

	public Boolean getAccettato()
	{
		return accettato;
	}

	public void setAccettato(Boolean accettato)
	{
		this.accettato = accettato;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public String getProvcDescrizioneContoEvidenza() {
		return provcDescrizioneContoEvidenza;
	}

	public void setProvcDescrizioneContoEvidenza(String provcDescrizioneContoEvidenza) {
		this.provcDescrizioneContoEvidenza = provcDescrizioneContoEvidenza;
	}

	public String getProvcCodiceContoEvidenza() {
		return provcCodiceContoEvidenza;
	}

	public void setProvcCodiceContoEvidenza(String provcCodiceContoEvidenza) {
		this.provcCodiceContoEvidenza = provcCodiceContoEvidenza;
	}

	public Date getProvcDataInvioServizio() {
		return provcDataInvioServizio;
	}

	public void setProvcDataInvioServizio(Date provcDataInvioServizio) {
		this.provcDataInvioServizio = provcDataInvioServizio;
	}

	public Date getProvcDataRifiutoErrataAttribuzione() {
		return provcDataRifiutoErrataAttribuzione;
	}

	public void setProvcDataRifiutoErrataAttribuzione(Date provcDataRifiutoErrataAttribuzione) {
		this.provcDataRifiutoErrataAttribuzione = provcDataRifiutoErrataAttribuzione;
	}

	public Date getProvcDataPresaInCaricoServizio() {
		return provcDataPresaInCaricoServizio;
	}

	public void setProvcDataPresaInCaricoServizio(Date provcDataPresaInCaricoServizio) {
		this.provcDataPresaInCaricoServizio = provcDataPresaInCaricoServizio;
	}
	
}