package com.project.webtoonzoa.repository;

import com.project.webtoonzoa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
