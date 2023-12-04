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

import it.csi.siac.siacbilser.business.service.capitolo.VerificaAnnullabilitaCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.TipoCapitolo;

/**
 * The Class VerificaAnnullabilitaCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaAnnullabilitaCapitoloUscitaGestioneService 
	extends VerificaAnnullabilitaCapitoloBaseService<VerificaAnnullabilitaCapitoloUscitaGestione, VerificaAnnullabilitaCapitoloUscitaGestioneResponse, CapitoloUscitaGestione> {

	@Transactional(readOnly = true)
	public VerificaAnnullabilitaCapitoloUscitaGestioneResponse executeService(VerificaAnnullabilitaCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected CapitoloUscitaGestione ricercaCapitolo() {
		Integer uid = capitoloDad.ricercaIdByDatiRicercaPuntualeCapitolo( TipoCapitolo.CAPITOLO_USCITA_GESTIONE,
				req.getCapitolo().getAnnoCapitolo(), req.getCapitolo().getNumeroCapitolo(), req.getCapitolo().getNumeroArticolo(),
				req.getCapitolo().getNumeroUEB(), req.getCapitolo().getStatoOperativoElementoDiBilancio());
		
		if(uid == null) {
			return null;
		}
		CapitoloUscitaGestione cug = new CapitoloUscitaGestione();
		cug.setUid(uid);
		return cug;
	}

	@Override
	protected void testImporti(CapitoloUscitaGestione cap, int offsetAnno) {
		int anno = req.getCapitolo().getAnnoCapitolo().intValue() + offsetAnno;
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
