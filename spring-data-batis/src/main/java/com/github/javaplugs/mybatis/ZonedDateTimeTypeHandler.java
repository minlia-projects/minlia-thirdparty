/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Vladislav Zablotsky
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. 
 */
package com.github.javaplugs.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Map Java 8 Instant &lt;-&gt; java.sql.Timestamp with timezone.
 */
@MappedTypes(ZonedDateTime.class)
public class ZonedDateTimeTypeHandler extends BaseTypeHandler<ZonedDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ZonedDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(
            i,
            Timestamp.from(parameter.toInstant()),
            GregorianCalendar.from(parameter)
        );
    }

    @Override
    public ZonedDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp ts = rs.getTimestamp(columnName, Calendar.getInstance());
        if (ts != null) {
            return ZonedDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
        }
        return null;
    }

    @Override
    public ZonedDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp ts = rs.getTimestamp(columnIndex, Calendar.getInstance());
        if (ts != null) {
            return ZonedDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
        }
        return null;
    }

    @Override
    public ZonedDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp ts = cs.getTimestamp(columnIndex, Calendar.getInstance());
        if (ts != null) {
            return ZonedDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
        }
        return null;
    }
}
