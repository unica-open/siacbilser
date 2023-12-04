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

import it.csi.siac.siacbilser.integration.entity.SiacTRegMovfin;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * The Class RegistrazioneMovFinIsCollegataAMovimentoResiduoConverter.
 *
 * @author Domenico
 */
@Component
public class RegistrazioneMovFinIsCollegataAMovimentoResiduoConverter extends BaseIsCollegataAMovimentoResiduoConverter<RegistrazioneMovFin, SiacTRegMovfin > {
	
	@PersistenceContext
	protected EntityManager entityManager;

	public RegistrazioneMovFinIsCollegataAMovimentoResiduoConverter() {
		super(RegistrazioneMovFin.class, SiacTRegMovfin.class);
	}

	@Override
	public RegistrazioneMovFin convertFrom(SiacTRegMovfin src, RegistrazioneMovFin dest) {
		String methodName = "convertFrom";
		
		Query query = entityManager.createNativeQuery(getSql());
		query.setParameter("regmovfinId", src.getUid());

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
		sb.append("     SELECT                                                           ");
		sb.append("		 	distinct r.regmovfin_id, dct.collegamento_tipo_code, rerm.campo_pk_id,");
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
		sb.append("		 FROM  ");
		sb.append("		    siac_t_reg_movfin r,");
		sb.append("		 	siac_t_bil b,");
		sb.append("		 	siac_t_periodo pe,");
		sb.append("		 	siac_r_evento_reg_movfin rerm,                                 ");
		sb.append("		 	siac_d_evento de,                                              ");
		sb.append("		 	siac_d_collegamento_tipo dct                                   ");
		sb.append("		 WHERE                                                            ");
		sb.append("		 	r.data_cancellazione is null");
		sb.append("		 	and b.data_cancellazione is null");
		sb.append("		 	and pe.data_cancellazione is null");
		sb.append("		 	and rerm.data_cancellazione is null	                           ");
		sb.append("		 	and de.data_cancellazione is null                              ");
		sb.append("		 	and dct.data_cancellazione is null                             ");
		//Join
		sb.append("		 	and r.regmovfin_id = rerm.regmovfin_id");
		sb.append("		 	and r.bil_id = b.bil_id");
		sb.append("		 	and b.periodo_id = pe.periodo_id");
		sb.append("		 	and rerm.evento_id = de.evento_id                              ");
		sb.append("		 	and de.collegamento_tipo_id = dct.collegamento_tipo_id         ");
		//Base Filter
		sb.append("		 	and rerm.regmovfin_id = :regmovfinId ");
				

				
		return sb.toString();
	}

	@Override
	public SiacTRegMovfin convertTo(RegistrazioneMovFin src, SiacTRegMovfin dest) {
		return dest;
	}



	

}
