/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

import java.util.Date;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.VerificaAnnullabilitaCapitoloBaseService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.VerificaAnnullabilitaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;

/**
 * The Class VerificaAnnullabilitaCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaAnnullabilitaCapitoloUscitaPrevisioneService 
	extends VerificaAnnullabilitaCapitoloBaseService<VerificaAnnullabilitaCapitoloUscitaPrevisione, VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse, CapitoloUscitaPrevisione> {
	
	/** The verifica annullabilita capitolo uscita gestione service. */
	@Autowired
	private VerificaAnnullabilitaCapitoloUscitaGestioneService verificaAnnullabilitaCapitoloUscitaGestioneService;
	
	/** The bilancio dad. */
	@Autowired
	private BilancioDad bilancioDad;

	@Override
	protected void init() {
		super.init();
		bilancioDad.setEnteEntity(req.getEnte());
	}
	
	@Transactional(readOnly = true)
	public VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse executeService(VerificaAnnullabilitaCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		super.execute();
		
		if (isBilancioInFaseEsercizioProvvisorio() && !isAnnullabilitaCapitoloUscitaGestione()) {
			setNonAnnullabilitaCapitolo(ErroreBil.CAPITOLO_NON_ANNULLABILE.getErrore("Fallimento controlli sul Capitolo Uscita Gestione Equivalente."));
		}
	}

	/**
	 * Checks if is bilancio in fase esercizio provvisorio.
	 *
	 * @return true, if is bilancio in fase esercizio provvisorio
	 */
	private boolean isBilancioInFaseEsercizioProvvisorio() {
		return bilancioDad.isFaseEsercizioProvvisiorio(req.getBilancio().getAnno());
	}

	/**
	 * Checks if is annullabilita capitolo uscita gestione.
	 *
	 * @param cug the cug
	 * @return true, if is annullabilita capitolo uscita gestione
	 */
	private boolean isAnnullabilitaCapitoloUscitaGestione() {
		CapitoloUscitaGestione cug = new CapitoloUscitaGestione();
		cug.setAnnoCapitolo(req.getCapitolo().getAnnoCapitolo());
		cug.setNumeroCapitolo(req.getCapitolo().getNumeroCapitolo());
		cug.setNumeroArticolo(req.getCapitolo().getNumeroArticolo());
		cug.setNumeroUEB(req.getCapitolo().getNumeroUEB());
		cug.setStatoOperativoElementoDiBilancio(req.getCapitolo().getStatoOperativoElementoDiBilancio());
		
		// inizializza parametri della request
		VerificaAnnullabilitaCapitoloUscitaGestione reqVACUG = new VerificaAnnullabilitaCapitoloUscitaGestione();
		reqVACUG.setEnte(req.getEnte());
		reqVACUG.setRichiedente(req.getRichiedente());
		reqVACUG.setCapitolo(cug);
		reqVACUG.setDataOra(new Date());
		reqVACUG.setBilancio(req.getBilancio());
		
		// effettua ricerca
		VerificaAnnullabilitaCapitoloUscitaGestioneResponse resVACUG = executeExternalService(verificaAnnullabilitaCapitoloUscitaGestioneService, reqVACUG);
		
		// valuta risposta
		return isInesistenteOrAnnullato(resVACUG);
	}
	
	@Override
	protected CapitoloUscitaPrevisione ricercaCapitolo() {
		Integer uid = capitoloDad.ricercaIdByDatiRicercaPuntualeCapitolo(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE,
				req.getCapitolo().getAnnoCapitolo(), req.getCapitolo().getNumeroCapitolo(), req.getCapitolo().getNumeroArticolo(),
				req.getCapitolo().getNumeroUEB(), req.getCapitolo().getStatoOperativoElementoDiBilancio());
		
		if(uid == null) {
			return null;
		}
		CapitoloUscitaPrevisione cug = new CapitoloUscitaPrevisione();
		cug.setUid(uid.intValue());
		return cug;
	}

	@Override
	protected void testImporti(CapitoloUscitaPrevisione cap, int offsetAnno) {
		int anno = req.getCapitolo().getAnnoCapitolo().intValue() + offsetAnno;
		ImportiCapitoloUP importi = importiCapitoloDad.findImportiCapitolo(cap, anno, ImportiCapitoloUP.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
		
		if (importi == null) {
			return;
		}
		
		testImporto(importi.getStanziamento(), "COMPETENZA per anno " + anno);
		testImporto(importi.getStanziamentoIniziale(), "COMPETENZA INIZIALE per anno " + anno);
		testImporto(importi.getStanziamentoCassa(), "CASSA per anno " + anno);
		testImporto(importi.getStanziamentoCassaIniziale(), "CASSA INIZIALE per anno " + anno);
		testImporto(importi.getStanziamentoResiduo(), "RESIDUO per anno " + anno);
		testImporto(importi.getStanziamentoResiduoIniziale(), "RESIDUO INIZIALE per anno " + anno);
		testImporto(importi.getStanziamentoProposto(), "PROPOSTO per anno " + anno);
		testImporto(importi.getFondoPluriennaleVinc(),"FONDO PLURIENNALE VINCOLATO per anno " + anno);
	}

	@Override
	protected void testMovimenti(CapitoloUscitaPrevisione cap) {
		checkVincoli(cap);
		checkVariazioniImporti(cap);
		checkVariazioniCodifiche(cap);
	}

}
