/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.model.MovimentoGestione;

@Component("SiacTMovgestMovimentoGestioneMapper")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMovgestMovimentoGestioneMapper extends BaseSiacTMovgestMovimentoGestioneMapper<MovimentoGestione> {
}
