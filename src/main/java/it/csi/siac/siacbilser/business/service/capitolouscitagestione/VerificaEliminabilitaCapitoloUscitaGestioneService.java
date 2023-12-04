/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.EnumSet;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.VerificaEliminabilitaCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class VerificaEliminabilitaCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaEliminabilitaCapitoloUscitaGestioneService 
	extends VerificaEliminabilitaCapitoloBaseService<VerificaEliminabilitaCapitoloUscitaGestione, VerificaEliminabilitaCapitoloUscitaGestioneResponse, CapitoloUscitaGestione> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getCapitoloUscitaGest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaGestione"));		
		
		//parametri obbligatori per ricerca puntuale
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		checkNotNull(req.getCapitoloUscitaGest().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(req.getCapitoloUscitaGest().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(req.getCapitoloUscitaGest().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(req.getCapitoloUscitaGest().getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);
	}

	@Override
	protected void init() {
		capitoloDad.setEnte(ente);
		res.setEliminabilitaCapitolo(true);	
	}
	
	@Transactional
	public VerificaEliminabilitaCapitoloUscitaGestioneResponse executeService(VerificaEliminabilitaCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected CapitoloUscitaGestione ricercaCapitolo() {
		Integer uid = capitoloDad.ricercaIdByDatiRicercaPuntualeCapitolo( TipoCapitolo.CAPITOLO_USCITA_GESTIONE,
				req.getCapitoloUscitaGest().getAnnoCapitolo(), req.getCapitoloUscitaGest().getNumeroCapitolo(), req.getCapitoloUscitaGest().getNumeroArticolo(),
				req.getCapitoloUscitaGest().getNumeroUEB(), req.getCapitoloUscitaGest().getStatoOperativoElementoDiBilancio());
		
		if(uid == null) {
			return null;
		}
		CapitoloUscitaGestione cug = new CapitoloUscitaGestione();
		cug.setUid(uid);
		return cug;
	}

	@Override
	protected void setNonEliminabilitaCapitolo(Errore errore) {
		res.setEliminabilitaCapitolo(false);
		res.addErrore(errore);
	}

	@Override
	protected void testImporti(CapitoloUscitaGestione cap, int offsetAnno) {
		int anno = req.getCapitoloUscitaGest().getAnnoCapitolo().intValue() + offsetAnno;
		ImportiCapitoloUG importi = importiCapitoloDad.findImportiCapitolo(cap, anno, ImportiCapitoloUG.class, EnumSet.noneOf(ImportiCapitoloEnum.class));
		
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
		
		testImporto(importi.getStanziamentoAsset(), "COMPETENZA ASSET per anno " + anno);
		testImporto(importi.getStanziamentoCassaAsset(), "CASSA ASSET per anno " + anno);
		testImporto(importi.getStanziamentoResAsset(), "RESIDUO ASSET per anno " + anno);
	}

	@Override
	protected void testMovimenti(CapitoloUscitaGestione cap) {
		checkVincoli(cap);
		checkVariazioniImporti(cap);
		checkVariazioniCodifiche(cap);
		checkDocumentiSpesa(cap);
		checkImpegni(cap);
	}	

}
