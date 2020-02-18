/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad.oil;

import java.util.ArrayList;
import java.util.List;

public class Node
{
	protected String tag;
	private String query;
	private String field;
	private String constant;
	protected List<Node> nodes = new ArrayList<Node>();
	protected boolean visible;

	public Node(String tag)
	{
		this();
		this.tag = tag;
	}

	public Node()
	{
		super();
	}

	public List<Node> getNodes()
	{
		return nodes;
	}

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public boolean isLeaf()
	{
		return nodes.isEmpty();
	}

	public void setNodes(List<Node> nodes)
	{
		this.nodes = nodes;
	}

	public String getTag()
	{
		return tag;
	}

	public void setTag(String tag)
	{
		this.tag = tag;
	}

	public String openString()
	{
		return String.format("<%s>", tag);
	}

	public String closeString()
	{
		return String.format("</%s>", tag);
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public String getConstant()
	{
		return constant;
	}

	public void setConstant(String constant)
	{
		this.constant = constant;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	@Override
	public String toString()
	{
		return tag;
	}

	public void addNode(Node node)
	{
		nodes.add(node);
	}
}