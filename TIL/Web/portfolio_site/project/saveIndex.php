<?php
include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속

//데이터 가져오기
  $getName = $_POST['name'];
  $getEmail = $_POST['email'];
  $title = $_POST['title'];
  $message = $_POST['message'];

  if ($getName != null && $getName != '') {
    $getName = $dbConnect->real_escape_string($getName);
  } else {
    echo "이름을 입력하세요.";
    echo "<a href='/index.php#contact'>작성 페이지로 이동</a>";
    exit;
  }

  if ($getEmail != null && $getEmail != '') {
    $getEmail = $dbConnect->real_escape_string($getEmail);
  } else {
    echo "이메일을 입력하세요.";
    echo "<a href='/index.php#contact'>작성 페이지로 이동</a>";
    exit;
  }

  if ($title != null && $title != '') {
    $title = $dbConnect->real_escape_string($title);
  } else {
    echo "제목을 입력하세요.";
    echo "<a href='/index.php#contact'>작성 페이지로 이동</a>";
    exit;
  }

  if ($message != null && $message != '') {
    $message = $dbConnect->real_escape_string($message);
  } else {
    echo "내용을 입력하세요.";
    echo "<a href='/index.php#contact'>작성 페이지로 이동</a>";
    exit;
  }

  $regTime = time();

  $sql = "INSERT INTO touch (getName, getEmail, title, message, regTime)";
  $sql .= "VALUES ('{$getName}', '{$getEmail}', '{$title}', '{$message}', {$regTime})";
  $result = $dbConnect->query($sql);

  if ($result) {
    echo "<script>
        window.alert('저장 완료');
        location.href='/index.php#contact';
    </script>";
      // echo "저장 완료";
      // echo "<br>";
      // echo "<a href='/index.php#contact'>이전 페이지 이동</a>";
      exit;
  } else {
    echo "<script>
        window.alert('저장 실패 : 관리자에게 문의');
        window.history.back();
    </script>";
    // echo "저장 실패 : 관리자에게 문의";
    // echo "<br>";
    // echo "<a href='/index.php#contact'>이전 페이지 이동</a>";
    exit;
  }
?>
