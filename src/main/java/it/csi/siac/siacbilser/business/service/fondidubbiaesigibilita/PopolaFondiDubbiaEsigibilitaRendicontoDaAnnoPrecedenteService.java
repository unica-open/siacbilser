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

import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaRendicontoDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.TipoCapitolo;

/**
 * Popolamento dei fondi a dubbia e difficile esazione, rendiconto, dall'anno precedente
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteService extends BasePopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService<
		//capitolo (gestione perche' rendiconto)
		CapitoloEntrataGestione,
		//capitolo equivalente
		CapitoloEntrataPrevisione,
		//accantonamenti relativi al capitolo
		AccantonamentoFondiDubbiaEsigibilitaRendiconto,
		//accantonamenti relativi al capitolo equivalente
		AccantonamentoFondiDubbiaEsigibilita,
		//request per il servizio di popolamento
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente,
		//response per il servizio di popolamento
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse> {
	//DADS
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaRendicontoDad accantonamentoFondiDubbiaEsigibilitaRendicontoDad;
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;

	@Override
	@Transactional
	public PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse executeService(PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//imposto ente e login operazione nei dad
		capitoloEntrataGestioneDad.setEnte(ente);
		accantonamentoFondiDubbiaEsigibilitaRendicontoDad.setEnte(ente);
		accantonamentoFondiDubbiaEsigibilitaRendicontoDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected List<AccantonamentoFondiDubbiaEsigibilita> findAccantonamentiBilancioPrecedente() {
		//trovo gli accantonamenti del bilancio
		return accantonamentoFondiDubbiaEsigibilitaDad.findByBilancio(bilancioAttuale, AccantonamentoFondiDubbiaEsigibilitaModelDetail.CapitoloEntrataPrevisione);
	}

	@Override
	protected List<Integer> ricercaIdCapitoliPerChiave(CapitoloEntrataPrevisione capitoloPrecedente) {
		//cerco il capitolo equivalente tramite tipo e anno/numero capitolo/ numero articolo / numero UEB
		return capitoloEntrataGestioneDad.ricercaIdCapitoliPerChiave(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE,
				Integer.valueOf(bilancioAttuale.getAnno()),
				capitoloPrecedente.getNumeroCapitolo(),
				capitoloPrecedente.getNumeroArticolo(),
				capitoloPrecedente.getNumeroUEB());
	}

	@Override
	protected CapitoloEntrataGestione findCapitolo(Integer uid) {
		//cerco il capitolo tramite uid
		return (CapitoloEntrataGestione) capitoloEntrataGestioneDad.findOneWithMinimalData(uid);
	}

	@Override
	protected AccantonamentoFondiDubbiaEsigibilitaRendiconto findAccantonamentoAttuale(CapitoloEntrataGestione cap) {
		//cerco gli accantonamenti presenti per il capitolo	
		return accantonamentoFondiDubbiaEsigibilitaRendicontoDad.findByCapitolo(cap);
	}

	@Override
	protected AccantonamentoFondiDubbiaEsigibilitaRendiconto initAccantonamento() {
		//istanzio l'oggetto
		return new AccantonamentoFondiDubbiaEsigibilitaRendiconto();
	}

	@Override
	protected void saveAccantonamento(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		//inserisco l'accenatonamento
		accantonamentoFondiDubbiaEsigibilitaRendicontoDad.create(afde);
	}
}
