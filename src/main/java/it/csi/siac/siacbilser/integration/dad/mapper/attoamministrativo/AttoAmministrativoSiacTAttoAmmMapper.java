/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.attoamministrativo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siaccommonser.util.mapper.EntitaSiacTBaseMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AttoAmministrativoSiacTAttoAmmMapper extends EntitaSiacTBaseMapper<AttoAmministrativo, SiacTAttoAmm> {
}


