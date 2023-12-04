/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dao.MovimentoGestioneDao;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsTipoEnum;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

/**
 * The Class MovimentoGestioneCalcolaDisponibilitaBaseConverter.
 */
public abstract class MovimentoGestioneCalcolaDisponibilitaBaseConverter<A, B> extends DozerConverter<A, B> {

	@Autowired
	private MovimentoGestioneDao movimentoGestioneDao;
	protected final LogSrvUtil log = new LogSrvUtil(getClass());
	
	/**
	 * Costruttore
	 * @param prototypeA
	 * @param prototypeB
	 */
	public MovimentoGestioneCalcolaDisponibilitaBaseConverter(Class<A> prototypeA, Class<B> prototypeB) {
		super(prototypeA, prototypeB);
	}

	@Override
	public B convertTo(A src, B dest) {
		return dest;
	}
	
	protected BigDecimal calcolaDisponibilita(Integer uid, String functionName) {
		BigDecimal disponibilita = movimentoGestioneDao.calcolaDisponibilita(uid, functionName);
		return disponibilita;
	}
	
	protected SiacTMovgestT ottieniTestata(SiacTMovgest src) {
		final String methodName = "ottieniTestata";
		for(SiacTMovgestT tmt : src.getSiacTMovgestTs()) {
			if(tmt != null && tmt.getSiacDMovgestTsTipo() != null && SiacDMovgestTsTipoEnum.Testata.getCodice().equals(tmt.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) {
				return tmt;
			}
		}
		log.warn(methodName, "Testata non trovata su SiacTMovgestTs");
		throw new IllegalStateException("Testata non trovata su SiacTMovgestTs per SiacTMovgest [uid: " + src.getUid() + "]");
	}
}
