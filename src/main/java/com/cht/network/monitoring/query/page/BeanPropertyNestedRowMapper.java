package com.cht.network.monitoring.query.page;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;


public class BeanPropertyNestedRowMapper<T> implements RowMapper<T> {

    private static final Logger log = LoggerFactory.getLogger(BeanPropertyNestedRowMapper.class);


    private Class<T> mappedClass;
    private boolean checkFullyPopulated = false;
    private boolean primitivesDefaultedForNullValue = false;
    private Map<String, PropertyDescriptor> mappedFields;
    private Set<String> mappedProperties;

    public BeanPropertyNestedRowMapper() {
    }

    public BeanPropertyNestedRowMapper(Class<T> mappedClass) {
        initialize(mappedClass);
    }

    public BeanPropertyNestedRowMapper(Class<T> mappedClass, boolean checkFullyPopulated) {
        initialize(mappedClass);
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public void setMappedClass(Class<T> mappedClass) {
        if (this.mappedClass == null) {
            initialize(mappedClass);
        } else {
            if (!this.mappedClass.equals(mappedClass)) {
                throw new InvalidDataAccessApiUsageException(
                        "The mapped class can not be reassigned to map to " + mappedClass
                                + " since it is already providing mapping for " + this.mappedClass);
            }
        }
    }

    public final Class<T> getMappedClass() {
        return this.mappedClass;
    }

    public void setCheckFullyPopulated(boolean checkFullyPopulated) {
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public boolean isCheckFullyPopulated() {
        return this.checkFullyPopulated;
    }

    public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
        this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
    }

    public boolean isPrimitivesDefaultedForNullValue() {
        return this.primitivesDefaultedForNullValue;
    }

    protected void initialize(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap<String, PropertyDescriptor>();
        this.mappedProperties = new HashSet<String>();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null) {
                this.mappedFields.put(lowerCaseName(pd.getName()), pd);
                String underscoredName = underscoreName(pd.getName());
                if (!lowerCaseName(pd.getName()).equals(underscoredName)) {
                    this.mappedFields.put(underscoredName, pd);
                }
                this.mappedProperties.add(pd.getName());
            }
        }
    }

    protected String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(lowerCaseName(name.substring(0, 1)));
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = lowerCaseName(s);
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }

    protected String lowerCaseName(String name) {
        return name.toLowerCase(Locale.US);
    }

    @Override
    public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        initBeanWrapper(bw);

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<String>() : null);

        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            String field = lowerCaseName(column.replaceAll(" ", ""));
            PropertyDescriptor pd = this.mappedFields.get(field);

            if (pd != null) {
                try {
                    Object value = getColumnValue(rs, index, pd);
                    if (rowNumber == 0 && log.isDebugEnabled()) {
                        log.debug("Mapping column '" + column + "' to property '" + pd.getName()
                                + "' of type [" + ClassUtils.getQualifiedName(pd.getPropertyType())
                                + "]");
                    }
                    try {
                        bw.setPropertyValue(pd.getName(), value);
                    } catch (TypeMismatchException ex) {
                        if (value == null && this.primitivesDefaultedForNullValue) {
                            if (log.isDebugEnabled()) {
                                log.debug("Intercepted TypeMismatchException for row " + rowNumber
                                        + " and column '" + column
                                        + "' with null value when setting property '" + pd.getName()
                                        + "' of type ["
                                        + ClassUtils.getQualifiedName(pd.getPropertyType())
                                        + "] on object: " + mappedObject, ex);
                            }
                        } else {
                            throw ex;
                        }
                    }
                    if (populatedProperties != null) {
                        populatedProperties.add(pd.getName());
                    }
                } catch (NotWritablePropertyException ex) {
                    throw new DataRetrievalFailureException("Unable to map column '" + column
                            + "' to property '" + pd.getName() + "'", ex);
                }
            } else {
                // No PropertyDescriptor found, try to set nested fields instead
                try {
                    Object value = JdbcUtils.getResultSetValue(rs, index,
                            Class.forName(rsmd.getColumnClassName(index)));
                    if (rowNumber == 0 && log.isDebugEnabled()) {
                        log.debug("No property found for column '" + column + "' mapped to field '"
                                + field + "'");
                    }
                    try {
                        bw.setPropertyValue(column, value);
                    } catch (TypeMismatchException ex) {
                        if (value == null && this.primitivesDefaultedForNullValue) {
                            if (log.isDebugEnabled()) {
                                log.debug(
                                        "Intercepted TypeMismatchException for row " + rowNumber
                                                + " and column '" + column
                                                + "' with null value on object: " + mappedObject,
                                        ex);
                            }
                        } else {
                            throw ex;
                        }
                    }
                } catch (ClassNotFoundException e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Class not found for column name " + column);
                    }
                }
            }
        }

        if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
            throw new InvalidDataAccessApiUsageException(
                    "Given ResultSet does not contain all fields "
                            + "necessary to populate object of class [" + this.mappedClass.getName()
                            + "]: " + this.mappedProperties);
        }

        return mappedObject;
    }

    protected void initBeanWrapper(BeanWrapper bw) {
        //Set auto grow nested path
        bw.setAutoGrowNestedPaths(true);
    }

    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd)
            throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

    public static <T> BeanPropertyNestedRowMapper<T> newInstance(Class<T> mappedClass) {
        return new BeanPropertyNestedRowMapper<T>(mappedClass);
    }
}
