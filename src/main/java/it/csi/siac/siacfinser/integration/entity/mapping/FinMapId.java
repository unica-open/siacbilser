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
	SiacTMovgestTs_SubImpegno,
	SiacDMovgestTipo_CodificaExtFin,
	SiacTModifica_ModificaMovimentoGestioneSpesa,
	SiacTModificaBIL_ModificaMovimentoGestioneSpesa,
	SiacTProgramma_CodificaFin,
	SiacTClass_CodificaExtFin,
	SiacTMovgest_Accertamento,
	SiacTModifica_ModificaMovimentoGestioneEntrata,
	SiacTModificaBIL_ModificaMovimentoGestioneEntrata,
	SiacDMutuoTipo_CodificaFin,
	SiacTMovgestTs_SubAccertamento,
	SiacTMutuo_Mutuo,
	SiacTMutuo_Mutuo_Minimo,
	SiacTLiquidazione_Liquidazione,
	SiacTLiquidazione_Liquidazione_Base,
	SiacTMutuoVoce_VoceMutuo,
	SiacTMutuoVoceVar_VariazioneImportoVoceMutuo,
	SiacDContoTesoreria_CodificaFin,
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
	SiacDMutuoTipo_ClassificatoreGenerico, 
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
	
	
	;
	
	
}