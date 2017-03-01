package org.hibernate.cfg.reveng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.cfg.reveng.dialect.MetaDataDialect;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.mapping.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableProcessor {

	private static final Logger log = LoggerFactory.getLogger(TableProcessor.class);

	public static Collection<Table> processTables(
			MetaDataDialect metaDataDialect, 
			ReverseEngineeringStrategy revengStrategy, 
			String  defaultSchema, 
			String defaultCatalog, 
			DatabaseCollector dbs, 
			SchemaSelection schemaSelection, 
			Set<Table> hasIndices, 
			ProgressListener progress) {
		Map<String,Object> tableRs = null;
		Iterator<Map<String,Object>> tableIterator = null;
		List<Map<String,Object>> tables = new ArrayList<Map<String,Object>>();
		boolean multiSchema = false; 
		// TODO: the code below detects if the reveng is multischema'ed, but not used for anything yet. should be used to remove schema/catalog info from output if only one schema/catalog used.
		
		  try {			  
		     progress.startSubTask("Finding tables in " + schemaSelection);
		     
		     tableIterator = metaDataDialect.getTables(StringHelper.replace(schemaSelection.getMatchCatalog(),".*", "%"), 
		    		                                        StringHelper.replace(schemaSelection.getMatchSchema(),".*", "%"), 
		    		                                        StringHelper.replace(schemaSelection.getMatchTable(),".*", "%"));
		     String[] lastQualifier = null;
		     String[] foundQualifier = new String[2];
		     
		     while (tableIterator.hasNext() ) {
		        tableRs = tableIterator.next();
		        String tableName = (String) tableRs.get("TABLE_NAME");
				String schemaName = (String) tableRs.get("TABLE_SCHEM");
		        String catalogName = (String) tableRs.get("TABLE_CAT");
		        
		        TableIdentifier ti = new TableIdentifier(catalogName, schemaName, tableName);		        
				if(revengStrategy.excludeTable(ti) ) {
					log.debug("Table " + ti + " excluded by strategy");
		        	continue;
		        }
				
				if(!multiSchema) {
					foundQualifier[0] = catalogName;
					foundQualifier[1] = schemaName;
					if(lastQualifier==null) {
						lastQualifier=new String[2];
						lastQualifier[0] = foundQualifier[0];
						lastQualifier[1] = foundQualifier[1];					
					}
					if((!safeEquals(lastQualifier[0],foundQualifier[0])) || (!safeEquals(lastQualifier[1],foundQualifier[1]))) {
						multiSchema = true;
					}
				}
				
				tables.add(new HashMap<String,Object>(tableRs));
		     }
		  } 
		  finally {
			  try {
				  if (tableIterator!=null) metaDataDialect.close(tableIterator);
			  } 
			  catch (Exception ignore) {
			  }
		  }
		  
		  List<Table> processedTables = new ArrayList<Table>();
		  tableIterator = tables.iterator();
		  while (tableIterator.hasNext() ) {
			  tableRs = tableIterator.next();
			  String tableName = (String) tableRs.get("TABLE_NAME");
			  String schemaName = (String) tableRs.get("TABLE_SCHEM");
			  String catalogName = (String) tableRs.get("TABLE_CAT");
			  
			  TableIdentifier ti = new TableIdentifier(catalogName, schemaName, tableName);
			   if(revengStrategy.excludeTable(ti) ) {
			   log.debug("Table " + ti + " excluded by strategy");
			   continue;
			   }
			  
			  String comment = (String) tableRs.get("REMARKS");
			  String tableType = (String) tableRs.get("TABLE_TYPE");
			  
			  if(dbs.getTable(schemaName, catalogName, tableName)!=null) {
				  log.debug("Ignoring " + tableName + " since it has already been processed");
				  continue;
			  } else {
				  if ( ("TABLE".equalsIgnoreCase(tableType) || "VIEW".equalsIgnoreCase(tableType) || "SYNONYM".equals(tableType) ) ) { //||
					  // ("SYNONYM".equals(tableType) && isOracle() ) ) { // only on oracle ? TODO: HBX-218
					  // it's a regular table or a synonym
					  
					  // ensure schema and catalogname is truly empty (especially mysql returns null schema, "" catalog)
					  if(schemaName!=null && schemaName.trim().length()==0) {
						  schemaName = null;
					  }                     
					  if(catalogName!=null && catalogName.trim().length()==0) {
						  catalogName=null;
					  }
					  log.debug("Adding table " + tableName + " of type " + tableType);
					  progress.startSubTask("Found " + tableName);
					  Table table = dbs.addTable(getSchemaForModel(schemaName, defaultSchema), getCatalogForModel(catalogName, defaultCatalog), tableName);
					  table.setComment(comment);
					  if(tableType.equalsIgnoreCase("TABLE")) {
						  hasIndices.add(table);
					  }
					  processedTables.add( table );
				  }
				  else {
					  log.debug("Ignoring table " + tableName + " of type " + tableType);
				  }
			  }
		  }
		  
		  return processedTables;
	}
	
	private static boolean safeEquals(Object value, Object tf) {
		if(value==tf) return true;
		if(value==null) return false;
		return value.equals(tf);
	}

	/** If catalog is equal to defaultCatalog then we return null so it will be null in the generated code. */
	private static String getCatalogForModel(String catalog, String defaultCatalog) {
		if(catalog==null) return null;
		if(catalog.equals(defaultCatalog)) return null;
		return catalog;
	}

	/** If catalog is equal to defaultSchema then we return null so it will be null in the generated code. */
	private static String getSchemaForModel(String schema, String defaultSchema) {
		if(schema==null) return null;
		if(schema.equals(defaultSchema)) return null;
		return schema;
	}
	
}
