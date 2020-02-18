/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * 
 */
package it.csi.siac.siacbilser.integration.dad;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacDBilElemCategoriaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;

/**
 * @author paggio
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class CategoriaCapitoloDad extends BaseDadImpl {
	
	/** The siac d class repository. */
	@Autowired
	private SiacDBilElemCategoriaRepository siacDBilElemCategoriaRepository;
	
	@Autowired
	private EnumEntityFactory enumEntityFactory;
	

	/**
	 * Effettua la ricerca delle categorie capitolo per un Ente.
	 *
	 * @param ente the ente
	 * @param tipoCapitolo the tipo capitolo
	 * @return the list
	 */
	public List<CategoriaCapitolo> ricercaCategoriaCapitolo(Ente ente, TipoCapitolo tipoCapitolo) {
		// TODO
		SiacDBilElemTipoEnum siacDBilElemTipoEnum = SiacDBilElemTipoEnum.byTipoCapitolo(tipoCapitolo);
		SiacDBilElemTipo siacDBilElemTipo = enumEntityFactory.getEntity(siacDBilElemTipoEnum, ente.getUid(), SiacDBilElemTipo.class);
		
		List<SiacDBilElemCategoria> elencoCategoriaCapitoloDB = siacDBilElemCategoriaRepository.findCategoriaCapitoloByEnte(ente.getUid(), siacDBilElemTipo.getUid());
		if(elencoCategoriaCapitoloDB == null) {
			return new ArrayList<CategoriaCapitolo>();
		}
		
		List<CategoriaCapitolo> elencoCategoriaCapitoloReturn = new ArrayList<CategoriaCapitolo>(elencoCategoriaCapitoloDB.size());
		
		for (SiacDBilElemCategoria codCatCapitolo : elencoCategoriaCapitoloDB) {
			
			CategoriaCapitolo categoriaCapitoloAdd = mapCategoriaCapitolo(codCatCapitolo);
			elencoCategoriaCapitoloReturn.add(categoriaCapitoloAdd);
		}
		return elencoCategoriaCapitoloReturn;
	}


	/**
	 * Map categoria capitolo
	 *
	 * @param catCapitoloDB the categoria capitolo from db
	 * @return the categoria capitolo
	 */
	private CategoriaCapitolo mapCategoriaCapitolo(SiacDBilElemCategoria catCapitoloDB) {
		CategoriaCapitolo catCapitoloToAdd = new CategoriaCapitolo();
		catCapitoloToAdd.setUid(catCapitoloDB.getUid());
		catCapitoloToAdd.setCodice(catCapitoloDB.getElemCatCode());
		catCapitoloToAdd.setDescrizione(catCapitoloDB.getElemCatDesc());
		return catCapitoloToAdd;
	}

}
