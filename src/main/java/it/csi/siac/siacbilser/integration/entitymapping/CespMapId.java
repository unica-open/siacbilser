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
public enum CespMapId implements MapId {
		
	SiacDCespitiCategoria_CategoriaCespiti,
	SiacDCespitiCategoria_CategoriaCespiti_ModelDetail,
	SiacDCespitiCategoria_CategoriaCespiti_Minimal,
	
	SiacDCespitiCategoriaCalcoloTipo_CategoriaCespitiCalcoloTipo,
	
	SiacDCespitiBeneTipo_TipoBeneCespite_ModelDetail,
	SiacDCespitiBeneTipo_TipoBeneCespite,

	SiacTCespiti_Cespite_ModelDetail,
	SiacTCespiti_Cespite,
	
	SiacDClassificazioneGiuridica_ClassificazioneGiuridica,
	
	//
	SiacTCespitiDismissioni_DismissioneCespite,
	SiacTCespitiDismissioni_DismissioneCespite_ModelDetail,
	
	SiacTCespitiVariazione_VariazioneCespite,
	SiacTCespitiVariazione_VariazioneCespite_ModelDetail,
	
	//ammortamento
	SiacTCespitiAmmortamento_AmmortamentoAnnuoCespite,
	SiacTCespitiAmmortamento_AmmortamentoAnnuoCespite_ModelDetail,
	
	//dettaglio ammortamento
	SiacTCespitiAmmortamentoDett_DettaglioAmmortamentoAnnuoCespite,
	SiacTCespitiAmmortamentoDett_DettaglioAmmortamentoAnnuoCespite_ModelDetail, 
	SiacTCespitiAmmortamento_AmmortamentoAnnuoCespite_All, 
	
	SiacTCespitiElabAmmortamentiDett_DettaglioAnteprimaAmmortamentoAnnuoCespite_ModelDetail,
	SiacTCespitiElabAmmortamentiDett_DettaglioAnteprimaAmmortamentoAnnuoCespite,
	SiacTCespitiElabAmmortamenti_AnteprimaAmmortamentoAnnuoCespite_ModelDetail,
	
	;
	
}
