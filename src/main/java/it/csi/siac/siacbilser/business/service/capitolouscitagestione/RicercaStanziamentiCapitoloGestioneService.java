/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.HashSet;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.BaseRicercaStanziamentiCapitoloGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStanziamentiCapitoloGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStanziamentiCapitoloGestioneResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.PrevisioneImpegnatoAccertato;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaStanziamentiCapitoloGestioneService 
	extends BaseRicercaStanziamentiCapitoloGestioneService<RicercaStanziamentiCapitoloGestione, RicercaStanziamentiCapitoloGestioneResponse> {
	
	

	@Override
	public void checkServiceParam() throws ServiceParamError {
		
		checkCondition(req.getUidCapitolo() != null && req.getUidCapitolo() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave capitolo uscita gestione"));
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaStanziamentiCapitoloGestioneResponse executeService(RicercaStanziamentiCapitoloGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		previsioneImpegnatoAccertatoDad.setEnte(ente);
		importiCapitoloDad.setEnte(ente);
		capitoloDad.setEnte(ente);
	}
	
	@Override
	public void execute() {
		TipoCapitolo findTipoCapitolo = capitoloDad.findTipoCapitolo(req.getUidCapitolo());
		Capitolo<?,?> cap = new CapitoloUscitaGestione();
		cap.setUid(req.getUidCapitolo());
		Bilancio bilancio = capitoloDad.getBilancioAssociatoACapitolo(cap);

//		req.setImportiDerivatiRichiesti(new HashSet<ImportiCapitoloEnum>()); req.getImportiDerivatiRichiesti().add(ImportiCapitoloEnum.diCuiImpegnatoAnno1UG); req.getImportiDerivatiRichiesti().add(ImportiCapitoloEnum.diCuiImpegnatoAnno2UG); req.getImportiDerivatiRichiesti().add(ImportiCapitoloEnum.diCuiImpegnatoAnno3UG);
//		List<ImportiCapitoloUG> listaImportiCapitolo = trovaImportiCapitolo(capitolo, ImportiCapitoloUG.class);
		
		PrevisioneImpegnatoAccertato previsioneImpegnatoAccertato = caricaPrevisioneImpegnatoAccertato();
		
		if(TipoCapitolo.CAPITOLO_USCITA_GESTIONE.equals(findTipoCapitolo)) {
			ImportiCapitoloUG importiCapitoloUG = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno(), ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());
			res.addImportoCapitolo(importiCapitoloUG);
			ImportiCapitoloUG importiCapitoloUG1 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno()+1, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());
			res.addImportoCapitolo(importiCapitoloUG1);
			ImportiCapitoloUG importiCapitoloUG2 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno()+2, ImportiCapitoloUG.class, req.getImportiDerivatiRichiesti());
			res.addImportoCapitolo(importiCapitoloUG2);
		}else if(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE.equals(findTipoCapitolo)) {
			ImportiCapitoloEG importiCapitoloEG = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno(), ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());			
			res.addImportoCapitolo(importiCapitoloEG);
			
			//Anno esercizio + 1
			ImportiCapitoloEG importiCapitoloEG1 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno()+1, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());		
			res.addImportoCapitolo(importiCapitoloEG1);

			//Anno esercizio + 2
			ImportiCapitoloEG importiCapitoloEG2 = importiCapitoloDad.findImportiCapitolo(cap, bilancio.getAnno()+2, ImportiCapitoloEG.class, req.getImportiDerivatiRichiesti());		
			res.addImportoCapitolo(importiCapitoloEG2);
		}
		
		res.setPrevisioneImpegnatoAccertato(previsioneImpegnatoAccertato);
	}
	
}
