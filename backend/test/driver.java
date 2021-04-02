import backend.JDBCBackend;
import backend.JDBCQuery;

public class Driver {
	public static void main(String[] args) throws Exception {
		JDBCBackend back = new JDBCBackend();
		JDBCQuery query = new JDBCQuery();
		query.columns = new String[]{"bus_name"};
		query.tables = new String[]{"businesses"};
		query.matchCols = new int[]{};
		query.relation = new String[]{};
		query.matchTo = new String[]{};
		query.orderCol = 0;
		query.order = "asc";
		query.limit = 100;

		back.executeQuery(query);
		System.out.println(back.getOutput("./data/data.csv"));
	}
}
