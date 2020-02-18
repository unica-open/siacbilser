/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.componenteimpcap;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaImportiCapitoloService extends BaseGestioneComponenteImportiCapitoloService<AggiornaImportiCapitolo, AggiornaImportiCapitoloResponse>{

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitolo(), "Capitolo");
		checkCondition(req.getCapitolo().getListaImportiCapitolo() != null && !req.getCapitolo().getListaImportiCapitolo().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Lista importi"));
		int j = 0;
		for(ImportiCapitolo importiCapitolo : req.getCapitolo().getListaImportiCapitolo()) {
			checkNotNull(importiCapitolo.getAnnoCompetenza(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno competenza importo " + j));
			// Altri controlli?
			j++;
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
	public AggiornaImportiCapitoloResponse executeService(AggiornaImportiCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		initCapitolo(req.getCapitolo().getUid());
		
		// TODO [ComponenteImportiCapitolo]: update importi
		// Update SOLO di residuo e cassa
		for(ImportiCapitolo importiCapitoloNew : req.getCapitolo().getListaImportiCapitolo()) {
			int annoImporti = importiCapitoloNew.getAnnoCompetenza().intValue();
			ImportiCapitolo importiCapitoloOld = getImportiCapitoloOld(annoImporti);
			
			importiCapitoloOld.setStanziamentoCassa(importiCapitoloNew.getStanziamentoCassa());
			importiCapitoloOld.setStanziamentoResiduo(importiCapitoloNew.getStanziamentoResiduo());
			
			importiCapitoloDad.aggiornaImportiCapitolo(capitolo, importiCapitoloOld, annoImporti);
		}
		importiCapitoloDad.flushAndClear();
		loadComponentiImportiCapitolo();
	}
	/**
	 * Ritorna gli importi capitolo gi&agrave; presenti su base dati per il dato anno
	 * @param annoImporti l'anno degli importi
	 * @return gli importi per anno
	 */
	private ImportiCapitolo getImportiCapitoloOld(int annoImporti) {
		Set<ImportiCapitoloEnum> emptySet = new HashSet<ImportiCapitoloEnum>();
		ImportiCapitolo importiCapitolo = importiCapitoloDad.findImportiCapitolo(
				capitolo,
				annoImporti,
				capitolo.getTipoCapitolo().getImportiCapitoloClass(),
				emptySet);
		checkBusinessCondition(importiCapitolo != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Importi capitolo", "per anno " + annoImporti));
		return importiCapitolo;
	}
	
}
