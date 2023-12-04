/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.capitolo;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaSinteticaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.capitolo.RicercaCapitoloEntrataGestione;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.capitolo.RicercaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacintegser.model.integ.CapitoloEntrataGestione;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCapitoloEntrataGestioneService extends
		RicercaCapitoloService<RicercaCapitoloEntrataGestione, RicercaCapitoloEntrataGestioneResponse>
{

	@Override
	protected RicercaCapitoloEntrataGestioneResponse execute(RicercaCapitoloEntrataGestione ireq)
	{
		RicercaSinteticaCapitoloEntrataGestione req = map(ireq,RicercaSinteticaCapitoloEntrataGestione.class, IntegMapId.RicercaCapitoloEntrataGestione_RicercaSinteticaCapitoloEntrataGestione);
		req.setEnte(ente);
		req.setParametriPaginazione(ParametriPaginazione.TUTTI_GLI_ELEMENTI);
		
		RicercaCapitoloEntrataGestioneResponse ires = instantiateNewIRes();

		RicercaSinteticaCapitoloEntrataGestioneResponse res = appCtx.getBean(RicercaSinteticaCapitoloEntrataGestioneService.class).executeService(req);

		checkServiceResponse(res);
		
		if(res.getCapitoli()==null || res.getCapitoli().isEmpty()){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " nessun filtro di ricerca soddisfatto");
			return ires;
		}
		
		List<CapitoloEntrataGestione> elencoCapitoli = dozerUtil.mapList(res.getCapitoli(),CapitoloEntrataGestione.class, IntegMapId.ListCapitoloEntrataGestione_IntegCapitoloEntrataGestione);
			
		// completo la sac con l'informazione della direzione, se il capitolo è agganciato a un cdc (settore)
		// serviceHelper.completaSacConLivelloDirezione(elencoCapitoli, ente, richiedente);
				
		//ires.setEnte(dozerUtil.map(req.getEnte(), Ente.class));
		ires.setCapitoliEntrataGestione(elencoCapitoli);

		return ires;
	}

}
