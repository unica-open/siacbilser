/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * The Class BaseIsCollegataAMovimentoResiduoConverter.
 *
 * @author Domenico
 */
public abstract class BaseIsCollegataAMovimentoResiduoConverter<A, B> extends DozerConverter<A, B> {

	protected LogUtil log = new LogUtil(this.getClass());

	public BaseIsCollegataAMovimentoResiduoConverter(Class<A> prototypeA, Class<B> prototypeB) {
		super(prototypeA, prototypeB);
	}

	protected void appendSqlWith(StringBuilder sb) {
		sb.append(" with ");
		sb.append(" subdocs as (select s.subdoc_id as id, m.movgest_anno as movgest_anno");
		sb.append("		 	                from siac_t_subdoc s");
		sb.append("		 	                join siac_r_subdoc_movgest_ts rsmt on s.subdoc_id = rsmt.subdoc_id");
		sb.append("		 	                join siac_t_movgest_ts mt on mt.movgest_ts_id = rsmt.movgest_ts_id");
		sb.append("		 	                join siac_t_movgest m on m.movgest_id = mt.movgest_id ");
		sb.append("		 	                where s.data_cancellazione is null");
		sb.append("		 	                and rsmt.data_cancellazione is null");
		sb.append("		 	                and mt.data_cancellazione is null");
		sb.append("		 	                and m.data_cancellazione is null),");
		sb.append("	submov as (select mt.movgest_id as id, m.movgest_anno as movgest_anno");
		sb.append("		 	                from siac_t_movgest_ts mt");
		sb.append("		 	                join siac_t_movgest m on m.movgest_id = mt.movgest_id ");
		sb.append("		 	                where mt.data_cancellazione is null");
		sb.append("		 	                and m.data_cancellazione is null");
		sb.append("		 	                ),");
		sb.append("	liq as (select l.liq_id as id, m.movgest_anno as movgest_anno");
		sb.append("		 	                from siac_t_liquidazione l");
		sb.append("		 	                join siac_r_liquidazione_movgest rlm on rlm.liq_id = l.liq_id");
		sb.append("		 	                join siac_t_movgest_ts mt on mt.movgest_ts_id = rlm.movgest_ts_id");
		sb.append("		 	                join siac_t_movgest m on m.movgest_id = mt.movgest_id ");
		sb.append("		 	                where l.data_cancellazione is null");
		sb.append("		 	                and rlm.data_cancellazione is null");
		sb.append("		 	                and mt.data_cancellazione is null");
		sb.append("		 	                and m.data_cancellazione is null");
		sb.append("		 	                ),");
		sb.append("    ord_spesa as (select o.ord_id as id, m.movgest_anno as movgest_anno");
		sb.append("		 	                from siac_t_ordinativo o");
		sb.append("		 	                join siac_t_ordinativo_ts ot on ot.ord_id = o.ord_id");
		sb.append("		 	                join siac_r_liquidazione_ord rlo on rlo.sord_id = ot.ord_ts_id");
		sb.append("		 	                join siac_r_liquidazione_movgest rlm on rlm.liq_id = rlo.liq_id");
		sb.append("		 	                join siac_t_movgest_ts mt on rlm.movgest_ts_id = mt.movgest_ts_id");
		sb.append("		 	                join siac_t_movgest m on m.movgest_id = mt.movgest_id ");
		sb.append("		 	                where o.data_cancellazione is null");
		sb.append("		 	                and ot.data_cancellazione is null");
		sb.append("		 	                and rlo.data_cancellazione is null");
		sb.append("		 	                and rlm.data_cancellazione is null");
		sb.append("		 	                and mt.data_cancellazione is null");
		sb.append("		 	                and m.data_cancellazione is null");
		sb.append("		 	                ),");
		sb.append("    ord_entrata as (select o.ord_id as id, m.movgest_anno as movgest_anno");
		sb.append("		 	                from siac_t_ordinativo o");
		sb.append("		 	                join siac_t_ordinativo_ts ot on ot.ord_id = o.ord_id");
		sb.append("		 	                join siac_r_ordinativo_ts_movgest_ts rotmt on rotmt.ord_ts_id = ot.ord_ts_id");
		sb.append("		 	                join siac_t_movgest_ts mt on rotmt.movgest_ts_id = mt.movgest_ts_id");
		sb.append("		 	                join siac_t_movgest m on m.movgest_id = mt.movgest_id ");
		sb.append("		 	                where o.data_cancellazione is null");
		sb.append("		 	                and ot.data_cancellazione is null");
		sb.append("		 	                and rotmt.data_cancellazione is null");
		sb.append("		 	                and mt.data_cancellazione is null");
		sb.append("		 	                and m.data_cancellazione is null");
		sb.append("		 	                ) ");
		//TODO aggiungere ModificaMovimentoGestione
		
	}


}
