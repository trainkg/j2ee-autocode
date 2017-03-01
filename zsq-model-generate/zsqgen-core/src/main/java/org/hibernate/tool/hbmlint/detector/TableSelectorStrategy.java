/**
 * 
 */
package org.hibernate.tool.hbmlint.detector;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.SchemaSelection;

public class TableSelectorStrategy extends DelegatingReverseEngineeringStrategy {
	
	List<SchemaSelection> selections = new ArrayList<SchemaSelection>();
	
	public TableSelectorStrategy(ReverseEngineeringStrategy res) {
		super(res);
	}
	
	public List<SchemaSelection> getSchemaSelections() {
		return selections;
	}
	

	public void clearSchemaSelections() {
		selections.clear();
	}
	
	public void addSchemaSelection(SchemaSelection selection) {
		selections.add(selection);
	}	
}