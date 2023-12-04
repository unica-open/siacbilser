/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * The Class PrimaNotaIsCollegataAMovimentoResiduoConverter.
 * 
 * @author Domenico
 */
@Component
public class PrimaNotaIsCollegataAMovimentoResiduoConverter extends BaseIsCollegataAMovimentoResiduoConverter<PrimaNota, SiacTPrimaNota> {
	
	@PersistenceContext
	protected EntityManager entityManager;

	public PrimaNotaIsCollegataAMovimentoResiduoConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		String methodName = "convertFrom";
		
		Query query = entityManager.createNativeQuery(getSql());
		query.setParameter("pnotaId", src.getUid());

		List<Object[]> resultList = query.getResultList();
		log.debug(methodName, "resultList: "+resultList);
		
		Boolean result = Boolean .FALSE;
		for(Object[] record:resultList){
			if(record!=null){
				Boolean b = (Boolean)record[3];
				if(Boolean.TRUE.equals(b)){
					result = Boolean.TRUE;
					break;
				}
			}
		}
		
		dest.setIsCollegataAMovimentoResiduo(result);
		return dest;
	}

	private String getSql() {
		StringBuilder sb = new StringBuilder();

		appendSqlWith(sb);
		sb.append("      SELECT                                                           ");
		sb.append("		 	distinct p.pnota_id, dct.collegamento_tipo_code, rerm.campo_pk_id,");
		sb.append("		 	(case when dct.collegamento_tipo_code IN ('I', 'A')");
		sb.append("		 	 		then exists(select 1 from siac_t_movgest where movgest_id = rerm.campo_pk_id ");
		sb.append("			 	             and movgest_anno < cast(pe.anno as int)");
		sb.append("			 	             )");
		sb.append("			 	  when dct.collegamento_tipo_code IN ('SI', 'SA')");
		sb.append("		 	 		then exists(select 1 from submov where id = rerm.campo_pk_id ");
		sb.append("			 	             and movgest_anno < cast(pe.anno as int)");
		sb.append("			 	             )");
		sb.append("		 	      when dct.collegamento_tipo_code IN ('SS', 'SE')");
		sb.append("		 	 		then exists(select 1 from subdocs where id = rerm.campo_pk_id ");
		sb.append("		 	                and movgest_anno < cast(pe.anno as int)");
		sb.append("			 	             )");
		sb.append("			 	  when dct.collegamento_tipo_code IN ('L')");
		sb.append("		 	 		then exists(select 1 from liq where id = rerm.campo_pk_id ");
		sb.append("		 	                and movgest_anno < cast(pe.anno as int)");
		sb.append("			 	             )");
		sb.append("			 	  when dct.collegamento_tipo_code IN ('OI')");
		sb.append("		 	 		then exists(select 1 from ord_entrata where id = rerm.campo_pk_id ");
		sb.append("		 	                and movgest_anno < cast(pe.anno as int)");
		sb.append("			 	             )");
		sb.append("			 	  when dct.collegamento_tipo_code IN ('OP')");
		sb.append("		 	 		then exists(select 1 from ord_spesa where id = rerm.campo_pk_id ");
		sb.append("		 	                and movgest_anno < cast(pe.anno as int)");
		sb.append("			 	             )");
		sb.append("		 	 else ");
		sb.append("		 	  false");
		sb.append("		 	end) as is_residuo");
		sb.append("		 FROM                                                             ");
		sb.append("		 	siac_t_prima_nota p,");
		sb.append("		 	siac_t_bil b,");
		sb.append("		 	siac_t_periodo pe,");
		sb.append("		 	siac_d_causale_ep_tipo cet,");
		sb.append("		 	siac_t_mov_ep tme,                                             ");
		sb.append("		 	siac_r_evento_reg_movfin rerm,                                 ");
		sb.append("		 	siac_d_evento de,                                              ");
		sb.append("		 	siac_d_collegamento_tipo dct                                   ");
		sb.append("		 WHERE                                                            ");
		sb.append("		 	p.data_cancellazione is null");
		sb.append("		 	and b.data_cancellazione is null");
		sb.append("		 	and pe.data_cancellazione is null");
		sb.append("		 	and cet.data_cancellazione is null                             ");
		sb.append("		 	and tme.data_cancellazione is null                             ");
		sb.append("		 	and rerm.data_cancellazione is null	                           ");
		sb.append("		 	and de.data_cancellazione is null                              ");
		sb.append("		 	and dct.data_cancellazione is null                             ");
		//Join
		sb.append("		 	and p.causale_ep_tipo_id = cet.causale_ep_tipo_id ");
		sb.append("		 	and p.bil_id = b.bil_id");
		sb.append("		 	and b.periodo_id = pe.periodo_id");
		sb.append("		 	and p.pnota_id = tme.regep_id  ");
		sb.append("		 	and rerm.regmovfin_id = tme.regmovfin_id                       ");
		sb.append("		 	and rerm.evento_id = de.evento_id                              ");
		sb.append("		 	and de.collegamento_tipo_id = dct.collegamento_tipo_id         ");
		//Base Filter
		sb.append("		 	and cet.causale_ep_tipo_code = 'INT'");
		sb.append("		 	and p.pnota_id = :pnotaId ");
				

		return sb.toString();
	}

	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
	
		return dest;
	}



	

}
