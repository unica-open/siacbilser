/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.componenteimpcap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.CategoriaCapitoloEnum;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.utils.TipoImportoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaComponenteImportiCapitoloService extends BaseGestioneComponenteImportiCapitoloService<AggiornaComponenteImportiCapitolo, AggiornaComponenteImportiCapitoloResponse>{
	
	private List<Pair<ComponenteImportiCapitolo, BigDecimal>> listComponentiImportoCapitolo;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitolo(), "Capitolo", false);
		checkCondition(req.getListaComponenteImportiCapitolo() != null && !req.getListaComponenteImportiCapitolo().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Lista componenti importi capitolo"));
		int i = 0;
		for(ComponenteImportiCapitolo cic : req.getListaComponenteImportiCapitolo()) {
			// ImportiCapitolo must be not null, with enough data to be identified
			checkEntita(cic, "Componente " + i);
			// Must have at least one DettaglioComponenteImportiCapitolo
			checkCondition(cic.getListaDettaglioComponenteImportiCapitolo() != null && !cic.getListaDettaglioComponenteImportiCapitolo().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Lista dettagli per componente " + i));
			
			int j = 0;
			for(DettaglioComponenteImportiCapitolo dcic : cic.getListaDettaglioComponenteImportiCapitolo()) {
				// Importo must not be null
				checkNotNull(dcic.getImporto(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo per dettaglio " + j + " per componente " + i));
				checkNotNull(dcic.getTipoDettaglioComponenteImportiCapitolo(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo per dettaglio " + j + " per componente " + i));
				j++;
			}
			i++;
		}
	}

	@Override
	protected void init() {
		super.init();
		componenteImportiCapitoloDad.setEnte(ente);
		componenteImportiCapitoloDad.setLoginOperazione(loginOperazione);
		
		importiCapitoloDad.setEnte(ente);
		importiCapitoloDad.setLoginOperazione(loginOperazione);
		
		listComponentiImportoCapitolo = new ArrayList<Pair<ComponenteImportiCapitolo, BigDecimal>>();
	}
	
	@Override
	@Transactional
	public AggiornaComponenteImportiCapitoloResponse executeService(AggiornaComponenteImportiCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		initCapitolo(req.getCapitolo().getUid());
		//task-236
		CategoriaCapitolo categoria = capitoloDad.findCategoriaCapitolo(Integer.valueOf(req.getCapitolo().getUid()));
				
		loadAndCheckComponentiImportiCapitolo();
		
		for(Pair<ComponenteImportiCapitolo, BigDecimal> pair : listComponentiImportoCapitolo) {
			
			componenteImportiCapitoloDad.aggiornaComponenteImportiCapitolo(pair.getLeft());
			
			// Aggiorna importo su ImportiCapitolo
			updateImportoCapitolo(pair.getLeft().getImportiCapitolo().getAnnoCompetenza(), pair.getRight(), TipoImportoCapitolo.Values.STANZIAMENTO);
			//SIAC-7916 si esclude applicazione dell'importo in diminuzione dello STANZIAMENTO sulla CASSA
//			updateImportoCapitolo(pair.getLeft().getImportiCapitolo().getAnnoCompetenza(), pair.getRight(), TipoImportoCapitolo.Values.CASSA);
			//task-236
			if(req.getCapitolo().getTipoCapitolo().equals(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE) && categoria != null && 
					(CategoriaCapitoloEnum.FPV.getCodice().equals(categoria.getCodice()) || CategoriaCapitoloEnum.DAM.getCodice().equals(categoria.getCodice()))) { 
				updateImportoCapitolo(pair.getLeft().getImportiCapitolo().getAnnoCompetenza(), BigDecimal.ZERO, TipoImportoCapitolo.Values.CASSA);
			}
			res.getListaComponenteImportiCapitolo().add(pair.getLeft());
		}
		componenteImportiCapitoloDad.flushAndClear();
		
		if(req.isComputeResultList()) {
			loadComponentiImportiCapitolo();
		}
	}
	
	/**
	 * Controlli di validit&agrave; degli importi capitolo:
	 * <ul>
	 *   <li>verifica che gli anni degli importi richiesti siano coerenti con gli anni del capitolo</li>
	 *   <li>verifica che gli stanziamenti per l'anno corrente siano coerenti</li>
	 * </ul>
	 */
	private void loadAndCheckComponentiImportiCapitolo() {
		int annoCapitolo = capitolo.getAnnoCapitolo().intValue();
		for(ComponenteImportiCapitolo componenteImportiCapitolo : req.getListaComponenteImportiCapitolo()) {
			ComponenteImportiCapitolo cic = componenteImportiCapitoloDad.findComponenteImportiCapitoloByUid(
					componenteImportiCapitolo.getUid(),
					ComponenteImportiCapitoloModelDetail.ImportiCapitolo,
					ComponenteImportiCapitoloModelDetail.Importo,
					ComponenteImportiCapitoloModelDetail.TipoComponenteImportiCapitolo);
			
			checkBusinessCondition(cic != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Componente importi capitolo", "uid " + componenteImportiCapitolo.getUid()));
			checkBusinessCondition(cic.getImportiCapitolo() != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Importi capitolo", "componente con uid  " + componenteImportiCapitolo.getUid()));
			int annoImporti = cic.getImportiCapitolo().getAnnoCompetenza().intValue();
			checkBusinessCondition(annoImporti >= annoCapitolo && annoImporti <= annoCapitolo + 2, ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno componente capitolo",
					": deve essere compreso tra l'anno del capitolo (" + annoCapitolo + ") e l'anno del capitolo + 2 (" + (annoCapitolo + 2) + "). Fornito anno " + annoImporti));
			
			Map<ComponentiModificaImporto, BigDecimal> importi = getImportiComponente(cic, componenteImportiCapitolo);
			setImportoComponente(cic, importi.get(ComponentiModificaImporto.NEW), TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
			
			listComponentiImportoCapitolo.add(new ImmutablePair<ComponenteImportiCapitolo, BigDecimal>(cic, importi.get(ComponentiModificaImporto.DELTA)));
		}
		
	}

	/**
	 * Ottiene gli importi per la componente, calcolandone il valore vecchio, il valore nuovo e il delta.
	 * @param oldComponenteImportiCapitolo la vecchia componente
	 * @param newComponenteImportiCapitolo la nuova componente
	 * @return gli importi suddivisi per tipo
	 */
	private Map<ComponentiModificaImporto, BigDecimal> getImportiComponente(ComponenteImportiCapitolo oldComponenteImportiCapitolo, ComponenteImportiCapitolo newComponenteImportiCapitolo) {
		Map<ComponentiModificaImporto, BigDecimal> importi = new HashMap<ComponentiModificaImporto, BigDecimal>();
		BigDecimal oldImporto = getImporto(oldComponenteImportiCapitolo);
		BigDecimal newImporto = getImporto(newComponenteImportiCapitolo);
		
		importi.put(ComponentiModificaImporto.OLD, oldImporto);
		importi.put(ComponentiModificaImporto.NEW, newImporto);
		importi.put(ComponentiModificaImporto.DELTA, newImporto.subtract(oldImporto));
		
		return importi;
	}
	
	/**
	 * Componenti dai dati in modifica per il capitolo
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 27/09/2019
	 */
	private static enum ComponentiModificaImporto {
		OLD, NEW, DELTA;
	}
	
}
