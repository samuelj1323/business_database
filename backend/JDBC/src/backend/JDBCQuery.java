package backend;

import java.util.HashMap;

public class JDBCQuery {
    public String[] columns = null;
    public String[] tables = null;
    public int[] matchCols = null;
    public String[] relation = null;
    public String[] matchTo = null;
    public int orderCol = -1;
    public String order = null;
    public int limit = -1;

	private HashMap<String, String[]> keys;

    public JDBCQuery() {
        keys = new HashMap<>();
		keys.put("locations", new String[]{"loc_id"});
		keys.put("categories", new String[]{"cat_id"});
		keys.put("users", new String[]{"usr_id"});
		keys.put("businesses", new String[]{"bus_id", "loc_id", "cat_id"});
		keys.put("elitehistory", new String[]{"hist_id", "usr_id"});
		keys.put("reviews", new String[]{"rev_id", "usr_id", "bus_id"});
		keys.put("tips", new String[]{"tip_id", "usr_id", "bus_id"});
    }

    public String toQueryString() throws InputException {
        if (columns == null || tables == null || matchCols == null || relation == null ||
            matchTo == null || orderCol == -1 || order == null || limit == -1) {
                throw new InputException("Inputs Incomplete");
        }

        if (tables.length == 0 || columns.length == 0 ||
			!(order.equalsIgnoreCase("ASC") || order.equalsIgnoreCase("DESC"))) {
			throw new InputException("Incorrect Inputs");
		}
		
		int index = 0;
		int[] indices = new int[tables.length];
		for (int i = 0; i < tables.length; i++) {
			for (int j = 0; j < keys.keySet().toArray().length; j++) {
				if (tables[i] == keys.keySet().toArray()[j]) {
					indices[index] = j;
					index++;
				}
			}
		}
		
		for (int i = 0; i < columns.length; i++) {
			for (int j = 0; j < index; j++) {
				String table = (String) keys.keySet().toArray()[indices[j]];
				for (String col : keys.get(table)) {
					if (columns[i].equals(col)) {
						columns[i] = table + "." + col;
						j = index;
						break;
					}
				}
			}
		}
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		
		for (String col : columns) {
			query.append(col.toLowerCase() + ", ");
		}
		String temp = query.toString().substring(0, query.length()-2) + " FROM " + tables[0].toLowerCase();
		query.delete(0, query.length());
		query.append(temp);

		String[] mainKeys = keys.get(tables[0].toLowerCase());
		for (int i = 1; i < tables.length; i++) {
			String table = tables[i].toLowerCase();
			String key = null;
			String[] secdKeys = keys.get(table);
			for (String mK : mainKeys) {
				for (String sK : secdKeys) {
					if (mK.equals(sK)) {
						key = mK;
						break;
					}
				}
				if (key != null) {
					break;
				}
			}
			if (key == null) {
				throw new InputException("Cannot join tables");
			}
			
			query.append(" JOIN " + table + " ON " + tables[0].toLowerCase() + "." + key + " = " + table + "." + key);
		}
		
		for (int mCol : matchCols) {
			query.append(" WHERE " + columns[mCol] + " " + relation[mCol] + " " + matchTo[mCol]);
		}
		
		String orderC = columns[orderCol];
		query.append(" ORDER BY " + orderC + " " + order.toUpperCase());
		
		if (limit > 0) {
			query.append(" LIMIT " + limit);
        }
        
        return query.toString();
    }
}
