/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.previsioneimpegnatoaccertato;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.PrevisioneImpegnatoAccertatoDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.PrevisioneImpegnatoAccertato;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceCapitoloDiEntrataGestioneService.
 */

public abstract class CrudPrevisioneImpegnatoAccertatoBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {


	/** The capitolo entrata gestione dad. */
	@Autowired
	protected PrevisioneImpegnatoAccertatoDad previsioneImpegnatoAccertatoDad;
	
	@Autowired
	protected CapitoloDad capitoloDad;
	
	protected PrevisioneImpegnatoAccertato previsioneImpegnatoAccertato;
	
	protected void initDads() {
		previsioneImpegnatoAccertatoDad.setEnte(ente);
		previsioneImpegnatoAccertatoDad.setLoginOperazione(loginOperazione);
		capitoloDad.setEnte(ente);
	}	
	
	
	protected PrevisioneImpegnatoAccertato caricaPrevisioneImpegnatoDaDb() {
		if(previsioneImpegnatoAccertato.getUid() != 0) {
			return previsioneImpegnatoAccertatoDad.caricaPrevisioneImpegnatoAccertatoById(previsioneImpegnatoAccertato.getUid());
		}
		
		return previsioneImpegnatoAccertatoDad.caricaPrevisioneImpegnatoAccertatoByIdCapitolo(previsioneImpegnatoAccertato.getCapitolo().getUid());
	}

	protected void caricaIdCapitoloByChiaveLogica() {
		Capitolo<?,?> cap = previsioneImpegnatoAccertato.getCapitolo();
		if(previsioneImpegnatoAccertato.getUid() != 0 || cap== null || cap.getUid() != 0) {
			return;
		}
		List<Integer> idCapitolos = capitoloDad.ricercaIdByChiaveLogicaCapitolo(cap.getTipoCapitolo(), cap.getAnnoCapitolo(), cap.getNumeroCapitolo(), cap.getNumeroArticolo(), cap.getNumeroUEB());
		if(idCapitolos == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("capitolo"  + getChiaveLogicaCapitolo() ));
		}
		if(idCapitolos.size() > 1) {
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("trovati piu' capitoli corrispondenti a questi dati: " + getChiaveLogicaCapitolo() ));
		}
		Integer idCapitolo = idCapitolos.get(0);
		if(idCapitolo == null || idCapitolo.intValue() == 0) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("capitolo" + getChiaveLogicaCapitolo()));
		}
		previsioneImpegnatoAccertato.getCapitolo().setUid(idCapitolo);
		
	}
	
	protected String getChiaveLogicaCapitolo() {
		Capitolo<?, ?> cap = previsioneImpegnatoAccertato.getCapitolo();
		if(cap == null) {
			return "null";
		}
		StringBuilder s = new StringBuilder();
		if(cap.getTipoCapitolo() != null) {
			s.append(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE.equals(cap.getTipoCapitolo()) ? " entrata "  
					:TipoCapitolo.CAPITOLO_USCITA_GESTIONE.equals(cap.getTipoCapitolo()) ? " spesa " : " [] "  );
		}
		s.append(cap.getAnnoCapitolo()!= null? cap.getAnnoCapitolo() : "null").append("/")
			.append(cap.getNumeroCapitolo()!= null? cap.getNumeroCapitolo() : "null").append("/")
			.append(cap.getNumeroArticolo()!= null? cap.getNumeroArticolo() : "null");
		if(isGestioneUEB()) {
			s.append("/").append(cap.getNumeroUEB() != null? cap.getNumeroUEB() : "null");
		}
		return s.toString();
	}
	
	protected void checkStatoCapitolo(Integer uidCapitolo) {
		StatoOperativoElementoDiBilancio statoOperativo = capitoloDad.findStatoOperativoCapitolo(uidCapitolo);
		if(statoOperativo == null || StatoOperativoElementoDiBilancio.ANNULLATO.equals(statoOperativo)){
			String descStato = statoOperativo == null? "null" : statoOperativo.name();
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("capitolo", descStato));
		}
	}

	
	
	
}
