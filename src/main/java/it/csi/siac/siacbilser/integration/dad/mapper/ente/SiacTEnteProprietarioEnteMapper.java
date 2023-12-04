/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.ente;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseEntitaMapper;
import it.csi.siac.siaccorser.model.Ente;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTEnteProprietarioEnteMapper extends SiacTBaseEntitaMapper<SiacTEnteProprietario, Ente> {

}


