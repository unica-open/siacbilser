/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetCompMacroTipoEnum;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;

@Component
@Transactional
public class ModificaMovimentoGestioneSpesaCollegataDaoImpl extends JpaDao<ModificaMovimentoGestioneSpesaCollegata, SiacRMovgestTsDetModFin> implements ModificaMovimentoGestioneSpesaCollegataDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<SiacRMovgestTsDetModFin> trovaModificaMovimentoGestioneCollegateAdAccertamento(
			Integer uidAccertamento, Integer uidModifica,boolean escludiModificheEntrataAnnullate) {
		
		StringBuilder jpql = new StringBuilder();
		List<SiacRMovgestTsDetModFin> listaModifiche = null;
		Map<String, Object> param = new HashMap<String, Object>();

		jpql.append(" SELECT DISTINCT srmtdmf ")
			.append(" FROM SiacRMovgestTsDetModFin srmtdmf ")
			.append(" JOIN srmtdmf.siacTMovgestTsDetModEntrata stmtdm ")
			.append(" JOIN stmtdm.siacTMovgestT.siacTMovgest movgest ")
			.append(" JOIN stmtdm.siacRModificaStato srms ");
		if(escludiModificheEntrataAnnullate) {
			jpql.append(" JOIN srms.siacDModificaStato modStato ");
		}
//			
		jpql.append(" WHERE movgest.movgestId = :uidAccertamento ")
			.append(escludiModificheEntrataAnnullate ? " AND modStato.modStatoCode = 'V' " : "");
			;
		
		if(NumberUtil.isValidAndGreaterThanZero(uidModifica)) {
			jpql.append(" AND stmtdm.movgestTsDetModId = :uidModifica ");
			param.put("uidModifica", uidModifica);
		}
		
		jpql
		//SIAC-8624
//		.append(" AND srmtdmf.dataCancellazione IS NULL ")
//			.append(" AND ( srmtdmf.dataFineValidita IS NULL OR srmtdmf.dataFineValidita >= :date ) ")
			//la SiacRModificaStato non necessita di controlli sulla data cancellazione
			.append(" AND ( srms.dataFineValidita IS NULL OR srms.dataFineValidita >= :date ) ")
			.append(" AND stmtdm.dataCancellazione IS NULL ")
			.append(" AND ( stmtdm.dataFineValidita IS NULL OR stmtdm.dataFineValidita >= :date ) ");
		
		param.put("uidAccertamento", uidAccertamento);
		param.put("date", new Date());

		Query query = createQuery(jpql.toString(), param);
		
		listaModifiche = (List<SiacRMovgestTsDetModFin>) query.getResultList();
		
		return CoreUtil.checkList(listaModifiche);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SiacTModificaFin> caricaModificheMovimentoGestionePerDatiDefaultVincoloEsplicito(
			Integer uidAccertamento) {
		StringBuilder jpql = new StringBuilder();
		List<SiacTModificaFin> listaModifiche = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT DISTINCT modificaSpesa ")
			.append(" FROM SiacTMovgestFin accertamento ")
			.append(" JOIN accertamento.siacTMovgestTs accertamentoTs ")
			.append(" JOIN accertamentoTs.siacRMovgestTsA vincolo ")
			.append(" JOIN vincolo.siacTMovgestTsB impegnoTs ")
			.append(" JOIN impegnoTs.siacTMovgestTsDetMods dettagliModificheImpegno ")
			.append(" JOIN dettagliModificheImpegno.siacRModificaStato statoModSpesa ")
			.append(" JOIN statoModSpesa.siacTModifica modificaSpesa ")
			.append(" JOIN statoModSpesa.siacDModificaStato modStato ")
			.append(" JOIN impegnoTs.siacTMovgest impegno ")
//			.append(" JOIN modificaSpesa.siacDModificaTipo modTipo ")
			.append(" JOIN impegnoTs.siacTMovgest.siacRMovgestBilElems rImpegniCapitoli ")
			.append(" JOIN rImpegniCapitoli.siacDBilElemDetCompTipo.siacDBilElemDetCompMacroTipo macroTipoCapitoli ")
			//prendo anche le modifiche che non sono collegate con una modifica di entrata
			.append(" LEFT JOIN dettagliModificheImpegno.siacTMovgestTsDetModsSpesa collegate ")
			.append(" WHERE accertamento.movgestId = :uidAccertamento ")
			.append(" AND modStato.modStatoCode = 'V' ")
			//SIAC-8401
			.append(" AND dettagliModificheImpegno.mtdmReimputazioneFlag = TRUE ")
//			.append(" AND modTipo.modTipoCode = 'REIMP' ")
			//controllo sui macro tipi capitolo
			.append(" AND macroTipoCapitoli.elemDetCompMacroTipoCode NOT IN (:macros) ")
		
			//cerco tra le collegate anche quelle presenti sulle collegate che abbiano un residuo 
			//(anche quelle con data cancellazione poiche' smarcate)
//			.append(" AND collegate.siacTMovgestTsDetModSpesa NOT IN ( ")
//			.append(" 	SELECT DISTINCT srmtdm.siacTMovgestTsDetModSpesa  ")
//			.append(" 	FROM SiacRMovgestTsDetModFin srmtdm ")
//			.append(" 	WHERE srmtdm.movgestTsModImpoResiduo = 0 ")
//			.append(" 	AND srmtdm.dataCancellazione IS NULL ")
//			.append(" ) ")
			.append(" AND accertamentoTs.dataCancellazione IS NULL ")
			.append(" AND ( accertamentoTs.dataFineValidita IS NULL OR accertamentoTs.dataFineValidita >= :date ) ")
			.append(" AND rImpegniCapitoli.dataCancellazione IS NULL ")
			.append(" AND ( rImpegniCapitoli.dataFineValidita IS NULL OR rImpegniCapitoli.dataFineValidita >= :date ) ")
			.append(" AND vincolo.dataCancellazione IS NULL ")
			.append(" AND ( vincolo.dataFineValidita IS NULL OR vincolo.dataFineValidita >= :date ) ")
			.append(" AND impegnoTs.dataCancellazione IS NULL ")
			.append(" AND ( impegnoTs.dataFineValidita IS NULL OR impegnoTs.dataFineValidita >= :date ) ")
			//la SiacRModificaStato non necessita di controlli sulla data cancellazione
			.append(" AND ( statoModSpesa.dataFineValidita IS NULL OR statoModSpesa.dataFineValidita >= :date ) ")
			.append(" AND modificaSpesa.dataCancellazione IS NULL ")
			.append(" AND ( modificaSpesa.dataFineValidita IS NULL OR modificaSpesa.dataFineValidita >= :date ) ")
			;
		
		param.put("uidAccertamento", uidAccertamento);
		param.put("date", new Date());
		param.put("macros", new ArrayList<String>(Arrays.asList(SiacDBilElemDetCompMacroTipoEnum.FPV.getCodice(), SiacDBilElemDetCompMacroTipoEnum.AVANZO.getCodice())));
		
		Query query = createQuery(jpql.toString(), param);
		
		listaModifiche = (List<SiacTModificaFin>) query.getResultList();
		
		return CoreUtil.checkList(listaModifiche);
	}

	@Override
	public List<SiacTModificaFin> caricaModificheMovimentoGestionePerDatiDefaultVincoloImplicito(Integer bilElemId,
			List<String> macroTipoCodes, Integer idEnte) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT DISTINCT modifica_mov");
		jpql.append(" FROM SiacTMovgestTsFin tsmov, SiacRMovgestBilElemFin rcap, SiacRVincoloBilElem r_vinc_c1");
		//prendo movgest collegati a delle modifiche di importo
		jpql.append(" JOIN tsmov.siacTMovgestTsDetMods movmod ");
		jpql.append(" JOIN tsmov.siacDMovgestTsTipo tsmovtipo ");
		jpql.append(" JOIN movmod.siacRModificaStato.siacTModifica modifica_mov ");
		jpql.append(" LEFT JOIN movmod.siacTMovgestTsDetModsSpesa collegate ");
		//condizioni di validita' del record
		jpql.append(" WHERE tsmov.dataCancellazione IS NULL");
		jpql.append(" AND tsmov.dataCancellazione IS NULL");
		jpql.append(" AND tsmov.siacTMovgest.dataCancellazione IS NULL");
		jpql.append(" AND rcap.dataCancellazione IS NULL");
		jpql.append(" AND r_vinc_c1.dataCancellazione IS NULL");
		jpql.append(" AND movmod.dataCancellazione IS NULL");
		jpql.append(" AND modifica_mov.dataCancellazione IS NULL");
//		jpql.append(" AND collegate.siacTMovgestTsDetModSpesa NOT IN ( ");
//		jpql.append(" 	SELECT DISTINCT srmtdm.siacTMovgestTsDetModSpesa  ");
//		jpql.append(" 	FROM SiacRMovgestTsDetModFin srmtdm ");
//		jpql.append(" 	WHERE srmtdm.movgestTsModImpoResiduo = 0 ");
//		jpql.append(" 	AND srmtdm.dataCancellazione IS NULL ");
//		jpql.append(" ) ");
		//cerco modifiche di reimputazione
		//SIAC-8401
		jpql.append(" AND movmod.mtdmReimputazioneFlag = TRUE ");
		//cerco solo le moodifiche associate alla testata
		jpql.append(" AND tsmovtipo.movgestTsTipoCode = 'T'");
		//condizioni di join
		jpql.append(" AND rcap.siacTMovgest = tsmov.siacTMovgest");
		jpql.append(" AND r_vinc_c1.siacTBilElem = rcap.siacTBilElem");
		//sto considerando gli impegni, che cono collegati ad un capitolo UG
		jpql.append(" AND rcap.siacTBilElem.siacDBilElemTipo.elemTipoCode = 'CAP-UG'");
		//filtro per ente
		jpql.append(" AND tsmov.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		//escludo FPV e AVANZO
		jpql.append(" AND rcap.siacDBilElemDetCompTipo.siacDBilElemDetCompMacroTipo.elemDetCompMacroTipoCode NOT IN :macrotipoCodes");
		//seleziono i capitoli UG vincolati con il capitolo di entrata in input
		jpql.append(" AND EXISTS ( ");
		jpql.append(" 	FROM SiacRVincoloBilElemFin r_vinc_c2");
		jpql.append(" 	WHERE r_vinc_c2.dataCancellazione IS NULL");
		jpql.append(" 	AND r_vinc_c2.siacTVincolo = r_vinc_c1.siacTVincolo");
		jpql.append(" 	AND r_vinc_c2.siacTBilElem.elemId = :elemId");
		jpql.append(" )");
		//prendo i movgest solo in stato diverso da annullato
		jpql.append(" AND EXISTS(");
		jpql.append(" 	FROM SiacRMovgestTsStatoFin rstato");
		jpql.append(" 	WHERE rstato.dataCancellazione IS NULL");
		jpql.append(" 	AND rstato.siacTMovgestT = tsmov");
		jpql.append(" 	AND rstato.siacDMovgestStato.movgestStatoCode <> 'A'");
		jpql.append(" )");
		//prendo movgest senza vincolo esplicito
		jpql.append(" AND NOT EXISTS(");
		jpql.append(" 	FROM SiacRMovgestTsFin r_vincolo_esplicito");
		jpql.append(" 	WHERE r_vincolo_esplicito.dataCancellazione IS NULL");
		jpql.append(" 	AND r_vincolo_esplicito.siacTMovgestTsB = tsmov");
		jpql.append(" )");
		
		param.put("macrotipoCodes", macroTipoCodes);
		param.put("enteProprietarioId", idEnte);
		param.put("elemId", bilElemId);
		Query query = createQuery(jpql.toString(), param);

		@SuppressWarnings("unchecked")
		List<SiacTModificaFin> resultList = (List<SiacTModificaFin>) query.getResultList();
		return CoreUtil.checkList(resultList);
	}

	/**
	 * [0] => importoResiduoCollegare, [1] => importoMaxCollegabile
	 * @param uidModifica
	 * @return List<BigDecimal>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BigDecimal> caricaImportiModificaSpesaCollegataDefault(int uidModifica, Integer uidAccertamento) {
		String methodName ="caricaImportiModificaSpesaCollegataDefault";
		String functionName = "fnc_siac_importi_modifica_spesa_collegata_set ";
		List<BigDecimal> importiModifica = null;
		//SIAC-8409
		try{
			Query query = entityManager.createNativeQuery("SELECT " + functionName  + "(:uidModifica, :uidAccertamento)")
					.setParameter("uidModifica", uidModifica).setParameter("uidAccertamento", uidAccertamento);
			
			
			importiModifica = (List<BigDecimal>) query.getResultList();
			if(CollectionUtils.isEmpty(importiModifica)){
				importiModifica = new ArrayList<BigDecimal>();
			}
		} catch(Exception e) {
			//SIAC-8266
			log.trace(methodName, extractErrorCaricaImportiModificaSpesaCollegataDefault(uidModifica, functionName, importiModifica) 
					+ " - [MESSAGE]: " + e.getMessage());
		}
		return importiModifica;
	}

	@Override
	public BigDecimal caricaImportoResiduoCollegare(Integer uidModifica) {
		String methodName ="caricaImportoResiduoCollegareDefault";
		String functionName="fnc_siac_importo_residuo_spesa_collegata ";
		BigDecimal importoResiduo = null;
		try{
			Query query = entityManager.createNativeQuery("SELECT "+ functionName  + "(:uidModifica)")
					.setParameter("uidModifica", uidModifica);		
			importoResiduo = (BigDecimal) query.getSingleResult();
			if(importoResiduo == null){
				importoResiduo = BigDecimal.ZERO;
			}
		} catch(Exception e) {
			//SIAC-8266
			log.trace(methodName, "Returning result: "+ importoResiduo + " for modId: "+ uidModifica + " and functionName: "+ functionName
					+ " - [MESSAGE]: " + e.getMessage());
		}
		return importoResiduo;
	}

	@Override
	public BigDecimal caricaImportoMassimoCollegabileDefault(Integer uidModifica, Integer uidAccertamento) {
		String methodName ="caricaImportoMassimoCollegabileDefault";
		String functionName="fnc_siac_importo_max_coll_spesa_collegata ";
		BigDecimal importoMaxCollegabile = null;
		try{
			Query query = entityManager.createNativeQuery("SELECT "+ functionName  + "(:uidModifica, :uidAccertamento)")
					.setParameter("uidModifica", uidModifica);		
			importoMaxCollegabile = (BigDecimal) query.getSingleResult();
			if(importoMaxCollegabile == null){
				importoMaxCollegabile = BigDecimal.ZERO;
			}
		} catch(Exception e) {
			//SIAC-8266
			log.trace(methodName, "Returning result: "+ importoMaxCollegabile + " for modId: "+ uidModifica + " and functionName: "+ functionName
					+ " - [MESSAGE]: " + e.getMessage());
		}
		return importoMaxCollegabile;
	}
	
	/**
	 * SIAC-8266
	 * @param uidModifica
	 * @param functionName
	 * @param importiModifica
	 * @return String
	 */
	private String extractErrorCaricaImportiModificaSpesaCollegataDefault(int uidModifica, String functionName, List<BigDecimal> importiModifica) {
		StringBuilder sb = new StringBuilder()
			.append("Returning result: ");
		if(CollectionUtils.isNotEmpty(importiModifica)) {
			sb.append("importoResiduoCollegare: " + importiModifica.get(0) != null ? "[" + importiModifica.get(0) + "]" : "[null]")
			.append(", importoMaxCollegabile: "+ importiModifica.get(1) != null ? "[" + importiModifica.get(1) + "]" : "[null]");
		} else {
			sb.append("[null]");
		}
		sb.append(" for modId: "+ uidModifica)
		.append(" and functionName: "+ functionName);
		return sb.toString();
	}

}

