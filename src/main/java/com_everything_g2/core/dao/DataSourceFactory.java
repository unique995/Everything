package com_everything_g2.core.dao;

import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//数据库的数据源
public class DataSourceFactory {
    private static  volatile DruidDataSource  instance;
    private  DataSourceFactory(){

    }
    public static DataSource getInstance(){
        if(instance==null){
            synchronized (DataSource.class){
                if(instance==null){
                    instance=new DruidDataSource();
                    //这是mysql的数据库配置
//                    instance.setUrl("jdbc:mysql://127.0.0.1:3306/everything_g2");
//                    instance.setUsername("root");
//                    instance.setPassword("123456");
//                    instance.setDriverClassName("com.mysql.jdbc.Driver");
                    //这是连接h2数据库的配置
                    instance.setTestWhileIdle(false);
                    instance.setDriverClassName("org.h2.Driver");
                    String path=System.getProperty("user.dir")+ File.separator+"everything_g2";

                    instance.setUrl("jdbc:h2:"+path);
                    //数据库创建完成之后，初始化表结构
                   databaseInit(false);
                }
            }
        }
        return instance;
    }
    public static void databaseInit(boolean buildIndex){
        //classpath:database.sql->String
        StringBuilder sb=new StringBuilder();
        try(InputStream in=DataSourceFactory.class
                .getClassLoader().getResourceAsStream("database1.sql");
           ) {
            if (in != null) {
                try (
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in)
                        )) {
                         String line=null;
                         while((line=reader.readLine())!=null){
                             sb.append(line);
                         }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("database1.sql script can't load please check it.");
            }
            }catch (IOException e){
            e.printStackTrace();
        }
        String sql=sb.toString();
        try(Connection connection=getInstance().getConnection();
        ){
            if(buildIndex){
                try(PreparedStatement statement=connection
                        .prepareStatement("drop table if exists thing;");){
                    statement.executeUpdate();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            try(PreparedStatement statement=connection
                    .prepareStatement(sql);){
                statement.executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}