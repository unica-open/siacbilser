/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping;

import it.csi.siac.siaccommonser.util.dozer.MapId;

/**
 * Id dei mapping di coversione.
 * 
 * Naming convention: * 
 * Il nome dell'Entity ed il nome del Model concatenati da "_" 
 * 
 * @author Domenico
 *
 */
public enum BilMapId implements MapId {
		
	SiacTEnteProprietario_Ente,
	SiacTEnteProprietario_Ente_Base,
	SiacTEnteProprietario_Ente_GestioneLivelli,
	
	SiacTBil_Bilancio,
	SiacTAzione_Azione,
	
	SiacTClass_Codifica,
		
	SiacTClass_ClassificatoreGenerico,
	SiacTClass_ClassificatoriGenerici,
	SiacTClass_ClassificatoreGerarchico_Reduced,
	SiacTClass_ClassificatoreGerarchico,
	SiacTClass_ClassificatoreGerarchici,
	
	SiacTClass_ElementoPianoDeiConti,
	SiacTClass_ElementiPianoDeiConti,
	SiacTClass_StrutturaAmministrativoContabile,
	SiacTClass_StrutturaAmministrativoContabile_Reduced,
	
	// SIAC-5169
	SiacDClassTipo_TipoClassificatore,
	
	SiacDBilElemCategoria_CategoriaCapitolo,

	SiacTBilElem_Bilancio,
	SiacTBilElem_Ente,	
	SiacTBilElem_ClassificatoreGenerico, 
	SiacTBilElem_ClassificatoreGerarchico, 
	SiacTBilElem_ClassificatoriGenerici,	
	SiacTBilElem_Capitolo_Base, 
	
    CapitoloMassivaUscitaPrevisione_CapitoloUscitaPrevisione,
	CapitoloMassivaUscitaGestione_CapitoloUscitaGestione,
	CapitoloMassivaEntrataPrevisione_CapitoloEntrataPrevisione,
	CapitoloMassivaEntrataGestione_CapitoloEntrataGestione,
	
//	SiacTBilElem_CapitoloUscitaPrevisione_Massivo,
//	SiacTBilElem_CapitoloUscitaGestione_Massivo,
//	SiacTBilElem_CapitoloEntrataPrevisione_Massivo,
//	SiacTBilElem_CapitoloEntrataGestione_Massivo,
	
	SiacTBilElem_CapitoloUscitaPrevisione,
	SiacTBilElem_CapitoloUscitaPrevisione_ImportiCapitolo,
	SiacTBilElem_CapitoloUscitaPrevisione_StatoOperativoElementoDiBilancio,
	SiacTBilElem_CapitoloUscitaPrevisione_ModelDetail,

	SiacTBilElem_CapitoloUscitaGestione,
	SiacTBilElem_CapitoloUscitaGestione_Minimal,
	SiacTBilElem_CapitoloUscitaGestione_ImportiCapitolo,
	SiacTBilElem_CapitoloUscitaGestione_StatoOperativoElementoDiBilancio,
	SiacTBilElem_CapitoloUscitaGestione_ModelDetail,
	
	SiacTBilElem_CapitoloEntrataPrevisione,
	SiacTBilElem_CapitoloEntrataPrevisione_ImportiCapitolo,
	SiacTBilElem_CapitoloEntrataPrevisione_StatoOperativoElementoDiBilancio,
	SiacTBilElem_CapitoloEntrataPrevisione_ModelDetail,
	
	SiacTBilElem_CapitoloEntrataGestione,
	SiacTBilElem_CapitoloEntrataGestione_ImportiCapitolo,
	SiacTBilElem_CapitoloEntrataGestione_StatoOperativoElementoDiBilancio,
	SiacTBilElem_CapitoloEntrataGestione_ModelDetail,
	SiacTBilElem_CapitoloEntrataGestione_Chiave,
	
	SiacTVariazione_Bilancio, 
	SiacTVariazione_Ente,
	SiacTVariazione_VariazioneImportoCapitolo,
	SiacTVariazione_VariazioneImportoCapitolo_Medium,
	SiacTVariazione_VariazioneImportoCapitolo_Base,
	SiacTVariazione_VariazioneImportoCapitolo_Minimal,
	SiacTVariazione_VariazioneCodificaCapitolo,
	SiacTVariazione_StornoUEB,
	SiacTVariazione_AttoAmministrativo,
	SiacTBilElem_DettaglioVariazioneImportoCapitolo,
	SiacTBilElemVar_DettaglioVariazioneCodificaCapitolo,
	
	SiacDAttoAmmTipo_TipoAtto,
	
	SiacTVincolo_Vincolo,
	SiacTVincolo_VincoloCapitoli,
	// SIAC-5076
	SiacDVincoloGenere_GenereVincolo,
	
	SiacTProgramma_Progetto,
	SiacTCronop_Cronoprogramma,
	SiacTCronop_Cronoprogramma_ModelDetail,
	SiacTCronopElem_DettaglioEntrataCronoprogramma, 
	SiacTCronopElem_DettaglioUscitaCronoprogramma, 
	//SIAC-6255
	SiacDProgrammaTipo_TipoProgetto,
	SiacDProgrammaAffidamento_ModalitaAffidamentoProgetto,
	//SIAC-6255
	SiacTQuadroEconomico_QuadroEconomico,
	SiacTQuadroEconomico_QuadroEconomico_Base,
	SiacTQuadroEconomico_QuadroEconomico_All,
	SiacTQuadroEconomico_QuadroEconomico_AllValidi,
	SiacTQuadroEconomico_QuadroEconomico_ModelDetail,
	
	
	SiacTDoc_DocumentoSpesa,
	SiacTDoc_DocumentoSpesa_Medium,
	SiacTDoc_DocumentoSpesa_Base,
	SiacTDoc_DocumentoSpesa_Minimal,
	SiacTDoc_DocumentoSpesa_ModelDetail,
	SiacTDoc_DocumentoSpesa_Collegati,
	
	SiacTDoc_DocumentoEntrata,
	SiacTDoc_DocumentoEntrata_Medium,
	SiacTDoc_DocumentoEntrata_Base,
	SiacTDoc_DocumentoEntrata_Minimal,
	SiacTDoc_DocumentoEntrata_ModelDetail,
	SiacTDoc_DocumentoEntrata_Collegati,

	SiacDDocTipo_TipoDocumento, 
	
	SiacTSubdoc_SubdocumentoSpesa,
	SiacTSubdoc_SubdocumentoSpesa_Medium,
	SiacTSubdoc_SubdocumentoSpesa_Emettitore,
	SiacTSubdoc_SubdocumentoSpesa_Base,
	SiacTSubdoc_SubdocumentoSpesa_Allegato,
	SiacTSubdoc_SubdocumentoSpesa_ModelDetail,

	
	SiacTSubdoc_SubdocumentoEntrata,
	SiacTSubdoc_SubdocumentoEntrata_Medium,
	SiacTSubdoc_SubdocumentoEntrata_Base,
	SiacTSubdoc_SubdocumentoEntrata_Allegato,
	SiacTSubdoc_SubdocumentoEntrata_ModelDetail,
	
	SiacDDistinta_Distinta,
	
	SiacDOnere_TipoOnere,
	SiacDOnere_TipoOnere_Base,
	SiacDOnereTipo_NaturaOnere,
	SiacDOnereAttivita_AttivitaOnere,
	SiacDCausale_Causale770,
	SiacDSommaNonSoggettaTipo_CodiceSommaNonSoggetta,
	
	SiacRDocOnere_DettaglioOnere, 
	
	SiacDCausale_CausaleSpesa,	
	SiacDCausale_CausaleSpesa_Base,
	SiacDCausale_CausaleSpesa_Minimal,
	
	SiacDCausale_CausaleEntrata,
	SiacDCausale_CausaleEntrata_Base,
	SiacDCausale_CausaleEntrata_Minimal,
	
	SiacDCausaleTipo_TipoCausale,
	SiacDContotesoreria_ContoTesoreria,
	SiacTPredoc_PreDocumentoSpesa,
	
	SiacTPredoc_PreDocumentoSpesa_Base,
	SiacTPredoc_PreDocumentoSpesa_ModelDetail,
	SiacTPredoc_PreDocumentoEntrata, 
	SiacTPredoc_PreDocumentoEntrata_Base,
	SiacTPredoc_PreDocumentoEntrata_ModelDetail,
	
	SiacTPredocAnagr_DatiAnagraficiPreDocumentoSpesa, 
	SiacTPredocAnagr_DatiAnagraficiPreDocumentoEntrata, 
	
	SiacTModpag_ContoCorrente, 

	SiacDCodicebollo_CodiceBollo, 
	
	SiacTIvaRegistro_RegistroIva,
	SiacTIvaRegistro_RegistroIva_Base,
	SiacTIvaRegistro_RegistroIva_GruppoBase,
	SiacTIvaRegistro_RegistroIva_GruppoMinimal,
	SiacTIvaRegistro_RegistroIva_Minimal,
	
	SiacDIvaRegistroTipo_TipoRegistroIva,
	
	SiacTIvaAttivita_AttivitaIva,
	SiacTIvaAttivita_AttivitaIva_Minimal,
	SiacTIvaAttivita_AttivitaIva_Base,
	
	SiacTIvaGruppo_GruppoAttivitaIva,
	SiacTIvaGruppo_GruppoAttivitaIva_Minimal,
	SiacTIvaGruppo_GruppoAttivitaIva_Base,
	
	SiacTIvaProrata_ProrataEChiusuraGruppoIva,
	
	SiacTIvaAliquota_AliquotaIva,
	
	SiacTIvamov_AliquotaSubdocumentoIva, 
	
	SiacDIvaRegistrazioneTipo_TipoRegistrazioneIva,
	
	SiacTIvaStampa_StampaIva,
	SiacTIvaStampa_StampaIva_Base,
	SiacTIvaStampa_StampaIva_FileBase,
	
	SiacTFile_File,
	SiacTFile_File_Base,
	
	SiacDFileTipo_TipoFile,
	
	SiacDFileStato_StatoFile,
	
	SiacDValuta_Valuta,
	
	SiacTSubdocIva_SubdocumentoIva_Minimal,
	SiacTSubdocIva_SubdocumentoIva_Base,
	SiacTSubdocIva_SubdocumentoIvaSpesa,
	SiacTSubdocIva_SubdocumentoIvaSpesa_Base,
	SiacTSubdocIva_SubdocumentoIvaEntrata, 
	SiacTSubdocIva_SubdocumentoIvaEntrata_Base,
	SiacTSubdocIva_SubdocumentoIvaEntrata_Minimal, 
	
	SiacTIvaRegistroTotale_ProgressiviIva,
	
	SiacTIndirizzoSoggetto_IndirizzoSoggetto,
	
	SiacTSoggetto_Soggetto,
	SiacTSoggetto_Soggetto_Matricola,
	SiacTSoggetto_Soggetto_Indirizzo,
	SiacDSoggettoClasse_ClasseSoggetto,
	SiacTSoggetto_Soggetto_DatiDurc,
	
	SiacTAttoAmm_AttoAmministrativo,
	SiacTAttoAmm,
	
	SiacTAttoAllegato_AllegatoAtto, 
	SiacTAttoAllegato_AllegatoAtto_Base,
	SiacTAttoAllegato_AllegatoAtto_Medium,
	SiacRAttoAllegatoSog_DatiSoggettoAllegato, 
	SiacTElencoDoc_ElencoDocumentiAllegato_Completo,
	SiacTElencoDoc_ElencoDocumentiAllegato,
	SiacTElencoDoc_ElencoDocumentiAllegato_Base,
	SiacTElencoDoc_ElencoDocumentiAllegato_Base_PagatoIncassato,
	SiacTElencoDoc_ElencoDocumentiAllegato_Minimal,
	SiacTElencoDoc_ElencoDocumentiAllegato_Minimal_AllegatoAtto,
	SiacTElencoDoc_ElencoDocumentiAllegato_Minimal_light,
	
	SiacTAttoAllegatoStampa_AllegatoAttoStampa_Base,
	SiacTAttoAllegatoStampa_AllegatoAttoStampa,
		
	SiacTProvCassa_ProvvisorioDiCassa,
	SiacTLiquidazione_Liquidazione,
	SiacTLiquidazione_Liquidazione_Minimal,
	SiacTLiquidazione_Liquidazione_MinimalStato,
	SiacTLiquidazione_Liquidazione_Soggetto,
	SiacTLiquidazione_Liquidazione_Impegno,
	SiacTLiquidazione_Liquidazione_ModelDetail,
	
	SiacTOrdinativo_Ordinativo,
	SiacTOrdinativo_Ordinativo_Minimal,
	SiacTOrdinativo_Ordinativo_Soggetto,
	
	SiacTModpag_ModalitaPagamentoSoggetto,
	
	
	SiacTMovgest_Impegno,
	SiacTMovgest_Impegno_Soggetto,
	SiacTMovgestT_SubImpegno,
	SiacTMovgestT_SubImpegno_Minimal,
	SiacTMovgest_Accertamento,
	SiacTMovgest_Accertamento_Soggetto,
	SiacTMovgestT_SubAccertamento,
	SiacTMovgestT_SubAccertamento_Minimal,
	SiacTMovgest_Impegno_ModelDetail,
	SiacTMovgestT_SubImpegno_ModelDetail,
	SiacTMovgest_Accertamento_ModelDetail,
	SiacTMovgestT_SubAccertamento_ModelDetail,
	
	// Modifica Movimento gestione
	SiacTModifica_ModificaMovimentoGestioneSpesa_BIL,
	SiacTModifica_ModificaMovimentoGestioneEntrata_BIL,
	
	SiacDTrasportoMezzo_MezziDiTrasporto,
	
	SiacDPccCodice_CodicePCC_Base,
	SiacDPccCodice_CodicePCC,
	SiacDPccUfficio_CodiceUfficioDestinatarioPCC_Base,
	SiacDPccUfficio_CodiceUfficioDestinatarioPCC, 
	SiacTRegistroPcc_RegistroComunicazioniPCC,
	SiacDPccCausale_CausalePCC,
	SiacDPccDebitoStato_StatoDebito,
	SiacDPccOperazioneTipo_TipoOperazionePCC,
	
	SiacTRegistrounicoDoc_RegistroUnico,
	
	// Mutuo (la nomenclatura con 'BIL' e' per evitare che condividano il nome con i mapping del modulo FIN)
	SiacTMutuo_Mutuo_BIL,
	SiacTMutuoVoce_VoceMutuo_BIL,
	
	SiacTOrdine_Ordine,
	
	// SIAC-3940
	ListOfObjectArrayTotale_RiepilogoDatiVariazioneImportoCapitoloAnno,
	ListOfObjectArrayCount_RiepilogoDatiVariazioneImportoCapitoloAnno,
	SiacTVariazione_VariazioneImportoSingoloCapitolo,
	SiacTBilElem_Capitolo_Minimal,
	
	
	// CR-4088
	SiacTBil_AttributiBilancio,
	SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilita_ModelDetail,
	SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilita,
	
	// SIAC-4422
	SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilitaRendiconto_ModelDetail,
	SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilitaRendiconto,
	
	// SIAC-4210
	SiacTCartacont_CartaContabile_BIL,
	
	// SIAC-5311 SIOPE+
	SiacDSiopeTipoDebito_SiopeTipoDebito,
	SiacDSiopeAssenzaMotivazione_SiopeAssenzaMotivazione,
	SiacDSiopeDocumentoTipo_SiopeDocumentoTipo,

	SiacDSiopeDocumentoTipoAnalogico_SiopeDocumentoTipoAnalogico_Minimal,
	SiacDSiopeDocumentoTipoAnalogico_SiopeDocumentoTipoAnalogico,
	SiacDSiopeScadenzaMotivo_SiopeScadenzaMotivo,
	
	// SIAC-5115
	SiacTSubdocSospensione_SospensioneSubdocumento,
	// SAIC-5481
	SiacTAzione_Azione_ConGruppo,
	SiacDGruppoAzioni_GruppoAzioni,
	// SIAC-5546
	SiacDContotesoreria_ContoTesoreria_Fin2, 
	
	// SIAC-6881
	SiacTBilElemDetComp_ComponenteImportiCapitolo_ModelDetail,
	SiacTBilElemDetComp_ComponenteImportiCapitolo,
	
	SiacTBilElemDet_DettaglioImportoCapitolo,
	SiacDBilElemDetTipo_TipoImportoCapitolo,

	SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo,
	SiacDBilElemDetCompTipo_TipoComponenteImportiCapitolo_ModelDetail, 

	SiacTBilElemDetVarComp_ComponenteImportiCapitolo,
	SiacTBilElemDetVarComp_ComponenteImportiCapitolo_ModelDetail,

	//SIAC 6929 
	Stilo_SiacTMovgestT_SubImpegno,
	Stilo_SiacTMovgest_Impegno,
	Stilo_SiacTMovgest_Accertamento,
	Stilo_SiacTMovgestT_SubAccertamento,
	Stilo_SiacTModifica_ModificaMovimentoGestioneSpesa_BIL,
	Stilo_SiacTModifica_ModificaMovimentoGestioneEntrata_BIL,
	;
	
}
