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
public enum MovimentiFromBilElemJpqlEnum {
	
	MOVGEST(Arrays.asList(SiacDCollegamentoTipoEnum.Impegno, SiacDCollegamentoTipoEnum.Accertamento),
			new StringBuilder()
				.append(" SELECT tm.movgest_id ")
				.append(" FROM siac_t_movgest tm ")
				.append(" JOIN siac_r_movgest_bil_elem rmbe ON (rmbe.movgest_id = tm.movgest_id AND rmbe.data_cancellazione IS NULL) ")
				.append(" WHERE tm.data_cancellazione IS NULL ")
				.append(" AND rmbe.elem_id = :elemId ")
				.toString(),
			new StringBuilder()
				.append(" SELECT tm.movgestId ")
				.append(" FROM SiacRMovgestBilElem rmbe, SiacTMovgest tm ")
				.append(" WHERE rmbe.siacTBilElem.elemId = :elemId ")
				.append(" AND rmbe.siacTMovgest = tm ")
				.append(" AND rmbe.dataCancellazione IS NULL ")
				.append(" AND tm.dataCancellazione IS NULL ")
				.append(" AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
			.toString()),
	
	SUBMOVGEST(Arrays.asList(SiacDCollegamentoTipoEnum.SubImpegno, SiacDCollegamentoTipoEnum.SubAccertamento),
			new StringBuilder()
				.append(" SELECT tmt.movgest_ts_id ")
				.append(" FROM siac_t_movgest_ts tmt ")
				.append(" JOIN siac_r_movgest_bil_elem rmbe ON (rmbe.movgest_id = tmt.movgest_id AND rmbe.data_cancellazione IS NULL) ")
				.append(" WHERE tmt.data_cancellazione IS NULL ")
				.append(" AND rmbe.elem_id = :elemId ")
				.toString(),
			new StringBuilder()
				.append(" SELECT tmt.movgestTsId ")
				.append(" FROM  SiacRMovgestBilElem rmbe, SiacTMovgestT tmt ")
				.append(" WHERE rmbe.siacTBilElem.elemId = :elemId ")
				.append(" AND rmbe.siacTMovgest = tmt.siacTMovgest ")
				.append(" AND rmbe.dataCancellazione IS NULL ")
				.append(" AND tmt.dataCancellazione IS NULL ")
				.append(" AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
			.toString()),
	
	SUBDOC(Arrays.asList(SiacDCollegamentoTipoEnum.SubdocumentoSpesa, SiacDCollegamentoTipoEnum.SubdocumentoEntrata),
			new StringBuilder()
				.append(" SELECT ts.subdoc_id ")
				.append(" FROM siac_t_subdoc ts ")
				.append(" JOIN siac_r_subdoc_movgest_ts rsmt ON (rsmt.subdoc_id = ts.subdoc_id AND rsmt.data_cancellazione IS NULL) ")
				.append(" JOIN siac_t_movgest_ts tmt ON (tmt.movgest_ts_id = rsmt.movgest_ts_id AND tmt.data_cancellazione IS NULL) ")
				.append(" JOIN siac_r_movgest_bil_elem rmbe ON (rmbe.movgest_id = tmt.movgest_id AND rmbe.data_cancellazione IS NULL) ")
				.append(" WHERE ts.data_cancellazione IS NULL ")
				.append(" AND rmbe.elem_id = :elemId ")
				.toString(), 
			new StringBuilder()
				.append(" SELECT rsmt.siacTSubdoc.subdocId ")
				.append(" FROM SiacRSubdocMovgestT rsmt, SiacTMovgestT tmt ")
				.append(" WHERE rsmt.siacTMovgestT = tmt ")
				.append(" AND EXISTS( ")
				.append("    FROM  tmt.siacTMovgest tm, SiacRMovgestBilElem rmbe ")
				.append(" 	 WHERE rmbe.siacTBilElem.elemId = :elemId ")
				.append(" 	 AND rmbe.siacTMovgest = tm	 ")
				.append(" 	 AND rmbe.dataCancellazione IS NULL ")
				.append(" 	 AND tm.dataCancellazione IS NULL ")
				.append(" 	 AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
				.append(" ) ")
				.append(" AND rsmt.dataCancellazione IS NULL ")
				.append(" AND rsmt.siacTSubdoc.dataCancellazione IS NULL ") 
				.toString()),
	
	LIQUIDAZIONE(Arrays.asList(SiacDCollegamentoTipoEnum.Liquidazione),
			new StringBuilder()
				.append(" SELECT tl.liq_id ")
				.append(" FROM siac_t_liquidazione tl ")
				.append(" JOIN siac_r_liquidazione_movgest rlm ON (rlm.liq_id = tl.liq_id AND rlm.data_cancellazione IS NULL )")
				.append(" JOIN siac_t_movgest_ts tmt ON (tmt.movgest_ts_id = rlm.movgest_ts_id AND tmt.data_cancellazione IS NULL) ")
				.append(" JOIN siac_r_movgest_bil_elem rmbe ON (rmbe.movgest_id = tmt.movgest_id AND rmbe.data_cancellazione IS NULL) ")
				.append(" WHERE tl.data_cancellazione IS NULL ")
				.append(" AND rmbe.elem_id = :elemId ")
				.toString(),
			new StringBuilder()
				.append(" SELECT tl.liqId ")
				.append(" FROM SiacTLiquidazione tl, SiacRLiquidazioneMovgest rlm ")
				.append(" WHERE rlm.siacTLiquidazione = tl ")
				.append(" AND rlm.dataCancellazione IS NULL ")
				.append(" AND tl.dataCancellazione IS NULL ")
				.append(" AND EXISTS( ")
				.append(" 	FROM  rlm.siacTMovgestT tmt, SiacRMovgestBilElem rmbe ")
				.append(" 	WHERE rmbe.siacTBilElem.elemId = :elemId ")
				.append(" 	AND rmbe.siacTMovgest = tmt.siacTMovgest ")
				.append(" 	AND rmbe.dataCancellazione IS NULL ")
				.append(" 	AND tmt.dataCancellazione IS NULL ")
				.append(" 	AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
				.append(" ) ")
			.toString()),
	
	ORDINATIVO_INC(Arrays.asList(SiacDCollegamentoTipoEnum.OrdinativoIncasso),
			new StringBuilder()
			.append(" SELECT tor.ord_id ")
			.append(" FROM siac_t_ordinativo tor ")
			.append(" JOIN siac_d_ordinativo_tipo dot ON (dot.ord_tipo_id = tor.ord_tipo_id AND dot.data_cancellazione IS NULL) ")
			.append(" JOIN siac_t_ordinativo_ts tot ON (tot.ord_id = tor.ord_id AND tot.data_cancellazione IS NULL) ")
			.append(" JOIN siac_r_ordinativo_ts_movgest_ts rotmt ON (rotmt.ord_ts_id = tot.ord_ts_id AND rotmt.data_cancellazione IS NULL) ")
			.append(" JOIN siac_t_movgest_ts tmt ON (tmt.movgest_ts_id = rotmt.movgest_ts_id AND tmt.data_cancellazione IS NULL) ")
			.append(" JOIN siac_r_movgest_bil_elem rmbe ON (rmbe.movgest_id = tmt.movgest_id AND rmbe.data_cancellazione IS NULL) ")
			.append(" WHERE tor.data_cancellazione IS NULL ")
			.append(" AND rmbe.elem_id = :elemId ")
			.append(" AND dot.ord_tipo_code = 'I' ")
			.toString(), 
			new StringBuilder()
				.append(" SELECT tot.siacTOrdinativo.ordId ")
				.append(" FROM SiacTOrdinativoT tot, SiacROrdinativoTsMovgestT rotmt ")
				.append(" WHERE rotmt.siacTOrdinativoT = tot ")
				.append(" AND EXISTS( ")
				.append(" 	FROM  rotmt.siacTMovgestT tmt, SiacRMovgestBilElem rmbe ")  
				.append(" 	WHERE rmbe.siacTBilElem.elemId = :elemId ")
				.append(" 	AND rmbe.siacTMovgest = tmt.siacTMovgest ")
				.append(" 	AND rmbe.dataCancellazione IS NULL ")
				.append(" 	AND tmt.dataCancellazione IS NULL ")
				.append(" 	AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
				.append(" ) ")
				.append(" AND tot.dataCancellazione IS NULL ")
				.append(" AND rotmt.dataCancellazione IS NULL ") 
				.toString()),
	
	ORDINATIVO_PAG(Arrays.asList(SiacDCollegamentoTipoEnum.OrdinativoPagamento),
			new StringBuilder()
			.append(" SELECT tor.ord_id ")
			.append(" FROM siac_t_ordinativo tor ")
			.append(" JOIN siac_d_ordinativo_tipo dot ON (dot.ord_tipo_id = tor.ord_tipo_id AND dot.data_cancellazione IS NULL) ")
			.append(" JOIN siac_t_ordinativo_ts tot ON (tot.ord_id = tor.ord_id AND tot.data_cancellazione IS NULL) ")
			.append(" JOIN siac_r_liquidazione_ord rlo ON (rlo.sord_id = tot.ord_ts_id AND rlo.data_cancellazione IS NULL) ")
			.append(" JOIN siac_r_liquidazione_movgest rlm ON (rlm.liq_id = rlo.liq_id AND rlm.data_cancellazione IS NULL )")
			.append(" JOIN siac_t_movgest_ts tmt ON (tmt.movgest_ts_id = rlm.movgest_ts_id AND tmt.data_cancellazione IS NULL) ")
			.append(" JOIN siac_r_movgest_bil_elem rmbe ON (rmbe.movgest_id = tmt.movgest_id AND rmbe.data_cancellazione IS NULL) ")
			.append(" WHERE tor.data_cancellazione IS NULL ")
			.append(" AND rmbe.elem_id = :elemId ")
			.append(" AND dot.ord_tipo_code = 'P' ")
			.toString(), 
			new StringBuilder()
				.append(" SELECT tot.siacTOrdinativo.ordId ")
				.append(" FROM SiacTOrdinativoT tot, SiacRLiquidazioneOrd rlo ")
				.append(" WHERE rlo.siacTOrdinativoT = tot ")
				.append(" AND EXISTS( ")
				.append(" 	FROM rlo.siacTLiquidazione tl, SiacRLiquidazioneMovgest rlm ")
				.append(" 	WHERE rlm.siacTLiquidazione = tl ")
				.append(" 	AND EXISTS( ")
				.append(" 		FROM  rlm.siacTMovgestT tmt, SiacRMovgestBilElem rmbe ")
				.append(" 		WHERE rmbe.siacTBilElem.elemId = :elemId ")
				.append(" 		AND rmbe.siacTMovgest = tmt.siacTMovgest ")
				.append(" 		AND rmbe.dataCancellazione IS NULL ")
				.append(" 		AND tmt.dataCancellazione IS NULL ")
				.append(" 		AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
				.append(" 	) ")
				.append(" 	AND tl.dataCancellazione IS NULL ")
				.append(" 	AND rlm.dataCancellazione IS NULL ")
				.append(" 	) ")
				.append(" AND tot.dataCancellazione IS NULL ")
				.append(" AND rlo.dataCancellazione IS NULL ")  
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
				.append("         FROM siac_r_movgest_ts_sog_mod rmtsm ")
				.append("         JOIN siac_t_movgest_ts tmt ON (tmt.movgest_ts_id = rmtsm.movgest_ts_id AND tmt.data_cancellazione IS NULL) ")
				.append("         JOIN siac_t_movgest tm ON (tm.movgest_id = tmt.movgest_id AND tm.data_cancellazione IS NULL) ")
				.append("         JOIN siac_r_movgest_bil_elem rmbe ON (rmbe.movgest_id = tm.movgest_id AND rmbe.data_cancellazione IS NULL) ")
				.append("         WHERE rmtsm.mod_stato_r_id = rms.mod_stato_r_id ")
				.append("         AND rmtsm.data_cancellazione IS NULL ")
				.append("         AND rmbe.elem_id = :elemId ")
				.append("     ) ")
				.append("     OR EXISTS ( ")
				.append("         SELECT 1 ")
				.append("         FROM siac_r_movgest_ts_sogclasse_mod rmtsm ")
				.append("         JOIN siac_t_movgest_ts tmt ON tmt.movgest_ts_id = rmtsm.movgest_ts_id ")
				.append("         JOIN siac_t_movgest tm ON (tm.movgest_id = tmt.movgest_id AND tm.data_cancellazione IS NULL) ")
				.append("         JOIN siac_r_movgest_bil_elem rmbe ON (rmbe.movgest_id = tm.movgest_id AND rmbe.data_cancellazione IS NULL) ")
				.append("         WHERE rmtsm.mod_stato_r_id = rms.mod_stato_r_id ")
				.append("         AND rmtsm.data_cancellazione IS NULL ")
				.append("         AND rmbe.elem_id = :elemId ")
				.append("     ) ")
				.append("     OR EXISTS ( ")
				.append("         SELECT 1 ")
				.append("         FROM siac_t_movgest_ts_det_mod rmtdm ")
				.append("         JOIN siac_t_movgest_ts tmt ON tmt.movgest_ts_id = rmtdm.movgest_ts_id ")
				.append("         JOIN siac_t_movgest tm ON (tm.movgest_id = tmt.movgest_id AND tm.data_cancellazione IS NULL) ")
				.append("         JOIN siac_r_movgest_bil_elem rmbe ON (rmbe.movgest_id = tm.movgest_id AND rmbe.data_cancellazione IS NULL) ")
				.append("         WHERE rmtdm.mod_stato_r_id = rms.mod_stato_r_id ")
				.append("         AND rmtdm.data_cancellazione IS NULL ")
				.append("         AND rmbe.elem_id = :elemId ")
				.append("     ) ")
				.append(" ) ")
				.toString(), 
			new StringBuilder()
				.append(" SELECT rms.siacTModifica.modId ")
				.append(" FROM SiacRModificaStato rms ")
				.append(" WHERE rms.dataCancellazione IS NULL ")
				.append(" AND ( ")
				.append(" EXISTS( ")
				.append(" 	FROM SiacRMovgestTsSogMod rmtsm, SiacRMovgestBilElem rmbe ")
				.append(" 	WHERE rmtsm.siacTMovgestT.siacTMovgest = rmbe.siacTMovgest ")
				.append(" 	AND rmtsm.siacRModificaStato = rms ")
				.append(" 	AND rmtsm.dataCancellazione IS NULL ")
				.append(" 	AND rmtsm.siacTMovgestT.dataCancellazione IS NULL ")
				.append(" 	AND rmbe.siacTMovgest.dataCancellazione IS NULL ")
				.append(" 	AND rmbe.dataCancellazione IS NULL ")
				.append(" 	AND rmbe.siacTBilElem.elemId = :elemId ")
				.append(" 	AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
				.append(" 	) ")
				.append(" OR EXISTS( ")
				.append(" 	FROM SiacRMovgestTsSogclasseMod rmtscm, SiacRMovgestBilElem rmbe ")
				.append(" 	WHERE rmtscm.siacTMovgestT.siacTMovgest = rmbe.siacTMovgest ")
				.append(" 	AND rmtscm.dataCancellazione IS NULL ")
				.append(" 	AND rmtscm.siacRModificaStato = rms ")
				.append(" 	AND rmbe.dataCancellazione IS NULL ")
				.append(" 	AND rmtscm.siacTMovgestT.dataCancellazione IS NULL ")
				.append(" 	AND rmbe.siacTMovgest.dataCancellazione IS NULL ")
				.append(" 	AND rmbe.siacTBilElem.elemId = :elemId ")
				.append(" 	AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
				.append(" 	) ")
				.append(" OR EXISTS( ")
				.append(" 	FROM SiacTMovgestTsDetMod rmtsdm, SiacRMovgestBilElem rmbe ")
				.append(" 	WHERE rmtsdm.siacTMovgestT.siacTMovgest = rmbe.siacTMovgest ")
				.append(" 	AND rmtsdm.siacRModificaStato = rms ")
				.append(" 	AND rmtsdm.dataCancellazione IS NULL ")
				.append(" 	AND rmtsdm.siacTMovgestT.dataCancellazione IS NULL ")
				.append(" 	AND rmbe.siacTMovgest.dataCancellazione IS NULL ")
				.append(" 	AND rmbe.dataCancellazione IS NULL ")
				.append(" 	AND rmbe.siacTBilElem.elemId = :elemId ")
				.append(" 	AND rmbe.siacTBilElem.dataCancellazione IS NULL ")
				.append(" 	) ")
				.append(" ) ")
				.toString()),
	
	;
	
	private final List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums;
	private final String nativeSql;
	private final String jpql;
	
	public static MovimentiFromBilElemJpqlEnum bySiacDCollegamentoTipoEnum(SiacDCollegamentoTipoEnum sdcte){
		for(MovimentiFromBilElemJpqlEnum e : MovimentiFromBilElemJpqlEnum.values()){
			if(e.getSiacDCollegamentoTipoEnums().contains(sdcte)){
				return e;
			}
			
		}
		throw new IllegalArgumentException("Il tipo di collegamento "+ sdcte.name() +" non supporta la ricerca per Capitolo. "
				+ "Dettagli: Collegamento tipo: "+ sdcte.name() + " non ha un mapping corrispondente in "+ MovimentiFromBilElemJpqlEnum.class.getSimpleName());
	}
	
	/**
	 * Ottiene il Set di MovimentiFromBilElemJpqlEnum a partire da un insieme di SiacDCollegamentoTipoEnum.
	 * 
	 * @param siacDCollegamentoTipoEnums
	 * @return il set di MovimentiFromBilElemJpqlEnum
	 */
	public static Set<MovimentiFromBilElemJpqlEnum> toMovimentiFromBilElemJpqlEnum(Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums){
		Set<MovimentiFromBilElemJpqlEnum> result = EnumSet.noneOf(MovimentiFromBilElemJpqlEnum.class);
		
		if(siacDCollegamentoTipoEnums!=null){
			for(SiacDCollegamentoTipoEnum e : siacDCollegamentoTipoEnums){
				result.add(bySiacDCollegamentoTipoEnum(e));
			}
		}
		return result;
	}
	
	
	private MovimentiFromBilElemJpqlEnum(List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums, String nativeSql, String jpql){
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