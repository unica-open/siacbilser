/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dad.CodiciMotiviModifiche;
import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.CodificaImportoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ConsultaDettaglioImpegnoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaImpSubParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaImpegnoParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno;
import it.csi.siac.siacfinser.integration.entity.ext.IdMovgestSubmovegest;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtil;

@Component
@Transactional
public class ImpegnoDaoImpl extends AbstractDao<SiacTMovgestFin, Integer> implements ImpegnoDao{

	
	/**
	 * E' il metodo di "engine" di ricerca degli impegni.
	 * Utilizzato sia per avere un'anteprima del numero di risultati attesi (rispetto al filtro indicato)
	 * sia per recuperare tutti i dati (rispetto al filtro indicato)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTMovgestFin> ricercaImpegni(Integer enteUid, RicercaImpegnoParamDto prs) {
		
		List<SiacTMovgestFin> listaImpegni = new ArrayList<SiacTMovgestFin>();
		
		Map<String,Object> param = new HashMap<String, Object>();
		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
		
		//PARAMETRI DI INPUT
		Integer annoBilancio = prs.getAnnoBilancio();
		Integer annoEsercizio = prs.getAnnoEsercizio(); //Bilancio.anno
		String elementoPianoDeiConti = prs.getElementoPianoDeiConti(); //Conti.codice
		Integer numeroImpegno = prs.getNumeroImpegno(); //Impegno.numero
		String stato = prs.getStato(); //StatoOperativoMovimentoGestione.stato
		String cig = prs.getCig(); //Impegno.cig
		String cup = prs.getCup(); //Impegno.cup
		String progetto = prs.getProgetto(); //CodiceProgetto.codice
		String flagDaRiaccerto = prs.getFlagDaRiaccertamento(); //Impegno.flagDaRiaccentramento
		String flagDaReanno = prs.getFlagDaReanno(); //Impegno.flagDaRiaccentramento
		Integer idCapitoloUscita = prs.getUidCapitolo();
		Integer idProvvedimento = prs.getUidProvvedimento();
		String codiceCreditore = prs.getCodiceCreditore(); // Soggetto.codice
		boolean competenzaCorrente = prs.isCompetenzaCorrente(); 
		boolean competenzaCompetenza = prs.isCompetenzaCompetenza();
		boolean competenzaFuturi = prs.isCompetenzaFuturi();
		Integer annoImpegnoRiaccertato = prs.getAnnoImpegnoRiaccertato(); //Impegno.annoRiaccertato
		Integer annoImpegnoOrigine = prs.getAnnoImpegnoOrigine(); //Impegno.annoOrigine
		Integer numeroImpegnoOrigine = prs.getNumeroImpegnoOrigine();//Impegno.numeroOrigine
		Integer numeroImpegnoRiaccertato = prs.getNumeroImpegnoRiaccertato(); //Impegno.numeroRiaccertato
		String codiceClasseSoggetto = prs.getCodiceClasseSoggetto();
		String pdc = prs.getElementoPianoDeiConti(); //PDC Finanziario
		
		Integer annoProvvedimento = prs.getAnnoProvvedimento();
		Integer numeroProvvedimento = prs.getNumeroProvvedimento();
		String idTipoProvvedimento = prs.getTipoProvvedimento();
		
		Integer numeroCapitolo = prs.getNumeroCapitolo();
		Integer numeroArticolo = prs.getNumeroArticolo();
		Integer numeroUeb = prs.getNumeroUEB();
		
		// usato solo da ricerca impegni e subimpegni
		String tipoImpegno = prs.getTipoImpegno();
		
		String uidStrutturaAmmProvvedimento = prs.getStrutturaAmministrativoContabileDelProvvedimento();
			
		//Creo La query per il modulo FIN
		StringBuilder jpql = new StringBuilder("SELECT DISTINCT mvg FROM SiacTMovgestFin mvg " +
				//"left join mvg.siacTMovgestTs movgestTs " +
				", IN(mvg.siacTMovgestTs) movgestTs ");
				//" left join movgestTs.siacDMovgestTsTipo dMovgestTsTipo " +
				//" left join mvg.siacDMovgestTipo dMovgestTipo ");
				
		
		//Inserisco la join del Capitolo
		jpql.append(" left join mvg.siacRMovgestBilElems rBilElem left join rBilElem.siacTBilElem tBilElem ");
		
		//Inserisco la join del provvedimento
		jpql.append(" left join movgestTs.siacRMovgestTsAttoAmms rTsAttoAmm left join rTsAttoAmm.siacTAttoAmm tAttoAmm");
		
		// struttura amministrativa
		if(!StringUtilsFin.isEmpty(uidStrutturaAmmProvvedimento)){
			jpql.append(" left join tAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
		}
		
		if(!StringUtilsFin.isEmpty(idTipoProvvedimento)){
			jpql.append(" left join tAttoAmm.siacDAttoAmmTipo dTipoAtto ");
		}
		
		
		
		if(!StringUtilsFin.isEmpty(stato)){
			jpql.append(" left join movgestTs.siacRMovgestTsStatos rMovgestTsStato left join rMovgestTsStato.siacDMovgestStato dMovgestStato ");
		}
		
		if(!StringUtilsFin.isEmpty(tipoImpegno)){
			jpql.append(" left join movgestTs.siacRMovgestClasses rMovgestClass left join rMovgestClass.siacTClass tClass ");
		}
		
		/**
		 * TODO ATTRIBUTI JOIN
		 */
		
		//soggetto
		if(!StringUtilsFin.isEmpty(codiceCreditore)){
			jpql.append(" left join movgestTs.siacRMovgestTsSogs rSogg left join rSogg.siacTSoggetto sogg ");
		}
		
		//classe soggetto
		if(!StringUtilsFin.isEmpty(codiceClasseSoggetto)){
			jpql.append(" left join movgestTs.siacRMovgestTsSogclasses rClass left join rClass.siacDSoggettoClasse classe ");
		}
		
		if(!StringUtilsFin.isEmpty(elementoPianoDeiConti)){
			jpql.append(" left join movgestTs.siacRMovgestClasses rPiano left join rPiano.siacTClass piano ");
		}
		
		//Cig
		if(!StringUtilsFin.isEmpty(cig)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cigRAttr left join cigRAttr.siacTAttr cigAtt ");
		}
		
		//Cup
		if(!StringUtilsFin.isEmpty(cup)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cupRAttr left join cupRAttr.siacTAttr cupAtt ");
		}
		
		if(!StringUtilsFin.isEmpty(pdc)){
			jpql.append(" left join movgestTs.siacRMovgestClasses rMovgestPdcClass left join rMovgestPdcClass.siacTClass tPdcClass ");
		}
		
		//Riaccertato //SIAC-6997 aggiunto da reanno
		if ((!StringUtilsFin.isEmpty(flagDaRiaccerto) && flagDaRiaccerto.equalsIgnoreCase("Si"))  || (!StringUtilsFin.isEmpty(flagDaReanno) && flagDaReanno.equalsIgnoreCase("Si"))) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs flagRAttr left join flagRAttr.siacTAttr flagAtt ");
		}

		//Numero e Anno Riaccertato
		if((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0) && (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs annoRiacRAttr left join annoRiacRAttr.siacTAttr annoRiacAtt ");
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs numRiacRAttr left join numRiacRAttr.siacTAttr numRiacAtt ");
		}
		
		//Numero e Anno Origine
		if((annoImpegnoOrigine != null && annoImpegnoOrigine != 0) && (numeroImpegnoOrigine != null && numeroImpegnoOrigine != 0)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs annoOriRAttr left join annoOriRAttr.siacTAttr annoOriAtt ");
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs numOriRAttr left join numOriRAttr.siacTAttr numOriAtt ");
		}
		
		//Progetto
		if(!StringUtilsFin.isEmpty(progetto)){
			jpql.append(" left join movgestTs.siacRMovgestTsProgrammas rProgrammas left join rProgrammas.siacTProgramma tProgramma ");
		}
		
		/**
		 * TODO WHERE CLAUSE
		 */
		
		jpql.append(" WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mvg.siacDMovgestTipo.movgestTipoCode = 'I' AND " +
				" (movgestTs.siacDMovgestTsTipo.movgestTsTipoCode = 'T' or movgestTs.siacDMovgestTsTipo.movgestTsTipoCode='S')" );
		
		param.put("enteProprietarioId", enteUid);
		
		jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("mvg"));
		
		//Ricerca Soggetto
		if(!StringUtilsFin.isEmpty(codiceCreditore)){
			jpql.append(" AND sogg.soggettoCode = :codiceSoggetto ");
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rSogg"));
			param.put("codiceSoggetto", codiceCreditore);
		}
		
		if(!StringUtilsFin.isEmpty(codiceClasseSoggetto)){
			jpql.append(" AND classe.soggettoClasseCode = :codiceClasse ");				
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rClass"));
			param.put("codiceClasse", codiceClasseSoggetto);
		}
		
		if(!StringUtilsFin.isEmpty(elementoPianoDeiConti)){
			jpql.append(" AND piano.classifCode = :codicePiano");
			param.put("codicePiano", elementoPianoDeiConti);
		}
		
		//Ricerca Stato
		if(!StringUtilsFin.isEmpty(stato)){
			jpql.append(" AND dMovgestStato.movgestStatoCode = :stato ");
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rMovgestTsStato"));
			param.put("stato", stato.toUpperCase());
			
		}
		
		
		//Tipo Impegno jpql.append(" left join movgestTs.siacRMovgestClass rMovgestClass left join rMovgestClass.siacDClassTipo dMovgestClassTipo ");
		if(!StringUtilsFin.isEmpty(tipoImpegno)){
			jpql.append(" AND tClass.classifCode = :tipoImpegno ");
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rMovgestClass"));
			param.put("tipoImpegno", tipoImpegno.toUpperCase());
			
		}
		
		//Ricerca Numero Impegno
		if(numeroImpegno != null && numeroImpegno != 0){
			jpql.append(" AND mvg.movgestNumero = :numeroImpegno ");
			param.put("numeroImpegno", BigDecimal.valueOf(numeroImpegno));
		}
		
		// Aggiungo condizione anno di bilancio
		jpql.append(" AND mvg.siacTBil.siacTPeriodo.anno = :annoBilancioSelezionato ");
		param.put("annoBilancioSelezionato", annoBilancio.toString());
		
		//Ricerca anno impegno
		if(annoEsercizio != null && annoEsercizio != 0){
			jpql.append(" AND mvg.movgestAnno = :annoImpegno ");
			param.put("annoImpegno",annoEsercizio);
		}
		
		//Impegni Correnti
		if(competenzaCorrente){
			jpql.append(" AND mvg.movgestAnno = :annoBilancio ");
			param.put("annoBilancio", annoBilancio);
		}
		
		//Impegni Passati
		if(competenzaCompetenza){
			jpql.append(" AND mvg.movgestAnno < :annoBilancio ");
			param.put("annoBilancio", annoBilancio);
		}
		
		//Impegni Futuri
		if(competenzaFuturi){
			jpql.append(" AND mvg.movgestAnno > :annoBilancio ");
			param.put("annoBilancio", annoBilancio);
		}
		
		//Capitolo	//jira 1337 ricerca capitolo con 0
		if(idCapitoloUscita != null && idCapitoloUscita != 0){			
			jpql.append(" AND tBilElem.elemId = :capitoloId");
			param.put("capitoloId", idCapitoloUscita);
		} else {
			
			if(numeroCapitolo != null){
				jpql.append(" AND tBilElem.elemCode = :numeroCapitolo ");
				param.put("numeroCapitolo", String.valueOf(numeroCapitolo));
			}
			
			if(numeroArticolo != null){
				jpql.append(" AND tBilElem.elemCode2 = :numeroArticolo ");
				param.put("numeroArticolo", String.valueOf(numeroArticolo));
			}
			
			if(numeroUeb != null){
				jpql.append(" AND tBilElem.elemCode3 = :numeroUeb ");
				param.put("numeroUeb", String.valueOf(numeroUeb));
			}
			
		}
		
		//Provvedimento
		if(idProvvedimento != null && idProvvedimento != 0){
			jpql.append(" AND tAttoAmm.attoammId = :provvedimentoId ");
			param.put("provvedimentoId", idProvvedimento);
		} else {
			
			if(annoProvvedimento != null && annoProvvedimento != 0){
				jpql.append(" AND tAttoAmm.attoammAnno = :annoProvvedimento ");
				param.put("annoProvvedimento", String.valueOf(annoProvvedimento));
			}
			
			if(numeroProvvedimento != null && numeroProvvedimento != 0){
				jpql.append(" AND tAttoAmm.attoammNumero = :numeroProvvedimento ");
				param.put("numeroProvvedimento", numeroProvvedimento);
			}
			
			if(!StringUtilsFin.isEmpty(idTipoProvvedimento)){
				jpql.append(" AND dTipoAtto.attoammTipoId = :idTipoProvvedimento");
				param.put("idTipoProvvedimento", Integer.valueOf(idTipoProvvedimento));
			}
			
			//aggiunto filtro per sac del provevdimento
			if(!StringUtilsFin.isEmpty(uidStrutturaAmmProvvedimento)){
				jpql.append(" AND tClassAtto.classifId = :uidStrutturaAmmProvvedimento ");
				param.put("uidStrutturaAmmProvvedimento", Integer.valueOf(uidStrutturaAmmProvvedimento));
			}
			
		}
		
		/**
		 * TODO ATTRIBUTI WHERE CLAUSE
		 */
		
		//Cig
		if(!StringUtilsFin.isEmpty(cig)){
			String clausolaCup = buildClausolaRicercaAttr(CostantiFin.T_ATTR_CODE_CIG, cig, "cigAtt", "cigRAttr", "cigLike", param);
			jpql.append(clausolaCup);
		}
		
		//Cup
		if(!StringUtilsFin.isEmpty(cup)){
			String clausolaCup = buildClausolaRicercaAttr(CostantiFin.T_ATTR_CODE_CUP, cup, "cupAtt", "cupRAttr", "cupLike", param);
			jpql.append(clausolaCup);
		}
		 
		
		//PDC Finanziario
		if(!StringUtilsFin.isEmpty(pdc)){
			String clausolaPDC = buildClausolaRicercaTClass(CostantiFin.getCodiciPianoDeiConti(), pdc, "tPdcClass", "rMovgestPdcClass", "pdcLike", param);
			jpql.append(clausolaPDC);
		}
		
		//Riaccertato
		if( (!StringUtilsFin.isEmpty(flagDaRiaccerto) && flagDaRiaccerto.equalsIgnoreCase("Si"))){
			jpql.append(" AND  flagAtt.attrCode = 'flagDaRiaccertamento' AND UPPER(flagRAttr.boolean_) = 'S' ");
		}

		//SIAC-6997 Reanno
		if( (!StringUtilsFin.isEmpty(flagDaReanno) && flagDaReanno.equalsIgnoreCase("Si"))){
				jpql.append(" AND  flagAtt.attrCode = 'flagDaReanno' AND UPPER(flagRAttr.boolean_) = 'S' ");
		}

		//Numero e Anno Riaccertato
		if((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0) && (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0) ){
			jpql.append(" AND  annoRiacAtt.attrCode = 'annoRiaccertato' AND annoRiacRAttr.testo = :annoRiacc");
			jpql.append(" AND  numRiacAtt.attrCode = 'numeroRiaccertato' AND numRiacRAttr.testo = :numeroRiacc");
			param.put("annoRiacc", String.valueOf(annoImpegnoRiaccertato));
			param.put("numeroRiacc", String.valueOf(numeroImpegnoRiaccertato));
		}
		
		//Numero e Anno Origine
		if((annoImpegnoOrigine != null && annoImpegnoOrigine != 0) && (numeroImpegnoOrigine != null && numeroImpegnoOrigine != 0)){
			jpql.append(" AND  annoOriAtt.attrCode = 'annoOriginePlur' AND annoOriRAttr.testo = :annoOrigine");
			jpql.append(" AND  numOriAtt.attrCode = 'numeroOriginePlur' AND numOriRAttr.testo = :numeroOrigine");
			param.put("annoOrigine", String.valueOf(annoImpegnoOrigine));
			param.put("numeroOrigine", String.valueOf(numeroImpegnoOrigine));
		}
		
		//SIAC-7144
		//Progetto
		if(!StringUtilsFin.isEmpty(progetto)){
			jpql.append(" AND UPPER(tProgramma.programmaCode) LIKE UPPER(:programmaCode)");
			String progettoLike = null;
			if(progetto.contains("%")){
				progettoLike = progetto;
			} else {
				progettoLike = '%' + progetto + '%';
			}
			param.put("programmaCode", progettoLike);
		}
		
		
		jpql.append(" ORDER BY mvg.movgestAnno, mvg.movgestNumero ");

		//Creo la query effettiva
		Query query =  createQuery(jpql.toString(), param);

	
		//CR 1907
		//if(query.getResultList().size() > CostantiFin.MAX_RIGHE_ESTRAIBILI){
		//	listaImpegni = query.setMaxResults(CostantiFin.MAX_RIGHE_ESTRAIBILI + 1).getResultList();
		//}else{
		listaImpegni = query.getResultList();
		//}	
		
		//Termino restituendo l'oggetto di ritorno: 
        return listaImpegni;
	}
		
	/**
	 * E' il metodo di "engine" di ricerca degli impegni e subimpegni
	 * Non restituisce gli oggetti completi ma solo l'elenco di identificativi. 
	 * Sara' il chiamante a vestire i dati come meglio crede.
	 */
	@SuppressWarnings("unchecked")
	@Override
	//task-168
	public List<IdImpegnoSubimpegno> ricercaImpegniSubImpegni(Integer enteUid, RicercaImpSubParamDto prs, boolean soloImpegni, boolean programmaCodeStrict) {
		
		
		List<IdImpegnoSubimpegno> listaID = new ArrayList<IdImpegnoSubimpegno>();
		
		Map<String,Object> param = new HashMap<String, Object>();
		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
		
		// PARAMETRI DI INPUT
		Integer annoEsercizio = prs.getAnnoEsercizio(); //Bilancio.anno , usato su competenza passata, attuale o futura
		
		// usato solo da ricerca impegni e subimpegni
		String tipoImpegno = prs.getTipoImpegno();
		String statoOperativo= prs.getStato();
		
		Integer annoImpegno = prs.getAnnoImpegno(); //Impegno.annoMovimento
		BigDecimal numeroImpegno = ((BigDecimal)prs.getNumeroImpegno()!=null? prs.getNumeroImpegno(): null); //Impegno.numero
		String cig = prs.getCig();
		String cup = prs.getCup(); 
		String progetto = prs.getProgetto(); //CodiceProgetto.codice
	
		String codiceCreditore = prs.getCodiceCreditore(); // Soggetto.codice
		String codiceClasseSoggetto = prs.getCodiceClasseSoggetto();
		
		String codiceProgetto = prs.getCodiceProgetto();
		
		Integer idProvvedimento = prs.getUidProvvedimento();
		Integer annoProvvedimento = prs.getAnnoProvvedimento();
		Integer numeroProvvedimento = prs.getNumeroProvvedimento();
		String codiceTipoProvvedimento ="";
		if(prs.getTipoProvvedimento()!=null){
			codiceTipoProvvedimento = prs.getTipoProvvedimento().getCodice();
		}	
		
		Integer uidStrutturaProvvedimento = prs.getUidStrutturaAmministrativoContabile();
		
		Integer uidCapitolo = prs.getUidCapitolo();
		Integer numeroCapitolo = prs.getNumeroCapitolo();
		Integer numeroArticolo = prs.getNumeroArticolo();
		Integer numeroUeb = prs.getNumeroUEB();
		String flagDaRiaccerto = prs.getFlagDaRiaccertamento() != null ? prs.getFlagDaRiaccertamento() : "";
		Integer annoImpegnoRiaccertato = prs.getAnnoImpegnoRiaccertato(); //Impegno.annoRiaccertato
		Integer annoImpegnoOrigine = prs.getAnnoImpegnoOrigine(); //Impegno.annoOrigine
		Integer numeroImpegnoOrigine = prs.getNumeroImpegnoOrigine();//Impegno.numeroOrigine
		Integer numeroImpegnoRiaccertato = prs.getNumeroImpegnoRiaccertato(); //Impegno.numeroRiaccertato
		boolean competenzaCorrente = prs.isCompetenzaCorrente(); 
		boolean competenzaCompetenza = prs.isCompetenzaCompetenza();
		boolean competenzaFuturi = prs.isCompetenzaFuturi();
		boolean competenzaResiduiRor = prs.isCompetenzaResiduiRor();
		
		
		
		
				
		//Creo La query per il modulo FIN
		StringBuilder jpql = new StringBuilder("SELECT NEW it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno(mvg.movgestId, movgestTs.movgestTsIdPadre, mvg.movgestNumero, mvg.movgestAnno"
				+ ",movgestTs.movgestTsId, movgestTs.movgestTsCode, dMovgestTsTipo.movgestTsTipoCode) ")
		.append(" FROM SiacTMovgestFin mvg ")
		.append(" left join mvg.siacTMovgestTs movgestTs left join mvg.siacDMovgestTipo dMovgestTipo ")
		.append(" left join movgestTs.siacDMovgestTsTipo dMovgestTsTipo ")
		.append(" left join mvg.siacTBil tBil left join tBil.siacTPeriodo tPeriodo ")
		.append(" left join mvg.siacRMovgestBilElems rBilElem left join rBilElem.siacTBilElem tBilElem  ");
		
		//SIAC-7349
		if(prs.getComponenteBilancioUid()!= null && prs.getComponenteBilancioUid().intValue()!=0){
			jpql.append(" left join rBilElem.siacDBilElemDetCompTipo dBilElemDetCompTipo  ");
		}
		
		
		//SIAC-6997
		if(!StringUtilsFin.isEmpty(tipoImpegno) || (prs.getStrutturaSelezionataCompetente()!= null && prs.getStrutturaSelezionataCompetente().length()>0)){
			jpql.append(" left join movgestTs.siacRMovgestClasses rMovgestClass left join rMovgestClass.siacTClass tClass ");
		}
		
		//Cig
		if(!StringUtilsFin.isEmpty(cig) ){ 
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cigRAttr left join cigRAttr.siacTAttr cigAtt ");
		}
		
		//Cup
		if(!StringUtilsFin.isEmpty(cup)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cupRAttr left join cupRAttr.siacTAttr cupAtt ");
		}
		
		//SIAC-6997 .....stessa condizione di prima per reanno ...sarebbe da sistemare 
		if((prs.getReanno()!= null && Boolean.TRUE.equals(prs.getReanno()))){ 
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs relAttrs left join relAttrs.siacTAttr tabAttr ");
		}
		
		
		

		boolean annoONumeroProvv = (CommonUtil.maggioreDiZero(annoProvvedimento) || CommonUtil.maggioreDiZero(numeroProvvedimento));
		
		//Inserisco la join del provvedimento
		jpql.append(" left join movgestTs.siacRMovgestTsAttoAmms rTsAttoAmm left join rTsAttoAmm.siacTAttoAmm tAttoAmm ");

		if(!StringUtilsFin.isEmpty(codiceTipoProvvedimento)){
			jpql.append(" left join tAttoAmm.siacDAttoAmmTipo dTipoAtto ");
		}
		
		// struttura amministrativa
		if(uidStrutturaProvvedimento!=null){
			jpql.append(" left join tAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
		}
		
		//soggetto
		if(!StringUtilsFin.isEmpty(codiceCreditore)){
			jpql.append(" left join movgestTs.siacRMovgestTsSogs rSogg left join rSogg.siacTSoggetto sogg ");
		}
		
		//progetto
		if (!StringUtilsFin.isEmpty(codiceProgetto)) {
			jpql.append(" left join movgestTs.siacRMovgestTsProgrammas rProgrammas left join rProgrammas.siacTProgramma tProgramma ");
		}
		
		//cronoprogramma
		if (prs.getUidCronoprogramma() != null && prs.getUidCronoprogramma().intValue() != 0) {
			jpql.append(" left join movgestTs.siacRMovgestTsCronopElemFins rElems left join rElems.siacTCronopElem.siacTCronop tCronop ");
		}
		
		// CR - 1907 RM 
		// aggiunto classe soggetto
		if(!StringUtilsFin.isEmpty(codiceClasseSoggetto)){
			jpql.append(" left join movgestTs.siacRMovgestTsSogclasses rClass left join rClass.siacDSoggettoClasse classe ");
		}
		
		// CR - 1907 RM 
		// aggiuntoNumero e Anno Origine
		if((annoImpegnoOrigine != null && annoImpegnoOrigine != 0) && (numeroImpegnoOrigine != null && numeroImpegnoOrigine != 0)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs annoOriRAttr left join annoOriRAttr.siacTAttr annoOriAtt ");
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs numOriRAttr left join numOriRAttr.siacTAttr numOriAtt ");
		}
		
		
		// CR - 1907 eliminato controllo su articolo !=0 (' && numeroArticolo != 0'), 
		// perchè per regione ci potrebbero essere capitoli con articolo 0 
		//if(uidCapitolo!=null || (numeroCapitolo != null && numeroCapitolo != 0) || (numeroArticolo != null) || (numeroUeb != null && numeroUeb != 0)){
			// capitolo 
		//jpql.append("left join mvg.siacRMovgestBilElems rBilElem left join rBilElem.siacTBilElem tBilElem  ");
		//}
		
		if(!StringUtilsFin.isEmpty(statoOperativo) || !StringUtilsFin.isEmpty(prs.getStatiDaEscludere())){
			jpql.append(" left join movgestTs.siacRMovgestTsStatos rMovgestTsStato left join rMovgestTsStato.siacDMovgestStato dMovgestStato");
		}
	
		
		//SIAC-6997
		if(prs.isRicercaResiduiRorFlag()){
			jpql.append(" left join movgestTs.siacTMovgestTsDets movgestDet left join movgestDet.siacDMovgestTsDetTipo movgestDetTipo ");
		}
		
		
		//WHERE 
		jpql.append(" WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
						.append("AND dMovgestTipo.movgestTipoCode = :tipoMovimentoImpegno ")
						.append("AND tPeriodo.anno = :annoBilancio ");
		jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("mvg"));
		jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("movgestTs"));
		jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rBilElem",null, true));
		
		//FIX JIRA SIAC-3386 AGGIUNTE VALIDITA SU CAMPI OPZIONALI:
		if(!StringUtilsFin.isEmpty(codiceCreditore)){
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rSogg"));
		}
		if(!StringUtilsFin.isEmpty(codiceClasseSoggetto)){
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rClass"));
		}
		//
		
		//codice progetto
		if(!StringUtilsFin.isEmpty(codiceProgetto)){
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rProgrammas"));
		}
		
		
		param.put("annoBilancio", String.valueOf(prs.getAnnoEsercizio()));
		param.put("enteProprietarioId", enteUid);
		param.put("tipoMovimentoImpegno", CostantiFin.MOVGEST_TIPO_IMPEGNO);

		
		if(!StringUtilsFin.isEmpty(statoOperativo) || !StringUtilsFin.isEmpty(prs.getStatiDaEscludere()) ){
			
			/*
			 * SIAC-6997
			 * Se solo impegni prendiamo la testata...
			 * in caso di ror vengono presi anche i sub
			 */
			if(soloImpegni){
				jpql.append(" AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTs ");
				param.put("tipoMovGestTs", CostantiFin.MOVGEST_TS_TIPO_TESTATA);
			}
			
			// Rm: 19.06.2015:
			// Se ricerco anche per stato mov. gestione non devo ricercare anche i sub ma solo gli impegni
			if(!StringUtilsFin.isEmpty(statoOperativo)){
					jpql.append(" AND dMovgestStato.movgestStatoCode = :statoOperativo ");
					param.put("statoOperativo", statoOperativo);
			}
			if(!StringUtilsFin.isEmpty(prs.getStatiDaEscludere()) ){
				String statiDaEscludere = buildElencoPerClausolaIN(prs.getStatiDaEscludere());
				jpql.append(" AND dMovgestStato.movgestStatoCode NOT IN :statiDaEscludere");
				param.put("statiDaEscludere", statiDaEscludere);
			}
			
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rMovgestTsStato"));
			
			
		} else {
			//non e' valorizzato ne lo stato ne gli stati da escludere
			jpql.append(" AND (dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTs OR  dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTsS ) ");
			param.put("tipoMovGestTs", CostantiFin.MOVGEST_TS_TIPO_TESTATA);
			param.put("tipoMovGestTsS", CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO);
		}		

		
		
		//SIAC-6997 da capire in caso ror
		if(flagDaRiaccerto.equalsIgnoreCase("Si")){
			String jpqlRiaccertato = buildClausolaExistVersoTAttr("flagRAttr", "movgestTs", "flagDaRiaccertamento", "boolean_", "S",true,false,false);
			jpql.append(jpqlRiaccertato);
			
			
			if((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0) && (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)){
				String jpqlAnnoRiacc = buildClausolaExistVersoTAttr("flagRAttrAnno", "movgestTs", "annoRiaccertato", "testo", "annoRiacc",false,true,false);
				jpql.append(jpqlAnnoRiacc);
				param.put("annoRiacc", String.valueOf(annoImpegnoRiaccertato));
				
				String jpqlNumRiacc = buildClausolaExistVersoTAttr("flagRAttrNumero", "movgestTs", "numeroRiaccertato", "testo", "numeroRiacc",false,true,false);
				jpql.append(jpqlNumRiacc);
				param.put("numeroRiacc", String.valueOf(numeroImpegnoRiaccertato));
			}
			
			
		}
		
		
		
		
		//Numero e Anno Origine
		if((annoImpegnoOrigine != null && annoImpegnoOrigine != 0) && (numeroImpegnoOrigine != null && numeroImpegnoOrigine != 0)){
			jpql.append(" AND  annoOriAtt.attrCode = 'annoOriginePlur' AND annoOriRAttr.testo = :annoOrigine");
			jpql.append(" AND  numOriAtt.attrCode = 'numeroOriginePlur' AND numOriRAttr.testo = :numeroOrigine");
			param.put("annoOrigine", String.valueOf(annoImpegnoOrigine));
			param.put("numeroOrigine", String.valueOf(numeroImpegnoOrigine));
		}
		

		// Tipo Impegno 
		if(!StringUtilsFin.isEmpty(tipoImpegno)){
			jpql.append(" AND tClass.classifCode = :tipoImpegno ");
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rMovgestClass"));
			param.put("tipoImpegno", tipoImpegno.toUpperCase());
			
		}

		// numero impegno
		if(numeroImpegno!=null){
			jpql.append(" AND mvg.movgestNumero = :numeroImpegno ");
			param.put("numeroImpegno", numeroImpegno);
		}
		

		if(annoImpegno != null && annoImpegno != 0 ){
			jpql.append(" AND mvg.movgestAnno = :annoImpegno ");
			param.put("annoImpegno",annoImpegno);
		}

		
		/*SIAC-6997
		 * In caso di reanno la condizione di competenza è per anno assunzione (anno N)
		 * e residuo (<anno N). Se nel criterio di ricerca viene inserito l anno impegno
		 * questa condizione (come nel caso di ricerca impegno normale) va considerata nella lista 
		 * finale perchè si avrebbe:
		 * AND mvg.movgestAnno = :annoImpegno
		 * AND (mvg.movgestAnno = :annoEsercizio OR mvg.movgestAnno < :annoEsercizio)
		 * 
		 */
		
		if(prs.isRicercaResiduiRorFlag()){
			/* Da richiesta bisognava prendere il residuo. 
			 * Tale operazione risulta troppo dispensiosa in termini di
			 * performance perchè i dati da filtrare sono calcolati 
			 * Prendiamo l'importo > 0
			 */
//			jpql.append(" AND movgestDet.movgestTsDetImporto >0 AND movgestDetTipo.movgestTsDetTipoCode = :tipoAttuale ");
//			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("movgestDet"));
//			param.put("tipoAttuale", CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
			
			
			//CONDIZIONE SULL ESISTENZA DELLE MODIFICHE PER IL FILTRAGGIO ROR
			jpql.append(" AND ( ");
			jpql.append(" ( movgestDet.movgestTsDetImporto >0 AND movgestDetTipo.movgestTsDetTipoCode = :tipoAttuale ");
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("movgestDet"));
			jpql.append(" ) ");
			jpql.append(" OR EXISTS ( SELECT movts from SiacTMovgestTsFin movts ");
			jpql.append(" left join movts.siacTMovgestTsDetMods mod ");
			jpql.append(" left join mod.siacRModificaStato modr ");
			jpql.append(" left join modr.siacDModificaStato statomod ");
			jpql.append(" left join modr.siacTModifica modifica ");
			jpql.append(" left join modifica.siacDModificaTipo tipoModifica ");
			jpql.append(" where  movts.movgestTsId = movgestTs.movgestTsId AND statomod.modStatoCode != :annullato ");
			jpql.append(" AND modr.dataCancellazione IS NULL AND (tipoModifica.modTipoCode= :RORM OR  tipoModifica.modTipoCode= :INEROR OR tipoModifica.modTipoCode = :INSROR ");
			jpql.append(" OR tipoModifica.modTipoCode= :REIMP)) ");
			jpql.append(" ) ");
			param.put("tipoAttuale", CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
			
			param.put("annullato", CostantiFin.D_MODIFICA_STATO_ANNULLATO);
			param.put("REIMP", CodiciMotiviModifiche.REIMP.getCodice());
			param.put("RORM", CodiciMotiviModifiche.RORM.getCodice());
			param.put("INEROR", CodiciMotiviModifiche.INEROR.getCodice());
			param.put("INSROR", CodiciMotiviModifiche.INSROR.getCodice());
			
			
		}
		
		
		if(competenzaResiduiRor){
			jpql.append(" AND (mvg.movgestAnno = :anno OR mvg.movgestAnno  < :anno) ");
			param.put("anno",  annoEsercizio);
		}else{
			//Lascio tutto come era prima
			/*
			 * SIAC-6997
			 * Se da Residui ROR bisognerebbe prendere la competenza corrente 
			 * unita al residuo. impegno semplice considerare il filtraggio
			 * residuo dovrebbe essere competenzaCompetenza ...con anno < del bilancio
			 */
			
				// CR - 1907 RM 
				// aggiunto Impegni Correnti
				if(competenzaCorrente){
					jpql.append(" AND mvg.movgestAnno = :anno ");
					param.put("anno", annoEsercizio);
				}
				
				// CR - 1907 RM 
				// aggiunto Impegni Passati
				if(competenzaCompetenza){
					jpql.append(" AND mvg.movgestAnno  < :anno ");
					param.put("anno", annoEsercizio);
				}
				
				// CR - 1907 RM 
				// aggiunto Impegni Futuri
				if(competenzaFuturi){
					jpql.append(" AND mvg.movgestAnno > :anno ");
					param.put("anno", annoEsercizio);
				}
			
			
			
		}
			
		
		
		

		
		// Capitolo	
		// jira 1337 ricerca capitolo con 0
		if(uidCapitolo != null && uidCapitolo != 0){			
			jpql.append(" AND tBilElem.elemId = :uidCapitolo");
			
			param.put("uidCapitolo", uidCapitolo);
		} else {
			
			
			// numero capitolo
			if(numeroCapitolo != null && numeroCapitolo != 0){
				jpql.append(" AND tBilElem.elemCode = :numeroCapitolo ");
				param.put("numeroCapitolo", String.valueOf(numeroCapitolo));
			}
			
			// numero articolo
			if(numeroArticolo != null && numeroArticolo != 0){
				jpql.append(" AND tBilElem.elemCode2 = :numeroArticolo ");
				param.put("numeroArticolo", String.valueOf(numeroArticolo));
			}
			
			// numero ueb
			if(numeroUeb != null && numeroUeb != 0){
				jpql.append(" AND tBilElem.elemCode3 = :numeroUeb ");
				param.put("numeroUeb", String.valueOf(numeroUeb));
			}	
		}
		
		/*SIAC-7349
		 * 
		 */
		if(prs.getComponenteBilancioUid()!= null && prs.getComponenteBilancioUid().intValue()!=0){
			jpql.append(" AND dBilElemDetCompTipo.elemDetCompTipoId = :elemDetCompTipoId");
			//jpql.append(" AND rBilElem.elemDetCompTipoId = :elemDetCompTipoId");
			
			param.put("elemDetCompTipoId", prs.getComponenteBilancioUid());
		}
		
		
		
		//Ricerca Soggetto
		if(!StringUtilsFin.isEmpty(codiceCreditore)){
			jpql.append(" AND sogg.soggettoCode = :codiceSoggetto ");
			param.put("codiceSoggetto", codiceCreditore);
		}
		
		// Ricerco per classe soggetto
		if(!StringUtilsFin.isEmpty(codiceClasseSoggetto)){
			jpql.append(" AND classe.soggettoClasseCode = :codiceClasse ");				
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rClass"));
			param.put("codiceClasse", codiceClasseSoggetto);
		}
		
		// progetto
		//SIAC-7144
		/*if(!StringUtilsFin.isEmpty(progetto)){
			jpql.append(" AND UPPER(tProgramma.programmaCode) LIKE UPPER(:programmaCode)");
			String progettoLike = null;
			if(progetto.contains("%")){
				progettoLike = progetto;
			} else {
				progettoLike = '%' + progetto + '%';
			}
			param.put("programmaCode", progettoLike);
		}		
		*/
		
		//progetto
		//task-168
		if(!StringUtilsFin.isEmpty(progetto)) {
			if (programmaCodeStrict) {
				jpql.append(" AND tProgramma.programmaCode=:programmaCode ");
			} else {
				if(progetto.contains("%")) {
					jpql.append(" AND UPPER(tProgramma.programmaCode) LIKE UPPER(:programmaCode)");
				} else {
					jpql.append(" AND UPPER(tProgramma.programmaCode) LIKE UPPER('%'||:programmaCode||'%')");
				}
			}
			
			param.put("programmaCode", progetto);
		}
		
		//cronoprogramma
		if (prs.getUidCronoprogramma() != null && prs.getUidCronoprogramma().intValue() != 0) {
			jpql.append(" AND tCronop.cronopId = :cronopId");
			param.put("cronopId", prs.getUidCronoprogramma());
		}
		

		//Provvedimento
		if(idProvvedimento != null && idProvvedimento != 0){
			
			//SIAC-6018 aggiungo condizione di validita su siac_r_movgest_ts_atto_amm:
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rTsAttoAmm"));
			//
			
			jpql.append(" AND tAttoAmm.attoammId = :provvedimentoId ");
			param.put("provvedimentoId", idProvvedimento);
		} else {
			
			// 	SIAC-6018 aggiungo condizione di validita su siac_r_movgest_ts_atto_amm:
			if(annoONumeroProvv){
				jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rTsAttoAmm"));
			}
			//
			
			if(annoProvvedimento != null && annoProvvedimento != 0){
				jpql.append(" AND tAttoAmm.attoammAnno = :annoProvvedimento ");
				param.put("annoProvvedimento", String.valueOf(annoProvvedimento));
			}
			
			if(numeroProvvedimento != null && numeroProvvedimento != 0){
				jpql.append(" AND tAttoAmm.attoammNumero = :numeroProvvedimento ");
				param.put("numeroProvvedimento", numeroProvvedimento);
			}
			
			if(!StringUtilsFin.isEmpty(codiceTipoProvvedimento)){
				jpql.append(" AND dTipoAtto.attoammTipoCode = :codiceTipoProvvedimento ");
				param.put("codiceTipoProvvedimento", codiceTipoProvvedimento);
			}	
			
			
			//aggiunto filtro per sac del provevdimento
			if(uidStrutturaProvvedimento!=null){
				jpql.append(" AND tClassAtto.classifId = :uidStrutturaAmmProvvedimento ");
				param.put("uidStrutturaAmmProvvedimento", uidStrutturaProvvedimento);
			}
		}
		
		//Cig
		if(!StringUtilsFin.isEmpty(cig)){
			jpql.append(" AND  cigAtt.attrCode = 'cig' AND UPPER(cigRAttr.testo) LIKE UPPER(:cigLike) ");
			//SIAC-8302
			jpql.append(DataValiditaUtil.ottieniClauseCheEscludeRecordCancellatiLogicamente("cigRAttr"));
			String cigLike = null;
			if(cig.contains("%")){
				cigLike = cig;
			} else {
				cigLike = '%' + cig + '%';
			}
			param.put("cigLike", cigLike);
		}
		
		//Cup
		if(!StringUtilsFin.isEmpty(cup)){
			jpql.append(" AND  cupAtt.attrCode = 'cup' AND UPPER(cupRAttr.testo) LIKE UPPER(:cupLike) ");
			jpql.append(DataValiditaUtil.ottieniClauseCheEscludeRecordCancellatiLogicamente("cupRAttr"));
			String cupLike = null;
			if(cup.contains("%")){
				cupLike = cig;
			} else {
				cupLike = '%' + cup + '%';
			}
			param.put("cupLike", cupLike);
		}

		
		//SIAC-6997
		if(prs.getReanno()!= null && Boolean.TRUE.equals(prs.getReanno())){
				jpql.append(" AND ((tabAttr.attrCode = :flagDaReanno AND relAttrs.boolean_ = 'S' ");
				
				if((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0) && (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)){
					String jpqlAnnoRiacc = buildClausolaExistVersoTAttr("flagRAttrAnno", "movgestTs", "annoRiaccertato", "testo", "annoRiacc",false,true,false);
					jpql.append(jpqlAnnoRiacc);
					param.put("annoRiacc", String.valueOf(annoImpegnoRiaccertato));
					String jpqlNumRiacc = buildClausolaExistVersoTAttr("flagRAttrNumero", "movgestTs", "numeroRiaccertato", "testo", "numeroRiacc",false,true,false);
					jpql.append(jpqlNumRiacc);
					param.put("numeroRiacc", String.valueOf(numeroImpegnoRiaccertato));
				}
				
				jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("relAttrs"));		
				jpql.append("AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestReannoT) " + 
								"OR " +
								"(dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestReannoS AND  mvg.movgestId in ( "+
								"SELECT mvg2.movgestId "+
								"FROM SiacTMovgestFin mvg2 "+  
								"left join mvg2.siacTMovgestTs movgestTs2 " +
								"left join mvg2.siacDMovgestTipo dMovgestTipo2 "+
								"left join movgestTs2.siacDMovgestTsTipo dMovgestTsTipo2 "+
								"left join movgestTs2.siacRMovgestTsAttrs relAttrs2 "+
								"left join relAttrs2.siacTAttr tabAttr2 "+
								"where tabAttr2.attrCode = :flagDaReanno AND relAttrs2.boolean_ = 'S' ");
				
				
				if((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0) && (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)){
					String jpqlAnnoRiacc = buildClausolaExistVersoTAttr("flagRAttrAnno", "movgestTs2", "annoRiaccertato", "testo", "annoRiacc1",false,true,false);
					jpql.append(jpqlAnnoRiacc);
					param.put("annoRiacc1", String.valueOf(annoImpegnoRiaccertato));
					String jpqlNumRiacc = buildClausolaExistVersoTAttr("flagRAttrNumero", "movgestTs2", "numeroRiaccertato", "testo", "numeroRiacc1",false,true,false);
					jpql.append(jpqlNumRiacc);
					param.put("numeroRiacc1", String.valueOf(numeroImpegnoRiaccertato));
					
				}
				jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("relAttrs2"));				
				jpql.append("AND dMovgestTsTipo2.movgestTsTipoCode = :tipoMovGestReannoT) "+
								")) ");
				param.put("flagDaReanno", "flagDaReanno");
				param.put("tipoMovGestReannoT", CostantiFin.MOVGEST_TS_TIPO_TESTATA);
				param.put("tipoMovGestReannoS", CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO);
				//param.put("anno", annoEsercizio);
			
		}
		
//		if(prs.getStrutturaSelezionataCompetente()!= null && prs.getStrutturaSelezionataCompetente().length()>0){
//			try{
//				Integer classificatoreId = Integer.parseInt(prs.getStrutturaSelezionataCompetente());
//				//jpql.append(" AND tClass.classifId = :classificatoreId ");
//				
//				jpql.append(" AND ((tClass.classifId = :classificatoreId AND rMovgestClass.dataCancellazione IS NULL AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestStrutturaT) " + 
//								"OR " +
//								"(dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestStrutturaS AND  mvg.movgestId in ( "+
//								"SELECT mvg1.movgestId "+
//								"FROM SiacTMovgestFin mvg1 "+  
//								"left join mvg1.siacTMovgestTs movgestTs1 " +
//								"left join mvg1.siacDMovgestTipo dMovgestTipo1 "+
//								"left join movgestTs1.siacDMovgestTsTipo dMovgestTsTipo1 "+
//								"left join movgestTs1.siacRMovgestClasses rMovgestClass1 "+
//								"left join rMovgestClass1.siacTClass tClass1 "+
//								"where  tClass1.classifId = :classificatoreId AND rMovgestClass1.dataCancellazione IS NULL AND dMovgestTsTipo1.movgestTsTipoCode = :tipoMovGestStrutturaT) "+
//								")) ");
//				param.put("classificatoreId", classificatoreId);
//				param.put("tipoMovGestStrutturaT", CostantiFin.MOVGEST_TS_TIPO_TESTATA);
//				param.put("tipoMovGestStrutturaS", CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO);
//				//param.put("anno", annoEsercizio);
//			}catch(Exception e){
//				log.error("ricercaImpegniSubImpegni", e.getMessage());
//			}
//		}
		
		
		//SIAC-7486
		if(prs.getListStrutturaCompetenteInt()!= null && !prs.getListStrutturaCompetenteInt().isEmpty()){
			try{
				
				//jpql.append(" AND tClass.classifId = :classificatoreId ");
				String inConditionac = buildInCondition(prs.getListStrutturaCompetenteInt());
				jpql.append(" AND ((tClass.classifId IN " + inConditionac +" AND rMovgestClass.dataCancellazione IS NULL AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestStrutturaT) " + 
								"OR " +
								"(dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestStrutturaS AND  mvg.movgestId in ( "+
								"SELECT mvg1.movgestId "+
								"FROM SiacTMovgestFin mvg1 "+  
								"left join mvg1.siacTMovgestTs movgestTs1 " +
								"left join mvg1.siacDMovgestTipo dMovgestTipo1 "+
								"left join movgestTs1.siacDMovgestTsTipo dMovgestTsTipo1 "+
								"left join movgestTs1.siacRMovgestClasses rMovgestClass1 "+
								"left join rMovgestClass1.siacTClass tClass1 "+
								"where  tClass1.classifId IN " + inConditionac +" AND rMovgestClass1.dataCancellazione IS NULL AND dMovgestTsTipo1.movgestTsTipoCode = :tipoMovGestStrutturaT) "+
								")) ");
				
				param.put("tipoMovGestStrutturaT", CostantiFin.MOVGEST_TS_TIPO_TESTATA);
				param.put("tipoMovGestStrutturaS", CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO);
				//param.put("anno", annoEsercizio);
			}catch(Exception e){
				log.error("ricercaImpegniSubImpegni", e.getMessage());
			}
		}
		
		
		//jpql.append(" ORDER BY mvg.movgestAnno, mvg.movgestNumero ");
		jpql.append(" ORDER BY mvg.movgestAnno , mvg.movgestNumero, movgestTs.movgestTsId "); //SIAC-6997

		String jpqlQuery = jpql.toString();
		//Creo la query effettiva
		Query query =  createQuery(jpqlQuery, param);			
		listaID = query.getResultList();
		//Termino restituendo l'oggetto di ritorno: 
        return listaID;
	}
	
	
	
//	private void buildAnnoNumeroReanno(Integer annoImpegnoRiaccertato, Integer numeroImpegnoRiaccertato, Map<String,Object> param, StringBuilder jpql){
//		if((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0) && (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)){
//			String jpqlAnnoRiacc = buildClausolaExistVersoTAttr("flagRAttrAnno", "movgestTs", "annoRiaccertato", "testo", "annoRiacc1",false,true,false);
//			jpql.append(jpqlAnnoRiacc);
//			param.put("annoRiacc1", String.valueOf(annoImpegnoRiaccertato));
//			
//			String jpqlNumRiacc = buildClausolaExistVersoTAttr("flagRAttrNumero", "movgestTs", "numeroRiaccertato", "testo", "numeroRiacc1",false,true,false);
//			jpql.append(jpqlNumRiacc);
//			param.put("numeroRiacc1", String.valueOf(numeroImpegnoRiaccertato));
//		}
//	}
	
	
	
	
	private String buildClausolaExistVersoTAttr(String aliasRAttr,String aliasMovgestTs,
			String attrCode,String colonnaValore,String valore, boolean upperValore, boolean valoreParametrico,boolean notExists){
		
		String clausola = "";
		
		StringBuilder jpql = new StringBuilder(" AND ");
		
		if(notExists){
			jpql.append(" NOT EXISTS ");
		} else {
			jpql.append(" EXISTS ");
		}
		
		jpql.append(" ("
				+ "SELECT "+aliasRAttr+".bilElemAttrId " +
				" from SiacRMovgestTsAttr "+aliasRAttr+" " +
				" where  " +
				" "+aliasRAttr+".siacTMovgestT.movgestTsId = "+aliasMovgestTs+".movgestTsId  " +
				" AND "+aliasRAttr+".siacTAttr.attrCode = '"+attrCode+"' ");
		
		jpql.append(" AND ");
		if(upperValore){
			jpql.append(" UPPER("+aliasRAttr+"."+colonnaValore+") ");
		} else {
			jpql.append(" " +aliasRAttr+"."+colonnaValore+" ");
		}
		if(valoreParametrico){
			jpql.append(" = :"+valore+" ");
		} else {
			jpql.append(" = '"+valore+"' ");
		}
		
		jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery(aliasRAttr));
		jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery(aliasRAttr+".siacTAttr"));
		jpql.append(")");
			
		clausola = jpql.toString();
		return clausola;
	}

	
	private List<IdMovgestSubmovegest> scremaDoppioni(List<IdMovgestSubmovegest> lista){
		ArrayList<IdMovgestSubmovegest> listaRicostruita = new ArrayList<IdMovgestSubmovegest>();
		ArrayList<Integer> aggiunti = new ArrayList<Integer>();
		if(lista!=null && lista.size()>0){
			for(IdMovgestSubmovegest it : lista){
				if(!aggiunti.contains(it.getMovgestTsId())){
					listaRicostruita.add(it);
					aggiunti.add(it.getMovgestTsId());
				}
			}
		}
		return listaRicostruita;
	}
	
	public BigDecimal calcolaDisponibilitaAImpegnarePerDodicesimi(Integer elemId){
		BigDecimal result  = new  BigDecimal(0);
		String methodName ="calcolaDisponibilitaAImpegnarePerDodicesimi";
		String functionName="fnc_siac_disponibilitadodicesimi_dim ";
		try{
			Query query = entityManager.createNativeQuery("SELECT "+ functionName  + "(:elemId)");
			query.setParameter("elemId", elemId);		
			result = (BigDecimal) query.getSingleResult();
			if(result==null){
				result  = new  BigDecimal(0);
			}
			log.debug(methodName, "Returning result: "+ result + " for elemId: "+ elemId + " and functionName: "+ functionName);
			
		}catch(Exception e){
			log.error(methodName, "Returning result: "+ result + " for elemId: "+elemId + " and functionName: "+ functionName);
		}
		return result;
	}


	/**
	 * calcola la disponibilita a liquidare per un impegno o per un subimpegno indicato
	 * tramite richiamo a function su database
	 * @param idMovGestTs
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal calcolaDisponibilitaALiquidare(Integer idMovGestTs){
		BigDecimal result  = new  BigDecimal(0);
		String methodName ="calcolaDisponibilitaALiquidare";
		String functionName="fnc_siac_disponibilitaliquidaremovgest ";
		try{
			Query query = entityManager.createNativeQuery("SELECT "+ functionName  + "(:idMovGestTs)");
			query.setParameter("idMovGestTs", idMovGestTs);		
			result = (BigDecimal) query.getSingleResult();
			if(result==null){
				result  = new  BigDecimal(0);
			}
//			log.debug(methodName, "Returning result: "+ result + " for idMovGestTs: "+ idMovGestTs + " and functionName: "+ functionName);
			
		}catch(Exception e){
			log.error(methodName, "Returning result: "+ result + " for idMovGestTs: "+ idMovGestTs + " and functionName: "+ functionName);
		}
		return result;
	}
	
	
	public ConsultaDettaglioImpegnoDto consultaDettaglioImpegno(Integer idMovGestTs){
		String methodName ="consultaDettaglioImpegno";
		String functionName="fnc_siac_consultadettaglioimpegno ";
		ConsultaDettaglioImpegnoDto esito = null;
		try{
			Query query = entityManager.createNativeQuery("SELECT * FROM "+ functionName  + "(:idMovGestTs)");
			query.setParameter("idMovGestTs", idMovGestTs);		
			
			List<Object[]> result = (List<Object[]>) query.getResultList();
			
			esito = mapResultToConsultaDettaglioImpegnoDto(result);
			
			log.debug(methodName, "Errore for idMovGestTs: "+ idMovGestTs + " and functionName: "+ functionName);
		}catch(Exception e){
			log.error(methodName, "Errore for idMovGestTs: "+ idMovGestTs + " and functionName: "+ functionName);
		}
		return esito;
	}
	
	protected ConsultaDettaglioImpegnoDto mapResultToConsultaDettaglioImpegnoDto(List<Object[]> result) {
		
		ConsultaDettaglioImpegnoDto esito = new ConsultaDettaglioImpegnoDto(); 

		if (!result.isEmpty()) {
			
		 Object[] lp = result.get(0);
			
		 esito.setTotImpLiq((BigDecimal) lp[0]);
		 esito.setNumeroLiq((Integer) lp[1]);
		 esito.setTotImpSubdoc((BigDecimal) lp[2]);
		 esito.setNumeroImpDoc((Integer) lp[3]);
		 esito.setTotImpLiqSudoc((BigDecimal) lp[4]);
		 esito.setNumeroDocLiq((Integer) lp[5]);
		 esito.setTotDocNonLiq((BigDecimal) lp[6]);
		 esito.setNumeroDocNonLiq((Integer) lp[7]);
		 esito.setTotImpPredoc((BigDecimal) lp[8]);
		 esito.setNumeroImpPredoc((Integer) lp[9]);
		 esito.setTotImpCartac((BigDecimal) lp[10]);
		 esito.setNumeroCartac((Integer) lp[11]);
		 esito.setTotImpCartacSubdoc((BigDecimal) lp[12]);
		 esito.setNumeroCartacSubdoc((Integer) lp[13]);
		 esito.setTotCarteNonReg((BigDecimal) lp[14]);
		 esito.setNumeroCarteNonReg((Integer) lp[15]); 
		 esito.setTotModProv((BigDecimal) lp[16]);
		 esito.setTotImpCecNoGiust((BigDecimal) lp[17]);
		 esito.setTotImp2NoGiust((BigDecimal) lp[18]);
		 esito.setTotImp2GiustIntegrato((BigDecimal) lp[19]);
		 esito.setTotImp2GiustRestituito((BigDecimal) lp[20]);
		 esito.setTotImpCecFattura((BigDecimal) lp[21]);
		 esito.setTotImpCecPafFatt((BigDecimal) lp[22]);
		 esito.setTotCec((BigDecimal) lp[23]);

		}

		return esito;
	}
	
	@SuppressWarnings("unchecked")
	public List<CodificaImportoDto> calcolaDisponibilitaALiquidareMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs) {

		String methodName = "calcolaDisponibilitaALiquidareMassive";
		String functionName = "fnc_siac_disponibilitaliquidaremovgest_rec";

		List<CodificaImportoDto> elencoCodificaImportoDto = new ArrayList<CodificaImportoDto>();
		try {
			if (listaSiacTMovgestTs != null && listaSiacTMovgestTs.size() > 0) {

				StringBuilder jpql = new StringBuilder("");

				int i = 0;
				for (SiacTMovgestTsFin it : listaSiacTMovgestTs) {
					if (i > 0) {
						jpql.append(" , ");
					}
					jpql.append("" + it.getMovgestTsId());
					i++;
				}

				String sql = "SELECT * FROM " + functionName
						+ "(:elencoMovgestTsIdIn)";

				log.debug(methodName, " Calling functionName: " + functionName
						+ " for uids: " + jpql.toString());

				Query query = entityManager.createNativeQuery(sql);

				query.setParameter("elencoMovgestTsIdIn", jpql.toString());

				List<Object[]> result = (List<Object[]>) query.getResultList();
				elencoCodificaImportoDto = mapResultToElencoCodificaImportoDto(result);

			}
		} catch (Exception e) {
			log.error("calcolaDisponibilitaALiquidareMassive", e);
		}
		return elencoCodificaImportoDto;
	}
	
	@SuppressWarnings("unchecked")
	public List<CodificaImportoDto> calcolaDisponibileAPagareMassive(List<SiacTMovgestTsFin> listaSiacTMovgestTs) {

		String methodName = "calcolaDisponibileAPagareMassive";
		String functionName = "fnc_siac_disponibileapagaremovgest_rec";

		List<CodificaImportoDto> elencoCodificaImportoDto = new ArrayList<CodificaImportoDto>();
		try {
			if (listaSiacTMovgestTs != null && listaSiacTMovgestTs.size() > 0) {

				StringBuilder jpql = new StringBuilder("");

				int i = 0;
				for (SiacTMovgestTsFin it : listaSiacTMovgestTs) {
					if (i > 0) {
						jpql.append(" , ");
					}
					jpql.append("" + it.getMovgestTsId());
					i++;
				}

				String sql = "SELECT * FROM " + functionName
						+ "(:elencoMovgestTsIdIn)";

				log.debug(methodName, " Calling functionName: " + functionName
						+ " for uids: " + jpql.toString());

				Query query = entityManager.createNativeQuery(sql);

				query.setParameter("elencoMovgestTsIdIn", jpql.toString());

				List<Object[]> result = (List<Object[]>) query.getResultList();
				elencoCodificaImportoDto = mapResultToElencoCodificaImportoDto(result);

			}
		} catch (Exception e) {
			log.error("calcolaDisponibilitaALiquidareMassive", e);
		}
		return elencoCodificaImportoDto;
	}
	
	public BigDecimal calcolaDisponibileAPagare(Integer idMovGestTs){
		BigDecimal result  = new  BigDecimal(0);
		String methodName ="calcolaDisponibileAPagare";
		String functionName="fnc_siac_disponibileapagaremovgest ";
		try{
			Query query = entityManager.createNativeQuery("SELECT "+ functionName  + "(:idMovGestTs)");
			query.setParameter("idMovGestTs", idMovGestTs);		
			result = (BigDecimal) query.getSingleResult();
			if(result==null){
				result  = new  BigDecimal(0);
			}
			log.debug(methodName, "Returning result: "+ result + " for idMovGestTs: "+ idMovGestTs + " and functionName: "+ functionName);
			
		}catch(Exception e){
			log.error(methodName, "Returning result: "+ result + " for idMovGestTs: "+ idMovGestTs + " and functionName: "+ functionName);
		}
		return result;
	}
	


	/*
	 * SIAC-6997
	 * SOLO PER I ROR
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IdImpegnoSubimpegno> ricercaImpegniSubImpegniROR(Integer enteUid, RicercaImpSubParamDto prs, boolean soloImpegni) {
		
		
		List<IdImpegnoSubimpegno> listaID = new ArrayList<IdImpegnoSubimpegno>();
		
		Map<String,Object> param = new HashMap<String, Object>();
		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
		
		// PARAMETRI DI INPUT
		Integer annoEsercizio = prs.getAnnoEsercizio(); //Bilancio.anno , usato su competenza passata, attuale o futura
		
		// usato solo da ricerca impegni e subimpegni
		String tipoImpegno = prs.getTipoImpegno();
		String statoOperativo= prs.getStato();
		
		Integer annoImpegno = prs.getAnnoImpegno(); //Impegno.annoMovimento
		BigDecimal numeroImpegno = ((BigDecimal)prs.getNumeroImpegno()!=null? prs.getNumeroImpegno(): null); //Impegno.numero
		String cig = prs.getCig();
		String cup = prs.getCup(); 
		String progetto = prs.getProgetto(); //CodiceProgetto.codice
	
		String codiceCreditore = prs.getCodiceCreditore(); // Soggetto.codice
		String codiceClasseSoggetto = prs.getCodiceClasseSoggetto();
		
		String codiceProgetto = prs.getCodiceProgetto();
		
		Integer idProvvedimento = prs.getUidProvvedimento();
		Integer annoProvvedimento = prs.getAnnoProvvedimento();
		Integer numeroProvvedimento = prs.getNumeroProvvedimento();
		String codiceTipoProvvedimento ="";
		if(prs.getTipoProvvedimento()!=null){
			codiceTipoProvvedimento = prs.getTipoProvvedimento().getCodice();
		}	
		
		Integer uidStrutturaProvvedimento = prs.getUidStrutturaAmministrativoContabile();
		
		Integer uidCapitolo = prs.getUidCapitolo();
		Integer numeroCapitolo = prs.getNumeroCapitolo();
		Integer numeroArticolo = prs.getNumeroArticolo();
		Integer numeroUeb = prs.getNumeroUEB();
		String flagDaRiaccerto = prs.getFlagDaRiaccertamento() != null ? prs.getFlagDaRiaccertamento() : "";
		Integer annoImpegnoRiaccertato = prs.getAnnoImpegnoRiaccertato(); //Impegno.annoRiaccertato
		Integer annoImpegnoOrigine = prs.getAnnoImpegnoOrigine(); //Impegno.annoOrigine
		Integer numeroImpegnoOrigine = prs.getNumeroImpegnoOrigine();//Impegno.numeroOrigine
		Integer numeroImpegnoRiaccertato = prs.getNumeroImpegnoRiaccertato(); //Impegno.numeroRiaccertato
		boolean competenzaCorrente = prs.isCompetenzaCorrente(); 
		boolean competenzaCompetenza = prs.isCompetenzaCompetenza();
		boolean competenzaFuturi = prs.isCompetenzaFuturi();
		boolean competenzaResiduiRor = prs.isCompetenzaResiduiRor();
				
		//Creo La query per il modulo FIN
		StringBuilder jpql = new StringBuilder("SELECT NEW it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno(mvg.movgestId, movgestTs.movgestTsIdPadre, mvg.movgestNumero, mvg.movgestAnno"
				+ ",movgestTs.movgestTsId, movgestTs.movgestTsCode, dMovgestTsTipo.movgestTsTipoCode) ")
		.append(" FROM SiacTMovgestFin mvg ")
		.append(" left join mvg.siacTMovgestTs movgestTs left join mvg.siacDMovgestTipo dMovgestTipo ")
		.append(" left join movgestTs.siacDMovgestTsTipo dMovgestTsTipo ")
		.append(" left join mvg.siacTBil tBil left join tBil.siacTPeriodo tPeriodo ")
		.append(" left join mvg.siacRMovgestBilElems rBilElem left join rBilElem.siacTBilElem tBilElem  ");
		
		//SIAC-7349
		if(prs.getComponenteBilancioUid()!= null && prs.getComponenteBilancioUid().intValue()!=0){
			jpql.append(" left join rBilElem.siacDBilElemDetCompTipo dBilElemDetCompTipo  ");
		}
		
		//SIAC-6997
		if(!StringUtilsFin.isEmpty(tipoImpegno) || (prs.getStrutturaSelezionataCompetente()!= null && prs.getStrutturaSelezionataCompetente().length()>0)){
			jpql.append(" left join movgestTs.siacRMovgestClasses rMovgestClass left join rMovgestClass.siacTClass tClass ");
		}
		
		//Cig
		if(!StringUtilsFin.isEmpty(cig) ){ 
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cigRAttr left join cigRAttr.siacTAttr cigAtt ");
		}
		
		//Cup
		if(!StringUtilsFin.isEmpty(cup)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cupRAttr left join cupRAttr.siacTAttr cupAtt ");
		}
		
		//SIAC-6997 .....stessa condizione di prima per reanno ...sarebbe da sistemare 
		if((prs.getReanno()!= null && Boolean.TRUE.equals(prs.getReanno()))){ 
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs relAttrs left join relAttrs.siacTAttr tabAttr ");
		}
		
		
		

		boolean annoONumeroProvv = (CommonUtil.maggioreDiZero(annoProvvedimento) || CommonUtil.maggioreDiZero(numeroProvvedimento));
		
		//Inserisco la join del provvedimento
		jpql.append(" left join movgestTs.siacRMovgestTsAttoAmms rTsAttoAmm left join rTsAttoAmm.siacTAttoAmm tAttoAmm ");

		if(!StringUtilsFin.isEmpty(codiceTipoProvvedimento)){
			jpql.append(" left join tAttoAmm.siacDAttoAmmTipo dTipoAtto ");
		}
		
		// struttura amministrativa
		if(uidStrutturaProvvedimento!=null){
			jpql.append(" left join tAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
		}
		
		//soggetto
		if(!StringUtilsFin.isEmpty(codiceCreditore)){
			jpql.append(" left join movgestTs.siacRMovgestTsSogs rSogg left join rSogg.siacTSoggetto sogg ");
		}
		
		//progetto
		if (!StringUtilsFin.isEmpty(codiceProgetto)) {
			jpql.append(" left join movgestTs.siacRMovgestTsProgrammas rProgrammas left join rProgrammas.siacTProgramma tProgramma ");
		}
		
		//cronoprogramma
		if (prs.getUidCronoprogramma() != null && prs.getUidCronoprogramma().intValue() != 0) {
			jpql.append(" left join movgestTs.siacRMovgestTsCronopElemFins rElems left join rElems.siacTCronopElem.siacTCronop tCronop ");
		}
		
		// CR - 1907 RM 
		// aggiunto classe soggetto
		if(!StringUtilsFin.isEmpty(codiceClasseSoggetto)){
			jpql.append(" left join movgestTs.siacRMovgestTsSogclasses rClass left join rClass.siacDSoggettoClasse classe ");
		}
		
		// CR - 1907 RM 
		// aggiuntoNumero e Anno Origine
		if((annoImpegnoOrigine != null && annoImpegnoOrigine != 0) && (numeroImpegnoOrigine != null && numeroImpegnoOrigine != 0)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs annoOriRAttr left join annoOriRAttr.siacTAttr annoOriAtt ");
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs numOriRAttr left join numOriRAttr.siacTAttr numOriAtt ");
		}
		
		
		// CR - 1907 eliminato controllo su articolo !=0 (' && numeroArticolo != 0'), 
		// perchè per regione ci potrebbero essere capitoli con articolo 0 
		//if(uidCapitolo!=null || (numeroCapitolo != null && numeroCapitolo != 0) || (numeroArticolo != null) || (numeroUeb != null && numeroUeb != 0)){
			// capitolo 
		//jpql.append("left join mvg.siacRMovgestBilElems rBilElem left join rBilElem.siacTBilElem tBilElem  ");
		//}
		
		if(!StringUtilsFin.isEmpty(statoOperativo) || !StringUtilsFin.isEmpty(prs.getStatiDaEscludere())){
			jpql.append(" left join movgestTs.siacRMovgestTsStatos rMovgestTsStato left join rMovgestTsStato.siacDMovgestStato dMovgestStato");
		}
		
		//SIAC-6997
		if(prs.isRicercaResiduiRorFlag()){
			jpql.append(" left join movgestTs.siacTMovgestTsDets movgestDet left join movgestDet.siacDMovgestTsDetTipo movgestDetTipo ");
		}
	
		//WHERE 
		jpql.append(" WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
						.append("AND dMovgestTipo.movgestTipoCode = :tipoMovimentoImpegno ")
						.append("AND tPeriodo.anno = :annoBilancio ");
		jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("mvg"));
		jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("movgestTs"));
		jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rBilElem",null, true));
		
		//FIX JIRA SIAC-3386 AGGIUNTE VALIDITA SU CAMPI OPZIONALI:
		if(!StringUtilsFin.isEmpty(codiceCreditore)){
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rSogg"));
		}
		if(!StringUtilsFin.isEmpty(codiceClasseSoggetto)){
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rClass"));
		}
		//
		
		//codice progetto
		if(!StringUtilsFin.isEmpty(codiceProgetto)){
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rProgrammas"));
		}
		
		
		param.put("annoBilancio", String.valueOf(prs.getAnnoEsercizio()));
		param.put("enteProprietarioId", enteUid);
		param.put("tipoMovimentoImpegno", CostantiFin.MOVGEST_TIPO_IMPEGNO);

		
		if(!StringUtilsFin.isEmpty(statoOperativo) || !StringUtilsFin.isEmpty(prs.getStatiDaEscludere()) ){
			
			/*
			 * SIAC-6997
			 * Se solo impegni prendiamo la testata...
			 * in caso di ror vengono presi anche i sub
			 */
			if(soloImpegni){
				jpql.append(" AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTs ");
				param.put("tipoMovGestTs", CostantiFin.MOVGEST_TS_TIPO_TESTATA);
			}
			
			// Rm: 19.06.2015:
			// Se ricerco anche per stato mov. gestione non devo ricercare anche i sub ma solo gli impegni
			if(!StringUtilsFin.isEmpty(statoOperativo)){
				//SIAC-6997 movgestStatoCode 
				if(soloImpegni){
					jpql.append(" AND dMovgestStato.movgestStatoCode = :statoOperativo ");
					param.put("statoOperativo", statoOperativo);
				}else{
					jpql.append(" AND ((dMovgestStato.movgestStatoCode = :statoOperativo ");
					jpql.append("AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestReannoT ) " + 
							"OR " +
							"(dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestReannoS AND  mvg.movgestId in ( "+
							"SELECT mvg2.movgestId "+
							"FROM SiacTMovgestFin mvg2 "+  
							"left join mvg2.siacTMovgestTs movgestTs2 " +
							"left join mvg2.siacDMovgestTipo dMovgestTipo2 "+
							"left join movgestTs2.siacDMovgestTsTipo dMovgestTsTipo2 "+
							"left join movgestTs2.siacRMovgestTsStatos rMovgestTsStato2 left join rMovgestTsStato2.siacDMovgestStato dMovgestStato2 "+
							"where dMovgestStato2.movgestStatoCode = :statoOperativo AND rMovgestTsStato2.dataCancellazione IS NULL ");
					jpql.append("AND dMovgestTsTipo2.movgestTsTipoCode = :tipoMovGestReannoT) "+
									")) ");
					param.put("tipoMovGestReannoT", CostantiFin.MOVGEST_TS_TIPO_TESTATA);
					param.put("tipoMovGestReannoS", CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO);
					param.put("statoOperativo", statoOperativo);
				}
			}
			if(!StringUtilsFin.isEmpty(prs.getStatiDaEscludere()) ){
				String statiDaEscludere = buildElencoPerClausolaIN(prs.getStatiDaEscludere());
				jpql.append(" AND dMovgestStato.movgestStatoCode NOT IN :statiDaEscludere");
				param.put("statiDaEscludere", statiDaEscludere);
			}
			
			param.put(DataValiditaUtil.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rMovgestTsStato"));
			
			
		} else {
			//non e' valorizzato ne lo stato ne gli stati da escludere
			jpql.append(" AND (dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTs OR  dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTsS ) ");
			param.put("tipoMovGestTs", CostantiFin.MOVGEST_TS_TIPO_TESTATA);
			param.put("tipoMovGestTsS", CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO);
		}		

		
		
		//SIAC-6997 da capire in caso ror
		if(flagDaRiaccerto.equalsIgnoreCase("Si")){
			String jpqlRiaccertato = buildClausolaExistVersoTAttr("flagRAttr", "movgestTs", "flagDaRiaccertamento", "boolean_", "S",true,false,false);
			jpql.append(jpqlRiaccertato);
			
			
			if((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0) && (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)){
				String jpqlAnnoRiacc = buildClausolaExistVersoTAttr("flagRAttrAnno", "movgestTs", "annoRiaccertato", "testo", "annoRiacc",false,true,false);
				jpql.append(jpqlAnnoRiacc);
				param.put("annoRiacc", String.valueOf(annoImpegnoRiaccertato));
				
				String jpqlNumRiacc = buildClausolaExistVersoTAttr("flagRAttrNumero", "movgestTs", "numeroRiaccertato", "testo", "numeroRiacc",false,true,false);
				jpql.append(jpqlNumRiacc);
				param.put("numeroRiacc", String.valueOf(numeroImpegnoRiaccertato));
			}
			
			
		}
		
		
		
		
		//Numero e Anno Origine
		if((annoImpegnoOrigine != null && annoImpegnoOrigine != 0) && (numeroImpegnoOrigine != null && numeroImpegnoOrigine != 0)){
			jpql.append(" AND  annoOriAtt.attrCode = 'annoOriginePlur' AND annoOriRAttr.testo = :annoOrigine");
			jpql.append(" AND  numOriAtt.attrCode = 'numeroOriginePlur' AND numOriRAttr.testo = :numeroOrigine");
			param.put("annoOrigine", String.valueOf(annoImpegnoOrigine));
			param.put("numeroOrigine", String.valueOf(numeroImpegnoOrigine));
		}
		

		// Tipo Impegno 
		if(!StringUtilsFin.isEmpty(tipoImpegno)){
			jpql.append(" AND tClass.classifCode = :tipoImpegno ");
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rMovgestClass"));
			param.put("tipoImpegno", tipoImpegno.toUpperCase());
			
		}

		// numero impegno
		if(numeroImpegno!=null){
			jpql.append(" AND mvg.movgestNumero = :numeroImpegno ");
			param.put("numeroImpegno", numeroImpegno);
		}
		

		if(annoImpegno != null && annoImpegno != 0 ){
			jpql.append(" AND mvg.movgestAnno = :annoImpegno ");
			param.put("annoImpegno",annoImpegno);
		}

		
		/*SIAC-6997
		 * In caso di reanno la condizione di competenza è per anno assunzione (anno N)
		 * e residuo (<anno N). Se nel criterio di ricerca viene inserito l anno impegno
		 * questa condizione (come nel caso di ricerca impegno normale) va considerata nella lista 
		 * finale perchè si avrebbe:
		 * AND mvg.movgestAnno = :annoImpegno
		 * AND (mvg.movgestAnno = :annoEsercizio OR mvg.movgestAnno < :annoEsercizio)
		 * 
		 */
		if(prs.isRicercaResiduiRorFlag()){
			/* Da richiesta bisognava prendere il residuo. 
			 * Tale operazione risulta troppo dispensiosa in termini di
			 * performance perchè i dati da filtrare sono calcolati 
			 * Prendiamo l'importo > 0
			 */
			//CONDIZIONE SULL ESISTENZA DELLE MODIFICHE PER IL FILTRAGGIO ROR
			jpql.append(" AND ( ");
			jpql.append(" ( movgestDet.movgestTsDetImporto >0 AND movgestDetTipo.movgestTsDetTipoCode = :tipoAttuale ");
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("movgestDet"));
			jpql.append(" ) ");
			jpql.append(" OR EXISTS ( SELECT movts from SiacTMovgestTsFin movts ");
			jpql.append(" left join movts.siacTMovgestTsDetMods mod ");
			jpql.append(" left join mod.siacRModificaStato modr ");
			jpql.append(" left join modr.siacDModificaStato statomod ");
			jpql.append(" left join modr.siacTModifica modifica ");
			jpql.append(" left join modifica.siacDModificaTipo tipoModifica ");
			jpql.append(" where  movts.movgestTsId = movgestTs.movgestTsId AND statomod.modStatoCode != :annullato ");
			jpql.append(" AND modr.dataCancellazione IS NULL AND (tipoModifica.modTipoCode= :RORM OR  tipoModifica.modTipoCode= :INEROR OR tipoModifica.modTipoCode = :INSROR ");
			jpql.append(" OR tipoModifica.modTipoCode= :REIMP)) ");
			jpql.append(" ) ");
			param.put("tipoAttuale", CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
			
			param.put("annullato", CostantiFin.D_MODIFICA_STATO_ANNULLATO);
			param.put("REIMP", CodiciMotiviModifiche.REIMP.getCodice());
			param.put("RORM", CodiciMotiviModifiche.RORM.getCodice());
			param.put("INEROR", CodiciMotiviModifiche.INEROR.getCodice());
			param.put("INSROR", CodiciMotiviModifiche.INSROR.getCodice());
			
		}
		
		
		
		if(competenzaResiduiRor){
			jpql.append(" AND (mvg.movgestAnno = :anno OR mvg.movgestAnno  < :anno) ");
			param.put("anno",  annoEsercizio);
			
			
		}else{
			//Lascio tutto come era prima
			/*
			 * SIAC-6997
			 * Se da Residui ROR bisognerebbe prendere la competenza corrente 
			 * unita al residuo. impegno semplice considerare il filtraggio
			 * residuo dovrebbe essere competenzaCompetenza ...con anno < del bilancio
			 */
			
				// CR - 1907 RM 
				// aggiunto Impegni Correnti
				if(competenzaCorrente){
					jpql.append(" AND mvg.movgestAnno = :anno ");
					param.put("anno", annoEsercizio);
				}
				
				// CR - 1907 RM 
				// aggiunto Impegni Passati
				if(competenzaCompetenza){
					jpql.append(" AND mvg.movgestAnno  < :anno ");
					param.put("anno", annoEsercizio);
				}
				
				// CR - 1907 RM 
				// aggiunto Impegni Futuri
				if(competenzaFuturi){
					jpql.append(" AND mvg.movgestAnno > :anno ");
					param.put("anno", annoEsercizio);
				}
		}
		
		// Capitolo	
		// jira 1337 ricerca capitolo con 0
		if(uidCapitolo != null && uidCapitolo != 0){			
			jpql.append(" AND tBilElem.elemId = :uidCapitolo");
			
			param.put("uidCapitolo", uidCapitolo);
		} else {
			
			
			// numero capitolo
			if(numeroCapitolo != null && numeroCapitolo != 0){
				jpql.append(" AND tBilElem.elemCode = :numeroCapitolo ");
				param.put("numeroCapitolo", String.valueOf(numeroCapitolo));
			}
			
			// numero articolo
			if(numeroArticolo != null && numeroArticolo != 0){
				jpql.append(" AND tBilElem.elemCode2 = :numeroArticolo ");
				param.put("numeroArticolo", String.valueOf(numeroArticolo));
			}
			
			// numero ueb
			if(numeroUeb != null && numeroUeb != 0){
				jpql.append(" AND tBilElem.elemCode3 = :numeroUeb ");
				param.put("numeroUeb", String.valueOf(numeroUeb));
			}	
		}
		
		
		/*SIAC-7349
		 * 
		 */
		if(prs.getComponenteBilancioUid()!= null && prs.getComponenteBilancioUid().intValue()!=0){
			jpql.append(" AND dBilElemDetCompTipo.elemDetCompTipoId = :elemDetCompTipoId");
			//jpql.append(" AND rBilElem.elemDetCompTipoId = :elemDetCompTipoId");
			
			param.put("elemDetCompTipoId", prs.getComponenteBilancioUid());
		}
		
		
		
		//Ricerca Soggetto
		if(!StringUtilsFin.isEmpty(codiceCreditore)){
			jpql.append(" AND sogg.soggettoCode = :codiceSoggetto ");
			param.put("codiceSoggetto", codiceCreditore);
		}
		
		// Ricerco per classe soggetto
		if(!StringUtilsFin.isEmpty(codiceClasseSoggetto)){
			jpql.append(" AND classe.soggettoClasseCode = :codiceClasse ");				
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rClass"));
			param.put("codiceClasse", codiceClasseSoggetto);
		}
		
		// progetto
		//SIAC-7144
		if(!StringUtilsFin.isEmpty(progetto)){
			jpql.append(" AND UPPER(tProgramma.programmaCode) LIKE UPPER(:programmaCode)");
			String progettoLike = null;
			if(progetto.contains("%")){
				progettoLike = progetto;
			} else {
				progettoLike = '%' + progetto + '%';
			}
			param.put("programmaCode", progettoLike);
		}		
		
		//cronoprogramma
		if (prs.getUidCronoprogramma() != null && prs.getUidCronoprogramma().intValue() != 0) {
			jpql.append(" AND tCronop.cronopId = :cronopId");
			param.put("cronopId", prs.getUidCronoprogramma());
		}
		

		//Provvedimento
		if(idProvvedimento != null && idProvvedimento != 0){
			
			//SIAC-6018 aggiungo condizione di validita su siac_r_movgest_ts_atto_amm:
			jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rTsAttoAmm"));
			//
			
			jpql.append(" AND tAttoAmm.attoammId = :provvedimentoId ");
			param.put("provvedimentoId", idProvvedimento);
		} else {
			
			// 	SIAC-6018 aggiungo condizione di validita su siac_r_movgest_ts_atto_amm:
			if(annoONumeroProvv){
				jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("rTsAttoAmm"));
			}
			//
			
			if(annoProvvedimento != null && annoProvvedimento != 0){
				jpql.append(" AND tAttoAmm.attoammAnno = :annoProvvedimento ");
				param.put("annoProvvedimento", String.valueOf(annoProvvedimento));
			}
			
			if(numeroProvvedimento != null && numeroProvvedimento != 0){
				jpql.append(" AND tAttoAmm.attoammNumero = :numeroProvvedimento ");
				param.put("numeroProvvedimento", numeroProvvedimento);
			}
			
			if(!StringUtilsFin.isEmpty(codiceTipoProvvedimento)){
				jpql.append(" AND dTipoAtto.attoammTipoCode = :codiceTipoProvvedimento ");
				param.put("codiceTipoProvvedimento", codiceTipoProvvedimento);
			}	
			
			
			//aggiunto filtro per sac del provevdimento
			if(uidStrutturaProvvedimento!=null){
				jpql.append(" AND tClassAtto.classifId = :uidStrutturaAmmProvvedimento ");
				param.put("uidStrutturaAmmProvvedimento", uidStrutturaProvvedimento);
			}
		}
		
		//Cig
		if(!StringUtilsFin.isEmpty(cig)){
			jpql.append(" AND  cigAtt.attrCode = 'cig' AND UPPER(cigRAttr.testo) LIKE UPPER(:cigLike) ");
			String cigLike = null;
			if(cig.contains("%")){
				cigLike = cig;
			} else {
				cigLike = '%' + cig + '%';
			}
			param.put("cigLike", cigLike);
		}
		
		//Cup
		if(!StringUtilsFin.isEmpty(cup)){
			jpql.append(" AND  cupAtt.attrCode = 'cup' AND UPPER(cupRAttr.testo) LIKE UPPER(:cupLike) ");
			String cupLike = null;
			if(cup.contains("%")){
				cupLike = cig;
			} else {
				cupLike = '%' + cup + '%';
			}
			param.put("cupLike", cupLike);
		}

		
		//SIAC-6997
		if(prs.getReanno()!= null && Boolean.TRUE.equals(prs.getReanno())){
				jpql.append(" AND ((tabAttr.attrCode = :flagDaReanno AND relAttrs.boolean_ = 'S' ");
				
				if((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0) && (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)){
					String jpqlAnnoRiacc = buildClausolaExistVersoTAttr("flagRAttrAnno", "movgestTs", "annoRiaccertato", "testo", "annoRiacc",false,true,false);
					jpql.append(jpqlAnnoRiacc);
					param.put("annoRiacc", String.valueOf(annoImpegnoRiaccertato));
					String jpqlNumRiacc = buildClausolaExistVersoTAttr("flagRAttrNumero", "movgestTs", "numeroRiaccertato", "testo", "numeroRiacc",false,true,false);
					jpql.append(jpqlNumRiacc);
					param.put("numeroRiacc", String.valueOf(numeroImpegnoRiaccertato));
				}
				
				jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("relAttrs"));		
				jpql.append("AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestReannoT) " + 
								"OR " +
								"(dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestReannoS AND  mvg.movgestId in ( "+
								"SELECT mvg2.movgestId "+
								"FROM SiacTMovgestFin mvg2 "+  
								"left join mvg2.siacTMovgestTs movgestTs2 " +
								"left join mvg2.siacDMovgestTipo dMovgestTipo2 "+
								"left join movgestTs2.siacDMovgestTsTipo dMovgestTsTipo2 "+
								"left join movgestTs2.siacRMovgestTsAttrs relAttrs2 "+
								"left join relAttrs2.siacTAttr tabAttr2 "+
								"where tabAttr2.attrCode = :flagDaReanno AND relAttrs2.boolean_ = 'S' ");
				
				
				if((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0) && (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)){
					String jpqlAnnoRiacc = buildClausolaExistVersoTAttr("flagRAttrAnno", "movgestTs2", "annoRiaccertato", "testo", "annoRiacc1",false,true,false);
					jpql.append(jpqlAnnoRiacc);
					param.put("annoRiacc1", String.valueOf(annoImpegnoRiaccertato));
					String jpqlNumRiacc = buildClausolaExistVersoTAttr("flagRAttrNumero", "movgestTs2", "numeroRiaccertato", "testo", "numeroRiacc1",false,true,false);
					jpql.append(jpqlNumRiacc);
					param.put("numeroRiacc1", String.valueOf(numeroImpegnoRiaccertato));
					
				}
				jpql.append(" AND ").append(DataValiditaUtil.validitaForQuery("relAttrs2"));				
				jpql.append("AND dMovgestTsTipo2.movgestTsTipoCode = :tipoMovGestReannoT) "+
								")) ");
				param.put("flagDaReanno", "flagDaReanno");
				param.put("tipoMovGestReannoT", CostantiFin.MOVGEST_TS_TIPO_TESTATA);
				param.put("tipoMovGestReannoS", CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO);
				//param.put("anno", annoEsercizio);
			
		}
		
//		if(prs.getStrutturaSelezionataCompetente()!= null && prs.getStrutturaSelezionataCompetente().length()>0){
//			try{
//				Integer classificatoreId = Integer.parseInt(prs.getStrutturaSelezionataCompetente());
//				//jpql.append(" AND tClass.classifId = :classificatoreId ");
//				
//				jpql.append(" AND ((tClass.classifId = :classificatoreId AND rMovgestClass.dataCancellazione IS NULL AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestStrutturaT) " + 
//								"OR " +
//								"(dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestStrutturaS AND  mvg.movgestId in ( "+
//								"SELECT mvg1.movgestId "+
//								"FROM SiacTMovgestFin mvg1 "+  
//								"left join mvg1.siacTMovgestTs movgestTs1 " +
//								"left join mvg1.siacDMovgestTipo dMovgestTipo1 "+
//								"left join movgestTs1.siacDMovgestTsTipo dMovgestTsTipo1 "+
//								"left join movgestTs1.siacRMovgestClasses rMovgestClass1 "+
//								"left join rMovgestClass1.siacTClass tClass1 "+
//								"where  tClass1.classifId = :classificatoreId AND rMovgestClass1.dataCancellazione IS NULL AND dMovgestTsTipo1.movgestTsTipoCode = :tipoMovGestStrutturaT) "+
//								")) ");
//				param.put("classificatoreId", classificatoreId);
//				param.put("tipoMovGestStrutturaT", CostantiFin.MOVGEST_TS_TIPO_TESTATA);
//				param.put("tipoMovGestStrutturaS", CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO);
//				//param.put("anno", annoEsercizio);
//			}catch(Exception e){
//				log.error("ricercaImpegniSubImpegni", e.getMessage());
//			}
//		}
		
		//SIAC-7486
		if(prs.getListStrutturaCompetenteInt()!= null && !prs.getListStrutturaCompetenteInt().isEmpty()){
			try{
				
				//jpql.append(" AND tClass.classifId = :classificatoreId ");
				String inConditionac = buildInCondition(prs.getListStrutturaCompetenteInt());
				jpql.append(" AND ((tClass.classifId IN " + inConditionac +" AND rMovgestClass.dataCancellazione IS NULL AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestStrutturaT) " + 
								"OR " +
								"(dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestStrutturaS AND  mvg.movgestId in ( "+
								"SELECT mvg1.movgestId "+
								"FROM SiacTMovgestFin mvg1 "+  
								"left join mvg1.siacTMovgestTs movgestTs1 " +
								"left join mvg1.siacDMovgestTipo dMovgestTipo1 "+
								"left join movgestTs1.siacDMovgestTsTipo dMovgestTsTipo1 "+
								"left join movgestTs1.siacRMovgestClasses rMovgestClass1 "+
								"left join rMovgestClass1.siacTClass tClass1 "+
								"where  tClass1.classifId IN " + inConditionac +" AND rMovgestClass1.dataCancellazione IS NULL AND dMovgestTsTipo1.movgestTsTipoCode = :tipoMovGestStrutturaT) "+
								")) ");
				
				param.put("tipoMovGestStrutturaT", CostantiFin.MOVGEST_TS_TIPO_TESTATA);
				param.put("tipoMovGestStrutturaS", CostantiFin.MOVGEST_TS_TIPO_SUBIMPEGNO);
				//param.put("anno", annoEsercizio);
			}catch(Exception e){
				log.error("ricercaImpegniSubImpegni", e.getMessage());
			}
		}
		
		
		//jpql.append(" ORDER BY mvg.movgestAnno, mvg.movgestNumero ");
		jpql.append(" ORDER BY mvg.movgestAnno , mvg.movgestNumero, movgestTs.movgestTsId "); //SIAC-6997

		String jpqlQuery = jpql.toString();
		//Creo la query effettiva
		Query query =  createQuery(jpqlQuery, param);			
		listaID = query.getResultList();
		//Termino restituendo l'oggetto di ritorno: 
        return listaID;
	}
	
	
	//SIAC-7486
	private String buildInCondition(List<Integer> idList){
		StringBuilder res =new StringBuilder();
		int sizeList = idList.size();
		if(idList!= null && !idList.isEmpty()){
			int count =0;
			res.append("(");
			for(Integer id : idList){
				
				res.append(id.toString());
				if(count<sizeList-1){
					res.append(",");
				}
				count++;
			}
			res.append(")");
		}
		return res.toString();
	}
	
	
}

