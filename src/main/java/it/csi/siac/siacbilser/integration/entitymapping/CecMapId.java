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
public enum CecMapId implements MapId {
		
	
	SiacTCassaEconOperaz_OperazioneCassa,
	SiacDCassaEconModpagTipo_ModalitaPagamentoCassa,
	SiacTCassaEcon_CassaEconomale_Minimal,
	SiacTCassaEcon_CassaEconomale, 
	SiacTCassaEconOperaz_OperazioneCassa_Base,
	SiacDCassaEconOperazTipo_TipoOperazioneCassa,
	SiacDGiustificativo_TipoGiustificativo,
	
	SiacTRichiestaEcon_RichiestaEconomale_ModelDetail,
	SiacTRichiestaEcon_RichiestaEconomale_Base,
	SiacTRichiestaEcon_RichiestaEconomale_Medium,
	SiacTRichiestaEcon_RichiestaEconomale,
	
	SiacTMovimento_Movimento_Base,
	SiacTMovimento_Movimento,
	SiacTMovimento_Movimento_Extra,
	
	SiacTGiustificativo_RendicontoRichiesta_Base,
	SiacTGiustificativo_RendicontoRichiesta_Base_Movimento,
	SiacTGiustificativo_RendicontoRichiesta_Medium,
	SiacTGiustificativo_RendicontoRichiesta,
	SiacTGiustificativoDet_Giustificativo_Base,
	SiacTGiustificativoDet_Giustificativo_Medium,
	SiacTGiustificativoDet_Giustificativo,
	
	SiacTTrasfMiss_DatiTrasfertaMissione,
	SiacDTrasportoMezzo_MezziDiTrasporto,
	SiacDRichiestaEconTipo_TipoRichiestaEconomale, 
	
	SiacRAccreditoTipoCassaEcon_ModalitaPagamentoDipendente,
	
	SiacTCassaEconStampa_StampeCassaFile_Base,
	SiacTCassaEconStampa_StampeCassaFile_ModelDetail,
	;
	
}
