<?php
include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속

  $sql = "CREATE TABLE diary (";
  $sql .= "master_id bigint(20) unsigned not null auto_increment,";
  $sql .= "user_id varchar(255) default null,";
  $sql .= "user_title text default null,";
  $sql .= "user_text longtext default null,";
  $sql .= "reg_time  varchar(255) default null,";
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
