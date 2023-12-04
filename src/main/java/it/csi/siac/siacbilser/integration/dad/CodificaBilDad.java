/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.CodificaBilDao;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Codifica;

// TODO: Auto-generated Javadoc
/**
 * The Class CodificaBilDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CodificaBilDad extends BaseDadImpl {

	/** The codifica bil dao. */
	@Autowired
	private CodificaBilDao codificaBilDao;
		
	/**
	 * Find codifiche by tipo elem bilancio.
	 *
	 * @param anno the anno
	 * @param idEnteProprietario the id ente proprietario
	 * @param tipoElementoBilancio the tipo elemento bilancio
	 * @return the list
	 */
	public List<Codifica> findCodificheByTipoElemBilancio(
			int anno, int idEnteProprietario, String tipoElementoBilancio) {
		
		List<SiacTClass> siacTClasses = codificaBilDao.findCodificheByTipoElemBilancio(anno, idEnteProprietario, tipoElementoBilancio);
		
		
		/*
		
		List<Codifica> result = new ArrayList<Codifica>();
		for (CodificaDto c : dtos) {

			TipologiaClassificatore tipo = TipologiaClassificatore.fromCodice(c.getCodice());

			switch (tipo) {
			case TIPO_FONDO:
				result.add(map(c, TipoFondo.class));
				break;

			case TIPO_FINANZIAMENTO:
				result.add(map(c, TipoFinanziamento.class));
				break;

			case CLASSIFICATORE_1:
			case CLASSIFICATORE_2:
			case CLASSIFICATORE_3:
			case CLASSIFICATORE_4:
			case CLASSIFICATORE_5:
			case CLASSIFICATORE_6:
			case CLASSIFICATORE_7:
			case CLASSIFICATORE_8:
			case CLASSIFICATORE_9:
			case CLASSIFICATORE_10:
				if (!map.containsKey(tipo))
					map.put(tipo, new ArrayList<ClassificatoreGenerico>());
				map.get(tipo).add(dozerBeanMapper.map(c, ClassificatoreGenerico.class));
				break;
			default:
				break;
			}
		}*/
		
		
		return convertiLista(siacTClasses, Codifica.class, BilMapId.SiacTClass_Codifica);
	}

	/**
	 * Find codifiche con livello by tipo elem bilancio.
	 *
	 * @param anno the anno
	 * @param idEnteProprietario the id ente proprietario
	 * @param tipoElementoBilancio the tipo elemento bilancio
	 * @return the list
	 */
	public List<Codifica> findCodificheConLivelloByTipoElemBilancio(
			int anno, int idEnteProprietario, String tipoElementoBilancio) {
		
		List<SiacTClass> siacTClasses = codificaBilDao.findCodificheConLivelloByTipoElemBilancio(anno, idEnteProprietario, tipoElementoBilancio);
		return convertiLista(siacTClasses, Codifica.class, BilMapId.SiacTClass_Codifica);
	}

	/**
	 * Codifica bil dadfind tree piano dei conti dto.
	 *
	 * @param anno the anno
	 * @param idEnteProprietario the id ente proprietario
	 * @param famigliaTreeCodice the famiglia tree codice
	 * @param idCodificaPadre the id codifica padre
	 * @return the list
	 */
	public List<ElementoPianoDeiConti> codificaBilDadfindTreePianoDeiContiDto(
			int anno, int idEnteProprietario, String famigliaTreeCodice, int idCodificaPadre) {
		
		List<SiacTClass> siacTClasses = codificaBilDao.findTreeByCodiceFamiglia(anno, idEnteProprietario,	famigliaTreeCodice,	idCodificaPadre, false);
		return convertiLista(siacTClasses, ElementoPianoDeiConti.class, BilMapId.SiacTClass_ElementoPianoDeiConti); /* da verificare : DmB.CodificaDto_ElementiPianoDeiConti*/
	}

}
