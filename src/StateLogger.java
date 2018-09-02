
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class StateLogger {
	private static final Logger log = Logger.getLogger("state_logger");

	private DataSource dataSource;

	private DataSource dataSourceGRAkt;

	private DataSource dataSourceMainProd;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		if (dataSource instanceof DataSourceWrapper) {
			this.dataSource = ((DataSourceWrapper) dataSource).getDataSource();
		} else {
			this.dataSource = dataSource;
		}
	}

	public DataSource getDataSourceGrAkt() {
		return dataSourceGRAkt;
	}

	public void setDataSourceGRAkt(DataSource dataSourceGRAkt) {
		if (dataSourceGRAkt instanceof DataSourceWrapper) {
			this.dataSourceGRAkt = ((DataSourceWrapper) dataSourceGRAkt).getDataSource();
		} else {
			this.dataSourceGRAkt = dataSourceGRAkt;
		}
	}

	public DataSource getDataSourceMainProd() {
		return dataSourceMainProd;
	}

	public void setDataSourceMainProd(DataSource dataSourceMainProd) {
		if (dataSourceMainProd instanceof DataSourceWrapper) {
			this.dataSourceMainProd = ((DataSourceWrapper) dataSourceMainProd).getDataSource();
		} else {
			this.dataSourceMainProd = dataSourceMainProd;
		}
	}

	public String getSystemState() {
		return ToolSO.getMemoryUsageShort() + " " + ToolSO.getConnectionsUsageShort(dataSource) + " on HD1: "
				+ ToolSO.getConnectionsUsageShort(dataSourceGRAkt) + " mainProd: " + ToolSO.getConnectionsUsageShort(dataSourceMainProd);
	}

	public void logSystemState() {
		log.info(getSystemState());
	}


}
