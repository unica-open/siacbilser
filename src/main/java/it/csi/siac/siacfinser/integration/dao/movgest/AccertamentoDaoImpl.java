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
import it.csi.siac.siacfinser.integration.dao.common.dto.ConsultaDettaglioAccertamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaAccSubAccParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaAccertamentoParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.ext.IdAccertamentoSubAccertamento;
import it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtils;

@Component
@Transactional
public class AccertamentoDaoImpl extends AbstractDao<SiacTMovgestFin, Integer>
		implements AccertamentoDao {

	/**
	 * E' il metodo di "engine" di ricerca degli accertamenti. Utilizzato sia
	 * per avere un'anteprima del numero di risultati attesi (rispetto al filtro
	 * indicato) sia per recuperare tutti i dati (rispetto al filtro indicato)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTMovgestFin> ricercaAccertamenti(Integer enteUid,
			RicercaAccertamentoParamDto prs) {

		List<SiacTMovgestFin> listaAccertamenti = new ArrayList<SiacTMovgestFin>();

		Map<String, Object> param = new HashMap<String, Object>();

		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, nowDate);

		// PARAMETRI DI INPUT
		Integer annoBilancio = prs.getAnnoBilancio();
		// RM Commento JIRA 1830:
		// in realtà non è l'anno di esercizio ma dell'accertamento, solo che
		// questi parametri arrivano da due punti diversi
		// e se lo cambio non funge piu la ricerca non da compilazione guidata
		Integer annoImpegno = prs.getAnnoEsercizio();
		Integer numeroImpegno = prs.getNumeroImpegno(); // Impegno.numero
		String stato = prs.getStato(); // StatoOperativoMovimentoGestione.stato
		String progetto = prs.getProgetto(); // CodiceProgetto.codice
		String flagDaRiaccerto = prs.getFlagDaRiaccertamento(); // Impegno.flagDaRiaccentramento
		String codiceCreditore = prs.getCodiceCreditore(); // Soggetto.codice
		boolean competenzaCorrente = prs.isCompetenzaCorrente();
		boolean competenzaCompetenza = prs.isCompetenzaCompetenza();
		boolean competenzaFuturi = prs.isCompetenzaFuturi();
		Integer annoImpegnoRiaccertato = prs.getAnnoImpegnoRiaccertato(); // Impegno.annoRiaccertato
		Integer annoImpegnoOrigine = prs.getAnnoImpegnoOrigine(); // Impegno.annoOrigine
		Integer numeroImpegnoOrigine = prs.getNumeroImpegnoOrigine();// Impegno.numeroOrigine
		Integer numeroImpegnoRiaccertato = prs.getNumeroImpegnoRiaccertato(); // Impegno.numeroRiaccertato
		Integer uidCapitolo = prs.getUidCapitolo();

		String codiceClasseSoggetto = prs.getCodiceClasseSoggetto();
		String pdc = prs.getElementoPianoDeiConti(); // PDC Finanziario

		Integer uidProvvedimento = prs.getUidProvvedimento();
		Integer annoProvvedimento = prs.getAnnoProvvedimento();
		Integer numeroProvvedimento = prs.getNumeroProvvedimento();
		String idTipoProvvedimento = prs.getTipoProvvedimento();
		String uidStrutturaAmmProvvedimento = prs
				.getStrutturaAmministrativoContabileDelProvvedimento();

		Integer numeroCapitolo = prs.getNumeroCapitolo();
		Integer numeroArticolo = prs.getNumeroArticolo();
		Integer numeroUeb = prs.getNumeroUEB();

		// Creo La query per il modulo FIN
		StringBuilder jpql = new StringBuilder(
				"SELECT DISTINCT mvg FROM SiacTMovgestFin mvg "
						+ ", IN(mvg.siacTMovgestTs) movgestTs ");

		if (!StringUtils.isEmpty(stato)) {
			jpql.append(" left join movgestTs.siacRMovgestTsStatos rMovgestTsStato left join rMovgestTsStato.siacDMovgestStato dMovgestStato ");
		}

		// ATTRIBUTI JOIN

		// join capitoli
		jpql.append(" left join mvg.siacRMovgestBilElems rBilElem left join rBilElem.siacTBilElem tBilElem ");

		// join provvedimenti
		jpql.append(" left join movgestTs.siacRMovgestTsAttoAmms rTsAttoAmm left join rTsAttoAmm.siacTAttoAmm tAttoAmm ");

		// struttura amministrativa
		if (!StringUtils.isEmpty(uidStrutturaAmmProvvedimento)) {
			jpql.append(" left join tAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
		}

		if (!StringUtils.isEmpty(idTipoProvvedimento)) {
			jpql.append(" left join tAttoAmm.siacDAttoAmmTipo dTipoAtto ");
		}

		// soggetto
		if (!StringUtils.isEmpty(codiceCreditore)) {
			jpql.append(" left join movgestTs.siacRMovgestTsSogs rSogg left join rSogg.siacTSoggetto sogg ");
		}

		// classe soggetto
		if (!StringUtils.isEmpty(codiceClasseSoggetto)) {
			jpql.append(" left join movgestTs.siacRMovgestTsSogclasses rClass left join rClass.siacDSoggettoClasse classe ");
		}

		// PDC
		if (!StringUtils.isEmpty(pdc)) {
			jpql.append(" left join movgestTs.siacRMovgestClasses rMovgestPdcClass left join rMovgestPdcClass.siacTClass tPdcClass ");
		}

		// Riaccertato
		if (flagDaRiaccerto.equalsIgnoreCase("Si")) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs flagRAttr left join flagRAttr.siacTAttr flagAtt ");
		}

		// Numero e Anno Riaccertato
		if ((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0)
				&& (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs annoRiacRAttr left join annoRiacRAttr.siacTAttr annoRiacAtt ");
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs numRiacRAttr left join numRiacRAttr.siacTAttr numRiacAtt ");
		}

		// Numero e Anno Origine
		if ((annoImpegnoOrigine != null && annoImpegnoOrigine != 0)
				&& (numeroImpegnoOrigine != null && numeroImpegnoOrigine != 0)) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs annoOriRAttr left join annoOriRAttr.siacTAttr annoOriAtt ");
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs numOriRAttr left join numOriRAttr.siacTAttr numOriAtt ");
		}

		// Programma
		if (!StringUtils.isEmpty(progetto)) {
			jpql.append(" left join movgestTs.siacRMovgestTsProgrammas rProgrammas left join rProgrammas.siacTProgramma tProgramma ");
		}

		// WHERE
		jpql.append(" WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mvg.siacDMovgestTipo.movgestTipoCode = 'A' AND "
				+ " (movgestTs.siacDMovgestTsTipo.movgestTsTipoCode = 'T' or movgestTs.siacDMovgestTsTipo.movgestTsTipoCode='S')");

		param.put("enteProprietarioId", enteUid);

		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("mvg"));

		// Ricerca Soggetto
		if (!StringUtils.isEmpty(codiceCreditore)) {
			jpql.append(" AND sogg.soggettoCode = :codiceSoggetto AND ")
					.append(DataValiditaUtils.validitaForQuery("rSogg"));
			param.put("codiceSoggetto", codiceCreditore);
		}

		if (!StringUtils.isEmpty(codiceClasseSoggetto)) {
			jpql.append(" AND classe.soggettoClasseCode = :codiceClasse ");
			jpql.append(" AND ").append(
					DataValiditaUtils.validitaForQuery("rClass"));
			param.put("codiceClasse", codiceClasseSoggetto);
		}

		// PDC Finanziario
		if (!StringUtils.isEmpty(pdc)) {
			String clausolaPDC = buildClausolaRicercaTClass(
					Constanti.getCodiciPianoDeiConti(), pdc, "tPdcClass",
					"rMovgestPdcClass", "pdcLike", param);
			jpql.append(clausolaPDC);
		}

		// Ricerca Stato
		if (!StringUtils.isEmpty(stato)) {
			jpql.append(" AND dMovgestStato.movgestStatoCode = :stato AND ")
					.append(DataValiditaUtils
							.validitaForQuery("rMovgestTsStato"));
			param.put("stato", stato.toUpperCase());
		}

		// Ricerca Numero Impegno
		if (numeroImpegno != null && numeroImpegno != 0) {
			jpql.append(" AND mvg.movgestNumero = :numeroImpegno ");
			param.put("numeroImpegno", BigDecimal.valueOf(numeroImpegno));
		}

		// Aggiungo condizione anno di bilancio
		jpql.append(" AND mvg.siacTBil.siacTPeriodo.anno = :annoBilancioSelezionato ");
		param.put("annoBilancioSelezionato", annoBilancio.toString());

		// Ricerca anno impegno
		if (annoImpegno != null && annoImpegno != 0) {
			jpql.append(" AND mvg.movgestAnno = :annoImpegno ");
			param.put("annoImpegno", annoImpegno);
		}

		// Impegni Correnti
		if (competenzaCorrente) {
			jpql.append(" AND mvg.movgestAnno = :annoBilancio ");
			param.put("annoBilancio", annoBilancio);
		}

		// Impegni Passati
		if (competenzaCompetenza) {
			jpql.append(" AND mvg.movgestAnno < :annoBilancio ");
			param.put("annoBilancio", annoBilancio);
		}

		// Impegni Futuri
		if (competenzaFuturi) {
			jpql.append(" AND mvg.movgestAnno > :annoBilancio ");
			param.put("annoBilancio", annoBilancio);
		}

		// Capitolo //jira 1338 ricerca capitolo con 0
		if (uidCapitolo != null && uidCapitolo != 0) {
			jpql.append(" AND tBilElem.elemId = :capitoloId ");
			param.put("capitoloId", uidCapitolo);
		} else {

			if (numeroCapitolo != null) {
				jpql.append(" AND tBilElem.elemCode = :numeroCapitolo ");
				param.put("numeroCapitolo", String.valueOf(numeroCapitolo));
			}

			if (numeroArticolo != null) {
				jpql.append(" AND tBilElem.elemCode2 = :numeroArticolo ");
				param.put("numeroArticolo", String.valueOf(numeroArticolo));
			}

			if (numeroUeb != null) {
				jpql.append(" AND tBilElem.elemCode3 = :numeroUeb ");
				param.put("numeroUeb", String.valueOf(numeroUeb));
			}

		}

		// Provvedimento
		if (uidProvvedimento != null && uidProvvedimento != 0) {
			jpql.append(" AND tAttoAmm.attoammId = :provvedimentoId ");
			param.put("provvedimentoId", uidProvvedimento);
		} else {

			if (annoProvvedimento != null && annoProvvedimento != 0) {
				jpql.append(" AND tAttoAmm.attoammAnno = :annoProvvedimento ");
				param.put("annoProvvedimento",
						String.valueOf(annoProvvedimento));
			}

			if (numeroProvvedimento != null && numeroProvvedimento != 0) {
				jpql.append(" AND tAttoAmm.attoammNumero = :numeroProvvedimento ");
				param.put("numeroProvvedimento", numeroProvvedimento);
			}

			if (!StringUtils.isEmpty(idTipoProvvedimento)) {
				jpql.append(" AND dTipoAtto.attoammTipoId = :idTipoProvvedimento");
				param.put("idTipoProvvedimento",
						Integer.valueOf(idTipoProvvedimento));
			}

			// aggiunto filtro per sac del provevdimento
			if (!StringUtils.isEmpty(uidStrutturaAmmProvvedimento)) {
				jpql.append(" AND tClassAtto.classifId = :uidStrutturaAmmProvvedimento");
				param.put("uidStrutturaAmmProvvedimento",
						Integer.valueOf(uidStrutturaAmmProvvedimento));
			}

		}

		// ATTRIBUTI WHERE

		// Riaccertato
		if (flagDaRiaccerto.equalsIgnoreCase("Si")) {
			jpql.append(" AND  flagAtt.attrCode = 'flagDaRiaccertamento' AND UPPER(flagRAttr.boolean_) = 'S' ");
		}

		// Numero e Anno Riaccertato
		if ((annoImpegnoRiaccertato != null && annoImpegnoRiaccertato != 0)
				&& (numeroImpegnoRiaccertato != null && numeroImpegnoRiaccertato != 0)) {
			jpql.append(" AND  annoRiacAtt.attrCode = 'annoRiaccertato' AND annoRiacRAttr.testo = :annoRiacc");
			jpql.append(" AND  numRiacAtt.attrCode = 'numeroRiaccertato' AND numRiacRAttr.testo = :numeroRiacc");
			param.put("annoRiacc", String.valueOf(annoImpegnoRiaccertato));
			param.put("numeroRiacc", String.valueOf(numeroImpegnoRiaccertato));
		}

		// Numero e Anno Origine
		if ((annoImpegnoOrigine != null && annoImpegnoOrigine != 0)
				&& (numeroImpegnoOrigine != null && numeroImpegnoOrigine != 0)) {
			jpql.append(" AND  annoOriAtt.attrCode = 'annoOriginePlur' AND annoOriRAttr.testo = :annoOrigine");
			jpql.append(" AND  numOriAtt.attrCode = 'numeroOriginePlur' AND numOriRAttr.testo = :numeroOrigine");
			param.put("annoOrigine", String.valueOf(annoImpegnoOrigine));
			param.put("numeroOrigine", String.valueOf(numeroImpegnoOrigine));
		}

		if (!StringUtils.isEmpty(progetto)) {
			jpql.append(" AND UPPER(tProgramma.programmaCode) LIKE UPPER(:programmaDesc)");
			String progettoLike = null;
			if (progetto.contains("%")) {
				progettoLike = progetto;
			} else {
				progettoLike = '%' + progetto + '%';
			}
			param.put("programmaDesc", progettoLike);
		}

		jpql.append(" ORDER BY mvg.movgestAnno, mvg.movgestNumero ");

		// Creo la query effettiva
		Query query = createQuery(jpql.toString(), param);

		// CR 1908, eliminare MAX_RIGHE_ESTRAIBILI
		// if(query.getResultList().size() > Constanti.MAX_RIGHE_ESTRAIBILI){
		// listaAccertamenti =
		// query.setMaxResults(Constanti.MAX_RIGHE_ESTRAIBILI +
		// 1).getResultList();
		// }else{
		listaAccertamenti = query.getResultList();
		// }

		// Termino restituendo l'oggetto di ritorno:
		return listaAccertamenti;
	}

	/**
	 * E' il metodo di "engine" di ricerca degli accertamenti e subaccertamenti
	 * Non restituisce gli oggetti completi ma solo l'elenco di identificativi.
	 * Sara' il chiamante a vestire i dati come meglio crede.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IdImpegnoSubimpegno> ricercaAccertamentiSubAccertamenti(
			Integer enteUid, RicercaAccSubAccParamDto prs, boolean soloAccertamenti) {

		List<IdImpegnoSubimpegno> listaID = new ArrayList<IdImpegnoSubimpegno>();

		Map<String, Object> param = new HashMap<String, Object>();
		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, nowDate);

		// PARAMETRI DI INPUT
		Integer annoEsercizio = prs.getAnnoEsercizio(); 
		String statoOperativo = prs.getStato();
		String cig = prs.getCig();
		String cup = prs.getCup();

		Integer annoAccertamento = prs.getAnnoAccertamento();
		BigDecimal numeroAccertamento = prs.getNumeroAccertamento();

		String codiceDebitore = prs.getCodiceDebitore();
		String codiceClasseSoggetto = prs.getCodiceClasseSoggetto();
		
		String codiceProgetto = prs.getCodiceProgetto();

		Integer idProvvedimento = prs.getUidProvvedimento();
		Integer annoProvvedimento = prs.getAnnoProvvedimento();
		Integer numeroProvvedimento = prs.getNumeroProvvedimento();
		String codiceTipoProvvedimento = "";
		if (prs.getTipoProvvedimento() != null) {
			codiceTipoProvvedimento = prs.getTipoProvvedimento().getCodice();
		}

		Integer uidStrutturaProvvedimento = prs
				.getUidStrutturaAmministrativoContabile();

		Integer uidCapitolo = prs.getUidCapitolo();
		Integer numeroCapitolo = prs.getNumeroCapitolo();
		Integer numeroArticolo = prs.getNumeroArticolo();
		Integer numeroUeb = prs.getNumeroUEB();

		String flagDaRiaccerto = prs.getFlagDaRiaccertamento() != null ? prs.getFlagDaRiaccertamento() : "";
		Integer annoAccertamentoRiaccertato = prs
				.getAnnoAccertamentoRiaccertato();
		Integer annoAccertamentoOrigine = prs.getAnnoAccertamentoOrigine();
		Integer numeroAccertamentoOrigine = prs.getNumeroAccertamentoOrigine();
		Integer numeroAccertamentoRiaccertato = prs
				.getNumeroAccertamentoRiaccertato();
		boolean competenzaCorrente = prs.isCompetenzaCorrente();
		boolean competenzaCompetenza = prs.isCompetenzaCompetenza();
		boolean competenzaFuturi = prs.isCompetenzaFuturi();

		// inizio a creare la query
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW it.csi.siac.siacfinser.integration.entity.ext.IdImpegnoSubimpegno(mvg.movgestId, movgestTs.movgestTsIdPadre, mvg.movgestNumero, mvg.movgestAnno) ")
				.append("FROM SiacTMovgestFin mvg ")
				.append("left join mvg.siacTMovgestTs movgestTs left join mvg.siacDMovgestTipo dMovgestTipo ")
				.append("left join movgestTs.siacDMovgestTsTipo dMovgestTsTipo ")
				.append(" left join mvg.siacTBil tBil left join tBil.siacTPeriodo tPeriodo ");

		// CR - 1908 RM nuovo Cig
		if (!StringUtils.isEmpty(cig)) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cigRAttr left join cigRAttr.siacTAttr cigAtt ");
		}

		// CR - 1908 RM nuovo Cup
		if (!StringUtils.isEmpty(cup)) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cupRAttr left join cupRAttr.siacTAttr cupAtt ");
		}
		
		boolean condizioneUidStrutturaProvvedimento = (idProvvedimento == null || idProvvedimento == 0) && (uidStrutturaProvvedimento != null);

		
		boolean annoONumeroProvv = (CommonUtils.maggioreDiZero(annoProvvedimento) || CommonUtils.maggioreDiZero(numeroProvvedimento));
		
		// provvedimento
		if (condizioneUidStrutturaProvvedimento || idProvvedimento != null
				|| annoONumeroProvv) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttoAmms rTsAttoAmm left join rTsAttoAmm.siacTAttoAmm tAttoAmm ");
		}
		
		// struttura amministrativa
		if (condizioneUidStrutturaProvvedimento) {
			jpql.append(" left join tAttoAmm.siacRAttoAmmClasses rAttoClass left join rAttoClass.siacTClass tClassAtto ");
		}

		if (!StringUtils.isEmpty(codiceTipoProvvedimento)) {
			jpql.append(" left join tAttoAmm.siacDAttoAmmTipo dTipoAtto ");
		}

		// soggetto
		if (!StringUtils.isEmpty(codiceDebitore)) {
			jpql.append(" left join movgestTs.siacRMovgestTsSogs rSogg left join rSogg.siacTSoggetto sogg ");
		}
		
		//progetto
		if (!StringUtils.isEmpty(codiceProgetto)) {
			jpql.append(" left join movgestTs.siacRMovgestTsProgrammas rProgrammas left join rProgrammas.siacTProgramma tProgramma ");
		}

		// CR - 1908 RM
		// aggiunto classe soggetto
		if (!StringUtils.isEmpty(codiceClasseSoggetto)) {
			jpql.append(" left join movgestTs.siacRMovgestTsSogclasses rClass left join rClass.siacDSoggettoClasse classe ");
		}

		// CR - 1908 RM
		// aggiunto Riaccertato
		if (flagDaRiaccerto != null && flagDaRiaccerto.equalsIgnoreCase("Si")) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs flagRAttr left join flagRAttr.siacTAttr flagAtt ");
		}

		// CR - 1908 RM
		// aggiun toNumero e Anno Riaccertato
		if ((annoAccertamentoRiaccertato != null && annoAccertamentoRiaccertato != 0)
				&& (numeroAccertamentoRiaccertato != null && numeroAccertamentoRiaccertato != 0)) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs annoRiacRAttr left join annoRiacRAttr.siacTAttr annoRiacAtt ");
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs numRiacRAttr left join numRiacRAttr.siacTAttr numRiacAtt ");
		}

		// CR - 1908 RM
		// aggiuntoNumero e Anno Origine
		if ((annoAccertamentoOrigine != null && annoAccertamentoOrigine != 0)
				&& (numeroAccertamentoOrigine != null && numeroAccertamentoOrigine != 0)) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs annoOriRAttr left join annoOriRAttr.siacTAttr annoOriAtt ");
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs numOriRAttr left join numOriRAttr.siacTAttr numOriAtt ");
		}


		// CR - 1908 RM Stato
		if(!StringUtils.isEmpty(statoOperativo) || !StringUtils.isEmpty(prs.getStatiDaEscludere())){
			jpql.append(" left join movgestTs.siacRMovgestTsStatos rMovgestTsStato left join rMovgestTsStato.siacDMovgestStato dMovgestStato ");
		}
		
		// Commento il vecchio controllo e lo sostituisco con quello nuovo ora
		// il capitolo, se presente, verrà sempre preso in considerazione.
		// A differenza di prima che veniva usato come filtro solo per gli
		// accertamenti e non per i sub-accertamenti
		// if(soloImpegni){
		if (uidCapitolo != null || (numeroCapitolo != null && numeroCapitolo != 0)
				|| (numeroArticolo != null )
				|| (numeroUeb != null && numeroUeb != 0)) {
			// capitolo entrata
			jpql.append(" left join mvg.siacRMovgestBilElems rBilElem left join rBilElem.siacTBilElem tBilElem  ");
		}

		// WHERE
		//jpql.append(" WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND dMovgestTipo.movgestTipoCode = :tipoMovimentoAccertamento ");
		jpql.append(" WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND dMovgestTipo.movgestTipoCode = :tipoMovimentoImpegno ");
		jpql.append(" AND tPeriodo.anno = :annoBilancio ");
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("mvg"));
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("movgestTs"));
		
		//FIX JIRA SIAC-3386 AGGIUNTE VALIDITA SU CAMPI OPZIONALI:
		if(!StringUtils.isEmpty(codiceDebitore)){
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rSogg"));
		}
		if(!StringUtils.isEmpty(codiceClasseSoggetto)){
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rClass"));
		}
		//
		
		if(!StringUtils.isEmpty(codiceProgetto)){
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rProgrammas"));
		}
		
		
		param.put("annoBilancio", String.valueOf(prs.getAnnoEsercizio()));
		param.put("enteProprietarioId", enteUid);
		param.put("tipoMovimentoImpegno", Constanti.MOVGEST_TIPO_ACCERTAMENTO);
		
		
		if(!StringUtils.isEmpty(statoOperativo) || !StringUtils.isEmpty(prs.getStatiDaEscludere()) ){
			
			jpql.append(" AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTs ");
			param.put("tipoMovGestTs", Constanti.MOVGEST_TS_TIPO_TESTATA);
			// Rm: 19.06.2015:
			// Se ricerco anche per stato mov. gestione non devo ricercare anche i sub ma solo gli accertamenti
			
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

		// numero accertamento
		if (numeroAccertamento != null) {
			jpql.append(" AND mvg.movgestNumero = :numeroAccertamento ");
			param.put("numeroAccertamento", numeroAccertamento);
		}

		// anno accertamento
		if (annoAccertamento != null && annoAccertamento != 0) {
			jpql.append(" AND mvg.movgestAnno = :annoAccertamento ");
			param.put("annoAccertamento", annoAccertamento);
		}

		//
		if (uidCapitolo != null && uidCapitolo != 0) {
			jpql.append(" AND tBilElem.elemId = :uidCapitolo");
			param.put("uidCapitolo", uidCapitolo);
		} else {
			// numero capitolo
			if (numeroCapitolo != null && numeroCapitolo != 0) {
				jpql.append(" AND tBilElem.elemCode = :numeroCapitolo ");
				param.put("numeroCapitolo", String.valueOf(numeroCapitolo));
			}

			// numero articolo
			if (numeroArticolo != null ) {
				jpql.append(" AND tBilElem.elemCode2 = :numeroArticolo ");
				param.put("numeroArticolo", String.valueOf(numeroArticolo));
			}

			// numero ueb
			if (numeroUeb != null && numeroUeb != 0) {
				jpql.append(" AND tBilElem.elemCode3 = :numeroUeb ");
				param.put("numeroUeb", String.valueOf(numeroUeb));
			}
		}


		// Provvedimento
		if (idProvvedimento != null && idProvvedimento != 0) {
			jpql.append(" AND tAttoAmm.attoammId = :provvedimentoId ");
			param.put("provvedimentoId", idProvvedimento);
		} else {
			
			if(annoONumeroProvv){
				//SIAC-6018 aggiungo condizione di validita su siac_r_movgest_ts_atto_amm:
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rTsAttoAmm"));
				//	
			}
			
			// anno provvedimento
			if (annoProvvedimento != null && annoProvvedimento != 0) {
				jpql.append(" AND tAttoAmm.attoammAnno = :annoProvvedimento ");
				param.put("annoProvvedimento",
						String.valueOf(annoProvvedimento));
			}

			// numero provvedimento
			if (numeroProvvedimento != null && numeroProvvedimento != 0) {
				jpql.append(" AND tAttoAmm.attoammNumero = :numeroProvvedimento ");
				param.put("numeroProvvedimento", numeroProvvedimento);
			}

			// tipo provvedimento
			if (!StringUtils.isEmpty(codiceTipoProvvedimento)) {
				jpql.append(" AND dTipoAtto.attoammTipoCode = :idTipoProvvedimento");
				param.put("idTipoProvvedimento", codiceTipoProvvedimento);
			}

			// aggiunto filtro per sac del provevdimento
			if (uidStrutturaProvvedimento != null) {
				jpql.append(" AND tClassAtto.classifId = :uidStrutturaAmmProvvedimento");
				param.put("uidStrutturaAmmProvvedimento",uidStrutturaProvvedimento);
			}
		}

		// codice debitore (soggetto)
		if (!StringUtils.isEmpty(codiceDebitore)) {
			jpql.append(" AND sogg.soggettoCode = :codiceDebitore ");
			param.put("codiceDebitore", codiceDebitore);
		}

		if (!StringUtils.isEmpty(codiceClasseSoggetto)) {
			jpql.append(" AND classe.soggettoClasseCode = :codiceClasse ");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rClass"));
			param.put("codiceClasse", codiceClasseSoggetto);
		}
		
		
		if (!StringUtils.isEmpty(codiceProgetto)) {
			jpql.append(" AND tProgramma.programmaCode = :codiceProgetto ");
			param.put("codiceProgetto", codiceProgetto);
		}
		
		
		// Cig
		if (!StringUtils.isEmpty(cig)) {
			jpql.append(" AND  cigAtt.attrCode = 'cig' AND UPPER(cigRAttr.testo) LIKE UPPER(:cigLike) ");
			String cigLike = null;
			if (cig.contains("%")) {
				cigLike = cig;
			} else {
				cigLike = '%' + cig + '%';
			}
			param.put("cigLike", cigLike);
		}

		// Cup
		if (!StringUtils.isEmpty(cup)) {
			jpql.append(" AND  cupAtt.attrCode = 'cup' AND UPPER(cupRAttr.testo) LIKE UPPER(:cupLike) ");
			String cupLike = null;
			if (cup.contains("%")) {
				cupLike = cig;
			} else {
				cupLike = '%' + cup + '%';
			}
			param.put("cupLike", cupLike);
		}

		
		// CR - 1908 RM
		// aggiunto Accertamenti Correnti
		if (competenzaCorrente) {
			jpql.append(" AND mvg.movgestAnno = :anno ");
			param.put("anno", annoEsercizio);
		}

		// CR - 1908 RM
		// aggiunto Accertamenti Passati
		if (competenzaCompetenza) {
			jpql.append(" AND mvg.movgestAnno < :anno ");
			param.put("anno", annoEsercizio);
		}

		// CR - 1908 RM
		// aggiunto Accertamenti Futuri
		if (competenzaFuturi) {
			jpql.append(" AND mvg.movgestAnno > :anno ");
			param.put("anno", annoEsercizio);
		}
		
		
		//Riaccertato
		if(flagDaRiaccerto.equalsIgnoreCase("Si")){
			jpql.append(" AND  flagAtt.attrCode = 'flagDaRiaccertamento' AND UPPER(flagRAttr.boolean_) = 'S' ");
		}
		
		//Numero e Anno Riaccertato
		if((annoAccertamentoRiaccertato != null && annoAccertamentoRiaccertato != 0) && (numeroAccertamentoRiaccertato != null && numeroAccertamentoRiaccertato != 0)){
			jpql.append(" AND  annoRiacAtt.attrCode = 'annoRiaccertato' AND annoRiacRAttr.testo = :annoRiacc");
			jpql.append(" AND  numRiacAtt.attrCode = 'numeroRiaccertato' AND numRiacRAttr.testo = :numeroRiacc");
			param.put("annoRiacc", String.valueOf(annoAccertamentoRiaccertato));
			param.put("numeroRiacc", String.valueOf(numeroAccertamentoRiaccertato));
		}
		
		//Numero e Anno Origine
		if((annoAccertamentoOrigine != null && annoAccertamentoOrigine != 0) && (numeroAccertamentoOrigine != null && numeroAccertamentoOrigine != 0)){
			jpql.append(" AND  annoOriAtt.attrCode = 'annoOriginePlur' AND annoOriRAttr.testo = :annoOrigine");
			jpql.append(" AND  numOriAtt.attrCode = 'numeroOriginePlur' AND numOriRAttr.testo = :numeroOrigine");
			param.put("annoOrigine", String.valueOf(annoAccertamentoOrigine));
			param.put("numeroOrigine", String.valueOf(numeroAccertamentoOrigine));
		}
		
		

		jpql.append(" ORDER BY mvg.movgestAnno, mvg.movgestNumero ");

		// creo la query effettiva
		Query query = createQuery(jpql.toString(), param);
		listaID = query.getResultList();

		// Termino restituendo l'oggetto di ritorno:
		return listaID;
	}
	
	public BigDecimal calcolaDisponibilitaAPagarePerDodicesimi(Integer elemId){
		BigDecimal result  = new  BigDecimal(0);
		String methodName ="calcolaDisponibilitaAPagarePerDodicesimi";
		String functionName="fnc_siac_disponibilitadodicesimi_dpm ";
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
	 * calcola la disponibilita a incassare per un accertamento o per un
	 * subaccertamento indicato tramite richiamo a function su database
	 * 
	 * @param idMovGestTs
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal calcolaDisponibilitaAIncassare(Integer idMovGestTs) {

		BigDecimal result = BigDecimal.ZERO;
		String methodName = "calcolaDisponibilitaAIncassare";
		String functionName = "fnc_siac_disponibilitaincassaremovgest";
		try {

			Query query = entityManager.createNativeQuery("SELECT "
					+ functionName + " (:idMovGestTs)");
			query.setParameter("idMovGestTs", idMovGestTs);
			result = (BigDecimal) query.getSingleResult();
			log.debug(methodName, "Returning result: " + result
					+ " for bilElemId: " + idMovGestTs + " and functionName: "
					+ functionName);
		} catch (Exception e) {
			log.error(methodName, "Returning result: " + result
					+ " for bilElemId: " + idMovGestTs + " and functionName: "
					+ functionName);
			// e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<CodificaImportoDto> calcolaDisponibilitaAIncassareMassive(
			List<SiacTMovgestTsFin> listaSiacTMovgestTs) {

		String methodName = "calcolaDisponibilitaAIncassareMassive";
		String functionName = "fnc_siac_disponibilitaincassaremovgest_rec";

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
			log.error("calcolaDisponibilitaAIncassareMassive", e);
		}
		return elencoCodificaImportoDto;
	}
	
	
	
	/**
	 * E' il metodo di "engine" di ricerca degli accertamenti e subaccertamenti
	 * Non restituisce gli oggetti completi ma solo l'elenco di identificativi.
	 * Sara' il chiamante a vestire i dati come meglio crede.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IdAccertamentoSubAccertamento> ricercaAccertamentiSubAccertamentiPerOrdinativoIncasso(Integer enteUid, RicercaAccSubAccParamDto prs, boolean soloAccertamenti) {

		List<IdAccertamentoSubAccertamento> listaID = new ArrayList<IdAccertamentoSubAccertamento>();

		Map<String, Object> param = new HashMap<String, Object>();
		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, nowDate);

		// PARAMETRI DI INPUT
		Integer annoEsercizio = prs.getAnnoEsercizio(); 
		String statoOperativo = prs.getStato();
		String cig = prs.getCig();
		String cup = prs.getCup();

		Integer annoAccertamento = prs.getAnnoAccertamento();
		BigDecimal numeroAccertamento = prs.getNumeroAccertamento();

		String codiceDebitore = prs.getCodiceDebitore();
		String codiceClasseSoggetto = prs.getCodiceClasseSoggetto();

		Integer idProvvedimento = prs.getUidProvvedimento();
		Integer annoProvvedimento = prs.getAnnoProvvedimento();
		Integer numeroProvvedimento = prs.getNumeroProvvedimento();
		String codiceTipoProvvedimento = "";
		if (prs.getTipoProvvedimento() != null) {
			codiceTipoProvvedimento = prs.getTipoProvvedimento().getCodice();
		}

		Integer uidStrutturaProvvedimento = prs
				.getUidStrutturaAmministrativoContabile();

		Integer uidCapitolo = prs.getUidCapitolo();
		Integer numeroCapitolo = prs.getNumeroCapitolo();
		Integer numeroArticolo = prs.getNumeroArticolo();
		Integer numeroUeb = prs.getNumeroUEB();

		String flagDaRiaccerto = prs.getFlagDaRiaccertamento() != null ? prs.getFlagDaRiaccertamento() : "";
		Integer annoAccertamentoRiaccertato = prs
				.getAnnoAccertamentoRiaccertato();
		Integer annoAccertamentoOrigine = prs.getAnnoAccertamentoOrigine();
		Integer numeroAccertamentoOrigine = prs.getNumeroAccertamentoOrigine();
		Integer numeroAccertamentoRiaccertato = prs
				.getNumeroAccertamentoRiaccertato();
		boolean competenzaCorrente = prs.isCompetenzaCorrente();
		boolean competenzaCompetenza = prs.isCompetenzaCompetenza();
		boolean competenzaFuturi = prs.isCompetenzaFuturi();

		// inizio a creare la query
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW it.csi.siac.siacfinser.integration.entity.ext.IdAccertamentoSubAccertamento")
				.append("(mvg.movgestId, movgestTs.movgestTsIdPadre, mvg.movgestNumero, mvg.movgestAnno, dMovgestStato.movgestStatoCode, sogg.soggettoCode, classe.soggettoClasseCode) ")
				.append("FROM SiacTMovgestFin mvg ")
				.append("left join mvg.siacTMovgestTs movgestTs left join mvg.siacDMovgestTipo dMovgestTipo ")
				.append("left join movgestTs.siacDMovgestTsTipo dMovgestTsTipo ")
				.append(" left join mvg.siacTBil tBil left join tBil.siacTPeriodo tPeriodo ");

		// CR - 1908 RM nuovo Cig
		if (!StringUtils.isEmpty(cig)) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cigRAttr left join cigRAttr.siacTAttr cigAtt ");
		}

		// CR - 1908 RM nuovo Cup
		if (!StringUtils.isEmpty(cup)) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs cupRAttr left join cupRAttr.siacTAttr cupAtt ");
		}

		// provvedimento
		if (idProvvedimento != null
				|| ((annoProvvedimento != null && annoProvvedimento != 0) || (numeroProvvedimento != null && numeroProvvedimento != 0))) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttoAmms rTsAttoAmm left join rTsAttoAmm.siacTAttoAmm tAttoAmm ");
		}

		if (!StringUtils.isEmpty(codiceTipoProvvedimento)) {
			jpql.append(" left join tAttoAmm.siacDAttoAmmTipo dTipoAtto ");
		}

		// soggetto
		//if (!StringUtils.isEmpty(codiceDebitore)) {
			jpql.append(" left join movgestTs.siacRMovgestTsSogs rSogg left join rSogg.siacTSoggetto sogg ");
		//}

		// CR - 1908 RM
		// aggiunto classe soggetto
		//if (!StringUtils.isEmpty(codiceClasseSoggetto)) {
			jpql.append(" left join movgestTs.siacRMovgestTsSogclasses rClass left join rClass.siacDSoggettoClasse classe ");
		//}

		// CR - 1908 RM
		// aggiunto Riaccertato
		if (flagDaRiaccerto != null && flagDaRiaccerto.equalsIgnoreCase("Si")) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs flagRAttr left join flagRAttr.siacTAttr flagAtt ");
		}

		// CR - 1908 RM
		// aggiun toNumero e Anno Riaccertato
		if ((annoAccertamentoRiaccertato != null && annoAccertamentoRiaccertato != 0)
				&& (numeroAccertamentoRiaccertato != null && numeroAccertamentoRiaccertato != 0)) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs annoRiacRAttr left join annoRiacRAttr.siacTAttr annoRiacAtt ");
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs numRiacRAttr left join numRiacRAttr.siacTAttr numRiacAtt ");
		}

		// CR - 1908 RM
		// aggiuntoNumero e Anno Origine
		if ((annoAccertamentoOrigine != null && annoAccertamentoOrigine != 0)
				&& (numeroAccertamentoOrigine != null && numeroAccertamentoOrigine != 0)) {
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs annoOriRAttr left join annoOriRAttr.siacTAttr annoOriAtt ");
			jpql.append(" left join movgestTs.siacRMovgestTsAttrs numOriRAttr left join numOriRAttr.siacTAttr numOriAtt ");
		}


		// CR - 1908 RM Stato
		//if(!StringUtils.isEmpty(statoOperativo)){
			jpql.append(" left join movgestTs.siacRMovgestTsStatos rMovgestTsStato left join rMovgestTsStato.siacDMovgestStato dMovgestStato ");
		//}

		// Commento il vecchio controllo e lo sostituisco con quello nuovo ora
		// il capitolo, se presente, verrà sempre preso in considerazione.
		// A differenza di prima che veniva usato come filtro solo per gli
		// accertamenti e non per i sub-accertamenti
		// if(soloImpegni){
		if (uidCapitolo != null || (numeroCapitolo != null && numeroCapitolo != 0)
				|| (numeroArticolo != null )
				|| (numeroUeb != null && numeroUeb != 0)) {
			// capitolo entrata
			jpql.append(" left join mvg.siacRMovgestBilElems rBilElem left join rBilElem.siacTBilElem tBilElem  ");
		}

		// WHERE
		//jpql.append(" WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND dMovgestTipo.movgestTipoCode = :tipoMovimentoAccertamento ");
		jpql.append(" WHERE mvg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND dMovgestTipo.movgestTipoCode = :tipoMovimentoImpegno ");
		jpql.append(" AND tPeriodo.anno = :annoBilancio ");
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("mvg"));
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("movgestTs"));
		
		param.put("annoBilancio", String.valueOf(prs.getAnnoEsercizio()));
		param.put("enteProprietarioId", enteUid);
		param.put("tipoMovimentoImpegno", Constanti.MOVGEST_TIPO_ACCERTAMENTO);
		
		
		if(statoOperativo == null){
			jpql.append(" AND (dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTs OR  dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTsS ) ");
			param.put("tipoMovGestTs", Constanti.MOVGEST_TS_TIPO_TESTATA);
			param.put("tipoMovGestTsS", Constanti.MOVGEST_TS_TIPO_SUBIMPEGNO);
		}else{
			jpql.append(" AND dMovgestTsTipo.movgestTsTipoCode = :tipoMovGestTs ");
			param.put("tipoMovGestTs", Constanti.MOVGEST_TS_TIPO_TESTATA);
			
			// Rm: 19.06.2015:
			// Se ricerco anche per stato mov. gestione non devo ricercare anche i sub ma solo gli accertamenti
			jpql.append(" AND dMovgestStato.movgestStatoCode = :statoOperativo ");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rMovgestTsStato"));
			param.put("statoOperativo", statoOperativo);
		}
		

		// numero accertamento
		if (numeroAccertamento != null) {
			jpql.append(" AND mvg.movgestNumero = :numeroAccertamento ");
			param.put("numeroAccertamento", numeroAccertamento);
		}

		// anno accertamento
		if (annoAccertamento != null && annoAccertamento != 0) {
			jpql.append(" AND mvg.movgestAnno = :annoAccertamento ");
			param.put("annoAccertamento", annoAccertamento);
		}

		//
		if (uidCapitolo != null && uidCapitolo != 0) {
			jpql.append(" AND tBilElem.elemId = :uidCapitolo");
			param.put("uidCapitolo", uidCapitolo);
		} else {
			// numero capitolo
			if (numeroCapitolo != null && numeroCapitolo != 0) {
				jpql.append(" AND tBilElem.elemCode = :numeroCapitolo ");
				param.put("numeroCapitolo", String.valueOf(numeroCapitolo));
			}

			// numero articolo
			if (numeroArticolo != null ) {
				jpql.append(" AND tBilElem.elemCode2 = :numeroArticolo ");
				param.put("numeroArticolo", String.valueOf(numeroArticolo));
			}

			// numero ueb
			if (numeroUeb != null && numeroUeb != 0) {
				jpql.append(" AND tBilElem.elemCode3 = :numeroUeb ");
				param.put("numeroUeb", String.valueOf(numeroUeb));
			}
		}


		// Provvedimento
		if (idProvvedimento != null && idProvvedimento != 0) {
			jpql.append(" AND tAttoAmm.attoammId = :provvedimentoId ");
			param.put("provvedimentoId", idProvvedimento);
		} else {
			// anno provvedimento
			if (annoProvvedimento != null && annoProvvedimento != 0) {
				jpql.append(" AND tAttoAmm.attoammAnno = :annoProvvedimento ");
				param.put("annoProvvedimento",
						String.valueOf(annoProvvedimento));
			}

			// numero provvedimento
			if (numeroProvvedimento != null && numeroProvvedimento != 0) {
				jpql.append(" AND tAttoAmm.attoammNumero = :numeroProvvedimento ");
				param.put("numeroProvvedimento", numeroProvvedimento);
			}

			// tipo provvedimento
			if (!StringUtils.isEmpty(codiceTipoProvvedimento)) {
				jpql.append(" AND dTipoAtto.attoammTipoCode = :idTipoProvvedimento");
				param.put("idTipoProvvedimento", codiceTipoProvvedimento);
			}

			// aggiunto filtro per sac del provevdimento
			if (uidStrutturaProvvedimento != null) {
				jpql.append(" AND tClassAtto.classifId = :uidStrutturaAmmProvvedimento");
				param.put("uidStrutturaAmmProvvedimento",
						uidStrutturaProvvedimento);
			}
		}

		// codice debitore (soggetto)
		if (!StringUtils.isEmpty(codiceDebitore)) {
			jpql.append(" AND sogg.soggettoCode = :codiceDebitore ");
			param.put("codiceDebitore", codiceDebitore);
		}

		if (!StringUtils.isEmpty(codiceClasseSoggetto)) {
			jpql.append(" AND classe.soggettoClasseCode = :codiceClasse ");
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rClass"));
			param.put("codiceClasse", codiceClasseSoggetto);
		}

		// Cig
		if (!StringUtils.isEmpty(cig)) {
			jpql.append(" AND  cigAtt.attrCode = 'cig' AND UPPER(cigRAttr.testo) LIKE UPPER(:cigLike) ");
			String cigLike = null;
			if (cig.contains("%")) {
				cigLike = cig;
			} else {
				cigLike = '%' + cig + '%';
			}
			param.put("cigLike", cigLike);
		}

		// Cup
		if (!StringUtils.isEmpty(cup)) {
			jpql.append(" AND  cupAtt.attrCode = 'cup' AND UPPER(cupRAttr.testo) LIKE UPPER(:cupLike) ");
			String cupLike = null;
			if (cup.contains("%")) {
				cupLike = cig;
			} else {
				cupLike = '%' + cup + '%';
			}
			param.put("cupLike", cupLike);
		}

		
		// CR - 1908 RM
		// aggiunto Accertamenti Correnti
		if (competenzaCorrente) {
			jpql.append(" AND mvg.movgestAnno = :anno ");
			param.put("anno", annoEsercizio);
		}

		// CR - 1908 RM
		// aggiunto Accertamenti Passati
		if (competenzaCompetenza) {
			jpql.append(" AND mvg.movgestAnno < :anno ");
			param.put("anno", annoEsercizio);
		}

		// CR - 1908 RM
		// aggiunto Accertamenti Futuri
		if (competenzaFuturi) {
			jpql.append(" AND mvg.movgestAnno > :anno ");
			param.put("anno", annoEsercizio);
		}
		
		
		//Riaccertato
		if(flagDaRiaccerto.equalsIgnoreCase("Si")){
			jpql.append(" AND  flagAtt.attrCode = 'flagDaRiaccertamento' AND UPPER(flagRAttr.boolean_) = 'S' ");
		}
		
		//Numero e Anno Riaccertato
		if((annoAccertamentoRiaccertato != null && annoAccertamentoRiaccertato != 0) && (numeroAccertamentoRiaccertato != null && numeroAccertamentoRiaccertato != 0)){
			jpql.append(" AND  annoRiacAtt.attrCode = 'annoRiaccertato' AND annoRiacRAttr.testo = :annoRiacc");
			jpql.append(" AND  numRiacAtt.attrCode = 'numeroRiaccertato' AND numRiacRAttr.testo = :numeroRiacc");
			param.put("annoRiacc", String.valueOf(annoAccertamentoRiaccertato));
			param.put("numeroRiacc", String.valueOf(numeroAccertamentoRiaccertato));
		}
		
		//Numero e Anno Origine
		if((annoAccertamentoOrigine != null && annoAccertamentoOrigine != 0) && (numeroAccertamentoOrigine != null && numeroAccertamentoOrigine != 0)){
			jpql.append(" AND  annoOriAtt.attrCode = 'annoOriginePlur' AND annoOriRAttr.testo = :annoOrigine");
			jpql.append(" AND  numOriAtt.attrCode = 'numeroOriginePlur' AND numOriRAttr.testo = :numeroOrigine");
			param.put("annoOrigine", String.valueOf(annoAccertamentoOrigine));
			param.put("numeroOrigine", String.valueOf(numeroAccertamentoOrigine));
		}
		
		

		jpql.append(" ORDER BY mvg.movgestAnno, mvg.movgestNumero ");

		// creo la query effettiva
		Query query = createQuery(jpql.toString(), param);
		listaID = query.getResultList();

		// Termino restituendo l'oggetto di ritorno:
		return listaID;
	}
	
	public ConsultaDettaglioAccertamentoDto consultaDettaglioAccertamento(Integer idMovGestTs){
		String methodName ="consultaDettaglioAccertamento";
		String functionName="fnc_siac_consultadettaglioaccertamento ";
		ConsultaDettaglioAccertamentoDto esito = null;
		try{
			Query query = entityManager.createNativeQuery("SELECT * FROM "+ functionName  + "(:idMovGestTs)");
			query.setParameter("idMovGestTs", idMovGestTs);		
			
			List<Object[]> result = (List<Object[]>) query.getResultList();
			
			esito = mapResultToConsultaDettaglioAccertamentoDto(result);
			
			log.debug(methodName, "Errore for idMovGestTs: "+ idMovGestTs + " and functionName: "+ functionName);
		}catch(Exception e){
			log.error(methodName, "Errore for idMovGestTs: "+ idMovGestTs + " and functionName: "+ functionName);
		}
		return esito;
	}
	
	protected ConsultaDettaglioAccertamentoDto mapResultToConsultaDettaglioAccertamentoDto(List<Object[]> result) {
		
		ConsultaDettaglioAccertamentoDto esito = new ConsultaDettaglioAccertamentoDto(); 

		if (!result.isEmpty()) {
			
		 Object[] lp = result.get(0);
		 
		 esito.setTotImpOrd((BigDecimal) lp[0]);
		 esito.setnOrd((Integer) lp[1]);
		 esito.setTotImpSubdoc((BigDecimal) lp[2]);
		 esito.setnImpDoc((Integer) lp[3]);
		 esito.setTotImpOrdSudoc((BigDecimal) lp[4]);
		 esito.setnDocOrd((Integer) lp[5]);
		 esito.setTotDocNonInc((BigDecimal) lp[6]);
		 esito.setnDocNonInc((Integer) lp[7]);
		 esito.setTotImpPredoc((BigDecimal) lp[8]);
		 esito.setnImpPredoc((Integer) lp[9]);
		 esito.setTotModProv((BigDecimal) lp[10]);

		}

		return esito;
	}
		

}
