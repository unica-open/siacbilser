/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;

import it.csi.siac.siacfinser.integration.entity.SiacTCartacontFin;
import it.csi.siac.siacfinser.model.carta.CartaContabile;

public class CartaContabileConverter extends FinExtendedDozerConverter<CartaContabile, SiacTCartacontFin> {

	
	public CartaContabileConverter() {
		super(CartaContabile.class, SiacTCartacontFin.class);
		
	}
	
	
	
	@Override
	public CartaContabile convertFrom(SiacTCartacontFin src, CartaContabile dest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiacTCartacontFin convertTo(CartaContabile src, SiacTCartacontFin dest) {
		// TODO Auto-generated method stub
		return null;
	}

}
