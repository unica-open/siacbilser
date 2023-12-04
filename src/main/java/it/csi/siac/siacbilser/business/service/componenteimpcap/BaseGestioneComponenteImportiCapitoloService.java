/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.componenteimpcap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.base.BaseComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ComponenteImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ComponenteImpegnatoPerAnno;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.ImportoDerivatoFunctionEnum;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.utils.DettaglioImportoCapitolo;
import it.csi.siac.siacbilser.model.utils.TipoImportoCapitolo;
import it.csi.siac.siacbilser.model.wrapper.ImportiImpegnatoPerComponente;
import it.csi.siac.siacbilser.model.wrapper.ImportiImpegnatoPerComponenteAnniSuccNoStanz;
import it.csi.siac.siacbilser.model.wrapper.ImportiImpegnatoPerComponenteTriennioNoStanz;
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
		//SIAC-7349 per disponibilita impegnare su singola componente
		
		
		Set<ImportiCapitoloEnum> importiDerivatiRichiesti = getImportiDerivatiRichiesti();
		ComponenteImportiCapitoloModelDetail[] modelDetails = getModelDetails();
		ImportiCapitolo annoCorrente = null;
		TipoCapitolo tipoCapitolo = capitolo.getTipoCapitolo();
		TipoCapitolo tipoCapitoloEx = capitoloDad.getTipoCapitoloExCapitolo(tipoCapitolo);
		
		ImportiCapitolo importiCapitoloEquivalente = getImportiCapitoloEquivalente(tipoCapitoloEx, importiDerivatiRichiesti, modelDetails);

		res.getListaImportiCapitolo().add(importiCapitoloEquivalente);
		
		// Data for 3 years (plus year - 1)
		for(int i = 0; i < 3; i++) {
			//SIAC-7349 - INIZIO - SR90 - MR - 03/2020 definizione function per calcolo della disponibilitaimpegnare per UG e dispvariare per UP			
			//TODO Adeguamento per i capitoli di previsione
				//String functionNameDispImpCap = ImportoDerivatoFunctionEnum.disponibilitaImpegnareAnnoComponente.getFunctionName();
			//SIAC-7349 - FINE
			int uidCapitolo = capitolo.getUid();
			int anno = capitolo.getAnnoCapitolo().intValue() + i;
			ImportiCapitolo importiCapitolo = popolaImporti(uidCapitolo, anno, tipoCapitolo, importiDerivatiRichiesti, modelDetails);
			//SIAC-7349 - INIZIO - SR90 - MR - 03/2020 chiamo function per calcolo della disponibilitaimpegnare per UG e dispvariare per UP
			res.getListaImportiCapitolo().add(importiCapitolo);
			// TODO: leggere i dati in maniera corretta
			if(i == 0) {
				annoCorrente = importiCapitolo;
			}
		}
		//SIAC-7349 -
		Map<Integer, List<ImportiImpegnatoPerComponente>> importiComponenteImpegnati = null;
		if(TipoCapitolo.isTipoCapitoloPrevisione(capitolo.getTipoCapitolo())){
			
			importiComponenteImpegnati = popolaImpegnatoComponente(res.getListaImportiCapitolo(), capitolo.getUid(), true);
		}else{
			importiComponenteImpegnati = popolaImpegnatoComponente(res.getListaImportiCapitolo(), capitolo.getUid(), false);
		}

		res.setImportiCapitoloResiduo(initResiduo(annoCorrente,importiComponenteImpegnati));
		res.setImportiCapitoloAnniSuccessivi(initAnniSuccessivi(annoCorrente));
		popolaImpegnatoAnnoSuccessivi(importiComponenteImpegnati);
		impostaValoreSuResiduo(importiComponenteImpegnati,tipoCapitolo);
		//SIAC-7349 - SR200 - START - MR - 07/05/2020: chiamo nuovo servizio per ottenere componente
		//con impegnato ma senza stanziamento anni > N+
		if(TipoCapitolo.isTipoCapitoloGestione(tipoCapitolo)){
			// SIAC-7349 GS 17/07/2020 Implemento anche il caso CUG

			List<Integer> componentiAssociati = getIdComponentiConStanziamento();			
			//if(!componentiAssociati.isEmpty()){
			String functionNameAnniSuccNoStanz = ImportoDerivatoFunctionEnum.impegnatoNoStanzAnniSuccUG.getFunctionName();
			List<ImportiImpegnatoPerComponenteAnniSuccNoStanz> anniSuccNoStanz = componentiNoStanzAnniSucc(capitolo.getUid(), componentiAssociati,  functionNameAnniSuccNoStanz);				
			if(!anniSuccNoStanz.isEmpty()){
				for(ImportiImpegnatoPerComponenteAnniSuccNoStanz item : anniSuccNoStanz){
					res.getListaImportiCapitoloAnniSuccessiviNoStanz().add(item);						
				}	
			}else{	
			}

			// SIAC-7349 GS 17/07/2020 Aggiungo anche le componenti che non hanno stanziamento ma hanno impegnato nel triennio
			List<Integer> componentiAssociatiTriennio = getIdComponentiConStanziamentoNelTriennio();			
			//if(!componentiAssociati.isEmpty()){
			String functionNameTriennioNoStanz = ImportoDerivatoFunctionEnum.impegnatoNoStanzTriennioUG.getFunctionName();
			List<ImportiImpegnatoPerComponenteTriennioNoStanz> triennioNoStanz = componentiNoStanzTriennio(capitolo.getUid(), componentiAssociatiTriennio,  functionNameTriennioNoStanz);				
			if(!triennioNoStanz.isEmpty()){
				for(ImportiImpegnatoPerComponenteTriennioNoStanz item : triennioNoStanz){
					res.getListaImportiCapitoloTriennioNoStanz().add(item);						
				}	
			}else{	
			}
			
			
			
		}else{
			List<Integer> componentiAssociati = getIdComponentiConStanziamento();			
			//if(!componentiAssociati.isEmpty()){
			String functionNameAnniSuccNoStanz = ImportoDerivatoFunctionEnum.impegnatoNoStanzAnniSucc.getFunctionName();
			List<ImportiImpegnatoPerComponenteAnniSuccNoStanz> anniSuccNoStanz = componentiNoStanzAnniSucc(capitolo.getUid(), componentiAssociati,  functionNameAnniSuccNoStanz);				
			if(!anniSuccNoStanz.isEmpty()){
				for(ImportiImpegnatoPerComponenteAnniSuccNoStanz item : anniSuccNoStanz){
					res.getListaImportiCapitoloAnniSuccessiviNoStanz().add(item);						
				}	
			}else{	
			}

			// SIAC-7349 GS 17/07/2020 Aggiungo anche le componenti che non hanno stanziamento ma hanno impegnato nel triennio
			List<Integer> componentiAssociatiTriennio = getIdComponentiConStanziamentoNelTriennio();			
			//if(!componentiAssociati.isEmpty()){
			String functionNameTriennioNoStanz = ImportoDerivatoFunctionEnum.impegnatoNoStanzTriennio.getFunctionName();
			List<ImportiImpegnatoPerComponenteTriennioNoStanz> triennioNoStanz = componentiNoStanzTriennio(capitolo.getUid(), componentiAssociatiTriennio,  functionNameTriennioNoStanz);				
			if(!triennioNoStanz.isEmpty()){
				for(ImportiImpegnatoPerComponenteTriennioNoStanz item : triennioNoStanz){
					res.getListaImportiCapitoloTriennioNoStanz().add(item);						
				}	
			}else{	
			}
			

		}
		
		//SIAC-7349 - FINE
		
	}
	//SIAC-7349 - SR200 - MR - 07/05/2020 Metodo che estrae id delle componenti con stanziamento gia al capitolo
	protected List<Integer> getIdComponentiConStanziamento() {
		List<Integer> componentiAssociati = new ArrayList<Integer>();
		ImportiCapitolo importiCapitoloAnnoPrec = res.getListaImportiCapitolo().get(0);
		ImportiCapitolo importiCapitoloTriennio = res.getListaImportiCapitolo().get(1);
		for(ComponenteImportiCapitolo cic : importiCapitoloAnnoPrec.getListaComponenteImportiCapitolo()){
			componentiAssociati.add(cic.getTipoComponenteImportiCapitolo().getUid());
		}
		for(ComponenteImportiCapitolo cic : importiCapitoloTriennio.getListaComponenteImportiCapitolo()){
			componentiAssociati.add(cic.getTipoComponenteImportiCapitolo().getUid());
		}
		return componentiAssociati;
	}
	
	//SIAC-7349 - GS 17/07/2020- Metodo che estrae id delle componenti con stanziamento gia al capitolo ma solo nel triennio
	protected List<Integer> getIdComponentiConStanziamentoNelTriennio() {
		List<Integer> componentiAssociati = new ArrayList<Integer>();
		ImportiCapitolo importiCapitoloTriennio = res.getListaImportiCapitolo().get(1);
		for(ComponenteImportiCapitolo cic : importiCapitoloTriennio.getListaComponenteImportiCapitolo()){
			componentiAssociati.add(cic.getTipoComponenteImportiCapitolo().getUid());
		}
		return componentiAssociati;
	}
	
	
	private List<ImportiImpegnatoPerComponenteAnniSuccNoStanz> componentiNoStanzAnniSucc(int uid,
			List<Integer> componentiAssociati, String functionNameAnniSuccNoStanz) {
		List<ImportiImpegnatoPerComponenteAnniSuccNoStanz> impegnatiAnniSuccNoStanz = importiCapitoloDad.findImpegnatoAnniSuccNoStanz(uid, componentiAssociati, functionNameAnniSuccNoStanz);		
		return impegnatiAnniSuccNoStanz;
	}
	
	// SIAC-7349 GS 20/07/2020
	private List<ImportiImpegnatoPerComponenteTriennioNoStanz> componentiNoStanzTriennio(int uid,
			List<Integer> componentiAssociati, String functionNameTriennioNoStanz) {
		List<ImportiImpegnatoPerComponenteTriennioNoStanz> impegnatiTriennioNoStanz = importiCapitoloDad.findImpegnatoTriennioNoStanz(uid, componentiAssociati, functionNameTriennioNoStanz);		
		return impegnatiTriennioNoStanz;
	}
	
	
	//SIAC-7349 - INIZIO - SR90 - MR - 03/04/2020 chiamo Servizio con calcolo della disponibilita a impegnare per i capitoli gestione
	protected void loadComponentiImportiCapitoloAbilitaDisponibilita() {
		final String methodName = "loadComponentiImportiCapitolo";
		//SIAC-7349 per disponibilita impegnare su singola componente
		
		
		Set<ImportiCapitoloEnum> importiDerivatiRichiesti = getImportiDerivatiRichiesti();
		ComponenteImportiCapitoloModelDetail[] modelDetails = getModelDetails();
		ImportiCapitolo annoCorrente = null;
		TipoCapitolo tipoCapitolo = capitolo.getTipoCapitolo();
		TipoCapitolo tipoCapitoloEx = capitoloDad.getTipoCapitoloExCapitolo(tipoCapitolo);
		
		ImportiCapitolo importiCapitoloEquivalente = getImportiCapitoloEquivalente(tipoCapitoloEx, importiDerivatiRichiesti, modelDetails);

		res.getListaImportiCapitolo().add(importiCapitoloEquivalente);
		
		// Data for 3 years (plus year - 1)
		for(int i = 0; i < 3; i++) {
			//SIAC-7349 - INIZIO - SR90 - MR - 03/2020 definizione function per calcolo della disponibilitaimpegnare per UG e dispvariare per UP			
			//TODO Adeguamento per i capitoli di previsione
			//String functionNameDispImpCap = ImportoDerivatoFunctionEnum.disponibilitaImpegnareAnnoComponente.getFunctionName();
			//SIAC-7349 - FINE
			int uidCapitolo = capitolo.getUid();
			int anno = capitolo.getAnnoCapitolo().intValue() + i;
			ImportiCapitolo importiCapitolo = popolaImporti(uidCapitolo, anno, tipoCapitolo, importiDerivatiRichiesti, modelDetails);
			//
			//SIAC-7349 - INIZIO - SR90 - MR - 03/2020 chiamo function per calcolo della disponibilitaimpegnare per UG e dispvariare per UP
			if(TipoCapitolo.isTipoCapitoloGestione(tipoCapitolo)){
				String functionNameDispImpCap = ImportoDerivatoFunctionEnum.disponibilitaImpegnareAnnoComponente.getFunctionName();
				String functionNameDispVarCap = ImportoDerivatoFunctionEnum.disponibilitaVariareAnnoComponente.getFunctionName();
				int j=i+1;
				functionNameDispImpCap = functionNameDispImpCap+String.valueOf(j);
				functionNameDispVarCap = functionNameDispVarCap+String.valueOf(j);
				importiCapitolo = popolaDisponibilitaImpegnare(importiCapitolo, uidCapitolo, functionNameDispImpCap);
				importiCapitolo = popolaDisponibilitaVariare(importiCapitolo, uidCapitolo, functionNameDispVarCap);
			}else{
				//SIAC-7349 - INIZIO - SR220 - MR - 29/04/2020 chiamo function per calcolo della disponibilitaimpegnare per UG e dispvariare per UP
				//String functionNameDispImpCap = ImportoDerivatoFunctionEnum.disponibilitaImpegnareAnnoComponente.getFunctionName();
				String functionNameDispVarCap = ImportoDerivatoFunctionEnum.disponibilitaVariareAnnoComponenteUP.getFunctionName();
				int j=i+1;
				//functionNameDispImpCap = functionNameDispImpCap+String.valueOf(j);
				functionNameDispVarCap = functionNameDispVarCap+String.valueOf(j);
				//importiCapitolo = popolaDisponibilitaImpegnare(importiCapitolo, uidCapitolo, functionNameDispImpCap);
				importiCapitolo = popolaDisponibilitaVariare(importiCapitolo, uidCapitolo, functionNameDispVarCap);
				
			}
			

			res.getListaImportiCapitolo().add(importiCapitolo);
			// TODO: leggere i dati in maniera corretta
			if(i == 0) {
				annoCorrente = importiCapitolo;
			}
		}
		//SIAC-7349 -
		//If sul tipo capitolo per WIP
		Map<Integer, List<ImportiImpegnatoPerComponente>> importiComponenteImpegnati = null;
		if(TipoCapitolo.isTipoCapitoloPrevisione(capitolo.getTipoCapitolo())){
			importiComponenteImpegnati = popolaImpegnatoComponente(res.getListaImportiCapitolo(), capitolo.getUid(), true);			
		}else{
			importiComponenteImpegnati = popolaImpegnatoComponente(res.getListaImportiCapitolo(), capitolo.getUid(), false);
		}
		// TODO [ComponenteImportiCapitolo]: data per residuo e anni successivi
		res.setImportiCapitoloResiduo(initResiduo(annoCorrente,importiComponenteImpegnati));
		res.setImportiCapitoloAnniSuccessivi(initAnniSuccessivi(annoCorrente));
		popolaImpegnatoAnnoSuccessivi(importiComponenteImpegnati);
		//SIAC-7349 - SR200 - START - MR - 07/05/2020: chiamo nuovo servizio per ottenere componente
		//con impegnato ma senza stanziamento anni > N+
		if(TipoCapitolo.isTipoCapitoloGestione(tipoCapitolo)){
			List<Integer> componentiAssociati = getIdComponentiConStanziamento();			
			//if(!componentiAssociati.isEmpty()){
			String functionNameAnniSuccNoStanz = ImportoDerivatoFunctionEnum.impegnatoNoStanzAnniSuccUG.getFunctionName();
			List<ImportiImpegnatoPerComponenteAnniSuccNoStanz> anniSuccNoStanz = componentiNoStanzAnniSucc(capitolo.getUid(), componentiAssociati,  functionNameAnniSuccNoStanz);				
			if(!anniSuccNoStanz.isEmpty()){
				for(ImportiImpegnatoPerComponenteAnniSuccNoStanz item : anniSuccNoStanz){
					res.getListaImportiCapitoloAnniSuccessiviNoStanz().add(item);						
				}
			}else{
							
			}
			// SIAC-7349 GS 20/07/2020
			List<Integer> componentiAssociatiNelTriennio = getIdComponentiConStanziamentoNelTriennio();			
			String functionNameTriennioNoStanz = ImportoDerivatoFunctionEnum.impegnatoNoStanzTriennioUG.getFunctionName();
			List<ImportiImpegnatoPerComponenteTriennioNoStanz> triennioNoStanz = componentiNoStanzTriennio(capitolo.getUid(), componentiAssociatiNelTriennio,  functionNameTriennioNoStanz);				
			if(!triennioNoStanz.isEmpty()){
				for(ImportiImpegnatoPerComponenteTriennioNoStanz item : triennioNoStanz){
					res.getListaImportiCapitoloTriennioNoStanz().add(item);						
				}
			}else{
							
			}
			
			
		}else{
			List<Integer> componentiAssociati = getIdComponentiConStanziamento();			
			//if(!componentiAssociati.isEmpty()){
			String functionNameAnniSuccNoStanz = ImportoDerivatoFunctionEnum.impegnatoNoStanzAnniSucc.getFunctionName();
			List<ImportiImpegnatoPerComponenteAnniSuccNoStanz> anniSuccNoStanz = componentiNoStanzAnniSucc(capitolo.getUid(), componentiAssociati,  functionNameAnniSuccNoStanz);				
			if(!anniSuccNoStanz.isEmpty()){
				for(ImportiImpegnatoPerComponenteAnniSuccNoStanz item : anniSuccNoStanz){
					res.getListaImportiCapitoloAnniSuccessiviNoStanz().add(item);						
				}
			}else{
							
			}
			// SIAC-7349 GS 20/07/2020
			List<Integer> componentiAssociatiNelTriennio = getIdComponentiConStanziamentoNelTriennio();			
			String functionNameTriennioNoStanz = ImportoDerivatoFunctionEnum.impegnatoNoStanzTriennio.getFunctionName();
			List<ImportiImpegnatoPerComponenteTriennioNoStanz> triennioNoStanz = componentiNoStanzTriennio(capitolo.getUid(), componentiAssociatiNelTriennio,  functionNameTriennioNoStanz);				
			if(!triennioNoStanz.isEmpty()){
				for(ImportiImpegnatoPerComponenteTriennioNoStanz item : triennioNoStanz){
					res.getListaImportiCapitoloTriennioNoStanz().add(item);						
				}
			}else{
							
			}

			
			
			//}
		}				
		//SIAC-7349 - FINE
		
	}
	
	//SIAC-7349 - FINE
	
	//SIAC-7349 -Start - SR200 - MR - 04/2020 - Metodo per popolare gli importi relativi agli anni successivi
	private void popolaImpegnatoAnnoSuccessivi(
			Map<Integer, List<ImportiImpegnatoPerComponente>> importiComponenteImpegnati) {
		
		for(int i=0; i<res.getImportiCapitoloAnniSuccessivi().getListaComponenteImportiCapitolo().size(); i++){
			//ciclo i 4 importi
			List<ComponenteImportiCapitolo> listaComponenti = res.getImportiCapitoloAnniSuccessivi().getListaComponenteImportiCapitolo();
			for(ComponenteImportiCapitolo cip: listaComponenti){
				int idComponente = cip.getTipoComponenteImportiCapitolo().getUid();
				List<ImportiImpegnatoPerComponente> importoImpegnato = importiComponenteImpegnati.get(idComponente);
				if(importoImpegnato != null && !importoImpegnato.isEmpty()){
					cip.getListaDettaglioComponenteImportiCapitolo().get(1).setImporto(importoImpegnato.get(4).getImpegnatoDefinitivo());					
				}
			}
	
		}
		
	}
	private void impostaValoreSuResiduo(Map<Integer, List<ImportiImpegnatoPerComponente>> importiComponenteImpegnati, TipoCapitolo tipoCapitolo) {
		
		//per sicurezza e retrocompatibilita', esco in caso di capitolo non di previsione. Ma dovrebbe essere corretto
		//farlo anche per capitolo di previsione
		if(!TipoCapitolo.isTipoCapitoloPrevisione(tipoCapitolo)) {
			return;
		}
		
		List<ComponenteImportiCapitolo> listaComponenti = res.getImportiCapitoloResiduo().getListaComponenteImportiCapitolo();
		for(ComponenteImportiCapitolo cip: listaComponenti){
			int idComponente = cip.getTipoComponenteImportiCapitolo().getUid();
			List<ImportiImpegnatoPerComponente> importoImpegnato = importiComponenteImpegnati.get(idComponente);
			if(importoImpegnato != null && !importoImpegnato.isEmpty()){
				cip.setImpegnatoAnniPrecedenti(importoImpegnato.get(0).getImpegnatoDefinitivo());					
			}
		}
	}
	//SIAC-7349 - End
	//SIAC-7349 - INIZIO - SR90 - MR - 03/2020 function per calcolo della disponibilitaimpegnare per UG 
	private ImportiCapitolo popolaDisponibilitaImpegnare(ImportiCapitolo importiCapitolo, int uidCapitolo,
			String funzioneDispComp) {
			BigDecimal disponibilitaImpegnareComponenti = BigDecimal.ZERO;
			
			List<ComponenteImportiCapitolo> listaCip = importiCapitolo.getListaComponenteImportiCapitolo();
			if(listaCip != null && !listaCip.isEmpty()){
				for(int j=0; j<importiCapitolo.getListaComponenteImportiCapitolo().size(); j++){
					DettaglioComponenteImportiCapitolo ddcDispImp = new DettaglioComponenteImportiCapitolo();
					disponibilitaImpegnareComponenti= importiCapitoloDad.findDisponibilitaImpegnareComponente(uidCapitolo, importiCapitolo.getListaComponenteImportiCapitolo().get(j).getTipoComponenteImportiCapitolo().getUid(), funzioneDispComp);
					ddcDispImp.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE);
					ddcDispImp.setImporto(disponibilitaImpegnareComponenti);
					ddcDispImp.setPropostaDefault(false);
					importiCapitolo.getListaComponenteImportiCapitolo().get(j).getListaDettaglioComponenteImportiCapitolo().add(ddcDispImp);
				}
				
			}
			 
		
		return importiCapitolo;
	}
	//SIAC-7349 - FINE
	
	//SIAC-7349 - INIZIO - SR90 - MR - 03/2020 function per calcolo della disponibilitaimpegnare per UG 
	private ImportiCapitolo popolaDisponibilitaVariare(ImportiCapitolo importiCapitolo, int uidCapitolo,
			String funzioneDispComp) {
			BigDecimal disponibilitaVariareComponenti = BigDecimal.ZERO;
				
			List<ComponenteImportiCapitolo> listaCip = importiCapitolo.getListaComponenteImportiCapitolo();
			if(listaCip != null && !listaCip.isEmpty()){
				for(int j=0; j<importiCapitolo.getListaComponenteImportiCapitolo().size(); j++){
					DettaglioComponenteImportiCapitolo ddcDispVar = new DettaglioComponenteImportiCapitolo();
					disponibilitaVariareComponenti= importiCapitoloDad.findDisponibilitaVariareComponente(uidCapitolo, importiCapitolo.getListaComponenteImportiCapitolo().get(j).getTipoComponenteImportiCapitolo().getUid(), funzioneDispComp);
					ddcDispVar.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAVARIARE);
					ddcDispVar.setImporto(disponibilitaVariareComponenti);
					ddcDispVar.setPropostaDefault(false);
					importiCapitolo.getListaComponenteImportiCapitolo().get(j).getListaDettaglioComponenteImportiCapitolo().add(ddcDispVar);
				}
					
			}
				 
			
		return importiCapitolo;
	}
		//SIAC-7349 - FINE
	
	//SIAC-7349 - INIZIO - SR200 - MR - 09/04/2020  calcolo della impegnatopercomponente per UP 
	@SuppressWarnings("unchecked")
	private Map<Integer, List<ImportiImpegnatoPerComponente>> popolaImpegnatoComponente(List<ImportiCapitolo> listaImportiCapitolo, int idCapitolo, boolean isPrevisione) {
		
		List<Integer> uidComponentiCapitolo = new ArrayList<Integer>();
		Map<Integer, List<ImportiImpegnatoPerComponente>> annualitaImpegnatoPerComponente = new HashMap<Integer, List<ImportiImpegnatoPerComponente>>();
		String funzioneImpComp = "";
		if(isPrevisione){
			funzioneImpComp = ImportoDerivatoFunctionEnum.impegnatoDefinitivoUPComponente.getFunctionName();			
		}else{
			funzioneImpComp = ImportoDerivatoFunctionEnum.impegnatoDefinitivoUGComponente.getFunctionName();
		}
		ImportiCapitolo importoCapitoloPerComponenti = res.getListaImportiCapitolo().get(1); //prendo la componente del primo item che si riferisce all'anno corrente
		ImportiCapitolo importoCapitoloPerComponentiPrec = res.getListaImportiCapitolo().get(0);
		//importoCapitoloPerComponenti.add(res.getListaImportiCapitolo().get(0));
		
		//prendo tutti gli id delle componenti associati al capitolo in questione
		
		for(ComponenteImportiCapitolo cip : importoCapitoloPerComponenti.getListaComponenteImportiCapitolo()){
			uidComponentiCapitolo.add(cip.getTipoComponenteImportiCapitolo().getUid());
		}
		
		for(ComponenteImportiCapitolo cip : importoCapitoloPerComponentiPrec.getListaComponenteImportiCapitolo()){
			uidComponentiCapitolo.add(cip.getTipoComponenteImportiCapitolo().getUid());
		}
		
		
		List<ImportiImpegnatoPerComponente> impegnatoPerComponente = new ArrayList<ImportiImpegnatoPerComponente>();
		//per ognuno di questi, invoco la function che mi permette di ottenere, per quella componente, i 5 valori di interesse
		
		for(Integer uidSingolaComponente : uidComponentiCapitolo){
			impegnatoPerComponente= importiCapitoloDad.findImpegnatoComponente(idCapitolo, uidSingolaComponente, funzioneImpComp);
			if (annualitaImpegnatoPerComponente.get(uidSingolaComponente) == null) {
				annualitaImpegnatoPerComponente.put(uidSingolaComponente, new ArrayList<ImportiImpegnatoPerComponente>());
				}
			annualitaImpegnatoPerComponente.get(uidSingolaComponente).addAll(impegnatoPerComponente);
		}
		
		for(int i=0; i<res.getListaImportiCapitolo().size(); i++){
			//ciclo i 4 importi
			List<ComponenteImportiCapitolo> listaComponenti = res.getListaImportiCapitolo().get(i).getListaComponenteImportiCapitolo();
			for(ComponenteImportiCapitolo cip: listaComponenti){
				int idComponente = cip.getTipoComponenteImportiCapitolo().getUid();
				List<ImportiImpegnatoPerComponente> importoImpegnato = annualitaImpegnatoPerComponente.get(idComponente);
				if(importoImpegnato != null && !importoImpegnato.isEmpty()){
					if(i<2){
						cip.setImpegnatoResiduoIniziale(importoImpegnato.get(5).getImpegnatoDefinitivo());
						cip.setImpegnatoResiduoFinale(importoImpegnato.get(6).getImpegnatoDefinitivo());
						if(i==1){
							cip.setImpegnatoAnniPrecedenti(importoImpegnato.get(0).getImpegnatoDefinitivo());
						}
					}
					cip.getListaDettaglioComponenteImportiCapitolo().get(1).setImporto(importoImpegnato.get(i).getImpegnatoDefinitivo());					
				}
			}
	
		}
		return annualitaImpegnatoPerComponente;
	}

	//SIAC-7349 - FINE
	
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
			//SIAC-7349 - INIZIO - SR90 - MR - 03/2020 calcolo importo impegnato per i capitolo di gestione -> TODO adeguamento per capitoli di previsione
			if(TipoCapitolo.isTipoCapitoloGestione(tipoCapitolo)){
				List<ComponenteImpegnatoPerAnno> impegnatiPerAnno = capitoloDad.findImpegniAssociati(uidCapitolo, anno);
				if(!impegnatiPerAnno.isEmpty()){
					listaComponenteImportiCapitolo = setImportoImpegnatoDerivato(listaComponenteImportiCapitolo,impegnatiPerAnno);
				}				
			}
			//SIAC-7349 - FINE
			
			importiCapitolo.setListaComponenteImportiCapitolo(listaComponenteImportiCapitolo);
		} else {
			log.debug(methodName, "Importi capitolo non presenti per anno " + anno + ". Inizializzazione con valore fasullo...");
			importiCapitolo = tipoCapitolo.newImportiCapitoloInstance();
			importiCapitolo.setAnnoCompetenza(Integer.valueOf(anno));
		}
		return importiCapitolo;
	}
	
	//SIAC-7349 - INIZIO - SR90 - MR - 03/2020 setting importo impegnato per i capitolo di gestione alle componenti -> TODO adeguamento per capitoli di previsione
	private List<ComponenteImportiCapitolo> setImportoImpegnatoDerivato(
			List<ComponenteImportiCapitolo> listaComponenteImportiCapitolo,
			List<ComponenteImpegnatoPerAnno> impegnatiPerAnno) {
		
		for(int i=0; i<impegnatiPerAnno.size(); i++){
			
			for(int j=0; j<listaComponenteImportiCapitolo.size(); j++){
				ComponenteImportiCapitolo cic = listaComponenteImportiCapitolo.get(j);
				ComponenteImpegnatoPerAnno cia = impegnatiPerAnno.get(i);
				if(cic.getUid()==cia.getIdComponente()){
					for(DettaglioComponenteImportiCapitolo dcic : cic.getListaDettaglioComponenteImportiCapitolo()){
						if(dcic.getTipoDettaglioComponenteImportiCapitolo().getCodice().equals(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO.getCodice())){
							dcic.setImporto(cia.getImportoImpegnato());
						}
						
					}
				}
		
			}
	
		}
		return listaComponenteImportiCapitolo;
	}
	//SIAC-7349 - FINE
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

		//SIAC-7916 si esclude il controllo della 7228 sulla CASSA in quanto la modifica sull'importo agira' solo sullo STANZIAMENTO
//		//SIAC-7228
//		if(TipoImportoCapitolo.Values.CASSA.equals(tipo)) {
//			checkBusinessCondition(dettaglioImportoCapitolo.getImporto().add(importo).compareTo(BigDecimal.ZERO) >= 0, ErroreBil.MODIFICA_STANZIAMENTO_NON_CONSENTITA.getErrore());
//		}
		
//		if(res.getErrori().isEmpty() && !res.getEsito().equals(Esito.FALLIMENTO)) {
			dettaglioImportoCapitolo.setImporto(dettaglioImportoCapitolo.getImporto().add(importo));
			importiCapitoloDad.aggiornaDettaglioImportoCapitolo(dettaglioImportoCapitolo);
//		}
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
	
	private ImportiCapitolo initResiduo(ImportiCapitolo annoCorrente, Map<Integer, List<ImportiImpegnatoPerComponente>> importiComponenteImpegnati) {
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
				int idComponente = cic.getTipoComponenteImportiCapitolo().getUid();
				//V2, metto l'impegnato
				List<ImportiImpegnatoPerComponente> importoImpegnato = importiComponenteImpegnati.get(idComponente);
				if(importoImpegnato != null && !importoImpegnato.isEmpty()){
					clone.setImpegnatoResiduoFinale(importoImpegnato.get(6).getImpegnatoDefinitivo());					
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
