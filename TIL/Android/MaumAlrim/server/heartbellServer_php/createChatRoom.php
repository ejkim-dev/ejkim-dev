<?php
include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속

  $sql = "CREATE TABLE chatRoom (";
  $sql .= "master_id bigint(20) unsigned not null auto_increment,";
  $sql .= "user_id varchar(255) not null,";
  $sql .= "room_id varchar(10) not null,";
  $sql .= "other_id varchar(255) not null,";
  $sql .= "chat_category  varchar(255) not null,";
  $sql .= "end_time  int(11) NOT NULL,";
  $sql .= "is_public enum('N', 'Y') default 'N' NOT NULL,";
  $sql .= "is_status enum('N', 'Y') default 'Y' NOT NULL,";
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
