/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacDBilElemCategoriaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoIvaStatoConverter.
 */
@Component
public class BilElemCategoriaConverter extends DozerConverter<CategoriaCapitolo, SiacTBilElem > {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	@Autowired
	private SiacDBilElemCategoriaRepository siacDBilElemCategoriaRepository;

	/**
	 * Instantiates a new subdocumento iva stato converter.
	 */
	public BilElemCategoriaConverter() {
		super(CategoriaCapitolo.class, SiacTBilElem.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CategoriaCapitolo convertFrom(SiacTBilElem src, CategoriaCapitolo dest) {
		for (SiacRBilElemCategoria r : src.getSiacRBilElemCategorias()) {
			if(r.getDataCancellazione()==null){
				CategoriaCapitolo cc = new CategoriaCapitolo();
				cc.setUid(r.getSiacDBilElemCategoria().getUid());
				cc.setCodice(r.getSiacDBilElemCategoria().getElemCatCode());
				cc.setDescrizione(r.getSiacDBilElemCategoria().getElemCatDesc());
				return cc;
			//	return CategoriaCapitolo.byCodice(r.getSiacDBilElemCategoria().getElemCatCode()).getCategoriaCapitolo();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTBilElem convertTo(CategoriaCapitolo src, SiacTBilElem dest) {
		final String methodName = "convertTo";
		
		if(dest == null || src == null) {
			return dest;
		}
		
		List<SiacRBilElemCategoria> siacRBilElemCategorias = new ArrayList<SiacRBilElemCategoria>();
		SiacRBilElemCategoria siacRBilElemCategoria = new SiacRBilElemCategoria();
		 
	//	SiacDBilElemCategoriaEnum categoria =  SiacDBilElemCategoriaEnum.byCategoriaCapitolo(src);
		SiacDBilElemCategoria siacDBilElemCategoria = siacDBilElemCategoriaRepository.findOne(src.getUid());
		//eef.getEntity(categoria, dest.getSiacTEnteProprietario().getUid(), SiacDBilElemCategoria.class); 
				
		log.debug(methodName, "setting SiacDBilElemCategoria to: "+siacDBilElemCategoria.getElemCatCode()+ " ["+siacDBilElemCategoria.getUid()+"]");
		siacRBilElemCategoria.setSiacDBilElemCategoria(siacDBilElemCategoria);
		siacRBilElemCategoria.setSiacTBilElem(dest);		
		
		siacRBilElemCategoria.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRBilElemCategoria.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRBilElemCategorias.add(siacRBilElemCategoria);
		dest.setSiacRBilElemCategorias(siacRBilElemCategorias);
		
		return dest;
	}



	

}
