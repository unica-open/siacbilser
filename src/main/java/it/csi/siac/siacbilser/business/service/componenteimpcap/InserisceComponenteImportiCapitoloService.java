/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.componenteimpcap;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.utils.TipoImportoCapitolo;
import it.csi.siac.siaccommon.util.cache.Cache;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siaccommon.util.cache.MapCache;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceComponenteImportiCapitoloService extends BaseGestioneComponenteImportiCapitoloService<InserisceComponenteImportiCapitolo, InserisceComponenteImportiCapitoloResponse>{

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitolo(), "Capitolo", false);
		checkCondition(req.getListaComponenteImportiCapitolo() != null && !req.getListaComponenteImportiCapitolo().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Lista componenti importi capitolo"));
		int i = 0;
		for(ComponenteImportiCapitolo cic : req.getListaComponenteImportiCapitolo()) {
			// ImportiCapitolo must be not null, with enough data to be identified
			checkNotNull(cic.getImportiCapitolo(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importi capitolo per componente " + i));
			checkNotNull(cic.getImportiCapitolo().getAnnoCompetenza(), "Anno Importi capitolo per componente " + i, false);
			// TipoComponenteImportiCapitolo must not be null
			checkEntita(cic.getTipoComponenteImportiCapitolo(), "Tipo componente importi capitolo per componente " + i, false);
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
	}
	
	@Override
	@Transactional
	public InserisceComponenteImportiCapitoloResponse executeService(InserisceComponenteImportiCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		initCapitolo(req.getCapitolo().getUid());
		
		loadAndCheckComponentiImportiCapitolo();
		
		for(ComponenteImportiCapitolo cic : req.getListaComponenteImportiCapitolo()) {
			// Save componente
			componenteImportiCapitoloDad.inserisciComponenteImportiCapitolo(cic);
			// Aggiornamento importi
			updateImportoCapitolo(cic.getImportiCapitolo().getAnnoCompetenza(), getImporto(cic), TipoImportoCapitolo.Values.STANZIAMENTO);
			updateImportoCapitolo(cic.getImportiCapitolo().getAnnoCompetenza(), getImporto(cic), TipoImportoCapitolo.Values.CASSA);
			res.getListaComponenteImportiCapitolo().add(cic);
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
	 *   <li>verifica che non si richieda di inserire dati su un tipo componente gia' utilizzato</li>
	 *   <li>verifica che gli stanziamenti per l'anno corrente siano coerenti</li>
	 * </ul>
	 */
	private void loadAndCheckComponentiImportiCapitolo() {
		int annoCapitolo = capitolo.getAnnoCapitolo().intValue();
		final Set<ImportiCapitoloEnum> emptySet = Collections.emptySet();
		Cache<Integer, ImportiCapitolo> cacheImportiCapitolo = new MapCache<Integer, ImportiCapitolo>();
		
		for(ComponenteImportiCapitolo cic : req.getListaComponenteImportiCapitolo()) {
			Integer annoImporti = cic.getImportiCapitolo().getAnnoCompetenza();
			int annoImportiInt = annoImporti.intValue();
			checkBusinessCondition(annoImportiInt >= annoCapitolo && annoImportiInt <= annoCapitolo + 2, ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno componente capitolo",
					": deve essere compreso tra l'anno del capitolo (" + annoCapitolo + ") e l'anno del capitolo + 2 (" + (annoCapitolo + 2) + "). Fornito anno " + annoImportiInt));
			Long collegamentiPerTipo = componenteImportiCapitoloDad.countByCapitoloAndTipoComponenteImportiCapitolo(capitolo.getUid(), cic.getTipoComponenteImportiCapitolo().getUid());
			checkBusinessCondition(collegamentiPerTipo == null || collegamentiPerTipo.longValue() == 0L,
					ErroreCore.ATTRIBUTO_GIA_PRESENTE.getErrore("Componente", "tipo " + cic.getTipoComponenteImportiCapitolo().getUid()));
			
			ImportiCapitolo importiCapitolo = cacheImportiCapitolo.get(annoImporti, new ImportiCapitoloCacheInitializer(importiCapitoloDad, capitolo, emptySet));
			checkBusinessCondition(importiCapitolo != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Importi capitolo", "anno " + annoImportiInt));
			// Save importi in Componente
			cic.setImportiCapitolo(importiCapitolo);
		}
		
	}
	
	/**
	 * Cache initializer per l'importo capitolo di un dato anno
	 * @author Marchino Alessandro
	 */
	private static class ImportiCapitoloCacheInitializer implements CacheElementInitializer<Integer, ImportiCapitolo> {
		
		private final ImportiCapitoloDad importiCapitoloDad;
		private final Capitolo<?, ?> capitolo;
		private final Set<ImportiCapitoloEnum> importiCapitoloEnum;

		/**
		 * Costruttore di wrap per i parametri
		 * @param importiCapitoloDad il DAD degli importi
		 * @param capitolo il capitolo
		 * @param importiCapitoloEnum gli importi richiesti
		 */
		ImportiCapitoloCacheInitializer(ImportiCapitoloDad importiCapitoloDad, Capitolo<?, ?> capitolo, Set<ImportiCapitoloEnum> importiCapitoloEnum) {
			this.importiCapitoloDad = importiCapitoloDad;
			this.capitolo = capitolo;
			this.importiCapitoloEnum = importiCapitoloEnum;
		}

		@Override
		public ImportiCapitolo initialize(Integer key) {
			return importiCapitoloDad.findImportiCapitolo(
					capitolo,
					key.intValue(),
					capitolo.getTipoCapitolo().getImportiCapitoloClass(),
					importiCapitoloEnum);
		}
		
	}

}
