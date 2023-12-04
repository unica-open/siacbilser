/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;

public class CollegaQuotaDocumentoARigaCartaInfoDto extends EsitoControlliDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Carta e riga:
	private CartaContabile cartaContabile;
	private PreDocumentoCarta preDocumentoCarta;
	
	//Documento e sub documento:
	private DocumentoSpesa documentoSpesa;
	private SubdocumentoSpesa subdocumentoSpesa;
	
	public CartaContabile getCartaContabile() {
		return cartaContabile;
	}
	public void setCartaContabile(CartaContabile cartaContabile) {
		this.cartaContabile = cartaContabile;
	}
	public PreDocumentoCarta getPreDocumentoCarta() {
		return preDocumentoCarta;
	}
	public void setPreDocumentoCarta(PreDocumentoCarta preDocumentoCarta) {
		this.preDocumentoCarta = preDocumentoCarta;
	}
	public DocumentoSpesa getDocumentoSpesa() {
		return documentoSpesa;
	}
	public void setDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		this.documentoSpesa = documentoSpesa;
	}
	public SubdocumentoSpesa getSubdocumentoSpesa() {
		return subdocumentoSpesa;
	}
	public void setSubdocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		this.subdocumentoSpesa = subdocumentoSpesa;
	}
	
}
