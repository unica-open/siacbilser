/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita;

import java.sql.Types;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.integration.utility.function.jdbc.SQLParam;
import it.csi.siac.siaccorser.model.Entita;

/**
 * Tipo dei parametri per le funzionalit&agrave; delle entit&agave; consultabili
 * @author Marchino Alessandro
 *
 */
public enum FunctionEntitaConsultabileParamType {
	
	VARCHAR {
		public SQLParam convert(Object value) {
			if(value instanceof String && StringUtils.isNotBlank((String) value)) {
				return new SQLParam(((String)value).trim(), Types.VARCHAR);
			}
			if(value instanceof Number) {
				return new SQLParam(value.toString(), Types.VARCHAR);
			}
			return new SQLParam(null, Types.VARCHAR);
		}
		@Override
		public Object getDefaultValue() {
			return "";
		}
	},
	INTEGER {
		@Override
		public SQLParam convert(Object value) {
			if(value instanceof Entita && ((Entita)value).getUid() != 0) {
				return new SQLParam(Integer.valueOf(((Entita)value).getUid()), Types.INTEGER);
			}
			if(value instanceof String) {
				return new SQLParam(Integer.valueOf((String) value), Types.INTEGER);
			}
			if(value instanceof Number) {
				return new SQLParam(Integer.valueOf(((Number)value).intValue()), Types.INTEGER);
			}
			return new SQLParam(null, Types.INTEGER);
		}
	},
	;
	
	/**
	 * Converts a value to che correspongind sqlParam
	 * @param value the value to convert
	 * @return the corresponding sql param
	 */
	public abstract SQLParam convert(Object value);
	
	/**
	 * Returns the default value for the type
	 * @return the default value
	 */
	public Object getDefaultValue() {
		// Base implementation
		return null;
	}
}
