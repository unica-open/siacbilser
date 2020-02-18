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

import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.CodificaImportoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ConsultaDettaglioImpegnoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaImpSubParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaImpegnoParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno;
import it.csi.siac.siacfinser.integration.entity.ext.IdMovgestSubmovegest;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtils;

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
		param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, nowDate);
		
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
		if(!StringUtils.isEmpty(uidStrutturaAmmProvvedimento)){
			jpql.append(" left join tAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
		}
		
		if(!StringUtils.isEmpty(idTipoProvvedimento)){
			jpql.append(" left join tAttoAmm.siacDAttoAmmTipo dTipoAtto ");
		}
		
		
		
		if(!StringUtils.isEmpty(stato)){
			jpql.append(" left join movgestTs.siacRMovgestTsStatos rMovgestTsStato left join rMovgestTsStato.siacDMovgestStato dMovgestStato ");
		}
		
		if(!StringUtils.isEmpty(tipoImpegno)){
			jpql.append(" left join movgestTs.siacRMovgestClasses rMovgestClass left join rMovgestClass.siacTClass tClass ");
		}
		
		/**
		 * TODO ATTRIBUTI JOIN
		 */
		
		//soggetto
		if(!StringUtils.isEmpty(codiceCreditore)){
			jpql.append(" left join movgestTs.siacRMovgestTsSogs rSogg left join rSogg.siacTSoggetto sogg ");
		}
		
		//classe soggetto
		if(!StringUtils.isEmpty(codiceClasseSoggetto)){
			jpql.append(" left join movgestTs.siacRMovgestTsSogclasses rClass left join rClass.siacDSoggettoClasse classe ");
		}
		
		if(!StringUtils.isEmpty(elementoPianoDeiConti)){
			jpql.append(" left join movgestTs.siacRMovgestClasses rPiano left join rPiano.siacTClass piano ");
		}
		
		//Cig
		if(!StringUtils.isEmpty(cig)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cigRAttr left join cigRAttr.siacTAttr cigAtt ");
		}
		
		//Cup
		if(!StringUtils.isEmpty(cup)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cupRAttr left join cupRAttr.siacTAttr cupAtt ");
		}
		
		if(!StringUtils.isEmpty(pdc)){
			jpql.append(" left join movgestTs.siacRMovgestClasses rMovgestPdcClass left join rMovgestPdcClass.siacTClass tPdcClass ");
		}
		
		//Riaccertato
		if(flagDaRiaccerto.equalsIgnoreCase("Si")){
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
		if(!StringUtils.isEmpty(progetto)){
			jpql.append(" left join movgestTs.siacRMovgestTsProgrammas rProgrammas left join rProgrammas.siacTProgramma tProgramma ");
		}
		
		/**
		 * TODO WHERE CLAUSE
		 */
		
		jpql.append(" WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mvg.siacDMovgestTipo.movgestTipoCode = 'I' AND " +
				" (movgestTs.siacDMovgestTsTipo.movgestTsTipoCode = 'T' or movgestTs.siacDMovgestTsTipo.movgestTsTipoCode='S')" );
		
		param.put("enteProprietarioId", enteUid);
		
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("mvg"));
		
		//Ricerca Soggetto
		if(!StringUtils.isEmpty(codiceCreditore)){
			jpql.append(" AND sogg.soggettoCode = :codiceSoggetto ");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rSogg"));
			param.put("codiceSoggetto", codiceCreditore);
		}
		
		if(!StringUtils.isEmpty(codiceClasseSoggetto)){
			jpql.append(" AND classe.soggettoClasseCode = :codiceClasse ");				
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rClass"));
			param.put("codiceClasse", codiceClasseSoggetto);
		}
		
		if(!StringUtils.isEmpty(elementoPianoDeiConti)){
			jpql.append(" AND piano.classifCode = :codicePiano");
			param.put("codicePiano", elementoPianoDeiConti);
		}
		
		//Ricerca Stato
		if(!StringUtils.isEmpty(stato)){
			jpql.append(" AND dMovgestStato.movgestStatoCode = :stato ");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rMovgestTsStato"));
			param.put("stato", stato.toUpperCase());
			
		}
		
		
		//Tipo Impegno jpql.append(" left join movgestTs.siacRMovgestClass rMovgestClass left join rMovgestClass.siacDClassTipo dMovgestClassTipo ");
		if(!StringUtils.isEmpty(tipoImpegno)){
			jpql.append(" AND tClass.classifCode = :tipoImpegno ");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rMovgestClass"));
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
			
			if(!StringUtils.isEmpty(idTipoProvvedimento)){
				jpql.append(" AND dTipoAtto.attoammTipoId = :idTipoProvvedimento");
				param.put("idTipoProvvedimento", Integer.valueOf(idTipoProvvedimento));
			}
			
			//aggiunto filtro per sac del provevdimento
			if(!StringUtils.isEmpty(uidStrutturaAmmProvvedimento)){
				jpql.append(" AND tClassAtto.classifId = :uidStrutturaAmmProvvedimento ");
				param.put("uidStrutturaAmmProvvedimento", Integer.valueOf(uidStrutturaAmmProvvedimento));
			}
			
		}
		
		/**
		 * TODO ATTRIBUTI WHERE CLAUSE
		 */
		
		//Cig
		if(!StringUtils.isEmpty(cig)){
			String clausolaCup = buildClausolaRicercaAttr(Constanti.T_ATTR_CODE_CIG, cig, "cigAtt", "cigRAttr", "cigLike", param);
			jpql.append(clausolaCup);
		}
		
		//Cup
		if(!StringUtils.isEmpty(cup)){
			String clausolaCup = buildClausolaRicercaAttr(Constanti.T_ATTR_CODE_CUP, cup, "cupAtt", "cupRAttr", "cupLike", param);
			jpql.append(clausolaCup);
		}
		 
		
		//PDC Finanziario
		if(!StringUtils.isEmpty(pdc)){
			String clausolaPDC = buildClausolaRicercaTClass(Constanti.getCodiciPianoDeiConti(), pdc, "tPdcClass", "rMovgestPdcClass", "pdcLike", param);
			jpql.append(clausolaPDC);
		}
		
		//Riaccertato
		if(flagDaRiaccerto.equalsIgnoreCase("Si")){
			jpql.append(" AND  flagAtt.attrCode = 'flagDaRiaccertamento' AND UPPER(flagRAttr.boolean_) = 'S' ");
		}
		
		//Numero e Anno Riaccertato
		if((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0) && (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)){
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
		if(!StringUtils.isEmpty(progetto)){
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
		//if(query.getResultList().size() > Constanti.MAX_RIGHE_ESTRAIBILI){
		//	listaImpegni = query.setMaxResults(Constanti.MAX_RIGHE_ESTRAIBILI + 1).getResultList();
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
	public List<IdImpegnoSubimpegno> ricercaImpegniSubImpegni(Integer enteUid, RicercaImpSubParamDto prs, boolean soloImpegni) {
		
		
		List<IdImpegnoSubimpegno> listaID = new ArrayList<IdImpegnoSubimpegno>();
		
		Map<String,Object> param = new HashMap<String, Object>();
		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, nowDate);
		
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
		
				
		//Creo La query per il modulo FIN
		StringBuilder jpql = new StringBuilder("SELECT NEW it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno(mvg.movgestId, movgestTs.movgestTsIdPadre, mvg.movgestNumero, mvg.movgestAnno) ")
		.append(" FROM SiacTMovgestFin mvg ")
		.append(" left join mvg.siacTMovgestTs movgestTs left join mvg.siacDMovgestTipo dMovgestTipo ")
		.append(" left join movgestTs.siacDMovgestTsTipo dMovgestTsTipo ")
		.append(" left join mvg.siacTBil tBil left join tBil.siacTPeriodo tPeriodo ")
		.append(" left join mvg.siacRMovgestBilElems rBilElem left join rBilElem.siacTBilElem tBilElem  ");
		
		if(!StringUtils.isEmpty(tipoImpegno)){
			jpql.append(" left join movgestTs.siacRMovgestClasses rMovgestClass left join rMovgestClass.siacTClass tClass ");
		}
		
		//Cig
		if(!StringUtils.isEmpty(cig)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cigRAttr left join cigRAttr.siacTAttr cigAtt ");
		}
		
		//Cup
		if(!StringUtils.isEmpty(cup)){
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cupRAttr left join cupRAttr.siacTAttr cupAtt ");
		}

		boolean annoONumeroProvv = (CommonUtils.maggioreDiZero(annoProvvedimento) || CommonUtils.maggioreDiZero(numeroProvvedimento));
		
		//Inserisco la join del provvedimento
		jpql.append(" left join movgestTs.siacRMovgestTsAttoAmms rTsAttoAmm left join rTsAttoAmm.siacTAttoAmm tAttoAmm ");

		if(!StringUtils.isEmpty(codiceTipoProvvedimento)){
			jpql.append(" left join tAttoAmm.siacDAttoAmmTipo dTipoAtto ");
		}
		
		// struttura amministrativa
		if(uidStrutturaProvvedimento!=null){
			jpql.append(" left join tAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
		}
		
		//soggetto
		if(!StringUtils.isEmpty(codiceCreditore)){
			jpql.append(" left join movgestTs.siacRMovgestTsSogs rSogg left join rSogg.siacTSoggetto sogg ");
		}
		
		//progetto
		if (!StringUtils.isEmpty(codiceProgetto)) {
			jpql.append(" left join movgestTs.siacRMovgestTsProgrammas rProgrammas left join rProgrammas.siacTProgramma tProgramma ");
		}
		
		//cronoprogramma
		if (prs.getUidCronoprogramma() != null && prs.getUidCronoprogramma().intValue() != 0) {
			jpql.append(" left join movgestTs.siacRMovgestTsCronopElemFins rElems left join rElems.siacTCronopElem.siacTCronop tCronop ");
		}
		
		// CR - 1907 RM 
		// aggiunto classe soggetto
		if(!StringUtils.isEmpty(codiceClasseSoggetto)){
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
		
		if(!StringUtils.isEmpty(statoOperativo) || !StringUtils.isEmpty(prs.getStatiDaEscludere())){
			jpql.append(" left join movgestTs.siacRMovgestTsStatos rMovgestTsStato left join rMovgestTsStato.siacDMovgestStato dMovgestStato");
		}
	
		//WHERE 
		jpql.append(" WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
						.append("AND dMovgestTipo.movgestTipoCode = :tipoMovimentoImpegno ")
						.append("AND tPeriodo.anno = :annoBilancio ");
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("mvg"));
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("movgestTs"));
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rBilElem",null, true));
		
		//FIX JIRA SIAC-3386 AGGIUNTE VALIDITA SU CAMPI OPZIONALI:
		if(!StringUtils.isEmpty(codiceCreditore)){
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rSogg"));
		}
		if(!StringUtils.isEmpty(codiceClasseSoggetto)){
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rClass"));
		}
		//
		
		//codice progetto
		if(!StringUtils.isEmpty(codiceProgetto)){
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rProgrammas"));
		}
		
		
		param.put("annoBilancio", String.valueOf(prs.getAnnoEsercizio()));
		param.put("enteProprietarioId", enteUid);
		param.put("tipoMovimentoImpegno", Constanti.MOVGEST_TIPO_IMPEGNO);

		
		if(!StringUtils.isEmpty(statoOperativo) || !StringUtils.isEmpty(prs.getStatiDaEscludere()) ){
			
			jpql.append(" AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTs ");
			param.put("tipoMovGestTs", Constanti.MOVGEST_TS_TIPO_TESTATA);
			// Rm: 19.06.2015:
			// Se ricerco anche per stato mov. gestione non devo ricercare anche i sub ma solo gli impegni
			
			
			if(!StringUtils.isEmpty(statoOperativo)){
				jpql.append(" AND dMovgestStato.movgestStatoCode = :statoOperativo ");
				param.put("statoOperativo", statoOperativo);
			}
			if(!StringUtils.isEmpty(prs.getStatiDaEscludere()) ){
				String statiDaEscludere = buildElencoPerClausolaIN(prs.getStatiDaEscludere());
				jpql.append(" AND dMovgestStato.movgestStatoCode NOT IN :statiDaEscludere");
				param.put("statiDaEscludere", statiDaEscludere);
			}
			
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, nowDate);
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rMovgestTsStato"));
			
			
		} else {
			//non e' valorizzato ne lo stato ne gli stati da escludere
			jpql.append(" AND (dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTs OR  dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTsS ) ");
			param.put("tipoMovGestTs", Constanti.MOVGEST_TS_TIPO_TESTATA);
			param.put("tipoMovGestTsS", Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO);
		}		

		if(flagDaRiaccerto.equalsIgnoreCase("Si")){
			String jpqlRiaccertato = buildClausolaExistVersoTAttr("flagRAttr", "movgestTs", "flagDaRiaccertamento", "boolean_", "S",true,false,false);
			jpql.append(jpqlRiaccertato);
		}
		
		if((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0) && (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)){
			String jpqlAnnoRiacc = buildClausolaExistVersoTAttr("flagRAttrAnno", "movgestTs", "annoRiaccertato", "testo", "annoRiacc",false,true,false);
			jpql.append(jpqlAnnoRiacc);
			param.put("annoRiacc", String.valueOf(annoImpegnoRiaccertato));
			
			String jpqlNumRiacc = buildClausolaExistVersoTAttr("flagRAttrNumero", "movgestTs", "numeroRiaccertato", "testo", "numeroRiacc",false,true,false);
			jpql.append(jpqlNumRiacc);
			param.put("numeroRiacc", String.valueOf(numeroImpegnoRiaccertato));
		}
		
		
		//Numero e Anno Origine
		if((annoImpegnoOrigine != null && annoImpegnoOrigine != 0) && (numeroImpegnoOrigine != null && numeroImpegnoOrigine != 0)){
			jpql.append(" AND  annoOriAtt.attrCode = 'annoOriginePlur' AND annoOriRAttr.testo = :annoOrigine");
			jpql.append(" AND  numOriAtt.attrCode = 'numeroOriginePlur' AND numOriRAttr.testo = :numeroOrigine");
			param.put("annoOrigine", String.valueOf(annoImpegnoOrigine));
			param.put("numeroOrigine", String.valueOf(numeroImpegnoOrigine));
		}
		

		// Tipo Impegno 
		if(!StringUtils.isEmpty(tipoImpegno)){
			jpql.append(" AND tClass.classifCode = :tipoImpegno ");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rMovgestClass"));
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
		
		//Ricerca Soggetto
		if(!StringUtils.isEmpty(codiceCreditore)){
			jpql.append(" AND sogg.soggettoCode = :codiceSoggetto ");
			param.put("codiceSoggetto", codiceCreditore);
		}
		
		// Ricerco per classe soggetto
		if(!StringUtils.isEmpty(codiceClasseSoggetto)){
			jpql.append(" AND classe.soggettoClasseCode = :codiceClasse ");				
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rClass"));
			param.put("codiceClasse", codiceClasseSoggetto);
		}
		
		// progetto
		//SIAC-7144
		if(!StringUtils.isEmpty(progetto)){
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
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rTsAttoAmm"));
			//
			
			jpql.append(" AND tAttoAmm.attoammId = :provvedimentoId ");
			param.put("provvedimentoId", idProvvedimento);
		} else {
			
			// 	SIAC-6018 aggiungo condizione di validita su siac_r_movgest_ts_atto_amm:
			if(annoONumeroProvv){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rTsAttoAmm"));
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
			
			if(!StringUtils.isEmpty(codiceTipoProvvedimento)){
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
		if(!StringUtils.isEmpty(cig)){
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
		if(!StringUtils.isEmpty(cup)){
			jpql.append(" AND  cupAtt.attrCode = 'cup' AND UPPER(cupRAttr.testo) LIKE UPPER(:cupLike) ");
			String cupLike = null;
			if(cup.contains("%")){
				cupLike = cig;
			} else {
				cupLike = '%' + cup + '%';
			}
			param.put("cupLike", cupLike);
		}

		jpql.append(" ORDER BY mvg.movgestAnno, mvg.movgestNumero ");

		String jpqlQuery = jpql.toString();
		//Creo la query effettiva
		Query query =  createQuery(jpqlQuery, param);			
		listaID = query.getResultList();
		//Termino restituendo l'oggetto di ritorno: 
        return listaID;
	}
	
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
		
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery(aliasRAttr));
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery(aliasRAttr+".siacTAttr"));
		jpql.append(")");
			
		clausola = jpql.toString();
		return clausola;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IdMovgestSubmovegest> ricercaImpegniSubImpegniPerVociMutuo(Integer idMutuo, Integer enteUid, RicercaImpSubParamDto prs, boolean soloImpegni) {
		
		
		List<IdMovgestSubmovegest> listaID = new ArrayList<IdMovgestSubmovegest>();
		
		Map<String,Object> param = new HashMap<String, Object>();
		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, nowDate);
		
		// PARAMETRI DI INPUT
		
		// usato solo da ricerca impegni e subimpegni
		String tipoImpegno = prs.getTipoImpegno();
		String statoOperativo= prs.getStato();
		
		Integer annoImpegno = prs.getAnnoImpegno(); //Impegno.annoMovimento
	
		//PROVVEDIMENTO
		Integer annoProvvedimento = prs.getAnnoProvvedimento();
		Integer numeroProvvedimento = prs.getNumeroProvvedimento();
		String codiceTipoProvvedimento ="";
		if(prs.getTipoProvvedimento()!=null){
			codiceTipoProvvedimento = prs.getTipoProvvedimento().getCodice();
		}	
		Integer uidStrutturaProvvedimento = prs.getUidStrutturaAmministrativoContabile();
		//
				
		//Creo La query per il modulo FIN
		StringBuilder jpql = new StringBuilder("SELECT NEW it.csi.siac.siacfinser.integration.entity.ext.IdMovgestSubmovegest"
				+ "(mvg.movgestId, movgestTs.movgestTsIdPadre, movgestTs.movgestTsId , mvg.movgestNumero, mvg.movgestAnno) ")
		.append("FROM SiacTMovgestFin mvg ")
		.append("left join mvg.siacTMovgestTs movgestTs left join mvg.siacDMovgestTipo dMovgestTipo ")
		.append("left join movgestTs.siacDMovgestTsTipo dMovgestTsTipo ")
		.append(" left join mvg.siacTBil tBil left join tBil.siacTPeriodo tPeriodo ");
		
		
		if(!StringUtils.isEmpty(tipoImpegno)){
			jpql.append(" left join movgestTs.siacRMovgestClasses rMovgestClass left join rMovgestClass.siacTClass tClass ");
		}
		

		//Inserisco la join del provvedimento
		jpql.append(" left join movgestTs.siacRMovgestTsAttoAmms rTsAttoAmm left join rTsAttoAmm.siacTAttoAmm tAttoAmm ");

		if(!StringUtils.isEmpty(codiceTipoProvvedimento)){
			jpql.append(" left join tAttoAmm.siacDAttoAmmTipo dTipoAtto ");
		}
		
		// struttura amministrativa
		if(uidStrutturaProvvedimento!=null){
			jpql.append(" left join tAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
		}

		
		if(!StringUtils.isEmpty(statoOperativo)){
			jpql.append(" left join movgestTs.siacRMovgestTsStatos rMovgestTsStato left join rMovgestTsStato.siacDMovgestStato dMovgestStato");
		}
	
		//WHERE 
		jpql.append(" WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
						.append("AND dMovgestTipo.movgestTipoCode = :tipoMovimentoImpegno ")
						.append("AND tPeriodo.anno = :annoBilancio ");
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("mvg"));
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("movgestTs"));
		param.put("annoBilancio", String.valueOf(prs.getAnnoEsercizio()));
		param.put("enteProprietarioId", enteUid);
		param.put("tipoMovimentoImpegno", Constanti.MOVGEST_TIPO_IMPEGNO);
		
		
		
		//CONDIZIONE CHE ESCLUDE GLI IMPEGNI/SUB CHE GIA' HANNO UNA VOCE DI MUTUO VERSO IL MUTUO INDICATO:
		String test = " AND  mvg.movgestId NOT IN ( "
				+ " SELECT srmvmf.siacTMovgestTs.movgestTsId FROM SiacRMutuoVoceMovgestFin srmvmf  "
				+ " WHERE "
				+ " srmvmf.siacTMutuoVoce.siacTMutuo.mutId = :idMutuo "
				+ " ) ";
		jpql.append(test);
		param.put("idMutuo", idMutuo);
		//

		
		if(statoOperativo == null){
			jpql.append(" AND (dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTs OR  dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTsS ) ");
			param.put("tipoMovGestTs", Constanti.MOVGEST_TS_TIPO_TESTATA);
			param.put("tipoMovGestTsS", Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO);
		}else{
			
			jpql.append(" AND dMovgestStato.movgestStatoCode = :statoOperativo ");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rMovgestTsStato"));
			param.put("statoOperativo", statoOperativo);
		}


		// Tipo Impegno 
		if(!StringUtils.isEmpty(tipoImpegno)){
			
			jpql.append(" AND (");
			
			//IMP:
			jpql.append(" ( movgestTs.movgestTsIdPadre is null ");
			jpql.append(" AND tClass.classifCode = :tipoImpegno ");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rMovgestClass"));
			param.put("tipoImpegno", tipoImpegno.toUpperCase());
			jpql.append(" )");
			//
			
			//SUB:
			jpql.append(" OR ( movgestTs.movgestTsIdPadre is not null ");
			
			jpql.append(" AND movgestTs.movgestTsIdPadre IN (");
			
			jpql.append(" SELECT movgestTs2.movgestTsId FROM SiacTMovgestTsFin movgestTs2  ");
			jpql.append(" left join movgestTs2.siacRMovgestClasses rMovgestClass2 left join rMovgestClass2.siacTClass tClass2 ");
			jpql.append(" WHERE ");
			
			//FIX PER JIRA SIAC-3933:
			jpql.append("  movgestTs2.movgestTsIdPadre is null  ");
			//jpql.append(" AND movgestTs2.movgestTsId = movgestTs.movgestTsIdPadre ");
			
			jpql.append(" AND tClass2.classifCode = :tipoImpegno2 ");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rMovgestClass2"));
			param.put("tipoImpegno2", tipoImpegno.toUpperCase());
			
			jpql.append(" )");//END EXISTS
			
			
			jpql.append(" )");//END OR ( movgestTs.movgestTsIdPadre is not null
			
			
			jpql.append(" )");
		}

		if(annoImpegno != null && annoImpegno != 0 ){
			jpql.append(" AND mvg.movgestAnno = :annoImpegno ");
			param.put("annoImpegno",annoImpegno);
		}
		
		//Provvedimento
		if(annoProvvedimento != null && annoProvvedimento != 0){
			jpql.append(" AND tAttoAmm.attoammAnno = :annoProvvedimento ");
			param.put("annoProvvedimento", String.valueOf(annoProvvedimento));
		}
		
		if(numeroProvvedimento != null && numeroProvvedimento != 0){
			jpql.append(" AND tAttoAmm.attoammNumero = :numeroProvvedimento ");
			param.put("numeroProvvedimento", numeroProvvedimento);
		}
		
		if(!StringUtils.isEmpty(codiceTipoProvvedimento)){
			jpql.append(" AND dTipoAtto.attoammTipoCode = :codiceTipoProvvedimento ");
			param.put("codiceTipoProvvedimento", codiceTipoProvvedimento);
		}	
		//aggiunto filtro per sac del provevdimento
		if(uidStrutturaProvvedimento!=null){
			jpql.append(" AND tClassAtto.classifId = :uidStrutturaAmmProvvedimento ");
			param.put("uidStrutturaAmmProvvedimento", uidStrutturaProvvedimento);
		}
		//
		

		jpql.append(" ORDER BY mvg.movgestAnno, mvg.movgestNumero, movgestTs.movgestTsCode ");

				
		//Creo la query effettiva
		String jpqlString = jpql.toString();
		
		Query query =  createQuery(jpqlString, param);			
		listaID = query.getResultList();
		//Termino restituendo l'oggetto di ritorno: 
		
		listaID = scremaDoppioni(listaID);
		
        return listaID;
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
	
	@SuppressWarnings("unchecked")
	public List<CodificaImportoDto> calcolaSommaImportiVociMutuo(List<Integer> idMovGestTsList){
		List<CodificaImportoDto> elencoCodificaImportoDto = new ArrayList<CodificaImportoDto>();
			
		String clausolaIn = "";
		int i=0;
		for(Integer idIt : idMovGestTsList){
			if(i>0){
				clausolaIn = clausolaIn + " , ";
			}
			String idParamNameIt = "movgestTsId" + i;
			clausolaIn = clausolaIn + ":" + idParamNameIt;
			i++;
		}
		
		String condizioniDiValidita =
				buildCondizioneValiditaSql("a", true) +
				" and " +
				buildCondizioneValiditaSql("b", true) +
				" and " +
				buildCondizioneValiditaSql("d", true);
		
		String sql = "select * from ( "+
				" select c.movgest_ts_id idmovgestts, "+
						" sum(a.mut_voce_importo_attuale) "+
						" from siac_t_mutuo_voce a , siac_r_mutuo_voce_movgest b, siac_t_movgest_ts c, siac_t_movgest_ts_det d, "+
						" siac_d_movgest_ts_det_tipo e "+
						" where a.mut_voce_id=b.mut_voce_id "+
						" and c.movgest_ts_id=b.movgest_ts_id "+
						" and d.movgest_ts_id=c.movgest_ts_id "+
						" and d.movgest_ts_det_tipo_id=e.movgest_ts_det_tipo_id "+
						" and e.movgest_ts_det_tipo_code='A' "+
						" and c.movgest_ts_id IN ("+clausolaIn+")" +
						" and " + condizioniDiValidita +
						" group by  "+
						" 	c.movgest_ts_id "+
						" 	) tb ";
		
		Query query = entityManager.createNativeQuery(sql);
		
		i=0;
		for(Integer idIt : idMovGestTsList){
			String idParamNameIt = "movgestTsId" + i;
			query.setParameter(idParamNameIt, idIt.intValue());	
			i++;
		}
		
		List<Object[]> result = (List<Object[]>) query.getResultList();
		elencoCodificaImportoDto = mapResultToElencoCodificaImportoDto(result);
			
		return elencoCodificaImportoDto;
	}

	
}

