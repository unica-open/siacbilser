/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommonser.util.mapper.EntitaSiacTBaseMapper;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class MutuoSiacTMutuoBaseMapper extends EntitaSiacTBaseMapper<Mutuo, SiacTMutuo> {

}
