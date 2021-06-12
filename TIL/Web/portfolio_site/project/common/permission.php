<?php
include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include

if (!isset($_SESSION['memberID'])) {//$_SESSION['memberID'] 로그인 성공 시 생성하는 세션
   echo "<script>window.alert('로그인 후 접근이 가능합니다.');
   location.replace('./loginForm.php');
   </script>";
  exit;
 // echo "세션값 없음";
}

// echo !isset($_SESSION['memberID']);
// echo " | ";
if ($_SESSION['isSuper'] == 'N') {//어드민 계정이 아닐 때
  // code...
  echo "<script>
      window.alert('어드민 계정만 접근이 가능합니다.');
      window.history.back();
  </script>";
  exit;
}
 ?>
