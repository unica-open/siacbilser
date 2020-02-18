/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva.model;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.business.service.stampa.base.DatiIva;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class StampaRegistroIvaReportModelFactory {
	
	public static StampaRegistroIvaReportModel obtainDatiMinimal(StampaRegistroIvaReportModel result) {
		StampaRegistroIvaReportModel stampaRegIstroIvaReportModelMinimal = new StampaRegistroIvaReportModel();
		
		StampaRegistroIvaIntestazione intestazioneMinimal = elaboraIntestazione(result.getIntestazione());
		stampaRegIstroIvaReportModelMinimal.setIntestazione(intestazioneMinimal);
		
		StampaRegistroIvaTitolo titoloMinimal = elaboraTitolo(result.getTitolo());
		stampaRegIstroIvaReportModelMinimal.setTitolo(titoloMinimal);
		
		StampaRegistroIvaDatiIva datIvaMinimal = elaboraDatiIva(result.getDatiIva());
		stampaRegIstroIvaReportModelMinimal.setDatiIva(datIvaMinimal);
		
		List<StampaRegistroIvaRiepilogo> elaboraListaRiepilogoMinimal = elaboraListaRiepilogo(result.getListaRiepilogo());
		stampaRegIstroIvaReportModelMinimal.setListaRiepilogo(elaboraListaRiepilogoMinimal);
		
		return stampaRegIstroIvaReportModelMinimal;
	}

	private static List<StampaRegistroIvaRiepilogo>  elaboraListaRiepilogo(List<StampaRegistroIvaRiepilogo> list) {
		List<StampaRegistroIvaRiepilogo> elaborato = new ArrayList<StampaRegistroIvaRiepilogo>();
		for (StampaRegistroIvaRiepilogo riepilogoSingolo : list) {
			StampaRegistroIvaRiepilogo riepilogoMinimal = elaboraRiepilogo(riepilogoSingolo);
			elaborato.add(riepilogoMinimal);
		}
		return elaborato;
	}

	/**
	 * @param stampaRegistroIvaIntestazione2 
	 * 
	 */
	private static StampaRegistroIvaIntestazione elaboraIntestazione(StampaRegistroIvaIntestazione daElaborare) {
		StampaRegistroIvaIntestazione elaborato = new StampaRegistroIvaIntestazione();
		
		Ente ente = new Ente();
		ente.setNome(daElaborare.getEnte().getNome());
		elaborato.setEnte(ente);
		
		elaborato.setIndirizzoSoggetto(daElaborare.getIndirizzoSoggetto());
		
		
		Soggetto soggetto = new Soggetto();
		soggetto.setPartitaIva(daElaborare.getSoggetto().getPartitaIva());
		elaborato.setSoggetto(soggetto);
		
		
		elaborato.setTipoStampa(daElaborare.getTipoStampa());
		elaborato.setNumeroDiPagina(daElaborare.getNumeroDiPagina());
		elaborato.setAnnoDiRiferimentoContabile(daElaborare.getAnnoDiRiferimentoContabile());
		return elaborato;
	}
	
	/* @param stampaRegistroIvaIntestazione2 
	 * 
	 */
	private static StampaRegistroIvaTitolo elaboraTitolo(StampaRegistroIvaTitolo daElaborare) {
		StampaRegistroIvaTitolo elaborato = new StampaRegistroIvaTitolo();
		
		GruppoAttivitaIva gruppoDaElaborare = daElaborare.getGruppoAttivitaIva();
		if(gruppoDaElaborare != null) {
			GruppoAttivitaIva gruppoElaborato = new GruppoAttivitaIva();
			gruppoElaborato.setCodice(gruppoDaElaborare.getCodice());
			gruppoElaborato.setDescrizione(gruppoDaElaborare.getDescrizione());
			elaborato.setGruppoAttivitaIva(gruppoElaborato);
		}
		
		RegistroIva registroElab = daElaborare.getRegistroIva();
		if(registroElab != null) {
			RegistroIva registroElaborato = new RegistroIva();
			registroElaborato.setCodice(registroElab.getCodice());
			registroElaborato.setDescrizione(registroElab.getDescrizione());
			registroElaborato.setTipoRegistroIva(registroElab.getTipoRegistroIva());
			elaborato.setRegistroIva(registroElaborato);
			
		}		
		
		elaborato.setPeriodo(daElaborare.getPeriodo());
		elaborato.setAnnoContabile(daElaborare.getAnnoContabile());
		return elaborato;
	}
	
	/* @param stampaRegistroIvaIntestazione2 
	 * 
	 */
	private static StampaRegistroIvaDatiIva elaboraDatiIva(StampaRegistroIvaDatiIva daElaborare) {
		StampaRegistroIvaDatiIva elaborato = new StampaRegistroIvaDatiIva();
		List<DatiIva> datiElaborati = elaboraDatiIva(daElaborare.getListaDatiIva());
		elaborato.setListaDatiIva(datiElaborati);
		elaborato.setTotaleImponibile(daElaborare.getTotaleImponibile());
		elaborato.setTotaleImposta(daElaborare.getTotaleImposta());
		elaborato.setTotaleTotale(daElaborare.getTotaleTotale());
		return elaborato;
	}
	
	private static List<DatiIva> elaboraDatiIva(List<DatiIva> listaDatiIva) {
		List<DatiIva> lista = new ArrayList<DatiIva>();
		for (DatiIva dati : listaDatiIva) {
			if(dati instanceof StampaRegistoIvaDatoIvaAcquisti) {
				StampaRegistoIvaDatoIvaAcquisti elaborato = new StampaRegistoIvaDatoIvaAcquisti();
				StampaRegistoIvaDatoIvaAcquisti daElaborare = (StampaRegistoIvaDatoIvaAcquisti) dati;
				
				SubdocumentoIvaSpesa subdocIvaElaborato = elaboraSubdocumentoIva(daElaborare.getSubdocumentoIva());
				elaborato.setSubdocumentoIva(subdocIvaElaborato);
				
				DocumentoSpesa docElaborato = elaboraDocumento(daElaborare.getDocumento());
				elaborato.setDocumento(docElaborato);
				
				elaborato.setAliquotaSubdocumentoIva(daElaborare.getAliquotaSubdocumentoIva());
				
				elaborato.setTotale(daElaborare.getTotale());
				lista.add(elaborato);
			}
		}
		
		return lista;
	}

	private static DocumentoSpesa elaboraDocumento(DocumentoSpesa documento) {
		if(documento == null) {
			return null;
		}
		
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setDataEmissione(documento.getDataEmissione());
		doc.setNumero(documento.getNumero());
		doc.setTipoDocumento(documento.getTipoDocumento());
		
		Soggetto soggetto = documento.getSoggetto();
		if( soggetto != null) {
			Soggetto soggettoElab = new Soggetto();
			soggettoElab.setCodiceSoggetto(soggetto.getCodiceSoggetto());
			soggettoElab.setDenominazione(soggetto.getDenominazione());
			doc.setSoggetto(soggettoElab);
		}
		
		return doc;
	}

	/**
	 * @param subdocIvaDaElaborare
	 */
	private static SubdocumentoIvaSpesa elaboraSubdocumentoIva(SubdocumentoIvaSpesa subdocIvaDaElaborare) {
		if(subdocIvaDaElaborare == null) {
			return null;
		}
		SubdocumentoIvaSpesa subdocIva = new SubdocumentoIvaSpesa();
		subdocIva.setNumeroProtocolloDefinitivo(subdocIvaDaElaborare.getNumeroProtocolloDefinitivo());
		subdocIva.setDataProtocolloDefinitivo(subdocIvaDaElaborare.getDataProtocolloDefinitivo());
		subdocIva.setNumeroOrdinativoDocumento(subdocIvaDaElaborare.getNumeroOrdinativoDocumento());
		subdocIva.setNumeroProtocolloProvvisorio(subdocIvaDaElaborare.getNumeroProtocolloProvvisorio());
		subdocIva.setDataProtocolloProvvisorio(subdocIvaDaElaborare.getDataProtocolloProvvisorio());
		subdocIva.setFlagRilevanteIRAP(subdocIvaDaElaborare.getFlagRilevanteIRAP());
		
		SubdocumentoSpesa subdocumento = subdocIvaDaElaborare.getSubdocumento();
		if( subdocumento != null) {
			SubdocumentoSpesa subdocSpesa = new SubdocumentoSpesa();
			subdocSpesa.setNumero(subdocumento.getNumero());
			subdocIva.setSubdocumento(subdocSpesa);
		}
		
		return subdocIva;
			
	}

	private static StampaRegistroIvaRiepilogo elaboraRiepilogo(StampaRegistroIvaRiepilogo daElaborare) {
		StampaRegistroIvaRiepilogo elaborato = new StampaRegistroIvaRiepilogo();
		
		elaborato.setPeriodo(daElaborare.getPeriodo());		
		elaborato.setTotaleImponibile(daElaborare.getTotaleImponibile());
		elaborato.setTotaleIVA(daElaborare.getTotaleIVA());
		elaborato.setTotaleTotale(daElaborare.getTotaleTotale());
		elaborato.setTotaleProgressivoImponibile(daElaborare.getTotaleProgressivoImponibile());
		elaborato.setTotaleProgressivoIva(daElaborare.getTotaleProgressivoIva());
		elaborato.setTotaleTotaleProgressivo(daElaborare.getTotaleTotaleProgressivo());
		elaborato.setTotaleImponibileIndetraibile (daElaborare.getTotaleImponibileIndetraibile());
		elaborato.setTotaleImponibileDetraibile(daElaborare.getTotaleImponibileDetraibile());
		elaborato.setTotaleImponibileEsente(daElaborare.getTotaleImponibileEsente());
		elaborato.setTotaleIVAIndetraibile(daElaborare.getTotaleIVAIndetraibile());
		elaborato.setTotaleIVADetraibile(daElaborare.getTotaleIVADetraibile());
		elaborato.setTotaleProgressivoImponibileIndetraibile(daElaborare.getTotaleProgressivoImponibileIndetraibile());
		elaborato.setTotaleProgressivoImponibileDetraibile(daElaborare.getTotaleProgressivoImponibileDetraibile());
		elaborato.setTotaleProgressivoImponibileEsente(daElaborare.getTotaleProgressivoImponibileEsente());
		elaborato.setTotaleProgressivoIvaIndetraibile(daElaborare.getTotaleProgressivoIvaIndetraibile());
		elaborato.setTotaleProgressivoIvaDetraibile(daElaborare.getTotaleProgressivoIvaDetraibile());
		elaborato.setPercProrata(daElaborare.getPercProrata());
		elaborato.setIvaIndetraibileCausaProRata(daElaborare.getIvaIndetraibileCausaProRata());
		
		List<DatiIva> listaRigheElaborate = elaboraListaRiepilogoIva(daElaborare.getListaRiepiloghiIva());
		elaborato.setListaRiepiloghiIva(listaRigheElaborate);
		
		return elaborato;
	}

	private static List<DatiIva> elaboraListaRiepilogoIva(List<DatiIva> listaRiepiloghiIva) {
		List<DatiIva> lista = new ArrayList<DatiIva>();
		for (DatiIva dati : listaRiepiloghiIva) {
			if(dati instanceof StampaRegistroIvaRiepilogoIva) {
				StampaRegistroIvaRiepilogoIva elaborato = new StampaRegistroIvaRiepilogoIva();
				StampaRegistroIvaRiepilogoIva daElaborare = (StampaRegistroIvaRiepilogoIva) dati;
				
				elaborato.setAliquotaSubdocumentoIva(daElaborare.getAliquotaSubdocumentoIva());
				
				elaborato.setImponibile(daElaborare.getImponibile());
				elaborato.setIva(daElaborare.getIva());
				elaborato.setTotale(daElaborare.getTotale());
				elaborato.setProgressivoImponibile(daElaborare.getProgressivoImponibile());
				elaborato.setProgressivoIva(daElaborare.getProgressivoIva());
				elaborato.setTotaleProgressivo(daElaborare.getTotaleProgressivo());
				
				lista.add(elaborato);
			}
		}
		
		return lista;
	}



}
