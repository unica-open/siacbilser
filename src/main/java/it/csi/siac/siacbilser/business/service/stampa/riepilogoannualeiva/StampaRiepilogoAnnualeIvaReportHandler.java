/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.registroiva.InserisceStampaIvaService;
import it.csi.siac.siacbilser.business.service.stampa.base.JAXBBaseReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.model.StampaRiepilogoAnnualeIvaAcquistiIva;
import it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.model.StampaRiepilogoAnnualeIvaAcquistoIva;
import it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.model.StampaRiepilogoAnnualeIvaIntestazione;
import it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.model.StampaRiepilogoAnnualeIvaReportModel;
import it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.model.StampaRiepilogoAnnualeIvaVenditaIva;
import it.csi.siac.siacbilser.business.service.stampa.riepilogoannualeiva.model.StampaRiepilogoAnnualeIvaVenditeIva;
import it.csi.siac.siacbilser.integration.dad.EnteDad;
import it.csi.siac.siacbilser.integration.dad.ProgressiviIvaDad;
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaIvaResponse;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.TipoOperazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRiepilogoAnnualeIvaReportHandler extends JAXBBaseReportHandler<StampaRiepilogoAnnualeIvaReportModel> {
	
	@Autowired
	private InserisceStampaIvaService inserisceStampaIvaService;
	
	private List<RegistroIva> listaRegistriIva;
	
	@Autowired
	private RegistroIvaDad registroIvaDad;
	
	@Autowired
	private ProgressiviIvaDad progressiviIvaDad;
	
	@Autowired
	private EnteDad enteDad;
	
	private Bilancio bilancio;
	private GruppoAttivitaIva gruppoAttivitaIva;
	
	
	
	@Override
	public String getCodiceTemplate() {
		return "StampaRiepilogoIvaAnnuale";
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public void elaborate() {	
		super.elaborate();
	}
	
	@Override
	protected void elaborateData() {
		final String methodName = "elaborateData";
		popolaListaRegistri();
//		popolaListaSubdocumentiIva();
		
		log.debug(methodName, "elaborazione intestazione");
		StampaRiepilogoAnnualeIvaIntestazione intestazione = elaboraIntestazione();
		result.setIntestazione(intestazione);
		
		//Per il gruppo selezionato -> listaRegistri (di un tipo) -> listaSubodcumentiIva di quel registro -> lista aliquote del subdocumentoIva
		
		log.debug(methodName, "elaborazione acquistiIvaImmediata");
		StampaRiepilogoAnnualeIvaAcquistiIva acquistiIvaImmediata = elaboraAcquistiIvaDef(TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA);
		result.setAcquistiIvaImmediata(acquistiIvaImmediata);
		
		log.debug(methodName, "elaborazione acquistiIvaDifferita");
		StampaRiepilogoAnnualeIvaAcquistiIva acquistiIvaDifferita = elaboraAcquistiIvaProvv(TipoRegistroIva.ACQUISTI_IVA_DIFFERITA);
		result.setAcquistiIvaDifferita(acquistiIvaDifferita);
		
		log.debug(methodName, "elaborazione acquistiIvaDifferitaPagata");
		StampaRiepilogoAnnualeIvaAcquistiIva acquistiIvaDifferitaPagata = elaboraAcquistiIvaDef(TipoRegistroIva.ACQUISTI_IVA_DIFFERITA);
		result.setAcquistiIvaDifferitaPagata(acquistiIvaDifferitaPagata);

		log.debug(methodName, "elaborazione venditeIvaImmediata");
		StampaRiepilogoAnnualeIvaVenditeIva venditeIvaImmediata = elaboraVenditeIvaDef(TipoRegistroIva.VENDITE_IVA_IMMEDIATA);
		result.setVenditeIvaImmediata(venditeIvaImmediata);
		
		log.debug(methodName, "elaborazione venditeIvaDifferita");
		StampaRiepilogoAnnualeIvaVenditeIva venditeIvaDifferita = elaboraVenditeIvaProvv(TipoRegistroIva.VENDITE_IVA_DIFFERITA);
		result.setVenditeIvaDifferita(venditeIvaDifferita);
		
		log.debug(methodName, "elaborazione venditeIvaDifferitaIncassati");
		StampaRiepilogoAnnualeIvaVenditeIva venditeIvaDifferitaIncassati = elaboraVenditeIvaDef(TipoRegistroIva.VENDITE_IVA_DIFFERITA);
		result.setVenditeIvaDifferitaIncassati(venditeIvaDifferitaIncassati);		
		
		log.debug(methodName, "elaborazione corrispettivi");
		StampaRiepilogoAnnualeIvaVenditeIva corrispettivi = elaboraVenditeIvaDef(TipoRegistroIva.CORRISPETTIVI);
		result.setCorrispettivi(corrispettivi);
		
		
		
		

	}
	
	private StampaRiepilogoAnnualeIvaIntestazione elaboraIntestazione() {
		StampaRiepilogoAnnualeIvaIntestazione intestazione = new StampaRiepilogoAnnualeIvaIntestazione();
		
		// Ottengo i dati dell'ente da db
		// Ottengo soggetto associato all'ente e indirizzo
		Soggetto soggetto = enteDad.getSoggettoByEnte(ente);
		IndirizzoSoggetto indirizzoSoggetto = enteDad.getIndirizzoSoggettoPrincipaleIvaByEnte(ente);
		
		intestazione.setEnte(ente);
		intestazione.setSoggetto(soggetto);
		intestazione.setIndirizzoSoggetto(indirizzoSoggetto);
		
		// Data di creazione del report
		intestazione.setDataCreazioneReport(new Date());
		
		// TODO: titolo?
		// TODO: tipoStampa?
		
		// L'anno di riferimento è presente all'interno del bilancio fornito in input
		intestazione.setAnnoDiRiferimentoContabile(bilancio.getAnno());
		
		// Il numero di pagina comincia sempre da 1
		intestazione.setNumeroDiPagina(1);
		
		// GruppoAttivitaIva
		intestazione.setGruppoAttivitaIva(gruppoAttivitaIva);
		
		return intestazione;
	}

	private StampaRiepilogoAnnualeIvaVenditeIva elaboraVenditeIvaDef(TipoRegistroIva tipoRegistroIva) {
		return elaboraVenditeIva(tipoRegistroIva, true);
	}
	
	private StampaRiepilogoAnnualeIvaVenditeIva elaboraVenditeIvaProvv(TipoRegistroIva tipoRegistroIva) {
		return elaboraVenditeIva(tipoRegistroIva, false);
	}
	
	private StampaRiepilogoAnnualeIvaVenditeIva elaboraVenditeIva(TipoRegistroIva tipoRegistroIva, boolean isDefinitivo) {
		StampaRiepilogoAnnualeIvaVenditeIva venditeIva = new StampaRiepilogoAnnualeIvaVenditeIva();
		
		List<AliquotaIva> aliquoteIva = registroIvaDad.findAliquoteByGruppoAttivitaIvaAndTipoRegistroIvaAndSubDocIvaAnno(ente, gruppoAttivitaIva, tipoRegistroIva, getBilancio().getAnno(), isDefinitivo);
		//List<AliquotaIva> aliquoteIva = registroIvaDad.findAliquoteByGruppoAttivitaIvaAndTipoRegistroIva(ente, gruppoAttivitaIva, tipoRegistroIva);
		for(AliquotaIva aliquotaIva : aliquoteIva){
			
			StampaRiepilogoAnnualeIvaVenditaIva venditaIva = new StampaRiepilogoAnnualeIvaVenditaIva();
			venditaIva.setAliquotaIva(aliquotaIva);			
			
			List<BigDecimal> imponibileEImposta = progressiviIvaDad.calcolaTotaleImponibileEImpostaAliquotaByGruppoAttivitaIvaAndTipoRegistroIvaAndAnno(ente, gruppoAttivitaIva, tipoRegistroIva, aliquotaIva,Integer.toString(getBilancio().getAnno()));
			
			int i = isDefinitivo?0:2;
			
			venditaIva.setImponibile(imponibileEImposta.get(0+i));
			venditaIva.setIva(imponibileEImposta.get(1+i));
			
			venditeIva.addVenditaIva(venditaIva);
				
			if(TipoOperazioneIva.NON_IMPONIBILE.equals(aliquotaIva.getTipoOperazioneIva())
					|| TipoOperazioneIva.ESENTE.equals(aliquotaIva.getTipoOperazioneIva())
					|| TipoOperazioneIva.ESCLUSO_FCI.equals(aliquotaIva.getTipoOperazioneIva()) ) {
				venditeIva.addTotaliEsNiFci(venditaIva.getImponibile());
			} else {
				venditeIva.addTotaliEsNiFci(BigDecimal.ZERO);
			}		
			
			venditeIva.addTotaliImponibile(venditaIva.getImponibile());
			
			venditeIva.addTotaliIva(venditaIva.getIva());
			
			if(!TipoOperazioneIva.ESCLUSO_FCI.equals(aliquotaIva.getTipoOperazioneIva()) ) {
				venditeIva.addTotaliImponibileEsclusoFci(venditaIva.getImponibile());
				venditeIva.addTotaliIvaEsclusoFci(venditaIva.getIva());
			}		
			
		}
		
		return venditeIva;
	}

	private StampaRiepilogoAnnualeIvaAcquistiIva elaboraAcquistiIvaDef(TipoRegistroIva tipoRegistroIva) {
		return elaboraAcquistiIva(tipoRegistroIva, true);
	}
	
	private StampaRiepilogoAnnualeIvaAcquistiIva elaboraAcquistiIvaProvv(TipoRegistroIva tipoRegistroIva) {
		return elaboraAcquistiIva(tipoRegistroIva, false);
	}

	private StampaRiepilogoAnnualeIvaAcquistiIva elaboraAcquistiIva(TipoRegistroIva tipoRegistroIva, boolean isDefinitivo) {
		StampaRiepilogoAnnualeIvaAcquistiIva acquistiIva = new StampaRiepilogoAnnualeIvaAcquistiIva();
		List<AliquotaIva> aliquoteIva = registroIvaDad.findAliquoteByGruppoAttivitaIvaAndTipoRegistroIvaAndSubDocIvaAnno(ente, gruppoAttivitaIva, tipoRegistroIva, getBilancio().getAnno(), isDefinitivo);
		
		//List<AliquotaIva> aliquoteIva = registroIvaDad.findAliquoteByGruppoAttivitaIvaAndTipoRegistroIva(ente, gruppoAttivitaIva, tipoRegistroIva);
				
		for(AliquotaIva aliquotaIva : aliquoteIva){
			
			StampaRiepilogoAnnualeIvaAcquistoIva acquistoIva = new StampaRiepilogoAnnualeIvaAcquistoIva();
			acquistoIva.setAliquotaIva(aliquotaIva);			
			
			List<BigDecimal> imponibileEImposta = progressiviIvaDad.calcolaTotaleImponibileEImpostaAliquotaByGruppoAttivitaIvaAndTipoRegistroIvaAndAnno(ente, gruppoAttivitaIva, tipoRegistroIva, aliquotaIva, Integer.toString(getBilancio().getAnno()));
			
			int i = isDefinitivo?0:2;
			
			acquistoIva.setImponibile(imponibileEImposta.get(0+i));
			acquistoIva.setIva(imponibileEImposta.get(1+i));
			
			acquistiIva.addAcquistoIva(acquistoIva);
			
			acquistiIva.addTotaleIndetraibiliImponibile(acquistoIva.getImponibile().multiply(aliquotaIva.getPercentualeIndetraibilita().divide(BigDecimal.valueOf(100))));
			acquistiIva.addTotaleIndetraibiliIva(acquistoIva.getIva().multiply(aliquotaIva.getPercentualeIndetraibilita().divide(BigDecimal.valueOf(100))));
			
			acquistiIva.addTotaleDetraibiliImponibile(acquistoIva.getImponibile().multiply(BigDecimal.valueOf(1).subtract(aliquotaIva.getPercentualeIndetraibilita().divide(BigDecimal.valueOf(100)))));
			acquistiIva.addTotaleDetraibiliIva(acquistoIva.getIva().multiply(BigDecimal.valueOf(1).subtract(aliquotaIva.getPercentualeIndetraibilita().divide(BigDecimal.valueOf(100)))));
			
			if(TipoOperazioneIva.NON_IMPONIBILE.equals(aliquotaIva.getTipoOperazioneIva())
					|| TipoOperazioneIva.ESENTE.equals(aliquotaIva.getTipoOperazioneIva())
					|| TipoOperazioneIva.ESCLUSO_FCI.equals(aliquotaIva.getTipoOperazioneIva()) ) {
				acquistiIva.addTotaliEsNiFci(acquistoIva.getImponibile());
			} else {
				acquistiIva.addTotaliEsNiFci(BigDecimal.ZERO);
			}			
			
			acquistiIva.addTotaliImponibile(acquistoIva.getImponibile());
			
			acquistiIva.addTotaliIva(acquistoIva.getIva());
			
			if(!TipoOperazioneIva.ESCLUSO_FCI.equals(aliquotaIva.getTipoOperazioneIva()) ) {
				acquistiIva.addTotaliImponibileEsclusoFci(acquistoIva.getImponibile());
			}		
			
		}
		
		return acquistiIva;
	}
	
	private void popolaListaRegistri() {
		listaRegistriIva = registroIvaDad.findRegistriByGruppoAttivitaIvaBase(ente, gruppoAttivitaIva);
	}	
//	
//	private void popolaListaSubdocumentiIva() {
//		listaSubdocumentoIva = new ArrayList<SubdocumentoIvaSpesa>();
//		for(RegistroIva registroIva : listaRegistroIva) {
//			SubdocumentoIvaSpesa sis = new SubdocumentoIvaSpesa();
//			sis.setAnnoEsercizio(handler.getAnnoEsercizio());
//			sis.setEnte(handler.getEnte());
//			sis.setRegistroIva(registroIva);
//			sis.setStatoSubdocumentoIva(StatoSubdocumentoIva.DEFINITIVO);
//			
//			List<SubdocumentoIvaSpesa> list = subdocumentoIvaSpesaDad.ricercaDettaglioSubdocumentoIvaSpesa(
//					sis,
//					null,
//					null,
//					handler.getPeriodo().getInizioPeriodo(handler.getAnnoEsercizio()),
//					handler.getPeriodo().getFinePeriodo(handler.getAnnoEsercizio()));
//			log.debug(methodName, "RegistroIva.uid = " + registroIva.getUid() + ", subdocumentiIva.size= " + list.size());
//			listaSubdocumentoIva.addAll(list);
//		}
//		
//	}

	
	

	@Override
	protected void handleResponse(GeneraReportResponse res) {
		final String methodName = "handleResponse";
		log.debug(methodName, "numero di pagine generata: "+ res.getNumeroPagineGenerate());
		
		persistiStampaIva(res);
	}
	
	
	/**
	 * Persiste la stampa IVA su database.
	 * 
	 * @param res la risposta del metodo di generazione del report
	 */
	private void persistiStampaIva(GeneraReportResponse res) {
		final String methodName = "persistiStampaIva";
		log.debug(methodName, "Persistenza della stampa");
		InserisceStampaIva reqISI = new InserisceStampaIva();
		reqISI.setDataOra(new Date());
		reqISI.setRichiedente(richiedente);
		reqISI.setStampaIva(creaStampaIva(res));
		
		InserisceStampaIvaResponse resISI = inserisceStampaIvaService.executeService(reqISI);
		if(resISI == null) {
			log.error(methodName, "Response del servizio InserisceStampaIva null");
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il servizio InserisceStampaIva ha risposto null"));
		}
		if(resISI.hasErrori()) {
			log.debug(methodName, "Errori nell'invocazione del servizio InserisceStampaIva");
			for(Errore e : resISI.getErrori()) {
				log.error(methodName, "Errore: " + e.getTesto());
			}
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il servizio InserisceStampaIva e' terminato con errori"));
		}
		log.info(methodName, "Stampa terminata con successo. Uid record inserito su database: " + resISI.getStampaIva().getUid());
	}
	
	/**
	 * Crea una StampaIva per la persistenza.
	 * 
	 * @param res la response della generazione del report
	 * @return la stampa iva creata
	 */
	private StampaIva creaStampaIva(GeneraReportResponse res) {
		StampaIva result = new StampaIva();
		
		result.setListaRegistroIva(listaRegistriIva);
		
		result.setPeriodo(Periodo.ANNO);
		result.setAnnoEsercizio(getBilancio().getAnno());
		
		File file = res.getReport();
		
		result.setCodice(file.getCodice());
		
		result.setEnte(ente);
		result.setFlagIncassati(true);
		result.setFlagPagati(true);
		result.setTipoStampa(TipoStampa.DEFINITIVA);
		result.setTipoStampaIva(TipoStampaIva.RIEPILOGO_ANNUALE);
		
		List<File> listaFile = new ArrayList<File>();
		
		listaFile.add(res.getReport());
		result.setFiles(listaFile);
		
		result.setFlagStampaDefinitivo(Boolean.TRUE);
		result.setFlagStampaProvvisorio(Boolean.FALSE);
		result.setUltimaPaginaStampaDefinitiva(res.getNumeroPagineGenerate());
		result.setUltimaPaginaStampaProvvisoria(null);
		
		return result;
	}
	
	



	/**
	 * @return the gruppoAttivitaIva
	 */
	public GruppoAttivitaIva getGruppoAttivitaIva() {
		return gruppoAttivitaIva;
	}

	/**
	 * @param gruppoAttivitaIva the gruppoAttivitaIva to set
	 */
	public void setGruppoAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva) {
		this.gruppoAttivitaIva = gruppoAttivitaIva;
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


	


	
	

}
