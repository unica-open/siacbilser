/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.componenteimpcap;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.base.BaseComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ComponenteImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.utils.DettaglioImportoCapitolo;
import it.csi.siac.siacbilser.model.utils.TipoImportoCapitolo;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siacfin2ser.model.CapitoloModelDetail;

/**
 * The Class BaseGestioneComponenteImportiCapitoloService.
 */
public abstract class BaseGestioneComponenteImportiCapitoloService<REQ extends ServiceRequest, RES extends BaseComponenteImportiCapitoloResponse> extends CheckedAccountBaseService<REQ, RES> {
	
	protected static final String TIPO_IMPORTO_STANZIAMENTO = "STA";
	
	@Autowired protected CapitoloDad capitoloDad;
	@Autowired protected ImportiCapitoloDad importiCapitoloDad;
	@Autowired protected ComponenteImportiCapitoloDad componenteImportiCapitoloDad;
	
	protected Capitolo<?, ?> capitolo;
	
	/**
	 * Inizializzazione del capitolo
	 */
	protected void initCapitolo(int uid) {
		capitolo = capitoloDad.findOneWithMinimalData(Integer.valueOf(uid), CapitoloModelDetail.ExCapitolo);
		Utility.CTL.set(capitolo);
	}
	/**
	 * Caricamento delle componenti importi capitolo
	 */
	protected void loadComponentiImportiCapitolo() {
		final String methodName = "loadComponentiImportiCapitolo";
		Set<ImportiCapitoloEnum> importiDerivatiRichiesti = getImportiDerivatiRichiesti();
		ComponenteImportiCapitoloModelDetail[] modelDetails = getModelDetails();
		ImportiCapitolo annoCorrente = null;
		TipoCapitolo tipoCapitolo = capitolo.getTipoCapitolo();
		TipoCapitolo tipoCapitoloEx = capitoloDad.getTipoCapitoloExCapitolo(tipoCapitolo);
		
		ImportiCapitolo importiCapitoloEquivalente = getImportiCapitoloEquivalente(tipoCapitoloEx, importiDerivatiRichiesti, modelDetails);

		res.getListaImportiCapitolo().add(importiCapitoloEquivalente);
		
		// Data for 3 years (plus year - 1)
		for(int i = 0; i < 3; i++) {
			int uidCapitolo = capitolo.getUid();
			int anno = capitolo.getAnnoCapitolo().intValue() + i;
			ImportiCapitolo importiCapitolo = popolaImporti(uidCapitolo, anno, tipoCapitolo, importiDerivatiRichiesti, modelDetails);
			res.getListaImportiCapitolo().add(importiCapitolo);
			// TODO: leggere i dati in maniera corretta
			if(i == 0) {
				annoCorrente = importiCapitolo;
			}
		}
		// TODO [ComponenteImportiCapitolo]: data per residuo e anni successivi
		res.setImportiCapitoloResiduo(initResiduo(annoCorrente));
		res.setImportiCapitoloAnniSuccessivi(initAnniSuccessivi(annoCorrente));
	}
	/**
	 * @param tipoCapitoloEx
	 * @param importiDerivatiRichiesti
	 * @param modelDetails
	 * @return
	 */
	private ImportiCapitolo getImportiCapitoloEquivalente(TipoCapitolo tipoCapitoloEx, Set<ImportiCapitoloEnum> importiDerivatiRichiesti, ComponenteImportiCapitoloModelDetail[] modelDetails) {
		int annoMenoUno = capitolo.getAnnoCapitolo().intValue() - 1;
		ImportiCapitolo importiCapitoloEquivalente = null;
		if(capitolo.getUidExCapitolo() == 0) {
			importiCapitoloEquivalente = tipoCapitoloEx.newImportiCapitoloInstance();
			importiCapitoloEquivalente.setAnnoCompetenza(Integer.valueOf(annoMenoUno));
			return importiCapitoloEquivalente;
		}
		
		importiCapitoloEquivalente = popolaImporti(capitolo.getUidExCapitolo(), annoMenoUno, tipoCapitoloEx, importiDerivatiRichiesti, modelDetails);
		return importiCapitoloEquivalente;
	}
	/**
	 * @param uidCapitolo
	 * @param anno
	 * @param tipoCapitolo
	 * @param importiDerivatiRichiesti
	 * @param modelDetails
	 * @param methodName
	 * @return
	 */
	private ImportiCapitolo popolaImporti(int uidCapitolo, int anno, TipoCapitolo tipoCapitolo,
			Set<ImportiCapitoloEnum> importiDerivatiRichiesti, ComponenteImportiCapitoloModelDetail[] modelDetails) {
		final String methodName = "popolaImporti";
		if(tipoCapitolo == null) {
			log.error(methodName, "Non e' stato fornito un tipo capitolo per anno " + anno + ". Inizializzazione con valore fasullo...");
			ImportiCapitoloUP importiCapitoloWorkAround = new ImportiCapitoloUP();
			importiCapitoloWorkAround.setAnnoCompetenza(Integer.valueOf(anno));
			return importiCapitoloWorkAround;
		}
		ImportiCapitolo importiCapitolo = importiCapitoloDad.findImportiCapitolo(uidCapitolo, anno, tipoCapitolo.getImportiCapitoloClass(), importiDerivatiRichiesti);
		if(importiCapitolo != null) {
			List<ComponenteImportiCapitolo> listaComponenteImportiCapitolo = componenteImportiCapitoloDad.findComponenteImportiCapitoloByUidCapitoloAnno(uidCapitolo, anno, modelDetails);
			importiCapitolo.setListaComponenteImportiCapitolo(listaComponenteImportiCapitolo);
		} else {
			log.debug(methodName, "Importi capitolo non presenti per anno " + anno + ". Inizializzazione con valore fasullo...");
			importiCapitolo = tipoCapitolo.newImportiCapitoloInstance();
			importiCapitolo.setAnnoCompetenza(Integer.valueOf(anno));
		}
		return importiCapitolo;
	}
	/**
	 * Caricamentod degli ImportiDerivati richiesti per il capitolo
	 * @return gli importi derivati
	 */
	private Set<ImportiCapitoloEnum> getImportiDerivatiRichiesti() {
		return EnumSet.allOf(ImportiCapitoloEnum.class);
	}
	/**
	 * Ottiene i modelDetails per il caricamento della componente importi capitolo.
	 * <br/>
	 * <strong>Default</strong>: Importo e TipoComponenteImportiCapitolo
	 * @return i model details
	 */
	private ComponenteImportiCapitoloModelDetail[] getModelDetails() {
		ComponenteImportiCapitoloModelDetail[] modelDetails = Utility.MDTL.byModelDetailClass(ComponenteImportiCapitoloModelDetail.class);
		// Richiedo importo e tipo, nel caso non siano meglio specificati
		if(modelDetails.length == 0) {
			modelDetails = new ComponenteImportiCapitoloModelDetail[] {
				ComponenteImportiCapitoloModelDetail.Importo,
				ComponenteImportiCapitoloModelDetail.TipoComponenteImportiCapitolo,
				ComponenteImportiCapitoloModelDetail.HasVariazioni
			};
		}
		return modelDetails;
	}

	/**
	 * Ottiene l'importo per la componente.
	 * <br/>
	 * L'importo &eacute; calcolato come la somma dei dettagli di tipo STANZIAMENTO
	 * @param componenteImportiCapitolo la componente da analizzare
	 * @return l'importo della componente
	 */
	protected BigDecimal getImporto(ComponenteImportiCapitolo componenteImportiCapitolo) {
		return componenteImportiCapitolo.computeImportoByTipoDettaglio(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
	}

	/**
	 * Aggiornamento dell'importo capitolo sul dettaglio
	 * @param componenteImportiCapitolo la componente il cui importo deve essere impostato
	 */
	protected void updateImportoCapitolo(Integer annoCompetenza, BigDecimal importo, TipoImportoCapitolo.Values tipo) {
		final String methodName = "updateImportoCapitolo";
		log.debug(methodName, "Richiesta modifica dell'importo per anno " + annoCompetenza + ", tipo " + tipo + " e valore " + Utility.formatCurrencyAsString(importo));
		// SIAC-7097
		if(!annoCompetenza.equals(capitolo.getAnnoCapitolo()) && !TipoImportoCapitolo.Values.STANZIAMENTO.equals(tipo)) {
			// L'unico importo che posso modificare su un anno differente dall'anno di competenza del capitolo e' il suo stanziamento
			log.debug(methodName, "E' possibile modificare solo uno STANZIAMENTO su anno differente dall'anno di competenza del capitolo");
			return;
		}
		
		DettaglioImportoCapitolo dettaglioImportoCapitolo = importiCapitoloDad.findDettaglioImportoCapitoloByCapitoloTipoAnno(
				capitolo.getUid(),
				tipo.getCodice(),
				annoCompetenza);

		//SIAC-7228
		if(TipoImportoCapitolo.Values.CASSA.equals(tipo)) {
			checkBusinessCondition(dettaglioImportoCapitolo.getImporto().add(importo).compareTo(BigDecimal.ZERO) >= 0, ErroreBil.MODIFICA_STANZIAMENTO_NON_CONSENTITA.getErrore());
		}
		
		if(res.getErrori().isEmpty() && !res.getEsito().equals(Esito.FALLIMENTO)) {
			dettaglioImportoCapitolo.setImporto(dettaglioImportoCapitolo.getImporto().add(importo));
			importiCapitoloDad.aggiornaDettaglioImportoCapitolo(dettaglioImportoCapitolo);
		}
		//
		
	}
	
	/**
	 * Imposta il valore dello stanziamento della componente
	 * @param componenteImportiCapitolo la componente importi capitolo
	 * @param value il valore da impostare
	 * @param tipoDettaglioComponenteImportiCapitolo il tipo di dettaglio da impostare
	 */
	protected void setImportoComponente(ComponenteImportiCapitolo componenteImportiCapitolo, BigDecimal value, TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponenteImportiCapitolo) {
		for(DettaglioComponenteImportiCapitolo dcic : componenteImportiCapitolo.getListaDettaglioComponenteImportiCapitolo()) {
			if(tipoDettaglioComponenteImportiCapitolo.equals(dcic.getTipoDettaglioComponenteImportiCapitolo())) {
				dcic.setImporto(value);
			}
		}
	}
	
	private ImportiCapitolo initResiduo(ImportiCapitolo annoCorrente) {
		ImportiCapitolo residuo = capitolo.getTipoCapitolo().newImportiCapitoloInstance();
		// V1: Clono le componenti e le inizializzo a zero
		if(annoCorrente != null) {
			for(ComponenteImportiCapitolo cic : annoCorrente.getListaComponenteImportiCapitolo()) {
				ComponenteImportiCapitolo clone = new ComponenteImportiCapitolo();
				clone.setTipoComponenteImportiCapitolo(cic.getTipoComponenteImportiCapitolo());
				for(DettaglioComponenteImportiCapitolo dcic : cic.getListaDettaglioComponenteImportiCapitolo()) {
					DettaglioComponenteImportiCapitolo cloneDettaglio = new DettaglioComponenteImportiCapitolo();
					cloneDettaglio.setEditabile(false);
					cloneDettaglio.setTipoDettaglioComponenteImportiCapitolo(dcic.getTipoDettaglioComponenteImportiCapitolo());
					clone.getListaDettaglioComponenteImportiCapitolo().add(cloneDettaglio);
				}
				residuo.getListaComponenteImportiCapitolo().add(clone);
			}
		}
		return residuo;
	}
	private ImportiCapitolo initAnniSuccessivi(ImportiCapitolo annoCorrente) {
		ImportiCapitolo annoSuccessivo = capitolo.getTipoCapitolo().newImportiCapitoloInstance();
		// V1: Clono le componenti e le inizializzo a zero
		if(annoCorrente != null) {
			for(ComponenteImportiCapitolo cic : annoCorrente.getListaComponenteImportiCapitolo()) {
				ComponenteImportiCapitolo clone = new ComponenteImportiCapitolo();
				clone.setTipoComponenteImportiCapitolo(cic.getTipoComponenteImportiCapitolo());
				for(DettaglioComponenteImportiCapitolo dcic : cic.getListaDettaglioComponenteImportiCapitolo()) {
					DettaglioComponenteImportiCapitolo cloneDettaglio = new DettaglioComponenteImportiCapitolo();
					cloneDettaglio.setEditabile(false);
					cloneDettaglio.setTipoDettaglioComponenteImportiCapitolo(dcic.getTipoDettaglioComponenteImportiCapitolo());
					clone.getListaDettaglioComponenteImportiCapitolo().add(cloneDettaglio);
				}
				annoSuccessivo.getListaComponenteImportiCapitolo().add(clone);
			}
		}
		return annoSuccessivo;
	}
}
