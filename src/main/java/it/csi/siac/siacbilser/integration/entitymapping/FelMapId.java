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
 *
 */
public enum FelMapId implements MapId {
		
	SirfelTFattura_FatturaFEL_Base,
	SirfelTFattura_FatturaFEL,
	SirfelTFattura_FatturaFEL_Full_PrestatoreFEL,
	
	SirfelTPrestatore_PrestatoreFEL_Base,
	SirfelTPrestatore_PrestatoreFEL,
	
	SirfelTCassaPrevidenziale_CassaPrevidenzialeFEL_Minimal,
	SirfelTCassaPrevidenziale_CassaPrevidenzialeFEL,
	
	SirfelTPagamento_PagamentoFEL,
	SirfelTPagamento_PagamentoFEL_FatturaFEL,
	SirfelTDettaglioPagamento_DettaglioPagamentoFEL,

	SirfelTRiepilogoBeni_RiepilogoBeniFEL_Base,
	SirfelTRiepilogoBeni_RiepilogoBeniFEL,
	
	SirfelTOrdineAcquisto_OrdineAcquistoFEL,
	SirfelTCausale_CausaleFEL,
	SirfelTFattureCollegate_FattureCollegateFEL,
	SirfelTDatiGestionali_DatiGestionaliFEL,
	SirfelTPortaleFatture_PortaleFattureFEL,
	SirfelTProtocollo_ProtocolloFEL,
	
	;
	
}
