/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.jpql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;

/**
 * Enumera gli SQL necessari ad individuare i Movimenti a partire dall'id di {@link SiacTBilElem}.
 * 
 * @author Domenico
 */
public enum MovimentiFromBilElemBySacJpqlEnum {
	
	MOVGEST(Arrays.asList(SiacDCollegamentoTipoEnum.Impegno, SiacDCollegamentoTipoEnum.Accertamento),
			new StringBuilder()
					.append(" SELECT siac_t_movgest.movgest_id ")
					.append(" FROM 	 siac_t_movgest ") 
					.append("		,siac_r_movgest_bil_elem ")
					.append("		,siac_t_bil_elem " )
					.append("       ,siac_r_bil_elem_class ")
					.append(" WHERE ")
					.append("        siac_t_movgest.movgest_id = siac_r_movgest_bil_elem.movgest_id ")
					.append("	 AND siac_r_movgest_bil_elem.elem_id = siac_t_bil_elem.elem_id ")
					.append("	 AND siac_t_bil_elem.elem_id = siac_r_bil_elem_class.elem_id ")
					.append("    AND siac_r_bil_elem_class.classif_id = :sacId ")
					.append("    AND siac_t_movgest.data_cancellazione is null ")
					.append(" 	 AND siac_r_movgest_bil_elem.data_cancellazione is null ")
					.append(" 	 AND siac_t_bil_elem.data_cancellazione is null ")
					.append("    AND siac_r_bil_elem_class.data_cancellazione is null ")
				.toString(),
				
			new StringBuilder()
				.append(" SELECT rmbe.siacTMovgest.movgestId ")
				.append(" FROM  SiacRMovgestBilElem rmbe ")
				.append("     , SiacRBilElemClass rbec ")
				.append(" WHERE rmbe.siacTBilElem = rbec.siacTBilElem ")
				.append("     AND rbec.siacTClass.classifId = :sacId ")
				.append("     AND rmbe.siacTMovgest.dataCancellazione IS NULL ")
				.append("     AND rmbe.dataCancellazione IS NULL ")
				.append("     AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
				.append("     AND rbec.dataCancellazione IS NULL ")
			.toString()),
	
	SUBMOVGEST(Arrays.asList(SiacDCollegamentoTipoEnum.SubImpegno, SiacDCollegamentoTipoEnum.SubAccertamento),
			new StringBuilder()
			.append(" SELECT 	siac_t_movgest_ts.movgest_ts_id ") 
			.append(" FROM ") 
			.append("      siac_t_movgest_ts ")
			.append("     ,siac_r_movgest_bil_elem ") 
			.append("     ,siac_t_bil_elem ") 
			.append("     ,siac_r_bil_elem_class ") 
			.append(" WHERE ") 
			.append("      siac_t_movgest_ts.movgest_id 	 =  siac_r_movgest_bil_elem.movgest_id ")
			.append(" AND siac_r_movgest_bil_elem.elem_id  =  siac_t_bil_elem.elem_id ") 
			.append(" AND siac_t_bil_elem.elem_id 		 =  siac_r_bil_elem_class.elem_id ") 
			.append(" AND siac_t_movgest_ts.data_cancellazione is null ")
			.append(" AND siac_r_movgest_bil_elem.data_cancellazione is null ") 
			.append(" AND siac_t_bil_elem.data_cancellazione is null ") 
			.append(" AND siac_r_bil_elem_class.data_cancellazione is null ") 
			.append(" AND siac_r_bil_elem_class.classif_id = :sacId ") 
			.toString(),
			
			new StringBuilder()
			.append(" SELECT tmt.movgestTsId ")
			.append(" FROM SiacTMovgestT tmt ")
			.append(" 	, SiacRMovgestBilElem rmbe ")
			.append(" 	, SiacRBilElemClass rbec ")
			.append(" WHERE tmt.siacTMovgest = rmbe.siacTMovgest ")
			.append(" 	AND rbec.siacTBilElem = rmbe.siacTBilElem ")
			.append(" 	AND tmt.dataCancellazione IS NULL ")
			.append(" 	AND rmbe.dataCancellazione IS NULL ")
			.append(" 	AND rbec.dataCancellazione IS NULL ")
			.append(" 	AND tmt.siacTMovgest.dataCancellazione IS NULL ")
			.append(" 	AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
			.append(" 	AND rbec.siacTClass.classifId = :sacId ")
			.toString()),
	
	SUBDOC(Arrays.asList(SiacDCollegamentoTipoEnum.SubdocumentoSpesa, SiacDCollegamentoTipoEnum.SubdocumentoEntrata),
			new StringBuilder()
			.append("SELECT ")
			.append("	siac_t_subdoc.subdoc_id ")
			.append("FROM ")
			.append("  siac_t_subdoc ")
			.append(" ,siac_r_subdoc_movgest_ts ")
			.append(" ,siac_t_movgest_ts ")
			.append(" ,siac_t_movgest ")
			.append(" ,siac_r_movgest_bil_elem ")
			.append(" ,siac_t_bil_elem ")
			.append(" ,siac_r_bil_elem_class ")
			.append("WHERE ")
			.append("    siac_t_subdoc.subdoc_id  = siac_r_subdoc_movgest_ts.subdoc_id ")
			.append("    AND siac_r_subdoc_movgest_ts.movgest_ts_id =  siac_t_movgest_ts.movgest_ts_id ")
			.append("    AND siac_t_movgest_ts.movgest_id = siac_t_movgest.movgest_id ")
			.append("    AND siac_t_movgest_ts.movgest_id = siac_r_movgest_bil_elem.movgest_id ")
			.append("    AND siac_r_movgest_bil_elem.elem_id =  siac_t_bil_elem.elem_id ")
			.append("    AND siac_t_bil_elem.elem_id = siac_r_bil_elem_class.elem_id ")
			.append("	 AND siac_r_bil_elem_class.classif_id = :sacId ")
			.append("    AND siac_t_subdoc.data_cancellazione is null ")
			.append("    AND siac_r_subdoc_movgest_ts.data_cancellazione is null ")
			.append("    AND siac_t_movgest_ts.data_cancellazione is null ")
			.append("    AND siac_t_movgest.data_cancellazione is null ")
			.append("    AND siac_r_movgest_bil_elem.data_cancellazione is null ")
			.append("    AND siac_t_bil_elem.data_cancellazione is null ")
			.append("   AND siac_r_bil_elem_class.data_cancellazione is null ")
			.toString(),
				
			new StringBuilder()
			.append(" SELECT s.subdocId ")
			.append(" FROM SiacTSubdoc s ")
			.append(" JOIN s.siacRSubdocMovgestTs smt ")
			.append(" JOIN smt.siacTMovgestT.siacTMovgest.siacRMovgestBilElems mbe ")
			.append(" JOIN mbe.siacTBilElem.siacRBilElemClasses bec ")
			.append(" WHERE bec.siacTClass.classifId = :sacId ")
			.append(" 	AND bec.dataCancellazione IS NULL ")
			.append(" 	AND mbe.dataCancellazione IS NULL ")
			.append(" 	AND smt.dataCancellazione IS NULL")
			.append(" 	AND s.dataCancellazione IS NULL ")
//			.append(" FROM SiacRSubdocMovgestT rsmt ")
//			.append(" 	, SiacTMovgestT tmt ")
//			.append(" 	, SiacRMovgestBilElem rmbe ")
//			.append(" 	, SiacRBilElemClass rbec ")
//			.append(" WHERE rsmt.siacTMovgestT.siacTMovgest = rmbe.siacTMovgest ")
//			.append(" 	AND rmbe.siacTBilElem = rbec.siacTBilElem ")
//			.append(" 	AND rbec.siacTClass.classifId = :sacId ")
//			.append(" 	AND rsmt.dataCancellazione IS NULL ")
//			.append(" 	AND tmt.dataCancellazione IS NULL ")
//			.append(" 	AND tmt.siacTMovgest.dataCancellazione IS NULL")
//			.append(" 	AND rmbe.dataCancellazione IS NULL ")
//			.append(" 	AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
//			.append(" 	AND rbec.dataCancellazione IS NULL ")
				.toString()),
				
	
	LIQUIDAZIONE(Arrays.asList(SiacDCollegamentoTipoEnum.Liquidazione),
			new StringBuilder()
			 .append(" SELECT siac_t_liquidazione.liq_id ")
			 .append(" FROM ")
			 .append("	  siac_t_liquidazione ") 
			 .append("    ,siac_r_liquidazione_movgest ")
			 .append("    ,siac_t_movgest_ts ")
			 .append("    ,siac_t_movgest ") 
			 .append("    ,siac_r_movgest_bil_elem ")
			 .append("    ,siac_t_bil_elem ")
			 .append("    ,siac_r_bil_elem_class ")
			 .append(" WHERE ")
			 .append(" 	     siac_t_liquidazione.liq_id = siac_r_liquidazione_movgest.liq_id ")
			 .append("    AND siac_r_liquidazione_movgest.movgest_ts_id = siac_t_movgest_ts.movgest_ts_id ")
			 .append("    AND siac_t_movgest_ts.movgest_id = siac_t_movgest.movgest_id ")
			 .append("    AND siac_t_movgest.movgest_id = siac_r_movgest_bil_elem.movgest_id ") 
			 .append("    AND siac_r_movgest_bil_elem.elem_id  = siac_t_bil_elem.elem_id ")
			 .append("    AND siac_t_bil_elem.elem_id  = siac_r_bil_elem_class.elem_id ")
			 .append("    AND siac_r_bil_elem_class.classif_id = :sacId ")
			 .append("    AND siac_t_liquidazione.data_cancellazione is null ")
			 .append("    AND siac_r_liquidazione_movgest.data_cancellazione is null ")
			 .append("    AND siac_t_movgest_ts.data_cancellazione is null ")
			 .append("    AND siac_t_movgest.data_cancellazione is null ")
			 .append("    AND siac_r_movgest_bil_elem.data_cancellazione is null ")
			 .append("    AND siac_t_bil_elem.data_cancellazione is null ")
			 .append("    AND siac_r_bil_elem_class.data_cancellazione is null ")
			 .toString(),	
			new StringBuilder()
			.append(" SELECT tl.liqId ")
			.append(" FROM SiacTLiquidazione tl ")
			.append(" 	JOIN tl.siacRLiquidazioneMovgests rlm ")
			.append(" 	JOIN rlm.siacTMovgestT tmt ")
			.append(" 	JOIN tmt.siacTMovgest.siacRMovgestBilElems rmbe ")
			.append(" 	JOIN rmbe.siacTBilElem.siacRBilElemClasses rbec")
			.append(" WHERE rbec.siacTClass.classifId = :sacId ")
			.append(" 	AND tl.dataCancellazione IS NULL ")
			.append(" 	AND rlm.dataCancellazione IS NULL ")
			.append(" 	AND tmt.dataCancellazione IS NULL ")
			.append(" 	AND rmbe.dataCancellazione IS NULL ")
			.append(" 	AND rbec.dataCancellazione IS NULL ")
//			.append(" SELECT rlm.siacTLiquidazione.liqId ")
//			.append(" FROM SiacRLiquidazioneMovgest rlm ")
//			.append(" 	, SiacTMovgestT tmt ")
//			.append(" 	, SiacRMovgestBilElem rmbe ")
//			.append(" 	, SiacRBilElemClass rbec ")
//			.append(" WHERE rlm.siacTLiquidazione.liqId = rlm.siacTLiquidazione ")
//			.append(" 	AND rlm.siacTMovgestT = tmt ")
//			.append(" 	AND tmt.siacTMovgest = rmbe.siacTMovgest ")
//			.append(" 	AND rmbe.siacTBilElem = rbec.siacTBilElem ")
//			.append(" 	AND rbec.siacTClass.classifId = :sacId ")
//			.append(" 	AND rlm.siacTLiquidazione.dataCancellazione IS NULL ")
//			.append(" 	AND rlm.dataCancellazione IS NULL ")
//			.append(" 	AND tmt.dataCancellazione IS NULL ")
//			.append(" 	AND tmt.siacTMovgest.dataCancellazione IS NULL ")
//			.append(" 	AND rmbe.dataCancellazione IS NULL ")
//			.append(" 	AND rbec.siacTBilElem.dataCancellazione IS NULL ")
//			.append(" 	AND rbec.dataCancellazione IS NULL ")
			.toString()),
	
	ORDINATIVO_INC(Arrays.asList(SiacDCollegamentoTipoEnum.OrdinativoIncasso),
			new StringBuilder()
			
			.append(" SELECT siac_t_ordinativo.ord_id ")
			.append(" FROM ")
			.append("    siac_t_ordinativo ")
			.append("   ,siac_d_ordinativo_tipo ")
			.append("   ,siac_t_ordinativo_ts ")
			.append("   ,siac_r_ordinativo_ts_movgest_ts ")
			.append("   ,siac_t_movgest_ts ")
			.append("   ,siac_t_movgest ")
			.append("   ,siac_r_movgest_bil_elem ")
			.append("   ,siac_t_bil_elem ")
			.append("   ,siac_r_bil_elem_class ") 
			.append("  WHERE ")
			.append("       siac_t_ordinativo.ord_tipo_id  = siac_d_ordinativo_tipo.ord_tipo_id ")
			.append("  AND siac_d_ordinativo_tipo.ord_tipo_code = 'I' ")
			.append("  AND siac_t_ordinativo.ord_id =  siac_t_ordinativo_ts.ord_id ") 
			.append("  AND siac_t_ordinativo_ts.ord_ts_id =  siac_r_ordinativo_ts_movgest_ts.ord_ts_id ")
			.append("  AND siac_r_ordinativo_ts_movgest_ts.movgest_ts_id= siac_t_movgest_ts.movgest_ts_id ")
			.append("  AND siac_t_movgest_ts.movgest_id = siac_t_movgest.movgest_id ")
			.append("  AND siac_t_movgest.movgest_id = siac_r_movgest_bil_elem.movgest_id ")
			.append("  AND siac_r_movgest_bil_elem.elem_id = siac_t_bil_elem.elem_id ") 
			.append("  AND siac_t_bil_elem.elem_id =  siac_r_bil_elem_class.elem_id ")
			.append("  AND siac_r_bil_elem_class.classif_id = :sacId ")
			.append("  AND siac_t_ordinativo.data_cancellazione is null ")
			.append("  AND siac_d_ordinativo_tipo.data_cancellazione is null ")
			.append("  AND siac_t_ordinativo_ts.data_cancellazione is null ")
			.append("  AND siac_r_ordinativo_ts_movgest_ts.data_cancellazione is null ")
			.append("  AND siac_t_movgest_ts.data_cancellazione is null ") 
			.append("  AND siac_t_movgest.data_cancellazione is null ")
			.append("  AND siac_r_movgest_bil_elem.data_cancellazione is null ") 
			.append("  AND siac_t_bil_elem.data_cancellazione is null ") 
			.append("  AND siac_r_bil_elem_class.data_cancellazione is null ")
			
			.toString(), 
			new StringBuilder()
			.append(" SELECT to.ordId ")
			.append(" FROM SiacTOrdinativo to ")
			.append(" 	JOIN to.siacTOrdinativoTs tot ")
			.append(" 	JOIN tot.siacROrdinativoTsMovgestTs rotmt ")
			.append(" 	JOIN rotmt.siacTMovgestT tmt ")
			.append(" 	JOIN tmt.siacTMovgest.siacRMovgestBilElems rmbe ")
			.append(" 	JOIN rmbe.siacTBilElem.siacRBilElemClasses rbec ")
			.append(" WHERE to.siacDOrdinativoTipo.ordTipoCode = 'I' ")
			.append(" 	AND rbec.siacTClass.classifId = :sacId ")
			.append(" 	AND to.dataCancellazione IS NULL ")
			.append(" 	AND to.siacDOrdinativoTipo.dataCancellazione IS NULL ")
			.append(" 	AND tot.dataCancellazione IS NULL ")
			.append(" 	AND rotmt.dataCancellazione IS NULL ")
			.append(" 	AND tmt.dataCancellazione IS NULL ")
			.append(" 	AND rmbe.dataCancellazione IS NULL ")
			.append(" 	AND rbec.dataCancellazione IS NULL ")
			.toString()),
	
	ORDINATIVO_PAG(Arrays.asList(SiacDCollegamentoTipoEnum.OrdinativoPagamento),
			new StringBuilder()
			.append(" select siac_t_ordinativo.ord_id ")
			.append(" FROM ")
			.append(" siac_t_ordinativo ")
			.append("  ,siac_d_ordinativo_tipo ")
			.append("  ,siac_t_ordinativo_ts")
			.append("  ,siac_r_liquidazione_ord ")
			.append("  ,siac_r_liquidazione_movgest ")
			.append("  ,siac_t_movgest_ts ")
			.append("  ,siac_t_movgest ")
			.append("  ,siac_r_movgest_bil_elem ")
			.append("  ,siac_t_bil_elem ")
			.append("  ,siac_r_bil_elem_class ")
			.append(" WHERE ")
			.append(" siac_t_ordinativo.ord_tipo_id  = siac_d_ordinativo_tipo.ord_tipo_id ")
			.append(" AND siac_d_ordinativo_tipo.ord_tipo_code = 'P' ")
			.append(" AND siac_t_ordinativo.ord_id =  siac_t_ordinativo_ts.ord_id ")
			.append(" AND siac_t_ordinativo_ts.ord_ts_id =  siac_r_liquidazione_ord.sord_id ")
			.append(" AND siac_r_liquidazione_ord.liq_id = siac_r_liquidazione_movgest.liq_id ")
			.append(" AND  siac_r_liquidazione_movgest.movgest_ts_id= siac_t_movgest_ts.movgest_ts_id ")
			.append(" AND siac_t_movgest_ts.movgest_id = siac_t_movgest.movgest_id ")
			.append(" AND siac_t_movgest.movgest_id = siac_r_movgest_bil_elem.movgest_id ")
			.append(" AND siac_r_movgest_bil_elem.elem_id = siac_t_bil_elem.elem_id ")
			.append(" AND siac_t_bil_elem.elem_id =  siac_r_bil_elem_class.elem_id ")
			.append(" AND siac_r_bil_elem_class.classif_id = :sacId ")
			.append(" AND siac_t_ordinativo.data_cancellazione is null ")
			.append(" AND siac_d_ordinativo_tipo.data_cancellazione is null ")
			.append(" AND siac_t_ordinativo_ts.data_cancellazione is null ")
			.append(" AND siac_r_liquidazione_ord.data_cancellazione is null ")
			.append(" AND siac_r_liquidazione_movgest.data_cancellazione is null ")
			.append(" AND siac_t_movgest_ts.data_cancellazione is null ")
			.append(" AND siac_t_movgest.data_cancellazione is null ")
			.append(" AND siac_r_movgest_bil_elem.data_cancellazione is null ")
			.append(" AND siac_t_bil_elem.data_cancellazione is null ")
			.append(" AND siac_r_bil_elem_class.data_cancellazione is null ")
			.toString(), 
			new StringBuilder()
			.append(" SELECT to.ordId ")
			.append(" FROM SiacTOrdinativo to ")
			.append(" JOIN to.siacTOrdinativoTs tot ")
			.append(" JOIN tot.siacRLiquidazioneOrds rlo")
			.append(" JOIN rlo.siacTLiquidazione.siacRLiquidazioneMovgests rlm")
			.append(" JOIN rlm.siacTMovgestT tmt ")
			.append(" JOIN tmt.siacTMovgest.siacRMovgestBilElems rmbe ")
			.append(" JOIN rmbe.siacTBilElem.siacRBilElemClasses rbec ")
			.append(" WHERE to.siacDOrdinativoTipo.ordTipoCode = 'P' ")
			.append(" AND rbec.siacTClass.classifId = :sacId ")
			.append(" AND to.dataCancellazione IS NULL ")
			.append(" AND to.siacDOrdinativoTipo.dataCancellazione IS NULL ")
			.append(" AND tot.dataCancellazione IS NULL ")
			.append(" AND tmt.dataCancellazione IS NULL ")
			.append(" AND rmbe.dataCancellazione IS NULL ")
			.append(" AND rbec.dataCancellazione IS NULL")
			.toString()),
			
	MODIFICA(Arrays.asList(SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa, SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata), 
			new StringBuilder()
				.append(" SELECT tmo.mod_id ") 
				.append(" FROM siac_t_modifica tmo ") 
				.append(" JOIN siac_r_modifica_stato rms ON (rms.mod_id = tmo.mod_id AND rms.data_cancellazione IS NULL) ")
				.append(" WHERE tmo.data_cancellazione IS NULL ")
				.append(" AND ( ") 
				.append("     EXISTS ( ") 
				.append("         SELECT 1 ")
				.append("         FROM ")
				.append("            siac_r_movgest_ts_sog_mod ")
				.append("           ,siac_t_movgest_ts ")
				.append("           ,siac_t_movgest ")
				.append("           ,siac_r_movgest_bil_elem ")
				.append("           ,siac_t_bil_elem ")
				.append("           ,siac_r_bil_elem_class ") 
				.append("         WHERE ")
				.append("           	siac_r_movgest_ts_sog_mod.movgest_ts_id = siac_t_movgest_ts.movgest_ts_id ")
				.append("           AND siac_t_movgest_ts.movgest_id =  siac_t_movgest.movgest_id ")
				.append("           AND siac_t_movgest.movgest_id = siac_r_movgest_bil_elem.movgest_id ") 
				.append("           AND siac_r_movgest_bil_elem.elem_id = siac_t_bil_elem.elem_id ") 
				.append("           AND siac_t_bil_elem.elem_id  = siac_r_bil_elem_class.elem_id ") 
				.append("           AND siac_r_bil_elem_class.classif_id = :sacId ")
				.append("           AND siac_r_movgest_ts_sog_mod.data_cancellazione is null ") 
				.append("           AND siac_t_movgest_ts.data_cancellazione is null ")
				.append("           AND siac_t_movgest.data_cancellazione is null ")
				.append("           AND siac_r_movgest_bil_elem.data_cancellazione is null ") 
				.append("           AND siac_t_bil_elem.data_cancellazione is null ")
				.append("           AND siac_r_bil_elem_class.data_cancellazione is null ") 
				.append("     ) ")
				.append("     OR EXISTS ( ") 
				.append("         SELECT 1 ")
				.append("         FROM ")
				.append("              siac_r_movgest_ts_sogclasse_mod ") 
				.append("             ,siac_t_movgest_ts ")
				.append("             ,siac_t_movgest ")
				.append("             ,siac_r_movgest_bil_elem ") 
				.append("             ,siac_t_bil_elem ")
				.append("             ,siac_r_bil_elem_class ") 
				.append("         WHERE ")
				.append("               siac_r_movgest_ts_sogclasse_mod.movgest_ts_id = siac_t_movgest_ts.movgest_ts_id ")
				.append("           AND siac_t_movgest_ts.movgest_id =  siac_t_movgest.movgest_id ")
				.append("           AND siac_t_movgest.movgest_id = siac_r_movgest_bil_elem.movgest_id ") 
				.append("           AND siac_r_movgest_bil_elem.elem_id = siac_t_bil_elem.elem_id ")
				.append("           AND siac_t_bil_elem.elem_id  = siac_r_bil_elem_class.elem_id ") 
				.append("           AND siac_r_bil_elem_class.classif_id = :sacId ") 
				.append("           AND siac_r_movgest_ts_sogclasse_mod.data_cancellazione is null ")
				.append("           AND siac_t_movgest_ts.data_cancellazione is null ") 
				.append("           AND siac_t_movgest.data_cancellazione is null ")
				.append("           AND siac_r_movgest_bil_elem.data_cancellazione is null ") 
				.append("           AND siac_t_bil_elem.data_cancellazione is null ")
				.append("           AND siac_r_bil_elem_class.data_cancellazione is null ") 
				.append("     ) ")
				.append("     OR EXISTS ( ")
				.append("         SELECT 1 ") 
				.append("         FROM ")
				.append("         	 siac_t_movgest_ts_det_mod ") 
				.append("          	,siac_t_movgest_ts ")
				.append("          	,siac_t_movgest ")
				.append("          	,siac_r_movgest_bil_elem ")
				.append("           ,siac_t_bil_elem ")
				.append("           ,siac_r_bil_elem_class ")
				.append("         WHERE ")
				.append("          		siac_t_movgest_ts_det_mod.movgest_ts_id = siac_t_movgest_ts.movgest_ts_id ") 
				.append("           	AND siac_t_movgest_ts.movgest_id = siac_t_movgest.movgest_id ")
				.append("           	AND siac_t_movgest.movgest_id = siac_r_movgest_bil_elem.movgest_id ")
				.append("           	AND siac_r_movgest_bil_elem.elem_id = siac_t_bil_elem.elem_id ")
				.append("            AND siac_t_bil_elem.elem_id = siac_r_bil_elem_class.elem_id ")
				.append("            AND siac_r_bil_elem_class.classif_id = :sacId ") 
				.append("            AND siac_t_movgest_ts_det_mod.data_cancellazione is null ")
				.append("           	AND siac_t_movgest_ts.data_cancellazione is null ") 
				.append("           	AND siac_t_movgest.data_cancellazione is null ")
				.append("           	AND siac_r_movgest_bil_elem.data_cancellazione is null ")
				.append("            AND siac_t_bil_elem.data_cancellazione is null ")
				.append("            AND siac_r_bil_elem_class.data_cancellazione is null ")
				.append("      ) ")
				.append("  ) ")
				.toString(), 
			new StringBuilder()
				.append("SELECT tmo.modId ")
				.append("FROM SiacTModifica tmo ")
				.append("	JOIN tmo.siacRModificaStatos rms ")
				.append("WHERE tmo.dataCancellazione IS NULL ")
				.append("	AND rms.dataCancellazione IS NULL ")
				.append("	AND ( ")
				.append("		EXISTS ( ")
				.append("			SELECT 1 ")
				.append("			FROM SiacRMovgestTsSogMod rmtsm ")
				.append("				JOIN rmtsm.siacTMovgestT.siacTMovgest.siacRMovgestBilElems rmbe ")
				.append("				JOIN rmbe.siacTBilElem.siacRBilElemClasses rbec ")
				.append("			WHERE rbec.siacTClass.classifId = :sacId ")
				.append("				AND rmtsm.dataCancellazione IS NULL ")
				.append("				AND rmbe.dataCancellazione IS NULL ")
				.append("				AND rbec.dataCancellazione IS NULL ")
				.append("		) ")
				.append("		OR EXISTS ( ")
				.append("			SELECT 1 ")
				.append("			FROM SiacRMovgestTsSogclasseMod rmtsm ")
				.append("				JOIN rmtsm.siacTMovgestT.siacTMovgest.siacRMovgestBilElems rmbe ")
				.append("				JOIN rmbe.siacTBilElem.siacRBilElemClasses rbec ")
				.append("			WHERE rbec.siacTClass.classifId = :sacId ")
				.append("				AND rmtsm.dataCancellazione IS NULL ")
				.append("				AND rmbe.dataCancellazione IS NULL ")
				.append("				AND rbec.dataCancellazione IS NULL ")
				.append("		) ")
				.append("		OR EXISTS ( ")
				.append("			SELECT 1 ")
				.append("			FROM SiacTMovgestTsDetMod tmtds ")
				.append("				JOIN tmtds.siacTMovgestT.siacTMovgest.siacRMovgestBilElems rmbe ")
				.append("				JOIN rmbe.siacTBilElem.siacRBilElemClasses rbec ")
				.append("			WHERE rbec.siacTClass.classifId = :sacId ")
				.append("				AND tmtds.dataCancellazione IS NULL ")
				.append("				AND rmbe.dataCancellazione IS NULL ")
				.append("				AND rbec.dataCancellazione IS NULL ")
				.append("		 ) ")
				.append("	) ")
				.toString()),
	
	;
	
	private final List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums;
	private final String nativeSql;
	private final String jpql;
	
	public static MovimentiFromBilElemBySacJpqlEnum bySiacDCollegamentoTipoEnum(SiacDCollegamentoTipoEnum sdcte){
		for(MovimentiFromBilElemBySacJpqlEnum e : MovimentiFromBilElemBySacJpqlEnum.values()){
			if(e.getSiacDCollegamentoTipoEnums().contains(sdcte)){
				return e;
			}
			
		}
		throw new IllegalArgumentException("Il tipo di collegamento "+ sdcte.name() +" non supporta la ricerca per Capitolo. "
				+ "Dettagli: Collegamento tipo: "+ sdcte.name() + " non ha un mapping corrispondente in "+ MovimentiFromBilElemBySacJpqlEnum.class.getSimpleName());
	}
	
	/**
	 * Ottiene il Set di MovimentiFromBilElemJpqlEnum a partire da un insieme di SiacDCollegamentoTipoEnum.
	 * 
	 * @param siacDCollegamentoTipoEnums
	 * @return il set di MovimentiFromBilElemJpqlEnum
	 */
	public static Set<MovimentiFromBilElemBySacJpqlEnum> toMovimentiFromBilElemBySacJpqlEnum(Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums){
		Set<MovimentiFromBilElemBySacJpqlEnum> result = EnumSet.noneOf(MovimentiFromBilElemBySacJpqlEnum.class);
		
		if(siacDCollegamentoTipoEnums!=null){
			for(SiacDCollegamentoTipoEnum e : siacDCollegamentoTipoEnums){
				result.add(bySiacDCollegamentoTipoEnum(e));
			}
		}
		return result;
	}
	
	
	private MovimentiFromBilElemBySacJpqlEnum(List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums, String nativeSql, String jpql){
		this.siacDCollegamentoTipoEnums = siacDCollegamentoTipoEnums;
		this.nativeSql = nativeSql;
		this.jpql = jpql;
	}


	public String getNativeSql() {
		return nativeSql;
	}
	
	public String getJpql() {
		return jpql;
	}

	public List<SiacDCollegamentoTipoEnum> getSiacDCollegamentoTipoEnums() {
		return new ArrayList<SiacDCollegamentoTipoEnum>(siacDCollegamentoTipoEnums);
	}
	
	public Set<String> getSiacDCollegamentoTipoCodes() {
		Set<String> result = new HashSet<String>();

		for(SiacDCollegamentoTipoEnum e : siacDCollegamentoTipoEnums){
			result.add(e.getCodice());
		}
		return result;
	}
	
	
}