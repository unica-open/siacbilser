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

import it.csi.siac.siacbilser.business.service.capitolo.VerificaEliminabilitaCapitoloBaseService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.VerificaEliminabilitaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class VerificaEliminabilitaCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaEliminabilitaCapitoloUscitaPrevisioneService
	extends VerificaEliminabilitaCapitoloBaseService<VerificaEliminabilitaCapitoloUscitaPrevisione, VerificaEliminabilitaCapitoloUscitaPrevisioneResponse, CapitoloUscitaPrevisione> {
	
	/** The verifica eliminabilita capitolo uscita gestione service. */
	@Autowired
	private VerificaEliminabilitaCapitoloUscitaGestioneService verificaEliminabilitaCapitoloUscitaGestioneService;
	
	/** The bilancio dad. */
	@Autowired
	private BilancioDad bilancioDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getCapitoloUscitaPrev(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaPrevisione"));
		
		//parametri obbligatori per ricerca puntuale
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		checkNotNull(req.getCapitoloUscitaPrev().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(req.getCapitoloUscitaPrev().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(req.getCapitoloUscitaPrev().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(req.getCapitoloUscitaPrev().getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);

	}

	@Override
	protected void init() {
		capitoloDad.setEnte(ente);
		res.setEliminabilitaCapitolo(true);	
	}
	
	@Transactional
	public VerificaEliminabilitaCapitoloUscitaPrevisioneResponse executeService(VerificaEliminabilitaCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		super.execute();
		
		if (isBilancioInFaseEsercizioProvvisorio() && !isEliminabilitaCapitoloUscitaGestione()) {
			setNonEliminabilitaCapitolo(ErroreBil.CAPITOLO_NON_ELIMINABILE.getErrore("Fallimento controlli sul Capitolo Uscita Gestione Equivalente."));
		}
	}

	@Override
	protected void testImporti(CapitoloUscitaPrevisione cup, int offsetAnno) {
		int anno = req.getCapitoloUscitaPrev().getAnnoCapitolo().intValue() + offsetAnno;
		ImportiCapitoloUP importi = importiCapitoloDad.findImportiCapitolo(cup, anno, ImportiCapitoloUP.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
		
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

	/**
	 * Checks if is bilancio in fase esercizio provvisorio.
	 *
	 * @return true, if is bilancio in fase esercizio provvisorio
	 */
	private boolean isBilancioInFaseEsercizioProvvisorio() {
		bilancioDad.setEnteEntity(req.getEnte());
		return bilancioDad.isFaseEsercizioProvvisiorio(req.getBilancio().getAnno());
	}
	
	/**
	 * Checks if is eliminabilita capitolo uscita gestione.
	 *
	 * @param cug the cug
	 * @return true, if is eliminabilita capitolo uscita gestione
	 */
	private boolean isEliminabilitaCapitoloUscitaGestione() {
		CapitoloUscitaGestione cug = new CapitoloUscitaGestione();
		cug.setAnnoCapitolo(req.getCapitoloUscitaPrev().getAnnoCapitolo());
		cug.setNumeroCapitolo(req.getCapitoloUscitaPrev().getNumeroCapitolo());
		cug.setNumeroArticolo(req.getCapitoloUscitaPrev().getNumeroArticolo());
		cug.setNumeroUEB(req.getCapitoloUscitaPrev().getNumeroUEB());
		cug.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.PROVVISORIO);
		
		// inizializza parametri della request
		VerificaEliminabilitaCapitoloUscitaGestione verificaEliminabilitaCapitoloUscitaGestione = new VerificaEliminabilitaCapitoloUscitaGestione();
		verificaEliminabilitaCapitoloUscitaGestione.setEnte(req.getEnte());
		verificaEliminabilitaCapitoloUscitaGestione.setBilancio(req.getBilancio());
		verificaEliminabilitaCapitoloUscitaGestione.setRichiedente(req.getRichiedente());
		verificaEliminabilitaCapitoloUscitaGestione.setCapitoloUscitaGest(cug);
		verificaEliminabilitaCapitoloUscitaGestione.setDataOra(new Date());
		
		// effettua ricerca
		VerificaEliminabilitaCapitoloUscitaGestioneResponse verificaEliminabilitaCapitoloUscitaGestioneResponse = 
				executeExternalService(verificaEliminabilitaCapitoloUscitaGestioneService,verificaEliminabilitaCapitoloUscitaGestione);
		
		if(verificaEliminabilitaCapitoloUscitaGestioneResponse.verificatoErrore(ErroreBil.CAPITOLO_INESISTENTE.getCodice())){
			return true;
		}
	
		res.addErrori(verificaEliminabilitaCapitoloUscitaGestioneResponse.getErrori());
		
		// valuta risposta
		return !verificaEliminabilitaCapitoloUscitaGestioneResponse.isFallimento()
				&& verificaEliminabilitaCapitoloUscitaGestioneResponse.isEliminabilitaCapitolo();
	}

	@Override
	protected CapitoloUscitaPrevisione ricercaCapitolo() {
		Integer uid = capitoloDad.ricercaIdByDatiRicercaPuntualeCapitolo(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE,
				req.getCapitoloUscitaPrev().getAnnoCapitolo(), req.getCapitoloUscitaPrev().getNumeroCapitolo(), req.getCapitoloUscitaPrev().getNumeroArticolo(),
				req.getCapitoloUscitaPrev().getNumeroUEB(), req.getCapitoloUscitaPrev().getStatoOperativoElementoDiBilancio());
		
		if(uid == null) {
			return null;
		}
		CapitoloUscitaPrevisione cug = new CapitoloUscitaPrevisione();
		cug.setUid(uid.intValue());
		return cug;
	}

	@Override
	protected void setNonEliminabilitaCapitolo(Errore errore) {
		res.setEliminabilitaCapitolo(false);
		res.addErrore(errore);
	}

	@Override
	protected void testMovimenti(CapitoloUscitaPrevisione cap) {
		checkVincoli(cap);
		checkVariazioniImporti(cap);
		checkVariazioniCodifiche(cap);
	}

}
