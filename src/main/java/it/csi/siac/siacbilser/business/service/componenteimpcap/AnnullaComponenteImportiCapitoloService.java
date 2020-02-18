/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.componenteimpcap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.utils.TipoImportoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaComponenteImportiCapitoloService extends BaseGestioneComponenteImportiCapitoloService<AnnullaComponenteImportiCapitolo, AnnullaComponenteImportiCapitoloResponse>{
	
	private Map<Integer, List<ComponenteImportiCapitolo>> mapComponentiImportoCapitolo;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitolo(), "Capitolo", false);
		checkEntita(req.getTipoComponenteImportiCapitolo(), "Tipo componente importi capitolo", false);
	}

	@Override
	@Transactional
	public AnnullaComponenteImportiCapitoloResponse executeService(AnnullaComponenteImportiCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		componenteImportiCapitoloDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setLoginOperazione(loginOperazione);
		
		mapComponentiImportoCapitolo = new HashMap<Integer, List<ComponenteImportiCapitolo>>();
	}
	
	@Override
	protected void execute() {
		initCapitolo(req.getCapitolo().getUid());
		
		loadAndCheckComponentiImportiCapitolo();
		// Clear data for each year
		
		for(Entry<Integer, List<ComponenteImportiCapitolo>> entry : mapComponentiImportoCapitolo.entrySet()) {
			BigDecimal importiToSubtract = BigDecimal.ZERO;
			
			for(ComponenteImportiCapitolo cic : entry.getValue()) {
				componenteImportiCapitoloDad.annullaComponenteImportiCapitolo(cic);
				importiToSubtract = importiToSubtract.subtract(getImporto(cic));
			}
			
			updateImportoCapitolo(entry.getKey(), importiToSubtract, TipoImportoCapitolo.Values.STANZIAMENTO);
			updateImportoCapitolo(entry.getKey(), importiToSubtract, TipoImportoCapitolo.Values.CASSA);
		}
		componenteImportiCapitoloDad.flushAndClear();
		// Load data
		loadComponentiImportiCapitolo();
	}
	
	/**
	 * Controllo che gli importi non diano problemi
	 */
	private void loadAndCheckComponentiImportiCapitolo() {
		// TODO [ComponenteImportiCapitolo]: aggiungere controlli?
		for(int i = 0; i < 3; i++) {
			int anno = capitolo.getAnnoCapitolo().intValue() + i;
			List<ComponenteImportiCapitolo> componentiImportiCapitolo = componenteImportiCapitoloDad.findComponenteImportiCapitoloByUidCapitoloAnnoTipoComponente(
					capitolo.getUid(),
					Integer.valueOf(anno),
					req.getTipoComponenteImportiCapitolo().getUid(),
					ComponenteImportiCapitoloModelDetail.Importo,
					ComponenteImportiCapitoloModelDetail.ImportiCapitolo);
			
			mapComponentiImportoCapitolo.put(Integer.valueOf(anno), componentiImportiCapitolo);
		}
		
	}

}
