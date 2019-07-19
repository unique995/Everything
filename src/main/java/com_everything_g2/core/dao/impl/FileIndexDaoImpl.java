package com_everything_g2.core.dao.impl;

import com_everything_g2.core.dao.DataSourceFactory;
import com_everything_g2.core.dao.FileIndexDao;
import com_everything_g2.core.model.FileType;
import com_everything_g2.core.model.Thing;
import com_everything_g2.core.model.Condition;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
索引与检索对数据库的操作
 */

public class FileIndexDaoImpl implements FileIndexDao {
    //DriverManager.getConnection
    //DataSource.getConnection 通过数据源工厂获取DataSource实例化对象
    private final DataSource dataSource;
    public FileIndexDaoImpl(DataSource dataSource){
        this.dataSource=dataSource;
    }
    @Override
    public void insert(Thing thing) {
        //JDBC操作
        //连接
        Connection connection=null;
        //命令
        PreparedStatement statement=null;
        try {
            //获取数据库连接
            connection=this.dataSource.getConnection();
            //准备SQL语句
            String sql="insert into thing(name, path, depth, file_type) values(?,?,?,?)";
            //System.out.println(sql);
            //准备命令
            statement=connection.prepareStatement(sql);
            //设置参数
            //预编译命令中SQL的占位符(?)赋值
            statement.setString(1,thing.getName());
            statement.setString(2,thing.getPath());
            statement.setInt(3,thing.getDepth());
            statement.setString(4,thing.getFileType().name());
            //执行命令
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            releaseResource(null,statement,connection);
        }

    }
    @Override
    public void delete(Thing thing) {
        //thing->path=> D:\a\b\hello.java
        //like path%
        //= 最多删除一个，绝对匹配
        Connection connection=null;
        PreparedStatement statement=null;
        try {
            connection=this.dataSource.getConnection();
            String sql="delete from thing where path=?";
            statement=connection.prepareStatement(sql);
            //预编译命令中SQL的占位符赋值
            statement.setString(1,thing.getPath());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            releaseResource(null,statement,connection);
        }

    }
    @Override
    //查询
    public List<Thing> query(Condition condition) {
        List<Thing> things = new ArrayList <>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet=null;//结果集
        try {
            connection = this.dataSource.getConnection();
            StringBuilder sb=new StringBuilder();//不会被多线程共享，所以不必用stringBuffer，属性上要用stringBuffer
            //拼接SQL语句
            sb.append(" select name,path,depth,file_type from thing ");
            sb.append(" where ");
            //search<name> [file_type]
            //采用模糊匹配
            //前模糊
            //后模糊 √
            //前后模糊
            sb.append(" name like '%").append(condition.getName()).append("%' ");
            if(condition.getFileType()!=null){
                FileType fileType=FileType.lookupByName(condition.getFileType());
                sb.append(" and file_type='"+fileType.name()+"'");
            }
            sb.append(" order by depth ").append(condition.getOrderByDepthAsc()?"asc":"desc");
            sb.append(" limit ").append(condition.getLimit());
            //System.out.println(sb);
            //准备命令
            statement = connection.prepareStatement(sb.toString());
            //执行命令
            resultSet=statement.executeQuery();
            //处理结果
            while(resultSet.next()){
                //数据库中的行记录--->java中的对象
                Thing thing=new Thing();
                thing.setName(resultSet.getString("name"));
                thing.setPath(resultSet.getString("path"));
                thing.setDepth(resultSet.getInt("depth"));
                thing.setFileType(FileType.lookupByName(resultSet.getString("file_type")));
                things.add(thing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseResource(resultSet,statement,connection);
        }
        return things;
    }
    //重构，解决大量代码重复
    //在不改变程序的功能和业务的前提下，对代码进行优化，使得代码更易阅读和扩展
    private void releaseResource(ResultSet resultSet,PreparedStatement statement, Connection connection){
        if(resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        FileIndexDao fileIndexDao = new FileIndexDaoImpl(DataSourceFactory.getInstance());
//        Condition condition = new Condition();
//        condition.setName("简历.pdf");
//        List<Thing> things = fileIndexDao.query(condition);
//        for (Thing t :things) {
//            System.out.println(t);
//        }
//        Thing thing = new Thing();
//        thing.setPath("D:\\test\\简历.ppt");
//        thing.setDepth(2);
//        thing.setFileType(FileType.DOC);
//        thing.setName("简历.ppt");fileIndexDao.insert(thing);
    }
}
