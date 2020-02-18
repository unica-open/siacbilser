/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.capitolo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaSinteticaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacintegser.business.service.ServiceHelper;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.capitolo.RicercaCapitoloUscitaGestione;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.capitolo.RicercaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacintegser.model.integ.CapitoloUscitaGestione;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCapitoloUscitaGestioneService extends
	RicercaCapitoloService<RicercaCapitoloUscitaGestione, RicercaCapitoloUscitaGestioneResponse>
{

	@Autowired 
	ServiceHelper serviceHelper;
	
	@Override
	protected RicercaCapitoloUscitaGestioneResponse execute(RicercaCapitoloUscitaGestione ireq)
	{
		RicercaSinteticaCapitoloUscitaGestione req = map(ireq,
				RicercaSinteticaCapitoloUscitaGestione.class, IntegMapId.RicercaCapitoloUscitaGestione_RicercaSinteticaCapitoloUscitaGestione);
		req.setEnte(ente);
		req.setParametriPaginazione(ParametriPaginazione.TUTTI_GLI_ELEMENTI);
		
		RicercaCapitoloUscitaGestioneResponse ires = instantiateNewIRes();
		
		RicercaSinteticaCapitoloUscitaGestioneResponse res = appCtx.getBean(RicercaSinteticaCapitoloUscitaGestioneService.class).executeService(req);
		
		checkBusinessServiceResponse(res);
		
		if(res.getCapitoli()==null || res.getCapitoli().isEmpty()){
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, " nessun filtro di ricerca soddisfatto");
			return ires;
		}
		
		List<CapitoloUscitaGestione> elencoCapitoli = dozerUtil.mapList(res.getCapitoli(),CapitoloUscitaGestione.class, IntegMapId.ListCapitoloUscitaGestione_IntegCapitoloUscitaGestione);
		
		// completo la sac con l'informazione della direzione, se il capitolo è agganciato a un cdc (settore)
		// serviceHelper.completaSacConLivelloDirezione(elencoCapitoli, ente, richiedente);
			
		//ires.setEnte(dozerUtil.map(req.getEnte(), Ente.class));
		ires.setCapitoliUscitaGestione(elencoCapitoli);

		return ires;
	}



}
