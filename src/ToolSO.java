import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.RhinoException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ToolSO {

	private static final Logger log = Logger.getLogger(ToolSO.class);

	public static Date getDate() {
		return new Date();
	}

    public static String getMemeoryUsage() {
        StringBuffer buf = new StringBuffer(256);
        final String info = "MB\n";
        Map<String, Float> memUsage = getMemoryUsageMap();
        buf.append("current size of heap: ").append(memUsage.get("total")).append(info);
        buf.append("free memory within the heap: ").append(memUsage.get("free")).append(info);
        buf.append("used memory within the heap: ").append(memUsage.get("usedMem")).append(info);
        buf.append("maximum size of heap: ").append(memUsage.get("max")).append(info);
        return buf.toString();
    }
    
    public static String getMemoryUsageShort() {
    	StringBuilder sb = new StringBuilder();
    	Map<String, Float> memUsage = getMemoryUsageMap();
        sb.append("MEM=[total=").append(memUsage.get("total"));
        sb.append(", free=").append(memUsage.get("free"));
        sb.append(", usedMem=").append(memUsage.get("usedMem"));
        sb.append(", max=").append(memUsage.get("max")).append("]");
    	return sb.toString();
    }
    
    public static Map<String, Float> getMemoryUsageMap() {
    	final Runtime runtime = Runtime.getRuntime();
        final double oneMB = 1024 * 1024;
        float total = (float) (runtime.totalMemory() / oneMB);
        float free = (float) (runtime.freeMemory() / oneMB);
        float usedMem = total - free;
        float max = (float) (runtime.maxMemory() / oneMB);
        Map<String, Float> memUsage = new HashMap<String, Float>();
        memUsage.put("total", total);
        memUsage.put("free", free);
        memUsage.put("usedMem", usedMem);
        memUsage.put("max", max);
        return memUsage;
    }
    
    public static String getConnectionsUsage(DataSource dataSource) {
    	StringBuilder sb = new StringBuilder();
    	try {
	    	if (dataSource instanceof ComboPooledDataSource) {
	    		ComboPooledDataSource cpds = (ComboPooledDataSource) dataSource;
	    		sb.append(getInfoFromComboPolledDataSource(cpds));
	    	} else if (dataSource instanceof DataSourceWrapper) {
	    		DataSourceWrapper dsw = (DataSourceWrapper) dataSource;
	    		if (null != dsw.getDataSource() && dsw.getDataSource() instanceof ComboPooledDataSource) {
	    			ComboPooledDataSource cpds = (ComboPooledDataSource) dsw.getDataSource();
	    			sb.append(getInfoFromComboPolledDataSource(cpds));
	    		} else {
	    			sb.append("unsupported data source type in DataSourceWrapper");
	    		}
	    	} else {
	    		sb.append("unsupported data source type");
	    	}
    	} catch (Exception e) {
    		sb.append("exception occurs: ").append(Arrays.toString(e.getStackTrace()));
    	}
    	return sb.toString();
    }
    
    private static String getInfoFromComboPolledDataSource(ComboPooledDataSource cpds) {
    	StringBuilder sb = new StringBuilder();
    	try {
	    	sb.append("thread pool size: ").append(cpds.getThreadPoolSize()).append('\n');
			sb.append("connections: ").append(cpds.getNumConnections()).append('\n');
			sb.append("busy connections: ").append(cpds.getNumBusyConnections()).append('\n');
			sb.append("idle connections: ").append(cpds.getNumIdleConnections()).append('\n');
			sb.append("unclosed orhpaned connections: ").append(cpds.getNumUnclosedOrphanedConnections()).append('\n');
			sb.append("max pool size: ").append(cpds.getMaxPoolSize()).append('\n');
    	} catch (Exception e) {
    		sb.append("exception occurs: ").append(Arrays.toString(e.getStackTrace()));
    	}
    	return sb.toString();
    }
    
    public static String getConnectionsUsageShort(DataSource dataSource) {
    	StringBuilder sb = new StringBuilder();
    	try {
    		sb.append("DB=[");
    		if (dataSource instanceof ComboPooledDataSource) {
    			ComboPooledDataSource cpds = (ComboPooledDataSource) dataSource;
    			sb.append("conn=").append(cpds.getNumConnections());
    			sb.append(", busy=").append(cpds.getNumBusyConnections());
    			sb.append(", idle=").append(cpds.getNumIdleConnections());
    			sb.append(", max=").append(cpds.getMaxPoolSize());
    			sb.append(", orhpaned=").append(cpds.getNumUnclosedOrphanedConnections());
    		} else {
	    		sb.append("unsupported");
	    	}
    		sb.append("]");
    	} catch (Exception e) {
	    	sb.append("exception occurs: ").append(Arrays.toString(e.getStackTrace()));
	    }
    	return sb.toString();
    }
}
