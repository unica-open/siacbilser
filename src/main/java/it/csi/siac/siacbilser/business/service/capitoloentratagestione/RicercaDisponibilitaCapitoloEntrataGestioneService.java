/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.utility.CompareOperator;
import it.csi.siac.siacbilser.model.DisponibilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportoDerivatoFunctionEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class RicercaDisponibilitaCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDisponibilitaCapitoloEntrataGestioneService extends CheckedAccountBaseService<RicercaDisponibilitaCapitoloEntrataGestione, RicercaDisponibilitaCapitoloEntrataGestioneResponse> {

	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	private List<Integer> uids;
	private Integer anno0;
	private Integer anno1;
	private Integer anno2;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAnnoBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"), false);
		checkEntita(req.getCapitoloEntrataGestione(), "capitolo", false);
	}
	

	@Override
	@Transactional(readOnly = true)
	public RicercaDisponibilitaCapitoloEntrataGestioneResponse executeService(RicercaDisponibilitaCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		uids = Arrays.asList(req.getCapitoloEntrataGestione().getUid());
		anno0 = req.getAnnoBilancio();
		anno1 = anno0 + 1;
		anno2 = anno0 + 2;
		
		//ANNO 0
		DisponibilitaCapitoloEntrataGestione d0 = new DisponibilitaCapitoloEntrataGestione();
		popolaDisponibilitaVariare(d0, anno0);
		popolaNumeroAccertamenti(d0, anno0, CompareOperator.EQUALS);
		popolaDisponibilitaAccertare(d0, anno0);
		popolaAccertato(d0, anno0, CompareOperator.EQUALS);
		popolaNumeroOrdinativi(d0, anno0, CompareOperator.EQUALS);
		popolaIncassato(d0, anno0, CompareOperator.EQUALS);
		popolaDisponibilitaIncassare(d0);
		res.setDisponibilitaCapitoloEntrataGestioneAnno0(d0);
		
		//ANNO 1
		DisponibilitaCapitoloEntrataGestione d1 = new DisponibilitaCapitoloEntrataGestione();
		popolaDisponibilitaVariare(d1, anno1);
		popolaNumeroAccertamenti(d1, anno1, CompareOperator.EQUALS);
		popolaDisponibilitaAccertare(d1, anno1);
		popolaAccertato(d1, anno1, CompareOperator.EQUALS);
		res.setDisponibilitaCapitoloEntrataGestioneAnno1(d1);
		
		//ANNO 2
		DisponibilitaCapitoloEntrataGestione d2 = new DisponibilitaCapitoloEntrataGestione();
		popolaDisponibilitaVariare(d2, anno2);
		popolaNumeroAccertamenti(d2, anno2, CompareOperator.EQUALS);
		popolaDisponibilitaAccertare(d2, anno2);
		popolaAccertato(d2, anno2, CompareOperator.EQUALS);
		res.setDisponibilitaCapitoloEntrataGestioneAnno2(d2);
		
		//ANNO RESIDUO
		DisponibilitaCapitoloEntrataGestione dr = new DisponibilitaCapitoloEntrataGestione();
		popolaNumeroAccertamenti(dr, anno0, CompareOperator.LESS);
		popolaNumeroOrdinativi(dr, anno0, CompareOperator.LESS);
		popolaAccertato(dr, anno0, CompareOperator.LESS);
		popolaIncassato(dr, anno0, CompareOperator.LESS);
		res.setDisponibilitaCapitoloEntrataGestioneResiduo(dr);

	}
	
	private void popolaDisponibilitaVariare(DisponibilitaCapitoloEntrataGestione d, Integer anno) {
		ImportoDerivatoFunctionEnum importoDerivatoFunctionEnum = ImportoDerivatoFunctionEnum.valueOf("disponibilitaVariareAnno"+(anno-anno0+1));
		BigDecimal disponibilitaVariareAnno = importiCapitoloDad.findImportoDerivato(req.getCapitoloEntrataGestione().getUid(), importoDerivatoFunctionEnum);
		d.setDisponibilitaVariare0(disponibilitaVariareAnno);
	}

	private void popolaNumeroAccertamenti(DisponibilitaCapitoloEntrataGestione d, Integer anno, CompareOperator compareOperator) {
		Long numeroImpegniAnno = capitoloEntrataGestioneDad.countMovimentiNonAnnullatiCapitoloByAnno(uids, anno, compareOperator);
		d.setNumeroAccertamenti(numeroImpegniAnno);
	}
	
	private void popolaDisponibilitaAccertare(DisponibilitaCapitoloEntrataGestione d, Integer anno) {
		ImportoDerivatoFunctionEnum importoDerivatoFunctionEnum = ImportoDerivatoFunctionEnum.valueOf("disponibilitaAccertareAnno"+(anno-anno0+1));
		BigDecimal disponibilitaImpegnareAnno = importiCapitoloDad.findImportoDerivato(req.getCapitoloEntrataGestione().getUid(), importoDerivatoFunctionEnum);
		d.setDisponibilitaAccertare0(disponibilitaImpegnareAnno);
	}
	
	private void popolaAccertato(DisponibilitaCapitoloEntrataGestione d, Integer anno, CompareOperator compareOperator) {
		BigDecimal impegnatoAnno = capitoloEntrataGestioneDad.computeTotaleImportiMovimentiNonAnnullatiCapitoloByAnno(uids, anno, compareOperator);
		d.setAccertato0(impegnatoAnno);
	}
	
	private void popolaNumeroOrdinativi(DisponibilitaCapitoloEntrataGestione d, Integer anno, CompareOperator compareOperator) {
		Long numeroOrdinativi = capitoloEntrataGestioneDad.countOrdinativiIncassoNonAnnullatiCapitoloByAnno(uids, anno, compareOperator);
		d.setNumeroOrdinativi(numeroOrdinativi);
	}

	private void popolaIncassato(DisponibilitaCapitoloEntrataGestione d, Integer anno, CompareOperator compareOperator) {
		BigDecimal incassato = capitoloEntrataGestioneDad.computeTotaleImportiOrdinativoIncassoNonAnnullatiCapitoloByAnno(uids, anno, compareOperator);
		d.setIncassato0(incassato);
	}
	
	private void popolaDisponibilitaIncassare(DisponibilitaCapitoloEntrataGestione d) {
		BigDecimal disponibilitaIncassare = importiCapitoloDad.findImportoDerivato(req.getCapitoloEntrataGestione().getUid(), ImportoDerivatoFunctionEnum.disponibilitaIncassare);
		d.setDisponibilitaIncassare0(disponibilitaIncassare);
	}

}
