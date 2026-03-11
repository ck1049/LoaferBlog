package com.loafer.blog.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AddFavoriteCountColumn {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/loafer_blog";
        String user = "postgres";
        String password = "123456";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // 检查 favorite_count 列是否存在
            String checkColumnQuery = "SELECT column_name FROM information_schema.columns WHERE table_name = 'post' AND column_name = 'favorite_count'";
            var rs = stmt.executeQuery(checkColumnQuery);

            if (!rs.next()) {
                // 添加 favorite_count 列
                String addColumnQuery = "ALTER TABLE post ADD COLUMN favorite_count INTEGER DEFAULT 0";
                stmt.executeUpdate(addColumnQuery);
                System.out.println("添加 favorite_count 列成功");

                // 添加列注释
                String addCommentQuery = "COMMENT ON COLUMN post.favorite_count IS '收藏次数'";
                stmt.executeUpdate(addCommentQuery);
                System.out.println("添加 favorite_count 列注释成功");
            } else {
                System.out.println("favorite_count 列已存在");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}