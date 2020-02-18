/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacgenser.model.Evento;


/**
 * Descrive il mapping degli eventi per le registrazioni.
 * 
 * @author Domenico
 */
@EnumEntity(entityName="SiacDEvento", idPropertyName="eventoId", codePropertyName="eventoCode")
public enum SiacDEventoEnum {
	
	//##### Documenti Entrata
	SubdocumentoEntrataIVAIns("DEI-INS", SiacDCollegamentoTipoEnum.SubdocumentoEntrata,    /*agg*/ false, /*iva*/ true,  /*cassaEcon*/ false),
	SubdocumentoEntrataIVAAgg("DEI-AGG", SiacDCollegamentoTipoEnum.SubdocumentoEntrata,    /*agg*/ true , /*iva*/ true,  /*cassaEcon*/ false),
	SubdocumentoEntrataNoIVAIns("DEN-INS", SiacDCollegamentoTipoEnum.SubdocumentoEntrata,  /*agg*/ false, /*iva*/ false, /*cassaEcon*/ false),
	SubdocumentoEntrataNoIVAAgg("DEN-AGG", SiacDCollegamentoTipoEnum.SubdocumentoEntrata,  /*agg*/ true,  /*iva*/ false, /*cassaEcon*/ false),
	SubdocumentoEntrataIVAProIns("DEP-INS", SiacDCollegamentoTipoEnum.SubdocumentoEntrata, /*agg*/ false, /*iva*/ true,  /*ivaProm*/ true, /*cassaEcon*/  false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ false), //Attualmente NON utilizzato
	SubdocumentoEntrataIVAProAgg("DEP-AGG", SiacDCollegamentoTipoEnum.SubdocumentoEntrata, /*agg*/ true,  /*iva*/ true,  /*ivaProm*/ true, /*cassaEcon*/  false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ false), //Attualmente NON utilizzato
	
	
	//##### Documenti Spesa
	SubdocumentoSpesaIVAIns("DSI-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,               /*agg*/ false, /*iva*/ true,  /*cassaEcon*/ false),
	SubdocumentoSpesaIVAAgg("DSI-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,               /*agg*/ true,  /*iva*/ true,  /*cassaEcon*/ false),
	SubdocumentoSpesaNoIVAIns("DSN-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,             /*agg*/ false, /*iva*/ false, /*cassaEcon*/ false),
	SubdocumentoSpesaNoIVAAgg("DSN-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,             /*agg*/ true,  /*iva*/ false, /*cassaEcon*/ false),
	SubdocumentoSpesaIVAProIns("DSP-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,            /*agg*/ false, /*iva*/ true,  /*ivaProm*/ true,  /*cassaEcon*/ false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ false),
	SubdocumentoSpesaIVAProAgg("DSP-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,            /*agg*/ true,  /*iva*/ true,  /*ivaProm*/ true,  /*cassaEcon*/ false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ false),
	SubdocumentoSpesaResiduoIns("DSN-RES-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,       /*agg*/ false, /*iva*/ false, /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ true),
	SubdocumentoSpesaResiduoAgg("DSN-RES-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,       /*agg*/ true,  /*iva*/ false, /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ true),
//	SubdocumentoSpesaRediduoAnn("DSN-RES-ANN", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,       /*agg*/ true,  /*iva*/ false, /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ true),
	SubdocumentoSpesaIVAResiduoIns("DSI-RES-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,    /*agg*/ false, /*iva*/ true,  /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ true),
	SubdocumentoSpesaIVAResiduoAgg("DSI-RES-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,    /*agg*/ true,  /*iva*/ true,  /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ true),
//	SubdocumentoSpesaIVAResiduoAnn("DSI-RES-ANN", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,    /*agg*/ true,  /*iva*/ true,  /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ true),
	SubdocumentoSpesaIVAProResiduoIns("DSP-RES-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa, /*agg*/ false, /*iva*/ true,  /*ivaProm*/ true,  /*cassaEcon*/ false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ true),
	SubdocumentoSpesaIVAProResiduoAgg("DSP-RES-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa, /*agg*/ true,  /*iva*/ true,  /*ivaProm*/ true,  /*cassaEcon*/ false, /*notaCredito*/ false, /*codRichEcon*/ null, /*residuo*/ true),
	
	
	//##### NotaDiCreditoSpesa
	NotaDiCreditoSpesaIns("NCD-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,                 /*agg*/ false, /*iva*/ false, /*cassaEcon*/ false, /*notaCredito*/ true),
	NotaDiCreditoSpesaAgg("NCD-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,                 /*agg*/ true,  /*iva*/ false, /*cassaEcon*/ false, /*notaCredito*/ true),
	NotaDiCreditoSpesaIVAIns("NDI-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,              /*agg*/ false, /*iva*/ true,  /*cassaEcon*/ false, /*notaCredito*/ true),
	NotaDiCreditoSpesaIVAAgg("NDI-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,              /*agg*/ true,  /*iva*/ true,  /*cassaEcon*/ false, /*notaCredito*/ true),
	NotaDiCreditoSpesaIVAProIns("NCP-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,           /*agg*/ false, /*iva*/ true,  /*ivaProm*/ true,  /*cassaEcon*/ false, /*notaCredito*/ true, /*codRichEcon*/ null, /*residuo*/ false),
	NotaDiCreditoSpesaIVAProAgg("NCP-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,           /*agg*/ true,  /*iva*/ true,  /*ivaProm*/ true,  /*cassaEcon*/ false, /*notaCredito*/ true, /*codRichEcon*/ null, /*residuo*/ false),
	NotaDiCreditoSpesaRediduoIns("NCD-RES-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,      /*agg*/ false, /*iva*/ false, /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ true,  /*codRichEcon*/ null, /*residuo*/ true),
	NotaDiCreditoSpesaRediduoAgg("NCD-RES-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,      /*agg*/ true,  /*iva*/ false, /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ true,  /*codRichEcon*/ null, /*residuo*/ true),
//	NotaDiCreditoSpesaRediduoAnn("NCD-RES-ANN", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,      /*agg*/ true,  /*iva*/ false, /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ true,  /*codRichEcon*/ null, /*residuo*/ true),
	NotaDiCreditoSpesaIVAResiduoIns("NDI-RES-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,   /*agg*/ false, /*iva*/ true,  /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ true,  /*codRichEcon*/ null, /*residuo*/ true),
	NotaDiCreditoSpesaIVAResiduoAgg("NDI-RES-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,   /*agg*/ true,  /*iva*/ true,  /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ true,  /*codRichEcon*/ null, /*residuo*/ true),
//	NotaDiCreditoSpesaIVAResiduoAnn("NDI-RES-ANN", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,   /*agg*/ true,  /*iva*/ true,  /*ivaProm*/ false, /*cassaEcon*/ false, /*notaCredito*/ true,  /*codRichEcon*/ null, /*residuo*/ true),
	NotaDiCreditoSpesaIVAProResiduoIns("NCP-RES-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,/*agg*/ false, /*iva*/ true,  /*ivaProm*/ true,  /*cassaEcon*/ false, /*notaCredito*/ true,  /*codRichEcon*/ null, /*residuo*/ true),
	NotaDiCreditoSpesaIVAProResiduoAgg("NCP-RES-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa,/*agg*/ true,  /*iva*/ true,  /*ivaProm*/ true,  /*cassaEcon*/ false, /*notaCredito*/ true,  /*codRichEcon*/ null, /*residuo*/ true),
	
	
	//##### NotaDiCreditoEntrata
	NotaDiCreditoEntrataIns("NCE-INS", SiacDCollegamentoTipoEnum.SubdocumentoEntrata,       /*agg*/ false, /*iva*/ false, /*cassaEcon*/ false, /*notaCredito*/ true),
	NotaDiCreditoEntrataAgg("NCE-AGG", SiacDCollegamentoTipoEnum.SubdocumentoEntrata,       /*agg*/ true,  /*iva*/ false, /*cassaEcon*/ false, /*notaCredito*/ true),
	//SIAC-6945
//	NotaDiCreditoEntrataIVAIns("NCV-INS", SiacDCollegamentoTipoEnum.SubdocumentoEntrata,    /*agg*/ false, /*iva*/ true,  /*cassaEcon*/ false, /*notaCredito*/ true),
//	NotaDiCreditoEntrataIVAAgg("NCV-AGG", SiacDCollegamentoTipoEnum.SubdocumentoEntrata,    /*agg*/ true,  /*iva*/ true,  /*cassaEcon*/ false, /*notaCredito*/ true),
	NotaDiCreditoEntrataIVAIns("NIE-INS", SiacDCollegamentoTipoEnum.SubdocumentoEntrata,    /*agg*/ false, /*iva*/ true,  /*cassaEcon*/ false, /*notaCredito*/ true),
	NotaDiCreditoEntrataIVAAgg("NIE-AGG", SiacDCollegamentoTipoEnum.SubdocumentoEntrata,    /*agg*/ true,  /*iva*/ true,  /*cassaEcon*/ false, /*notaCredito*/ true),
	NotaDiCreditoEntrataIVAProIns("NEP-INS", SiacDCollegamentoTipoEnum.SubdocumentoEntrata, /*agg*/ false, /*iva*/ true,  /*ivaProm*/ true,  /*cassaEcon*/false, /*notaCredito*/ true, /*codRichEcon*/ null, /*residuo*/ false),
	NotaDiCreditoEntrataIVAProAgg("NEP-AGG", SiacDCollegamentoTipoEnum.SubdocumentoEntrata, /*agg*/ true,  /*iva*/ true,  /*ivaProm*/ true,  /*cassaEcon*/false, /*notaCredito*/ true, /*codRichEcon*/ null, /*residuo*/ false),
	
	
	//##### Cassa Economale - Fatture
	SubdocumentoSpesaCassaEconomaleINS("DEC-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa, /*agg*/ false, /*iva*/ false, /*cassaEcon*/ true),
	SubdocumentoSpesaCassaEconomaleAgg("DEC-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa, /*agg*/ true,  /*iva*/ false, /*cassaEcon*/ true),
	
	
	//##### Cassa Economale - Note di creduto
	NotaDiCreditoSpesaCassaEconomaleINS("NEC-INS", SiacDCollegamentoTipoEnum.SubdocumentoSpesa, /*agg*/ false, /*iva*/ false, /*cassaEcon*/ true, /*notaCredito*/ true),
	NotaDiCreditoSpesaCassaEconomaleAgg("NEC-AGG", SiacDCollegamentoTipoEnum.SubdocumentoSpesa, /*agg*/ true,  /*iva*/ false, /*cassaEcon*/ true, /*notaCredito*/ true),
	
	
	//##### Cassa Economale - Richieste
	RichiestaEconomaleCassaEconomaleRIM("CEC-RIM-INS", SiacDCollegamentoTipoEnum.RichiestaEconomale,    /*agg*/ false, /*iva*/ false, /*cassaEcon*/ true,  /*codRichEcon*/ "RIMBORSO_SPESE"),
	RichiestaEconomaleCassaEconomaleANT("CEC-ANT-INS", SiacDCollegamentoTipoEnum.RichiestaEconomale,    /*agg*/ false, /*iva*/ false, /*cassaEcon*/ true,  /*codRichEcon*/ "ANTICIPO_SPESE"),
	RichiestaEconomaleCassaEconomaleATR("CEC-ATR-INS", SiacDCollegamentoTipoEnum.RichiestaEconomale,    /*agg*/ false, /*iva*/ false, /*cassaEcon*/ true,  /*codRichEcon*/ "ANTICIPO_TRASFERTA_DIPENDENTI"),
	RichiestaEconomaleCassaEconomaleAMIIns("CEC-AMI-INS", SiacDCollegamentoTipoEnum.RichiestaEconomale, /*agg*/ false, /*iva*/ false, /*cassaEcon*/ true,  /*codRichEcon*/ "ANTICIPO_SPESE_MISSIONE"),
	RichiestaEconomaleCassaEconomalePAG("CEC-PAG_INS", SiacDCollegamentoTipoEnum.RichiestaEconomale,    /*agg*/ false, /*iva*/ false, /*cassaEcon*/ true,  /*codRichEcon*/ "PAGAMENTO"),
	
	
	//##### Cassa Economale - Rendiconti
	RendicontoRichiestaCassaEconomaleAMIInt("CEC-AMI-INT", SiacDCollegamentoTipoEnum.RendicontoRichiesta, /*agg*/ false, /*iva*/ false, /*cassaEcon*/ true,  /*codRichEcon*/ "ANTICIPO_SPESE_MISSIONE"),
	RendicontoRichiestaCassaEconomaleAMIRes("CEC-AMI-RES", SiacDCollegamentoTipoEnum.RendicontoRichiesta, /*agg*/ true,  /*iva*/ false, /*cassaEcon*/ true,  /*codRichEcon*/ "ANTICIPO_SPESE_MISSIONE"),
	RendicontoRichiestaCassaEconomaleANTInt("CEC-ANT-INT", SiacDCollegamentoTipoEnum.RendicontoRichiesta, /*agg*/ false, /*iva*/ false, /*cassaEcon*/ true,  /*codRichEcon*/ "ANTICIPO_SPESE"),
	RendicontoRichiestaCassaEconomaleANTRes("CEC-ANT-RES", SiacDCollegamentoTipoEnum.RendicontoRichiesta, /*agg*/ true,  /*iva*/ false, /*cassaEcon*/ true,  /*codRichEcon*/ "ANTICIPO_SPESE"),
	
	
	//##### Impegni
	Impegno("IMP-INS", SiacDCollegamentoTipoEnum.Impegno),
	ImpegnoPRG("IMP-PRG", SiacDCollegamentoTipoEnum.Impegno),
	ImpegnoInserisciModificaImporto("MIM-INS-I", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa), 
	ImpegnoAnnullaModificaImporto("MIM-ANN", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa), 
	ImpegnoInserisciModificaSoggetto("MIM-INS-S", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa), 
	SubImpegnoInserisciModificaImporto("MSI-INS-I", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa), 
	SubImpegnoAnnullaModificaImporto("MSI-ANN-I", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa), 
	SubImpegnoInserisciModificaSoggetto("MSI-INS-S", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa),
	SubImpegnoInserisci("SIM-INS", SiacDCollegamentoTipoEnum.SubImpegno), 
	SubImpegnoAnnulla("SIM-ANN", SiacDCollegamentoTipoEnum.SubImpegno),
	// Nuove causali per fase di bilancio Predisposizione consuntivo
	ImpegnoInserisciModificaImportoRoRCompetenzaMeno("ROR-I-COMP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa), 
	ImpegnoInserisciModificaImportoRoRCompetenzaPiu("ROR-I-COMP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa), 
	ImpegnoInserisciModificaImportoRoRResiduoMeno("ROR-I-RM-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa),
	ImpegnoInserisciModificaImportoRoRResiduoPiu("ROR-I-RP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa),
	SubImpegnoInserisciModificaImportoRoRCompetenzaMeno("ROR-SI-COMP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa), 
	SubImpegnoInserisciModificaImportoRoRCompetenzaPiu("ROR-SI-COMP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa), 
	SubImpegnoInserisciModificaImportoRoRResiduoMeno("ROR-SI-RM-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa),
	SubImpegnoInserisciModificaImportoRoRResiduoPiu("ROR-SI-RP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa),
	
	//##### Accertamenti
	Accertamento("ACC-INS", SiacDCollegamentoTipoEnum.Accertamento),
	AccertamentoInserisciModificaImporto("MAC-INS-I", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata), 
	AccertamentoAnnullaModificaImporto("MAC-ANN", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata), 
	AccertamentoInserisciModificaSoggetto("MAC-INS-S", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata),
	SubAccertamentoInserisciModificaImporto("MSA-INS-I", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata), 
	SubAccertamentoAnnullaModificaImporto("MSA-ANN-I", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata), 
	SubAccertamentoInserisciModificaSoggetto("MSA-INS-S", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata), 
	SubAccertamentoInserisci("SAC-INS", SiacDCollegamentoTipoEnum.SubAccertamento), 
	SubAccertamentoAnnulla("SAC-ANN", SiacDCollegamentoTipoEnum.SubAccertamento), 
	// Nuove causali per fase di bilancio Predisposizione consuntivo
	AccertamentoInserisciModificaImportoRoRCompetenzaMeno("ROR-A-COMP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata),
	AccertamentoInserisciModificaImportoRoRCompetenzaPiu("ROR-A-COMP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata),
	AccertamentoInserisciModificaImportoRoRResiduoMeno("ROR-A-RM-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata),
	AccertamentoInserisciModificaImportoRoRResiduoPiu("ROR-A-RP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata),
	SubAccertamentoInserisciModificaImportoRoRCompetenzaMeno("ROR-SA-COMP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata),
	SubAccertamentoInserisciModificaImportoRoRCompetenzaPiu("ROR-SA-COMP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata),
	SubAccertamentoInserisciModificaImportoRoRResiduoMeno("ROR-SA-RM-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata),
	SubAccertamentoInserisciModificaImportoRoRResiduoPiu("ROR-SA-RP-INS", SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata),

	
	//##### Liquidazioni
	LiquidazioneInserisci("LIQ-INS", SiacDCollegamentoTipoEnum.Liquidazione), 
	LiquidazioneAnnulla("LIQ-ANN", SiacDCollegamentoTipoEnum.Liquidazione), 
	LiquidazioneInserisciResiduo("LIQ-RES-INS", SiacDCollegamentoTipoEnum.Liquidazione),
//	LiquidazioneAggiornaResiduo("LIQ-RES-AGG", SiacDCollegamentoTipoEnum.Liquidazione),
//	LiquidazioneAnnullaResiduo("LIQ-RES-ANN", SiacDCollegamentoTipoEnum.Liquidazione),
	
	
	//##### Ordinativo Pagamento
	OrdinativoPagamentoInserisci("OPA-INS", SiacDCollegamentoTipoEnum.OrdinativoPagamento), 
	OrdinativoPagamentoAggiorna("OPA-AGG", SiacDCollegamentoTipoEnum.OrdinativoPagamento), 
	OrdinativoPagamentoAnnulla("OPA-ANN", SiacDCollegamentoTipoEnum.OrdinativoPagamento),
	OrdinativoPagamentoInserisciCec("ORD-CEC-INS", SiacDCollegamentoTipoEnum.OrdinativoPagamento), 
	OrdinativoPagamentoAggiornaCec("ORD-CEC-AGG", SiacDCollegamentoTipoEnum.OrdinativoPagamento), 
	OrdinativoPagamentoAnnullaCec("ORD-CEC-ANN", SiacDCollegamentoTipoEnum.OrdinativoPagamento),
	OrdinativoPagamentoInserisciGsa("OPA-SAN-INS", SiacDCollegamentoTipoEnum.OrdinativoPagamento), 
	OrdinativoPagamentoAggiornaGsa("OPA-SAN-AGG", SiacDCollegamentoTipoEnum.OrdinativoPagamento),
	
	//##### Ordinativo Incasso
	OrdinativoIncassoInserisci("OIN-INS", SiacDCollegamentoTipoEnum.OrdinativoIncasso), 
	OrdinativoIncassoAggiorna("OIN-AGG", SiacDCollegamentoTipoEnum.OrdinativoIncasso), 
	OrdinativoIncassoAnnulla("OIN-ANN", SiacDCollegamentoTipoEnum.OrdinativoIncasso), 
	OrdinativoIncassoInserisciGsa("OIN-SAN-INS", SiacDCollegamentoTipoEnum.OrdinativoIncasso), 
	OrdinativoIncassoAggiornaGsa("OIN-SAN-AGG", SiacDCollegamentoTipoEnum.OrdinativoIncasso),

	
	//##### Ratei Attivi
	RateoAttivoInserisci("RTA-AC-INS", SiacDCollegamentoTipoEnum.Rateo), //Acc
	RateoAttivoPrecedenteInserisci("RTA-AP-INS", SiacDCollegamentoTipoEnum.Rateo),
	RateoAttivoAggiorna("RTA-AC-AGG", SiacDCollegamentoTipoEnum.Rateo),
	RateoAttivoPrecedenteAggiorna("RTA-AP-AGG", SiacDCollegamentoTipoEnum.Rateo),
	
	
	//##### Ratei Passivi
	RateoPassivoInserisci("RTP-AC-INS", SiacDCollegamentoTipoEnum.Rateo), //Imp
	RateoPassivoPrecedenteInserisci("RTP-AP-INS", SiacDCollegamentoTipoEnum.Rateo),
	RateoPassivoAggiorna("RTP-AC-AGG", SiacDCollegamentoTipoEnum.Rateo),
	RateoPassivoPrecedenteAggiorna("RTP-AP-AGG", SiacDCollegamentoTipoEnum.Rateo),
	
	
	//##### Risconti Attivi
	RiscontoAttivoInserisci("RSA-AC-INS", SiacDCollegamentoTipoEnum.Risconto), //Imp
	RiscontoAttivoPrecedenteInserisci("RSA-AP-INS", SiacDCollegamentoTipoEnum.Risconto),
	RiscontoAttivoAggiorna("RSA-AC-AGG", SiacDCollegamentoTipoEnum.Risconto),
	RiscontoAttivoPrecedenteAggiorna("RSA-AP-AGG", SiacDCollegamentoTipoEnum.Risconto),
	
	
	//##### Risconti Passivi
	RiscontoPassivoInserisci("RSP-AC-INS", SiacDCollegamentoTipoEnum.Risconto), //Acc
	RiscontoPassivoPrecedenteInserisci("RSP-AP-INS", SiacDCollegamentoTipoEnum.Risconto),
	RiscontoPassivoAggiorna("RSP-AC-AGG", SiacDCollegamentoTipoEnum.Risconto),
	RiscontoPassivoPrecedenteAggiorna("RSP-AP-AGG", SiacDCollegamentoTipoEnum.Risconto),
	
	// SIAC-5344
	//##### Prima Nota Manuale
	PrimaNotaManuale("EXTR", null),
	PrimaNotaManualeImpegno("EXTR-I", SiacDCollegamentoTipoEnum.Impegno),
	PrimaNotaManualeAccertamento("EXTR-A", SiacDCollegamentoTipoEnum.Accertamento),
	PrimaNotaManualeSubImpegno("EXTR-SI", SiacDCollegamentoTipoEnum.SubImpegno),
	PrimaNotaManualeSubAccertamento("EXTR-SA", SiacDCollegamentoTipoEnum.SubAccertamento),
	
	;
	
	private final String codice;
	private final Evento evento;
	private final SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum;
	private final boolean aggiornamento;
	private final boolean iva;
	private final boolean isTipoIvaPromisqua;
	private final boolean cassaEconomale;
	private final boolean notaCredito;
	private final String codiceTipoRichiestaEconomale;
	private final boolean isResiduo;
	
	private static final String COD_RISCONTO        = "RS";
	private static final String COD_RATEO           = "RT";
	private static final String COD_ANNO_CORRENTE   = "-AC"; //Anno Corrente
	private static final String COD_ANNO_PRECEDENTE = "-AP";//Anno Precedente
	private static final String COD_INSERIMENTO     = "-INS"; //Anno Corrente
	private static final String COD_AGGIORNAMENTO   = "-AGG";
	private static final String COD_ATTIVO          = "A";
	private static final String COD_PASSIVO         = "P";
	
	
	/**
	 * 
	 * @param modelClass Classe di Model
	 * @param entityClass Classe di Entitty (es: SiacD*.class)
	 * @param idColumnName nome jpql della colonna contenente l'id
	 * @param mapId mapId per il mapping di Dozer
	 */
	private SiacDEventoEnum(String codice, SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean aggiornamento, boolean iva, boolean isTipoIvaPromisqua, boolean cassaEconomale, boolean notaCredito, String codiceTipoRichiestaEconomale, boolean isResiduo) {
		this.codice = codice;
		this.evento = new Evento(codice);
		this.siacDCollegamentoTipoEnum = siacDCollegamentoTipoEnum;
		this.aggiornamento = aggiornamento;
		this.iva = iva;
		this.isTipoIvaPromisqua = isTipoIvaPromisqua;
		this.cassaEconomale = cassaEconomale;
		this.notaCredito = notaCredito;
		this.codiceTipoRichiestaEconomale = codiceTipoRichiestaEconomale;
		this.isResiduo = isResiduo;
	}
	
	private SiacDEventoEnum(String codice, SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean aggiornamento, boolean iva) {
		this(codice,siacDCollegamentoTipoEnum,aggiornamento,iva,false,false,false,null,false);
	}
	
	private SiacDEventoEnum(String codice, SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean aggiornamento, boolean iva, boolean cassaEconomale, String codiceTipoRichiestaEconomale) {
		this(codice,siacDCollegamentoTipoEnum,aggiornamento,iva,false,cassaEconomale,false,codiceTipoRichiestaEconomale,false);
	}
	
	private SiacDEventoEnum(String codice, SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean aggiornamento, boolean iva, boolean cassaEconomale) {
		this(codice,siacDCollegamentoTipoEnum,aggiornamento,iva,false,cassaEconomale,false,null,false);
	}
	
	private SiacDEventoEnum(String codice, SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean aggiornamento, boolean iva, boolean cassaEconomale, boolean notaCredito) {
		this(codice,siacDCollegamentoTipoEnum,aggiornamento,iva,false,cassaEconomale,notaCredito,null,false);
	}
	
	/**
	 * Definizione per gestione mov fin
	 * @param modelClass Classe di Model
	 * @param entityClass Classe di Entitty (es: SiacD*.class)
	 * @param idColumnName nome jpql della colonna contenente l'id
	 * @param mapId mapId per il mapping di Dozer
	 */
	private SiacDEventoEnum(String codice, SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum) {
		this(codice, siacDCollegamentoTipoEnum, false, false, false, false, false, null, false);
	}
	
	public static SiacDEventoEnum byEventoCodice(String eventoCodice){
		for(SiacDEventoEnum ce : SiacDEventoEnum.values()){
			if(ce.getEvento().getCodice().equals(eventoCodice)){
				return ce;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping per il codice: "+ eventoCodice + " in SiacDEventoEnum");
	}
	
	public static SiacDEventoEnum byCollegamentoTipoAggiornamentoEIVA(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean aggiornamento, boolean iva, boolean isTipoIvaPromisqua, boolean isResiduo) {
		
		for(SiacDEventoEnum ce : SiacDEventoEnum.values()){
			if(ce.getSiacDCollegamentoTipoEnum() != null && ce.getSiacDCollegamentoTipoEnum().equals(siacDCollegamentoTipoEnum)
					&& ce.isAggiornamento() == aggiornamento 
					&& ((ce.isIva() == iva && !isTipoIvaPromisqua) || (ce.isTipoIvaPromisqua() == isTipoIvaPromisqua && isTipoIvaPromisqua))
					&& !ce.isNotaCredito()
					&& !ce.isCassaEconomale()
					&& (ce.isResiduo() == isResiduo)
					){
				return ce;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping per tipoCollegamento: "+ siacDCollegamentoTipoEnum +" aggiornamento: "+aggiornamento  +" iva: "+ iva + " in SiacDEventoEnum");
	}
	
	public static SiacDEventoEnum byCollegamentoTipoAggiornamentoENotaCredito(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean aggiornamento, boolean iva, boolean isTipoIvaPromisqua, boolean isResiduo) {
		
		for(SiacDEventoEnum ce : SiacDEventoEnum.values()){
			if(ce.getSiacDCollegamentoTipoEnum() != null && ce.getSiacDCollegamentoTipoEnum().equals(siacDCollegamentoTipoEnum)
					&& ce.isAggiornamento() == aggiornamento 
					&& ce.isNotaCredito()
					&& !ce.isCassaEconomale()
					&& ((ce.isIva() == iva && !isTipoIvaPromisqua) || (ce.isTipoIvaPromisqua() == isTipoIvaPromisqua && isTipoIvaPromisqua))
					&& (ce.isResiduo() == isResiduo)
					){
				return ce;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping NotaCredito per tipoCollegamento: "+ siacDCollegamentoTipoEnum +" aggiornamento: "+aggiornamento + " in SiacDEventoEnum");
	}

	public static SiacDEventoEnum byCollegamentoTipoAggiornamentoECassaEconomale(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean aggiornamento) {
		for(SiacDEventoEnum ce : SiacDEventoEnum.values()){
			if(ce.getSiacDCollegamentoTipoEnum() != null && ce.getSiacDCollegamentoTipoEnum().equals(siacDCollegamentoTipoEnum)
					&& ce.isAggiornamento() == aggiornamento 
					&& !ce.isNotaCredito()
					&& ce.isCassaEconomale()
					) {
				return ce;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping per tipoCollegamento: "+ siacDCollegamentoTipoEnum +" aggiornamento: "+aggiornamento  +" cassaEconomale: true in SiacDEventoEnum");
	}
	
	public static SiacDEventoEnum byCollegamentoTipoAggiornamentoECassaEconomaleENotaCredito(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean aggiornamento, boolean notaCredito) {
		for(SiacDEventoEnum ce : SiacDEventoEnum.values()){
			if(ce.getSiacDCollegamentoTipoEnum() != null && ce.getSiacDCollegamentoTipoEnum().equals(siacDCollegamentoTipoEnum)
					&& ce.isAggiornamento() == aggiornamento 
					&& ce.isNotaCredito() == notaCredito
					&& ce.isCassaEconomale()
					) {
				return ce;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping per tipoCollegamento: "+ siacDCollegamentoTipoEnum +" aggiornamento: "+aggiornamento  +" cassaEconomale: true in SiacDEventoEnum");
	}
	
	public static SiacDEventoEnum byCollegamentoTipoAggiornamentoECassaEconomale(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, String codiceTipoRichiestaEconomale, boolean aggiornamento) {
		for(SiacDEventoEnum ce : SiacDEventoEnum.values()){
			if(ce.getSiacDCollegamentoTipoEnum() != null && ce.getSiacDCollegamentoTipoEnum().equals(siacDCollegamentoTipoEnum)
					&& ce.isAggiornamento() == aggiornamento 
					&& ce.isCassaEconomale()
					&& (ce.getCodiceTipoRichiestaEconomale()!=null && ce.getCodiceTipoRichiestaEconomale().equals(codiceTipoRichiestaEconomale))) {
				return ce;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping per tipoCollegamento: "+ siacDCollegamentoTipoEnum +" aggiornamento: "+aggiornamento  +" codiceTipoRichiestaEconomale: "+codiceTipoRichiestaEconomale+" cassaEconomale: true in SiacDEventoEnum");
	}

	
	public static SiacDEventoEnum byCollegamentoTipoECodice(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, String codice) {
		for(SiacDEventoEnum ce : SiacDEventoEnum.values()){
			if(ce.getSiacDCollegamentoTipoEnum() != null && ce.getSiacDCollegamentoTipoEnum().equals(siacDCollegamentoTipoEnum) && ce.getCodice().equals(codice)) {
				return ce;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping per tipoCollegamento: "+ siacDCollegamentoTipoEnum +" codice: "+ codice +" in SiacDEventoEnum");
	}
	
	public static List<SiacDEventoEnum> byCollegamentoTipoENotaCreditoECassaEconomale(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean notaCredito, boolean cassaEconomale) {
		List<SiacDEventoEnum> result = new ArrayList<SiacDEventoEnum>();
		for(SiacDEventoEnum ce : SiacDEventoEnum.values()){
			if(ce.getSiacDCollegamentoTipoEnum() != null && ce.getSiacDCollegamentoTipoEnum().equals(siacDCollegamentoTipoEnum) && ce.isNotaCredito() == notaCredito &&  ce.isCassaEconomale() == cassaEconomale) {
				result.add(ce);
			}
		}
		return result;
	}
	
	public static List<SiacDEventoEnum> byCollegamentoTipoECassaEconomale(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean cassaEconomale) {
		List<SiacDEventoEnum> result = new ArrayList<SiacDEventoEnum>();
		for(SiacDEventoEnum ce : SiacDEventoEnum.values()){
			if(ce.getSiacDCollegamentoTipoEnum() != null && ce.getSiacDCollegamentoTipoEnum().equals(siacDCollegamentoTipoEnum) && ce.isCassaEconomale() == cassaEconomale) {
				result.add(ce);
			}
		}
		return result;
	}
	
	@Deprecated
	public static SiacDEventoEnum byCollegamentoTipoRateiRiscontiOld(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean isRateo,
			boolean isInserimento, boolean isAnnoCorrente) {
		
		
		StringBuilder codiceEvento = new StringBuilder();
		if(isRateo){
			codiceEvento.append("RT"); //Rateo
		} else {
			codiceEvento.append("RS"); //Risconto
		}
		
		if(siacDCollegamentoTipoEnum.equals(SiacDCollegamentoTipoEnum.Impegno)
				|| siacDCollegamentoTipoEnum.equals(SiacDCollegamentoTipoEnum.SubImpegno)){
			codiceEvento.append(isRateo ? "P" : "A"); //Attivo
		} else if(siacDCollegamentoTipoEnum.equals(SiacDCollegamentoTipoEnum.Accertamento)
				|| siacDCollegamentoTipoEnum.equals(SiacDCollegamentoTipoEnum.SubAccertamento)){
			codiceEvento.append(isRateo ? "A" : "P"); //Passivo
		} else {
			//throw new IllegalArgumentException("Tipo collegamento "+siacDCollegamentoTipoEnum+ " non supportato. Ammessi solo Impegno e Accertamento.");
			throw new IllegalArgumentException("Tipo collegamento "+siacDCollegamentoTipoEnum+ " non supportato.");
		}
		
		if(isAnnoCorrente){
			codiceEvento.append("-AC"); //Anno Corrente
		} else {
			codiceEvento.append("-AP"); //Anno Precedente
		}
		
		if(isInserimento){
			codiceEvento.append("-INS"); //Anno Corrente
		} else {
			codiceEvento.append("-AGG"); //Anno Precedente
		}
		
		return byEventoCodice(codiceEvento.toString());
	}
	
	/**
	 * Ottiene il valore di siacDEventoEnum del rateo o del risconto per un certo collegamento tipo.
	 *
	 * @param siacDCollegamentoTipoEnum the siac D collegamento tipo enum
	 * @param isRateo                   the is rateo
	 * @param isInserimento             the is inserimento
	 * @param isAnnoCorrente            the is anno corrente
	 * @return siacDeventoEnum          the siac D evento enum
	 */
	public static SiacDEventoEnum byCollegamentoTipoRateiRisconti(SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum, boolean isRateo,
			boolean isInserimento, boolean isAnnoCorrente) {
		Boolean isAssociabileRateoAttivoRiscontoPassivo =siacDCollegamentoTipoEnum.isAssociabileRateoAttivoRiscontoPassivo(); 
//		if(isAssociabileRateoAttivoRiscontoPassivo == null) {
//			throw new IllegalArgumentException("Tipo collegamento " + siacDCollegamentoTipoEnum + " non supportato.");
//		}
		return isRateo? byCollegamentoTipoRatei(isAssociabileRateoAttivoRiscontoPassivo.booleanValue(), isInserimento, isAnnoCorrente)
				: byCollegamentoTipoRisconti(!isAssociabileRateoAttivoRiscontoPassivo.booleanValue(), isInserimento,isAnnoCorrente);
		
	}
	
	public static SiacDEventoEnum byCollegamentoTipoRisconti(boolean isAttivo,boolean isInserimento, boolean isAnnoCorrente) {
		String codiceEvento = creaStringaRateoRisconto( COD_RISCONTO, 
				isAttivo? COD_ATTIVO : COD_PASSIVO, 
				isAnnoCorrente ?  COD_ANNO_CORRENTE : COD_ANNO_PRECEDENTE,
				isInserimento? COD_INSERIMENTO : COD_AGGIORNAMENTO);
		return byEventoCodice(codiceEvento);
	}
	
	public static SiacDEventoEnum byCollegamentoTipoRatei(boolean isAttivo, boolean isInserimento, boolean isAnnoCorrente) {
		
		String codiceEvento = creaStringaRateoRisconto( COD_RATEO, 
				isAttivo? COD_ATTIVO : COD_PASSIVO, 
				isAnnoCorrente ?  COD_ANNO_CORRENTE : COD_ANNO_PRECEDENTE, 
				isInserimento? COD_INSERIMENTO : COD_AGGIORNAMENTO);
		return byEventoCodice(codiceEvento);
	}
	
	public static String creaStringaRateoRisconto(String ...codici) {
		StringBuilder codiceEvento = new StringBuilder();
		for (String codice : codici) {
			codiceEvento.append(codice);
		}
		return codiceEvento.toString();
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}


	/**
	 * @return the evento
	 */
	public Evento getEvento() {
		return evento;
	}


	/**
	 * @return the siacDCollegamentoTipoEnum
	 */
	public SiacDCollegamentoTipoEnum getSiacDCollegamentoTipoEnum() {
		return siacDCollegamentoTipoEnum;
	}


	/**
	 * @return the aggiornamento
	 */
	public boolean isAggiornamento() {
		return aggiornamento;
	}


	/**
	 * @return the iva
	 */
	public boolean isIva() {
		return iva;
	}


	/**
	 * @return the isTipoIvaPromisqua
	 */
	public boolean isTipoIvaPromisqua() {
		return isTipoIvaPromisqua;
	}

	/**
	 * @return the cassaEconomale
	 */
	public boolean isCassaEconomale() {
		return cassaEconomale;
	}
	
	
	/**
	 * @return the notaCredito
	 */
	public boolean isNotaCredito() {
		return notaCredito;
	}

	/**
	 * @return the codice tipo richiesta economale
	 */
	public Object getCodiceTipoRichiestaEconomale() {
		return codiceTipoRichiestaEconomale;
	}

	/**
	 * @return the isResiduo
	 */
	public boolean isResiduo() {
		return isResiduo;
	}

}
