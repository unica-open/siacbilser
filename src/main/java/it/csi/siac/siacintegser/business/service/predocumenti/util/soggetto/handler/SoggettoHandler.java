/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto.handler;

import javax.persistence.EntityManager;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacintegser.business.service.predocumenti.model.Predocumento;
import it.csi.siac.siacintegser.business.service.predocumenti.util.SoggettoPredocumentoException;

public interface SoggettoHandler<P extends Predocumento>
{
	public void init(Ente ente, Richiedente richiedente, EntityManager entityManager);

	public void initInfoPersona(P predocumento) throws SoggettoPredocumentoException;

	public Soggetto cercaSoggetto(P predocumento) throws SoggettoPredocumentoException;

	public Soggetto inserisciSoggetto(P predocumento) throws SoggettoPredocumentoException;

}
