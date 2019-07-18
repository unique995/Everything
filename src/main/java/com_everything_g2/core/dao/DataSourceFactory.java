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
    private static  volatile DruidDataSource  instance;//阿里巴巴提供的数据源，用单例获取数据源
    private  DataSourceFactory(){}
    //数据源
    public static DataSource getInstance(){
        if(instance==null){
            synchronized (DataSource.class){
                if(instance==null){
                    //new了一个对象
                    instance=new DruidDataSource();
                    //这是mysql的数据库配置
//                    instance.setUrl("jdbc:mysql://127.0.0.1:3306/everything_g2");
//                    instance.setUsername("root");
//                    instance.setPassword("123456");
//                    instance.setDriverClassName("com.mysql.jdbc.Driver");
                    //这是连接h2数据库的配置
                    instance.setTestWhileIdle(false);
                    //实例化数据库对象，设置驱动名称
                    instance.setDriverClassName("org.h2.Driver");

                    //获取当前项目的工程路径
                    String path=System.getProperty("user.dir")+ File.separator+"everything_g2";
                    //把数据存到本地工程文件里
                    instance.setUrl("jdbc:h2:"+path);

                    //数据库创建完成之后，初始化表结构
                   databaseInit(false);
                }
            }
        }
        return instance;
    }
    //初始化数据库
    public static void databaseInit(boolean buildIndex){
        //1.获取数据源
        DataSource dataSource = DataSourceFactory.getInstance();
        //classpath:database.sql->String
        StringBuilder sb=new StringBuilder();
        //2.获取SQL语句      getResourceAsStream:读取资源文件并转为stream流，下面方法使得变成字符串，获得SQL语句
        //try-with-resource方法
        try(InputStream in=DataSourceFactory.class.getClassLoader().getResourceAsStream("database1.sql");) {
            if (in != null) {
                try (
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
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
        //获取数据库连接和名称执行SQL
        String sql=sb.toString();
        try(Connection connection=getInstance().getConnection();){ //1.获取数据库连接
            if(buildIndex){
                //2.创建命令
                try(PreparedStatement statement=connection.prepareStatement("drop table if exists thing;");){//PreparedStatement对象用于执行SQL语句
                    //3.执行SQL语句
                    statement.executeUpdate();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


}