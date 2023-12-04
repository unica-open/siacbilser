/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * The Class ControllaClassificatoriModificabiliCapitoloService.
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ControllaClassificatoriModificabiliCapitoloService extends ControllaModificabilitaCapitoloBaseService<ControllaClassificatoriModificabiliCapitolo, ControllaClassificatoriModificabiliCapitoloResponse> {

	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.capitolo.ControllaModificabilitaCapitoloBaseService#caricaLegamiACapitolo()
	 */
	@Override
	public void caricaLegamiACapitolo() {
		Map<TipologiaClassificatore,Integer> classificatoriLegatiACapitolo = capitoloDad.findClassificatoriLegatiACapitolo(req.getTipoCapitolo(), req.getNumeroCapitolo());
		res.addClassificatoriNonModificabili(classificatoriLegatiACapitolo);		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.business.service.capitolo.ControllaModificabilitaCapitoloBaseService#caricaLegamiACapitoloArticolo()
	 */
	@Override
	public void caricaLegamiACapitoloArticolo() {
		Map<TipologiaClassificatore,Integer> classificatoriLegatiACapitoloArticolo = capitoloDad.findClassificatoriLegatiACapitoloArticolo(req.getTipoCapitolo(),req.getNumeroCapitolo(), req.getNumeroArticolo());
		res.addClassificatoriNonModificabili(classificatoriLegatiACapitoloArticolo);
		
	}
	
	@Override
	protected void execute() {
		String methodName = "execute";
		//CR SIAC-3591
		if(stanziamentoCapitoliMaggioreDiZeroOppurePresenteMovimentoGestione()) {
			res.setPresenteMovimentoGestione(true);
			TipologiaClassificatore[] tipologieClassificatoreNonModificabiliSePresenteMovimentoGestione = getTipologieClassifNonModificabiliSePresenteMovimentoGestione(req.getTipoCapitolo());
			
			Map<TipologiaClassificatore,Integer> classificatori = capitoloDad.findClassificatoriLegati(req.getTipoCapitolo(), 
					req.getNumeroCapitolo(), req.getNumeroArticolo(), req.getNumeroUEB(), 
					tipologieClassificatoreNonModificabiliSePresenteMovimentoGestione);
			
			log.debug(methodName, "Il capitolo presente movimenti collegati. Aggiunta non modificabilita' per i seguenti classificatori: "+classificatori);
			res.addClassificatoriNonModificabili(classificatori);
			res.addClassificatoriNonModificabiliPerMovimentoGestione(classificatori);	
		}
		
		super.execute();
	}

	private boolean stanziamentoCapitoliMaggioreDiZeroOppurePresenteMovimentoGestione() {
		final String methodName = "stanziamentoCapitoliMaggioreDiZeroOppurePresenteMovimentoGestione";
		
		List<Integer> capitoliElemIds = capitoloDad.findCapitoliElemIds(req.getTipoCapitolo(), req.getNumeroCapitolo(), req.getNumeroArticolo(), req.getNumeroUEB(), StatoOperativoElementoDiBilancio.VALIDO);
		
		//Controllo se e' presente un movimento gestione non annullato
		Long count = capitoloDad.countMovimentiNonAnnullatiCapitolo(capitoliElemIds);//req.getTipoCapitolo(), req.getNumeroCapitolo(), req.getNumeroArticolo(), req.getNumeroUEB());
		if(count!= null && count > 0){
			log.debug(methodName, "Capitolo legato a "+count+" movimento gestione. Returning true.");
			return true;
		}
		
		//Contorllo se gli importi dei capitoli sono maggiori di zero
		for(Integer uidCapitolo : capitoliElemIds){
			ImportiCapitolo importiCapitolo = importiCapitoloDad.findImportiCapitolo(uidCapitolo.intValue(), req.getBilancio().getAnno(), ImportiCapitolo.class, null);
			if((importiCapitolo.getStanziamento()!= null && importiCapitolo.getStanziamento().compareTo(BigDecimal.ZERO)>0)
					|| (importiCapitolo.getStanziamentoCassa()!= null && importiCapitolo.getStanziamentoCassa().compareTo(BigDecimal.ZERO)>0)
					|| (importiCapitolo.getStanziamentoResiduo()!= null && importiCapitolo.getStanziamentoResiduo().compareTo(BigDecimal.ZERO)>0)){
				log.debug(methodName, "Capitolo con stanziamento maggiore di zero: "+importiCapitolo.getStanziamento()+". Returning true.");
				return true;
			}
		}
		
		log.debug(methodName, "Capitolo senza impegno valido e con stanziamento a 0. Returning false.");
		return false;
	}
	
	private TipologiaClassificatore[] getTipologieClassifNonModificabiliSePresenteMovimentoGestione(TipoCapitolo tipoCapitolo){
		if(TipoCapitolo.isTipoCapitoloEntrata(tipoCapitolo)) {
			return new TipologiaClassificatore[] { 	TipologiaClassificatore.TITOLO_ENTRATA, 
													TipologiaClassificatore.TIPOLOGIA, 
													TipologiaClassificatore.CATEGORIA,
													TipologiaClassificatore.PDC,
													TipologiaClassificatore.PDC_I,
													TipologiaClassificatore.PDC_II,
													TipologiaClassificatore.PDC_III,
													TipologiaClassificatore.PDC_IV,
													TipologiaClassificatore.PDC_V,
													TipologiaClassificatore.SIOPE_ENTRATA,
													TipologiaClassificatore.SIOPE_ENTRATA_I,
													TipologiaClassificatore.SIOPE_ENTRATA_II,
													TipologiaClassificatore.SIOPE_ENTRATA_III,
													};
		} else {
			return new TipologiaClassificatore[] { 	TipologiaClassificatore.MISSIONE, 
													TipologiaClassificatore.PROGRAMMA, 
													TipologiaClassificatore.TITOLO_SPESA, 
													TipologiaClassificatore.MACROAGGREGATO,
													TipologiaClassificatore.PDC,
													TipologiaClassificatore.PDC_I,
													TipologiaClassificatore.PDC_II,
													TipologiaClassificatore.PDC_III,
													TipologiaClassificatore.PDC_IV,
													TipologiaClassificatore.PDC_V,
													TipologiaClassificatore.GRUPPO_COFOG,
													TipologiaClassificatore.DIVISIONE_COFOG,
													TipologiaClassificatore.CLASSE_COFOG,
													TipologiaClassificatore.SIOPE_SPESA,
													TipologiaClassificatore.SIOPE_SPESA_I,
													TipologiaClassificatore.SIOPE_SPESA_II,
													TipologiaClassificatore.SIOPE_SPESA_III,
													};
		}
	}

}
