/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacboser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacboser.integration.dao.limiteimpegnabile.LimiteImpegnabileDao;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.messaggio.MessaggioCore;
import it.csi.siac.siacintegser.business.service.limiteimpegnabile.model.CapitoloLimiteImpegnabile;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class LimiteImpegnabileDad extends BaseDadImpl
{
	private List<Messaggio> messaggi = new ArrayList<Messaggio>();

	@Autowired
	private LimiteImpegnabileDao limiteImpegnabileDao;

	private String[] ANNI_IMP = new String[] { "", "1", "2" };

	public List<Messaggio> aggiornaCapitoloLimiteImpegnabile(CapitoloLimiteImpegnabile capitoloLimiteImpegnabile,
			Integer idEnte, String loginOperazione) throws Exception
	{
		Integer idCapitolo = limiteImpegnabileDao.leggiCapitolo(capitoloLimiteImpegnabile.getNumeroCapitolo(),
				capitoloLimiteImpegnabile.getNumeroArticolo(), capitoloLimiteImpegnabile.getNumeroUeb(),
				String.valueOf(capitoloLimiteImpegnabile.getAnno()), idEnte);

		if (idCapitolo != null)
			for (String annoNum : ANNI_IMP)
				checkEAggiornaImportoCapitolo(idCapitolo, capitoloLimiteImpegnabile, annoNum, idEnte, loginOperazione);

		return messaggi;
	}

	private void checkEAggiornaImportoCapitolo(Integer idCapitolo, CapitoloLimiteImpegnabile capitoloLimiteImpegnabile,
			String annoNum, Integer idEnte, String loginOperazione) throws Exception
	{
		String anno = getAnno(capitoloLimiteImpegnabile, annoNum);
		BigDecimal importoCapitolo = getImportoAnno(capitoloLimiteImpegnabile, annoNum);

		if (checkImportoCapitolo(importoCapitolo, idCapitolo, anno, idEnte))
			aggiornaImportoCapitolo(idCapitolo, importoCapitolo, anno, idEnte, loginOperazione);
	}

	private boolean checkImportoCapitolo(BigDecimal importoCapitolo, Integer idCapitolo, String anno, Integer idEnte)
	{
		Map<String, Object> importoCompetenzaCapitoloMap = limiteImpegnabileDao.leggiImportoCapitolo(idCapitolo, anno,
				SiacDBilElemDetTipoEnum.Stanziamento.getCodice(), idEnte);

		if (importoCompetenzaCapitoloMap == null)
		{
			messaggi.add(MessaggioCore.MESSAGGIO_DI_SISTEMA.getMessaggio(
					String.format("stanziamento di competenza dell'anno %s non trovato per il capitolo ID %d", anno, idCapitolo)));

			return false;
		}

		BigDecimal importoCompetenzaCapitolo = (BigDecimal) importoCompetenzaCapitoloMap.get("elem_det_importo");

		if (importoCapitolo.compareTo(importoCompetenzaCapitolo) <= 0)
			return true;

		messaggi.add(MessaggioCore.MESSAGGIO_DI_SISTEMA.getMessaggio(String.format(
				"il limite impegnabile dell'anno %s per il capitolo ID %s supera lo stanziamento di competenza (%s Euro)",
				anno, idCapitolo, importoCompetenzaCapitolo.toPlainString())));

		return false;
	}

	private void aggiornaImportoCapitolo(Integer idCapitolo, BigDecimal importoCapitolo, String anno, Integer idEnte,
			String loginOperazione) throws Exception
	{
		Map<String, Object> importoCapitoloMap = limiteImpegnabileDao.leggiImportoCapitolo(idCapitolo, anno,
				SiacDBilElemDetTipoEnum.MassimoImpegnabile.getCodice(), idEnte);

		Integer idImportoCapitolo = getIdImportoCapitolo(importoCapitoloMap);

		if (idImportoCapitolo != null)
			limiteImpegnabileDao.aggiornaImportoCapitolo(idImportoCapitolo, importoCapitolo, loginOperazione);
		else
			limiteImpegnabileDao.inserisciImportoCapitolo(idCapitolo, importoCapitolo, anno, idEnte, loginOperazione);
	}

	private Integer getIdImportoCapitolo(Map<String, Object> importoCapitoloMap)
	{
		if (importoCapitoloMap == null)
			return null;
		
		return (Integer) importoCapitoloMap.get("elem_det_id");
	}

	private String getAnno(CapitoloLimiteImpegnabile capitoloLimiteImpegnabile, String annoNum)
	{
		return String
				.valueOf(capitoloLimiteImpegnabile.getAnno() + ("".equals(annoNum) ? 0 : Integer.parseInt(annoNum)));
	}

	private BigDecimal getImportoAnno(CapitoloLimiteImpegnabile capitoloLimiteImpegnabile, String anno) throws Exception
	{
		return (BigDecimal) PropertyUtils.getSimpleProperty(capitoloLimiteImpegnabile, "importoAnno" + anno);
	}

}
