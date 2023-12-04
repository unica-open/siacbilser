/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.dao.common.SiacDAttoAmmTipoFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacTEnteProprietarioFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.dto.ConsultaDettaglioAccertamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaAccSubAccParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacDAttoAmmTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDModificaTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAttoAmmClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoTsMovgestTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtil;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.RiepilogoImportoNumero;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaAccSubAcc;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;



@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AccertamentoOttimizzatoDad extends MovimentoGestioneOttimizzatoDad<Accertamento, SubAccertamento>{

	
		private static final String MOVGEST_TIPO_CODE_ACCERTAMENTO="A";
	
		@Autowired
		private SiacTEnteProprietarioFinRepository siacTEnteProprietarioRepository;
		
		@Autowired
		private SiacDAttoAmmTipoFinRepository siacDAttoAmmTipoRepository;
	
		@Override
		protected String getMovgestTipoCode(){
			return MOVGEST_TIPO_CODE_ACCERTAMENTO;
		}

		/**
		 * wrapper di retro compatibilita'
		 */
		@Override
		protected Accertamento convertiMovimentoGestione(SiacTMovgestFin siacTMovgest){
			return convertiMovimentoGestione(siacTMovgest);
		}
		
		
		@Override
		protected Accertamento convertiMovimentoGestione(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto, boolean ottimizzatoCompletamenteDaChiamante){
			Accertamento accertamentoTrovato = map(siacTMovgest, Accertamento.class, FinMapId.SiacTMovgest_Accertamento);	
			accertamentoTrovato = EntityToModelConverter.siacTMovgestEntityToAccertamentoModel(siacTMovgest, accertamentoTrovato,ottimizzazioneDto,ottimizzatoCompletamenteDaChiamante);	
			//Termino restituendo l'oggetto di ritorno: 
			return accertamentoTrovato;
		}
	
		@Override
		protected Accertamento convertiMovimentoGestione(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
			Accertamento accertamentoTrovato = map(siacTMovgest, Accertamento.class, FinMapId.SiacTMovgest_Accertamento);	
			accertamentoTrovato = EntityToModelConverter.siacTMovgestEntityToAccertamentoModel(siacTMovgest, accertamentoTrovato,ottimizzazioneDto);	
			//Termino restituendo l'oggetto di ritorno: 
			return accertamentoTrovato;
		}
		
		@Override
		protected Accertamento convertiMovimentoGestioneOPT(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
			Accertamento accertamentoTrovato = map(siacTMovgest, Accertamento.class, FinMapId.SiacTMovgest_Accertamento);	
			accertamentoTrovato = EntityToModelConverter.siacTMovgestEntityToAccertamentoModelOPT(siacTMovgest, accertamentoTrovato,ottimizzazioneDto);	
			//Termino restituendo l'oggetto di ritorno: 
			return accertamentoTrovato;
		}
	
		/**
		 * wrapper di retro compatibilita'
		 */
		@Override
		protected SubAccertamento convertiSubMovimento(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest, boolean caricaDatiUlteriori){
			return convertiSubMovimento(siacTMovgestTs, siacTMovgest, caricaDatiUlteriori,null);
		}
	
		@Override
		protected SubAccertamento convertiSubMovimento(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest, boolean caricaDatiUlteriori,OttimizzazioneMovGestDto ottimizzazioneDto){
			SubAccertamento subAccertamentoTrovato = map(siacTMovgestTs, SubAccertamento.class, FinMapId.SiacTMovgestTs_SubAccertamento);
			subAccertamentoTrovato = EntityToModelConverter.siacTMovgestTsEntityToSubAccertamentoModel(siacTMovgestTs, subAccertamentoTrovato, siacTMovgest,ottimizzazioneDto);
			
			//Termino restituendo l'oggetto di ritorno: 
			return subAccertamentoTrovato;
		}
		
		@Override
		protected SubAccertamento convertiSubMovimentoOPT(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
			SubAccertamento subAccertamentoTrovato = map(siacTMovgestTs, SubAccertamento.class, FinMapId.SiacTMovgestTs_SubAccertamento);
			subAccertamentoTrovato = EntityToModelConverter.siacTMovgestTsEntityToSubAccertamentoModelOPT(toList(siacTMovgestTs), 
					toList(subAccertamentoTrovato), siacTMovgest,ottimizzazioneDto).get(0);
			//Termino restituendo l'oggetto di ritorno: 
			return subAccertamentoTrovato;
		}


		@Override
		protected boolean checkStato(String stato) {
			return CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(stato) || CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(stato);
		}
		
		
		/**
		 * Ricerco le eventuali reversali collegate al movimento
		 * @param uidMovimetno
		 * @return
		 */
		public Boolean ricercaReversaliByAccertamento(Integer idMovimento, Integer idEnte){
			
			Boolean movimentoConReversali = Boolean.FALSE;
			// Devo cercare prima il movimento, perchè non è detto sia di tipo Testata
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			List<SiacTMovgestTsFin> siacTMovgestTs = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, now, idMovimento);
			
			if(siacTMovgestTs==null || siacTMovgestTs.isEmpty()){
				//l'uid potrebbe essere di un sub, ricerco
				siacTMovgestTs = siacTMovgestTsRepository.findSubMovgestTsByMovgest(idEnte, now, idMovimento);
			}
			
			// Qui screma già sulle R valide 
			List<SiacROrdinativoTsMovgestTFin> siacRordinativiTsMovgest = movimentoGestioneDao.ricercaByMovGestTsMassive(siacTMovgestTs,"SiacROrdinativoTsMovgestTFin");
			
			if(siacRordinativiTsMovgest!=null && !siacRordinativiTsMovgest.isEmpty()){
				
				for (SiacROrdinativoTsMovgestTFin siacROrdinativoTsMovgestTFin : siacRordinativiTsMovgest) {
					
					SiacTOrdinativoFin tOrdinativo = siacROrdinativoTsMovgestTFin.getSiacTOrdinativoT().getSiacTOrdinativo();
					
					List<SiacROrdinativoStatoFin> siacROrdinativoStatos= tOrdinativo.getSiacROrdinativoStatos();
					
					for (SiacROrdinativoStatoFin siacROrdinativoStatoFin : siacROrdinativoStatos) {
						
						if(siacROrdinativoStatoFin.getDataFineValidita() == null && !siacROrdinativoStatoFin.getSiacDOrdinativoStato().getOrdStatoCode().equalsIgnoreCase(CostantiFin.D_ORDINATIVO_STATO_ANNULLATO)){
							
							movimentoConReversali = true;
							break;
						}
					}
					
				}
			}	
			
			return movimentoConReversali; 
		}

		
		
		public ModificaMovimentoGestioneEntrata creaModificaImportoSub(Integer uIdAccertamento, SubAccertamento sub,
				ModificaMovimentoGestioneEntrata movimento, DatiOperazioneDto datiOperazioneDto,
				int numeroModifica) {
			Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
			ModificaMovimentoGestioneEntrata mod = (ModificaMovimentoGestioneEntrata)inserisciModificaMovimentoImporto(movimento, sub.getUid(), idEnte , datiOperazioneDto, numeroModifica);
			mod.setNumeroSubAccertamento(sub.getNumeroBigDecimal().intValue());
			return mod;
		}
		



		/**
		 * Operazione interna di doppia gestione
		 * 
		 * @param richiedente
		 * @param ente
		 * @param bilancio
		 * @param primoAccertamentoDaInserire
		 * @param datiOperazione
		 * @return EsitoOperazioneInserimentoMovgestDoppiaGestioneDto
		 */
		/*@Override
		public EsitoOperazioneInserimentoMovgestDoppiaGestioneDto operazioneInternaInserisceMovimento(Richiedente richiedente, Ente ente, Bilancio bilancio, MovimentoGestione primoImpegnoDaInserire, DatiOperazioneDto datiOperazione,Integer forzaMaxCodePerDoppia){
			
			EsitoOperazioneInserimentoMovgestDoppiaGestioneDto esito = new EsitoOperazioneInserimentoMovgestDoppiaGestioneDto();
			
			List<Errore> listaErrori = this.controlliDiMeritoInserimentoAccertamentoOperazioneInterna(richiedente,  ente, bilancio, (Accertamento) primoImpegnoDaInserire, datiOperazione);
			
			// setto eventuali errori
			esito.setListaErrori(listaErrori);
			
			// se non ci sono errori procedo con inserimento
			if(listaErrori==null || listaErrori.size()==0){
				Accertamento accertamento  = (Accertamento) this.inserisceImpegno(richiedente, ente, bilancio, primoImpegnoDaInserire, datiOperazione,forzaMaxCodePerDoppia);
				// setto accertamento inserito
				esito.setMovimentoGestione(accertamento);
			}
			
			
			//Termino restituendo l'oggetto di ritorno: 
			return esito;
		}	*/
		
		/*@Override	
		public EsitoAggiornamentoMovimentoGestioneDto operazioneInternaAggiornaMovGest(Richiedente richiedente, Ente ente, Bilancio bilancio, MovimentoGestione impegnoDaAggiornare, Soggetto soggettoCreditore, UnitaElementareGestione unitaElemDiGest, 
				DatiOperazioneDto datiOperazione, SubMovgestInModificaInfoDto subMovgestInModificaValutati,boolean fromDoppiaGestione,CapitoliInfoDto capitoliInfo){
			EsitoAggiornamentoMovimentoGestioneDto esito=new EsitoAggiornamentoMovimentoGestioneDto();
			ImpegnoInModificaInfoDto impegnoInModificaInfoDto = getDatiGeneraliImpegnoInAggiornamento((Accertamento)impegnoDaAggiornare, datiOperazione, bilancio);
		
			impegnoInModificaInfoDto.setDoppiaGestione(fromDoppiaGestione);
		
			if(subMovgestInModificaValutati==null){
				impegnoInModificaInfoDto = valutaSubImp(impegnoDaAggiornare, impegnoInModificaInfoDto, datiOperazione, bilancio);
			} else {
				impegnoInModificaInfoDto.setInfoSubValutati(subMovgestInModificaValutati);
			}
			
			HashMap<Integer, CapitoloEntrataGestione> capitoliEntrata = capitoliInfo.getCapitoliDaServizioEntrata();
			
			List<Errore> listaErrori = this.controlliDiMeritoAggiornamentoAccertamentoOperazioneInterna(richiedente,  ente, bilancio, (Accertamento) impegnoDaAggiornare,
					datiOperazione, impegnoInModificaInfoDto,capitoliEntrata);
			if(listaErrori!=null && listaErrori.size()>0){
				esito.setListaErrori(listaErrori);
				return esito;
			}
	
			
			esito = this.aggiornaImpegno(richiedente, ente, bilancio, impegnoDaAggiornare, soggettoCreditore, unitaElemDiGest,datiOperazione,
					impegnoInModificaInfoDto,capitoliInfo);
				
			//Termino restituendo l'oggetto di ritorno: 
			return esito;
		}*/
		
		
		/*
		 * SIAC 6997
		 */
		public Integer getCountRicercaAccertamentiSubAccertamentiROR(Ente ente, Richiedente richiedente, ParametroRicercaAccSubAcc paramRic){
			final String methodName="getCountRicercaAccertamentiSubAccertamentiROR";
			Integer countResult = 0;
			
			if(null!=paramRic.getTipoProvvedimento() && paramRic.getTipoProvvedimento().getUid()!=0){	
				Timestamp now = new Timestamp(currentTimeMillis());
				List<SiacDAttoAmmTipoFin> siacDAttoAmmTipoList = siacDAttoAmmTipoRepository.findDAttoAmmTipoValidoByAttoAmmTipoIdAndEnteId(ente.getUid(), paramRic.getTipoProvvedimento().getUid(), now); 
				if(null!=siacDAttoAmmTipoList && siacDAttoAmmTipoList.size() > 0){
					String codiceTipoProvvedimento = siacDAttoAmmTipoList.get(0).getAttoammTipoCode();
					paramRic.getTipoProvvedimento().setCodice(codiceTipoProvvedimento);
				}
			}
			
			RicercaAccSubAccParamDto paramSearch = map(paramRic, RicercaAccSubAccParamDto.class);

			if(paramRic.getUidStrutturaAmministrativoContabile()!=null){
				paramSearch.setUidStrutturaAmministrativoContabile(paramRic.getUidStrutturaAmministrativoContabile());
			}
			
			//SIAC-7486 e SIAC-7709
			if(org.apache.commons.lang3.StringUtils.isNotBlank(paramRic.getStrutturaSelezionataCompetente())){
				try{
					Integer strutturaCompetenteInt = Integer.parseInt(paramRic.getStrutturaSelezionataCompetente());
					SiacTClass classificatoreStruttAmm = siacTClassDao.findById(strutturaCompetenteInt);
					List<Integer> listSacInteger = siacTClassDao.findFigliClassificatoreIds(paramRic.getAnnoEsercizio().toString(), 
							SiacDClassTipoEnum.byCodiceEvenNull(classificatoreStruttAmm.getSiacDClassTipo().getClassifTipoCode()), ente.getUid(),
							classificatoreStruttAmm.getClassifCode());
					if(listSacInteger!= null && !listSacInteger.isEmpty()){
						paramSearch.setListStrutturaCompetenteInt(listSacInteger);
					}
				}catch(Exception e ){
					log.error(methodName, "Errore durante la gestione della struttura competente: " + e.getMessage());
				}
			}
			
			
			// QUERY PRINCIPALE - ACCERTAMENTI
			// JIRA-1057
			List<IdImpegnoSubimpegno> listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();
			listaIdImpegni = accertamentoDao.ricercaAccertamentiSubAccertamentiROR(ente.getUid(), paramSearch, false);
			
			if(listaIdImpegni!=null && !listaIdImpegni.isEmpty()){
				
				String clausolaIN =  parseArrayToInMovgestTs(listaIdImpegni);
				String[] ids =  clausolaIN.split(",");
				countResult = ids.length;
				
			}else countResult = 0;
			
			return countResult;
		}
		
		
		public List<SubAccertamento> ricercaSinteticaAccertamentiSubAccertamentiROR(Ente ente, Richiedente richiedente, ParametroRicercaAccSubAcc paramRic,
				int numPagina, int numRisPerPagina){
			final String methodName="ricercaSinteticaAccertamentiSubAccertamentiROR";
		
			
			
			RicercaAccSubAccParamDto paramSearch = map(paramRic, RicercaAccSubAccParamDto.class);

			if(paramRic.getUidStrutturaAmministrativoContabile()!=null){
				paramSearch.setUidStrutturaAmministrativoContabile(paramRic.getUidStrutturaAmministrativoContabile());
			}
			
			
			//SIAC-7486 e SIAC-7709
			if(org.apache.commons.lang3.StringUtils.isNotBlank(paramRic.getStrutturaSelezionataCompetente())){
				try{
					Integer strutturaCompetenteInt = Integer.parseInt(paramRic.getStrutturaSelezionataCompetente());
					SiacTClass classificatoreStruttAmm = siacTClassDao.findById(strutturaCompetenteInt);
					List<Integer> listSacInteger = siacTClassDao.findFigliClassificatoreIds(paramRic.getAnnoEsercizio().toString(), 
							SiacDClassTipoEnum.byCodiceEvenNull(classificatoreStruttAmm.getSiacDClassTipo().getClassifTipoCode()), ente.getUid(),
							classificatoreStruttAmm.getClassifCode());
					if(listSacInteger!= null && !listSacInteger.isEmpty()){
						paramSearch.setListStrutturaCompetenteInt(listSacInteger);
					}
				}catch(Exception e ){
					log.error(methodName, e.getMessage());
				}
			}
			
			// QUERY PRINCIPALE - ACCERTAMENTI
			// JIRA-1057
			List<IdImpegnoSubimpegno> listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();
			listaIdImpegni = accertamentoDao.ricercaAccertamentiSubAccertamentiROR(ente.getUid(), paramSearch, false);
			
			if(listaIdImpegni==null)
				listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();

			
			List<SubAccertamento> listaSubAccertamenti = new ArrayList<SubAccertamento>();
			
			if(listaIdImpegni!=null && listaIdImpegni.size() > 0){
				
				String clausolaIN =  parseArrayToInMovgestTs(listaIdImpegni);
				
				String[] ids =  clausolaIN.split(",");
				List<String> idsList = new ArrayList<String>(Arrays.asList(ids));
				String idsPaginati  = getIdsPaginati(idsList, numPagina, numRisPerPagina);
				
				
				
				List<SiacTMovgestTsFin> listaSubAcc = movimentoGestioneDao.ricercaSiacTMovgestTsPerIN(idsPaginati);
				
				if(listaSubAcc!=null && !listaSubAcc.isEmpty()){
					
					
					BigDecimal importoAttualePadre = BigDecimal.ZERO;
					//StrutturaAmministrativoContabile sacComponente = new StrutturaAmministrativoContabile();
					
					for (SiacTMovgestTsFin siacTMovgestts : listaSubAcc) {
						
							

						SubAccertamento subTrovato = map(siacTMovgestts, SubAccertamento.class, FinMapId.SiacTMovgestTs_SubAccertamento);
					
						//ANNOMOVIMENTO
						if(siacTMovgestts.getSiacTMovgest()!= null){
							subTrovato.setAnnoMovimento(siacTMovgestts.getSiacTMovgest().getMovgestAnno());
							
								
							//CAPITOLO
							if(siacTMovgestts.getSiacTMovgest().getSiacRMovgestBilElems() != null &&
									!siacTMovgestts.getSiacTMovgest().getSiacRMovgestBilElems().isEmpty() ){
								
								List<SiacRMovgestBilElemFin> listaSiacRMovgestBilElem = siacTMovgestts.getSiacTMovgest().getSiacRMovgestBilElems();
									SiacRMovgestBilElemFin relazioneValida = DatiOperazioneUtil.getValido(listaSiacRMovgestBilElem, null);
									if(relazioneValida!= null && relazioneValida.getSiacTBilElem()!= null){
										SiacTBilElemFin siacTBilElem = relazioneValida.getSiacTBilElem();
										CapitoloEntrataGestione capUG = new CapitoloEntrataGestione();
										capUG.setUid(siacTBilElem.getUid());
										capUG.setAnnoCapitolo(Integer.parseInt(siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno()));
										capUG.setNumeroCapitolo(Integer.parseInt(siacTBilElem.getElemCode()));
										capUG.setNumeroArticolo(Integer.parseInt(siacTBilElem.getElemCode2()));
										capUG.setNumeroUEB(Integer.parseInt(siacTBilElem.getElemCode3()));
										capUG.setStrutturaAmministrativoContabile(mapStrutturaAmministrativaContabileCapitolo(siacTBilElem.getSiacRBilElemClasses()));
										subTrovato.setCapitoloEntrataGestione(capUG);
									}
							}
							
							
							
							
						}
						//STATO
						if(siacTMovgestts.getSiacRMovgestTsStatos()!= null && !siacTMovgestts.getSiacRMovgestTsStatos().isEmpty() &&
								siacTMovgestts.getSiacRMovgestTsStatos().get(0).getSiacDMovgestStato()!= null){
							
							SiacRMovgestTsStatoFin statoFin =   DatiOperazioneUtil.getValido(siacTMovgestts.getSiacRMovgestTsStatos(), null);
							if(statoFin != null && statoFin.getSiacDMovgestStato()!= null){
								subTrovato.setDescrizioneStatoOperativoMovimentoGestioneEntrata(statoFin.getSiacDMovgestStato().getMovgestStatoDesc());
								subTrovato.setStatoOperativoMovimentoGestioneEntrata(statoFin.getSiacDMovgestStato().getMovgestStatoCode());
							}
							
							
						}
						
						//IMPORTO
						List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet = siacTMovgestts.getSiacTMovgestTsDets();
						if(listaSiacTMovgestTsDet!=null && listaSiacTMovgestTsDet.size()>0){
							//prendo solo i validi
							listaSiacTMovgestTsDet = DatiOperazioneUtil.soloValidi(listaSiacTMovgestTsDet, null);
							for (SiacTMovgestTsDetFin siacTMovgestTsDet : listaSiacTMovgestTsDet) {
								//controllo x sicurezza di nuovo la validita del dato
								if(siacTMovgestTsDet!=null && DatiOperazioneUtil.isValido(siacTMovgestTsDet, null)){
									if(CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE.equalsIgnoreCase(siacTMovgestTsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())){
										subTrovato.setImportoAttuale(siacTMovgestTsDet.getMovgestTsDetImporto());											
									} 
								}
							}
						}
						
						
						//SOGGETTO
						boolean soggettoValid=false;
						if(siacTMovgestts.getSiacRMovgestTsSogs()!= null && !siacTMovgestts.getSiacRMovgestTsSogs().isEmpty() &&
								siacTMovgestts.getSiacRMovgestTsSogs().get(0).getSiacTSoggetto()!= null){
							Soggetto soggetto = new Soggetto();
							
							List<SiacRMovgestTsSogFin> listaSiacRMovgestTsSogFin = siacTMovgestts.getSiacRMovgestTsSogs();
							SiacRMovgestTsSogFin soggettoFin =  DatiOperazioneUtil.getValido(listaSiacRMovgestTsSogFin, null);
							if(soggettoFin!= null && soggettoFin.getSiacTSoggetto()!= null){
								soggetto.setCodiceSoggetto(soggettoFin.getSiacTSoggetto().getSoggettoCode());
								soggetto.setDenominazione(soggettoFin.getSiacTSoggetto().getSoggettoDesc());
								soggettoValid=true;
							}
							subTrovato.setSoggetto(soggetto);
						}
						
						if(!soggettoValid){
							//PER CLASSE
							if(siacTMovgestts.getSiacRMovgestTsSogclasses()!= null && !siacTMovgestts.getSiacRMovgestTsSogclasses().isEmpty() ){
								Soggetto soggetto = new Soggetto();
								
								List<SiacRMovgestTsSogclasseFin> listaSiacRMovgestTsSogFin = siacTMovgestts.getSiacRMovgestTsSogclasses();
								SiacRMovgestTsSogclasseFin soggettoFin =  DatiOperazioneUtil.getValido(listaSiacRMovgestTsSogFin, null);
								if(soggettoFin!= null && soggettoFin.getSiacDSoggettoClasse()!= null){
									soggetto.setCodiceSoggetto(soggettoFin.getSiacDSoggettoClasse().getSoggettoClasseCode());
									soggetto.setDenominazione(soggettoFin.getSiacDSoggettoClasse().getSoggettoClasseDesc());
									soggettoValid=true;
								}
								subTrovato.setSoggetto(soggetto);
							}
							
						}
						
						
								
						//ATTO
						if(siacTMovgestts.getSiacRMovgestTsAttoAmms()!= null && !siacTMovgestts.getSiacRMovgestTsAttoAmms().isEmpty() && 
								siacTMovgestts.getSiacRMovgestTsAttoAmms().get(0).getSiacTAttoAmm()!= null){
							
							AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
							
							List<SiacRMovgestTsAttoAmmFin> listaSiacRMovgestTsAttoAmmFin= siacTMovgestts.getSiacRMovgestTsAttoAmms();
							SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm = DatiOperazioneUtil.getValido(listaSiacRMovgestTsAttoAmmFin, null);
							
							if(siacRMovgestTsAttoAmm!= null && siacRMovgestTsAttoAmm.getSiacTAttoAmm()!= null ){
								attoAmministrativo.setAnno(Integer.parseInt(siacRMovgestTsAttoAmm.getSiacTAttoAmm().getAttoammAnno()));
								attoAmministrativo.setNumero(siacRMovgestTsAttoAmm.getSiacTAttoAmm().getAttoammNumero());
								attoAmministrativo.setOggetto(siacRMovgestTsAttoAmm.getSiacTAttoAmm().getAttoammOggetto());
								
								if(siacRMovgestTsAttoAmm.getSiacTAttoAmm().getSiacDAttoAmmTipo()!= null){
									TipoAtto tipoAtto = new TipoAtto();
									tipoAtto.setCodice(siacRMovgestTsAttoAmm.getSiacTAttoAmm().getSiacDAttoAmmTipo().getAttoammTipoCode());
									attoAmministrativo.setTipoAtto(tipoAtto);
								}
								if(siacRMovgestTsAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses()!= null &&
										!siacRMovgestTsAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses().isEmpty() &&
										siacRMovgestTsAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses().get(0).getSiacTClass()!= null){
									
									StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
									SiacRAttoAmmClassFin siacRAttoAmmClassFin = DatiOperazioneUtil.getValido(siacRMovgestTsAttoAmm.getSiacTAttoAmm().getSiacRAttoAmmClasses(), null);
									if(siacRAttoAmmClassFin!= null && siacRAttoAmmClassFin.getSiacTClass()!=null){
										sac.setCodice(siacRAttoAmmClassFin.getSiacTClass().getClassifCode());
									}
									attoAmministrativo.setStrutturaAmmContabile(sac);
									
								}
							}
							
							
							
							
							subTrovato.setAttoAmministrativo(attoAmministrativo);
						
						}
						
						
						
						
						BigDecimal importoDisponibilitaIncassare = BigDecimal.ZERO;
						
						//SE TESTATA O SUB
						if(siacTMovgestts.getSiacDMovgestTsTipo()!=null && siacTMovgestts.getSiacDMovgestTsTipo().getMovgestTsTipoCode()!= null
								&& siacTMovgestts.getSiacDMovgestTsTipo().getMovgestTsTipoCode().equals("T")){
							subTrovato.setNumeroAccertamentoPadre(subTrovato.getNumeroBigDecimal());
							subTrovato.setNumeroBigDecimal(null);
							
							//STRUTTURA COMPETENTE
							subTrovato.setStrutturaCompetenteObj(getStrutturaAmmCont(siacTMovgestts));
							
							//DISPONIBILITA A PAGARE
							DisponibilitaMovimentoGestioneContainer disponibilitaIncassare = calcolaDisponibiltaAIncassareAccertamento(siacTMovgestts, subTrovato.getStatoOperativoMovimentoGestioneEntrata(), ente.getUid());
							importoDisponibilitaIncassare = (disponibilitaIncassare != null) ? disponibilitaIncassare.getDisponibilita() : importoDisponibilitaIncassare;
							
							importoAttualePadre = subTrovato.getImportoAttuale();
							
						
						}else{
							
							subTrovato.setNumeroAccertamentoPadre(siacTMovgestts.getSiacTMovgest().getMovgestNumero());
							if(siacTMovgestts.getSiacTMovgest()!= null && siacTMovgestts.getSiacTMovgest().getSiacTMovgestTs()!= null
									&& !siacTMovgestts.getSiacTMovgest().getSiacTMovgestTs().isEmpty()){
								
								for(SiacTMovgestTsFin siacTmovgestTsFin:siacTMovgestts.getSiacTMovgest().getSiacTMovgestTs()){
									if(siacTmovgestTsFin.getMovgestTsIdPadre()==null){//PADRE
										subTrovato.setStrutturaCompetenteObj(getStrutturaAmmCont(siacTmovgestTsFin));
										break;
									}
								}
							}
							
							DisponibilitaMovimentoGestioneContainer disponibilitaIncassare = calcolaDisponibiltaAIncassareSubAccertamento(siacTMovgestts, subTrovato.getStatoOperativoMovimentoGestioneEntrata(), ente.getUid());
							importoDisponibilitaIncassare = (disponibilitaIncassare != null) ? disponibilitaIncassare.getDisponibilita() : importoDisponibilitaIncassare;
							subTrovato.setImportoAttualePadre(importoAttualePadre);
						}
						//START
						int  annoEsercizio = paramRic.getAnnoEsercizio()!= null ? paramRic.getAnnoEsercizio().intValue() : 0;
						buildRiepilogoImportiRor( annoEsercizio, subTrovato, siacTMovgestts,  importoDisponibilitaIncassare,ente.getUid());
						listaSubAccertamenti.add(subTrovato);
					}	

				}
				
			}	
			
			//Termino restituendo l'oggetto di ritorno: 
		    return listaSubAccertamenti;
		}
		
		
		
		
		
		
		
		private void buildRiepilogoImportiRor(int  annoEsercizio, SubAccertamento subTrovato, 
				SiacTMovgestTsFin siacTMovgestts, BigDecimal importoDisponibilitaIncassare, int enteUid){
			
			
			 BigDecimal importoDaRiaccertare =  subTrovato.getImportoAttuale();
			 BigDecimal modificheRor =  BigDecimal.ZERO;
			 BigDecimal importoGestitoInRo = BigDecimal.ZERO;
			 BigDecimal importoCancellareInsussistenza = BigDecimal.ZERO;
			 BigDecimal importoCancellareInesigibilita = BigDecimal.ZERO;
			 BigDecimal impertoReimpDifferitoN = BigDecimal.ZERO;
			 BigDecimal impertoReimpDifferitoN1 = BigDecimal.ZERO;
			 BigDecimal impertoReimpDifferitoN2 = BigDecimal.ZERO;
			 BigDecimal impertoReimpDifferitoNP = BigDecimal.ZERO;
			 
			 
			//SIAC-7551
			 BigDecimal documentiNonIncassatiAnnoSuccessico = BigDecimal.ZERO;
			BigDecimal incassatoAnnoSuccessico = BigDecimal.ZERO;
				try{

					if(siacTMovgestts.getSiacTMovgest()!= null){
						subTrovato.setAnnoMovimento(siacTMovgestts.getSiacTMovgest().getMovgestAnno());
						subTrovato.setNumeroAccertamentoPadre(siacTMovgestts.getSiacTMovgest().getMovgestNumero());
					}
					
					
					int annoSuccessivo = annoEsercizio+1;
					 List<SiacTMovgestTsFin> siacTmovgestTsAnnoSuccessuvi =  movimentoGestioneDao.ricercaSiacTMovgestTsPerAnnoEsercizioUid( enteUid,
							 annoSuccessivo,  subTrovato.getNumeroAccertamentoPadre(), 
							 subTrovato.getAnnoMovimento(),CostantiFin.MOVGEST_TIPO_ACCERTAMENTO);
					if(siacTmovgestTsAnnoSuccessuvi!= null && !siacTmovgestTsAnnoSuccessuvi.isEmpty()){
						SiacTMovgestTsFin siacTmovgestTsAnnoSucc = new SiacTMovgestTsFin();
						for(SiacTMovgestTsFin s :siacTmovgestTsAnnoSuccessuvi){
							if(s.getDataCancellazione()== null){
								siacTmovgestTsAnnoSucc = s;
								break;
							}
						}
						
						if(siacTmovgestTsAnnoSucc.getMovgestTsId()!= null){
							ConsultaDettaglioAccertamentoDto datiConsulta = accertamentoDao.consultaDettaglioAccertamento(siacTmovgestTsAnnoSucc.getMovgestTsId());
							if(datiConsulta!=null){
								if(datiConsulta.getTotDocNonInc()!= null){
									RiepilogoImportoNumero rinTotDocNonInc = new RiepilogoImportoNumero(datiConsulta.getTotDocNonInc(), datiConsulta.getnDocNonInc());
									documentiNonIncassatiAnnoSuccessico = (rinTotDocNonInc!= null && rinTotDocNonInc.getImporto()!= null) ? rinTotDocNonInc.getImporto() : documentiNonIncassatiAnnoSuccessico;
								}
								
								if(datiConsulta.getTotImpOrd()!= null){
									RiepilogoImportoNumero rinIncassatoAnnoSucc = new RiepilogoImportoNumero(datiConsulta.getTotImpOrd(), datiConsulta.getnOrd());
									incassatoAnnoSuccessico = (rinIncassatoAnnoSucc!= null && rinIncassatoAnnoSucc.getImporto()!= null) ? rinIncassatoAnnoSucc.getImporto() : incassatoAnnoSuccessico;
								}
							}
						}
						
						
					}
					
					subTrovato.setIncassatoAnnoSuccessivo(incassatoAnnoSuccessico);
					subTrovato.setDocumentiNoIncassatiAnnoSuccessivo(documentiNonIncassatiAnnoSuccessico);
					
				}catch(Exception e){
					log.error("AccertamentoOttimizzatoDad", e.getMessage());
				}
			 
			 
			 
			 /*Conteggio del numero totale
			  * delle modifiche appartenente al padre
			  */
			 int numeroModifiche =0;
			 if(siacTMovgestts.getSiacTMovgest()!= null && siacTMovgestts.getSiacTMovgest().getSiacTMovgestTs()!= null && !siacTMovgestts.getSiacTMovgest().getSiacTMovgestTs().isEmpty()){
				 for(SiacTMovgestTsFin stmFin : siacTMovgestts.getSiacTMovgest().getSiacTMovgestTs()){
					 if(stmFin.getSiacTMovgestTsDetMods()!= null && !stmFin.getSiacTMovgestTsDetMods().isEmpty()){
						 for(SiacTMovgestTsDetModFin mod : stmFin.getSiacTMovgestTsDetMods()){
							 numeroModifiche++;
						 }
					 }
				 }
			 }
			 subTrovato.setNumeroTotaleModifcheMovimento(numeroModifiche);
			 
			 
			if(siacTMovgestts.getSiacTMovgestTsDetMods()!= null && !siacTMovgestts.getSiacTMovgestTsDetMods().isEmpty()){
				List<ModificaMovimentoGestioneEntrata> listaModificheMovimentoGestioneEntrata = new ArrayList<ModificaMovimentoGestioneEntrata>();
				for(SiacTMovgestTsDetModFin modFin : siacTMovgestts.getSiacTMovgestTsDetMods()){
					
					if(modFin.getSiacRModificaStato()!= null && modFin.getSiacRModificaStato().getSiacTModifica()!= null
							//SIAC-8052: le modifiche contestuali hanno tutte mod_id = null, ma devono essere considerate
//							modFin.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo()!= null &&
//							modFin.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo().getModTipoCode()!= null
							&& modFin.getSiacRModificaStato().getSiacDModificaStato()!= null 
							&& !modFin.getSiacRModificaStato().getSiacDModificaStato().getModStatoCode().equals(CostantiFin.D_MODIFICA_STATO_ANNULLATO)){
						
						//SIAC-8052: le modifiche contestuali hanno tutte mod_id = null, ma devono essere considerate
						SiacDModificaTipoFin tipo = modFin.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo();
						String modTipoCode = tipo != null && StringUtils.isNotBlank(tipo.getModTipoCode())? 
								modFin.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo().getModTipoCode() 
								//SIAC-8052: lo lascio stringa vuota: non fara' match con nient'altro e sono sicura di non spaccare niente
								: "";
						if(modTipoCode.equals(CodiciMotiviModifiche.RORM.getCodice()) || modTipoCode.equals(CodiciMotiviModifiche.REIMP.getCodice())
								|| modTipoCode.equals(CodiciMotiviModifiche.INEROR.getCodice()) || modTipoCode.equals(CodiciMotiviModifiche.INSROR.getCodice())){
							
							modificheRor = modificheRor.add(modFin.getMovgestTsDetImporto());
						}
						
						
						if(modTipoCode.equals(CodiciMotiviModifiche.REIMP.getCodice()) || modTipoCode.equals(CodiciMotiviModifiche.INEROR.getCodice()) 
								|| modTipoCode.equals(CodiciMotiviModifiche.INSROR.getCodice())){
							
							if(modFin.getMovgestTsDetImporto()!= null){
								importoGestitoInRo = importoGestitoInRo.add(modFin.getMovgestTsDetImporto().abs());
							}
							
						}
						
						
						if( modTipoCode.equals(CodiciMotiviModifiche.INSROR.getCodice())){
							importoCancellareInsussistenza = importoCancellareInsussistenza.add(modFin.getMovgestTsDetImporto());
						}
						if( modTipoCode.equals(CodiciMotiviModifiche.INEROR.getCodice())){
							importoCancellareInesigibilita = importoCancellareInesigibilita.add(modFin.getMovgestTsDetImporto());
						}
						if( modTipoCode.equals(CodiciMotiviModifiche.REIMP.getCodice()) && annoEsercizio !=0){
							if(modFin.getMtdmReimputazioneAnno()!= null && modFin.getMtdmReimputazioneAnno().intValue()==annoEsercizio+1){
								impertoReimpDifferitoN = impertoReimpDifferitoN.add(modFin.getMovgestTsDetImporto());
							}else if(modFin.getMtdmReimputazioneAnno()!= null && modFin.getMtdmReimputazioneAnno().intValue() == annoEsercizio+2){
								impertoReimpDifferitoN1 = impertoReimpDifferitoN1.add(modFin.getMovgestTsDetImporto());
							}else if(modFin.getMtdmReimputazioneAnno()!= null && modFin.getMtdmReimputazioneAnno().intValue() == annoEsercizio+3){
								impertoReimpDifferitoN2 = impertoReimpDifferitoN2.add(modFin.getMovgestTsDetImporto());
							}else if(modFin.getMtdmReimputazioneAnno()!= null && modFin.getMtdmReimputazioneAnno().intValue() > annoEsercizio+3){
								impertoReimpDifferitoNP = impertoReimpDifferitoNP.add(modFin.getMovgestTsDetImporto());
							}
						}
						modFin.getMtdmReimputazioneAnno();
					}
					/*Inserimento delle modifiche sulla lista 
					 * 
					 */
					if(modFin.getSiacRModificaStato()!= null && modFin.getSiacRModificaStato().getSiacTModifica()!= null 
							){
						ModificaMovimentoGestioneEntrata mmgs = map(modFin.getSiacRModificaStato().getSiacTModifica(), ModificaMovimentoGestioneEntrata.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneEntrata);
						//SIAC-8052: le modifiche contestuali non hanno tipo
						if(modFin.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo()!= null ){
							mmgs.setTipoModificaMovimentoGestione(modFin.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo().getModTipoCode());
						}
						mmgs.setAnnoReimputazione(modFin.getMtdmReimputazioneAnno());
						if(modFin.getSiacRModificaStato().getSiacTModifica().getSiacTAttoAmm() != null && modFin.getSiacRModificaStato().getSiacTModifica().getSiacTAttoAmm().getAttoammId()!= null){
							AttoAmministrativo atto = new AttoAmministrativo();
							atto.setUid(modFin.getSiacRModificaStato().getSiacTModifica().getSiacTAttoAmm().getAttoammId());
							mmgs.setAttoAmministrativo(atto);
						}
						if(modFin.getMtdmReimputazioneFlag()==null){
							mmgs.setReimputazione(false);
							
						}else{
							mmgs.setReimputazione(modFin.getMtdmReimputazioneFlag());
						}
						for( SiacRModificaStatoFin siacTModificaStato : modFin.getSiacRModificaStato().getSiacTModifica().getSiacRModificaStatos()){
							if(siacTModificaStato.getDataCancellazione()==null && siacTModificaStato.getSiacDModificaStato().getModStatoCode().equals("V")){
								mmgs.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.VALIDO);
								mmgs.setCodiceStatoOperativoModificaMovimentoGestione("V");
								break;
							}else{
								mmgs.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.ANNULLATO);
								mmgs.setCodiceStatoOperativoModificaMovimentoGestione("A");
							}
							
						}
						listaModificheMovimentoGestioneEntrata.add(mmgs);
					}
				}
				modificheRor = modificheRor.abs();
				importoDaRiaccertare = importoDaRiaccertare.add(modificheRor);
				subTrovato.setListaModificheMovimentoGestioneEntrata(listaModificheMovimentoGestioneEntrata);
			}
			
			BigDecimal incassato = BigDecimal.ZERO;
			if(subTrovato.getImportoAttuale()!= null ){
				incassato = subTrovato.getImportoAttuale().subtract(importoDisponibilitaIncassare);
			}
			subTrovato.setImportoDaRiaccertare(importoDaRiaccertare.subtract(incassato));
			subTrovato.setImportoModifiche(importoGestitoInRo);
			subTrovato.setDaCancellareInsussistenza(importoCancellareInsussistenza);
			subTrovato.setDaCancellareInesigibilita(importoCancellareInesigibilita);
			subTrovato.setDifferitoN(impertoReimpDifferitoN);
			subTrovato.setDifferitoN1(impertoReimpDifferitoN1);
			subTrovato.setDifferitoN2(impertoReimpDifferitoN2);
			subTrovato.setDifferitoNP(impertoReimpDifferitoNP);
			
			
			
			BigDecimal residuoEventualeDaMantenere = subTrovato.getImportoAttuale().subtract(incassato);//MODIFICA PTR
			subTrovato.setResiduoDaMantenere(residuoEventualeDaMantenere);
			
			
			//SIAC-7551
//			BigDecimal importoMaxDaRiaccertare = subTrovato.getImportoDaRiaccertare();
//			subTrovato.setImportoMaxDaRiaccertare(importoMaxDaRiaccertare);
			
			BigDecimal sommatorieFattureLiqNSuccessivi = documentiNonIncassatiAnnoSuccessico.add(incassatoAnnoSuccessico);
			BigDecimal importoMaxDaRiaccertare = subTrovato.getImportoDaRiaccertare().subtract(sommatorieFattureLiqNSuccessivi);
			subTrovato.setImportoMaxDaRiaccertare(importoMaxDaRiaccertare);
			
		}
		
		
		
		public SubAccertamento campiRiepilogoAccROR(String annoEsercizio, Integer movgestTsNumber, int enteUid){
			SubAccertamento subTrovato = new SubAccertamento();
			String idsPaginati = movgestTsNumber.toString();
			
			List<SiacTMovgestTsFin> listaSubAcc = movimentoGestioneDao.ricercaSiacTMovgestTsPerIN(idsPaginati);
			if(listaSubAcc!= null && !listaSubAcc.isEmpty()){
				SiacTMovgestTsFin siacTMovgestts = listaSubAcc.get(0);
				 subTrovato = map(siacTMovgestts, SubAccertamento.class, FinMapId.SiacTMovgestTs_SubAccertamento);
				
				//IMPORTO
				List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet = siacTMovgestts.getSiacTMovgestTsDets();
				if(listaSiacTMovgestTsDet!=null && listaSiacTMovgestTsDet.size()>0){
					//prendo solo i validi
					listaSiacTMovgestTsDet = DatiOperazioneUtil.soloValidi(listaSiacTMovgestTsDet, null);
					for (SiacTMovgestTsDetFin siacTMovgestTsDet : listaSiacTMovgestTsDet) {
						//controllo x sicurezza di nuovo la validita del dato
						if(siacTMovgestTsDet!=null && DatiOperazioneUtil.isValido(siacTMovgestTsDet, null)){
							if(CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE.equalsIgnoreCase(siacTMovgestTsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())){
								subTrovato.setImportoAttuale(siacTMovgestTsDet.getMovgestTsDetImporto());											
							} 
						}
					}
				}
				
				
				//DISPONIBILITA A INCASSARE
				BigDecimal importoDisponibilitaIncassare = BigDecimal.ZERO;
				if(siacTMovgestts.getSiacDMovgestTsTipo()!=null && siacTMovgestts.getSiacDMovgestTsTipo().getMovgestTsTipoCode()!= null
						&& siacTMovgestts.getSiacDMovgestTsTipo().getMovgestTsTipoCode().equals("T")){
					DisponibilitaMovimentoGestioneContainer disponibilitaIncassare = calcolaDisponibiltaAIncassareAccertamento(siacTMovgestts, subTrovato.getStatoOperativoMovimentoGestioneEntrata(), enteUid);
					importoDisponibilitaIncassare = (disponibilitaIncassare != null) ? disponibilitaIncassare.getDisponibilita() : importoDisponibilitaIncassare;
				}else{
					DisponibilitaMovimentoGestioneContainer disponibilitaIncassare = calcolaDisponibiltaAIncassareSubAccertamento(siacTMovgestts, subTrovato.getStatoOperativoMovimentoGestioneEntrata(), enteUid);
					importoDisponibilitaIncassare = (disponibilitaIncassare != null) ? disponibilitaIncassare.getDisponibilita() : importoDisponibilitaIncassare;
				}
				
				if(annoEsercizio!= null){
					try{
						int annoEsercizioInt = Integer.parseInt(annoEsercizio);
						buildRiepilogoImportiRor( annoEsercizioInt, subTrovato, siacTMovgestts,  importoDisponibilitaIncassare,enteUid);
					}catch(Exception e){
						log.error("AccertamentoOttimizzatoDad", e.getMessage());
					}
				}
				
			}
			return subTrovato;
		}
		
		
		
		private StrutturaAmministrativoContabile getStrutturaAmmCont(SiacTMovgestTsFin siacTMovgestts){
			StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
			//STRUTTURA COMPETENTE
			if(siacTMovgestts.getSiacRMovgestClasses()!= null && !siacTMovgestts.getSiacRMovgestClasses().isEmpty()){
				for(SiacRMovgestClassFin srmc : siacTMovgestts.getSiacRMovgestClasses()){
					if(srmc.isEntitaValida() && srmc.getSiacTClass()!= null && srmc.getSiacTClass().getSiacDClassTipo()!= null && 
							srmc.getSiacTClass().getSiacDClassTipo().getClassifTipoCode()!=null){
						
						if(SiacDClassTipoEnum.Cdc.getCodice().equals(srmc.getSiacTClass().getSiacDClassTipo().getClassifTipoCode())){
							sac.setCodice(srmc.getSiacTClass().getClassifCode());
							sac.setUid(srmc.getSiacTClass().getUid());
							break;
						}
					}
				}
			}
			
			return sac;
			
		}

		//SIAC-8142
		public List<Integer> caricaAnniAccertamentiConStessoNumeroNelBilancio(Accertamento accertamento, String annoBilancio, Ente ente) {
			List<Integer> lista = siacTMovgestRepository.caricaAnniAccertamentiConStessoNumeroNelBilancio(accertamento.getUid(), accertamento.getNumeroBigDecimal(), MOVGEST_TIPO_CODE_ACCERTAMENTO, annoBilancio, ente.getUid());
			return lista;
		}
		
		
}
