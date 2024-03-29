/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.util.converter;

import it.csi.siac.siaccommonser.util.dozer.MapId;


public enum IntegMapId implements MapId {
		
	
	RicercaImpegno_RicercaImpegniSubImpegni,
	RicercaImpegniSubImpegniResponse_RicercaImpegnoResponse,
	ListImpegno_IntegImpegno,
	RicercaAccertamento_RicercaAccertamentiSubAccertamenti,
	RicercaAccertamentoSubAccertamentoResponse_RicercaAccertamentoResponse,
	ListAccertamento_IntegAccertamento,	
	RicercaCapitoloEntrataGestione_RicercaSinteticaCapitoloEntrataGestione,
	RicercaCapitoloUscitaGestione_RicercaSinteticaCapitoloUscitaGestione,
	ListCapitoloEntrataGestione_IntegCapitoloEntrataGestione,
	ListCapitoloUscitaGestione_IntegCapitoloUscitaGestione,
	ImportiCapitoloEG_ImportoCapitoloEntrataGestione,
	ImportiCapitoloUG_ImportoCapitoloUscitaGestione,
	ClassificatoriGenerici_IntegClassificatoriGenerici, 
	//
	RicercaLiquidazione_RicercaLiquidazioni,
	ListLiquidazione_IntegLiquidazione,
	RicercaOrdinativoSpesa_RicercaOrdinativo,
	ListOrdinativoPagamento_IntegOrdinativoSpesa,
	RicercaOrdinativoIncasso_RicercaOrdinativo,
	ListOrdinativoIncasso_IntegOrdinativoIncasso,
	RicercaDocumentoSpesa_RicercaSinteticaDocumentoSpesa,
	ListDocumentiSpesa_IntegDocumentiSpesa,
	RicercaDocumentoEntrata_RicercaSinteticaDocumentoEntrata,
	ListDocumentiEntrata_IntegDocumentiEntrata,
	//
	BaseRicercaSoggetti_RicercaSoggetti,
	RicercaSinteticaSoggetti_RicercaSoggetti,
	RicercaDettaglioSoggetti_RicercaSoggettoPerChiave,
	RicercaSinteticaSoggettiResponse_RicercaSoggettiResponse,
	RicercaDettaglioSoggettiResponse_RicercaSoggettoPerChiaveResponse,
	RicercaDettaglioSoggettiResponse_RicercaSoggettiResponse,
	
	ElaboraDocumento_ElaboraFile, 
	ElaboraDocumentoResponse_ElaboraFileResponse, 
	ElaboraDocumentoAsyncResponse_AsyncServiceResponse,
	ElaboraAttiAmministrativi_ElaboraFile, 
	ElaboraAttiAmministrativiResponse_ElaboraFileResponse, 
	ElaboraAttiAmministrativiAsyncResponse_AsyncServiceResponse,
	
	//
	RicercaProvvisoriDiCassa_IntegRicercaProvvisoriDiCassa,
	ListProvvisorioDiCassa_IntegImpegnoProvvisorioDiCassa,
	
	RicercaEstesaOrdinativiSpesa_RicercaEstesaOrdinativiPagamento,
	ListRicercaEstesaOrdinativoDiPagamento_MandatoDiPagamento,
	
	RicercaEstesaLiquidazioni_RicercaEstesaLiquidazioniFin,
	ListLiquidazioni_LiquidazioniAtti,
	ListMovimentoGestione_MovimentoGestioneStilo,
	
	;
	
}
