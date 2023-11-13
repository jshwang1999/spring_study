package com.example.jdbc_example.dao;

import com.example.jdbc_example.domain.TodoVO;
import lombok.Cleanup;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {
    public String getTime() {
        String now = null;

        try (Connection conn = ConnectionUtil.INSTANCE.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("select now()");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            now = resultSet.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    public String getTime2() throws Exception {
        @Cleanup Connection conn = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = conn.prepareStatement("select now()");
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        String now = resultSet.getString(1);

        return now;
    }

    // insert into values ... 문 수행
    public void insert(TodoVO vo) throws Exception {
        String sql = "insert into tbl_todo(title, dueDate, finished) values(?,?,?)";

        @Cleanup Connection conn = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = conn.prepareStatement(sql);

        preparedStatement.setString(1, vo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(vo.getDueDate()));
        preparedStatement.setBoolean(3, vo.isFinished());

        preparedStatement.executeUpdate();

    }

    // select * from "테이블" 을 수행하는 java 코드
    public List<TodoVO> selectAll() throws Exception {
        String sql = "select * from tbl_todo";

        @Cleanup Connection conn = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = conn.prepareStatement(sql);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        List<TodoVO> list = new ArrayList<>();

        while (resultSet.next()) {
            TodoVO vo = TodoVO.builder()
                    .tno(resultSet.getLong("tno"))
                    .title(resultSet.getString("title"))
                    .dueDate(resultSet.getDate("dueDate").toLocalDate())
                    .finished(resultSet.getBoolean("finished"))
                    .build();

            list.add(vo);
        }
    return list;
}

    // 튜플 1개 select를 수행하는 java 코드
    public TodoVO selectOne(Long tno) throws Exception{
    String sql = "select * from tbl_todo where tno = ?";

        @Cleanup Connection conn = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = conn.prepareStatement(sql);

        preparedStatement.setLong(1,tno);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        TodoVO vo = TodoVO.builder()
                .tno(resultSet.getLong("tno"))
                .title(resultSet.getString("title"))
                .dueDate(resultSet.getDate("dueDate").toLocalDate())
                .finished(resultSet.getBoolean("finished"))
                .build();

        return vo;
    }

    // delete from "테이블" where tno = ??? 를 수행하는 java 코드
    public void deleteOne(Long tno) throws Exception{
        String sql = "delete from tbl_todo where tno = ?";

        @Cleanup Connection conn = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = conn.prepareStatement(sql);

        preparedStatement.setLong(1,tno);
        preparedStatement.executeUpdate();
    }

    // update tbl_todo set title=?, duedate=?, ... 수행
    public void updateOne(TodoVO todoVO) throws Exception{
        String sql = "update tbl_todo set title=?, dueDate=?, finished=? where tno = ?";

        @Cleanup Connection conn = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = conn.prepareStatement(sql);

        preparedStatement.setString(1,todoVO.getTitle());
        preparedStatement.setDate(2,Date.valueOf(todoVO.getDueDate()));
        preparedStatement.setBoolean(3, todoVO.isFinished());
        preparedStatement.setLong(4, todoVO.getTno());

        preparedStatement.executeUpdate();




    }


}
