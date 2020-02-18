/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.TipoCapitolo;

/**
 * Popolamento dei fondi a dubbia e difficile esazione dall'anno precedente
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService extends BasePopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService<
		//capitolo (previsione perche' non si tratta di un rendiconto)
		CapitoloEntrataPrevisione,
		//capitolo equivalente
		CapitoloEntrataPrevisione,
		//accantonamenti relativi al capitolo
		AccantonamentoFondiDubbiaEsigibilita,
		//accantonamenti relativi al capitolo equivalente
		AccantonamentoFondiDubbiaEsigibilita,
		//request per il servizio di popolamento
		PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente,
		//response per il servizio di popolamento
		PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	@Override
	@Transactional
	public PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteResponse executeService(PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//imposto ente e login operazione nei dad
		capitoloEntrataPrevisioneDad.setEnte(ente);
		accantonamentoFondiDubbiaEsigibilitaDad.setEnte(ente);
		accantonamentoFondiDubbiaEsigibilitaDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected List<AccantonamentoFondiDubbiaEsigibilita> findAccantonamentiBilancioPrecedente() {
		//trovo gli accantonamenti del bilancio
		return accantonamentoFondiDubbiaEsigibilitaDad.findByBilancio(bilancioPrecedente, AccantonamentoFondiDubbiaEsigibilitaModelDetail.CapitoloEntrataPrevisione);
	}

	@Override
	protected List<Integer> ricercaIdCapitoliPerChiave(CapitoloEntrataPrevisione capitoloPrecedente) {
		//cerco il capitolo tramite tipo e anno/numero capitolo/ numero articolo / numero UEB
		return capitoloEntrataPrevisioneDad.ricercaIdCapitoliPerChiave(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE,
				Integer.valueOf(bilancioAttuale.getAnno()),
				capitoloPrecedente.getNumeroCapitolo(),
				capitoloPrecedente.getNumeroArticolo(),
				capitoloPrecedente.getNumeroUEB());
	}

	@Override
	protected CapitoloEntrataPrevisione findCapitolo(Integer uid) {
		//cerco il capitolo tramite uid
		return (CapitoloEntrataPrevisione) capitoloEntrataPrevisioneDad.findOneWithMinimalData(uid);
	}

	@Override
	protected AccantonamentoFondiDubbiaEsigibilita findAccantonamentoAttuale(CapitoloEntrataPrevisione cap) {
		//cerco gli accantonamenti presenti per il capitolo
		return accantonamentoFondiDubbiaEsigibilitaDad.findByCapitolo(cap);
	}

	@Override
	protected AccantonamentoFondiDubbiaEsigibilita initAccantonamento() {
		//istanzio l'oggetto
		return new AccantonamentoFondiDubbiaEsigibilita();
	}

	@Override
	protected void saveAccantonamento(AccantonamentoFondiDubbiaEsigibilita afde) {
		//inserisco l'accantonamento
		accantonamentoFondiDubbiaEsigibilitaDad.create(afde);
	}
	
}
