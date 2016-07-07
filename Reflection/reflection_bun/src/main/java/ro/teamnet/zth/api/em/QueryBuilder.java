package ro.teamnet.zth.api.em;

import ro.teamnet.zth.api.annotations.Column;

import javax.management.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Condition;

/**
 * Created by user on 7/8/2016.
 */
public class QueryBuilder {
    private Object tableName;
    private List<ColumnInfo> queryColumns;
    private QueryType queryType;
    private List<Condition> conditions;

    public String getValueForQuery(Object value){
        if(value instanceof String){
            return "'" + value.toString() + "'";
        }
        if(value instanceof Date){
            DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
            return "TO_DATE('"+dateFormat.format((Date)value)+"','mm-dd-YYYY'";
        }
        return null;
    }

    public QueryBuilder addCondition(Condition condition){
        if(conditions.isEmpty()){
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        else conditions.add(condition);
        return this;
    }

    public QueryBuilder setTableName(Object tableName){
        this.tableName = tableName;
        return this;
    }

    public QueryBuilder addQueryColumns(List<ColumnInfo> queryColumns){
        for(int i = 0; i < queryColumns.size() ; i++){
            this.queryColumns.set(i, queryColumns.get(i));
        }
        return this;
    }

    public QueryBuilder setQueryType(QueryType queryType){
        this.queryType = queryType;
        return this;
    }

    private String createSelectQuery(){
        StringBuilder sb = new StringBuilder();
            String columns = "";
            String condition = "";
            boolean isEmpty = false;
            if(!conditions.isEmpty()){
               for(int j = 0; j < conditions.size(); j ++){
                   condition = condition.toString() + ((ColumnInfo)conditions.get(j)).getColumnName().toString() + " = " + ((ColumnInfo)conditions.get(j)).getValue().toString();
                   if(j != conditions.size())
                   {
                       condition = condition.toString() + "AND";
                   }
               }
            }
            else isEmpty = true;

            if(!queryColumns.isEmpty()){
                for(int i = 0; i < queryColumns.size(); i ++)
                {
                    columns = columns.toString() + ((ColumnInfo)queryColumns.get(i)).getDbName() ;
                    if(i != queryColumns.size() - 1)
                    {
                        columns = columns.toString() + ",";
                    }
                }
                if(isEmpty == true)  sb.append("SELECT "+ columns + " FROM " + tableName.toString());
                else sb.append("SELECT "+ columns + " FROM " + tableName.toString() + " WHERE " + condition.toString());
            }
        else if(isEmpty == true){
            sb.append("SELECT * FROM " + tableName.toString());
        }
        else sb.append("SELECT * FROM " + tableName.toString() + " WHERE " + condition.toString());
        sb.append(";");
        return sb.toString();

    }

    private String createDeleteQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("TRUNCATE TABLE " + tableName.toString());
        return sb.toString();
    }

    private String createUpdateQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE " + tableName.toString() + " SET ");

        String columns = "";
        String condition = "";
        boolean isEmpty = false;
        if(!conditions.isEmpty()){
            for(int j = 0; j < conditions.size(); j ++){
                condition = condition.toString() + ((ColumnInfo)conditions.get(j)).getColumnName().toString() + " = " + ((ColumnInfo)conditions.get(j)).getValue().toString();
                if(j != conditions.size())
                {
                    condition = condition.toString() + "AND";
                }
            }
        }
        else isEmpty = true;

        if(!queryColumns.isEmpty()){
            for(int i = 0; i < queryColumns.size(); i ++)
            {
                columns = columns.toString() + ((ColumnInfo)queryColumns.get(i)).getDbName() ;
                if(i != queryColumns.size() - 1)
                {
                    columns = columns.toString() + ",";
                }
            }
            if(isEmpty == true)  sb.append(columns);
            else sb.append(columns + " WHERE " + condition.toString());
        }

        sb.append(";");
        return sb.toString();
    }

    public String createInsertQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + tableName.toString() + " VALUES (");
        boolean isFirstValue = true;
        for(ColumnInfo c: queryColumns){
            if(isFirstValue != true){
                sb.append(",");
            }
            sb.append(getValueForQuery(c.getValue()));
            isFirstValue = false;
        }
        sb.append(");");
        return sb.toString();
    }

    public String createQuery(){
        if(this.queryType.equals(QueryType.SELECT)) return createSelectQuery();
        if(this.queryType.equals(QueryType.INSERT)) return createInsertQuery();
        if(this.queryType.equals(QueryType.DELETE)) return createDeleteQuery();
        if(this.queryType.equals(QueryType.UPDATE)) return createUpdateQuery();
        return null;
    }



}
