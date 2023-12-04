/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.limiteimpegnabile.model;

import java.math.BigDecimal;

public class CapitoloLimiteImpegnabile
{
	private Integer anno;
	private String numeroCapitolo;
	private String numeroArticolo;
	private String numeroUeb;
	private BigDecimal importoAnno;
	private BigDecimal importoAnno1;
	private BigDecimal importoAnno2;

	public BigDecimal getImportoAnno()
	{
		return importoAnno;
	}

	public void setImportoAnno(BigDecimal importoAnno)
	{
		this.importoAnno = importoAnno;
	}

	public BigDecimal getImportoAnno1()
	{
		return importoAnno1;
	}

	public void setImportoAnno1(BigDecimal importoAnno1)
	{
		this.importoAnno1 = importoAnno1;
	}

	public BigDecimal getImportoAnno2()
	{
		return importoAnno2;
	}

	public void setImportoAnno2(BigDecimal importoAnno2)
	{
		this.importoAnno2 = importoAnno2;
	}

	public Integer getAnno()
	{
		return anno;
	}

	public void setAnno(Integer anno)
	{
		this.anno = anno;
	}

	public String getNumeroCapitolo()
	{
		return numeroCapitolo;
	}

	public void setNumeroCapitolo(String numeroCapitolo)
	{
		this.numeroCapitolo = numeroCapitolo;
	}

	public String getNumeroArticolo()
	{
		return numeroArticolo;
	}

	public void setNumeroArticolo(String numeroArticolo)
	{
		this.numeroArticolo = numeroArticolo;
	}

	public String getNumeroUeb()
	{
		return numeroUeb;
	}

	public void setNumeroUeb(String numeroUeb)
	{
		this.numeroUeb = numeroUeb;
	}

	public String getCodiceCompleto()
	{
		return String.format("%s/%s/%s", getNumeroCapitolo(), getNumeroArticolo(), getNumeroUeb());
	}
}
