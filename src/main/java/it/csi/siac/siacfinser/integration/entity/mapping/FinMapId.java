/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.mapping;

import it.csi.siac.siaccommonser.util.dozer.MapId;


/**
 * Id dei mapping di conversione.
 * 
 * Naming convention: * 
 * Il nome dell'Entity ed il nome del Model concatenati da "_" 
 * 
 * @author paolos
 *
 */
public enum FinMapId implements MapId {
		
	SiacDSoggettoTipo, 
	SiacDSoggettoClasse_ClasseSoggetto,
	SiacDOnere_CodificaFin,
	SiacDModificaTipo_CodificaFin,
	SiacDRecapitoModo_CodificaFin,
	SiacDSoggettoStato_CodificaFin,
	SiacDMovgestStato_CodificaFin,
	SiacDFormaGiuridicaTipo_CodificaFin,
	SiacDSoggettoClasse_CodificaFin,
	SiacTNazione_Nazione,
	SiacTNazione_CodificaFin,
	SiacTSoggettoMod_SedeSecondariaSoggetto,
	SiacTFormaNaturaGiuridica_NaturaGiuridicaSoggetto,
	SiacTComune_ComuneNascita,
	SiacTSoggetto_Soggetto,
	SiacTPersonaFisica_Soggetto,
	SiacTEnteProprietario_Ente,
	SiacTSoggetto_Ente,
	SiacTPersonaFisica_Ente,
	SiacTSoggetto_SedeSecondariaSoggetto,
	SiacTIndirizzo_SedeSecondariaIndirizzo, 
	SiacTFormaNaturaGiuridica_CodificaFin,
	SiacTIndirizzoMod_IndirizzoSoggetto,
	//SiacTFormaNaturaGiuridica_CodificaExtFin, 
	SiacDSoggettoTipo_CodificaFin,
	SiacDIndirizzoTipo_CodificaFin,
	SiacTModPag_ModalitaPagamentoSoggetto,
	SiacTModPagMod_ModalitaPagamentoSoggetto,
	SiacTIndirizzo_IndirizzoSoggetto,
	SiacTSoggettoMod_Soggetto,
	SiacDTipoAccredito_CodificaFin,
	SiacDViaTipo_CodificaExtFin,
	SiacDRelazTipo_CodificaFin,
	SiacTMovgest_Impegno,
	SiacTMovgest_Impegno_Bilancio,
	SiacTMovgest_Impegno_Capitolo_Bilancio,
	SiacTMovgestTs_SubImpegno,
	SiacDMovgestTipo_CodificaExtFin,
	SiacTModifica_ModificaMovimentoGestioneSpesa,
	SiacTModificaBIL_ModificaMovimentoGestioneSpesa,
	SiacTProgramma_CodificaFin,
	SiacTClass_CodificaExtFin,
	SiacTMovgest_Accertamento,
	SiacTMovgest_Accertamento_Bilancio,
	SiacTMovgest_Accertamento_Capitolo_Bilancio,
	SiacTModifica_ModificaMovimentoGestioneEntrata,
	SiacTModificaBIL_ModificaMovimentoGestioneEntrata,
	SiacTMovgestTs_SubAccertamento,
	SiacTLiquidazione_Liquidazione,
	SiacTLiquidazione_Liquidazione_Base,
	SiacDContoTesoreria_ContoTesoreriaCodificaFin,
	SiacDContotesoreria_Contotesoreria,
	SiacDDistinta_CodificaFin,
	SiacDDistinta_Distinta,	
	SiacDCodiceBollo_CodificaFin,
	SiacDCodiceBollo_CodiceBollo,
	SiacDCommissioni_CodificaFin,
	SiacDCommissioneTipo_CommissioneDocumento,
	SiacTOrdinativo_OrdinativoPagamento,
	SiacTOrdinativoT_SubOrdinativoPagamento_ConOrdinativo,
	SiacTOrdinativoT_SubOrdinativoIncasso_ConOrdinativo,
	SiacDNoteTesoriere_NoteTesoriere,
	SiacTProvCassa_ProvvisorioDiCassa,
	SiacDNoteTesoriere_CodificaFin,
	SiacTOrdinativoT_SubOrdinativoPagamento,
	SiacTOrdinativoT_SubOrdinativoPagamento_Minimo,
	SiacTOrdinativoT_SubOrdinativoPagamento_Medium,
	SiacDOrdinativoStato_CodificaFin,
	SiacTOrdinativo_OrdinativoIncasso,
	SiacTOrdinativoT_SubOrdinativoIncasso,
	//SIAC-6831
	SiacTOrdinativoT_SubOrdinativoIncasso_ModelDetail,
	SiacDCartaContStato_CodificaFin,
	SiacTCartacont_CartaContabile,
	SiacTCartacontEstera_CartaEstera,
	SiacTCartacontDet_PreDocumentoCarta,
	SiacDValuta_CodificaFin,
	SiacDCommissioniestero_CodificaFin,
	SiacDDocumentoTipo_CodificaFin,
	SiacTBil_Bilancio,
	SiacTCab_Banca,
	SiacTDocFin_DocumentoSpesa,
	SiacTDocFin_DocumentoSpesa_Minimo,
	SiacTDocFin_DocumentoEntrata,
	SiacTSubdocFin_SubdocumentoSpesa,
	SiacTSubdocFin_SubdocumentoSpesa_Minimo,
	SiacTSubdocFin_SubdocumentoEntrata,
	SiacDOnereFin_TipoOnere,
	SiacTAvanzovincoloFin_Avanzovincolo,
	LiquidazioneDto_liquidazione, 
	SiacTAttoAmmFin_AttoAmministrativo,
	SiacTCbpiSaldoFin_VociContoCorrente,
	SiacTCbpiSaldoAddebitoFin_AddebitoContoCorrente,
	SiacDSiopeAssenzaMotivazioneFin_CodificaExtFin,
	SiacTEnteProprietario_Ente_GestioneLivelliFin,
	//SIAC-7779
	SiacRMovgestTsFin_VincoloImpegno,
	SiacRMovgestTsFin_VincoloAccertamento,	
	//SIAC-7779
	
	//SIAC-8117
	ModificaMovimentoGestioneSpesaCollegata_SiacRMovgestTsDetModFin_ModelDetail,
	ModificaMovimentoGestioneSpesaCollegata_SiacRMovgestTsDetModFin_Default_ModelDetail,
	//SIAC-8650
	VincoloAccertamento_SiacRMovgestTsFin_Base_ModelDetail,
	VincoloImpegno_SiacRMovgestTsFin_Base_ModelDetail,
	SiacTAvanzovincoloFin_Avanzovincolo_Complete,
	SiacRMovgestTsFin_VincoloImpegno_Accertamento_Base,
	SiacRMovgestTsFin_VincoloAccertamento_Impegno_Base,
	SiacRMovgestTsFin_VincoloImpegno_Accertamento_Bilancio,
	SiacRMovgestTsFin_VincoloAccertamento_Impegno_Bilancio,
	SiacRMovgestTsFin_VincoloAccertamento_Importo_Impegno,
	SiacRMovgestTsFin_VincoloImpegno_Importo_Accertamento,
	
	VincoloImpegno_SiacRMovgestTsDetModFin_ModelDetail,
	SiacRMovgestTsDetModFin_VincoloEsplicito,
	SiacRMovgestTsDetModFin_ModificaMovimentoGestioneSpesa,
	SiacRMovgestTsDetModFin_ModificaMovimentoGestioneEntrata,
	SiacRMovgestTsDetModFin_ImportiModificaMovimentoGestioneSpesa,
	SiacRMovgestTsDetModFin_ImportiModificaMovimentoGestioneEntrata,
	SiacRMovgestTsDetModFin_ReimputazioneModificaMovimentoGestioneSpesa,
	SiacRMovgestTsDetModFin_ReimputazioneModificaMovimentoGestioneEntrata,
	SiacRMovgestTsDetModFin_AltriDatiImportoModificaMovimentoGestioneSpesaConverter,
	SiacRMovgestTsDetModFin_AltriDatiImportoModificaMovimentoGestioneEntrataConverter,
	SiacTModificaFin_ModificaMovimentoGestioneSpesaCollegataConverter,
	SiacTModificaFin_ModificaMovimentoGestioneSpesaConverter,
	//SIAC-8589
	SiacTBilElemFin_CapitoloUscitaGestione_ModelDetail,
	SiacTBilElemFin_CapitoloEntrataGestione_ModelDetail
	;
	
	
}