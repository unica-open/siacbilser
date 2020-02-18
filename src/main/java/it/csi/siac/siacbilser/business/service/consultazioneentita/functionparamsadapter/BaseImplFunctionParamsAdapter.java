/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita.functionparamsadapter;

import java.sql.Types;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.integration.utility.function.jdbc.SQLParam;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaEntitaConsultabile;
import it.csi.siac.siaccorser.model.Entita;

/**
 * Base impl per i {@link FunctionParamsAdapter}.
 * 
 * @author Domenico
 *
 * @param <P>
 */
public abstract class BaseImplFunctionParamsAdapter<P extends ParametriRicercaEntitaConsultabile> implements FunctionParamsAdapter<P> {

	public BaseImplFunctionParamsAdapter() {
		super();
	}
	
	
	protected Object[] toArray(Object...objects){
		return objects;
	}
	

	protected Object varchar(String s) {
		return StringUtils.isNotBlank(s) ? s.trim() : new SQLParam(null, Types.VARCHAR);
	}
	
	protected Object varchar(Integer i) {
		return i != null ? i.toString() : new SQLParam(null, Types.VARCHAR);
	}

	protected Object integer(Entita e) {
		return e != null && e.getUid() != 0 ? Integer.valueOf(e.getUid()) : new SQLParam(null, Types.INTEGER);
	}
	
	protected Object integer(String s) {
		return s != null ? Integer.valueOf(s) : new SQLParam(null, Types.INTEGER);
	}
	
	protected Object integer(Integer i) {
		return i != null ? i : new SQLParam(null, Types.INTEGER);
	}
	
	protected Object integer(int i) {
		return i;
	}
	

}