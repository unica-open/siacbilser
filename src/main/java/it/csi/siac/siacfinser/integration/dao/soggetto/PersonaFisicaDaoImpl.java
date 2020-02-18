/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaFin;

@Component
@Transactional
public class PersonaFisicaDaoImpl extends AbstractDao<SiacTPersonaFisicaFin, Integer> implements PersonaFisicaDao {
	 
}
