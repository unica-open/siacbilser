/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.integration.dad.MutuoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.model.Impegno;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public abstract class RicercaAttributiMovimentoGestioneOttimizzatoService<REQ extends RicercaAttributiMovimentoGestioneOttimizzato, RES extends RicercaAttributiMovimentoGestioneOttimizzatoResponse> extends AbstractBaseService<REQ, RES> {

	@Autowired
	protected CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Autowired
	protected CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
	@Autowired
	protected ProvvedimentoService provvedimentoService;
	
	@Autowired
	MutuoDad mutuoDad;
	
	
	//METODI COMPLETAMENTO DATI IMPEGNO:
	
	
	protected Impegno completaDatiImpegnoESubDopoRicercaMovimentoPk(Impegno impegno,Richiedente richiedente,String annoEsercizio,Integer idEnte,DatiOpzionaliCapitoli datiOpzionaliCapitoli){
		//si invoca il metodo completaDatiRicercaImpegnoPk che si occupa di vestire i dati ottenuti:
		impegno = completaDatiRicercaImpegnoPk(richiedente, impegno, annoEsercizio,datiOpzionaliCapitoli,null,null);
		
		
		/* LUGLIO 2016 OTTIMIZZANDO I MUTUI QUESTO CODICE DIVENTA SUPERFLUO, GIA' CARICATO TUTTI DENTRO AL ricercaMovimentoPK
		 * in maniera ottimizzata, per ora commento 
		
		// completo con i MUTUI
		if(impegno.getListaVociMutuo()!=null && !impegno.getListaVociMutuo().isEmpty()){
			List<String> listaNumeriMutuo =  impegnoOttimizzatoDad.listaNumeriMutuo(impegno.getListaVociMutuo());
			impegno.setElencoMutui(impegnoOttimizzatoDad.getListaMutuiAssociati(listaNumeriMutuo,idEnte));
		}		
		
		//Jira 1887 se il mutuo non e' associato all'impegno 
		if(impegno.getElencoSubImpegni()!=null && !impegno.getElencoSubImpegni().isEmpty()){
			
			for (SubImpegno subIterato : impegno.getElencoSubImpegni()) {
				List<VoceMutuo> listaVociMutuo = subIterato.getListaVociMutuo();
				if(listaVociMutuo!=null && listaVociMutuo.size()>0){
					if(subIterato.getElencoMutui()==null || subIterato.getElencoMutui().size()==0){
						//Completo i dati dei mutui solo se non sono gia caricati dal chiamanete:
						List<String> listaNumeriMutuo =  impegnoOttimizzatoDad.listaNumeriMutuo(listaVociMutuo);
						subIterato.setElencoMutui(impegnoOttimizzatoDad.getListaMutuiAssociati(listaNumeriMutuo,idEnte));
					}
				}
			}
			
		}	*/
		
		return impegno;
	}
	
	
	//Gestione richieste paginazione sub:
	protected PaginazioneSubMovimentiDto analizzaRichiestaPaginazioneSub(){
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		if(!req.isCaricaSub()){
			//non richiesti i sub:
			paginazioneSubMovimentiDto.setNoSub(true);
			paginazioneSubMovimentiDto.setPaginazione(false);
		} else {
			paginazioneSubMovimentiDto.setNoSub(false);
			
			//eventuale sub specifico richiesto:
			if(req instanceof RicercaImpegnoPerChiaveOttimizzato){
				RicercaImpegnoPerChiaveOttimizzato reqImp = (RicercaImpegnoPerChiaveOttimizzato) req;
				paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(reqImp.getpRicercaImpegnoK().getNumeroSubDaCercare());
			} else if(req instanceof RicercaAccertamentoPerChiaveOttimizzato){
				RicercaAccertamentoPerChiaveOttimizzato reqAcc = (RicercaAccertamentoPerChiaveOttimizzato) req;
				paginazioneSubMovimentiDto.setNumeroSubMovimentoRichiesto(reqAcc.getpRicercaAccertamentoK().getNumeroSubDaCercare());
			}
			
			//paginazione:
			if(req.isSubPaginati()){
				//SI RICHIESTA PAGINAZIONE
				paginazioneSubMovimentiDto.setPaginazione(true);
				paginazioneSubMovimentiDto.setDimensionePagina(req.getNumRisultatiPerPagina());
				paginazioneSubMovimentiDto.setNumeroPagina(req.getNumPagina());
			} else {
				//NO RICHIESTA PAGINAZIONE
				paginazioneSubMovimentiDto.setPaginazione(false);
			}
			
			//eventuale esclusione degli annullati:
			paginazioneSubMovimentiDto.setEscludiSubAnnullati(req.isEscludiSubAnnullati());
			
			//eventuale stato specifico richiesto per i sub:
			paginazioneSubMovimentiDto.setFiltroSubSoloInQuestoStato(req.getFiltroSubSoloInQuestoStato());
			
			//eventuale paginazione solo su dati minimi:
			paginazioneSubMovimentiDto.setPaginazioneSuDatiMinimi(req.isPaginazioneSuDatiMinimi());
			
		}
		return paginazioneSubMovimentiDto;
	}
	
	protected void impostaDatiRespPaginazioneSub(EsitoRicercaMovimentoPkDto esitoRicerca){
		//set dei dati di paginazione sub:
		res.setNumeroPaginaSubRestituita(esitoRicerca.getNumeroPaginaSubMovimentiRestituita());
		res.setDimensionePaginaSub(esitoRicerca.getDimensionePaginaSubMovimenti());
		res.setNumeroTotaleSub(esitoRicerca.getNumeroTotaleSubMovimenti());
		res.setNumeroTotPagineSub(esitoRicerca.getNumeroTotalePagineSubMovimenti());
		res.setNumPagina(esitoRicerca.getNumeroPaginaSubMovimentiRestituita());
		res.setNumRisultati(esitoRicerca.getNumeroTotaleSubMovimenti());
		//
		res.setElencoSubAccertamentiTuttiConSoloGliIds(esitoRicerca.getElencoSubAccertamentiTuttiConSoloGliIds());
		res.setElencoSubImpegniTuttiConSoloGliIds(esitoRicerca.getElencoSubImpegniTuttiConSoloGliIds());
	}
	
}
