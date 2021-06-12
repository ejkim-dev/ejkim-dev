<!-- 게시판은 글을 작성하는 페이지, 작성한 글을 저장하는 페이지,
글 목록을 표시하는 페이지, 내용을 보는 페이지,
다음 링크를 표시하는 페이지로 구성 -->

<?php
// board 테이블을 생성하는 예제
  include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속

    $sql = "CREATE TABLE board (";
    $sql .= "boardID int(10) unsigned NOT NULL auto_increment,";
    $sql .= "memberID int(10) unsigned NOT NULL,";
    $sql .= "title varchar(50) NOT NULL,";
    $sql .= "category varchar(50) NOT NULL,";
    $sql .= "content longtext NOT NULL,";
    $sql .= "regTime int(10) unsigned NOT NULL,";
    $sql .= "checked enum('o','p','x') default 'x' NOT NULL,";
    $sql .= "primary key (boardID)";
    $sql .= ") charset=utf8";
    // thumbnail 추가 : deault url: /blog/images/destination-9.jpg
    // thumbnail varchar(255) default '/blog/images/destination-9.jpg' NOT NULL
    // ALTER TABLE board ADD COLUMN thumbnail varchar(255) default '/blog/images/destination-9.jpg' NOT NULL;

    $res = $dbConnect->query($sql);

    if ( $res ) {
      echo "테이블 생성 완료";
    } else {
      echo "테이블 생성 실패";
    }

 ?>
