<?php
  // 세션을 사용하기 때문에 파일 include
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  //자동로그인 쿠키
  echo "<script>
        document.cookie = 'autologin=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
  </script>";
  unset($_SESSION['memberID']);
  unset($_SESSION['nickName']);
  // echo "로그아웃 되었습니다.";
  // echo '<br>';
  // echo "<a href='/index.php'>메인으로 이동</a>";
  echo "<script>
      window.alert('로그아웃 되었습니다.');
      window.history.back();
  </script>";
 ?>
