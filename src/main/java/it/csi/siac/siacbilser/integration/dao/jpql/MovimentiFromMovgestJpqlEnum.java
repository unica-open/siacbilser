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
public enum MovimentiFromMovgestJpqlEnum {
	
	LIQUIDAZIONE(Arrays.asList(SiacDCollegamentoTipoEnum.Liquidazione), 
			new StringBuilder()
				.append(" SELECT ")
				.append(" siac_r_liquidazione_movgest.liq_id ")  
				.append(" FROM ") 
				.append(" siac_t_movgest_ts ")
				.append(" ,siac_r_liquidazione_movgest ")
				.append(" WHERE ")
				.append(" 	   siac_t_movgest_ts.movgest_ts_id = siac_r_liquidazione_movgest.movgest_ts_id ")
				.append(" AND siac_t_movgest_ts.movgest_id = :movgestId ")   
				.append(" AND siac_r_liquidazione_movgest.data_cancellazione is null ")
				.append(" AND siac_t_movgest_ts.data_cancellazione is NULL ") 
			.toString(),
			new StringBuilder()
				.append(" SELECT ") 
				.append("	rlm.siacTLiquidazione.liqId ")  
				.append(" FROM ") 
				.append("	SiacRLiquidazioneMovgest  rlm ")
				.append(" WHERE ") 
				.append("	rlm.siacTMovgestT.siacTMovgest.movgestId = :movgestId ")  			
			.toString()					
			),
	
	SUBDOC(Arrays.asList(SiacDCollegamentoTipoEnum.SubdocumentoSpesa, SiacDCollegamentoTipoEnum.SubdocumentoEntrata), 
			new StringBuilder()
			.append(" SELECT siac_t_subdoc.subdoc_id ")
			.append(" FROM ")
			.append(" siac_t_subdoc ")
			.append(" ,siac_r_subdoc_movgest_ts ")
			.append(" ,siac_t_movgest_ts ")
			.append(" ,siac_t_movgest ")
			.append(" WHERE ")
			.append("     siac_t_subdoc.subdoc_id = siac_r_subdoc_movgest_ts.subdoc_id ")
			.append(" AND siac_r_subdoc_movgest_ts.movgest_ts_id = siac_t_movgest_ts.movgest_ts_id ")
			.append(" AND siac_t_movgest_ts.movgest_id = siac_t_movgest.movgest_id ")
			.append(" AND siac_t_movgest.movgest_id = :movgestId ")
			.append(" AND siac_t_subdoc.data_cancellazione IS NULL ")
			.append(" AND siac_r_subdoc_movgest_ts.data_cancellazione IS NULL ")
			.append(" AND siac_t_movgest_ts.data_cancellazione IS NULL ")
			.append(" AND siac_t_movgest.data_cancellazione IS NULL ")
			.toString(), 
		new StringBuilder()
			.append(" SELECT rsdmg.subdocId ")
			.append(" FROM ")
			.append(" SiacRSubdocMovgestT rsdmg ")
			.append(" WHERE ")
			.append("     rsdmg.dataCancellazione IS NULL ")
			.append(" AND rsdmg.siacTMovgestT.siacTMovgest.movgestId = :movgestId ")
			.toString()),

	ORDINATIVO_INC(Arrays.asList(SiacDCollegamentoTipoEnum.OrdinativoIncasso),
			new StringBuilder()
					.append(" SELECT tot.ord_id ")
					.append(" FROM siac_t_ordinativo_ts tot, siac_r_ordinativo_ts_movgest_ts rotmt, siac_t_movgest_ts tmt, siac_t_movgest tm ")
					.append(" WHERE rotmt.ord_ts_id = tot.ord_ts_id ")
					.append(" AND rotmt.movgest_ts_id = tmt.movgest_ts_id ")
					.append(" AND tmt.movgest_id = tm.movgest_id ")
					.append(" AND tm.movgest_id = :movgestId ")
					.append(" AND tm.data_cancellazione IS NULL ")
					.append(" AND tot.data_cancellazione IS NULL ")
					.append(" AND rotmt.data_cancellazione IS NULL ")
					.append(" AND tmt.data_cancellazione IS NULL ")
				.toString(), 
			new StringBuilder()
				.append(" SELECT tot.siacTOrdinativo.ordId ")
				.append(" FROM SiacTOrdinativoT tot, SiacROrdinativoTsMovgestT rotmt, SiacTMovgestT tmt ")
				.append(" WHERE rotmt.siacTOrdinativoT = tot ")
				.append(" AND rotmt.siacTMovgestT =  tmt ")
				.append(" AND tmt.siacTMovgest.movgestId = :movgestId ")
				.append(" AND tmt.dataCancellazione IS NULL ")  
				.append(" AND tot.dataCancellazione IS NULL ")
				.append(" AND rotmt.dataCancellazione IS NULL ")
				.toString()),
	
	ORDINATIVO_PAG(Arrays.asList(SiacDCollegamentoTipoEnum.OrdinativoPagamento),
			new StringBuilder()
				.append(" SELECT tot.ord_id ")
				.append(" FROM siac_t_ordinativo_ts tot, siac_r_liquidazione_ord rlo ")
				.append(" WHERE rlo.sord_id = tot.ord_ts_id ")
				.append(" AND ")
				.append(" ( ")
				.append("     EXISTS ")
				.append("     ( ")
				.append("         SELECT tl.liq_id, rlm.liq_movgest_id, tmt.movgest_ts_id ")
				.append("         FROM siac_t_liquidazione tl, siac_r_liquidazione_movgest rlm, siac_t_movgest_ts tmt ")
				.append("         WHERE rlo.liq_id = tl.liq_id ")
				.append("         AND rlm.liq_id = tl.liq_id ")
				.append("         AND rlm.movgest_ts_id = tmt.movgest_ts_id ")
				.append("         AND (tmt.data_cancellazione IS NULL) ")
				.append("         AND tmt.movgest_id = :movgestId ")
				.append("         AND (rlm.data_cancellazione IS NULL) ")
				.append("     ) ")
				.append(" ) ")
				.append(" AND (tot.data_cancellazione IS NULL) ")
				.append(" AND (rlo.data_cancellazione IS NULL) ")
				.toString(), 
			new StringBuilder()
				.append(" SELECT tot.siacTOrdinativo.ordId ")
				.append(" FROM SiacTOrdinativoT tot, SiacRLiquidazioneOrd rlo ")
				.append(" WHERE rlo.siacTOrdinativoT = tot ")
				.append(" AND EXISTS( ")
				.append(" 	FROM rlo.siacTLiquidazione tl, SiacRLiquidazioneMovgest rlm, SiacTMovgestT tmt ")
				.append(" 	WHERE rlm.siacTLiquidazione = tl ")
				.append(" 	AND rlm.siacTMovgestT = tmt ")
				.append(" 	AND tmt.dataCancellazione IS NULL ")
				.append(" 	AND tmt.siacTMovgest.movgestId = :movgestId ")
				.append(" 	AND rlm.dataCancellazione IS NULL ")
				.append(" 	) ")
				.append(" AND tot.dataCancellazione IS NULL ")
				.append(" AND rlo.dataCancellazione IS NULL ")
			.toString()),
	;
	private final List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums;
	private final String nativeSql;
	private final String jpql;
	
	public static MovimentiFromMovgestJpqlEnum bySiacDCollegamentoTipoEnum(SiacDCollegamentoTipoEnum sdcte){
		for(MovimentiFromMovgestJpqlEnum e : MovimentiFromMovgestJpqlEnum.values()){
			if(e.getSiacDCollegamentoTipoEnums().contains(sdcte)){
				return e;
			}
			
		}
		throw new IllegalArgumentException("Il tipo di collegamento "+ sdcte.name() +" non supporta la ricerca per Capitolo. "
				+ "Dettagli: Collegamento tipo: "+ sdcte.name() + " non ha un mapping corrispondente in "+ MovimentiFromMovgestJpqlEnum.class.getSimpleName());
	}
	
	/**
	 * Ottiene il Set di MovimentiFromBilElemJpqlEnum a partire da un insieme di SiacDCollegamentoTipoEnum.
	 * 
	 * @param siacDCollegamentoTipoEnums
	 * @return il set di MovimentiFromBilElemJpqlEnum
	 */
	public static Set<MovimentiFromMovgestJpqlEnum> toMovimentiFromBilElemJpqlEnum(Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums){
		Set<MovimentiFromMovgestJpqlEnum> result = EnumSet.noneOf(MovimentiFromMovgestJpqlEnum.class);
		
		if(siacDCollegamentoTipoEnums!=null){
			for(SiacDCollegamentoTipoEnum e : siacDCollegamentoTipoEnums){
				result.add(bySiacDCollegamentoTipoEnum(e));
			}
		}
		return result;
	}
	
	
	private MovimentiFromMovgestJpqlEnum(List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums, String nativeSql, String jpql){
		this.siacDCollegamentoTipoEnums = siacDCollegamentoTipoEnums;
		this.nativeSql = nativeSql;
		this.jpql = jpql;
	}

//  NON ANCORA GESTITO
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