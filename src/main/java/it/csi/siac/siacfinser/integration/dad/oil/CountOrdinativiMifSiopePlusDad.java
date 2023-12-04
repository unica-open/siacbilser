/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad.oil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfin2ser.model.TipoOrdinativo;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CountOrdinativiMifSiopePlusDad extends BaseCountOrdinativiMifDad
{
	public Map<Integer, String> countOrdinativiPerAnnoEsercizioCodiceIstat(int idElaborazione)
	{
		TipoOrdinativo codiceTipo = getCodiceTipo(idElaborazione);
		
		Map<Integer, String> numeroOrdinativiPerAnnoEsercizioCodiceIstat = new HashMap<Integer, String>();
		
		for (Integer anno : getAnniEsercizio(codiceTipo, idElaborazione)) {

			List<String> numeroOrdinativiPerCodiceIstat = new ArrayList<String>();

			for (String codiceIstat: getCodiciIstatAnnoEsercizio(codiceTipo, anno, idElaborazione)) {
				numeroOrdinativiPerCodiceIstat.add(String.format("%s=%d", codiceIstat, countOrdinativiAnnoCodiceIstat(codiceTipo, idElaborazione, anno, codiceIstat)));
			}
	
			numeroOrdinativiPerAnnoEsercizioCodiceIstat.put(anno, StringUtils.join(numeroOrdinativiPerCodiceIstat, ","));
		}
		
		return numeroOrdinativiPerAnnoEsercizioCodiceIstat;
	}

	@Deprecated
	public Map<Integer, Integer> countOrdinativiPerAnnoEsercizio(int idElaborazione)
	{
		Map<Integer, Integer> numeroOrdinativiPerAnnoEsercizio = new HashMap<Integer, Integer>();
		
		TipoOrdinativo codiceTipo = getCodiceTipo(idElaborazione);
		
		for (Integer anno : getAnniEsercizio(codiceTipo, idElaborazione)) {
			numeroOrdinativiPerAnnoEsercizio.put(anno, countOrdinativiAnno(codiceTipo, idElaborazione, anno));
		}
		
		return numeroOrdinativiPerAnnoEsercizio;
	}

	private Integer countOrdinativiAnnoCodiceIstat(TipoOrdinativo codiceTipo, int idElaborazione, Integer anno, String codiceIstat)
	{
		switch (codiceTipo)
		{
		case INCASSO:
			return ordinativoMifDao.countOrdinativiAnnoCodiceIstatEntrata(idElaborazione, anno, codiceIstat);
		case PAGAMENTO:
			return ordinativoMifDao.countOrdinativiAnnoCodiceIstatSpesa(idElaborazione, anno, codiceIstat);
		}

		throw new IllegalArgumentException("Codice tipo errato: " + codiceTipo);
	}

	@Deprecated
	private Integer countOrdinativiAnno(TipoOrdinativo codiceTipo, int idElaborazione, Integer anno)
	{
		switch (codiceTipo)
		{
		case INCASSO:
			return ordinativoMifDao.countOrdinativiAnnoEntrata(idElaborazione, anno);
		case PAGAMENTO:
			return ordinativoMifDao.countOrdinativiAnnoSpesa(idElaborazione, anno);
		}

		throw new IllegalArgumentException("Codice tipo errato: " + codiceTipo);
	}

	private List<Integer> getAnniEsercizio(TipoOrdinativo codiceTipo, int idElaborazione)
	{
		switch (codiceTipo)
		{
		case INCASSO:
			return ordinativoMifDao.getAnniEsercizioOrdinativiEntrata(idElaborazione);
		case PAGAMENTO:
			return ordinativoMifDao.getAnniEsercizioOrdinativiSpesa(idElaborazione);
		}

		throw new IllegalArgumentException("Codice tipo errato: " + codiceTipo);
	}

	private List<String> getCodiciIstatAnnoEsercizio(TipoOrdinativo codiceTipo, Integer anno, int idElaborazione)
	{
		switch (codiceTipo)
		{
		case INCASSO:
			return ordinativoMifDao.getCodiciIstatAnnoEsercizioOrdinativiEntrata(idElaborazione, anno);
		case PAGAMENTO:
			return ordinativoMifDao.getCodiciIstatAnnoEsercizioOrdinativiSpesa(idElaborazione, anno);
		}

		throw new IllegalArgumentException("Codice tipo errato: " + codiceTipo);
	}
}
