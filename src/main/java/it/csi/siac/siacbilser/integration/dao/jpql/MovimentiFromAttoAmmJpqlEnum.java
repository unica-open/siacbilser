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

import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;

/**
 * Enumera gli SQL necessari ad individuare i Movimenti a partire dall'id di {@link SiacTAttoAmm}.
 * 
 * @author Domenico
 */




public enum MovimentiFromAttoAmmJpqlEnum {
	
	IMP(Arrays.asList(SiacDCollegamentoTipoEnum.Impegno, SiacDCollegamentoTipoEnum.Accertamento), 
			new StringBuilder().append(" select m.movgest_id ")
								.append(" from siac_t_movgest m ")
								.append(" join siac_t_movgest_ts mt on m.movgest_id = mt.movgest_id ")
								.append(" join siac_r_movgest_ts_atto_amm rmta on mt.movgest_ts_id = rmta.movgest_ts_id ")
								.append(" where rmta.data_cancellazione is null ")
								.append(" and m.data_cancellazione is null ")
								.append(" and mt.data_cancellazione is null ")
								.append(" and rmta.attoamm_id = :attoammId ")
								.toString(),
			new StringBuilder().append(" SELECT rmta.siacTMovgestT.siacTMovgest.movgestId ") 
								.append(" FROM SiacRMovgestTsAttoAmm rmta ")
								.append(" WHERE rmta.siacTAttoAmm.attoammId = :attoammId ")
								.append(" AND rmta.dataCancellazione is null  ")
								.append(" AND rmta.siacTMovgestT.siacTMovgest.dataCancellazione is null  ")
								.append(" AND rmta.siacTMovgestT.dataCancellazione is null  ")
								.toString()		
												
			),
	
	SUBIMP(Arrays.asList(SiacDCollegamentoTipoEnum.SubImpegno, SiacDCollegamentoTipoEnum.SubAccertamento), 
			new StringBuilder().append(" select mt.movgest_ts_id ")
								.append(" from siac_t_movgest_ts mt ")
								.append(" join siac_r_movgest_ts_atto_amm rmta on mt.movgest_ts_id = rmta.movgest_ts_id ")
								.append(" where rmta.data_cancellazione is null ")
								.append(" and mt.data_cancellazione is null ")
								.append(" and rmta.attoamm_id = :attoammId ")
								.toString(),
	
	new StringBuilder().append(" select rmta.siacTMovgestT.movgestTsId ")
								.append(" FROM SiacRMovgestTsAttoAmm rmta   ")
								.append(" WHERE ")
								.append("     rmta.siacTAttoAmm.attoammId = :attoammId ")
								.append(" and rmta.dataCancellazione is null ")
								.append(" and rmta.siacTMovgestT.dataCancellazione is null ")
								
			.toString()
	),
	
	SUBDOC(Arrays.asList(SiacDCollegamentoTipoEnum.SubdocumentoSpesa, SiacDCollegamentoTipoEnum.SubdocumentoEntrata), 
			new StringBuilder().append(" SELECT s.subdoc_id ")
								.append(" FROM siac_t_subdoc s ")
								.append(" JOIN siac_r_subdoc_atto_amm rsa ON rsa.subdoc_id = s.subdoc_id ")
								.append(" WHERE rsa.data_cancellazione IS null ")
								.append(" and rsa.attoamm_id = :attoammId ")
								.toString(),
								new StringBuilder().append(" SELECT rsa.siacTSubdoc.subdocId ")
								.append(" FROM SiacRSubdocAttoAmm rsa ")
								.append(" WHERE rsa.dataCancellazione IS null ")
								.append(" AND rsa.siacTSubdoc.dataCancellazione IS null")
								.append(" AND rsa.siacTAttoAmm.attoammId = :attoammId ")
								.toString()					
			),
	
	LIQUIDAZIONE(Arrays.asList(SiacDCollegamentoTipoEnum.Liquidazione), 
			new StringBuilder().append(" select l.liq_id ")
								.append(" from siac_t_liquidazione l ")
								.append(" join siac_r_liquidazione_atto_amm rlam on l.liq_id = rlam.liq_id ")
								.append(" where l.data_cancellazione is null ")
								.append(" and rlam.data_cancellazione is null ")
								.append(" and rlam.attoamm_id = :attoammId ")
								.toString(),
			new StringBuilder().append(" select rlam.siacTLiquidazione.liqId ")
								.append(" from SiacRLiquidazioneAttoAmm rlam ")
								.append(" where rlam.siacTLiquidazione.dataCancellazione IS null ")
								.append(" AND rlam.dataCancellazione IS null ")
								.append(" AND rlam.siacTAttoAmm.attoammId = :attoammId ")
								.toString()					
			),
	
	ORDINATIVO(Arrays.asList(SiacDCollegamentoTipoEnum.OrdinativoIncasso, SiacDCollegamentoTipoEnum.OrdinativoPagamento), 
			new StringBuilder().append(" select o.ord_id ")
								.append(" from siac_t_ordinativo o ")
								.append(" join siac_r_ordinativo_atto_amm roam on o.ord_id = roam.ord_id ")
								.append(" where o.data_cancellazione is null ")
								.append(" and roam.data_cancellazione is null ")
								.append(" and roam.attoamm_id = :attoammId ")
								.toString(),
			new StringBuilder().append(" select roam.siacTOrdinativo.ordId ")
								.append(" from SiacROrdinativoAttoAmm roam")
								.append(" where roam.siacTOrdinativo.dataCancellazione IS null ")
								.append(" AND roam.dataCancellazione IS null ")
								.append(" AND roam.siacTAttoAmm.attoammId = :attoammId ")
								.toString()					
			),
	
	MODIFICA(Arrays.asList(SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa, SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata), 
			new StringBuilder().append(" select m.mod_id ")
								.append(" from siac_t_modifica m ")
								.append(" where m.data_cancellazione is null ")
								.append(" and m.attoamm_id = :attoammId ")
								.toString(),
								new StringBuilder().append(" select m.modId ")
								.append(" from SiacTModifica m ")
								.append(" WHERE m.dataCancellazione is null ")
								.append(" AND m.siacTAttoAmm.attoammId = :attoammId ")
								.toString()					
			),
	
	
	;
	
	private final String jpql;
	private final String nativeSql;
	private final List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums;
	
	
	public static MovimentiFromAttoAmmJpqlEnum bySiacDCollegamentoTipoEnum(SiacDCollegamentoTipoEnum sdcte){
		for(MovimentiFromAttoAmmJpqlEnum e : MovimentiFromAttoAmmJpqlEnum.values()){
			if(e.getSiacDCollegamentoTipoEnums().contains(sdcte)){
				return e;
			}
			
		}
		throw new IllegalArgumentException("Il tipo di collegamento "+ sdcte.name() +" non supporta la ricerca per Atto Amministrativo. "
				+ "Dettagli: Collegamento tipo: "+ sdcte.name() 
				+ " non ha un mapping corrispondente in "+ MovimentiFromAttoAmmJpqlEnum.class.getSimpleName());
	}
	
	/**
	 * Ottiene il Set di MovimentiFromAttoAmmJpqlEnum a partire da un insieme di SiacDCollegamentoTipoEnum.
	 * 
	 * @param siacDCollegamentoTipoEnums
	 * @return il set di MovimentiFromAttoAmmJpqlEnum
	 */
	public static Set<MovimentiFromAttoAmmJpqlEnum> toMovimentiFromAttoAmmJpqlEnum(Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums){
		Set<MovimentiFromAttoAmmJpqlEnum> result = EnumSet.noneOf(MovimentiFromAttoAmmJpqlEnum.class);
		
		if(siacDCollegamentoTipoEnums!=null){
			for(SiacDCollegamentoTipoEnum e : siacDCollegamentoTipoEnums){
				result.add(bySiacDCollegamentoTipoEnum(e));
			}
		}
		return result;
	}
	
	
	private MovimentiFromAttoAmmJpqlEnum(List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums, String nativeSql, String jpql){
		this.siacDCollegamentoTipoEnums = siacDCollegamentoTipoEnums;
		this.nativeSql = nativeSql;
		this.jpql = jpql;
	}

	/**
	 * @return the jpql
	 */
	public String getJpql() {
		return jpql;
	}

	/**
	 * @return the nativeSql
	 */
	public String getNativeSql() {
		return nativeSql;
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