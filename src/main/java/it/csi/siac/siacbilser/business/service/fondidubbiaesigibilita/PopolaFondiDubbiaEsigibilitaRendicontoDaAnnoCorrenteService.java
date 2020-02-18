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

import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrenteResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaRendicontoDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * Popolamento dei fondi a dubbia e difficile esazione, rendiconto, dall'anno precedente
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrenteService extends BasePopolaFondiDubbiaEsigibilitaDaAnnoCorrenteService<
		//capitolo (gestione perche' e' rendiconto)
		CapitoloEntrataGestione,
		//capitolo equivalente
		CapitoloEntrataPrevisione,
		//accantonamento relativo al capitolo
		AccantonamentoFondiDubbiaEsigibilitaRendiconto,
		//accantonamento relativo al capitolo equivalente
		AccantonamentoFondiDubbiaEsigibilita,
		//request del servizio da chiamare
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente,
		//response del servizio
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrenteResponse> {

	//DADS
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaRendicontoDad accantonamentoFondiDubbiaEsigibilitaRendicontoDad;
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;

	@Override
	@Transactional
	public PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrenteResponse executeService(PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoCorrente serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//inizializzazione dei dad
		capitoloEntrataGestioneDad.setEnte(ente);
		accantonamentoFondiDubbiaEsigibilitaDad.setEnte(ente);
		accantonamentoFondiDubbiaEsigibilitaDad.setLoginOperazione(loginOperazione);
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

	@Override
	protected Bilancio ottieniBilancioPrecedente() {
		//prendo il bilancio dalla request
		return req.getBilancio();
	}
	
	
}
