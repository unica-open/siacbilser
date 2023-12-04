/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.capitolo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTBilElemCapitoloEntrataGestioneMapper extends SiacTBilElemCapitoloMapper<CapitoloEntrataGestione> {
}


