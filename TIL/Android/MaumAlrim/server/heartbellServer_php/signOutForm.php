<?php
// 세션을 사용하기 때문에
include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
//자동로그인 쿠키
// echo "<script>
//       document.cookie = 'autologin=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
// </script>";
unset($_SESSION['memberID']);
unset($_SESSION['nickName']);
unset($_SESSION['isSuper']);

echo "<script>
    window.alert('로그아웃 되었습니다.');
    location.replace('./loginForm.php');
</script>";
 ?>
