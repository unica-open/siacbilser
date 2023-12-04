/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.tefa;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.tefa.SiacTTefaTribImporti;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

@Component
@Transactional
public class TefaDaoImpl extends JpaDao<SiacTTefaTribImporti, Integer> implements TefaDao
{
}