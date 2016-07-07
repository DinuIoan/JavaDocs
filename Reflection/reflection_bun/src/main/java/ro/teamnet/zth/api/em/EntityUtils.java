package ro.teamnet.zth.api.em;

import ro.teamnet.zth.api.annotations.Column;
import ro.teamnet.zth.api.annotations.Id;
import ro.teamnet.zth.api.annotations.Table;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by user on 7/7/2016.
 */
public class EntityUtils {

    private EntityUtils() throws UnsupportedOperationException
    {

    }

    public static String getTableName(Class entity){
        return entity.getAnnotation(Table.class).toString();
    }

    public static List<ColumnInfo> getColumns(Class entity){
        List<ColumnInfo> columnInfo =new ArrayList<ColumnInfo>();
        for(Field field: entity.getFields()){
            ColumnInfo columnInfo1 = new ColumnInfo();
            columnInfo1.setColumnName(field.getName());
            columnInfo1.setColumnType(field.getType());
            Column c = field.getAnnotation(Column.class);
            Id id = field.getAnnotation(Id.class);
            if(c == null){
                columnInfo1.setDbName(id.name());
                columnInfo1.setId(true);
            }
            else {
                columnInfo1.setDbName(c.name());
                columnInfo1.setId(false);;
            }


            columnInfo.add(columnInfo1);
        }
        return columnInfo;
    }

    public static Object castFromSqlType(Object value, Class wantedType){
        if(value instanceof BigDecimal && wantedType.getClass().equals(Integer.class)) {
           return ((BigDecimal) value).intValue();
        }
        if(value instanceof BigDecimal && wantedType.getClass().equals(Long.class)){
            return ((BigDecimal) value).longValue();
        }
        if(value instanceof BigDecimal && wantedType.getClass().equals(Float.class)){
            return ((BigDecimal) value).floatValue();
        }
        if(value instanceof BigDecimal && wantedType.getClass().equals(Double.class)){
            return ((BigDecimal) value).doubleValue();
        }
        if(!(value instanceof BigDecimal))
            return value;

    return null;

    }

    public static List<Field> getFieldsByAnnotations(Class clazz, Class annotation)
    {
        List<Field> list = new ArrayList<Field>();
        for(Field field: clazz.getFields()){
            if(field.getAnnotations().equals("annotation"))
            {
                list.add(field);
            }
        }
        return list;
    }

    public static Object getSqlValue(Object obj) throws IllegalAccessException {
        if(obj.getClass().getAnnotation(Table.class) != null){
            Field ifField = getFieldsByAnnotations(obj.getClass(),Id.class).get(0);
            ifField.setAccessible(true);
            return ifField.get(obj);
        }
        return obj;
    }
}
