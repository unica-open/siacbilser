/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.utility.CompareOperator;
import it.csi.siac.siacbilser.model.DisponibilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportoDerivatoFunctionEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class RicercaDisponibilitaCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDisponibilitaCapitoloUscitaGestioneService extends CheckedAccountBaseService<RicercaDisponibilitaCapitoloUscitaGestione, RicercaDisponibilitaCapitoloUscitaGestioneResponse> {

	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	private List<Integer> uids;
	private Integer anno0;
	private Integer anno1;
	private Integer anno2;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAnnoBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"), false);
		checkEntita(req.getCapitoloUscitaGestione(), "capitolo", false);
	}

	@Override
	@Transactional(readOnly = true)
	public RicercaDisponibilitaCapitoloUscitaGestioneResponse executeService(RicercaDisponibilitaCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		uids = Arrays.asList(req.getCapitoloUscitaGestione().getUid());
		anno0 = req.getAnnoBilancio();
		anno1 = anno0 + 1;
		anno2 = anno0 + 2;
		
		//ANNO 0
		DisponibilitaCapitoloUscitaGestione d0 = new DisponibilitaCapitoloUscitaGestione();
		popolaDisponibilitaVariare(d0, anno0);
		popolaNumeroImpegni(d0, anno0, CompareOperator.EQUALS);
		popolaDisponibilitaImpegnare(d0, anno0);
		popolaImpegnato(d0, anno0, CompareOperator.EQUALS);
		// SIAC-4970
		popolaImpegnatoDaRiaccertamento(d0, anno0, CompareOperator.EQUALS);
		popolaImpegnatoDaEserciziPrec(d0, anno0, CompareOperator.EQUALS,anno0);
		
		// SIAC-6899 
		popolaFinanziatoDaFPV(d0, anno0, CompareOperator.EQUALS);
		popolaFinanziatoDaAvanzo(d0, anno0, CompareOperator.EQUALS);
		
		popolaImpegnatoDaPrenotazione(d0, anno0, CompareOperator.EQUALS);
		
		
		popolaNumeroLiquidazioni(d0, anno0, CompareOperator.EQUALS);
		popolaLiquidato(d0, anno0, CompareOperator.EQUALS);
		// SIAC-4970 
		popolaLiquidatoDaPrenotazione(d0, anno0, CompareOperator.EQUALS);
		popolaNumeroOrdinativi(d0,anno0, CompareOperator.EQUALS);
		popolaPagato(d0, anno0, CompareOperator.EQUALS);
		popolaDisponibilitaPagare(d0);
		res.setDisponibilitaCapitoloUscitaGestioneAnno0(d0);
		
		//ANNO 1
		DisponibilitaCapitoloUscitaGestione d1 = new DisponibilitaCapitoloUscitaGestione();
		popolaDisponibilitaVariare(d1, anno1);
		popolaNumeroImpegni(d1, anno1, CompareOperator.EQUALS);
		popolaDisponibilitaImpegnare(d1, anno1);
		popolaImpegnato(d1, anno1, CompareOperator.EQUALS);
		popolaImpegnatoDaRiaccertamento(d1, anno1, CompareOperator.EQUALS);
		popolaImpegnatoDaEserciziPrec(d1, anno1, CompareOperator.EQUALS,anno0);
		// SIAC-6899 
		popolaFinanziatoDaFPV(d1, anno1, CompareOperator.EQUALS);
		popolaFinanziatoDaAvanzo(d1, anno1, CompareOperator.EQUALS);
		
		popolaImpegnatoDaPrenotazione(d1, anno1, CompareOperator.EQUALS);
		res.setDisponibilitaCapitoloUscitaGestioneAnno1(d1);
		
		//ANNO 2
		DisponibilitaCapitoloUscitaGestione d2 = new DisponibilitaCapitoloUscitaGestione();
		popolaDisponibilitaVariare(d2, anno2);
		popolaNumeroImpegni(d2, anno2, CompareOperator.EQUALS);
		popolaDisponibilitaImpegnare(d2, anno2);
		popolaImpegnato(d2, anno2, CompareOperator.EQUALS);
		popolaImpegnatoDaRiaccertamento(d2, anno2, CompareOperator.EQUALS);
		popolaImpegnatoDaEserciziPrec(d2, anno2, CompareOperator.EQUALS,anno0);
		
		// SIAC-6899 
		popolaFinanziatoDaFPV(d2, anno2, CompareOperator.EQUALS);
		popolaFinanziatoDaAvanzo(d2, anno2, CompareOperator.EQUALS);
		
		popolaImpegnatoDaPrenotazione(d2, anno2, CompareOperator.EQUALS);
		res.setDisponibilitaCapitoloUscitaGestioneAnno2(d2);
		
		//ANNO RESIDUO
		DisponibilitaCapitoloUscitaGestione dr = new DisponibilitaCapitoloUscitaGestione();
		popolaNumeroImpegni(dr, anno0, CompareOperator.LESS);
		popolaNumeroLiquidazioni(dr, anno0, CompareOperator.LESS);
		popolaNumeroOrdinativi(dr, anno0, CompareOperator.LESS);
		popolaImpegnato(dr, anno0, CompareOperator.LESS);
		popolaLiquidato(dr, anno0, CompareOperator.LESS);
		popolaPagato(dr, anno0, CompareOperator.LESS);
		res.setDisponibilitaCapitoloUscitaGestioneResiduo(dr);
		
	}
	
	private void popolaDisponibilitaVariare(DisponibilitaCapitoloUscitaGestione d, Integer anno) {
		ImportoDerivatoFunctionEnum importoDerivatoFunctionEnum = ImportoDerivatoFunctionEnum.valueOf("disponibilitaVariareAnno"+(anno-anno0+1));
		BigDecimal disponibilitaVariareAnno = importiCapitoloDad.findImportoDerivato(req.getCapitoloUscitaGestione().getUid(), importoDerivatoFunctionEnum);
		d.setDisponibilitaVariare0(disponibilitaVariareAnno);
	}
	
	private void popolaNumeroImpegni(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator) {
		Long numeroImpegniAnno = capitoloUscitaGestioneDad.countMovimentiNonAnnullatiCapitoloByAnno(uids, anno, compareOperator);
		d.setNumeroImpegni(numeroImpegniAnno);
	}
	
	private void popolaDisponibilitaImpegnare(DisponibilitaCapitoloUscitaGestione d, Integer anno) {
		ImportoDerivatoFunctionEnum importoDerivatoFunctionEnum = ImportoDerivatoFunctionEnum.valueOf("disponibilitaImpegnareAnno"+(anno-anno0+1));
		BigDecimal disponibilitaImpegnareAnno = importiCapitoloDad.findImportoDerivato(req.getCapitoloUscitaGestione().getUid(), importoDerivatoFunctionEnum);
		d.setDisponibilitaImpegnare0(disponibilitaImpegnareAnno);
	}
	
	private void popolaImpegnato(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator) {
		BigDecimal impegnatoAnno = capitoloUscitaGestioneDad.computeTotaleImportiMovimentiNonAnnullatiCapitoloByAnno(uids, anno, compareOperator);
		d.setImpegnato0(impegnatoAnno);
	}
	private void popolaImpegnatoDaRiaccertamento(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator) {
		BigDecimal impegnatoAnnoDaRiaccertamento = capitoloUscitaGestioneDad.computeTotaleImportiDaRiaccMovimentiNonAnnullatiCapitoloByAnno(uids, anno, compareOperator);
		d.setImpegnatoDaRiaccertamento0(impegnatoAnnoDaRiaccertamento);
	}
	private void popolaImpegnatoDaEserciziPrec(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator,Integer annoEsercizio) {
		BigDecimal impegnatoAnnoDaEserciziPrec = capitoloUscitaGestioneDad.computeTotaleImportiDaEserciziPrecMovimentiNonAnnullatiCapitoloByAnno(uids, anno, compareOperator,annoEsercizio);
		d.setImpegnatoDaEserciziPrec0(impegnatoAnnoDaEserciziPrec);
	}
	
	
	
	// SIAC-6899 
	
	private void popolaFinanziatoDaAvanzo(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator) {
		List<String>  avavincoloTipoCode = Arrays.asList("AAM");
		BigDecimal finanziatodaAvanzo = capitoloUscitaGestioneDad.computeTotaleImportidaAvanzodaFPVNonAnnullatiCapitoloByAnno(uids, anno, compareOperator,avavincoloTipoCode);
		d.setFinanziatoDaAvanzo0(finanziatodaAvanzo);
	}
	private void popolaFinanziatoDaFPV(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator) {
		List<String>  avavincoloTipoCode = Arrays.asList("FPVSC","FPVCC");
		BigDecimal finanziatodaFPV = capitoloUscitaGestioneDad.computeTotaleImportidaAvanzodaFPVNonAnnullatiCapitoloByAnno(uids, anno, compareOperator,avavincoloTipoCode);
		d.setFinanziatoDaFPV0(finanziatodaFPV);
	}
	
	
	private void popolaImpegnatoDaPrenotazione(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator) {
		BigDecimal impegnatoAnnoDaPrenotazione = capitoloUscitaGestioneDad.computeTotaleImportiDaPrenotazioneMovimentiNonAnnullatiCapitoloByAnno(uids, anno, compareOperator);
		d.setImpegnatoDaPrenotazione0(impegnatoAnnoDaPrenotazione);
	}	
	private void popolaNumeroLiquidazioni(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator) {
		Long numeroLiquidazioni = capitoloUscitaGestioneDad.countLiquidazioniNonAnnullateCapitoloByAnno(uids, anno, compareOperator);
		d.setNumeroLiquidazioni(numeroLiquidazioni);
	}
	
	private void popolaLiquidato(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator) {
		BigDecimal liquidato = capitoloUscitaGestioneDad.computeTotaleImportiLiquidazioniNonAnnullateCapitoloByAnno(uids, anno, compareOperator);
		d.setLiquidato0(liquidato);
	}
	private void popolaLiquidatoDaPrenotazione(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator) {
		BigDecimal liquidatoDaPrenotazione = capitoloUscitaGestioneDad.computeTotaleImportiDaPrenotazioneLiquidazioniNonAnnullateCapitoloByAnno(uids, anno, compareOperator);
		d.setLiquidatoDaPrenotazioni0(liquidatoDaPrenotazione);
	}
	private void popolaNumeroOrdinativi(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator) {
		Long numeroOrdinativi = capitoloUscitaGestioneDad.countOrdinativiPagamentoNonAnnullatiCapitoloByAnno(uids, anno, compareOperator);
		d.setNumeroOrdinativi(numeroOrdinativi);
	}
	
	
	private void popolaPagato(DisponibilitaCapitoloUscitaGestione d, Integer anno, CompareOperator compareOperator) {
		BigDecimal pagato = capitoloUscitaGestioneDad.computeTotaleImportiOrdinativoPagamentoNonAnnullatiCapitoloByAnno(uids, anno, compareOperator);
		d.setPagato0(pagato);
	}
	
	private void popolaDisponibilitaPagare(DisponibilitaCapitoloUscitaGestione d) {
		BigDecimal disponibilitaPagare = importiCapitoloDad.findImportoDerivato(req.getCapitoloUscitaGestione().getUid(), ImportoDerivatoFunctionEnum.disponibilitaPagare);
		d.setDisponibilitaPagare0(disponibilitaPagare);
	}
	

}
