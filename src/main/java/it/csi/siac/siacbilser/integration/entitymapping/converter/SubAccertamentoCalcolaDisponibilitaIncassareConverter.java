/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * The Class AccertamentoCalcolaDisponibilitaIncassareConverter.
 */
@Component
public class SubAccertamentoCalcolaDisponibilitaIncassareConverter extends MovimentoGestioneCalcolaDisponibilitaBaseConverter<Accertamento, SiacTMovgestT> {
	
	private static final String FUNCTION_NAME = "fnc_siac_disponibilitaincassaremovgest";

	public SubAccertamentoCalcolaDisponibilitaIncassareConverter() {
		super(Accertamento.class, SiacTMovgestT.class);
	}
	
	@Override
	public Accertamento convertFrom(SiacTMovgestT src, Accertamento dest) {
		calcolaDisponibilita(dest, src.getUid());
		return dest;
	}

	protected void calcolaDisponibilita(Accertamento dest, Integer uid) {
		BigDecimal disponibilitaIncassare = calcolaDisponibilita(uid, FUNCTION_NAME);
		dest.setDisponibilitaIncassare(disponibilitaIncassare);
		dest.setMotivazioneDisponibilitaIncassare("Disponibilita calcolata dalla function");
	}
	
}
