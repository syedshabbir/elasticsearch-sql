package com.elastic.sql.repo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elastic.sql.model.JdbcData;

@Repository
public class EsRepo
{

    @Autowired private DataSource dataSource;

    public List<JdbcData> getEntries(String search) throws SQLException
    {
        final List<JdbcData> jdbcData = new ArrayList<JdbcData>();

        final String query = "select id,name,SUBSTRING(text, 1,100) as text, score() as score "
                + "from topic where match('name^2,text', ?) "
                + "ORDER BY SCORE() DESC";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmnt = connection.prepareStatement(query);)
        {
            stmnt.setString(1, search);
            final ResultSet rs = stmnt.executeQuery();

            while (rs.next())
            {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String text = rs.getString("text");
                float score = rs.getFloat("score");

                jdbcData.add(new JdbcData(id, name, text, score));
            }
        }
        return jdbcData;
    }
}
