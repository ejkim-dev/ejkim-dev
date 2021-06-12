<?php
include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속

  $sql = "CREATE TABLE member (";
  $sql .= "master_id bigint(20) unsigned not null auto_increment,";
  $sql .= "user_nickname varchar(255) not null,";
  $sql .= "user_email varchar(255) default NULL,";
  $sql .= "user_pw varchar(255) default NULL,";
  $sql .= "birth_year  varchar(10) not null,";
  $sql .= "job  varchar(255) not null,";
  $sql .= "gender enum('M', 'F', 'X') default 'X' NOT NULL,";
  $sql .= "is_cert enum('N', 'Y') default 'N' NOT NULL,";
  $sql .= "is_status enum('N', 'Y') default 'Y' NOT NULL,";
  $sql .= "reg_time int(11) NOT NULL,";
  $sql .= "primary key (master_id)";
  $sql .= ") DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci";

  $stmt = $con->prepare($sql);
  $stmt->execute();

  if ( $stmt ) {
    echo "테이블 생성 완료";
  } else {
    echo "테이블 생성 실패";
  }
 ?>
