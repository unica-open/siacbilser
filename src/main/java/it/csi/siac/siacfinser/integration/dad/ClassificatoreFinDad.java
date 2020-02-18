/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siacfinser.integration.dao.classificatorefin.SiacTClassFinDao;
import it.csi.siac.siacfinser.integration.entity.SiacCodifica;
import it.csi.siac.siacfinser.model.ElementoContoEconomico;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ClassificatoreFinDad extends BaseDadImpl {

	@Autowired
	private SiacTClassFinDao siacTClassDao;

	public List<ClassificatoreGenerico> findClassificatoriGenericiByTipoMovimentoGestione(
			int anno, int idEnteProprietario, String codiceTipoMovimentoGestione) {
		List<SiacCodifica> dtos = siacTClassDao.findClassificatoriGenericiByTipoMovimentoGestione(anno, idEnteProprietario, codiceTipoMovimentoGestione);
		return convertiLista(dtos, ClassificatoreGenerico.class);
	}

	public List<ClassificatoreGenerico> findClassificatoriGenericiByTipoOrdinativoGestione(int anno, int idEnteProprietario, String codiceTipoOrdinativoGestione) {
		List<SiacCodifica> dtos = siacTClassDao.findClassificatoriGenericiByTipoOrdinativoGestione(anno, idEnteProprietario, codiceTipoOrdinativoGestione);
		return convertiLista(dtos, ClassificatoreGenerico.class);
	}

	public List<ClassificatoreGerarchico> findClassificatoriGerarchiciILivelloByTipoMovimentoGestione(
			int anno, int idEnteProprietario, String codiceTipoMovimentoGestione) {
		List<SiacCodifica> dtos = siacTClassDao.findClassificatoriGerarchiciILivelloByTipoMovimentoGestione(anno, idEnteProprietario, codiceTipoMovimentoGestione);
		return convertiLista(dtos, ClassificatoreGerarchico.class);
	}

	public List<ClassificatoreGerarchico> findClassificatoriGerarchiciByIdPadre(
			int anno, int idEnteProprietario, int idPadre) {
		List<SiacCodifica> dtos = siacTClassDao.findClassificatoriGerarchiciByIdPadre(anno, idEnteProprietario, idPadre);
		return convertiLista(dtos, ClassificatoreGerarchico.class);
	}

	public List<ElementoContoEconomico> findPianoContoEconomico(
			Integer anno, Integer idEnteProprietario, String codiceFamigliaTree, Integer idPadre) {
		List<SiacCodifica> dtos = siacTClassDao.findTreeClassificatoriGerarchiciByFamigliaId(anno, idEnteProprietario,codiceFamigliaTree, idPadre);
		List<ElementoContoEconomico> tree = new ArrayList<ElementoContoEconomico>();
		return tree = convertTreeDto(dtos,tree, new ElementoContoEconomico());
	}

	@Transactional(propagation=Propagation.MANDATORY)
	public  List<ElementoContoEconomico> convertTreeDto(List<SiacCodifica> dtos,
			List<ElementoContoEconomico> list, ElementoContoEconomico padre) {

		for (SiacCodifica dto : dtos) {

			ElementoContoEconomico obj = new ElementoContoEconomico();

			obj.setUid(dto.getUid());
			obj.setCodice(dto.getCodice());
			obj.setDescrizione(dto.getDescrizione());

			if (!dto.getFigli().isEmpty()) {
				padre = obj;

				List<SiacCodifica> figli = new ArrayList<SiacCodifica>(dto.getFigli());
				List<ElementoContoEconomico> elemPce = new ArrayList<ElementoContoEconomico>();
				elemPce = convertTreeDto(figli, elemPce, padre);
				padre.setElemPdCEconomico(elemPce);
				list.add(padre);

			} else {
				list.add(obj);
			}

		}
		//Termino restituendo l'oggetto di ritorno: 
        return list;
	}
}
