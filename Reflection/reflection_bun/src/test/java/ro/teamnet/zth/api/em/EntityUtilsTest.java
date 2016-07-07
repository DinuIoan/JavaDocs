package ro.teamnet.zth.api.em;

import org.junit.Test;
import ro.teamnet.zth.api.annotations.Id;
import ro.teamnet.zth.appl.domain.Department;
import ro.teamnet.zth.appl.domain.Job;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by user on 7/8/2016.
 */
public class EntityUtilsTest {
    @Test
    public void testGetTableNameMethod() {
        String tableName = EntityUtils.getTableName(Department.class);
        assertEquals("Table name should be departments!", "departments", tableName);
    }
    @Test
    public void testGetColumns(){
        List<ColumnInfo> list = EntityUtils.getColumns(Job.class);
        assertEquals("Columns should be 4",4,list.size());
    }

    @Test
    public void testCastFromSqlType(){
        BigDecimal bd = new BigDecimal(5);
        Object obj = EntityUtils.castFromSqlType(bd,Integer.class);
        assertEquals("The class should be Integer",Integer.class,obj);
    }

    @Test
    public void testGetFieldsByAnnotations(){
        int size = EntityUtils.getFieldsByAnnotations(Job.class, Id.class).size();
        assertEquals("The size should be 1",1,size);
    }

    @Test
    public void testGetSqlValue() throws IllegalAccessException {
        Object obj = new Object();
        Job job = new Job();
        job.setId(3);
        obj = EntityUtils.getSqlValue(job);
        assertEquals("The object should be 3",(long)obj,3);
    }


}
