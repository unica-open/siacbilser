/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dao.SiacTClassDao;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.dao.common.SiacDAttoAmmTipoFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacTEnteProprietarioFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttributoTClassInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ConsultaDettaglioImpegnoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OggettoDellAttributoTClass;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneModificheMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaImpSubParamDto;
import it.csi.siac.siacfinser.integration.dao.liquidazione.SiacRLiquidazioneMovgestRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestAggiudicazioneFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestTsRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDAttoAmmStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDAttoAmmTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAttoAmmClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAttoAmmStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestAggiudicazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtil;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.RiepilogoImportoNumero;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ComponenteBilancioImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ImpegnoOttimizzatoDad extends MovimentoGestioneOttimizzatoDad<Impegno, SubImpegno> {

	private static final String MOVGEST_TIPO_CODE_IMPEGNO = "I";
	
	
	@Autowired
	private SiacTEnteProprietarioFinRepository siacTEnteProprietarioRepository;

	@Autowired private CapitoloDad capitoloDad;
	
	@Autowired
	private SiacDAttoAmmTipoFinRepository siacDAttoAmmTipoRepository;
	
	@Autowired
	protected SiacRLiquidazioneMovgestRepository siacRLiquidazioneMovgestRepository;
	
	//SIAC-6865
	@Autowired
	private SiacRMovgestAggiudicazioneFinRepository siacRMovgestAggiudicazioneRepository;
	
	@Autowired
	protected SiacTClassDao siacTClassDao;
	
	//SIAC-7779
	@Autowired
	protected SiacRMovgestTsRepository siacRMovgestTsRepository;
	
	
	@Override
	protected String getMovgestTipoCode(){
		return MOVGEST_TIPO_CODE_IMPEGNO;
	}
	/**
	 * Wrapper di retro compatibilita'
	 */
	@Override
	protected Impegno convertiMovimentoGestione(SiacTMovgestFin siacTMovgest){
		return convertiMovimentoGestione(siacTMovgest,null);
	}
	
	
	@Override
	protected Impegno convertiMovimentoGestione(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto,boolean ottimizzatoCompletamenteDaChiamante){
		// mappo solo i dati minimi dell'impegno, anno, codice e descrizione
		Impegno impegnoTrovato = map(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno);	
		impegnoTrovato = EntityToModelConverter.siacTMovgestEntityToImpegnoModel(siacTMovgest, impegnoTrovato,ottimizzazioneDto, ottimizzatoCompletamenteDaChiamante);	
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoTrovato;
	}
	
	@Override
	protected Impegno convertiMovimentoGestione(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
		// mappo solo i dati minimi dell'impegno, anno, codice e descrizione
		Impegno impegnoTrovato = map(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno);	
		impegnoTrovato = EntityToModelConverter.siacTMovgestEntityToImpegnoModel(siacTMovgest, impegnoTrovato,ottimizzazioneDto);	
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoTrovato;
	}
	
	@Override
	protected Impegno convertiMovimentoGestioneOPT(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
		Impegno impegnoTrovato = map(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno);	
		impegnoTrovato = EntityToModelConverter.siacTMovgestEntityToImpegnoModel(siacTMovgest, impegnoTrovato,ottimizzazioneDto);	
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoTrovato;
	}
	
	/**
	 * wrapper di retro compatibilita'
	 */
	@Override
	protected SubImpegno convertiSubMovimento(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest, boolean caricaDatiUlteriori){
		return convertiSubMovimento(siacTMovgestTs, siacTMovgest, caricaDatiUlteriori,null);
	}
	
	@Override
	protected SubImpegno convertiSubMovimento(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest, boolean caricaDatiUlteriori,OttimizzazioneMovGestDto ottimizzazioneDto){
		SubImpegno subImpegnoTrovato = map(siacTMovgestTs, SubImpegno.class, FinMapId.SiacTMovgestTs_SubImpegno);
		subImpegnoTrovato = EntityToModelConverter.siacTMovgestTsEntityToSubImpegnoModel(siacTMovgestTs, subImpegnoTrovato,siacTMovgest, caricaDatiUlteriori,ottimizzazioneDto);
		//Termino restituendo l'oggetto di ritorno: 
        return subImpegnoTrovato;
	}

	@Override
	protected SubImpegno convertiSubMovimentoOPT(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
		SubImpegno subImpegnoTrovato = map(siacTMovgestTs, SubImpegno.class, FinMapId.SiacTMovgestTs_SubImpegno);
		subImpegnoTrovato = EntityToModelConverter.siacTMovgestTsEntityToSubImpegnoModel(siacTMovgestTs, subImpegnoTrovato,siacTMovgest, false,ottimizzazioneDto);
		//Termino restituendo l'oggetto di ritorno: 
        return subImpegnoTrovato;
	}

	@Override
	protected boolean checkStato(String stato) {
		return CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(stato) || CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(stato);
	}
	
	
	
	/**
	 * Ricerco le eventuali liquidazioni collegate al movimento
	 * @param uidMovimetno
	 * @return
	 */
	public Boolean ricercaLiquidazioniByMovimentoGestione(Integer idMovimento, Integer idEnte){
		
		Boolean movimentoConLiquidazioni = Boolean.FALSE;
		
		// Devo cercare prima il movimento, perche' non e' detto sia di tipo Testata
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List<SiacTMovgestTsFin> siacTMovgestTs = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, now, idMovimento);
		if(siacTMovgestTs==null || siacTMovgestTs.isEmpty()){
			//l'uid potrebbe essere di un sub, ricerco
			siacTMovgestTs = siacTMovgestTsRepository.findSubMovgestTsByMovgest(idEnte, now, idMovimento);
		}
		
		// Qui screma gia' sulle R valide, basta anche un solo legame valido 
		
		List<Integer> idsRLiqMovgest = movimentoGestioneDao.getIdsSiacRLiquidazioneMovgestFinByMovGestTsMassive(siacTMovgestTs);
		
		//List<SiacRLiquidazioneMovgestFin> listaSiacRLiquidazioneMovgest = movimentoGestioneDao.ricercaByMovGestTsMassive(siacTMovgestTs,"SiacRLiquidazioneMovgestFin");
		
		if(idsRLiqMovgest!=null && idsRLiqMovgest.size()>0){
			
			for (Integer idRIt : idsRLiqMovgest) {
				
				SiacRLiquidazioneMovgestFin rLiquidazioneMovgest = siacRLiquidazioneMovgestRepository.findOne(idRIt);
				
				SiacTLiquidazioneFin tLiquidazione = rLiquidazioneMovgest.getSiacTLiquidazione();
				
				List<SiacRLiquidazioneStatoFin> siacRLiquidazioneStatos= tLiquidazione.getSiacRLiquidazioneStatos();
				
				for (SiacRLiquidazioneStatoFin rliquidazioneStato : siacRLiquidazioneStatos) {
					if(rliquidazioneStato.getDataFineValidita() == null && !rliquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode().equalsIgnoreCase(CostantiFin.D_LIQUIDAZIONE_STATO_ANNULLATO)){
						movimentoConLiquidazioni = true;
						break;
					}
				}
				if(movimentoConLiquidazioni){
					break;
				}
				
			}
		}	
		
		return movimentoConLiquidazioni; 
	}
	
	/**
	 * @param uidImpegno
	 * @param codice attributo to find
	 * @return the Boolean il valore dell'attributo corrispondente al codice attributo
	 */
	public Boolean getAttributoBoolean(int uidImpegno, String codiceAttributo) {
		String methodName = "getAttributoBoolean";
		
//		 SiacTAttrEnum siacTAttrEnum = SiacTAttrEnum.byTipologiaAttributo(tipologiaAttributo);
//		 if(!String.class.equals(siacTAttrEnum.getFieldType())){
//		 	throw new IllegalArgumentException("Attributo "+tipologiaAttributo+" non di tipo testo!");
//		 }
		
//		String result = siacTMovgestTRepository.findTestoAttrValueBySiacTMovgestId(uidImpegno, siacTAttrEnum.getCodice()); //TODO mettere in SiacTAttr
		String result = siacRMovgestTsAttrRepository.findBooleanAttrValueBySiacTMovgestId(uidImpegno, codiceAttributo);
		log.debug(methodName, "Returning: " + result + " (for uidImpegno: " + uidImpegno + " and 'attr' " + codiceAttributo + ". ");
		
		Boolean bResult = null;
		if (result != null && result.trim().equalsIgnoreCase("S")) {
			bResult = Boolean.TRUE;
		} else if (result != null && result.trim().equalsIgnoreCase("N")) {
			bResult = Boolean.FALSE;
		}
		
		return bResult;
	}
	
	
	
	
	
	/** SIAC-6997
	 * Metodo che si occupa di calcolare solo il numero di impegni trovati.
	 *
	 * @param ente the ente
	 * @param richiedente the richiedente
	 * @param paramRic the param ric
	 * @return the count ricerca impegni sub impegni
	 */
	public Integer getCountRicercaImpegniSubImpegniROR(Ente ente, Richiedente richiedente , ParametroRicercaImpSub paramRic){
		final String methodName="getCountRicercaImpegniSubImpegniROR";
		Integer countResult = 0;
		if(paramRic.getTipoProvvedimento()!=null && paramRic.getTipoProvvedimento().getUid()!=0){
			Timestamp now = new Timestamp(currentTimeMillis());
			List<SiacDAttoAmmTipoFin> siacDAttoAmmTipoList = siacDAttoAmmTipoRepository.findDAttoAmmTipoValidoByAttoAmmTipoIdAndEnteId(ente.getUid(), paramRic.getTipoProvvedimento().getUid(), now); 
			if(null!=siacDAttoAmmTipoList && siacDAttoAmmTipoList.size() > 0){
				String codiceTipoProvvedimento = siacDAttoAmmTipoList.get(0).getAttoammTipoCode();
				paramRic.getTipoProvvedimento().setCodice(codiceTipoProvvedimento);
			}
		}
		
		RicercaImpSubParamDto paramSearch = map(paramRic, RicercaImpSubParamDto.class);

		if(paramRic.getUidStrutturaAmministrativoContabile()!=null){
			paramSearch.setUidStrutturaAmministrativoContabile(paramRic.getUidStrutturaAmministrativoContabile());
		}

		
		//SIAC-7486 e SIAC-7709
		if(StringUtils.isNotBlank(paramRic.getStrutturaSelezionataCompetente())){
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
				log.error(methodName, "Errore nella gestione della struttura selezionata competente: " + e.getMessage());
			}
		}
		
		
		// QUERY PRINCIPALE - IMPEGNI
		// JIRA-1057
		List<IdImpegnoSubimpegno> listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();
		listaIdImpegni = impegnoDao.ricercaImpegniSubImpegniROR(ente.getUid(), paramSearch, false);
		if(listaIdImpegni!=null && listaIdImpegni.size() > 0){
			String clausolaIN =  parseArrayToInMovgestTs(listaIdImpegni);
			String[] ids =  clausolaIN.split(",");
			countResult = ids.length;
		}else countResult = 0;
		return countResult;
	}
	
	
	
	
	public List<SubImpegno> ricercaSinteticaImpegniSubImpegniROR(Ente ente, Richiedente richiedente , 
			ParametroRicercaImpSub paramRic,int numPagina, int numRisPerPagina){
		final String methodName="ricercaSinteticaImpegniSubImpegniROR";
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(ente.getUid());
		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(currentTimeMillis(), Operazione.RICERCA , siacTEnteProprietario, richiedente.getAccount().getId());
		
		RicercaImpSubParamDto paramSearch = map(paramRic, RicercaImpSubParamDto.class);
		if(paramRic.getUidStrutturaAmministrativoContabile()!=null){
			paramSearch.setUidStrutturaAmministrativoContabile(paramRic.getUidStrutturaAmministrativoContabile());
		}
		
		//SIAC-7486 e SIAC-7709
		if(StringUtils.isNotBlank(paramRic.getStrutturaSelezionataCompetente())){
			try{
				Integer strutturaCompetenteInt = Integer.parseInt(paramRic.getStrutturaSelezionataCompetente());
				SiacTClass classificatoreStruttAmm = siacTClassDao.findById(strutturaCompetenteInt);
				List<Integer> listSacInteger = siacTClassDao.findFigliClassificatoreIds(paramRic.getAnnoEsercizio().toString(), 
						SiacDClassTipoEnum.byCodiceEvenNull(classificatoreStruttAmm.getSiacDClassTipo().getClassifTipoCode()), siacTEnteProprietario.getEnteProprietarioId(),
						classificatoreStruttAmm.getClassifCode());
				if(listSacInteger!= null && !listSacInteger.isEmpty()){
					paramSearch.setListStrutturaCompetenteInt(listSacInteger);
				}
			}catch(Exception e ){
				log.error(methodName, "Errore durante la gestione della struttura selezionata competente: " + e.getMessage());
			}
		}
		
		
	
		
		
		
		// QUERY PRINCIPALE - IMPEGNI
		// JIRA-1057
		List<IdImpegnoSubimpegno> listaIdImpegni = new ArrayList<IdImpegnoSubimpegno>();
		listaIdImpegni = impegnoDao.ricercaImpegniSubImpegniROR(ente.getUid(), paramSearch, false);

		List<SubImpegno> listaSubImpegni = new ArrayList<SubImpegno>();
		
		if(listaIdImpegni!=null && listaIdImpegni.size() > 0){
			
			String clausolaIN =  parseArrayToInMovgestTs(listaIdImpegni);
			
			//CommonUtil.println("clausolaIN: "+clausolaIN);
			String[] ids =  clausolaIN.split(",");
			List<String> idsList = new ArrayList<String>(Arrays.asList(ids));
			String idsPaginati  = getIdsPaginati(idsList, numPagina, numRisPerPagina);
			
			//CommonUtil.println("query in(idsPaginati): "+idsPaginati);

			// query totale con la IN
//			List<SiacTMovgestFin> listaOggettiFinale = movimentoGestioneDao.ricercaSiacTMovgestPerIN(idsPaginati);
			
			List<SiacTMovgestTsFin> listaSubImp = movimentoGestioneDao.ricercaSiacTMovgestTsPerIN(idsPaginati);
			if(listaSubImp!= null && !listaSubImp.isEmpty()){
				//String strutturaCompotente = "";
				BigDecimal importoAttualePadre = BigDecimal.ZERO;
				//StrutturaAmministrativoContabile sacComponente = new StrutturaAmministrativoContabile();
				
				for (SiacTMovgestTsFin siacTMovgestts : listaSubImp) {
					SubImpegno subTrovato = map(siacTMovgestts, SubImpegno.class, FinMapId.SiacTMovgestTs_SubImpegno);
				
					//ANNOMOVIMENTO
					if(siacTMovgestts.getSiacTMovgest()!= null){
						subTrovato.setAnnoMovimento(siacTMovgestts.getSiacTMovgest().getMovgestAnno());
						
						subTrovato.setParereFinanziario(siacTMovgestts.getSiacTMovgest().getParereFinanziario());
						
						AttributoTClassInfoDto attributoInfo = new AttributoTClassInfoDto();
						attributoInfo.setSiacTMovgestTs(siacTMovgestts);
						attributoInfo.setTipoOggetto(OggettoDellAttributoTClass.T_MOVGEST_TS);
						Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
						String prenotazione = getValoreAttr(attributoInfo, datiOperazioneDto, idEnte, CostantiFin.T_ATTR_CODE_FLAG_PRENOTAZIONE);
						subTrovato.setFlagPrenotazione(CostantiFin.TRUE.equals(prenotazione));
						
						//CAPITOLO
						if(siacTMovgestts.getSiacTMovgest().getSiacRMovgestBilElems() != null &&
								!siacTMovgestts.getSiacTMovgest().getSiacRMovgestBilElems().isEmpty() ){
							
							List<SiacRMovgestBilElemFin> listaSiacRMovgestBilElem = siacTMovgestts.getSiacTMovgest().getSiacRMovgestBilElems();
								SiacRMovgestBilElemFin relazioneValida = DatiOperazioneUtil.getValido(listaSiacRMovgestBilElem, null);
								if(relazioneValida!= null && relazioneValida.getSiacTBilElem()!= null){
									SiacTBilElemFin siacTBilElem = relazioneValida.getSiacTBilElem();
									CapitoloUscitaGestione capUG = new CapitoloUscitaGestione();
									capUG.setUid(siacTBilElem.getUid());
									capUG.setAnnoCapitolo(Integer.parseInt(siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno()));
									capUG.setNumeroCapitolo(Integer.parseInt(siacTBilElem.getElemCode()));
									capUG.setNumeroArticolo(Integer.parseInt(siacTBilElem.getElemCode2()));
									capUG.setNumeroUEB(Integer.parseInt(siacTBilElem.getElemCode3()));
									capUG.setStrutturaAmministrativoContabile(mapStrutturaAmministrativaContabileCapitolo(siacTBilElem.getSiacRBilElemClasses()));
									subTrovato.setCapitoloUscitaGestione(capUG);
								}
								
								
								//SIAC-7349
								if(relazioneValida!= null && relazioneValida.getSiacDBilElemDetCompTipo()!= null){
									ComponenteBilancioImpegno cbi = new ComponenteBilancioImpegno();
									cbi.setUid(relazioneValida.getSiacDBilElemDetCompTipo().getUid());
									cbi.setDescrizioneTipoComponente(relazioneValida.getSiacDBilElemDetCompTipo().getElemDetCompTipoDesc());
									if(relazioneValida.getSiacDBilElemDetCompTipo().getSiacDBilElemDetCompMacroTipo()!= null){
										cbi.setDescrizioneMacroComponente(relazioneValida.getSiacDBilElemDetCompTipo().getSiacDBilElemDetCompMacroTipo().getElemDetCompMacroTipoDesc());
									}
									subTrovato.setComponenteBilancioImpegno(cbi);
								}
								
								
						}
						
						
						
						
					}
					//STATO
					if(siacTMovgestts.getSiacRMovgestTsStatos()!= null && !siacTMovgestts.getSiacRMovgestTsStatos().isEmpty() ){
						
						for(SiacRMovgestTsStatoFin statoFin : siacTMovgestts.getSiacRMovgestTsStatos()){
							if(statoFin.getDataCancellazione()== null && statoFin.getSiacDMovgestStato()!=null){
								subTrovato.setDescrizioneStatoOperativoMovimentoGestioneSpesa(statoFin.getSiacDMovgestStato().getMovgestStatoDesc());
								subTrovato.setStatoOperativoMovimentoGestioneSpesa(statoFin.getSiacDMovgestStato().getMovgestStatoCode());
								break;
							}
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
							soggettoValid = true;
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
							//SIAC-8592
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
					
					
					
					
					BigDecimal importoDisponibilitaPagare = BigDecimal.ZERO;
					
					//SE TESTATA O SUB
					if(siacTMovgestts.getSiacDMovgestTsTipo()!=null && siacTMovgestts.getSiacDMovgestTsTipo().getMovgestTsTipoCode()!= null
							&& siacTMovgestts.getSiacDMovgestTsTipo().getMovgestTsTipoCode().equals("T")){
						subTrovato.setNumeroImpegnoPadre(subTrovato.getNumeroBigDecimal());
						subTrovato.setNumeroBigDecimal(null);
						
						//STRUTTURA COMPETENTE
						subTrovato.setStrutturaCompetenteObj(getStrutturaAmmCont(siacTMovgestts));
						
						
						//DISPONIBILITA A PAGARE
						DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareImpegno(siacTMovgestts, subTrovato.getStatoOperativoMovimentoGestioneSpesa(), ente.getUid(), null,null);
						importoDisponibilitaPagare = (disponibilitaPagare != null) ? disponibilitaPagare.getDisponibilita() : importoDisponibilitaPagare;
						
						importoAttualePadre = subTrovato.getImportoAttuale();
						
					
					}else{
					
						//PRENDIAMO IL PADRE
						if(siacTMovgestts.getSiacTMovgest()!= null && siacTMovgestts.getSiacTMovgest().getSiacTMovgestTs()!= null
								&& !siacTMovgestts.getSiacTMovgest().getSiacTMovgestTs().isEmpty()){
							
							for(SiacTMovgestTsFin siacTmovgestTsFin:siacTMovgestts.getSiacTMovgest().getSiacTMovgestTs()){
								if(siacTmovgestTsFin.getMovgestTsIdPadre()==null){//PADRE
									subTrovato.setStrutturaCompetenteObj(getStrutturaAmmCont(siacTmovgestTsFin));
									break;
								}
							}
						}
						
						
						
						
						subTrovato.setNumeroImpegnoPadre(siacTMovgestts.getSiacTMovgest().getMovgestNumero());

						
						
						DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareSubImpegno(siacTMovgestts, subTrovato.getStatoOperativoMovimentoGestioneSpesa(), ente.getUid(), null,null);
						importoDisponibilitaPagare = (disponibilitaPagare != null) ? disponibilitaPagare.getDisponibilita() : importoDisponibilitaPagare;
						subTrovato.setImportoAttualePadre(importoAttualePadre);
					}
					
					
					int  annoEsercizio = paramRic.getAnnoEsercizio()!= null ? paramRic.getAnnoEsercizio().intValue() : 0;
					buildRiepilogoImportiRor( annoEsercizio, subTrovato,  siacTMovgestts,    importoDisponibilitaPagare, ente.getUid());
					listaSubImpegni.add(subTrovato);
					
				}
			}
		}

        return listaSubImpegni;
	}
	
	
	private void buildRiepilogoImportiRor(int  annoEsercizio, SubImpegno subTrovato, SiacTMovgestTsFin siacTMovgestts, BigDecimal importoDisponibilitaPagare, int enteUid){
		
		 BigDecimal importoDaRiaccertare =  subTrovato.getImportoAttuale();
		 BigDecimal modificheRor =  BigDecimal.ZERO;
		 BigDecimal importoGestitoInRo = BigDecimal.ZERO;
		 BigDecimal importoCancellareInsussistenza = BigDecimal.ZERO;
		 BigDecimal importoCancellareInesigibilita = BigDecimal.ZERO;
		 BigDecimal impertoReimpDifferitoN = BigDecimal.ZERO;
		 BigDecimal impertoReimpDifferitoN1 = BigDecimal.ZERO;
		 BigDecimal impertoReimpDifferitoN2 = BigDecimal.ZERO;
		 BigDecimal impertoReimpDifferitoNP = BigDecimal.ZERO;
		 
		
		
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
			List<ModificaMovimentoGestioneSpesa> listaModificheMovimentoGestioneSpesa = new ArrayList<ModificaMovimentoGestioneSpesa>();
			for(SiacTMovgestTsDetModFin modFin : siacTMovgestts.getSiacTMovgestTsDetMods()){
				
				
				if(modFin.getSiacRModificaStato()!= null && 
						//SIAC-8048
						modFin.getSiacRModificaStato().getDataCancellazione() == null && 
						modFin.getSiacRModificaStato().getSiacTModifica()!= null &&
						modFin.getSiacRModificaStato().getSiacTModifica().getDataCancellazione() == null &&
						modFin.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo()!= null &&
						modFin.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo().getModTipoCode()!= null
						&& modFin.getSiacRModificaStato().getSiacDModificaStato()!= null 
						&& !modFin.getSiacRModificaStato().getSiacDModificaStato().getModStatoCode().equals(CostantiFin.D_MODIFICA_STATO_ANNULLATO)){
					
					
					
					String modTipoCode = modFin.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo().getModTipoCode();
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
					ModificaMovimentoGestioneSpesa mmgs = map(modFin.getSiacRModificaStato().getSiacTModifica(), ModificaMovimentoGestioneSpesa.class, FinMapId.SiacTModifica_ModificaMovimentoGestioneSpesa);
					if(modFin.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo()!= null ){
						mmgs.setTipoModificaMovimentoGestione(modFin.getSiacRModificaStato().getSiacTModifica().getSiacDModificaTipo().getModTipoCode());
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
								break;
							}else{
								mmgs.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.ANNULLATO);
							}
							
						}
					}
					listaModificheMovimentoGestioneSpesa.add(mmgs);
				}
			}
			modificheRor = modificheRor.abs();
			importoDaRiaccertare = importoDaRiaccertare.add(modificheRor);
			subTrovato.setListaModificheMovimentoGestioneSpesa(listaModificheMovimentoGestioneSpesa);
		}
		BigDecimal pagato = BigDecimal.ZERO;
		if(subTrovato.getImportoAttuale()!= null ){
			pagato = subTrovato.getImportoAttuale().subtract(importoDisponibilitaPagare);
		}
		subTrovato.setImportoDaRiaccertare(importoDaRiaccertare.subtract(pagato));
		subTrovato.setImportoModifiche(importoGestitoInRo);
		subTrovato.setDaCancellareInsussistenza(importoCancellareInsussistenza);
		subTrovato.setDaCancellareInesigibilita(importoCancellareInesigibilita);
		subTrovato.setDifferitoN(impertoReimpDifferitoN);
		subTrovato.setDifferitoN1(impertoReimpDifferitoN1);
		subTrovato.setDifferitoN2(impertoReimpDifferitoN2);
		subTrovato.setDifferitoNP(impertoReimpDifferitoNP);
		
		
		BigDecimal documentiPassiviAnnoNSucc = BigDecimal.ZERO;	
		BigDecimal liquidazioniNSucc = BigDecimal.ZERO;
		
		BigDecimal documentiPassiviAnnoNSuccTOTSub = BigDecimal.ZERO;	
		BigDecimal liquidazioniNSuccTOTSub = BigDecimal.ZERO;
		
		
		/*Per lo stesso impegno dobbiamo trovare 
		 * liquidazioni e documento spesa per l anno successivo
		 * 
		 */
		int annoSuccessivo = annoEsercizio+1;
		
		
		 List<SiacTMovgestTsFin> siacTmovgestTsAnnoSuccessuvi =  movimentoGestioneDao.ricercaSiacTMovgestTsPerAnnoEsercizioUid( enteUid,
				 annoSuccessivo,  subTrovato.getNumeroImpegnoPadre(),  subTrovato.getAnnoMovimento(),CostantiFin.MOVGEST_TIPO_IMPEGNO);
		if(siacTmovgestTsAnnoSuccessuvi!= null && !siacTmovgestTsAnnoSuccessuvi.isEmpty()){
			
			SiacTMovgestTsFin siacTmovgestTsAnnoSucc = new SiacTMovgestTsFin();
			for(SiacTMovgestTsFin s :siacTmovgestTsAnnoSuccessuvi){
				BigDecimal documentiPassiviAnnoNSuccSub = BigDecimal.ZERO;	
				BigDecimal liquidazioniNSuccSub = BigDecimal.ZERO;
				
				//SIAC-7980 Inizio Per l'impegno padre mi prendo anche quelli dei sub
				if(s.getDataCancellazione()== null && siacTMovgestts.getMovgestTsIdPadre() == null && !siacTMovgestts.getMovgestTsCode().equals(s.getMovgestTsCode()) ){
					
					ConsultaDettaglioImpegnoDto datiConsultaSub = impegnoDao.consultaDettaglioImpegno(s.getMovgestTsId());
					if(datiConsultaSub!=null){
						if(datiConsultaSub.getTotImpLiq()!= null){
							RiepilogoImportoNumero rinTotImpLiqSub = new RiepilogoImportoNumero(datiConsultaSub.getTotImpLiq(),datiConsultaSub.getNumeroLiq());
							liquidazioniNSuccSub = (rinTotImpLiqSub!= null && rinTotImpLiqSub.getImporto()!= null) ? rinTotImpLiqSub.getImporto() : liquidazioniNSuccSub;
						}
						if(datiConsultaSub.getTotDocNonLiq()!= null){
							RiepilogoImportoNumero rinDocNonLiqSub = new RiepilogoImportoNumero(datiConsultaSub.getTotDocNonLiq(), datiConsultaSub.getNumeroDocNonLiq());
							documentiPassiviAnnoNSuccSub = (rinDocNonLiqSub!= null && rinDocNonLiqSub.getImporto()!= null) ? rinDocNonLiqSub.getImporto() : documentiPassiviAnnoNSuccSub;
						}
					}
					
					documentiPassiviAnnoNSuccTOTSub = documentiPassiviAnnoNSuccTOTSub.add(documentiPassiviAnnoNSuccSub);
					liquidazioniNSuccTOTSub = liquidazioniNSuccTOTSub.add(liquidazioniNSuccSub);
					
					
				}
				
				//SIAC-7980 fine 
				
				if(s.getDataCancellazione()== null){
					//SIAC-7980 prendo l'info del sub.
					if (siacTMovgestts.getMovgestTsCode().equals(s.getMovgestTsCode())) {
						siacTmovgestTsAnnoSucc = s;
					
//					if (s.getMovgestTsIdPadre()!=null) {
//						break;
//					}
						
					}
				}
			}
			
			
			
			/*
			 * SIAC-7599
			 */
			if(siacTmovgestTsAnnoSucc.getMovgestTsId()!= null){
				ConsultaDettaglioImpegnoDto datiConsulta = impegnoDao.consultaDettaglioImpegno(siacTmovgestTsAnnoSucc.getMovgestTsId());
				if(datiConsulta!=null){
					if(datiConsulta.getTotImpLiq()!= null){
						RiepilogoImportoNumero rinTotImpLiq = new RiepilogoImportoNumero(datiConsulta.getTotImpLiq(),datiConsulta.getNumeroLiq());
						liquidazioniNSucc = (rinTotImpLiq!= null && rinTotImpLiq.getImporto()!= null) ? rinTotImpLiq.getImporto() : liquidazioniNSucc;
					}
					if(datiConsulta.getTotDocNonLiq()!= null){
						RiepilogoImportoNumero rinDocNonLiq = new RiepilogoImportoNumero(datiConsulta.getTotDocNonLiq(), datiConsulta.getNumeroDocNonLiq());
						documentiPassiviAnnoNSucc = (rinDocNonLiq!= null && rinDocNonLiq.getImporto()!= null) ? rinDocNonLiq.getImporto() : documentiPassiviAnnoNSucc;
					}
				}
			}
					
					
					
		}
		//SIAC-7980 Inizio Per l'impegno padre mi prendo anche quelli dei sub
		 if (siacTMovgestts.getMovgestTsIdPadre() == null ){
			 liquidazioniNSucc =liquidazioniNSucc.add(liquidazioniNSuccTOTSub);
			 documentiPassiviAnnoNSucc = documentiPassiviAnnoNSucc.add(documentiPassiviAnnoNSuccTOTSub);
		}
		//SIAC-7980 Fine
		
		 
		BigDecimal sommatorieFattureLiqNSuccessivi = liquidazioniNSucc.add(documentiPassiviAnnoNSucc);
		
		BigDecimal importoMaxDaRiaccertare = BigDecimal.ZERO;
		BigDecimal residuoEventualeDaMantenere = BigDecimal.ZERO;

		importoMaxDaRiaccertare = subTrovato.getImportoDaRiaccertare().subtract(sommatorieFattureLiqNSuccessivi);
		if(subTrovato.getImportoAttuale()!= null){
			//SIC-7599
//			residuoEventualeDaMantenere = subTrovato.getImportoAttuale().subtract(sommatorieFattureLiqNSuccessivi);
//			residuoEventualeDaMantenere = residuoEventualeDaMantenere.subtract(pagato); //MODIFICA PTR
			//IMPORTO- PAGATO (dovrebbe esssere lo stesso di importo da riaccertare - importo modifiche)
			//SIC-7599
			residuoEventualeDaMantenere = subTrovato.getImportoAttuale().subtract(pagato);
			
			
		}
		subTrovato.setImportoMaxDaRiaccertare(importoMaxDaRiaccertare);
		subTrovato.setResiduoDaMantenere(residuoEventualeDaMantenere); 
		
		
		//SIAC-7599
		subTrovato.setLiquidatoAnnoSuccessivo(liquidazioniNSucc);
		subTrovato.setDocumentiNoLiqAnnoSuccessivo(documentiPassiviAnnoNSucc);
		
		
	}
	
	
	/*SIAC-6997
	 * ANNO ESERCIO 
	 * NUMERO IMP
	 * ANNO IMP
	 * NUMERO SUB 
	 */
	public SubImpegno campiRiepilogoROR(String annoEsercizio, Integer movgestTsNumber, int enteUid){
		final String methodName="campiRiepilogoROR";
		SubImpegno subTrovato = new SubImpegno();
		String idsPaginati = movgestTsNumber.toString();
		
		List<SiacTMovgestTsFin> listaSubImp = movimentoGestioneDao.ricercaSiacTMovgestTsPerIN(idsPaginati);
		
		
		if(listaSubImp!= null && !listaSubImp.isEmpty()){
			SiacTMovgestTsFin siacTMovgestts = listaSubImp.get(0);
				
			if(siacTMovgestts!= null){
				subTrovato = map(siacTMovgestts, SubImpegno.class, FinMapId.SiacTMovgestTs_SubImpegno);
				
				//ANNO MOVIMENTO E IMPEGNO PADRE
				if(siacTMovgestts.getSiacTMovgest()!=null){
					subTrovato.setNumeroImpegnoPadre(siacTMovgestts.getSiacTMovgest().getMovgestNumero());
					if(siacTMovgestts.getSiacTMovgest().getMovgestAnno()!= null){
						subTrovato.setAnnoMovimento(siacTMovgestts.getSiacTMovgest().getMovgestAnno().intValue());
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
					
					
					
					//DISPONIBILITA A PAGARE
					BigDecimal importoDisponibilitaPagare = BigDecimal.ZERO;
					if(siacTMovgestts.getSiacDMovgestTsTipo()!=null && siacTMovgestts.getSiacDMovgestTsTipo().getMovgestTsTipoCode()!= null
							&& siacTMovgestts.getSiacDMovgestTsTipo().getMovgestTsTipoCode().equals("T")){
						DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareImpegno(siacTMovgestts, subTrovato.getStatoOperativoMovimentoGestioneSpesa(), enteUid, null,null);
						importoDisponibilitaPagare = (disponibilitaPagare != null) ? disponibilitaPagare.getDisponibilita() : importoDisponibilitaPagare;
					}else{
						DisponibilitaMovimentoGestioneContainer disponibilitaPagare = calcolaDisponibilitaAPagareSubImpegno(siacTMovgestts, subTrovato.getStatoOperativoMovimentoGestioneSpesa(), enteUid, null,null);
						importoDisponibilitaPagare = (disponibilitaPagare != null) ? disponibilitaPagare.getDisponibilita() : importoDisponibilitaPagare;
					}
					//SIAC-7709
					if(StringUtils.isNotBlank(annoEsercizio)){
						try{
							int annoEsercizioInt = Integer.parseInt(annoEsercizio);
							buildRiepilogoImportiRor(annoEsercizioInt, subTrovato,  siacTMovgestts,   importoDisponibilitaPagare, enteUid);
						}catch(Exception e){
							log.error(methodName, "Errore durante buildRiepilogoROR: " + e.getMessage());
						}
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


	/**
	 * Collega impegno aggiudicazione con prenotazione modifica.
	 *
	 * @param impegnoAggiudicazione the imp aggiudicazione
	 * @param impegnoPrenotazione the impegno prenotazione
	 * @param mods the mods
	 * @param datiOperazioneDto 
	 * @param bilancio 
	 */
	public void collegaImpegnoAggiudicazioneConPrenotazioneModifica(Impegno impegnoAggiudicazione, 
			Impegno impegnoPrenotazione,	ModificaMovimentoGestioneSpesa mods, DatiOperazioneDto datiOperazioneDto, Bilancio bilancio) {
		final String methodName="collegaImpegnoAggiudicazioneConPrenotazioneModifica";		
		StringBuilder operazioneDaEseguire = new StringBuilder().append("Collego impegno aggiudicazione ")
				.append(impegnoAggiudicazione != null? impegnoAggiudicazione.getAnnoMovimento() : "null")
				.append("/")
				.append(impegnoAggiudicazione != null? impegnoAggiudicazione.getNumeroBigDecimal() : "null")
				.append("[uid: ")
				.append(impegnoAggiudicazione != null? impegnoAggiudicazione.getUid() : "null")
				.append(" ] all'impegno prenotazione ")
				.append(impegnoPrenotazione != null? impegnoPrenotazione.getAnnoMovimento() : "null")
				.append("/")
				.append(impegnoPrenotazione != null? impegnoPrenotazione.getNumeroBigDecimal() : "null")
				.append("[uid: ")
				.append(impegnoPrenotazione != null? impegnoPrenotazione.getUid() : "null")
				.append(" . La modifica sull'impegno di prenotazione da cui e' nato l'impegno di aggiudicazione e' la modifica mnumero ")
				.append(mods.getNumeroModificaMovimentoGestione())
				.append("[uid: ")
				.append(mods.getUid())
				.append(" ] all'impegno prenotazione ")
				;
		log.debug(methodName, operazioneDaEseguire);
		if(impegnoAggiudicazione == null || impegnoAggiudicazione.getUid() == 0) {
			return;
		}
		
		Date now = new Date();
		
		SiacRMovgestAggiudicazioneFin siacRMovgestAggiudicazione = new SiacRMovgestAggiudicazioneFin();
		
		SiacTMovgestFin movgestFinDa = new SiacTMovgestFin();
		movgestFinDa.setUid(impegnoPrenotazione.getUid());
		siacRMovgestAggiudicazione.setSiacTMovgestDa(movgestFinDa);
		
		SiacTMovgestFin movgestFinA = siacTMovgestRepository.findOne(impegnoAggiudicazione.getUid());
		siacRMovgestAggiudicazione.setSiacTMovgestA(movgestFinA);
		
		SiacTAttoAmmFin siacTAttoAmm = new SiacTAttoAmmFin();
		//SIAC-7781 si passa l'uid del provvedimento dell'impegno della prenotazione
		siacTAttoAmm.setUid(impegnoPrenotazione.getAttoAmministrativo().getUid());
		siacRMovgestAggiudicazione.setSiacTAttoAmm(siacTAttoAmm);
		
		SiacTModificaFin siacTModifica = new SiacTModificaFin();
		siacTModifica.setUid(mods.getUid());
		
		siacRMovgestAggiudicazione.setSiacTModifica(siacTModifica);
		
		siacRMovgestAggiudicazione.setDataModificaInserimento(now);
		siacRMovgestAggiudicazione.setLoginOperazione(datiOperazioneDto.getAccountCode());
		siacRMovgestAggiudicazione.setSiacTEnteProprietario(datiOperazioneDto.getSiacTEnteProprietario());
		

		/*
		SiacTMovgestFin movgestFinDa = siacTMovgestRepository.findOne(impegnoPrenotazione.getUid());
		siacRMovgestAggiudicazione.setSiacTMovgestDa(movgestFinDa);
		
		SiacTMovgestFin movgestFinA = siacTMovgestRepository.findOne(impAggiudicazione.getUid());
		siacRMovgestAggiudicazione.setSiacTMovgestA(movgestFinA);
		
		SiacTAttoAmmFin siacTAttoAmm = siacTAttoAmmRepository.findOne(mods.getAttoAmministrativo().getUid());
		siacRMovgestAggiudicazione.setSiacTAttoAmm(siacTAttoAmm);
		
		SiacTModificaFin siacTModifica = siacTModificaRepository.findOne(mods.getUid());
		siacRMovgestAggiudicazione.setSiacTModifica(siacTModifica);
		*/
		siacRMovgestAggiudicazioneRepository.saveAndFlush(siacRMovgestAggiudicazione);
		
		//SIAC-7779
		collegaAggiudicazioneAVincoli(impegnoAggiudicazione, impegnoPrenotazione, datiOperazioneDto, movgestFinA);
		
		//SIAC-8794
		ribaltaStoricoImpegnoAccertamentoAggiudicazioneConPrenotazione(impegnoPrenotazione, impegnoAggiudicazione, bilancio, datiOperazioneDto, ente);
		
	}
	
	//SIAC-7779
	protected void collegaAggiudicazioneAVincoli(Impegno impAggiudicazione, Impegno impegnoPrenotazione, DatiOperazioneDto datiOperazioneDto, SiacTMovgestFin movgestFinA) {
		SiacTMovgestTsFin movgestTsFinDa = null;

		//devo lavorare con la testata in quanto e' lei a tenere il vincolo
		List<SiacTMovgestTsFin> listMovgest = siacTMovgestRepository.findOne(impegnoPrenotazione.getUid()).getSiacTMovgestTs();
		for (SiacTMovgestTsFin movimento : listMovgest) {
			if("T".equals(movimento.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) {
				movgestTsFinDa = movimento ;				
			}
		}
		
		//cerco i vincoli legati alla testata
		List<SiacRMovgestTsFin> siacRMovgestTsFin = 
				siacRMovgestTsRepository.findVincoliByImpegno(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(), 
						movgestTsFinDa != null ? movgestTsFinDa.getMovgestTsId() : new Integer(0));
		
		List<VincoloImpegno> listaVincoli = new ArrayList<VincoloImpegno>();
		
		//creo i vincoliImpegno per riutilizzare il metodo 'salvaVincoloImpegno'
		for (SiacRMovgestTsFin vincolo : CoreUtil.checkList(siacRMovgestTsFin)) {
			VincoloImpegno mapped = map(vincolo, VincoloImpegno.class, FinMapId.SiacRMovgestTsFin_VincoloImpegno);
			if(vincolo.isEntitaValida() && !listaVincoli.contains(mapped)) {
				listaVincoli.add(mapped);
			}
		}
		
		BigDecimal imp = impAggiudicazione.getImportoAttuale();
		
		for (VincoloImpegno vincolo : listaVincoli) {
		
			//se l'importo di partenza e' ZERO chiudo interrompendo la creazione di altri vincoli
			if(imp.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}
			
			//controllo che l'importo dell'aggiudicazione sia maggiore o uguale all'importo del vincolo 
			if(imp.compareTo(vincolo.getImporto()) >= 0){
				//passo uid 0 perche' voglio creare un nuovo vincolo
				vincolo.setUid(0);
				salvaVincoloImpegno(vincolo, datiOperazioneDto, movgestFinA.getSiacTMovgestTs().get(0));
				//una volta vincolata la cifra ne deduco l'importo di partenza
				imp = imp.subtract(vincolo.getImporto());
			} else {
				//se l'importo e' minore passo l'importo rimanente per completare l'ultimo vincolo
				vincolo.setImporto(imp);
				//passo uid 0 perche' voglio creare un nuovo vincolo
				vincolo.setUid(0);
				salvaVincoloImpegno(vincolo, datiOperazioneDto, movgestFinA.getSiacTMovgestTs().get(0));
				//setto l'importo a 0
				imp = BigDecimal.ZERO;
			}

		}
		
	}
	
	
	public OttimizzazioneModificheMovimentoGestioneDto caricaOttimizzazioneModificheMovimentoGestioneDto(List<SiacTMovgestTsFin> listaSiacTMovgestTsCoinvolti, Integer movgestId){
		OttimizzazioneModificheMovimentoGestioneDto ottimizzazione = caricaOttimizzazioneModificheMovimentoGestioneDto(listaSiacTMovgestTsCoinvolti);

		List<Integer> uidsModPreEsistenti = siacTModificaRepository.findListModIdImportoByMovgestId(movgestId);
		ottimizzazione.setUidsModificheImportoPreEsistentiValide(uidsModPreEsistenti);
		
		return ottimizzazione;
		
	}
	
	
	@Override
	protected void buildImpegnoPrenotazioneOrigine(Impegno trovatoMovGestione, SiacTMovgestFin siacTMovgest) {
		List<SiacTMovgestFin> agg = siacRMovgestAggiudicazioneRepository.findSiacRMovgestDaByMovgestTsA(trovatoMovGestione.getUid());
		if(agg == null || agg.isEmpty() || agg.get(0) == null) {
			return;
		}
		SiacTMovgestFin stmta = agg.get(0);
		Impegno impPrenotazione = new Impegno();
		impPrenotazione.setUid(stmta.getUid());
		impPrenotazione.setAnnoMovimento(stmta.getMovgestAnno());
		impPrenotazione.setNumeroBigDecimal(stmta.getMovgestNumero());
		impPrenotazione.setDescrizione(stmta.getMovgestDesc());
		trovatoMovGestione.setImpegnoPrenotazioneOrigine(impPrenotazione);
	}
	
	@Override
	protected void buildAttoAmministrativoPrenotazione(Impegno trovatoMovGestione, SiacTMovgestFin siacTMovgest) {
		List<SiacTAttoAmmFin> agg = siacRMovgestAggiudicazioneRepository.findSiacSiacTAttoAmmByMovgestTsA(trovatoMovGestione.getUid());
		if(agg == null || agg.isEmpty() || agg.get(0) == null || agg.get(0).getUid() == 0) {
			return;
		}
		SiacTAttoAmmFin siacTAttoAmm = agg.get(0);
		AttoAmministrativo atto = new AttoAmministrativo();
		//TIPO ATTO:
		TipoAtto tipoAtto = new TipoAtto();
		tipoAtto.setDescrizione(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoDesc());
		tipoAtto.setCodice(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoCode());
		tipoAtto.setUid(siacTAttoAmm.getSiacDAttoAmmTipo().getUid());
		//DATI ATTO:
		atto.setUid(siacTAttoAmm.getUid());
		atto.setAnno(new Integer(siacTAttoAmm.getAttoammAnno()));
		atto.setNumero(siacTAttoAmm.getAttoammNumero());
		atto.setTipoAtto(tipoAtto);
		
		atto.setOggetto(siacTAttoAmm.getAttoammOggetto());
		
		//EVENTUALE STRUTTURA AMMINISTRATIVA:
		if(siacTAttoAmm!=null && siacTAttoAmm.getSiacRAttoAmmClasses()!=null){
			SiacRAttoAmmClassFin legameValido = DatiOperazioneUtil.getValido(siacTAttoAmm.getSiacRAttoAmmClasses(), null);
			if(legameValido!=null && legameValido.getSiacTClass()!=null){
				StrutturaAmministrativoContabile struttura = new StrutturaAmministrativoContabile();
				struttura.setUid(legameValido.getSiacTClass().getUid());
				struttura.setCodice(legameValido.getSiacTClass().getClassifCode());
				struttura.setDescrizione(legameValido.getSiacTClass().getClassifDesc());
				atto.setStrutturaAmmContabile(struttura);
			}
		}
		for (SiacRAttoAmmStatoFin siacRAttoAmmStato : siacTAttoAmm.getSiacRAttoAmmStatos()) {
			if(siacRAttoAmmStato.getDataCancellazione()==null){
				SiacDAttoAmmStatoFin siacDAttoAmmStato = siacRAttoAmmStato.getSiacDAttoAmmStato();
				atto.setStatoOperativo(siacDAttoAmmStato.getAttoammStatoCode());
				break;
			}
		}
		
		trovatoMovGestione.setAttoAmministrativoAggiudicazione(atto);
	}
	
	@Override
	protected void caricaHasModificheAggiudicazione(Impegno impegnoTrovato) {
		List<Integer> uids = siacRMovgestAggiudicazioneRepository.getUidsModificheAggiudicazioneValideCollegate(impegnoTrovato.getUid());
		impegnoTrovato.setHasModificheAggiudicazione(uids != null && !uids.isEmpty());
	}
	

	
	public Errore checkPdcImpegnoCoerenteConPdcCapitolo(int idEnte, int idCapitolo, String codPdc) {

		ElementoPianoDeiConti pdcCapitolo = capitoloDad.findPianoDeiContiCapitolo(idCapitolo);
		
//		String descEntitaMancante = pdcCapitolo == null? "Piano dei conti del capitolo" : (" Piano dei conti " + StringUtils.defaultIfEmpty(codPdc, "N.D.") + " del movimento");
//		Errore errore = 
//				ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile verificare la congruenza tra piano dei conti del capitolo e del movimento." 
//				+ descEntitaMancante + " non trovato");
//
//		if (pdcCapitolo == null) {
//			esito.addErrore(errore);
//			return false;
//		}
		
		SiacTClassFin cl = CollectionUtil.getFirst(siacTClassRepository.findByTipoCodesAndEnteAndSelezionato(idEnte, CostantiFin.getCodiciPianoDeiConti(), codPdc));

		if(cl == null) {
//			esito.addErrore(errore);
			return null;
		}
		
		if(cl.getUid() == pdcCapitolo.getUid()) {
			return null;
		}
		
		SiacTClassFin clPadre = siacTClassRepository.findPadreClassificatoreByClassifId(cl.getUid());
		
		if(clPadre == null || clPadre.getUid() != pdcCapitolo.getUid()) {
			return ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore( "il piano dei conti associato al movimento ["
					+ codPdc + " ]  o il suo padre [ " + (clPadre != null? StringUtils.defaultIfEmpty(clPadre.getClassifCode(), "N.D.") : "null") + " ] risulta essere diverso da quello del capitolo [ " + pdcCapitolo.getCodice() + " ].");
		}
		
		return null;
	}


}