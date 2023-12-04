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
public enum GenMapId implements MapId {
		
	SiacTPdceConto_Conto,
	SiacTPdceConto_Conto_Medium,
	SiacTPdceConto_Conto_Base,
	SiacTPdceConto_Conto_Minimal_WithPianoDeiConti,
	SiacTPdceConto_Conto_Minimal,
	SiacTCausaleEp_CausaleEP_Minimal_Without_Ente,
	
	SiacDPdceContoTipo_TipoConto,
	SiacDPdceFam_ClassePiano, 
	
	SiacTCausaleEp_CausaleEP, 
	SiacTCausaleEp_CausaleEP_Base, 
	SiacTCausaleEp_CausaleEP_Minimal,
	
	SiacDEvento_Evento,
	SiacDEventoTipo_TipoEvento, 
	
	SiacTPrimaNota_PrimaNota_ModelDetail,
	SiacTPrimaNota_PrimaNota_Minimal,
	SiacTPrimaNota_PrimaNota_Base,
	SiacTPrimaNota_PrimaNota,
	SiacTPrimaNota_PrimaNota_Complete,
	SiacTPrimaNota_PrimaNota_DettaglioConto,
	SiacDPrimaNotaRelTipo_TipoRelazionePrimaNota,
	SiacTPrimaNotaRateiRisconti_Rateo,
	SiacTPrimaNotaRateiRisconti_Risconto,
	
	SiacTMovEp_MovimentoEP_Base,
	SiacTMovEp_MovimentoEP_Medium,
	SiacTMovEp_MovimentoEP,
	SiacTMovEp_MovimentoEP_DettaglioConto,
	SiacTMovEpDet_MovimentoDettaglio,
	SiacTMovEpDet_MovimentoDettaglio_DettaglioConto,
	SiacTMovEpDet_MovimentoDettaglio_ModelDetail,
	
	SiacTRegMovfin_RegistrazioneMovFin_Minimal,
	SiacTRegMovfin_RegistrazioneMovFin_Base,
	SiacTRegMovfin_RegistrazioneMovFinLight_Base,
	SiacTRegMovfin_RegistrazioneMovFin,
	SiacTRegMovfin_RegistrazioneMovFinLight,
	
	SiacRConciliazioneTitolo_ConciliazionePerTitolo,
	SiacRConciliazioneCapitolo_ConciliazionePerCapitolo,
	SiacRConciliazioneBeneficiario_ConciliazionePerBeneficiario,
	
	//SIAC-5336
	SiacTGsaClassif_ClassificatoreGSA,
	SiacTGsaClassif_ClassificatoreGSA_Base,
	SiacTGsaClassif_ClassificatoreGSA_All,
	SiacTGsaClassif_ClassificatoreGSA_AllValidi,
	SiacTGsaClassif_ClassificatoreGSA_ModelDetail,

;
	
}
