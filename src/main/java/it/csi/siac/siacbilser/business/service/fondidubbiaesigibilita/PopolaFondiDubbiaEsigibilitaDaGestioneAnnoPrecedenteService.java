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

import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaRendicontoDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.TipoCapitolo;

/**
 * Popolamento dei fondi a dubbia e difficile esazione dalla gestione dell'anno precedente
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteService extends BasePopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService<
		//capitolo (previsione perche' non si tratta di un rendiconto)
		CapitoloEntrataPrevisione,
		//capitolo equivalente
		CapitoloEntrataGestione,
		//accantonamenti relativi al capitolo
		AccantonamentoFondiDubbiaEsigibilita,
		//accantonamenti relativi al capitolo equivalente
		AccantonamentoFondiDubbiaEsigibilitaRendiconto,
		//request per il servizio di popolamento
		PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente,
		//response per il servizio di popolamento
		PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaRendicontoDad accantonamentoFondiDubbiaEsigibilitaRendicontoDad;
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	@Override
	@Transactional
	public PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteResponse executeService(PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedente serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//inizializzo i campi comuni
		capitoloEntrataPrevisioneDad.setEnte(ente);
		accantonamentoFondiDubbiaEsigibilitaDad.setEnte(ente);
		accantonamentoFondiDubbiaEsigibilitaDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> findAccantonamentiBilancioPrecedente() {
		//trovo gli accantonamenti del bilancio precedente
		return accantonamentoFondiDubbiaEsigibilitaRendicontoDad.findByBilancio(bilancioPrecedente, AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.CapitoloEntrataGestione);
	}

	@Override
	protected List<Integer> ricercaIdCapitoliPerChiave(CapitoloEntrataGestione capitoloPrecedente) {
		//cerco il capitolo equivalente tramite tipo e anno/numero capitolo/ numero articolo / numero UEB
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
		//inserisco l'accenatonamento
		accantonamentoFondiDubbiaEsigibilitaDad.create(afde);
	}
}
