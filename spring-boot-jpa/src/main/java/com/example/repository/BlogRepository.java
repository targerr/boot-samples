package com.example.repository;

import com.example.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: wgs
 * @Date 2022/4/24 11:14
 * @Classname BlogRepository
 * @Description
 */
public interface BlogRepository extends JpaRepository<Blog, String> {

}
