/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.stampa.base.JAXBBaseReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.model.StampaGiornaleCassaIntestazione;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.model.StampaGiornaleCassaMovimento;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.model.StampaGiornaleCassaOperazioneCassa;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.model.StampaGiornaleCassaReportModel;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.giornale.model.StampaGiornaleCassaTotali;
import it.csi.siac.siacbilser.business.utility.DummyMapper;
import it.csi.siac.siacbilser.integration.dad.CassaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.MovimentoDad;
import it.csi.siac.siacbilser.integration.dad.OperazioneDiCassaDad;
import it.csi.siac.siacbilser.integration.dad.StampeCassaFileDad;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.StampaGiornale;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.StatoOperativoOperazioneCassa;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccecser.model.TipologiaOperazioneCassa;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaGiornaleCassaReportHandler extends JAXBBaseReportHandler<StampaGiornaleCassaReportModel> {
	
	// Costante per la verifica sul tipo di richiesta
	private static final String CODIFICA_TIPO_RICHIESTA_ECONOMALE_PAGAMENTO_FATTURE = "PAGAMENTO_FATTURE";
	
	//DADs
	@Autowired
	private CassaEconomaleDad cassaEconomaleDad;
	@Autowired
	private OperazioneDiCassaDad operazioneDiCassaDad;
	@Autowired
	private MovimentoDad movimentoDad;
	@Autowired 
	private StampeCassaFileDad stampeCassaFileDad;
	
	/*impostate da Service*/
	private Bilancio bilancio;
	private CassaEconomale cassaEconomale;
	private Date dataStampaGiornaleCassa;	
	private TipoStampa tipoStampa;
	private Integer primaPaginaDaStampare;
	private StampeCassaFile ultimaStampaDefinitiva;
	private String loginOperazione;

	private List<OperazioneCassa> listaOperazioneDiCassa = new ArrayList<OperazioneCassa>();
	private List<Movimento> listaMovimento =new ArrayList<Movimento>();
	private List<StampaGiornaleCassaMovimento> listaMovimentoPerStampa = new ArrayList<StampaGiornaleCassaMovimento>();
	private List<StampaGiornaleCassaOperazioneCassa> listaOperazioneDiCassaPerStampa = new ArrayList<StampaGiornaleCassaOperazioneCassa>();
	
	private List<OperazioneCassa> listaOperazioneDiCassaAnno = new ArrayList<OperazioneCassa>();
	private List<Movimento> listaMovimentoAnno =new ArrayList<Movimento>();
	
	private BigDecimal totaleDisponibilitaCC = BigDecimal.ZERO;
	private BigDecimal totaleDisponibilitaCO = BigDecimal.ZERO;

	private boolean isGestioneMultipla; //true se lì'ente gestisce piu casse;
	
	private StampaGiornaleCassaTotali totaliCC = new StampaGiornaleCassaTotali();
	private StampaGiornaleCassaTotali totaliCO = new StampaGiornaleCassaTotali();

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public void elaborate() {	
		super.elaborate();
	}
	
	@Override
	protected void elaborateData() {
		final String methodName = "elaborateData";
		log.debug(methodName, "Inizio elaborazione Report ");
		log.debug(methodName, "primapagina passata da service  " + primaPaginaDaStampare);
		impostaDads();
		cassaEconomale = cassaEconomaleDad.ricercaDettaglioCassaEconomale(cassaEconomale.getUid());
		result.setCassaEconomale(cassaEconomale);
		result.setTipoDiCassa(cassaEconomale.getTipoDiCassa());
		log.debug(methodName, "TipoCassa : " + cassaEconomale.getTipoDiCassa().getDescrizione());

		Long numeroCassePerEnte = cassaEconomaleDad.contaCassaEconomalePerEnte(getBilancio());
		isGestioneMultipla = numeroCassePerEnte > 0;
		log.debug(methodName, "L'ente gestisce piu' casse: " + isGestioneMultipla);
		log.debug(methodName, "elaborazione intestazione");
		
		// INTESTAZIONE
		StampaGiornaleCassaIntestazione intestazioneStampa = elaboraIntestazione();
		result.setIntestazione(intestazioneStampa);
	
		log.debug(methodName, "elaborazione movimenti");
		//SEZIONE1
		popolaMovimenti (); 
		
		//result.setMovimenti(listaMovimento);
		result.setMovimenti(listaMovimentoPerStampa);
		 
	
		//sezione 2//operazioni di cassa
		popolaOperazioniCassa();
		result.setOperazioniDiCassa(listaOperazioneDiCassaPerStampa);
		
		//O1
		log.debug(methodName, " I3 : " + totaliCC.getTotaleEntrate()  + " (Totale entrate CC )");
		log.debug(methodName, " L3 : " + totaliCC.getTotaleUscite() + " (Totale uscite CC  )");
		totaliCC.setSaldoTotale(totaliCC.getTotaleEntrate().subtract(totaliCC.getTotaleUscite()));
		log.debug(methodName, " O1 :  " + totaliCC.getSaldoTotale() + " (Saldo totale CC)");
		//O2
		log.debug(methodName, " M3 : " + totaliCO.getTotaleEntrate()+ " (Totale entrate CO)");
		log.debug(methodName, " N3 : " + totaliCO.getTotaleUscite()+ " (Totale uscite CO)");
		totaliCO.setSaldoTotale(totaliCO.getTotaleEntrate().subtract(totaliCO.getTotaleUscite()));
		log.debug(methodName, " O2 : " + totaliCO.getSaldoTotale()+ " (Saldo totaleCO)");
		//SE DEFINITIVA CAMBIANO I TOTALI
		//if (!isTipoStampaBozza()) {
		if ( this.getUltimaStampaDefinitiva()!= null) {

			result.setDataUltimaStampaDef(this.getUltimaStampaDefinitiva().getStampaGiornale().getDataUltimaStampa());
			if ((TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(cassaEconomale.getTipoDiCassa())) || (TipoDiCassa.MISTA.equals(cassaEconomale.getTipoDiCassa()))){
				//P2
				totaliCC.setUltTotaleCassaEntrate( this.getUltimaStampaDefinitiva().getStampaGiornale().getUltimoImportoEntrataCC());
				//P3
				totaliCC.setUltTotaleCassaUscite( this.getUltimaStampaDefinitiva().getStampaGiornale().getUltimoImportoUscitaCC());
				//Q1 = I3 + P2
				totaliCC.setTotaleRipEntrate(totaliCC.getTotaleEntrate().add(totaliCC.getUltTotaleCassaEntrate()));
				//Q2 = L3 + P3
				totaliCC.setTotaleRipUscite(totaliCC.getTotaleUscite().add(totaliCC.getUltTotaleCassaUscite()));
				//R1 = Q1-Q2
				totaliCC.setSaldoRiporto(totaliCC.getTotaleRipEntrate().subtract(totaliCC.getTotaleRipUscite()));
			}
			if ((TipoDiCassa.CONTANTI.equals(cassaEconomale.getTipoDiCassa())) || (TipoDiCassa.MISTA.equals(cassaEconomale.getTipoDiCassa()))){
				//P4 
				totaliCO.setUltTotaleCassaEntrate( this.getUltimaStampaDefinitiva().getStampaGiornale().getUltimoImportoEntrataContanti());
				//P5
				totaliCO.setUltTotaleCassaUscite( this.getUltimaStampaDefinitiva().getStampaGiornale().getUltimoImportoUscitaContanti());
				//Q3 = M3 + P4
				totaliCO.setTotaleRipEntrate(totaliCO.getTotaleEntrate().add(totaliCO.getUltTotaleCassaEntrate()));
				//Q4 = N3 + P5
				totaliCO.setTotaleRipUscite(totaliCO.getTotaleUscite().add(totaliCO.getUltTotaleCassaUscite()));
				//R2= Q3-Q4
				totaliCO.setSaldoRiporto(totaliCO.getTotaleRipEntrate().subtract(totaliCO.getTotaleRipUscite()));
			}
			
		} else { //non esistono dati di ultiuma definitiva
			if ((TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(cassaEconomale.getTipoDiCassa())) || (TipoDiCassa.MISTA.equals(cassaEconomale.getTipoDiCassa()))){
				//P2
				totaliCC.setUltTotaleCassaEntrate( BigDecimal.ZERO);
				//P3
				totaliCC.setUltTotaleCassaUscite( BigDecimal.ZERO);
				//Q1 = I3 + P2
				totaliCC.setTotaleRipEntrate(totaliCC.getTotaleEntrate());
				//Q2 = L3 + P3
				totaliCC.setTotaleRipUscite(totaliCC.getTotaleUscite());
				//R1 = Q1-Q2
				totaliCC.setSaldoRiporto(totaliCC.getTotaleRipEntrate().subtract(totaliCC.getTotaleRipUscite()));
			}
			if ((TipoDiCassa.CONTANTI.equals(cassaEconomale.getTipoDiCassa())) || (TipoDiCassa.MISTA.equals(cassaEconomale.getTipoDiCassa()))){
				//P4 
				totaliCO.setUltTotaleCassaEntrate( BigDecimal.ZERO);
				//P5
				totaliCO.setUltTotaleCassaUscite( BigDecimal.ZERO);
				//Q3 = M3 + P4
				totaliCO.setTotaleRipEntrate(totaliCO.getTotaleEntrate());
				//Q4 = N3 + P5
				totaliCO.setTotaleRipUscite(totaliCO.getTotaleUscite());
				//R2= Q3-Q4
				totaliCO.setSaldoRiporto(totaliCO.getTotaleRipEntrate().subtract(totaliCO.getTotaleRipUscite()));
			}
		}

		result.setTotaliCC(totaliCC);
		result.setTotaliCO(totaliCO);
		popolaMovimentiOperazioniCassaAnno ();
		calcolaTotaleDisponibilita();
		result.setTotaleDisponibilitaCassaCC(this.getTotaleDisponibilitaCC());
		result.setTotaleDisponibilitaCassaCO(this.getTotaleDisponibilitaCO());	
		
		result.setTotaleFondoCassa(this.getTotaleDisponibilitaCC().add(this.getTotaleDisponibilitaCO()));

	}
	
	private void impostaDads (){
		
		cassaEconomaleDad.setEnte(ente);
		cassaEconomaleDad.setLoginOperazione(loginOperazione);
		//movimentoDad.setLoginOperazione(loginOperazione);
		operazioneDiCassaDad.setEnte(ente);
		operazioneDiCassaDad.setLoginOperazione(loginOperazione);
	}
	
	private StampaGiornaleCassaIntestazione elaboraIntestazione() {
		final String methodName = "elaboraIntestazione";
		
		StampaGiornaleCassaIntestazione intestazione = new StampaGiornaleCassaIntestazione();
		intestazione.setEnte(cassaEconomale.getEnte());
		log.debug(methodName, "Ente Denominazione: " + cassaEconomale.getEnte().getNome());
		intestazione.setTipoStampa(tipoStampa);
		log.debug(methodName, "Tipo Stampa: " + tipoStampa.getDescrizione());
		// Il numero di pagina comincia sempre da 1
		intestazione.setNumeroDiPagina(primaPaginaDaStampare);
		log.debug(methodName, "Prima pagina da stampare: " + primaPaginaDaStampare);
		
		intestazione.setDirezione(cassaEconomale.getVariabiliStampa()!= null && cassaEconomale.getVariabiliStampa().getIntestazioneDirezione()!= null ? cassaEconomale.getVariabiliStampa().getIntestazioneDirezione() : "");
		log.debug(methodName, "Direzione: " + intestazione.getDirezione());
		intestazione.setSettore(cassaEconomale.getVariabiliStampa()!= null && cassaEconomale.getVariabiliStampa().getIntestazioneSettore()!= null ? cassaEconomale.getVariabiliStampa().getIntestazioneSettore() : "");
		log.debug(methodName, "Settore: " + intestazione.getSettore());
		
		if (isGestioneMultipla) {
			intestazione.setRiferimentoCassaEconomale(cassaEconomale.getCodice() + " " + cassaEconomale.getDescrizione());
		}else{
			intestazione.setRiferimentoCassaEconomale(this.cassaEconomale.getCodice());
		}
		log.debug(methodName, "Cassa Economale: " + intestazione.getRiferimentoCassaEconomale());
		intestazione.setDataStampaGiornale(dataStampaGiornaleCassa);
		log.debug(methodName, "Data Stampa Giornale: " + intestazione.getDataStampaGiornale());
		// L'anno di riferimento è presente all'interno del bilancio fornito in input
		intestazione.setAnnoDiRiferimentoContabile(bilancio.getAnno());
		log.debug(methodName, "Anno riferimento:  " + intestazione.getAnnoDiRiferimentoContabile());
		// Data di creazione del report
		intestazione.setDataCreazioneReport(new Date());
		log.debug(methodName, "Data Creazione Report: " + intestazione.getDataCreazioneReport());
		return intestazione;
		
	}
	
	private void popolaMovimenti() {
		final String methodName = "popolaMovimenti";
		listaMovimento = movimentoDad.findByDataMovimentoCassaEconId(dataStampaGiornaleCassa, cassaEconomale.getUid(), ente.getUid(), bilancio.getUid());
		calcoloParzialiListaMovimenti();
		log.debug(methodName, "numero movimenti : " +  listaMovimento.size());
		
	}
	private void calcoloParzialiListaMovimenti() {
		final String methodName = "calcoloParzialiListaMovimenti";
		for (Movimento movimento :listaMovimento ){
			log.debug(methodName, "NUOVA RIGA ");
			StampaGiornaleCassaMovimento movStampa = new StampaGiornaleCassaMovimento();
			DummyMapper.mapNotNullNotEmpty(movimento, movStampa);
			
			
			log.debug(methodName, "Movimento uid : " +  movimento.getUid());
			log.debug(methodName, "movStampa uid : " +  movStampa.getUid());
			log.debug(methodName, "A1: " + movStampa.getNumeroMovimento());
			log.debug(methodName, "B1: " + movStampa.getRichiestaEconomale().getTipoRichiestaEconomale().getDescrizione());
			if (movimento.getRichiestaEconomale().getSospeso()!=null ) {
				log.debug(methodName, "C1: " + movStampa.getRichiestaEconomale().getSospeso().getNumeroSospeso());
			}
			if (movimento.getRichiestaEconomale().getSoggetto()!=null ) {
				
				
				if (CODIFICA_TIPO_RICHIESTA_ECONOMALE_PAGAMENTO_FATTURE.equals(movStampa.getRichiestaEconomale().getTipoRichiestaEconomale().getCodice())) {
					List<SubdocumentoSpesa> listaSubSpesa = movStampa.getRichiestaEconomale().getSubdocumenti();
					for (SubdocumentoSpesa ss :listaSubSpesa ) {
						if (movStampa.getDenominazioneSoggetto().isEmpty()) {
							movStampa.setDenominazioneSoggetto(ss.getDocumento().getSoggetto().getDenominazione());
						}
					
					}
				} else {
					movStampa.setDenominazioneSoggetto(movStampa.getRichiestaEconomale().getSoggetto().getDenominazione());
				}

			} else {
				 movStampa.setDenominazioneSoggetto(movStampa.getRichiestaEconomale().getCognome()!=null ? movStampa.getRichiestaEconomale().getCognome():"" );
				
			}
			log.debug(methodName, "D1: Soggetto/Beneficiario " +  movStampa.getDenominazioneSoggetto());
			log.debug(methodName, "E1: " + movStampa.getRichiestaEconomale().getDescrizioneDellaRichiesta());
			
			computaDescrizioneModalitaPagamento(movStampa);
			log.debug(methodName, "F1: " + movStampa.getModalitaPagamentoPerStampa());
			computaAnnoCapitoloImpegno(movStampa);
			log.debug(methodName, "G1: " + movStampa.getAnnoCapitoloPerStampa()+"/" +movStampa.getNumeroCapitoloPerStampa()+"/"+movStampa.getArticoloCapitoloPerStampa());
			log.debug(methodName, "H1: " + movStampa.getRichiestaEconomale().getImpegno().getAnnoMovimento() + "/" +movStampa.getRichiestaEconomale().getImpegno().getNumeroBigDecimal());
			log.debug(methodName, "TipoCassa Movimento " + movimento.getModalitaPagamentoCassa().getTipoDiCassa());
			if (TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(movStampa.getModalitaPagamentoCassa().getTipoDiCassa())){
				
				String logInfoEntrate = "I1:";
				String logInfoUscite = "I1:";
				totaliCC = calcolaTotaliParzialiDaMovimento(movStampa,totaliCC, logInfoEntrate, logInfoUscite, true);
				
			}
			if (TipoDiCassa.CONTANTI.equals(movStampa.getModalitaPagamentoCassa().getTipoDiCassa())){
				
				String logInfoEntrate = "M1:";
				String logInfoUscite = "N1:";
				
				totaliCO = calcolaTotaliParzialiDaMovimento(movStampa, totaliCO, logInfoEntrate, logInfoUscite, false);
				
			}
			listaMovimentoPerStampa.add(movStampa);
		}
	
		
		
	}
	private void computaAnnoCapitoloImpegno (StampaGiornaleCassaMovimento movimentoStampa){
		String annoCapitoloPerStampa ="" + movimentoStampa.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione().getAnnoCapitolo();
		String numeroCapitoloPerStampa = "" + movimentoStampa.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione().getNumeroCapitolo();
		String articoloCapitoloPerStampa = "";
	
		if (movimentoStampa.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione().getNumeroArticolo()!=null) {
			articoloCapitoloPerStampa = "" + movimentoStampa.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione().getNumeroArticolo();
		}
		
		 
		if (movimentoStampa.getRendicontoRichiesta()!=null && movimentoStampa.getRendicontoRichiesta().getImpegno()!=null) {
			annoCapitoloPerStampa = "" + movimentoStampa.getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione().getAnnoCapitolo();
			numeroCapitoloPerStampa = "" + movimentoStampa.getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione().getNumeroCapitolo();
			
			if (movimentoStampa.getRendicontoRichiesta().getImpegno().getCapitoloUscitaGestione().getNumeroArticolo()!=null) {
				articoloCapitoloPerStampa = "" + movimentoStampa.getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione().getNumeroArticolo();
			}
				
		}
		
		movimentoStampa.setAnnoCapitoloPerStampa(annoCapitoloPerStampa);
		movimentoStampa.setNumeroCapitoloPerStampa(numeroCapitoloPerStampa);
		movimentoStampa.setArticoloCapitoloPerStampa(articoloCapitoloPerStampa);
		
	}
	
	private void computaDescrizioneModalitaPagamento (StampaGiornaleCassaMovimento movimentoStampa){
		String descrizione =  movimentoStampa.getModalitaPagamentoDipendente().getDescrizione();
		if (TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(movimentoStampa.getModalitaPagamentoCassa().getTipoDiCassa()) && movimentoStampa.getIban()!=null){
			StringBuilder sb = new StringBuilder();
			sb.append(movimentoStampa.getIban()); 
			sb.append(movimentoStampa.getModalitaPagamentoDipendente().getCodice()).append(" ");
			sb.append(movimentoStampa.getModalitaPagamentoDipendente().getDescrizione()).append(" ");
			sb.append(movimentoStampa.getBic()).append(" ");
			sb.append(movimentoStampa.getCin()).append(" ");
			sb.append(movimentoStampa.getContoCorrente());
			descrizione = sb.toString();
		}

		movimentoStampa.setModalitaPagamentoPerStampa(descrizione);
	}
	
	private void popolaOperazioniCassa() {
		final String methodName = "popolaOperazioniCassa";
		TipoOperazioneCassa top = new TipoOperazioneCassa();
		top.setInclusoInGiornale(true);
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();		
		parametriPaginazione.setNumeroPagina(0);
		parametriPaginazione.setElementiPerPagina(Integer.MAX_VALUE);
		//recuperto i tipidioperazionecassa
		List<TipoOperazioneCassa> listaTipoOperazioneCassa= operazioneDiCassaDad.ricercaTipoOperazioneCassa(cassaEconomale);
	
		for(TipoOperazioneCassa tipoOperazioneCassa : listaTipoOperazioneCassa) {
			if (tipoOperazioneCassa.getInclusoInGiornale()) {
			//	List<OperazioneCassa> listaOperazioneCassaPerTipoOp = operazioneDiCassaDad.ricercaSinteticaOperazioneDiCassa(bilancio, cassaEconomale, dataStampaGiornaleCassa, tipoOperazioneCassa, null, parametriPaginazione);
				List<OperazioneCassa> listaOperazioneCassaPerTipoOp = operazioneDiCassaDad.ricercaOperazioniCassaByDataPerStampaGiornale(bilancio, cassaEconomale, dataStampaGiornaleCassa, tipoOperazioneCassa);
				
				if (listaOperazioneCassaPerTipoOp != null && !listaOperazioneCassaPerTipoOp.isEmpty()) {
					listaOperazioneDiCassa.addAll(listaOperazioneCassaPerTipoOp);
				}
			}
		}
		
		calcoloParzialeListaOperazioni();
		log.debug(methodName, "numero operazioni cassa: " +  listaOperazioneDiCassa.size());
	}
	private void calcoloParzialeListaOperazioni(){
		final String methodName = "calcoloParzialeListaOperazioni";
		for(OperazioneCassa operazioneCassa : listaOperazioneDiCassa) {
			log.debug(methodName, "NUOVA RIGA ");
			StampaGiornaleCassaOperazioneCassa operazioneStampa = new StampaGiornaleCassaOperazioneCassa();
			DummyMapper.mapNotNullNotEmpty(operazioneCassa, operazioneStampa);
			log.debug(methodName, "OperazioneCassa uid : " +  operazioneStampa.getUid());
			log.debug(methodName, "OperazioneCassa  DESCRIZIONE: " +  operazioneStampa.getTipoOperazioneCassa().getDescrizione());
			if (TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(operazioneStampa.getModalitaPagamentoCassa().getTipoDiCassa())){
				
				String logInfoEntrate = "I2:";
				String logInfoUscite = "L2:";
				totaliCC = calcolaTotaliParzialiDaOperazioneCassa(operazioneStampa,totaliCC, logInfoEntrate, logInfoUscite, true);
				
			}
			if (TipoDiCassa.CONTANTI.equals(operazioneStampa.getModalitaPagamentoCassa().getTipoDiCassa())){
				
				String logInfoEntrate = "M2:";
				String logInfoUscite = "N2:";
				totaliCO = calcolaTotaliParzialiDaOperazioneCassa(operazioneStampa, totaliCO, logInfoEntrate, logInfoUscite, false);
				
			}
			String descrizioneOp = operazioneCassa.getNumeroOperazione() + " - " + operazioneCassa.getTipoOperazioneCassa().getDescrizione() + " - "  + operazioneCassa.getTipoOperazioneCassa().getTipologiaOperazioneCassa().getDescrizione();
			operazioneStampa.setDescrizione(descrizioneOp);
			listaOperazioneDiCassaPerStampa.add(operazioneStampa);
		}
		
	}
	
	
	
	
	

	private StampaGiornaleCassaTotali calcolaTotaliParzialiDaMovimento(StampaGiornaleCassaMovimento movimentoStampa, StampaGiornaleCassaTotali totali, String logInfoEntrate, String logInfoUscite, boolean isCC) {
		final String methodName = "calcolaTotaliParzialiDaMovimento";
		
		// Controllo che la richiesta non sia annullata
		log.error(methodName, "[uid = " + movimentoStampa.getUid() + "] - Richiesta economale null ? " + (movimentoStampa.getRichiestaEconomale() != null)
				+ " - stato richiesta = " + (movimentoStampa.getRichiestaEconomale() != null ? movimentoStampa.getRichiestaEconomale().getStatoOperativoRichiestaEconomale() : "<null>"));
//		if(movimentoStampa.getRichiestaEconomale() != null && StatoOperativoRichiestaEconomale.ANNULLATA.equals(movimentoStampa.getRichiestaEconomale().getStatoOperativoRichiestaEconomale())) {
//			log.debug(methodName, "Richiesta annullata: ignoro l'impostazione dei totali [uid = " + movimentoStampa.getUid() + "]");
//			return totali;
//		}
		
		BigDecimal importoEntrate = BigDecimal.ZERO;
		BigDecimal importoUscite = BigDecimal.ZERO;
		//verifico se rendioconto o richiesta
		if (movimentoStampa.getRendicontoRichiesta()!=null) { //sono in un rendiconto
			log.debug(methodName, " RENDICONTO RICHIESTA");
			if (movimentoStampa.getRendicontoRichiesta().getImportoIntegrato()!=null) {
				log.debug(methodName, logInfoUscite + " ImportoIntegrato " + movimentoStampa.getRendicontoRichiesta().getImportoIntegrato());
				log.debug(methodName, " Totale uscite parziale precendete : " + totali.getTotaleUscite() );	
				totali.setTotaleUscite(	totali.getTotaleUscite().add(movimentoStampa.getRendicontoRichiesta().getImportoIntegrato()));
				log.debug(methodName, " Totale uscite parziale nuovo : " + totali.getTotaleUscite() );	
				importoUscite = movimentoStampa.getRendicontoRichiesta().getImportoIntegrato();
			} 
			if (movimentoStampa.getRendicontoRichiesta().getImportoRestituito()!=null) {
				log.debug(methodName, logInfoEntrate + " ImportoRestituito " + movimentoStampa.getRendicontoRichiesta().getImportoRestituito());
				log.debug(methodName, " Totale entrate parziale precendete : " + totali.getTotaleEntrate() );	
				totali.setTotaleEntrate(totali.getTotaleEntrate().add(movimentoStampa.getRendicontoRichiesta().getImportoRestituito()));
				log.debug(methodName, " Totale entrate parziale nuovo : " + totali.getTotaleEntrate() );
				importoEntrate = movimentoStampa.getRendicontoRichiesta().getImportoRestituito();
			}
		} else if (movimentoStampa.getRichiestaEconomale()!=null) { //richiesta
			log.debug(methodName, " RICHIESTA");
			log.debug(methodName, logInfoUscite + " Importo " + movimentoStampa.getRichiestaEconomale().getImporto());
			log.debug(methodName, " Totale uscite parziale precendete : " + totali.getTotaleUscite() );	
			//JIRA 3284 MOSTRO l'importo ma non lo considero nel totale per le richieste annullate 
			totali.setTotaleUscite(	totali.getTotaleUscite().add(StatoOperativoRichiestaEconomale.ANNULLATA.equals(movimentoStampa.getRichiestaEconomale().getStatoOperativoRichiestaEconomale()) ? BigDecimal.ZERO : movimentoStampa.getRichiestaEconomale().getImporto()));
			log.debug(methodName, " Totale uscite parziale nuovo : " + totali.getTotaleUscite() );	
			importoUscite = movimentoStampa.getRichiestaEconomale().getImporto();
		} else {
			log.debug(methodName, "nessun importo legato alla richiesta");
		}
		if (isCC) {
			movimentoStampa.setImportoMovEntrataCC(importoEntrate);
			movimentoStampa.setImportoMovUscitaCC(importoUscite);
		} else {
			movimentoStampa.setImportoMovEntrataCO(importoEntrate);
			movimentoStampa.setImportoMovUscitaCO(importoUscite);
		}
		return totali;
	}
	
	private StampaGiornaleCassaTotali calcolaTotaliParzialiDaOperazioneCassa(StampaGiornaleCassaOperazioneCassa opStampa, StampaGiornaleCassaTotali totali,String logInfoEntrate, String logInfoUscite, boolean isCC) {
		final String methodName = "calcolaTotaliParzialiDaOperazioneCassa";
		
		log.error(methodName, "[uid = " + opStampa.getUid() + "] - stato operazione = " + opStampa.getStatoOperativoOperazioneCassa());
		
		// Controllo che la richiesta non sia annullata
//		if(StatoOperativoOperazioneCassa.ANNULLATO.equals(opStampa.getStatoOperativoOperazioneCassa())) {
//			log.debug(methodName, "Operazione di cassa annullata: ignoro l'impostazione dei totali [uid = " + opStampa.getUid() + "]");
//			return totali;
//		}
		
		//verifico se rendioconto o richiesta
		if (opStampa.getImporto()!=null) {
			
			if (TipologiaOperazioneCassa.SPESA.equals(opStampa.getTipoOperazioneCassa().getTipologiaOperazioneCassa())) {
				log.debug(methodName, logInfoUscite + " Importo " + opStampa.getImporto());
				log.debug(methodName, " Totale uscite parziale precendete : " + totali.getTotaleUscite() );	
				//JIRA 3284 MOSTRO l'importo ma non lo considero nel totale per le richieste annullate 
				totali.setTotaleUscite(	totali.getTotaleUscite().add(StatoOperativoOperazioneCassa.ANNULLATO.equals(opStampa.getStatoOperativoOperazioneCassa()) ? BigDecimal.ZERO : opStampa.getImporto()));
				log.debug(methodName, " Totale uscite parziale nuovo : " + totali.getTotaleUscite() );	
				if (isCC) {
					opStampa.setImportoOpUscitaCC(opStampa.getImporto());
				} else {
					opStampa.setImportoOpUscitaCO(opStampa.getImporto());
				}
							
			} else{
				log.debug(methodName, logInfoEntrate + " Importo " + opStampa.getImporto());
				log.debug(methodName, " Totale entrate parziale precendete : " + totali.getTotaleEntrate() );	
				//JIRA 3284 MOSTRO l'importo ma non lo considero nel totale per le richieste annullate 
				totali.setTotaleEntrate(totali.getTotaleEntrate().add(StatoOperativoOperazioneCassa.ANNULLATO.equals(opStampa.getStatoOperativoOperazioneCassa()) ? BigDecimal.ZERO : opStampa.getImporto()));
				log.debug(methodName, " Totale entrate parziale nuovo : " + totali.getTotaleEntrate() );
				if (isCC) {
					opStampa.setImportoOpEntrataCC(opStampa.getImporto());

				} else {
					opStampa.setImportoOpEntrataCO(opStampa.getImporto());

				}
			} 
		} else {
			log.debug(methodName,  " Importo dell'operazione nullo uid: " + opStampa.getUid());
		}

		return totali;
	}


	
	private BigDecimal calcolaTotaliMovimentoAnno (Movimento movimento) {
		BigDecimal totaleCalcolato = BigDecimal.ZERO;
		//verifico se rendioconto o richiesta
		if (movimento.getRendicontoRichiesta()!=null) { // è rendiconto
			if (movimento.getRendicontoRichiesta().getImportoIntegrato()!=null && movimento.getRendicontoRichiesta().getImportoIntegrato().signum()>0 ) {
				//totaleCalcolato = totaleCalcolato.add(movimento.getRendicontoRichiesta().getImportoIntegrato());
				totaleCalcolato = totaleCalcolato.subtract(movimento.getRendicontoRichiesta().getImportoIntegrato());

			} else {
				totaleCalcolato = totaleCalcolato.add(movimento.getRendicontoRichiesta().getImportoRestituito());
			}
		} else if (movimento.getRichiestaEconomale()!=null &&(movimento.getRichiestaEconomale().getStatoOperativoRichiestaEconomale() != null && !StatoOperativoRichiestaEconomale.ANNULLATA.equals(movimento.getRichiestaEconomale().getStatoOperativoRichiestaEconomale()))) {
			totaleCalcolato = totaleCalcolato.subtract(movimento.getRichiestaEconomale().getImporto());
		} else {
			//TODO gestione errore
		}
		return totaleCalcolato;
	}
	
	private BigDecimal calcolaTotaliOperazioneAnno(OperazioneCassa operazione) {
		final String methodName = "calcolaTotaliOperazioneAnno";
		
		log.error(methodName, "[uid = " + operazione.getUid() + "] - stato operazione = " + operazione.getStatoOperativoOperazioneCassa());
		// Se annullato non lo considero
		if(StatoOperativoOperazioneCassa.ANNULLATO.equals(operazione.getStatoOperativoOperazioneCassa())) {
			log.debug(methodName, "operazione annullata: non ne considero i totali [uid = " + operazione.getUid() + "]");
			return BigDecimal.ZERO;
		}
		
		BigDecimal totaleCalcolato = BigDecimal.ZERO;
		//verifico se rendioconto o richiesta
		if (operazione.getImporto()!=null) {
			if (TipologiaOperazioneCassa.ENTRATA.equals(operazione.getTipoOperazioneCassa().getTipologiaOperazioneCassa())) {
				//FIXME: abs aggiunto per evitare problemi nei conti del totale se considerate le vecchie operazioniCassa con val negativo
				totaleCalcolato = totaleCalcolato.add(operazione.getImporto().abs()); 
			} else {
				//FIXME: abs aggiunto per evitare problemi nei conti del totale se considerate le vecchie operazioniCassa con val negativo
				totaleCalcolato = totaleCalcolato.subtract(operazione.getImporto().abs());
			}	
			//totaleCalcolato =  totaleCalcolato.add(operazione.getImporto());
				
		} else {
			log.debug(methodName, " Importo dell'operazione nullo uid: " + operazione.getUid());
		}
		return totaleCalcolato;
	}
	
	private void calcolaTotaleDisponibilita(){
		for(Movimento movimento : this.getListaMovimentoAnno()) {
			if (TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(movimento.getModalitaPagamentoCassa().getTipoDiCassa())){
				this.setTotaleDisponibilitaCC(getTotaleDisponibilitaCC().add(calcolaTotaliMovimentoAnno(movimento)));
				
			}
			if (TipoDiCassa.CONTANTI.equals(movimento.getModalitaPagamentoCassa().getTipoDiCassa())){
			
				this.setTotaleDisponibilitaCO(getTotaleDisponibilitaCO().add(calcolaTotaliMovimentoAnno(movimento)));
				
			}

		}
		for(OperazioneCassa operazioneCassa : this.getListaOperazioneDiCassaAnno()) {
			if (TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(operazioneCassa.getModalitaPagamentoCassa().getTipoDiCassa())){
				this.setTotaleDisponibilitaCC(this.getTotaleDisponibilitaCC().add(calcolaTotaliOperazioneAnno(operazioneCassa)));
				
			}
			if (TipoDiCassa.CONTANTI.equals(operazioneCassa.getModalitaPagamentoCassa().getTipoDiCassa())){
				this.setTotaleDisponibilitaCO(getTotaleDisponibilitaCO().add(calcolaTotaliOperazioneAnno(operazioneCassa)));
				
			}
		}
		
		
	}
	
	
	private void popolaMovimentiOperazioniCassaAnno() {
		final String methodName = "popolaMovimentiAnno";
		Date firstDayBil = new GregorianCalendar(getBilancio().getAnno(), 0, 1).getTime();
		listaMovimentoAnno = movimentoDad.findAllMovimentiBilancioTillDateCassaEconId(firstDayBil, dataStampaGiornaleCassa, cassaEconomale.getUid(), ente.getUid(), bilancio.getUid());
		log.debug(methodName, "numero movimenti Da inizio anno: " +  listaMovimentoAnno.size());
		operazioneDiCassaDad.setEnte(ente);
		List<TipoOperazioneCassa> listaTipoOperazioneCassa= operazioneDiCassaDad.ricercaTipoOperazioneCassa(cassaEconomale);
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();		
		parametriPaginazione.setNumeroPagina(0);
		parametriPaginazione.setElementiPerPagina(Integer.MAX_VALUE);
		for(TipoOperazioneCassa tipoOperazioneCassa : listaTipoOperazioneCassa) {
			if (tipoOperazioneCassa.getInclusoInGiornale()) {
			//	List<OperazioneCassa> listaOperazioneCassaPerTipoOp = operazioneDiCassaDad.ricercaSinteticaOperazioneDiCassa(bilancio, cassaEconomale, null, tipoOperazioneCassa, null, parametriPaginazione);
				List<OperazioneCassa> listaOperazioneCassaPerTipoOp = operazioneDiCassaDad.ricercaOperazioniCassaByPeriodoPerStampaGiornale(bilancio, cassaEconomale, firstDayBil, dataStampaGiornaleCassa, tipoOperazioneCassa);
				if (listaOperazioneCassaPerTipoOp != null && !listaOperazioneCassaPerTipoOp.isEmpty()) {
					listaOperazioneDiCassaAnno.addAll(listaOperazioneCassaPerTipoOp);
				}
			}
		}
		//listaOperazioneDiCassaAnno = movimentoDad.findByBilancioMovimentoCassaEconId(getBilancio().getUid(), cassaEconomale.getUid(), ente.getUid());
		log.debug(methodName, "numero operazioni di cassa da inizio anno: " + listaOperazioneDiCassaAnno.size());
		
	}


	
	

	@Override
	public String getCodiceTemplate() {
		return "StampaGiornaleCassa";
	}
	/**
	 * Persiste la stampa IVA su database.
	 * 
	 * @param res la risposta del metodo di generazione del report
	 */
	private void persistiStampaFile(GeneraReportResponse res) {
		final String methodName = "persistiStampaFile";
		log.debug(methodName, "Persistenza della stampa");
		stampeCassaFileDad.setEnte(ente);
		stampeCassaFileDad.setLoginOperazione(getRichiedente().getOperatore().getCodiceFiscale());
		StampeCassaFile stampeCF = stampeCassaFileDad.inserisciStampa(creaStampaGiornale(res));
	
		
		log.info(methodName, "Stampa terminata con successo. Uid record inserito su database: " + stampeCF.getUid());
	}
	
	/**
	 * Crea una StampaIva per la persistenza.
	 * 
	 * @param res la response della generazione del report
	 * @return la stampa iva creata
	 */
	private StampeCassaFile creaStampaGiornale(GeneraReportResponse res) {
		final String methodName = "creaStampaGiornale";
		
		Integer numeroPagineGenerate = res.getNumeroPagineGenerate();
		log.debug(methodName, "numeroPagineGenerate :" + numeroPagineGenerate);
		log.debug(methodName, "primaPaginaDaStampare :" + primaPaginaDaStampare);
		Integer ultimaPagina = numeroPagineGenerate.intValue() + primaPaginaDaStampare.intValue() - 1;
		StampeCassaFile result = new StampeCassaFile();
		
		
		result.setAnnoEsercizio(getBilancio().getAnno());
		
		File file = res.getReport();
		
		result.setCodice(file.getCodice());
		
		result.setEnte(getEnte());
		result.setCassaEconomale(this.cassaEconomale);
		result.setBilancio(getBilancio());
		
		result.setTipoStampa(getTipoStampa());
		result.setTipoDocumento(TipoDocumento.GIORNALE_CASSA);
		

		List<File> listaFile = new ArrayList<File>();
		
		listaFile.add(file);
		result.setFiles(listaFile);
		StampaGiornale sg = new StampaGiornale();
		sg.setCassaEconomale(this.cassaEconomale);
		//salvo comuqnue la data  per poter reperire in seguito la Stampa in base al criterio Data Giornale
		sg.setDataUltimaStampa(getDataStampaGiornaleCassa());
		if (TipoStampa.DEFINITIVA.equals(getTipoStampa())) {
			// salvo i dati necessari
			sg.setUltimaPaginaStampataDefinitiva(ultimaPagina);
			sg.setUltimoImportoEntrataCC(totaliCC.getTotaleRipEntrate());
			sg.setUltimoImportoEntrataContanti(totaliCO.getTotaleRipEntrate());
			sg.setUltimoImportoUscitaCC(totaliCC.getTotaleRipUscite());
			sg.setUltimoImportoUscitaContanti(totaliCO.getTotaleRipUscite());
			//Aggiorno lo stato delle operaizopni di cassa
			for (OperazioneCassa op : listaOperazioneDiCassa){
				op.setStatoOperativoOperazioneCassa(StatoOperativoOperazioneCassa.DEFINITIVO);
				operazioneDiCassaDad.aggiornaStatoOperazioneCassa(op);
				
			}
			
		}
		result.setStampaGiornale(sg);
		return result;
	}
	
	
	@Override
	protected void handleResponse(GeneraReportResponse res) {
		final String methodName = "handleResponse";
		log.debug(methodName, "numero di pagine generata: "+ res.getNumeroPagineGenerate());
		
		persistiStampaFile(res);
	}

	/**
	 * @param cassaEconomale the cassaEconomale to set
	 */
	public void setCassaEconomale(CassaEconomale cassaEconomale) {
		this.cassaEconomale = cassaEconomale;
	}


	

	/**
	 * @return the dataStampaGiornaleCassa
	 */
	public Date getDataStampaGiornaleCassa() {
		return dataStampaGiornaleCassa == null ? null : new Date(dataStampaGiornaleCassa.getTime());
	}


	/**
	 * @param dataStampaGiornaleCassa the dataStampaGiornaleCassa to set
	 */
	public void setDataStampaGiornaleCassa(Date dataStampaGiornaleCassa) {
		this.dataStampaGiornaleCassa = dataStampaGiornaleCassa == null ? null : new Date(dataStampaGiornaleCassa.getTime());
	}


	/**
	 * @return the tipoStampa
	 */
	public TipoStampa getTipoStampa() {
		return tipoStampa;
	}


	/**
	 * @param tipoStampa the tipoStampa to set
	 */
	public void setTipoStampa(TipoStampa tipoStampa) {
		this.tipoStampa = tipoStampa;
	}


	/**
	 * @return the bilancio
	 */
	public Bilancio getBilancio() {
		return bilancio;
	}


	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	/**
	 * @return the totaleDisponibilitaCC
	 */
	private BigDecimal getTotaleDisponibilitaCC() {
		return totaleDisponibilitaCC;
	}

	/**
	 * @param totaleDisponibilitaCC the totaleDisponibilitaCC to set
	 */
	private void setTotaleDisponibilitaCC(BigDecimal totaleDisponibilitaCC) {
		this.totaleDisponibilitaCC = totaleDisponibilitaCC;
	}

	/**
	 * @return the totaleDisponibilitaCO
	 */
	private BigDecimal getTotaleDisponibilitaCO() {
		return totaleDisponibilitaCO;
	}

	/**
	 * @param totaleDisponibilitaCO the totaleDisponibilitaCO to set
	 */
	private void setTotaleDisponibilitaCO(BigDecimal totaleDisponibilitaCO) {
		this.totaleDisponibilitaCO = totaleDisponibilitaCO;
	}


	/**
	 * @param primaPaginaDaStampare the primaPaginaDaStampare to set
	 */
	public void setPrimaPaginaDaStampare(Integer primaPaginaDaStampare) {
		this.primaPaginaDaStampare = primaPaginaDaStampare;
	}
	
	/**
	 * @return the listaOperazioneDiCassa
	 */
	public List<OperazioneCassa> getListaOperazioneDiCassa() {
		return listaOperazioneDiCassa;
	}

	/**
	 * @param listaOperazioneDiCassa the listaOperazioneDiCassa to set
	 */
	public void setListaOperazioneDiCassa(
			List<OperazioneCassa> listaOperazioneDiCassa) {
		this.listaOperazioneDiCassa = listaOperazioneDiCassa;
	}

	/**
	 * @return the listaMovimento
	 */
	public List<Movimento> getListaMovimento() {
		return listaMovimento;
	}

	/**
	 * @param listaMovimento the listaMovimento to set
	 */
	public void setListaMovimento(List<Movimento> listaMovimento) {
		this.listaMovimento = listaMovimento;
	}

	/**
	 * @return the listaOperazioneDiCassaAnno
	 */
	public List<OperazioneCassa> getListaOperazioneDiCassaAnno() {
		return listaOperazioneDiCassaAnno;
	}

	/**
	 * @param listaOperazioneDiCassaAnno the listaOperazioneDiCassaAnno to set
	 */
	public void setListaOperazioneDiCassaAnno(
			List<OperazioneCassa> listaOperazioneDiCassaAnno) {
		this.listaOperazioneDiCassaAnno = listaOperazioneDiCassaAnno;
	}

	/**
	 * @return the listaMovimentoAnno
	 */
	public List<Movimento> getListaMovimentoAnno() {
		return listaMovimentoAnno;
	}

	/**
	 * @param listaMovimentoAnno the listaMovimentoAnno to set
	 */
	public void setListaMovimentoAnno(List<Movimento> listaMovimentoAnno) {
		this.listaMovimentoAnno = listaMovimentoAnno;
	}

	/**
	 * @return the ultimaStampDefinitiva
	 */
	public StampeCassaFile getUltimaStampaDefinitiva() {
		return ultimaStampaDefinitiva;
	}

	/**
	 * @param ultimaStampDefinitiva the ultimaStampDefinitiva to set
	 */
	public void setUltimaStampaDefinitiva(StampeCassaFile ultimaStampaDefinitiva) {
		this.ultimaStampaDefinitiva = ultimaStampaDefinitiva;
	}

	/**
	 * @return the loginOperazione
	 */
	public String getLoginOperazione() {
		return loginOperazione;
	}

	/**
	 * @param loginOperazione the loginOperazione to set
	 */
	public void setLoginOperazione(String loginOperazione) {
		this.loginOperazione = loginOperazione;
	}
	
	
}
