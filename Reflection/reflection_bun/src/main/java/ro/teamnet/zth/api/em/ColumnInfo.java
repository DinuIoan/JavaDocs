package ro.teamnet.zth.api.em;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;

/**
 * Created by user on 7/7/2016.
 */
public class ColumnInfo {
    private String columnName;
    private Class columnType;
    private String dbName;
    private boolean isId;
    private Object value;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Class getColumnType() {
        return columnType;
    }

    public void setColumnType(Class columnType) {
        this.columnType = columnType;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public boolean isId() {
        return isId;
    }

    public void setId(boolean id) {
        isId = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
