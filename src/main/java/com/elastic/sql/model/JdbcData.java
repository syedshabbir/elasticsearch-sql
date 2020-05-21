package com.elastic.sql.model;

public class JdbcData
{

    private String id;
    private String name;
    private String text;
    private float score;

    public JdbcData()
    {
    }

    public JdbcData(String id,
            String name,
            String text,
            float score)
    {
        this.id = id;
        this.name = name;
        this.text = text;
        this.score = score;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getText()
    {
        return text;
    }

    public float getScore()
    {
        return score;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setScore(float score)
    {
        this.score = score;
    }

}
