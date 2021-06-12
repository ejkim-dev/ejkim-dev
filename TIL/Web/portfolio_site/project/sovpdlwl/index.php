<?php
  // 메인 어드민 페이지
  // 로그인을 하지 않은 경우 회원가입고 로그인 링크가 표시되며, 로그인을 한 경우
  // 여러 프로젝트로 이동하는 링크를 표시함
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
 ?>

 <!DOCTYPE html>
 <html lang="en" dir="ltr">
   <head>
     <meta charset="utf-8">
     <title>EJKIM ADMIN</title>
   </head>
   <body>
     <?php
     if (!isset($_SESSION['memberID'])&&!isset($_COOKIE['autologin'])) {//$_SESSION['memberID'] 로그인 성공 시 생성하는 세션
       Header("Location:/signIn/signInForm.php");
       exit;
     } else {
      ?>
      <!-- 상단 메뉴바 -->
      <header><?php include $_SERVER['DOCUMENT_ROOT'].'/common/adminHeader.php'; ?></header>

      <?php
      }
       ?>


   </body>
 </html>
